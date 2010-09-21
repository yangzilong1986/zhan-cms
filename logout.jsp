<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.user.UserManager" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>

<html>
     <head>
          <title>Security WebApp - Logout Page</title>
     </head>
     <body bgcolor="#AAACCC" link="#3366cc" vlink="#9999cc" alink="#0000cc">
          <table border=0 cellspacing="18" cellpadding="0">
               <tr>
                    <td valign="top">
                         <%
                              UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                              if ( um != null )
                              	um.logout(um.getUser().getUsername());
                              session.removeAttribute(SystemAttributeNames.USER_INFO_NAME);
                         %>
                         <jsp:forward page="login.jsp"/>
                    </td>
               </tr>
          </table>
     </body>
</html>