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

    public AsymmetricEncryption() throws Exception {
    }

    public String decrypt(String base64, Key privates) {
        return null;
    }

    public String encrypt(String message, Key publics) {
        return null;
    }

    public KeyPair getKeyPair(){
        return null;
    }


}
