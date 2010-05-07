<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>


<html>
	<head>
		<title>海尔财务公司信贷管理系统</title>
		<link href="../css/platform.css" rel="stylesheet" type="text/css">
	</head>
	<body background="../images/checks_02.jpg">
		<% ServiceProxy sp = new SeviceProxyHttpImpl(); sp.service(request,response);%>
	     <table width="580" height="400" border="" align="center" bordercolorlight="#336699" bordercolordark="#336699" bgcolor="#9DBBD9" class="main_table">
			<tr align="left">
			     <td width="100%" height="30" class="form_title_label">
					<%=sp.getHead()%>
				</td>
			</tr>
			<tr align="center" valign="middle">
			     <td width="580" height="350">
					<%=sp.getBody()%>
				</td>
			</tr>
			<tr align="center" valign="bottom">
			     <td width="100%">
					<%=sp.getSysButton()%>
				</td>
			</tr>
	</table>
	</body>
</html>
