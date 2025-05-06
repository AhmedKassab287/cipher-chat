package org.example.secretchatapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SecretChatApp extends Application {

    private TextArea chatArea;
    private TextField messageField;
    private ListView<String> userList;
    private ChatClient client;
    private final String username = "Me";

    @Override
    public void start(Stage primaryStage) {
        // العنوان
        Label title = new Label("🔒 Secret Chat");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // قائمة المستخدمين
        userList = new ListView<>();
        userList.setPrefSize(150, 400);
        userList.getItems().add(username);

        VBox userBox = new VBox(10, new Label("Active Users:"), userList);

        // منطقة الدردشة
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(400);

        // حقل الرسالة وزر الإرسال
        messageField = new TextField();
        messageField.setPromptText("Type your message...");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        HBox messageBox = new HBox(10, messageField, sendButton);
        messageBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        // أزرار إضافية
        Button decryptButton = new Button("🔓 Decrypt Message");
        decryptButton.setOnAction(e -> decryptMessage());

        Button fileButton = new Button("📁 Send File");
        fileButton.setOnAction(e -> sendFile());

        HBox actionsBox = new HBox(10, decryptButton, fileButton);

        VBox chatBox = new VBox(10, new Label("Chat Area:"), chatArea, messageBox, actionsBox);
        chatBox.setVgrow(chatArea, Priority.ALWAYS);

        // الحاوية الرئيسية
        HBox mainBox = new HBox(15, userBox, chatBox);
        VBox root = new VBox(10, title, mainBox);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f8f8f8;");

        // المشهد
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Secret Chat");
        primaryStage.setScene(scene);
        primaryStage.show();

        // الاتصال بالسيرفر
        try {
            client = new ChatClient("127.0.0.1", 5000); // عدل IP حسب الحاجة
            client.receiveMessages(this);
        } catch (Exception ex) {
            chatArea.appendText("❌ Failed to connect to server.\n");
        }
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            String encrypted = EncryptionUtil.encrypt(msg);
            client.sendMessage(encrypted);
            chatArea.appendText(username + " (Encrypted): " + encrypted + "\n");
            messageField.clear();
        }
    }

    private void decryptMessage() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Decrypt Message");
        dialog.setHeaderText("Enter the encrypted message:");

        dialog.showAndWait().ifPresent(encrypted -> {
            try {
                String decrypted = EncryptionUtil.decrypt(encrypted);
                chatArea.appendText("🔓 Decrypted: " + decrypted + "\n");
            } catch (Exception e) {
                chatArea.appendText("⚠️ Invalid encrypted text.\n");
            }
        });
    }

    private void sendFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to send");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            chatArea.appendText("📁 Selected file: " + file.getName() + "\n");
            // TODO: send file bytes to server later
        }
    }

    public void receiveMessage(String msg) {
        chatArea.appendText("Friend: " + msg + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
