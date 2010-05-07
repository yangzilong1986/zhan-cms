package zt.cmsi.biz;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.math.BigDecimal;

public class CMCreditLimitData {
    public BigDecimal creditLimit = null;
    public BigDecimal loanBal = null;
    public BigDecimal limitCommit = null;
    public boolean disabled;
    public boolean limitApproved;
}