package zt.cms.fcsort.transfer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import java.util.HashMap;


import zt.cms.fcsort.common.FcsortUtil;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶���಻���ʲ�Ǩ�������ѯ 
 * <p/>===============================================
 * <p/>Description: Ϊǰ̨ҳ���ṩ��ѯ����Ҫ�����ݺͷ�ҳ��ز�����
 * 
 * @version $Revision: 1.4 $ $Date: 2007/05/28 14:38:57 $
 * @author zhengxin <p/>�޸ģ�$Author: zhengx $ 
 */

public class TransferPageobject  {
	private int sumA,sumB;
	private double sumC,sumD,sumE,sumF;// �����ͺϼ���
	private String brhid;// ������
	private String scbrhname;//��������
	private String creadate;// ���ʱ��
	private String startdate;// ��ʼʱ��
	private String enddate;// ����ʱ��
	private String sartbal;// ʱ�㿪ʼ���
	private String endbal;// ʱ��������
	private String stype;// 1 ����  2 ����
	private String mory;//���ڻ���
	private String moeltitle;//�б���
	private String clientmgr;//CLIENTMGR �ͻ�����
	private String firstresp;//��һ������
	
	ConnectionManager manager;// ���ݲ�����
	private HashMap fcsortQueryPathMap = new HashMap();
	private FcsortQueryPath fcsortquery ;
	private String queryid;//��Ŀ���
	private String sqlpart;//����ҳ������Ҫ��һ����SQL
	private String sqlpartname;//����ҳ������Ҫ��һ����SQL����
	private String sqlpartvalue;//����ҳ������Ҫ��һ����SQLֵ
	
	//**��ҳ���
	private int pagecount;//ҳ���¼��
	private int pagesize;// ҳ���С
	private int currpage;// ��ǰҳ
	private int maxpage;// ���ҳ
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat dfB=new DecimalFormat("###,###,###,##0.0000");
	FcsortUtil util;
	/**
	 * ���ò�ѯ�����͹���ҳ������
	 * 
	 * @param request
	 * @param response
	 */
	public TransferPageobject() {
		util = new FcsortUtil();
		manager = ConnectionManager.getInstance();
	}
	/**
	 * ���ò�ѯ����
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean setRequest(HttpServletRequest request) throws Exception
	{
		this.mory=request.getParameter("MORY") == null ? "2":request.getParameter("MORY");
		this.init(request);
		if(request.getParameter("QUERYID")==null||request.getParameter("QUERYID").equals(""))
		{
			return false;
		}
		else
		{
			this.queryid=request.getParameter("QUERYID").trim();
		}
	
		System.out.println(this.mory+"_______________________________________");
		//Session ��ȡ�� hashmap - FcsortQueryPath
		fcsortQueryPathMap =(HashMap)request.getSession().getAttribute("FCSORT_TRANSFER_PATH_MAP");
		fcsortquery = (FcsortQueryPath)fcsortQueryPathMap.get(queryid);
		this.brhid =request.getParameter("brhid");//�õ�������
		if(this.brhid==null)
		{
				UserManager um = (UserManager)request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
				String strUserName = um.getUserName(); 
				this.brhid = SCUser.getBrhId(strUserName).trim();
		}
		this.scbrhname = SCBranch.getSName(brhid);
		
		this.creadate=request.getParameter("creadate")==null?util.getDT():request.getParameter("creadate").trim();// ���ʱ��
		this.startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");// ��ʼʱ��
		this.enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate");// ����ʱ��
		this.sartbal=request.getParameter("sartbal");// ʱ�㿪ʼ���
		this.endbal=request.getParameter("endbal");// ʱ��������
		this.stype=request.getParameter("stype")==null?"2":request.getParameter("stype");
		this.sqlpartname=request.getParameter("sqlpartname")==null?"1":request.getParameter("sqlpartname").trim();
		this.sqlpartvalue=request.getParameter("sqlpartvalue")==null?"1 ":request.getParameter("sqlpartvalue").trim();
		this.clientmgr=request.getParameter("CLIENTMGR")==null?"":request.getParameter("CLIENTMGR").trim();//�ͻ�����
		this.firstresp=request.getParameter("FIRSTRESP")==null?"":request.getParameter("FIRSTRESP").trim();//��һ������
		
		//һ��ƴװ��ѯ����SQLPART
		if(queryid.trim().equals("DTRANSFER"))//����
		{
			this.sqlpart=this.chooseSqlpartD(Integer.valueOf(this.sqlpartname).intValue());
		}
		else
		{
			this.sqlpart=this.chooseSqlpartU(Integer.valueOf(this.sqlpartname).intValue());
		}
		if(this.sqlpartname.trim().equals("sum"))
		{
			this.sqlpart=" 1=1 ";
		}
		this.setTitle();
		
		//************��ҳ���*******************
		this.pagesize=Integer.valueOf(request.getParameter("pagesize")==null?"10":request.getParameter("pagesize")).intValue();
		this.currpage=Integer.valueOf(request.getParameter("currpage")==null?"1":request.getParameter("currpage")).intValue();
		return true;
	}
	/**
	 * һ��ҳ���ѯ��� FCBMAIN���
	 * @return String
	 */
	public String getFristsqlForFcBmain(String onebrhid,String sqltabname)
	{
		String brhids = util.formatBrhids(SCBranch.getSubBranchAll(onebrhid));
		StringBuffer sql = new StringBuffer("select  count(FC.bmno),count(distinct(FC.idno)),");
		sql.append("sum(FC.bal) fcbal,sum(contractamt) contractamt  ");
		sql.append(" from "+sqltabname+" FC,rqloanlist where  rqloanlist.bmno=FC.bmno and ");
		sql.append(getSqlBixuan());
		sql.append("   FC.brhid  in "+ brhids);
		sql.append("and  rqloanlist.CLIENTMGR like '%"+this.clientmgr+"%' ");
		return sql.toString();
	}

/**
 * �õ�һ��ҳ��Ľ���� getSubBranchAll(brhid);
 * @return List
 * @throws Exception 
 */
public List getFirstList() throws Exception {
		List list = new ArrayList();
		List emnnulist =fcsortquery.getEmnulist();
		int sum1=0;
		int sum2=0;
		double sum3=0.00;
		double sum4=0.00;
		for (int i = 0; i < emnnulist.size(); i++) {
			FirstTr tr = new FirstTr();
			FcsortQueryPathEmnu emnu =(FcsortQueryPathEmnu)emnnulist.get(i);
			tr.setEnutp(Integer.valueOf(emnu.getEnutp()).intValue());//1- 12 
			tr.setEnudt(emnu.getEnudt());
			String sqltabname="";
			if(this.queryid.trim().equals("DTRANSFER")){//����
				if(((8-i==1)||(11-i==1)))
				{
					tr.setTotalA(sum1);
					tr.setTotalB(sum2);
					tr.setBalA(sum3);
					tr.setBalB(sum4);
					sum1=sum2=0;
					sum3=sum4=0;
					list.add(tr);
					continue;
				}
				sqltabname=i==0?"FCMAIN":"FCBMAIN";	
			}
			else //����
			{
				if((5-i==1)||(9-i==1)||(12-i==1))
				{
					tr.setTotalA(sum1);
					tr.setTotalB(sum2);
					tr.setBalA(sum3);
					tr.setBalB(sum4);
					sum1=sum2=0;
					sum3=sum4=0;
					list.add(tr);
					continue;
				}
				sqltabname=((i==2||i==3||i==7)?"FCBMAIN":"FCMAIN");	
			}
			String sql = this.getFristsqlForFcBmain(this.brhid,sqltabname) +" and  "+emnu.getSqlpart()+" ";
			CachedRowSet crs = manager.getRs(sql);
			while (crs.next()) {
				tr.setTotalA(crs.getInt(1));
				sum1+=tr.getTotalA();
				sumA+=tr.getTotalA();
				tr.setTotalB(crs.getInt(2));
				sum2+=tr.getTotalB();
				sumB+=tr.getTotalB();
				tr.setBalA(crs.getDouble("contractamt")/10000);
				sum3+=tr.getBalA();
				sumE+=tr.getBalA();
				tr.setBalB(crs.getDouble(3)/10000);
				sum4+=tr.getBalB();
				sumF+=tr.getBalB();
			}
			list.add(tr);
		}
		return list;
	}


 
/**
 * ���ö���ҳ��ļ�¼�� 
 */
public void setPagecount() {
	int sum =0;
	try {
		String sqltabname="";
		int sqltab=Integer.valueOf(this.sqlpartname.trim()).intValue();//equals("1")
		if(this.queryid.trim().equals("DTRANSFER")){//����
			sqltabname=sqltab==1?"FCMAIN":"FCBMAIN";
		
		}
		else
		{
			sqltabname=((sqltab==3||sqltab==4||sqltab==8)?"FCBMAIN":"FCMAIN");	
		}
		
		StringBuffer sql  = new StringBuffer("");
		sql.append("select rownumber() over() rn,  fc.bmno,fc.clientname,");
    	sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=(select  LOANCAT3 from BMTABLEAPP where bmno=FC.bmno)), ");
    	sql.append("(select perimon from RQLOANLEDGER where bmno=FC.bmno),");
    	sql.append("(select crtrate from RQLOANLEDGER where bmno=FC.bmno),");
    	sql.append("rqloanlist.contractamt,   ");
    	sql.append("fc.bal,fc.paydate,fc.duedate,");
		sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=FC.loancat2) loancat2,");
		sql.append("(select username from scuser where loginname=FC.firstresp),");
		sql.append("(select enudt from PTENUMINFODETL where enuid='BadReason' and enutp=FC.badreason) ");
		
		sql.append("from "+sqltabname+"  FC ,rqloanlist where     FC.brhid in "+util.formatBrhids(SCBranch.getSubBranchAll(this.brhid))+"  and  ");
		sql.append(" rqloanlist.CLIENTMGR like'%"+this.clientmgr+"%' and ");
		sql.append(" rqloanlist.bmno=FC.bmno  and");
		sql.append(this.getSqlBixuan());
		sql.append(" "+this.sqlpart+"   ");
		CachedRowSet crs =  manager.getRs(sql.toString());
		sum=crs.size();
	} catch (Exception e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
	}
	
	this.pagecount = sum;
	
}
/**
 * �õ�����ҳ��Ľ����
 */
public List getThirdList()
{
	List list = new ArrayList();
	try {
		String sqltabname="";
		int sqltab=Integer.valueOf(this.sqlpartname.trim()).intValue();//equals("1")
		if(this.queryid.trim().equals("DTRANSFER")){//����
			sqltabname=sqltab==1?"FCMAIN":"FCBMAIN";
		
		}
		else
		{
			sqltabname=((sqltab==3||sqltab==4||sqltab==8)?"FCBMAIN":"FCMAIN");	
		}
		StringBuffer sql  = new StringBuffer("");
		sql.append("select * from( ");
		sql.append("select rownumber() over() rn, fc.bmno,fc.clientname,");
    	sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=(select  LOANCAT3 from BMTABLEAPP where bmno=FC.bmno)), ");
    	sql.append("(select perimon from RQLOANLEDGER where bmno=FC.bmno),");
    	sql.append("(select crtrate from RQLOANLEDGER where bmno=FC.bmno),");
    	sql.append("fc.bal,fc.paydate,fc.duedate,");
		sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=FC.loancat2) loancat2,");
		sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat1' and enutp=FC.FCCLASS) LOANCAT1,");
		sql.append("(select username from scuser where loginname=FC.firstresp),");
		sql.append("(select enudt from PTENUMINFODETL where enuid='BadReason' and enutp=FC.badreason), ");
		sql.append("rqloanlist.contractamt   ");
		sql.append("from "+sqltabname+"  FC ,rqloanlist where     FC.brhid in "+util.formatBrhids(SCBranch.getSubBranchAll(this.brhid))+"  and  ");
		sql.append(" rqloanlist.CLIENTMGR like'%"+this.clientmgr+"%' and ");
		sql.append(" rqloanlist.bmno=FC.bmno  and");
		sql.append(this.getSqlBixuan());
		sql.append(" "+this.sqlpart+"   ");
		sql.append(" ) AS A1 WHERE A1.rn BETWEEN " + this.getStartrow()+" AND "+ this.getEndrow()+" ");
		CachedRowSet tcrs =  manager.getRs(sql.toString());
		while(tcrs.next())
		{
			ThirdTr tr = new ThirdTr();
			
			tr.setTd0(tcrs.getString(2)==null?"":tcrs.getString(2));
			tr.setTd1(tcrs.getString(3)==null?"":tcrs.getString(3));
			tr.setTd2(tcrs.getString(4)==null?"":tcrs.getString(4));
			tr.setTd3(tcrs.getString(5)==null?"":tcrs.getString(5));
			tr.setTd4(dfB.format(tcrs.getDouble(6)));
			tr.setTd5(df.format(tcrs.getDouble(7)));
			tr.setContractamt(df.format(tcrs.getDouble("contractamt")));
			sumD+=tcrs.getDouble("contractamt");
			sumE+=tcrs.getDouble(7);
			tr.setTd6(tcrs.getString(8));
			tr.setTd7(tcrs.getString(9));
			tr.setTdF(tcrs.getString(10));
			tr.setTd8(tcrs.getString(11));
			tr.setTd9(tcrs.getString(12));
			tr.setTd10(tcrs.getString(13)==null?"":tcrs.getString(13));
			list.add(tr);
		}
		
		
		
	} catch (Exception e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
	}
	return list;
}
/**
 * ��ѡ��ѯ��� ��ѯʱ�� ������� ���� ���� for Fcmain
 */
public String getSqlBixuan()
{
	StringBuffer sqlpart2 = new StringBuffer("");
	sqlpart2.append(" fc.bmtype not in(12,13,14) and ");
	if ((startdate== null ||startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
	{
		sqlpart2.append("FC.paydate <= '" + enddate.trim()+ "'  and ");
	}
	else if ((startdate!= null && !startdate.equals(""))&&(enddate== null || enddate.equals(""))) 
	{
		sqlpart2.append("FC.paydate >= '" + startdate.trim()+ "' and ");
	}
	else if((startdate!= null && !startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
	{
		sqlpart2.append("FC.paydate between '" + startdate.trim()+ "' and '"+enddate.trim()+"' and ");
	}
	
	
	if (this.creadate!= null && !creadate.equals("")) {
		sqlpart2.append("FC.CREATEDATE='" +creadate.trim()+ "' and  ");
	}
	if (this.sartbal != null && !sartbal.equals("")) {
		sqlpart2.append("FC.bal >="+ Double.valueOf(sartbal.trim()).doubleValue()* (10000) + " and  ");
	}
	if (this.endbal!= null && !this.endbal.equals("")) {
		sqlpart2.append("FC.bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and ");
	}
	return  sqlpart2.toString();
}
/**
 * �õ���ʾ�ı���
 * @return
 */
public String getTitle()
{
	StringBuffer str = new StringBuffer("");

	str.append(this.mory.trim().equals("1")?"����":"����");
	str.append(fcsortquery.getQueryname().trim());
	str.append(this.getMoeltitle());
	str.append("ͳ�Ʊ�");
	return str.toString();
}
//************ҳ����ʾ****************
public String getMTitle()
{
	String str = "";
	
	if ((startdate!= null && !startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
	{
		str="������գ�"+startdate+"&nbsp;��&nbsp;"+enddate;
	}
	else if ((startdate== null || startdate.equals(""))&&(enddate!= null && !enddate.equals("")))
	{
		str="������գ�"+enddate+"��ǰ";
	}
	else if((startdate!= null && !startdate.equals(""))&&(enddate== null || enddate.equals("")))
	{
		str="������գ�"+startdate+"�Ժ�";
	}
	else{
		str="������գ�ȫʱ���";
	}
	return str;
}

/**
 * �õ�ÿ�����һ��
 * @param i
 * @return String
 */
public static String getMonthLastDay(int i) {  
	Calendar calendar = Calendar.getInstance();    
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));   
    DateFormat   df=new   SimpleDateFormat("yyyy-MM-dd");
    String m1 = df.format(calendar.getTime());
    switch (i) {
	case 1:
		return m1;
		
	

	default:
		return "2008-08-08";
	}
      
}  
/**
 * ���ò�ѯģ����ʾ��������
 */
private void setTitle(){
	if((sartbal==null||sartbal.equals(""))&&(endbal==null||endbal.equals("")))
	{
		this.setMoeltitle("");
		this.setSartbal("");
		this.setEndbal("");
		
	}
	else if((sartbal==null||sartbal.equals(""))&&(endbal!=null&&!endbal.equals("")))
	{
		this.setMoeltitle(endbal+"��Ԫ����");
		this.setSartbal("");
	}
	else if((sartbal!=null&&!sartbal.equals(""))&&(endbal==null||endbal.equals("")))
	{
		this.setMoeltitle(sartbal+"��Ԫ����");
		this.setEndbal("");
	}
	else
	{
		this.setMoeltitle(sartbal+"��Ԫ---"+endbal+"��Ԫ");
	}
}

	public int getCurrpage() {		
		return currpage > getMaxpage() ? getMaxpage() : currpage;
	}
	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}
	public int getMaxpage() {
		return maxpage == 0 ? 1 : this.maxpage;
	}
	public void setMaxpage() {
		this.maxpage = pagecount / this.pagesize == 0 ? 1 : (pagecount + pagesize - 1)/ pagesize;
	}
	public int getPagecount() {
		return pagecount;
	}

	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getEndrow() {
		return this.currpage >= this.maxpage ? this.pagecount:this.currpage * this.pagesize;
	}
	public int getStartrow() {
		return (this.currpage - 1) * this.pagesize+1;
	}
	public String getLefttitle() {
		if(this.queryid.trim().equals("DTRANSFER"))
		{
			return this.chooseEnudtD(Integer.valueOf(this.sqlpartname).intValue());
		}
		else
		{
			return this.chooseEnudtU(Integer.valueOf(this.sqlpartname).intValue());
		}
		
	}
	/**
	 * ����  lefttitle
	 * @param lefttitle
	 */
	public void setLefttitle(String lefttitle) {
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
		map.put("DTRANSFER", this.setDTransfer());
		map.put("UTRANSFER", this.setUTransfer());
		request.getSession().setAttribute("FCSORT_TRANSFER_PATH_MAP", map);
		
		return true;
	}
	/**
	 * �����弶��������Ǩ�������ѯģ�����
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setDTransfer() {
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("DTRANSFER");
		query.setQueryname("�弶��������Ǩ�������ѯ");
		
		
		for(int i=1;i<=12;i++)
		{
			FcsortQueryPathEmnu emnu = new FcsortQueryPathEmnu();
			emnu.setEnuid("DTRANSFER");
			emnu.setEnutp(String.valueOf(i));
			emnu.setEnudt(this.chooseEnudtD(i));
			emnu.setSqlpart(this.chooseSqlpartD(i));
			query.setType(0);
			query.addEmnu(emnu);
		}
		return  query;
	}
	/**
	 * ����Ǩ��ѡ��Enudt
	 */
	public String chooseEnudtD(int i)
	{
		
		
		switch (i) {
		case 1:
			return "��������ע";
		case 2:
			return "�������μ�";
		case 3:
			return "����������";
		case 4:
			return "��������ʧ";
		case 5:
			return "��ע���μ�";
		case 6:
			return "��ע������";
		case 7:
			return "��ע����ʧ";
		case 8:
			return "������������Ǩ��ϼ�";
		case 9:
			return "�μ�������";
		case 10:
			return "�μ�����ʧ";
		case 11:
			return "�μ���������Ǩ��ϼ�";
		case 12:
			return "���ɵ���ʧ";
		default:
			return " 1=1 ";
		}
		
	}
	/**
	 * ����ѡ��Sqlpart
	 */
	public String chooseSqlpartD(int i)
	{
		String my ="LMFCCLASS";
		if(this.mory.equals("1"))
		{
			my="LYFCCLASS";
		}

		
		
		switch (i) {
		case 1:
			//return "��������ע";
			return " FC.FCCLASS=2 AND FC."+my+" = 1 ";
		case 2:
			//return "�������μ�";
			return " FC.FCCLASS=3 AND FC."+my+" = 1  ";
		case 3:
			//return "����������";
			return " FC.FCCLASS=4 AND FC."+my+" = 1  ";
		case 4:
			//return "��������ʧ";
			return " FC.FCCLASS=5 AND FC."+my+" = 1  ";
		case 5:
			//return "��ע���μ�";
			return " FC.FCCLASS=3 AND FC."+my+" = 2  ";
		case 6:
			//return "��ע������";
			return " FC.FCCLASS=4 AND FC."+my+" = 2  ";
		case 7:
			//return "��ע����ʧ";
			return " FC.FCCLASS=5 AND FC."+my+" = 2 ";
		case 8:
			//return "������������Ǩ��ϼ�";
			return "sumZD";
		case 9:
			//return "�μ�������";
			return " FC.FCCLASS=4 AND FC."+my+" = 3  ";
		case 10:
			//return "�μ�����ʧ";
			return " FC.FCCLASS=5 AND FC."+my+" = 3  ";
		case 11:
			//return "�μ���������Ǩ��ϼ�";
			return "sumCD";
		case 12:
			//return "���ɵ���ʧ";
			return " FC.FCCLASS=5 AND FC."+my+" = 4  ";
		default:
			return " 1=1 ";
		}
		
	}
	/**
	 * �弶��������Ǩ�������ѯ
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setUTransfer() {
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("UTRANSFER");
		query.setQueryname("�弶��������Ǩ�������ѯ");
		
		
		for(int i=1;i<=13;i++)
		{
			FcsortQueryPathEmnu emnu = new FcsortQueryPathEmnu();
			emnu.setEnuid("UTRANSFER");
			emnu.setEnutp(String.valueOf(i));
			emnu.setEnudt(this.chooseEnudtU(i));
			emnu.setSqlpart(this.chooseSqlpartU(i));
			query.setType(0);
			query.addEmnu(emnu);
		}
		return  query;
	}
	/**
	 * ����Ǩ��ѡ��Enudt
	 */
	public String chooseEnudtU(int i)
	{
		switch (i) {
		case 1:
			return "��ʧ������";
		case 2:
			return "��ʧ����ע";
		case 3:
			return "��ʧ���μ�";
		case 4:
			return "��ʧ������";
		case 5:
			return "��ʧ��������Ǩ��ϼ�";
		case 6:
			return "���ɵ�����";
		case 7:
			return "���ɵ���ע";
		case 8:
			return "���ɵ��μ�";
		case 9:
			return "���ɴ�������Ǩ��ϼ�";
		case 10:
			return "�μ�������";
		case 11:
			return "�μ�����ע";
		case 12:
			return "�μ���������Ǩ��ϼ�";
		case 13:
			return "��ע������";
		default:
			return " 1=1 ";
		}
		
	}
	/**
	 * ����ѡ��Sqlpart
	 */
	public String chooseSqlpartU(int i)
	{
		String my ="LMFCCLASS";
		if(this.mory.equals("1"))
		{
			my="LYFCCLASS";
		}
		switch (i) {
		case 1:
			//return "��ʧ������"; fcmain
			return " FC.FCCLASS=1 AND FC."+my+" = 5 ";
		case 2:
			//return "��ʧ����ע"; fcmain
			return " FC.FCCLASS=2 AND FC."+my+" = 5  ";
		case 3:
			//return "��ʧ���μ�"; fcbmain
			return " FC.FCCLASS=3 AND FC."+my+" = 5 ";
		case 4:
			//return "��ʧ������"; fcmbmain
			return " FC.FCCLASS=4 AND FC."+my+" = 5  ";
		case 5:
			//return "��ʧ��������Ǩ��ϼ�";
			return "USUMSD";
		case 6:
			//return "���ɵ�����"; fcmain
			return " FC.FCCLASS=1 AND FC."+my+" = 4  ";
		case 7:
			//return "���ɵ���ע"; fcmain
			return " FC.FCCLASS=2 AND FC."+my+" = 4  ";
		case 8:
			//return "���ɵ��μ�"; fcbmain
			return " FC.FCCLASS=3 AND FC."+my+" = 4  ";
		case 9:
			//return "���ɴ�������Ǩ��ϼ�";
			return "USUMKD";
		case 10:
			//return "�μ�������"; fcmain
			return " FC.FCCLASS=1 AND FC."+my+" = 3  ";
		case 11:
			//return "�μ�����ע"; fcmain
			return " FC.FCCLASS=2 AND FC."+my+" = 3  ";
		case 12:
			//return "�μ���������Ǩ��ϼ�";
			return "USUMCD";
		case 13:
			//return "��ע������"; fcmain
			return " FC.FCCLASS=1 AND FC."+my+" = 2  ";
		default:
			return " 1=1 ";
		}
		
		
	}
	public String getClientmgr() {
		return clientmgr;
	}
	public void setClientmgr(String clientmgr) {
		this.clientmgr = clientmgr;
	}
	public String getFirstresp() {
		return firstresp;
	}
	public void setFirstresp(String firstresp) {
		this.firstresp = firstresp;
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
	 * @return endbal
	 */
	public String getEndbal() {
		return endbal;
	}
	/**
	 * ���� endbal
	 * @param endbal 
	 */
	public void setEndbal(String endbal) {
		this.endbal = endbal;
	}
	/**
	 * @return enddate
	 */
	public String getEnddate() {
		return enddate;
	}
	/**
	 * ���� enddate
	 * @param enddate 
	 */
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	/**
	 * @return fcsortquery
	 */
	public FcsortQueryPath getFcsortquery() {
		return fcsortquery;
	}
	/**
	 * ���� fcsortquery
	 * @param fcsortquery 
	 */
	public void setFcsortquery(FcsortQueryPath fcsortquery) {
		this.fcsortquery = fcsortquery;
	}
	/**
	 * @return fcsortQueryPathMap
	 */
	public HashMap getFcsortQueryPathMap() {
		return fcsortQueryPathMap;
	}
	/**
	 * ���� fcsortQueryPathMap
	 * @param fcsortQueryPathMap 
	 */
	public void setFcsortQueryPathMap(HashMap fcsortQueryPathMap) {
		this.fcsortQueryPathMap = fcsortQueryPathMap;
	}
	/**
	 * @return moeltitle
	 */
	public String getMoeltitle() {
		return moeltitle;
	}
	/**
	 * ���� moeltitle
	 * @param moeltitle 
	 */
	public void setMoeltitle(String moeltitle) {
		this.moeltitle = moeltitle;
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
	 * @return sartbal
	 */
	public String getSartbal() {
		return sartbal;
	}
	/**
	 * ���� sartbal
	 * @param sartbal 
	 */
	public void setSartbal(String sartbal) {
		this.sartbal = sartbal;
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
	 * @return sqlpart
	 */
	public String getSqlpart() {
		return sqlpart;
	}
	/**
	 * ���� sqlpart
	 * @param sqlpart 
	 */
	public void setSqlpart(String sqlpart) {
		this.sqlpart = sqlpart;
	}
	/**
	 * @return sqlpartname
	 */
	public String getSqlpartname() {
		return sqlpartname;
	}
	/**
	 * ���� sqlpartname
	 * @param sqlpartname 
	 */
	public void setSqlpartname(String sqlpartname) {
		this.sqlpartname = sqlpartname;
	}
	/**
	 * @return sqlpartvalue
	 */
	public String getSqlpartvalue() {
		return sqlpartvalue;
	}
	/**
	 * ���� sqlpartvalue
	 * @param sqlpartvalue 
	 */
	public void setSqlpartvalue(String sqlpartvalue) {
		this.sqlpartvalue = sqlpartvalue;
	}
	/**
	 * @return startdate
	 */
	public String getStartdate() {
		return startdate;
	}
	/**
	 * ���� startdate
	 * @param startdate 
	 */
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	/**
	 * @return stype
	 */
	public String getStype() {
		return stype;
	}
	/**
	 * ���� stype
	 * @param stype 
	 */
	public void setStype(String stype) {
		this.stype = stype;
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
	public int getSumB() {
		return sumB;
	}
	/**
	 * ���� sumB
	 * @param sumB 
	 */
	public void setSumB(int sumB) {
		this.sumB = sumB;
	}
	/**
	 * @return sumC
	 */
	public double getSumC() {
		return sumC;
	}
	/**
	 * ���� sumC
	 * @param sumC 
	 */
	public void setSumC(double sumC) {
		this.sumC = sumC;
	}
	/**
	 * @return sumD
	 */
	public double getSumD() {
		return sumD;
	}
	/**
	 * ���� sumD
	 * @param sumD 
	 */
	public void setSumD(double sumD) {
		this.sumD = sumD;
	}
	/**
	 * @return sumE
	 */
	public double getSumE() {
		return sumE;
	}
	/**
	 * ���� sumE
	 * @param sumE 
	 */
	public void setSumE(double sumE) {
		this.sumE = sumE;
	}
	/**
	 * @return sumF
	 */
	public double getSumF() {
		return sumF;
	}
	/**
	 * ���� sumF
	 * @param sumF 
	 */
	public void setSumF(double sumF) {
		this.sumF = sumF;
	}
	/**
	 * ���� maxpage
	 * @param maxpage 
	 */
	public void setMaxpage(int maxpage) {
		this.maxpage = maxpage;
	}
	/**
	 * ���� pagecount
	 * @param pagecount 
	 */
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
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
