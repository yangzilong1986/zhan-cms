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

	String query_sql="select * from FCMCMT where FCNO='"+FCNO+"' and FCCMTTYPE=7 and DT="+DBUtil. toSqlDate(DT)+"";
	CachedRowSet crs=manager.getRs(query_sql);
	if(crs.next()){
		request.setAttribute("mess","日期:"+DBUtil. toSqlDate(DT)+"已存在");
	%>
	<jsp:forward page="/fcworkbench/z_cwzk/z_cwzk_add.jsp"/>
	<%
	}
  String sql="insert into FCMCMT(FCNO,FCCMTTYPE,DT,AMT1,AMT2,AMT3,AMT4,AMT5,AMT6,AMT7,AMT8) "+
  "values('"+FCNO+"',7,"+DBUtil.toSqlDate(DT)+","+AMT1+","+AMT2+","+AMT3+","+AMT4+","+AMT5+","+AMT6+","+AMT7+","+AMT8+")";
  temp=manager.ExecCmd(sql);
  
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/fcworkbench/z_cwzk/z_cwzk_add.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/fcworkbench/z_cwzk/z_cwzk_add.jsp"/>
	<%
  }
%>