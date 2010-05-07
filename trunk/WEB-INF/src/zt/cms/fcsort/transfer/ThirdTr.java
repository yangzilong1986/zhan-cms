package zt.cms.fcsort.transfer;
import java.text.DecimalFormat;

import zt.platform.db.DBUtil;
/**
 * <p/>=============================================== 
 * <p/>Title:五级分类贷款不良资产迁徙率第三页数据封装类 
 * <p/>===============================================
 * <p/>Description:
 * 
 * @version $Revision: 1.2 $ $Date: 2007/05/23 06:52:26 $
 * @author zhengxin <p/>修改：$Author: zhengx $
 */
public class ThirdTr {
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	private String td0="";
	private String td1="";
	private String td2="";
	private String td3="0.00";
	private String td4="0.00";
	private String td5="0.00";
	private String td6="0.00";
	private String td7="";
	private String td8="";
	private String td9="";
	private String td10="";
	private String tdF="";
	private String contractamt="0.00";
	public String getTd0() {
		return td0;
	}
	public void setTd0(String td0) {
		this.td0 = td0;
	}
	public String getTd1() {
		return DBUtil.fromDB(td1);
	}
	public void setTd1(String td1) {
		this.td1 = td1;
	}
	public String getTd2() {
		return DBUtil.fromDB(td2);
	}
	public void setTd2(String td2) {
		this.td2 = td2;
	}
	public String getTd3() {
		return td3;
	}
	public void setTd3(String td3) {
		this.td3 = td3;
	}
	public String getTd4() {
		return td4;
	}
	public void setTd4(String td4) {
		this.td4 = td4;
	}
	public String getTd5() {
		return td5;
	}
	public void setTd5(String td5) {
		this.td5 = td5;
	}
	public String getTd6() {
		return td6;
	}
	public void setTd6(String td6) {
		this.td6 = td6;
	}
	public String getTd7() {
		return td7;
	}
	public void setTd7(String td7) {
		this.td7 = td7;
	}
	public String getTd8() {
		return DBUtil.fromDB(td8);
	}
	public void setTd8(String td8) {
		this.td8 = td8;
	}
	public String getTd9() {
		return DBUtil.fromDB(td9);
	}
	public void setTd9(String td9) {
		this.td9 = td9;
	}
	public String getTd10() {
		return DBUtil.fromDB(td10);
	}
	public void setTd10(String td10) {
		this.td10 = td10;
	}
	public String getTdF() {
		return DBUtil.fromDB(tdF);
	}
	public void setTdF(String tdF) {
		this.tdF = tdF;
	}
	public String getContractamt() {
		return contractamt;
	}
	public void setContractamt(String contractamt) {
		this.contractamt = contractamt;
	}
	

}
