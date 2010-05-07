<%@ page contentType="text/html; charset=GBK" errorPage="/fc/error.jsp"%>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.cmsi.fc.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String clientno=request.getParameter("CLIENTNO");
String sql="select * from fcqycw where clientno='"+clientno+"'";

	//CachedRowSet crs=manager.getRs(sql);
	String pnStr = request.getParameter("pn");
	if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
	int pn=Integer.parseInt(pnStr==null?"1":pnStr);
	int ps=10;
	Vector vec=DB2_81.getPageRs(sql,pn,ps);
	int rows=((Integer)vec.get(0)).intValue();
	CachedRowSet crs=(CachedRowSet)vec.get(1);
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");
UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
String userid=um.getUserId();
String usertype="";
CachedRowSet rs=DB2_81.getRs("select SCUSER.USERNO,SCUSER.USERNAME,SCUSER.LOGINNAME,SCUSER.BRHID,SCUSER.USERSTATUS,SCUSER.USERTYPE,SCUSER.USERJOB,SCUSER.USERPWD from SCUSER where SCUSER.USERNO = "+userid);
while (rs.next())
{
	usertype=rs.getString("usertype");
}
String readonly="";
String submit="";
/*
if(FCSTATUS.equals("1")){
  readonly="";
  submit="class='page_button_active'";
}
if(FCSTATUS.equals("2")){
  readonly="readonly";
  submit="disabled='true' class='page_button_disabled'";
}
if(FCSTATUS.equals("3")){
  readonly="readonly";
  submit="disabled='true' class='page_button_disabled'";
}
*/
 %>



<html>
<head>
<base href="<%=basePath%>">
<title>信贷管理</title>
<link href="css/platform.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
body {
	margin-top: 5px;
}
-->
</style>
<script language="JavaScript" type="text/JavaScript">
function newAffair(affairName){
	var flag=0;
	if(affairName=='add'){
		var url="/fcworkbench/qycw/qycw_add.jsp?CLIENTNO=<%=clientno%>";
		window.open(url,'FI5','height=768,width=700,toolbar=no,scrollbars=yes,resizable=yes');
	}
	else{
		
		for(i=0;i<form1.elements.length;i++){
			if(form1.elements[i].checked == true){}
			else{
				flag= flag+1;
			}
		}
		if(flag>=form1.elements.length){
			alert("请选择要删除的纪录！");
			return false;
		}
		else{
			if(confirm('确定删除吗？')){
				form1.action='/fcworkbench/qycw/qycw_del.jsp';
				form1.submit();
			}
			else{return false;}
		
		}
	}
}
function info(CLIENTNO,DT){
		var url='/fcworkbench/qycw/qycw_info.jsp?CLIENTNO='+CLIENTNO+'&DT='+DT;
		window.open(url,'FI4','height=600,width=700,toolbar=no,scrollbars=yes,resizable=yes');
}
</script>
<script src='js/check.js' type='text/javascript'></script>
<script src='js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="js/flippage.js"></script>
</head>
<body background="images/checks_02.jpg">
<form action="" name="form1" method="post" >
<input type="hidden" id="pnstr" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
<input type="hidden" name="CLIENTNO" value="<%=clientno%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle">
      <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" width="100%">
        <tr align="left">
          <td height="30" bgcolor="#4477AA"> <img src="images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>主要财务指标</b></font> <img src="/images/form/xing1.jpg" align="absmiddle"></td>
        </tr>
        <tr align="center">
          <td height="260" valign="middle">
            <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
              <tr>
                <td width="20">&nbsp;</td>
                <td align="center" valign="middle">
                  <script src='js/querybutton.js' type='text/javascript'></script>
                  <script src='js/meizzDate.js' type='text/javascript'></script>
                  <table>
                    <tr>
                      <td>
                        <table cellpadding='0' cellspacing='0' border='0'>
                          <tr>
                            <td height='5'></td>
                          </tr>
                        </table>
                        <table class='list_form_table' width='530' align='center' cellpadding='0' cellspacing='1' border='0'>
                            <tr class='list_form_title_tr'>
							 <td   nowrap width="5%" align="center"><input name="f02" type='checkbox' id="f02" onClick="checkSub('f02','form1','elements')" value="1"></td>
                              <td width='13%' class='list_form_title_td' nowrap align="center">日期（年月）</td>
                              <td width='15%' class='list_form_title_td' nowrap align="center">净现金流量</td>
							  <td width='15%' class='list_form_title_td' nowrap align="center">流动比率</td>
							  <td width='15%' class='list_form_title_td' nowrap align="center">资产负债率</td>
							  <td width='15%' class='list_form_title_td' nowrap align="center">资产利润率</td>
							  <td width='7%' class='list_form_title_td' nowrap align="center">详细</td>
							  </tr>
                            <%
int j=0;
while(crs.next()){
%>
                            <tr class='list_form_tr'>
							<td align='center' nowrap><input name="DT" type='checkbox' id="f020<%=j++%>" value="<%=crs.getString("DT")%>"></td>
                              <td nowrap class='list_form_td' align="center"><%=DBUtil.to_Date(crs.getString("DT")).substring(0,DBUtil.to_Date(crs.getString("DT")).length()-2)%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("CUZB1")==null?"":df.format(crs.getBigDecimal("CUZB1"))%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("CUZB5")==null?"":df.format(crs.getBigDecimal("CUZB5"))%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("CUZB6")==null?"":df.format(crs.getBigDecimal("CUZB6"))%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("CUZB8")==null?"":df.format(crs.getBigDecimal("CUZB8"))%></td>
							  <td nowrap class='list_form_td' align="center"><a class="list_edit_href" href='javascript:info("<%=clientno%>","<%=crs.getString("DT")%>")' >详细</a></td>
							  </tr>
                            <%
}

for(int i = crs.size();i<10;i++){
%>
        
                            <tr class='list_form_tr'>
							  <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
							  <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
							  <td nowrap class='list_form_td'></td>
                              <td nowrap class='list_form_td'></td>
							  <td nowrap class='list_form_td'></td>
                            </tr>
                            <%

}
%>
                      </table></td>
                    </tr>
                </table></td>
                <td width="20">&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr height="35" align="center" valign="middle">
          <td align="center">
            <table border="0" cellspacing="0" cellpadding="0" width="538">
              <tr>
                <td nowrap align="left">
             
                  <table class='list_button_tbl'>
                    <tr class="list_button_tbl_tr">
                      <td class="list_form_button_td"><input name='add' <%=(usertype!=null&&!usertype.equals("2"))?"class='page_button_disabled' disabled":"class='page_button_active'" %> type='button' <%=submit%> id="add" onClick="newAffair(this.name);" value=' 增加 '></td>
                      <td class="list_form_button_td"><input name='del' <%=(usertype!=null&&!usertype.equals("2"))?"class='page_button_disabled' disabled":"class='page_button_active'" %> type='button' <%=submit%> id="del" onClick="newAffair(this.name);" value=' 删除 '></td>
                      <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active' value=' 刷新 '  onclick="return req();"></td>
					<td class="list_form_button_td"><input type='submit' name='a' class='list_button_active' value=' 关闭 '  onclick="window.close();"></td>
					<script language="javascript">
    				createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"/fcworkbench/qycw/qycw_list.jsp?pn=","form1");
					</script>
                    </tr>
                </table></td>
              </tr>
          </table></td>
        </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
<script language="javascript">
document.title="主要财务指标";
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
  document.form1.FCCLASS.value="";
}
function req(){
	document.location.replace("/fcworkbench/q_zycwzb/q_zycwzb.jsp");
}
</script>
