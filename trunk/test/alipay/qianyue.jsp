
<%
	/*
	���ܣ�֧��������ҳ�棬���ҳ����Լ��ɵ��̻���վ��ʵ�ʲ����Ĵ�����Ը���ҵ�����
	�ӿ����ƣ����۽ӿ��е�ǩԼ���۽ӿ�
	�汾��2.0
	���ڣ�2008-12-31
	���ߣ�֧������˾���۲�����֧���Ŷ�
	��ϵ��0571-26888888
	��Ȩ��֧������˾
	 */
%>
<em><%@ page language="java" import="java.util.*"
		pageEncoding="GBK"%> <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html>
		<head>
			<title>֧����--ǩԼ����</title>
			<meta http-equiv="pragma" content="no-cache">
		</head>

		<body bgcolor="#FF6600">
			<form action="alipay_qianyue.jsp" method="post" name="qianyue"
				target="_blank">
				<table>
					<tr>
						<td colspan="2">
							<a href="http://www.alipay.com"><img src="images/logo.gif"
									border="0" />
							</a>
						</td>
					</tr>
					<tr>
						<td>
							����ҵ���ţ�
						</td>
						<td>
							<input type="text" name="type_code" value="TEST100011000101"
								readonly />
							<span style="color: #FF0000;">*</span>type_code�������ID(����),����ɿͻ��������룬��̨��ͨ�����Դӿͻ������õ�
						</td>
					</tr>
					<tr>
						<td>
							ǩԼe_mail��
						</td>
						<td>
							<input type="text" name="email" />
							<span style="color: #FF0000;">*</span>ǩԼ���˻���֧�����˻�
						</td>
					</tr>
					<tr>
						<td>
							������
						</td>
						<td>
							<input type="submit" name="button" id="button" value="ǩԼ" />
						</td>
					</tr>
				</table>

			</form>
		</body>
	</html> </em>