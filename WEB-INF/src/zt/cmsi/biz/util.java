package zt.cmsi.biz;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;


public class util {

    public util() {
    }

    public static Calendar dateToCalendar(Date date1) {
        if (date1 == null) return null;
        Calendar cc = Calendar.getInstance();
        cc.setTime(date1);
        return cc;
    }

    public static String calToString(Calendar date1, String comma) {
        if (date1 == null) return null;
        String sDate = new String();
        int iYear = date1.get(Calendar.YEAR);
        int iMonth = date1.get(Calendar.MONTH) + 1;
        int iDate = date1.get(Calendar.DAY_OF_MONTH);
        sDate = String.valueOf(iYear) + (comma == null ? "" : comma);
        if (iMonth < 10)
            sDate += "0";
        sDate += iMonth + (comma == null ? "" : comma);
        if (iDate < 10)
            sDate += "0";
        sDate += iDate;
        return sDate;
    }

    public static int daysBetweenCals(Calendar date1, Calendar date2) {
        if (date1 == null || date2 == null) return 0;

        int iYear = date1.get(Calendar.YEAR);
        int iMonth = date1.get(Calendar.MONTH) + 1;
        int iDate = date1.get(Calendar.DAY_OF_MONTH);

        int iYear2 = date2.get(Calendar.YEAR);
        int iMonth2 = date2.get(Calendar.MONTH) + 1;
        int iDate2 = date2.get(Calendar.DAY_OF_MONTH);

        return ((iYear - iYear2) * 360 + (iMonth - iMonth2) * 30 + iDate - iDate2);
    }


    public static String calToString(Calendar date1) {
        if (date1 == null) return null;
        String sDate = new String();
        int iYear = date1.get(Calendar.YEAR);
        int iMonth = date1.get(Calendar.MONTH) + 1;
        int iDate = date1.get(Calendar.DAY_OF_MONTH);
        sDate = String.valueOf(iYear);
        if (iMonth < 10)
            sDate += "0";
        sDate += iMonth;
        if (iDate < 10)
            sDate += "0";
        sDate += iDate;
        return sDate;
    }

    public static String numberToString(BigDecimal num, int prec, int dec, boolean haspoint) {
        if (prec <= 0 || dec < 0) return null;
        if (num == null) return null;
        try {
            String ss = null;
            NumberFormat f = NumberFormat.getInstance();
            f.setGroupingUsed(false);
            //f.setMaximumIntegerDigits(prec);
            //f.setMinimumIntegerDigits(prec);
            f.setMaximumFractionDigits(dec);
            f.setMinimumFractionDigits(dec);
            ss = f.format(num.doubleValue());
            if (ss.length() < prec + dec + 1)
                ss = util.getNSpace(prec + dec + 1 - ss.length()) + ss;
            return ss;
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            return null;
        }
    }

    public static String fixLenString(String str, int len) {
        if (str == null) return null;
        String ss;
        if (str.length() == len)
            ss = str;
        else if (str.length() > len)
            ss = str.substring(0, len);
        else
            ss = (str + util.getNSpace(len - str.length()));

        return ss;
    }

    public static String getNSpace(int n) {
        StringBuffer sb = new StringBuffer(n);
        for (int i = 1; i <= n; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static Calendar stringToCal(String aDate) {
        Calendar c = Calendar.getInstance();
        String[] d;

        d = aDate.split("-");
        if (d.length == 3) {
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
            return c;
        } else if (aDate.length() == 8) {
            d = new String[3];
            d[0] = aDate.substring(0, 4);
            d[1] = aDate.substring(4, 6);
            d[2] = aDate.substring(6, 8);
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1,
                    Integer.parseInt(d[2]));
            return c;
        }
        return null;

    }

    public static boolean isDate(String aDate) {
        try {
            if (aDate == null) return true;
            aDate = aDate.trim();
            if (aDate.length() != 8) return false;
            for (int i = 0; i <= 5; i++) {
                if (Character.isDigit(aDate.charAt(i)) == false) return false;
            }

            int t;
            t = Integer.parseInt(aDate.substring(0, 4));
            t = Integer.parseInt(aDate.substring(4, 6));
            if (t < 1 || t > 12) return false;
            t = Integer.parseInt(aDate.substring(6, 8));
            if (t < 1 || t > 31) return false;

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        //util util1 = new util();
        //Calendar data1 = Calendar.getInstance();
        //System.out.println(util.calToString(data1));
        System.out.println(util.numberToString(new BigDecimal(189.87), 1, 3, true));
        //System.out.println("["+util.fixLenString(null,18)+"]");
        try {
//      int a = 1/0;
            System.out.println(util.stringToCal("20030101").before(util.stringToCal("20030201")));
        }
        catch (Exception e) {
//      for(int i=1;i<=1;i++)
//        Debug.debug(e);
//      Debug.debug(Debug.TYPE_MESSAGE,"Message!!!!!!!!!!!!!!!!");
//      Debug.debug(Debug.TYPE_ERROR,"ERROR!!!!!!!!!!!!!!!!");
//      Debug.debug(Debug.TYPE_SQL,"SSSQQQKKKK!!!!!!!!!!!!!!!!");
//      Debug.debug(Debug.TYPE_WARNING ,"WARNING!!!!!!!!!!!!!!!!");
            //e.printStackTrace(System.out);
            ;
        }

        String id = "370205197704224589";
        {
            int iS = 0, iY;
            int iW[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            String szVerCode = "10X98765432";

            int i;
            for (i = 0; i < 17; i++) {
                iS += (int) (id.charAt(i) - '0') * iW[i];
            }
            iY = iS % 11;
            System.out.println("check code is:" + szVerCode.charAt(iY));
            if (szVerCode.charAt(iY) != id.charAt(i)) {
                System.out.println("error");
                return;
            }
        }
        System.out.println(util.isDate("88880131"));

    }

}
