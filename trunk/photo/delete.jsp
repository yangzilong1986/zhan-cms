<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.cm.report.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="zt.platform.form.util.*" %>
<%@ page import="zt.platform.db.*" %>
<%

DatabaseConnection con = ConnectionManager.getInstance().getConnection();
String str = "delete from glblob where seqno="+request.getParameter("seqno");
if(con.executeUpdate(str)<0){
     request.setAttribute("msg","ɾ��ʧ��");
}else{
    request.setAttribute("msg","ɾ���ɹ�");
}
ConnectionManager.getInstance().releaseConnection(con);
RequestDispatcher rd=request.getRequestDispatcher("/showinfo.jsp");
rd.forward(request,response);

%>