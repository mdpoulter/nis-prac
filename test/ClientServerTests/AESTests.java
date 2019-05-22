package ClientServerTests;

import ClientServer.AES;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AESTests {
    @Test
    @DisplayName("Test basic encryption and decryption")
    void encrypt_the_decrypt() {
        String orl = "Hello World";
        AES encryptor = new AES();
        String plainText = encryptor.decrypt(encryptor.encrypt(orl));
        assertEquals(plainText, orl);
    }

    @Test
    @DisplayName("Decrypt cipher with given key")
    void decrypt_cipher_with_key() {
        AES encryptor = new AES();
        AES decryptor = new AES();
        String orl = "Hello World";
        String cipherText = encryptor.encrypt(orl);
        String key = encryptor.getKey();
        decryptor.setKey(key);
        String plainText = decryptor.decrypt(cipherText);
        assertEquals(plainText, orl);
    }
}
