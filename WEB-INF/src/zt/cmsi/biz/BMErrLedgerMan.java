package zt.cmsi.biz;

import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
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

public class BMErrLedgerMan {

    static public int finishErrLedgerProcess(String bmno) {
        if (bmno == null) return ErrorCode.PARAM_IS_NULL;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null)
            return ErrorCode.NO_DB_CONN;

        int errorcode = 0;

        try {
            String sSql = "update BTErrLedger set ProcStatus=" + EnumValue.ProcStatus_YiBuShouQuan +
                    " where BMNo='" + bmno + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn <= 0) {
                Debug.debug(Debug.TYPE_ERROR, "Failed to setting ErrLedger to status:Authorized!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true)
                Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when setting ErrLedger to status:Authorized!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }


    }


    static public BTErrLedgerData getErrLedger(String bmno) {
        BTErrLedgerData ap = null;
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):BMNo is null when Getting BTErrLedger info!");
            return null;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting BTErrLedger info!");
            return null;
        }

        Connection con = null;
        Statement st = null;
        int errorcode = 0;

        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;

            String sSql = "select * from BTErrLedger where BMNo='" + bmno + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                ap = new BTErrLedgerData();
                ap.bmNo = rs.getString("BMNo");
                ap.clientNo = rs.getString("ClientNo");
                ap.ProcStatus = rs.getString("ProcStatus") == null ? null : new Integer(rs.getInt("ProcStatus"));
                ap.sBMNo = rs.getString("sBMNo");
                ap.brhID = rs.getString("BRHID");
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No BMErrLedger DB Record when Getting BMErrLedger info!BMNo=" + bmno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when Getting BTErrLedger info!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ap;
        }
    }


    public static void main(String[] args) {
        //BMErrLedgerMan BMErrLedgerMan1 = new BMErrLedgerMan();
        System.out.println(BMErrLedgerMan.finishErrLedgerProcess(null));
    }

}