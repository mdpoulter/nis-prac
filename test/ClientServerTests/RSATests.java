package ClientServerTests;

import ClientServer.RSA;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RSATests {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    @DisplayName("RSA encryption is correct")
    void EncryptionDecryptionTest() throws Exception {
        Key privateKey = RSA.getKeyFromFile("server", "private");
        Key publicKey = RSA.getKeyFromFile("server", "public");

        String toBeEncrypted = "Hello";
        String encrypted = RSA.encrypt(toBeEncrypted, publicKey);
        Cipher cipher = Cipher.getInstance("RSA", "BC");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String correctEncryption = Base64.getEncoder().encodeToString(cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8)));

        assertEquals(correctEncryption, encrypted);

        String decrypted = RSA.decrypt(encrypted, privateKey);
        String correctDecryption = RSA.decrypt(correctEncryption, privateKey);
        assertEquals(correctDecryption, decrypted);
    }

    @Test
    @DisplayName("RSA decryption is correct")
    void DecryptionTest() throws Exception {
        Key privateKey = RSA.getKeyFromFile("server", "private");
        Key publicKey = RSA.getKeyFromFile("server", "public");

        String encrypted = RSA.encrypt("Hello", publicKey);
        String decrypted = RSA.decrypt(encrypted, privateKey);
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String correctDecryption = null;
        if (encrypted != null) {
            correctDecryption = new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        }
        assertEquals(decrypted, correctDecryption);
    }

    @Test
    @DisplayName("Public encryption and private decryption work correctly together")
    void EnncryptionDecryptionTest() throws Exception {
        Key privateKey = RSA.getKeyFromFile("server", "private");
        Key publicKey = RSA.getKeyFromFile("server", "public");

        String encrypted = RSA.encrypt("Hello", publicKey);
        String decrypted = RSA.decrypt(encrypted, privateKey);
        assertEquals(decrypted, "Hello");
    }

    @Test
    @DisplayName("Private encryption and public decryption work correctly together")
    void EnncryptionDecryptionTest2() throws Exception {
        Key privateKey = RSA.getKeyFromFile("server", "private");
        Key publicKey = RSA.getKeyFromFile("server", "public");

        String encrypted = RSA.encrypt("Hello", privateKey);
        String decrypted = RSA.decrypt(encrypted, publicKey);
        assertEquals(decrypted, "Hello");
    }
}
