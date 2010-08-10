<%@ page contentType="text/html; charset=GBK"  errorPage="error.jsp"%>
<%@page import=" zt.cmsi.fc.FcUpXML"%>
<jsp:useBean id="form1" scope="page" class="zt.cmsi.fc.form.FormZrrnh"  />
<jsp:setProperty name="form1" property="*"  />
<%--
=============================================== 
Title: 修改自然人一般贷款和消费类贷款分类信息明细
Description: 修改自然人一般贷款和消费类贷款分类信息明细。
 * @version  $Revision: 1.1 $  $Date: 2007/05/16 01:59:10 $
 * @author   weiyb
 * <p/>修改：$Author: weiyb $
=============================================== 
--%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
FcUpXML fc= new FcUpXML();
String fcno=request.getParameter("fcno");
form1.setFcno(fcno);
boolean ret=fc.updateZrrnh(form1);
response.sendRedirect("upzrrnh_detail.jsp?fcno="+form1.getFcno()+"&flag="+ret);
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