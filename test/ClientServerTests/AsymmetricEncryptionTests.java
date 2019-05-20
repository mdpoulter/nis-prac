package ClientServerTests;

import ClientServer.AsymmetricEncryption;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AsymmetricEncryptionTests {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    @DisplayName("Testing Correct Encryption")
    void EncryptionDecryptionTest() throws Exception{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        String encrypted = new AsymmetricEncryption().encrypt("Hello", pair.getPublic());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
        String correctEncryption = Base64.getEncoder().encodeToString(cipher.doFinal("Hello".getBytes("UTF-8")));
        assertTrue(correctEncryption.equals(encrypted));
    }

    @Test
    @DisplayName("Testing Decryption")
    void DecryptionTest() throws Exception{

        KeyPair pair = new AsymmetricEncryption().getKeyPair();
        String encrypted = new AsymmetricEncryption().encrypt("Hello", pair.getPublic());
        String decrypted = new AsymmetricEncryption().decrypt(encrypted, pair.getPrivate());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
        String correctDecryption = new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        assertTrue(decrypted.equals(correctDecryption));
    }

    @Test
    @DisplayName("Testing Encryption and Decryption together")
    void EnncryptionDecryptionTest() throws Exception{
        KeyPair pair = new AsymmetricEncryption().getKeyPair();
        String encrypted = new AsymmetricEncryption().encrypt("Hello", pair.getPublic());
        String decrypted = new AsymmetricEncryption().decrypt(encrypted, pair.getPrivate());
        assertTrue(decrypted.equals("Hello"));
    }


}
