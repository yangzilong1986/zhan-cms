<%@page contentType="text/html; charset=gb2312" language="java"%>
<%
/*
* @Description: ��Ǯ����ҿ��׸��ۿ����ؽӿڷ���
* @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
* @version 2.0
*/

/*
�ڱ��ļ��У��̼�Ӧ�����ݿ��У���ѯ��������״̬��Ϣ�Լ������Ĵ�����������֧������Ӧ����ʾ��

������������򵥵�ģʽ��ֱ�Ӵ�receiveҳ���ȡ֧��״̬��ʾ���û���
*/

    String msg=(String)request.getParameter("msg").trim();

    System.out.println("============show.jsp=========");

    session.setAttribute("msg", "��Ǯ���۽����" + msg);
    session.setAttribute("flag", "0");

    response.sendRedirect("/showinfo.jsp");


%>
<%--<!doctype html public "-//w3c//dtd html 4.0 transitional//en" >--%>
<%--<html>--%>
	<%--<head>--%>
		<%--<title>��Ǯ���׸����</title>--%>
		<%--<meta http-equiv="content-type" content="text/html; charset=gb2312" >--%>
	<%--</head>--%>
	<%----%>
<%--<BODY>--%>
	<%----%>
	<%--<div align="center">--%>
		<%--<table width="259" border="0" cellpadding="1" cellspacing="1" bgcolor="#CCCCCC" >--%>
			<%--<tr bgcolor="#FFFFFF">--%>
				<%--<td width="80">�ۿʽ:</td>--%>
				<%--<td >��Ǯ[99bill]</td>--%>
			<%--</tr>--%>
			<%--<tr bgcolor="#FFFFFF">--%>
				<%--<td>�ۿ���:</td>--%>
				<%--<td><%=msg %></td>--%>
			<%--</tr>--%>
			<%--<tr>--%>
				<%--<td></td>--%>
				<%--<td></td>--%>
			<%--</tr>--%>
	  <%--</table>--%>
	<%--</div>--%>

<%--</BODY>--%>
<%--</HTML>--%>