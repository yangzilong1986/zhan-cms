<%
    /*
    �������������ص���Ϣ
      */
%>
<%@ page language="java" contentType="text/html; charset=GBK"
         pageEncoding="GBK" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
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
    Log logger = LogFactory.getLog("ccb_return.jsp");

    String privateKey = "30819c300d06092a864886f70d0101";

    String sign = request.getParameter("sign");

    Map params = new HashMap();
    //���POST �����������õ��µ�params��
    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
        String name = (String) iter.next();
        String[] values = (String[]) requestParams.get(name);
        String valueStr = "";
        for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
        }
        //����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת��
        valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
        params.put(name, valueStr);
    }

    String mysign = com.ccb.SignatureHelper_return.sign(params, privateKey);

    if (mysign.equals(request.getParameter("sign"))) {
        request.setCharacterEncoding("GBK");
        
        out.println("��ʾ������Ϣ" + "</br>");
        out.println("�̻�����:" + request.getParameter("merchent_id") + "</br>");
        out.println("��Ȩ��־:" + request.getParameter("accredit_flag") + "</br>");
        out.println("��Ȩ��:" + request.getParameter("accredit_id") + "</br>");
        out.println("���ʽ����޶�:" + request.getParameter("tx_quota") + "</br>");
        out.println("����ǩ��:" + request.getParameter("sign") + "</br>");

        out.println(mysign + "--------------------" + sign + "</br>");
        out.println(" ǩԼ�ɹ�" + "</br>");

        boolean temp = false;
        ConnectionManager manager = ConnectionManager.getInstance();

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
        out.println("ǩԼʧ��" + "</br>");
        session.setAttribute("msg", "�������뻹δ��ɣ����ڽ������е�ǩԼʧ��!");
        session.setAttribute("flag", "0");
    }
    response.sendRedirect("/showinfo.jsp");
%>


</body>
</html>