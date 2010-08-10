<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.cmsi.pub.code.FCPrd" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%--
=============================================== 
Title: 清分时点设置增加保存页面
Description: 增加清分时点后保存功能。
 * @version   $Revision: 1.1 $  $Date: 2007/04/28 14:19:56 $
 * @author   
 * <p/>修改：$Author: liuj $
=============================================== 
--%>
<%
request.setCharacterEncoding("GBK");
ConnectionManager manager=ConnectionManager.getInstance();

boolean temp=false;
String DT=null;
String ENDDT=null;
  if(request.getParameter("DT")==null){
    DT=null;
  }
  else{
    DT=request.getParameter("DT").trim();
  }
  if(request.getParameter("ENDDT")==null){
    ENDDT=null;
  }
  else{
    ENDDT=request.getParameter("ENDDT").trim();
  }
String INITIALIZED=null;
  if(request.getParameter("INITIALIZED")==null){
    INITIALIZED=null;
  }
  else{
    INITIALIZED=request.getParameter("INITIALIZED").trim();
  }
  String COMMENT=null;
  if(request.getParameter("COMMENT")==null){
    COMMENT=null;
  }
  else{
    COMMENT=request.getParameter("COMMENT").trim();
  }


  String sql="insert into FCPRD values("+FCPrd.getNextNo()+","+DBUtil. toSqlDate(DT)+","+DBUtil. toSqlDate(ENDDT)+","+INITIALIZED+",'"+DBUtil.toDB(COMMENT)+"')";
  temp=manager.ExecCmd(sql);
  BMFCLimit.setDirty();
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/wjflset/Qftime_add.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/wjflset/Qftime_add.jsp"/>
	<%
  }
%>