package zt.cms.fcsort.swear;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>=============================================== 
 * <p/>Title: 五级分类不良资产查询模块封装类
 * <p/>===============================================
 * <p/>Description:五级分类不良资产查询模块封装类。
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class FcsortQueryPath {
	private String queryid;//模块编号
	private String queryname;//模块名称
	private String emnuname;//枚举名称
	private List emnulist = new ArrayList();
	private int type;// 0 全部不良   1 新增不良
	/**
	 * @return emnulist
	 */
	public List getEmnulist() {
		return emnulist;
	}
	/**
	 * 设置 emnulist
	 * @param emnulist 
	 */
	public void setEmnulist(List emnulist) {
		this.emnulist = emnulist;
	}
	/**
	 * @return emnuname
	 */
	public String getEmnuname() {
		return emnuname;
	}
	/**
	 * 设置 emnuname
	 * @param emnuname 
	 */
	public void setEmnuname(String emnuname) {
		this.emnuname = emnuname;
	}
	/**
	 * @return queryid
	 */
	public String getQueryid() {
		return queryid;
	}
	/**
	 * 设置 queryid
	 * @param queryid 
	 */
	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}
	/**
	 * @return queryname
	 */
	public String getQueryname() {
		return queryname;
	}
	/**
	 * 设置 queryname
	 * @param queryname 
	 */
	public void setQueryname(String queryname) {
		this.queryname = queryname;
	}
	/**
	 * @return type
	 */
	public int getType() {
		return type;
	}
	/**
	 * 设置 type
	 * @param type 
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 向emnulist中添加元素
	 * @param FcsortQueryPathEmnu
	 */
	public void addEmnu(FcsortQueryPathEmnu emnu)
	{
		this.emnulist.add(emnu);
	}
	
	
}
