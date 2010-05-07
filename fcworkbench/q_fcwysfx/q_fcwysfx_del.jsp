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
String FCNO=request.getParameter("FCNO");
String FCSTATUS=request.getParameter("FCSTATUS");
  if(FCNO == null || FCSTATUS==null)
  {
    session.setAttribute("lettermess","没有发现传送入的参数！");
    response.sendRedirect("/fcworkbench/lettersucces.jsp");
  }
String [] DT=request.getParameterValues("DT");
 if(DT==null || DT.length==0){
		response.sendRedirect("q_other.jsp");
	}
ConnectionManager manager=ConnectionManager.getInstance();
boolean temp=false;
int flag=0;
String sqls="";
for(int i=0;i<DT.length;i++){
	sqls="delete from FCMCMT where DT='"+DT[i]+"' and FCNO='"+FCNO+"' and FCCMTTYPE=8";
	temp=manager.ExecCmd(sqls);
	
	if(temp==false){
		flag=flag+1;
	}
}
if(temp==true && flag==0){
%>
	<jsp:forward page="/fcworkbench/q_fcwysfx/q_fcwysfx.jsp"/>
	<%
}

%>