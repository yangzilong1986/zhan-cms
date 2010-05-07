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
 * <p/>Title: 五级分类不良资产台帐查询页面BEAN 
 * <p/>===============================================
 * <p/>Description: 为前台页面提供查询所需要的数据和分页相关参数。
 * 
 * @version $Revision: 1.5 $ $Date: 2007/06/19 00:43:07 $
 * @author zhengxin <p/>修改：$Author: zhengx $ SelectDtel
 */

public class SwearPageobject  {
	private int sumA,sumB;
	private double sumC,sumD,sumE,sumF,sumG;// 浮点型合计列
	private String brhid;// 网点编号
	private String scbrhname;//网点名称
	private String creadate;// 清分时点
	private String startdate;// 开始时间
	private String enddate;// 结束时间
	private String sartbal;// 时点开始余额
	private String endbal;// 时点结束余额
	private String stype;// 1 本年  2 本期
	private String clientmgr;//CLIENTMGR 客户经理
	private String firstresp;//第一责任人
	private String moeltitle;//中标题
	
	ConnectionManager manager;// 数据操作类
	private HashMap fcsortQueryPathMap = new HashMap();
	private FcsortQueryPath fcsortquery ;
	private String queryid;//项目编号
	private String sqlpart;//三级页面所需要的一部分SQL
	private String sqlpartname;//三级页面所需要的一部分SQL名称
	private String sqlpartvalue;//三级页面所需要的一部分SQL值
	//**分页相关
	private int pagecount;//页面记录数
	private int pagesize;// 页面大小
	private int currpage;// 当前页
	private int maxpage;// 最大页
	private String thirlasttr;
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
	DecimalFormat dfB=new DecimalFormat("###,###,###,##0.0000");
	FcsortUtil util;
	/**
	 * 设置查询条件和构建页面结果集
	 * 
	 * @param request
	 * @param response
	 */
	public SwearPageobject() {
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
	
		
		this.brhid =request.getParameter("brhid");//得到网点编号
		this.creadate=request.getParameter("creadate")==null?util.getDT():request.getParameter("creadate").trim();// 清分时点
		
		/*
		this.startdate=request.getParameter("startdate")==null?"1999-01-01":request.getParameter("startdate");// 开始时间
		this.enddate=request.getParameter("enddate")==null?getMonthLastDay(1):request.getParameter("enddate").trim();// 结束时间
		*/
		
		this.startdate=request.getParameter("startdate")==null?"":request.getParameter("startdate");// 开始时间
		this.enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate").trim();// 结束时间
		
		this.stype=request.getParameter("stype")==null?"2":request.getParameter("stype");
		this.sqlpartname=request.getParameter("sqlpartname")==null?"1":request.getParameter("sqlpartname").trim();
		this.sqlpartvalue=request.getParameter("sqlpartvalue")==null?"1":request.getParameter("sqlpartvalue").trim();
		this.clientmgr=request.getParameter("CLIENTMGR")==null?"":request.getParameter("CLIENTMGR").trim();//客户经理
		this.firstresp=request.getParameter("FIRSTRESP")==null?"":request.getParameter("FIRSTRESP").trim();//第一责任人
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
		//Session 中取得 hashmap - FcsortQueryPath 
		fcsortQueryPathMap =(HashMap)request.getSession().getAttribute("FCSORT_QUERY_PATH_MAP");
		fcsortquery = (FcsortQueryPath)fcsortQueryPathMap.get(queryid);
		this.scbrhname = SCBranch.getSName(brhid);
		this.sartbal=request.getParameter("sartbal");// 时点开始余额
		this.endbal=request.getParameter("endbal");// 时点结束余额
		this.setTitle();
		
		//************分页相关*******************
		this.pagesize=Integer.valueOf(request.getParameter("pagesize")==null?"10":request.getParameter("pagesize")).intValue();
		this.currpage=Integer.valueOf(request.getParameter("currpage")==null?"1":request.getParameter("currpage")).intValue();
		
		
		return true;
	}
	
	/**
	 * 一级页面的第一条查询SQL
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
	 * 一级页面的第二条查询SQL
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
 * 得到一级页面的结果集
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
					 int totalA=child.getInt(1);//笔数
					 obj.setTotalA(totalA);
					 sumA+=totalA;
					 int totalB=child.getInt(2);//户数
					 obj.setTotalB(totalB);
					 sumB+=totalB;
					 
					 double balA=Double.valueOf(child.getString(3)==null?"0":child.getString(3)).doubleValue()/10000;//结欠金额
					 obj.setBalA(balA);
					 sumG+=balA;
					 double balB=Double.valueOf(child.getString(4)==null?"0":child.getString(4)).doubleValue()/10000;//结欠金额
					 obj.setBalB(balB);
					
					 sumC+=balB;
					 double h3=Double.valueOf(child.getString(5)==null?"0":child.getString(5)).doubleValue()/10000;//借欠金额-次级
					 obj.setH3(h3);
					 sumD+=h3;
					 double h4=Double.valueOf(child.getString(6)==null?"0":child.getString(6)).doubleValue()/10000;//借欠金额-可疑
					 obj.setH4(h4);
					 sumE+=h4;
					 double h5=Double.valueOf(child.getString(7)==null?"0":child.getString(7)).doubleValue()/10000;//借欠金额-损失
					 obj.setH5(h5);
					 sumF+=h5;
				}
				list.add(obj);

			}
			
			
		} catch (RuntimeException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return list;
	}


 
/**
 * 二级页面查询列表
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
 * 设置三级页面的记录数 
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
			this.thirlasttr="形成原因";
		}
		if(this.queryid.trim().equals("BYVOUCH"))
		{
			
			this.thirlasttr="担保方式";
		}
		if(this.queryid.trim().equals("BYWAY"))
		{
			
			this.thirlasttr="贷款投向";
		}
		if(this.queryid.trim().equals("BADREASON"))
		{
			this.thirlasttr="形成原因";
		}
		if(this.queryid.trim().equals("BYTERM"))
		{
			this.thirlasttr="贷款期限";
		}
	} catch (Exception e) {
		// TODO 自动生成 catch 块
		e.printStackTrace();
	}
	
	this.pagecount = sum;
}
/**
 * 得到三级页面的结果集
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
					tr.setTd10(DBUtil.toDB("短期"));
				}else if(d>1&&d<3)
				{
					tr.setTd10(DBUtil.toDB("中期"));
				}
				else
				{
					tr.setTd10(DBUtil.toDB("长期"));
				}
			}
			
		
			
			list.add(tr);
		}
		
		
		
	} catch (Exception e) {
		// TODO 自动生成 catch 块
		e.printStackTrace();
	}
	return list;
}
/**
 * 得到必选查询语句 查询时点 贷款发放日 本年 本月
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
 * 得到列表的标题
 * @return String
 */
public String getTitle()
{
	StringBuffer str = new StringBuffer("");
	
	/*if(!this.queryid.endsWith("FCBMAIN"))
	{
		str.append(this.stype.equals("1")?"本年":"本月");
	}*/
	
	//System.out.println(fcsortquery.getQueryname().trim());
	
	str.append(fcsortquery.getQueryname().trim());
	str.append(this.getMoeltitle());
	str.append("统计表");
	return str.toString();
}
/**
 * 得到列表的中标题
 * @return String
 */
public String getMTitle()
{
	String str = "";
	
	if ((startdate!= null && !startdate.equals(""))&&(enddate!= null && !enddate.equals(""))) 
	{
		str="贷款发放日："+startdate+"&nbsp;至&nbsp;"+enddate;
	}
	else if ((startdate== null || startdate.equals(""))&&(enddate!= null && !enddate.equals("")))
	{
		str="贷款发放日："+enddate+"以前";
	}
	else if((startdate!= null && !startdate.equals(""))&&(enddate== null || enddate.equals("")))
	{
		str="贷款发放日："+startdate+"以后";
	}
	else{
		str="贷款发放日：全时间段";
	}
	return str;
}
/**
 * 处理网点集合
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
 * 得到每月最后一天
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
 * 设置查询模块显示的主标题
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
		this.setMoeltitle(endbal+"万元以下");
		this.setSartbal("");
	}
	else if((sartbal!=null&&!sartbal.equals(""))&&(endbal==null||endbal.equals("")))
	{
		this.setMoeltitle(sartbal+"万元以上");
		this.setEndbal("");
	}
	else
	{
		this.setMoeltitle(sartbal+"万元---"+endbal+"万元");
	}
}

	
/**
 * @return brhid
 */
public String getBrhid() {
	return brhid;
}
/**
 * 设置 brhid
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
 * 设置 creadate
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
 * 设置 endbal
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
 * 设置 enddate
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
 * 设置 fcsortquery
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
 * 设置 fcsortQueryPathMap
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
 * 设置 moeltitle
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
 * 设置 queryid
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
 * 设置 sartbal
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
 * 设置 scbrhname
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
 * 设置 sqlpart
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
 * 设置 sqlpartname
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
 * 设置 sqlpartvalue
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
 * 设置 startdate
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
 * 设置 stype
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
 * 设置 sumA
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
 * 设置 sumB
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
 * 设置 sumC
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
 * 设置 sumD
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
 * 设置 sumE
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
 * 设置 sumF
 * @param sumF 
 */
public void setSumF(double sumF) {
	this.sumF = sumF;
}
/**
 * 设置 maxpage
 * @param maxpage 
 */
public void setMaxpage(int maxpage) {
	this.maxpage = maxpage;
}
/**
 * 设置 pagecount
 * @param pagecount 
 */
public void setPagecount(int pagecount) {
	this.pagecount = pagecount;
}
/**
 * 得到第几页
 * @return int
 */
	public int getCurrpage() {		
		return currpage > getMaxpage() ? getMaxpage() : currpage;
	}
	/**
	 * 设置页码
	 * 
	 */
	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}
	/**
	 * 得到最大页数
	 * @return int
	 */
	public int getMaxpage() {
		return maxpage == 0 ? 1 : this.maxpage;
	}
	/**
	 * 设置最大页数
	 */
	public void setMaxpage() {
		this.maxpage = pagecount / this.pagesize == 0 ? 1 : (pagecount + pagesize - 1)/ pagesize;
	}
	/**
	 * 得到页面最大数
	 * @return int
	 */
	public int getPagecount() {
		return pagecount;
	}
	/**
	 * 得到页面显示行数
	 * @return int
	 */
	public int getPagesize() {
		return pagesize;
	}
	/**
	 * 设置页面显示行数
	 * @param  pagesize
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	/**
	 * 得到开始行数
	 * @return int
	 */
	public int getEndrow() {
		return this.currpage >= this.maxpage ? this.pagecount:this.currpage * this.pagesize;
	}
	/**
	 * 得到结束行数
	 * @return int
	 */
	public int getStartrow() {
		return (this.currpage - 1) * this.pagesize+1;
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
		map.put("FCBMAIN", this.setFcbmain());
		map.put("BYVOUCH", this.setByvouch());
		map.put("BYWAY", this.setByway());
		map.put("BADREASON", this.setBadReason());
		map.put("BYTERM", this.setByTerm());
		request.getSession().setAttribute("FCSORT_QUERY_PATH_MAP", map);
		
		return true;
	}
	/**
	 * 返回查询枚举类型的SQlPART3
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
			sqlpart3[0]="担保方式";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanType3' and enutp=fcbmain.LoanType3) ";
		}
		if(this.queryid.trim().equals("BYWAY"))
		{
			sqlpart3[0]="贷款投向";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=integer(fcbmain.LoanWay)) ";
		}
		if(this.queryid.trim().equals("BADREASON"))
		{
			sqlpart3[0]="形成原因";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=fcbmain.BadReason) ";
		}
		/*if(this.queryid.trim().equals("BYTERM"))
		{
			sqlpart3[0]="担保方式";
			sqlpart3[1]=",(select ENUDT from PTENUMINFODETL where enuid ='LoanWay' and enutp=fcbmain.LoanWay) ";
		}*/
		return sqlpart3;
	}
	/**
	 * 设置五级分类不良资产模块参数
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setFcbmain() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("FCBMAIN");
		query.setQueryname("五级分类不良资产");
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
	 * 设置五级分类不良贷款按担保方式模块参数
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setByvouch() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("BYVOUCH");
		query.setQueryname("五级分类不良贷款按担保方式");
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
	 * 设置五级分类不良贷款按贷款投向模块参数
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
		query.setQueryname("五级分类不良贷款按贷款投向 ");
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
	 * 设置五级分类不良贷款按形成原因模块参数
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setBadReason() throws Exception
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("BADREASON");
		query.setQueryname("五级分类不良贷款按形成原因 ");
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
	 * 设置五级分类不良贷款按贷款期限模块参数
	 * @param request
	 * @throws Exception
	 * @return FcsortQueryPath
	 */
	public FcsortQueryPath setByTerm() 
	{
		
		FcsortQueryPath query = new FcsortQueryPath();
		query.setQueryid("BYTERM");
		query.setQueryname("五级分类不良贷款按贷款期限 ");
		
			FcsortQueryPathEmnu emnua = new FcsortQueryPathEmnu();
			emnua.setEnuid("BYTERM");
			emnua.setEnudt(DBUtil.toDB("短期"));
			
			emnua.setSqlpart(" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  <= 12  ");
			emnua.setEnutp("1");
			
			FcsortQueryPathEmnu emnub = new FcsortQueryPathEmnu();
			emnub.setEnuid("BYTERM");
			emnub.setEnudt(DBUtil.toDB("中期"));
			emnub.setSqlpart(" (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  > 12 and (days(FCBMAIN.DUEDATE) - days(FCBMAIN.PAYDATE))/30  <36  ");
			emnub.setEnutp("2");
			FcsortQueryPathEmnu emnuc = new FcsortQueryPathEmnu();
			emnuc.setEnuid("BYTERM");
			emnuc.setEnudt(DBUtil.toDB("长期"));
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
	 * 设置 clientmgr
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
	 * 设置 firstresp
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
	 * 设置 sumG
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
	 * 设置 thirlasttr
	 * @param thirlasttr 
	 */
	public void setThirlasttr(String thirlasttr) {
		this.thirlasttr = thirlasttr;
	}
	

}
