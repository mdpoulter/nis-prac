package ClientServerTests;

import ClientServer.Server;

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

    static void waitSecond() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
        }
    }

    static class ServerThread implements Runnable {

        @Override
        public void run() {
            Server.main(null);
        }
    }
}
