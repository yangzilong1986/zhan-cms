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
         pageEncoding="GBK" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>ͬ�����ش���ҳ��</title>
</head>
<body>
<%
    //String partner = AlipayConfig.partnerID; //partner�������id��������д��
    String partner = PropertyManager.getProperty("ZFB_partnerID"); //partner�������id��������д��
    //String privateKey = AlipayConfig.key; //partner �Ķ�Ӧ���װ�ȫУ���루������д��
    String privateKey = PropertyManager.getProperty("ZFB_key"); //partner �Ķ�Ӧ���װ�ȫУ���루������д��
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
    //String responseTxt = CheckURL.check(alipayNotifyURL);

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

//			if (mysign.equals(request.getParameter("sign")) && responseTxt.equals("true")) {
    if (mysign.equals(request.getParameter("sign"))) {
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
        request.setCharacterEncoding("GBK");
        out.println("��ʾ������Ϣ" + "</br>");
        out.println("����ʻ�:" + get_buyerUserId + "</br>");
        out.println("�ͻ�����:" + get_customer_code + "</br>");
        out.println("�ͻ�����:" + request.getParameter("cert_no") + "</br>");
        out.println("�ⲿǩԼ��:" + request.getParameter("out_sign_no") + "</br>");
        out.println("ǩԼ��֤������:" + request.getParameter("cert_type") + "</br>");
        out.println("�û�email:" + request.getParameter("email") + "</br>");
        out.println("�Ƿ�ɹ�:" + request.getParameter("is_success") + "</br>");
        out.println("ǩԼ�˺�:" + request.getParameter("sign_account_no") + "</br>");
        out.println("ǩԼuserid:" + request.getParameter("sign_user_id") + "</br>");
        out.println("ǩԼ������:" + request.getParameter("sign_user_name") + "</br>");
        out.println("����ҵ����:" + request.getParameter("type_code") + "</br>");
        out.println("ǩԼ���ֻ�:" + request.getParameter("user_cell") + "</br>");
        out.println("�Ƿ��Ķ���֪ͨ:" + request.getParameter("sms_service") + "</br>");
        out.println("��ͨ����:" + request.getParameter("katong_bank") + "</br>");
        out.println("���п���:" + request.getParameter("bank_card_no") + "</br>");

        out.println(mysign + "--------------------" + sign + "</br>");
        out.println("���۳ɹ�" + "</br>");

        boolean temp = false;
        ConnectionManager manager = ConnectionManager.getInstance();

        String sta = manager.getCellValue("APPSTATUS", "XFAPP", "seqno='" + request.getParameter("out_sign_no") + "'");
        sta = (sta == null) ? "0" : sta;

        if (Float.parseFloat(sta) < 1 && Float.parseFloat(sta) != 0) {
            String sql1 = "update XFAPP " +
                    "set APPSTATUS='1',CUSTOMER_CODE='" + get_customer_code + "',SIGN_ACCOUNT_NO='" + request.getParameter("sign_account_no") + "' " +
                    "where seqno='" + request.getParameter("out_sign_no") + "'";
            System.out.println("alipay_return.jsp:" + sql1);
            temp = manager.ExecCmd(sql1);
        }

        if (temp) {
            session.setAttribute("msg", "�����������ύ�ɹ������ڵ���ȷ�����������Ĵ�ӡ����ѡ���������뵥��ӡ��ǩ����������ͬ����֤���ļ���ӡ��һͬ���͸����ǣ����ͨ�������ǽ�����ȡ����ϵ!");
            session.setAttribute("funcdel", "window.opener.document.getElementById('print').click();pageWinClose();");
        } else {
            session.setAttribute("msg", "�����������ύ�ɹ����������뵥״̬��������ϵ����!");
            session.setAttribute("flag", "0");
        }
        session.setAttribute("isback", "0");
        %>
 <script type="text/javascript">
    parent.opener.opener.location.reload();
</script>
<%
        //out.println("responseTxt=" + responseTxt);
    } else {
        //��ӡ���յ���Ϣ�ȶ�sign�ļ������ʹ�������sign�Ƿ�ƥ��
        out.println(mysign + "--------------------" + sign + "</br>");
        //out.println("responseTxt=" + responseTxt +"</br>");
        out.println("����ʧ��" + "</br>");
        session.setAttribute("msg", "�������뻹δ��ɣ�����֧������ǩԼʧ�ܣ�����ϵ֧����!");
        session.setAttribute("flag", "0");
    }
    response.sendRedirect("/showinfo.jsp");
%>


</body>
</html>