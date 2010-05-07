package zt.cms.fcsort.swear;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类不良资产查询模块枚举类型封装类
 * <p/>===============================================
 * <p/>Description:五级分类不良资产查询模块枚举类型封装类。
 * @version $Revision: 1.3 $  $Date: 2007/05/28 11:44:29 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class FcsortQueryPathEmnu {
	private String enuid="";//枚举编号
	private String enutp="";//枚举值
	private String enudt="";//枚举含义
	private String sqlpart="";//二级SQl的一部分 替换 10=10

	/**
	 * @return enudt
	 */
	public String getEnudt() {
		return enudt;
	}

	/**
	 * 设置 enudt
	 * @param enudt 
	 */
	public void setEnudt(String enudt) {
		this.enudt = enudt;
	}

	/**
	 * @return enuid
	 */
	public String getEnuid() {
		return enuid;
	}

	/**
	 * 设置 enuid
	 * @param enuid 
	 */
	public void setEnuid(String enuid) {
		this.enuid = enuid;
	}

	/**
	 * @return enutp
	 */
	public String getEnutp() {
		return enutp;
	}

	/**
	 * 设置 enutp
	 * @param enutp 
	 */
	public void setEnutp(String enutp) {
		this.enutp = enutp;
	}

	/**
	 * 设置 sqlpart
	 * @param sqlpart 
	 */
	public void setSqlpart(String sqlpart) {
		this.sqlpart = sqlpart;
	}
	/**
	 * 得到对应枚举类的查询SQL
	 * @return String
	 */
	public String getSqlpart() {
		if(this.enuid.trim().equals("LoanWay"))
		{
			return  " FCBMAIN."+this.enuid.trim()+" in('"+this.enutp+"') ";
		}
		if(this.enuid.trim().equals("BYTERM"))
		{
			return sqlpart ;
		}
		return " FCBMAIN."+this.enuid.trim()+" in("+this.enutp+") ";
	}
	

}
