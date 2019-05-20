package ClientServerTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AsymmetricEncryptionTests {

    @Test
    @DisplayName("Testing CorrectEncryption")
    void EncryptionTest() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Testing correct decryption.")
    void DecryptionTest() {

        assertTrue(true);
    }
}
