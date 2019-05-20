package ClientServer;


import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
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

    public static String decrypt(String base64, Key privates) throws Exception{
        Cipher cipher =  Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privates);
        return new String(cipher.doFinal(Base64.getDecoder().decode(base64)));
    }

    public static String encrypt(String message, Key publics) throws Exception{
        Cipher cipher =  Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publics);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
    }

    public static KeyPair getKeyPair()throws Exception{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        return pair;
    }

    public static Key recieveKey(Socket chatSocket) throws Exception{
        byte[] lenb = new byte[4];
        chatSocket.getInputStream().read(lenb,0,4);
        ByteBuffer bb = ByteBuffer.wrap(lenb);
        int len = bb.getInt();
        byte[] servPubKeyBytes = new byte[len];
        chatSocket.getInputStream().read(servPubKeyBytes);
        X509EncodedKeySpec ks = new X509EncodedKeySpec(servPubKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
        Key serverPubKey = kf.generatePublic(ks);
        return serverPubKey;
    }
    public static void sendKey(Socket chatSocket, KeyPair keyPair) throws Exception{
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(keyPair.getPublic().getEncoded().length);
        chatSocket.getOutputStream().write(bb.array());
        chatSocket.getOutputStream().write(keyPair.getPublic().getEncoded());
        chatSocket.getOutputStream().flush();
    }


}
