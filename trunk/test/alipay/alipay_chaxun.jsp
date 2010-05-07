<%
	/*
	功能：设置支付宝所签约代扣需参数
	接口名称：代扣中的查询代扣情况接口
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>支付宝--查询代扣情况</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<%
			//Date Now_Date=new Date();
			String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //支付接口（不用改）
			String service = "cae_batch_query_charge_agent";//代扣批量查询接口（不用改）
			String sign_type = "MD5";   //加密机制（不用改）
			String input_charset = AlipayConfig.CharSet;   //页面编码（不用改）
			
			String partner = AlipayConfig.partnerID; //支付宝合作伙伴id (账户内提取)
			String key = AlipayConfig.key; //支付宝安全校验码(账户内提取)
			//注意：开始和结束的间隔天数不能超过2天 
			String gmt_create_start = request.getParameter("gmt_create_start")
					.trim(); //支付宝订单创建时间：开始时间 格式：YYYY-MM-DD hh:mm:ss   
			String gmt_create_end = request.getParameter("gmt_create_end")
					.trim(); //支付宝订单创建时间：结束时间 格式：YYYY-MM-DD hh:mm:ss 
			
			String type_code = "TEST100011000101"; //type_code合作伙伴ID(必填),这个由销售申请，后台开通，可以从销售拿到。例如：XSCS100011000101

			String ItemUrl = Payment.CreateUrl4(paygateway, service, partner,
					sign_type, gmt_create_start, gmt_create_end, type_code, key, input_charset);

			out.println(ItemUrl);

			response.sendRedirect(ItemUrl);
			//解析XML
			//dom4j dom4 = new dom4j();
			//String result = dom4.DomXml(ItemUrl);
		%>

	</head>

	<body>

	</body>
</html>
