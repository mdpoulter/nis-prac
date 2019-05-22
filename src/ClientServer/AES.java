package ClientServer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

/**
 * Contains functions to perform symmetric encryption and decryption using of the BouncyCastle encryption module.
 *
 * @author Jarryd Dunn
 * @version 2019/05/22
 */

public class AES {
    private Cipher cipher;
    private SecureRandom secureRandom;
    private SecretKeySpec key;
    private IvParameterSpec ivspec;
    private String keyAlgorithm;
    private String encryptionAlgorithm;

    /**
     * Constructor to generate Symmetric Encryption object.
     */
    public AES() {
        Security.addProvider(new BouncyCastleProvider());

        this.keyAlgorithm = "AES";
        this.encryptionAlgorithm = "AES/CBC/PKCS5Padding";

        this.secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[16];
        this.secureRandom.nextBytes(keyBytes);

        this.ivspec = new IvParameterSpec(keyBytes);
        this.key = new SecretKeySpec(keyBytes, this.keyAlgorithm);
    }

    /**
     * Generates a new key to be used for encryption or decryption
     */
    void newKey() {
        byte[] keyBytes = new byte[16];
        this.secureRandom.nextBytes(keyBytes);
        this.ivspec = new IvParameterSpec(keyBytes);
        this.key = new SecretKeySpec(keyBytes, this.keyAlgorithm);
    }

    /**
     * Sets the values of the key
     *
     * @param keyBytes The new key to be used.
     */
    public void setKey(byte[] keyBytes) {
        this.ivspec = new IvParameterSpec(keyBytes);
        this.key = new SecretKeySpec(keyBytes, this.keyAlgorithm);
    }

    /**
     * Updates the encryption key to the given key.
     *
     * @param keyStr String representation of new key to be used.
     */
    public void setKey(String keyStr) {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        setKey(keyBytes);
    }

    /**
     * Returns the key.
     *
     * @return String This returns the key currently being used by the cipher.
     */
    public String getKey() {
        return Base64.getEncoder().encodeToString(this.key.getEncoded());
    }


    /**
     * Encrypt a string array
     *
     * @param input String array
     * @return Encrypted string array
     */
    public String[] encrypt(String[] input) {
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null) {
                output[i] = input[i];
            } else {
                output[i] = encrypt(input[i]);
            }
        }
        return output;
    }

    /**
     * Encrypt a string
     *
     * @param plainText This is the string to be encrypted.
     * @return String Returns the encrypted string.
     */
    public String encrypt(String plainText) {
        try {
            cipher = Cipher.getInstance(this.encryptionAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, this.key, this.ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Decrypts a string array
     *
     * @param input String array
     * @return Decrypted string array
     */
    public String[] decrypt(String[] input, int max) {
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            if (i < max) {
                output[i] = decrypt(input[i]);
            } else {
                output[i] = input[i];
            }
        }
        return output;
    }

    /**
     * Decrypt a string
     *
     * @param cipherText This is the string to be decrypted.
     * @return String This is the decrypted string.
     */
    public String decrypt(String cipherText) {
        try {
            cipher = Cipher.getInstance(this.encryptionAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, this.key, this.ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
