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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>����---ǩԼ</title>
		<meta http-equiv="pragma" content="no-cache">

		<%
			String paygeteway = "https://www.alipay.com/cooperate/gateway.do?"; //֧���ӿڷ��ʵ�ַ(���ø�)
			String service = "customer_sign"; //��������--ǩԼ(���ø�)
			String input_charset = AlipayConfig.CharSet;   //ҳ����루���øģ�
			String type_code = "TEST100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺XSCS100011000101	
			String partner = AlipayConfig.partnerID;  //֧������ȫУ����(�˻�����ȡ)
			String key = AlipayConfig.key; //key�˻���Ӧ��֧������ȫУ����(����)
			//�������˻���Ϣ�����д����վ�Լ���
			String sign_type = "MD5"; //ǩ����ʽ(���ø�)
			
			String path = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort();
			//String notify_url = path +"/alipay_daikou/alipay_notify.jsp"; //"http://10.2.5.51/liuzhuo/daikou/qianyue/alipay/Return_Alipay_Notify.asp"
			String notify_url = path +"/test/alipay/alipay_notify.jsp"; //"http://10.2.5.51/liuzhuo/daikou/qianyue/alipay/Return_Alipay_Notify.asp"
			//������֪ͨurl��Alipay_Notify.asp�ļ�����·���� 
			String return_url = path +"/test/alipay/alipay_return.jsp";
			//������֪ͨurl��return_Alipay_Notify.asp�ļ�����·���� 
			//��ز������ƾ��庬�壬������֧�����ӿڷ����ĵ��в�ѯ����
			//���������ļ���֪ͨ������������notify dataĿ¼�ҵ�֪ͨ��������־
			
			String customer_email = request.getParameter("email").trim(); //ǩԼ�ͻ�ע��֧������Email
			
//			String itemUrl = Payment.CreateUrl2(paygeteway, service, type_code,
//					partner, key, sign_type, notify_url, return_url,
//					customer_email, input_charset);
//			response.sendRedirect(itemUrl);
		%>

	</head>
</html>
