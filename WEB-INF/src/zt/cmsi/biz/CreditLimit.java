package zt.cmsi.biz;

import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class CreditLimit {
    public static CreditLimitData getCreditLimit(String clientno, int bmtypeno) {
        CreditLimitData ap = null;
        if (clientno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):ClientNo is null when Getting CreditLimit info!");
            return null;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting CreditLimit info!");
            return null;
        }

        Connection con = null;
        Statement st = null;
        int errorcode = 0;

        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;

            String sSql = "select * from BMCreditLimit where ClientNo='" + clientno + "' and TypeNo=" + bmtypeno;
            //String sSql = "select * from BMTableApp where BMNO='" + BMNo + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                ap = new CreditLimitData();
                ap.bmTypeNo = rs.getString("TypeNo") == null ? null : new Integer(rs.getInt("TypeNo"));
                ap.clientNo = rs.getString("ClientNo");
                ap.creditLimit = rs.getBigDecimal("CreditLimit");
                ap.hasBadLoan = rs.getString("HasBadLoan") == null ? null : new Integer(rs.getInt("HasBadLoan"));
                ap.limitApproved = rs.getBigDecimal("LimitApproved");
                ap.limitCommit = rs.getBigDecimal("LimitCommit");
                ap.loanBal = rs.getBigDecimal("LoanBal");
                ap.ifRespLoan = rs.getString("IfRespLoan") == null ? null : new Integer(rs.getInt("IfRespLoan"));
                ap.operator = rs.getString("operator");
                ap.firstResp = rs.getString("FIRSTRESP");
                ap.decidedBy = rs.getString("decidedby");
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No CreditLimit Record when Getting CreditLimit info!client=" + clientno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when Getting CreditLimit info!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ap;
        }

    }

    public static void main(String[] args) {
        CreditLimit.getCreditLimit("12131231 ", 1);
    }
}
