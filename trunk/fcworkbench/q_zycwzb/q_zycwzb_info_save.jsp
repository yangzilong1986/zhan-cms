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
String FCNO=request.getParameter("FCNO");
String FCSTATUS=request.getParameter("FCSTATUS");
if(FCNO == null || FCSTATUS==null)
  {
   session.setAttribute("lettermess","没有发现传送入的参数！");
    response.sendRedirect("/fcworkbench/lettersucces.jsp");
  }
request.setCharacterEncoding("GBK");
ConnectionManager manager=ConnectionManager.getInstance();

boolean temp=false;
String DT=null;
  if(request.getParameter("DT")==null){
    DT=null;
  }
  else{
    DT=request.getParameter("DT").trim();
	DT=DT+"01";
  }
String AMT1=null;
  if(request.getParameter("AMT1").trim()==null || request.getParameter("AMT1").trim().equals("")){
    AMT1=null;
  }
  else{
    AMT1=request.getParameter("AMT1").trim();
  }
  String AMT2=null;
  if(request.getParameter("AMT2")==null || request.getParameter("AMT2").trim().equals("")){
    AMT2=null;
  }
  else{
    AMT2=request.getParameter("AMT2").trim();
  }
  String AMT3=null;
  if(request.getParameter("AMT3")==null || request.getParameter("AMT3").trim().equals("")){
    AMT3=null;
  }
  else{
    AMT3=request.getParameter("AMT3").trim();
  }
  String AMT4=null;
  if(request.getParameter("AMT4")==null || request.getParameter("AMT4").trim().equals("")){
    AMT4=null;
  }
  else{
    AMT4=request.getParameter("AMT4").trim();
  }
  String AMT5=null;
  if(request.getParameter("AMT5")==null || request.getParameter("AMT5").trim().equals("")){
    AMT5=null;
  }
  else{
    AMT5=request.getParameter("AMT5").trim();
  }
  String AMT6=null;
  if(request.getParameter("AMT6")==null || request.getParameter("AMT6").trim().equals("")){
    AMT6=null;
  }
  else{
    AMT6=request.getParameter("AMT6").trim();
  }
  String AMT7=null;
  if(request.getParameter("AMT7")==null || request.getParameter("AMT7").trim().equals("")){
    AMT7=null;
  }
  else{
    AMT7=request.getParameter("AMT7").trim();
  }
  String AMT8=null;
  if(request.getParameter("AMT8")==null || request.getParameter("AMT8").trim().equals("")){
    AMT8=null;
  }
  else{
    AMT8=request.getParameter("AMT8").trim();
  }
  String AMT9=null;
  if(request.getParameter("AMT9")==null || request.getParameter("AMT9").trim().equals("")){
    AMT9=null;
  }
  else{
    AMT9=request.getParameter("AMT9").trim();
  }
  String AMT10=null;
  if(request.getParameter("AMT10")==null || request.getParameter("AMT10").trim().equals("")){
    AMT10=null;
  }
  else{
    AMT10=request.getParameter("AMT10").trim();
  }
  /*String CMT1=null;
  if(request.getParameter("CMT1")==null){
    CMT1=null;
  }
  else{
    CMT1=request.getParameter("CMT1").trim();
  }*/

  String sql="update FCMCMT set AMT1="+AMT1+",AMT2="+AMT2+",AMT3="+AMT3+",AMT4="+AMT4+","+
  "AMT5="+AMT5+",AMT6="+AMT6+",AMT7="+AMT7+",AMT8="+AMT8+",AMT9="+AMT9+",AMT10="+AMT10+""+
  " where FCNO='"+FCNO+"' and  DT="+DBUtil.toSqlDate(DT)+" and FCCMTTYPE=10";
  temp=manager.ExecCmd(sql);
  
  if(temp){
	request.setAttribute("mess","修改信息成功");
	%>
	<jsp:forward page="/fcworkbench/q_zycwzb/q_zycwzb_info.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","修改信息失败");
	%>
	<jsp:forward page="/fcworkbench/q_zycwzb/q_zycwzb_info.jsp"/>
	<%
  }
%>