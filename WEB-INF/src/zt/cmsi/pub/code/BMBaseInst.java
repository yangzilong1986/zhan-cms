package zt.cmsi.pub.code;

public class BMBaseInst extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("SCBASEINST", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("SCBASEINST", "SEQNO");
    }

}
