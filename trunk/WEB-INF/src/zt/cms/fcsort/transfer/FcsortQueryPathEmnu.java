package zt.cms.fcsort.transfer;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类不良资产查询模块枚举类型封装类
 * <p/>===============================================
 * <p/>Description:五级分类不良资产查询模块枚举类型封装类。
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class FcsortQueryPathEmnu {
	private String enuid="";//枚举编号
	private String enutp="";//枚举值
	private String enudt="";//枚举含义
	private String sqlpart="";//二级SQl的一部分 替换 10=10
	public String getEnuid() {
		return enuid;
	}
	public void setEnuid(String enuid) {
		this.enuid = enuid;
	}

	public String getEnutp() {
		return enutp;
	}
	public void setEnutp(String enutp) {
		this.enutp = enutp;
	}
	public String getSqlpart() {
	
			return sqlpart ;
		
	}
	public String getEnudt() {
		return enudt;
	}
	public void setEnudt(String enudt) {
		this.enudt = enudt;
	}
	public void setSqlpart(String sqlpart) {
		this.sqlpart = sqlpart;
	}

}
