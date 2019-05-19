package ClientServer;

import java.security.Security;
import java.security.SecureRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.util.Base64;

/*
 * Contains functions to perform symmetric encryption and decryption using of the BouncyCastle encryption module.
 *
 * @authur Jarryd Dunn
 * @version 1.0
 * @since 2019/05/19
 */

public class SymmetricEncryptor{
    private Cipher cipher;
    private SecureRandom secureRandom;
    private SecretKeySpec key;
    private IvParameterSpec ivspec;
    private String keyAlgorithm;
    private String encryptionAlgorithm;

    /*
     *Constructor to generate Symmetric Encryption object.
     * @param encryptionAlg Name of the encryption algorithm to be used by the cipher.
     * @param keyAlg Name of the algorithm used to generate keys.
     */
    public SymmetricEncryptor(String encryptionAlg, String keyAlg){
        Security.addProvider(new BouncyCastleProvider());
        this.keyAlgorithm = keyAlg;
        this.encryptionAlgorithm = encryptionAlg;
        this.secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[16];
        this.secureRandom.nextBytes(keyBytes);
        ivspec = new IvParameterSpec(keyBytes);
        key = new SecretKeySpec(keyBytes, this.keyAlgorithm);
    }

    /*
     * Sets the values of the key
     * @param keyBytes The new key to be used.
     */
    public void setKey(byte[] keyBytes){
        this.ivspec = new IvParameterSpec(keyBytes);
        this.key = new SecretKeySpec(keyBytes,this.keyAlgorithm);
    }

    /*
     * Updates the encryption key to the given key.
     * @param keyStr String representation of new key to be used.
     */
    public void setKey(String keyStr){
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        setKey(keyBytes);
    }

    /*
     * Returns the key.
     * @return String This returns the key currently being used by the cipher.
     */
    public String getKey(){
        return Base64.getEncoder().encodeToString(this.key.getEncoded());
    }

    /*
     * Encrypt a string
     * @param plainText This is the string to be encrypted.
     * @return String Returns the encrypted string.
     */
    public String encrypt(String plainText){
        try{
            cipher = Cipher.getInstance(this.encryptionAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, this.key, this.ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /*
     * Decrypt a string
     * @param cipherText This is the string to be decrypted.
     * @return String This is the decrypted string.
     */
    public String decrypt(String cipherText){
        try{
            cipher = Cipher.getInstance(this.encryptionAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, this.key, this.ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
