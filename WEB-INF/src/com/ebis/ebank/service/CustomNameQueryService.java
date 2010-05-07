package com.ebis.ebank.service;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.dx.*;
import java.util.logging.*;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: 5.2 客户名称查询,3301</p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003年11月6日
 *@version    1.0
 */

public class CustomNameQueryService implements CMSTrans {
    /**
     *  Constructor for the Mid1111 object
     */
    public CustomNameQueryService() { }


    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年11月9日
     */
    public static String transID = "33s1";
    public static Logger logger=Logger.getLogger("com.ebis.ebank.service.CustomNameQueryService");


    /**
     *  Description of the Method
     *
     *@param  apData  Description of the Parameter
     *@return         Description of the Return Value
     */
    public ApDataPoint service(ApDataPoint apData) {
        apData.initRead();



        apData=null;

        ApDataPoint returnData = new ApDataPoint(null);
        MidControlCenter.makeOutApDataPointMeta(returnData, transID);
        returnData.addRow();
        returnData.setData("IdNo", "1234567891234567891234567");
        returnData.setData("CustomName", "zhongtian");
        returnData.setData("ReturnCode", "W000");
        returnData.setData("TransInfo", "info");
        return returnData;
    }

}
