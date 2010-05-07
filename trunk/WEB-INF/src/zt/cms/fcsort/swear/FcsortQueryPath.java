package zt.cms.fcsort.swear;

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
	/**
	 * @return emnulist
	 */
	public List getEmnulist() {
		return emnulist;
	}
	/**
	 * ���� emnulist
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
	 * ���� emnuname
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
	 * ���� queryid
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
	 * ���� queryname
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
	 * ���� type
	 * @param type 
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * ��emnulist�����Ԫ��
	 * @param FcsortQueryPathEmnu
	 */
	public void addEmnu(FcsortQueryPathEmnu emnu)
	{
		this.emnulist.add(emnu);
	}
	
	
}
