package zt.cmsi.extrans;
/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description:5.7 授权信息查询, 3304</p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author not attributable
 *@created 2003年11月12日
 *@version 1.0
 */

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.MidControlCenter;
import zt.cmsi.biz.BMTableData;
import zt.cmsi.biz.LoanGrantData;
import zt.cmsi.biz.LoanGranted;
import zt.cmsi.biz.util;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.mydb.MyDB;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.logging.Logger;

public class AuthorizationInfoQueryService implements CMSTrans {
    public static String transID = "3304";
    public static Logger logger = Logger.getLogger("com.ebis.ebank.service.AuthorizationInfoQueryService");

    public ApDataPoint service(ApDataPoint apData) {
        apData.initRead();
        String txnTp = (String) apData.getData("TxnTp");
        String idNo = (String) apData.getData("IdNo");

        apData = null;
        ApDataPoint returnData = null;
        if (idNo == null) {
            returnData = AuthorizationInfoQueryService.makeErrorAp(txnTp, "S001", "证件号码为空");
            return returnData;
        }
        returnData = AuthorizationInfoQueryService.createData(txnTp, idNo);
        if (returnData.getDataSize() <= 0) {
            returnData = AuthorizationInfoQueryService.makeErrorAp(txnTp, "S006", "没有授权信息");
        }
        return returnData;
    }

    private static ApDataPoint makeErrorAp(String txnTp, String code, String msg) {
        ApDataPoint returnData = null;
        returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, txnTp);
        returnData.addRow();
        returnData.setData("TxnTp", "");
        returnData.setData("TxId", "");
        returnData.setData("IdNo", "");
        returnData.setData("CustomName", "");
        returnData.setData("CreditSum", "");

        returnData.setData("DueDate", "");
        returnData.setData("CreditInterestRate", "");
        returnData.setData("AuthorizationDate", "");
        returnData.setData("AuthorizationType", "");
        returnData.setData("AuthorizationValidDate", "");

        returnData.setData("CreditType", "");
        returnData.setData("CreditAccount", "");
        returnData.setData("DueBillCode", "");
        returnData.setData("ReturnCode", code);
        returnData.setData("TransInfo", msg);
        return returnData;
    }

    private static ApDataPoint createData(String txnTp, String id) {
        ApDataPoint returnData = null;
        CMClient client = CMClientMan.getCMClientFromID(id);
        if (client == null) {
            Debug.debug(Debug.TYPE_ERROR, "Client Info not found when collecting Grant Info in AuthorizationQueryService!");
            returnData = AuthorizationInfoQueryService.makeErrorAp(txnTp, "S003", "证件号不存在");
            return returnData;
        }

        DatabaseConnection dc = MyDB.getInstance().apGetConn();
        if (dc == null) {
            Debug.debug(Debug.TYPE_ERROR, "Can not get DB connection when collecting Grant Info in AuthorizationQueryService!");
            returnData = AuthorizationInfoQueryService.makeErrorAp(txnTp, "S002", "系统数据库不能连接");
            return returnData;
        }

        BMTableData bmdata = null;
        String bmno = null;

        returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, txnTp);

        try {
            String sSql = "select BMTable.BMNo from BMTable,BMLoanGranted where BMStatus=" + EnumValue.BMStatus_FaFang + " and ClientNo='" +
                    client.clientNo + "' and BMTable.BMNo=BMLoanGranted.BMNo and BMLoanGranted.AuthorizedStatus=" + EnumValue.AuthorizedStatus_YiShouQuan;

            RecordSet rs = null;
            Debug.debug(Debug.TYPE_SQL, sSql);
            rs = dc.executeQuery(sSql);
            while (rs.next()) {
                bmno = rs.getString(0);
                if (bmno != null) {
                    LoanGrantData grantdata = LoanGranted.getLoanGrant(bmno);
                    if (grantdata != null && grantdata.cmClient != null && grantdata.upToDateApp != null) {
                        System.out.println("^^^^^^" + bmno);
                        returnData.addRow();
                        returnData.setData("TxnTp", txnTp);
                        returnData.setData("TxId", util.fixLenString(bmno, 11));
                        returnData.setData("IdNo", util.fixLenString(grantdata.cmClient.ID, 25));
                        returnData.setData("CustomName", util.fixLenString(grantdata.cmClient.name, 40));
                        returnData.setData("CreditSum", util.numberToString(grantdata.upToDateApp.finalAmt, 10, 2, true));
                        returnData.setData("DueDate", util.calToString(grantdata.upToDateApp.finalEndDate, null));
                        returnData.setData("CreditInterestRate", util.numberToString(grantdata.upToDateApp.finalRate, 2, 4, true));
                        returnData.setData("AuthorizationDate", util.calToString(grantdata.beginDate, null));
                        if (grantdata.upToDateApp.bmTypeNo != null && grantdata.upToDateApp.bmTypeNo.intValue() == EnumValue.BMType_ZhanQi) {
                            returnData.setData("AuthorizationType", "2");
                        } else {
                            returnData.setData("AuthorizationType", "1");
                        }
                        returnData.setData("AuthorizationValidDate", util.calToString(grantdata.endDate, null));
                        returnData.setData("CreditType", "2");
                        if (grantdata.upToDateApp.bmTypeNo != null && grantdata.upToDateApp.bmTypeNo.intValue() == EnumValue.BMType_ZhanQi) {
                            returnData.setData("CreditAccount", util.fixLenString(grantdata.upToDateApp.origAccNo, 25)); //
                            returnData.setData("DueBillCode", util.fixLenString(grantdata.upToDateApp.origDueBillNo, 13));
                        } else {
                            returnData.setData("CreditAccount", util.fixLenString(" ", 25));
                            returnData.setData("DueBillCode", util.fixLenString(" ", 13));
                        }
                        //not include the mac
                        returnData.setData("ReturnCode", "W000");
                        returnData.setData("TransInfo", util.fixLenString("info:OK", 60));
                    }
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode == true) {
                Debug.debug(e);
            }
            Debug.debug(Debug.TYPE_ERROR, "when collecting Grant Info in AuthorizationQueryService!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            returnData = AuthorizationInfoQueryService.makeErrorAp(txnTp, "S004", "系统处理错误");
            return returnData;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }

        return returnData;
    }

    static public void main(String[] args) {
        AuthorizationInfoQueryService tt = new AuthorizationInfoQueryService();
        ApDataPoint returnData = null;
        returnData = new ApDataPoint(null);
        returnData.addMeta("TxnTp", 0, null);

        returnData.addMeta("IdNo", 0, null);

        returnData.addRow();
        returnData.setData("TxnTp", "3304");
        returnData.setData("IdNo", "370725999900000149");

        returnData = tt.service(returnData);
        ApDataPoint.toString(returnData);
    }
}
