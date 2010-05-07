package zt.cmsi.pub.code;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class BMReviewLimitCode extends SerialNumber2 {
    public static int getNextNo() {
        return (int) SerialNumber2.getNextSN("BMREVIEWLIMIT", "SEQNO");
    }

    public static int getCurNo() {
        return (int) SerialNumber2.getCurSN("BMREVIEWLIMIT", "SEQNO");
    }

}