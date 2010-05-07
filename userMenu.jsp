<%--
*******************************************************************
*    程序名称:    userMenu.jsp
*    程序标识:
*    功能描述:    展示功能树。
*    连接网页:
*    传递参数:
*    作   者:     Houym
*    开发日期:    2003-05-29
*    修 改 人:    WangHaiLei
*    修改日期:    2003-12-05-09-36
*    修 改 人:    wxj
*    修改日期:    040211
*    版    权:    青岛中天信息技术有限公司
*
*
*******************************************************************
--%>
<%@ page contentType="text/html; charset=GBK" language="java" %>
<%@ page import="zt.cmsi.pub.LogControl" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.Tree" %>
<%@ page import="zt.platform.user.UserManager" %>

<html>
<head>
    <title>Menu</title>
</head>
<script type="text/javascript" src="js/JSTreeMenus.js"></script>
<script type="text/javascript">
    //var MTMTableWidth = "280";
    var menuAppear = true;
    var MTMenuFrame = "leftFrame";//左侧显示菜单的Frame的名字

    var MTShowFrame = "mainFrame" ;//主工作Frame
    var MTMSubsGetPlus = "always";
    var MTMEmulateWE = true;

    var MTMenuImageDirectory = "images/menu-images/";
    var MTMBGColor = "#3E76B3";

    var MTMBackground = "";
    var MTMTextColor = "white";

    var MTMLinkColor = "black";
    var MTMAhoverColor = "white";

    var MTMTrackColor = "red";

    var MTMSubExpandColor = "red";
    var MTMSubClosedColor = "red";

    //Text of Root
    var MTMenuText = "";//"Tree";
    var MTMRootColor = "white";
    var MTMRootFont = "Courier New, 宋体, Arial";
    var MTMRootCSSize = "9pt";
    var MTMRootFontSize = "-1";

    var MTMenuFont = "Courier New, 宋体, Arial";
    var MTMenuCSSize = "9pt";
    var MTMenuFontSize = "-1";

    var MTMLinkedSS = false;
    var MTMSSHREF = "";

    var MTMExtraCSS = "";

    var MTMHeader = "";
    var MTMFooter = "";

    /** Noted by WangHaiLei: <br>
     *  This property is used to detemine whether the Menu sub items move in Collapse Mode or Accordion Mode.
     *  Collapse Mode: false;
     *  Accrodion Mode: true;
     */
    var MTMSubsAutoClose = true;

    var MTMTimeOut = 10;

    var MTMUseCookies = "no";
    var MTMCookieName = "adminMenusTree";
    var MTMCookieDays = 3;

    var MTMUseToolTips = true;

    var menu = null;
    menu = new MTMenu();
    menu.MTMAddItem(new MTMenuItem("Root", "main.jsp", "mainFrame", ""));

    <%
        // 从上下文中取到UserManger object， 就是从login.jsp中产生的那个UserManger。
        UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        try {
            Tree tree = new Tree(um.getXmlString());
            tree.output(out);
//            for (int j = 0; j < tree.getDomTree(out).getChildNodes().getLength(); j++) {
//                System.out.println("["+j+"]="+tree.getDomTree(out).getChildNodes().item(j).getAttributes().getNamedItem("action").getNodeValue());
//            }
        } catch(Exception e) {
            System.out.println(e);
        }
    %>

</script>
<body onLoad="MTMStartMenu()" bgcolor="#336699" text="#FFFFFF" link="yellow" vlink="lime" alink="red">
</body>
</html>