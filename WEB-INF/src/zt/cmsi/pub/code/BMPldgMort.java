//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPldgMort.java

package zt.cmsi.pub.code;


public class BMPldgMort extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPLDGMORT", "PLEDGENO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPLDGMORT", "PLEDGENO");
    }

}
