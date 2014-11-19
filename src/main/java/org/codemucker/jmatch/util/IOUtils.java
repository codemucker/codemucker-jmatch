package org.codemucker.jmatch.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static String readToString(InputStream is, String encoding) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, encoding);
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int readLength;
        while ((readLength = isr.read(buf)) != -1) {
            sb.append(buf, 0, readLength);
        }
        if (is.markSupported()) {
            is.reset();
        }
        return sb.toString();
    }

    public static byte[] readToBytes(InputStream is) throws IOException {
        return readToByteArrayOutputStream(is).toByteArray();
    }

    public static ByteArrayOutputStream readToByteArrayOutputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int readLength;
        while ((readLength = is.read(buf)) != -1) {
            os.write(buf, 0, readLength);
        }
        if (is.markSupported()) {
            is.reset();
        }
        return os;
    }
}
