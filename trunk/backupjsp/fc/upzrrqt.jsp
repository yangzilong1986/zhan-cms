<%@ page contentType="text/html; charset=GBK"%>
<%--
=============================================== 
Title: 自然人其他和微型企业分类信息维护
Description: 自然人其他和微型企业分类分类信息维护
 * @version  $Revision: 1.1 $  $Date: 2007/05/16 01:59:09 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

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
<iframe name="ifrmMain" src="fc/upzrrqt_list.jsp" height="30%" width="100%" frameborder="0" scrolling="auto" align="top"></iframe>
<iframe name="ifrmDetail" src="fc/upzrrqt_detail.jsp" height="110%" width="100%" frameborder="0" scrolling="auto" align="top"></iframe>
  </body>
</html>
