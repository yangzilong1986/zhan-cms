package zt.cms.report;

import zt.platform.db.*;
import zt.cmsi.mydb.*;
import zt.cms.report.util.*;
import zt.cms.report.pub.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SimpLoanDetailsReport {
    private DatabaseConnection conn = null;
    public SimpLoanDetailsReport()
    {
        if (conn == null) {
            conn = MyDB.getInstance().apGetConn();
        }
    }

    public BaseInfoObject getBaseInfoByName(String strbmno)
    {
        ReportFunction rf = new ReportFunction(conn);
        if (rf.getClientTypeByClientNo(strbmno) == ReportFunction.I_INDVCLIENT) {
            return rf.getIndvBaseInfoByBmno(strbmno);
        }
        else {
            return rf.getCorpBaseInfoByBmno(strbmno);
        }
    }

    public BaseMessage getRQPayBackInfoByName(String strbmno)
    {
        String strsql =
            "select ACTNO,CNLNO,TXNDATE,PAYCRBAL,PAYDBBAL,DTLBAL,'"+DBUtil.toDB("未添")+"' A from RQPayBack  where BMNO='"
            + strbmno + "'";
        System.out.println("SimpLoanDetailsReport.getRQPayBackInfoByName(line:43):"+strsql);
        RecordSet rs = null;
        BaseMessage bm = null;
        Vector vc = new Vector();
        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        while (rs.next()) {
            vc.addElement(new RQPayBackObject(DBUtil.fromDB(rs.getString(0)),
                                              DBUtil.fromDB(rs.getString(1)),
                                              DBUtil.fromDB(rs.getString(2)),
                                              DBUtil.fromDB(rs.getString(3)),
                                              DBUtil.fromDB(rs.getString(4)),
                                              DBUtil.fromDB(rs.getString(5)),
                                              DBUtil.fromDB(rs.getString(6))));
        }

        bm = new BaseMessage(vc);
        return bm;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////     根据参数构造sql语句并获取结果集的函数源码    /////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public LoanInfoObject getLoanInfoObjByName(String strbmno)
    {
        String strsql = "select b.CONTRACTNO,b.SCONTRACTNO,a.CONTRACTAMT,a.PAYDATE,a.ACTNO,a.CNLNO,a.ACCNO,a.CRTRATE,a.ENDDATE,a.NOWENDDATE,a.PERIMON,a.NOWBAL,b.LOANTYPE3,b.LOANTYPE5,a.LOANCAT2,1.02 as JQValue from RQLoanLedger a,bmtableapp b where a.bmno=b.bmno and a.bmno='" +
            strbmno + "'";
        RecordSet rs = null;

        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        while (rs.next()) {
            return new LoanInfoObject(DBUtil.fromDB(rs.getString(0)),
                                      DBUtil.fromDB(rs.getString(1)),
                                      DBUtil.fromDB(rs.getString(2)),
                                      DBUtil.fromDB(rs.getString(3)),
                                      DBUtil.fromDB(rs.getString(4)),
                                      DBUtil.fromDB(rs.getString(5)),
                                      DBUtil.fromDB(rs.getString(6)),
                                      DBUtil.fromDB(rs.getString(7)),
                                      DBUtil.fromDB(rs.getString(8)),
                                      DBUtil.fromDB(rs.getString(9)),
                                      DBUtil.fromDB(rs.getString(10)),
                                      DBUtil.fromDB(rs.getString(11)),
                                      DBUtil.fromDB(rs.getString(12)),
                                      DBUtil.fromDB(rs.getString(13)),
                                      DBUtil.fromDB(rs.getString(14)),
                                      DBUtil.fromDB(rs.getString(15)));
        }
        return null;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////     根据参数构造sql语句并获取结果集的函数源码    /////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public InactInfoObject getInactLoanInfoObjByName(String strbmno)
    {
        String strsql = "select TransferDate,AdminedBy,LastNotifyDate,PenaltyDate,PenaltyRule,Penalty from BMInactLoan	where BMNO='" +
            strbmno + "'";
        RecordSet rs = null;

        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        while (rs.next()) {
            return new InactInfoObject(DBUtil.fromDB(rs.getString(0)), DBUtil.fromDB(rs.getString(1)),
                                       DBUtil.fromDB(rs.getString(2)), DBUtil.fromDB(rs.getString(3)),
                                       DBUtil.fromDB(rs.getString(4)), DBUtil.fromDB(rs.getString(5)));
        }
        return null;
    }

    public void printRequest()
    {

    }
}