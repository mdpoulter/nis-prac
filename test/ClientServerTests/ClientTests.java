package ClientServerTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        client = new Thread(new ClientTestHelpers.ClientThread());
        Thread server = new Thread(new ClientTestHelpers.ServerTestThread());
        server.start();
        ClientTestHelpers.smallWait();
    }

    private void sendInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @AfterEach
    void finishTest() {
        ClientTestHelpers.ClientThread.close();
        ClientTestHelpers.ServerTestThread.close();

        System.setOut(originalSystemOut);
        System.setIn(originalSystemIn);
        System.out.println(systemOutContent);
    }

    @Test
    @DisplayName("Client asks for server when started and connects correctly")
    void client_connects_to_inputted_server() {
        sendInput("localhost\n");

        client.start();
        ClientTestHelpers.smallWait();

        assertTrue(systemOutContent.toString().startsWith("Server: "));
        ClientTestHelpers.smallWait();
        assertTrue(ClientTestHelpers.ServerTestThread.isConnected());
    }

    @Test
    @DisplayName("Client closes on 'exit' comment")
    void client_closes_on_exit_command() {
        sendInput("localhost\n" +
            "exit\n");

        client.start();
        ClientTestHelpers.smallWait();

        assertFalse(client.isAlive());
    }

    @Test
    @DisplayName("Client sends exact message to server")
    void client_sends_message_to_server() {
        sendInput("localhost\n" +
            "message\n");

        client.start();
        ClientTestHelpers.smallWait();

        assertTrue(ClientTestHelpers.ServerTestThread.getReceivedText().contains("message"));
    }

    @Test
    @DisplayName("Client sends messages in correct order to server")
    void client_sends_message_in_order_to_server() {
        sendInput("localhost\n" +
            "message1\n" +
            "message2\n");

        client.start();
        ClientTestHelpers.smallWait();

        assertTrue(ClientTestHelpers.ServerTestThread.getReceivedText().contains("message1"));
        assertTrue(ClientTestHelpers.ServerTestThread.getReceivedText().contains("message2"));
        assertTrue(ClientTestHelpers.ServerTestThread.getReceivedText().indexOf("message1") < ClientTestHelpers.ServerTestThread.getReceivedText().indexOf("message2"));
    }
}
