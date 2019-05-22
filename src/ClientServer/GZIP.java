package ClientServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compression functions
 *
 * @author Matthew Poulter
 * @version 2019/05/22
 */
public class GZIP {
    /**
     * Compress an array using GZIP compression
     *
     * @param input The input array
     * @return The compressed array
     */
    public static String[] compress(String[] input) {
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null) {
                output[i] = input[i];
            } else {
                output[i] = compress(input[i]);
            }
        }
        return output;
    }

    /**
     * Compress a string with GZIP compression
     *
     * @param data The input string
     * @return The compressed output string
     */
    private static String compress(String data) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(data.length())) {
            GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream);
            gzipStream.write(data.getBytes());
            gzipStream.close();

            byte[] compressed = byteStream.toByteArray();
            return Base64.getEncoder().encodeToString(compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decompress an array using GZIP compression
     *
     * @param input The input array
     * @return The deompressed array
     */
    public static String[] decompress(String[] input) {
        return decompress(input, input.length);
    }

    /**
     * Decompress an array using GZIP compression with a maximum number of keys
     *
     * @param input The input array
     * @param max   The maximum number of keys
     * @return The decompressed array
     */
    static String[] decompress(String[] input, int max) {
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            if (i < max) {
                output[i] = decompress(input[i]);
            } else {
                output[i] = input[i];
            }
        }
        return output;
    }

    /**
     * Decompress a string with GZIP compression
     *
     * @param data The input string
     * @return The decompressed output string
     */
    private static String decompress(String data) {
        byte[] compressed = Base64.getDecoder().decode(data);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressed); GZIPInputStream gis = new GZIPInputStream(bis); BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
