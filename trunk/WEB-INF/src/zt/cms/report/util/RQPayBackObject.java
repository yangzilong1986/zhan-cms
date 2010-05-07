package zt.cms.report.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RQPayBackObject {
  private String strActNo;
  private String strCnlNo;
  private String strTXNDate;
  private String strPayCrBal;
  private String strPayDbBal;
  private String strDtlBal;
  private String strLoanCat2;
  public RQPayBackObject(   String strActNo1,
   String strCnlNo1,
   String strTXNDate1,
   String strPayCrBal1,
   String strPayDbBal1,
   String strDtlBal1,
   String strLoanCat21){
        this.strActNo = strActNo1;
        this.strCnlNo = strCnlNo1;
        this.strTXNDate = strTXNDate1;
        this.strPayCrBal = strPayCrBal1;
        this.strPayDbBal = strPayDbBal1;
        this.strDtlBal = strDtlBal1;
        this.strLoanCat2 = strLoanCat21;
  }
  public String getStrActNo() {
    return strActNo;
  }
  public String getStrCnlNo() {
    return strCnlNo;
  }
  public String getStrTXNDate() {
    return strTXNDate;
  }
  public String getStrPayCrBal() {
    return strPayCrBal;
  }
  public String getStrPayDbBal() {
    return strPayDbBal;
  }
  public String getStrDtlBal() {
    return strDtlBal;
  }
  public String getStrLoanCat2() {
    return strLoanCat2;
  }
}