package zt.cms.fcsort.fcnbloan;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类新增不良资产查询类
 * <p/>===============================================
 * <p/>Description: 定义不良查分页和页面参数。
 * 
 * @version $Revision: 1.2 $ $Date: 2007/05/14 07:30:11 $
 * @author houcs <p/>修改：$Author: houcs $
 */


public class FcnbloanT {
	
	String clientname="";//客户名称
	String loanyt="";//贷款用途
	String loanbal="";//合同金额
	String loanbal1="";
    String bal="";//结欠金额
    String bal1="";
    String enudate="";//期限
    String perimon="";//利率
    String padate="";//发放日
    String duedate="";//到期日
    String fourS="";//四级分类形态
    String fiveS="";//五级分类形态
    String firstPerson="";//第一责任人
    String PayField="";//发放时段
    String bmno="";//业务号
    String badReason="";//形成愿意
	public String getBadReason() {
		return badReason;
	}
	public void setBadReason(String badReason) {
		this.badReason = badReason;
	}
	public String getBmno() {
		return bmno;
	}
	public void setBmno(String bmno) {
		this.bmno = bmno;
	}
	public String getBal() {
		return bal;
	}
	public void setBal(String bal) {
		this.bal = bal;
	}
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	public String getEnudate() {
		return enudate;
	}
	public void setEnudate(String enudate) {
		this.enudate = enudate;
	}
	public String getFirstPerson() {
		return firstPerson;
	}
	public void setFirstPerson(String firstPerson) {
		this.firstPerson = firstPerson;
	}
	public String getFiveS() {
		return fiveS;
	}
	public void setFiveS(String fiveS) {
		this.fiveS = fiveS;
	}
	public String getFourS() {
		return fourS;
	}
	public void setFourS(String fourS) {
		this.fourS = fourS;
	}
	public String getLoanbal() {
		return loanbal;
	}
	public void setLoanbal(String loanbal) {
		this.loanbal = loanbal;
	}
	public String getLoanyt() {
		return loanyt;
	}
	public void setLoanyt(String loanyt) {
		this.loanyt = loanyt;
	}
	public String getPadate() {
		return padate;
	}
	public void setPadate(String padate) {
		this.padate = padate;
	}
	public String getPayField() {
		return PayField;
	}
	public void setPayField(String payField) {
		PayField = payField;
	}
	public String getPerimon() {
		return perimon;
	}
	public void setPerimon(String perimon) {
		this.perimon = perimon;
	}
	public String getBal1() {
		return bal1;
	}
	public void setBal1(String bal1) {
		this.bal1 = bal1;
	}
	public String getLoanbal1() {
		return loanbal1;
	}
	public void setLoanbal1(String loanbal1) {
		this.loanbal1 = loanbal1;
	}
	
}
