//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMAcptBill.java

package zt.cmsi.pub.code;


public class BMAcptBill extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMACPTBILL", "ACPTBILLNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMACPTBILL", "ACPTBILLNO");
    }

}
