
<%
	/*
	���ܣ�����֧������ǩԼ���������
	�ӿ����ƣ������е�ǩԼ���۽ӿ�
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
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>֧��������--����</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<%
			String itemUrl = "";
			String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //'֧���ӿڣ����øģ�
			String t4 = "images/alipay_bwrx.gif"; //'֧������ťͼƬ
			String t5 = "�Ƽ�ʹ��֧��������"; //'��ť��ͣ˵��
			String input_charset = AlipayConfig.CharSet; //ҳ����루���øģ�
			String service = "cae_charge_agent";//��������---���ۣ����øģ�

			String partner = AlipayConfig.partnerID; //partner�������ID(����)	
			String key = AlipayConfig.key; //partner�˻���Ӧ��֧������ȫУ����(����)
			String type_code = "TEST100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺100410000192	

            String return_url = "http://218.58.70.181/test/alipay/alipay_return.jsp";
			//�������˻���Ϣ�����д����վ�Լ���
			//*****************************************************************
			String sign_type = "MD5"; //'ǩ����ʽ�����øģ�

            String subject = "commodityname"; //subject ��Ʒ���ơ���վ������

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sFmt.format(new Date());

            String gmt_out_order_create = "2009-05-14 15:15:15"; //�̻���������ʱ��
//            String gmt_out_order_create = date; //�̻���������ʱ��
            String out_order_no = "100100"; //�̻���վ������Ҳ�����ⲿ�����ţ���ͨ���ͻ���վ����֧�������������ظ���
            String amount = "0.01"; //�����ܼ�	0.01��50000.00


//			String subject = request.getParameter("goodName").trim(); //subject ��Ʒ���ơ���վ������
//			String gmt_out_order_create = request.getParameter("ordeCreateTime").trim(); //�̻���������ʱ��
//			String out_order_no = request.getParameter("orderNum").trim(); //�̻���վ������Ҳ�����ⲿ�����ţ���ͨ���ͻ���վ����֧�������������ظ���
//			String amount = request.getParameter("amount").trim(); //�����ܼ�	0.01��50000.00

			String customer_code = request.getParameter("customer_code").trim(); //�����޸�

			//********************************************************************

//			itemUrl = Payment.CreateUrl(paygateway, t4, t5, service, partner,
//					sign_type, subject, gmt_out_order_create, out_order_no,
//					amount, customer_code, key, type_code, input_charset);
            itemUrl = Payment.CreateUrl_zr(paygateway, t4, t5, service, partner,
                    sign_type, subject, gmt_out_order_create, out_order_no,
                    amount, customer_code, key, type_code, input_charset, return_url);
			response.sendRedirect(itemUrl);
		%>

	</head>
</html>
