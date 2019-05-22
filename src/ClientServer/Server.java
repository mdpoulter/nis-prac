package ClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * The server application
 *
 * @author Matthew Poulter
 * @version 2019/05/06
 */
public class Server {

    public static ServerSocket serverSocket;

    /**
     * Starting up the server.
     *
     * @param args The arguments
     */
    public static void main(String[] args) {
        serverSocket = null;
        Socket chatSocket = null;
        ObjectInputStream is = null;

        try {
            serverSocket = new ServerSocket(12345);

            System.out.println("Server started");
            System.out.println("Waiting for messages...");

            boolean running = true;
            while (running) {
                chatSocket = serverSocket.accept();

                is = new ObjectInputStream(chatSocket.getInputStream());
                String[] encrypted_message;
                while ((encrypted_message = (String[]) is.readObject()) != null) {

                    // Set the secret key and decrypt the message
                    AES aes = new AES();
                    aes.setKey(encrypted_message[2]);
                    encrypted_message = aes.decrypt(encrypted_message, 2);

                }
            }
        } catch (SocketException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (chatSocket != null) {
                    chatSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
