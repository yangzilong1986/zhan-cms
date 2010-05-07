<%@ page language="java"  pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
String basePath = request.getContextPath();
%>
<html>
	<head>
		<title>树列表展示</title>
		<%--<link rel="stylesheet" type="text/css" href="<%=basePath%>/scripts/ext-3.0.0/resources/css/ext-all.css">--%>
		<%--<script type="text/javascript" src="<%=basePath%>/scripts/ext-3.0.0/adapter/ext/ext-base.js"></script>--%>
		<%--<script type="text/javascript" src="<%=basePath%>/scripts/ext-3.0.0/ext-all.js"></script>--%>
		<link rel="stylesheet" type="text/css" href="/js/ext/resources/css/ext-all.css">
		<script type="text/javascript" src="/js/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="/js/ext/ext-all.js"></script>

		<script type="text/javascript" src="treedemo.js"></script>
	</head>
	<body>
		<form id="wsd-siteForm" method="post">
			<p><input type="button" value="树窗口" onclick="Wsd.fn.common.TreeDemo.createWindow();" /></p>
			
		</form>
	</body>
</html>
