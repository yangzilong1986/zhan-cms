//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMLoanAdmChange.java

package zt.cmsi.pub.code;


public class BMLoanAdmChange extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMLOANADMCHANGE", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMLOANADMCHANGE", "SEQNO");
    }

}
