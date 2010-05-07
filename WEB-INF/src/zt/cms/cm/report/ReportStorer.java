/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ReportStorer.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.report;

import zt.platform.db.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

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
 * @created 2003Äê12ÔÂ2ÈÕ
 */

public class ReportStorer {
    /**
     * Constructor for the ReportStorer object
     */
    private ReportStorer() {
    }


    /**
     * Description of the Method
     *
     * @param report Description of the Parameter
     * @return Description of the Return Value
     */
    public static boolean storeReport(Report report) {
        String insertStr = "insert into CMREPORTDAY(reporttype,entityno,\"DATE\",reportname) values(?,?,?,?)";
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();

        con.setAuto(false);
        PreparedStatement pst = con.getPreparedStatement(insertStr);
        try {
            Calendar c = report.getReportDate();
            Date theDate = new Date(c.getTime().getTime());

            pst.setString(1, report.getReportType());
            pst.setString(2, report.getEntityNo());
            pst.setDate(3, theDate);
            pst.setString(4, DBUtil.toDB(report.getReportName()));
            pst.execute();

            Item[] items = report.getPrintItems();
            PreparedStatement itemPst = con.getPreparedStatement("insert into cmreportdata values(?,?,?,?,?,?,?)");

            for (int i = 0; i < items.length; i++) {
                itemPst.setString(1, report.getReportType());
                itemPst.setInt(2, items[i].getItemNo());
                itemPst.setString(3, report.getEntityNo());

                itemPst.setDate(4, theDate);
                itemPst.setDouble(5, items[i].getItemValue());
                itemPst.setNull(6, 1);
                itemPst.setNull(7, 1);
                itemPst.execute();
            }
            con.commit();
            manager.releaseConnection(con);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            con.rollback();
            manager.releaseConnection(con);
            return false;
        }
    }


    /**
     * Description of the Method
     *
     * @param report Description of the Parameter
     * @return Description of the Return Value
     */
    public static boolean changeReport(Report report) {
        String insertStr = "update CMREPORTDAY set reportname=? where reporttype=? and entityno=? and \"DATE\"=?";
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();

        con.setAuto(false);
        PreparedStatement pst = con.getPreparedStatement(insertStr);
        try {
            Calendar c = report.getReportDate();
            Date theDate = new Date(c.getTime().getTime());

            pst.setString(2, report.getReportType());
            pst.setString(3, report.getEntityNo());
            pst.setDate(4, theDate);
            pst.setString(1, DBUtil.toDB(report.getReportName()));
            pst.execute();

            Item[] items = report.getPrintItems();
            PreparedStatement itemPst = con.getPreparedStatement("update cmreportdata set dayvalue=? where reporttype=? and itemno=? and entityno=? and \"DATE\"=?");

            for (int i = 0; i < items.length; i++) {
                //System.out.println("-----------------" + items[i].getItemValue());
                itemPst.setDouble(1, items[i].getItemValue());
                itemPst.setString(2, report.getReportType());
                itemPst.setInt(3, items[i].getItemNo());
                itemPst.setString(4, report.getEntityNo());
                itemPst.setDate(5, theDate);

                itemPst.execute();
            }
            con.commit();
            manager.releaseConnection(con);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            con.rollback();
            manager.releaseConnection(con);
            return false;
        }
    }


    /**
     * Description of the Method
     *
     * @param report Description of the Parameter
     * @return Description of the Return Value
     */
    public static boolean deleteReport(Report report) {
        String deleteStr1 = "delete from CMREPORTDATA where reporttype=? and entityno=? and \"DATE\"=?";
        String deleteStr2 = "delete from CMREPORTDAY where reporttype=? and entityno=? and \"DATE\"=?";
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();

        con.setAuto(false);
        PreparedStatement pst1 = con.getPreparedStatement(deleteStr1);
        PreparedStatement pst2 = con.getPreparedStatement(deleteStr2);
        try {
            Calendar c = report.getReportDate();
            Date theDate = new Date(c.getTime().getTime());

            pst1.setString(1, report.getReportType());
            pst1.setString(2, report.getEntityNo());
            pst1.setDate(3, theDate);
            pst1.execute();

            pst2.setString(1, report.getReportType());
            pst2.setString(2, report.getEntityNo());
            pst2.setDate(3, theDate);
            pst2.execute();

            con.commit();
            manager.releaseConnection(con);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            con.rollback();
            manager.releaseConnection(con);
            return false;
        }
    }

}
