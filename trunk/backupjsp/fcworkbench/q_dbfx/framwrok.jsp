<%@ page contentType="text/html; charset=GBK" %>

<%--
===============================================
Title: 企业财务分析-抵置押物
Description: 原担保分析，改为在五级分类企业基本财务分析中录入。
 * @version  $Revision: 1.2 $  $Date: 2007/05/28 09:21:44 $
 * @author
 * <p/>修改：$Author: liuj $
===============================================
--%>

<%
    String FCNO = request.getParameter("FCNO");
    String FCTYPE = request.getParameter("FCTYPE");
    String FCSTATUS = request.getParameter("FCSTATUS");
    String BMNO = request.getParameter("BMNO");
    if (FCNO == null || FCTYPE == null || FCSTATUS == null || BMNO == null) {
        session.setAttribute("lettermess", "没有发现传送入的参数！");
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
    function tabCreate(objTab, id) {

        objTab.design = "1";
        objTab.orientation = "0";
        objTab.tabarea = true;
        objTab.designmode = "IMAGE";
        objTab.tabMode = 1;
        // set to mutiple rows
        objTab.maxTabItemsPerRow = 10;
        // we want to have max 3 items per row
        objTab.tabAlignment = 0;
        // center tabs
        objTab.loadOnStartup = true;
        //If true, all pages are loaded during startup. If false, only the
        //active page is loaded

        var item = objTab.createItem();
        item.text = "抵押物登记信息";
        item.title = "抵押物登记信息";
        item.active = true; // lj changed in 2007-05-26
        item.cached = false;
        item.url = "/templates/defaultform.jsp?Plat_Form_Request_Form_ID=BMPLEDGELIST&Plat_Form_Request_Event_ID=0&BMNO=<%=BMNO%>";
        objTab.add(item);

        var item = objTab.createItem();
        item.text = "质押物登记信息";
        item.title = "质押物登记信息";
        item.active = false; 
        item.cached = false;
        item.url = "/templates/defaultform.jsp?Plat_Form_Request_Form_ID=BMPLEDGELIST1&Plat_Form_Request_Event_ID=0&BMNO=<%=BMNO%>";
        objTab.add(item);

        <%--****************************************lj deleted in 2007-05-26--%>
        <%--var item = objTab.createItem();--%>
        <%--item.text = "担保分析";--%>
        <%--item.title = "担保分析";--%>
        <%--item.active = true;--%>
        <%--item.cached = false;--%>
        <%--item.url = "../q_dbfx/q_dbfx.jsp?FCNO=<%=FCNO%>&FCSTATUS=<%=FCSTATUS%>&FCTYPE=<%=FCTYPE%>";--%>
        <%--objTab.add(item);--%>
        
        objTab.create();
    }

    // this function is called when an item is clicked
    function tabEventTabClick(objItem)
    {
        //alert("Item clicked: " + objItem.text);
    }
</script>
<body topmargin="5" leftmargin="0">
<iframe id="tab" style="Z-INDEX: 100; LEFT: 30px; WIDTH: 95%; POSITION: absolute; TOP: 5px; HEIGHT: 500"
        src="pinTab.html?id=tab" frameBorder="0"></iframe>
</body>
</html>