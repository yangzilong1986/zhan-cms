<%
	/*
	���ܣ�֧��������ҳ�棬���ҳ����Լ��ɵ��̻���վ��ʵ�ʲ����Ĵ�����Ը���ҵ�����
	�ӿ����ƣ����۽ӿ�
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
		<title>֧��������--����</title>
		<meta http-equiv="pragma" content="no-cache">
	</head>

	<body bgcolor="#FF6600">
		<form action="alipay_daikou.jsp" method="post" name="qianyue" target="_blank">
        	<table>
            	<tr>
                	<td colspan="2">
                    	<a href="http://www.alipay.com"><img src="/alipay/images/logo.gif" border="0"/></a>
                    </td>
                </tr>
                <%
					UtilDate date = new UtilDate();
					%>
                <tr>
                	<td>���۶����ţ�
                    </td>
                    <td><input type="text" name="orderNum" value="<%=date.getOrderNum()%>"/>
                    </td>
                </tr>
            	<tr>
                	<td>��Ʒ���ƣ�
                    </td>
                    <td><input type="text" name="goodName" value="֧�������Դ��۽ӿ�"/>
                    </td>
                </tr>
                <tr>
                	<td>�̻���������ʱ�䣺
                    </td>
                    <td><input type="text" name="ordeCreateTime" value="<%=date.getDateFormatter()%>"/>
                    </td>
                </tr>
                <tr>
                	<td>���۽�
                    </td>
                    <td><input type="text" name="amount" value="0.01"/>
                    </td>
                </tr>
                <tr>
                	<td>����֧�����ͻ�ǩԼ���룺
                    </td>
                    <td><input type="text" name="customer_code"/>
                    </td>
                </tr>
                <tr>
                	<td>������
                    </td>
                	<td>
                    	<input type="submit" name="button" id="button" value="����" />
                    </td>
                </tr>
            </table>
		</form>
	</body>
</html>
