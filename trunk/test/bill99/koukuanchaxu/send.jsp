<%@ page contentType="text/html;charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util"%>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.zt.util.PropertyManager" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" >
</head>
<br/>

<%

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    TimeZone tz = TimeZone.getTimeZone("GMT+8");
    sdf.setTimeZone(tz);


//������˺�
//����������ָ�����տ���Ŀ�Ǯ�û���������˺�
//String merchantAcctId ="1001147971701";
String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

//�ͻ��������Ӧ����Կ�������˻������л�ȡ
//String key = "1234567891234567";
    String key = PropertyManager.getProperty("KQ_key");

//�ַ���  �̶�ֵ��1 1����UTF-8 
String inputCharset ="1";

//��ѯ�ӿڰ汾   �̶�ֵ��v2.0ע��ΪСд��ĸ
String version = "v2.0";

//ǩ������   �̶�ֵ��1  1����MD5����ǩ����ʽ
String signType ="1";

//��ѯ��ʽ   �̶�ѡ��ֵ��0��1  
//0���̻������ŵ��ʲ�ѯ�����ظö�����Ϣ��
//1�����׽���ʱ��������ѯ��ֻ���سɹ�������
String queryType ="0";

//���׿�ʼʱ��  ���ִ���һ��14λ
//��ʽΪ����[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]�����磺20071117020101

String startTime ="20090601000000" ;

//���׽���ʱ��  ���ִ���һ��14λ
//��ʽΪ����[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]�����磺20071117020101
String endTime ="20090605000000";


//�̻�������  ֻ����ʹ����ĸ�����֡�- ��_,������ĸ�����ֿ�ͷ
String orderId ="20090606002";



//����ַ����������밴�մ�˳���鴮
String signMsgVal="";
signMsgVal = AppendParam(signMsgVal,"inputCharset",inputCharset);
signMsgVal = AppendParam(signMsgVal,"version",version);
signMsgVal = AppendParam(signMsgVal,"signType",signType);
signMsgVal = AppendParam(signMsgVal,"merchantAcctId",merchantAcctId);
signMsgVal = AppendParam(signMsgVal,"queryType",queryType);
signMsgVal = AppendParam(signMsgVal,"startTime",startTime);
signMsgVal = AppendParam(signMsgVal,"endTime",endTime);
signMsgVal = AppendParam(signMsgVal,"orderId",orderId);
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
	<td>������˺�</td>
	<td><%=merchantAcctId%></td>
</tr>
<tr>
	<td>��ѯ��ʽ</td>
	<td><%=queryType%></td>
</tr>
<tr>
	<td>���׿�ʼʱ��</td>
	<td><%=startTime%></td>
</tr>
<tr>
	<td>���׽���ʱ��</td>
	<td><%=endTime%></td>
</tr>
<tr>
	<td>�̻�������</td>
	<td><%=orderId%></td>
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
	<td><input type="hidden" name="orderId" value="<%=orderId%>" /></td>
</tr>
<tr>
	<td><input type="hidden" name="signMsg" value="<%=signMsg%>" size="50" /></td>
</tr>
<tr>
	<td></td>
	<td><input type="submit" value="�ύ����Ǯ" /></td>
</tr>
</table>
</form>
</body>
</html>