<%
	/*
	���ܣ�֧��������ҳ�棬���ҳ����Լ��ɵ��̻���վ��ʵ�ʲ����Ĵ�����Ը���ҵ�����
	�ӿ����ƣ������еĽ��ǩԼ���۽ӿ�
	�汾��2.0
	���ڣ�2008-12-31
	���ߣ�֧������˾���۲�����֧���Ŷ�
	��ϵ��0571-26888888
	��Ȩ��֧������˾
	 */
%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>֧��������--���ǩԼ����</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>

	<body bgcolor="#FF6600">
		<form action="alipay_jieyue.jsp" method="post" name="qianyue" target="_blank">
        	<table>
            	<tr>
                	<td colspan="2">
                    	<a href="http://www.alipay.com"><img src="images/logo.gif" border="0"/></a>
                    </td>
                </tr>
            	<tr>
                	<td>��Լ�ͻ�����:</td>
                    <td><input name="customer_code" type="text" />
                    <span style="color:#FF0000;">*</span>����Լ֧����customer_code,��ǩԼ�ش��������ҵ�
                    </td>
                </tr>
                <tr>
                	<td>
                    ������
                    </td>
                    <td><input type="submit" name="button" id="button" value="����--��Լ" /></td>
                </tr>
                <tr>
                	<td>
                    ��ע��
                    </td>
                    <td>�û�ÿǩ��һ��type_codeЭ��,����õ�һ��Ψһ�Ŀͻ����룬<br>�紫��ò��������������ҵ�����</td>
                </tr>
            </table>
		</form>
	</body>
</html>
