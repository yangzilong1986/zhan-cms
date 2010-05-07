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
        (2)����+(4)CICS+(4)���׺�+(8)����+(6)��ˮ��+(12)�����+(4)�ն˺�+(4)��Ա��+(10)����(0x00)
        */
        if ( len < 0 ) len = 0;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            //LU��
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
            //��ˮ��(6)
            bout.write(txnseq.getBytes());
            //����ţ�12��
            bout.write(res.getBrhid().getBytes());
            //�ն˺ţ�4)
            bout.write(res.getWsid().getBytes());
            //��Ա�ţ�4��
            bout.write(res.getTellid().getBytes());
            //other ��10��
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