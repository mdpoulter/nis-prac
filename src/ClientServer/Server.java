package ClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Key;

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

                Key serverPrivatekey = RSA.getKeyFromFile("server", "private");
                Key clientPublicKey = RSA.getKeyFromFile("client", "public");

                is = new ObjectInputStream(chatSocket.getInputStream());
                String message;
                while ((message = (String) is.readObject()) != null) {
                    System.out.println("Message received: " + message);

                    String key = "INSERT KEY HERE";

                    String decryptedKey = RSA.decrypt(key, serverPrivatekey);

                    // TODO: Symmetric

                    // TODO: Compression
                    String hash = "INSERT HASH HERE";
                    String decryptedHash = RSA.decrypt(hash, clientPublicKey);

                    // TODO: Decode

                    running = !message.equals("exit");
                    if (!running) {
                        break;
                    }
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
