package zt.cms.fcsort.transfer;

import java.util.ArrayList;
import java.util.List;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶���಻���ʲ���ѯģ���װ��
 * <p/>===============================================
 * <p/>Description:�弶���಻���ʲ���ѯģ���װ�ࡣ
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class FcsortQueryPath {
	private String queryid;//ģ����
	private String queryname;//ģ������
	private String emnuname;//ö������
	private List emnulist = new ArrayList();
	private int type;// 0 ȫ������   1 ��������
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
