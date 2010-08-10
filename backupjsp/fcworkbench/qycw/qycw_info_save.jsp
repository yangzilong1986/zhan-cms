<%@ page contentType="text/html; charset=GBK" errorPage="/fc/error.jsp"%>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.cms.report.db.*" %>
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
<%@ page import="zt.cms.bm.workbench.Cwfc" %>
<jsp:useBean id="form1" scope="page" class="zt.cms.bm.workbench.FormCwfx" />
<jsp:setProperty name="form1" property="*"  />
<%
boolean temp=false;
String dt=request.getParameter("DT1");
String clientno=form1.getCLIENTNO();
  Cwfc fx=new Cwfc();
  form1.setDT(dt);
  temp=fx.editCwfx(form1);
  if(temp){
	request.setAttribute("mess","修改信息成功");
	%>
	
	<jsp:forward page="/fcworkbench/qycw/qycw_info.jsp">
	
	<jsp:param name="CLIENTNO" value="<%=clientno%>"/>
	<jsp:param name="DT" value="<%=dt%>"/>
	
	</jsp:forward>
	<%
  }
  else{
	request.setAttribute("mess","修改信息失败");
	%>
	
	<jsp:forward page="/fcworkbench/qycw/qycw_info.jsp">

	<jsp:param name="CLIENTNO" value="<%=clientno%>"/>
	<jsp:param name="DT" value="<%=dt%>"/>
	
	</jsp:forward>
	<%
  }
%>