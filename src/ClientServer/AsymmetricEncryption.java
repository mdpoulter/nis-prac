package ClientServer;


import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

/**
 * AsymmetricEncryption
 *
 * @author Duncan Campbell
 * @version 1.0
 * @since 2019/05/20
 */
public class AsymmetricEncryption {


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    private final Cipher cipher;
    private final KeyPair pair;

    public AsymmetricEncryption() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(1024);
        pair = keyGen.generateKeyPair();
        this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
    }

    public String decrypt(String base64, Key privates) throws Exception{
         cipher.init(Cipher.DECRYPT_MODE, privates);
        return new String(cipher.doFinal(Base64.getDecoder().decode(base64)));
    }

    public String encrypt(String message, Key publics) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, publics);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
    }

    public KeyPair getKeyPair()throws Exception{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        return pair;
    }


}
