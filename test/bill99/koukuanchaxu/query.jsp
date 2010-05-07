<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util"%>
<%@page import="com._99bill.www.bankdebit.services.bankDebitQuery.*"%>
<%@page import="com.bill99.seashell.domain.dto.bankdebit.*"%>
<%@ page import="com.zt.util.PropertyManager" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" >
</head>
<br/>
<body>

<%
String inputCharset = request.getParameter("inputCharset");
String version = request.getParameter("version");
String signType = request.getParameter("signType");
String merchantAcctId = request.getParameter("merchantAcctId");
String queryType = request.getParameter("queryType");
String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");
String orderId = request.getParameter("orderId");

//String key = "1234567891234567";
       String key = PropertyManager.getProperty("KQ_key");
    String signMsg = request.getParameter("signMsg");

QueryBankDebitRequest queryRequest = new QueryBankDebitRequest();
queryRequest.setInputCharset(inputCharset);
queryRequest.setVersion(version);
queryRequest.setSignType(signType);
queryRequest.setMerchantAcctId(merchantAcctId);
queryRequest.setQueryType(queryType);
queryRequest.setOrderId(orderId);
queryRequest.setStartTime(startTime);
queryRequest.setEndTime(endTime);
queryRequest.setSignMsg(signMsg);

BankDebitQueryServiceLocator locator = new BankDebitQueryServiceLocator();
QueryBankDebitResponse queryResponse = locator.getbankDebitQuery().bankDebitQuery(queryRequest);

String signMsgVal="";
signMsgVal = AppendParam(signMsgVal,"version",queryResponse.getVersion() );
signMsgVal = AppendParam(signMsgVal,"signType",""+queryResponse.getSignType());
signMsgVal = AppendParam(signMsgVal,"merchantAcctId",queryResponse.getMerchantAcctId());
signMsgVal = AppendParam(signMsgVal,"errCode",queryResponse.getErrCode());
signMsgVal = AppendParam(signMsgVal,"key",key);

String mysignMsg = MD5Util.md5Hex(signMsgVal.getBytes()).toUpperCase();
%>
<%!
public String AppendParam(String ReturnStr,String ParamId,String ParamValue)
{
		if(!ReturnStr.equals(""))
		{
			if(!ParamValue.equals(""))
			{
				ReturnStr=ReturnStr+"&"+ParamId+"="+ParamValue;
			}                                                                                             
		}
		else
		{
			if(!ParamValue.equals(""))
			{
				ReturnStr=ParamId+"="+ParamValue;
			}
		}	
		return ReturnStr;
}
%>
<table border="2">
<tr>
	<td>网关版本version: </td>
	<td><%= queryResponse.getVersion() %></td>
</tr>
<tr>
	<td>签名类型signType:</td>
	<td><%= queryResponse.getSignType() %></td>
</tr>
<tr>
	<td>人民币账号merchantAcctId: </td>
	<td><%= queryResponse.getMerchantAcctId() %></td>
</tr>
<tr>
	<td>错误代码errCode: </td>
	<td><%= queryResponse.getErrCode() %></td>
</tr>
<tr>
	<td>签名字符串signMsg: </td>
	<td><%= queryResponse.getSignMsg() %></td>
</tr>
<tr>
	<td>自己签名字符串mysignMsg: </td>
	<td><%= mysignMsg %></td>
</tr></table>
<br/>
<br/>

<%
if (queryResponse.getErrCode() != null && !"".equals(queryResponse.getErrCode())) {
%>
<% } else {
BankDebitResult[] orders = queryResponse.getBankDebitResults();
%>
<table border="2">
<tr>
	<td></td>
	<td>商户订单号</td>
	<td>商户订单金额</td>
	<td>商户订单提交时间</td>
	<td>产品的名字</td>
	<td>产品的编号</td>
	<td>扩展字段1</td>
	<td>扩展字段2</td>
	<td>快钱交易号</td>
	<td>银行交易号</td>
	<td>交易时间</td>
	<td>处理结果</td>
	<td>费用</td>
	<td>快易付协议号</td>
	<td>订单签名字符串</td>
</tr>
<%
for (int i = 0; i < orders.length; i++) {
	BankDebitResult detail = orders[i];
	signMsgVal="";
	signMsgVal = AppendParam(signMsgVal,"orderId",detail.getOrderId());
	signMsgVal = AppendParam(signMsgVal,"orderAmount",""+detail.getOrderAmount());
	signMsgVal = AppendParam(signMsgVal,"orderTime",detail.getOrderTime());
	signMsgVal = AppendParam(signMsgVal,"productName",detail.getProductName());
	signMsgVal = AppendParam(signMsgVal,"productId",detail.getProductId());
	signMsgVal = AppendParam(signMsgVal,"ext1",""+detail.getExt1());
	signMsgVal = AppendParam(signMsgVal,"ext2",""+detail.getExt2());
	signMsgVal = AppendParam(signMsgVal,"dealId",detail.getDealId());
	signMsgVal = AppendParam(signMsgVal,"bankDealId",detail.getBankDealId());
	signMsgVal = AppendParam(signMsgVal,"dealTime",detail.getDealTime());
	signMsgVal = AppendParam(signMsgVal,"dealResult",detail.getDealResult());
	signMsgVal = AppendParam(signMsgVal,"fee",""+detail.getFee());
	signMsgVal = AppendParam(signMsgVal,"debitProtocolId",detail.getDebitProtocolId());
	signMsgVal = AppendParam(signMsgVal,"key",key);
	
	mysignMsg = MD5Util.md5Hex(signMsgVal.getBytes()).toUpperCase();

%>
<tr>
	<td><%= i+1 %></td>
	<td><%=detail.getOrderId()%></td>
	<td><%=detail.getOrderAmount()%></td>
	<td><%=detail.getOrderTime()%></td>
	<td><%=detail.getProductName()%></td>
	<td><%=detail.getProductId()%></td>
	<td><%=detail.getExt1()%></td>
	<td><%=detail.getExt2()%></td>
	<td><%=detail.getDealId()%></td>
	<td><%=detail.getBankDealId()%></td>
	<td><%=detail.getDealTime()%></td>
	<td><%=detail.getDealResult()%></td>
	<td><%=detail.getFee()%></td>
	<td><%=detail.getDebitProtocolId()%></td>
	<td><%=detail.getSignInfo()%><br/><%=mysignMsg%></td>
</tr>
<% } } %>
</table>


</body>
</html>