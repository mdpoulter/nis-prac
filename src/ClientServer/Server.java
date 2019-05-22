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
                String[] encrypted_message;
                while ((encrypted_message = (String[]) is.readObject()) != null) {
                    System.out.println("Message received: " + Arrays.toString(encrypted_message));

                    // Decrypt Secret Key
                    encrypted_message[2] = RSA.decrypt(encrypted_message[2], serverPrivatekey);
                    System.out.println("Decrypted key: " + encrypted_message[2]);

                    // Set the secret key and decrypt the message
                    AES aes = new AES();
                    aes.setKey(encrypted_message[2]);
                    encrypted_message = aes.decrypt(encrypted_message, 2);
                  
                    // Decompress
                    encrypted_message = GZIP.decompress(encrypted_message, 2);
                    System.out.println("Decompressed message: " + Arrays.toString(encrypted_message));
                    String message = encrypted_message[0];

                    // Decrypt hash
                    encrypted_message[1] = RSA.decrypt(encrypted_message[1], clientPublicKey);
                    System.out.println("Decrypted hash: " + encrypted_message[1]);

                    String hash = Hashing.hash(message);
                    System.out.println("Server calculated hash: " + hash);

                    // Check hash
                    if (encrypted_message[1].equalsIgnoreCase(hash)) {
                        System.out.println("Success! Message was successfully encrypted and decoded with confidentiality and authentication!");

                        System.out.println("Unencrypted message: " + message);

                        if (!(running = !message.equals("exit"))) {
                            break;
                        }
                    } else {
                        System.out.println("Failure in checking confidentiality and authentication!");
                    }

                    System.out.println();
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
