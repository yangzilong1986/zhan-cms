<%@ page contentType="text/html; charset=GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%--
=============================================== 
Title: 自然人一般贷款和消费类贷款分类信息维护
Description: 自然人一般贷款和消费类贷款分类信息维护
 * @version  $Revision: 1.1 $  $Date: 2007/05/16 01:59:09 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
<body>
<iframe name="ifrmMain" src="fc/upzrrnh_list.jsp"  height="35%" width="100%" frameborder="0" scrolling="auto" align="top"></iframe>
<iframe name="ifrmDetail" src="fc/upzrrnh_detail.jsp" height="60%" width="100%" frameborder="0" scrolling="auto" align="top"></iframe>
</body>
</html>
