<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%
    UserManager um = (UserManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if ( um == null ) {
        um = new UserManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME,um);
    }
    if ( !um.isLogin() ) {
%>
    <jsp:forward page="login.jsp"></jsp:forward>
<%  }
%>
