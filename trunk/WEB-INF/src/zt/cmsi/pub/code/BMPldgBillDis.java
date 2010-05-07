//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPldgBillDis.java

package zt.cmsi.pub.code;


public class BMPldgBillDis extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPLDGBILLDIS", "PLEDGENO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPLDGBILLDIS", "PLEDGENO");
    }

}
