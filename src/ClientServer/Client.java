package ClientServer;

import java.io.*;
import java.net.Socket;
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); Socket chatSocket = new Socket(br.readLine(), 12345); BufferedWriter os = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()))) {
            String line;
            while (!exit && (line = br.readLine()) != null) {
                System.out.print("> ");

                String[] encrypted_message = {null, null, null};

                // Generates a new once-off session key
                AES aes = new AES();
                aes.newKey();

                // Encrypt message with symmetric encryption
                encrypted_message = aes.encrypt(encrypted_message);
                System.out.println("Encrypted message: " + Arrays.toString(encrypted_message));

                // Get Secret Key
                String key = aes.getKey();
                System.out.println("Secret key: " + key);

                os.write(line + "\n");
                os.flush();
                if (line.equals("exit")) {
                    break;
                }
            }
        } catch (IOException ignored) {
        }
    }
}
