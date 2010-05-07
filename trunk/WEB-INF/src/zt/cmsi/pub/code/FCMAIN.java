package zt.cmsi.pub.code;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @GZL(JGO)
 * @version 1.0
 */

import java.text.NumberFormat;


public class FCMAIN extends SerialNumber2 {


    public static String getNextNo() {
        long sn = SerialNumber2.getNextSN("FCMAIN", "FCNO");
        //System.out.println(sn);
        if (sn <= 0) return "20000000001";

        {
            try {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumIntegerDigits(11);
                nf.setGroupingUsed(false);
                return nf.format(sn);
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    public static String getCurNo() {
        long sn = SerialNumber2.getCurSN("FCMAIN", "FCNO");
        if (sn <= 0) return "20000000000";

        {
            try {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumIntegerDigits(11);
                nf.setGroupingUsed(false);
                return nf.format(sn);
            }
            catch (Exception e) {
                return null;
            }
        }
    }

}
