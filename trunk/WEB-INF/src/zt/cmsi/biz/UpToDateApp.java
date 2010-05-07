//Source file: e:\\java\\zt\\cmsi\\biz\\UpToDateApp.java

package zt.cmsi.biz;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Description of the Class
 *
 * @author Administrator
 * @created 2004Äê1ÔÂ12ÈÕ
 */
public class UpToDateApp extends Object implements Cloneable {
    /**
     * Description of the Field
     */
    public String clientNo = null;
    /**
     * Description of the Field
     */
    public Integer bmTypeNo = null;
    /**
     * Description of the Field
     */
    public Calendar appDate = null;
    /**
     * Description of the Field
     */
    public String curNo = null;
    /**
     * Description of the Field
     */
    public BigDecimal rate = null;
    /**
     * Description of the Field
     */
    public BigDecimal appAmt = null;
    /**
     * Description of the Field
     */
    public Calendar appStartDate = null;
    /**
     * Description of the Field
     */
    public Calendar appEndDate = null;
    /**
     * Description of the Field
     */
    public Integer appMonths = null;
    /**
     * Description of the Field
     */
    public Integer loanType3 = null;
    /**
     * Description of the Field
     */
    public Integer loanType5 = null;
    /**
     * Description of the Field
     */
    public Integer loanCat3 = null;
    /**
     * Description of the Field
     */
    public String loanPurpose = null;
    /**
     * Description of the Field
     */
    public BigDecimal finalRate = null;
    /**
     * Description of the Field
     */
    public BigDecimal finalAmt = null;
    /**
     * Description of the Field
     */
    public BigDecimal finalAmt2 = null;
    /**
     * Description of the Field
     */
    public Calendar finalStartDate = null;
    /**
     * Description of the Field
     */
    public Calendar finalEndDate = null;
    /**
     * Description of the Field
     */
    public Integer finalMonths = null;
    /**
     * Description of the Field
     */
    public Integer resultType = null;
    /**
     * Description of the Field
     */
    public String finalCurNo = null;
    /**
     * Description of the Field
     */
    public String origBMNo = null;
    /**
     * Description of the Field
     */
    public String origAccNo = null;
    /**
     * Description of the Field
     */
    public String origDueBillNo = null;
    /**
     * Description of the Field
     */
    public Integer ifRespLoan = null;
    /**
     * Description of the Field
     */
    public String firstResp = null;
    /**
     * Description of the Field
     */
    public BigDecimal firstRespPct = null;
    /**
     * Description of the Field
     */
    public String contractNo = null;
    /**
     * Description of the Field
     */
    public String sContractNo = null;
    /**
     * Description of the Field
     */
    public Integer interestType = null;

    /**
     * Description of the Field
     */
    public Integer clientType = null;
    /**
     * Description of the Field
     */
    public Integer eComType = null;
    /**
     * Description of the Field
     */
    public Integer eComDeptType = null;
    /**
     * Description of the Field
     */
    public Integer etpScopType = null;
    /**
     * Description of the Field
     */
    public Integer sectorCat1 = null;
    /**
     * Description of the Field
     */
    public String clientMgr = null;
    public String decidedBy = null;
    public BigDecimal bRate = null;
    public BigDecimal fRate = null;
    public String cnlNo = null;


    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public Object clone() {
        UpToDateApp newapp = new UpToDateApp();
        /*
         *  UpToDateApp newapp = new UpToDateApp();
         *  newapp.appAmt = new BigDecimal(this.appAmt.doubleValue());
         *  newapp.appDate = this.appDate.clone();
         *  newapp.appEndDate = this.appEndDate.clone();
         *  newapp.appMonths = new Integer(this.appmonths
         *  newapp.appStartDate =
         *  newapp.bmTypeNo =
         *  newapp.clientMgr =
         *  newapp.clientNo =
         *  newapp.clientType =
         *  newapp.contractNo =
         *  newapp.curNo =
         *  newapp.eComDeptType =
         *  newapp.eComType =
         *  newapp.etpScopType =
         *  newapp.finalAmt =
         *  newapp.finalAmt2 =
         *  newapp.finalCurNo =
         *  newapp.finalEndDate =
         *  newapp.finalMonths =
         *  newapp.finalRate =
         *  newapp.finalStartDate =
         *  newapp.firstResp =
         *  newapp.firstRespPct =
         *  newapp.ifRespLoan =
         *  newapp.interestType =
         *  newapp.loanCat3 =
         *  newapp.loanPurpose =
         *  newapp.loanType3 =
         *  newapp.loanType5 =
         *  newapp.origAccNo =
         *  newapp.origBMNo =
         *  newapp.origDueBillNo =
         *  newapp.rate =
         *  newapp.resultType =
         *  newapp.sContractNo =
         *  newapp.sectorCat1 =
         */
        return newapp;
    }


    /**
     * Description of the Method
     */
    public void printMe() {
        System.out.println("appamt:" + this.appAmt);
        System.out.println("appDate:" + this.appDate);
        System.out.println("appEndDate:" + this.appEndDate);
        System.out.println("appMonths:" + this.appMonths);
        System.out.println("appStartDate:" + this.appStartDate);
        System.out.println("bmTypeNo:" + this.bmTypeNo);
        System.out.println("clientNo:" + this.clientNo);
        System.out.println("contractNo:" + this.contractNo);
        System.out.println("curNo:" + this.curNo);
        System.out.println("finalAmt:" + this.finalAmt);
        System.out.println("finalAmt2:" + this.finalAmt2);
        System.out.println("finalCurNo:" + this.finalCurNo);
        System.out.println("finalEndDate:" + this.finalEndDate);
        System.out.println("finalMonths:" + this.finalMonths);
        System.out.println("finalRate:" + this.finalRate);
        System.out.println("finalStartDate:" + this.finalStartDate);
        System.out.println("firstResp:" + this.firstResp);
        System.out.println("firstRespPct:" + this.firstRespPct);
        System.out.println("ifRespLoan:" + this.ifRespLoan);
        System.out.println("interestType:" + this.interestType);
        System.out.println("loanCat3:" + this.loanCat3);
        System.out.println("loanPurpose:" + this.loanPurpose);
        System.out.println("loanType3:" + this.loanType3);
        System.out.println("loanType5:" + this.loanType5);
        System.out.println("origAccNo:" + this.origAccNo);
        System.out.println("origBMNo:" + this.origBMNo);
        System.out.println("origDueBillNo:" + this.origDueBillNo);
        System.out.println("rate:" + this.rate);
        System.out.println("resultType:" + this.resultType);
        System.out.println("sContractNo:" + this.sContractNo);

        System.out.println("clientType:" + this.clientType);
        System.out.println("eComType:" + this.eComType);
        System.out.println("eComDeptType:" + this.eComDeptType);
        System.out.println("etpScopType:" + this.etpScopType);
        System.out.println("sectorCat1:" + this.sectorCat1);
        System.out.println("clientMgr:" + this.clientMgr);

    }


    /**
     * Gets the appEndDate attribute of the UpToDateApp object
     *
     * @return The appEndDate value
     */
    public Calendar getAppEndDate() {
        return appEndDate;
    }


    /**
     * Sets the appEndDate attribute of the UpToDateApp object
     *
     * @param appEndDate The new appEndDate value
     */
    public void setAppEndDate(String appEndDate) {
        if (appEndDate == null) return;

        Calendar c = Calendar.getInstance();
        String[] d;
        d = appEndDate.split("-");
        if (d.length == 3) {
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
        } else {
            d = new String[3];
            d[0] = appEndDate.substring(0, 4);
            d[1] = appEndDate.substring(4, 6);
            d[2] = appEndDate.substring(6, 8);
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1,
                    Integer.parseInt(d[2]));
        }
        this.appEndDate = c;
    }


    /**
     * Gets the appStartDate attribute of the UpToDateApp object
     *
     * @return The appStartDate value
     */
    public Calendar getAppStartDate() {
        return appStartDate;
    }


    /**
     * Sets the appStartDate attribute of the UpToDateApp object
     *
     * @param appStartDate The new appStartDate value
     */
    public void setAppStartDate(String appStartDate) {
        if (appStartDate == null) return;
        Calendar c = Calendar.getInstance();
        String[] d;
        d = appStartDate.split("-");
        if (d.length == 3) {
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]));
        } else {
            d = new String[3];
            d[0] = appStartDate.substring(0, 4);
            d[1] = appStartDate.substring(4, 6);
            d[2] = appStartDate.substring(6, 8);
            c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]) - 1,
                    Integer.parseInt(d[2]));
        }
        this.appStartDate = c;

    }
}
