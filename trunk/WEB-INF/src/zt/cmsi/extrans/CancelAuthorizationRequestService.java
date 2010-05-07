package zt.cmsi.extrans;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: 5.5 授权请求取消,3302</p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author not attributable
 *@created 2003年11月11日
 *@version 1.0
 */

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.MidControlCenter;
import zt.cmsi.biz.BMTable;
import zt.cmsi.biz.BMTableData;
import zt.cmsi.biz.util;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.utils.Debug;

import java.util.logging.Logger;

public class CancelAuthorizationRequestService implements CMSTrans {
    public static String transID = "3302";
    public static Logger logger = Logger.getLogger("com.ebis.ebank.service.CancelAuthorizationRequestService");

    public ApDataPoint service(ApDataPoint apData) {
        int errorcode = 0;
        String retmsg = null;

        try {
            apData.initRead();
            String txnTp = (String) apData.getData("TxnTp");
            String txId = (String) apData.getData("TxId");
            String idNo = (String) apData.getData("IdNo");
            String creditSum = (String) apData.getData("CreditSum");
            String dueDate = (String) apData.getData("DueDate");
            String creditInterestRate = (String) apData.getData("CreditType");
            apData = null;

            if (Debug.isDebugMode) {
                Debug.debug(Debug.TYPE_MESSAGE, "txid=" + txId);
            }

            BMTableData bmtable = BMTable.getBMTable(txId);
            if (txId == null) {
                errorcode = -1;
                retmsg = "业务号为空";
            } else {
                if (bmtable == null) {
                    errorcode = -1;
                    retmsg = "对应的业务记录未找到";
                } else {
                    if (bmtable.bmStatus > EnumValue.BMStatus_FaFang) {
                        errorcode = -2;
                        retmsg = "对应的业务状态已经取消或贷款已经发放";
                    } else {
                        //Add reducec the 贷款余额 loanbal在这里
                        DatabaseConnection con = MyDB.getInstance().apGetConn();
                        if (BMTable.cancelBMTable(txId, "000000") < 0) {
                            errorcode = -3;
                            retmsg = "取消业务失败";
                            MyDB.getInstance().apReleaseConn(-1);
                        } else {
//              String sql = "update bmcreditlimit set loanbal=loanbal-(select finalamt from bmtableapp where bmno='" + txId + "') where clientno=(select clientno from cmclient where id='" + idNo +
//                "')";
                            String sql = "update bmcreditlimit set loanbal=loanbal-(select finalamt from bmtableapp where bmno='" + txId + "') where clientno='" + bmtable.clientNo + "'";
                            Debug.debug(Debug.TYPE_SQL, sql);
                            MyDB.getInstance().apReleaseConn(con.executeUpdate(sql));
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode) {
                Debug.debug(e);
            }
            errorcode = -4;
            retmsg = "出现异常";
        }

        ApDataPoint returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, transID);

        returnData.addRow();
        if (errorcode == -1) {
            returnData.setData("ReturnCode", "E021");
            returnData.setData("TransInfo", util.fixLenString(retmsg, 60));
        } else if (errorcode == -2) {
            returnData.setData("ReturnCode", "E022");
            returnData.setData("TransInfo", util.fixLenString(retmsg, 60));
        } else if (errorcode < 0) {
            returnData.setData("ReturnCode", "E023");
            returnData.setData("TransInfo", util.fixLenString(retmsg, 60));
        } else {
            returnData.setData("ReturnCode", "W000");
            returnData.setData("TransInfo", util.fixLenString("成功", 60));
        }
        return returnData;
    }

    public static void main(String args[]) {
        CancelAuthorizationRequestService aaa = new CancelAuthorizationRequestService();
        ApDataPoint apData = new ApDataPoint(null);
        //MidControlCenter.makeOutApDataPointMeta(apData, transID);
        apData.addMeta("TxnTp", 1, null);
        apData.addMeta("TxId", 1, null);
        apData.addMeta("IdNo", 1, null);
        apData.addMeta("CreditSum", 1, null);
        apData.addMeta("DueDate", 1, null);
        apData.addMeta("CreditType", 1, null);

        ApDataPoint ret = null;
        apData.toString(apData);

        apData.addRow();

        apData.setData("TxnTp", "3302");
        apData.setData("TxId", "10000000578");
        apData.setData("IdNo", "370725999900000149");
        //apData.setData("CustomName","test");
        apData.setData("CreditSum", "       100.00");
        apData.setData("DueDate", "20050109");
        apData.setData("CreditType", "1");

        ret = aaa.service(apData);
        if (ret == null) {
            System.out.println("==========ret is null=============");
        } else {
            ret.toString(ret);
        }
    }

}
