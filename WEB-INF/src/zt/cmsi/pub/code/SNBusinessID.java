//Source file: e:\\java\\zt\\cmsi\\pub\\code\\SNBusinessID.java

package zt.cmsi.pub.code;

import java.text.NumberFormat;


public class SNBusinessID extends SerialNumber2 {
    public static String getNextNo() {
        long sn = SerialNumber2.getNextSN("BMTABLE", "BMNO");
        //System.out.println(sn);
        if (sn <= 0) return null;

        if (sn < Long.parseLong("10000000000")) sn = Long.parseLong("10000000001");
        //System.out.println(sn);

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
        long sn = SerialNumber2.getCurSN("BMTABLE", "BMNO");
        if (sn <= 0) return null;

        if (sn < Long.parseLong("10000000000")) sn = Long.parseLong("10000000001");

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

    static public void main(String[] args) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(11);
        //nf.setParseIntegerOnly(false);
        nf.setGroupingUsed(false);
        System.out.println("FFF:" + nf.format(1123));
        System.out.print(SNBusinessID.getNextNo());
    }
}
