<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%--
=============================================== 
Title: 清分时点设置详细信息修改保存页面
Description: 实现每条清分时点设置的详细信息的修改保存功能。
 * @version   $Revision: 1.1 $  $Date: 2007/04/28 14:19:56 $
 * @author   
 * <p/>修改：$Author: liuj $
=============================================== 
--%>
<%
request.setCharacterEncoding("GBK");
ConnectionManager manager=ConnectionManager.getInstance();

boolean temp=false;
String SEQNO=null;
  if(request.getParameter("SEQNO")==null){
    SEQNO=null;
  }
  else{
    SEQNO=request.getParameter("SEQNO").trim();
  }
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


  String sql="update FCPRD set DT="+DBUtil.toSqlDate(DT)+",ENDDT="+DBUtil.toSqlDate(ENDDT)+",INITIALIZED="+INITIALIZED+",COMMENT='"+DBUtil.toDB(COMMENT)+"' where SEQNO="+SEQNO+"";
  temp=manager.ExecCmd(sql);
  BMFCLimit.setDirty();
  if(temp){
	session.setAttribute("mess","修改信息成功");
	response.sendRedirect("../wjflset/Qftime_info.jsp?SEQNO="+SEQNO);
  }
  else{
	session.setAttribute("mess","修改信息失败");
	response.sendRedirect("../wjflset/Qftime_info.jsp?SEQNO="+SEQNO);
  }
%>