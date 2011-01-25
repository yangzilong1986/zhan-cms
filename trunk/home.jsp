<%--
*******************************************************************
*    程序名称:    home.jsp
*    程序标识:
*    功能描述:    框架页
*    连接网页:
*    传递参数:
*    作   者:     王海雷
*    开发日期:    2004-02-05
*    修改日期:    2004-02-05
*    版    权:    青岛中天信息技术有限公司
*
*
*******************************************************************
--%>
<%--
===============================================
Title: 页面框架
Description:  添加客户直接关闭浏览器处理。
 * @version   $Revision: 1.4 $  $Date: 2007/05/16 09:19:55 $
 * @author  王海雷
 * <p/>修改：$Author: liuj $
===============================================
--%>
<%@ page contentType="text/html; charset=gb2312" %>
<%@ include file="loginassistor.jsp" %>

<html>
<head>
    <title>海尔财务公司</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link href="/css/platform.css" rel="stylesheet" type="text/css">
</head>
<%--<% String menuPath = request.getContextPath() + "/userMenu.jsp"; %>--%>
<script type="text/javascript">
    //lj chenged in2007-05-16 for user unstandardized exit.
    window.onbeforeunload = function() {
        killSession();
    }
    function killSession() {
        var xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
        xmlhttp.open("POST", "logout.jsp?isclose=1", true);
        xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xmlhttp.send();

        //        var auth = new ActiveXObject("Msxml2.XMLHTTP") ;
        //        auth.open("POST", "logout.jsp?isclose=1", false);
        //
        //        var xmlstr = "<root><action></action></root>";
        //        auth.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        //
        //        auth.send(xmlstr);
    }
</script>
<frameset rows="80,*,15" cols="*" frameborder="NO" border="0" framespacing="0">
    <frame src="top.jsp" name="topFrame" scrolling="NO" noresize onload>
    <frameset id="menuFrame" framespacing="0" cols="200,12,*" frameborder="0" framespacing="0">
        <frame id="menuList" src="menuPage.jsp" frameborder="0" noresize="noresize" scrolling="no">
        <frame id="dockFrame" src="dockPage.jsp" frameborder="0" noresize="noresize" scrolling="no">
        <frame id="workFrame" src="/bulletin/bulletin.jsp" frameborder="0" noresize="noresize" scrolling="auto">
    </frameset>

    <%--
        <frameset id="middle" scrolling="NO" rows="*" cols="240,*" framespacing="0" frameborder="NO" border="0">
            <frameset rows="0,*" framespacing="0" frameborder="NO" border="0">
                &lt;%&ndash;<frame id="menupage" src="userMenu.jsp" name="code" scrolling="no">&ndash;%&gt;
                <frame id="menuList" src="menuPage.jsp" name="code" scrolling="no">
                &lt;%&ndash;<frame id="menutemp" src="left.html" name="leftFrame" scrolling="no">&ndash;%&gt;
            </frameset>
            <frame src="/bulletin/bulletin.jsp" name="mainFrame">
        </frameset>
    --%>

    <frame src="bottom.jsp" name="bottomFrame" scrolling="NO" noresize>
</frameset>
<noframes>
    <body></body>
</noframes>
</html>