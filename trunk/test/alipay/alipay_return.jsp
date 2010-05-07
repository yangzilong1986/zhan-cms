<%
	/*
	名称：付完款后跳转的页面
	功能：同服务器返回功能，但容易出现掉单.
	版本：2.0
	日期：2008-12-31
	作者：支付宝公司销售部技术支持团队
	联系：0571-26888888
	版权：支付宝公司
	 */
%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.alipay.util.*"%>
<%@page import="com.alipay.config.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>同步返回处理页面</title>
	</head>
	<body>
		<%
			String partner = AlipayConfig.partnerID; //partner合作伙伴id（必须填写）
			String privateKey = AlipayConfig.key; //partner 的对应交易安全校验码（必须填写）
			//**********************************************************************************
			//如果您服务器不支持https交互，可以使用http的验证查询地址
			/*注意下面的注释，如果在测试的时候导致response等于空值的情况，请将下面一个注释，打开上面一个验证连接，另外检查本地端口，
			请挡开80或者443端口*/
			//String alipayNotifyURL = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify"
			String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
					+ "partner="
					+ partner;
			//**********************************************************************************		
			String sign = request.getParameter("sign");
			
			//获取支付宝ATN返回结果，true是正确的订单信息，false 是无效的
			String responseTxt = CheckURL.check(alipayNotifyURL);

			Map params = new HashMap();
			//获得POST 过来参数设置到新的params中
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
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化（现在已经使用）
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
				params.put(name, valueStr);
			}

			String mysign = com.alipay.util.SignatureHelper_return.sign(params,
					privateKey);

			if (mysign.equals(request.getParameter("sign"))
					&& responseTxt.equals("true")) {
				//以下是会员共享的部分信息 
				String get_buyerUserId = ""; //买家id
				String get_customer_code = "";  //客户代码customer_code
				if (request.getParameter("buyerUserId") == null
				|| request.getParameter("buyerUserId").equals("")) {
					get_buyerUserId = "没有得到买家id";
				} else {
					get_buyerUserId = request.getParameter("buyerUserId");
				}
				
				if (request.getParameter("customer_code") == null
				|| request.getParameter("customer_code").equals("")) {
					get_customer_code = "没有得到买家id";
				} else {
					get_customer_code = request.getParameter("customer_code");
				}
				
				out.println("显示订单信息" +"</br>");
				out.println("买家帐户:" + get_buyerUserId +"</br>");
				out.println("客户代码:" + get_customer_code +"</br>");
				out.println("客户代码:" + request.getParameter("cert_no") +"</br>");
				out.println("签约人证件类型:" + request.getParameter("cert_type") +"</br>");
				out.println("用户email:" + request.getParameter("email") +"</br>");
				out.println("是否成功:" + request.getParameter("is_success") +"</br>");
				out.println("签约账号:" + request.getParameter("sign_account_no") +"</br>");
				out.println("签约userid:" + request.getParameter("sign_user_id") +"</br>");
				out.println("签约人姓名:" + request.getParameter("sign_user_name") +"</br>");
				out.println("代理业务编号:" + request.getParameter("type_code") +"</br>");
				
				out.println(mysign + "--------------------" + sign +"</br>");
				out.println("代扣成功" +"</br>");
				
				out.println("responseTxt=" + responseTxt);
			} else {
				//打印，收到消息比对sign的计算结果和传递来的sign是否匹配
				out.println(mysign + "--------------------" + sign +"</br>");
				out.println("responseTxt=" + responseTxt +"</br>");
				out.println("代扣失败" +"</br>");
			}
		%>



	</body>
</html>