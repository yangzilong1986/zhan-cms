<%--
*******************************************************************
*    ��������:    userMenu.jsp
*    �����ʶ:
*    ��������:    չʾ��������
*    ������ҳ:
*    ���ݲ���:
*    ��   ��:     Houym
*    ��������:    2003-05-29
*    �� �� ��:    WangHaiLei
*    �޸�����:    2003-12-05-09-36
*    �� �� ��:    wxj
*    �޸�����:    040211
*    ��    Ȩ:    �ൺ������Ϣ�������޹�˾
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
    var MTMenuFrame = "leftFrame";//�����ʾ�˵���Frame������

    var MTShowFrame = "mainFrame" ;//������Frame
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
    var MTMRootFont = "Courier New, ����, Arial";
    var MTMRootCSSize = "9pt";
    var MTMRootFontSize = "-1";

    var MTMenuFont = "Courier New, ����, Arial";
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
        // ����������ȡ��UserManger object�� ���Ǵ�login.jsp�в������Ǹ�UserManger��
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