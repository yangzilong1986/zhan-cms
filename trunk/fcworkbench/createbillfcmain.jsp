<%--
*******************************************************************
*    ��������:    createfcmain.jsp
*    �����ʶ:
*    ��������:    ��ʱ���弶����
*    ������ҳ:
*    ���ݲ���:
*    ��   ��:     JGO(GZL)
*    ��������:    2005-07-13
*    �� �� ��:    
*    �޸�����:    
*    ��    Ȩ:    �ൺ������Ϣ�������޹�˾
*******************************************************************
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cmsi.fc.*" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="com.zt.util.*" %>

<!--jsp:include page="/checkpermission.jsp"/-->
<script src='/js/fcmain.js' type='text/javascript'></script>

<%
String lname = null;
String billno = (String)request.getParameter("billno");
String confirmed = (String)request.getParameter("confirmed");
String msg = "";
int ret = 0;

UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
if ( um == null ) {
	msg = "δ���ֵ�ǰ�û�,�����µ�½!";
}
else
{
	lname= um.getUserName();


//request
FCParam fp = null;

if(billno == null || confirmed == null)
{
	ret = 100;	
	msg = "δȡ��Ʊ�ݺŵȲ�����Ϣ!";
}
else
{
	boolean conf = (confirmed.compareToIgnoreCase("1") == 0)? true: false;
	////System.out.println("confirmed========="+conf+"===="+billno);
	fp = fcmain.createBillFC(Integer.parseInt(billno),conf,lname);
	ret = fp.errorCode;
	if(ret == 1)
		msg = "Ʊ���Ѿ�����ֹ���̨��,��ȷ���Ƿ������µ��������?���Ժ�...";
	else if(ret == 0)
	{
	   msg = "succeed in creating the transaction!";
%>	
<script language="JavaScript" type="text/JavaScript">
	banli("<%=fp.fcNo%>","<%=fp.bmNo%>","<%=fp.brhId%>","<%=fp.operBrhId%>","<%=fp.fcCrtType%>","<%=fp.fcType%>","<%=fp.fcStatus%>","<%=fp.bmType%>","<%=fp.clientType%>");
	window.close();
</script>		
<%	    
	}
	else
		msg = "����!"+PropertyManager.getProperty(""+ret)+"[�������:"+ret+"]";	
}

}
%>

<script language="JavaScript" type="text/JavaScript">
function askyou(p_userid){
	if(confirm("���жһ�Ʊ�Ѿ�����ֹ���̨��,��ȷ���Ƿ������µ��������?")){
		document.all("form1").action="/fcworkbench/createbillfcmain.jsp?confirmed=1&billno=<%=billno%>";
		document.all("form1").submit();
	}
	else
		window.close();
}
</script>

<html>
	<head>
		<title>ȷ����Ϣ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="/css/platform.css" rel="stylesheet" type="text/css">
	<style type="text/css">
<!--
body {
	margin-top: 50px;
}
-->
</style></head>
<form action="" method="post" name="form1">
	<body background="/images/checks_02.jpg"  
	<%if(ret == 1) {%>
	onLoad="askyou()"
	<%}%>	
	>
  <table width="400" height="300" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9" onKeyDown="pressKeyDown(event.keyCode);" >
			<tr>
			    <td valign="top">
					<table width="100%">
						<tr height="70" bgcolor="#4477AA">
							<td background="../images/showinfo.jpg" valign="bottom"><b><font size="2">&nbsp;�жһ�Ʊ���ȷ����Ϣ</font></b></td>
						</tr>
					</table>
					<table width="100%" height="200" class='page_form_table'>
						<tr class='page_form_tr'>
							<td class='page_form_td'>&nbsp;
							<%=msg%>
							</td>
						</tr>
					</table>
					<table class='page_button_tbl' align="center">
						<tr class='page_button_tbl_tr'>
							<td class='page_button_tbl_td' align="center" valign="middle" height="30">
								<!--input class="list_button_active" type="button" name="ok" value=" ��һ�� " onClick="history.go(-1)"-->
								<input class="list_button_active" type="button" name="close" value=" �� �� " onClick="window.close();">
							</td>
						</tr>
					</table>
					</td>
			</tr>
	</table>
	</body>
</form>
</html>