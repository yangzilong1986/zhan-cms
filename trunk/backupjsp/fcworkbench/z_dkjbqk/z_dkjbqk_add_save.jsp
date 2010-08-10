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

	String query_sql="select * from FCMCMT where FCNO='"+FCNO+"' and FCCMTTYPE=2 and DT="+DBUtil. toSqlDate(DT)+"";
	CachedRowSet crs=manager.getRs(query_sql);
	if(crs.next()){
		request.setAttribute("mess","日期:"+DBUtil. toSqlDate(DT)+"已存在");
	%>
	<jsp:forward page="/fcworkbench/z_dkjbqk/z_dkjbqk_add.jsp"/>
	<%
	}
    String sql="insert into FCMCMT(FCNO,FCCMTTYPE,DT,CMT1,CMT2,CMT3) values('"+FCNO+"',2,"+DBUtil.toSqlDate(DT)+",'"+DBUtil.toDB(CMT1)+"','"+DBUtil.toDB(CMT2)+"','"+DBUtil.toDB(CMT3)+"')";
  temp=manager.ExecCmd(sql);
  
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/fcworkbench/z_dkjbqk/z_dkjbqk_add.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/fcworkbench/z_dkjbqk/z_dkjbqk_add.jsp"/>
	<%
  }
%>