//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMBILLDIS.java

package zt.cmsi.pub.code;


public class BMBILLDIS extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMBILLDIS", "BILLDISNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMBILLDIS", "BILLDISNO");
    }
}
