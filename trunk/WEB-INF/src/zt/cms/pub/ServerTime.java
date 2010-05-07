package zt.cms.pub;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Yusg
 * @update wxj 20031201 getCurrentDate2
 * @version 1.0
 */
import java.util.*;

public class ServerTime {
  /** 用于指定日期类型，取得日期年份 */
  public static final String DATE_YEAR = "year";
  /** 用于指定日期类型，取得日期月份 */
  public static final String DATE_MONTH = "month";
  /** 用于指定日期类型，取得日期天 */
  public static final String DATE_DAY = "day";
  /** 用于指定日期类型，取得日期 */
  public static final String DATE_ALL = "all";

  static Calendar day = null;

  /**
   * 获得当前日期，格式为"2003-01-01"
   */
  public static String getDbCurrentDate() {
    day = Calendar.getInstance();
    int iYear = day.get(day.YEAR);
    int iMonth = day.get(day.MONTH) + 1;
    int iDay = day.get(day.DAY_OF_MONTH);
    String strDate = String.valueOf(iYear);
    strDate += '-';
    if (iMonth < 10) {
      strDate += '0';
    }
    strDate += iMonth;
    strDate += '-';
    if (iDay < 10) {
      strDate += '0';
    }
    strDate += iDay;
    return strDate;
  }
  /**
   * 获得当前日期，格式为"20030101"
   */
  public static String getPgCurrentDate() {
    return getDbCurrentDate().replaceAll("-","");
  }


  /**
   * <p>Title: </p>
   */
  public static String getDate(String content, String type) {
    if (content == null || content.length() < 10) {
      return content;
    }
    if (type == null) {
      return content.substring(0, 4) + content.substring(5, 7) +
          content.substring(8, 10);
    }
    if (type.toLowerCase().equals(DATE_YEAR)) {
      return content.substring(0, 4);
    }
    if (type.toLowerCase().equals(DATE_MONTH)) {
      return content.substring(5, 7);
    }
    if (type.toLowerCase().equals(DATE_DAY)) {
      return content.substring(8, 10);
    }
    if (type.toLowerCase().equals(DATE_ALL)) {
      return content.substring(0, 4) + content.substring(5, 7) +
          content.substring(8, 10);
    }
    return content.substring(0, 4) + content.substring(5, 7) +
        content.substring(8, 10);
  }

  public static void main(String[] args) {
    System.out.println(getDbCurrentDate());
    System.out.println(getPgCurrentDate());
  }

}