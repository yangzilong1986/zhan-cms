package zt.cms.fcsort.anterior;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import java.util.HashMap;

import zt.platform.db.DBUtil;
import zt.cms.fcsort.common.FcsortUtil;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶����������10������ҳ��BEAN
 * <p/>===============================================
 * <p/>Description:�弶����������10����ѯҳ�档
 * 
 * @version $Revision: 1.9 $ $Date: 2007/06/19 00:42:55 $
 * @author zhengxin
 *  <p/>�޸ģ�$Author: zhengx $
 */
public class AnteriorPageobject  {
	private int sumA;
	private double sumB[] = new double[13] ;// �����ͺϼ���
	private double sunE=0.00;
	
	//���ݿ�����
	ConnectionManager manager;// ���ݲ�����
	private HashMap AnteriorQueryPathMap = new HashMap();
	private AnteriorQueryPath AnteriorQuery ;
	private String queryid;//��Ŀ���
	
	//��ѯ����
	private String brhid;// ������
	private String scbrhname;//��������
	private String creadate;// ��Чʱ��
	private String anterior;//����
	private String clientno;//ҵ���
	private String righttitle;//�ұ���
	private String lefttitle;//�����
	//��ʽ����ʾ
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat dfB=new DecimalFormat("###,###,###,##0.0000");
	FcsortUtil util;
	/**
	 * ���ò�ѯ�����͹���ҳ������
	 * @param request
	 * @param response
	 */
	public AnteriorPageobject() {
		util = new FcsortUtil();
		manager = ConnectionManager.getInstance();
	}
	/**
	 * ���ò�ѯ����
	 * @param request
	 * @return boolean
	 * @throws Exception
	 */
	public boolean setRequest(HttpServletRequest request) throws Exception
	{
		this.init(request);
		if(request.getParameter("QUERYID")==null||request.getParameter("QUERYID").equals(""))
		{
			return false;
		}
		else
		{
			this.queryid=request.getParameter("QUERYID").trim();
		}
		//Session ��ȡ�� hashmap - FcsortQueryPath
		AnteriorQueryPathMap =(HashMap)request.getSession().getAttribute("FCSORT_ANTERI_PATH_MAP");
		AnteriorQuery = (AnteriorQueryPath)AnteriorQueryPathMap.get(queryid);
		this.brhid =request.getParameter("brhid");//�õ�������
		if(this.brhid==null)//���Ϊ�վ�ȡ�û���������
		{
				UserManager um = (UserManager)request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
				String strUserName = um.getUserName(); 
				this.brhid = SCUser.getBrhId(strUserName).trim();
				System.out.println(strUserName);
		}
		this.scbrhname = SCBranch.getSName(brhid);	
		this.creadate=request.getParameter("creadate")==null?util.getDT().substring(0, 4)+util.getDT().substring(5, 7):request.getParameter("creadate").trim();//��Ч����
		this.anterior=request.getParameter("anterior")==null?"10":request.getParameter("anterior").trim();//����
		this.clientno =request.getParameter("clientno")==null?"00000":request.getParameter("clientno").trim();//
		return true;
	}
/**
 * �õ�һ��ҳ��Ľ����
 * @return List
 * @throws Exception 
 */
public List getFirstList() throws Exception {
		List list = new ArrayList();
		//����������
		String brhids = util.formatBrhids(SCBranch.getSubBranchAll(this.brhid));
		String sqlpart =AnteriorQuery.getSqlpart();
		//��ѯ���
		StringBuffer sql = new StringBuffer(" SELECT  CLIENTNO,max(CLIENTNAME) KEHUNAME,sum(DBCOUNT) SDBCOUNT,sum(BAL)");
			sql.append("SBAL,sum(BAL3) SBAL3,sum(BAL4) SBAL4,");
			sql.append("sum(BAL5) SBAL5,sum(BAL9) SBAL9,");
			sql.append("sum(DUEBAL1) SDUEBAL1,sum(DUEBAL2) SDUEBAL2,");
			sql.append("sum(FC_ZC_BAL) SFC_ZC_BAL,sum(FC_GZ_BAL) SFC_GZ_BAL,");
			sql.append("sum(FC_CJ_BAL) SFC_CJ_BAL,sum(FC_KY_BAL) SFC_KY_BAL,");
			sql.append("sum(FC_SH_BAL) SFC_SH_BAL ,");
			sql.append("(sum(FC_CJ_BAL)+sum(FC_KY_BAL)+sum(FC_SH_BAL)) SUMBAL FROM   RQCLIENTDATA ");
			sql.append(" where   availdt='"+this.creadate+"' ");
			sql.append(" and brhid in "+brhids);
			sql.append(" group by CLIENTNO ");
			sql.append(" order by "+sqlpart+" desc  FETCH   FIRST   "+anterior+"  ROWS   ONLY ");
		CachedRowSet crs =  manager.getRs(sql.toString());
		while (crs.next()) {
			FirstTr tr =new FirstTr();
			tr.setClentno(crs.getString("CLIENTNO"));
			tr.setClentname(crs.getString("KEHUNAME"));
		
			//tr.setBrhid(crs.getString("000000"));
			tr.setBrhname(SCBranch.getSName(tr.getBrhid()));
			tr.setDbcount(crs.getInt("SDBCOUNT"));
			sumA+=crs.getInt("SDBCOUNT");
			
			
			tr.setCountbal(crs.getDouble("SBAL")/10000);
			sumB[0]+=crs.getDouble("SBAL")/10000;
			
			tr.setBalA(crs.getDouble("SBAL")/10000);
			sumB[1]+=crs.getDouble("SBAL")/10000;
			
			tr.setBalB(crs.getDouble("SBAL9")/10000);
			sumB[2]+=crs.getDouble("SBAL9")/10000;
			
			tr.setBal3(crs.getDouble("SBAL3")/10000);
			sumB[3]+=crs.getDouble("SBAL3")/10000;
			
			tr.setBal4(crs.getDouble("SBAL4")/10000);
			sumB[4]+=crs.getDouble("SBAL4")/10000;
			
			tr.setBal5(crs.getDouble("SBAL5")/10000);
			sumB[5]+=crs.getDouble("SBAL5")/10000;
			
			tr.setDuebal1(crs.getDouble("SDUEBAL1")/10000);
			sumB[6]+=crs.getDouble("SDUEBAL1")/10000;
			
			tr.setDuebal2(crs.getDouble("SDUEBAL2")/10000);
			sumB[7]+=crs.getDouble("SDUEBAL2")/10000;
			
			tr.setFczcbal(crs.getDouble("SFC_ZC_BAL")/10000);
			sumB[8]+=crs.getDouble("SFC_ZC_BAL")/10000;
			
			tr.setFcgzbal(crs.getDouble("SFC_GZ_BAL"));
			sumB[9]+=crs.getDouble("SFC_GZ_BAL")/10000;
			
			tr.setFcbalcj(crs.getDouble("SFC_CJ_BAL")/10000);
			sumB[10]+=crs.getDouble("SFC_CJ_BAL")/10000;
			
			tr.setFcbalky(crs.getDouble("SFC_KY_BAL")/10000);
			sumB[11]+=crs.getDouble("SFC_KY_BAL")/10000;
			
			tr.setFcbalsh(crs.getDouble("SFC_SH_BAL")/10000);
			sumB[12]+=crs.getDouble("SFC_SH_BAL")/10000;
			
			
			tr.setSumbad(crs.getDouble("SUMBAL")/10000);
			sunE+=crs.getDouble("SUMBAL")/10000;
			list.add(tr);
		}
		return list;
	}	
/**
 * �õ�����ҳ��Ľ����
 * @return List
 * @throws Exception 
 */
public List getSecondList() throws Exception {
		List list = new ArrayList();
		//��ѯ���
		StringBuffer sql = new StringBuffer("select BMNO,CNLNO,BRHID,(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=LOANCAT3) LOANCAT3 , ");
		sql.append("  NOWBAL,PERIMON,CRTRATE,PAYDATE,ENDDATE, ");
		sql.append(" (select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=LOANCAT2) LOANCAT2 ,");
		sql.append(" (select enudt from PTENUMINFODETL where enuid='LoanCat1' and enutp=LOANCAT1) LOANCAT1,");
		sql.append(" (select USERNAME from SCUSER where LOGINNAME=FIRSTRESP) FIRSTRESP");
		sql.append("  from RQLOANLIST where CLIENTNO ='"+this.clientno+"'");
		CachedRowSet crs =  manager.getRs(sql.toString());
		while (crs.next()) {
			SecondTr tr = new SecondTr();
			tr.setBmno(crs.getString("BMNO"));
			tr.setCnlno(crs.getString("CNLNO"));
			tr.setBrhid(crs.getString("BRHID"));
			tr.setLoancat3(crs.getString("LOANCAT3"));
			tr.setNowbal(crs.getDouble("NOWBAL")/10000);
			sumB[0]+=crs.getDouble("NOWBAL")/10000;
			tr.setPerimon(crs.getInt("PERIMON"));
			tr.setCrteate(crs.getDouble("CRTRATE"));
			tr.setPaydate(crs.getString("PAYDATE"));
			tr.setEnddate(crs.getString("ENDDATE"));
			tr.setLoancat2(crs.getString("LOANCAT2"));
			tr.setLoancat1(crs.getString("LOANCAT1"));
			tr.setFirstresp(crs.getString("FIRSTRESP"));
			list.add(tr);
		}
		

		return list;
	}
/**
 * �õ��ͻ������Ϣ
 * @param clientno
 * @param brhid
 * @param date
 * @param bal
 * @return ClientInfo
 * @throws Exception
 */
public ClientInfo getClientInfo(String clientno,String brhid,String date,String bal) throws Exception
{
	ClientInfo vo  = new ClientInfo();
	
	StringBuffer sql1 = new StringBuffer(" select  ADDRESSINLAW,(select enudt  from PTENUMINFODETL where enutp=SECTORCAT1 and enuid='SectorCat2'),(select enudt  from PTENUMINFODETL where enutp=CLIENTTYPE and enuid='ClientType2' )  from CMCORPCLIENT ");
	sql1.append(" where  CLIENTNO='"+clientno+"'");
	CachedRowSet crs =  manager.getRs(sql1.toString());
	StringBuffer sql2 = new  StringBuffer("select  CURRENTADDRESS,(select enudt  from PTENUMINFODETL where enutp=SECTORCAT1 and enuid='SectorCat2'),(select enudt  from PTENUMINFODETL where enutp=CLIENTTYPE and enuid='ClientType2' )  from CMINDVCLIENT  ");
	sql2.append(" where  CLIENTNO='"+clientno+"'");
	if(crs.size()==0)
	{
		 crs =  manager.getRs(sql2.toString());
	}
	
	while (crs.next()) {
		vo.setDizhi(DBUtil.fromDB(crs.getString(1)==null?"":crs.getString(1)));
		vo.setHangye(DBUtil.fromDB(crs.getString(2)==null?"":crs.getString(2)));
		vo.setXingzhi(DBUtil.fromDB(crs.getString(3)==null?"":crs.getString(3)));
	}
	//����
	String  sqlm ="SELECT  BAL FROM   RQCLIENTDATA where availdt='"+(Integer.valueOf(date).intValue()-1)+"' and CLIENTNO='"+clientno+"'";
	//����
	String  sqly ="SELECT  BAL FROM   RQCLIENTDATA where availdt='"+(Integer.valueOf(date.substring(0, 4)).intValue()*100-88)+"' and CLIENTNO='"+clientno+"'";
	//ȫ��
	String sqla = "select  LASTYEARBAL from RQTBLACCDATA where TBLACCNO='F0010' and brhid='"+brhid+"'";
	String d1  =date.substring(0, 4)+"-"+date.substring(4,6)+"-01";
	String d2 =date.substring(0, 4)+"-"+(Integer.valueOf(date.substring(4,6)).intValue()+1)+"-01";
	String sqlbad ="select sum(amt1) from FCDATA where ftype=1 and dim1>=3 and brhid='"+brhid+"' and dt between'"+d1+"' and '"+d2+"' ";
	double balM =0.00;
	double balY =0.00;
	double balA =1;
	double balbad =1;
	crs= manager.getRs(sqly);
	while(crs.next())
	{
		balY =crs.getDouble(1);
	}
	crs= manager.getRs(sqlm);
	while(crs.next())
	{
	
		balM =crs.getDouble(1);
	}
	crs= manager.getRs(sqla);
	while(crs.next())
	{
	
		balA =crs.getDouble(1);
	}
	crs= manager.getRs(sqlbad);
	while(crs.next())
	{
	
		balbad =crs.getDouble(1);
	}
	
	double sumbal =Double.valueOf(bal).doubleValue()*10000;
	vo.setBY(String.valueOf((sumbal-balY)/10000));
	vo.setBM(String.valueOf((sumbal-balM)/10000));
	vo.setBA(String.valueOf(sumbal/balA));
	vo.setBad(String.valueOf(sumbal/balbad));
	

	
	return vo;
}

/**
 * ���ò�ѯҳ������Ҫ��ģ����Ϣ
 * @param request
 * @return boolean
 * @throws Exception
 */
	public boolean init(HttpServletRequest request) throws Exception
	{
		HashMap map = new HashMap();
		map.put("ANTERIALL", this.setANTERIALL());
		map.put("ANTERIONE", this.setANTERIONE());
		map.put("ANTERITWO", this.setANTERITWO());
		map.put("ANTERITHR", this.setANTERITHR());
		map.put("ANTERIBAD",this.setANTERIBAD());
		request.getSession().setAttribute("FCSORT_ANTERI_PATH_MAP", map);
		
		return true;
	}
	/**
	 * �����弶����������10����Ҫ����
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERIALL() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERIALL");
		query.setQueryname("�弶����������");
		query.setSqlpart("SBAL");
		return  query;
	}
	/**
	 * �����弶���಻���������10����Ҫ����
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERIBAD() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERIBAD");
		query.setQueryname("�弶���಻���������");
		query.setSqlpart("SUMBAL");
		return  query;
	}
	/**
	 * �����弶���಻������μ������10����Ҫ����
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERIONE() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERIONE");
		query.setQueryname("�弶���಻������μ������");
		query.setSqlpart("SFC_CJ_BAL");
		return  query;
	}
	/**
	 * �����弶���಻��������������10����Ҫ����
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERITWO() {
	
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERITWO");
		query.setQueryname("�弶���಻��������������");
		query.setSqlpart("SFC_KY_BAL");
		return  query;
	}
	/**
	 * �����弶���಻��������ʧ�����10����Ҫ����
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERITHR() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERITHR");
		query.setQueryname("�弶���಻��������ʧ�����");
		query.setSqlpart("SFC_SH_BAL");
		return  query;
	}

	/**
	 * �õ���ѯҳ������
	 * @retur String
	 */	
	public String getTitile() {
		return AnteriorQuery.getQueryname()+this.getAnterior()+"��ͳ�Ʊ�";
	}
	
	/**
	 * �õ���ѯҳ���ұ���
	 * @retur String
	 */	
	public String getRighttitle() {
		return righttitle;
	}
	/**
	 * �õ���ѯҳ���б���
	 * @retur String
	 */	
	public String getMoeltitle() {
		return  this.creadate.trim().substring(0, 4)+"��"+this.creadate.trim().substring(4, this.creadate.trim().length())+"�·�";
	}
	/**
	 * �õ���ѯҳ�������
	 * @retur String
	 */	
	public String getLefttitle() {
		return lefttitle;
	}
	/**
	 * @return anterior
	 */
	public String getAnterior() {
		return anterior;
	}
	/**
	 * @param anterior Ҫ���õ� anterior
	 */
	public void setAnterior(String anterior) {
		this.anterior = anterior;
	}
	/**
	 * @return anteriorQuery
	 */
	public AnteriorQueryPath getAnteriorQuery() {
		return AnteriorQuery;
	}
	/**
	 * @param anteriorQuery Ҫ���õ� anteriorQuery
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
	 * @param anteriorQueryPathMap Ҫ���õ� anteriorQueryPathMap
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
	 * @param brhid Ҫ���õ� brhid
	 */
	public void setBrhid(String brhid) {
		this.brhid = brhid;
	}
	/**
	 * @return clientno
	 */
	public String getClientno() {
		return clientno;
	}
	/**
	 * @param clientno Ҫ���õ� clientno
	 */
	public void setClientno(String clientno) {
		this.clientno = clientno;
	}
	/**
	 * @return creadate
	 */
	public String getCreadate() {
		return creadate;
	}
	/**
	 * @param creadate Ҫ���õ� creadate
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
	 * @param queryid Ҫ���õ� queryid
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
	 * @param scbrhname Ҫ���õ� scbrhname
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
	 * @param sumA Ҫ���õ� sumA
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
	 * @param sumB Ҫ���õ� sumB
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
	 * @param sunE Ҫ���õ� sunE
	 */
	public void setSunE(double sunE) {
		this.sunE = sunE;
	}
	/**
	 * @param lefttitle Ҫ���õ� lefttitle
	 */
	public void setLefttitle(String lefttitle) {
		this.lefttitle = lefttitle;
	}
	/**
	 * @param moeltitle Ҫ���õ� moeltitle
	 */
	public void setMoeltitle(String moeltitle) {
	}
	/**
	 * @param righttitle Ҫ���õ� righttitle
	 */
	public void setRighttitle(String righttitle) {
		this.righttitle = righttitle;
	}
	/**
	 * @param titile Ҫ���õ� titile
	 */
	public void setTitile(String titile) {
	}
	
}
