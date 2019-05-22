package ClientServerTests;

import ClientServer.AES;
import ClientServer.Hashing;
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
        AES decryptor = new AES("AES/CBC/PKCS5Padding", "AES");
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
        AES encryptor = new AES("AES/CBC/PKCS5Padding", "AES");
        String plainText = encryptor.decrypt(encryptor.encrypt(orl));
        assertEquals(plainText,orl);
    }

    @Test
    @DisplayName("Decrypt cipher with given key")
    void decrypt_cipher_with_key(){
        AES encryptor = new AES("AES/CBC/PKCS5Padding", "AES");
        AES decryptor = new AES("AES/CBC/PKCS5Padding", "AES");
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

        client.start();
        for (int i=0;i<10;++i) {
            smallWait();
        }
        AES decryptor = new AES("AES/CBC/PKCS5Padding", "AES");

        String rcv = ServerTestThread.getReceivedText();
        StringBuilder plainBuild = new StringBuilder();
        String[] msgs = rcv.split("<end>");
        for (String msg : msgs) {
            String[] msgKey = msg.split("<key>"); //Split into key and message
            //key must be decrypted using servers private key
            decryptor.setKey(msgKey[1]);// set the session key
            plainBuild.append(decryptor.decrypt(msgKey[0])); //decrypt message
        }
        String plain = plainBuild.toString();
        assertTrue(plain.contains(message1));
        assertTrue(plain.contains(message2));
        assertTrue(plain.indexOf(message1) < plain.indexOf(message2));
    }

    @ParameterizedTest
    @DisplayName("Client prints out hash of message")
    @ValueSource(strings = {"Lorem ipsum", "More text...", "CAPITALS", "lowercase", "12345", "!£$%^&*()"})
    void client_prints_out_hash_of_message(String message) {
        String hash = Hashing.hash(message);

        sendInput("localhost\n" +
            message + "\n");

        client.start();
        smallWait();

        assertTrue(systemOutContent.toString().contains("Hashed: " + hash));
    }
}
