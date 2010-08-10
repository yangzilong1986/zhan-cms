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

boolean temp=false;
String FCCLASS=null;
  if(request.getParameter("FCCLASS")==null){
    FCCLASS=null;
  }
  else{
    FCCLASS=request.getParameter("FCCLASS").trim();
  }
String REASON=null;
  if(request.getParameter("REASON")==null){
    REASON=null; 
  }
  else{
    REASON=request.getParameter("REASON").trim();
  }
  String sql="insert into FCREASON values("+FCReason.getNextNo()+","+FCCLASS+",'"+DBUtil.toDB(REASON)+"')";
  temp=manager.ExecCmd(sql);
  BMFCLimit.setDirty();
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/wjflset/Flyy_add.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/wjflset/Flyy_add.jsp"/>
	<%
  }
%>