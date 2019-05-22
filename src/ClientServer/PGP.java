package ClientServer;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * PGP functions
 *
 * @author Claire Denny
 * @version 2019/05/22
 */
public class PGP {

    /**
     * Hashes an input string to a 128 bit hex string i.e. calculates SHA-512 bit hash.
     *
     * @param message plaintext string
     */
    public static String hashing(String message) {
        String hashed = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update(message.getBytes(StandardCharsets.UTF_8));
            hashed = String.format("%040x", new BigInteger(1, md.digest()));
        } catch (Exception e) {
            System.err.println("Hash Exception: " + e.getMessage());
        }

        return hashed;
    }//end hashing
}//end class
