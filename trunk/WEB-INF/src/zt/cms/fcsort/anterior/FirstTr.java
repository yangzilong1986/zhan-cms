package zt.cms.fcsort.anterior;
import zt.platform.db.DBUtil;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶����������10����ҳ���ݷ�װ��
 * <p/>===============================================
 * <p/>Description:
 * @version $Revision: 1.3 $  $Date: 2007/05/23 06:52:26 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class FirstTr {
	private String clentno;//�ͻ����  
	private String clentname;//�ͻ����� 
	private String brhname;//��������
	private String brhid;//������
	
	private int  dbcount;//����
	
	private double countbal;//�ϼ����
	
	private double balA;//����	���

	private double balB;//�жҵ��

	private double bal3;//����
	private double bal4;//��֤����
	private double bal5;//ֵ��Ѻ
	
	private double duebal1;//����
	private double duebal2;//����
	
	private double sumbad;//��������ϼ�
	private double fczcbal;//����
	private double fcgzbal;//��ע
	private double fcbalcj;//�μ�
	private double fcbalky;//����
	private double fcbalsh;//��ʧ

	public String getClentname() {
		return DBUtil.fromDB(clentname);
	}

	/**
	 * @return bal3
	 */
	public double getBal3() {
		return bal3;
	}

	/**
	 * ���� bal3
	 * @param bal3 
	 */
	public void setBal3(double bal3) {
		this.bal3 = bal3;
	}

	/**
	 * @return bal4
	 */
	public double getBal4() {
		return bal4;
	}

	/**
	 * ���� bal4
	 * @param bal4 
	 */
	public void setBal4(double bal4) {
		this.bal4 = bal4;
	}

	/**
	 * @return bal5
	 */
	public double getBal5() {
		return bal5;
	}

	/**
	 * ���� bal5
	 * @param bal5 
	 */
	public void setBal5(double bal5) {
		this.bal5 = bal5;
	}

	/**
	 * @return balA
	 */
	public double getBalA() {
		return balA;
	}

	/**
	 * ���� balA
	 * @param balA 
	 */
	public void setBalA(double balA) {
		this.balA = balA;
	}

	/**
	 * @return balB
	 */
	public double getBalB() {
		return balB;
	}

	/**
	 * ���� balB
	 * @param balB 
	 */
	public void setBalB(double balB) {
		this.balB = balB;
	}

	/**
	 * @return brhid
	 */
	public String getBrhid() {
		return brhid;
	}

	/**
	 * ���� brhid
	 * @param brhid 
	 */
	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}

	/**
	 * @return brhname
	 */
	public String getBrhname() {
		return brhname;
	}

	/**
	 * ���� brhname
	 * @param brhname 
	 */
	public void setBrhname(String brhname) {
		this.brhname = brhname;
	}

	/**
	 * @return clentno
	 */
	public String getClentno() {
		return clentno;
	}

	/**
	 * ���� clentno
	 * @param clentno 
	 */
	public void setClentno(String clentno) {
		this.clentno = clentno;
	}

	/**
	 * @return countbal
	 */
	public double getCountbal() {
		return countbal;
	}

	/**
	 * ���� countbal
	 * @param countbal 
	 */
	public void setCountbal(double countbal) {
		this.countbal = countbal;
	}

	/**
	 * @return dbcount
	 */
	public int getDbcount() {
		return dbcount;
	}

	/**
	 * ���� dbcount
	 * @param dbcount 
	 */
	public void setDbcount(int dbcount) {
		this.dbcount = dbcount;
	}

	/**
	 * @return duebal1
	 */
	public double getDuebal1() {
		return duebal1;
	}

	/**
	 * ���� duebal1
	 * @param duebal1 
	 */
	public void setDuebal1(double duebal1) {
		this.duebal1 = duebal1;
	}

	/**
	 * @return duebal2
	 */
	public double getDuebal2() {
		return duebal2;
	}

	/**
	 * ���� duebal2
	 * @param duebal2 
	 */
	public void setDuebal2(double duebal2) {
		this.duebal2 = duebal2;
	}

	/**
	 * @return fcbalcj
	 */
	public double getFcbalcj() {
		return fcbalcj;
	}

	/**
	 * ���� fcbalcj
	 * @param fcbalcj 
	 */
	public void setFcbalcj(double fcbalcj) {
		this.fcbalcj = fcbalcj;
	}

	/**
	 * @return fcbalky
	 */
	public double getFcbalky() {
		return fcbalky;
	}

	/**
	 * ���� fcbalky
	 * @param fcbalky 
	 */
	public void setFcbalky(double fcbalky) {
		this.fcbalky = fcbalky;
	}

	/**
	 * @return fcbalsh
	 */
	public double getFcbalsh() {
		return fcbalsh;
	}

	/**
	 * ���� fcbalsh
	 * @param fcbalsh 
	 */
	public void setFcbalsh(double fcbalsh) {
		this.fcbalsh = fcbalsh;
	}

	/**
	 * @return fcgzbal
	 */
	public double getFcgzbal() {
		return fcgzbal;
	}

	/**
	 * ���� fcgzbal
	 * @param fcgzbal 
	 */
	public void setFcgzbal(double fcgzbal) {
		this.fcgzbal = fcgzbal;
	}

	/**
	 * @return fczcbal
	 */
	public double getFczcbal() {
		return fczcbal;
	}

	/**
	 * ���� fczcbal
	 * @param fczcbal 
	 */
	public void setFczcbal(double fczcbal) {
		this.fczcbal = fczcbal;
	}

	/**
	 * @return sumbad
	 */
	public double getSumbad() {
		return sumbad;
	}

	/**
	 * ���� sumbad
	 * @param sumbad 
	 */
	public void setSumbad(double sumbad) {
		this.sumbad = sumbad;
	}

	/**
	 * ���� clentname
	 * @param clentname 
	 */
	public void setClentname(String clentname) {
		this.clentname = clentname;
	}
	
	
}
