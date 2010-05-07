package com.ebis.ebank.dx;

import java.io.*;

public class BTPTxn {

    public static byte[] makeBTPCont(BTPResource res,String date,String txnseq) {

        if ( date.length() != 8 )
            date = "        ";
        if ( txnseq.length() != 6 )
            txnseq = "000001";

        return makeTiaHead("0002",res,date,txnseq,0);

    }
    public static byte[] makeBTPOver(BTPResource res,String date,String txnseq) {

        if ( date.length() != 8 )
            date = "        ";
        if ( txnseq.length() != 6 )
            txnseq = "000001";

        return makeTiaHead("0001",res,date,txnseq,0);

    }

    public static byte[] makeTiaHead(String txncode,BTPResource res,String date,String txnseq,int len) {
        /*
        (2)长度+(4)CICS+(4)交易号+(8)日期+(6)流水号+(12)网点号+(4)终端号+(4)柜员号+(10)其他(0x00)
        */
        if ( len < 0 ) len = 0;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            //LU号
            bout.write(0x03);
            bout.write(0x00);

            //len=54
            bout.write(52+len);
            bout.write(0);
            //CICSCODE(4)
            bout.write("CICS".getBytes());
            //TXNCODE(4)
            bout.write(txncode.getBytes());
            //TXNDATE (8)
            if ( date == null || date.length() != 8 ) {
                writeBytes(bout,8);
            } else
               bout.write(date.getBytes());
            //流水号(6)
            bout.write(txnseq.getBytes());
            //网点号（12）
            bout.write(res.getBrhid().getBytes());
            //终端号（4)
            bout.write(res.getWsid().getBytes());
            //柜员号（4）
            bout.write(res.getTellid().getBytes());
            //other （10）
            writeBytes(bout,10);

            bout.flush();
            byte[] head = bout.toByteArray();
            bout.close();
            return head;
        } catch ( Exception e ) {

        }
        return null;
    }
    public static byte[] combine(byte[] head,byte[] body) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(head);
            out.write(body);
            out.flush();
            byte[] data = out.toByteArray();
            out.close();
            return data;
        } catch ( Exception e ) {
            return head;
        }
    }
    private static void writeBytes(ByteArrayOutputStream bout,int size) {
        for ( int i = 0 ; i < size ; i++ )
            bout.write(0);
    }
}