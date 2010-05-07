<%@ page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%
    /**
     * @Description: ��Ǯ�̻��ۿ�ӿڷ���
     * @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
     * @version 2.0
     */

//    request.setCharacterEncoding("GBK");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    TimeZone tz = TimeZone.getTimeZone("GMT+8");
    sdf.setTimeZone(tz);

//����������˻���
///���¼��Ǯϵͳ��ȡ�û���ţ��û���ź��01��Ϊ����������˻��š�
//    String merchantAcctId = "1001147971701";
    String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

//�����������Կ
///���ִ�Сд.�����Ǯ��ϵ��ȡ
//    String key = "1234567891234567";
    String key = PropertyManager.getProperty("KQ_key");

//���׸�Э����
//�������������׸�����󣬵õ���
///�ַ���.ֻ֧����ĸ�����֣�_��-���������ֻ���ĸ��ͷ��
//    String debitProtocolId = "123456";
//    String debitProtocolId = "1126531";                 //bill99
    String debitProtocolId = "1126637";                 //alippy


//�ַ���.�̶�ѡ��ֵ����Ϊ�ա�
///ֻ��ѡ��1��2��3.
///1����UTF-8; 2����GBK; 3����gb2312
///Ĭ��ֵΪ1
    String inputCharset = "3";

//���ܿۿ�����ҳ���ַ
///�������ҳ�淵�أ���Ǯ����GET��ʽ�ύ���̻���ַ��
///�����bgUrl���أ���ôҳ��ת����bgUrl��ת���ַΪ׼���緵��û��ת���ַ����ת���Դ˵�ַΪ׼��
///bgUrl��pageUrl���߲���ͬʱΪ��
//    String pageUrl = "";
    String pageUrl = PropertyManager.getProperty("KQ_KK_pageUrl");

//���������տۿ����ĺ�̨��ַ
///��Ǯ��ͨ�����������ӵķ�ʽ�����׽���������ݸ��̻��ṩ�����url���̻������������ܽ���ͷ���ҳ���ַ��
///�����Ǯ���ܲ����̻��ķ��أ���ֱ��Redirect��pageUrl��ȥ��ͬʱ����֧���������
//    String bgUrl = "http://localhost/test/bill99/koukuan/receive.jsp";
    String bgUrl = PropertyManager.getProperty("KQ_KK_bgUrl");
//    String bgUrl = "http://192.168.91.66/99bill/receive.jsp";

//���ذ汾.�̶�ֵ
///��Ǯ����ݰ汾�������ö�Ӧ�Ľӿڴ������
///������汾�Ź̶�Ϊv2.0
    String version = "v2.0";

//��������.�̶�ѡ��ֵ��
///ֻ��ѡ��1��2��3
///1�������ģ�2����Ӣ��
///Ĭ��ֵΪ1
    String language = "1";

//ǩ������.�̶�ֵ
///1����MD5ǩ��
///��ǰ�汾�̶�Ϊ1
    String signType = "1";

//�̻�������
///����ĸ�����֡���[-][_]���
//    String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    String orderId = "20090606004";

//�������
///�Է�Ϊ��λ����������������
///�ȷ�2������0.02Ԫ
    String orderAmount = "999";

//�����ύʱ��
///14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磻20080101010101

    String orderTime = sdf.format(new java.util.Date());
//String orderTime=new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

//��Ʒ����
///��Ϊ���Ļ�Ӣ���ַ�
    String productName = "productName0606";

//��Ʒ����
///��Ϊ�ַ���������
    String productId = "";

//��չ�ֶ�1
///��֧��������ԭ�����ظ��̻�
    String ext1 = "";

//��չ�ֶ�2
///��֧��������ԭ�����ظ��̻�
    String ext2 = "";


    //���ɼ���ǩ����
    ///����ذ�������˳��͹�����ɼ��ܴ���
    String signMsgVal = "";

    signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
    signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
    signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
    signMsgVal = appendParam(signMsgVal, "version", version);
    signMsgVal = appendParam(signMsgVal, "language", language);
    signMsgVal = appendParam(signMsgVal, "signType", signType);
    signMsgVal = appendParam(signMsgVal, "merchantAcctId", merchantAcctId);
    signMsgVal = appendParam(signMsgVal, "debitProtocolId", debitProtocolId);
    signMsgVal = appendParam(signMsgVal, "orderId", orderId);
    signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
    signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
    signMsgVal = appendParam(signMsgVal, "productName", productName);
    signMsgVal = appendParam(signMsgVal, "productId", productId);
    signMsgVal = appendParam(signMsgVal, "ext1", ext1);
    signMsgVal = appendParam(signMsgVal, "ext2", ext2);
    signMsgVal = appendParam(signMsgVal, "key", key);

    String signMsg = MD5Util.md5Hex(signMsgVal.getBytes("gb2312")).toUpperCase();

%>
<%!
    //���ܺ�����������ֵ��Ϊ�յĲ�������ַ���
    public String appendParam(String returnStr, String paramId, String paramValue) {
        if (!returnStr.equals("")) {
            if (!paramValue.equals("")) {
                returnStr = returnStr + "&" + paramId + "=" + paramValue;
            }
        } else {
            if (!paramValue.equals("")) {
                returnStr = paramId + "=" + paramValue;
            }
        }
        return returnStr;
    }
    //���ܺ�����������ֵ��Ϊ�յĲ�������ַ���������

%>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en" >
<html>
<head>
    <title>ʹ�ÿ�Ǯ���׸��ۿ�</title>
    <meta http-equiv="content-type" content="text/html; charset=gb2312">
</head>

<BODY>

<div align="center">
    <table width="259" border="0" cellpadding="1" cellspacing="1" bgcolor="#CCCCCC">
        <tr bgcolor="#FFFFFF">
            <td width="80">�ۿʽ:</td>
            <td>��Ǯ[99bill]</td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>�������:</td>
            <td><%=orderId %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>�������:</td>
            <td><%=orderAmount %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>��Ʒ����:</td>
            <td><%=productName %>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
    </table>
</div>

<div align="center" style="font-size:12px;font-weight: bold;color:red;">
    <form name="kqPay" action="http://www.99bill.com/bankdebit/serviceDeduction.htm" method="post">
        <input type="hidden" name="inputCharset" value="<%=inputCharset %>"/>
        <input type="hidden" name="bgUrl" value="<%=bgUrl %>"/>
        <input type="hidden" name="pageUrl" value="<%=pageUrl %>"/>
        <input type="hidden" name="version" value="<%=version %>"/>
        <input type="hidden" name="language" value="<%=language %>"/>
        <input type="hidden" name="signType" value="<%=signType %>"/>
        <input type="hidden" name="signMsg" value="<%=signMsg %>"/>
        <input type="hidden" name="merchantAcctId" value="<%=merchantAcctId %>"/>
        <input type="hidden" name="debitProtocolId" value="<%=debitProtocolId %>"/>
        <input type="hidden" name="orderId" value="<%=orderId %>"/>
        <input type="hidden" name="orderAmount" value="<%=orderAmount %>"/>
        <input type="hidden" name="orderTime" value="<%=orderTime %>"/>
        <input type="hidden" name="productName" value="<%=productName %>"/>
        <input type="hidden" name="productId" value="<%=productId %>"/>
        <input type="hidden" name="ext1" value="<%=ext1 %>"/>
        <input type="hidden" name="ext2" value="<%=ext2 %>"/>
        <input type="submit" name="submit" value="�ύ����Ǯ">
    </form>
</div>

</BODY>
</HTML>