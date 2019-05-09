package ClientServerTests;

import ClientServer.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class ClientTestHelpers {
    static void smallWait() {
        try {
            Thread.sleep(200);
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
            BufferedReader is = null;

            try {
                serverSocket = new ServerSocket(12345);

                while (true) {
                    chatSocket = serverSocket.accept();
                    is = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

                    String message;
                    while ((message = is.readLine()) != null) {
                        text.append(message);
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
}
