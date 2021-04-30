package net.kunmc.lab.iboost.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IkisugiUtil {
    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

    public static String getPercentageBar(float par, int cont) {
        StringBuilder sb = new StringBuilder();
        int parCont = (int) ((float) cont * par);
        for (int i = 0; i < cont; i++) {
            if (parCont > i)
                sb.append("■");
            else
                sb.append("□");
        }

        return sb.toString();
    }

    public static byte[] inputStreamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = stream.read(buffer);
            if (len < 0) {
                break;
            }
            bout.write(buffer, 0, len);
        }
        return bout.toByteArray();
    }
}
