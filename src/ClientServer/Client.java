package ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.util.Arrays;

/**
 * The client application
 *
 * @author Matthew Poulter
 * @version 2019/05/06
 */
public class Client {
    /**
     * Force exit flag
     */
    public static boolean exit = false;

    /**
     * Starting up the client.
     *
     * @param args The arguments
     */
    public static void main(String[] args) {
        System.out.print("Server: ");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); Socket chatSocket = new Socket(br.readLine(), 12345); ObjectOutputStream os = new ObjectOutputStream(chatSocket.getOutputStream())) {
            Key clientPrivatekey = RSA.getKeyFromFile("client", "private");
            Key serverPublicKey = RSA.getKeyFromFile("server", "public");

            String line;
            System.out.print("> ");
            while (!exit && (line = br.readLine()) != null) {

                // TODO: Encode
                String hash = "INSERT HASH HERE";

                String hashEncrypted = RSA.encrypt(hash, clientPrivatekey);
                System.out.println("Encrypted hash: " + hashEncrypted);

                String[] encrypted_message = {line, hashEncrypted, null};

                // TODO: Compress

                // TODO: Symmetric
                String key = "INSERT SECRET KEY HERE";

                String keyEncrypted = RSA.encrypt(key, serverPublicKey);
                System.out.println("Encrypted SecretKey: " + keyEncrypted);

                encrypted_message[2] = keyEncrypted;
                System.out.println("Final encrypted message: " + Arrays.toString(encrypted_message));

                os.writeObject(encrypted_message);
                os.flush();
                if (line.equals("exit")) {
                    break;
                }

                System.out.print("> ");
            }
        } catch (IOException ignored) {
        }
    }
}
