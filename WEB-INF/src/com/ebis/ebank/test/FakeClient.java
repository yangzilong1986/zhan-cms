package com.ebis.ebank.test;
import java.net.*;
import java.io.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FakeClient {
    public static void main(String[] args) {
        Socket s = null;
        try {
            s = new Socket("192.9.100.249", 8090);
            OutputStream out = s.getOutputStream();
            InputStream in=s.getInputStream();
            byte bt[]=new byte[50];
            out.write(bt);
            out.write('\r');
            out.write('\n');
            out.flush();
            out.close();
//            in.read(bt);
//            System.out.println(new String(bt));
            s.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}