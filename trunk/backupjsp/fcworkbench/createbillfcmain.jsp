<%--
*******************************************************************
*    程序名称:    createfcmain.jsp
*    程序标识:
*    功能描述:    非时点五级分类
*    连接网页:
*    传递参数:
*    作   者:     JGO(GZL)
*    开发日期:    2005-07-13
*    修 改 人:    
*    修改日期:    
*    版    权:    青岛中天信息技术有限公司
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
	msg = "未发现当前用户,请重新登陆!";
}
else
{
	lname= um.getUserName();


//request
FCParam fp = null;

if(billno == null || confirmed == null)
{
	ret = 100;	
	msg = "未取得票据号等参数信息!";
}
else
{
	boolean conf = (confirmed.compareToIgnoreCase("1") == 0)? true: false;
	////System.out.println("confirmed========="+conf+"===="+billno);
	fp = fcmain.createBillFC(Integer.parseInt(billno),conf,lname);
	ret = fp.errorCode;
	if(ret == 1)
		msg = "票据已经在清分工作台中,请确认是否增加新的清分任务?请稍候...";
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
		msg = "错误!"+PropertyManager.getProperty(""+ret)+"[错误代码:"+ret+"]";	
}

}
%>

<script language="JavaScript" type="text/JavaScript">
function askyou(p_userid){
	if(confirm("本承兑汇票已经在清分工作台中,请确认是否增加新的清分任务?")){
		document.all("form1").action="/fcworkbench/createbillfcmain.jsp?confirmed=1&billno=<%=billno%>";
		document.all("form1").submit();
	}
	else
		window.close();
}
</script>

<html>
	<head>
		<title>确认信息</title>
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
							<td background="../images/showinfo.jpg" valign="bottom"><b><font size="2">&nbsp;承兑汇票清分确认信息</font></b></td>
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
								<!--input class="list_button_active" type="button" name="ok" value=" 上一步 " onClick="history.go(-1)"-->
								<input class="list_button_active" type="button" name="close" value=" 关 闭 " onClick="window.close();">
							</td>
						</tr>
					</table>
					</td>
			</tr>
	</table>
	</body>
</form>
</html>