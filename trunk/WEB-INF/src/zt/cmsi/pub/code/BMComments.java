//Source file: e:\\java\\zt\\cmsi\\pub\\code\\BMComments.java

package zt.cmsi.pub.code;


public class BMComments extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMCOMMENTS", "COMMENTNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMCOMMENTS", "COMMENTNO");
    }

}
