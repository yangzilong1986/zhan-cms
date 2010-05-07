//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMLoanRespChange.java

package zt.cmsi.pub.code;


public class BMLoanRespChange extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMLOANRESPCHANGE", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMLOANRESPCHANGE", "SEQNO");
    }

}
