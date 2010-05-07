package com.ebis.ebank.service;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.*;
import java.util.logging.*;

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
 *@author     not attributable
 *@created    2003年11月11日
 *@version    1.0
 */

public class CancelAuthorizationRequestService implements CMSTrans {
    /**
     *  Description of the Method
     *
     *@param  apDataExch  Description of the Parameter
     *@return             Description of the Return Value
     */
    public static String transID = "3302";
/**
 *  Description of the Field
 */
public static Logger logger = Logger.getLogger("com.ebis.ebank.service.CancelAuthorizationRequestService");


    public ApDataPoint service(ApDataPoint apData) {
        apData.initRead();
        String txnTp=(String)apData.getData("TxnTp");
        String txId=(String)apData.getData("TxId");
        String idNo=(String)apData.getData("IdNo");
        String creditSum=(String)apData.getData("CreditSum");
        String dueDate=(String)apData.getData("DueDate");
        String creditInterestRate=(String)apData.getData("CreditType");
        apData = null;


        ApDataPoint returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, transID);
        returnData.addRow();
        returnData.setData("ReturnCode", "W000");
        returnData.setData("TransInfo", "12345543211234554321");
        return returnData;


    }
}
