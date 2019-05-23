package ClientServerTests;

import ClientServer.*;

import java.io.IOException;
import java.net.Socket;

class ServerTestHelpers {
    static boolean serverListeningOn(String host, int port) {
        Socket socket = null;

        try {
            socket = new Socket(host, port);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (socket != null)
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
        }
    }

    static void smallWait() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ignored) {
        }
    }

    static String[] prepareMessage(String message) {
        String hash = Hashing.hash(message);
        hash = RSA.encrypt(hash, RSA.getKeyFromFile("client", "private"));
        String[] encrypted_message = {message, hash, null};
        encrypted_message = GZIP.compress(encrypted_message);
        AES aes = new AES();
        aes.newKey();
        encrypted_message = aes.encrypt(encrypted_message);
        String key = aes.getKey();
        String keyEncrypted = RSA.encrypt(key, RSA.getKeyFromFile("server", "public"));
        encrypted_message[2] = keyEncrypted;

        return encrypted_message;
    }

    static class ServerThread implements Runnable {

        @Override
        public void run() {
            Server.main(null);
        }
    }

    static void closeServer() {
        try {
            Server.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
