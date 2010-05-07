//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMILNotifi.java

package zt.cmsi.pub.code;


public class BMILNotifi extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMILNOTIFI", "NOTIFNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMILNOTIFI", "NOTIFNO");
    }

}
