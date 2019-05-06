package ClientServerTests;

import ClientServer.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

    @BeforeEach
    void redirectSystemOutStream() {
        originalSystemOut = System.out;

        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
    }

    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalSystemOut);
    }

    /**
     * Test: Server displays 'Started' when started
     */
    @Test
    @DisplayName("Server displays 'Started' when started")
    void server_displays_started_message() {
        Server.main(null);

        assertTrue(systemOutContent.toString().startsWith("Started"));
    }
}
