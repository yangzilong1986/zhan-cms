<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<html>
<head>
	<title>信贷管理系统-参考窗口</title>
	<link href="../css/platform.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#ffffff">
<% ServiceProxy sp = new SeviceProxyHttpImpl(); sp.service(request,response);%>
<%=sp.getHead()%>
<%=sp.getBody()%>
<%=sp.getSysButton()%>
</body>
</html>
