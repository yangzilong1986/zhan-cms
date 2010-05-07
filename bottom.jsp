<%--
*******************************************************************
*    程序名称:    bottom.jsp
*    程序标识:
*    功能描述:    底页
*    连接网页:
*    传递参数:
*    作   者:     王海雷
*    开发日期:    2004-02-05
*    修 改 人:    wxj(添加登录用户的所在网点)
*    修改日期:    2004-02-05
*    版    权:    青岛中天信息技术有限公司
*
*
*******************************************************************
--%>
<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.cms.pub.SCUser" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<style type="text/css">
<!--
.style4 {font-size: 11px;
color:white;
}
-->
</style>
<body topmargin="0" rightmargin="0" leftmargin="0" bottommargin="0">
<%
UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
if ( um == null ) {
	um = new UserManager();
	session.setAttribute(SystemAttributeNames.USER_INFO_NAME,um);
}
String LGNNAME= um.getUserName();
String USRNAME= SCUser.getName(LGNNAME);
String BRHID 	= SCUser.getBrhId(LGNNAME);
String BRHNAME= SCBranch.getSName(BRHID);
BRHID=(BRHID	==null?"":BRHID);
BRHNAME=(BRHNAME	==null?"":BRHNAME);
%>
<html>
<head>
	<title>底页</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<body>
	<table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td height="100%" valign="bottom" nowrap background="images/bottom/bottom_1.jpg" class="style4">
	  当前用户：<%=USRNAME+"("+LGNNAME+")"%>&nbsp;&nbsp; 所属网点：<%= BRHNAME +"("+ BRHID +")"%></td>
	</tr>
	</table>
</body>
</html>
