<%@ page import="zt.cmsi.pub.LogControl" %>
<%@ page import="zt.platform.form.config.SystemAttributeNames" %>
<%@ page import="zt.platform.user.UserManager" %>
<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    boolean isLogin = false;
    int loginStatus = -1;
    UserManager um = (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
        um = new UserManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME, um);
    }
    try {
        if (LogControl.getInstance().checkIfLogged(username, pageContext) == true) {
            session.setAttribute("loginassistorAct", "exceed");
            isLogin = false;
            loginStatus = -2;
        } else {
            //isLogin = um.login(username, password);
            //if (!isLogin)    session.setAttribute("loginassistorAct", "relogin");
            loginStatus = um.login(username, password);
            if (loginStatus == 0){
               session.setAttribute("loginassistorAct", "nouser");
            }else if (loginStatus == -1) {
                session.setAttribute("loginassistorAct", "noenable");
            }else if (loginStatus == -2) {
                session.setAttribute("loginassistorAct", "passworderror");
            }else if (loginStatus == -3) {
                session.setAttribute("loginassistorAct", "errorlogintimes");
            }else if (loginStatus == -100) {
                session.setAttribute("loginassistorAct", "othererror"); 
            } else{
                isLogin = true;
            }
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

