<%
	/*
	���ܣ�����֧������ǩԼ���������
	�ӿ����ƣ������еĽ��ǩԼ���۽ӿ�
	�汾��2.0
	���ڣ�2008-12-31
	���ߣ�֧������˾���۲�����֧���Ŷ�
	��ϵ��0571-26888888
	��Ȩ��֧������˾
	 */
%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.alipay.util.*"%>
<%@page import="com.alipay.config.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>����--��Լ</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<%
			String paygetway = "https://www.alipay.com/cooperate/gateway.do?"; //֧���ӿڷ��ʵ�ַ(���ø�)
			String service = "customer_unsign"; //��������--��Լ(���ø�)
			String sign_type = "MD5"; //ǩ����ʽ�����øġ� 
			String input_charset = AlipayConfig.CharSet;  //ҳ������ʽ(���ø�)
			
			String key = AlipayConfig.key; //�˻���Ӧ��֧������ȫУ����(����)
			String partner = AlipayConfig.partnerID;  //֧������ȫУ����(�˻�����ȡ)	

			String customer_code = request.getParameter("customer_code").trim(); //���۵Ŀͻ�����(����)		

			String itemUrl = Payment.CreateUrl3(paygetway, service,
				 key, partner, customer_code, sign_type,input_charset);
			response.sendRedirect(itemUrl);  //ǩԼ�ɹ��󷵻�XML��Ϣ
		%>

	</head>
</html>
