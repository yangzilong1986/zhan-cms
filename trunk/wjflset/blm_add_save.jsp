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
<%@ page import="zt.cmsi.pub.cenum.level"%>
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

  String query_sql="select * from FCFACTOR where FCCLASS="+FCCLASS+"";
  CachedRowSet crs=manager.getRs(query_sql);
	if(crs.next()){
		request.setAttribute("mess","五级分类:"+level.getEnumItemName("LoanCat1",FCCLASS)+"已存在");
	%>
	<jsp:forward page="/wjflset/blm_add.jsp"/>
	<%
	}
  String sql="insert into FCFACTOR values("+FCCLASS+","+FACTOR+")";
  temp=manager.ExecCmd(sql);
  BMFCLimit.setDirty();
  if(temp){
	request.setAttribute("mess","添加信息成功");
	%>
	<jsp:forward page="/wjflset/blm_add.jsp"/>
	<%
  }
  else{
	request.setAttribute("mess","添加信息失败");
	%>
	<jsp:forward page="/wjflset/blm_add.jsp"/>
	<%
  }
%>