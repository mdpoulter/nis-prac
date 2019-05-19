package ClientServer;

import java.security.Security;
import java.security.SecureRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.util.Base64;

public class SymmetricEncryptor{
    private Cipher cipher;
    private SecureRandom secureRandom;
    private SecretKeySpec key;
    private IvParameterSpec ivspec;
    private String keyAlgorithm;
    private String encryptionAlgorithm;

    /*
     *Constructor to generate Symmetric Encryption object.
     */
    public SymmetricEncryptor(String encryptionAlg, String keyAlg, byte[] keyBytes){
        Security.addProvider(new BouncyCastleProvider());
        this.keyAlgorithm = keyAlg;
        this.encryptionAlgorithm = encryptionAlg;
        //ivspec = new IvParameterSpec(keyBytes);
        key = new SecretKeySpec(keyBytes, keyAlg);
    }

    /*
     *Constructor to generate Symmetric Encryption object, using an exising key.
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
     */
    public void setKey(byte[] keyBytes){
        this.ivspec = new IvParameterSpec(keyBytes);
        this.key = new SecretKeySpec(keyBytes,this.keyAlgorithm);
    }

    public void setKey(String keyStr){
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        setKey(keyBytes);
    }

    public String getKey(){
        return Base64.getEncoder().encodeToString(this.key.getEncoded());
    }

    /*
     * Encrypt a string
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
