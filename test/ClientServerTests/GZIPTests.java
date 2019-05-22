package ClientServerTests;

import ClientServer.GZIP;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GZIPTests {
    @ParameterizedTest
    @DisplayName("Compressed output is different to input")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void compressed_output_is_different_to_input(String message1, String message2) {
        String[] compressed = GZIP.compress(new String[]{message1, message2});
        assertFalse(message1.equalsIgnoreCase(compressed[0]));
        assertFalse(message2.equalsIgnoreCase(compressed[1]));
    }

    @ParameterizedTest
    @DisplayName("Compressed output the same for the same input")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void compressed_output_is_the_same_for_the_same_input(String message1, String message2) {
        String[] compressed1 = GZIP.compress(new String[]{message1, message2});
        String[] compressed2 = GZIP.compress(new String[]{message1, message2});
        assertEquals(compressed1[0], compressed2[0]);
        assertEquals(compressed1[1], compressed2[1]);
    }

    @ParameterizedTest
    @DisplayName("Compressed output is different for a different input")
    @CsvSource({"Lorem ipsum,More text...", "CAPITALS,lowercase", "12345,!£$%^&*()"})
    void compressed_output_is_different_for_a_different_input(String message1, String message2) {
        String[] compressed1 = GZIP.compress(new String[]{message1, message1});
        String[] compressed2 = GZIP.compress(new String[]{message2, message2});
        assertNotEquals(compressed1[0], compressed2[0]);
        assertNotEquals(compressed1[1], compressed2[1]);
    }
}
