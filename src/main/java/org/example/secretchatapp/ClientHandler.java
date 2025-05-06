package org.example.secretchatapp;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                System.out.println("📩 Received: " + msg);
                ServerMain.broadcast(msg, this); // إعادة الإرسال لكل العملاء
            }
        } catch (IOException e) {
            System.out.println("⚠️ Client disconnected: " + socket);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // تجاهل
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.err.println("❌ Failed to send message to client");
        }
    }
}
