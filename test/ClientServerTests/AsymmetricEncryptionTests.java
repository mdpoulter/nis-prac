// TODO: Rewrite tests

//package ClientServerTests;
//
//import ClientServer.AsymmetricEncryption;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import javax.crypto.Cipher;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.Security;
//import java.util.Base64;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
///**
// * AsymmetricEncryptionTests
// *
// * @author Duncan Campbell
// * @version 1.0
// * @since 2019/05/20
// */
//
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//class AsymmetricEncryptionTests {
//
//    static {
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    @Test
//    @DisplayName("Testing Correct Encryption")
//    void EncryptionDecryptionTest() throws Exception{
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
//        keyGen.initialize(1024);
//        KeyPair pair = keyGen.generateKeyPair();
//        String toBeEncrypted = "Hello";
//        String encrypted = AsymmetricEncryption.encrypt(toBeEncrypted, pair.getPublic());
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
//
//        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
//        String correctEncryption =  Base64.getEncoder().encodeToString(cipher.doFinal(toBeEncrypted.getBytes("UTF-8")));
//
//        //assertEquals(correctEncryption, encrypted);
//
//        String decrypted = AsymmetricEncryption.decrypt(encrypted, pair.getPrivate());
//        String correctDencryption = AsymmetricEncryption.decrypt(correctEncryption, pair.getPrivate());
//        assertEquals(correctDencryption, decrypted);
//    }
//
//    @Test
//    @DisplayName("Testing Decryption")
//    void DecryptionTest() throws Exception{
//
//        KeyPair pair = AsymmetricEncryption.getKeyPair();
//        String encrypted = AsymmetricEncryption.encrypt("Hello", pair.getPublic());
//        String decrypted = AsymmetricEncryption.decrypt(encrypted, pair.getPrivate());
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
//        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
//        String correctDecryption = new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
//        assertEquals(decrypted, correctDecryption);
//    }
//
//    @Test
//    @DisplayName("Testing Encryption and Decryption together. Public encryption and private Decryption")
//    void EnncryptionDecryptionTest() throws Exception{
//        KeyPair pair = AsymmetricEncryption.getKeyPair();
//        String encrypted = AsymmetricEncryption.encrypt("Hello", pair.getPublic());
//        String decrypted = AsymmetricEncryption.decrypt(encrypted, pair.getPrivate());
//        assertEquals(decrypted,"Hello");
//    }
//
//    @Test
//    @DisplayName("Testing Encryption and Decryption together. Private encryption and public Decryption")
//    void EnncryptionDecryptionTest2() throws Exception{
//        KeyPair pair = AsymmetricEncryption.getKeyPair();
//        String encrypted = AsymmetricEncryption.encrypt("Hello", pair.getPrivate());
//        String decrypted = AsymmetricEncryption.decrypt(encrypted, pair.getPublic());
//        assertEquals(decrypted,"Hello");
//    }
//
//
//}
