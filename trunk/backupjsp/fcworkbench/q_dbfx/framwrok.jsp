<%@ page contentType="text/html; charset=GBK" %>

<%--
===============================================
Title: ��ҵ�������-����Ѻ��
Description: ԭ������������Ϊ���弶������ҵ�������������¼�롣
 * @version  $Revision: 1.2 $  $Date: 2007/05/28 09:21:44 $
 * @author
 * <p/>�޸ģ�$Author: liuj $
===============================================
--%>

<%
    String FCNO = request.getParameter("FCNO");
    String FCTYPE = request.getParameter("FCTYPE");
    String FCSTATUS = request.getParameter("FCSTATUS");
    String BMNO = request.getParameter("BMNO");
    if (FCNO == null || FCTYPE == null || FCSTATUS == null || BMNO == null) {
        session.setAttribute("lettermess", "û�з��ִ�����Ĳ�����");
        response.sendRedirect("../fcworkbench/lettersucces.jsp");
    }
%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>�й��ź�</title>
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
        item.text = "��Ѻ��Ǽ���Ϣ";
        item.title = "��Ѻ��Ǽ���Ϣ";
        item.active = true; // lj changed in 2007-05-26
        item.cached = false;
        item.url = "/templates/defaultform.jsp?Plat_Form_Request_Form_ID=BMPLEDGELIST&Plat_Form_Request_Event_ID=0&BMNO=<%=BMNO%>";
        objTab.add(item);

        var item = objTab.createItem();
        item.text = "��Ѻ��Ǽ���Ϣ";
        item.title = "��Ѻ��Ǽ���Ϣ";
        item.active = false; 
        item.cached = false;
        item.url = "/templates/defaultform.jsp?Plat_Form_Request_Form_ID=BMPLEDGELIST1&Plat_Form_Request_Event_ID=0&BMNO=<%=BMNO%>";
        objTab.add(item);

        <%--****************************************lj deleted in 2007-05-26--%>
        <%--var item = objTab.createItem();--%>
        <%--item.text = "��������";--%>
        <%--item.title = "��������";--%>
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