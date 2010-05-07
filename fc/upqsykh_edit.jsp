<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@page import=" zt.cmsi.fc.FcUpXML"%>
<jsp:useBean id="form1" scope="page" class="zt.cmsi.fc.form.FormQsykh"  />
<jsp:setProperty name="form1" property="*"  />
<%--
=============================================== 
Title: 修改企事业贷款分类信息明细
Description: 修改企事业贷款分类信息明细。
 * @version  $Revision: 1.1 $  $Date: 2007/05/16 01:59:10 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
FcUpXML fc= new FcUpXML();
String serialno=request.getParameter("serialno");
form1.setSerialno(serialno);
boolean ret=fc.updateQsykh(form1);
response.sendRedirect("upqsykh_detail.jsp?serialno="+form1.getSerialno()+"&flag="+ret);
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
  </body>
</html>
