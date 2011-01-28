<%@ page contentType="text/html; charset=GBK" %>

<%
  if ( 1==1 ) {
      String path = request.getContextPath();
      out.println("<script language=\"javascript\">alert ('操作员超时，请重新登录！'); if(top){ top.location.href='"+path+"/login.jsp'; } else { location.href = '"+path+"/login.jsp';} </script>");
      return;
  }%>
