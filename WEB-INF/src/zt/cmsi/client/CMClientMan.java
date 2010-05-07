//Source file: e:\\java\\zt\\cmsi\\client\\CMClientMan.java

package zt.cmsi.client;

import zt.cms.pub.SCUser;
import zt.cmsi.biz.CMCreditLimitData;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CMClientMan {
    public static int updateClientCatAndType(String clientNo, String Operator) {
        int errorcode = 0;

        CMClient data = CMClientMan.getCMClient(clientNo);
        if (data == null) {
            return ErrorCode.CLIENT_NOT_FOUND;
        }

        if (SCUser.isExist(Operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        try {
            String sSql =
                    "update BMTableApp set ";

            if (data.clientType != null) {
                sSql += "ClientType=" + data.clientType + ",";
            }
            if (data.ecomType != null) {
                sSql += "EcomType=" + data.ecomType + ",";
            }
            if (data.ecomDeptType != null) {
                sSql += "EcomDeptType=" + data.ecomDeptType + ",";
            }
            if (data.etpScopType != null) {
                sSql += "EtpScopType=" + data.etpScopType + ",";
            }
            if (data.sectorCat1 != null) {
                sSql += "SectorCat1=" + data.sectorCat1 + ",";
            }
            if (data.clientMgr != null) {
                sSql += "ClientMgr=" + "'" + data.clientMgr + "',";
            }

            if (sSql.length() > 0) {
                sSql += " LastModified='" + sysdate + "' where ClientNo='" + clientNo +
                        "'";

            }
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR,
                        "failed to udpate BMTableApp Type fields!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when updating BMTableApp Type fields!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * @param clientNo
     * @return zt.cmsi.client.CMClient
     * @roseuid 3FEBC1A8008C
     */
    public static CMClient getCMClient(String clientNo) {
        if (clientNo == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Parameter:ClientNo is null when Getting Client Info!");
            return null;
        }

        DatabaseConnection conn = MyDB.getInstance().apGetConn();
        if (conn == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Can not get DB Connection when Getting Client Info!");
        }
        //System.out.println("DB in CLIENT is:" + conn.hashCode());

        //RecordSet rs = null;
        CMClient cm = null;
        String sSql;
        int errorcode = 0;

        Connection con = null;
        Statement st = null;

        try {
            con = conn.getConnection();
            st = con.createStatement();
            ResultSet rs2 = null;

            sSql =
                    "select CLIENTNO,IDTYPE,ID,CLIENTTYPE,ECOMTYPE,ECOMDEPTTYPE,SECTORCAT1," +
                            "ETPSCOPTYPE,CLIENTMGR,NAME,AppBrhID,CreditClass,LOANCARDNO from CMINDVCLIENT where CLIENTNO='" +
                            clientNo + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs2 = st.executeQuery(sSql);
            if (rs2.next()) {
                cm = new CMClient();
                cm.ifIndv = true;
                cm.clientNo = rs2.getString(1);
                cm.IDType = rs2.getString(2) == null ? null : (Integer) rs2.getObject(2);
                cm.ID = rs2.getString(3);
                cm.clientType = rs2.getString(4) == null ? null :
                        (Integer) rs2.getObject(4);
                cm.ecomType = rs2.getString(5) == null ? null :
                        (Integer) rs2.getObject(5);
                cm.ecomDeptType = rs2.getString(6) == null ? null :
                        (Integer) rs2.getObject(6);
                cm.sectorCat1 = rs2.getString(7) == null ? null :
                        (Integer) rs2.getObject(7);
                cm.etpScopType = rs2.getString(8) == null ? null :
                        (Integer) rs2.getObject(8);
                cm.clientMgr = rs2.getString(9);
                cm.name = rs2.getString(10);
                if (cm.name != null) {
                    cm.name = DBUtil.fromDB(cm.name);
                }
                cm.appBrhID = rs2.getString(11);
                cm.creditClass = rs2.getString(12) == null ? null :
                        (Integer) rs2.getObject(12);
                cm.loanCardNo = rs2.getString(13);
            } else {

                sSql =
                        "select CLIENTNO,IDTYPE,ID,CLIENTTYPE,ECOMTYPE,ECOMDEPTTYPE,SECTORCAT1," +
                                "ETPSCOPTYPE,CLIENTMGR,NAME,AppBrhID,CreditClass,LOANCARDNO from CMCORPCLIENT where CLIENTNO='" +
                                clientNo + "'";
                Debug.debug(Debug.TYPE_SQL, sSql);

                rs2 = st.executeQuery(sSql);
                if (rs2.next()) {
                    cm = new CMClient();
                    cm.ifIndv = false;
                    cm.clientNo = rs2.getString(1);
                    cm.IDType = rs2.getString(2) == null ? null :
                            (Integer) rs2.getObject(2);
                    cm.ID = rs2.getString(3);
                    cm.clientType = rs2.getString(4) == null ? null :
                            (Integer) rs2.getObject(4);
                    cm.ecomType = rs2.getString(5) == null ? null :
                            (Integer) rs2.getObject(5);
                    cm.ecomDeptType = rs2.getString(6) == null ? null :
                            (Integer) rs2.getObject(6);
                    cm.sectorCat1 = rs2.getString(7) == null ? null :
                            (Integer) rs2.getObject(7);
                    cm.etpScopType = rs2.getString(8) == null ? null :
                            (Integer) rs2.getObject(8);
                    cm.clientMgr = rs2.getString(9);
                    cm.name = rs2.getString(10);
                    if (cm.name != null) {
                        cm.name = DBUtil.fromDB(cm.name);
                    }
                    cm.appBrhID = rs2.getString(11);
                    cm.creditClass = rs2.getString(12) == null ? null :
                            (Integer) rs2.getObject(12);
                    cm.loanCardNo = rs2.getString(13);
                } else {
                    //errorcode = ErrorCode.CLIENT_NOT_FOUND;
                    Debug.debug(Debug.TYPE_ERROR,
                            "Can not find Client Info when Getting Client Info! ClientNo=" +
                                    clientNo);
                    cm = null;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when Getting Client Info! Info=" + e.getMessage());
            cm = null;
            //errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }

            MyDB.getInstance().apReleaseConn(0);
        }

        return cm;
    }

    public static CMClient getCMClientFromID(String id) {
        id = id.trim();
        if (id == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Parameter:IDNO is null when Getting Client Info!");
            return null;
        }

        DatabaseConnection conn = MyDB.getInstance().apGetConn();
        if (conn == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Can not get DB Connection when Getting Client Info!");
        }
        //System.out.println("DB in CLIENT is:" + conn.hashCode());

        //RecordSet rs = null;
        CMClient cm = null;
        String sSql;
        int errorcode = 0;

        Connection con = null;
        Statement st = null;

        try {
            con = conn.getConnection();
            st = con.createStatement();
            ResultSet rs2 = null;

            sSql =
                    "select CLIENTNO,IDTYPE,ID,CLIENTTYPE,ECOMTYPE,ECOMDEPTTYPE,SECTORCAT1," +
                            "ETPSCOPTYPE,CLIENTMGR,NAME,AppBrhID,CreditClass,LOANCARDNO from CMCORPCLIENT where ID='" +
                            id + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs2 = st.executeQuery(sSql);
            if (rs2.next()) {
                cm = new CMClient();
                cm.ifIndv = false;
                cm.clientNo = rs2.getString(1);
                cm.IDType = rs2.getString(2) == null ? null : (Integer) rs2.getObject(2);
                cm.ID = rs2.getString(3);
                cm.clientType = rs2.getString(4) == null ? null :
                        (Integer) rs2.getObject(4);
                cm.ecomType = rs2.getString(5) == null ? null :
                        (Integer) rs2.getObject(5);
                cm.ecomDeptType = rs2.getString(6) == null ? null :
                        (Integer) rs2.getObject(6);
                cm.sectorCat1 = rs2.getString(7) == null ? null :
                        (Integer) rs2.getObject(7);
                cm.etpScopType = rs2.getString(8) == null ? null :
                        (Integer) rs2.getObject(8);
                cm.clientMgr = rs2.getString(9);
                cm.name = rs2.getString(10);
                if (cm.name != null) {
                    cm.name = DBUtil.fromDB(cm.name);
                }
                cm.appBrhID = rs2.getString(11);
                cm.creditClass = rs2.getString(12) == null ? null :
                        (Integer) rs2.getObject(12);
                cm.loanCardNo = rs2.getString(13);
            } else {
                sSql =
                        "select CLIENTNO,IDTYPE,ID,CLIENTTYPE,ECOMTYPE,ECOMDEPTTYPE,SECTORCAT1," +
                                "ETPSCOPTYPE,CLIENTMGR,NAME,AppBrhID,CreditClass,LOANCARDNO from CMINDVCLIENT where ID='" +
                                id + "'";
                Debug.debug(Debug.TYPE_SQL, sSql);

                rs2 = st.executeQuery(sSql);
                if (rs2.next()) {
                    cm = new CMClient();
                    cm.ifIndv = true;
                    cm.clientNo = rs2.getString(1);
                    cm.IDType = rs2.getString(2) == null ? null :
                            (Integer) rs2.getObject(2);
                    cm.ID = rs2.getString(3);
                    cm.clientType = rs2.getString(4) == null ? null :
                            (Integer) rs2.getObject(4);
                    cm.ecomType = rs2.getString(5) == null ? null :
                            (Integer) rs2.getObject(5);
                    cm.ecomDeptType = rs2.getString(6) == null ? null :
                            (Integer) rs2.getObject(6);
                    cm.sectorCat1 = rs2.getString(7) == null ? null :
                            (Integer) rs2.getObject(7);
                    cm.etpScopType = rs2.getString(8) == null ? null :
                            (Integer) rs2.getObject(8);
                    cm.clientMgr = rs2.getString(9);
                    cm.name = rs2.getString(10);
                    if (cm.name != null) {
                        cm.name = DBUtil.fromDB(cm.name);
                    }
                    cm.appBrhID = rs2.getString(11);
                    cm.creditClass = rs2.getString(12) == null ? null :
                            (Integer) rs2.getObject(12);
                    cm.loanCardNo = rs2.getString(13);
                } else {
                    //errorcode = ErrorCode.CLIENT_NOT_FOUND;
                    Debug.debug(Debug.TYPE_ERROR,
                            "Can not find Client Info when Getting Client Info! ID=" +
                                    id);
                    cm = null;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when Getting Client Info! Info=" + e.getMessage());
            cm = null;
            //errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }

            MyDB.getInstance().apReleaseConn(0);
        }

        return cm;
    }

    public static CMCreditLimitData getCMCreditLimit(String clientNo,
                                                     int bmtypeno) {
        if (clientNo == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Parameter:ClientNo is null when Getting Client CreditLimit Info!");
            return null;
        }

        DatabaseConnection conn = MyDB.getInstance().apGetConn();
        if (conn == null) {
            Debug.debug(Debug.TYPE_ERROR,
                    "Can not get DB Connection when Getting Client Info!");
        }
        //System.out.println("DB in CLIENT is:" + conn.hashCode());

        //RecordSet rs = null;
        CMCreditLimitData cm = null;
        String sSql;
        int errorcode = 0;

        Connection con = null;
        Statement st = null;

        try {
            con = conn.getConnection();
            st = con.createStatement();
            ResultSet rs2 = null;

            sSql = "select * from BMCreditLimit where CLIENTNO='" + clientNo +
                    "' and TypeNo=" + EnumValue.BMType_DaiKuanZhengDaiKuan;
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs2 = st.executeQuery(sSql);
            if (rs2.next()) {
                cm = new CMCreditLimitData();
                cm.creditLimit = rs2.getBigDecimal("CreditLimit");
                cm.disabled = rs2.getBoolean("Disabled");
                cm.limitApproved = rs2.getBoolean("LimitApproved");
                cm.limitCommit = rs2.getBigDecimal("LimitCommit");
                cm.loanBal = rs2.getBigDecimal("LoanBal");
            } else {
                //errorcode = ErrorCode.CLIENT_NOT_FOUND;
                Debug.debug(Debug.TYPE_ERROR,
                        "Can not find Client Credit Limit Info when Getting Client CreditLimit Info! ClientNo=" +
                                clientNo);
                cm = null;
            }

        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when Getting Client Info! Info=" + e.getMessage());
            cm = null;
            //errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }

            MyDB.getInstance().apReleaseConn(0);
        }

        return cm;
    }

    static public void main(String[] args) {
//     CMClient cm;
//     cm = CMClientMan.getCMClient("1");
//     System.out.println(cm);
//     if(cm!= null)
//       System.out.println(cm.clientMgr+ "|"+cm.clientNo +"|"+cm.clientType+"|"+
//           cm.ecomDeptType+"|"+cm.ecomType +"|"+cm.etpScopType+"|"+cm.ID+"|"+
//           cm.IDType+"|"+cm.sectorCat1+"|"+cm.name );
        //System.out.println(CMClientMan.updateClientCatAndType("1","system"));
        // System.out.println(CMClientMan.getCMClient("18").loanCardNo);
        System.out.println(CMClientMan.getCMCreditLimit("1", 0).disabled);
    }
}
