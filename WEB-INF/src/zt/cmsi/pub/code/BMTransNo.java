//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMTransNo.java

package zt.cmsi.pub.code;


public class BMTransNo extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMTRANS", "BMTRANSNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMTRANS", "BMTRANSNO");
    }

}
