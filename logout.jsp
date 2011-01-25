<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="javax.security.auth.login.LoginException" %>

<html>
<head>
    <title>Security WebApp - Logout Page</title>
</head>
<body bgcolor="#AAACCC" link="#3366cc" vlink="#9999cc" alink="#0000cc">
<table border=0 cellspacing="18" cellpadding="0">
    <tr>
        <td valign="top">
            <%
                UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                if (um != null)
                    try {
                        um.logout(um.getUser().getUsername());
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                session.removeAttribute(SystemAttributeNames.USER_INFO_NAME);
            %>
            <jsp:forward page="login.jsp"/>
        </td>
    </tr>
</table>
</body>
</html>