<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%
String FCNO=request.getParameter("FCNO");
String FCSTATUS=request.getParameter("FCSTATUS");
  if(FCNO == null || FCSTATUS==null)
  {
    session.setAttribute("lettermess","û�з��ִ�����Ĳ�����");
    response.sendRedirect("/fcworkbench/error.jsp");
  }
request.setCharacterEncoding("GBK");
//request.setCharacterEncoding("GBK");
//UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//String brhId = SCUser.getBrhId(um.getUserName()); //��¼�û���������
ConnectionManager manager=ConnectionManager.getInstance();

String sql="select * from FCMCMT where FCNO='"+FCNO+"' and FCCMTTYPE=10";

	//CachedRowSet crs=manager.getRs(sql);
	String pnStr = request.getParameter("pn");
	if(pnStr == null || pnStr.trim().length() <= 0) pnStr = null;
	int pn=Integer.parseInt(pnStr==null?"1":pnStr);
	int ps=10;
	Vector vec=manager.getPageRs(sql,pn,ps);
	int rows=((Integer)vec.get(0)).intValue();
	CachedRowSet crs=(CachedRowSet)vec.get(1);
	DecimalFormat df=new DecimalFormat("###,###,###,##0.00");

String readonly="";
String submit="";
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
 %>



<html>
<head>
<title>�Ŵ�����</title>
<link href="/css/platform.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
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
		var url="q_zycwzb_add.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>";
		window.open(url,'FI3','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
	}
	else{
		
		for(i=0;i<form1.elements.length;i++){
			if(form1.elements[i].checked == true){}
			else{
				flag= flag+1;
			}
		}
		if(flag>=form1.elements.length){
			alert("��ѡ��Ҫɾ���ļ�¼��");
			return false;
		}
		else{
			if(confirm('ȷ��ɾ����')){
				form1.action='q_zycwzb_del.jsp';
				form1.submit();
			}
			else{return false;}
		
		}
	}
}
function info(FCNO,DT,FCSTATUS){
		var url='q_zycwzb_info.jsp?FCNO='+FCNO+'&DT='+DT+'&FCSTATUS='+FCSTATUS;
		window.open(url,'FI4','height=500,width=600,toolbar=no,scrollbars=yes,resizable=yes');
}
</script>
<script src='/js/check.js' type='text/javascript'></script>
<script src='/js/pagebutton.js' type='text/javascript'></script>
<script language="javascript" src="/js/flippage.js"></script>
</head>
<body background="/images/checks_02.jpg">
<form action="" name="form1" method="post" >
<input type="hidden" name="pnstr" value="<%=pnStr==null?"1":pnStr%>">
<input type="hidden" name="FCNO" value="<%=FCNO%>">
<input type="hidden" name="FCSTATUS" value="<%=FCSTATUS%>">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" valign="middle">
      <table height="325" border="2" align="center" cellpadding="2" cellspacing="2" bordercolor="#006699" bgcolor="AACCEE" >
        <tr align="left">
          <td height="30" bgcolor="#4477AA"> <img src="/images/form/xing1.jpg" align="absmiddle"> <font size="2" color="#FFFFFF"><b>��Ҫ����ָ��</b></font> <img src="/images/form/xing1.jpg" align="absmiddle"></td>
        </tr>
        <tr align="center">
          <td height="260" valign="middle">
            <table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
              <tr>
                <td width="20">&nbsp;</td>
                <td align="center" valign="middle">
                  <script src='/js/querybutton.js' type='text/javascript'></script>
                  <script src='/js/meizzDate.js' type='text/javascript'></script>
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
                              <td width='13%' class='list_form_title_td' nowrap align="center">���ڣ����£�</td>
                              <td width='15%' class='list_form_title_td' nowrap align="center">���ֽ�����</td>
							  <td width='15%' class='list_form_title_td' nowrap align="center">��������</td>
							  <td width='15%' class='list_form_title_td' nowrap align="center">�ʲ���ծ��</td>
							  <td width='15%' class='list_form_title_td' nowrap align="center">�ʲ�������</td>
							  <td width='7%' class='list_form_title_td' nowrap align="center">��ϸ</td>
							  </tr>
                            <%
int j=0;


while(crs.next()){
%>
                            <tr class='list_form_tr'>
							<td align='center' nowrap><input name="DT" type='checkbox' id="f020<%=j++%>" value="<%=crs.getString("DT")%>"></td>
                              <td nowrap class='list_form_td' align="center"><%=DBUtil.to_Date(crs.getString("DT")).substring(0,DBUtil.to_Date(crs.getString("DT")).length()-2)%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("AMT1")==null?"":df.format(crs.getBigDecimal("AMT1"))%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("AMT3")==null?"":df.format(crs.getBigDecimal("AMT3"))%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("AMT4")==null?"":df.format(crs.getBigDecimal("AMT4"))%></td>
							  <td nowrap class='list_form_td' align="right"><%=crs.getBigDecimal("AMT6")==null?"":df.format(crs.getBigDecimal("AMT6"))%></td>
							  <td nowrap class='list_form_td' align="center"><a class="list_edit_href" href='javascript:info("<%=FCNO%>","<%=crs.getString("DT")%>","<%=FCSTATUS%>")' >��ϸ</a></td>
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
                      <td class="list_form_button_td"><input name='add' type='button' <%=submit%> id="add" onClick="newAffair(this.name);" value=' ���� '></td>
                      <td class="list_form_button_td"><input name='del' type='button' <%=submit%> id="del" onClick="newAffair(this.name);" value=' ɾ�� '></td>
                      <td class="list_form_button_td"><input type='submit' name='a' class='list_button_active' value=' ˢ�� '  onclick="return req();"></td>
					<td class="list_form_button_td"><input type='submit' name='a' class='list_button_active' value=' �ر� '  onclick="window.close();"></td>
					<script language="javascript">
    				createFlipPage(<%=pn%>,<%=ps%>,<%=rows%>,"q_zycwzb.jsp?pn=","form1");
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
document.title="��Ҫ����ָ��";
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

<%
//MyDB.getInstance().removeCurrentThreadConn("workbench.list(END).jsp"); //added by JGO on 2004-07-17
%>