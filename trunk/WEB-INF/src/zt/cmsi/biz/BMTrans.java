package zt.cmsi.biz;

import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.code.BMTransNo;
import zt.cmsi.pub.define.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class BMTrans {

    /**
     * ������ҵ����ϸ��¼
     * 1. ����µ�BMTRANSNO
     * 2. TRANSSTATUS=ִ��
     * 3. CREATEDATE=ҵ������
     * 4. ���BMActType,OPERBRNID,OPERATOR�Ƿ����, �����ڷ��ش������
     * <p/>
     * �ɹ�,����BMTRANSNO, ʧ�ܷ��ش������
     *
     * @param BMNo
     * @param BMActType
     * @param OperBrhID
     * @param Operator
     * @return int
     * @roseuid 3FE522240079
     */
    public static int createBMTrans(String BMNo, int BMActType, String OperBrhID, String Operator) {
        if (BMNo == null || OperBrhID == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        if (SCUser.isExist(Operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }
        if (SCBranch.isExist(OperBrhID) == false) {
            return ErrorCode.BRANCH_NOT_FOUND;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        int errorcode = 0;
        int bmtransno = BMTransNo.getNextNo();
        if (bmtransno < 0) {
            return ErrorCode.GET_SN_ERROR;
        }

        try {
            String sSql =
                    "insert into BMTrans (BMNo,BMTransNo,BMActType,OperBrhID,TransStatus,CreateDate,Operator,Viewed) values('" +
                            BMNo + "'," + bmtransno + "," + BMActType + ",'" + OperBrhID + "'," + EnumValue.TransStatus_ZhiXing +
                            ",'" + sysdate + "','" + Operator + "'," + EnumValue.YesNo_No + ")";

            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to insert BMTrans!");
                errorcode = ErrorCode.DB_INSERT_FAILED;
            } else {
                errorcode = bmtransno;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating BMTrans!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    public static BMTransData getBMTransData(String bmno, int BMTransNo) {
        if (bmno == null) {
            return null;
        }
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return null;
        }

        try {
            String sSql =
                    "select BMActType,OperBrhID,BMNo,BMTransNo,TransStatus from BMTrans where BMTransNo=" + BMTransNo + " and BMNo='" + bmno + "'";
            ;

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                data = new BMTransData();
                data.actType = rs.getInt(0);
                data.operBrhID = rs.getString(1);
                data.bmNo = rs.getString(2);
                data.bmTransNo = rs.getInt(3);
                data.transStatus = rs.getInt(4);
            } else {
                Debug.debug(Debug.TYPE_WARNING, "No DB Record when getting BMTrans Data!");
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when getting ActType in BMTrans table!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
            return data;
        }

    }

    /**
     * ҵ��ȡ��
     * ������TransStatus��Ϊȡ��
     * ��������BMActType�ó�ȡ�������
     * ��������������������������������ͨ��
     * ������������������������������ͨ��
     * ��һ������������һ��������ͨ��
     * ��������ͬ��������ͬȡ��
     * BMActType������->ȡ��
     * ����ȡ���£ͣԣ��£̣�
     * 4. �������ά����
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE5250F03D0
     */
    public static int cancelBMTrans(String bmNo, int BMTransNo, String Operator) {
        int errorcode = 0;

        if (bmNo == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        if (SCUser.isExist(Operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        BMTransData bmdata = BMTrans.getBMTransData(bmNo, BMTransNo);
        if (bmdata == null) {
            return ErrorCode.GET_BMTRANSDATA_ERROR;
        }

        String sysdate = SystemDate.getSystemDate2();
        if (sysdate == null) {
            return ErrorCode.BUSI_DATE_NOT_FOUND;
        }

        int acttype = bmdata.actType;
        int statustype;

        if (acttype == EnumValue.BMActType_DiSanJiShenPi) {
            statustype = EnumValue.BMStatus_SanJiShenPiBuTongGuo;
        } else if (acttype == EnumValue.BMActType_DiErJiShenPi) {
            statustype = EnumValue.BMStatus_ErJiShenPiBuTongGuo;
        } else if (acttype == EnumValue.BMActType_DiYiJiShenPi) {
            statustype = EnumValue.BMStatus_YiJiShenPiBuTongGuo;
        } else if (acttype == EnumValue.BMActType_HeTong) {
            statustype = EnumValue.BMStatus_HeTongQuXiao;
        } else {
            statustype = EnumValue.BMStatus_QuXiao;
        }
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        try {
            errorcode = BMTable.cancelBMTable(bmNo, statustype, Operator);
            if (errorcode >= 0) {
                String sSql =
                        "update BMTrans set Operator='" + Operator + "',EndDate='" +
                                sysdate + "',TransStatus=" +
                                EnumValue.TransStatus_WanCheng + " where BMTransNo=" + BMTransNo +
                                " and BMNo='" + bmNo + "'";

                Debug.debug(Debug.TYPE_SQL, sSql);
                int prodrtn = dc.executeUpdate(sSql);
                if (prodrtn < 0) {
                    Debug.debug(Debug.TYPE_ERROR, "failed to cancel BMTrans!");
                    errorcode = ErrorCode.DB_UPDATE_FAILED;
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when cancelling BMTrans!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * @param BMTransNo
     * @param Operator
     * @return int
     * @roseuid 3FE6F2790006
     */
    public static int compltTrans(String bmNo, int BMTransNo, String Operator) {
        if (bmNo == null || Operator == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        BMTransData transdata = BMTrans.getBMTransData(bmNo, BMTransNo);

        if (transdata == null) {
            return ErrorCode.GET_BMTRANSDATA_ERROR;
        }
        if (transdata.transStatus != EnumValue.TransStatus_ZhiXing) {
            return ErrorCode.BM_TRANS_NOT_EXECUTING;
        }

        if (transdata.actType == EnumValue.BMActType_DengJi) {
            return BMTrans.compltApp(bmNo, BMTransNo, transdata, Operator);
        } else if (transdata.actType == EnumValue.BMActType_DiSanJiShenPi) {
            return BMTrans.compltReview3rd(bmNo, BMTransNo, transdata, Operator);
        } else if (transdata.actType == EnumValue.BMActType_DiErJiShenPi) {
            return BMTrans.compltReview2nd(bmNo, BMTransNo, transdata, Operator);
        } else if (transdata.actType == EnumValue.BMActType_DiYiJiShenPi) {
            return BMTrans.compltReview1st(bmNo, BMTransNo, transdata, Operator);
        } else if (transdata.actType == EnumValue.BMActType_HeTong) {
            return BMTrans.compltContract(bmNo, BMTransNo, transdata, Operator);
        } else if (transdata.actType == EnumValue.BMActType_ShouQuan) {
            return BMTrans.compltAuthorization(bmNo, BMTransNo, transdata, Operator);
        } else {
            return ErrorCode.BMTYPE_ERROR_COMPLT_TRANS;
        }
    }

    /**
     * .ͳ�Ʊ��£ͣԣң��ΣӣΣϵ����������Ƿ�ͨ��
     * ������ԣң��Σӵģ£ͣģţãɣӣɣϣλ�BMCOMMENT���κ����Ϊ��ͬ�⣬����Ϊ��\uFFFD
     * ��⣬����Ϊͬ�\uFFFD
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE8548501B3
     */
    public static int getReviewResult(String bmNo, int BMTransNo) {
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        RecordSet rs = null;
        int errorcode = EnumValue.ResultType_TongYi;

        try {
            String sSql =
                    "select ResultType from BMComments where BMNo='" + bmNo + "' and BMTransNo=" + BMTransNo;

            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            boolean nocomments = true;
            while (rs.next()) {
                nocomments = false;
                if (rs.getInt(0) == EnumValue.ResultType_BuTongYi) {
                    errorcode = EnumValue.ResultType_BuTongYi;
                    break;
                }
            }
            //if(nocomments == true) errorcode = ErrorCode.NO_ANY_COMMENTS;
            //else
            if (true) {
                //if (errorcode != EnumValue.ResultType_BuTongYi) {
                if (true) {
                    sSql = "select ResultType from BMDecision where BMNo='" + bmNo +
                            "' and BMTransNo=" + BMTransNo;
                    Debug.debug(Debug.TYPE_SQL, sSql);
                    rs = dc.executeQuery(sSql);
                    if (rs.next()) {
                        if (errorcode == EnumValue.ResultType_BuTongYi &&
                                rs.getInt(0) == EnumValue.ResultType_BuTongYi) {
                            errorcode = ErrorCode.DECISION_TYPE_MUST_BE_BUTONGYI;
                        } else {
                            if (rs.getInt(0) != EnumValue.ResultType_TongYi) {
                                errorcode = EnumValue.ResultType_BuTongYi;
                            }
                        }

                    } else {
                        errorcode = ErrorCode.NO_ANY_DECISION;
                    }
                }
            }
        }
        catch (Exception e) {
            //data = null;
            errorcode = ErrorCode.EXCPT_FOUND;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when getting Decision Result!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    /**
     * �жһ�Ʊ��Ʊ�ж�
     * <p/>
     * 1. ����Ƿ��Ѿ��б�BMNO����Ȩ��Ϣ, ����з��ش���
     * 2. ʹ�ñ�BMNO������Ȩ̨��
     * 3. ������Ȩ��Ϣ
     *
     * @param acptBillNo
     * @return int
     * @roseuid 3FE8E9EE03AF
     */
    public static int acptBillRecv(int acptBillNo, String operator) {
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        String bmno = BMTrans.acptBillNoToBMNo(acptBillNo);
        if (bmno == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_ACPTBILLLEDGER;
        }

        int errorcode = 0;
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        UpToDateApp appdata = null;

        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        Statement st = null;

        try {
            st = con.createStatement();
            String sSql =
                    "select IfAdvanced,AdvAmt,StartDate,EndDate,Rate,FirstResp,FisrtRespPct from BMAcptBillHonor where AcptBillNo=" + acptBillNo;

            ResultSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getInt("IfAdvanced") == EnumValue.YesNo_No) {
                    sSql = "update BMAcptBill set AcptBillStatus=" + EnumValue.AcptBillStatus_ZhengChangShouHui +
                            ",Operator='" + operator +
                            "' where AcptBillNo=" + acptBillNo + " and AcptBillStatus=" + EnumValue.AcptBillStatus_FaFang;
                    Debug.debug(Debug.TYPE_SQL, sSql);
                    int prodrtn = dc.executeUpdate(sSql);
                    if (prodrtn <= 0) {
                        Debug.debug(Debug.TYPE_ERROR, "failed to update Acceptance Bill status from FaFang to ZhengChangShouHuo!");
                        errorcode = ErrorCode.DB_UPDATE_FAILED;
                        if (prodrtn == 0) {
                            errorcode = ErrorCode.ACPT_BILL_STS_NOT_ZHENGCHANG;
                        }
                    }
                } else {
                    appdata = new UpToDateApp();
                    appdata.finalStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.finalEndDate = util.dateToCalendar(rs.getDate("EndDate"));
                    appdata.finalAmt = rs.getBigDecimal("AdvAmt");
                    appdata.firstResp = rs.getString("FirstResp");
                    appdata.firstRespPct = rs.getBigDecimal("FisrtRespPct");
                    appdata.finalRate = rs.getBigDecimal("Rate");

                    if (appdata.finalStartDate == null || appdata.finalEndDate == null || appdata.finalAmt == null ||
                            appdata.firstResp == null || appdata.firstRespPct == null || appdata.finalRate == null) {
                        errorcode = ErrorCode.ACPT_BILL_HONOUR_FLD_EMPTY;
                    }
                    BMTableData tbldata = BMTable.getBMTable(bmno);
                    if (tbldata == null) {
                        errorcode = ErrorCode.GET_BMTABLE_ERROR;
                    } else {
                        if (errorcode >= 0) {
                            errorcode = BMTable.updateUpToDateApp(bmno, appdata);
                            if (errorcode >= 0) {
                                errorcode = LoanGranted.createGrant(bmno, operator, tbldata.brhID);
                                if (errorcode >= 0) {
                                    errorcode = (int) LoanGranted.sendLoanGrant(bmno, true);
                                }
                            }
                        }
                    }
                }
            } else {
                errorcode = ErrorCode.ACPT_BILL_HONOUR_DB_NOTFOUND;
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when processing Acceptance Bill Receiving!");
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

    /**
     * �жһ�Ʊ��Ʊ�ж�-ȡ������
     * <p/>
     * 1. ����Ƿ��Ѿ��б�BMNO����Ȩ��Ϣ, ����з��ش���
     * 2. ʹ�ñ�BMNO������Ȩ̨��
     * 3. ������Ȩ��Ϣ
     *
     * @param acptBillNo
     * @return int
     * @roseuid 3FE8E9EE03AF
     */
    public static int acptBillRecvVoid(int acptBillNo, String operator) {
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        String bmno = BMTrans.acptBillNoToBMNo(acptBillNo);
        if (bmno == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_ACPTBILLLEDGER;
        }

        BMTableData tbldata = BMTable.getBMTable(bmno);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }
        if (tbldata.bmStatus != EnumValue.BMStatus_FaFang) {
            return ErrorCode.BM_STATUS_NOT_FAFANG;
        }

        int errorcode = 0;
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        UpToDateApp appdata = null;

        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        Statement st = null, st2 = null;

        try {
            st = con.createStatement();
            String sSql =
                    "select IfAdvanced,AdvAmt,StartDate,EndDate,Rate,FirstResp,FisrtRespPct from BMAcptBillHonor where AcptBillNo=" + acptBillNo;

            ResultSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getInt("IfAdvanced") == EnumValue.YesNo_No) {
                    sSql = "update BMAcptBill set AcptBillStatus=" + EnumValue.AcptBillStatus_FaFang +
                            ",Operator='" + operator +
                            "' where AcptBillNo=" + acptBillNo + " and AcptBillStatus=" + EnumValue.AcptBillStatus_ZhengChangShouHui;
                    Debug.debug(Debug.TYPE_SQL, sSql);
                    int prodrtn = dc.executeUpdate(sSql);
                    if (prodrtn <= 0) {
                        Debug.debug(Debug.TYPE_ERROR, "failed to update Acceptance Bill status from ZhengChangShouHuo to FaFang(VOID)!");
                        errorcode = ErrorCode.DB_UPDATE_FAILED;
                        if (prodrtn == 0) {
                            errorcode = ErrorCode.ACPT_BILL_STS_NOT_ZHENGCHANGSHOUHUI;
                        }
                    }
                } else {
                    errorcode = (int) LoanGranted.sendLoanGrant(bmno, false);
                }

                if (errorcode >= 0) { //cancel OK, start to delete Honour record
                    st2 = con.createStatement();
                    sSql = "delete from BMAcptBillHonor where AcptBillNo=" + acptBillNo;
                    Debug.debug(Debug.TYPE_SQL, sSql);
                    if (st2.executeUpdate(sSql) < 0) {
                        errorcode = ErrorCode.DB_UPDATE_FAILED;
                    }
                }
            } else {
                errorcode = ErrorCode.ACPT_BILL_HONOUR_DB_NOTFOUND;
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when processing Acceptance Bill Honour voiding!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st2 != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }

            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    /**
     * ����Ʊ�ݵ�������
     * <p/>
     * 1. �����BMNO
     * 2. ʹ�ñ�BMNO������Ȩ̨��
     * 3. ������Ȩ��Ϣ
     *
     * @param disbillno
     * @return int
     * @roseuid 3FE8ED2D02E2
     */
    public static int disBillRecv(int disbillno, String operator) {
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }
        BillDisData billdata = BMTrans.billDisNoToBMNo(disbillno);
        if (billdata == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }
        String bmno = billdata.BMNo;
        if (bmno == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }
        if (billdata.BillDisStatus != EnumValue.BillDisStatus_TieXian) {
            return ErrorCode.BILLDIS_HONOUR_NOT_DIS_STATUS;
        }

        int errorcode = 0;
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        Statement st = null;

        UpToDateApp appdata = null, newappdata = null;
        String newbmno = null;

        try {
            st = con.createStatement();
            String sSql =
                    "select IfAdvanced,AdvAmt,StartDate,EndDate,Rate,FirstResp,FisrtRespPct,IfDis from BMBillDisHouor where BillDisNo=" + disbillno;

            ResultSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getInt("IfDis") == EnumValue.YesNo_No) {
                    errorcode = ErrorCode.HONOUR_REC_NOT_DIS_TYPE;
                } else {
                    if (rs.getInt("IfAdvanced") == EnumValue.YesNo_No) {
                        sSql = "update BMBillDis set BillDisStatus=" +
                                EnumValue.BillDisStatus_TieXianShouHui +
                                ",Operator='" + operator +
                                "' where BillDisNo=" + disbillno; //+ " and BillDisStatus=" + EnumValue.BillDisStatus_TieXian;
                        Debug.debug(Debug.TYPE_SQL, sSql);
                        int prodrtn = dc.executeUpdate(sSql);
                        if (prodrtn <= 0) {
                            Debug.debug(Debug.TYPE_ERROR,
                                    "failed to update Bill Discount status from TieXian to ZhengChangShouHuo!");
                            errorcode = ErrorCode.DB_UPDATE_FAILED;
                            if (prodrtn == 0) {
                                errorcode = ErrorCode.BILL_DIS_STS_NOT_ZHENGCHANG;
                            }
                        }
                    } else {
                        Calendar startdate = null, enddate = null;
                        BigDecimal advamt = null, resppct = null, rate = null;
                        String resp = null;

                        startdate = util.dateToCalendar(rs.getDate("StartDate"));
                        enddate = util.dateToCalendar(rs.getDate("EndDate"));
                        advamt = rs.getBigDecimal("AdvAmt");
                        rate = rs.getBigDecimal("Rate");
                        resp = rs.getString("FirstResp");
                        resppct = rs.getBigDecimal("FisrtRespPct");

                        if (startdate == null || enddate == null || rate == null ||
                                advamt == null || resp == null || resppct == null) {
                            errorcode = ErrorCode.BILL_DIS_HONOUR_FLD_EMPTY;

                        }
                        if (errorcode >= 0) {
                            BMTableData tbldata = BMTable.getBMTable(bmno);
                            if (tbldata == null) {
                                errorcode = ErrorCode.GET_BMTABLE_ERROR;
                            } else {
                                if (tbldata.clientNo == null || tbldata.brhID == null ||
                                        tbldata.initBrhID == null) {
                                    errorcode = ErrorCode.ORIG_BMTABLE_CLIENT_BRH_INITBRH_NULL;
                                } else {
                                    appdata = BMTable.getUpToDateApp(bmno);
                                    newappdata = BMTable.getUpToDateApp(bmno);
                                    if (appdata == null || newappdata == null) {
                                        errorcode = ErrorCode.GET_UPTODATE_APP_NULL;
                                    } else {
                                        newbmno = BMTable.createBMTable(EnumValue.
                                                BMType_TieXianDianKuan,
                                                tbldata.clientNo,
                                                tbldata.brhID,
                                                tbldata.initBrhID, operator);
                                        if (newbmno == null) {
                                            errorcode = ErrorCode.CAN_NOT_CREATE_BMTABLE;
                                        } else {
                                            BMTable.updateTableStatus(newbmno, EnumValue.BMStatus_FaFang, operator);
                                            newappdata.finalAmt = advamt;
                                            newappdata.finalStartDate = startdate;
                                            newappdata.finalEndDate = enddate;
                                            newappdata.finalRate = rate;
                                            newappdata.firstResp = resp;
                                            newappdata.firstRespPct = resppct;
                                            newappdata.origBMNo = bmno;
                                            newappdata.contractNo = null;
                                            newappdata.sContractNo = null;
                                            newappdata.origAccNo = null;
                                            newappdata.origDueBillNo = null;

                                            errorcode = BMTable.updateUpToDateApp(newbmno, newappdata);
                                            if (errorcode >= 0) {

                                                sSql = "update BMBillDis set Operator='" + operator +
                                                        "', AdvBMNo='" + newbmno +
                                                        "', AdvAmt=" + advamt +
                                                        " where BillDisNo=" + disbillno; // update Advanced amt and advanced BMNO;
                                                Debug.debug(Debug.TYPE_SQL, sSql);
                                                int prodrtn = dc.executeUpdate(sSql);
                                                if (prodrtn <= 0) {
                                                    Debug.debug(Debug.TYPE_ERROR,
                                                            "failed to update advanced BMNO and amount of Bill Discount Transaction");
                                                    errorcode = ErrorCode.UPDATE_DIS_BILL_ADV_BMNO_ERROR;
                                                }

                                                if (errorcode >= 0) {
                                                    errorcode = LoanGranted.createGrant(newbmno, operator,
                                                            tbldata.brhID);
                                                    if (errorcode >= 0) {
                                                        errorcode = (int) LoanGranted.sendLoanGrant(newbmno, true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                errorcode = ErrorCode.BILL_DIS_HONOUR_DB_NOTFOUND;
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when processing Bill Discount Receiving!");
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

    /**
     * ����Ʊ�ݵ�������-ȡ��
     * <p/>
     * 1. �����BMNO
     * 2. ʹ�ñ�BMNO������Ȩ̨��
     * 3. ������Ȩ��Ϣ
     *
     * @param disbillno
     * @return int
     * @roseuid 3FE8ED2D02E2
     */
    public static int disBillRecvVoid(int disbillno, String operator) {
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }
        BillDisData billdata = BMTrans.billDisNoToBMNo(disbillno);
        if (billdata == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }

        String bmno = billdata.BMNo;
        if (bmno == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }

        BMTableData tbldata = BMTable.getBMTable(bmno);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }
        if (tbldata.bmStatus != EnumValue.BMStatus_FaFang) {
            return ErrorCode.BM_STATUS_NOT_FAFANG;
        }

        //if(tbldata.bmStatus != EnumValue.BMStatus_FaFang) return ErrorCode.BM_STATUS_NOT_FAFANG;

        //if(billdata.BillDisStatus != EnumValue.BillDisStatus_TieXian)
        //  return ErrorCode.BILLDIS_HONOUR_NOT_DIS_STATUS;

        int errorcode = 0;
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        Statement st = null, st2 = null;

        UpToDateApp appdata = null, newappdata = null;

        try {
            st = con.createStatement();
            String sSql =
                    "select IfAdvanced,AdvAmt,StartDate,EndDate,Rate,FirstResp,FisrtRespPct,IfDis from BMBillDisHouor where BillDisNo=" + disbillno;

            ResultSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getInt("IfDis") == EnumValue.YesNo_No) {
                    errorcode = ErrorCode.HONOUR_REC_NOT_DIS_TYPE;
                } else {
                    if (rs.getInt("IfAdvanced") == EnumValue.YesNo_No) {
                        sSql = "update BMBillDis set BillDisStatus=" +
                                EnumValue.BillDisStatus_TieXian + //EnumValue.BillDisStatus_TieXianShouHui +
                                ",Operator='" + operator +
                                "' where BillDisNo=" + disbillno; //+ " and BillDisStatus=" + EnumValue.BillDisStatus_TieXian;
                        Debug.debug(Debug.TYPE_SQL, sSql);
                        int prodrtn = dc.executeUpdate(sSql);
                        if (prodrtn <= 0) {
                            Debug.debug(Debug.TYPE_ERROR,
                                    "failed to update Bill Discount status from ZhengChangShouHuo to TieXian!");
                            errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    } else {
                        if (billdata.advBMNo == null) {
                            errorcode = ErrorCode.DIS_OR_REDIS_ADVBMNO_NOT_EXIST;
                        } else {
                            if (BMTable.getBMTable(billdata.advBMNo) == null) {
                                errorcode = ErrorCode.DIS_OR_REDIS_ADVBMNO_NOT_EXIST;
                            } else {
                                errorcode = (int) LoanGranted.sendLoanGrant(billdata.advBMNo, false);
                                if (errorcode >= 0) {
                                    errorcode = BMTable.cancelBMTable(billdata.advBMNo,
                                            EnumValue.BMStatus_QuXiao,
                                            operator);
                                }
                            }
                        }
                    }

                    if (errorcode >= 0) { //cancel OK, start to delete Honour record
                        st2 = con.createStatement();
                        sSql = "delete from BMBillDisHouor where BillDisNo=" + disbillno;
                        Debug.debug(Debug.TYPE_SQL, sSql);
                        if (st2.executeUpdate(sSql) < 0) {
                            errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    }
                }
            } else {
                errorcode = ErrorCode.BILL_DIS_HONOUR_DB_NOTFOUND;
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when processing Bill Discount Receiving!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st2 != null) {
                    st.close();

                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    /**
     * ת���ֵ���
     * <p/>
     * 1. �����BMNO
     * 2. ʹ�ñ�BMNO������Ȩ̨��
     * 3. ������Ȩ��Ϣ
     *
     * @param disbillno
     * @return int
     * @roseuid 3FE8ED450340
     */
    static public int redisBillRecv(int disbillno, String operator) {
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        BillDisData billdata = BMTrans.billDisNoToBMNo(disbillno);
        if (billdata == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }
        String bmno = billdata.BMNo;
        if (bmno == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }
        if (billdata.BillDisStatus != EnumValue.BillDisStatus_ZhuanTieXian) {
            return ErrorCode.BILLDIS_HONOUR_NOT_REDIS_STATUS;
        }

        int errorcode = 0;
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        Statement st = null;

        UpToDateApp appdata = null, newappdata = null;
        String newbmno = null;

        try {
            st = con.createStatement();
            String sSql =
                    "select IfAdvanced,AdvAmt,StartDate,EndDate,Rate,FirstResp,FisrtRespPct,IfDis from BMBillDisHouor where BillDisNo=" + disbillno;

            ResultSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getInt("IfDis") != EnumValue.YesNo_No) {
                    errorcode = ErrorCode.HONOUR_REC_NOT_REDIS_TYPE;
                } else {
                    if (rs.getInt("IfAdvanced") == EnumValue.YesNo_No) {
                        //errorcode = ErrorCode.ACPT_BILL_HONOUR_NO_ADV;
                        sSql = "update BMBillDis set BillDisStatus=" +
                                EnumValue.BillDisStatus_TieXian +
                                ",Operator='" + operator +
                                "' where BillDisNo=" + disbillno; //+ " and BillDisStatus=" + EnumValue.BillDisStatus_TieXian;
                        Debug.debug(Debug.TYPE_SQL, sSql);
                        int prodrtn = dc.executeUpdate(sSql);
                        if (prodrtn <= 0) {
                            Debug.debug(Debug.TYPE_ERROR,
                                    "failed to update Bill Discount status from ZhuanTieXian to TieXian!");
                            errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    } else {
                        Calendar startdate = null, enddate = null;
                        BigDecimal advamt = null, resppct = null, rate = null;
                        String resp = null;

                        startdate = util.dateToCalendar(rs.getDate("StartDate"));
                        enddate = util.dateToCalendar(rs.getDate("EndDate"));
                        advamt = rs.getBigDecimal("AdvAmt");
                        rate = rs.getBigDecimal("Rate");
                        resp = rs.getString("FirstResp");
                        resppct = rs.getBigDecimal("FisrtRespPct");

                        if (startdate == null || enddate == null || rate == null ||
                                advamt == null || resp == null || resppct == null) {
                            errorcode = ErrorCode.BILL_DIS_HONOUR_FLD_EMPTY;

                        }
                        if (errorcode >= 0) {
                            BMTableData tbldata = BMTable.getBMTable(bmno);
                            if (tbldata == null) {
                                errorcode = ErrorCode.GET_BMTABLE_ERROR;
                            } else {
                                if (tbldata.clientNo == null || tbldata.brhID == null ||
                                        tbldata.initBrhID == null) {
                                    errorcode = ErrorCode.ORIG_BMTABLE_CLIENT_BRH_INITBRH_NULL;
                                } else {
                                    appdata = BMTable.getUpToDateApp(bmno);
                                    newappdata = BMTable.getUpToDateApp(bmno);
                                    if (appdata == null || newappdata == null) {
                                        errorcode = ErrorCode.GET_UPTODATE_APP_NULL;
                                    } else {
                                        newbmno = BMTable.createBMTable(EnumValue.
                                                BMType_ZhuanTieXianDianKuan,
                                                tbldata.clientNo,
                                                tbldata.brhID,
                                                tbldata.initBrhID, operator);
                                        if (newbmno == null) {
                                            errorcode = ErrorCode.CAN_NOT_CREATE_BMTABLE;
                                        } else {
                                            BMTable.updateTableStatus(newbmno, EnumValue.BMStatus_FaFang, operator);
                                            newappdata.finalAmt = advamt;
                                            newappdata.finalStartDate = startdate;
                                            newappdata.finalEndDate = enddate;
                                            newappdata.finalRate = rate;
                                            newappdata.firstResp = resp;
                                            newappdata.firstRespPct = resppct;
                                            newappdata.origBMNo = bmno;
                                            newappdata.contractNo = null;
                                            newappdata.sContractNo = null;
                                            newappdata.origAccNo = null;
                                            newappdata.origDueBillNo = null;

                                            errorcode = BMTable.updateUpToDateApp(newbmno, newappdata);
                                            if (errorcode >= 0) {
                                                sSql = "update BMBillDis set Operator='" + operator +
                                                        "', AdvBMNo='" + newbmno +
                                                        "', AdvAmt=" + advamt +
                                                        " where BillDisNo=" + disbillno; // update Advanced amt and advanced BMNO;
                                                Debug.debug(Debug.TYPE_SQL, sSql);
                                                int prodrtn = dc.executeUpdate(sSql);
                                                if (prodrtn <= 0) {
                                                    Debug.debug(Debug.TYPE_ERROR,
                                                            "failed to update advanced BMNO and amount of Bill ReDiscount Transaction");
                                                    errorcode = ErrorCode.
                                                            UPDATE_REDIS_BILL_ADV_BMNO_ERROR;
                                                }

                                                if (errorcode >= 0) {
                                                    errorcode = LoanGranted.createGrant(newbmno, operator,
                                                            tbldata.brhID);
                                                    if (errorcode >= 0) {
                                                        errorcode = (int) LoanGranted.sendLoanGrant(newbmno, true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                errorcode = ErrorCode.BILL_DIS_HONOUR_DB_NOTFOUND;
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when processing Bill Rediscount Receiving!");
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

    /**
     * ת���ֵ���-ȡ��
     * <p/>
     * 1. �����BMNO
     * 2. ʹ�ñ�BMNO������Ȩ̨��
     * 3. ������Ȩ��Ϣ
     *
     * @param disbillno
     * @return int
     * @roseuid 3FE8ED450340
     */
    static public int redisBillRecvVoid(int disbillno, String operator) {
        if (SCUser.isExist(operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }
        BillDisData billdata = BMTrans.billDisNoToBMNo(disbillno);
        if (billdata == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }

        String bmno = billdata.BMNo;
        if (bmno == null) {
            return ErrorCode.NOT_FOUND_BMNO_IN_BILLDISLEDGER;
        }

        BMTableData tbldata = BMTable.getBMTable(bmno);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }
        if (tbldata.bmStatus != EnumValue.BMStatus_FaFang) {
            return ErrorCode.BM_STATUS_NOT_FAFANG;
        }

        //if(billdata.BillDisStatus != EnumValue.BillDisStatus_TieXian)
        //  return ErrorCode.BILLDIS_HONOUR_NOT_DIS_STATUS;

        int errorcode = 0;
        BMTransData data = null;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        Statement st = null, st2 = null;

        UpToDateApp appdata = null, newappdata = null;

        try {
            st = con.createStatement();
            String sSql =
                    "select IfAdvanced,AdvAmt,StartDate,EndDate,Rate,FirstResp,FisrtRespPct,IfDis from BMBillDisHouor where BillDisNo=" + disbillno;

            ResultSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getInt("IfDis") != EnumValue.YesNo_No) {
                    errorcode = ErrorCode.HONOUR_REC_NOT_REDIS_TYPE;
                } else {
                    if (rs.getInt("IfAdvanced") == EnumValue.YesNo_No) {
                        sSql = "update BMBillDis set BillDisStatus=" +
                                EnumValue.BillDisStatus_ZhuanTieXian +
                                ",Operator='" + operator +
                                "' where BillDisNo=" + disbillno; //+ " and BillDisStatus=" + EnumValue.BillDisStatus_TieXian;
                        Debug.debug(Debug.TYPE_SQL, sSql);
                        int prodrtn = dc.executeUpdate(sSql);
                        if (prodrtn <= 0) {
                            Debug.debug(Debug.TYPE_ERROR,
                                    "failed to update Bill Discount status to ZhuanTieXian!");
                            errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    } else {
                        if (billdata.advBMNo == null) {
                            errorcode = ErrorCode.DIS_OR_REDIS_ADVBMNO_NOT_EXIST;
                        } else {
                            if (BMTable.getBMTable(billdata.advBMNo) == null) {
                                errorcode = ErrorCode.DIS_OR_REDIS_ADVBMNO_NOT_EXIST;
                            } else {
                                errorcode = (int) LoanGranted.sendLoanGrant(billdata.advBMNo, false);
                                if (errorcode >= 0) {
                                    errorcode = BMTable.cancelBMTable(billdata.advBMNo,
                                            EnumValue.BMStatus_QuXiao,
                                            operator);
                                }
                            }
                        }
                    }

                    if (errorcode >= 0) { //cancel OK, start to delete Honour record
                        st2 = con.createStatement();
                        sSql = "delete from BMBillDisHouor where BillDisNo=" + disbillno;
                        Debug.debug(Debug.TYPE_SQL, sSql);
                        if (st2.executeUpdate(sSql) < 0) {
                            errorcode = ErrorCode.DB_UPDATE_FAILED;
                        }
                    }
                }
            } else {
                errorcode = ErrorCode.BILL_DIS_HONOUR_DB_NOTFOUND;
            }
        }
        catch (Exception e) {
            data = null;
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when processing Bill Discount Receiving!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (st2 != null) {
                    st.close();
                }
            }
            catch (Exception e) {
            }
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    /**
     * �û��鿴һ��ҵ�����ִ�б�����.
     * ��BMTRANS��״̬����Ϊ�Ѿ��鿴
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE8EE5403D6
     */
    public static int viewTrans(String bmno, int BMTransNo) {
        if (bmno == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        int errorcode = 0;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        try {
            String sSql =
                    "update BMTrans set Viewed=" + EnumValue.YesNo_Yes + " where BMTransNo=" + BMTransNo + " and BMNo='" + bmno + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to set BMTrans to Viewed Status!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when setting BMTrans to Viewed Status!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    protected static int finishBMTrans(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        int errorcode = 0;

        if (SCUser.isExist(Operator) == false) {
            return ErrorCode.USER_NOT_FOUND;
        }

        if (transdata == null) {
            return ErrorCode.GET_BMTRANSDATA_ERROR;
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
                    "update BMTrans set Operator='" + Operator + "',EndDate='" + sysdate + "',TransStatus=" +
                            EnumValue.TransStatus_WanCheng + " where BMTransNo=" + BMTransNo + " and BMNo='" + bmNo + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            int prodrtn = dc.executeUpdate(sSql);
            if (prodrtn < 0) {
                Debug.debug(Debug.TYPE_ERROR, "failed to finish BMTrans!");
                errorcode = ErrorCode.DB_UPDATE_FAILED;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when finishing BMTrans!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    protected static int checkApp(String bmNo, BMTableData tbldata) {
        if (tbldata == null || bmNo == null) {
            return 0;
        }

        String tblname = null;
        Connection con = null;
        Statement st = null;

        if (tbldata.TypeNo == EnumValue.BMType_TieXian) {
            tblname = "BMPldgBillDis";
        } else if (tbldata.TypeNo == EnumValue.BMType_ZhuanTieXian) {
            tblname = "BMPldgBillRedis";
        } else if (tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
            tblname = "BMPldgPDAsset";

        }
        if (tblname == null) {
            return 0;
        }
        int errorcode = 0;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB connection when checking Appointment Data!");
            return ErrorCode.NO_DB_CONN;
        }

        try {
            con = dc.getConnection();
            st = con.createStatement();

            String sSql = "select count(*),count(*) from " + tblname + " where BMNo='" + bmNo + "'";

            ResultSet rs = null;
            //st = cn.createStatement();
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = st.executeQuery(sSql);

            if (rs.next()) {
                //System.out.println("count is --------------"+rs.getInt(1));
                if (rs.getInt(1) <= 0) {
                    if (tbldata.TypeNo == EnumValue.BMType_TieXian) {
                        errorcode = ErrorCode.APP_DIS_PLDG_NO_REC;
                    } else if (tbldata.TypeNo == EnumValue.BMType_ZhuanTieXian) {
                        errorcode = ErrorCode.APP_REDIS_PLDG_NO_REC;
                    } else if (tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
                        errorcode = ErrorCode.APP_PDASSET_PLDG_NO_REC;
                    }
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR,
                        "Can not find Record Number when checking Appointment Data info! BMNO=" +
                                bmNo);
                errorcode = ErrorCode.DB_QUERY_ERROR;
            }

            if (tbldata.TypeNo == EnumValue.BMType_ZhuanTieXian && errorcode < 0) {
                sSql = "select count(*) from BMPldgBillDis where BMNo='" + bmNo + "'";
                Debug.debug(Debug.TYPE_SQL, sSql);
                rs = st.executeQuery(sSql);

                if (rs.next()) {
                    if (rs.getInt(1) <= 0) {
                        errorcode = ErrorCode.APP_REDIS_PLDG_NO_REC;
                    } else {
                        errorcode = 0;
                    }
                } else {
                    Debug.debug(Debug.TYPE_ERROR,
                            "Can not find Record Number when checking Appointment Data info! BMNO=" +
                                    bmNo);
                    errorcode = ErrorCode.DB_QUERY_ERROR;
                }
            }

            if (tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
                BigDecimal amt = null, amt2 = null;
                sSql = "select sum(amt) as amt,sum(amt2) as amt2 from BMPldgPDILLoan where bmno='" + bmNo + "'" +
                        " union select sum(amt) as amt,0 as amt2 from BMPldgPDAsset where bmno='" + bmNo + "'";
                //+ " union select sum(appamt) as amt,sum(appamt2) as amt2 from BMAppInactiveLoan where bmno='" + bmNo + "'";
                Debug.debug(Debug.TYPE_SQL, sSql);
                rs = st.executeQuery(sSql);
                if (rs.next()) {
                    System.out.println("=11111111111111==" + rs.getBigDecimal("amt") + "===" + rs.getBigDecimal("amt2"));
                    amt = rs.getBigDecimal("amt");
                    if (amt != null) {
                        if (rs.getBigDecimal("amt2") != null) amt = amt.add(rs.getBigDecimal("amt2"));
                    }

                    if (rs.next()) {
                        System.out.println("==22222222222222=" + rs.getBigDecimal("amt") + "===" + rs.getBigDecimal("amt2"));
                        amt2 = rs.getBigDecimal("amt");
                        if (amt2 != null) {
                            if (rs.getBigDecimal("amt2") != null) amt2 = amt2.add(rs.getBigDecimal("amt2"));
                        }
                    }

                    System.out.println("=============================" + amt + "==================" + amt2);
                    if (amt != null && amt2 != null) {
                        if (amt.compareTo(amt2) > 0) errorcode = ErrorCode.PDASSETAMT_MUST_GE_ILLOANAMT;
                    }
                }
//        while (rs.next()) {
//          //System.out.println("==="+rs.getBigDecimal("amt")+"==="+rs.getBigDecimal("amt2"));
//          if (amt == null) {
//            amt = rs.getBigDecimal("amt");
//            if (amt == null) {
//              amt = new BigDecimal(0);
//            }
//            if (rs.getBigDecimal("amt2") != null) {
//              amt = amt.add(rs.getBigDecimal("amt2"));
//            }
//          }
//          else {
//            amt2 = rs.getBigDecimal("amt");
//            if (amt2 == null) {
//              amt2 = new BigDecimal(0);
//            }
//            if (rs.getBigDecimal("amt2") != null) {
//              amt2 = amt2.add(rs.getBigDecimal("amt2"));
//
//              //System.out.println(":"+amt+":"+amt2);
//            }
//            if (amt.compareTo(amt2) != 0) {
//              errorcode = ErrorCode.YIZIDIZHAI_APPAMT_NOT_MATCH;
//              break;
//            }
//          }
//        }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when checking Appointment!");
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
            MyDB.getInstance().apReleaseConn(0);
            return errorcode;
        }
    }

    /**
     * 1.��ñ������Ӧ�����ݱ�����
     * 2.���˱�Ĺ����ֶο������£ͣԣ��£̣ţ��У���
     * 3.�����£ͣԣңΣ��Σϼ�¼��Ϊ��ɣ����ά����ͬ������
     * 4.�����һ����Ĳ�������
     * 5.��ñ�BMTRANS�Ĳ���������ϼ��Ŵ����ż�������
     * 6.�������һ���裬��������һ���ģ£ͣԣң��Σ�
     * 7.����BMTable.BMStatus
     *
     * @param BMTransNo
     * @param Operator
     * @return int
     * @roseuid 3FE530350280
     */
    protected static int compltApp(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        int errorcode = 0;
        BMTableData tbldata = BMTable.getBMTable(transdata.bmNo);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }

        errorcode = BMTrans.checkApp(bmNo, tbldata);
        if (errorcode < 0) {
            return errorcode;
        }

        String apptable = BMRoute.getInstance().getActTblName(tbldata.TypeNo, transdata.actType, null);
        if (apptable == null) {
            return ErrorCode.GET_ROUTETBL_ERROR;
        }

        //apptable = "BMAppHousing";
        int nextBMStatus;
        nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.TypeNo, transdata.actType, null);
        if (nextBMStatus < 0) {
            return ErrorCode.NEXT_BM_STATUS_IS_NULL;
        }

        //get Client of the application
        CMClient client = CMClientMan.getCMClient(tbldata.clientNo);
        if (client == null) {
            return ErrorCode.CLIENT_NOT_FOUND;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        UpToDateApp appdata = null;
        ResultSet rs = null;
        Statement st = null;

        try {
            String sSql =
                    //"select * from " +
                    "select CurNo,AppDate,Rate,AppAmt,AppStartDate,AppEndDate,AppMonths,LoanType3,LoanType5,LoanCat3,LoanPurpose,bRate,fRate from " +
                            apptable +
                            " where BMNo='" + bmNo + "'";

            if (tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai)
                sSql = "select CurNo,AppDate,Rate,AppAmt,AppStartDate,AppEndDate,AppMonths,LoanType3,LoanType5,LoanCat3,LoanPurpose,bRate,fRate,acqdate from " +
                        apptable +
                        " where BMNo='" + bmNo + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (rs.getBigDecimal("AppAmt") == null) {
                    errorcode = ErrorCode.NOT_INPUT_APP_AMT;
                } else {
                    appdata = new UpToDateApp();
                    appdata.curNo = rs.getString("CurNo");
                    appdata.appDate = util.dateToCalendar(rs.getDate("AppDate"));
                    appdata.rate = rs.getBigDecimal("Rate");
                    appdata.bRate = rs.getBigDecimal("bRate");
                    appdata.fRate = rs.getBigDecimal("fRate");
                    appdata.appAmt = rs.getBigDecimal("AppAmt");
                    appdata.finalAmt = rs.getBigDecimal("AppAmt"); //update this field early, so desktop can display AMT
                    //appdata.
                    appdata.appStartDate = util.dateToCalendar(rs.getDate("AppStartDate"));
                    appdata.appEndDate = util.dateToCalendar(rs.getDate("AppEndDate"));
                    appdata.appMonths = rs.getString("AppMonths") == null ? null :
                            new Integer(rs.getInt("AppMonths"));
                    appdata.loanType3 = rs.getString("loanType3") == null ? null :
                            new Integer(rs.getInt("loanType3"));
                    appdata.loanType5 = rs.getString("loanType5") == null ? null :
                            new Integer(rs.getInt("loanType5"));
                    appdata.loanCat3 = rs.getString("loanCat3") == null ? null :
                            new Integer(rs.getInt("loanCat3"));
                    appdata.loanPurpose = rs.getString("LoanPurpose");
                    appdata.clientNo = tbldata.clientNo;
                    appdata.bmTypeNo = new Integer(tbldata.TypeNo);
                    if (client.clientType != null) {
                        appdata.clientType = client.clientType;
                    }
                    if (client.clientMgr != null) {
                        appdata.clientMgr = client.clientMgr;
                    }
                    if (client.ecomDeptType != null) {
                        appdata.eComDeptType = client.ecomDeptType;
                    }
                    if (client.ecomType != null) {
                        appdata.eComType = client.ecomType;
                    }
                    if (client.etpScopType != null) {
                        appdata.etpScopType = client.etpScopType;
                    }
                    if (client.sectorCat1 != null) {
                        appdata.sectorCat1 = client.sectorCat1;

                    }

                    if (tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
                        appdata.appStartDate = util.dateToCalendar(rs.getDate("acqdate"));
                    }

                    errorcode = BMTable.updateTableStatus(tbldata.bmNo, nextBMStatus,
                            Operator);
                    if (errorcode >= 0) {
                        errorcode = BMTable.updateUpToDateApp(tbldata.bmNo, appdata);
                        if (errorcode >= 0) {
                            errorcode = BMTrans.finishBMTrans(bmNo, BMTransNo, transdata,
                                    Operator);
                            if (errorcode >= 0) {
                                Integer nextact = BMRoute.getInstance().getActNextStep(tbldata.
                                        TypeNo, transdata.actType, null);
                                if (nextact != null) {
                                    Debug.debug(Debug.TYPE_MESSAGE,
                                            "begin to find parent branch!");
                                    //String Operbrhid = SCBranch.getSupBrh(transdata.operBrhID,EnumValue.BrhLevel_XinYongShe);
                                    String Operbrhid = SCBranch.getSupBrh(tbldata.brhID,
                                            EnumValue.BrhLevel_XinYongShe);
                                    Debug.debug(Debug.TYPE_MESSAGE,
                                            "finished finding parent branch!");
                                    if (Operbrhid != null) {
                                        errorcode = BMTrans.createBMTrans(tbldata.bmNo,
                                                nextact.intValue(), Operbrhid,
                                                Operator);
                                    } else {
                                        Debug.debug(Debug.TYPE_WARNING,
                                                "Get UpBranch Error, return is null,current branch is " +
                                                        transdata.operBrhID);
                                        errorcode = ErrorCode.GET_UP_BRH_ERROR;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR, "failed to get App data of Appointment table!");
                errorcode = ErrorCode.GET_APPDATE_ERROR;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when completing Appointment step!");
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

    /**
     * 1.ͳ�Ʊ��£ͣԣң��ΣӣΣϵ����������Ƿ�ͨ��
     * 2.��BMDECISION�Ĺ����ֶκͿ������������Ƿ�ͨ�����£ͣԣ��£̣ţ��У���
     * if �������Ϊͬ��
     * �����£ͣԣңΣ��Σϼ�¼��Ϊ���
     * ��ñ�������������
     * if ���߽����˶��
     * ��BMTrans״̬����Ϊ���
     * ��   BMTable.BMStatus����Ϊ����ͨ��
     * else
     * �����һ����Ĳ�������
     * ��ñ�BMTRANS�Ĳ���������ϼ������缶������
     * if ����һ����
     * ��������һ���ģ£ͣԣң��Σ�
     * else
     * �������, ����DB COMMIT
     * ��   end if
     * end if
     * else if �������Ϊ��ͬ��
     * ȡ�����£ͣԣң��Σ�
     * end if
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE531160319
     */
    protected static int compltReview1st(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        int resulttype = BMTrans.getReviewResult(bmNo, BMTransNo);
        if (resulttype < 0) {
            return resulttype;
        }
        if (resulttype == EnumValue.ResultType_BuTongYi) {
            return BMTrans.cancelBMTrans(bmNo, BMTransNo, Operator);
        }

        BMTableData tbldata = BMTable.getBMTable(transdata.bmNo);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }
        UpToDateApp origappdata = BMTable.getUpToDateApp(transdata.bmNo);
        if (origappdata == null) {
            return ErrorCode.GET_UPTODATE_APP_NULL;
        }

        int nextBMStatus;
        nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.TypeNo, transdata.actType, null);
        if (nextBMStatus < 0) {
            return nextBMStatus;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        UpToDateApp appdata = null;
        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null;

        try {
            String sSql =
                    //"select * from " +
                    "select CurNo,Amt,Amt2,Rate,StartDate,EndDate,Months,IfRespLoan,FirstResp,FisrtRespPct,DecidedBy,fRate,bRate,\"DATE\" from BMDecision where BMNo='" + bmNo + "' and BMTransNo=" + BMTransNo;

            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (origappdata.finalAmt.compareTo(rs.getBigDecimal("Amt")) < 0) {
                    errorcode = ErrorCode.DECISION_AMT_CANNOT_INCREASE;
                } else {
                    appdata = new UpToDateApp();
                    appdata.finalCurNo = rs.getString("CurNo");
                    appdata.finalRate = rs.getBigDecimal("Rate");
                    appdata.bRate = rs.getBigDecimal("bRate");
                    appdata.fRate = rs.getBigDecimal("fRate");
                    appdata.finalAmt = rs.getBigDecimal("Amt");
                    appdata.finalAmt2 = rs.getBigDecimal("Amt2");

                    appdata.appStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.appEndDate = util.dateToCalendar(rs.getDate("EndDate"));

                    //appdata.finalStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.finalStartDate = util.dateToCalendar(rs.getDate("Date"));
                    appdata.finalEndDate = util.dateToCalendar(rs.getDate("EndDate"));
                    appdata.finalMonths = rs.getString("Months") == null ? null :
                            new Integer(rs.getInt("Months"));
                    appdata.ifRespLoan = rs.getString("IfRespLoan") == null ? null :
                            new Integer(rs.getInt("IfRespLoan"));
                    appdata.firstRespPct = rs.getBigDecimal("FisrtRespPct");
                    appdata.firstResp = rs.getString("FirstResp");
                    appdata.resultType = new Integer(resulttype);
                    appdata.decidedBy = rs.getString("DecidedBy");

                    if (appdata.ifRespLoan.intValue() == EnumValue.YesNo_Yes &&
                            appdata.finalAmt != null && appdata.firstRespPct != null &&
                            tbldata.TypeNo != EnumValue.BMType_BuLiangDaiKuanHeXiao &&
                            tbldata.TypeNo != EnumValue.BMType_YiZiDiZhai) {
                        if (appdata.firstRespPct.compareTo(appdata.finalAmt) > 0) {
                            errorcode = ErrorCode.RESP_AMT_LT_DEC_AMT;
                        }
                    }

                    if (errorcode >= 0 && tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
                        if (util.daysBetweenCals(appdata.finalStartDate, origappdata.finalStartDate) < 0)
                            errorcode = ErrorCode.YIZIDIZHAI_DATE_ERROR;
                    }

                    if (errorcode >= 0) {
                        if (tbldata.TypeNo == EnumValue.BMType_ShouXin) {
                            if (origappdata.appStartDate != null && origappdata.appEndDate != null
                                    && appdata.appStartDate != null && appdata.appEndDate != null) {
                                if (util.daysBetweenCals(appdata.appEndDate, appdata.appStartDate)
                                        > util.daysBetweenCals(origappdata.appEndDate, origappdata.appStartDate))
                                    errorcode = ErrorCode.DECISION_PRD_CANNOT_INCREASE;
                            }
                        } else {
                            if (BMTable.isNormalLoanBMType(tbldata.TypeNo) == true) {
                                if (appdata.finalMonths != null && origappdata.finalMonths != null) {
                                    if (appdata.finalMonths.intValue() >
                                            origappdata.finalMonths.intValue())
                                        errorcode = ErrorCode.DECISION_PRD_CANNOT_INCREASE;
                                }
                            }
                        }
                    }


                    if (errorcode >= 0) {

                        errorcode = BMTable.updateTableStatus(tbldata.bmNo, nextBMStatus,
                                Operator);
                        if (errorcode >= 0) {
                            errorcode = BMTable.updateUpToDateApp(tbldata.bmNo, appdata);
                            if (errorcode >= 0) {
                                errorcode = BMTrans.finishBMTrans(bmNo, BMTransNo, transdata,
                                        Operator);
                                if (errorcode >= 0) {
                                    Integer nextact = BMRoute.getInstance().getActNextStep(
                                            tbldata.
                                                    TypeNo, transdata.actType, null);
                                    if (nextact != null) {
                                        String Operbrhid = tbldata.initBrhID;
                                        ;
                                        if (Operbrhid != null) {
                                            errorcode = BMTrans.createBMTrans(tbldata.bmNo,
                                                    nextact.intValue(), Operbrhid,
                                                    Operator);
                                        } else {
                                            Debug.debug(Debug.TYPE_WARNING,
                                                    "Initial Branch is null for a BMTable!BMNO=" +
                                                            transdata.bmNo);
                                            errorcode = ErrorCode.INIT_BRH_IS_NULL;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR, "failed to get BMDecision info in Review1st!");
                errorcode = ErrorCode.GET_APPDATE_ERROR;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when completing Review1st!");
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

    /**
     * 1.ͳ�Ʊ��£ͣԣң��ΣӣΣϵ����������Ƿ�ͨ��
     * 2.��BMDECISION�Ĺ����ֶκͿ������������Ƿ�ͨ�����£ͣԣ��£̣ţ��У���
     * if �������Ϊͬ��
     * �����£ͣԣңΣ��Σϼ�¼��Ϊ���
     * ��ñ�������������
     * if ���߽����˶��
     * ��BMTrans״̬����Ϊ���
     * ��   BMTable.BMStatus����Ϊ����ͨ��
     * else
     * �����һ����Ĳ�������
     * ��ñ�BMTRANS�Ĳ���������ϼ������缶������
     * if ����һ����
     * ��������һ���ģ£ͣԣң��Σ�
     * else
     * �������, ����DB COMMIT
     * ��   end if
     * end if
     * else if �������Ϊ��ͬ��
     * ȡ�����£ͣԣң��Σ�
     * end if
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE5312F00B2
     */
    protected static int compltReview2nd(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        int resulttype = BMTrans.getReviewResult(bmNo, BMTransNo);
        if (resulttype < 0) {
            return resulttype;
        }
        if (resulttype == EnumValue.ResultType_BuTongYi) {
            return BMTrans.cancelBMTrans(bmNo, BMTransNo, Operator);
        }

        BMTableData tbldata = BMTable.getBMTable(transdata.bmNo);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }
        UpToDateApp origappdata = BMTable.getUpToDateApp(transdata.bmNo);
        if (origappdata == null) {
            return ErrorCode.GET_UPTODATE_APP_NULL;
        }

        if (origappdata.clientType == null) {
            return ErrorCode.CLIENTTYPE_IS_NULL;
        }
        if (origappdata.loanType3 == null) {
            return ErrorCode.LOANTYPE3_IS_NULL;
        }
        if (origappdata.loanType5 == null) {
            return ErrorCode.LOANTYPE5_IS_NULL;
        }

        int nextBMStatus;
        //nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.TypeNo, transdata.actType,null);
        //if(nextBMStatus < 0) return ErrorCode.NEXT_BM_STATUS_IS_NULL;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        UpToDateApp appdata = null;
        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null;

        try {
            String sSql =
                    //"select * from " +
                    "select CurNo,Amt,Amt2,Rate,StartDate,EndDate,Months,IfRespLoan,FirstResp,FisrtRespPct,DecidedBy,fRate,bRate,\"DATE\" from BMDecision where BMNo='" + bmNo + "' and BMTransNo=" + BMTransNo;

            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (origappdata.finalAmt.compareTo(rs.getBigDecimal("Amt")) < 0) {
                    errorcode = ErrorCode.DECISION_AMT_CANNOT_INCREASE;
                } else {

                    appdata = new UpToDateApp();
                    appdata.finalCurNo = rs.getString("CurNo");
                    appdata.finalRate = rs.getBigDecimal("Rate");
                    appdata.bRate = rs.getBigDecimal("bRate");
                    appdata.fRate = rs.getBigDecimal("fRate");
                    appdata.finalAmt = rs.getBigDecimal("Amt");
                    appdata.finalAmt2 = rs.getBigDecimal("Amt2");

                    appdata.appStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.appEndDate = util.dateToCalendar(rs.getDate("EndDate"));

                    //appdata.finalStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.finalStartDate = util.dateToCalendar(rs.getDate("Date"));
                    appdata.finalEndDate = util.dateToCalendar(rs.getDate("EndDate"));
                    appdata.finalMonths = rs.getString("Months") == null ? null :
                            new Integer(rs.getInt("Months"));
                    appdata.ifRespLoan = rs.getString("IfRespLoan") == null ? null :
                            new Integer(rs.getInt("IfRespLoan"));
                    appdata.firstRespPct = rs.getBigDecimal("FisrtRespPct");
                    appdata.firstResp = rs.getString("FirstResp");
                    appdata.resultType = new Integer(resulttype);
                    appdata.decidedBy = rs.getString("DecidedBy");

                    if (appdata.ifRespLoan.intValue() == EnumValue.YesNo_Yes &&
                            appdata.finalAmt != null && appdata.firstRespPct != null &&
                            tbldata.TypeNo != EnumValue.BMType_BuLiangDaiKuanHeXiao &&
                            tbldata.TypeNo != EnumValue.BMType_YiZiDiZhai) {
                        if (appdata.firstRespPct.compareTo(appdata.finalAmt) > 0) {
                            errorcode = ErrorCode.RESP_AMT_LT_DEC_AMT;
                        }
                    }

                    if (errorcode >= 0 && tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
                        if (util.daysBetweenCals(appdata.finalStartDate, origappdata.finalStartDate) < 0)
                            errorcode = ErrorCode.YIZIDIZHAI_DATE_ERROR;
                    }

                    if (errorcode >= 0) {
                        if (tbldata.TypeNo == EnumValue.BMType_ShouXin) {
                            if (origappdata.appStartDate != null && origappdata.appEndDate != null
                                    && appdata.appStartDate != null && appdata.appEndDate != null) {
                                if (util.daysBetweenCals(appdata.appEndDate, appdata.appStartDate)
                                        > util.daysBetweenCals(origappdata.appEndDate, origappdata.appStartDate))
                                    errorcode = ErrorCode.DECISION_PRD_CANNOT_INCREASE;
                            }
                        } else {
                            if (BMTable.isNormalLoanBMType(tbldata.TypeNo) == true) {
                                if (appdata.finalMonths != null && origappdata.finalMonths != null) {
                                    if (appdata.finalMonths.intValue() >
                                            origappdata.finalMonths.intValue())
                                        errorcode = ErrorCode.DECISION_PRD_CANNOT_INCREASE;
                                }
                            }
                        }
                    }


                    if (errorcode >= 0) {

                        BigDecimal amt = rs.getBigDecimal("Amt");
                        if (appdata.finalAmt2 != null) {
                            amt.add(appdata.finalAmt2);
                        }
                        Debug.debug(Debug.TYPE_MESSAGE, "Fianl Amt is " + amt);

                        int bmstatus, cridefine;
                        BMReviewData limit = BMReviewLimit.getInstance().getReviewLimitofBrh2(transdata.operBrhID, tbldata.TypeNo, origappdata.clientType.intValue(), origappdata.loanType5.intValue(),
                                origappdata.loanType3.intValue());
                        if (limit == null) {
                            Debug.debug(Debug.TYPE_ERROR,
                                    "Review limit return null, OperbrhID=" +
                                            transdata.operBrhID + "BMType is " + tbldata.TypeNo);
                            errorcode = ErrorCode.REIVEW_LIMIT_NOT_FOUND;
                            return -1;
                        }

                        cridefine = BMTrans.checkCriDefine(amt, limit.Limit, limit.reviewLimitType, tbldata.clientNo, tbldata.brhID, Operator);
                        if (cridefine >= 0) {
                            nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.
                                    TypeNo,
                                    transdata.actType, new Integer(cridefine));
                            if (nextBMStatus < 0) {
                                return nextBMStatus;
                            }

                            errorcode = BMTable.updateTableStatus(tbldata.bmNo, nextBMStatus,
                                    Operator);
                            if (errorcode >= 0) {
                                errorcode = BMTable.updateUpToDateApp(tbldata.bmNo, appdata);
                                if (errorcode >= 0) {
                                    errorcode = BMTrans.finishBMTrans(bmNo, BMTransNo, transdata,
                                            Operator);
                                    if (errorcode >= 0) {
                                        Integer nextact = BMRoute.getInstance().getActNextStep(
                                                tbldata.
                                                        TypeNo, transdata.actType, new Integer(cridefine));
                                        if (nextact != null) {
                                            String Operbrhid = null;
//                      if (cridefine == EnumValue.CriDefine_ErDuWai) {
//                        Operbrhid = SCBranch.getSupBrh(transdata.operBrhID,
//                            EnumValue.BrhLevel_ShiLianShe);
//                      }
//                      else {
//                        Operbrhid = tbldata.initBrhID;
//
//                      }

                                            Operbrhid = mapToNextOperBrhid(transdata.operBrhID, tbldata.initBrhID, nextBMStatus);

                                            if (Operbrhid != null) {
                                                errorcode = BMTrans.createBMTrans(tbldata.bmNo,
                                                        nextact.intValue(), Operbrhid,
                                                        Operator);
                                            } else {
                                                Debug.debug(Debug.TYPE_WARNING,
                                                        "Get Parent Branch is null for a branch!Branch=" +
                                                                transdata.operBrhID);
                                                errorcode = ErrorCode.INIT_BRH_IS_NULL;
                                            }
                                        }
                                    }
                                }
                            }
                        } else
                            errorcode = cridefine;
                    }
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR, "failed to get BMDecision info in Review2nd!");
                errorcode = ErrorCode.GET_APPDATE_ERROR;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when completing Review2nd!");
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

    /**
     * 1.ͳ�Ʊ��£ͣԣң��ΣӣΣϵ����������Ƿ�ͨ��
     * 2.��BMDECISION�Ĺ����ֶκͿ������������Ƿ�ͨ�����£ͣԣ��£̣ţ��У���
     * if �������Ϊͬ��
     * �����£ͣԣңΣ��Σϼ�¼��Ϊ���
     * �����һ����Ĳ�������
     * ��ñ�BMTRANS�Ĳ���������ϼ������缶������
     * if ����һ����
     * ��������һ���ģ£ͣԣң��Σ�
     * else
     * �������, ����DB COMMIT
     * end if
     * else if �������Ϊ��ͬ��
     * ȡ�����£ͣԣң��Σ�
     * end if
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE531370317
     */
    protected static int compltReview3rd(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        int resulttype = BMTrans.getReviewResult(bmNo, BMTransNo);
        if (resulttype < 0) {
            return resulttype;
        }
        if (resulttype == EnumValue.ResultType_BuTongYi) {
            return BMTrans.cancelBMTrans(bmNo, BMTransNo, Operator);
        }

        BMTableData tbldata = BMTable.getBMTable(transdata.bmNo);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }
        UpToDateApp origappdata = BMTable.getUpToDateApp(transdata.bmNo);
        if (origappdata == null) {
            return ErrorCode.GET_UPTODATE_APP_NULL;
        }

        if (origappdata.clientType == null) {
            return ErrorCode.CLIENTTYPE_IS_NULL;
        }
        if (origappdata.loanType3 == null) {
            return ErrorCode.LOANTYPE3_IS_NULL;
        }
        if (origappdata.loanType5 == null) {
            return ErrorCode.LOANTYPE5_IS_NULL;
        }

        int nextBMStatus;

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }
        UpToDateApp appdata = null;
        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null;
        try {
            String sSql = "select CurNo,Amt,Amt2,Rate,StartDate,EndDate,Months,IfRespLoan,FirstResp,FisrtRespPct,DecidedBy,fRate,bRate,\"DATE\" from BMDecision where BMNo='" + bmNo + "' and BMTransNo=" + BMTransNo;
            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                if (origappdata.finalAmt.compareTo(rs.getBigDecimal("Amt")) < 0) {
                    errorcode = ErrorCode.DECISION_AMT_CANNOT_INCREASE;
                } else {
                    appdata = new UpToDateApp();
                    appdata.finalCurNo = rs.getString("CurNo");
                    appdata.finalRate = rs.getBigDecimal("Rate");
                    appdata.bRate = rs.getBigDecimal("bRate");
                    appdata.fRate = rs.getBigDecimal("fRate");
                    appdata.finalAmt = rs.getBigDecimal("Amt");
                    appdata.finalAmt2 = rs.getBigDecimal("Amt2");

                    appdata.appStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.appEndDate = util.dateToCalendar(rs.getDate("EndDate"));

                    //appdata.finalStartDate = util.dateToCalendar(rs.getDate("StartDate"));
                    appdata.finalStartDate = util.dateToCalendar(rs.getDate("Date"));
                    appdata.finalEndDate = util.dateToCalendar(rs.getDate("EndDate"));
                    appdata.finalMonths = rs.getString("Months") == null ? null :
                            new Integer(rs.getInt("Months"));
                    appdata.ifRespLoan = rs.getString("IfRespLoan") == null ? null :
                            new Integer(rs.getInt("IfRespLoan"));
                    appdata.firstRespPct = rs.getBigDecimal("FisrtRespPct");
                    appdata.firstResp = rs.getString("FirstResp");
                    appdata.resultType = new Integer(resulttype);
                    appdata.decidedBy = rs.getString("DecidedBy");

                    if (appdata.ifRespLoan.intValue() == EnumValue.YesNo_Yes &&
                            appdata.finalAmt != null && appdata.firstRespPct != null &&
                            tbldata.TypeNo != EnumValue.BMType_BuLiangDaiKuanHeXiao &&
                            tbldata.TypeNo != EnumValue.BMType_YiZiDiZhai) {
                        if (appdata.firstRespPct.compareTo(appdata.finalAmt) > 0) {
                            errorcode = ErrorCode.RESP_AMT_LT_DEC_AMT;
                        }
                    }

                    if (errorcode >= 0) {
                        if (tbldata.TypeNo == EnumValue.BMType_ShouXin) {
                            if (origappdata.appStartDate != null && origappdata.appEndDate != null
                                    && appdata.appStartDate != null && appdata.appEndDate != null) {
                                if (util.daysBetweenCals(appdata.appEndDate, appdata.appStartDate)
                                        > util.daysBetweenCals(origappdata.appEndDate, origappdata.appStartDate))
                                    errorcode = ErrorCode.DECISION_PRD_CANNOT_INCREASE;
                            }
                        } else {
                            if (BMTable.isNormalLoanBMType(tbldata.TypeNo) == true) {
                                if (appdata.finalMonths != null && origappdata.appMonths != null) {
                                    if (appdata.finalMonths.intValue() >
                                            origappdata.appMonths.intValue())
                                        errorcode = ErrorCode.DECISION_PRD_CANNOT_INCREASE;
                                }
                            }
                        }
                    }

                    if (errorcode >= 0 && tbldata.TypeNo == EnumValue.BMType_YiZiDiZhai) {
                        if (util.daysBetweenCals(appdata.finalStartDate, origappdata.appStartDate) < 0)
                            errorcode = ErrorCode.YIZIDIZHAI_DATE_ERROR;
                    }


                    if (errorcode >= 0) {

                        BigDecimal amt = rs.getBigDecimal("Amt");
                        if (appdata.finalAmt2 != null) {
                            amt.add(appdata.finalAmt2);
                        }
                        Debug.debug(Debug.TYPE_MESSAGE, "Fianl Amt is " + amt);

                        int cridefine;
                        BMReviewData limit = BMReviewLimit.getInstance().getReviewLimitofBrh2(transdata.operBrhID, tbldata.TypeNo, origappdata.clientType.intValue(), origappdata.loanType5.intValue(), origappdata.loanType3.intValue());
                        if (limit == null) {
                            Debug.debug(Debug.TYPE_ERROR,
                                    "Review limit return null, OperbrhID=" +
                                            transdata.operBrhID + "BMType is " + tbldata.TypeNo);
                            errorcode = ErrorCode.REIVEW_LIMIT_NOT_FOUND;
                            return -1; //why I do like this, think over, should be no problem
                        }

                        cridefine = BMTrans.checkCriDefine(amt, limit.Limit, limit.reviewLimitType, tbldata.clientNo, tbldata.brhID, Operator);
                        if (cridefine >= 0) {
                            nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.
                                    TypeNo,
                                    transdata.actType, new Integer(cridefine));
                            if (nextBMStatus < 0) {
                                errorcode = nextBMStatus;
                                return nextBMStatus;
                            }

                            errorcode = BMTable.updateTableStatus(tbldata.bmNo, nextBMStatus,
                                    Operator);
                            if (errorcode >= 0) {
                                errorcode = BMTable.updateUpToDateApp(tbldata.bmNo, appdata);
                                if (errorcode >= 0) {
                                    errorcode = BMTrans.finishBMTrans(bmNo, BMTransNo, transdata,
                                            Operator);
                                    if (errorcode >= 0) {
                                        Integer nextact = BMRoute.getInstance().getActNextStep(
                                                tbldata.
                                                        TypeNo, transdata.actType, new Integer(cridefine));
                                        if (nextact != null) {
                                            String Operbrhid = null;
//                      if (cridefine == EnumValue.CriDefine_ErDuWai) {
//                        Operbrhid = SCBranch.getSupBrh(transdata.operBrhID,
//                            EnumValue.BrhLevel_XianLianShe);
//                      }
//                      else {
//                        Operbrhid = tbldata.initBrhID;
//
//                      }

                                            Operbrhid = mapToNextOperBrhid(transdata.operBrhID, tbldata.initBrhID, nextBMStatus);

                                            if (Operbrhid != null) {
                                                errorcode = BMTrans.createBMTrans(tbldata.bmNo,
                                                        nextact.intValue(), Operbrhid,
                                                        Operator);
                                            } else {
                                                Debug.debug(Debug.TYPE_WARNING,
                                                        "Get Parent Branch is null for a branch!Branch=" +
                                                                transdata.operBrhID);
                                                errorcode = ErrorCode.INIT_BRH_IS_NULL;
                                            }
                                        }
                                    }
                                }
                            }
                        } else
                            errorcode = cridefine;
                    }
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR, "failed to get BMDecision info in Review3rd!");
                errorcode = ErrorCode.GET_APPDATE_ERROR;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when completing Review3rd!");
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

    /**
     * @param BMTransNo
     * @return int
     * @roseuid 3FE5317B01DE
     */
    protected static int compltContract(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        BMTableData tbldata = BMTable.getBMTable(transdata.bmNo);
        //UpToDateApp oldapp = BMTable.getUpToDateApp(transdata.bmNo);

        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }

        int nextBMStatus;
        nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.TypeNo, transdata.actType, null);
        if (nextBMStatus < 0) {
            return ErrorCode.NEXT_BM_STATUS_IS_NULL;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        Connection con = dc.getConnection();
        if (con == null) {
            return ErrorCode.NO_DB_CONN_PTCON;
        }

        UpToDateApp appdata = null;
        int errorcode = 0;
        ResultSet rs = null;
        Statement st = null;

        try {
            String sSql =
                    "select ContractNo,InterestType,sContractNo,STARTDATE,ENDDATE,CNLNO from BMContract where BMNo='" + bmNo + "'";

            Debug.debug(Debug.TYPE_SQL, sSql);
            st = con.createStatement();
            rs = st.executeQuery(sSql);
            if (rs.next()) {
                appdata = new UpToDateApp();
                appdata.contractNo = rs.getString("ContractNo");
                appdata.sContractNo = rs.getString("sContractNo");
                appdata.interestType = rs.getString("InterestType") == null ? null : new Integer(rs.getInt("InterestType"));
                appdata.finalStartDate = util.dateToCalendar(rs.getDate("STARTDATE"));
                appdata.finalEndDate = util.dateToCalendar(rs.getDate("ENDDATE"));
                appdata.cnlNo = rs.getString("CNLNO");


                errorcode = BMTable.updateTableStatus(tbldata.bmNo, nextBMStatus, Operator);
                if (errorcode >= 0) {
                    errorcode = BMTable.updateUpToDateApp(tbldata.bmNo, appdata);
                    if (errorcode >= 0) {
                        errorcode = BMTrans.finishBMTrans(bmNo, BMTransNo, transdata, Operator);
                        if (errorcode >= 0) {
                            Integer nextact = BMRoute.getInstance().getActNextStep(tbldata.TypeNo, transdata.actType, null);
                            if (nextact != null) {
                                String Operbrhid = tbldata.initBrhID;
                                ;
                                if (Operbrhid != null) {
                                    errorcode = BMTrans.createBMTrans(tbldata.bmNo, nextact.intValue(), Operbrhid,
                                            Operator);
                                } else {
                                    Debug.debug(Debug.TYPE_WARNING, "Initial Branch is null for a BMTable!BMNO=" + transdata.bmNo);
                                    errorcode = ErrorCode.INIT_BRH_IS_NULL;
                                }
                            }
                        }
                    }
                }
            } else {
                Debug.debug(Debug.TYPE_ERROR, "failed to get BMContract info in Contract Completetion!");
                errorcode = ErrorCode.CONTRACT_DATA_NOT_FOUND;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when completing Contract Step!");
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

    /**
     * 1.������Ȩ��
     * 2.������Ȩ����Ȩ�Уϣϣ���
     * 3.ҵ�����ͣ�
     * ����֤������ô���֤������Ȩ
     * �жһ�Ʊ:����̨��
     * ����:����̨��
     * ת����:����̨��
     * ���ʵ�ծ:����̨��
     * �����������:����̨��
     *
     * @param BMTransNo
     * @return int
     * @roseuid 3FE53182012A
     */
    protected static int compltAuthorization(String bmNo, int BMTransNo, BMTransData transdata, String Operator) {
        BMTableData tbldata = BMTable.getBMTable(bmNo);
        if (tbldata == null) {
            return ErrorCode.GET_BMTABLE_ERROR;
        }

        UpToDateApp appdata = BMTable.getUpToDateApp(bmNo);
        if (appdata == null) {
            return ErrorCode.GET_UPTODATE_APP_NULL;
        }

        int nextBMStatus;
        nextBMStatus = BMRoute.getInstance().getNextBMStatus(tbldata.TypeNo, transdata.actType, null);
        if (nextBMStatus < 0) {
            return ErrorCode.NEXT_BM_STATUS_IS_NULL;
        }

        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }

        try {
            errorcode = BMTable.updateTableStatus(tbldata.bmNo, nextBMStatus, Operator);
            if (errorcode >= 0) {
                errorcode = BMTrans.finishBMTrans(bmNo, BMTransNo, transdata, Operator);
                if (errorcode >= 0) {
                    ContractData contr = ContractMan.getContract(bmNo);
                    switch (tbldata.TypeNo) {
                        case EnumValue.BMType_ChengDuiHuiPiao:
                            if (contr == null) {
                                errorcode = ErrorCode.GET_CONTRACT_IS_NULL;
                            } else {
                                errorcode = Ledger.createAcptBillLedger(bmNo, contr, tbldata, appdata, Operator);
                            }
                            break;
                        case EnumValue.BMType_TieXian:
                            if (contr == null) {
                                errorcode = ErrorCode.GET_CONTRACT_IS_NULL;
                            } else {
                                errorcode = Ledger.creatBillDisLedger(bmNo, contr, tbldata, appdata, Operator);
                            }
                            break;
                        case EnumValue.BMType_ZhuanTieXian:
                            if (contr == null) {
                                errorcode = ErrorCode.GET_CONTRACT_IS_NULL;
                            } else {
                                errorcode = Ledger.creatBillRedisLedger(bmNo, contr, tbldata,
                                        appdata, Operator);
                                if (errorcode >= 0) {
                                    errorcode = Ledger.creatBillDisLedger(bmNo, contr, tbldata, appdata, Operator);
                                }
                            }

                            break;
                        case EnumValue.BMType_YiZiDiZhai:
                            errorcode = Ledger.creatPDAssetsLedger(bmNo, tbldata, appdata, Operator);
                            break;
                        case EnumValue.BMType_BuLiangDaiKuanHeXiao:
                            errorcode = Ledger.creatILCAVLedger(bmNo, tbldata, appdata, Operator);
                            break;
                        case EnumValue.BMType_ShouXin:
                            errorcode = Ledger.createCreditLimit(bmNo, tbldata, appdata, Operator);
                            break;

                    }
                    if (errorcode >= 0) {
                        errorcode = Ledger.creatBMGuarantor(bmNo);
                        if (errorcode >= 0) {
                            if (tbldata.TypeNo != EnumValue.BMType_BuLiangDaiKuanHeXiao &&
                                    tbldata.TypeNo != EnumValue.BMType_YiZiDiZhai &&
                                    tbldata.TypeNo != EnumValue.BMType_ChengDuiHuiPiao &&
                                    tbldata.TypeNo != EnumValue.BMType_TieXian &&
                                    tbldata.TypeNo != EnumValue.BMType_ZhuanTieXian &&
                                    tbldata.TypeNo != EnumValue.BMType_ShouXin) {
                                errorcode = LoanGranted.createGrant(bmNo, Operator,
                                        tbldata.brhID);

                                if (errorcode >= 0) {
                                    errorcode = LoanGranted.sendLoanGrant(bmNo, true);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when completing Authorization Step!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }

    }

    public static String acptBillNoToBMNo(int acptbillno) {
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return null;
        }

        try {
            String sSql =
                    "select BMNo from BMAcptBill where AcptBillNo=" + acptbillno;

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                return rs.getString("BMNo");
            } else {
                return null;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when matching BMAcptBillNo to BMNO!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return null;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }

    }

    public static BillDisData billDisNoToBMNo(int acptbillno) {
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return null;
        }
        BillDisData data = null;

        try {
            String sSql =
                    "select BMNo,BillDisStatus,AdvBMNo from BMBillDis where BillDisNo=" + acptbillno;

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            if (rs.next()) {
                data = new BillDisData();
                data.BMNo = rs.getString("BMNo");
                data.advBMNo = rs.getString("AdvBMNo");
                data.BillDisStatus = rs.getInt("BillDisStatus");
                return data;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "Exception when matching BMBillDisNo to BMNO!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return null;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }

    }


    public static int checkCriDefine(BigDecimal appamt, BigDecimal revlimit, int revlimittype, String clientno, String brhid, String operator) {

        int cridefine = 0;
        if (appamt == null || revlimit == null) {
            cridefine = ErrorCode.PARAM_IS_NULL;
            return ErrorCode.PARAM_IS_NULL;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            cridefine = ErrorCode.NO_DB_CONN;
            return ErrorCode.NO_DB_CONN;
        }

        if (revlimittype == EnumValue.ReviewLimitType_DanBi) {
            if (appamt.compareTo(revlimit) <= 0) {
                cridefine = EnumValue.CriDefine_ErDuNei;
            } else {
                int temp = BMTrans.getNextBindRoute(brhid, operator);
                if (temp > 0) cridefine = temp;
                else
                    cridefine = EnumValue.CriDefine_ErDuWai;
            }
            //System.out.println("APPAMT="+appamt+" revlimit:"+revlimit+" CRIDEFINE:"+cridefine);
        } else if (revlimittype == EnumValue.ReviewLimitType_DanHu) {
            BigDecimal amt = new BigDecimal(0);
            amt = amt.add(appamt);
            BigDecimal loanbal = BMTable.getClientLoanBal(clientno);
            BigDecimal unposted = BMTable.getUnPostedLoan(clientno);
            if (loanbal != null) amt = amt.add(loanbal);
            if (unposted != null) amt = amt.add(unposted);

            if (amt.compareTo(revlimit) <= 0) {
                cridefine = EnumValue.CriDefine_ErDuNei;
            } else {
                int temp = BMTrans.getNextBindRoute(brhid, operator);
                if (temp > 0) cridefine = temp;
                else
                    cridefine = EnumValue.CriDefine_ErDuWai;
            }
            //System.out.println("*****BAL:"+loanbal+" UNPSOT:"+unposted+ " APPAMT="+amt+" revlimit:"+revlimit+" CRIDEFINE:"+cridefine);
        } else {
            cridefine = ErrorCode.REIVEW_LIMIT_NOT_FOUND;
        }

        return cridefine;
    }


    public static int getNextBindRoute(String brhid, String operator) {

        Integer nextrouteno = BMRouteBind.getInstance().getRouteNoByBindNo(EnumValue.BndType_YongHuDaiMa, operator);
        if (nextrouteno == null)
            nextrouteno = BMRouteBind.getInstance().getRouteNoByBindNo(EnumValue.BndType_WangDianDaiMa, brhid);

        if (nextrouteno != null) {
            switch (nextrouteno.intValue()) {
                case EnumValue.RouteNo_LiangJiShenPi:
                    return EnumValue.CriDefine_LiangJiShenPiLiuCheng;
                default:
                    return -1;
            }
        } else return -1;
    }

    public static String mapToNextOperBrhid(String thisbrh, String initbrhid, int nextstatus) {
        String Operbrhid = null;

        if (nextstatus == EnumValue.BMStatus_ErJiShenPi)
            Operbrhid = SCBranch.getSupBrh(thisbrh, EnumValue.BrhLevel_XianLianShe);
        else if (nextstatus == EnumValue.BMStatus_YiJiShenPi)
            Operbrhid = SCBranch.getSupBrh(thisbrh, EnumValue.BrhLevel_ShiLianShe);
        else
            Operbrhid = initbrhid;

        return Operbrhid;
    }

    static public void main(String[] args) {
        System.out.println(SCBranch.getSupBrh("907010130", EnumValue.BrhLevel_XinYongShe));
        //System.out.println(BMTrans.createBMTrans("00000000021",EnumValue.BMActType_DengJi,"907010130","system"));
        //System.out.println(BMTrans.cancelBMTrans("00000000020",16,"system"));
        //System.out.println(BMTrans.viewTrans("00000000020",16));
        //System.out.println(BMTrans.compltTrans("00000000021",23,"system"));
        //System.out.println(BMTrans.getReviewResult("00000000021",19));
        //System.out.println(BMTrans.acptBillRecv(4, "system"));
        //System.out.println(BMTrans.disBillRecv(3, "system"));
        //System.out.println(BMTrans.redisBillRecv(1, "system"));
        //System.out.println(BMTrans.redisBillRecvVoid(1, "system"));
        //System.out.println(BMTrans.disBillRecv(1,"bmnt1"));
        //System.out.println(BMTrans.disBillRecvVoid(1,"bmnt1"));
        BMTableData dt = BMTable.getBMTable("10000000024");
        System.out.println(BMTrans.checkApp("10000000024", dt));

    }
}
