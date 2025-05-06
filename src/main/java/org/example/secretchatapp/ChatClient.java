package org.example.secretchatapp;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessages(SecretChatApp app) {
        Thread thread = new Thread(() -> {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    String finalMsg = msg;
                    // تمرير الرسالة إلى الواجهة
                    javafx.application.Platform.runLater(() -> app.receiveMessage(finalMsg));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void close() throws IOException {
        socket.close();
    }
}
