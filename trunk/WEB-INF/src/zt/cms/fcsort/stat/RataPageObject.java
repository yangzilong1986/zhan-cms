package zt.cms.fcsort.stat;
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
 * <p/>Title: �弶����ͳ��ҳ��BEAN
 * <p/>===============================================
 * <p/>Description
 * @version $Revision: 1.1 $  $Date: 2007/05/28 11:43:39 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class RataPageObject {
	private int sumA;

	private double sumB[] = new double[6];// �����ͺϼ���

	private double sun1,sun2,sun3,sun4,sun5,sun6,sun7,sun8,sun9 = 0.00;

	// ���ݿ�����
	ConnectionManager manager;// ���ݲ�����

	private HashMap AnteriorQueryPathMap = new HashMap();

	private AnteriorQueryPath AnteriorQuery;

	private String queryid;// ��Ŀ���

	// ��ѯ����
	private String brhid;// ������

	private String scbrhname;// ��������

	private String creadate;// ��ѯʱ��
	private String kumulist;///��Ŀ

	private String righttitle;// �ұ���

	private String lefttitle;// �����

	// ��ʽ����ʾ
	DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

	DecimalFormat dfB = new DecimalFormat("###,###,###,##0.0000");

	FcsortUtil util;

	/**
	 * ���ò�ѯ�����͹���ҳ������
	 * @param request
	 * @param response
	 */
	public RataPageObject() {
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
		AnteriorQueryPathMap = (HashMap) request.getSession().getAttribute("FCSORT_SORTSATA_PATH_MAP");
		AnteriorQuery = (AnteriorQueryPath) AnteriorQueryPathMap.get(queryid);
		this.brhid = request.getParameter("brhid");// �õ�������
		if (this.brhid == null)// ���Ϊ�վ�ȡ�û���������
		{
			UserManager um = (UserManager) request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
			String strUserName = um.getUserName();
			this.brhid = SCUser.getBrhId(strUserName).trim();
		}
		this.scbrhname = SCBranch.getSName(brhid);
		this.creadate = request.getParameter("creadate") == null ? util.getDT(): request.getParameter("creadate").trim();// ��Ч����
		this.kumulist =request.getParameter("arracclist")==null?"":request.getParameter("arracclist");
		//System.out.println("arracclist="+"'"+this.kumulist.replaceAll(",", "','")+"'");
		return true;
	}
	/**
	 * �õ��弶���ఴ��Ŀ��ѯ�Ľ��
	 * @return List
	 */
	public List getStatByKemuList()
	{
		List list = new ArrayList();
		try {
			StringBuffer accsql=new StringBuffer("select AccNo,AccName from SCHostAcc where ACCSTATUS='0' and acctp='3' and BALTYPE='1' ");
			accsql.append("and accno in"+"('"+this.kumulist.replaceAll(",", "','")+"') ");
			accsql.append(" order by AccNo");
			CachedRowSet crsA = manager.getRs(accsql.toString());
			 
			while (crsA.next()) {
				FirstTr tr =new FirstTr();
				tr.setAccno(crsA.getString("AccNo"));
				tr.setAccname(DBUtil.fromDB(crsA.getString("AccName")));
				String brhids = this.getString(SCBranch.getAllSubBrh1(this.getBrhid()));//�õ����㼯��
				if (brhids == null || brhids.equals("")) {
					brhids = "('" +this.getBrhid() + "')";
				} else {
					brhids = "('" + brhids + "')";
				}
				StringBuffer sql = new StringBuffer();
				sql.append("select  sum(amt1) A1,sum(amt2) A2, ");
				sql.append(" sum(case when dim1=1 then  AMT1 else 0 end) H1, ");
				sql.append(" sum(case when dim1=2 then  AMT1 else 0 end) H2, ");
				sql.append(" sum(case when dim1=3 then  AMT1 else 0 end) H3, ");
				sql.append(" sum(case when dim1=4 then  AMT1 else 0 end) H4, ");
				sql.append(" sum(case when dim1=5 then  AMT1 else 0 end) H5  ");
				sql.append(" from FCDATA ");
				sql.append(" where ftype=24 and dt='"+this.creadate+"' and  ");
				sql.append(" brhid in"+brhids+" and  ");
				sql.append(" dim2="+crsA.getString("AccNo"));
				CachedRowSet crsB = manager.getRs(sql.toString());
				while (crsB.next()) {
					tr.setBal(crsB.getDouble("A1")/10000);
					sun1+=tr.getBal();
					tr.setCount(crsB.getDouble("A2"));
					sun2+=tr.getCount();
					tr.setH1(crsB.getDouble("H1")/10000);
					sun4+=tr.getH1();
					tr.setH2(crsB.getDouble("H2")/10000);
					sun5+=tr.getH2();
					tr.setBala(tr.getH1()+tr.getH2());
					sun3+=tr.getBala();
					
					tr.setH3(crsB.getDouble("H3")/10000);
					sun7+=tr.getH3();
					tr.setH4(crsB.getDouble("H4")/10000);
					sun8+=tr.getH4();
					tr.setH5(crsB.getDouble("H5")/10000);
					sun9+=tr.getH5();
					tr.setBalb(tr.getH3()+tr.getH4()+tr.getH5());
					sun6+=tr.getBalb();
				}
				
				list.add(tr);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/***
	 * �õ������
	 * @return List
	 */
	public List getSortStatlist()
	{
		if(this.queryid.trim().equals("STATBYTERM"))
		{
			return this.getListForTerm();
		}
		else if(this.queryid.trim().equals("RATEBYVOUCH"))
		{
			return this.getListForVouch();
		}
		return null;
	}
	/**
	 * �õ��弶�����Ǵ������޵Ľ����
	 * @return List
	 */
	public List getListForTerm()
	{
		List list = new ArrayList();
		try {
			 	String brhids = this.getString(SCBranch.getAllSubBrh1(this.getBrhid()));//�õ����㼯��
				if (brhids == null || brhids.equals("")) {
					brhids = "('" +this.getBrhid() + "')";
				} else {
					brhids = "('" + brhids + "')";
				}
				for(int i =1;i<4;i++)
				{
					FirstTr tr =new FirstTr();
					StringBuffer sql = new StringBuffer();
					sql.append("select  sum(amt1) A1,sum(amt2) A2, ");
					sql.append(" sum(case when dim1=1 then  AMT1 else 0 end) H1, ");
					sql.append(" sum(case when dim1=2 then  AMT1 else 0 end) H2, ");
					sql.append(" sum(case when dim1=3 then  AMT1 else 0 end) H3, ");
					sql.append(" sum(case when dim1=4 then  AMT1 else 0 end) H4, ");
					sql.append(" sum(case when dim1=5 then  AMT1 else 0 end) H5  ");
					sql.append(" from FCDATA ");
					sql.append(" where ftype=25 and dt='"+this.creadate+"' and  ");
					sql.append(" brhid in"+brhids+" and  ");
					sql.append(" dim2="+i);
					CachedRowSet crsB = manager.getRs(sql.toString());
					while (crsB.next()) {
						
						if(i==1)
						{
							tr.setAccname("����");
						}
						if(i==2)
						{
							tr.setAccname("����");
						}
						if(i==3)
						{
							tr.setAccname("����");
						}
						tr.setBal(crsB.getDouble("A1")/10000);
						sun1+=tr.getBal();
						tr.setCount(crsB.getDouble("A2"));
						sun2+=tr.getCount();
						tr.setH1(crsB.getDouble("H1")/10000);
						sun4+=tr.getH1();
						tr.setH2(crsB.getDouble("H2")/10000);
						sun5+=tr.getH2();
						tr.setBala(tr.getH1()+tr.getH2());
						sun3+=tr.getBala();
						
						tr.setH3(crsB.getDouble("H3")/10000);
						sun7+=tr.getH3();
						tr.setH4(crsB.getDouble("H4")/10000);
						sun8+=tr.getH4();
						tr.setH5(crsB.getDouble("H5")/10000);
						sun9+=tr.getH5();
						tr.setBalb(tr.getH3()+tr.getH4()+tr.getH5());
						sun6+=tr.getBalb();
					}
					
					list.add(tr);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * �õ��弶���ఴ������ʽ�Ľ���� 
	 * @return List
	 */
	public List getListForVouch()
	{
		List list = new ArrayList();
		try {
			StringBuffer accsql=new StringBuffer("select ENUTP,ENUDT from PTENUMINFODETL where ENUID ='LoanType3'");
			CachedRowSet crsA = manager.getRs(accsql.toString());
			while (crsA.next()) {
				FirstTr tr =new FirstTr();
				tr.setAccname(DBUtil.fromDB(crsA.getString("ENUDT")));
				String brhids = this.getString(SCBranch.getAllSubBrh1(this.getBrhid()));//�õ����㼯��
				if (brhids == null || brhids.equals("")) {
					brhids = "('" +this.getBrhid() + "')";
				} else {
					brhids = "('" + brhids + "')";
				}
				StringBuffer sql = new StringBuffer();
				sql.append("select  sum(amt1) A1,sum(amt2) A2, ");
				sql.append(" sum(case when dim1=1 then  AMT1 else 0 end) H1, ");
				sql.append(" sum(case when dim1=2 then  AMT1 else 0 end) H2, ");
				sql.append(" sum(case when dim1=3 then  AMT1 else 0 end) H3, ");
				sql.append(" sum(case when dim1=4 then  AMT1 else 0 end) H4, ");
				sql.append(" sum(case when dim1=5 then  AMT1 else 0 end) H5  ");
				sql.append(" from FCDATA ");
				sql.append(" where ftype=3 and dt='"+this.creadate+"' and  ");
				sql.append(" brhid in"+brhids+" and  ");
				sql.append(" dim2="+crsA.getString("ENUTP"));
				CachedRowSet crsB = manager.getRs(sql.toString());
				while (crsB.next()) {
					tr.setBal(crsB.getDouble("A1")/10000);
					sun1+=tr.getBal();
					tr.setCount(crsB.getDouble("A2"));
					sun2+=tr.getCount();
					tr.setH1(crsB.getDouble("H1")/10000);
					sun4+=tr.getH1();
					tr.setH2(crsB.getDouble("H2")/10000);
					sun5+=tr.getH2();
					tr.setBala(tr.getH1()+tr.getH2());
					sun3+=tr.getBala();
					
					tr.setH3(crsB.getDouble("H3")/10000);
					sun7+=tr.getH3();
					tr.setH4(crsB.getDouble("H4")/10000);
					sun8+=tr.getH4();
					tr.setH5(crsB.getDouble("H5")/10000);
					sun9+=tr.getH5();
					tr.setBalb(tr.getH3()+tr.getH4()+tr.getH5());
					sun6+=tr.getBalb();
				}
				
				list.add(tr);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
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
	 * ���ò�ѯҳ������Ҫ��ģ����Ϣ
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public boolean init(HttpServletRequest request) throws Exception {
		HashMap map = new HashMap();
		map.put("STATBYKEMU", this.setSTATBYKEMU());
		map.put("STATBYTERM", this.setSTATBYTERM());
		map.put("RATEBYVOUCH", this.setRATEBYVOUCH());

		request.getSession().setAttribute("FCSORT_SORTSATA_PATH_MAP", map);

		return true;
	}

	/**
	 * �����弶���ఴ��Ŀͳ�Ʊ�ģ���������
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setSTATBYKEMU() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("STATBYKEMU");
		query.setQueryname("�弶���ఴ��Ŀͳ�Ʊ�");
		query.setSqlpart("STATBYKEMU");
		return query;
	}
	/**
	 * �����弶���ఴ��������ͳ�ƻ�������
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setSTATBYTERM() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("STATBYTERM");
		query.setQueryname("�弶���ఴ��������ͳ�Ʊ�");
		query.setSqlpart("STATBYTERM");
		return query;
	}

	/**
	 * �����弶���ఴ������ʽͳ�Ʊ��������
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setRATEBYVOUCH() {

		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("RATEBYVOUCH");
		query.setQueryname("�弶���ఴ������ʽͳ�Ʊ�");
		query.setSqlpart("RATEBYVOUCH");
		return query;
	}
	/**
	 * �õ��б�Ĵ����
	 * @return String
	 */
	public String getTitles() {
		return this.AnteriorQuery.getQueryname();
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
	 * @return kumulist
	 */
	public String getKumulist() {
		return kumulist;
	}

	/**
	 * ���� kumulist
	 * @param kumulist 
	 */
	public void setKumulist(String kumulist) {
		this.kumulist = kumulist;
	}

	/**
	 * @return sun1
	 */
	public double getSun1() {
		return sun1;
	}

	/**
	 * ���� sun1
	 * @param sun1 
	 */
	public void setSun1(double sun1) {
		this.sun1 = sun1;
	}

	/**
	 * @return sun2
	 */
	public double getSun2() {
		return sun2;
	}

	/**
	 * ���� sun2
	 * @param sun2 
	 */
	public void setSun2(double sun2) {
		this.sun2 = sun2;
	}

	/**
	 * @return sun3
	 */
	public double getSun3() {
		return sun3;
	}

	/**
	 * ���� sun3
	 * @param sun3 
	 */
	public void setSun3(double sun3) {
		this.sun3 = sun3;
	}

	/**
	 * @return sun4
	 */
	public double getSun4() {
		return sun4;
	}

	/**
	 * ���� sun4
	 * @param sun4 
	 */
	public void setSun4(double sun4) {
		this.sun4 = sun4;
	}

	/**
	 * @return sun5
	 */
	public double getSun5() {
		return sun5;
	}

	/**
	 * ���� sun5
	 * @param sun5 
	 */
	public void setSun5(double sun5) {
		this.sun5 = sun5;
	}

	/**
	 * @return sun6
	 */
	public double getSun6() {
		return sun6;
	}

	/**
	 * ���� sun6
	 * @param sun6 
	 */
	public void setSun6(double sun6) {
		this.sun6 = sun6;
	}

	/**
	 * @return sun7
	 */
	public double getSun7() {
		return sun7;
	}

	/**
	 * ���� sun7
	 * @param sun7 
	 */
	public void setSun7(double sun7) {
		this.sun7 = sun7;
	}

	/**
	 * @return sun8
	 */
	public double getSun8() {
		return sun8;
	}

	/**
	 * ���� sun8
	 * @param sun8 
	 */
	public void setSun8(double sun8) {
		this.sun8 = sun8;
	}

	/**
	 * @return sun9
	 */
	public double getSun9() {
		return sun9;
	}

	/**
	 * ���� sun9
	 * @param sun9 
	 */
	public void setSun9(double sun9) {
		this.sun9 = sun9;
	}

	
}
