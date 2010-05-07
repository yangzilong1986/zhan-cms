package zt.cms.fcsort.swear;

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
import zt.platform.db.DBUtil;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
/**
 * <p/>=============================================== 
 * <p/>Title: �弶���಻���ʲ�̨�ʲ�ѯҳ��BEAN 
 * <p/>===============================================
 * <p/>Description: Ϊǰ̨ҳ���ṩ��ѯ����Ҫ�����ݺͷ�ҳ��ز�����
 * 
 * @version $Revision: 1.5 $ $Date: 2007/06/19 00:43:07 $
 * @author zhengxin <p/>�޸ģ�$Author: zhengx $ SelectDtel
 */

public class SwearPageobject  {
	private int sumA,sumB;
	private double sumC,sumD,sumE,sumF,sumG;// �����ͺϼ���
	private String brhid;// ������
	private String scbrhname;//��������
	private String creadate;// ���ʱ��
	private String startdate;// ��ʼʱ��
	private String enddate;// ����ʱ��
	private String sartbal;// ʱ�㿪ʼ���
	private String endbal;// ʱ��������
	private String stype;// 1 ����  2 ����
	private String clientmgr;//CLIENTMGR �ͻ�����
	private String firstresp;//��һ������
	private String moeltitle;//�б���
	
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
	private String thirlasttr;
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat dfB=new DecimalFormat("###,###,###,##0.0000");
	FcsortUtil util;
	/**
	 * ���ò�ѯ�����͹���ҳ������
	 * 
	 * @param request
	 * @param response
	 */
	public SwearPageobject() {
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
	
		
		this.brhid =request.getParameter("brhid");//�õ�������
		this.creadate=request.getParameter("creadate")==null?util.getDT():request.getParameter("creadate").trim();// ���ʱ��
		
		/*
		this.startdate=request.getParameter("startdate")==null?"1999-01-01":request.getParameter("startdate");// ��ʼʱ��
		this.enddate=request.getParameter("enddate")==null?getMonthLastDay(1):request.getParameter("enddate").trim();// ����ʱ��
		*/
		
		this.startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");// ��ʼʱ��
		this.enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate").trim();// ����ʱ��
		
		this.stype=request.getParameter("stype")==null?"2":request.getParameter("stype");
		this.sqlpartname=request.getParameter("sqlpartname")==null?"1":request.getParameter("sqlpartname").trim();
		this.sqlpartvalue=request.getParameter("sqlpartvalue")==null?"1":request.getParameter("sqlpartvalue").trim();
		this.clientmgr=request.getParameter("CLIENTMGR")==null?"":request.getParameter("CLIENTMGR").trim();//�ͻ�����
		this.firstresp=request.getParameter("FIRSTRESP")==null?"":request.getParameter("FIRSTRESP").trim();//��һ������
		if(sqlpartname.trim().equals("LoanWay"))
		{
			if(this.sqlpartvalue.startsWith("LOANWAY"))
			{
				if(this.sqlpartvalue.equals("LOANWAY199"))
				{
					this.sqlpart=" FCBMAIN."+sqlpartname.trim()+" between '101' and '199'  ";
				}
				if(this.sqlpartvalue.equals("LOANWAY299"))
				{
					this.sqlpart=" FCBMAIN."+sqlpartname.trim()+" between '201' and '299'  ";
				}
				if(this.sqlpartvalue.equals("LOANWAY399"))
				{
					this.sqlpart=" FCBMAIN."+sqlpartname.trim()+" between '301' and '399'  ";
				}
			}
			else
			{
				this.sqlpart=" FCBMAIN."+sqlpartname.trim()+"='"+sqlpartvalue+"' ";
			}
			
		}
		else if (sqlpartname.trim().equals("BYTERM"))
		{
			switch (Integer.valueOf(this.sqlpartvalue).intValue()) {
			case 1:
				this.sqlpart=" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  <= 12 ";
				break;
			case 2:
				this.sqlpart=" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  > 12 and (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  <36  ";
				break;
			case 3:
				this.sqlpart=" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  >= 36 ";
				break;
			default:
				this.sqlpart=" 1=1 ";
				break;
			}
		}
		else
		{
			this.sqlpart=" FCBMAIN."+sqlpartname.trim()+"="+sqlpartvalue+" ";
		}
		
		if(this.sqlpartname.trim().equals("sum"))
		{
			this.sqlpart=" 1=1 ";
		}
		if(this.brhid==null)
		{
				UserManager um = (UserManager)request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME);
				String strUserName = um.getUserName(); 
				this.brhid = SCUser.getBrhId(strUserName).trim();
		}
		//Session ��ȡ�� hashmap - FcsortQueryPath 
		fcsortQueryPathMap =(HashMap)request.getSession().getAttribute("FCSORT_QUERY_PATH_MAP");
		fcsortquery = (FcsortQueryPath)fcsortQueryPathMap.get(queryid);
		this.scbrhname = SCBranch.getSName(brhid);
		this.sartbal=request.getParameter("sartbal");// ʱ�㿪ʼ���
		this.endbal=request.getParameter("endbal");// ʱ��������
		this.setTitle();
		
		//************��ҳ���*******************
		this.pagesize=Integer.valueOf(request.getParameter("pagesize")==null?"10":request.getParameter("pagesize")).intValue();
		this.currpage=Integer.valueOf(request.getParameter("currpage")==null?"1":request.getParameter("currpage")).intValue();
		
		
		return true;
	}
	
	/**
	 * һ��ҳ��ĵ�һ����ѯSQL
	 * @return String
	 */
	private String getSqlPart1(int i){
		switch (i) {
		case 1:
			return "select brhid,sname,brhtype from scbranch where BRHLEVEL >1 and upbrh='"+ this.getBrhid() + "'";
			
		case 2:
			return "select brhid,sname,brhtype from scbranch where BRHLEVEL >1 and brhid='"+ this.getBrhid() + "'";
		
		}
		return "";
		
		
	}
	/**
	 * һ��ҳ��ĵڶ�����ѯSQL
	 */
	private String getSqlPart2(String onebrhid)
	{
		String brhids = this.getString(SCBranch.getAllSubBrh1(onebrhid));
		if (brhids == null || brhids.equals("")) {
			brhids = "('" +onebrhid + "')";
		} else {
			brhids = "('" + brhids + "')";
		}
		StringBuffer sqlbn = new StringBuffer(
		"select count(fcbmain.bmno),count(distinct(fcbmain.idno)),sum(contractamt) contractamt ,sum(fcbmain.bal) fcbal,sum(case when fcbmain.FCCLASS=3 then bal else 0 end ) h3,");
		sqlbn.append(" sum(case when fcbmain.FCCLASS=4 then bal else 0 end ) h4,sum(case when fcbmain.FCCLASS=5 then bal else 0 end ) h5");
		sqlbn.append(" from fcbmain , rqloanlist r where fcbmain.bmno=r.bmno and  ");
		sqlbn.append(getSqlBixuan());
		sqlbn.append("  and fcbmain.brhid  in "+ brhids);
		
		return sqlbn.toString();
	}
/**
 * �õ�һ��ҳ��Ľ����
 * @return List
 */
public List getFirstList() {
		List list = new ArrayList();
		try {
			CachedRowSet crs = manager.getRs(this.getSqlPart1(1));
			if(crs.size()<=0)
			{	
				crs=manager.getRs(this.getSqlPart1(2));
			}
			while (crs.next()) {
				First obj = new First();
				obj.setBrhid(Integer.valueOf(crs.getString(1).trim()).intValue());
				obj.setSname(DBUtil.fromDB(crs.getString(2).trim()));
				obj.setBrhtype(crs.getString(3).trim());
				CachedRowSet child = manager.getRs(this.getSqlPart2(String.valueOf(obj.getBrhid())));
				while (child.next()) {
					 int totalA=child.getInt(1);//����
					 obj.setTotalA(totalA);
					 sumA+=totalA;
					 int totalB=child.getInt(2);//����
					 obj.setTotalB(totalB);
					 sumB+=totalB;
					 
					 double balA=Double.valueOf(child.getString(3)==null?"0":child.getString(3)).doubleValue()/10000;//��Ƿ���
					 obj.setBalA(balA);
					 sumG+=balA;
					 double balB=Double.valueOf(child.getString(4)==null?"0":child.getString(4)).doubleValue()/10000;//��Ƿ���
					 obj.setBalB(balB);
					
					 sumC+=balB;
					 double h3=Double.valueOf(child.getString(5)==null?"0":child.getString(5)).doubleValue()/10000;//��Ƿ���-�μ�
					 obj.setH3(h3);
					 sumD+=h3;
					 double h4=Double.valueOf(child.getString(6)==null?"0":child.getString(6)).doubleValue()/10000;//��Ƿ���-����
					 obj.setH4(h4);
					 sumE+=h4;
					 double h5=Double.valueOf(child.getString(7)==null?"0":child.getString(7)).doubleValue()/10000;//��Ƿ���-��ʧ
					 obj.setH5(h5);
					 sumF+=h5;
				}
				list.add(obj);

			}
			
			
		} catch (RuntimeException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return list;
	}


 
/**
 * ����ҳ���ѯ�б�
 * @return List
 * @throws Exception 
 */
public List getSecondList() throws Exception
{
	List secondList = new ArrayList();
	List emnnulist =fcsortquery.getEmnulist();
	
	String brhids = this.getString(SCBranch.getAllSubBrh1(this.getBrhid()));
	if (brhids == null || brhids.equals("")) {
		brhids = "('" +this.getBrhid() + "')";
	} else {
		brhids = "('" + brhids + "')";
	}
	for (int i = 0; i < emnnulist.size(); i++) {
		
		FcsortQueryPathEmnu emnu =(FcsortQueryPathEmnu)emnnulist.get(i);
		StringBuffer sql =  new StringBuffer(" select count(fcbmain.bmno),count(distinct(idno)),sum(bal), ");
		sql.append(" sum(case when FCCLASS=3 then bal else 0 end ) h3, ");
		sql.append(" sum(case when FCCLASS=4 then bal else 0 end ) h4, ");
		sql.append(" sum(case when FCCLASS=5 then bal else 0 end ) h5, ");
		sql.append(" sum(contractamt) contractamt ");
		sql.append(" from FCBMAIN ,rqloanlist where rqloanlist.bmno=fcbmain.bmno  and "+emnu.getSqlpart().trim()+" ");
		sql.append(" and FCBMAIN.brhid in "+brhids+" and ");
		sql.append(this.getSqlBixuan());
		sql.append("and  rqloanlist.CLIENTMGR like '%"+this.clientmgr+"%' ");
		CachedRowSet secondcrs =  manager.getRs(sql.toString());
		SecondTR tr = new SecondTR();
		tr.setSqlpartname(emnu.getEnuid());
		tr.setSqlpartvalue(emnu.getEnutp());
		tr.setTd1(emnu.getEnudt());
		while (secondcrs.next()) {
			tr.setTd2(secondcrs.getString(1));
			sumA+=secondcrs.getInt(1);
			tr.setTd3(secondcrs.getString(2));
			sumB+=secondcrs.getInt(2);
			tr.setTd4(String.valueOf(secondcrs.getDouble(3)/10000));
			sumC+=secondcrs.getDouble(3)/10000;
			tr.setTd5(String.valueOf(secondcrs.getDouble(4)/10000));
			sumD+=secondcrs.getDouble(4)/10000;
			tr.setTd6(String.valueOf(secondcrs.getDouble(5)/10000));
			sumE+=secondcrs.getDouble(5)/10000;
			tr.setTd7(String.valueOf(secondcrs.getDouble(6)/10000));
			sumF+=secondcrs.getDouble(6)/10000;
			tr.setTd8(String.valueOf(secondcrs.getDouble(7)/10000));
			sumG+=secondcrs.getDouble(7)/10000;
		}
		secondList.add(tr);
	}
	return secondList;
}
/**
 * ��������ҳ��ļ�¼�� 
 */
public void setPagecount() {
	int sum =0;
	try {
		String brhids = this.getString(SCBranch.getAllSubBrh1(this.getBrhid()));
		if (brhids == null || brhids.equals("")) {
			brhids = "('" +this.getBrhid() + "')";
		} else {
			brhids = "('" + brhids + "')";
		}
		StringBuffer sql  = new StringBuffer("");
		sql.append("select rownumber() over() rn, fcbmain.bmno,(select SNAME from SCBRANCH where brhid=fcbmain.brhid) , fcbmain.clientname,");
    	sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=(select  LOANCAT3 from BMTABLEAPP where bmno=FCBMAIN.bmno)), ");
    	sql.append("(select perimon from RQLOANLEDGER where bmno=FCBMAIN.bmno),");
    	sql.append("(select crtrate from RQLOANLEDGER where bmno=FCBMAIN.bmno),");
    	sql.append("fcbmain.bal,fcbmain.paydate,fcbmain.duedate,");
		sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=FCBMAIN.loancat2) loancat2,");
		sql.append("(select username from scuser where loginname=FCBMAIN.firstresp),");
		sql.append("(select enudt from PTENUMINFODETL where enuid='BadReason' and enutp=FCBMAIN.badreason), ");
		sql.append("rqloanlist.contractamt   ");
		sql.append("from FCBMAIN,rqloanlist  where FCBMAIN.FCCLASS between 3 and 5   and FCBMAIN.brhid in "+brhids+"  and  ");
		sql.append(" rqloanlist.CLIENTMGR like'%"+this.clientmgr+"%' and ");
		sql.append(" FCBMAIN.firstresp like'%"+this.firstresp+"%' and ");
		sql.append(" rqloanlist.bmno=FCBMAIN.bmno  and"); 
		sql.append(" "+this.sqlpart+" and  ");
		sql.append(this.getSqlBixuan());
		CachedRowSet crs =  manager.getRs(sql.toString());
		sum=crs.size();
		if(this.queryid.trim().equals("FCBMAIN"))
		{
			this.thirlasttr="�γ�ԭ��";
		}
		if(this.queryid.trim().equals("BYVOUCH"))
		{
			
			this.thirlasttr="������ʽ";
		}
		if(this.queryid.trim().equals("BYWAY"))
		{
			
			this.thirlasttr="����Ͷ��";
		}
		if(this.queryid.trim().equals("BADREASON"))
		{
			this.thirlasttr="�γ�ԭ��";
		}
		if(this.queryid.trim().equals("BYTERM"))
		{
			this.thirlasttr="��������";
		}
	} catch (Exception e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
	}
	
	this.pagecount = sum;
}
/**
 * �õ�����ҳ��Ľ����
 * @return  List
 */
public List getThirdList()
{
	List list = new ArrayList();
	try {
		String brhids = this.getString(SCBranch.getAllSubBrh1(this.getBrhid()));
		if (brhids == null || brhids.equals("")) {
			brhids = "('" +this.getBrhid() + "')";
		} else {
			brhids = "('" + brhids + "')";
		}
		StringBuffer sql  = new StringBuffer("");
		sql.append("select * from( ");
		sql.append("select rownumber() over() rn,  fcbmain.bmno,(select SNAME from SCBRANCH where brhid=fcbmain.brhid),fcbmain.clientname,");
    	sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat3' and enutp=(select  LOANCAT3 from BMTABLEAPP where bmno=FCBMAIN.bmno)), ");
    	sql.append("(select perimon from RQLOANLEDGER where bmno=FCBMAIN.bmno),");
    	sql.append("(select crtrate from RQLOANLEDGER where bmno=FCBMAIN.bmno),");
    	
    	sql.append("fcbmain.bal,fcbmain.paydate,fcbmain.duedate,");
		sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat2' and enutp=FCBMAIN.loancat2) loancat2,");
		sql.append("(select enudt from PTENUMINFODETL where enuid='LoanCat1' and enutp=FCBMAIN.FCCLASS) LOANCAT1,");
		sql.append("(select username from scuser where loginname=FCBMAIN.firstresp),");
		sql.append(" rqloanlist.contractamt   ");
		if(this.queryid.trim().equals("FCBMAIN"))
		{
			sql.append(",(select enudt from PTENUMINFODETL where enuid='BadReason' and enutp=FCBMAIN.badreason) fenlei ");
		}
		if(this.queryid.trim().equals("BYVOUCH"))
		{
			
			sql.append(",(select ENUDT from PTENUMINFODETL where enuid ='LoanType3' and enutp=fcbmain.LoanType3)  fenlei ");
		}
		if(this.queryid.trim().equals("BYWAY"))
		{
			
			sql.append(",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=integer(fcbmain.LoanWay))  fenlei ");
		}
		if(this.queryid.trim().equals("BADREASON"))
		{
			sql.append(",(select ENUDT from PTENUMINFODETL where enuid ='BadReason' and enutp=fcbmain.BadReason)  fenlei ");
		}
		
		sql.append("from FCBMAIN,rqloanlist  where FCBMAIN.FCCLASS between 3 and 5   and FCBMAIN.brhid in"+brhids+"  and  ");
		sql.append(" rqloanlist.CLIENTMGR like'%"+this.clientmgr+"%' and ");
		sql.append(" FCBMAIN.firstresp like'%"+this.firstresp+"%' and ");
		sql.append(" rqloanlist.bmno=FCBMAIN.bmno  and"); 
		sql.append(" "+this.sqlpart+" and  ");
		sql.append(this.getSqlBixuan());
		sql.append("order by fcbmain.brhid,fcbmain.fcclass ");
		if(this.queryid.trim().equals("BYTERM"))
		{
			sql.append(",crtrate");
		}else
		{
			sql.append(",fenlei ");
		}
		sql.append("  ) AS A1 WHERE A1.rn BETWEEN " + this.getStartrow()+" AND "+ this.getEndrow()+" ");
		CachedRowSet tcrs =  manager.getRs(sql.toString());
		while(tcrs.next())
		{
			ThirdTr tr = new ThirdTr();
			
			tr.setTd0(tcrs.getString(2)==null?"":tcrs.getString(2));
			tr.setBrhidname(tcrs.getString("SNAME"));
			tr.setTd1(tcrs.getString(4)==null?"":tcrs.getString(4));
			tr.setTd2(tcrs.getString(5)==null?"":tcrs.getString(5));
			tr.setTd3(tcrs.getString(6)==null?"":tcrs.getString(6));
			tr.setTd4(dfB.format(tcrs.getDouble(7)));
			
			tr.setContractamt(df.format(tcrs.getDouble("contractamt")));
			sumD+=tcrs.getDouble("contractamt");
			tr.setTd5(df.format(tcrs.getDouble(8)));			
			sumE+=tcrs.getDouble(8);
			
			tr.setTd6(tcrs.getString(9));
			tr.setTd7(tcrs.getString(10));
			tr.setTdF(tcrs.getString(11));
			tr.setTd8(tcrs.getString(12));
			tr.setTd9(tcrs.getString(13));
			if(!this.queryid.trim().equals("BYTERM"))
			{
				tr.setTd10(tcrs.getString("fenlei")==null?"":tcrs.getString("fenlei"));
			}
			else
			{
				double d =Double.valueOf(tr.getTd3()).doubleValue()/12;
				//tr.setTd10(tr.getTd3()+"="+d);
				if(d<=1)
				{
					tr.setTd10(DBUtil.toDB("����"));
				}else if(d>1&&d<3)
				{
					tr.setTd10(DBUtil.toDB("����"));
				}
				else
				{
					tr.setTd10(DBUtil.toDB("����"));
				}
			}
			
		
			
			list.add(tr);
		}
		
		
		
	} catch (Exception e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
	}
	return list;
}
/**
 * �õ���ѡ��ѯ��� ��ѯʱ�� ������� ���� ����
 * @return  String
 */
public String getSqlBixuan()
{
	StringBuffer sqlpart2 = new StringBuffer(" FCBMAIN.bmtype not in(12,13,14) and ");
	if ((startdate== null ||startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
	{
		sqlpart2.append("FCBMAIN.paydate <= '" + enddate.trim()+ "'  and ");
	}
	else if ((startdate!= null && !startdate.equals(""))&&(enddate== null || enddate.equals(""))) 
	{
		sqlpart2.append("FCBMAIN.paydate >= '" + startdate.trim()+ "' and ");
	}
	else if((startdate!= null && !startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
	{
		sqlpart2.append("FCBMAIN.paydate between '" + startdate.trim()+ "' and '"+enddate.trim()+"' and ");
	}
	
	
	if (this.creadate!= null && !creadate.equals("")) {
		sqlpart2.append("FCBMAIN.CREATEDATE='" +creadate.trim()+ "' and  ");
	}
	if (this.sartbal != null && !sartbal.equals("")) {
		sqlpart2.append("FCBMAIN.bal >="+ Double.valueOf(sartbal.trim()).doubleValue()* (10000) + " and  ");
	}
	if (this.endbal!= null && !this.endbal.equals("")) {
		sqlpart2.append("FCBMAIN.bal <="+ Double.valueOf(this.getEndbal().trim()).doubleValue()* (10000) + " and  ");
	}
	sqlpart2.append(" FCBMAIN.FCCLASS between 3 and 5 ");
	
	/*if(this.stype!=null&&!this.stype.equals("")&&!this.queryid.endsWith("FCBMAIN"))
	{
		sqlpart2.append(this.stype.equals("1")?"and  FCBMAIN.LYFCCLASS < 2 ":" and  FCBMAIN.LMFCCLASS < 2");
	}*/
	
	return  sqlpart2.toString();
}
/**
 * �õ��б�ı���
 * @return String
 */
public String getTitle()
{
	StringBuffer str = new StringBuffer("");
	
	/*if(!this.queryid.endsWith("FCBMAIN"))
	{
		str.append(this.stype.equals("1")?"����":"����");
	}*/
	
	//System.out.println(fcsortquery.getQueryname().trim());
	
	str.append(fcsortquery.getQueryname().trim());
	str.append(this.getMoeltitle());
	str.append("ͳ�Ʊ�");
	return str.toString();
}
/**
 * �õ��б���б���
 * @return String
 */
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
 * �õ��ڼ�ҳ
 * @return int
 */
	public int getCurrpage() {		
		return currpage > getMaxpage() ? getMaxpage() : currpage;
	}
	/**
	 * ����ҳ��
	 * 
	 */
	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}
	/**
	 * �õ����ҳ��
	 * @return int
	 */
	public int getMaxpage() {
		return maxpage == 0 ? 1 : this.maxpage;
	}
	/**
	 * �������ҳ��
	 */
	public void setMaxpage() {
		this.maxpage = pagecount / this.pagesize == 0 ? 1 : (pagecount + pagesize - 1)/ pagesize;
	}
	/**
	 * �õ�ҳ�������
	 * @return int
	 */
	public int getPagecount() {
		return pagecount;
	}
	/**
	 * �õ�ҳ����ʾ����
	 * @return int
	 */
	public int getPagesize() {
		return pagesize;
	}
	/**
	 * ����ҳ����ʾ����
	 * @param  pagesize
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	/**
	 * �õ���ʼ����
	 * @return int
	 */
	public int getEndrow() {
		return this.currpage >= this.maxpage ? this.pagecount:this.currpage * this.pagesize;
	}
	/**
	 * �õ���������
	 * @return int
	 */
	public int getStartrow() {
		return (this.currpage - 1) * this.pagesize+1;
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
		map.put("FCBMAIN", this.setFcbmain());
		map.put("BYVOUCH", this.setByvouch());
		map.put("BYWAY", this.setByway());
		map.put("BADREASON", this.setBadReason());
		map.put("BYTERM", this.setByTerm());
		request.getSession().setAttribute("FCSORT_QUERY_PATH_MAP", map);
		
		return true;
	}
	/**
	 * ���ز�ѯö�����͵�SQlPART3
	 * @return String[]
	 */
	public String[] getEmnuSqlPart()
	{
		String[] sqlpart3= new String[2];
		if(this.queryid.trim().equals("FCBMAIN"))
		{
			sqlpart3[0]="";
			sqlpart3[1]=" ";
		}
		if(this.queryid.trim().equals("BYVOUCH"))
		{
			sqlpart3[0]="������ʽ";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanType3' and enutp=fcbmain.LoanType3) ";
		}
		if(this.queryid.trim().equals("BYWAY"))
		{
			sqlpart3[0]="����Ͷ��";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=integer(fcbmain.LoanWay)) ";
		}
		if(this.queryid.trim().equals("BADREASON"))
		{
			sqlpart3[0]="�γ�ԭ��";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=fcbmain.BadReason) ";
		}
		/*if(this.queryid.trim().equals("BYTERM"))
		{
			sqlpart3[0]="������ʽ";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=fcbmain.LoanWay) ";
		}*/
		return sqlpart3;
	}
	/**
	 * �����弶���಻���ʲ�ģ�����
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setFcbmain() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("FCBMAIN");
		query.setQueryname("�弶���಻���ʲ�");
		String sql="select ENUID, ENUTP,ENUDT from PTENUMINFODETL where enuid ='LoanCat1' and enutp>=3";
		CachedRowSet crss = manager.getRs(sql);
		while (crss.next()) {
			FcsortQueryPathEmnu emnu = new FcsortQueryPathEmnu();
			emnu.setEnuid("FCCLASS");
			emnu.setEnutp(crss.getString("ENUTP"));
			emnu.setEnudt(crss.getString("ENUDT"));	
			query.addEmnu(emnu);
		}
		query.setType(0);
		return  query;
	}
	
	/**
	 * �����弶���಻�����������ʽģ�����
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setByvouch() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("BYVOUCH");
		query.setQueryname("�弶���಻�����������ʽ");
		String sql="select ENUID, ENUTP,ENUDT from PTENUMINFODETL where enuid ='LoanType3'";
		CachedRowSet crss = manager.getRs(sql);
		while (crss.next()) {
			FcsortQueryPathEmnu emnu = new FcsortQueryPathEmnu();
			emnu.setEnuid(crss.getString(1));
			emnu.setEnutp(crss.getString(2));
			emnu.setEnudt(crss.getString(3));
			query.addEmnu(emnu);
		}

		query.setType(1);
		return  query;
	}
	/**
	 * �����弶���಻���������Ͷ��ģ�����
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setByway() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
	
		String sql="select ENUID, ENUTP,ENUDT from PTENUMINFODETL where enuid ='LoanWay'";
		CachedRowSet crss = manager.getRs(sql);
		query.setQueryid("BYWAY");
		query.setQueryname("�弶���಻���������Ͷ�� ");
		while (crss.next()) {
			FcsortQueryPathEmnu emnu = new FcsortQueryPathEmnu();
			emnu.setEnuid(crss.getString(1));
			emnu.setEnutp(crss.getString(2));
			emnu.setEnudt(crss.getString(3));	
			query.addEmnu(emnu);
		}
		query.setType(0);
		return  query;
	}
	/**
	 * �����弶���಻������γ�ԭ��ģ�����
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setBadReason() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("BADREASON");
		query.setQueryname("�弶���಻������γ�ԭ�� ");
		String sql="select ENUID, ENUTP,ENUDT from PTENUMINFODETL where enuid ='BadReason'";
		CachedRowSet crss = manager.getRs(sql);
		while (crss.next()) {
			FcsortQueryPathEmnu emnu = new FcsortQueryPathEmnu();
			emnu.setEnuid(crss.getString(1));
			emnu.setEnutp(crss.getString(2));
			emnu.setEnudt(crss.getString(3));
			query.addEmnu(emnu);
		}
		query.setType(0);
		return  query;
	}
	/**
	 * �����弶���಻�������������ģ�����
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setByTerm() 
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("BYTERM");
		query.setQueryname("�弶���಻������������� ");
		
			FcsortQueryPathEmnu emnua = new FcsortQueryPathEmnu();
			emnua.setEnuid("BYTERM");
			emnua.setEnudt(DBUtil.toDB("����"));
			
			emnua.setSqlpart(" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  <= 12  ");
			emnua.setEnutp("1");
			
			FcsortQueryPathEmnu emnub = new FcsortQueryPathEmnu();
			emnub.setEnuid("BYTERM");
			emnub.setEnudt(DBUtil.toDB("����"));
			emnub.setSqlpart(" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  > 12 and (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  <36  ");
			emnub.setEnutp("2");
			FcsortQueryPathEmnu emnuc = new FcsortQueryPathEmnu();
			emnuc.setEnuid("BYTERM");
			emnuc.setEnudt(DBUtil.toDB("����"));
			emnuc.setEnutp("3");
			emnuc.setSqlpart(" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  >= 36  ");
		
			
			query.addEmnu(emnua);
			query.addEmnu(emnub);
			query.addEmnu(emnuc);
			
		query.setType(0);
		return  query;
	}
	/**
	 * @return clientmgr
	 */
	public String getClientmgr() {
		return clientmgr;
	}
	/**
	 * ���� clientmgr
	 * @param clientmgr 
	 */
	public void setClientmgr(String clientmgr) {
		this.clientmgr = clientmgr;
	}
	/**
	 * @return firstresp
	 */
	public String getFirstresp() {
		return firstresp;
	}
	/**
	 * ���� firstresp
	 * @param firstresp 
	 */
	public void setFirstresp(String firstresp) {
		this.firstresp = firstresp;
	}
	/**
	 * @return sumG
	 */
	public double getSumG() {
		return sumG;
	}
	/**
	 * ���� sumG
	 * @param sumG 
	 */
	public void setSumG(double sumG) {
		this.sumG = sumG;
	}
	/**
	 * @return thirlasttr
	 */
	public String getThirlasttr() {
		return thirlasttr;
	}
	/**
	 * ���� thirlasttr
	 * @param thirlasttr 
	 */
	public void setThirlasttr(String thirlasttr) {
		this.thirlasttr = thirlasttr;
	}
	

}
