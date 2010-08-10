<%@ page contentType="text/html; charset=GBK" errorPage="/fc/error.jsp"%>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.cmsi.pub.code.FCReason" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cmsi.pub.define.BMFCLimit" %>
<%
String clientno=request.getParameter("CLIENTNO");
String [] DT=request.getParameterValues("DT");
ConnectionManager manager=ConnectionManager.getInstance();
boolean temp=false;
int flag=0;
String sqls="";
for(int i=0;i<DT.length;i++){
	sqls="delete from fcqycw where DT='"+DT[i]+"' and CLIENTNO='"+clientno+"'";
	temp=manager.ExecCmd(sqls);
	
	if(temp==false){
		flag=flag+1;
	}
}
if(temp==true && flag==0){
%>
	<jsp:forward page="/fcworkbench/qycw/qycw_list.jsp">
	<jsp:param name="CLIENTNO" value="<%=clientno%>"/>
	</jsp:forward>
	<%
}

%>