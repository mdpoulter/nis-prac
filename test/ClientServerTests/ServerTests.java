package ClientServerTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The server application tests
 *
 * @author Matthew Poulter
 * @version 2019/05/06
 */
class ServerTests {
    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;
    private Thread t;

    @BeforeEach
    void redirectSystemOutStream() {
        originalSystemOut = System.out;

        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));

        t = new Thread(new ServerTestHelpers.ServerThread());
        t.start();
        ServerTestHelpers.smallWait();
    }

    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalSystemOut);
        // System.out.println(systemOutContent.toString());
    }

    @Test
    @DisplayName("Server displays 'Server started' when started")
    void server_displays_started_message() {
        assertTrue(systemOutContent.toString().startsWith("Server started"));
    }

    @Test
    @DisplayName("Server listens on port 12345")
    void server_listens_on_correct_port() {
        assertTrue(ServerTestHelpers.serverListeningOn("localhost", 12345));
    }

    @Test
    @DisplayName("Server exits when 'exit' message sent")
    void server_exits_on_command() {
        try (Socket chatSocket = new Socket("localhost", 12345); BufferedWriter os = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()))) {
            os.write("exit\n");
            os.flush();
        } catch (IOException ignored) {
        }

        ServerTestHelpers.smallWait();
        assertFalse(ServerTestHelpers.serverListeningOn("localhost", 12345));
    }

    @ParameterizedTest
    @DisplayName("Server receives and outputs message as is")
    @ValueSource(strings = {"Lorem ipsum", "More text...", "CAPITALS", "lowercase", "12345", "!£$%^&*()"})
    void server_receives_message_and_outputs_it(String message) {
        try (Socket chatSocket = new Socket("localhost", 12345); BufferedWriter os = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()))) {
            os.write(message + "\n");
            os.write("exit\n");
            os.flush();
        } catch (IOException ignored) {
        }

        while (t.isAlive()) {
            assert true;
        }

        assertTrue(systemOutContent.toString().contains("Message received: " + message));
    }

    @ParameterizedTest
    @DisplayName("Server receives and outputs multiple messages as is and in order")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void server_receives_multiple_messages_and_outputs_them_in_order(String message1, String message2) {
        try (Socket chatSocket = new Socket("localhost", 12345); BufferedWriter os = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()))) {
            os.write(message1 + "\n");
            os.write(message2 + "\n");
            os.write("exit\n");
            os.flush();
        } catch (IOException ignored) {
        }

        while (t.isAlive()) {
            assert true;
        }

        assertTrue(systemOutContent.toString().contains("Message received: " + message1));
        assertTrue(systemOutContent.toString().contains("Message received: " + message2));
        assertTrue(systemOutContent.toString().indexOf("Message received: " + message1) < systemOutContent.toString().indexOf("Message received: " + message2));
    }


}
