<%
	/*
	���ܣ�����֧������ǩԼ���������
	�ӿ����ƣ������еĲ�ѯ��������ӿ�
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
		<title>֧����--��ѯ�������</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<%
			//Date Now_Date=new Date();
			String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //֧���ӿڣ����øģ�
			String service = "cae_batch_query_charge_agent";//����������ѯ�ӿڣ����øģ�
			String sign_type = "MD5";   //���ܻ��ƣ����øģ�
			String input_charset = AlipayConfig.CharSet;   //ҳ����루���øģ�
			
			String partner = AlipayConfig.partnerID; //֧�����������id (�˻�����ȡ)
			String key = AlipayConfig.key; //֧������ȫУ����(�˻�����ȡ)
			//ע�⣺��ʼ�ͽ����ļ���������ܳ���2�� 
			String gmt_create_start = request.getParameter("gmt_create_start")
					.trim(); //֧������������ʱ�䣺��ʼʱ�� ��ʽ��YYYY-MM-DD hh:mm:ss   
			String gmt_create_end = request.getParameter("gmt_create_end")
					.trim(); //֧������������ʱ�䣺����ʱ�� ��ʽ��YYYY-MM-DD hh:mm:ss 
			
			String type_code = "TEST100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺XSCS100011000101

			String ItemUrl = Payment.CreateUrl4(paygateway, service, partner,
					sign_type, gmt_create_start, gmt_create_end, type_code, key, input_charset);

			out.println(ItemUrl);

			response.sendRedirect(ItemUrl);
			//����XML
			//dom4j dom4 = new dom4j();
			//String result = dom4.DomXml(ItemUrl);
		%>

	</head>

	<body>

	</body>
</html>
