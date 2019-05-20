package ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyPair;

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
        BufferedReader is = null;


        try {
            serverSocket = new ServerSocket(12345);

            System.out.println("Server started");
            System.out.println("Waiting for messages...");

            boolean running = true;
            while (running) {
                chatSocket = serverSocket.accept();

                //Create public and private key pair
                KeyPair serverKeyPair = AsymmetricEncryption.getKeyPair();

                //Send Server Public Key
                AsymmetricEncryption.sendKey(chatSocket, serverKeyPair);

                //Receive Client Private Key
                Key clientPublicKey = AsymmetricEncryption.recieveKey(chatSocket);



                is = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
                String message;
                while ((message = is.readLine()) != null) {
                    // TODO: Decode

                    //String decryptedKey = AsymmetricEncryption.decrypt(key, serverKeyPair.getPrivate());

                    //String decryptedHash = AsymmetricEncryption.decrypt(hash, clientPublicKey());

                    System.out.println("Message received: " + message);
                    running = !message.equals("exit");
                }
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
