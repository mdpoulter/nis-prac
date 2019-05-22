package ClientServer;

import java.io.*;
import java.net.Socket;

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
        SymmetricEncryptor encryptor = new SymmetricEncryptor("AES/CBC/PKCS5Padding","AES");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); Socket chatSocket = new Socket(br.readLine(), 12345); BufferedWriter os = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()))) {
            String line;
            while (!exit && (line = br.readLine()) != null) {
                System.out.print("> ");

                // TODO: Encode
                encryptor.newKey(); //Generates a new once-off session key
                String cipherText = encryptor.encrypt(line+"\n"); //Encrypt message
                String key = encryptor.getKey(); //This must be encrypted using the public key of the receiver
                cipherText+="<key>"+key+"<key>"; //Add key to message

                os.write(cipherText+"<end>");
                os.flush();
                if (line.equals("exit")) {
                    break;
                }
            }
        } catch (IOException ignored) {
        }
    }
}
