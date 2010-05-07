package zt.cmsi.extrans;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.MidControlCenter;
import com.ebis.encrypt.EncryptData;
import zt.cmsi.client.CMClient;
import zt.cmsi.client.CMClientMan;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.utils.Debug;

import java.util.logging.Logger;

/**
 * <p>
 * <p/>
 * Title: </p> <p>
 * <p/>
 * Description: 5.2 客户名称查询,3301</p> <p>
 * <p/>
 * Copyright: Copyright (c) 2003</p> <p>
 * <p/>
 * Company: </p>
 *
 * @author not attributable
 * @version 1.0
 * @created 2003年11月6日
 */

public class CustomNameQueryService implements CMSTrans {


    /**
     * Constructor for the Mid1111 object
     */
    public CustomNameQueryService() {
    }


    /**
     * Description of the Field
     *
     * @author sun
     * @since 2003年11月9日
     */
    public static String transID = "33s1";
    public static Logger logger = Logger.getLogger("com.ebis.ebank.service.CustomNameQueryService");


    /**
     * Description of the Method
     *
     * @param apData Description of the Parameter
     * @return Description of the Return Value
     */
    public ApDataPoint service(ApDataPoint apData) {
        int errorcode = 0;
        String retmsg = null;
        CMClient cm = null;
        String idNo = null;
        String passwd = null;

        try {
            apData.initRead();
            String txnTp = (String) apData.getData("TxnTp");
            String txId = (String) apData.getData("BranchId");
            idNo = (String) apData.getData("IdNo");
            passwd = (String) apData.getData("Passwd");

            apData = null;

            if (Debug.isDebugMode) {
                Debug.debug(Debug.TYPE_MESSAGE, "idno=" + idNo);
            }

            cm = CMClientMan.getCMClientFromID(idNo);

            if (cm == null) {
                errorcode = -1;
                retmsg = "客户不存在";
            } else if (passwd == null || passwd.trim().length() <= 0) {
                errorcode = -1;
                retmsg = "密码不允许为空";
            } else {

                EncryptData ed = new EncryptData();
                String encrypted = new String(ed.enPasswd(passwd.getBytes()));

                zt.platform.cachedb.ConnectionManager db = zt.platform.cachedb.ConnectionManager.getInstance();
                String dbpasswd = db.getCellValue("passwd", "bmcreditlimit", "typeno=" + EnumValue.BMType_DaiKuanZhengDaiKuan + " and clientno='" + cm.clientNo + "'");
                if (dbpasswd == null) {
                    errorcode = -1;
                    retmsg = "贷款证还没有设置密码";
                } else {
                    if (dbpasswd.compareToIgnoreCase(encrypted) != 0) {
                        errorcode = -1;
                        retmsg = "密码错误";
                    }
                }
            }
        }
        catch (Exception e) {
            if (Debug.isDebugMode) Debug.debug(e);
            errorcode = -1;
            retmsg = "出现异常";
        }


        ApDataPoint returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, transID);

        if (errorcode != 0) {
            returnData.addRow();
            returnData.setData("ReturnCode", "E002");
            returnData.setData("TransInfo", retmsg);
        } else {
            returnData.addRow();
            returnData.setData("IdNo", idNo);
            returnData.setData("CustomName", cm.name);
            returnData.setData("ReturnCode", "W000");
            returnData.setData("TransInfo", "成功");
        }

        if (Debug.isDebugMode && returnData != null) ApDataPoint.toString(returnData);
        return returnData;
    }

}
