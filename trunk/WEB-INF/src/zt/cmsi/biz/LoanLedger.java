//Source file: e:\\java\\zt\\cmsi\\biz\\LoanLedger.java

package zt.cmsi.biz;

import java.math.BigDecimal;
import java.util.Calendar;

public class LoanLedger {
    public String BMNo = null;
    public String actNo = null;
    public String cnlNo = null;
    public String curNo = null;
    public Integer BMTypeNo = null; //
    public String brhID = null;
    public String clientNo = null; //
    public String clientName = null; //
    public String contractNo = null; //
    public String sContractNo = null; //
    public BigDecimal crtRate = null;
    public BigDecimal contractAMt = null;
    public BigDecimal nowBal = null;
    public Integer perimon = null;
    public Calendar payDate = null;
    public Calendar endDate = null;
    public Calendar nowEndDate = null;
    public Calendar closeDate = null;
    public Integer isExtented = null;
    public Integer isClosed = null;
    public String firstResp = null; //
    public BigDecimal firstRespPct = null; //
    public Integer loanCat2 = null;
    public Integer loanType3 = null; //
    public Integer loanType5 = null; //

    public String toString() {
        return BMNo + "_" + actNo + "_" + cnlNo + "_" + brhID + "_" + clientNo;
    }
}
