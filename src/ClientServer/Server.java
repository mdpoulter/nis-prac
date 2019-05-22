package ClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Key;
import java.util.Arrays;

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
                String[] message;
                while ((message = (String[]) is.readObject()) != null) {
                    System.out.println("Message received: " + Arrays.toString(message));

                    String decryptedKey = RSA.decrypt(message[2], serverPrivatekey);
                    System.out.println("Decrypted key: " + decryptedKey);

                    // TODO: Symmetric

                    // TODO: Compression with output:
                    String hash = "NKaTh4IYGBJJ+ooMF76j8Rxz3RE14kj4C3xR/H8lBt/P5JL0shEHGcjDpcH5iNnI+Hiqs9Z5fB9Swg6Z0ZPvYqigQukSGXMT/K7KdHIUmpMIskGOyNWxIoIGa3BQ8D5nhxi7V7S0csV9zrhYlfhSGmP7RqA1Uk6UnySDyZfvQPg=";

                    String decryptedHash = RSA.decrypt(hash, clientPublicKey);
                    System.out.println("Decrypted hash: " + decryptedHash);

                    // TODO: Decode with output:
                    String original_message = "";

                    if (!(running = !original_message.equals("exit"))) {
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
