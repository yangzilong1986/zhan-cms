//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPDTrans.java

package zt.cmsi.pub.code;


public class BMPDTrans extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPDTRANS", "PDTRANSNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPDTRANS", "PDTRANSNO");
    }

}
