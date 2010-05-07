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
 * <p/>Title: 五级分类贷款最大10户排名页面BEAN
 * <p/>===============================================
 * <p/>Description:五级分类贷款最大10户查询页面。
 * 
 * @version $Revision: 1.9 $ $Date: 2007/06/19 00:42:55 $
 * @author zhengxin
 *  <p/>修改：$Author: zhengx $
 */
public class AnteriorPageobject  {
	private int sumA;
	private double sumB[] = new double[13] ;// 浮点型合计列
	private double sunE=0.00;
	
	//数据库连接
	ConnectionManager manager;// 数据操作类
	private HashMap AnteriorQueryPathMap = new HashMap();
	private AnteriorQueryPath AnteriorQuery ;
	private String queryid;//项目编号
	
	//查询条件
	private String brhid;// 网点编号
	private String scbrhname;//网点名称
	private String creadate;// 有效时间
	private String anterior;//排名
	private String clientno;//业务号
	private String righttitle;//右标题
	private String lefttitle;//左标题
	//格式化显示
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat dfB=new DecimalFormat("###,###,###,##0.0000");
	FcsortUtil util;
	/**
	 * 设置查询条件和构建页面结果集
	 * @param request
	 * @param response
	 */
	public AnteriorPageobject() {
		util = new FcsortUtil();
		manager = ConnectionManager.getInstance();
	}
	/**
	 * 设置查询参数
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
		//Session 中取得 hashmap - FcsortQueryPath
		AnteriorQueryPathMap =(HashMap)request.getSession().getAttribute("FCSORT_ANTERI_PATH_MAP");
		AnteriorQuery = (AnteriorQueryPath)AnteriorQueryPathMap.get(queryid);
		this.brhid =request.getParameter("brhid");//得到网点编号
		if(this.brhid==null)//如果为空就取用户所在网点
		{
				UserManager um = (UserManager)request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
				String strUserName = um.getUserName(); 
				this.brhid = SCUser.getBrhId(strUserName).trim();
				System.out.println(strUserName);
		}
		this.scbrhname = SCBranch.getSName(brhid);	
		this.creadate=request.getParameter("creadate")==null?util.getDT().substring(0, 4)+util.getDT().substring(5, 7):request.getParameter("creadate").trim();//有效日期
		this.anterior=request.getParameter("anterior")==null?"10":request.getParameter("anterior").trim();//排名
		this.clientno =request.getParameter("clientno")==null?"00000":request.getParameter("clientno").trim();//
		return true;
	}
/**
 * 得到一级页面的结果集
 * @return List
 * @throws Exception 
 */
public List getFirstList() throws Exception {
		List list = new ArrayList();
		//处理网点编号
		String brhids = util.formatBrhids(SCBranch.getSubBranchAll(this.brhid));
		String sqlpart =AnteriorQuery.getSqlpart();
		//查询语句
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
 * 得到二级页面的结果集
 * @return List
 * @throws Exception 
 */
public List getSecondList() throws Exception {
		List list = new ArrayList();
		//查询语句
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
 * 得到客户相关信息
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
	//上月
	String  sqlm ="SELECT  BAL FROM   RQCLIENTDATA where availdt='"+(Integer.valueOf(date).intValue()-1)+"' and CLIENTNO='"+clientno+"'";
	//上年
	String  sqly ="SELECT  BAL FROM   RQCLIENTDATA where availdt='"+(Integer.valueOf(date.substring(0, 4)).intValue()*100-88)+"' and CLIENTNO='"+clientno+"'";
	//全部
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
 * 设置查询页面所需要的模块信息
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
	 * 设置五级分类贷款最大10户主要参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERIALL() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERIALL");
		query.setQueryname("五级分类贷款最大");
		query.setSqlpart("SBAL");
		return  query;
	}
	/**
	 * 设置五级分类不良贷款最大10户主要参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERIBAD() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERIBAD");
		query.setQueryname("五级分类不良贷款最大");
		query.setSqlpart("SUMBAL");
		return  query;
	}
	/**
	 * 设置五级分类不良贷款次级类最大10户主要参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERIONE() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERIONE");
		query.setQueryname("五级分类不良贷款次级类最大");
		query.setSqlpart("SFC_CJ_BAL");
		return  query;
	}
	/**
	 * 设置五级分类不良贷款可疑类最大10户主要参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERITWO() {
	
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERITWO");
		query.setQueryname("五级分类不良贷款可疑类最大");
		query.setSqlpart("SFC_KY_BAL");
		return  query;
	}
	/**
	 * 设置五级分类不良贷款损失类最大10户主要参数
	 * @return AnteriorQueryPath
	 */
	public AnteriorQueryPath setANTERITHR() {
		
		AnteriorQueryPath query = new AnteriorQueryPath();
		query.setQueryid("ANTERITHR");
		query.setQueryname("五级分类不良贷款损失类最大");
		query.setSqlpart("SFC_SH_BAL");
		return  query;
	}

	/**
	 * 得到查询页面大标题
	 * @retur String
	 */	
	public String getTitile() {
		return AnteriorQuery.getQueryname()+this.getAnterior()+"户统计表";
	}
	
	/**
	 * 得到查询页面右标题
	 * @retur String
	 */	
	public String getRighttitle() {
		return righttitle;
	}
	/**
	 * 得到查询页面中标题
	 * @retur String
	 */	
	public String getMoeltitle() {
		return  this.creadate.trim().substring(0, 4)+"年"+this.creadate.trim().substring(4, this.creadate.trim().length())+"月份";
	}
	/**
	 * 得到查询页面左标题
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
	 * @param anterior 要设置的 anterior
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
	 * @param anteriorQuery 要设置的 anteriorQuery
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
	 * @param anteriorQueryPathMap 要设置的 anteriorQueryPathMap
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
	 * @param brhid 要设置的 brhid
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
	 * @param clientno 要设置的 clientno
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
	 * @param creadate 要设置的 creadate
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
	 * @param queryid 要设置的 queryid
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
	 * @param scbrhname 要设置的 scbrhname
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
	 * @param sumA 要设置的 sumA
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
	 * @param sumB 要设置的 sumB
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
	 * @param sunE 要设置的 sunE
	 */
	public void setSunE(double sunE) {
		this.sunE = sunE;
	}
	/**
	 * @param lefttitle 要设置的 lefttitle
	 */
	public void setLefttitle(String lefttitle) {
		this.lefttitle = lefttitle;
	}
	/**
	 * @param moeltitle 要设置的 moeltitle
	 */
	public void setMoeltitle(String moeltitle) {
	}
	/**
	 * @param righttitle 要设置的 righttitle
	 */
	public void setRighttitle(String righttitle) {
		this.righttitle = righttitle;
	}
	/**
	 * @param titile 要设置的 titile
	 */
	public void setTitile(String titile) {
	}
	
}
