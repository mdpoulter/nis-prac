package ClientServer;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 */
public class RSA {
    /**
     * Generate new set of public and private keys
     *
     * @param args No expected arguments
     */
    public static void main(String[] args) {
        String[] set = {"server", "client"};
        for (String use : set) {
            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
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
            } catch (NoSuchAlgorithmException | IOException e) {
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
    Key getKeyFromFile(String use, String keyType) {
        Key key = null;

        try {
            File f = new File("keys/" + use + "-" + keyType + "key");
            FileInputStream fis = new FileInputStream(f);

            DataInputStream dis = new DataInputStream(fis);

            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();


            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            if (keyType.equals("private")) {
                key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
            } else {
                key = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
            }
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return key;
    }
}
