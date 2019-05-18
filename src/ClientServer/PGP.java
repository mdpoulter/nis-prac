package ClientServer;

import java.security.MessageDigest;
import java.math.BigInteger;


public class PGP {

    /**
     * Hashes an input string to a 128 bit hex string i.e. calculates SHA-512 bit hash.
     *
     * By Claire Denny
     *
     * @param message plaintext string
     */
    public static String hashing(String message) {
        String hashed="";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update(message.getBytes("utf8"));
            byte[] digest = md.digest();
            hashed = String.format("%040x", new BigInteger(1, digest));
        }
        catch(Exception e) {
            System.out.println("Hash Exception");
        }
        System.out.println(hashed);
        return hashed;
    }//end hashing


}//end class

