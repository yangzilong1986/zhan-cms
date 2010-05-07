package zt.cms.bm.loancert;

import java.math.*;

public class Loancert {
  private int typeNo;
  private String clientNo;
  private double limitApproved;
  private BigDecimal loanBal;
  private BigDecimal limitCommit;
  private BigDecimal creditLimit;
  private int disabled;
  private int hasBadLoan;
  private java.util.Calendar endDate;
  private java.util.Calendar lastModified;
  private java.util.Calendar startDate;
  private String brhid;
  private int ifRespLoan;
  private String firstResp;
  private String decidedby;
  private String bmno;
  private String operator;

  public String getOperator()
  {
    return this.operator;
  }

  public void setOperator(String opr)
  {
    this.operator = opr;
  }

  public Loancert() {
  }

  public int getTypeNo() {
    return typeNo;
  }

  public void setTypeNo(int typeNo) {
    this.typeNo = typeNo;
  }

  public String getClientNo() {
    return clientNo;
  }

  public void setClientNo(String clientNo) {
    this.clientNo = clientNo;
  }

  public double getLimitApproved() {
    return limitApproved;
  }

  public void setLimitApproved(double limitApproved) {
    this.limitApproved = limitApproved;
  }

  public BigDecimal getLoanBal() {
    return loanBal;
  }

  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
  }

  public int getDisabled() {
    return disabled;
  }

  public void setDisabled(int disabled) {
    this.disabled = disabled;
  }

  public java.util.Calendar getStartDate() {
    return startDate;
  }

  public void setStartDate(java.util.Calendar startDate) {
    this.startDate = startDate;
  }

  public java.util.Calendar getEndDate() {
    return endDate;
  }

  public void setEndDate(java.util.Calendar endDate) {
    this.endDate = endDate;
  }

  public BigDecimal getLimitCommit() {
    return limitCommit;
  }

  public void setLimitCommit(BigDecimal limitCommit) {
    this.limitCommit = limitCommit;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }

  public java.util.Calendar getLastModified() {
    return lastModified;
  }

  public void setLastModified(java.util.Calendar lastModified) {
    this.lastModified = lastModified;
  }

  public int getHasBadLoan() {
    return hasBadLoan;
  }

  public void setHasBadLoan(int hasBadLoan) {
    this.hasBadLoan = hasBadLoan;
  }

  public String toString() {
    return getClientNo() + getCreditLimit() + getCreditLimit() + getDisabled()
      + getEndDate() + getHasBadLoan() + getLastModified() + getLimitApproved()
      + getLimitCommit() + getLoanBal() + getStartDate() + getTypeNo();
  }

  public String getBrhid() {
    return brhid;
  }

  public void setBrhid(String brhid) {
    this.brhid = brhid;
  }

  public int getIfRespLoan() {
    return ifRespLoan;
  }

  public void setIfRespLoan(int ifRespLoan) {
    this.ifRespLoan = ifRespLoan;
  }

  public String getFirstResp() {
    return firstResp;
  }

  public void setFirstResp(String firstResp) {
    this.firstResp = firstResp;
  }

  public String getDecidedby() {
    return decidedby;
  }

  public void setDecidedby(String decidedby) {
    this.decidedby = decidedby;
  }
    public String getBmno()
    {
        return bmno;
    }
    public void setBmno(String bmno)
    {
        this.bmno = bmno;
    }

}
