//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMLoanTypeChange.java

package zt.cmsi.pub.code;


public class BMLoanTypeChange extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMLOANTYPECHANGE", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMLOANTYPECHANGE", "SEQNO");
    }

}
