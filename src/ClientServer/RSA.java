package ClientServer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * AsymmetricEncryption
 *
 * @author Duncan Campbell
 * @version 2019/05/22
 */
public class RSA {
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Generate new set of public and private keys
     *
     * @param args No expected arguments
     */
    public static void main(String[] args) {
        init();

        String[] set = {"server", "client"};
        for (String use : set) {
            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
                keyGen.initialize(1024);
                KeyPair pair = keyGen.generateKeyPair();

                byte[] publicKeyBytes = pair.getPublic().getEncoded();
                byte[] privateKeyBytes = pair.getPrivate().getEncoded();

                FileOutputStream fos = new FileOutputStream("keys/" + use + "-publickey");
                fos.write(publicKeyBytes);
                fos.close();

                fos = new FileOutputStream("keys/" + use + "-privatekey");
                fos.write(privateKeyBytes);
                fos.close();
            } catch (NoSuchAlgorithmException | IOException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the desired key from its storage file
     *
     * @param use     The key use: "server" or "client"
     * @param keyType The key type: "publickey" or "privatekey"
     * @return The key from the desired file
     */
    public static Key getKeyFromFile(String use, String keyType) {
        init();

        Key key = null;

        try {
            File f = new File("keys/" + use + "-" + keyType + "key");
            FileInputStream fis = new FileInputStream(f);

            DataInputStream dis = new DataInputStream(fis);

            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();


            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

            if (keyType.equals("private")) {
                key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
            } else {
                key = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
            }
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        return key;
    }

    /**
     * Encrypt a message input with the desired key
     *
     * @param message The message to be encrypted
     * @param key     The key to encrypt with
     * @return The encrypted string
     */
    public static String encrypt(String message, Key key) {
        init();

        try {
            Cipher cipher = Cipher.getInstance("RSA", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypt a message input with the desired key
     *
     * @param message The message to be decrypted
     * @param key     The key to decrypt with
     * @return The decrypted string
     */
    public static String decrypt(String message, Key key) {
        init();

        try {
            Cipher cipher = Cipher.getInstance("RSA", "BC");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(message)));
        } catch (IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
