package ClientServerTests;

import ClientServer.PGP;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static ClientServerTests.ClientTestHelpers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        //System.out.println(systemOutContent.toString());
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

    @Test
    @DisplayName("Hashing function hashes message")
    void hashing_function_hashes_message(){ //move this to separate testing script?
        String message1 = "Hash this";
        String hashed_message1 = PGP.hashing(message1);
        String message2 = "Hash that";
        String hashed_message2 = PGP.hashing(message2);
        String message3 = "Hash this";
        String hashed_message3 = PGP.hashing(message3);

        System.out.println(message1+" \thashes to\t "+hashed_message1);
        System.out.println(message2+" \thashes to\t "+hashed_message2);
        System.out.println(message3+" \thashes to\t "+hashed_message3);

        assertFalse(message1.equalsIgnoreCase(hashed_message1)); //test hash different to input
        assertFalse(message2.equalsIgnoreCase(hashed_message2)); //test next hash different to next input
        assertFalse(hashed_message1.equals(hashed_message2)); //test hashing different inputs yields different hashes
        assertTrue(hashed_message1.equals(hashed_message3)); //test hashing same input yields same hash
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
