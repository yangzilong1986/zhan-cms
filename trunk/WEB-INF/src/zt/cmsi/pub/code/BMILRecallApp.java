//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMILRecallApp.java

package zt.cmsi.pub.code;


public class BMILRecallApp extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMILRECALLAPP", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMILRECALLAPP", "SEQNO");
    }

}
