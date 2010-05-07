package com.ebis.ebank.dx;

import java.io.*;

public class KEYStore {
    public static final String MAC_KEY_FILE = "mackey.txt";
    public static final String PIN_KEY_FILE = "pinkey.txt";
    public static boolean store(String filename,byte[] key) {
        try {
            if ( key.length != 16 )
                return false;
            FileOutputStream out = new FileOutputStream(filename);
            out.write(key);
            out.flush();
            out.close();
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }
    public static byte[] get(String filename) {
        String bmk = null;
        try {
            FileInputStream in = new FileInputStream(filename);
            byte[] keybyte  = new byte[16];
            in.read(keybyte);
            in.close();
            return keybyte;
        } catch ( IOException ioe ) {
            return null;
        }
    }
}