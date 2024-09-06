package com.example.bfandemo.userData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {

    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest object with SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the digest with the password bytes
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
            return null;
        }
    }
}
