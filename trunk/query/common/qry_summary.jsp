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
Title: �弶����̨���ۺϲ�ѯ
Description: �弶����̨���ۺϲ�ѯ��
 * @version  $Revision: 1.6 $  $Date: 2007/06/20 09:40:27 $
 * @author   weiyb
 * <p/>�޸ģ�$Author: weiyb $
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
//String brhId = SCUser.getBrhId(um.getUserName()); //��¼�û���������
//ConnectionManager manager=ConnectionManager.getInstance();

 String CREATEDATEGO = request.getParameter("CREATEDATEGO");
 String CREATEDATEER = request.getParameter("CREATEDATEER");
 String DUEDATEGO = request.getParameter("DUEDATEGO");
 String DUEDATEER = request.getParameter("DUEDATEER");
 String arracclist=request.getParameter("arracclist");
 if (arracclist!=null&&!arracclist.trim().equals(""))arracclist=arracclist.trim();
 
 String BRHID = request.getParameter("BRHID");//�����
 String branchname=request.getParameter("branchname");//��������
 String display=request.getParameter("displayval");//��ѯ���Ƿ���ʾ
 String CLIENTNAME = request.getParameter("CLIENTNAME");//�ͻ�����
 String FIRSTRESP = request.getParameter("FIRSTRESP");//��һ������
 String CLIENTMGR=request.getParameter("CLIENTMGR");// �ͻ�����
 String CNLNO = request.getParameter("CNLNO");//����������
 String MOTYPE=request.getParameter("MOTYPE");//ҵ������
 String ACCLIST=request.getParameter("ACCLIST");//��Ŀ
 String LOANCAT2=request.getParameter("LOANCAT2");//�ļ�������̬
 String LOANCAT1=request.getParameter("LOANCAT1");//�弶������̬
 String LOANCAT3=request.getParameter("LOANCAT3");//������;


if(CREATEDATEGO != null && CREATEDATEGO.trim().length() <= 0) CREATEDATEGO = null;
if(CREATEDATEER != null && CREATEDATEER.trim().length() <= 0) CREATEDATEER = null;
if(DUEDATEGO != null && DUEDATEGO.trim().length() <= 0) DUEDATEGO = null;
if(DUEDATEER != null && DUEDATEER.trim().length() <= 0) DUEDATEER = null;

if(CLIENTNAME != null && CLIENTNAME.trim().length() <= 0) CLIENTNAME = null;//�ͻ�����
if(BRHID != null && BRHID.trim().length() <= 0) BRHID = null;//������
if(FIRSTRESP != null &&( FIRSTRESP.trim().length() <= 0||FIRSTRESP.equals("0"))) FIRSTRESP = null;//��һ������
if (CLIENTMGR!=null &&(CLIENTMGR.trim().length()<=0||CLIENTMGR.equals("0"))) CLIENTMGR=null;//�ͻ�����
if(CNLNO != null && CNLNO.trim().length() <= 0) CNLNO = null;//����������


//��ҳ
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
if(CLIENTNAME != null) s_where += " and rq.CLIENTNAME like '%"+DBUtil.toDB(CLIENTNAME.trim())+"%'";//�ͻ�
//if(FIRSTRESP != null) s_where += " and su.USERNAME like '%"+DBUtil.toDB(FIRSTRESP.trim())+"%'";//��һ������
//if (CLIENTMGR!=null) s_where+=" and su1.USERNAME like '%"+DBUtil.toDB(CLIENTMGR.trim())+"%'";//�ͻ�����
if(FIRSTRESP != null) s_where += " and rq.FIRSTRESP='"+DBUtil.toDB(FIRSTRESP.trim())+"'";//��һ������
if (CLIENTMGR!=null) s_where+=" and rq.CLIENTMGR='"+DBUtil.toDB(CLIENTMGR.trim())+"'";//�ͻ�����
if(CNLNO != null) s_where += " and exists(select 1 from bmpldgsecurity where rq.BMNO=BMNO and clientname like '%"+DBUtil.toDB(CNLNO.trim())+"%')";//����������
if(DUEDATEGO != null) s_where += " and rq.ENDDATE >= "+DBUtil.toSqlDate(DUEDATEGO)+"";
if(DUEDATEGO != null && DUEDATEER != null) s_where += " and rq.ENDDATE >= "+DBUtil.toSqlDate(DUEDATEGO)+" and rq.ENDDATE <= "+DBUtil.toSqlDate(DUEDATEER)+"";
if (MOTYPE!=null&&!MOTYPE.trim().equals(""))s_where+=" and rq.TYPENO="+MOTYPE+"";//ҵ������
if (arracclist!=null&&!arracclist.trim().equals(""))s_where+=" and rq.ACCNO in ('"+arracclist.replaceAll(",","','")+"')";//��Ŀ
if (LOANCAT2!=null&&!LOANCAT2.trim().equals(""))s_where+=" and rq.LOANCAT2="+LOANCAT2+"";//�ļ�����
if (LOANCAT1!=null&&!LOANCAT1.trim().equals(""))s_where+=" and rq.LOANCAT1="+LOANCAT1+"";//�弶����
if (LOANCAT3!=null&&!LOANCAT3.trim().equals(""))s_where+=" and rq.LOANCAT3="+LOANCAT3+"";//������;
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
			sumRs=DB2_81.getRs(s_sum+s_where);//���һҳ,����ϼ���
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
<title>�Ŵ�����</title>
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
       alert("��ѡ�����㣡");
       brhname.focus();
       return false;
    }
    /*
    if(date.value==null){
      alert("��ѡ�����ڣ�");
      date.focus();
      return false;
   }
   if (!isDate(date.value)) {
      alert("���ڸ�ʽ����");
      date.focus();
      return false;
   }
   */
   var str='���ݼ����С�����������';
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
<option value="">��ѡ��</option>
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
                                          <td height="19"  >����ѡ��</td><td><input type="text"
															name="BRHID" id='BRHID' value="<%=BRHID==null ? "":BRHID %>" class="query_form_text" e="25"
															readonly> <input type="button" name="nameref"
															value="��" onclick="deptrefer_click()"
															class="button">
                                          </td><td>��������</td><td>
                                           <input type="text" name="CREATEDATEGO" readonly class="input" value="<%=CREATEDATEGO==null?"":CREATEDATEGO%>"   size="10">
										   <input name="button" type="button" class="button"   onclick="setday(this,winform.CREATEDATEGO)" value="��">
										   ��
                                           <input type="text"  class="input" readonly  name="CREATEDATEER" value="<%=CREATEDATEER==null?"":CREATEDATEER%>"    size="10">
										   <input name="button"  class="button"  type="button"   onclick="setday(this,winform.CREATEDATEER)" value="��">
									     </td><td>
									     	��������
									     	</td><td >
                                           <input type="text" name="DUEDATEGO" readonly class="input"  value="<%=DUEDATEGO==null?"":DUEDATEGO%>"   size="10">
										   <input name="button" type="button"   class="button"  onclick="setday(this,winform.DUEDATEGO)" value="��">
											��
                                           <input type="text" name="DUEDATEER" readonly class="input"  value="<%=DUEDATEER==null?"":DUEDATEER%>"    size="10">
										   <input name="button" type="button"   class="button"  onclick="setday(this,winform.DUEDATEER)" value="��">
										   <td>����������</td>
										   <td><input type="text"  class="input"  name="CNLNO" value="<%=CNLNO==null?"":CNLNO%>" size="12"></td>
					      				</tr>
										<tr  align=left bgcolor="#ffffff">
                                          <td height="19" nowrap >�ͻ�����</td><td>
                                          <input type="text"  class="input"  name="CLIENTNAME" value="<%=CLIENTNAME==null?"":CLIENTNAME%>"    size="28">
                                         </td><td>��һ������</td><td>
                                         <select  id="FIRSTRESP"  name="FIRSTRESP"  >
                                         <option value="0">��ѡ��</option>
                                         <%if (BRHID!=null&&!BRHID.equals("")){out.write(upxml.getUserNameList(BRHID));}%>
                                         </select>
                                         <script language="javascript">
											setVal(document.all.FIRSTRESP,'<%=DBUtil.fromDB2(FIRSTRESP)%>');
										</script>
                                         &nbsp;&nbsp;&nbsp;&nbsp;
                                    </td><td>
                                          ��;</td><td>
                                          <select id="LOANCAT3" name="LOANCAT3">
                                           <option value="">��ѡ��</option>
                                         <%=qry.getLoanCat3()%>
                                          </select></td><td>
          								�ļ���̬</td><td>
                                         <select id="LOANCAT2"  name="LOANCAT2">
                                         <option value="">��ѡ��</option>
                                        <%=qry.getLoanCat2() %>
                                         </select>                                          
										   </td>
                                         </tr>
                                         <tr  align=left bgcolor="#ffffff">
                                         
                                         <td>�ͻ�����</td><td>
                                         <select  id="CLIENTMGR" name="CLIENTMGR" >
                                          	<option value="0">��ѡ��</option>
                                              <%if (BRHID!=null&&!BRHID.equals("")){out.write(upxml.getUserNameList(BRHID));}%>
                                         </select>
                                         <script language="javascript">
											setVal(document.all.CLIENTMGR,'<%=DBUtil.fromDB2(CLIENTMGR)%>');
										</script>
                                         </td><td>
                                          ��Ŀ</td><td>
                                       	<input class="button" name="btnSelCategory" value="ѡ���Ŀ" type="button" onclick="getCategory();">
                                    </td><td>
                                         ҵ������</td><td>
                                          <select id="MOTYPE" name="MOTYPE">
                                           <option value="">��ѡ��</option>
                                         <%=qry.getType()%>
                                          </select>
                                       </td><td>
                                         �弶����</td><td>      
                                         <select id="LOANCAT1" name="LOANCAT1">
                                         <option value="">��ѡ��</option>
                                         <%=qry.getLoanCat1() %>
                                         </select>
										</td>
                                        </tr>
                                        </tbody>
                    </table>
<div id="showTable" align="left" width="100%">
<input class="button" type="button" name="btn0" id ='Submit1' value=" �� �� " onclick="query();">
<input class="button" type="button"  name="btn2" value=" �� �� " onclick="cls();">
<input class="button" type="button"  name="btn1" value=" �� ӡ " onclick="printform();">
<input class="button" type="button"  name="btn3" value=" �� �� �� ѯ �� �� " onclick="display(document.all.tblmain.style.display);">
</div>
<div id="showTable" align="center" width="100%">
         <div align="center" class=caption>����̨���ۺ��嵥</div>
</div>
<div id="showTable" align="left" width="100%">
<span  id='branchname1' name="branchname1">��������:<%=branchname==null?"":branchname%></span>
</div>
<%if (crs!=null){ %>
                        <table  width=100%  align='center' cellSpacing=1 cellPadding=1 border=0 id='exportTable' class=table>
                            <tr class=head >
                              <!--  <td nowrap align="center">��������</td>-->
                              <td  nowrap align="center">ҵ������</td>
                              <td nowrap align="center">�����ʺ�</td>
                              <td nowrap align="center">��Ŀ��</td>
                              <td nowrap align="center">��ݺ�</td>
                              <td nowrap align="center">������</td>
                              <td nowrap align="center">�ͻ�����</td>
							  <td nowrap align="center">�ͻ�����</td>
                              <td  nowrap align="center">������ʽ</td>
                              <!--  
                              <td  nowrap align="center">����������</td>
							  <td  nowrap align="center">�����˴���</td>
							  -->
                              <td  nowrap align="center">�ļ���̬</td>
                              <td   nowrap align="center">�弶����</td>
							  <td  nowrap align="center">������</td>
                              <td   nowrap align="center">չ�ڵ�����</td>
                              <td   nowrap align="center">�����;</td>
                              <td   nowrap align="center">����</td>
                              <td   nowrap align="center">��ͬ���</td>
                              <td  nowrap align="center"> ������</td>
                              <td  nowrap align="center"> ǷϢ���</td>
                              <!-- <td   nowrap align="center">������</td>-->
                              <td   nowrap align="center">��һ������</td>
                              <td   nowrap align="center">���ν��</td>
                              <td   nowrap align="center">ҵ���</td>
                            </tr>
<%
		while(crs.next()){
%>
                            <tr   bgcolor="#ffffff" onmouseover="mOvr(this)" onclick="mClk('<%=crs.getString("BMNO") %>');" onmouseout="mOut(this);">
                              <!--<td nowrap  align="left"  ><div title='<%=crs.getString("BRHID") %>' ><%=DBUtil.fromDB2(crs.getString("LNAME"))%></div></td> //�������� -->
                              <td nowrap align="left" ><%=DBUtil.fromDB2(crs.getString("TYPENAME"))%></td><!-- ҵ������ -->
                              <td nowrap align="center" ><%=crs.getString("ACTNO")%></td><!-- �����ʺ� -->
                              <td nowrap align="center" ><div title='<%=DBUtil.fromDB(crs.getString("ACCNAME"))%>'><%=crs.getString("ACCNO")%> </div></td><!-- ��Ŀ�� -->
                              <td nowrap align="center" ><%=crs.getString("CNLNO")%></td><!-- ��ݺ� -->
                              <td nowrap align="center" ><%=crs.getString("PAYDATE")%></td><!-- �������� -->
                              <td nowrap  align="left"> <%=crs.getString("CLIENTNO")==null?"":DBUtil.fromDB(crs.getString("CLIENTNO"))%></td><!--//���������  -->
							  <td nowrap align="left"><%=crs.getString("CLIENTNAME")==null?"":DBUtil.fromDB(crs.getString("CLIENTNAME"))%></td><!--//����˴���  -->
                              <td nowrap align="center"><%=crs.getString("LOANTYPE3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANTYPE3NAME"))%></td><!--//������ʽ  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("CLIENTNAME")%></td>//����������  -->
							  <!--<td nowrap align="left"><%//=crs.getString("ID")%></td>//�����˴���  -->
                              <td nowrap align="center"><%=DBUtil.fromDB2(crs.getString("LOANCAT2NAME"))%></td><!--//�ļ�������̬  -->
                              <td nowrap  align="center"><%=DBUtil.fromDB2(crs.getString("LOANCAT1NAME"))%></td><!--//�弶������̬  -->
							  <td nowrap align="left"><%=crs.getString("ENDDATE")%></td><!--//������  -->
                              <td nowrap  align="left"><%=crs.getString("ISEXTEND").equals("1")?crs.getString("NOWENDDATE"):""%></td><!--//չ�ڵ�����  -->
                              <td nowrap  align="left"><%=crs.getString("LOANCAT3NAME")==null?"":DBUtil.fromDB(crs.getString("LOANCAT3NAME"))%></td><!--//�����;  -->
                              <td nowrap  align="right"><%=crs.getBigDecimal("BRATE")%></td><!--//����  -->
                               <td nowrap  align="right"><%=df.format(crs.getBigDecimal("CONTRACTAMT"))%></td><!--//��ͬ���  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("NOWBAL"))%></td><!--//�����  -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("lixi")==null?new BigDecimal(0.00):crs.getBigDecimal("lixi"))%></td><!--//ǷϢ���  -->
                              <!--<td nowrap  align="left"><%//=crs.getString("")%></td>//������  -->
                              <td nowrap  align="center"><%=crs.getString("USERNAME")==null?"":DBUtil.fromDB(crs.getString("USERNAME"))%></td><!-- //��һ������ -->
                              <td nowrap  align="right"><%=df.format(crs.getBigDecimal("FISRTRESPPCT"))%></td><!--//���ν��  -->
                              <td nowrap  align="right"><%=crs.getString("BMNO")%></td><!--//ҵ��� -->
                            </tr>
<%
}
if(rows<=end&&rows>0){
while(sumRs.next()){
%>
                            <tr   bgcolor="#ffffff" >
                              <td nowrap align="left" colspan='14'>�ϼ�</td><!-- ҵ������ -->
                               <td nowrap  align="right"><%=df.format(sumRs.getBigDecimal("T_CONTRACTAMT"))%></td><!--//��ͬ���  -->
                              <td nowrap  align="right"><%=df.format(sumRs.getBigDecimal("T_NOWBAL"))%></td><!--//�����  -->
                              <td nowrap  align="right"></td><!--//ǷϢ���  -->
                              <td nowrap  align="center"></td><!-- //��һ������ -->
                              <td nowrap  align="right"><%=df.format(sumRs.getBigDecimal("T_FISRTRESPPCT"))%></td><!--//���ν��  -->
                              <td nowrap  align="right"></td><!--//ҵ��� -->
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
document.title="����̨���ۺϲ�ѯ";
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
		alert('û�пɵ��������ݣ�');
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
*   ������   |      ˵��
*-----------------------------------
*   mOvr()   |  ��������Чһ
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
*   ������   |      ˵��
*-----------------------------------
*   mOut()   |  ��������Ч��
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
		obj.value=' �� ʾ �� ѯ �� �� ';
	}
	else
	{
		document.all.tblmain.style.display='';
		display.value='';
	    obj.value=' �� �� �� ѯ �� �� ';
	}
}
function displayAll(val)
{
	var obj=document.all.btn3;
	document.all.tblmain.style.display=val;
	document.all.displayval.value=val;
	if (val=='')
	{
		 obj.value=' �� �� �� ѯ �� �� ';
	}
	else
	{
	    obj.value=' �� ʾ �� ѯ �� �� ';
	}
}
</script>