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
   session.setAttribute("lettermess","û�з��ִ�����Ĳ�����");
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

  String sql="update FCMCMT set CMT1='"+DBUtil.toDB(CMT1)+"'"+
  " where FCNO='"+FCNO+"' and  DT='"+DT+"' and FCCMTTYPE=9";
  temp=manager.ExecCmd(sql);
  
  if(temp){
	request.setAttribute("mess","�޸���Ϣ�ɹ�");
	%>
	<jsp:forward page="/fcworkbench/z_qtqksm/z_qtqksm_info.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","�޸���Ϣʧ��");
	%>
	<jsp:forward page="/fcworkbench/z_qtqksm/z_qtqksm_info.jsp"/>
	<%
  }
%>