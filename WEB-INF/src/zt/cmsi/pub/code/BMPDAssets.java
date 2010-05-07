//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPDAssets.java

package zt.cmsi.pub.code;


public class BMPDAssets extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPDASSETS", "PDNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPDASSETS", "PDNO");
    }

}
