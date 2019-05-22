package ClientServerTests;

import ClientServer.PGP;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HashingTests {
    @ParameterizedTest
    @DisplayName("Hash is different to input")
    @ValueSource(strings = {"Lorem ipsum", "More text...", "CAPITALS", "lowercase", "12345", "!£$%^&*()"})
    void returned_hash_is_different_to_input(String message) {
        String hash = PGP.hashing(message);
        assertFalse(message.equalsIgnoreCase(hash));
    }

    @ParameterizedTest
    @DisplayName("Hash is the same for the same input")
    @ValueSource(strings = {"Lorem ipsum", "More text...", "CAPITALS", "lowercase", "12345", "!£$%^&*()"})
    void returned_hash_is_the_same_for_the_same_input(String message) {
        String hash1 = PGP.hashing(message);
        String hash2 = PGP.hashing(message);
        assertEquals(hash1, hash2);
    }

    @ParameterizedTest
    @DisplayName("Hash is different for a different input")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void returned_hash_is_different_for_a_different_input(String message1, String message2) {
        String hash1 = PGP.hashing(message1);
        String hash2 = PGP.hashing(message2);
        assertNotEquals(hash1, hash2);
    }
}
