<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.cms.report.db.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*,java.net.URLEncoder" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.cmsi.fc.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch,zt.cms.report.QrySummary" %>
<%@ page import="zt.cmsi.pub.define.UserRoleMan,zt.cmsi.fc.FcUpXML" %>
<%--
=============================================== 
Title: 五级分类台帐综合查询
Description: 五级分类台帐综合查询。
 * @version  $Revision: 1.6 $  $Date: 2007/06/20 09:40:27 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
QrySummary qry=new QrySummary();
FcUpXML upxml=new FcUpXML();
%>

<%
request.setCharacterEncoding("GBK");
UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
if(um==null){
	response.sendRedirect("error.jsp");
}
//String brhId = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
//ConnectionManager manager=ConnectionManager.getInstance();

 String CREATEDATEGO = request.getParameter("CREATEDATEGO");
 String CREATEDATEER = request.getParameter("CREATEDATEER");
 String DUEDATEGO = request.getParameter("DUEDATEGO");
 String DUEDATEER = request.getParameter("DUEDATEER");
 String arracclist=request.getParameter("arracclist");
 if (arracclist!=null&&!arracclist.trim().equals(""))arracclist=arracclist.trim();
 
 String BRHID = request.getParameter("BRHID");//网点号
 String branchname=request.getParameter("branchname");//网点名称
 String display=request.getParameter("displayval");//查询框是否显示
 String CLIENTNAME = request.getParameter("CLIENTNAME");//客户名称
 String FIRSTRESP = request.getParameter("FIRSTRESP");//第一责任人
 String CLIENTMGR=request.getParameter("CLIENTMGR");// 客户经理
 String CNLNO = request.getParameter("CNLNO");//担保人名称
 String MOTYPE=request.getParameter("MOTYPE");//业务种类
 String ACCLIST=request.getParameter("ACCLIST");//科目
 String LOANCAT2=request.getParameter("LOANCAT2");//四级分类形态
 String LOANCAT1=request.getParameter("LOANCAT1");//五级分类形态
 String LOANCAT3=request.getParameter("LOANCAT3");//贷款用途


if(CREATEDATEGO != null && CREATEDATEGO.trim().length() <= 0) CREATEDATEGO = null;
if(CREATEDATEER != null && CREATEDATEER.trim().length() <= 0) CREATEDATEER = null;
if(DUEDATEGO != null && DUEDATEGO.trim().length() <= 0) DUEDATEGO = null;
if(DUEDATEER != null && DUEDATEER.trim().length() <= 0) DUEDATEER = null;

if(CLIENTNAME != null && CLIENTNAME.trim().length() <= 0) CLIENTNAME = null;//客户名称
if(BRHID != null && BRHID.trim().length() <= 0) BRHID = null;//网点编号
if(FIRSTRESP != null &&( FIRSTRESP.trim().length() <= 0||FIRSTRESP.equals("0"))) FIRSTRESP = null;//第一责任人
if (CLIENTMGR!=null &&(CLIENTMGR.trim().length()<=0||CLIENTMGR.equals("0"))) CLIENTMGR=null;//客户经理
if(CNLNO != null && CNLNO.trim().length() <= 0) CNLNO = null;//担保人名称


//分页
int start=0;
int end=0;
String pnStr = request.getParameter("pn");
if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
int pn=Integer.parseInt(pnStr==null?"1":pnStr);
int ps=50;
start = (pn - 1) * ps + 1;
end = pn * ps;
//String sql="select rq.*,s.LNAME,t.TYPENAME,c.NAME,el.ENUDT as LOANTYPE3NAME,el_2.ENUDT as LOANCAT1NAME,bg.CLIENTNAME,bg.ID,el_3.ENUDT AS LOANCAT3NAME,su.USERNAME  from  rqloanlist rq";
String s_count="select rq.BMNO from rqloanlist rq";
String s_sum="select sum(rq.CONTRACTAMT) as T_CONTRACTAMT,sum(rq.NOWBAL) AS T_NOWBAL,sum(rq.FISRTRESPPCT) AS T_FISRTRESPPCT from rqloanlist rq";
String s_main="select * from (select rownumber() over() AS rn,rq.*,s.LNAME,t.TYPENAME,el.ENUDT as LOANTYPE3NAME,el_2.ENUDT as LOANCAT1NAME,el_3.ENUDT AS LOANCAT2NAME,el_4.ENUDT as LOANCAT3NAME,su.USERNAME,su1.USERNAME as USERNAME1,tr.duebal1+tr.duebal2+tr.duebal3 as lixi,acc.accname from rqloanlist rq";

String sql=" left join SCBRANCH s on  rq.BRHID=s.BRHID";
sql+=" left join BMType t on rq.TYPENO=t.typeno ";
//sql+="left join CMCORPCLIENT c on rq.CLIENTNO=c.CLIENTNO ";
sql+=" left join PTENUMINFODETL el on rq.LOANTYPE3=el.ENUTP and el.ENUID='LoanType3'";
sql+=" left join PTENUMINFODETL el_2 on rq.LoanCat1=el_2.ENUTP and el_2.ENUID='LoanCat1'";
sql+=" left join PTENUMINFODETL el_3 on rq.LOANCAT2=el_3.ENUTP and el_3.ENUID='LoanCat2'";
sql+=" left join PTENUMINFODETL el_4 on rq.LOANCAT3=el_4.ENUTP and el_4.ENUID='LoanCat3'";
sql+=" left join SCUSER su1 on rq.CLIENTMGR=su1.LOGINNAME ";
//sql+=" left join BMGUARANTOR bg on rq.BMNO=bg.BMNO";
sql+=" left join SCUSER su on rq.FIRSTRESP=su.LOGINNAME";
sql+=" left join rqdueintrst tr on rq.bmno=tr.bmno";
sql+=" left join SCHostAcc acc on rq.accno=acc.accno";
String s_where=" where 1=1 ";
if(CREATEDATEGO != null)  s_where += " and rq.PAYDATE >= "+DBUtil.toSqlDate(CREATEDATEGO);
if(CREATEDATEGO != null && CREATEDATEER != null) s_where += " and rq.PAYDATE >= "+DBUtil.toSqlDate(CREATEDATEGO)+" and rq.PAYDATE <= "+DBUtil.toSqlDate(CREATEDATEER);
if (BRHID!=null)s_where+=" and rq.brhid='"+BRHID.trim()+"'";
if(CLIENTNAME != null) s_where += " and rq.CLIENTNAME like '%"+DBUtil.toDB(CLIENTNAME.trim())+"%'";//客户
//if(FIRSTRESP != null) s_where += " and su.USERNAME like '%"+DBUtil.toDB(FIRSTRESP.trim())+"%'";//第一责任人
//if (CLIENTMGR!=null) s_where+=" and su1.USERNAME like '%"+DBUtil.toDB(CLIENTMGR.trim())+"%'";//客户经理
if(FIRSTRESP != null) s_where += " and rq.FIRSTRESP='"+DBUtil.toDB(FIRSTRESP.trim())+"'";//第一责任人
if (CLIENTMGR!=null) s_where+=" and rq.CLIENTMGR='"+DBUtil.toDB(CLIENTMGR.trim())+"'";//客户经理
if(CNLNO != null) s_where += " and exists(select 1 from bmpldgsecurity where rq.BMNO=BMNO and clientname like '%"+DBUtil.toDB(CNLNO.trim())+"%')";//担保人名称
if(DUEDATEGO != null) s_where += " and rq.ENDDATE >= "+DBUtil.toSqlDate(DUEDATEGO)+"";
if(DUEDATEGO != null && DUEDATEER != null) s_where += " and rq.ENDDATE >= "+DBUtil.toSqlDate(DUEDATEGO)+" and rq.ENDDATE <= "+DBUtil.toSqlDate(DUEDATEER)+"";
if (MOTYPE!=null&&!MOTYPE.trim().equals(""))s_where+=" and rq.TYPENO="+MOTYPE+"";//业务种类
if (arracclist!=null&&!arracclist.trim().equals(""))s_where+=" and rq.ACCNO in ('"+arracclist.replaceAll(",","','")+"')";//科目
if (LOANCAT2!=null&&!LOANCAT2.trim().equals(""))s_where+=" and rq.LOANCAT2="+LOANCAT2+"";//四级分类
if (LOANCAT1!=null&&!LOANCAT1.trim().equals(""))s_where+=" and rq.LOANCAT1="+LOANCAT1+"";//五级分类
if (LOANCAT3!=null&&!LOANCAT3.trim().equals(""))s_where+=" and rq.LOANCAT3="+LOANCAT3+"";//贷款用途
//s_where +=" order by CREATEDATE desc,FCTYPE,BRHID,BMNO";
s_main+=sql+s_where+" ) AS a1   "+" WHERE a1.rn BETWEEN "+start+" AND "+end;
s_where=sql+s_where;
	
	//CachedRowSet crs=manager.getRs(sql);
	CachedRowSet sumRs=null;
	Vector vec=null;
	int rows=0;
	CachedRowSet crs=null;
	if (BRHID!=null)
	{
		vec=DB2_81.pageRs(s_main,s_count,s_where);
		rows=((Integer)vec.get(0)).intValue();
		crs=(CachedRowSet)vec.get(1);
		if(rows<=end&&rows>0)
		{
			sumRs=DB2_81.getRs(s_sum+s_where);//最后一页,计算合计项
		}
	}
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");


String mess=(String)request.getAttribute("mess");
if(mess==null){
	mess="";
}
else{
	mess="<li class='error_message_li'>"+mess.trim()+"</li>";
}
%>
<html>
<head>
<base href="<%=basePath%>">
<title>信贷管理</title>
<link href="query/setup/web.css" rel="stylesheet" type="text/css">
<script src='js/fc.js' type='text/javascript'></script>
<script type="text/javascript" src='/setup/meizzDate.js'></script>
<script language="JavaScript" type="text/JavaScript">

function info(infoname){
		var url='Flyy_info.jsp?SEQNO='+infoname;
		window.open(url,'FI2','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}
function buttonDisabled(){
    with(form2){
        a.disabled="true";
        b.disabled="true";
        c.disabled="true";
    }
}

function query(){
    var brhname=document.all.BRHID;
    var obj=document.all.Submit1;
    //var date=document.all.date;

    if(brhname.value==''){
       alert("请选择网点！");
       brhname.focus();
       return false;
    }
    /*
    if(date.value==null){
      alert("请选择日期！");
      date.focus();
      return false;
   }
   if (!isDate(date.value)) {
      alert("日期格式错误！");
      date.focus();
      return false;
   }
   */
   var str='数据检索中。。。。。。';
   	obj.disabled="true";
	window.status=str;
	document.title=str;
	document.forms[0].submit();
}
function getCategory()
{
	if (document.all.Layer1.style.display=='none')
	{
		document.all.Layer1.style.display='';
	}
	else
	{
		document.all.Layer1.style.display='none';
		var s=new Array();
		var acclist = document.all.ACCLIST;
		for (var i=0;i<acclist.options.length;i++)
		{
			if (acclist.options[i].selected==true)
			s.push(acclist.options[i].value.Trim());
		}
		document.forms[0].arracclist.value=s.join(',');
	}
}
</script>
<script src='js/check.js' type='text/javascript'></script>
<script src='js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="js/flippage3.js"></script>
<script src='js/querybutton.js' type='text/javascript'></script>
<script src='js/meizzDate.js' type='text/javascript'></script>
<script src='js/fcmain.js' type='text/javascript'></script>
<script src='js/main.js' type='text/javascript'></script>
<style type="text/css">
<!--
#Layer1 {
	position:absolute;
	width:250px;
	height:373px;
	z-index:1;
	left: 366px;
	top: 80px;
}
-->
</style>
</head>
<body onload="displayAll('<%=display==null?"":display%>');">
<form  name="form1" method="post"  id="winform" action="query/common/qry_summary.jsp" >
<div id="Layer1" style="display:none" align="center" >
<select id="ACCLIST" name="ACCLIST"  multiple="multiple" size="30">
<option value="">请选择</option>
<%=qry.getAccList() %>
</select>
</div>
<input name="referValue" type="hidden">
<input name="s_count" type="hidden">
<input name="s_where" type="hidden">
<input name="s_main" type="hidden">
<input name="branchname" id="branchname" type="hidden" value="<%=branchname==null?"":branchname%>">
<input name="displayval" id="displayval" type="hidden" >
<input name="arracclist" id="arracclist" type="hidden" value="<%=arracclist==null?"":arracclist%>">
<table <%if(rows==0){ %>width='1000'<%}else{ %> width='100%' <%} %> border=1 align=center cellPadding=0 cellSpacing=0 borderColor=#999999  bgcolor=#f1f1f1 class=table>
 <tbody>
  <tr Align=center class=head bgcolor="#ffffff">
  <td align="center" valign="middle" id="detailTab">
 <table align='center' cellSpacing=1 cellPadding=1  bgcolor="#003300"  width=100% id="tblmain">
 <tbody>
                            <input type="hidden" name="flag" value="">
							<input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
                                        <tr align=left bgcolor="#ffffff">
                                          <td height="19"  >网点选择</td><td><input type="text"
															name="BRHID" id='BRHID' value="<%=BRHID==null ? "":BRHID %>" class="query_form_text" e="25"
															readonly> <input type="button" name="nameref"
															value="…" onclick="deptrefer_click()"
															class="button">
                                          </td><td>发放日期</td><td>
                                           <input type="text" name="CREATEDATEGO" readonly class="input" value="<%=CREATEDATEGO==null?"":CREATEDATEGO%>"   size="10">
										   <input name="button" type="button" class="button"   onclick="setday(this,winform.CREATEDATEGO)" value="…">
										   至
                                           <input type="text"  class="input" readonly  name="CREATEDATEER" value="<%=CREATEDATEER==null?"":CREATEDATEER%>"    size="10">
										   <input name="button"  class="button"  type="button"   onclick="setday(this,winform.CREATEDATEER)" value="…">
									     </td><td>
									     	到期日期
									     	</td><td >
                                           <input type="text" name="DUEDATEGO" readonly class="input"  value="<%=DUEDATEGO==null?"":DUEDATEGO%>"   size="10">
										   <input name="button" type="button"   class="button"  onclick="setday(this,winform.DUEDATEGO)" value="…">
											至
                                           <input type="text" name="DUEDATEER" readonly class="input"  value="<%=DUEDATEER==null?"":DUEDATEER%>"    size="10">
										   <input name="button" type="button"   class="button"  onclick="setday(this,winform.DUEDATEER)" value="…">
										   <td>担保人名称</td>
										   <td><input type="text"  class="input"  name="CNLNO" value="<%=CNLNO==null?"":CNLNO%>" size="12"></td>
					      				</tr>
										<tr  align=left bgcolor="#ffffff">
                                          <td height="19" nowrap >客户名称</td><td>
                                          <input type="text"  class="input"  name="CLIENTNAME" value="<%=CLIENTNAME==null?"":CLIENTNAME%>"    size="28">
                                         </td><td>第一责任人</td><td>
                                         <select  id="FIRSTRESP"  name="FIRSTRESP"  >
                                         <option value="0">请选择</option>
                                         <%if (BRHID!=null&&!BRHID.equals("")){out.write(upxml.getUserNameList(BRHID));}%>
                                         </select>
                                         <script language="javascript">
											setVal(document.all.FIRSTRESP,'<%=DBUtil.fromDB2(FIRSTRESP)%>');
										</script>
                                         &nbsp;&nbsp;&nbsp;&nbsp;
                                    </td><td>
                                          用途</td><td>
                                          <select id="LOANCAT3" name="LOANCAT3">
                                           <option value="">请选择</option>
                                         <%=qry.getLoanCat3()%>
                                          </select></td><td>
          								四级形态</td><td>
                                         <select id="LOANCAT2"  name="LOANCAT2">
                                         <option value="">请选择</option>
                                        <%=qry.getLoanCat2() %>
                                         </select>                                          
										   </td>
                                         </tr>
                                         <tr  align=left bgcolor="#ffffff">
                                         
                                         <td>客户经理</td><td>
                                         <select  id="CLIENTMGR" name="CLIENTMGR" >
                                          	<option value="0">请选择</option>
                                              <%if (BRHID!=null&&!BRHID.equals("")){out.write(upxml.getUserNameList(BRHID));}%>
                                         </select>
                                         <script language="javascript">
											setVal(document.all.CLIENTMGR,'<%=DBUtil.fromDB2(CLIENTMGR)%>');
										</script>
                                         </td><td>
                                          科目</td><td>
                                       	<input class="button" name="btnSelCategory" value="选择科目" type="button" onclick="getCategory();">
                                    </td><td>
                                         业务种类</td><td>
                                          <select id="MOTYPE" name="MOTYPE">
                                           <option value="">请选择</option>
                                         <%=qry.getType()%>
                                          </select>
                                       </td><td>
                                         五级分类</td><td>      
                                         <select id="LOANCAT1" name="LOANCAT1">
                                         <option value="">请选择</option>
                                         <%=qry.getLoanCat1() %>
                                         </select>
										</td>
                                        </tr>
                                        </tbody>
                    </table>
<div id="showTable" align="left" width="100%">
<input class="button" type="button" name="btn0" id ='Submit1' value=" 检 索 " onclick="query();">
<input class="button" type="button"  name="btn2" value=" 重 置 " onclick="cls();">
<input class="button" type="button"  name="btn1" value=" 打 印 " onclick="printform();">
<input class="button" type="button"  name="btn3" value=" 隐 藏 查 询 条 件 " onclick="display(document.all.tblmain.style.display);">
</div>
<div id="showTable" align="center" width="100%">
         <div align="center" class=caption>贷款台帐综合清单</div>
</div>
<div id="showTable" align="left" width="100%">
<span  id='branchname1' name="branchname1">网点名称:<%=branchname==null?"":branchname%></span>
</div>
<%if (crs!=null){ %>
                        <table  width=100%  align='center' cellSpacing=1 cellPadding=1 border=0 id='exportTable' class=table>
                            <tr class=head >
                              <!--  <td nowrap align="center">网点名称</td>-->
                              <td  nowrap align="center">业务种类</td>
                              <td nowrap align="center">贷款帐号</td>
                              <td nowrap align="center">科目号</td>
                              <td nowrap align="center">借据号</td>
                              <td nowrap align="center">发放日</td>
                              <td nowrap align="center">客户代码</td>
							  <td nowrap align="center">客户名称</td>
                              <td  nowrap align="center">担保方式</td>
                              <!--  
                              <td  nowrap align="center">担保人名称</td>
							  <td  nowrap align="center">担保人代码</td>
							  -->
                              <td  nowrap align="center">四级形态</td>
                              <td   nowrap align="center">五级分类</td>
							  <td  nowrap align="center">到期日</td>
                              <td   nowrap align="center">展期到期日</td>
                              <td   nowrap align="center">借款用途</td>
                              <td   nowrap align="center">利率</td>
                              <td   nowrap align="center">合同金额</td>
                              <td  nowrap align="center"> 借款余额</td>
                              <td  nowrap align="center"> 欠息金额</td>
                              <!-- <td   nowrap align="center">审批人</td>-->
                              <td   nowrap align="center">第一责任人</td>
                              <td   nowrap align="center">责任金额</td>
                              <td   nowrap align="center">业务号</td>
                            </tr>
<%
		while(crs.next()){
%>
                            <tr   bgcolor="#ffffff" onmouseover="mOvr(this)" onclick="mClk('<%=crs.getString("BMNO") %>');" onmouseout="mOut(this);">
                              <!--<td nowrap  align="left"  ><div title='<%=crs.getString("BRHID") %>' ><%=DBUtil.fromDB2(crs.getString("LNAME"))%></div></td> //网点名称 -->
                              <td nowrap align="left" ><%=DBUtil.fromDB2(crs.getString("TYPENAME"))%></td><!-- 业务种类 -->
                              <td nowrap align="center" ><%=crs.getString("ACTNO")%></td><!-- 贷款帐号 -->
                              <td nowrap align="center" ><div title='<%=DBUtil.fromDB(crs.getString("ACCNAME"))%>'><%=crs.getString("ACCNO")%> </div></td><!-- 科目号 -->
                              <td nowrap align="center" ><%=crs.getString("CNLNO")%></td><!-- 借据号 -->
                              <td nowrap align="center" ><%=crs.getString("PAYDATE")%></td><!-- 发放日期 -->
                              <td nowrap  align="left"> <%=crs.getString("CLIENTNO")==null?"":DBUtil.fromDB(crs.getString("CLIENTNO"))%></td><!--//借款人名称  -->
							  <td nowrap align="left"><%=crs.getString("CLIENTNAME")==null?"":DBUtil.fromDB(crs.getString("CLIENTNAME"))%></td><!--//借款人代码  -->
                              <td nowrap align="center"><%=crs.getString("LOANTYPE3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANTYPE3NAME"))%></td><!--//担保方式  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("CLIENTNAME")%></td>//担保人名称  -->
							  <!--<td nowrap align="left"><%//=crs.getString("ID")%></td>//担保人代码  -->
                              <td nowrap align="center"><%=DBUtil.fromDB2(crs.getString("LOANCAT2NAME"))%></td><!--//四级分类形态  -->
                              <td nowrap  align="center"><%=DBUtil.fromDB2(crs.getString("LOANCAT1NAME"))%></td><!--//五级分类形态  -->
							  <td nowrap align="left"><%=crs.getString("ENDDATE")%></td><!--//到期日  -->
                              <td nowrap  align="left"><%=crs.getString("ISEXTEND").equals("1")?crs.getString("NOWENDDATE"):""%></td><!--//展期到期日  -->
                              <td nowrap  align="left"><%=crs.getString("LOANCAT3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANCAT3NAME"))%></td><!--//借款用途  -->
                              <td nowrap  align="right"><%=crs.getBigDecimal("BRATE")%></td><!--//利率  -->
                               <td nowrap  align="right"><%=df.format(crs.getBigDecimal("CONTRACTAMT"))%></td><!--//合同金额  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("NOWBAL"))%></td><!--//借款金额  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("lixi")==null?new BigDecimal(0.00):crs.getBigDecimal("lixi"))%></td><!--//欠息金额  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("")%></td>//审批人  -->
                              <td nowrap  align="center"><%=crs.getString("USERNAME")==null?"":DBUtil.fromDB(crs.getString("USERNAME"))%></td><!-- //第一责任人 -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("FISRTRESPPCT"))%></td><!--//责任金额  -->
                              <td nowrap  align="right"><%=crs.getString("BMNO")%></td><!--//业务号 -->
                            </tr>
<%
}
if(rows<=end&&rows>0){
while(sumRs.next()){
%>
                            <tr   bgcolor="#ffffff" >
                              <td nowrap align="left" colspan='14'>合计</td><!-- 业务种类 -->
                               <td nowrap  align="right"><%=df.format(sumRs.getBigDecimal("T_CONTRACTAMT"))%></td><!--//合同金额  -->
                              <td nowrap  align="right"><%=df.format(sumRs.getBigDecimal("T_NOWBAL"))%></td><!--//借款金额  -->
                              <td nowrap  align="right"></td><!--//欠息金额  -->
                              <td nowrap  align="center"></td><!-- //第一责任人 -->
                              <td nowrap  align="right"><%=df.format(sumRs.getBigDecimal("T_FISRTRESPPCT"))%></td><!--//责任金额  -->
                              <td nowrap  align="right"></td><!--//业务号 -->
                            </tr>

<%}} %>
</table>
<%} %>
</td>
</tr>
</tbody>
</table>
<%if (rows>0){ %>
<script language="javascript">
createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"/query/common/qry_summary.jsp?pn=","form1");
</script>
<%} %>
</form>
</body>
</html>
<script language="javascript">
document.title="贷款台帐综合查询";
document.focus();


function checkSub(curChkName,formnm,funcnm)
{
  //alert("dddddddd");
	if(curChkName=="" || formnm=="" || funcnm==""){
		alert("checkSub error!");
		return;
	}
	var i;

	if(eval(formnm+"."+curChkName+".checked")){
		for(i=0;i<eval(formnm +"." + funcnm +".length");i++){
			if(eval(formnm + "." + funcnm + "[i].id.indexOf('"+ curChkName + "')!=-1"))
				eval(formnm+"."+eval(formnm + "." + funcnm + "[i].id")+".checked=true");
		}
	}

	if(eval(formnm+"."+curChkName+".checked==false")){
		for(i=0;i<eval(formnm +"." + funcnm +".length");i++){
			if(eval(formnm + "." + funcnm + "[i].id.indexOf('"+ curChkName + "')!=-1"))
				eval(formnm+"."+eval(formnm + "." + funcnm + "[i].id")+".checked=false");
		}
	}

	if(eval(formnm+"."+curChkName+".checked")){
		for(i=0;i<eval(formnm +"." + funcnm +".length");i++){
			if(eval(formnm + "." + funcnm + "[i].id.indexOf('"+ curChkName + "')!=-1"))
				eval(formnm+"."+eval(formnm + "." + funcnm + "[i].id")+".checked=true");
		}
	}
}
function check_click(){
  document.form1.CREATEDATEGO.value="";
  document.form1.CREATEDATEER.value="";
  document.form1.CLIENTNAME.value="";
  document.form1.DUEDATEGO.value="";
  document.form1.DUEDATEER.value="";
}

function req(){
	//alert(document.all.pnstr.value);
	alert();
	form1.action="/query/common/qry_summary.jsp?CREATEDATEGO="+document.all.CREATEDATEGO.value+""+
	"&CREATEDATEER="+document.all.CREATEDATEER.value+""+
	"&CLIENTNAME="+document.all.CLIENTNAME.value+""+
	"&IDNO="+document.all.IDNO.value+""+
	"&BMNO="+document.all.BMNO.value+""+
	"&CNLNO="+document.all.CNLNO.value+""+
	"&DUEDATEGO="+document.all.DUEDATEGO.value+""+
	"&pn="+document.all.pnstr.value+""+
	"&DUEDATEER="+document.all.DUEDATEER.value+"";
	form1.submit();
}
function printform(){
	//form1.referValue.value=document.all.exportTable.innerHTML;
	if (<%=rows%>==0)
	{
		alert('没有可导出的数据！');
		return;
	}
	form1.document.all.s_main.value='<%=URLEncoder.encode(DBUtil.fromDB(s_main),"GBK")%>';
	form1.document.all.s_count.value='<%=URLEncoder.encode(DBUtil.fromDB(s_count),"GBK")%>';
	form1.document.all.s_where.value='<%=URLEncoder.encode(DBUtil.fromDB(s_where),"GBK")%>';
	window.open("/exporttoExcel.jsp"); 
}
setvalList('MOTYPE','<%=MOTYPE%>');
<%
if (arracclist!=null&&!arracclist.trim().equals("")){
String arr[]=arracclist.split(",");
for (int i=0;i<arr.length;i++){
 %>
setvalList('ACCLIST','<%=arr[i]%>');
<%}}%>
setvalList('LOANCAT3','<%=LOANCAT3%>');
setvalList('LOANCAT2','<%=LOANCAT2%>');
setvalList('LOANCAT1','<%=LOANCAT1%>');
function setvalList(name,val)
{
	var obj=document.getElementById(name);
	for(var i=0;i<obj.options.length;i++)
	{
		if (obj.options[i].value.Trim()==val)
		{
		    obj.options[i].selected=true;
			break;
		}
	}
}
function deptrefer_click() {
        var url = "view.jsp";
        var vArray;
        document.all.referValue.value = window.open(url, 0, "height=400,width=460,toolbar=no,scrollbars=yes,status");
        if (document.all.referValue.value == "undefined") {
        return;
        }

    }
    
/*
*   函数名   |      说明
*-----------------------------------
*   mOvr()   |  表格鼠标特效一
*
*/

function mOvr(src){
	if (!src.contains(event.fromElement)) {
	dataBgColor=src.bgColor;
	src.style.cursor = 'hand';
	src.bgColor = '#E7E9CF';
	}
}

/*
*   函数名   |      说明
*-----------------------------------
*   mOut()   |  表格鼠标特效二
*
*/

function mOut(src){
	if (!src.contains(event.toElement)) {
	src.style.cursor = 'default';
	src.bgColor = '#FFFFFF';
	}
}    
function mClk(val)
{
	window.open('q01020.jsp?BMNO='+val,'');
}
function cls()
{	
	var objs = document.form1.elements.length;
	for (var i=0;i<objs;i++)
	{
	   if ((typeof form1[i]) != "object")
        {
            continue;
        }
        var objectType = form1[i].type.toLowerCase(); 
        if (objectType == "text" || objectType == "hidden") 
        	form1[i].value="";
        if (objectType.search("select") >=0) 
        	form1[i].options[0].selected=true;
	}
}
function display(val)
{
	var obj=document.all.btn3;
	var display=document.all.displayval;
	if (val=='')
	{
		document.all.tblmain.style.display='none';
		display.value='none';
		obj.value=' 显 示 查 询 条 件 ';
	}
	else
	{
		document.all.tblmain.style.display='';
		display.value='';
	    obj.value=' 隐 藏 查 询 条 件 ';
	}
}
function displayAll(val)
{
	var obj=document.all.btn3;
	document.all.tblmain.style.display=val;
	document.all.displayval.value=val;
	if (val=='')
	{
		 obj.value=' 隐 藏 查 询 条 件 ';
	}
	else
	{
	    obj.value=' 显 示 查 询 条 件 ';
	}
}
</script>