package net.kunmc.lab.iboost.util;

public class IkisugiUtil {
    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

    public static String getPercentegeBar(float par, int cont) {
        StringBuffer sb = new StringBuffer();
        int parCont = (int) ((float) cont * par);
        for (int i = 0; i < cont; i++) {
            if (parCont > i)
                sb.append("■");
            else
                sb.append("□");
        }

        return sb.toString();
    }
}
