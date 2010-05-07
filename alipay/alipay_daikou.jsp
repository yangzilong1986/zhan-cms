
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

<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.alipay.util.*"%>
<%@page import="com.alipay.config.*"%>
<%@ page import="com.zt.util.PropertyManager" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>支付宝服务--代扣</title>

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
			String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //'支付接口（不用改）
			String t4 = "/alipay/images/alipay_bwrx.gif"; //'支付宝按钮图片
			String t5 = "推荐使用支付宝付款"; //'按钮悬停说明
			String input_charset = AlipayConfig.CharSet; //页面编码（不用改）
			String service = "cae_charge_agent";//服务名称---代扣（不用改）

			//String partner = AlipayConfig.partnerID; //partner合作伙伴ID(必填)
			//String key = AlipayConfig.key; //partner账户对应的支付宝安全校验码(必填)
			//String type_code = ""; //type_code合作伙伴ID(必填),这个由销售申请，后台开通，可以从销售拿到，例如：100410000192

            String type_code = PropertyManager.getProperty("ZFB_type_code");//type_code合作伙伴ID(必填)
			String partner = PropertyManager.getProperty("ZFB_partnerID");  //支付宝安全校验码(账户内提取)
			String key = PropertyManager.getProperty("ZFB_key");  //key账户对应的支付宝安全校验码(必填)

			//以上是账户信息。请改写成网站自己的
			//*****************************************************************
			String sign_type = "MD5"; //'签名方式（不用改）
			String subject = request.getParameter("goodName").trim(); //subject 商品名称“网站变量”
			String gmt_out_order_create = request
					.getParameter("ordeCreateTime").trim(); //商户订单创建时间
			String out_order_no = request.getParameter("orderNum").trim(); //商户网站订单（也就是外部订单号，是通过客户网站传给支付宝，不可以重复）
			String amount = request.getParameter("amount").trim(); //订单总价	0.01～50000.00

			String customer_code = request.getParameter("customer_code").trim(); //不用修改

			//********************************************************************

			itemUrl = Payment.CreateUrl(paygateway, t4, t5, service, partner,
					sign_type, subject, gmt_out_order_create, out_order_no,
					amount, customer_code, key, type_code, input_charset);
			response.sendRedirect(itemUrl);
		%>

	</head>
</html>
