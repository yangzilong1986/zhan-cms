<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.cmsi.pub.code.FCReason" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%
request.setCharacterEncoding("GBK");
ConnectionManager manager=ConnectionManager.getInstance();
String FCNO=request.getParameter("FCNO");
String FCSTATUS=request.getParameter("FCSTATUS");
  if(FCNO == null || FCSTATUS==null)
  {
    session.setAttribute("lettermess","没有发现传送入的参数！");
    response.sendRedirect("/fcworkbench/lettersucces.jsp");
  }
boolean temp=false;
String CMT1=null;
  if(request.getParameter("CMT1")==null){
    CMT1=null;
  }
  else{
    CMT1=request.getParameter("CMT1").trim();
  }
String CMT2=null;
  if(request.getParameter("CMT2")==null){
    CMT2=null;
  }
  else{
    CMT2=request.getParameter("CMT2").trim();
  }
  String CMT3=null;
  if(request.getParameter("CMT3")==null){
    CMT3=null;
  }
  else{
    CMT3=request.getParameter("CMT3").trim();
  }
  String CMT4=null;
  if(request.getParameter("CMT4")==null){
    CMT4=null;
  }
  else{
    CMT4=request.getParameter("CMT4").trim();
  }
    String CMT5=null;
  if(request.getParameter("CMT5")==null){
    CMT5=null;
  }
  else{
    CMT5=request.getParameter("CMT5").trim();
  }
  String sql="";
  String query_sql="select *from FCCMT where FCNO='"+FCNO+"' and FCCMTTYPE=3";
  CachedRowSet crs=manager.getRs(query_sql);
  if(crs.next()){
	sql="update FCCMT set cmt1='"+DBUtil.toDB(CMT1)+"',cmt2='"+DBUtil.toDB(CMT2)+"',cmt3='"+DBUtil.toDB(CMT3)+"',cmt4='"+DBUtil.toDB(CMT4)+"',cmt5='"+DBUtil.toDB(CMT5)+"'"+
		" where FCNO='"+FCNO+"' and FCCMTTYPE=3";
  }
  else{
	sql="insert into FCCMT(FCNO,FCCMTTYPE,CMT1,CMT2,CMT3,CMT4,CMT5) values('"+FCNO+"',3,'"+DBUtil.toDB(CMT1)+"','"+DBUtil.toDB(CMT2)+"','"+DBUtil.toDB(CMT3)+"','"+DBUtil.toDB(CMT4)+"','"+DBUtil.toDB(CMT5)+"')";
  }
  temp=manager.ExecCmd(sql);
  
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/fcworkbench/z_fcwys/z_fcwys.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/fcworkbench/z_fcwys/z_fcwys.jsp"/>
	<%
  }
%>