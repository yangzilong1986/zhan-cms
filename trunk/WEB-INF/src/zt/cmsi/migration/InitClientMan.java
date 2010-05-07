package zt.cmsi.migration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cmsi.biz.util;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class InitClientMan {
    public static InitClientData getInitClient(String initclientno) {
        InitClientData ap = null;
        if (initclientno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):ClientNo is null when Getting InitClient info!");
            return null;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting InitClient info!");
            return null;
        }

        Connection con = null;
        Statement st = null;
        int errorcode = 0;

        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;

            String sSql = "select * from InitClient where ClientNo='" + initclientno + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                ap = new InitClientData();
                ap.appBrhID = rs.getString("AppBrhID");
                ap.appDate = util.dateToCalendar(rs.getDate("AppDate"));
                ap.brhId = rs.getString("BrhID");
                ap.clientMgr = rs.getString("ClientMgr");
                ap.clientNo = rs.getString("ClientNo");
                ap.clientType = rs.getString("ClientType") == null ? null : new Integer(rs.getInt("ClientType"));
                ap.ecomDeptType = rs.getString("EcomDeptType") == null ? null : new Integer(rs.getInt("EcomDeptType"));
                ap.ecomType = rs.getString("EcomType") == null ? null : new Integer(rs.getInt("EcomType"));
                ap.etpScopType = rs.getString("EtpScopType") == null ? null : new Integer(rs.getInt("EtpScopType"));
                ap.gender = rs.getString("Gender") == null ? null : new Integer(rs.getInt("Gender"));
                ap.ID = rs.getString("ID");
                ap.IDType = rs.getString("IDType") == null ? null : new Integer(rs.getInt("IDType"));
                ap.nClientNo = rs.getString("nClientNo");
                ap.name = DBUtil.fromDB(rs.getString("Name"));
                ap.procStatus = rs.getString("ProcStatus") == null ? null : new Integer(rs.getInt("ProcStatus"));
                ap.sectorCat1 = rs.getString("SectorCat1") == null ? null : new Integer(rs.getInt("SectorCat1"));
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No BMTableApp Record when Getting InitClient info!CLIENTNO=" + initclientno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when Getting InitClient info!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ap;
        }

    }

    public static void main(String[] args) {
        InitClientMan initClientMan1 = new InitClientMan();
        InitClientData ap = InitClientMan.getInitClient("900000001");
        System.out.println("" + ap.name);
        System.out.println("" + ap.clientNo);

    }

}