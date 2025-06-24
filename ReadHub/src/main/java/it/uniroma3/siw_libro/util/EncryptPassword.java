package it.uniroma3.siw_libro.util;

import org.jasypt.util.text.AES256TextEncryptor;

public class EncryptPassword {
    public static void main(String[] args) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword("TechStoreEncKey123!"); // La tua chiave segreta
        String encryptedPassword = encryptor.encrypt("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");
        System.out.println("Password cifrata: ENC(" + encryptedPassword + ")");
    }
}
