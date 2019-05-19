package ClientServerTests;

import ClientServer.SymmetricEncryptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static ClientServerTests.ClientTestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Security;
import java.security.SecureRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.*;

/**
 * The client application tests
 *
 * @author Matthew Poulter
 * @version 2019/05/08
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ClientTests {
    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;
    private InputStream originalSystemIn;
    private Thread client;

    @BeforeEach
    void prepareTest() {
        originalSystemOut = System.out;
        originalSystemIn = System.in;

        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));

        client = new Thread(new ClientThread());
        Thread server = new Thread(new ServerTestThread());
        server.start();
        smallWait();
    }

    private void sendInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @AfterEach
    void finishTest() {
        ClientThread.close();
        ServerTestThread.close();

        System.setOut(originalSystemOut);
        System.setIn(originalSystemIn);
    }

    @Test
    @DisplayName("Client asks for server when started and connects correctly")
    void client_connects_to_inputted_server() {
        sendInput("localhost\n");

        client.start();
        smallWait();

        assertTrue(systemOutContent.toString().startsWith("Server: "));
        smallWait();
        assertTrue(ServerTestThread.isConnected());
    }

    @Test
    @DisplayName("Client closes on 'exit' comment")
    void client_closes_on_exit_command() {
        sendInput("localhost\n" +
            "exit\n");

        client.start();
        smallWait();

        assertFalse(client.isAlive());
    }

    @ParameterizedTest
    @DisplayName("Client sends exact message to server")
    @ValueSource(strings = {"Lorem ipsum", "More text...", "CAPITALS", "lowercase", "12345", "!£$%^&*()"})
    void client_sends_message_to_server(String message) {
        sendInput("localhost\n" +
            message + "\n");
        SymmetricEncryptor decryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
        client.start();
        smallWait();
        String rcvMsg = ServerTestThread.getReceivedText();
        String[] msg = rcvMsg.split("<key>");
        decryptor.setKey(msg[1]);
        rcvMsg = decryptor.decrypt(msg[0]);
        assertTrue(rcvMsg.contains(message));
    }

    @Test
    @DisplayName("Test basic encryption and decryption")
    void encrypt_the_decrypt(){
        String orl = "Hello World";
        SymmetricEncryptor encryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
        String plainText = encryptor.decrypt(encryptor.encrypt(orl));
        assertEquals(plainText,orl);
    }

    @Test
    @DisplayName("Decrypt cipher with given key")
    void decrypt_cipher_with_key(){
        SymmetricEncryptor encryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
        SymmetricEncryptor decryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
        String orl = "Hello World";
        String cipherText = encryptor.encrypt(orl);
        String key = encryptor.getKey();
        decryptor.setKey(key);
        String plainText = decryptor.decrypt(cipherText);
        assertEquals(plainText,orl);
    }

    @ParameterizedTest
    @DisplayName("Client sends messages in correct order to server")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void client_sends_message_in_order_to_server(String message1, String message2) {
        sendInput("localhost\n" +
            message1 + "\n" +
            message2 + "\n");
        SymmetricEncryptor decryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
        client.start();
        smallWait();

        assertTrue(ServerTestThread.getReceivedText().contains(message1));
        assertTrue(ServerTestThread.getReceivedText().contains(message2));
        assertTrue(ServerTestThread.getReceivedText().indexOf(message1) < ServerTestThread.getReceivedText().indexOf(message2));
    }
}
