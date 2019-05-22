package ClientServerTests;

import ClientServer.AES;
import ClientServer.Client;
import ClientServer.GZIP;
import ClientServer.RSA;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Key;
import java.util.Arrays;

class ClientTestHelpers {
    static void smallWait() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    static class ClientThread implements Runnable {

        static void close() {
            Client.exit = true;
        }

        @Override
        public void run() {
            Client.exit = false;
            Client.main(null);
        }
    }

    static class ServerTestThread implements Runnable {
        private static ServerSocket serverSocket = null;
        private static Socket chatSocket = null;
        private static StringBuilder text = new StringBuilder();

        static void close() {
            text.setLength(0);
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }

        static boolean isConnected() {
            return chatSocket != null && chatSocket.isConnected();
        }

        static String getReceivedText() {
            return text.toString();
        }

        @Override
        public void run() {
            ObjectInputStream is = null;

            try {
                serverSocket = new ServerSocket(12345);

                while (true) {
                    chatSocket = serverSocket.accept();

                    Key serverPrivatekey = RSA.getKeyFromFile("server", "private");
                    Key clientPublicKey = RSA.getKeyFromFile("client", "public");

                    is = new ObjectInputStream(chatSocket.getInputStream());
                    String[] encrypted_message;
                    while ((encrypted_message = (String[]) is.readObject()) != null) {
                        text.append(Arrays.toString(encrypted_message)).append("\n");

                        encrypted_message[2] = RSA.decrypt(encrypted_message[2], serverPrivatekey);
                        text.append(encrypted_message[2]).append("\n");

                        AES aes = new AES();
                        aes.setKey(encrypted_message[2]);
                        encrypted_message = aes.decrypt(encrypted_message, 2);

                        encrypted_message = GZIP.decompress(encrypted_message, 2);
                        text.append(Arrays.toString(encrypted_message)).append("\n");
                        String message = encrypted_message[0];

                        encrypted_message[1] = RSA.decrypt(encrypted_message[1], clientPublicKey);
                        text.append(encrypted_message[1]).append("\n");

                        text.append(message).append("\n");
                    }
                }
            } catch (SocketException | EOFException ignored) {
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
}
