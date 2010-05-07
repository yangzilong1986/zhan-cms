package zt.cms.fcsort.fcnbloan;


/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类新增不良资产查询页面一级页面类
 * <p/>===============================================
 * <p/>Description: 定义不良查分页和页面参数。
 * 
 * @version $Revision: 1.1 $ $Date: 2007/05/28 14:28:03 $
 * @author houcs <p/>修改：$Author: houcs $
 */

public class FirstnbLoan
{
	private String brhid,sname,newloansum,newpersonsum,loanbal,bal,h3,h4,h5,brhtype;
    private String loanbal1="";
    private String bal1="";
    private String hh3="";
    private String hh4="";
    private String hh5="";
    private String brhidLevel="";
	public String getBrhidLevel() {
		return brhidLevel;
	}

	public void setBrhidLevel(String brhidLevel) {
		this.brhidLevel = brhidLevel;
	}

	public String getBal1() {
		return bal1;
	}

	public void setBal1(String bal1) {
		this.bal1 = bal1;
	}

	public String getHh3() {
		return hh3;
	}

	public void setHh3(String hh3) {
		this.hh3 = hh3;
	}

	public String getHh4() {
		return hh4;
	}

	public void setHh4(String hh4) {
		this.hh4 = hh4;
	}

	public String getHh5() {
		return hh5;
	}

	public void setHh5(String hh5) {
		this.hh5 = hh5;
	}

	public String getLoanbal1() {
		return loanbal1;
	}

	public void setLoanbal1(String loanbal1) {
		this.loanbal1 = loanbal1;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getBrhid() {
		return brhid;
	}

	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}

	public String getH3() {
		return h3;
	}

	public void setH3(String h3) {
		this.h3 = h3;
	}

	public String getH4() {
		return h4;
	}

	public void setH4(String h4) {
		this.h4 = h4;
	}

	public String getH5() {
		return h5;
	}

	public void setH5(String h5) {
		this.h5 = h5;
	}

	public String getLoanbal() {
		return loanbal;
	}

	public void setLoanbal(String loanbal) {
		this.loanbal = loanbal;
	}

	
	public String getNewloansum() {
		return newloansum;
	}

	public void setNewloansum(String newloansum) {
		this.newloansum = newloansum;
	}

	public String getNewpersonsum() {
		return newpersonsum;
	}

	public void setNewpersonsum(String newpersonsum) {
		this.newpersonsum = newpersonsum;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getBrhtype() {
		return brhtype;
	}

	public void setBrhtype(String brhtype) {
		this.brhtype = brhtype;
	}
}
