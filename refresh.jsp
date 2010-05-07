<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.config.EnumerationType" %>
<%@ page import="zt.platform.form.config.FormBeanManager" %>
<%@ page import="zt.platform.form.config.TableBeanManager" %>
<%@ page import="zt.cmsi.pub.define.BMRouteBind" %>
<%@ page import="zt.cmsi.pub.define.XFGradeMark" %>
<html>
<head>
    <title>
        refresh
    </title>
</head>
<body bgcolor="#ffffff">
<h1>
    JBuilder Generated JSP
</h1>
<%
    EnumerationType.init();
    TableBeanManager.init();
    FormBeanManager.init();
    BMRouteBind.setDirty();
    //XFGradeMark.setDirty();
%>
Refresh OK

</body>
</html>
