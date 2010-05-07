package zt.cms.fcsort.transfer;


/**
 * <p/>=============================================== 
 * <p/>Title: 不良资产迁移数据封装类
 * <p/>===============================================
 * <p/>Description:
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class FirstTr {
	private int enutp;// 1-12 
	private String Enudt;
	private int totalA;//笔数
	private int totalB;//户数
	private double balA;
	private double balB;
	public double getBalA() {
		return balA;
	}
	public void setBalA(double balA) {
		this.balA = balA;
	}
	public double getBalB() {
		return balB;
	}
	public void setBalB(double balB) {
		this.balB = balB;
	}
	public String getEnudt() {
		return Enudt;
	}
	public void setEnudt(String enudt) {
		Enudt = enudt;
	}
	public int getEnutp() {
		return enutp;
	}
	public void setEnutp(int enutp) {
		this.enutp = enutp;
	}
	public int getTotalA() {
		return totalA;
	}
	public void setTotalA(int totalA) {
		this.totalA = totalA;
	}
	public int getTotalB() {
		return totalB;
	}
	public void setTotalB(int totalB) {
		this.totalB = totalB;
	}


}
