//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPldgSecurity.java

package zt.cmsi.pub.code;


public class BMPldgSecurity extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPLDGSECURITY", "PLEDGENO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPLDGSECURITY", "PLEDGENO");
    }

}
