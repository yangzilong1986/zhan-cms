//Source file: e:\\java\\zt\\cmsi\\pub\\define\\SystemDate.java

package zt.cmsi.pub.define;

import com.zt.util.setup.SetupManager;
import zt.cmsi.biz.util;
import zt.cmsi.pub.confitem;
import zt.platform.utils.Debug;

import java.sql.Date;
import java.util.Calendar;

/**
 * ��ȡϵͳʱ�䣬�����ַ�ʽ
 *
 * @author zhouwei
 *         $Date: 2007/07/04 02:11:40 $
 * @version 1.0
 *          <p/>
 *          ��Ȩ���ൺ���칫˾
 */

public class SystemDate {

    /**
     * ϵͳ��ǰʱ��
     */
    private static Calendar systemDate = null;
    /**
     * ����ʱ��
     */
    private static Calendar yesterday = null;
    /**
     * ����ʱ��
     */
    private static Calendar tomorrow = null;

    /**
     * ������������
     *
     * @return java.util.Calendar
     *         roseuid 3FE4003A007E
     */
    public static Calendar getSystemDate1() {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
            temp.set(systemDate.get(Calendar.YEAR),
                    systemDate.get(Calendar.MONTH),
                    systemDate.get(Calendar.DAY_OF_MONTH));
            return temp;
        } else {
            return null;
        }
    }

    /**
     * ���ô�ȷ��
     *
     * @return boolean
     *         roseuid 3FE400AC008C
     */
    public static boolean refresh() {
        try {
            systemDate = SetupManager.getCalendarProperty(confitem.DATE_Module + ":" +
                    confitem.DATE_TODAY);
            yesterday = SetupManager.getCalendarProperty(confitem.DATE_Module + ":" +
                    confitem.DATE_YESTERDAY);
            tomorrow = SetupManager.getCalendarProperty(confitem.DATE_Module + ":" +
                    confitem.DATE_TMRW);

            if (systemDate == null || yesterday == null || tomorrow == null) {
                Debug.debug(Debug.TYPE_ERROR, "CAN NOT GET BUSINESS DATE IN SETUP MANGAGER!");
                return false;
            }
            return true;
        } catch (Exception e) {
            Debug.debug(Debug.TYPE_ERROR, "Exception when GET BUSINESS DATE IN SETUP MANGAGER!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return false;
        }
    }

    /**
     * ����-�����ָ������������ַ���
     *
     * @return String
     *         roseuid 3FE400D202CB
     */
    public static String getSystemDate2() {
        if (getDBDate() == true)
            return getSystemDateByComma(systemDate, "-");
        else
            return null;
    }

    /**
     * �����������͵Ķ���
     *
     * @return Date
     *         roseuid 3FE40117019E
     */
    public static java.util.Date getSystemDate3() {
        if (getDBDate() == true)
            return systemDate.getTime();
        else
            return null;
    }

    public static Date getSystemDate4() {
        if (getDBDate() == true) {
            java.sql.Date dd = new java.sql.Date(systemDate.getTimeInMillis());
            return dd;
        } else
            return null;
    }

    /**
     * @param comma �ָ��
     * @return �����Զ����ʱ���ʽ ����
     */
    public static String getSystemDate5(String comma) {
        if (getDBDate() == true)
            return getSystemDateByComma(systemDate, comma);
        else
            return null;
    }

    /**
     * @param comma �ָ��
     * @return �����Զ����ʱ���ʽ ����
     */
    public static String getYesterday(String comma) {
        if (getDBDate() == true)
            return getSystemDateByComma(yesterday, comma);
        else
            return null;
    }

    /**
     * @param comma �ָ��
     * @return �����Զ����ʱ���ʽ ����
     */
    public static String getTomorrow(String comma) {
        if (getDBDate() == true)
            return getSystemDateByComma(tomorrow, comma);
        else
            return null;
    }

    /**
     * ����ϵͳ����ͬ��ʱ��
     * Author lj
     *
     * @return java.util.Calendar
     */
    public static String getLastMonSDate(String date, String comma) {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
//            temp.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)));
//            int var = 0;
//            if (temp.get(Calendar.DAY_OF_MONTH) > 28) {
//                if (temp.get(Calendar.MONTH) == 3) {
//                    var = 28 - temp.get(Calendar.DAY_OF_MONTH);
//                } else if (temp.get(Calendar.DAY_OF_MONTH) > 30) {
//                    var = -1;
//                }
//            }
//            temp.set(temp.get(Calendar.YEAR),
//                    temp.get(Calendar.MONTH) - 2,
//                    temp.get(Calendar.DAY_OF_MONTH) + var);

            //lj modified in 2007-05-13 ����������µ�����,�򷵻������µ����ڡ�
            temp.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
            if (temp.get(Calendar.DAY_OF_MONTH) == temp.getActualMaximum(Calendar.DAY_OF_MONTH))
                temp.set(Calendar.DAY_OF_MONTH, 0);
            else temp.add(Calendar.MONTH, -1);

            return getSystemDateByComma(temp, comma);
        } else {
            return null;
        }
    }

    /**
     * ����ϵͳ�������ʱ��
     * Author lj
     *
     * @return java.util.Calendar
     */
    public static String getLastYearDate(String date, String comma) {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
            temp.set(Integer.parseInt(date.substring(0, 4)), 0, 0);

            return getSystemDateByComma(temp, comma);
        } else {
            return null;
        }
    }

    /**
     * ����ϵͳ����ͬ��ʱ��
     * Author lj
     *
     * @return java.util.Calendar
     */
    public static String getLastYearSDate(String date, String comma) {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
//            temp.set(Integer.parseInt(date.substring(0, 4)) - 1, Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));

            //lj modified in 2007-05-13
            temp.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
            temp.add(Calendar.YEAR, -1);
            return getSystemDateByComma(temp, comma);
        } else {
            return null;
        }
    }


    private static boolean getDBDate() {
        if (systemDate == null || tomorrow == null || yesterday == null)
            return refresh();
        else
            return true;
    }

    /**
     * ��ָ���ķָ������������ַ���
     * Author lj
     *
     * @param comma - ָ���ķָ���
     * @return String
     */
    public static String getDateByComma(String date, String comma) {
        return date.substring(0, 4) + comma + date.substring(5, 7) + comma + date.substring(8, 10);
    }

    /**
     * ����ϵͳ�����µ�ʱ��
     * Author houcs
     *
     * @return
     */
    public static String getLastMonthDate(String date, String comma) {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
            temp.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, 0);
            return getSystemDateByComma(temp, comma);
        } else {
            return null;
        }
    }

    /**
     * ����ϵͳ�������ʱ��
     * <p/>
     * Author houcs
     *
     * @return
     */
    public static String getThisYearDate(String date, String comma) {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
            temp.set(Integer.parseInt(date.substring(0, 4)) + 1, 0, 0);
            return getSystemDateByComma(temp, comma);
        } else {
            return null;
        }
    }

    /**
     * ����ϵͳ�����µ�ʱ��
     * Author houcs
     *
     * @return
     */
    public static String getThisMonthDate(String date, String comma) {
        if (getDBDate() == true) {
            Calendar temp = Calendar.getInstance();
            temp.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), 0);
            return getSystemDateByComma(temp, comma);
        } else {
            return null;
        }
    }

    /**
     * ����ϵͳ�ϼ����µ�ʱ��
     * Author liujian
     * added in 2007-05-28
     *
     * @return String
     */
    public static String getLastQuarterDate() {
        if (getDBDate()) {
            Calendar date = (Calendar) systemDate.clone();
            int mon = date.get(Calendar.MONTH);
            switch (mon) {
                case 0:
                case 1:
                case 2:
                    date.set(date.get(Calendar.YEAR), Calendar.JANUARY, 0);
                    break;
                case 3:
                case 4:
                case 5:
                    date.set(Calendar.MONTH, Calendar.APRIL);
                    date.set(Calendar.DATE, 0);
                    break;
                case 6:
                case 7:
                case 8:
                    date.set(Calendar.MONTH, Calendar.JULY);
                    date.set(Calendar.DATE, 0);
                    break;
                case 9:
                case 10:
                case 11:
                    date.set(Calendar.MONTH, Calendar.OCTOBER);
                    date.set(Calendar.DATE, 0);
                    break;
            }
            return getSystemDateByComma(date, "-");
        } else
            return null;
    }

    /**
     * ���ظ���ʱ���ϼ����µ�ʱ��
     * Author liujian
     * added in 2007-07-02
     *
     * @return String
     */
    public static String getLastQuarterDate(String adate) {
        Calendar date = (Calendar) systemDate.clone();
        int mon = Integer.parseInt(adate.substring(5, 7)) - 1;

        switch (mon) {
            case 0:
            case 1:
            case 2:
                date.set(date.get(Calendar.YEAR), Calendar.JANUARY, 0);
                break;
            case 3:
            case 4:
            case 5:
                date.set(Calendar.MONTH, Calendar.APRIL);
                date.set(Calendar.DATE, 0);
                break;
            case 6:
            case 7:
            case 8:
                date.set(Calendar.MONTH, Calendar.JULY);
                date.set(Calendar.DATE, 0);
                break;
            case 9:
            case 10:
            case 11:
                date.set(Calendar.MONTH, Calendar.OCTOBER);
                date.set(Calendar.DATE, 0);
                break;
        }
        return getSystemDateByComma(date, "-");
    }

    /**
     * ��ָ���ķָ������������ַ���
     *
     * @param sysdate - ָ��������
     * @param comma   - ָ���ķָ���
     * @return String
     */
    private static String getSystemDateByComma(Calendar sysdate, String comma) {
        String sDate = "";
        int iYear = sysdate.get(Calendar.YEAR);
        int iMonth = sysdate.get(Calendar.MONTH) + 1;
        int iDate = sysdate.get(Calendar.DAY_OF_MONTH);
        sDate = String.valueOf(iYear) + (comma == null ? "" : comma);
        if (iMonth < 10)
            sDate += "0";
        sDate += iMonth + (comma == null ? "" : comma);
        if (iDate < 10)
            sDate += "0";
        sDate += iDate;
        return sDate;
    }


    public static void main(String[] args) {
        System.out.println(util.calToString(SystemDate.getSystemDate1(), "*"));
        System.out.println(SystemDate.getSystemDate2());
        System.out.println(SystemDate.getSystemDate3());
        System.out.println(SystemDate.getSystemDate4());
        System.out.println(SystemDate.getSystemDate5(null));
        System.out.println(SystemDate.getSystemDate5("/"));

        System.out.println(SystemDate.getYesterday(null));
        System.out.println(SystemDate.getTomorrow(null));
        //zt.platform.db.DatabaseConnection db = zt.platform.db.ConnectionManager.getInstance().getConnection();
    }
}
