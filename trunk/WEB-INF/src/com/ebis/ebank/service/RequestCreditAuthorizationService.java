package com.ebis.ebank.service;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.*;
import java.util.logging.*;

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
 *@author     not attributable
 *@created    2003年11月9日
 *@version    1.0
 */

public class RequestCreditAuthorizationService implements CMSTrans {
    /**
     *  Description of the Method
     *
     *@param  apDataExch  Description of the Parameter
     *@return             Description of the Return Value
     */

    public static String transID = "3301";
    /**
     *  Description of the Field
     */
    public static Logger logger = Logger.getLogger("com.ebis.ebank.service.RequestCreditAuthorizationService");


    /**
     *  Description of the Method
     *
     *@param  apData  Description of the Parameter
     *@return         Description of the Return Value
     */

    public ApDataPoint service(ApDataPoint apData) {
        apData.initRead();
        String txnTp=(String)apData.getData("TxnTp");
        String idNo=(String)apData.getData("IdNo");
        String customName=(String)apData.getData("CustomName");
        String creditSum=(String)apData.getData("CreditSum");
        String maturity=(String)apData.getData("Maturity");
        String creditInterestRate=(String)apData.getData("CreditInterestRate");


        apData = null;
        ApDataPoint returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, transID);
        returnData.addRow();
        returnData.setData("BranchId", "988010100");
        returnData.setData("TxId", "12345678912");
        returnData.setData("IdNo", idNo);
        returnData.setData("CreditSum","       100.00");
        returnData.setData("DueDate", "20030211");
        returnData.setData("CreditInterestRate", " 0.0010");

        returnData.setData("CustomeName", customName);
        returnData.setData("AuthorizationDate", "20020315");
        returnData.setData("AuthorizationType", "1");
        returnData.setData("AuthorizationValidDate", "20031112");
        returnData.setData("CreditType", "1");

        returnData.setData("MAC", "");
        returnData.setData("ReturnCode", "W000");
        returnData.setData("BackInfo", "information");

        return returnData;

    }
}
