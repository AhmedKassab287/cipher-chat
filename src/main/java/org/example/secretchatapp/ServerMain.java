package org.example.secretchatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerMain {
    private static final int PORT = 5000;
    static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("🚀 Secret Chat Server is running on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // انتظار اتصال من عميل جديد
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔌 New client connected: " + clientSocket);

                // إنشاء ClientHandler له وتشغيله في Thread خاص
                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    // إرسال الرسالة إلى جميع العملاء المتصلين
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
