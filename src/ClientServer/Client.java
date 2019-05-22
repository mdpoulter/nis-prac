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
                // Hash message
                String hash = Hashing.hash(line);
                System.out.println("Hashed: " + hash);

                // Encrypt hash
                hash = RSA.encrypt(hash, clientPrivatekey);
                System.out.println("Encrypted hash: " + hash);

                String[] encrypted_message = {line, hash, null};

                // Compress
                encrypted_message = GZIP.compress(encrypted_message);
                System.out.println("Compresses message: " + Arrays.toString(encrypted_message));

                // Generates a new once-off session key
                AES aes = new AES();
                aes.newKey();

                // Encrypt message with symmetric encryption
                encrypted_message = aes.encrypt(encrypted_message);
                System.out.println("Encrypted message: " + Arrays.toString(encrypted_message));

                // Get Secret Key
                String key = aes.getKey();
                System.out.println("Secret key: " + key);

                // Encrypt Secret Key
                String keyEncrypted = RSA.encrypt(key, serverPublicKey);
                System.out.println("Encrypted SecretKey: " + keyEncrypted);

                // Prepare final message
                encrypted_message[2] = keyEncrypted;
                System.out.println("Final encrypted message: " + Arrays.toString(encrypted_message));

                os.writeObject(encrypted_message);
                os.flush();
                if (line.equals("exit")) {
                    break;
                }

                System.out.println();
                System.out.print("> ");
            }
        } catch (IOException ignored) {
        }
    }
}
