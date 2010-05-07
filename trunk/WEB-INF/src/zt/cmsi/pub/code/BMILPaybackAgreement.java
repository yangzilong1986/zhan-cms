//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMILPaybackAgreement.java

package zt.cmsi.pub.code;


public class BMILPaybackAgreement extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMILPAYBACKAGREEMENT", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMILPAYBACKAGREEMENT", "SEQNO");
    }

}
