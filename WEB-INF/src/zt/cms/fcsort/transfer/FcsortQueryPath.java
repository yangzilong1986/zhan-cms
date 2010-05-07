package zt.cms.fcsort.transfer;

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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public void addEmnu(FcsortQueryPathEmnu emnu)
	{
		this.emnulist.add(emnu);
	}
	public List getEmnulist() {
		return emnulist;
	}
	public String getEmnuname() {
		return emnuname;
	}
	public void setEmnuname(String emnuname) {
		this.emnuname = emnuname;
	}
}
