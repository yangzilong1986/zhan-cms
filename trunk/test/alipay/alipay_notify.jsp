<%
	/*
	���ƣ���������з�����֪ͨҳ��
	���ܣ�������֪ͨ���أ�������ֵ���������Ƽ�ʹ��
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
			+ partner
			+ "&notify_id="
			+ request.getParameter("notify_id");
	//**********************************************************************************

	//��ȡ֧����ATN���ؽ����true����ȷ�Ķ�����Ϣ��false ����Ч��
	String responseTxt = CheckURL.check(alipayNotifyURL);
	System.out.println("---responseTxt----" + responseTxt);
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
		System.out.println("-------" + name + "--" + valueStr);
		//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת���������Ѿ�ʹ�ã�
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
		params.put(name, valueStr);
	}

	String mysign = com.alipay.util.SignatureHelper.sign(params,
			privateKey);
	
	if (mysign.equals(request.getParameter("sign"))
			&& responseTxt.equals("true")) {
		/*�����ڲ�ͬ״̬�»�ȡ������Ϣ�������̻����ݿ�ʹ����ͬ��*/
		if (request.getParameter("is_success") == "T") {
			//�ȴ���Ҹ���
			//���������д�����ݴ���
			out.println("success"); //ע��һ��Ҫ���ظ�֧����һ���ɹ�����Ϣ
		} 
	} else {
		out.println("fail" + "<br>");
		//��ӡ���յ���Ϣ�ȶ�sign�ļ������ʹ�������sign�Ƿ�ƥ��
		out.println(mysign + "--------------------"
				+ request.getParameter("sign") + "<br>");
	}
%>
