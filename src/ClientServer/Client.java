package ClientServer;

import java.io.*;
import java.net.Socket;

public class Client {
    public static boolean exit = false;

    public static void main(String[] args) {
        System.out.print("Server: ");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); Socket chatSocket = new Socket(br.readLine(), 12345); BufferedWriter os = new BufferedWriter(new OutputStreamWriter(chatSocket.getOutputStream()))) {
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
        }
    }
}
