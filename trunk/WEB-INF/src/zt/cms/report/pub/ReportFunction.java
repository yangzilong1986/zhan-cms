package zt.cms.report.pub;

import zt.platform.db.*;
import zt.cms.report.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ReportFunction {
///values
    private DatabaseConnection conn = null;

    public final static int I_CORPCLIENT = 0;
    public final static int I_INDVCLIENT = 1;
    public final static int I_NONECLIENT = -1;
    public final static int I_CLIENTNOISNULL = -10000;
    public final static int I_SQLISNULL = -20000;

///function
    public ReportFunction(DatabaseConnection conn1)
    {
        if (conn == null) {
            conn = conn1;
        }
    }

    public int getClientTypeByClientNo(String strclientno)
    {

        if (strclientno == null) {
            return this.I_CLIENTNOISNULL;
        }
        if (isClientInIndv(strclientno)) {
            return this.I_INDVCLIENT;
        }
        return this.I_CORPCLIENT;
    }

    public boolean isClientInIndv(String strclientno)
    {
        String strsql = "select * from CMIndvClient where ClientNo='" + strclientno + "'";
        RecordSet rs = null;
        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean isClientInCorp(String strclientno)
    {
        String strsql = "select * from CMCorpClient where ClientNo='" + strclientno + "'";
        RecordSet rs = null;
        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public BaseInfoObject getIndvBaseInfoByBmno(String strbmno)
    {
        String strsql = "select a.BrhID,a.ClientName,c.CORRESPADDRESS,b.LoanType5,b.LoanCat3,c.ID,b.ClientMgr,b.FirstResp,b.FisrtRespPct from bmtable a,bmtableapp b,CMIndvClient c where a.bmno=b.bmno and a.clientno=c.clientno and a.bmno ='"
            + strbmno + "'";
        RecordSet rs = null;
        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (rs.next()) {
            return new BaseInfoObject(DBUtil.fromDB(rs.getString(0)),DBUtil.fromDB(rs.getString(1)),DBUtil.fromDB(rs.getString(2)),
                                      DBUtil.fromDB(rs.getString(3)),DBUtil.fromDB(rs.getString(4)),DBUtil.fromDB(rs.getString(5)),
                                      DBUtil.fromDB(rs.getString(6)),DBUtil.fromDB(rs.getString(7)),DBUtil.fromDB(rs.getString(8)));
        }

        return null;
    }

    public BaseInfoObject getCorpBaseInfoByBmno(String strbmno)
    {
        String strsql = "select a.BrhID,a.ClientName,c.ADDRESSINLAW,b.LoanType5,b.LoanCat3,c.ID,b.ClientMgr,b.FirstResp,b.FisrtRespPct from bmtable a,bmtableapp b,CMCORPCLIENT c where a.bmno=b.bmno and a.clientno=c.clientno and a.bmno ='"
            + strbmno + "'";
        RecordSet rs = null;
        try {
            rs = conn.executeQuery(strsql);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (rs.next()) {
            return new BaseInfoObject(DBUtil.fromDB(rs.getString(0)),DBUtil.fromDB(rs.getString(1)),DBUtil.fromDB(rs.getString(2)),
                                      DBUtil.fromDB(rs.getString(3)),DBUtil.fromDB(rs.getString(4)),DBUtil.fromDB(rs.getString(5)),
                                      DBUtil.fromDB(rs.getString(6)),DBUtil.fromDB(rs.getString(7)),DBUtil.fromDB(rs.getString(8)));
        }

        return null;

    }

}