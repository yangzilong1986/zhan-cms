<%@ page contentType="text/html; charset=GBK" %>

<%
  if ( 1==1 ) {
      String path = request.getContextPath();
      out.println("<script language=\"javascript\">alert ('����Ա��ʱ�������µ�¼��'); if(top){ top.location.href='"+path+"/login.jsp'; } else { location.href = '"+path+"/login.jsp';} </script>");
      return;
  }%>
