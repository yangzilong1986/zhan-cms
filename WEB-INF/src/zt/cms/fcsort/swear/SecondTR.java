package zt.cms.fcsort.swear;

import java.text.DecimalFormat;
import zt.platform.db.DBUtil;
/**
 * <p/>=============================================== 
 * <p/>Title:五级分类贷款不良资产第二页数据封装类 
 * <p/>===============================================
 * <p/>Description:
 * 
 * @version $Revision: 1.3 $ $Date: 2007/06/19 00:43:07 $
 * @author zhengxin <p/>修改：$Author: zhengx $
 */
public class SecondTR {
	private String sqlpartname;
	private String sqlpartvalue;
	private String td1="";
	private String td2="0";
	private String td3="0";
	private String td4="0.00";
	private String td5="0.00";
	private String td6="0.00";
	private String td7="0.00";
	private String td8="0.00";
	private String td9="0.00";
	/**
	 * @return sqlpartname
	 */
	public String getSqlpartname() {
		return sqlpartname;
	}
	/**
	 * 设置 sqlpartname
	 * @param sqlpartname 
	 */
	public void setSqlpartname(String sqlpartname) {
		this.sqlpartname = sqlpartname;
	}
	/**
	 * @return sqlpartvalue
	 */
	public String getSqlpartvalue() {
		return sqlpartvalue;
	}
	/**
	 * 设置 sqlpartvalue
	 * @param sqlpartvalue 
	 */
	public void setSqlpartvalue(String sqlpartvalue) {
		this.sqlpartvalue = sqlpartvalue;
	}
	/**
	 * @return td1
	 */
	public String getTd1() {
		return DBUtil.fromDB(td1);
	}
	/**
	 * 设置 td1
	 * @param td1 
	 */
	public void setTd1(String td1) {
		this.td1 = td1;
	}
	/**
	 * @return td2
	 */
	public String getTd2() {
		return td2;
	}
	/**
	 * 设置 td2
	 * @param td2 
	 */
	public void setTd2(String td2) {
		this.td2 = td2;
	}
	/**
	 * @return td3
	 */
	public String getTd3() {
		return td3;
	}
	/**
	 * 设置 td3
	 * @param td3 
	 */
	public void setTd3(String td3) {
		this.td3 = td3;
	}
	/**
	 * @return td4
	 */
	public String getTd4() {
		return td4;
	}
	/**
	 * 设置 td4
	 * @param td4 
	 */
	public void setTd4(String td4) {
		this.td4 = td4;
	}
	/**
	 * @return td5
	 */
	public String getTd5() {
		return td5;
	}
	/**
	 * 设置 td5
	 * @param td5 
	 */
	public void setTd5(String td5) {
		this.td5 = td5;
	}
	/**
	 * @return td6
	 */
	public String getTd6() {
		return td6;
	}
	/**
	 * 设置 td6
	 * @param td6 
	 */
	public void setTd6(String td6) {
		this.td6 = td6;
	}
	/**
	 * @return td7
	 */
	public String getTd7() {
		return td7;
	}
	/**
	 * 设置 td7
	 * @param td7 
	 */
	public void setTd7(String td7) {
		this.td7 = td7;
	}
	/**
	 * @return td8
	 */
	public String getTd8() {
		return td8;
	}
	/**
	 * 设置 td8
	 * @param td8 
	 */
	public void setTd8(String td8) {
		this.td8 = td8;
	}
	/**
	 * @return td9
	 */
	public String getTd9() {
		return td9;
	}
	/**
	 * 设置 td9
	 * @param td9 
	 */
	public void setTd9(String td9) {
		this.td9 = td9;
	}
	

}
