<%@ page contentType="text/html; charset=GBK" %>

<%
request.setCharacterEncoding("GB2312");

RequestDispatcher rd=null;
String strType="";
String strClientNo="";
String strFormId="";

String strTmpType=request.getParameter("type");
String strTmpNo=request.getParameter("clientno");

if(strTmpNo!=null)
{
  strClientNo=strTmpNo.trim();
}
else
{
    request.setAttribute("msg","客户号为空!");
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
}

if(strTmpType!=null)
  strType=strTmpType.trim();
else
{
	request.setAttribute("msg","客户类型为空！!");
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
}

request.setAttribute("CLIENTNO",strClientNo);
request.setAttribute("flag","read");

if(strType.equals("企业"))
{
	strFormId="CMCC02";
}
else if(strType.equals("个人"))
{
	strFormId="100001";
}
else
{
    request.setAttribute("msg","客户种类超出了系统规定范围!");
    rd=request.getRequestDispatcher("/showinfo.jsp");
    rd.forward(request,response);
}

String url= "/templates/defaultform.jsp?Plat_Form_Request_Form_ID="+strFormId+"&Plat_Form_Request_Event_ID=0&flag=read";

rd=request.getRequestDispatcher(url);
rd.forward(request,response);

%>
