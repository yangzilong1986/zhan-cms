<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<html>
<head>
	<title>�Ŵ�����ϵͳ-�ο�����</title>
	<link href="../css/platform.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#ffffff">
<% ServiceProxy sp = new SeviceProxyHttpImpl(); sp.service(request,response);%>
<%=sp.getHead()%>
<%=sp.getBody()%>
<%=sp.getSysButton()%>
</body>
</html>
