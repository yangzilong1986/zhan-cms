//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMPostLoanCheck.java

package zt.cmsi.pub.code;


public class BMPostLoanCheck extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMPOSTLOANCHECK", "CHECKNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMPOSTLOANCHECK", "CHECKNO");
    }

}
