/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: Report.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.report;

import zt.platform.db.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <p/>
 * Title: </p> <p>
 * <p/>
 * Description: </p> <p>
 * <p/>
 * Copyright: Copyright (c) 2003</p> <p>
 * <p/>
 * Company: </p>
 *
 * @author not attributable
 * @version 1.0
 * @created 2003Äê11ÔÂ20ÈÕ
 */

public class Report {

    private String reportType;
    private String reportTypeName;
    Map items = new HashMap();
    private java.util.Calendar reportDate;
    private String entityNo = "";
    private String reportName = "";

    String limits[] = null;


    /**
     * Constructor for the Report object
     *
     * @param type Description of the Parameter
     * @param name Description of the Parameter
     */
    public Report(String type, String name) {
        reportType = type;
        reportTypeName = name;
    }


    /**
     * Gets the reportType attribute of the Report object
     *
     * @return The reportType value
     */
    public String getReportType() {
        return reportType;
    }


    /**
     * Sets the reportType attribute of the Report object
     *
     * @param reportType The new reportType value
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }


    /**
     * Gets the reportName attribute of the Report object
     *
     * @return The reportName value
     */
    public String getReportTypeName() {
        return reportTypeName;
    }


    /**
     * Sets the reportName attribute of the Report object
     *
     * @param reportName The new reportName value
     */
    public void setReportTypeName(String reportName) {
        this.reportTypeName = reportName;
    }


    /**
     * Adds a feature to the Item attribute of the Report object
     *
     * @param itemNo   The feature to be added to the Item attribute
     * @param itemName The feature to be added to the Item attribute
     * @param printNo  The feature to be added to the Item attribute
     * @return Description of the Return Value
     */
    public boolean addItem(int itemNo, String itemName, int printNo) {
        Item item = new Item();
        item.setItemNo(itemNo);
        item.setItemName(itemName);
        item.setPrintNo(printNo);
        if (containItem(item)) {
            return false;
        } else {
            items.put(new Integer(item.getItemNo()), item);
            item.setReport(this);
            return true;
        }
    }


    /**
     * Sets the itemValue attribute of the Report object
     *
     * @param itemNo    The new itemValue value
     * @param itemValue The new itemValue value
     * @return Description of the Return Value
     */
    public boolean setItemValue(int itemNo, double itemValue) {
        Object o = items.get(new Integer(itemNo));
        if (o == null) {
            return false;
        } else {
            Item item = (Item) o;
            item.setItemValue(itemValue);
            return true;
        }

    }


    /**
     * Description of the Method
     *
     * @param item Description of the Parameter
     * @return Description of the Return Value
     */
    public boolean containItem(Item item) {
        if (items.containsKey(new Integer(item.getItemNo()))) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Gets the printItems attribute of the Report object
     *
     * @return The printItems value
     */
    public Item[] getPrintItems() {
        Object[] o = items.values().toArray();

        Item[] items = new Item[o.length];
        for (int i = 0; i < o.length; i++) {
            items[i] = (Item) o[i];
        }
        Arrays.sort(items);
        return items;
    }


    /**
     * Gets the reportDate attribute of the Report object
     *
     * @return The reportDate value
     */
    public java.util.Calendar getReportDate() {
        if (reportDate == null) {
            return Calendar.getInstance();
        } else {
            return reportDate;
        }
    }


    /**
     * Sets the reportDate attribute of the Report object
     *
     * @param reportDate The new reportDate value
     */
    public void setReportDate(java.util.Calendar reportDate) {
        this.reportDate = reportDate;
    }


    /**
     * Gets the entityNo attribute of the Report object
     *
     * @return The entityNo value
     */
    public String getEntityNo() {
        return entityNo;
    }


    /**
     * Sets the entityNo attribute of the Report object
     *
     * @param entityNo The new entityNo value
     */
    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }


    /**
     * Gets the reportName attribute of the Report object
     *
     * @return The reportName value
     */
    public String getReportName() {
        return reportName;
    }


    /**
     * Sets the reportName attribute of the Report object
     *
     * @param reportName The new reportName value
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }


    /**
     * Sets the reportDate attribute of the Report object
     *
     * @param date The new reportDate value
     * @return Description of the Return Value
     */
    public boolean setReportDate(String date) {
        Calendar c = Calendar.getInstance();
        String[] d;

        d = date.split("-");
        if (d.length == 3) {
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
        } else {
            d = new String[3];
            d[0] = date.substring(0, 4);
            d[1] = date.substring(4, 6);
            d[2] = date.substring(6, 8);
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1,
                    Integer.parseInt(d[2]));
        }
        this.reportDate = c;
        return true;
    }


    /**
     * Gets the dataString attribute of the Report object
     *
     * @return The dataString value
     */
    public String getDataString() {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new java.sql.Date(getReportDate().getTime().getTime()));
        return dateString;
    }


    /**
     * Gets the values attribute of the Report object
     *
     * @return The values value
     */
    public boolean getValues() {
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        String str = "select reportname from cmreportday where \"DATE\"=? and reporttype=? and entityno=?";
        PreparedStatement namePst = con.getPreparedStatement(str);

        PreparedStatement pst = con.getPreparedStatement("select * from cmreportdata where \"DATE\"=? and reporttype=? and entityno=?");
        try {
            namePst.setDate(1, new java.sql.Date(reportDate.getTime().getTime()));
            namePst.setString(2, getReportType());
            namePst.setString(3, getEntityNo());
            ResultSet nameRs = namePst.executeQuery();
            if (nameRs.next()) {
                setReportName(DBUtil.fromDB(nameRs.getString("reportname")));
            }

            pst.setDate(1, new java.sql.Date(reportDate.getTime().getTime()));
            pst.setString(2, getReportType());
            pst.setString(3, getEntityNo());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int itemNo = rs.getInt("itemno");
                double itemValue = rs.getDouble("dayvalue");
                setItemValue(itemNo, itemValue);
            }

            manager.releaseConnection(con);
            return true;
        } catch (SQLException ex) {
            manager.releaseConnection(con);
            ex.printStackTrace();
            return false;
        }

    }


    /**
     * Gets the reportTypes attribute of the Report class
     *
     * @return The reportTypes value
     */
    public static Map getReportTypes() {
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        RecordSet rs = con.executeQuery("select * from cmreport");
        Map types = new HashMap();

        while (rs.next()) {
            types.put(rs.getString("reporttype").trim(),
                    DBUtil.fromDB(rs.getString("reportname")));
        }
        manager.releaseConnection(con);
        return types;
    }

    public String[] getLimits() {
        return limits;
    }

    public void setLimits(String[] limits) {
        this.limits = limits;
    }

    public String[] getCondition(int i) {
        String patternStr = "([0-9]{1,})";

// Compile regular expression
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(limits[i]);

        StringBuffer buf = new StringBuffer();
        boolean found = false;
        while ((found = matcher.find())) {
            String replaceStr = matcher.group();
            replaceStr = "parseFloat(r" + replaceStr + ".value)";
            matcher.appendReplacement(buf, replaceStr);
        }
        matcher.appendTail(buf);
        String result = buf.toString();
        return result.split("=");
    }

    public String getMessage(int i) {


        String patternStr = "([0-9]{1,})";

// Compile regular expression
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(limits[i]);

        StringBuffer buf = new StringBuffer();
        boolean found = false;
        while ((found = matcher.find())) {
            String replaceStr = matcher.group();
            replaceStr = replaceStr + "," + this.getPrintItems()[Integer.parseInt(replaceStr) - 1].getItemName().replaceAll("¡¡", "");
            matcher.appendReplacement(buf, replaceStr);
        }
        matcher.appendTail(buf);
        String result = buf.toString();
        return result;
    }
}
