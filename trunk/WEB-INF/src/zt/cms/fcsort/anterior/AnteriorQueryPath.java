package zt.cms.fcsort.anterior;
/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类贷款最大10户查询定义模块
 * <p/>===============================================
 * <p/>Description:为五级分类最大10户提供查询必须的参数。
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */

public class AnteriorQueryPath {
	private String queryid;//模块编号
	private String queryname;//模块名称
	private String sqlpart;//统计字段
	public String getQueryid() {
		return queryid;
	}
	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}
	public String getQueryname() {
		return queryname;
	}
	public void setQueryname(String queryname) {
		this.queryname = queryname;
	}
	public String getSqlpart() {
		return sqlpart;
	}
	public void setSqlpart(String sqlpart) {
		this.sqlpart = sqlpart;
	}
	
}
