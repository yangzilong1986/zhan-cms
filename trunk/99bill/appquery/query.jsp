<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@page import="com._99bill.www.bankdebit.services.serviceBindingQuery.*"%>
<%@page import="com.bill99.seashell.domain.dto.bankdebitbind.*"%>
<%@ page import="com.bill99.encrypt.MD5Util" %>
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
String requestId = request.getParameter("requestId");
String key = PropertyManager.getProperty("KQ_key");
String signMsg = request.getParameter("signMsg");
out.println(inputCharset+version+signType+merchantAcctId+queryType+startTime+endTime+requestId+key);

QueryBankDebitBindRequest queryRequest = new QueryBankDebitBindRequest();
queryRequest.setInputCharset(inputCharset);
queryRequest.setVersion(version);
queryRequest.setSignType(signType);
queryRequest.setMerchantAcctId(merchantAcctId);
queryRequest.setQueryType(queryType);
queryRequest.setRequestId(requestId);
queryRequest.setStartTime(startTime);
queryRequest.setEndTime(endTime);
queryRequest.setSignMsg(signMsg);

BankDebitBindQueryServiceLocator locator = new BankDebitBindQueryServiceLocator();
QueryBankDebitBindResponse queryResponse2= locator.getserviceBindingQuery().bankDebitBindQuery(queryRequest);

String signMsgVal="";
signMsgVal = AppendParam(signMsgVal,"version",queryResponse2.getVersion() );
signMsgVal = AppendParam(signMsgVal,"signType",""+queryResponse2.getSignType());
signMsgVal = AppendParam(signMsgVal,"merchantAcctId",queryResponse2.getMerchantAcctId());
signMsgVal = AppendParam(signMsgVal,"errCode",queryResponse2.getErrCode());
signMsgVal = AppendParam(signMsgVal,"key",key);
String mysignMsg = MD5Util.md5Hex(signMsgVal.getBytes()).toUpperCase();
//    System.out.println("signMsgVal = " + signMsgVal);
//    System.out.println("mysignMsg = " + mysignMsg);
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

<br/>
<table border="2">
<tr>
	<td>���ذ汾version: </td>
	<td><%= queryResponse2.getVersion() %></td>
</tr>
<tr>
	<td>ǩ������signType:</td>
	<td><%= queryResponse2.getSignType() %></td>
</tr>
<tr>
	<td>������˺�merchantAcctId: </td>
	<td><%= queryResponse2.getMerchantAcctId() %></td>
</tr>
<tr>
	<td>�������errCode: </td>
	<td><%= queryResponse2.getErrCode() %></td>
</tr>
<tr>
	<td>ǩ���ַ���signMsg: </td>
	<td><%= queryResponse2.getSignMsg() %></td>
</tr>
<tr>
	<td>�Լ�ǩ���ַ���mysignMsg: </td>
	<td><%= mysignMsg %></td>
</tr></table>
<br/>

<%
BankDebitBindResult[] orders = queryResponse2.getBankDebitBindResults();
%>
<table border="2">
<tr>
	<td></td>
	<td>���붩����</td>
	<td>���붩���ύʱ��</td>
	<td>��Ʒ������</td>
	<td>��Ʒ�ı��</td>
	<td>��չ�ֶ�1</td>
	<td>��չ�ֶ�2</td>
	<td>��Ǯ����ʱ��</td>
	<td>������</td>
	<td>���׸�Э���</td>
	<td>����ǩ���ַ���</td>
</tr>
<%
for (int i = 0; i < orders.length; i++) {
	BankDebitBindResult detail = orders[i];
	signMsgVal="";
	signMsgVal = AppendParam(signMsgVal,"requestId",detail.getRequestId());
	signMsgVal = AppendParam(signMsgVal,"requestTime",detail.getRequestTime());
	signMsgVal = AppendParam(signMsgVal,"productName",detail.getProductName());
	signMsgVal = AppendParam(signMsgVal,"productId",detail.getProductId());
	signMsgVal = AppendParam(signMsgVal,"ext1",""+detail.getExt1());
	signMsgVal = AppendParam(signMsgVal,"ext2",""+detail.getExt2());
	signMsgVal = AppendParam(signMsgVal,"dealTime",detail.getDealTime());
	signMsgVal = AppendParam(signMsgVal,"dealResult",detail.getDealResult());
	signMsgVal = AppendParam(signMsgVal,"debitProtocolId",detail.getDebitProtocolId());
	signMsgVal = AppendParam(signMsgVal,"key",key);

	mysignMsg = MD5Util.md5Hex(signMsgVal.getBytes()).toUpperCase();

%>
<tr>
	<td><%= i+1 %></td>
	<td><%=detail.getRequestId()%></td>
	<td><%=detail.getRequestTime()%></td>
	<td><%=detail.getProductName()%></td>
	<td><%=detail.getProductId()%></td>
	<td><%=detail.getExt1()%></td>
	<td><%=detail.getExt2()%></td>
	<td><%=detail.getDealTime()%></td>
	<td><%=detail.getDealResult()%></td>
	<td><%=detail.getDebitProtocolId()%></td>
	<td><%=detail.getSignInfo()%><br/><%=mysignMsg%></td>
</tr>

<% }
%>
</table>
</body>
</html>