<%@ page contentType="text/html; charset=gb2312" %>
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
<%@ page import="zt.platform.utils.*" %>
<%
request.setCharacterEncoding("GBK");
String [] SEQNO=request.getParameterValues("SEQNO");
 if(SEQNO==null || SEQNO.length==0){
		response.sendRedirect("Qftime_set.jsp");
	}
ConnectionManager manager=ConnectionManager.getInstance();
boolean temp=false;
int flag=0;
String sqls="";
for(int i=0;i<SEQNO.length;i++){
	sqls="delete from FCPRD where SEQNO="+SEQNO[i]+"";
	temp=manager.ExecCmd(sqls);
	if(temp==false){
		flag=flag+1;
	}
}
if(temp==true && flag==0){
	response.sendRedirect("../wjflset/Qftime_set.jsp");
}

%>