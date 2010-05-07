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
String FACTOR=null;
  if(request.getParameter("FACTOR")==null){
    FACTOR=null;
  }
  else{
    FACTOR=request.getParameter("FACTOR").trim();
  }
  ////System.out.println("******************************length=="+FCCLASS.length());
  String sql="update FCFACTOR set FACTOR="+FACTOR+" where FCCLASS="+FCCLASS+"";
  temp=manager.ExecCmd(sql);
  BMFCLimit.setDirty();
  if(temp){
	session.setAttribute("mess","修改信息成功");
	response.sendRedirect("../wjflset/blm_info.jsp?FCCLASS="+FCCLASS);
  }
  else{
	session.setAttribute("mess","修改信息失败");
	response.sendRedirect("../wjflset/blm_info.jsp?FCCLASS="+FCCLASS);
  }
%>