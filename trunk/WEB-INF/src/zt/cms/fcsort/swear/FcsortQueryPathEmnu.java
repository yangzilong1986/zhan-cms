package zt.cms.fcsort.swear;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶���಻���ʲ���ѯģ��ö�����ͷ�װ��
 * <p/>===============================================
 * <p/>Description:�弶���಻���ʲ���ѯģ��ö�����ͷ�װ�ࡣ
 * @version $Revision: 1.3 $  $Date: 2007/05/28 11:44:29 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class FcsortQueryPathEmnu {
	private String enuid="";//ö�ٱ��
	private String enutp="";//ö��ֵ
	private String enudt="";//ö�ٺ���
	private String sqlpart="";//����SQl��һ���� �滻 10=10

	/**
	 * @return enudt
	 */
	public String getEnudt() {
		return enudt;
	}

	/**
	 * ���� enudt
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
	 * ���� enuid
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
	 * ���� enutp
	 * @param enutp 
	 */
	public void setEnutp(String enutp) {
		this.enutp = enutp;
	}

	/**
	 * ���� sqlpart
	 * @param sqlpart 
	 */
	public void setSqlpart(String sqlpart) {
		this.sqlpart = sqlpart;
	}
	/**
	 * �õ���Ӧö����Ĳ�ѯSQL
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
