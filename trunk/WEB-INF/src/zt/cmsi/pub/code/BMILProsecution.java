//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMILProsecution.java

package zt.cmsi.pub.code;


public class BMILProsecution extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMILPROSECUTION", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMILPROSECUTION", "SEQNO");
    }

}
