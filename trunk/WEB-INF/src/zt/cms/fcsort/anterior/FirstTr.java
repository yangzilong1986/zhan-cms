package zt.cms.fcsort.anterior;
import zt.platform.db.DBUtil;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类贷款最大10户首页数据封装类
 * <p/>===============================================
 * <p/>Description:
 * @version $Revision: 1.3 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class FirstTr {
	private String clentno;//客户编号  
	private String clentname;//客户名称 
	private String brhname;//网点名称
	private String brhid;//网店编号
	
	private int  dbcount;//笔数
	
	private double countbal;//合计余额
	
	private double balA;//贷款	余额

	private double balB;//承兑垫款

	private double bal3;//信用
	private double bal4;//保证贷款
	private double bal5;//值抵押
	
	private double duebal1;//表内
	private double duebal2;//表外
	
	private double sumbad;//不良贷款合计
	private double fczcbal;//正常
	private double fcgzbal;//关注
	private double fcbalcj;//次级
	private double fcbalky;//可疑
	private double fcbalsh;//损失

	public String getClentname() {
		return DBUtil.fromDB(clentname);
	}

	/**
	 * @return bal3
	 */
	public double getBal3() {
		return bal3;
	}

	/**
	 * 设置 bal3
	 * @param bal3 
	 */
	public void setBal3(double bal3) {
		this.bal3 = bal3;
	}

	/**
	 * @return bal4
	 */
	public double getBal4() {
		return bal4;
	}

	/**
	 * 设置 bal4
	 * @param bal4 
	 */
	public void setBal4(double bal4) {
		this.bal4 = bal4;
	}

	/**
	 * @return bal5
	 */
	public double getBal5() {
		return bal5;
	}

	/**
	 * 设置 bal5
	 * @param bal5 
	 */
	public void setBal5(double bal5) {
		this.bal5 = bal5;
	}

	/**
	 * @return balA
	 */
	public double getBalA() {
		return balA;
	}

	/**
	 * 设置 balA
	 * @param balA 
	 */
	public void setBalA(double balA) {
		this.balA = balA;
	}

	/**
	 * @return balB
	 */
	public double getBalB() {
		return balB;
	}

	/**
	 * 设置 balB
	 * @param balB 
	 */
	public void setBalB(double balB) {
		this.balB = balB;
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

	/**
	 * @return brhname
	 */
	public String getBrhname() {
		return brhname;
	}

	/**
	 * 设置 brhname
	 * @param brhname 
	 */
	public void setBrhname(String brhname) {
		this.brhname = brhname;
	}

	/**
	 * @return clentno
	 */
	public String getClentno() {
		return clentno;
	}

	/**
	 * 设置 clentno
	 * @param clentno 
	 */
	public void setClentno(String clentno) {
		this.clentno = clentno;
	}

	/**
	 * @return countbal
	 */
	public double getCountbal() {
		return countbal;
	}

	/**
	 * 设置 countbal
	 * @param countbal 
	 */
	public void setCountbal(double countbal) {
		this.countbal = countbal;
	}

	/**
	 * @return dbcount
	 */
	public int getDbcount() {
		return dbcount;
	}

	/**
	 * 设置 dbcount
	 * @param dbcount 
	 */
	public void setDbcount(int dbcount) {
		this.dbcount = dbcount;
	}

	/**
	 * @return duebal1
	 */
	public double getDuebal1() {
		return duebal1;
	}

	/**
	 * 设置 duebal1
	 * @param duebal1 
	 */
	public void setDuebal1(double duebal1) {
		this.duebal1 = duebal1;
	}

	/**
	 * @return duebal2
	 */
	public double getDuebal2() {
		return duebal2;
	}

	/**
	 * 设置 duebal2
	 * @param duebal2 
	 */
	public void setDuebal2(double duebal2) {
		this.duebal2 = duebal2;
	}

	/**
	 * @return fcbalcj
	 */
	public double getFcbalcj() {
		return fcbalcj;
	}

	/**
	 * 设置 fcbalcj
	 * @param fcbalcj 
	 */
	public void setFcbalcj(double fcbalcj) {
		this.fcbalcj = fcbalcj;
	}

	/**
	 * @return fcbalky
	 */
	public double getFcbalky() {
		return fcbalky;
	}

	/**
	 * 设置 fcbalky
	 * @param fcbalky 
	 */
	public void setFcbalky(double fcbalky) {
		this.fcbalky = fcbalky;
	}

	/**
	 * @return fcbalsh
	 */
	public double getFcbalsh() {
		return fcbalsh;
	}

	/**
	 * 设置 fcbalsh
	 * @param fcbalsh 
	 */
	public void setFcbalsh(double fcbalsh) {
		this.fcbalsh = fcbalsh;
	}

	/**
	 * @return fcgzbal
	 */
	public double getFcgzbal() {
		return fcgzbal;
	}

	/**
	 * 设置 fcgzbal
	 * @param fcgzbal 
	 */
	public void setFcgzbal(double fcgzbal) {
		this.fcgzbal = fcgzbal;
	}

	/**
	 * @return fczcbal
	 */
	public double getFczcbal() {
		return fczcbal;
	}

	/**
	 * 设置 fczcbal
	 * @param fczcbal 
	 */
	public void setFczcbal(double fczcbal) {
		this.fczcbal = fczcbal;
	}

	/**
	 * @return sumbad
	 */
	public double getSumbad() {
		return sumbad;
	}

	/**
	 * 设置 sumbad
	 * @param sumbad 
	 */
	public void setSumbad(double sumbad) {
		this.sumbad = sumbad;
	}

	/**
	 * 设置 clentname
	 * @param clentname 
	 */
	public void setClentname(String clentname) {
		this.clentname = clentname;
	}
	
	
}
