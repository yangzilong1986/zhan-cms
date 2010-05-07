<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.cms.bm.common.*" %>
<%@ page import="zt.cms.bm.workbench.Affair" %>
<%@ page import="zt.cms.bm.workbench.db.AffairFactory" %>
<%@ page import="zt.cms.pub.*" %>
<%@ page import="zt.platform.cachedb.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.form.control.impl.*" %>
<%@ page import="zt.platform.user.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.cmsi.mydb.MyDB" %>
<%@ page import="zt.cmsi.pub.cenum.level"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="zt.platform.utils.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%@ page import="zt.cms.pub.SCBranch" %>
<%
String FCNO=request.getParameter("FCNO");
String FCTYPE=request.getParameter("FCTYPE");
String FCSTATUS=request.getParameter("FCSTATUS");
  if(FCNO == null || FCTYPE==null || FCSTATUS==null)
  {
   session.setAttribute("lettermess","没有发现传送入的参数！");
    response.sendRedirect("../fcworkbench/lettersucces.jsp");
  }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>中国信合</title>
</head>
<link href="/css/main.css" rel="stylesheet" type="text/css">
<link href="/css/platform.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/js/main.js"></script>
<script language="javascript">

function tabCreate(objTab,id){

	objTab.design            = "1";
	objTab.orientation       = "0";
	objTab.tabarea           = true;
	objTab.designmode        = "IMAGE";
	objTab.tabMode           = 1;       // set to mutiple rows
	objTab.maxTabItemsPerRow = 10;      // we want to have max 3 items per row
	objTab.tabAlignment      = 0;       // center tabs
	objTab.loadOnStartup = true;		//If true, all pages are loaded during startup. If false, only the
										//active page is loaded

	var item = objTab.createItem();
	item.text = "信贷员认定";
	item.title = "信贷员认定";
	item.active = <%=FCSTATUS%>=="1"?true:false;
	item.cached = false;
	item.url = "/fcworkbench/one.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>&FCTYPE=<%=FCTYPE%>";
	objTab.add(item);

	var item = objTab.createItem();
	item.text = "信贷部门审批";
	item.title = "信贷部门审批";
	item.active = <%=FCSTATUS%>=="2"?true:false;
	item.cached = false;
	item.url =  "/fcworkbench/two.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>&FCTYPE=<%=FCTYPE%>";
	objTab.add(item);

	var item = objTab.createItem();
	item.text = "县联社审批";
	item.title = "县联社审批";
	item.active = <%=FCSTATUS%>=="3"?true:false;
	item.cached = false;
	item.url =  "/fcworkbench/three.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>&FCTYPE=<%=FCTYPE%>";
	objTab.add(item);
	objTab.create();
}

// this function is called when an item is clicked
function tabEventTabClick(objItem)
{
	//alert("Item clicked: " + objItem.text);
}
</script>
<body topmargin="5" leftmargin="0">
<iframe id="tab" style="Z-INDEX: 100; LEFT: 30px; WIDTH: 95%; POSITION: absolute; TOP: 5px; HEIGHT: 900"
src="pinTab.html?id=tab" frameBorder="0"></iframe>
</body>
</html>