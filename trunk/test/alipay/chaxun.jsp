<%
	/*
	���ܣ�֧��������ҳ�棬���ҳ����Լ��ɵ��̻���վ��ʵ�ʲ����Ĵ�����Ը���ҵ�����
	�ӿ����ƣ������еĲ�ѯ���۽���Ľӿ�
	�汾��2.0
	���ڣ�2008-12-31
	���ߣ�֧������˾���۲�����֧���Ŷ�
	��ϵ��0571-26888888
	��Ȩ��֧������˾
	 */
%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.alipay.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>֧��������</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>
	<body bgcolor="#FF6600">
		<form action="alipay_chaxun.jsp" method="post" name="myform">
			<table border="0" cellspacing="1" cellpadding="0" align="left"
				valign=absmiddle width=450>
                <tr>
                	<td>
                    	<a href="http://www.alipay.com"><img src="images/logo.gif" border="0"/></a>
                    </td>
                </tr>
				<tr>
					<td align=center width=50% height=120>
						<script type="text/javascript" language="javascript"
							src="js/calendar.js"></script>
						����ʱ����ʼʱ�䣺
						<input id="gmt_create_start" name="gmt_create_start"
							onfocus="setday(this)" />*
						<br>
						����ʱ����ʼʱ�䣺
						<input id="gmt_create_end" name="gmt_create_end"
							onfocus="setday(this)" />*
						
						<br>
						<br>
						������<input name="submit" type="submit" value="����������ѯ" />
					</td>
				</tr>
			</table>
		</form>

	</body>
</html>