package ClientServerTests;

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

        client.start();
        smallWait();

        assertTrue(ServerTestThread.getReceivedText().contains(message));
    }

    @Test
    @DisplayName("Test basic encryption and decryption")
    void encrypt_the_decrypt(){

        Security.addProvider(new BouncyCastleProvider());
        SecureRandom secureRandom = new SecureRandom();

        byte[] keyBytes = new byte[16];
        secureRandom.nextBytes(keyBytes);

        String algorithm  = "AES";
        SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
        IvParameterSpec ivspec = new IvParameterSpec(keyBytes);
        String orl = "Hello World";
        String plainText = "";
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);//Sets up cipher to encrypt


            byte[] plainBytes = orl.getBytes();
            byte[] cipherText = cipher.doFinal(plainBytes);

            cipher.init(Cipher.DECRYPT_MODE, key, ivspec); //Sets up cipher to decrypt

            plainText = new String(cipher.doFinal(cipherText));
            System.out.println(plainText+" "+orl);
        }catch(Exception e){
            System.out.println("Failed to create Cipher: "+e.toString());
        }

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
        smallWait();

        assertTrue(ServerTestThread.getReceivedText().contains(message1));
        assertTrue(ServerTestThread.getReceivedText().contains(message2));
        assertTrue(ServerTestThread.getReceivedText().indexOf(message1) < ServerTestThread.getReceivedText().indexOf(message2));
    }
}
