<%
    /*
        ��������ǩԼ
        ����	����	����	��ע
        MERCHANTID	�̻�����	CHAR(15)	�ɽ���ͳһ����
        POSID	�̻���̨����	CHAR(9)	�ɽ���ͳһ���䣬
        BRANCHID	���д���	CHAR(9)	�ɽ���ͳһָ��
        AUTHID	��Ȩ��	CHAR(30)	���̻��ṩ
        
        PAYMENT	��Ȩ���	NUMBER(16,2)	���̻��ṩ����ʵ�ʽ�����
        CURCODE	����	CHAR(2)	ȱʡΪ01�������
        ��ֻ֧�������֧����
        TXCODE	������	CHAR(6)	�ɽ���ͳһ����Ϊ520105
        REM1	��ע1	CHAR(30)	����������ֱ�Ӵ���������
        REM2	��ע2	CHAR(30)	����������ֱ�Ӵ���������
        PUB32	��Կǰ30λ	CHAR(30)	ֻ����MD5����ʱʹ�ã�����ʱ���á�
        MAC	MACУ����	CHAR(32)	���ñ�׼MD5�㷨�����̻�ʵ��
      */

%>
<%@ page language="java" import="com.alipay.config.AlipayConfig" pageEncoding="GBK" %>
<%@page import="com.ccb.Payment" %>
<%@page import="com.zt.util.PropertyManager" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

    <title>����--ǩԼ</title>
    <meta http-equiv="pragma" content="no-cache">
    <%
        Log logger = LogFactory.getLog("ccb_qianyue.jsp");

        String paygeteway = "https://ibsbjstar.ccb.com.cn/app/ccbMain?";

        String merchantid = "105370257320044";
        String posid = "100000192";
        String branchid = "371000000";
        String authid =  request.getParameter("out_sign_no");    //�ͻ�ǩԼ��

        //TODO:
        String payment = "1000.00";
        String curcode = "01";
        String txcode = "520105";
        String rem1 = "test";
        String rem2 = "test";
        String pub32 = "30819c300d06092a864886f70d0101";  //ֻ����MD5����ʱʹ�ã�����ʱ���á�
        String mac = "";

//        String service = "customer_sign"; //��������--ǩԼ(���ø�)
        String input_charset = AlipayConfig.CharSet;   //ҳ����루���øģ�
        //String type_code = "XSCS100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺XSCS100011000101
        //String partner = AlipayConfig.partnerID;  //֧������ȫУ����(�˻�����ȡ)
        //String key = AlipayConfig.key; //key�˻���Ӧ��֧������ȫУ����(����)

//        String type_code = PropertyManager.getProperty("ZFB_type_code");//type_code�������ID(����)
//        String partner = PropertyManager.getProperty("ZFB_partnerID");  //֧������ȫУ����(�˻�����ȡ)
//        String key = PropertyManager.getProperty("ZFB_key");  //key�˻���Ӧ��֧������ȫУ����(����)
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

        Map params = new LinkedHashMap();
        params.put("MERCHANTID", merchantid);
        params.put("POSID", posid);
        params.put("BRANCHID", branchid);
        params.put("AUTHID", authid);
        
        params.put("PAYMENT", payment);
        params.put("CURCODE", curcode);
        params.put("TXCODE", txcode);
        params.put("REMARK1", rem1);
        params.put("REMARK2", rem2);

        params.put("PUB32", pub32);

        String itemUrl = Payment.CreateUrlQianyue(pub32, input_charset, params);
        itemUrl = "https://ibsbjstar.ccb.com.cn/app/ccbMain?" +   itemUrl;
        logger.info("CCBǩԼ���͵�URL="+itemUrl);
        response.sendRedirect(itemUrl);
    %>
</head>
</html>
