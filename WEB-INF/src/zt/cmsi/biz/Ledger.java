package zt.cmsi.biz;

/**
 * <p>Title:台帐处理 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * update by wxj at 20040509
 * @version 1.0
 */

import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.code.BMAcptBill;
import zt.cmsi.pub.code.BMBILLDIS;
import zt.cmsi.pub.code.BMGuarantor;
import zt.cmsi.pub.code.BMPDAssets;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Ledger {

    public static int createAcptBillLedger(String bmno, ContractData contr, BMTableData tbldata, UpToDateApp appdata, String Operator) {
        if (bmno == null || contr == null || tbldata == null || appdata == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        int acptbillno = BMAcptBill.getNextNo();
        if (acptbillno < 0) {
            return ErrorCode.GET_SN_ERROR;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null;

        try {
            String sSql =
                    //"select * from " +
                    "select * from BMAppAcptBill where BMNo='" + bmno + "'";
            String sql = "";
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                sql = "insert into BMAcptBill(AcptBillNo,BMNo,CreateDate,ClientNo,ClientName,AppBank,AppBankAcc,AgreementNo,GuarantyNo,BillNo,BankNoteType," +
                        "Payee,PayeeBank,PayeeBankAcc,IssueDate,DueDate,FaceAmt,MarginAmt,MarginRate,ServiceCharge,IssueBankType,AcptBillStatus,BrhID,Operator,AdvAmt) values(" +
                        acptbillno + ",'" + bmno + "','" + sysdate + "','" +
                        tbldata.clientNo + "',";

                if (tbldata.clientName != null) {
                    sql += "'" + DBUtil.toDB(tbldata.clientName) + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("AppBank") != null) {
                    sql += "'" + rs.getString("AppBank") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("AppBankAcc") != null) {
                    sql += "'" + rs.getString("AppBankAcc") + "',";
                } else {
                    sql += "null,";
                }
                if (contr.contractNo != null) {
                    sql += "'" + contr.contractNo + "',";
                } else {
                    sql += "null,";
                }
                if (contr.sContractNo != null) {
                    sql += "'" + contr.sContractNo + "',";
                } else {
                    sql += "null,";

                }
                if (contr.bankNoteNo != null) {
                    sql += "'" + contr.bankNoteNo + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getString("BankNoteType") == null) {
                    sql += EnumValue.BankNoteType_YinHangChengDuiHuiPiao + ",";
                } else {
                    sql += rs.getInt("BankNoteType") + ",";

                }
                if (rs.getString("Payee") != null) {
                    sql += "'" + rs.getString("Payee") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("PayeeBank") != null) {
                    sql += "'" + rs.getString("PayeeBank") + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getString("PayeeBankAcc") != null) {
                    sql += "'" + rs.getString("PayeeBankAcc") + "',";
                } else {
                    sql += "null,";
                }
                if (appdata.finalStartDate != null) {
                    sql += "'" + util.calToString(appdata.finalStartDate, "-") + "',";
                } else {
                    sql += "null,";
                }
                if (appdata.finalEndDate != null) {
                    sql += "'" + util.calToString(appdata.finalEndDate, "-") + "',";
                } else {
                    sql += "null,";

                }
                if (appdata.finalAmt != null) {
                    sql += appdata.finalAmt + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getBigDecimal("MarginAmt") != null) {
                    sql += rs.getBigDecimal("MarginAmt") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getBigDecimal("MarginRate") != null) {
                    sql += rs.getBigDecimal("MarginRate") + ",";
                } else {
                    sql += "null,";

                    //service charge
                }
                if (rs.getBigDecimal("ServiceCharge") != null) {
                    sql += rs.getBigDecimal("ServiceCharge") + ",";
                } else {
                    sql += "null,";

                }

                if (rs.getString("IssueBankType") != null) {
                    sql += rs.getString("IssueBankType") + ",";
                } else {
                    sql += "null,";

                }
                sql += EnumValue.AcptBillStatus_FaFang + ",";
                if (tbldata.brhID != null) {
                    sql += "'" + tbldata.brhID + "',";
                } else {
                    sql += "null,";
                }
                sql += "'" + Operator + "',";
                sql += "0" + ")";

                Debug.debug(Debug.TYPE_SQL, sql);
                errorcode = st.executeUpdate(sql);
                if (errorcode < 0) {
                    errorcode = ErrorCode.DB_INSERT_FAILED;
                }
            } else {
                errorcode = ErrorCode.ACPT_BILL_APP_NOT_EXIST;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when creating Acceptance Bill Ledger!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    public static int LoopEntryAcptBillManually() {
        int errorcode = 0;
        int failed = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        RecordSet rs = null;
        int acptbill = 0;


        String bmno = null;
        try {
            String sSql = "Select acptbillno from bmacptbill where bmno is null and clientno is not null";
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs = dc.executeQuery(sSql);
            while (rs.next()) {
                acptbill = rs.getInt("acptbillno");
                System.out.println("Aceptance Bill Processing:" + acptbill);
                errorcode = entryAcptBillManually(acptbill);
                if (errorcode < 0) {
                    failed++;
                    System.out.println("Error occurred, code=" + errorcode);
                    break;
                } else
                    System.out.println("OK:)");
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating credit authorization!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            System.out.println("Total failed:" + failed + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int LoopEntryBillDisManually() {
        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        RecordSet rs = null;
        int acptbill = 0;
        int failed = 0;


        String bmno = null;
        try {
            String sSql = "Select billdisno from bmbilldis where bmno is null and clientno is not null";
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs = dc.executeQuery(sSql);
            while (rs.next()) {
                acptbill = rs.getInt("billdisno");
                System.out.println("Bill Discount Processing:" + acptbill);
                errorcode = entryBillDisManually(acptbill);
                if (errorcode < 0) {
                    failed++;
                    System.out.println("Error occurred, code=" + errorcode);
                    break;
                } else
                    System.out.println("OK:)");
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating credit authorization!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            System.out.println("Total failed:" + failed + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int entryAcptBillManually(long acptbillno) {

        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        RecordSet rs = null;


        String bmno = null;
        try {
            String sSql = "Select brhid,clientno from bmacptbill where acptbillno=" + acptbillno;
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                String clientno = rs.getString("clientno");
                String brhid = rs.getString("brhid");
                bmno = BMTable.createBMTable(EnumValue.BMType_ChengDuiHuiPiao,
                        clientno,
                        brhid,
                        brhid,
                        "000000");

                if (bmno != null) {
                    errorcode = BMTable.updateTableStatus(bmno, EnumValue.BMStatus_FaFang,
                            "000000");
                    if (errorcode >= 0) {
                        UpToDateApp data = null;

                        data = new UpToDateApp();
                        data.appAmt = new BigDecimal(0);
                        data.appDate = SystemDate.getSystemDate1();
                        data.appEndDate = SystemDate.getSystemDate1();
                        data.appStartDate = SystemDate.getSystemDate1();
                        data.finalStartDate = SystemDate.getSystemDate1();
                        data.finalEndDate = SystemDate.getSystemDate1();
                        data.finalAmt = new BigDecimal(0);
                        data.finalRate = new BigDecimal(0);
                        data.ifRespLoan = new Integer(1);
                        data.firstResp = "000000";
                        data.decidedBy = "000000";
                        data.firstRespPct = new BigDecimal(0);

                        errorcode = BMTable.updateUpToDateApp(bmno, data);
                        if (errorcode >= 0) {
                            sSql = "update bmacptbill set bmno='" + bmno +
                                    "' where acptbillno=" + acptbillno;
                            Debug.debug(Debug.TYPE_SQL, sSql);

                            if (dc.executeUpdate(sSql) < 0)
                                errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    }
                } else
                    errorcode = ErrorCode.CAN_NOT_CREATE_BMTABLE;
            } else
                errorcode = ErrorCode.ACPT_BILL_APP_NOT_EXIST;

        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating credit authorization!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }


    public static int entryBillDisManually(long billdisno) {

        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        RecordSet rs = null;


        String bmno = null;
        try {
            String sSql = "Select brhid,clientno from bmbilldis where billdisno=" + billdisno;
            Debug.debug(Debug.TYPE_SQL, sSql);

            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                String clientno = rs.getString("clientno");
                String brhid = rs.getString("brhid");
                bmno = BMTable.createBMTable(EnumValue.BMType_TieXian,
                        clientno,
                        brhid,
                        brhid,
                        "000000");

                if (bmno != null) {
                    errorcode = BMTable.updateTableStatus(bmno, EnumValue.BMStatus_FaFang,
                            "000000");
                    if (errorcode >= 0) {
                        UpToDateApp data = null;

                        data = new UpToDateApp();
                        data.appAmt = new BigDecimal(0);
                        data.appDate = SystemDate.getSystemDate1();
                        data.appEndDate = SystemDate.getSystemDate1();
                        data.appStartDate = SystemDate.getSystemDate1();
                        data.finalStartDate = SystemDate.getSystemDate1();
                        data.finalEndDate = SystemDate.getSystemDate1();
                        data.finalAmt = new BigDecimal(0);
                        data.finalRate = new BigDecimal(0);
                        data.ifRespLoan = new Integer(1);
                        data.firstRespPct = new BigDecimal(0);
                        data.firstResp = "000000";
                        data.decidedBy = "000000";

                        errorcode = BMTable.updateUpToDateApp(bmno, data);
                        if (errorcode >= 0) {
                            sSql = "update bmbilldis set bmno='" + bmno +
                                    "' where billdisno=" + billdisno;
                            Debug.debug(Debug.TYPE_SQL, sSql);

                            if (dc.executeUpdate(sSql) < 0)
                                errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    }
                } else
                    errorcode = ErrorCode.CAN_NOT_CREATE_BMTABLE;
            } else
                errorcode = ErrorCode.ACPT_BILL_APP_NOT_EXIST;

        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating credit authorization!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }


    public static int creatBillDisLedger(String bmno, ContractData contr, BMTableData tbldata, UpToDateApp appdata, String Operator) {
        if (bmno == null || contr == null || tbldata == null || appdata == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        int acptbillno;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null, st1 = null;

        try {
            String sSql =
                    //"select * from " +
                    "select * from BMPldgBillDis where BMNo='" + bmno + "'";
            String sql = "";
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            st1 = con.createStatement();
            rs = st.executeQuery(sSql);
            boolean hasOne = false;

            while (rs.next()) {
                hasOne = true;
                acptbillno = BMBILLDIS.getNextNo();
                if (acptbillno < 0) {
                    errorcode = ErrorCode.GET_SN_ERROR;
                    break;
                }

                sql = "insert into BMBillDis (BillDisNo,BMNo,CreateDate,ClientNo,ClientName,AgreementNo,BillDisStatus,BrhID," +
                        "Operator,BillDisType,BankNoteType,BankNoteNo,IssueDate,DueDate,FaceAmt,AcptBy,Payee,IssuerAcc,disdate,actdisamt,IssuerBank) values(" +
                        acptbillno + ",'" + bmno + "','" + sysdate + "','" + tbldata.clientNo + "',";

                if (tbldata.clientName != null) {
                    sql += "'" + DBUtil.toDB(tbldata.clientName) + "',";
                } else {
                    sql += "null,";

                }
                if (contr.contractNo != null) {
                    sql += "'" + contr.contractNo + "',";
                } else {
                    sql += "null,";

                }
                sql += EnumValue.BillDisStatus_TieXian + ",";
                if (tbldata.brhID != null) {
                    sql += "'" + tbldata.brhID + "',";
                } else {
                    sql += "null,";

                }
                sql += "'" + Operator + "',";
                sql += rs.getInt("BillDisType") + "," + rs.getInt("BankNoteType") + ",";

                if (rs.getString("BankNoteNo") != null) {
                    sql += "'" + rs.getString("BankNoteNo") + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getDate("IssueDate") != null) {
                    sql += "'" + util.calToString(util.dateToCalendar(rs.getDate("IssueDate")), "-") + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getDate("DueDate") != null) {
                    sql += "'" + util.calToString(util.dateToCalendar(rs.getDate("DueDate")), "-") + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getBigDecimal("FaceAmt") != null) {
                    sql += rs.getBigDecimal("FaceAmt") + ",";
                } else {
                    sql += "null,";

                }
                if (rs.getString("AcptBy") != null) {
                    sql += "'" + rs.getString("AcptBy") + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getString("Payee") != null) {
                    sql += "'" + rs.getString("Payee") + "',";
                } else {
                    sql += "null,";

                }
                if (rs.getString("IssuerAcc") != null) {
                    sql += "'" + rs.getString("IssuerAcc") + "',";
                } else {
                    sql += "null,";

                }
                if (contr.beginDate != null) {
                    sql += "'" + util.calToString(contr.beginDate, "-") + "',";
                } else {
                    sql += "null,";

                }

                if (contr.actualDisAmt != null) {
                    sql += contr.actualDisAmt + ",";
                } else {
                    sql += "0,";
                }

                if (rs.getString("IssuerBank") != null) {
                    sql += "'" + rs.getString("IssuerBank") + "')";
                } else {
                    sql += "null)";

                }
                Debug.debug(Debug.TYPE_SQL, sql);
                errorcode = st1.executeUpdate(sql);
                if (errorcode < 0) {
                    errorcode = ErrorCode.DB_INSERT_FAILED;
                    break;
                }
            }
            //if(hasOne == false) errorcode = ErrorCode.BILLDIS_APP_NO_RECORD;

            //  errorcode = ErrorCode.ACPT_BILL_APP_NOT_EXIST;
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when creating Bill Discount Ledger!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st1 != null) {
                    st1.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int creatBillRedisLedger(String bmno, ContractData contr, BMTableData tbldata, UpToDateApp appdata, String Operator) {
        if (bmno == null || contr == null || tbldata == null || appdata == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        int acptbillno;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null, st1 = null;

        try {
            String sSql =
                    //"select * from " +
                    "select * from BMPldgBillRedis where BMNo='" + bmno + "'";
            String sql = "";
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            st1 = con.createStatement();
            rs = st.executeQuery(sSql);
            boolean hasOne = false;

            while (rs.next()) {
                hasOne = true;
                if (rs.getString("BillDisNo") == null) {
                    errorcode = ErrorCode.BILLDIS_LEDGER_IS_NULL;
                    break;
                }
                acptbillno = rs.getInt("BillDisNo");

                sql = "update BMBillDis set GuarantyNo='" + contr.contractNo + "',BillDisStatus=" + EnumValue.BillDisStatus_ZhuanTieXian + ",";
                if (tbldata.clientName != null) {
                    sql += "RedisBy='" + DBUtil.toDB(tbldata.clientName) + "',";
                }
                if (contr.beginDate != null) {
                    sql += "RedisDate='" + util.calToString(contr.beginDate, "-") + "',";
                }
                if (rs.getBigDecimal("PledgeAmt") != null) {
                    sql += "RedisAmt=" + rs.getBigDecimal("PledgeAmt") + ",";
                }
                if (rs.getString("BillRedisType") != null) {
                    sql += "BillRedisType=" + rs.getInt("BillRedisType") + ",";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += " where BillDisNo=" + acptbillno;

                Debug.debug(Debug.TYPE_SQL, sql);
                errorcode = st1.executeUpdate(sql);
                if (errorcode < 0) {
                    errorcode = ErrorCode.DB_UPDATE_FAILED;
                    break;
                }
            }
            //if(hasOne == false) errorcode = ErrorCode.BILLDIS_APP_NO_RECORD;

            //  errorcode = ErrorCode.ACPT_BILL_APP_NOT_EXIST;
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when creating Bill Rediscount Ledger!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st1 != null) {
                    st1.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int creatPDAssetsLedger(String bmno, BMTableData tbldata, UpToDateApp appdata, String Operator) {
        if (bmno == null || tbldata == null || appdata == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        int acptbillno;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        int errorcode = 0;
        ResultSet rs = null, rs2 = null;
        Statement st = null, st1 = null;
        Statement st_wxj = null;
        try {
            //Edit by wxj 增加了写抵债资产台帐基本信息
            String pdmainno = zt.cms.pub.code.SerialNumber.getNextSN("BMPDASMAIN", "PDMAINNO") + "";
            st_wxj = con.createStatement();
            String sSql_wxj = "insert into bmpdasmain (PDMAINNO,APPDATE,ACQDATE,CLIENTNAME,LOANBRANCH,PDACQMETHOD,CURNO,APPAMT,APPAMT2,LOCATION,OPERATOR,BRHID,BMNO,REMARK) ";
            sSql_wxj += "select '" + pdmainno + "',APPDATE,ACQDATE,REPAYSRC,LOANBRANCH,PDACQMETHOD,CURNO,APPAMT,APPAMT2,LOCATION,OPERATOR,BRHID,BMNO,REMARK ";
            sSql_wxj += "from BMAPPINACTIVELOAN ";
            sSql_wxj += "where bmno='" + bmno + "'";
            //System.out.println("sSql_wxj:"+sSql_wxj);
            st_wxj.execute(sSql_wxj);
            //if(!st_wxj.execute(sSql_wxj)) return -1;

            String sSql = "select * from BMPldgPDAsset where BMNo='" + bmno + "'";
            String sql = "";
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            st1 = con.createStatement();
            String sSql2 = "select PDAcqMethod from BMAppInactiveLoan where BMNO='" + bmno + "'";
            Integer acqMethod = null;
            rs2 = st.executeQuery(sSql2);
            if (rs2.next()) {
                acqMethod = (rs2.getString("PDAcqMethod") == null ? null : new Integer(rs2.getInt("PDAcqMethod")));
            }
            rs2 = null;

            rs = st.executeQuery(sSql);
            boolean hasOne = false;
            while (rs.next()) {
                hasOne = true;
                acptbillno = BMPDAssets.getNextNo();
                if (acptbillno < 0) {
                    errorcode = ErrorCode.GET_SN_ERROR;
                    break;
                }

                sql = "insert into BMPDAssets (PDMainNo,PDNo,Operator,CreateDate,PDStatus,PDAcqMethod,BrhId,LedgerAmt,Bal,OrigBMNo," +
                        "PDPldgType,Spec,Spec2,IfPermitOK,PermitNo,IfTransferred,Qty,RecvDate,Location,PDQltStatus,PDLevel,Amt,Name,SpecDesc,Owner,MeasurementUnit,Remark) values('" + pdmainno + "'," +
                        acptbillno + ",'" + Operator + "','" + sysdate + "'," + EnumValue.PDStatus_JianLi + ",";

                if (acqMethod != null) {
                    sql += acqMethod.intValue() + ",";
                } else {
                    sql += "null,";
                }
                if (tbldata.brhID != null) {
                    sql += "'" + tbldata.brhID + "',";
                } else {
                    sql += "null,";
                }
                sql += "0,";
                if (rs.getBigDecimal("Amt") != null) {
                    sql += rs.getBigDecimal("Amt") + ","; //ledgeramt
                } else {
                    sql += "null,";
                }
                if (bmno != null) {
                    sql += "'" + bmno + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("PDPldgType") != null) {
                    sql += rs.getInt("PDPldgType") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getString("Spec") != null) {
                    sql += rs.getInt("Spec") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getString("Spec2") != null) {
                    sql += rs.getInt("Spec2") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getString("IfPermitOK") != null) {
                    sql += rs.getInt("IfPermitOK") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getString("PermitNo") != null) {
                    sql += "'" + rs.getString("PermitNo") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("IfTransferred") != null) {
                    sql += rs.getInt("IfTransferred") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getBigDecimal("Qty") != null) {
                    sql += rs.getBigDecimal("Qty") + ","; //ledgeramt
                } else {
                    sql += "null,";
                }
                if (rs.getDate("RecvDate") != null) {
                    sql += "'" + util.calToString(util.dateToCalendar(rs.getDate("RecvDate")), "-") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("Location") != null) {
                    sql += "'" + rs.getString("Location") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("PDQltStatus") != null) {
                    sql += rs.getInt("PDQltStatus") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getString("PDLevel") != null) {
                    sql += rs.getInt("PDLevel") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getBigDecimal("Amt") != null) {
                    sql += rs.getBigDecimal("Amt") + ","; //ledgeramt
                } else {
                    sql += "null,";
                }
                if (rs.getString("Name") != null) {
                    sql += "'" + rs.getString("Name") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("SpecDesc") != null) {
                    sql += "'" + rs.getString("SpecDesc") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("Owner") != null) {
                    sql += "'" + rs.getString("Owner") + "',";
                } else {
                    sql += "null,";
                }
                if (rs.getString("MeasurementUnit") != null) {
                    sql += rs.getInt("MeasurementUnit") + ",";
                } else {
                    sql += "null,";
                }
                if (rs.getString("Remark") != null) {
                    sql += "'" + rs.getString("Remark") + "')";
                } else {
                    sql += "null)";

                }
                Debug.debug(Debug.TYPE_SQL, sql);
                System.out.println("sql:" + sql);
                errorcode = st1.executeUpdate(sql);
                if (errorcode < 0) {
                    errorcode = ErrorCode.DB_INSERT_FAILED;
                    break;
                }
            }
            if (hasOne == false) {
                errorcode = ErrorCode.PDASSETS_APP_NO_RECORD;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when creating PDAssets ledger!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (st_wxj != null) {
                    st_wxj.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int creatILCAVLedger(String bmno, BMTableData tbldata, UpToDateApp appdata, String Operator) {
        if (bmno == null || tbldata == null || appdata == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        int acptbillno = BMAcptBill.getNextNo();
        if (acptbillno < 0) {
            return ErrorCode.GET_SN_ERROR;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null;

        try {
            String sSql =
                    //"select * from " +
                    "select Reason from BMAppInactiveLoan where BMNo='" + bmno + "'";
            String sql = "";
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                sql = "insert into BMLoanCAV(BMNo,CancelDate,CancelAmt,CancelInt,ClientNo,ClientName,OrigBMNo,BrhID,Operator,Reason) values('" +
                        bmno + "','" + sysdate + "',";

                if (appdata.finalAmt != null) {
                    sql += appdata.finalAmt + ",";
                } else {
                    sql += "null,";
                }
                if (appdata.finalAmt2 != null) {
                    sql += appdata.finalAmt2 + ",";
                } else {
                    sql += "null,";

                }
                if (tbldata.clientNo != null) {
                    sql += "'" + tbldata.clientNo + "',";
                } else {
                    sql += "null,";

                }
                if (tbldata.clientName != null) {
                    sql += "'" + DBUtil.toDB(tbldata.clientName) + "',";
                } else {
                    sql += "null,";
                }
                if (appdata.origBMNo != null) {
                    sql += "'" + appdata.origBMNo + "',";
                } else {
                    sql += "null,";
                }
                if (tbldata.brhID != null) {
                    sql += "'" + tbldata.brhID + "',";
                } else {
                    sql += "null,";
                }
                sql += "'" + Operator + "',";

                String reason = rs.getString("Reason");
                if (reason != null) {
                    sql += "'" + reason + "',";
                } else {
                    sql += "null,";
                }
                sql = sql.substring(0, sql.length() - 1) + ")";

                Debug.debug(Debug.TYPE_SQL, sql);
                errorcode = st.executeUpdate(sql);
                if (errorcode < 0) {
                    errorcode = ErrorCode.DB_INSERT_FAILED;
                }
            } else {
                errorcode = ErrorCode.ACPT_BILL_APP_NOT_EXIST;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when creating Inactive Loan CAV ledger!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int creatBMGuarantor(String bmno) {
        if (bmno == null) {
            return ErrorCode.PARAM_IS_NULL;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null, st2 = null;

        try {
            String sSql =
                    //"select * from " +
                    "select * from BMPldgSecurity where BMNo='" + bmno + "'";
            String sql = "";
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            st2 = con.createStatement();
            rs = st.executeQuery(sSql);
            int seqno;

            while (rs.next()) {
                seqno = BMGuarantor.getNextNo();
                if (seqno < 0) {
                    errorcode = ErrorCode.GET_SN_ERROR;
                    break;
                } else {
                    sql = "insert into BMGuarantor(BMNo,SeqNo,ClientNo,ClientName,LoanType3,SecurityAmt,Operator,IDType,ID,LoanCard,Remark) values('" +
                            bmno + "'," + seqno + ",";

                    if (rs.getString("ClientNo") != null) {
                        sql += "'" + rs.getString("ClientNo") + "',";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getString("ClientName") != null) {
                        sql += "'" + rs.getString("ClientName") + "',";
                    } else {
                        sql += "null,";

                    }

                    if (rs.getString("LoanType3") != null) {
                        sql += rs.getInt("LoanType3") + ",";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getBigDecimal("SecurityAmt") != null) {
                        sql += rs.getBigDecimal("SecurityAmt") + ",";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getString("Operator") != null) {
                        sql += "'" + rs.getString("Operator") + "',";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getString("IDType") != null) {
                        sql += rs.getInt("IDType") + ",";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getString("ID") != null) {
                        sql += "'" + rs.getString("ID") + "',";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getString("LoanCard") != null) {
                        sql += "'" + rs.getString("LoanCard") + "',";
                    } else {
                        sql += "null,";

                    }
                    if (rs.getString("Remark") != null) {
                        sql += "'" + rs.getString("Remark") + "')";
                    } else {
                        sql += "null)";

                    }
                    Debug.debug(Debug.TYPE_SQL, sql);
                    errorcode = st2.executeUpdate(sql);
                    if (errorcode < 0) {
                        errorcode = ErrorCode.DB_INSERT_FAILED;
                    }
                }
            }

        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR,
                    "Exception when creating BMGuator Info!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st2 != null) {
                    st2.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static int createCreditLimit(String bmno, BMTableData tbldata, UpToDateApp appdata, String Operator) {
        DatabaseConnection dc = MyDB.getInstance().apGetConn();

        //CreditLimitData dt = CreditLimit.getCreditLimit(tbldata.clientNo,EnumValue.BMType_DaiKuanZhengDaiKuan);
        int ret = dc.executeUpdate("delete from bmcreditlimit where clientno='" + tbldata.clientNo + "'");

        if (ret >= 0) {
            String str = "insert into bmcreditlimit(typeno,clientno,limitapproved,loanbal,disabled,startdate,"
                    + "enddate,limitcommit,creditlimit,lastmodified,hasbadloan,operator,"
                    + "ifresploan,firstresp,decidedby) "
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            //12项
            Connection con = dc.getConnection();
            try {
                //System.out.println("TTTTTTTTTTTTTTTTTTTTt");
                PreparedStatement pst = con.prepareStatement(str);

                if (appdata.clientType == null || appdata.clientType.intValue() == EnumValue.ClientType_GeTiGongShangHu ||
                        appdata.clientType.intValue() == EnumValue.ClientType_NongHu || appdata.clientType.intValue() == EnumValue.ClientType_QiTa) {
                    pst.setInt(1, EnumValue.BMType_DaiKuanZhengDaiKuan);
                } else {
                    pst.setInt(1, EnumValue.BMType_QiYeLiuDongZiJinDaiKuan);
                }

                pst.setString(2, tbldata.clientNo);
                pst.setDouble(3, 0);//审批额度

//          if(dt != null && dt.loanBal != null) pst.setDouble(4,dt.loanBal.doubleValue());
//          else
                //CachedRowSet cs = zt.platform.cachedb.ConnectionManager.getInstance().getRs("SELECT sum(nowbal) as bal FROM rqloanledger WHERE bmno IN (SELECT bmno FROM BMTable WHERE clientno='"+ tbldata.clientNo+"' AND typeno=1)");
                String tSql = "SELECT sum(nowbal) as bal FROM rqloanledger WHERE bmno IN (SELECT bmno FROM BMTable WHERE clientno='" + tbldata.clientNo + "' AND typeno=1)";
                Debug.debug(Debug.TYPE_SQL, tSql);
                RecordSet cs = dc.executeQuery(tSql);
                if (cs != null && cs.next()) {
                    //System.out.println("------------------------------bal="+cs.getDouble("bal"));
                    if (cs.getObject("bal") != null)
                        pst.setDouble(4, cs.getDouble("bal"));
                    else
                        pst.setDouble(4, 0);
                } else
                    pst.setDouble(4, 0);

                pst.setInt(5, EnumValue.LoanCertSts_KeYou);
                pst.setDate(6, new java.sql.Date(appdata.finalStartDate.getTime().getTime()));

                pst.setDate(7, new java.sql.Date(appdata.finalEndDate.getTime().getTime()));

//          if(dt != null && dt.limitCommit != null) pst.setDouble(8,dt.limitCommit.doubleValue());
//          else
                pst.setDouble(8, 0);

                pst.setDouble(9, appdata.finalAmt.doubleValue());
                pst.setDate(10, new java.sql.Date(SystemDate.getSystemDate3().getTime()));
                pst.setInt(11, 0);
                pst.setString(12, Operator);
                pst.setInt(13, appdata.ifRespLoan.intValue());
                pst.setString(14, appdata.firstResp);
                pst.setString(15, appdata.decidedBy);
                pst.execute();

            }
            catch (Exception ex) {
                ex.printStackTrace();
                MyDB.getInstance().apReleaseConn(-1);
                return ErrorCode.DB_INSERT_FAILED;
            }

        } else {
            MyDB.getInstance().apReleaseConn(-1);
            return -1;
        }
        MyDB.getInstance().apReleaseConn(1);
        return 0;
    }

    public static void main(String[] args) {
        //Ledger ledger1 = new Ledger();
        //System.out.println(Ledger.creatBMGuarantor("10000000009"));
        //System.out.println(Ledger.entryAcptBillManually(1));
        //System.out.println(Ledger.entryBillDisManually(1));
        System.out.println(Ledger.LoopEntryAcptBillManually());
        System.out.println(Ledger.LoopEntryBillDisManually());
    }

}
