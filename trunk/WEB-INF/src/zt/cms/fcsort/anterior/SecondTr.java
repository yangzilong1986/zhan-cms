package zt.cms.fcsort.anterior;

import zt.platform.db.DBUtil;

/**
 * <p/>=============================================== 
 * <p/>Title:五级分类贷款最大10户第二页数据封装类 
 * <p/>===============================================
 * <p/>Description:
 * 
 * @version $Revision: 1.3 $ $Date: 2007/06/19 00:42:55 $
 * @author zhengxin <p/>修改：$Author: zhengx $
 */
public class SecondTr {

	/**
	 * 业务号
	 */
	private String bmno; 

	private String cnlno;// 借据号
	
	private String brhid;//网点号

	private String loancat3;// 贷款用途

	private double nowbal;// 接歉金额

	private int perimon;

	private double crteate;

	private String paydate;

	private String enddate;

	private String loancat2;

	private String loancat1;

	private String firstresp;

	public String getFirstresp() {
		return DBUtil.fromDB(firstresp);
	}

	public String getLoancat1() {
		return DBUtil.fromDB(loancat1);
	}

	public String getLoancat2() {
		return DBUtil.fromDB(loancat2);
	}

	public String getLoancat3() {
		return DBUtil.fromDB(loancat3);
	}

	/**
	 * @return bmno
	 */
	public String getBmno() {
		return bmno;
	}

	/**
	 * 设置 bmno
	 * 
	 * @param bmno
	 */
	public void setBmno(String bmno) {
		this.bmno = bmno;
	}

	/**
	 * @return cnlno
	 */
	public String getCnlno() {
		return cnlno;
	}

	/**
	 * 设置 cnlno
	 * 
	 * @param cnlno
	 */
	public void setCnlno(String cnlno) {
		this.cnlno = cnlno;
	}

	/**
	 * @return crteate
	 */
	public double getCrteate() {
		return crteate;
	}

	/**
	 * 设置 crteate
	 * 
	 * @param crteate
	 */
	public void setCrteate(double crteate) {
		this.crteate = crteate;
	}

	/**
	 * @return enddate
	 */
	public String getEnddate() {
		return enddate;
	}

	/**
	 * 设置 enddate
	 * 
	 * @param enddate
	 */
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	/**
	 * @return nowbal
	 */
	public double getNowbal() {
		return nowbal;
	}

	/**
	 * 设置 nowbal
	 * 
	 * @param nowbal
	 */
	public void setNowbal(double nowbal) {
		this.nowbal = nowbal;
	}

	/**
	 * @return paydate
	 */
	public String getPaydate() {
		return paydate;
	}

	/**
	 * 设置 paydate
	 * 
	 * @param paydate
	 */
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	/**
	 * @return perimon
	 */
	public int getPerimon() {
		return perimon;
	}

	/**
	 * 设置 perimon
	 * 
	 * @param perimon
	 */
	public void setPerimon(int perimon) {
		this.perimon = perimon;
	}

	/**
	 * 设置 firstresp
	 * 
	 * @param firstresp
	 */
	public void setFirstresp(String firstresp) {
		this.firstresp = firstresp;
	}

	/**
	 * 设置 loancat1
	 * 
	 * @param loancat1
	 */
	public void setLoancat1(String loancat1) {
		this.loancat1 = loancat1;
	}

	/**
	 * 设置 loancat2
	 * 
	 * @param loancat2
	 */
	public void setLoancat2(String loancat2) {
		this.loancat2 = loancat2;
	}

	/**
	 * 设置 loancat3
	 * 
	 * @param loancat3
	 */
	public void setLoancat3(String loancat3) {
		this.loancat3 = loancat3;
	}

	/**
	 * @return brhid
	 */
	public String getBrhid() {
		return brhid;
	}

	/**
	 * 设置 brhid
	 * @param brhid 
	 */
	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}

}
