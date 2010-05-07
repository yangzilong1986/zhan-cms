package zt.cms.fcsort.swear;

/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类贷款不良资产首页数据封装类
 * <p/>===============================================
 * <p/>Description:
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class First {
	private int brhid;//网点号
	private String sname;//网点名称
	private String brhtype;//网点虚实  9 为虚网点
	private int totalA;//新总笔数
	private int totalB;//新总户数
	private double balA;//合同金额
	private double balB;//结欠金额
	private double h3;//借欠金额-次级
	private double h4;//借欠金额-可疑
	private double h5;//借欠金额-损失
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
	public int getBrhid() {
		return brhid;
	}
	/**
	 * 设置 brhid
	 * @param brhid 
	 */
	public void setBrhid(int brhid) {
		this.brhid = brhid;
	}
	/**
	 * @return brhtype
	 */
	public String getBrhtype() {
		return brhtype;
	}
	/**
	 * 设置 brhtype
	 * @param brhtype 
	 */
	public void setBrhtype(String brhtype) {
		this.brhtype = brhtype;
	}
	/**
	 * @return h3
	 */
	public double getH3() {
		return h3;
	}
	/**
	 * 设置 h3
	 * @param h3 
	 */
	public void setH3(double h3) {
		this.h3 = h3;
	}
	/**
	 * @return h4
	 */
	public double getH4() {
		return h4;
	}
	/**
	 * 设置 h4
	 * @param h4 
	 */
	public void setH4(double h4) {
		this.h4 = h4;
	}
	/**
	 * @return h5
	 */
	public double getH5() {
		return h5;
	}
	/**
	 * 设置 h5
	 * @param h5 
	 */
	public void setH5(double h5) {
		this.h5 = h5;
	}
	/**
	 * @return sname
	 */
	public String getSname() {
		return sname;
	}
	/**
	 * 设置 sname
	 * @param sname 
	 */
	public void setSname(String sname) {
		this.sname = sname;
	}
	/**
	 * @return totalA
	 */
	public int getTotalA() {
		return totalA;
	}
	/**
	 * 设置 totalA
	 * @param totalA 
	 */
	public void setTotalA(int totalA) {
		this.totalA = totalA;
	}
	/**
	 * @return totalB
	 */
	public int getTotalB() {
		return totalB;
	}
	/**
	 * 设置 totalB
	 * @param totalB 
	 */
	public void setTotalB(int totalB) {
		this.totalB = totalB;
	}
	
	
}
