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
<%@ page language="java" import="com.alipay.config.AlipayConfig" pageEncoding="GBK" %>
<%@page import="com.alipay.util.Payment" %>
<%@page import="com.zt.util.PropertyManager" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

    <title>����---ǩԼ</title>
    <meta http-equiv="pragma" content="no-cache">
    <%
        String paygeteway = "https://www.alipay.com/cooperate/gateway.do?"; //֧���ӿڷ��ʵ�ַ(���ø�)
        String service = "customer_sign"; //��������--ǩԼ(���ø�)
        String input_charset = AlipayConfig.CharSet;   //ҳ����루���øģ�
        //String type_code = "XSCS100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺XSCS100011000101
        //String partner = AlipayConfig.partnerID;  //֧������ȫУ����(�˻�����ȡ)
        //String key = AlipayConfig.key; //key�˻���Ӧ��֧������ȫУ����(����)

        String type_code = PropertyManager.getProperty("ZFB_type_code");//type_code�������ID(����)
        String partner = PropertyManager.getProperty("ZFB_partnerID");  //֧������ȫУ����(�˻�����ȡ)
        String key = PropertyManager.getProperty("ZFB_key");  //key�˻���Ӧ��֧������ȫУ����(����)
        //�������˻���Ϣ�����д����վ�Լ���

        String sign_type = "MD5"; //ǩ����ʽ(���ø�)

        String path = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort();
//			String notify_url = path +"/alipay_daikou/alipay_notify.jsp"; //"http://10.2.5.51/liuzhuo/daikou/qianyue/alipay/Return_Alipay_Notify.asp"
//        String notify_url = path + PropertyManager.getProperty("ZFB_notify_url");
        String notify_url =  PropertyManager.getProperty("ZFB_notify_url");
        //������֪ͨurl��Alipay_Notify.asp�ļ�����·����
//			String return_url = path +"/alipay_daikou/alipay_return.jsp";
//        String return_url = path + PropertyManager.getProperty("ZFB_return_url");
        String return_url =  PropertyManager.getProperty("ZFB_return_url");
        //������֪ͨurl��return_Alipay_Notify.asp�ļ�����·����
        //��ز������ƾ��庬�壬������֧�����ӿڷ����ĵ��в�ѯ����
        //���������ļ���֪ͨ������������notify dataĿ¼�ҵ�֪ͨ��������־

        String customer_email = request.getParameter("email").trim(); //ǩԼ�ͻ�ע��֧������Email
        String out_sign_no = request.getParameter("out_sign_no").trim(); //�ⲿǩԼ��,һ�����������Ψһ
        String is_account_read_only = "true"; //�Ƿ�ǩԼ���ʺſ��Ա��޸ģ���Ϊtrue�Һ�̨��ֻͨ��Ȩ����Ϊֻ����

        Map params = new HashMap();
        params.put("service", service);
        params.put("partner", partner);
        params.put("type_code", type_code);
        params.put("notify_url", notify_url);
        params.put("return_url", return_url);
        params.put("customer_email", customer_email);
        params.put("out_sign_no", out_sign_no);
        params.put("input_charset", input_charset);
        params.put("is_account_read_only", is_account_read_only);

        String itemUrl = Payment.CreateUrl2(paygeteway, key, sign_type, input_charset, params);
        response.sendRedirect(itemUrl);
    %>
</head>
</html>
