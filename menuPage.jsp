<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>

<%
    response.setContentType("text/html; charset=GBK");
    String contextPath = request.getContextPath();

%>
<script type="text/javascript">
    var g_jsContextPath = "<%=contextPath%>";
</script>

<html xmlns:hGui>
<head>
     <%--<link href="/css/platform.css" rel="stylesheet" type="text/css">--%>

    <LINK href="<%=contextPath%>/dhtmlx/codebase/dhtmlxlayout.css" type="text/css" rel="stylesheet">
    <LINK href="<%=contextPath%>/dhtmlx/codebase/skins/dhtmlxlayout_dhx_skyblue.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxcommon.js"></script>
    <script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxlayout.js"></script>

    <LINK href="<%=contextPath%>/dhtmlx/codebase/skins/dhtmlxaccordion_dhx_skyblue.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxaccordion.js"></script>
    <script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxcontainer.js"></script>

    <LINK href="<%=contextPath%>/dhtmlx/codebase/dhtmlxtree.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/dhtmlxtree.js"></script>
    <script language="javascript" src="<%=contextPath%>/dhtmlx/codebase/ext/dhtmlxtree_json.js"></script>

    <script language="javascript">
       <%
             String jsonDefaultMenu = null;
             String jsonSystemMenu = null;
             UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

             try {
                 //xmlString =om.getXmlString();
                 jsonDefaultMenu = um.getJsonString("default");
                 jsonSystemMenu = um.getJsonString("system");
              } catch(Exception e) {
                 System.out.println("jsp" +e +"\n");
              }
        %>
        var defaultMenuStr = '<%=jsonDefaultMenu%>';
        var systemMenuStr = '<%=jsonSystemMenu%>';

        var dhxAccord;
        function doOnLoad() {
            document.all("accordObj").style.height = document.body.clientHeight - 3;
            dhxAccord = new dhtmlXAccordion("accordObj");
            dhxAccord.setSkin("dhx_skyblue");
            dhxAccord.setIconsPath("<%=contextPath%>/dhtmlx/codebase/icons/");
            dhxAccord.addItem("a1", "业务处理");
            dhxAccord.addItem("a2", "系统管理");
            dhxAccord.addItem("a3", "用户信息");
            dhxAccord.addItem("a4", "帮助信息");
            dhxAccord.openItem("a1");
            dhxAccord._enableOpenEffect = true;

            dhxAccord.cells("a1").setIcon("accord_biz.png");
            dhxAccord.cells("a2").setIcon("accord_manage.png");
            dhxAccord.cells("a3").setIcon("editor.gif");
            dhxAccord.cells("a4").setIcon("accord_support.png");
            var biztree = dhxAccord.cells("a1").attachTree();
            var managetree = dhxAccord.cells("a2").attachTree();

            var treeDefaultJson = eval('(' + defaultMenuStr + ')');

            biztree.setSkin('dhx_skyblue');
            biztree.setImagePath("<%=contextPath%>/dhtmlx/codebase/imgs/csh_books/");
            //biztree.enableTreeLines(true);
            biztree.loadJSONObject(treeDefaultJson);
            biztree.attachEvent("onClick", function(id) {
                var action = (biztree.getUserData(id, "url"));
                if (action == "#") {
                    biztree.openItem(id);
                } else {
                    parent.window.workFrame.location.replace("<%=contextPath%>" + action);
                }
                return true;
            });

            var treeSystemJson = eval('(' + systemMenuStr + ')');

            managetree.setSkin('dhx_skyblue');
            managetree.setImagePath("<%=contextPath%>/dhtmlx/codebase/imgs/csh_books/");
            managetree.loadJSONObject(treeSystemJson);
            managetree.attachEvent("onClick", function(id) {
                var action = (managetree.getUserData(id, "url"));
                if (action == "#") {
                    managetree.openItem(id);
                } else {
                    parent.window.workFrame.location.replace("<%=contextPath%>" + action);
                }
                return true;
            });
        }

        function doOnResize() {
            var parentObj = document.getElementById("accordObj");
            parentObj.style.height = document.body.clientHeight - 3;
            dhxAccord.setSizes();
        }
    </script>

</head>


<body onload="doOnLoad();" onResize="doOnResize();" leftmargin="0" topmargin="1" rightmargin="0" bottommargin="0">
<div id="accordObj" style="width: 199px; height: 500px; margin-left:1px;"></div>
</body>
</html>
