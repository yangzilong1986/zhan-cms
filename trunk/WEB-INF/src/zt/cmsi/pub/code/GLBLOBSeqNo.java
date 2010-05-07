package zt.cmsi.pub.code;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class GLBLOBSeqNo {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("GLBLOB", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("GLBLOB", "SEQNO");
    }

    static public void main(String[] args) {
        System.out.print(GLBLOBSeqNo.getNextNo());
        System.out.print(GLBLOBSeqNo.getNextNo());
    }

}