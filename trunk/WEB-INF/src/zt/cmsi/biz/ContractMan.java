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

public class ContractMan {

    public static ContractData getContract(String bmno) {
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter:BMNO is null when getting Contract Info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when getting Contract Info!");
            return null;
        }

        ContractData bmdata = null;
        Connection con = null;
        Statement st = null;

        try {
            //String sSql = "select ContractNo,sContractNo,BankNoteNo,ActualDisAmt,OtherCertNo,StartDate,EndDate,InterestType,billno from BMContract where BMNo='"+bmno+"'";
            String sSql = "select ContractNo,sContractNo,BankNoteNo,ActualDisAmt,OtherCertNo,StartDate,EndDate,InterestType from BMContract where BMNo='" + bmno + "'";

            con = dc.getConnection();
            st = con.createStatement();

            ResultSet rs = null;
            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                bmdata = new ContractData();
                bmdata.actualDisAmt = rs.getBigDecimal("ActualDisAmt");
                bmdata.bankNoteNo = rs.getString("BankNoteNo");
                bmdata.contractNo = rs.getString("ContractNo");
                bmdata.otherCertNo = rs.getString("OtherCertNo");
                bmdata.sContractNo = rs.getString("sContractNo");
                bmdata.beginDate = util.dateToCalendar(rs.getDate("StartDate"));
                bmdata.endDate = util.dateToCalendar(rs.getDate("EndDate"));
                bmdata.interestType = new Integer(rs.getInt("InterestType"));
                //bmdata.billNo = rs.getString("billno");
            } else {
                Debug.debug(Debug.TYPE_WARNING, "No Records when getting Contract Info!");
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when getting contract info of BMNO=!" + bmno);
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            try {
                if (st != null) st.close();
            }
            catch (Exception e) {
                Debug.debug(e);
            }

            MyDB.getInstance().apReleaseConn(0);
            return bmdata;
        }

    }

    public static void main(String[] args) {
        ContractMan contractMan1 = new ContractMan();
    }

}
