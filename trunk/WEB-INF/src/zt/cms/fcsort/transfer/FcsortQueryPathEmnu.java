package zt.cms.fcsort.transfer;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶���಻���ʲ���ѯģ��ö�����ͷ�װ��
 * <p/>===============================================
 * <p/>Description:�弶���಻���ʲ���ѯģ��ö�����ͷ�װ�ࡣ
 * @version $Revision: 1.2 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class FcsortQueryPathEmnu {
	private String enuid="";//ö�ٱ��
	private String enutp="";//ö��ֵ
	private String enudt="";//ö�ٺ���
	private String sqlpart="";//����SQl��һ���� �滻 10=10
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
