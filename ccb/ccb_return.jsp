<%
    /*
    处理建行网银返回的信息
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
    <title>同步返回处理页面</title>
</head>
<body>
<%
    Log logger = LogFactory.getLog("ccb_return.jsp");

    String privateKey = "30819c300d06092a864886f70d0101";

    String sign = request.getParameter("sign");

    Map params = new HashMap();
    //获得POST 过来参数设置到新的params中
    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
        String name = (String) iter.next();
        String[] values = (String[]) requestParams.get(name);
        String valueStr = "";
        for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
        }
        //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
        valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
        params.put(name, valueStr);
    }

    String mysign = com.ccb.SignatureHelper_return.sign(params, privateKey);

    if (mysign.equals(request.getParameter("sign"))) {
        request.setCharacterEncoding("GBK");
        
        out.println("显示反馈信息" + "</br>");
        out.println("商户代码:" + request.getParameter("merchent_id") + "</br>");
        out.println("授权标志:" + request.getParameter("accredit_flag") + "</br>");
        out.println("授权号:" + request.getParameter("accredit_id") + "</br>");
        out.println("单笔交易限额:" + request.getParameter("tx_quota") + "</br>");
        out.println("数字签名:" + request.getParameter("sign") + "</br>");

        out.println(mysign + "--------------------" + sign + "</br>");
        out.println(" 签约成功" + "</br>");

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
        //打印，收到消息比对sign的计算结果和传递来的sign是否匹配
        out.println(mysign + "--------------------" + sign + "</br>");
        //out.println("responseTxt=" + responseTxt +"</br>");
        out.println("签约失败" + "</br>");
        session.setAttribute("msg", "您的申请还未完成，您在建设银行的签约失败!");
        session.setAttribute("flag", "0");
    }
    response.sendRedirect("/showinfo.jsp");
%>


</body>
</html>