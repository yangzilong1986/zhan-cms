package zt.platform.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p/>
 * <p/>
 * Title: </p> <p>
 * <p/>
 * Description: </p> <p>
 * <p/>
 * Copyright: Copyright (c) 2003</p> <p>
 * <p/>
 * Company: </p>
 *
 * @author not attributable
 * @version 1.0
 * @created 2003Äê11ÔÂ9ÈÕ
 */

public class KeyManager {
    static byte[] macKey;
    static byte[] bmkKey;

    static {
        bmkKey = pullKey("bmkkey.txt");
        macKey = pullKey("mackey.txt");
    }

    /**
     * Description of the Method
     *
     * @param fileName Description of the Parameter
     * @return Description of the Return Value
     */
    private static byte[] pullKey(String fileName) {
        InputStream in = null;
        byte[] result = new byte[0];
        try {
            in = new FileInputStream(fileName);
            byte[] temp = new byte[80];
            int length = in.read(temp);
            if (length != -1) {
                result = new byte[length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = temp[i];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Gets the macKey attribute of the KeyManager class
     *
     * @return The macKey value
     */
    public static byte[] getMacKey() {
        //return macKey;
        return "69C887130AA5C26A".getBytes();
    }

    /**
     * Gets the bmkKey attribute of the KeyManager class
     *
     * @return The bmkKey value
     */
    public static byte[] getBmkKey() {
        //return bmkKey;
        byte[] bmk = {
                (byte) 0xF9, 0x78, 0x11, 0x39, 0x40, (byte) 0xEE, (byte) 0x9A, (byte) 0xF7, (byte) 0xF9, 0x78, 0x11, 0x39, 0x40, (byte) 0xEE, (byte) 0x9A, (byte) 0xF7, (byte) 0xF9,
                0x78, 0x11, 0x39, 0x40, (byte) 0xEE,
                (byte) 0x9A, (byte) 0xF7, (byte) 0xF9, 0x78, 0x11, 0x39, 0x40, (byte) 0xEE, (byte) 0x9A, (byte) 0xF7};
        return bmk;
    }

}
