/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: ReportType.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */
package zt.cms.cm.report;

import zt.platform.db.*;
import java.util.logging.*;
import java.util.*;
/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003Äê11ÔÂ20ÈÕ
 *@version    1.0
 */

public class ReportType {
    /**
     *  Constructor for the ReportType object
     */
    private ReportType() { }


    private static Logger logger = Logger.getLogger("zt.cms.cm.report.ReportType");


    /**
     *  Gets the reportTemplateByType attribute of the ReportType class
     *
     *@param  reportType  Description of the Parameter
     *@return             The reportTemplateByType value
     */
    public static Report getReportTemplateByType(String reportType) {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        String typeStr = "select * from cmreport where reporttype='" + reportType + "'";
        logger.info("Query Report sql :" + typeStr);
        RecordSet rs = con.executeQuery(typeStr);
        if (rs.next()) {
            Report report = new Report(rs.getString("REPORTTYPE"), DBUtil.fromDB(rs.getString("REPORTNAME")));
            String itemsStr = "select itemno,itemname,printno from cmreportitem where reporttype = '" + reportType + "'";
            logger.info("Query Item sql : " + itemsStr);
            RecordSet itemRs = con.executeQuery(itemsStr);
            while (itemRs.next()) {
                int itemNo = itemRs.getInt("itemno");
                String itemName = DBUtil.fromDB(itemRs.getString("itemname"));
                int printNo = itemRs.getInt("printno");

                String limitsString = rs.getString("limit");
                String limits[] = limitsString.split("\r\n");
                report.setLimits(limits);

                logger.info("Add item of :" + itemNo + "|" + itemName + "|" + printNo);
                report.addItem(itemNo, itemName, printNo);
            }
            ConnectionManager.getInstance().releaseConnection(con);
            return report;
        } else {
            ConnectionManager.getInstance().releaseConnection(con);
            logger.warning("The report is null , ReportType no is : " + reportType);
            return null;
        }
    }
}
