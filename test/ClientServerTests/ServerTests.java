package ClientServerTests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import static ClientServerTests.ServerTestHelpers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The server application tests
 *
 * @author Matthew Poulter
 * @version 2019/05/06
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ServerTests {
    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;
    private Thread t;

    @BeforeEach
    void redirectSystemOutStream() {
        originalSystemOut = System.out;

        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));

        t = new Thread(new ServerThread());
        t.start();
        smallWait();
    }

    @AfterEach
    void restoreSystemOutStream() {
        closeServer();
        System.setOut(originalSystemOut);
    }

    @Test
    @DisplayName("Server displays 'Server started' when started")
    void server_displays_started_message() {
        assertTrue(systemOutContent.toString().startsWith("Server started"));
    }

    @Test
    @DisplayName("Server listens on port 12345")
    void server_listens_on_correct_port() {
        assertTrue(serverListeningOn("localhost", 12345));
    }

    @Test
    @DisplayName("Server exits when 'exit' message sent")
    void server_exits_on_command() {
        try (Socket chatSocket = new Socket("localhost", 12345); ObjectOutputStream os = new ObjectOutputStream(chatSocket.getOutputStream())) {
            os.writeObject(ServerTestHelpers.prepareMessage("exit"));
            os.flush();
        } catch (IOException ignored) {
        }

        smallWait();
        assertFalse(serverListeningOn("localhost", 12345));
    }

    @ParameterizedTest
    @DisplayName("Server receives and outputs message as is")
    @ValueSource(strings = {"Lorem ipsum", "More text...", "CAPITALS", "lowercase", "12345", "!£$%^&*()"})
    void server_receives_message_and_outputs_it(String message) {
        try (Socket chatSocket = new Socket("localhost", 12345); ObjectOutputStream os = new ObjectOutputStream(chatSocket.getOutputStream())) {
            os.writeObject(ServerTestHelpers.prepareMessage(message));
            os.writeObject(ServerTestHelpers.prepareMessage("exit"));
            os.flush();
        } catch (IOException ignored) {
        }

        while (t.isAlive()) {
            assert true;
        }

        assertTrue(systemOutContent.toString().contains(message));
    }

    @ParameterizedTest
    @DisplayName("Server receives and outputs multiple messages as is and in order")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void server_receives_multiple_messages_and_outputs_them_in_order(String message1, String message2) {
        try (Socket chatSocket = new Socket("localhost", 12345); ObjectOutputStream os = new ObjectOutputStream(chatSocket.getOutputStream())) {
            os.writeObject(ServerTestHelpers.prepareMessage(message1));
            os.writeObject(ServerTestHelpers.prepareMessage(message2));
            os.writeObject(ServerTestHelpers.prepareMessage("exit"));
            os.flush();
        } catch (IOException ignored) {
        }

        while (t.isAlive()) {
            assert true;
        }

        assertTrue(systemOutContent.toString().contains(message1));
        assertTrue(systemOutContent.toString().contains(message2));
        assertTrue(systemOutContent.toString().indexOf(message1) < systemOutContent.toString().indexOf(message2));
    }


}
