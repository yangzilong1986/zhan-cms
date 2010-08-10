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
String SEQNO=null;
  if(request.getParameter("SEQNO")==null){
    SEQNO=null;
  }
  else{
    SEQNO=request.getParameter("SEQNO").trim();
  }
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
  //System.out.println("******************************length=="+REASON.length());
  String sql="update FCREASON set FCCLASS="+FCCLASS+",REASON='"+DBUtil.toDB(REASON)+"' where SEQNO="+SEQNO+"";
  temp=manager.ExecCmd(sql);
  BMFCLimit.setDirty();
  if(temp){
	session.setAttribute("mess","修改信息成功");
	response.sendRedirect("../wjflset/Flyy_info.jsp?SEQNO="+SEQNO);
  }
  else{
	session.setAttribute("mess","修改信息失败");
	response.sendRedirect("../wjflset/Flyy_info.jsp?SEQNO="+SEQNO);
  }
%>