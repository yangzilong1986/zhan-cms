package zt.cms.report;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

import zt.cms.pub.SCBranch;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;


public class ReportSql {
    //处理了基本表在内(除有关利息的表)
    public static String getSql(String strRptNo, String upbrh, String month) {
        int level = -1;
        String condition, sql;
        String strBrhid;
        int rptNo = Integer.parseInt(strRptNo.substring(2));
        int iTenTh = 10000;

        ScbranchLevel sbl = new ScbranchLevel(upbrh);
        level = sbl.intBrhLevel;
        //System.out.println("=========upbrh="+upbrh+"=========level="+level+"=================month="+month);

        if (level == 1) {
            strBrhid = "s.upbrh='";
            iTenTh = 10000;
        } else if (level == 2) {
            strBrhid = "s.upbrh='";
            iTenTh = 1000;
        } else if (level == 3) {
            String yingyebrhid = upbrh;
            String subbrhall = SCBranch.getAllSubBrh1(yingyebrhid);
            if (subbrhall != null && subbrhall.trim().length() <= 0) {
                subbrhall = null;
            }
            if (yingyebrhid != null && level == 3 && subbrhall == null) {
                strBrhid = "s.brhid='";
                iTenTh = 100;
            } else {
                strBrhid = "s.upbrh='";
                iTenTh = 100;
            }
        } else {
            strBrhid = "s.brhid='";
            iTenTh = 100;
        }

        switch (rptNo) {
            case 2040:
                if (month.equals("00"))
                    condition =
                            " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid";
                else
                    condition =
                            " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid and a.month='" +
                                    month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F20401' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F20402' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F20402' then a.itmdata1 else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F20402' then a.itmdata2 else 0 end) as f4,"

                        + " max(case a.tblitmno when 'F20403' then a.itmdata else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F20403' then a.itmdata1 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F20403' then a.itmdata2 else 0 end) as f8,"

                        + " max(case a.tblitmno when 'F20404' then a.itmdata else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F20404' then a.itmdata1 else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F20404' then a.itmdata2 else 0 end) as f12,"

                        + " max(case a.tblitmno when 'F20405' then a.itmdata else 0 end) as f14,"
                        + " max(case a.tblitmno when 'F20405' then a.itmdata1 else 0 end) as f15,"
                        + " max(case a.tblitmno when 'F20405' then a.itmdata2 else 0 end) as f16,"

                        + " max(case a.tblitmno when 'F20406' then a.itmdata else 0 end) as f18,"
                        + " max(case a.tblitmno when 'F20406' then a.itmdata1 else 0 end) as f19,"
                        + " max(case a.tblitmno when 'F20406' then a.itmdata2 else 0 end) as f20,"
                        + " max(case a.tblitmno when 'F20401' then a.itmdata1 else 0 end) as f21,"
                        + " max(case a.tblitmno when 'F20401' then a.itmdata2 else 0 end) as f22"
                        + condition
                        + " group by s.brhid order by s.brhid ";
                break;
            case 2060:
                if (month.equals("00"))
                    condition =
                            " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid";
                else
                    condition =
                            " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid and a.month='" +
                                    month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F20601' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F20601' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F20601' then a.itmdata2 else 0 end) as f3,"

                        + " max(case a.tblitmno when 'F20602' then a.itmdata else 0 end) as f4,"
                        + " max(case a.tblitmno when 'F20602' then a.itmdata1 else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F20602' then a.itmdata2 else 0 end) as f6"
                        + condition
                        + " group by s.brhid order by s.brhid ";
                break;
            case 2070:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtblaccdata a where " + strBrhid + upbrh +
                            "'   and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblaccmondata a where " + strBrhid + upbrh +
                            "'   and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblaccno when 'F0090' then a.monbal else 0 end) as f1,"
                        + " max(case a.tblaccno when 'F0090' then a.lastmonbal else 0 end) as f2,"
                        + " max(case a.tblaccno when 'F0090' then a.lastyearbal else 0 end) as f3,"
                        + " max(case a.tblaccno when 'F0092' then a.monbal else 0 end) as f5,"
                        + " max(case a.tblaccno when 'F0091' then a.monbal else 0 end) as f6"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 2080:
                if (month.equals("00"))
                    condition =
                            " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid";
                else
                    condition =
                            " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid and a.month='" +
                                    month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F20801' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F20801' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F20801' then a.itmdata2 else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F20802' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F20802' then a.itmdata1 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F20802' then a.itmdata2 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F20803' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F20803' then a.itmdata1 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F20803' then a.itmdata2 else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F20804' then a.itmdata else 0 end) as f13"
                        + condition
                        + " group by s.brhid order by s.brhid ";
                break;

            case 2120:
                if (month.equals("00"))
                    condition =
                            " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid";
                else
                    condition =
                            " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                                    "' and s.brhid=a.brhid and a.month='" +
                                    month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21201' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21201' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21202' then a.itmdata else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F21202' then a.itmdata1 else 0 end) as f4,"
                        + " max(case a.tblitmno when 'F21202' then a.itmdata2 else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21203' then a.itmdata else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F21203' then a.itmdata1 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F21203' then a.itmdata2 else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F21204' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F21204' then a.itmdata1 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F21204' then a.itmdata2 else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F21205' then a.itmdata else 0 end) as f12,"
                        + " max(case a.tblitmno when 'F21205' then a.itmdata1 else 0 end) as f13,"
                        + " max(case a.tblitmno when 'F21205' then a.itmdata2 else 0 end) as f14,"
                        + " max(case a.tblitmno when 'F21201' then a.itmdata2 else 0 end) as f15"
                        + condition
                        + " group by s.brhid order by s.brhid ";
                break;
            case 2130:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21301' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21301' then a.itmdata2 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21302' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21302' then a.itmdata2 else 0 end) as f6"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 2140:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21401' then a.itmcount*" + iTenTh + " else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21401' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21401' then a.itmcount1*" + iTenTh + " else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F21401' then a.itmdata1 else 0 end) as f4,"
                        + " max(case a.tblitmno when 'F21401' then a.itmcount2*" + iTenTh + " else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21401' then a.itmdata2 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F21402' then a.itmcount*" + iTenTh + " else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F21402' then a.itmdata else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F21402' then a.itmcount1*" + iTenTh + " else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F21402' then a.itmdata1 else 0 end) as f10"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 2150:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21501' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21501' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21502' then a.itmdata else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F21502' then a.itmdata1 else 0 end) as f4,"
                        + " max(case a.tblitmno when 'F21502' then a.itmdata2 else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21503' then a.itmdata else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F21503' then a.itmdata1 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F21503' then a.itmdata2 else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F21504' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F21504' then a.itmdata1 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F21504' then a.itmdata2 else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F21505' then a.itmdata else 0 end) as f12,"
                        + " max(case a.tblitmno when 'F21505' then a.itmdata1 else 0 end) as f13,"
                        + " max(case a.tblitmno when 'F21505' then a.itmdata2 else 0 end) as f14,"
                        + " max(case a.tblitmno when 'F21501' then a.itmdata2 else 0 end) as f15"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 2160:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21601' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21601' then a.itmdata2 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21602' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21602' then a.itmdata1 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F21602' then a.itmdata2 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F21604' then a.itmdata else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F21603' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F21603' then a.itmdata2 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F21604' then a.itmdata1 else 0 end) as f13"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 2170:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21701' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21701' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21702' then a.itmdata else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F21702' then a.itmdata1 else 0 end) as f4,"
                        + " max(case a.tblitmno when 'F21702' then a.itmdata2 else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21703' then a.itmdata else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F21703' then a.itmdata1 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F21703' then a.itmdata2 else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F21704' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F21704' then a.itmdata1 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F21704' then a.itmdata2 else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F21705' then a.itmdata else 0 end) as f12,"
                        + " max(case a.tblitmno when 'F21705' then a.itmdata1 else 0 end) as f13,"
                        + " max(case a.tblitmno when 'F21705' then a.itmdata2 else 0 end) as f14,"
                        + " max(case a.tblitmno when 'F21701' then a.itmdata2 else 0 end) as f15"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 2180:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F21801' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F21801' then a.itmdata2 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F21802' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F21802' then a.itmdata1 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F21802' then a.itmdata2 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F21804' then a.itmdata else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F21803' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F21803' then a.itmdata2 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F21804' then a.itmdata2 else 0 end) as f13"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 3010:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F30101' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F30101' then a.itmdata1 else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F30101' then a.itmdata2 else 0 end) as f4,"

                        + " max(case a.tblitmno when 'F30102' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F30102' then a.itmdata1 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F30102' then a.itmdata2 else 0 end) as f7,"

                        + " max(case a.tblitmno when 'F30103' then a.itmdata else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F30103' then a.itmdata1 else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F30103' then a.itmdata2 else 0 end) as f10,"

                        + " max(case a.tblitmno when 'F30104' then a.itmdata1 else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F30104' then a.itmdata2 else 0 end) as f12"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 3050:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F30501' then a.itmcount*" + iTenTh + " else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F30501' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F30501' then a.itmcount1*" + iTenTh + " else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F30501' then a.itmdata1 else 0 end) as f4,"

                        + " max(case a.tblitmno when 'F30502' then a.itmcount*" + iTenTh + " else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F30502' then a.itmdata else 0 end) as f6,"

                        + " max(case a.tblitmno when 'F30503' then a.itmcount*" + iTenTh + " else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F30503' then a.itmdata else 0 end) as f8,"

                        + " max(case a.tblitmno when 'F30504' then a.itmcount*" + iTenTh + " else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F30504' then a.itmdata else 0 end) as f10,"

                        + " max(case a.tblitmno when 'F30505' then a.itmcount*" + iTenTh + " else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F30505' then a.itmdata else 0 end) as f12,"
                        + " max(case a.tblitmno when 'F30505' then a.itmcount1*" + iTenTh + " else 0 end) as f13,"
                        + " max(case a.tblitmno when 'F30505' then a.itmdata1 else 0 end) as f14,"

                        + " max(case a.tblitmno when 'F30506' then a.itmcount*" + iTenTh + " else 0 end) as f15,"
                        + " max(case a.tblitmno when 'F30506' then a.itmdata else 0 end) as f16,"
                        + " max(case a.tblitmno when 'F30506' then a.itmcount1*" + iTenTh + " else 0 end) as f17,"
                        + " max(case a.tblitmno when 'F30506' then a.itmdata1 else 0 end) as f18,"
                        + " max(case a.tblitmno when 'F30506' then a.itmdata2 else 0 end) as f19"

                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 3060:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F30601' then a.itmcount*" + iTenTh + " else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F30601' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F30601' then a.itmcount1*" + iTenTh + " else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F30601' then a.itmdata1 else 0 end) as f4,"

                        + " max(case a.tblitmno when 'F30602' then a.itmcount*" + iTenTh + " else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F30602' then a.itmdata else 0 end) as f6,"

                        + " max(case a.tblitmno when 'F30603' then a.itmcount*" + iTenTh + " else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F30603' then a.itmdata else 0 end) as f8,"

                        + " max(case a.tblitmno when 'F30604' then a.itmcount*" + iTenTh + " else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F30604' then a.itmdata else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F30604' then a.itmcount1*" + iTenTh + " else 0 end) as f11,"
                        + " max(case a.tblitmno when 'F30604' then a.itmdata1 else 0 end) as f12,"

                        + " max(case a.tblitmno when 'F30605' then a.itmcount*" + iTenTh + " else 0 end) as f13,"
                        + " max(case a.tblitmno when 'F30605' then a.itmdata else 0 end) as f14,"
                        + " max(case a.tblitmno when 'F30605' then a.itmcount1*" + iTenTh + " else 0 end) as f15,"
                        + " max(case a.tblitmno when 'F30605' then a.itmdata1 else 0 end) as f16,"

                        + " max(case a.tblitmno when 'F30606' then a.itmcount*" + iTenTh + " else 0 end) as f17,"
                        + " max(case a.tblitmno when 'F30606' then a.itmdata else 0 end) as f18,"
                        + " max(case a.tblitmno when 'F30606' then a.itmcount1*" + iTenTh + " else 0 end) as f19,"
                        + " max(case a.tblitmno when 'F30606' then a.itmdata1 else 0 end) as f20,"

                        + " max(case a.tblitmno when 'F30607' then a.itmcount*" + iTenTh + " else 0 end) as f21,"
                        + " max(case a.tblitmno when 'F30607' then a.itmdata else 0 end) as f22,"
                        + " max(case a.tblitmno when 'F30607' then a.itmcount1*" + iTenTh + " else 0 end) as f23,"
                        + " max(case a.tblitmno when 'F30607' then a.itmdata1 else 0 end) as f24"

                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 3070:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F30701' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F30701' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F30701' then a.itmdata2 else 0 end) as f3,"

                        + " max(case a.tblitmno when 'F30702' then a.itmdata else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F30702' then a.itmdata1 else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F30702' then a.itmdata2 else 0 end) as f11,"

                        + " max(case a.tblitmno when 'F30703' then a.itmdata else 0 end) as f8,"
                        + " max(case a.tblitmno when 'F30703' then a.itmdata1 else 0 end) as f10,"
                        + " max(case a.tblitmno when 'F30703' then a.itmdata2 else 0 end) as f12,"

                        + " max(case a.tblitmno when 'F30704' then a.itmdata else 0 end) as f13,"
                        + " max(case a.tblitmno when 'F30704' then a.itmdata1 else 0 end) as f14,"

                        + " max(case a.tblitmno when 'F30705' then a.itmdata else 0 end) as f15,"
                        + " max(case a.tblitmno when 'F30705' then a.itmdata1 else 0 end) as f16"

                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 5030:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F50301' then a.itmdata else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F50301' then a.itmdata1 else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F50301' then a.itmdata2 else 0 end) as f3,"

                        + " max(case a.tblitmno when 'F50302' then a.itmdata else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F50302' then a.itmdata1 else 0 end) as f7,"
                        + " max(case a.tblitmno when 'F50302' then a.itmdata2 else 0 end) as f8,"

                        + " max(case a.tblitmno when 'F50303' then a.itmdata else 0 end) as f9,"
                        + " max(case a.tblitmno when 'F50303' then a.itmcount*" + iTenTh + " else 0 end) as f10"
                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 5040:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F50401' then a.itmcount*" + iTenTh + " else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F50401' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F50401' then a.itmdata2 else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F50401' then a.itmdata1 else 0 end) as f4,"

                        + " max(case a.tblitmno when 'F50402' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F50402' then a.itmdata1 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F50402' then a.itmdata2 else 0 end) as f7"

                        + condition
                        + " group by s.brhid order by s.brhid";
                break;
            case 5050:
                if (month.equals("00"))
                    condition = " from scbranch s,rqtbldata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid";
                else
                    condition = " from scbranch s,rqtblmondata a where " + strBrhid + upbrh +
                            "' and s.brhid=a.brhid and a.month='" +
                            month + "'";
                sql = "select s.brhid,"
                        + " max(case a.tblitmno when 'F50501' then a.itmcount*" + iTenTh + " else 0 end) as f1,"
                        + " max(case a.tblitmno when 'F50501' then a.itmdata else 0 end) as f2,"
                        + " max(case a.tblitmno when 'F50501' then a.itmdata2 else 0 end) as f3,"
                        + " max(case a.tblitmno when 'F50501' then a.itmdata1 else 0 end) as f4,"

                        + " max(case a.tblitmno when 'F50502' then a.itmdata else 0 end) as f5,"
                        + " max(case a.tblitmno when 'F50502' then a.itmdata1 else 0 end) as f6,"
                        + " max(case a.tblitmno when 'F50502' then a.itmdata2 else 0 end) as f7"

                        + condition
                        + " group by s.brhid order by s.brhid";
                break;

            default:
                sql = "";
        }
        return sql;
    }

    //获得有关利息的表的sql语句
    public static String getRateSql(String strRptNo, String upbrh, String month) {
        String strSql, strCondition;
        int intRptNo = Integer.parseInt(strRptNo.substring(2));

        switch (intRptNo) {
            case 6010:
                if (month.equals("00"))
                    strCondition = " from rqratedata where brhid='" + upbrh + "'";
                else
                    strCondition = "from rqratemondata where brhid='" + upbrh + "' and month='" + month + "'";
                strSql = "select itmname ,itmdata as F1,itmdata1 as F2,itmdata2 as F3,"
                        + "itmdata3 as F4,itmdata4 as F5,itmdata5 as F6"
                        + strCondition + "  and tblitmno like 'F601%'";
                break;
            case 6020:
                if (month.equals("00"))
                    strCondition = " from rqratedata where brhid='" + upbrh + "'";
                else
                    strCondition = " from rqratemondata where brhid='" + upbrh + "' and month='" + month + "'";
                strSql = " select "
                        + "max(case tblitmno when 'F60201' then itmdata else 0 end) as F1,"
                        + "max(case tblitmno when 'F60201' then itmdata1 else 0 end) as F2,"
                        + "max(case tblitmno when 'F60201' then itmdata2 else 0 end) as F3,"
                        + "max(case tblitmno when 'F60201' then itmdata3 else 0 end) as F4,"
                        + "max(case tblitmno when 'F60201' then itmdata4 else 0 end) as F5,"
                        + "max(case tblitmno when 'F60201' then itmdata5 else 0 end) as F6,"
                        + "max(case tblitmno when 'F60202' then itmdata else 0 end) as F7,"
                        + "max(case tblitmno when 'F60202' then itmdata1 else 0 end) as F8,"
                        + "max(case tblitmno when 'F60202' then itmdata2 else 0 end) as F9,"
                        + "max(case tblitmno when 'F60202' then itmdata3 else 0 end) as F10,"
                        + "max(case tblitmno when 'F60202' then itmdata4 else 0 end) as F11,"
                        + "max(case tblitmno when 'F60202' then itmdata5 else 0 end) as F12,"
                        + "max(case tblitmno when 'F60203' then itmdata else 0 end) as F13,"
                        + "max(case tblitmno when 'F60203' then itmdata1 else 0 end) as F14,"
                        + "max(case tblitmno when 'F60203' then itmdata2 else 0 end) as F15,"
                        + "max(case tblitmno when 'F60203' then itmdata3 else 0 end) as F16,"
                        + "max(case tblitmno when 'F60203' then itmdata4 else 0 end) as F17,"
                        + "max(case tblitmno when 'F60203' then itmdata5 else 0 end) as F18,"
                        + "max(case tblitmno when 'F60204' then itmdata else 0 end) as F19,"
                        + "max(case tblitmno when 'F60204' then itmdata1 else 0 end) as F20,"
                        + "max(case tblitmno when 'F60204' then itmdata2 else 0 end) as F21,"
                        + "max(case tblitmno when 'F60204' then itmdata3 else 0 end) as F22,"
                        + "max(case tblitmno when 'F60204' then itmdata4 else 0 end) as F23,"
                        + "max(case tblitmno when 'F60204' then itmdata5 else 0 end) as F24,"
                        + "max(case tblitmno when 'F60205' then itmdata else 0 end) as F25,"
                        + "max(case tblitmno when 'F60205' then itmdata1 else 0 end) as F26,"
                        + "max(case tblitmno when 'F60205' then itmdata2 else 0 end) as F27,"
                        + "max(case tblitmno when 'F60205' then itmdata3 else 0 end) as F28,"
                        + "max(case tblitmno when 'F60205' then itmdata4 else 0 end) as F29,"
                        + "max(case tblitmno when 'F60205' then itmdata5 else 0 end) as F30,"
                        + "max(case tblitmno when 'F60206' then itmdata else 0 end) as F31,"
                        + "max(case tblitmno when 'F60206' then itmdata1 else 0 end) as F32,"
                        + "max(case tblitmno when 'F60206' then itmdata2 else 0 end) as F33,"
                        + "max(case tblitmno when 'F60206' then itmdata3 else 0 end) as F34,"
                        + "max(case tblitmno when 'F60206' then itmdata4 else 0 end) as F35,"
                        + "max(case tblitmno when 'F60206' then itmdata5 else 0 end) as F36,"
                        + "max(case tblitmno when 'F60207' then itmdata else 0 end) as F37,"
                        + "max(case tblitmno when 'F60207' then itmdata1 else 0 end) as F38,"
                        + "max(case tblitmno when 'F60207' then itmdata2 else 0 end) as F39,"
                        + "max(case tblitmno when 'F60207' then itmdata3 else 0 end) as F40,"
                        + "max(case tblitmno when 'F60207' then itmdata4 else 0 end) as F41,"
                        + "max(case tblitmno when 'F60207' then itmdata5 else 0 end) as F42,"
                        + "max(case tblitmno when 'F60208' then itmdata else 0 end) as F43,"
                        + "max(case tblitmno when 'F60208' then itmdata1 else 0 end) as F44,"
                        + "max(case tblitmno when 'F60208' then itmdata2 else 0 end) as F45,"
                        + "max(case tblitmno when 'F60208' then itmdata3 else 0 end) as F46,"
                        + "max(case tblitmno when 'F60208' then itmdata4 else 0 end) as F47,"
                        + "max(case tblitmno when 'F60208' then itmdata5 else 0 end) as F48"
                        + strCondition;
                break;
            case 6030:
                if (month.equals("00"))
                    strCondition = " from rqratedata where brhid='" + upbrh + "'";
                else
                    strCondition = " from rqratemondata where brhid='" + upbrh + "' and month='" + month + "'";
                strSql = "select itmname,itmdata as F1,itmdata1 as F2,itmdata2 as F3,"
                        + "itmdata3 as F4,itmdata4 as F5,itmdata5 as F6,itmdata6 as F7,itmdata7 as F8"
                        + strCondition + " and tblitmno like 'F603%'";
                break;
            default:
                strSql = "";
        }
        return strSql;
    }


    //获得有关五级分类的表的sql语句    -- lj added in 20050710
    public static String getFCSql(String strRptNo, String upbrh, String date) {
        String strSql;
        int intRptNo = Integer.parseInt(strRptNo.substring(2));
        //if (intRptNo > 7100 && intRptNo < 7190 && date.equals(SystemDate.getYesterday("-")))
        if (intRptNo > 7100 && intRptNo < 7190 )                     //lj chenged in 2007-05-13
            date = "2000-01-01";
        //if(intRptNo==7210 ||intRptNo==7220||intRptNo==7230 ||intRptNo==7240)
        	  //date = "2000-01-01";
        String sData = SystemDate.getDateByComma(date, "-");
        
        int level = -1;
        String condition = "";
        String strBrhid = "s.brhid";
        ScbranchLevel sbl = new ScbranchLevel(upbrh);
        level = sbl.intBrhLevel;

        if (level == 1) {
            strBrhid = "s.bnkid";
            condition = "s.brhid=F0.brhid";
        } else if (level == 2) {
            strBrhid = "s.chgid";
            condition = "s.bnkid = '" + upbrh + "' and s.brhid=F0.brhid";
        } else if (level == 3) {
            strBrhid = "s.brhid";
            String yingyebrhid = upbrh;
            String subbrhall = SCBranch.getAllSubBrh1(yingyebrhid);
            if (subbrhall != null && subbrhall.trim().length() <= 0) {
                subbrhall = null;
            }
            if (yingyebrhid != null && level == 3 && subbrhall == null) {
                condition = "s.brhid = '" + upbrh + "' and s.brhid=F0.brhid";
            } else {
                condition = "s.upbrh = '" + upbrh + "' and s.brhid=F0.brhid";
            }
        } else {
            strBrhid = "s.brhid";
            condition = "s.brhid = '" + upbrh + "' and s.brhid=F0.brhid";
        }
        switch (intRptNo) {
            case 7010:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,sum(AMT1) AMT1 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=1 and F0.dt in " +
                        "('" + SystemDate.getDateByComma(date, "-") +
                        "','" + SystemDate.getLastMonSDate(date, "-") +
                        "','" + SystemDate.getLastYearDate(date, "-") +
                        "','" + SystemDate.getLastYearSDate(date, "-") + "') " +
                        "group by " + strBrhid + ",dt,dim1)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=1) D01," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=1) D03," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=2) D05," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=2) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=2) D07," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=2) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=3) D09," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=3) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=3) D11," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=3) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=4) D13," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=4) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=4) D15," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=4) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=5) D17," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=5) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=5) D19," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=5) D20 " +
                        "from  F ";
                break;
            case 7020:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=2 and F0.dt='" + sData + "'" +
                        "group by " + strBrhid + ",dt,dim1,dim2)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=1) D01," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=2) D03," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=2) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=3) D05," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=3) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=4) D07," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=4) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=1) D09," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=1) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=2) D11," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=2) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=3) D13," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=3) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=4) D15," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=4) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=1) D17," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=1) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=2) D19," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=2) D20," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=3) D21," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=3) D22," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=4) D23," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=4) D24," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=1) D25," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=1) D26," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=2) D27," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=2) D28," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=3) D29," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=3) D30," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=4) D31," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=4) D32," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=1) D33," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=1) D34," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=2) D35," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=2) D36," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=3) D37," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=3) D38," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=4) D39," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=4) D40 " +
                        "from  F";
                break;
                
            case 7040:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=2 and F0.dt='" + sData + "' " +
                        "group by " + strBrhid + ",dt,dim1,dim2)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=1) D01," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=1) D03," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=1) D05," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=1) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=1) D07," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=1) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=1) D09," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=1) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=2) D11," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=2) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=2) D13," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=2) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=2) D15," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=2) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=2) D17," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=2) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=2) D19," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=2) D20," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=3) D21," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=3) D22," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=3) D23," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=3) D24," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=3) D25," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=3) D26," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=3) D27," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=3) D28," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=3) D29," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=3) D30," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=4) D31," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=4) D32," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=4) D33," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=4) D34," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=4) D35," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=4) D36," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=4) D37," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=4) D38," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=4) D39," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=4) D40 " +
                        "from  F";
                break;
            case 7050:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,sum(AMT1) AMT1 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=1 and F0.dt in " +
                        "('" + SystemDate.getDateByComma(date, "-") +
                        "','" + SystemDate.getLastMonSDate(date, "-") +
                        "','" + SystemDate.getLastYearDate(date, "-") +
                        "','" + SystemDate.getLastYearSDate(date, "-") + "') " +
                        "group by " + strBrhid + ",dt,dim1)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=1) D01," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=1) D03," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=2) D05," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=2) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=2) D07," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=2) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=3) D09," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=3) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=3) D11," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=3) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=4) D13," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=4) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=4) D15," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=4) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=5) D17," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=5) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=5) D19," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=5) D20," +
                        "(select FACTOR from fcfactor where FCCLASS=1) D21," +
                        "(select FACTOR from fcfactor where FCCLASS=2) D22," +
                        "(select FACTOR from fcfactor where FCCLASS=3) D23," +
                        "(select FACTOR from fcfactor where FCCLASS=4) D24," +
                        "(select FACTOR from fcfactor where FCCLASS=5) D25 " +
                        "from  F ";
                break;
            case 7060:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,case when dim2 is null then 210 else dim2 end dim2," +
                        "sum(AMT1) AMT1,ftype from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype in (3,5) and F0.dt='" + sData + "' " +
                        "and (dim2 in (100,210,220,230,13,14) or dim2 is null) " +
                        "group by " + strBrhid + ",dt,dim1,dim2,ftype)," +
                        "T1 as (select brhid,dt,dim1,dim2,sum(AMT1) AMT1 from T where ftype=3 group by brhid,dt,dim1,dim2)," +
                        "T2 as (select brhid,sum(AMT1) AMT1 from T where ftype=5 and dim2=13 group by brhid)," +
                        "F as (select distinct brhid from T1) " +

                        "select brhid," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=100) D01," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=100) D02," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=100) D03," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=100) D04," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=100) D05," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=210) D06," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=210) D07," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=210) D08," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=210) D09," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=210) D10," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=220) D11," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=220) D12," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=220) D13," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=220) D14," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=220) D15," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=230) D16," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=230) D17," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=230) D18," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=230) D19," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=230) D20," +
                        "(select AMT1 from T2 where brhid=F.brhid) D21 " +
                        "from  F";
                break;
            case 7070:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=4 and F0.dt='" + sData + "' " +
                        "group by " + strBrhid + ",dt,dim1,dim2)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=1) D01," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=1) D03," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=1) D05," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=1) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=1) D07," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=1) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=1) D09," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=1) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=2) D11," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=2) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=2) D13," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=2) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=2) D15," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=2) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=2) D17," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=2) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=2) D19," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=2) D20," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=3) D21," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=3) D22," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=3) D23," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=3) D24," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=3) D25," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=3) D26," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=3) D27," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=3) D28," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=3) D29," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=3) D30," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=4) D31," +//郑鑫 修改过
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=4) D32," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=4) D33," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=4) D34," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=4) D35," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=4) D36," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=4) D37," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=4) D38," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=4) D39," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=4) D40 " +
                        "from  F";
                break;
            case 7080:
                strSql = "WITH " +
                        "T1 as (select sum(amt1) AT1,sum(amt2) AT2,dim2,dim1 from scbranch s,fcdata F0 where " + condition + " and F0.dt='" + sData + "' and F0.ftype=5 group by dim2,dim1)," +
                        "T2 as (select enudt,enutp from PTENUMINFODETL where enuid='BMType' order by enutp)," +
                        "T3 as (select enudt,enutp,AT1,AT2,dim1,dim2 from T2 left outer join T1 on (T1.dim2=T2.enutp))," +
                        "T4 as (select enudt,enutp," +
                        "(select AT1 from T3 where dim1=1 and enutp=T2.enutp) D01," +
                        "(select AT2 from T3 where dim1=1 and enutp=T2.enutp) D02," +
                        "(select AT1 from T3 where dim1=2 and enutp=T2.enutp) D03," +
                        "(select AT2 from T3 where dim1=2 and enutp=T2.enutp) D04," +
                        "(select AT1 from T3 where dim1=3 and enutp=T2.enutp) D05," +
                        "(select AT2 from T3 where dim1=3 and enutp=T2.enutp) D06," +
                        "(select AT1 from T3 where dim1=4 and enutp=T2.enutp) D07," +
                        "(select AT2 from T3 where dim1=4 and enutp=T2.enutp) D08," +
                        "(select AT1 from T3 where dim1=5 and enutp=T2.enutp) D09," +
                        "(select AT2 from T3 where dim1=5 and enutp=T2.enutp) D10 from T2) " +

                        "select enudt,D01,D02,D03,D04,D05,D06,D07,D08,D09,D10 from T4 order by enutp";
                break;
            case 7090:
                strSql = "WITH " +
                        "T1 as (select sum(amt1) AT1,sum(amt2) AT2,dim2,dim1 from scbranch s,fcdata F0 where " + condition + " and F0.dt='" + sData + "' and F0.ftype=6 group by dim2,dim1)," +
                        "T2 as (select enudt,enutp from PTENUMINFODETL where enuid='BMType' order by enutp)," +
                        "T3 as (select enudt,enutp,AT1,AT2,dim1,dim2 from T2 left outer join T1 on (T1.dim2=T2.enutp))," +
                        "T4 as (select enudt,enutp," +
                        "(select AT1 from T3 where dim1=1 and enutp=T2.enutp) D01," +
                        "(select AT2 from T3 where dim1=1 and enutp=T2.enutp) D02," +
                        "(select AT1 from T3 where dim1=2 and enutp=T2.enutp) D03," +
                        "(select AT2 from T3 where dim1=2 and enutp=T2.enutp) D04," +
                        "(select AT1 from T3 where dim1=3 and enutp=T2.enutp) D05," +
                        "(select AT2 from T3 where dim1=3 and enutp=T2.enutp) D06," +
                        "(select AT1 from T3 where dim1=4 and enutp=T2.enutp) D07," +
                        "(select AT2 from T3 where dim1=4 and enutp=T2.enutp) D08," +
                        "(select AT1 from T3 where dim1=5 and enutp=T2.enutp) D09," +
                        "(select AT2 from T3 where dim1=5 and enutp=T2.enutp) D10 from T2) " +

                        "select enudt,D01,D02,D03,D04,D05,D06,D07,D08,D09,D10 from T4 order by enutp";
                break;
            case 7100:
                strSql = "select clientname SNAME," +
                        "(select enudt from ptenuminfodetl where F0.LoanType3 = enutp and enuid='LoanType3') D02," +
                        "bal D03,paydate D04,duedate D05," +
                        "pastduedays D06," +
                        "(select enudt from ptenuminfodetl where F0.creditclass = enutp and enuid='CreditClass') D07," +
                        "case when fcauto=1 then 1 else 0 end D08," +
                        "case when fcauto=2 then 1 else 0 end D09," +
                        "case when fcauto=3 then 1 else 0 end D10," +
                        "case when fcauto=4 then 1 else 0 end D11," +
                        "case when fcauto=5 then 1 else 0 end D12," +
                        "(select username from scuser where F0.firstresp = loginname) D13 " +
                        "from scbranch s,fcmain F0 " +
                        "where " + condition + " and F0.createdate='" + sData + "' " +
                        "and F0.FCType = 1 " +
                        "order by firstresp,duedate";
                break;

            case 7110:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,sum(AMT1) AMT1 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=7 and F0.dt in " +
                        "('" + SystemDate.getDateByComma(date, "-") +
                        "','" + SystemDate.getLastMonSDate(date, "-") +
                        "','" + SystemDate.getLastYearDate(date, "-") +
                        "','" + SystemDate.getLastYearSDate(date, "-") + "') " +
                        "group by " + strBrhid + ",dt,dim1)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=1) D01," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=1) D03," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=2) D05," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=2) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=2) D07," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=2) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=3) D09," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=3) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=3) D11," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=3) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=4) D13," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=4) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=4) D15," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=4) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=5) D17," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=5) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=5) D19," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=5) D20 " +
                        "from  F ";
                break;
            case 7120:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=8 and F0.dt='" + sData + "'" +
                        "group by " + strBrhid + ",dt,dim1,dim2)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=1) D01," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=2) D03," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=2) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=3) D05," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=3) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=4) D07," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=4) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=1) D09," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=1) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=2) D11," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=2) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=3) D13," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=3) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=4) D15," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=4) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=1) D17," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=1) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=2) D19," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=2) D20," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=3) D21," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=3) D22," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=4) D23," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=4) D24," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=1) D25," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=1) D26," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=2) D27," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=2) D28," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=3) D29," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=3) D30," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=4) D31," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=4) D32," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=1) D33," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=1) D34," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=2) D35," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=2) D36," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=3) D37," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=3) D38," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=4) D39," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=4) D40 " +
                        "from  F";
                break;
            case 7130:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=8 and F0.dt='" + sData + "' " +
                        "group by " + strBrhid + ",dt,dim1,dim2)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=1) D01," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=1) D03," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=1) D05," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=1) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=1) D07," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=1) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=1) D09," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=1) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=2) D11," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=2) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=2) D13," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=2) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=2) D15," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=2) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=2) D17," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=2) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=2) D19," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=2) D20," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=3) D21," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=3) D22," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=3) D23," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=3) D24," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=3) D25," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=3) D26," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=3) D27," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=3) D28," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=3) D29," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=3) D30," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=4) D31," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=4) D32," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=4) D33," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=4) D34," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=4) D35," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=4) D36," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=4) D37," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=4) D38," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=4) D39," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=4) D40 " +
                        "from  F";
                break;
            case 7140:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,sum(AMT1) AMT1 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=7 and F0.dt in " +
                        "('" + SystemDate.getDateByComma(date, "-") +
                        "','" + SystemDate.getLastMonSDate(date, "-") +
                        "','" + SystemDate.getLastYearDate(date, "-") +
                        "','" + SystemDate.getLastYearSDate(date, "-") + "') " +
                        "group by " + strBrhid + ",dt,dim1)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=1) D01," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=1) D03," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=2) D05," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=2) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=2) D07," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=2) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=3) D09," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=3) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=3) D11," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=3) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=4) D13," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=4) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=4) D15," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=4) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + sData + "' and dim1=5) D17," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastMonSDate(date, "-") + "' and dim1=5) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearDate(date, "-") + "' and dim1=5) D19," +
                        "(select AMT1 from T where brhid=F.brhid and dt='" + SystemDate.getLastYearSDate(date, "-") + "' and dim1=5) D20," +
                        "(select FACTOR from fcfactor where FCCLASS=1) D21," +
                        "(select FACTOR from fcfactor where FCCLASS=2) D22," +
                        "(select FACTOR from fcfactor where FCCLASS=3) D23," +
                        "(select FACTOR from fcfactor where FCCLASS=4) D24," +
                        "(select FACTOR from fcfactor where FCCLASS=5) D25 " +
                        "from  F ";
                break;
            case 7150:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,case when dim2 is null then 210 else dim2 end dim2," +
                        "sum(AMT1) AMT1,ftype from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype in (9,11) and F0.dt='" + sData + "' " +
                        "and (dim2 in (100,210,220,230,13,14) or dim2 is null) " +
                        "group by " + strBrhid + ",dt,dim1,dim2,ftype)," +
                        "T1 as (select brhid,dt,dim1,dim2,sum(AMT1) AMT1 from T where ftype=9 group by brhid,dt,dim1,dim2)," +
                        "T2 as (select brhid,sum(AMT1) AMT1 from T where ftype=11 and dim2=13 group by brhid)," +
                        "F as (select distinct brhid from T1) " +

                        "select brhid," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=100) D01," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=100) D02," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=100) D03," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=100) D04," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=100) D05," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=210) D06," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=210) D07," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=210) D08," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=210) D09," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=210) D10," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=220) D11," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=220) D12," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=220) D13," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=220) D14," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=220) D15," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=1 and dim2=230) D16," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=2 and dim2=230) D17," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=3 and dim2=230) D18," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=4 and dim2=230) D19," +
                        "(select AMT1 from T1 where brhid=F.brhid and dim1=5 and dim2=230) D20," +
                        "(select AMT1 from T2 where brhid=F.brhid) D21 " +
                        "from  F";
                break;
            case 7160:
                strSql = "with " +
                        "T as (select " + strBrhid + " brhid,dt,dim1,dim2,sum(AMT1) AMT1,sum(AMT2) AMT2 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=10 and F0.dt='" + sData + "' " +
                        "group by " + strBrhid + ",dt,dim1,dim2)," +
                        "F as (select distinct brhid from T) " +

                        "select brhid," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=1) D01," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=1) D02," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=1) D03," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=1) D04," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=1) D05," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=1) D06," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=1) D07," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=1) D08," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=1) D09," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=1) D10," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=2) D11," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=2) D12," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=2) D13," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=2) D14," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=2) D15," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=2) D16," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=2) D17," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=2) D18," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=2) D19," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=2) D20," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=1 and dim2=3) D21," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=1 and dim2=3) D22," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=2 and dim2=3) D23," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=2 and dim2=3) D24," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=3 and dim2=3) D25," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=3 and dim2=3) D26," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=4 and dim2=3) D27," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=4 and dim2=3) D28," +
                        "(select AMT1 from T where brhid=F.brhid and dim1=5 and dim2=3) D29," +
                        "(select AMT2 from T where brhid=F.brhid and dim1=5 and dim2=3) D30 " +
                        "from  F";
                break;
            case 7170:
                strSql = "WITH " +
                        "T1 as (select sum(amt1) AT1,sum(amt2) AT2,dim2,dim1 from scbranch s,fcdata F0 where " + condition + " and F0.dt='" + sData + "' and F0.ftype=11 group by dim2,dim1)," +
                        "T2 as (select enudt,enutp from PTENUMINFODETL where enuid='BMType' order by enutp)," +
                        "T3 as (select enudt,enutp,AT1,AT2,dim1,dim2 from T2 left outer join T1 on (T1.dim2=T2.enutp))," +
                        "T4 as (select enudt,enutp," +
                        "(select AT1 from T3 where dim1=1 and enutp=T2.enutp) D01," +
                        "(select AT2 from T3 where dim1=1 and enutp=T2.enutp) D02," +
                        "(select AT1 from T3 where dim1=2 and enutp=T2.enutp) D03," +
                        "(select AT2 from T3 where dim1=2 and enutp=T2.enutp) D04," +
                        "(select AT1 from T3 where dim1=3 and enutp=T2.enutp) D05," +
                        "(select AT2 from T3 where dim1=3 and enutp=T2.enutp) D06," +
                        "(select AT1 from T3 where dim1=4 and enutp=T2.enutp) D07," +
                        "(select AT2 from T3 where dim1=4 and enutp=T2.enutp) D08," +
                        "(select AT1 from T3 where dim1=5 and enutp=T2.enutp) D09," +
                        "(select AT2 from T3 where dim1=5 and enutp=T2.enutp) D10 from T2) " +

                        "select enudt,D01,D02,D03,D04,D05,D06,D07,D08,D09,D10 from T4 order by enutp";
                break;
            case 7180:
                strSql = "WITH " +
                        "T1 as (select sum(amt1) AT1,sum(amt2) AT2,dim2,dim1 from scbranch s,fcdata F0 where " + condition + " and F0.dt='" + sData + "' and F0.ftype=12 group by dim2,dim1)," +
                        "T2 as (select enudt,enutp from PTENUMINFODETL where enuid='BMType' order by enutp)," +
                        "T3 as (select enudt,enutp,AT1,AT2,dim1,dim2 from T2 left outer join T1 on (T1.dim2=T2.enutp))," +
                        "T4 as (select enudt,enutp," +
                        "(select AT1 from T3 where dim1=1 and enutp=T2.enutp) D01," +
                        "(select AT2 from T3 where dim1=1 and enutp=T2.enutp) D02," +
                        "(select AT1 from T3 where dim1=2 and enutp=T2.enutp) D03," +
                        "(select AT2 from T3 where dim1=2 and enutp=T2.enutp) D04," +
                        "(select AT1 from T3 where dim1=3 and enutp=T2.enutp) D05," +
                        "(select AT2 from T3 where dim1=3 and enutp=T2.enutp) D06," +
                        "(select AT1 from T3 where dim1=4 and enutp=T2.enutp) D07," +
                        "(select AT2 from T3 where dim1=4 and enutp=T2.enutp) D08," +
                        "(select AT1 from T3 where dim1=5 and enutp=T2.enutp) D09," +
                        "(select AT2 from T3 where dim1=5 and enutp=T2.enutp) D10 from T2) " +

                        "select enudt,D01,D02,D03,D04,D05,D06,D07,D08,D09,D10 from T4 order by enutp";
                break;
            case 7210://五级分类/四级分类对照表
                strSql = "with " +
                        "F as (select " + strBrhid + " brhid,dt,dim1,dim2,dim3,AMT1,AMT3 from scbranch s,fcdata F0 " +
                        "where " + condition + " and F0.ftype=23 and F0.dt='" + sData + "')" +
                        "select brhid," +
                        //企业-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D01,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D02,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D03,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D04,"+
                        // 企业-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D05,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D06,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D07,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D08,"+
                       // 企业-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D09,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D10,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D11,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D12,"+
                       // 企业-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D13,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D14,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D15,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D16,"+
                      //   企业-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D17,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D18,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D19,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3 in (101,102,103,199) and AMT3 in (3.00,4.00) then AMT1 else 0 end) D20,"+
                        //自然人-农户小额信用-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D21,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D22,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D23,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D24,"+
                        //自然人-农户小额信用-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D25,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D26,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D27,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D28,"+
                        //自然人-农户小额信用-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D29,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D30,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D31,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D32,"+
                        //自然人-农户小额信用-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D33,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D34,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D35,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D36,"+
                        //自然人-农户小额信用-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D37,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D38,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D39,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3=201 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D40,"+
                        //自然人-农户联保-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D41,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D42,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D43,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D44,"+
//                      自然人-农户联保-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D45,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D46,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D47,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D48,"+
//                      自然人-农户联保-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D49,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D50,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D51,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D52,"+
//                      自然人-农户联保-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D53,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D54,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D55,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D56,"+
//                      自然人-农户联保-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D57,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D58,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D59,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3=202 and AMT3 in (1.00,2.00) then AMT1 else 0 end) D60,"+
                        //自然人-助学-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D61,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D62,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D63,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D64,"+
                        //自然人-助学-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D65,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D66,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D67,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D68,"+
                        //自然人-助学-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D69,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D70,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D71,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D72,"+
                        //自然人-助学-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D73,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D74,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D75,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D76,"+
                        //自然人-助学-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D77,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D78,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D79,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3 in (203,302) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D80,"+
                        //自然人-住房按揭-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D81,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D82,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D83,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D84,"+
                        //自然人-住房按揭-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D85,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D86,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D87,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D88,"+
                        //自然人-住房按揭-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D89,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D90,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D91,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D92,"+
                        //自然人-住房按揭-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D93,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D94,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D95,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D96,"+
                        //自然人-住房按揭-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D97,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D98,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D99,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3 in (204,303) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D100,"+
                        //自然人-银行卡透支-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D101,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D102,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D103,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D104,"+
//                      自然人-银行卡透支-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D105,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D106,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D107,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D108,"+
//                      自然人-银行卡透支-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D109,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D110,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D111,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D112,"+
//                      自然人-银行卡透支-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D113,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D114,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D115,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D116,"+
//                      自然人-银行卡透支-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D117,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D118,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D119,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3 in (206,305) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D120,"+
                        //自然人-汽车贷款-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D121,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D122,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D123,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D124,"+
//                      自然人-汽车贷款-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D125,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D126,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D127,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D128,"+
//                      自然人-汽车贷款-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D129,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D130,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D131,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D132,"+
//                      自然人-汽车贷款-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D133,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D134,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D135,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D136,"+
//                      自然人-汽车贷款-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D137,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D138,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D139,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3 in (205,304) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D140,"+
                        //自然人-其他-正常
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=1 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D141,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=2 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D142,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=3 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D143,"+
                        "sum(case when brhid=F.brhid and dim1=1 and dim2=4 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D144,"+
                        //自然人-其他-关注
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=1 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D145,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=2 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D146,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=3 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D147,"+
                        "sum(case when brhid=F.brhid and dim1=2 and dim2=4 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D148,"+
                        //自然人-其他-次级
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=1 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D149,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=2 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D150,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=3 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D151,"+
                        "sum(case when brhid=F.brhid and dim1=3 and dim2=4 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D152,"+
                        //自然人-其他-可疑
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=1 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D153,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=2 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D154,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=3 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D155,"+
                        "sum(case when brhid=F.brhid and dim1=4 and dim2=4 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D156,"+
                        //自然人-其他-损失
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=1 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D157,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=2 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D158,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=3 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D159,"+
                        "sum(case when brhid=F.brhid and dim1=5 and dim2=4 and dim3 in (299,301,399) and AMT3 in (1.00,2.00) then AMT1 else 0 end) D160"+
                        " from  F group by brhid";
                break;
				case 7230://业务种类
					strSql = "WITH " +
                    "T1 as (select sum(amt1) AT1,sum(amt2) AT2,dim2,dim1 from scbranch s,fcdata F0 where " + condition + " and F0.dt='" + sData + "' and F0.ftype=5 group by dim2,dim1)," +
                    "T2 as (select enudt,enutp from PTENUMINFODETL where enuid='BMType' order by enutp)," +
                    "T3 as (select enudt,enutp,AT1,AT2,dim1,dim2 from T2 left outer join T1 on (T1.dim2=T2.enutp))," +
                    "T4 as (select enudt,enutp," +
                    "(select AT1 from T3 where dim1=1 and enutp=T2.enutp) D01," +
                    "(select AT2 from T3 where dim1=1 and enutp=T2.enutp) D02," +
                    "(select AT1 from T3 where dim1=2 and enutp=T2.enutp) D03," +
                    "(select AT2 from T3 where dim1=2 and enutp=T2.enutp) D04," +
                    "(select AT1 from T3 where dim1=3 and enutp=T2.enutp) D05," +
                    "(select AT2 from T3 where dim1=3 and enutp=T2.enutp) D06," +
                    "(select AT1 from T3 where dim1=4 and enutp=T2.enutp) D07," +
                    "(select AT2 from T3 where dim1=4 and enutp=T2.enutp) D08," +
                    "(select AT1 from T3 where dim1=5 and enutp=T2.enutp) D09," +
                    "(select AT2 from T3 where dim1=5 and enutp=T2.enutp) D10 from T2) " +

                    "select enudt,D01,D02,D03,D04,D05,D06,D07,D08,D09,D10 from T4 order by enutp";
                break;
            default:
                strSql = "";
        }
        return strSql;
    }

    //获得有关五级分类的表的sql语句    -- lj added in 20050710
    public static String getFCSql1(String strRptNo, String upbrh, String date, String clientName, int flag) {
        String strSql, tempstrSql = "";
        String sData = SystemDate.getDateByComma(date, "-");
        int intRptNo = Integer.parseInt(strRptNo.substring(2));

        int level = -1;
        String condition = "";
        String strBrhid = "s.brhid";
        ScbranchLevel sbl = new ScbranchLevel(upbrh);
        level = sbl.intBrhLevel;

        if (level == 1) {
            strBrhid = "s.bnkid";
            condition = "s.brhid=F0.brhid";
        } else if (level == 2) {
            strBrhid = "s.chgid";
            condition = "s.bnkid = '" + upbrh + "' and s.brhid=F0.brhid";
        } else if (level == 3) {
            strBrhid = "s.brhid";
            String yingyebrhid = upbrh;
            String subbrhall = SCBranch.getAllSubBrh1(yingyebrhid);
            if (subbrhall != null && subbrhall.trim().length() <= 0) {
                subbrhall = null;
            }
            if (yingyebrhid != null && level == 3 && subbrhall == null) {
                condition = "s.brhid = '" + upbrh + "' and s.brhid=F0.brhid";
            } else {
                condition = "s.upbrh = '" + upbrh + "' and s.brhid=F0.brhid";
            }
        } else {
            strBrhid = "s.brhid";
            condition = "s.brhid = '" + upbrh + "' and s.brhid=F0.brhid";
        }
        switch (intRptNo) {
            case 7190:
                strSql = "select C.NAME D01,C.corpresp D02," +
                        "(select enudt from ptenuminfodetl where C.ECOMTYPE = enutp and enuid='EcomType') D03," +
                        "C.MAINBUSINESS D04,F0.bal D05," +
                        "(select enudt from ptenuminfodetl where F0.LoanType3 = enutp and enuid='LoanType3') D06," +
                        "F0.paydate D07,F0.duedate D08 " +
                        "from scbranch s,fcmain F0,CMINDVCLIENT C " +
                        "where " + condition + " and F0.idno = C.id and F0.createdate='" + sData + "' " +
                        "and F0.clientNAME like '%" + DBUtil.toDB(clientName) + "%' and F0.FCType = 2";
                break;
            case 7200:
                switch (flag) {
                    case 1:
                        tempstrSql = "select C.CLIENTNO CNO,C.NAME D01," +
                                "(select enudt from ptenuminfodetl where C.CLIENTTYPE = enutp and enuid='ClientType2') D02," +
                                "C.SUPERVISION D03,C.LAWPERSON D04,C.FOUNDDATE D05,C.ADDRESSINLAW D06,C.CAPITALAMT D07,C.BIZSCOPE D08 " +
                                "from scbranch s,fcmain F0,CMCORPCLIENT C " +
                                "where " + condition + " and F0.idno = C.id and F0.createdate='" + sData + "' " +
                                "and F0.clientNAME like '%" + DBUtil.toDB(clientName) + "%' and F0.FCType = 3";
                        break;
                    case 2:
                        tempstrSql = "select L.CLIENTNO CNO,F0.CNLNO D01," +
                                "(select enudt from ptenuminfodetl where L.LoanType1 = enutp and enuid='LoanType1') D02," +
                                "(select enudt from ptenuminfodetl where L.LoanType3 = enutp and enuid='LoanType3') D03," +
                                "L.CONTRACTAMT D04,F0.paydate D05,F0.duedate D06,L.NOWENDDATE D07," +
                                "(select enudt from ptenuminfodetl where L.LoanCat3 = enutp and enuid='LoanCat3') D08," +
                                "L.LOANPURPOSE D09,G.CLIENTNAME D10,P.PLEDGENAME D11,P.ESTIMATEPRICE D12,P.PLDGMORTRATE D13," +
                                "(select enudt from ptenuminfodetl where P.IfReg = enutp and enuid='IfReg') D14," +
                                "case when L.CLOSEDATE>L.NOWENDDATE then '" + DBUtil.toDB("是") + "' else '" + DBUtil.toDB("否") + "' end D15," +
                                "C.AMT D16," +
                                "(select enudt from ptenuminfodetl where F0.CreditClass = enutp and enuid='CreditClass') D17 " +

                                "from scbranch s,fcmain F0 " +
                                "inner join RQLOANLIST L on (F0.bmno=L.bmno) " +
                                "left outer join BMGUARANTOR G  " +
                                "on (F0.bmno=G.bmno) " +
                                "left outer join BMPLDGMORT P  " +
                                "on (F0.bmno=P.bmno) " +
                                "left outer join CMCORPNEGINFO C  " +
                                "on (L.CLIENTNO = C.CLIENTNO and C.NEGINFOTYPE=2) " +
                                "where " + condition + " and F0.createdate='" + sData + "' " +
                                "and F0.clientNAME like '%" + clientName + "%' and F0.FCType = 3 ";
                        break;
                    case 3:
                        tempstrSql = "select C.CLIENTNO CNO," +
                                "sum(L.AREA) D02,sum(L.ESTIMATE) D03,max(P1.enudt) D04," +
                                "sum(E.AREA) D06,sum(E.ESTIMATE) D07,max(P2.enudt) D08," +
                                "sum(Q.QTY)  D09,sum(Q.ESTIMATE) D11,max(P3.enudt) D12 " +

                                "from scbranch s,fcmain F0 " +
                                "inner join CMCORPCLIENT C on (F0.idno = C.id) " +
                                "left outer join CMCORPLANDHOLDING L " +
                                "on (C.CLIENTNO = L.CLIENTNO and L.EXPIRYDATE>'" + sData + "') " +
                                "left outer join ptenuminfodetl P1 on (L.MORTAGAGED = P1.enutp and P1.enuid='YesNo') " +
                                "left outer join CMCORPESTATE E " +
                                "on (C.CLIENTNO = E.CLIENTNO and E.EXPIRYDATE>'" + sData + "') " +
                                "left outer join ptenuminfodetl P2 on (E.MORTAGAGED = P2.enutp and P2.enuid='HasNo') " +
                                "left outer join CMCORPEQUIPMENT Q " +
                                "on (C.CLIENTNO = Q.CLIENTNO and Q.SCRAPDATE>'" + sData + "') " +
                                "left outer join ptenuminfodetl P3 on (Q.MORTAGAGED = P3.enutp and P3.enuid='YesNo') " +

                                "where " + condition + " and F0.createdate='" + sData + "' " +
                                "and F0.clientNAME like '%" + clientName + "%' and F0.FCType = 3 " +
                                "group by " + strBrhid + ",C.CLIENTNO";
                        break;
                }
                strSql = tempstrSql;
                break;
            default:
                strSql = "";
        }
      
        return strSql;
    }
}
