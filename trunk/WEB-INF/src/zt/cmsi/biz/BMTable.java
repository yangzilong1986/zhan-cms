package zt.cmsi.biz;

/**
 * 生成新业务主表记录
 *
 * 所有输入参数不能为空
 * 1. 获得新BMNo
 * 2. 从CLIENTNO获得CLIENTNAME, CLIENTNO不存在返回错误信息
 * 3. CREATEDATE为业务日期
 * 4. STATUSDATE为业务日期
 * 5. lastcheckdate为业务日期
 * 5. 检查BRHID,INITBRHID,OPERATOR必须存在,否则返回错误信息
 * 6. INSERT记录到BMTABLE
 *
 * 成功返回新的BMNO, 失败返回错误信息
 *
 * @param BMTypeNo
 * @param ClientNo
 * @param BrhID
 * @param InitBrhID
 * @param Operator
 * @return int
 * @roseuid 3FE5173F0329
 */

import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.code.SNBusinessID;
import zt.cmsi.pub.confitem;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class BMTable {

    public static String createBMTable(int BMTypeNo, String ClientNo, String BrhID, String InitBrhID, String Operator) {
        if (ClientNo == null || BrhID == null || InitBrhID == null || Operator == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s) is null when creating BMTable!");
            return null;
        }

        if (SCBranch.isExist(BrhID) == false) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):BrhID(branch) does not exist when creating BMTable!");
            return null;
        }

        if (SCBranch.isExist(InitBrhID) == false) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):InitBrhID(branch) does not exist when creating BMTable!");
            return null;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):Can not get System Date when creating BMTable!");
            return null;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        //System.out.println("DB in BMTABL is:" + dc.hashCode());
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when creating BMTable!");
            return null;
        }
        //Connection cn = dc.getConnection();
        //Statement st = null;
        //if(cn == null)
        //{
        //  MyDB.getInstance().apReleaseConn();
        //  return ErrorCode.NO_DB_CONN_PTCON;
        //}

        int errorcode = 0;
        String bmno = SNBusinessID.getNextNo();
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get Next Business No when creating BMTable!");
            MyDB.getInstance().apReleaseConn(ErrorCode.GET_SN_ERROR);
            return null;
        }

        CMClient cm = CMClientMan.getCMClient(ClientNo);
        if (cm == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not find Client Info when creating BMTable! ClientNo=" + ClientNo);
            MyDB.getInstance().apReleaseConn(ErrorCode.CLIENT_NOT_FOUND);
            return null;
        }

        try {
            String sSql = "insert into BMTable (BMNO,BrhID,InitBrhID,Operator,ClientNo,ClientName," +
                    "TypeNo,BMStatus,StatusDate,LastCheckDate,CreateDate,ID) values('" +
                    bmno + "','" + BrhID + "','" + InitBrhID + "','" + Operator + "','" +
                    ClientNo + "','" +
                    DBUtil.toDB(cm.name) + "'," + BMTypeNo + "," + EnumValue.BMStatus_DengJi + ",'" +
                    sysdate + "','" + sysdate + "','" + sysdate + "','" + cm.ID + "')";

            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to insert BMTable!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
            /*
                   st = cn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                                             ResultSet.CONCUR_READ_ONLY);


                   ResultSet rs = st.executeQuery("select * from BMTABLE");

                   rs.moveToInsertRow();
                   rs.updateString("BMNO",bmno);
                   rs.updateString("BrhID",BrhID);
                   rs.updateString("InitBrhID",InitBrhID);
                   rs.updateString("Operator",Operator);
                   rs.updateString("ClientNo",ClientNo);
                   rs.updateString("ClientName",cm.name);
                   rs.updateInt("TypeNo",BMTypeNo);
                   rs.updateInt("BMStatus",EnumValue.BMStatus_DengJi);
                   rs.updateDate("StatusDate",sysdate);
                   rs.updateDate("LastCheckDate",sysdate);
                   rs.updateDate("CreateDate",sysdate);
                   rs.insertRow();
            */
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            if (errorcode >= 0) {
                return bmno;
            } else {
                return null;
            }
        }
    }

    public static String createBMTable(String bmNo, int BMTypeNo, String ClientNo, String BrhID, String InitBrhID, String Operator) {
        if (bmNo == null || ClientNo == null || BrhID == null || InitBrhID == null || Operator == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s) is null when creating BMTable!");
            return null;
        }

        if (SCUser.isExist(Operator) == false) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):Operator does not exist when creating BMTable!");
            return null;
        }
        if (SCBranch.isExist(BrhID) == false) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):BrhID(branch) does not exist when creating BMTable!");
            return null;
        }

        if (SCBranch.isExist(InitBrhID) == false) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):InitBrhID(branch) does not exist when creating BMTable!");
            return null;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):Can not get System Date when creating BMTable!");
            return null;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        //System.out.println("DB in BMTABL is:" + dc.hashCode());
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when creating BMTable!");
            return null;
        }
        //Connection cn = dc.getConnection();
        //Statement st = null;
        //if(cn == null)
        //{
        //  MyDB.getInstance().apReleaseConn();
        //  return ErrorCode.NO_DB_CONN_PTCON;
        //}

        int errorcode = 0;
        String bmno = bmNo;
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get Next Business No when creating BMTable!");
            MyDB.getInstance().apReleaseConn(ErrorCode.GET_SN_ERROR);
            return null;
        }

        CMClient cm = CMClientMan.getCMClient(ClientNo);
        if (cm == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not find Client Info when creating BMTable! ClientNo=" + ClientNo);
            MyDB.getInstance().apReleaseConn(ErrorCode.CLIENT_NOT_FOUND);
            return null;
        }

        try {
            String sSql = "insert into BMTable (BMNO,BrhID,InitBrhID,Operator,ClientNo,ClientName," +
                    "TypeNo,BMStatus,StatusDate,LastCheckDate,CreateDate,ID) values('" +
                    bmno + "','" + BrhID + "','" + InitBrhID + "','" + Operator + "','" +
                    ClientNo + "','" +
                    DBUtil.toDB(cm.name) + "'," + BMTypeNo + "," + EnumValue.BMStatus_DengJi + ",'" +
                    sysdate + "','" + sysdate + "','" + sysdate + "','" + cm.ID + "')";

            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to insert BMTable!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
            /*
                   st = cn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                                             ResultSet.CONCUR_READ_ONLY);


                   ResultSet rs = st.executeQuery("select * from BMTABLE");

                   rs.moveToInsertRow();
                   rs.updateString("BMNO",bmno);
                   rs.updateString("BrhID",BrhID);
                   rs.updateString("InitBrhID",InitBrhID);
                   rs.updateString("Operator",Operator);
                   rs.updateString("ClientNo",ClientNo);
                   rs.updateString("ClientName",cm.name);
                   rs.updateInt("TypeNo",BMTypeNo);
                   rs.updateInt("BMStatus",EnumValue.BMStatus_DengJi);
                   rs.updateDate("StatusDate",sysdate);
                   rs.updateDate("LastCheckDate",sysdate);
                   rs.updateDate("CreateDate",sysdate);
                   rs.insertRow();
            */
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            if (errorcode >= 0) {
                return bmno;
            } else {
                return null;
            }
        }
    }

    //this functions has been scrapped
    private static int createBMTable2(int BMTypeNo, String ClientNo, String BrhID, String InitBrhID, String Operator) {
        if (ClientNo == null || BrhID == null || InitBrhID == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        if (SCUser.isExist(Operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }
        if (SCBranch.isExist(BrhID) == false) {
            return ErrorCode.BRANCH_NOT_FOUND;
        }
        if (SCBranch.isExist(InitBrhID) == false) {
            return ErrorCode.BRANCH_NOT_FOUND;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        //System.out.println("DB in BMTABL is:" + dc.hashCode());
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        //Connection cn = dc.getConnection();
        //Statement st = null;
        //if(cn == null)
        //{
        //  MyDB.getInstance().apReleaseConn();
        //  return ErrorCode.NO_DB_CONN_PTCON;
        //}

        int errorcode = 0;
        String bmno = SNBusinessID.getNextNo();
        CMClient cm = CMClientMan.getCMClient(ClientNo);
        if (cm == null) {
            MyDB.getInstance().apReleaseConn(ErrorCode.CLIENT_NOT_FOUND);
            return ErrorCode.CLIENT_NOT_FOUND;
        }

        try {
            String sSql = "insert into BMTable (BMNO,BrhID,InitBrhID,Operator,ClientNo,ClientName," +
                    "TypeNo,BMStatus,StatusDate,LastCheckDate,CreateDate) values('" +
                    bmno + "','" + BrhID + "','" + InitBrhID + "','" + Operator + "','" +
                    ClientNo + "','" +
                    cm.name + "'," + BMTypeNo + "," + EnumValue.BMStatus_DengJi + ",'" +
                    sysdate + "','" + sysdate + "','" + sysdate + "')";

            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to insert BMTable!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            }
            /*
                   st = cn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                                             ResultSet.CONCUR_READ_ONLY);


                   ResultSet rs = st.executeQuery("select * from BMTABLE");

                   rs.moveToInsertRow();
                   rs.updateString("BMNO",bmno);
                   rs.updateString("BrhID",BrhID);
                   rs.updateString("InitBrhID",InitBrhID);
                   rs.updateString("Operator",Operator);
                   rs.updateString("ClientNo",ClientNo);
                   rs.updateString("ClientName",cm.name);
                   rs.updateInt("TypeNo",BMTypeNo);
                   rs.updateInt("BMStatus",EnumValue.BMStatus_DengJi);
                   rs.updateDate("StatusDate",sysdate);
                   rs.updateDate("LastCheckDate",sysdate);
                   rs.updateDate("CreateDate",sysdate);
                   rs.insertRow();
            */
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * 将BMStatus置为参数的状态（取消, 三级审批不通过, 二级审批不通过, 一级审批不通过,
     * 合同取消中的一个）
     *
     * @param BMNo
     * @param BMStatus
     * @return int
     * @roseuid 3FE840490132
     */
    public static int cancelBMTable(String BMNo, int BMStatus, String operator) {

        if (BMNo == null || operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        int errorcode = 0;

        try {
            String sSql = "update BMTABLE set BMStatus =" + BMStatus +
                    ",StatusDate='" + sysdate + "',Operator='" + operator +
                    "' where BMNO='" + BMNo + "'";

            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to insert BMTable!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            } else {
                LoanGranted.cancelGrant(BMNo);

            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when cancelling BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
            //return -9;
        }
    }

    /**
     * 直接CANCEL BMTABLE, 用户调用来在授权前取消业务.
     *
     * @param bmNo
     * @param Operator
     * @return
     */
    public static int cancelBMTable(String bmNo, String Operator) {
        int errorcode = 0;

        if (bmNo == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
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
            errorcode = BMTable.cancelBMTable(bmNo, EnumValue.BMStatus_QuXiao, Operator);
            if (errorcode >= 0) {
                String sSql =
                        "update BMTrans set Operator='" + Operator + "',EndDate='" +
                                sysdate + "',TransStatus=" +
                                EnumValue.TransStatus_QuXiao + " where TransStatus=" + EnumValue.TransStatus_ZhiXing +
                                " and BMNo='" + bmNo + "'";

                Debug.debug(Debug.TYPE_SQL, sSql);
                int prodrtn = dc.executeUpdate(sSql);
                if (prodrtn < 0) {
                    Debug.debug(Debug.TYPE_ERROR, "failed to cancel BMTable!");
                    errorcode = ErrorCode.DB_UPDATE_FAILED;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when cancelling BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    public static int chgBMTableClient(String oldclientno, String clientno, String operator) {

        if (oldclientno == null || operator == null || clientno == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        int errorcode = 0;

        try {
            String sSql = "update BMTABLE set ClientNo ='" + clientno +
                    "',Operator='" + operator +
                    "' where ClientNo='" + oldclientno + "'";

            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to change BMTable Client!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            } else {
                sSql = "update BMTABLEAPP set ClientNo ='" + clientno +
                        "' where clientno='" + oldclientno + "'";

                //st = cn.createStatement();
                Debug.debug(Debug.TYPE_SQL, sSql);
                prodrtn = dc.executeUpdate(sSql);
                if (prodrtn < 0) {
                    Debug.debug(Debug.TYPE_ERROR, "failed to change BMTable Client!");
                    errorcode = ErrorCode.DB_UPDATE_FAILED;
                }

            }

        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when cancelling BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
            //return -9;
        }
    }

    public static int updateTableStatus(String BMNo, int BMStatus, String operator) {
        if (BMNo == null || operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        int errorcode = 0;

        try {
            String sSql = "update BMTABLE set BMStatus =" + BMStatus +
                    ",StatusDate='" + sysdate + "',Operator='" + operator +
                    "' where BMNO='" + BMNo + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to change BMTable BMStaus!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when changing BMStatus of BMTable !");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * 从BMTableApp表中读数据
     *
     * @param BMNo
     * @return zt.cmsi.biz.UpToDateApp
     * @roseuid 3FE7CF9D00F6
     */
    public static UpToDateApp getUpToDateApp(String BMNo) {
        UpToDateApp ap = null;

        if (BMNo == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):BMNo is null when Getting UpToDateApp info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting UpToDateApp info!");
            return null;
        }
        Connection con = null;
        Statement st = null;
        try {
            con = dc.getConnection();

            st = con.createStatement();
            ResultSet rs = null;
            String sSql = "select * from BMTableApp where BMNo='" + BMNo + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                ap = new UpToDateApp();
                ap.appAmt = rs.getBigDecimal("AppAmt");
                ap.appDate = util.dateToCalendar(rs.getDate("AppDate"));
                ap.appEndDate = util.dateToCalendar(rs.getDate("AppEndDate"));
                ap.appMonths = rs.getString("AppMonths") == null ? null : new Integer(rs.getInt("AppMonths"));
                ap.appStartDate = util.dateToCalendar(rs.getDate("AppStartDate"));
                ap.bmTypeNo = rs.getString("TypeNo") == null ? null : new Integer(rs.getInt("TypeNo"));
                ap.clientNo = rs.getString("ClientNo");
                ap.contractNo = DBUtil.fromDB(rs.getString("ContractNo"));
                ap.curNo = rs.getString("CurNo");
                ap.finalAmt = rs.getBigDecimal("FinalAmt");
                ap.finalAmt2 = rs.getBigDecimal("FinalAmt2");
                ap.finalCurNo = rs.getString("FinalCurNo");
                ap.finalEndDate = util.dateToCalendar(rs.getDate("FinalEndDate"));
                ap.finalMonths = rs.getString("FinalMonths") == null ? null : new Integer(rs.getInt("FinalMonths"));
                ap.finalRate = rs.getBigDecimal("FinalRate");
                ap.finalStartDate = util.dateToCalendar(rs.getDate("FinalStartDate"));
                ap.firstResp = rs.getString("FirstResp");
                ap.firstRespPct = rs.getBigDecimal("FisrtRespPct");
                ap.ifRespLoan = rs.getString("IfRespLoan") == null ? null : new Integer(rs.getInt("IfRespLoan"));
                ap.interestType = rs.getString("InterestType") == null ? null : new Integer(rs.getInt("InterestType"));
                ap.loanCat3 = rs.getString("LoanCat3") == null ? null : new Integer(rs.getInt("LoanCat3"));
                ap.loanPurpose = rs.getString("LoanPurpose");
                ap.loanType3 = rs.getString("LoanType3") == null ? null : new Integer(rs.getInt("LoanType3"));
                ap.loanType5 = rs.getString("LoanType5") == null ? null : new Integer(rs.getInt("LoanType5"));
                ap.origAccNo = rs.getString("OrigAccountNo");
                ap.origBMNo = rs.getString("OrigBMNo");
                ap.origDueBillNo = rs.getString("OrigDueBillNo");
                ap.rate = rs.getBigDecimal("Rate");
                ap.bRate = rs.getBigDecimal("BRate");
                ap.fRate = rs.getBigDecimal("FRate");
                ap.resultType = rs.getString("ResultType") == null ? null : new Integer(rs.getInt("ResultType"));
                ap.sContractNo = DBUtil.fromDB(rs.getString("sContractNo"));
                ap.clientType = rs.getString("ClientType") == null ? null : new Integer(rs.getInt("ClientType"));
                ap.eComType = rs.getString("EcomType") == null ? null : new Integer(rs.getInt("EcomType"));
                ap.eComDeptType = rs.getString("EcomDeptType") == null ? null : new Integer(rs.getInt("EcomDeptType"));
                ap.etpScopType = rs.getString("EtpScopType") == null ? null : new Integer(rs.getInt("EtpScopType"));
                ap.sectorCat1 = rs.getString("SectorCat1") == null ? null : new Integer(rs.getInt("SectorCat1"));
                ap.clientMgr = rs.getString("ClientMgr");
                ap.decidedBy = rs.getString("DecidedBy");
                ap.cnlNo = rs.getString("CNLNO");
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No BMTableApp Record when Getting UpToDateApp info!BMNO=" + BMNo);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when Getting UpToDateApp info!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ap;
        }
    }

    /**
     * 更新BMTableApp表中的数据
     *
     * @param data
     * @return int
     * @roseuid 3FE840F70313
     */
    public static int updateUpToDateApp(String bmno, UpToDateApp data) {
        if (data == null || bmno == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        String sSql = "";
        int errorcode = 0;
        boolean exist = BMTable.ifExistApp(bmno);
        try {
            if (exist == true) {
                if (data.appAmt != null) {
                    sSql += "AppAmt=" + data.appAmt + ",";
                }
                if (data.appDate != null) {
                    sSql += "AppDate=" + "'" + util.calToString(data.appDate, "-") + "',";
                }
                if (data.appEndDate != null) {
                    sSql += "AppEndDate=" + "'" + util.calToString(data.appEndDate, "-") + "',";
                }
                if (data.appMonths != null) {
                    sSql += "AppMonths=" + data.appMonths + ",";
                }
                if (data.appStartDate != null) {
                    sSql += "AppStartDate=" + "'" + util.calToString(data.appStartDate, "-") + "',";
                }
                if (data.bmTypeNo != null) {
                    sSql += "TypeNo=" + data.bmTypeNo + ",";
                }
                if (data.clientNo != null) {
                    sSql += "ClientNo=" + "'" + data.clientNo + "',";
                }
                if (data.contractNo != null) {
                    sSql += "ContractNo=" + "'" + data.contractNo + "',";
                }
                if (data.curNo != null) {
                    sSql += "CurNo=" + "'" + data.curNo + "',";
                }
                if (data.finalAmt != null) {
                    sSql += "FinalAmt=" + data.finalAmt + ",";
                }
                if (data.finalAmt2 != null) {
                    sSql += "FinalAmt2=" + data.finalAmt2 + ",";
                }
                if (data.finalCurNo != null) {
                    sSql += "FinalCurNo=" + "'" + data.finalCurNo + "',";
                }
                if (data.finalEndDate != null) {
                    sSql += "FinalEndDate=" + "'" + util.calToString(data.finalEndDate, "-") + "',";
                }
                if (data.finalMonths != null) {
                    sSql += "FinalMonths=" + data.finalMonths + ",";
                }
                if (data.finalRate != null) {
                    sSql += "FinalRate=" + data.finalRate + ",";
                }
                if (data.bRate != null) {
                    sSql += "bRate=" + data.bRate + ",";
                }
                if (data.fRate != null) {
                    sSql += "fRate=" + data.fRate + ",";
                }
                if (data.finalStartDate != null) {
                    sSql += "FinalStartDate=" + "'" + util.calToString(data.finalStartDate, "-") + "',";
                }
                if (data.firstResp != null) {
                    sSql += "FirstResp=" + "'" + data.firstResp + "',";
                }
                if (data.firstRespPct != null) {
                    sSql += "FisrtRespPct=" + data.firstRespPct + ",";
                }
                if (data.ifRespLoan != null) {
                    sSql += "IfRespLoan=" + data.ifRespLoan + ",";
                }
                if (data.interestType != null) {
                    sSql += "InterestType=" + data.interestType + ",";
                }
                if (data.loanCat3 != null) {
                    sSql += "LoanCat3=" + data.loanCat3 + ",";
                }
                if (data.loanPurpose != null) {
                    sSql += "LoanPurpose=" + "'" + data.loanPurpose + "',";
                }
                if (data.loanType3 != null) {
                    sSql += "LoanType3=" + data.loanType3 + ",";
                }
                if (data.loanType5 != null) {
                    sSql += "LoanType5=" + data.loanType5 + ",";
                }
                if (data.origAccNo != null) {
                    sSql += "OrigAccountNo=" + "'" + data.origAccNo + "',";
                }
                if (data.origBMNo != null) {
                    sSql += "OrigBMNo=" + "'" + data.origBMNo + "',";
                }
                if (data.origDueBillNo != null) {
                    sSql += "OrigDueBillNo=" + "'" + data.origDueBillNo + "',";
                }
                if (data.rate != null) {
                    sSql += "Rate=" + data.rate + ",";
                }
                if (data.resultType != null) {
                    sSql += "ResultType=" + data.resultType + ",";
                }
                if (data.sContractNo != null) {
                    sSql += "sContractNo=" + "'" + data.sContractNo + "',";
                }
                if (data.clientType != null) {
                    sSql += "ClientType=" + data.clientType + ",";
                }
                if (data.eComType != null) {
                    sSql += "EcomType=" + data.eComType + ",";
                }
                if (data.eComDeptType != null) {
                    sSql += "EcomDeptType=" + data.eComDeptType + ",";
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
                if (data.decidedBy != null) {
                    sSql += "DecidedBy=" + "'" + data.decidedBy + "',";
                }
                if (data.cnlNo != null) {
                    sSql += "CNLNO=" + "'" + data.cnlNo + "',";
                }
                if (sSql.length() > 0) {
                    sSql += "LastModified='" + sysdate + "'";
                    sSql = "update BMTableApp set " + sSql + " where BMNO='" + bmno + "'";
                }
            } else {
                sSql = "insert into BMTableApp(BMNO,";
                String vals = " values('" + bmno + "',";
                if (data.appAmt != null) {
                    sSql += "AppAmt,";
                    vals += "" + data.appAmt + ",";
                }
                if (data.appDate != null) {
                    sSql += "AppDate,";
                    vals += "'" + util.calToString(data.appDate, "-") + "',";
                }
                if (data.appEndDate != null) {
                    sSql += "AppEndDate,";
                    vals += "'" + util.calToString(data.appEndDate, "-") + "',";
                }
                if (data.appMonths != null) {
                    sSql += "AppMonths,";
                    vals += "" + data.appMonths + ",";
                }
                if (data.appStartDate != null) {
                    sSql += "AppStartDate,";
                    vals += "'" + util.calToString(data.appStartDate, "-") + "',";
                }
                if (data.bmTypeNo != null) {
                    sSql += "TypeNo,";
                    vals += "" + data.bmTypeNo + ",";
                }
                if (data.clientNo != null) {
                    sSql += "ClientNo,";
                    vals += "'" + data.clientNo + "',";
                }
                if (data.contractNo != null) {
                    sSql += "ContractNo,";
                    vals += "'" + data.contractNo + "',";
                }
                if (data.curNo != null) {
                    sSql += "CurNo,";
                    vals += "'" + data.curNo + "',";
                }
                if (data.finalAmt != null) {
                    sSql += "FinalAmt,";
                    vals += "" + data.finalAmt + ",";
                }
                if (data.finalAmt2 != null) {
                    sSql += "FinalAmt2,";
                    vals += "" + data.finalAmt2 + ",";
                }
                if (data.finalCurNo != null) {
                    sSql += "FinalCurNo,";
                    vals += "'" + data.finalCurNo + "',";
                }
                if (data.finalEndDate != null) {
                    sSql += "FinalEndDate,";
                    vals += "'" + util.calToString(data.finalEndDate, "-") + "',";
                }
                if (data.finalMonths != null) {
                    sSql += "FinalMonths,";
                    vals += "" + data.finalMonths + ",";
                }
                if (data.finalRate != null) {
                    sSql += "FinalRate,";
                    vals += "" + data.finalRate + ",";
                }
                if (data.bRate != null) {
                    sSql += "bRate,";
                    vals += "" + data.bRate + ",";
                }
                if (data.fRate != null) {
                    sSql += "fRate,";
                    vals += "" + data.fRate + ",";
                }
                if (data.finalStartDate != null) {
                    sSql += "FinalStartDate,";
                    vals += "'" + util.calToString(data.finalStartDate, "-") + "',";
                }
                if (data.firstResp != null) {
                    sSql += "FirstResp,";
                    vals += "'" + data.firstResp + "',";
                }
                if (data.firstRespPct != null) {
                    sSql += "FisrtRespPct,";
                    vals += "" + data.firstRespPct + ",";
                }
                if (data.ifRespLoan != null) {
                    sSql += "IfRespLoan,";
                    vals += "" + data.ifRespLoan + ",";
                }
                if (data.interestType != null) {
                    sSql += "InterestType,";
                    vals += "" + data.interestType + ",";
                }
                if (data.loanCat3 != null) {
                    sSql += "LoanCat3,";
                    vals += "" + data.loanCat3 + ",";
                }
                if (data.loanPurpose != null) {
                    sSql += "LoanPurpose,";
                    vals += "'" + data.loanPurpose + "',";
                }
                if (data.loanType3 != null) {
                    sSql += "LoanType3,";
                    vals += "" + data.loanType3 + ",";
                }
                if (data.loanType5 != null) {
                    sSql += "LoanType5,";
                    vals += "" + data.loanType5 + ",";
                }
                if (data.origAccNo != null) {
                    sSql += "OrigAccountNo,";
                    vals += "'" + data.origAccNo + "',";
                }
                if (data.origBMNo != null) {
                    sSql += "OrigBMNo,";
                    vals += "'" + data.origBMNo + "',";
                }
                if (data.origDueBillNo != null) {
                    sSql += "OrigDueBillNo,";
                    vals += "'" + data.origDueBillNo + "',";
                }
                if (data.rate != null) {
                    sSql += "Rate,";
                    vals += "" + data.rate + ",";
                }
                if (data.resultType != null) {
                    sSql += "ResultType,";
                    vals += "" + data.resultType + ",";
                }
                if (data.sContractNo != null) {
                    sSql += "sContractNo,";
                    vals += "'" + data.sContractNo + "',";
                }
                if (data.clientType != null) {
                    sSql += "ClientType,";
                    vals += "" + data.clientType + ",";
                }
                if (data.eComType != null) {
                    sSql += "EcomType,";
                    vals += "" + data.eComType + ",";
                }
                if (data.eComDeptType != null) {
                    sSql += "EcomDeptType,";
                    vals += "" + data.eComDeptType + ",";
                }
                if (data.etpScopType != null) {
                    sSql += "EtpScopType,";
                    vals += "" + data.etpScopType + ",";
                }
                if (data.sectorCat1 != null) {
                    sSql += "SectorCat1,";
                    vals += "" + data.sectorCat1 + ",";
                }
                if (data.clientMgr != null) {
                    sSql += "ClientMgr,";
                    vals += "'" + data.clientMgr + "',";
                }
                if (data.decidedBy != null) {
                    sSql += "DecidedBy,";
                    vals += "'" + data.decidedBy + "',";
                }
                if (data.cnlNo != null) {
                    sSql += "CNLNO,";
                    vals += "'" + data.cnlNo + "',";
                }
                sSql += "LastModified)";
                vals += "'" + sysdate + "')";
                sSql = sSql + vals;
            }
            if (dc.executeUpdate(sSql) < 0) {
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                e.printStackTrace();
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when updating UpToDate BMTableApp!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    static public boolean ifExistApp(String bmno) {
        if (bmno == null) {
            return false;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return false;
        }
        Connection con = null;
        Statement st = null;
        boolean ret = false;
        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;

            String sSql = "select * from BMTableApp where BMNO='" + bmno + "'";
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                ret = true;
            }
        }
        catch (Exception e) {
            ret = false;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ret;
        }
    }

    public static BMTableData getBMTable(String bmno) {
        if (bmno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter:BMNO is null when getting BMTable info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB connection when getting BMTable info!");
            return null;
        }
        BMTableData bmdata = null;
        try {
            String sSql = "select * from BMTable where BMNo='" + bmno + "'";
            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                bmdata = new BMTableData();
                bmdata.bmNo = rs.getString("BMNo");
                bmdata.brhID = rs.getString("BrhID");
                bmdata.clientName = rs.getString("ClientName");
                if (bmdata.clientName != null) {
                    bmdata.clientName = DBUtil.fromDB(bmdata.clientName);
                }
                bmdata.clientNo = rs.getString("ClientNo");
                bmdata.initBrhID = rs.getString("InitBrhID");
                bmdata.TypeNo = rs.getInt("TypeNo");
                bmdata.bmStatus = rs.getInt("BMStatus");
            } else {
                Debug.debug(Debug.TYPE_ERROR, "Can not find BMTable Record when getting BMTable info! BMNO=" + bmno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating BMTable!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return bmdata;
        }
    }

    static public int updateBMNo(String bmno, String newbmno) {
        if (bmno == null || newbmno == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB connection when changing BMNO!");
            return ErrorCode.NO_DB_CONN;
        }
        try {
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppAcptBill", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppBillDis", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppBillRedis", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppCarLoan", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppCDPledger", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppCorpAggrLoan", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppEduAiding", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppEstateDev", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppGeneral", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppHousing", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppInactiveLoan", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppLoanContinuation", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMAppProjLoan", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMComments", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMLoanAdmChange", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMLoanRespChange", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMLoanTypeChange", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMPostLoanCheck", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMLoanCAV", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMLoanGranted", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMPldgBillDis", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMPldgBillRedis", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMPldgMort", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMPldgPDAsset", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMPldgSecurity", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMTrans", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMContract", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMDecision", "BMNo", bmno, newbmno);
            }
            if (errorcode >= 0) {
                errorcode = BMTable.updateFld("BMTableApp", "BMNo", bmno, newbmno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when  changing BMNO!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    static public int updateFld(String tblname, String fldname, String oldvalue, String newvalue) {
        if (tblname == null || fldname == null || oldvalue == null || newvalue == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        int errorcode = 0;
        try {
            String sSql;
            sSql = "update " + tblname + " set " + fldname + "='" + newvalue + "' where " +
                    fldname + "='" + oldvalue + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to Execute Update in UpdateFld Utility!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when Executing Update in UpdateFld Utility!Table,Fld,OldVal,NewVal=" + tblname + "," + fldname + "," + oldvalue + "," + newvalue);
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    public static BigDecimal getUnPostCertLoanBal(String clientno) {
        BigDecimal ret = null;
        if (clientno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):ClientNo is null when Getting UnpostCertLoanBal info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting UnpostCertLoanBal info!");
            return null;
        }
        Connection con = null;
        Statement st = null;
        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;
            String sSql = "select sum(finalamt) from bmtable,bmtableapp,bmloangranted where bmtable.bmno=bmtableapp.bmno and bmloangranted.bmno=bmtable.bmno and bmloangranted.AuthorizedStatus<" + EnumValue.AuthorizedStatus_ShouQuanQuXiao + " and bmtable.typeno = " + EnumValue.BMType_DaiKuanZhengDaiKuan + " and bmtable.bmstatus=" + EnumValue.BMStatus_FaFang + " and bmtable.clientno ='" + clientno + "' and bmtableapp.contractno ='" + confitem.LOAN_CERT_ISSUE + "'";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                ret = rs.getBigDecimal(1);
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No Record when Getting UnpostCertLoanBal info!clientno=" + clientno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ret;
        }

    }

    public static BigDecimal getClientLoanBal(String clientno) {
        BigDecimal ret = null;
        if (clientno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):ClientNo is null when Getting getClientLoanBal info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting getClientLoanBal info!");
            return null;
        }
        Connection con = null;
        Statement st = null;
        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;
            String sSql = "select sum(nowbal) from bmtable,rqloanledger where bmtable.clientno='" + clientno + "' and bmtable.bmno=rqloanledger.bmno and rqloanledger.isclosed=0";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                ret = rs.getBigDecimal(1);
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No Record when Getting getClientLoanBal info!clientno=" + clientno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ret;
        }

    }


    public static BigDecimal getUnPostedLoan(String clientno) {
        BigDecimal ret = null;
        if (clientno == null) {
            Debug.debug(Debug.TYPE_ERROR, "Parameter(s):ClientNo is null when Getting getUnPostedLoan info!");
            return null;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB Connection when Getting getUnPostedLoan info!");
            return null;
        }
        Connection con = null;
        Statement st = null;
        try {
            con = dc.getConnection();
            st = con.createStatement();
            ResultSet rs = null;
            String sSql = "select sum(finalamt) from bmtable,bmtableapp,bmloangranted where bmtable.bmno=bmtableapp.bmno and bmtable.bmno=bmloangranted.bmno and bmtable.bmstatus=" + EnumValue.BMStatus_FaFang + " and bmtable.clientno ='" + clientno +
                    "' and bmloangranted.authorizedstatus <>" + EnumValue.AuthorizedStatus_ShouQuanQuXiao + " and ( bmtable.typeno <=" + EnumValue.BMType_FangDiChanKaiFaDaiKuan + " or bmtable.typeno >=" + EnumValue.BMType_GeTiGongShangHuDaiKuan + ")";
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                ret = rs.getBigDecimal(1);
            } else {
                Debug.debug(Debug.TYPE_ERROR, "No Record when Getting getUnPostedLoan info!clientno=" + clientno);
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return ret;
        }

    }

    public static boolean isNormalLoanBMType(int bmtype) {
        if ((bmtype >= EnumValue.BMType_DaiKuanZhengDaiKuan && bmtype <= EnumValue.BMType_FangDiChanKaiFaDaiKuan)
                || (bmtype >= EnumValue.BMType_ChengDuiHuiPiao && bmtype <= EnumValue.BMType_ZhanQi)
                || (bmtype >= EnumValue.BMType_GeTiGongShangHuDaiKuan && bmtype <= EnumValue.BMType_GeRenQiTaDaiKuan)
                )
            return true;
        else
            return false;
    }
}
