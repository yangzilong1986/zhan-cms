package zt.cmsi.extrans;
/**
 *  <p>
 *
 *  Title: 5.3 贷款授权请求</p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author JGO
 *@created 2003年11月9日
 *@version 1.0
 */

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.MidControlCenter;
import zt.cms.bm.loancert.Loancert;
import zt.cms.bm.loancert.LoancertFactory;
import zt.cmsi.biz.BMTable;
import zt.cmsi.biz.LoanGranted;
import zt.cmsi.biz.UpToDateApp;
import zt.cmsi.biz.util;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.confitem;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.define.SystemDate;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.logging.Logger;

public class RequestCreditAuthorizationService implements CMSTrans {
    public static String transID = "3301";
    public static Logger logger = Logger.getLogger("com.ebis.ebank.service.RequestCreditAuthorizationService");
    private static String ec = "E001";
    private String txId = "";
    private String returnCode = null;
    private String backInfo = null;
    private Loancert loancert = null;

    private String in_TxnTP = null;
    private String in_IdNo = null;
    private String in_CustomName = null;
    private BigDecimal in_Amt = null;
    private BigDecimal in_Rate = null;
    private Calendar in_DueDate = null;
    private String in_Orig_Amt = null;
    private String in_Orig_Rate = null;
    private String in_Orig_DueDate = null;
    private String in_Brhid = null;

    public int extractAPDate(ApDataPoint apData) {
        if (apData == null) {
            returnCode = ec;
            return -1;
        }

        try {
            apData.initRead();
            in_TxnTP = (String) apData.getData("TxnTp");
            in_IdNo = (String) apData.getData("IdNo");
            in_CustomName = (String) apData.getData("CustomName");
            in_Orig_Amt = (String) apData.getData("CreditSum");
            in_Orig_DueDate = (String) apData.getData("DueDate");
            in_Orig_Rate = (String) apData.getData("CreditInterestRate");
            in_Brhid = (String) apData.getData("Brhid");
            if (Debug.isDebugMode) {
                Debug.debug(Debug.TYPE_MESSAGE, "txntp:" + in_TxnTP + " IDNO=" + in_IdNo
                        + " customername=" + in_CustomName + " amt=" + in_Orig_Amt +
                        " duedate=" + in_Orig_DueDate + " rate=" + in_Orig_Rate);
            }

            if (in_TxnTP == null || in_IdNo == null || in_CustomName == null ||
                    in_Orig_Amt == null || in_Orig_DueDate == null || in_Orig_Rate == null) {
                returnCode = ec;
                backInfo = "数据有空值";
                return -1;
            }

            try {
                in_Amt = new BigDecimal(Double.parseDouble(in_Orig_Amt));
                in_Amt = in_Amt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                in_Rate = new BigDecimal(Double.parseDouble(in_Orig_Rate));
                in_Rate = in_Rate.setScale(4, BigDecimal.ROUND_HALF_EVEN);
                in_DueDate = util.stringToCal(in_Orig_DueDate);
            }
            catch (Exception e) {
                if (Debug.isDebugMode) {
                    Debug.debug(e);
                }
                returnCode = ec;
                backInfo = "数字或日期数据格式不正确";
                return -1;
            }

            if (in_IdNo.length() <= 0) {
                returnCode = ec;
                backInfo = "身份证为空";
                return -1;
            }

            return 0;
        }
        catch (Exception e) {
            if (Debug.isDebugMode) {
                Debug.debug(e);
            }
            returnCode = ec;
            backInfo = "出现异常";
            return -1;
        }
    }

    public ApDataPoint service(ApDataPoint apData) {
        if (this.extractAPDate(apData) < 0) {
            return makeOutApData();
        }

        if (!validUserAndSumAndDueDate()) {
            return makeOutApData();
        }

        if (getRequestData() < 0) {
            returnCode = ec;
            backInfo = "建立业务授权失败";
        }

        return makeOutApData();

    }

    private boolean validUserAndSumAndDueDate() {
        Calendar thisdate = SystemDate.getSystemDate1();

        Loancert loancert = LoancertFactory.findLoancertById(this.in_IdNo);

        String str = "select canloancert from scbranchapp where brhid = '" + in_Brhid + "' ";
        DatabaseConnection con = MyDB.getInstance().apGetConn();
        RecordSet rs = con.executeQuery(str);
        Debug.debug(Debug.TYPE_SQL, str);
        if (rs.next()) {
            if (rs.getInt("canloancert") == 0) {
                this.returnCode = "E009";
                this.backInfo = "网点不存在或者网点不可以贷款证贷款";
                return false;
            }
        } else {
            this.returnCode = "E009";
            this.backInfo = "网点不存在或者网点不可以贷款证贷款";
            return false;
        }
        MyDB.getInstance().apReleaseConn(1);


        if (loancert == null) {
            this.returnCode = "E010";
            this.backInfo = "客户授信信息不存在";
            return false;
        }

        if (loancert.getClientNo() == null || loancert.getStartDate() == null || loancert.getEndDate() == null
                || loancert.getCreditLimit() == null || loancert.getLoanBal() == null) {
            this.returnCode = "E011";
            this.backInfo = "客户授信信息不完整";
            return false;
        }

        if (loancert.getDisabled() != EnumValue.LoanCertSts_KeYou) {
            this.returnCode = "E012";
            this.backInfo = "";
            if (loancert.getDisabled() == EnumValue.LoanCertSts_DaiKuanZhengGuaShi)
                this.backInfo += "贷款证挂失";
            else if (loancert.getDisabled() == EnumValue.LoanCertSts_MiMaGuaShi)
                this.backInfo += "贷款证密码挂失";
            else
                this.backInfo += "贷款证状态为禁止";
            return false;
        }

        if (loancert.getStartDate().after(thisdate)) {
            this.returnCode = "E013";
            this.backInfo = "贷款开始日期早于授信生效日期";
            return false;
        }

        if (loancert.getEndDate().before(thisdate)) {
            this.returnCode = "E013";
            this.backInfo = "贷款开始日期晚于授信结束日期";
            return false;
        }

        BigDecimal bal = loancert.getCreditLimit().subtract(loancert.getLoanBal());
        if (bal.compareTo(this.in_Amt) < 0) {
            this.returnCode = "E015";
            this.backInfo = "贷款超过授信额度";
            return false;
        }
        String brhid1 = loancert.getBrhid();
        String brhid2 = in_Brhid;
        if (brhid1 == null || brhid2 == null || !brhid1.trim().equals(brhid2.trim())) {
            this.returnCode = "E016";
            this.backInfo = "客户的授信网点（" + brhid1 + "）与本营业网点" + brhid2 + "不符";
            return false;
        }

        this.loancert = loancert;
        return true;
    }

    private int getRequestData() {

        int errorcode = 0;
        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            return ErrorCode.NO_DB_CONN;
        }
        String bmno = null;
        try {
            bmno = BMTable.createBMTable(EnumValue.BMType_DaiKuanZhengDaiKuan,
                    this.loancert.getClientNo(),
                    this.loancert.getBrhid()
                    , this.loancert.getBrhid(), "000000");
            if (bmno != null) {
                errorcode = BMTable.updateTableStatus(bmno, EnumValue.BMStatus_FaFang, "000000");
                if (errorcode >= 0) {
                    this.txId = bmno;
                    UpToDateApp data = null;

                    //得到原来的授信信息,然后在这个基础上进行编辑,如果没有,新建一个
                    String getBmno = "select bmno from bmtableapp where clientno='" + loancert.getClientNo() + "' and typeno=" + zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin + " order by bmno desc";
                    RecordSet bmnoRs = dc.executeQuery("getbmno");
                    if (bmnoRs.next()) {
                        data = BMTable.getUpToDateApp(bmnoRs.getString("BMNO"));
                    } else {
                        data = new UpToDateApp();
                    }
                    data.appAmt = this.in_Amt;
                    data.appDate = SystemDate.getSystemDate1();
                    data.appEndDate = this.in_DueDate;
                    data.appStartDate = SystemDate.getSystemDate1();
                    data.finalStartDate = SystemDate.getSystemDate1();
                    data.finalEndDate = this.in_DueDate;
                    data.finalAmt = this.in_Amt;
                    data.finalRate = this.in_Rate;
                    data.ifRespLoan = new Integer(loancert.getIfRespLoan());
                    data.firstResp = loancert.getFirstResp();
                    data.decidedBy = loancert.getDecidedby();
                    data.contractNo = confitem.LOAN_CERT_ISSUE;


                    errorcode = BMTable.updateUpToDateApp(bmno, data);
                    if (errorcode >= 0) {
                        errorcode = LoanGranted.createGrant(bmno, "000000", this.loancert.getBrhid());
                        if (errorcode >= 0) {
                            errorcode = LoanGranted.sendGrantOK(bmno);
                            //放款
                            if (errorcode >= 0) {
                                String sql = "update bmcreditlimit set loanbal=loanbal+" + data.appAmt + " where clientno='" + loancert.getClientNo() + "'";
                                errorcode = dc.executeUpdate(sql);
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
            Debug.debug(Debug.TYPE_ERROR, "Exception when creating credit authorization!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            errorcode = ErrorCode.EXCPT_FOUND;
        }
        finally {
            MyDB.getInstance().apReleaseConn(errorcode);
            return errorcode;
        }
    }

    private ApDataPoint makeOutApData() {

        ApDataPoint returnData = new ApDataPoint(null);

        if (this.returnCode == null) {
            MidControlCenter.makeOutApDataPointMeta(returnData, transID);
            returnData.addRow();
            returnData.setData("BranchId", util.fixLenString(this.loancert.getBrhid(), 12));
            returnData.setData("TxId", util.fixLenString(this.txId, 11));
            returnData.setData("IdNo", util.fixLenString(this.in_IdNo, 25));
            returnData.setData("CreditSum", util.numberToString(new BigDecimal(this.in_Orig_Amt.trim()), 10, 2, true));
            returnData.setData("DueDate", this.in_Orig_DueDate);
            returnData.setData("CreditInterestRate", util.numberToString(new BigDecimal(this.in_Orig_Rate.trim()), 2, 4, true));

            returnData.setData("CustomeName", this.in_CustomName);
            returnData.setData("AuthorizationDate", SystemDate.getSystemDate5(null));
            returnData.setData("AuthorizationType", "1");
            returnData.setData("AuthorizationValidDate", SystemDate.getSystemDate5(null));
            returnData.setData("CreditType", "1");

            returnData.setData("MAC", "");
            returnData.setData("ReturnCode", "W000");
            returnData.setData("BackInfo", "成功");
        } else {
            MidControlCenter.makeOutApDataPointMeta(returnData, transID);
            returnData.addRow();
            returnData.setData("BranchId", " ");
            returnData.setData("TxId", " ");
            returnData.setData("IdNo", " ");
            returnData.setData("CreditSum", " ");
            returnData.setData("DueDate", " ");
            returnData.setData("CreditInterestRate", " ");

            returnData.setData("CustomeName", " ");
            returnData.setData("AuthorizationDate", " ");
            returnData.setData("AuthorizationType", " ");
            returnData.setData("AuthorizationValidDate", " ");
            returnData.setData("CreditType", " ");

            returnData.setData("MAC", " ");
            returnData.setData("ReturnCode", this.returnCode);
            returnData.setData("BackInfo", this.backInfo);
        }

        return returnData;

    }

    public static void main(String args[]) {
        RequestCreditAuthorizationService aaa = new RequestCreditAuthorizationService();
        ApDataPoint apData = new ApDataPoint(null);
        //MidControlCenter.makeOutApDataPointMeta(apData, transID);
        apData.addMeta("TxnTp", 1, null);
        apData.addMeta("IdNo", 1, null);
        apData.addMeta("CustomName", 1, null);
        apData.addMeta("CreditSum", 1, null);
        apData.addMeta("DueDate", 1, null);
        apData.addMeta("CreditInterestRate", 1, null);

        ApDataPoint ret = null;
        apData.toString(apData);

        apData.addRow();

        apData.setData("TxnTp", "3301");
        apData.setData("IdNo", "370725999900000149");
        apData.setData("CustomName", "test");
        apData.setData("CreditSum", "       100.00");
        apData.setData("DueDate", "20050109");
        apData.setData("CreditInterestRate", " 7.8999");

        ret = aaa.service(apData);
        if (ret == null) {
            System.out.println("==========ret is null=============");
        } else {
            ret.toString(ret);
        }
    }
}
