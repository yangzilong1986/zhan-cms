<%@ page import="zt.cmsi.pub.LogControl" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    boolean isLogin = false;
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        um = new UserManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME, um);
    }
    try {
        if (LogControl.getInstance().checkIfLogged(username, pageContext) == true) {
            session.setAttribute("loginassistorAct", "exceed");
            isLogin = false;
        } else {
            isLogin = um.login(username, password);
            if (!isLogin)
                session.setAttribute("loginassistorAct", "relogin");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    if (!isLogin) {

%>
<jsp:forward page="./login.jsp"></jsp:forward>
<%
    }
%>

