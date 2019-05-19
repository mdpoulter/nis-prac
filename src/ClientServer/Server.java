package ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        BufferedReader is = null;

        try {
            serverSocket = new ServerSocket(12345);
            SymmetricEncryptor decryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
            System.out.println("Server started");
            System.out.println("Waiting for messages...");

            boolean running = true;
            while (running) {
                chatSocket = serverSocket.accept();

                is = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

                String message;
                while ((message = is.readLine()) != null) {
                    String[] msg = message.split("<key>");
                    decryptor.setKey(msg[1]);
                    message = decryptor.decrypt(msg[0]);
                    System.out.println("Message received: " + message);

                    running = !message.equals("exit");

                    // TODO: Decode
                }
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
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
