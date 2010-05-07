<%
	/*
	名称：付款过程中服务器通知页面
	功能：服务器通知返回，不会出现掉单情况，推荐使用
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
			+ partner
			+ "&notify_id="
			+ request.getParameter("notify_id");
	//**********************************************************************************

	//获取支付宝ATN返回结果，true是正确的订单信息，false 是无效的
	String responseTxt = CheckURL.check(alipayNotifyURL);
	System.out.println("---responseTxt----" + responseTxt);
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
		System.out.println("-------" + name + "--" + valueStr);
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化（现在已经使用）
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
		params.put(name, valueStr);
	}

	String mysign = com.alipay.util.SignatureHelper.sign(params,
			privateKey);
	
	if (mysign.equals(request.getParameter("sign"))
			&& responseTxt.equals("true")) {
		/*可以在不同状态下获取订单信息，操作商户数据库使数据同步*/
		if (request.getParameter("is_success") == "T") {
			//等待买家付款
			//在这里可以写入数据处理
			out.println("success"); //注意一定要返回给支付宝一个成功的信息
		} 
	} else {
		out.println("fail" + "<br>");
		//打印，收到消息比对sign的计算结果和传递来的sign是否匹配
		out.println(mysign + "--------------------"
				+ request.getParameter("sign") + "<br>");
	}
%>
