package zt.cms.report.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class BaseInfoObject {
    private String strBrhID;
    private String strClientName;
    private String strAddressInLaw;
    private String strLoanType5;
    private String strLoanCat3;
    private String strID;
    private String strClientMgr;
    private String strFirstResp;
    private String strFisrtRespPct;

    public BaseInfoObject(String strBrhID1,
                          String strClientName1,
                          String strAddressInLaw1,
                          String strLoanType51,
                          String strLoanCat31,
                          String strID1,
                          String strClientMgr1,
                          String strFirstResp1,
                          String strFisrtRespPct1)
    {
        this.strBrhID = strBrhID1;
        this.strClientName = strClientName1;
        this.strAddressInLaw = strAddressInLaw1;
        this.strLoanType5 = strLoanType51;
        this.strLoanCat3 = strLoanCat31;
        this.strID = strID1;
        this.strClientMgr = strClientMgr1;
        this.strFirstResp = strFirstResp1;
        this.strFisrtRespPct = strFisrtRespPct1;
    }

    public String getStrBrhID()
    {
        return strBrhID;
    }

    public String getStrClientName()
    {
        return strClientName;
    }

    public String getStrAddressInLaw()
    {
        return strAddressInLaw;
    }

    public String getStrLoanType5()
    {
        return strLoanType5;
    }

    public String getStrLoanCat3()
    {
        return strLoanCat3;
    }

    public String getStrID()
    {
        return strID;
    }

    public String getStrClientMgr()
    {
        return strClientMgr;
    }

    public String getStrFirstResp()
    {
        return strFirstResp;
    }

    public String getStrFisrtRespPct()
    {
        return strFisrtRespPct;
    }
}