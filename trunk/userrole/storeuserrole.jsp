<%@ page contentType="text/html; charset=gb2312" language="java" import="zt.cms.scuser.Role" errorPage="" %>
<%@ page import="zt.cms.scuser.RoleFacotory" %>
<%@ page import="zt.cmsi.pub.define.UserRoleMan" %>
<%@ page import="zt.platform.db.ConnectionManager" %>
<%@ page import="zt.platform.db.DatabaseConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%

    String[] roles = request.getParameterValues("role");
    if (roles == null) {
        roles = new String[0];
    }
    String userid = request.getParameter("userid");

    DatabaseConnection con = ConnectionManager.getInstance().getConnection();
    con.begin();
    String str = "delete from scuserrole where userno=" + userid;
//System.out.println("SQL:"+str);
    if (con.executeUpdate(str) < 0) {
        con.rollback();
        request.setAttribute("msg", "发生数据库错误1");
        ConnectionManager.getInstance().releaseConnection(con);
%>
<jsp:forward page="/showinfo.jsp"/>
<%

} else {
    PreparedStatement pst = con.getPreparedStatement("insert into scuserrole(userno,roleno) values (?,?)");
    pst.setInt(1, Integer.parseInt(userid));
    for (int i = 0; i < roles.length; i++) {
        pst.setInt(2, Integer.parseInt(roles[i]));
        try {
            pst.execute();
        } catch (Exception e) {
            con.rollback();
            ConnectionManager.getInstance().releaseConnection(con);
            request.setAttribute("msg", "发生数据库错误2");
%>
<jsp:forward page="/showinfo.jsp"/>
<%
                break;
            }

        }
    }

    con.commit();
    ConnectionManager.getInstance().releaseConnection(con);
    UserRoleMan.setDirty(true);
    request.setAttribute("msg", "更改角色成功！");

%>
<jsp:forward page="/showinfo.jsp"/>