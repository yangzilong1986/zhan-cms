<%
    /*
     功能：设置支付宝所签约代扣需参数
     接口名称：代扣中的签约代扣接口
     版本：2.0
     日期：2008-12-31
     作者：支付宝公司销售部技术支持团队
     联系：0571-26888888
     版权：支付宝公司
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

    <title>代扣---签约</title>
    <meta http-equiv="pragma" content="no-cache">
    <%
        String paygeteway = "https://www.alipay.com/cooperate/gateway.do?"; //支付接口访问地址(不用改)
        String service = "customer_sign"; //服务名称--签约(不用改)
        String input_charset = AlipayConfig.CharSet;   //页面编码（不用改）
        //String type_code = "XSCS100011000101"; //type_code合作伙伴ID(必填),这个由销售申请，后台开通，可以从销售拿到，例如：XSCS100011000101
        //String partner = AlipayConfig.partnerID;  //支付宝安全校验码(账户内提取)
        //String key = AlipayConfig.key; //key账户对应的支付宝安全校验码(必填)

        String type_code = PropertyManager.getProperty("ZFB_type_code");//type_code合作伙伴ID(必填)
        String partner = PropertyManager.getProperty("ZFB_partnerID");  //支付宝安全校验码(账户内提取)
        String key = PropertyManager.getProperty("ZFB_key");  //key账户对应的支付宝安全校验码(必填)
        //以上是账户信息。请改写成网站自己的

        String sign_type = "MD5"; //签名方式(不用改)

        String path = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort();
//			String notify_url = path +"/alipay_daikou/alipay_notify.jsp"; //"http://10.2.5.51/liuzhuo/daikou/qianyue/alipay/Return_Alipay_Notify.asp"
//        String notify_url = path + PropertyManager.getProperty("ZFB_notify_url");
        String notify_url =  PropertyManager.getProperty("ZFB_notify_url");
        //服务器通知url（Alipay_Notify.asp文件所在路经）
//			String return_url = path +"/alipay_daikou/alipay_return.jsp";
//        String return_url = path + PropertyManager.getProperty("ZFB_return_url");
        String return_url =  PropertyManager.getProperty("ZFB_return_url");
        //服务器通知url（return_Alipay_Notify.asp文件所在路经）
        //相关参数名称具体含义，可以在支付宝接口服务文档中查询到，
        //以上两个文件，通知正常都可以在notify data目录找到通知过来的日志

        String customer_email = request.getParameter("email").trim(); //签约客户注册支付宝的Email
        String out_sign_no = request.getParameter("out_sign_no").trim(); //外部签约号,一个合作伙伴中唯一
        String is_account_read_only = "true"; //是否签约的帐号可以被修改，若为true且后台开通只读权限则为只读。

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
