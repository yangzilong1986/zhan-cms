package com.ebis.ebank.dx;

import java.io.*;
import java.util.*;

import com.ebis.encrypt.*;

/**
 * ±£¥Ê°¢∂¡»°BMK
 *
 * @author unascribed
 * @version 1.0
 */
public class BMKStore {
    public static final String BMK_FILE = "bmkkey.txt";
    private String first32;
    private String last32;

    public void setFirst32(String first32) {
        this.first32 = first32;
    }
    public void setLast32(String last32) {
        this.last32  = last32;
    }

    public BMKStore() {
    }

    public boolean store() {
        if ( first32.length() != 32 && last32.length() != 32 )
            return false;

        try {
            FileOutputStream out = new FileOutputStream(this.BMK_FILE);
            char[] fc32 = first32.toCharArray();
            char[] lc32 = last32.toCharArray();
            int length  = fc32.length;
            char[] bmkc = new char[length];
            for ( int i = 0 ; i < length ; i++ ) {
                bmkc[i] = (char)((fc32[i] ^ lc32[i]) + 48);
            }
            String bmk = new String(bmkc);
            EncryptData encrypt = new EncryptData();
            byte[] result = encrypt.enBmkKey(bmk.getBytes());
            out.write(result);
            out.flush();
            out.close();
            return true;
        } catch ( Exception ioe ) {
            return false;
        }
    }
    public static boolean store(String first32,String last32) {
        if ( first32.length() != 32 && last32.length() != 32 )
            return false;

        try {
            FileOutputStream out = new FileOutputStream(BMK_FILE);

            char[] fc32 = first32.toCharArray();
            char[] lc32 = last32.toCharArray();
            int length  = fc32.length;
            char[] bmkc = new char[length];
            for ( int i = 0 ; i < length ; i++ ) {
                bmkc[i] = (char)((fc32[i] ^ lc32[i])+48);
            }
            String bmk = new String(bmkc);
            EncryptData encrypt = new EncryptData();
            byte[] result = encrypt.enBmkKey(bmk.getBytes());
            out.write(result);
            out.flush();
            out.close();
            return true;
        } catch ( Exception ioe ) {
            return false;
        }
    }

    public static byte[] read(boolean flag) {
        String bmk = null;
        try {
            FileInputStream in = new FileInputStream(BMK_FILE);
            byte[] bmkbyte  = new byte[32];
            in.read(bmkbyte);
            in.close();
            EncryptData encrypt = new EncryptData();
            byte[] result = encrypt.deBmkKey(bmkbyte);
            System.out.println(new String(result));
            return result;
        } catch ( IOException ioe ) {
            return null;
        }
    }

    public static byte[] read() {
        String bmk = null;
        try {
            FileInputStream in = new FileInputStream(BMK_FILE);
            byte[] bmkbyte  = new byte[32];
            in.read(bmkbyte);
            in.close();
            return bmkbyte;
        } catch ( IOException ioe ) {
            return null;
        }
    }

    public static void main(String[] args) {
        BMKStore store = new BMKStore();
        store.read(true);
        store.setFirst32("00000000000000000000000000000000");
        store.setLast32("00000000000000000000000000000000");
        store.store();
        byte[] r = store.read();
        for ( int i = 0 ; i < r.length ; i++)
        System.out.print(r[i]+"=");
        System.out.println();
    }
}