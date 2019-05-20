package ClientServer;

import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
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

            //Create public and private key pair
            KeyPair keyPair = AsymmetricEncryption.getKeyPair();

            //Receive server public key
            Key serverPublicKey = AsymmetricEncryption.recieveKey(chatSocket);

            //Send Client Public Key
            AsymmetricEncryption.sendKey(chatSocket, keyPair);


            String line;
            while (!exit && (line = br.readLine()) != null) {
                System.out.print("> ");

                // TODO: Encode

                os.write(line + "\n");
                os.flush();
                if (line.equals("exit")) {
                    break;
                }
            }
        } catch (IOException ignored) {
        }catch(Exception e){

        }
    }
}
