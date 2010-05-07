package zt.cmsi.biz;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.math.BigDecimal;

public class CreditLimitData {
    public String clientNo = null;
    public Integer bmTypeNo = null;
    public BigDecimal creditLimit = null;
    public BigDecimal loanBal = null;
    public BigDecimal limitCommit = null;
    public BigDecimal limitApproved = null;
    public Integer hasBadLoan = null;
    public Integer ifRespLoan = null;
    public String operator = null;
    public String firstResp = null;
    public String decidedBy = null;
}
