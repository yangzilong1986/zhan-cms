//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMILPaymentOrder.java

package zt.cmsi.pub.code;


public class BMILPaymentOrder extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMILPAYMENTORDER", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMILPAYMENTORDER", "SEQNO");
    }

}
