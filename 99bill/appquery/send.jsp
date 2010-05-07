<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<br/>

<%
    //人民币账号
//本参数用来指定接收款项的快钱用户的人民币账号
    String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

//客户编号所对应的密钥。。在账户邮箱中获取
    String key = PropertyManager.getProperty("KQ_key");

//字符集  固定值：1 1代表UTF-8
String inputCharset ="1";

//查询接口版本   固定值：v2.0注意为小写字母
String version = "v2.0";

//签名类型   固定值：1  1代表MD5加密签名方式
String signType ="1";

//查询方式   固定选择值：0、1
//0 表示按商户申请编号单笔查询，返回为该授权申请处理信息；
//1 表示按申请提交时间批量查询，只返回成功授权申请的信息
String queryType ="1";

//交易开始时间  数字串，一共14位
//格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]，例如：20071117020101

String startTime ="20090601000000" ;

//交易结束时间  数字串，一共14位
//格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]，例如：20071117020101
String endTime ="20090701000000";


//申请订单号  只允许使用字母、数字、- 、_,并以字母或数字开头
String requestId ="6";



//组合字符串。。必须按照此顺序组串
String signMsgVal="";
signMsgVal = AppendParam(signMsgVal,"inputCharset",inputCharset);
signMsgVal = AppendParam(signMsgVal,"version",version);
signMsgVal = AppendParam(signMsgVal,"signType",signType);
signMsgVal = AppendParam(signMsgVal,"merchantAcctId",merchantAcctId);
signMsgVal = AppendParam(signMsgVal,"queryType",queryType);
signMsgVal = AppendParam(signMsgVal,"startTime",startTime);
signMsgVal = AppendParam(signMsgVal,"endTime",endTime);
signMsgVal = AppendParam(signMsgVal,"requestId",requestId);
signMsgVal = AppendParam(signMsgVal,"key",key);

String signMsg = MD5Util.md5Hex(signMsgVal.getBytes()).toUpperCase();
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
<body>

<table align="center" border="1">
<tr>
	<td>人民币账号</td>
	<td><%=merchantAcctId%></td>
</tr>
<tr>
	<td>查询方式</td>
	<td><%=queryType%></td>
</tr>
<tr>
	<td>交易开始时间</td>
	<td><%=startTime%></td>
</tr>
<tr>
	<td>交易结束时间</td>
	<td><%=endTime%></td>
</tr>
<tr>
	<td>申请订单号</td>
	<td><%=requestId%></td>
</tr>
</table>
<form action="query.jsp" method="get">
<table align="center">
<tr>
	<td><input type="hidden" name="inputCharset" value="<%=inputCharset%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="version" value="<%=version%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="signType" value="<%=signType%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="merchantAcctId" value="<%=merchantAcctId%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="queryType" value="<%=queryType%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="startTime" value="<%=startTime%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="endTime" value="<%=endTime%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="requestId" value="<%=requestId%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="signMsg" value="<%=signMsg%>" size="50" /></td>
</tr>
<tr>
	<td></td>
	<td><input type="submit" value="提交到快钱" /></td>
</tr>
</table>
</form>
</body>
</html>