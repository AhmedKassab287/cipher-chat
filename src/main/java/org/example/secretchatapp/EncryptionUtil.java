package org.example.secretchatapp;

import java.util.Base64;

public class EncryptionUtil {

    public static String encrypt(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }

    public static String decrypt(String encrypted) {
        return new String(Base64.getDecoder().decode(encrypted));
    }
}
