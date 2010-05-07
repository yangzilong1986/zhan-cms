package zt.cms.fcsort.anterior.rate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import java.util.HashMap;
import zt.platform.db.DBUtil;
import zt.cms.fcsort.anterior.AnteriorQueryPath;
import zt.cms.fcsort.common.FcsortUtil;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶�������Ǩ����ҳ��BEAN
 * <p/>===============================================
 * <p/>Description:�ṩ�弶�������Ǩ����ҳ��������ʾ���ݣ��ͳ�ʼ������
 * @version $Revision: 1.5 $  $Date: 2007/05/29 12:18:00 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class RatePageObject {
	private int sumA;

	private double sumB[] = new double[13];// �����ͺϼ���

	private double sunE = 0.00;

	// ���ݿ�����
	ConnectionManager manager;// ���ݲ�����

	private HashMap AnteriorQueryPathMap = new HashMap();

	private AnteriorQueryPath AnteriorQuery;

	private String queryid;// ��Ŀ���

	// ��ѯ����
	private String brhid;// ������

	private String scbrhname;// ��������

	private String creadate;// ��ѯʱ��

	private String righttitle;// �ұ���

	private String lefttitle;// �����
	
	private String mory;//���ڻ���

	// ��ʽ����ʾ
	DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

	DecimalFormat dfB = new DecimalFormat("###,###,###,##0.0000");

	FcsortUtil util;

	/**
	 * ���ò�ѯ�����͹���ҳ������
	 * @param request
	 * @param response
	 */
	public RatePageObject() {
		util = new FcsortUtil();
		manager = ConnectionManager.getInstance();
	}

	/**
	 * ���ò�ѯ����
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public boolean setRequest(HttpServletRequest request) throws Exception {
		this.init(request);
		if (request.getParameter("QUERYID") == null
				|| request.getParameter("QUERYID").equals("")) {
			return false;
		} else {
			this.queryid = request.getParameter("QUERYID").trim();
		}
		// Session ��ȡ�� hashmap - FcsortQueryPath
		AnteriorQueryPathMap = (HashMap) request.getSession().getAttribute(
				"FCSORT_ANTERI_PATH_MAP");
		AnteriorQuery = (AnteriorQueryPath) AnteriorQueryPathMap.get(queryid);
		this.brhid = request.getParameter("brhid");// �õ�������
		if (this.brhid == null)// ���Ϊ�վ�ȡ�û���������
		{
			UserManager um = (UserManager) request.getSession().getAttribute(
					SystemAttributeNames.USER_INFO_NAME);
			String strUserName = um.getUserName();
			this.brhid = SCUser.getBrhId(strUserName).trim();
			System.out.println(strUserName);
		}
		this.scbrhname = SCBranch.getSName(brhid);
		this.creadate = request.getParameter("creadate") == null ? util.getDT(): request.getParameter("creadate").trim();// ��Ч����
		this.mory=request.getParameter("MORY") == null ? "2":request.getParameter("MORY");
		return true;
	}

	/**
	 * �õ�һ��ҳ��Ĳ�ѯ���
	 * 
	 * @param i
	 * @return String
	 */
	private String getSqlPart1(int i) {
		switch (i) {
		case 1:
			return "select brhid,sname,brhtype from scbranch where BRHLEVEL >1 and upbrh='"
					+ this.getBrhid() + "'";

		case 2:
			return "select brhid,sname,brhtype from scbranch where BRHLEVEL >1 and brhid='"
					+ this.getBrhid() + "'";

		}
		return "";

	}
	/**
	 * �������㼯��
	 * @param strs
	 * @return String
	 */
	public String getString(String strs) {
		String str = "";
		String h[] = strs.split(",");
		for (int i = 0; i < h.length; i++) {
			if (i < h.length - 1) {
				str += h[i] + "','";
			} else {
				str += h[i];
			}

		}
		return str;

	}
	/**
	 * �õ�һ��ҳ��Ľ���� KS
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List getListKS() throws Exception {
		List list = new ArrayList();
		CachedRowSet crs = manager.getRs(this.getSqlPart1(1));
		if (crs.size() <= 0) {
			crs = manager.getRs(this.getSqlPart1(2));
		}
		
		//���ڻ���
		String ftype ="22";
		if(this.mory.trim().equals("1"))
		{
			ftype="26";
		}
		
		while (crs.next()) {
			FirstTr tr = new FirstTr();
			tr.setBrhid(crs.getString("brhid").trim());
			tr.setBrhname(DBUtil.fromDB(crs.getString("sname").trim()));
			tr.setBrhtype(crs.getString("brhtype"));
			String brhids = this.getString(SCBranch.getAllSubBrh1(tr.getBrhid()));
			if (brhids == null || brhids.equals("")) {
				brhids = "('" +tr.getBrhid() + "')";
			} else {
				brhids = "('" + brhids + "')";
			}
			StringBuffer sql = new StringBuffer("select sum(amt1) from fcdata where ftype="+ftype+" and brhid in " +brhids + " and dt='" + this.creadate+ "' and dim1="+this.AnteriorQuery.getSqlpart().trim()+" and dim2 = ");
			for (int i = 1; i <= 3; i++) {
				CachedRowSet child = manager.getRs(sql.toString()+i);
				while (child.next()) {
					if (i == 1) {
						tr.setBala(child.getDouble(1) / 10000);

					}
					if (i == 2) {
						tr.setBalb(child.getDouble(1) / 10000);

					}
					if (i == 3) {
						tr.setBalc(child.getDouble(1) / 10000);

					}
				}
			}
			double C=(tr.getBalb()-tr.getBalc())==0?1:(tr.getBalb()-tr.getBalc());
			tr.setRate((tr.getBala()/C)*100);
			list.add(tr);

		}

		return list;
	}
	/**
	 * �õ�һ��ҳ��Ľ����zs
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List getListZS() throws Exception {
		List list = new ArrayList();
		CachedRowSet crs = manager.getRs(this.getSqlPart1(1));
		if (crs.size() <= 0) {
			crs = manager.getRs(this.getSqlPart1(2));
		}
		String ftype ="22";
		if(this.mory.trim().equals("1"))
		{
			ftype="26";
		}
		while (crs.next()) {
			FirstTr tr = new FirstTr();
			tr.setBrhid(crs.getString("brhid").trim());
			tr.setBrhname(DBUtil.fromDB(crs.getString("sname").trim()));
			tr.setBrhtype(crs.getString("brhtype"));
			String brhids = this.getString(SCBranch.getAllSubBrh1(tr.getBrhid()));
			if (brhids == null || brhids.equals("")) {
				brhids = "('" +tr.getBrhid() + "')";
			} else {
				brhids = "('" + brhids + "')";
			}
			
			
			
			StringBuffer sql = new StringBuffer("select sum(case when dim1=1 then amt1 else 0 end),sum(case when dim1=2 then amt1 else 0 end) from fcdata where ftype="+ftype+" and brhid in " +brhids + " and dt='" + this.creadate+ "' and dim1<=2 and dim2 = ");
			for (int i = 1; i <= 3; i++) {
				CachedRowSet child = manager.getRs(sql.toString()+i);
				while (child.next()) {
					if (i == 1) {
						tr.setBalza(child.getDouble(1) / 10000);
						tr.setBalga(child.getDouble(2) / 10000);
					}
					if (i == 2) {
						tr.setBalzb(child.getDouble(1) / 10000);
						tr.setBalgb(child.getDouble(2) / 10000);

					}
					if (i == 3) {
						tr.setBalzc(child.getDouble(1) / 10000);
						tr.setBalgc(child.getDouble(2) / 10000);


					}
				}
			}
			double C=(tr.getBalgb()+tr.getBalzb()-tr.getBalzc()-tr.getBalgc())==0?1:(tr.getBalgb()+tr.getBalzb()-tr.getBalzc()-tr.getBalgc());
			
			
			tr.setRate(((tr.getBalga()+tr.getBalza())/C)*100);
			list.add(tr);

		}
		return list;
	}

	/**
	 * ���ò�ѯҳ������Ҫ��ģ����Ϣ
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public boolean init(HttpServletRequest request) throws Exception {
		HashMap map = new HashMap();
		map.put("RATEZS", this.setRATEZS());
		map.put("RATECJ", this.setRATECJ());
		map.put("RATEKY", this.setRATEKY());

		request.getSession().setAttribute("FCSORT_ANTERI_PATH_MAP", map);

		return true;
	}

	/**
	 * �����弶������������Ǩ����ģ���������
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATEZS() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATEZS");
		query.setQueryname("�弶������������Ǩ����");
		query.setSqlpart("RATEZS");
		return query;
	}
	/**
	 * �����弶����μ������Ǩ����ģ���������
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATECJ() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATECJ");
		query.setQueryname("�弶����μ������Ǩ����");
		query.setSqlpart("3");
		return query;
	}

	/**
	 * �����弶������������Ǩ����ģ���������
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATEKY() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATEKY");
		query.setQueryname("�弶������������Ǩ����");
		query.setSqlpart("4");
		return query;
	}
	/**
	 * �õ��б�Ĵ����
	 * @return String
	 */
	public String getTitles() {
		
		return (this.mory.trim().equals("1")?"����":"����")+this.AnteriorQuery.getQueryname();
	}

	/**
	 * �õ��б���ұ���
	 * @return String
	 */
	public String getRighttitle() {
		return righttitle;
	}
	/**
	 * �õ��б���б���
	 * @return String
	 */
	public String getMoeltitle() {
		return this.creadate.trim().substring(0, 4)
				+ "��"
				+ this.creadate.trim().substring(4,
						this.creadate.trim().length()) + "�·�";
	}
	/**
	 * �õ��б�������
	 * @return String
	 */
	public String getLefttitle() {
		return lefttitle;
	}

	/**
	 * @return anteriorQuery
	 */
	public AnteriorQueryPath getAnteriorQuery() {
		return AnteriorQuery;
	}

	/**
	 * ���� anteriorQuery
	 * @param anteriorQuery 
	 */
	public void setAnteriorQuery(AnteriorQueryPath anteriorQuery) {
		AnteriorQuery = anteriorQuery;
	}

	/**
	 * @return anteriorQueryPathMap
	 */
	public HashMap getAnteriorQueryPathMap() {
		return AnteriorQueryPathMap;
	}

	/**
	 * ���� anteriorQueryPathMap
	 * @param anteriorQueryPathMap 
	 */
	public void setAnteriorQueryPathMap(HashMap anteriorQueryPathMap) {
		AnteriorQueryPathMap = anteriorQueryPathMap;
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
	 * @return creadate
	 */
	public String getCreadate() {
		return creadate;
	}

	/**
	 * ���� creadate
	 * @param creadate 
	 */
	public void setCreadate(String creadate) {
		this.creadate = creadate;
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
	 * @return scbrhname
	 */
	public String getScbrhname() {
		return scbrhname;
	}

	/**
	 * ���� scbrhname
	 * @param scbrhname 
	 */
	public void setScbrhname(String scbrhname) {
		this.scbrhname = scbrhname;
	}

	/**
	 * @return sumA
	 */
	public int getSumA() {
		return sumA;
	}

	/**
	 * ���� sumA
	 * @param sumA 
	 */
	public void setSumA(int sumA) {
		this.sumA = sumA;
	}

	/**
	 * @return sumB
	 */
	public double[] getSumB() {
		return sumB;
	}

	/**
	 * ���� sumB
	 * @param sumB 
	 */
	public void setSumB(double[] sumB) {
		this.sumB = sumB;
	}

	/**
	 * @return sunE
	 */
	public double getSunE() {
		return sunE;
	}

	/**
	 * ���� sunE
	 * @param sunE 
	 */
	public void setSunE(double sunE) {
		this.sunE = sunE;
	}

	/**
	 * ���� lefttitle
	 * @param lefttitle 
	 */
	public void setLefttitle(String lefttitle) {
		this.lefttitle = lefttitle;
	}

	/**
	 * ���� righttitle
	 * @param righttitle 
	 */
	public void setRighttitle(String righttitle) {
		this.righttitle = righttitle;
	}

	/**
	 * @return mory
	 */
	public String getMory() {
		return mory;
	}

	/**
	 * ���� mory
	 * @param mory 
	 */
	public void setMory(String mory) {
		this.mory = mory;
	}

	
}
