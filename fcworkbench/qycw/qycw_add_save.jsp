<%@ page contentType="text/html; charset=GBK" errorPage="/fc/error.jsp"%>
<jsp:useBean id="form1" scope="page" class="zt.cms.bm.workbench.FormCwfx"  />
<jsp:setProperty name="form1" property="*"  />
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.cms.report.db.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.cmsi.pub.code.FCReason" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.bm.workbench.Cwfc" %>

<%
Cwfc fx=new Cwfc();
boolean temp=false;
String DT=form1.getDT();
  if(DT!=null){
	DT=DT.trim()+"01";
	form1.setDT(DT);
  }

	String query_sql="select * from FCQYCW where CLIENTNO='"+form1.getCLIENTNO()+"' and DT="+DBUtil.toSqlDate(DT)+"";
	CachedRowSet crs=DB2_81.getRs(query_sql);
	if(crs.next()){
		request.setAttribute("mess","日期:"+DT.substring(0,6)+"已存在");
	%>
	<jsp:forward page="/fcworkbench/qycw/qycw_add.jsp"/>
	<%
	}
  temp=fx.addCwfx(form1);
  
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/fcworkbench/qycw/qycw_add.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/fcworkbench/qycw/qycw_add.jsp"/>
	<%
  }
%>