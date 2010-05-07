
<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="zt.platform.form.control.ServiceProxy" %>
<%@ page import="zt.platform.form.control.impl.SeviceProxyHttpImpl" %>
<%@ page import="zt.cmsi.pub.*" %>
<%@ page import="java.util.*" %>
<%@ page import="zt.platform.form.util.*" %>


<%
request.setCharacterEncoding("GB2312");

Param param = (Param)request.getAttribute("BMPARAM");
//param.addParam(ParamName.BMNo, request.getParameter(ParamName.BMNo));
//param.addParam(ParamName.BMTransNo, new Integer(request.getParameter(ParamName.BMTransNo)));
//param.addParam(ParamName.ClientName, request.getParameter(ParamName.ClientName));
//param.addParam(ParamName.Flag, request.getParameter(ParamName.Flag));

System.out.println("BMNo:"+param.getParam(ParamName.BMNo));
System.out.println("BMTransNo:"+param.getParam(ParamName.BMTransNo));
System.out.println("ClientName:"+param.getParam(ParamName.ClientName));
System.out.println("Flag:"+param.getParam(ParamName.Flag));
System.out.println("BMActType:"+param.getParam(ParamName.BMActType));
System.out.println("BrhID:"+param.getParam(ParamName.BrhID));
System.out.println("BMPARAM:"+param.toString());
%>