/**
 * Copyright 2003 ZhongTian, Inc. All rights reserved.
 *
 * Zhongtian PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * $Id: DBUtil.java,v 1.3 2007/06/21 00:42:11 weiyb Exp $
 * File:DBUtil.java
 * Date Author Changes
 * March 10 2003 wangdeliang Created
 */

package zt.cmsi.fc;

import com.zt.util.PropertyManager;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.utils.Debug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * 数据库工具包
 *
 * @author <a href="mailto:wangdl@zhongtian.biz">WangDeLiang</a>
 * @version $Revision: 1.3 $ $Date: 2007/06/21 00:42:11 $
 */

public class DBUtil {
    static Logger logger = Logger.getLogger("zt.platform.db.DBUtil");

    /**
     * 将日期类型转换成SQL92的标准
     *
     * @param date 日期
     * @return
     */
    public static String formatWithSql92Date(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh : mm : ss");
            return "{ts '" + sdf.format(date) + "'}";
        } else {
            return "NULL";
        }
    }

    /**
     * 按要求转码
     *
     * @param content
     * @param fromEncoding
     * @param toEncoding
     * @return
     */
    public static String toDB(String content, String fromEncoding, String toEncoding) {
        if (content == null)
            return null;
        try {
            byte[] tt;
            if (fromEncoding == null) {
                tt = content.getBytes();
            } else {
                //logger.info("formEncoding is "+fromEncoding);
                tt = content.getBytes(fromEncoding);
            }
            if (toEncoding == null)
                return new String(tt);
            return new String(tt, toEncoding);
        } catch (Exception e) {
            return content;
        }
    }

    /**
     * 按要求转码
     *
     * @param content
     * @param fromEncoding
     * @param toEncoding
     * @return
     */
    public static String fromDB(String content, String fromEncoding, String toEncoding) {
        return toDB(content, fromEncoding, toEncoding);
    }

    /**
     * 按要求将web编码转为DB编码(by wxj)
     * 从配置文件中读取转码要求，将字符串转码。
     *
     * @param p_value
     * @return
     */
    public static String toDB(String p_value) {
        String encod1 = PropertyManager.getProperty(SystemAttributeNames.WEB_SERVER_ENCODING);
        String encod2 = PropertyManager.getProperty(SystemAttributeNames.DB_SERVER_ENCODING);
        return toDB(p_value, encod1, encod2);
    }

    /**
     * 按要求将web编码转为DB编码(by wxj)
     * 从配置文件中读取转码要求，将字符串转码。
     *
     * @param p_value
     * @return
     */
    public static String fromDB(String p_value) {
        String encod1 = PropertyManager.getProperty(SystemAttributeNames.DB_SERVER_ENCODING);
        String encod2 = PropertyManager.getProperty(SystemAttributeNames.WEB_SERVER_ENCODING);
        return fromDB(p_value, encod1, encod2);
    }

    /**
     * 按要求将web编码转为DB编码(by wxj)
     * 从配置文件中读取转码要求，将字符串转码。
     *
     * @param p_value
     * @return
     */
    public static String fromDB2(String p_value) {
        if (p_value == null) return "";
        String encod1 = PropertyManager.getProperty(SystemAttributeNames.DB_SERVER_ENCODING);
        String encod2 = PropertyManager.getProperty(SystemAttributeNames.WEB_SERVER_ENCODING);
        return fromDB(p_value, encod1, encod2);
    }

    /**
     * 进行toSQL转换，将'转换为''
     */
    public static final String toSql(String p_str) {
        if (p_str == null || p_str.length() < 1) return "";
        return p_str.replaceAll("'", "''");
    }

    /**
     * 进行toSQL转换，将,转换为','
     * lj added in 20050713
     */
    public static final String toSqlIn(String p_str) {
        if (p_str == null || p_str.length() < 1) return "";
        return "'" + p_str.replaceAll(",", "','") + "'";
    }

    /**
     * 进行toHtml转换
     */
    public static final String toHtml(String p_str) {
        if (p_str == null || p_str.length() == 0) return "";
        p_str = p_str.replaceAll("&", "&amp;");
        p_str = p_str.replaceAll("<", "&lt;");
        p_str = p_str.replaceAll(">", "&gt;");
        p_str = p_str.replaceAll("\"", "&quot;");
        //p_str=p_str.replaceAll("\r","<br>");
        return p_str;
    }

    /**
     * 进行toSqlDate转换
     */
    public static final String toSqlDate(String p_str) {
        if (p_str == null || p_str.length() != 8) return null;
        return "'" + p_str.substring(0, 4) + "-" + p_str.substring(4, 6) + "-" + p_str.substring(6, 8) + "'";
    }

    /**
     * 进行toSqlDate转换
     */
    public static final String toSqlDate2(String p_str) {
        if (p_str == null || p_str.length() != 8) return null;
        return "" + p_str.substring(0, 4) + "-" + p_str.substring(4, 6) + "-" + p_str.substring(6, 8) + "";
    }

    /**
     * 进行to_Date转换
     */
    public static final String to_Date(String p_str) {
        if (p_str == null || p_str.length() != 10) return null;
        return "" + p_str.substring(0, 4) + "" + p_str.substring(5, 7) + "" + p_str.substring(8, 10) + "";
    }

    /**
     * 进行to_Date转换
     */
    public static final String toDate(String p_str) {
        if (p_str == null || p_str.length() != 10) return "";
        return "" + p_str.substring(0, 4) + "" + p_str.substring(5, 7) + "" + p_str.substring(8, 10) + "";
    }

    /**
     * 将double转换为字符串，去掉科学计数法
     */
    public static String doubleToStr(double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("###0.00");
        return df.format(d);
    }

    /**
     * 将double转换为字符串，去掉科学计数法
     */
    public static String MoneytoNumber(String d) {
        //if (d==null||d.equals("")||d.equals("0.00")||d.equals("0")) return "NULL";
        if (d == null || d.equals("")) return "NULL";
        double dd = Double.parseDouble(d.replaceAll(",", ""));
        java.text.DecimalFormat df = new java.text.DecimalFormat("###0.00");
        return df.format(dd);
    }

    public static String doubleToStrb(double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("###0.000");
        return df.format(d);
    }

    public static String doubleToStr1(double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,##0.00");
        return df.format(d);
    }

    public static String numberToMoney(Object d) {
        if (d == null) return "0.00";
        java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,##0.00");
        return df.format(d);
    }

    public static String doubleToStr1b(double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,##0.000");
        return df.format(d);
    }

    public static String doubleToStrWithDigits(double d, int digits) {
        StringBuffer sb = new StringBuffer();
        sb.append("###0.");
        for (int i = 0; i < digits; i++) {
            sb.append("0");
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat(sb.toString());
        return df.format(d);
    }

    public static String doubleToStr1WithDigits(double d, int digits) {
        StringBuffer sb = new StringBuffer();
        sb.append("###,###,###,##0.");
        for (int i = 0; i < digits; i++) {
            sb.append("0");
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat(sb.toString());
        return df.format(d);
    }


    public static String intToStr(int d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("###########0");
        return df.format(d);
    }

    /**
     * 取表中某一行，某一列的数据
     * 自己负责取连接
     */
    public static String getCellValue(String p_table, String p_field, String p_where) {
        if (p_field == null || p_table == null || p_where == null) return null;
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        String sql = "select " + p_field;
        sql += " from " + p_table;
        sql += " where " + p_where;
        try {
            RecordSet rs = con.executeQuery(sql);
            if (rs.next()) {
                String tmp = rs.getString(0);
                if (tmp != null) return tmp.trim();
            }
            rs.close();
            return null;
        } catch (Exception ex) {
            Debug.debug(ex);
            return null;
        } finally {
            manager.releaseConnection(con);
        }
    }

    /**
     * 取表中某一行，某一列的数据
     * 使用传进来的连接
     */
    public static String getCellValue(DatabaseConnection p_con, String p_table, String p_field, String p_where) {
        if (p_field == null || p_table == null || p_where == null) return null;
        String sql = "select " + p_field;
        sql += " from " + p_table;
        sql += " where " + p_where;
        try {
            RecordSet rs = p_con.executeQuery(sql);
            if (rs.next()) {
                String tmp = rs.getString(0);
                if (tmp != null) return tmp.trim();
            }
            rs.close();
            return null;
        } catch (Exception ex) {
            Debug.debug(ex);
            return null;
        }
    }

    public static void main(String[] args) {
    }
}
