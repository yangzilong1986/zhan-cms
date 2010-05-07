<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cms.bm.bulletin.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.config.*" %>
<%@ page import="zt.platform.db.DBUtil" %>
<%
int ret = MsgFactory.deleteMsgByNo(Integer.parseInt(request.getParameter("msgno")));

//request.setAttribute(SessionAttributes.BACKGROUND_DISPATCH,"111");	
RequestDispatcher rd=null;
if(ret>=0){
					request.setAttribute("msg","É¾³ý³É¹¦");	

}else{
request.setAttribute("msg","É¾³ýÊ§°Ü");	
}				    
rd=request.getRequestDispatcher("/showinfo.jsp");
rd.forward(request,response);
%>
