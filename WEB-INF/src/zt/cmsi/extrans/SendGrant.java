package zt.cmsi.extrans;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.defines.ConfigManager;
import com.ebis.ebank.dx.BTPControlCenter;
import com.ebis.ebank.dx.ControlCenter;
import com.ebis.ebank.dx.MidControlCenter;
import zt.cmsi.biz.LoanGrantData;
import zt.cmsi.biz.LoanGranted;
import zt.cmsi.biz.util;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cmsi.pub.confitem;
import zt.platform.utils.Debug;

import java.util.List;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class SendGrant {
    static public int send(IOEntity io) {
        if (io == null) return ErrorCode.PARAM_IS_NULL;
        if (io.BMNo == null) return ErrorCode.PARAM_IS_NULL;
        LoanGrantData grantdata = LoanGranted.getLoanGrant(io.BMNo);

        if (grantdata == null) return ErrorCode.GRANT_RECORD_NOT_EXIST;
        if (grantdata.cmClient == null) return ErrorCode.CLIENT_NOT_FOUND;
        if (grantdata.upToDateApp == null) return ErrorCode.GET_UPTODATE_APP_NULL;

        if (io.ifCreateGrant == true)
            return SendGrant.sendGrantTrue(io, grantdata);
        else
            return SendGrant.sendRevokeTrue(io, grantdata);

    }

    static public int sendRevokeTrue(IOEntity io, LoanGrantData grantdata) {
        //System.out.println("------------------------------------");
        ControlCenter center = new BTPControlCenter();

        try {
            String transId = "3306";
            ConfigManager manager = ConfigManager.getInstance();
            List l = manager.getTransField("3306");
            ApDataPoint data = new ApDataPoint(null);
            MidControlCenter.makeApDataPointMeta(data, "3306");

            data.addRow();

            data.setData("TxnTp", transId);
            data.setData("TxId", util.fixLenString(io.BMNo, 11)); //
            data.setData("IdNo", util.fixLenString(grantdata.cmClient.ID, 25));
            data.setData("CreditSum",
                    util.numberToString(grantdata.upToDateApp.finalAmt, 10, 2, true));

            data.setData("DueDate", util.calToString(grantdata.upToDateApp.finalEndDate, null));
            data.setData("CreditType", "2");

            //data.initRead();
            data.setData("MAC", new String(MidControlCenter.calculateMac(data)));

            if (Debug.isDebugMode == true) System.out.println("Revoke apData : \n" + data.toString());
            long series = center.send(data, transId);
            if (Debug.isDebugMode == true) System.out.println("Revoke series is " + series);
            if (series < 0)
                return ErrorCode.SEND_GRANT_SEND_FAILED;

            //if(Debug.isDebugMode == true) System.out.println("Revoke Data is \n" + data.toString());

            ApDataPoint returnData = new ApDataPoint(null);
            long no = center.receive(returnData, series,
                    confitem.SEND_GRANT_WAIT_TIME);

            if (no < 0)
                return ErrorCode.SEND_GRANT_RECV_FAILED;

            if (Debug.isDebugMode == true) System.out.println("Return no is " + no);
            if (Debug.isDebugMode == true) System.out.println("Return Data is \n" + returnData.toString());
            returnData.toString(returnData);

            if (false)//returnData.getRequestSuccessed() == false)
                return ErrorCode.SEND_GRANT_RECV_RESULT_FAILED;
            else {
                String retcode, msg;
                retcode = SendGrant.getRetCode(returnData);
                msg = SendGrant.getRetMsg(returnData);
                Debug.debug(Debug.TYPE_MESSAGE,
                        "Revoke Sending Process Result: RetCode=" + retcode +
                                " Msg=" + msg);

                if (retcode != null && retcode.compareToIgnoreCase("W000") == 0)
                    return 0;
                else
                    return ErrorCode.SEND_GRANT_RECV_RESULT_FAILED;
            }
        } catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when sending Loan Grant in Core Sending Process!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return ErrorCode.EXCPT_FOUND;
        }
    }


    static public int sendGrantTrue(IOEntity io, LoanGrantData grantdata) {
        ControlCenter center = new BTPControlCenter();

        try {
            String transId = "3305";
            ConfigManager manager = ConfigManager.getInstance();
            List l = manager.getTransField("3305");
            ApDataPoint data = new ApDataPoint(null);
            MidControlCenter.makeApDataPointMeta(data, "3305");

            data.addRow();

            data.setData("TxnTp", transId);
            data.setData("BranchId", util.fixLenString(grantdata.brhID, 12)); //
            data.setData("TxId", util.fixLenString(io.BMNo, 11));
            data.setData("IdNo", util.fixLenString(grantdata.cmClient.ID, 25));
            data.setData("CreditSum",
                    util.numberToString(grantdata.upToDateApp.finalAmt, 10, 2, true));

            data.setData("DueDate", util.calToString(grantdata.upToDateApp.finalEndDate, null));
            data.setData("CreditInterestRate",
                    util.numberToString(grantdata.upToDateApp.finalRate, 2, 4, true));
            data.setData("AuthorizationDate", util.calToString(grantdata.beginDate, null));
            data.setData("AuthorizationValidDate", util.calToString(grantdata.endDate, null));
            if (grantdata.upToDateApp.bmTypeNo != null && grantdata.upToDateApp.bmTypeNo.intValue() == EnumValue.BMType_ZhanQi)
                data.setData("AuthorizationType", "2");
            else
                data.setData("AuthorizationType", "1");

            data.setData("CreditType", "2");
            if (grantdata.upToDateApp.bmTypeNo != null && grantdata.upToDateApp.bmTypeNo.intValue() == EnumValue.BMType_ZhanQi) {
                data.setData("CreditAccount", grantdata.upToDateApp.origAccNo); //
                data.setData("DueBillCode", grantdata.upToDateApp.origDueBillNo);
            } else {
                data.setData("CreditAccount", " ");
                data.setData("DueBillCode", " ");
            }

            //not include the mac

            data.setData("ReturnCode", "W000");
            data.setData("TransInfo", "info:OK");

            //data.initRead();
            data.setData("MAC", new String(MidControlCenter.calculateMac(data)));

            //byte buffer[] = new byte[1024 * 4];
            //if(Debug.isDebugMode == true) System.out.println("apData : \n" + data.toString());
            long series = center.send(data, transId);
            if (Debug.isDebugMode == true) System.out.println("series is " + series);
            if (series < 0)
                return ErrorCode.SEND_GRANT_SEND_FAILED;

            if (Debug.isDebugMode == true) System.out.println("Data is \n" + data.toString());

            ApDataPoint returnData = new ApDataPoint(null);
            long no = center.receive(returnData, series,
                    confitem.SEND_GRANT_WAIT_TIME);

            if (Debug.isDebugMode == true) System.out.println("no is:" + no);
            if (no < 0)
                return ErrorCode.SEND_GRANT_RECV_FAILED;

            if (Debug.isDebugMode == true) System.out.println("Return no is " + no);
            if (Debug.isDebugMode == true) System.out.println("Return Data is \n" + returnData.toString());
            //returnData.toString(returnData);

            if (false)//returnData.getRequestSuccessed() == false)
                return ErrorCode.SEND_GRANT_RECV_RESULT_FAILED;
            else {
                String retcode, msg;
                retcode = SendGrant.getRetCode(returnData);
                msg = SendGrant.getRetMsg(returnData);
                Debug.debug(Debug.TYPE_MESSAGE,
                        "Grant Sending Process Result: RetCode=" + retcode +
                                " Msg=" + msg);

                if (retcode != null && retcode.compareToIgnoreCase("W000") == 0)
                    return 0;
                else
                    return ErrorCode.SEND_GRANT_RECV_RESULT_FAILED;

            }
        } catch (Exception e) {
            if (Debug.isDebugMode == true) Debug.debug(e);
            Debug.debug(Debug.TYPE_ERROR, "Exception when sending Loan Grant in Core Sending Process!");
            Debug.debug(Debug.TYPE_ERROR, e.getMessage());
            return ErrorCode.EXCPT_FOUND;
        }
    }

    public static String getRetCode(ApDataPoint data) {
        if (data == null) return null;
        if (data.getBlockSize() < 1) return null;
        return data.getBlockName(0);
    }

    public static String getRetMsg(ApDataPoint data) {
        if (data == null) return null;
        if (data.getBlockSize() < 1) return null;
        data.setCurrentBlock(0);
        data.initRead();
        if (!data.eof()) {
            return (String) data.getData(0);
        } else
            return null;
    }


    public static void main(String[] args) {
        SendGrant sendGrant1 = new SendGrant();
    }

}