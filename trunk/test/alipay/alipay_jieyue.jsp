<%
	/*
	功能：设置支付宝所签约代扣需参数
	接口名称：代扣中的解除签约代扣接口
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
		<title>代扣--解约</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<%
			String paygetway = "https://www.alipay.com/cooperate/gateway.do?"; //支付接口访问地址(不用改)
			String service = "customer_unsign"; //服务名称--解约(不用改)
			String sign_type = "MD5"; //签名方式“不用改” 
			String input_charset = AlipayConfig.CharSet;  //页面编码格式(不用改)
			
			String key = AlipayConfig.key; //账户对应的支付宝安全校验码(必填)
			String partner = AlipayConfig.partnerID;  //支付宝安全校验码(账户内提取)	

			String customer_code = request.getParameter("customer_code").trim(); //代扣的客户代码(必填)		

			String itemUrl = Payment.CreateUrl3(paygetway, service,
				 key, partner, customer_code, sign_type,input_charset);
			response.sendRedirect(itemUrl);  //签约成功后返回XML信息
		%>

	</head>
</html>
