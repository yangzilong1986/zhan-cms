<%
	/*
	���ƣ���������ת��ҳ��
	���ܣ�ͬ���������ع��ܣ������׳��ֵ���.
	�汾��2.0
	���ڣ�2008-12-31
	���ߣ�֧������˾���۲�����֧���Ŷ�
	��ϵ��0571-26888888
	��Ȩ��֧������˾
	 */
%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.alipay.util.*"%>
<%@page import="com.alipay.config.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>ͬ�����ش���ҳ��</title>
	</head>
	<body>
		<%
			String partner = AlipayConfig.partnerID; //partner�������id��������д��
			String privateKey = AlipayConfig.key; //partner �Ķ�Ӧ���װ�ȫУ���루������д��
			//**********************************************************************************
			//�������������֧��https����������ʹ��http����֤��ѯ��ַ
			/*ע�������ע�ͣ�����ڲ��Ե�ʱ����response���ڿ�ֵ��������뽫����һ��ע�ͣ�������һ����֤���ӣ������鱾�ض˿ڣ�
			�뵲��80����443�˿�*/
			//String alipayNotifyURL = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify"
			String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
					+ "partner="
					+ partner;
			//**********************************************************************************		
			String sign = request.getParameter("sign");
			
			//��ȡ֧����ATN���ؽ����true����ȷ�Ķ�����Ϣ��false ����Ч��
			String responseTxt = CheckURL.check(alipayNotifyURL);

			Map params = new HashMap();
			//���POST �����������õ��µ�params��
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
				}
				//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת���������Ѿ�ʹ�ã�
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
				params.put(name, valueStr);
			}

			String mysign = com.alipay.util.SignatureHelper_return.sign(params,
					privateKey);

			if (mysign.equals(request.getParameter("sign"))
					&& responseTxt.equals("true")) {
				//�����ǻ�Ա����Ĳ�����Ϣ 
				String get_buyerUserId = ""; //���id
				String get_customer_code = "";  //�ͻ�����customer_code
				if (request.getParameter("buyerUserId") == null
				|| request.getParameter("buyerUserId").equals("")) {
					get_buyerUserId = "û�еõ����id";
				} else {
					get_buyerUserId = request.getParameter("buyerUserId");
				}
				
				if (request.getParameter("customer_code") == null
				|| request.getParameter("customer_code").equals("")) {
					get_customer_code = "û�еõ����id";
				} else {
					get_customer_code = request.getParameter("customer_code");
				}
				
				out.println("��ʾ������Ϣ" +"</br>");
				out.println("����ʻ�:" + get_buyerUserId +"</br>");
				out.println("�ͻ�����:" + get_customer_code +"</br>");
				out.println("�ͻ�����:" + request.getParameter("cert_no") +"</br>");
				out.println("ǩԼ��֤������:" + request.getParameter("cert_type") +"</br>");
				out.println("�û�email:" + request.getParameter("email") +"</br>");
				out.println("�Ƿ�ɹ�:" + request.getParameter("is_success") +"</br>");
				out.println("ǩԼ�˺�:" + request.getParameter("sign_account_no") +"</br>");
				out.println("ǩԼuserid:" + request.getParameter("sign_user_id") +"</br>");
				out.println("ǩԼ������:" + request.getParameter("sign_user_name") +"</br>");
				out.println("����ҵ����:" + request.getParameter("type_code") +"</br>");
				
				out.println(mysign + "--------------------" + sign +"</br>");
				out.println("���۳ɹ�" +"</br>");
				
				out.println("responseTxt=" + responseTxt);
			} else {
				//��ӡ���յ���Ϣ�ȶ�sign�ļ������ʹ�������sign�Ƿ�ƥ��
				out.println(mysign + "--------------------" + sign +"</br>");
				out.println("responseTxt=" + responseTxt +"</br>");
				out.println("����ʧ��" +"</br>");
			}
		%>



	</body>
</html>