<%@ page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    /**
     * @Description: ��Ǯ���׸�ҵ����Ȩ�ӿڷ���
     * @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
     * @version 2.0
     */
    request.setCharacterEncoding("GBK");
//����������˻���
///���¼��Ǯϵͳ��ȡ�û���ţ��û���ź��01��Ϊ����������˻��š�
//String merchantAcctId="1001710038201";
    String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

//�����������Կ
///���ִ�Сд.�����Ǯ��ϵ��ȡ
//String key="AL5JEXGKZREDJHM3";
    String key = PropertyManager.getProperty("KQ_key");

//�ַ���.�̶�ѡ��ֵ����Ϊ�ա�
///ֻ��ѡ��1��2��3.
///1����UTF-8; 2����GBK; 3����gb2312
///Ĭ��ֵΪ1
    String inputCharset = "2";

//����֧�������ҳ���ַ.��[bgUrl]����ͬʱΪ�ա������Ǿ��Ե�ַ��
    String pageUrl = "";

//����������֧������ĺ�̨��ַ.��[pageUrl]����ͬʱΪ�ա������Ǿ��Ե�ַ��
///��Ǯͨ�����������ӵķ�ʽ�����׽�����͵�[bgUrl]��Ӧ��ҳ���ַ��
//    String bgUrl = PropertyManager.getProperty("KQ_bgUrl");
    String bgUrl = "http://192.168.91.66/99bill/receive.jsp";

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

//֧��������
///��Ϊ���Ļ�Ӣ���ַ�
//    String payerName = request.getParameter("NAME");
    String payerName = "NAME";

//֧������ϵ��ʽ����.�̶�ѡ��ֵ
///ֻ��ѡ��1, 2
///1����Email,2����mobil
    String payerContactType = "1";

//֧������ϵ��ʽ
///��payerContactTypeΪ��ʱ�����ֶβ���
///��payerContactType��Ϊ��ʱ�����ֶα���
///ֻ��ѡ��Email���ֻ���
//    String payerContact = request.getParameter("email");
    String payerContact = "zhanrui@yahoo.cn";

//������
///����ĸ�����֡���[-][_]���
//    String requestId = request.getParameter("out_sign_no");
    String requestId = "out_sign_no";
//    String requestId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

//�����ύʱ��
///14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磻20080101010101

    SimpleDateFormat  sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    TimeZone tz = TimeZone.getTimeZone("GMT+8");
    sdf.setTimeZone(tz);
    String requestTime = sdf.format(new java.util.Date());
//    String requestTime = "20090602054135";

//��Ʒ����
///��Ϊ���Ļ�Ӣ���ַ�
    String productName = "COMMNAME";

//��Ʒ����
///��Ϊ�ַ���������
    String productId = "";

//��Ʒ����
    String productDesc = "";

//һ���Կۿ��ʶ
///��Ϊ��
///0�������οۿ�    1������һ���Կۿ�
    String onceOrNot = "0";

//����ۿ���
///��Ϊ��
///���ͣ��Է�Ϊ��λ����1000����10Ԫ
///��ֵΪ0ʱ����ʾ�����Ƶ��οۿ�������οۿ���ô��ڿۿ��ܶ
///��ֵ��Ϊ0ʱ����ʾ�ۿ�ʱ�������뱾�ֶν��һ�£�����ۿ��ʧ�ܡ�
    String debitAmount = "0";

//�ۿ��ܶ�
///��Ϊ�գ���������
    String totalAmount = "";

//�����ڿۿ��ܶ�
///��������
///�������ۼƿɿۿ�������Է�Ϊ��λ����1000����10Ԫ��
    String cycleAmount = "10000000";

//���ڿۿ�����
///���ɿ�
///0���������ڣ�1��ÿ�죻2��ÿ�ܣ�3��ÿ�£�4��ÿ����5��ÿ�ꡣ
///�ۿ�ʱ�䲻�ޣ��ۿ���������ۿ��������
    String debitCycle = "0";

//�����ڿۿ�������
///������
///��debitCycle=0ʱ�����ֶ�����Ϊ�գ�
///��debitCycle>0ʱ�� ���ֶα�ʾ�ڸ�����������ܳɹ��ۿ�Ĵ���
    String debitTimes = "1";

//��Ȩ��Ч��ʱ��
///���ɿ�
///�ۿ���Ȩ��Ч��ʱ�䡣
///�������ڵ�ǰʱ�䡣
///��ʽΪ����[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///���磺20080103000000

//    String debitStartTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
//    String debitStartTime = "20090602054135";
      String debitStartTime = sdf.format(new java.util.Date());

//��ȨʧЧ��ʱ��
///���ɿ�
///�ۿ���ȨʧЧ��ʱ�䡣
///�������ڿۿ���Ȩ��Чʱ�䡣
///��ʽΪ����[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///���磺20081103000000
    String debitEndTime = "29990508160154";


//��Ȩ�������
///���ɿ�
///�Ƿ������û���;ȡ����Ȩ
///Y��ʾ����
///N��ʾ������
    String debitCancelAllowed = "Y";


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
    signMsgVal = appendParam(signMsgVal, "payerName", payerName);
    signMsgVal = appendParam(signMsgVal, "payerContactType", payerContactType);
    signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
    signMsgVal = appendParam(signMsgVal, "merchantAcctId", merchantAcctId);
    signMsgVal = appendParam(signMsgVal, "requestId", requestId);
    signMsgVal = appendParam(signMsgVal, "requestTime", requestTime);
    signMsgVal = appendParam(signMsgVal, "productName", productName);
    signMsgVal = appendParam(signMsgVal, "productId", productId);
    signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
    signMsgVal = appendParam(signMsgVal, "onceOrNot", onceOrNot);
    signMsgVal = appendParam(signMsgVal, "debitAmount", debitAmount);
    signMsgVal = appendParam(signMsgVal, "totalAmount", totalAmount);
    signMsgVal = appendParam(signMsgVal, "cycleAmount", cycleAmount);
    signMsgVal = appendParam(signMsgVal, "debitCycle", debitCycle);
    signMsgVal = appendParam(signMsgVal, "debitTimes", debitTimes);
    signMsgVal = appendParam(signMsgVal, "debitStartTime", debitStartTime);
    signMsgVal = appendParam(signMsgVal, "debitEndTime", debitEndTime);
    signMsgVal = appendParam(signMsgVal, "debitCancelAllowed", debitCancelAllowed);
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
    <title>ʹ�ÿ�Ǯ֧��</title>
    <meta http-equiv="content-type" content="text/html; charset=gb2312">
</head>

<body bgcolor="#FFFFCC">
<div id="info" style="color:#CC3300; font-size:14px; font-weight:bold; font-style:normal; display:none;left:15%;top:20%;position:absolute;">���ڽ����Ǯ��վ�����Ժ�...</div>
<div align="center" id="kq1">
    <table width="380" border="0" cellpadding="1" cellspacing="1" bgcolor="#CCCCCC">
        <tr bgcolor="#FFFFFF">
            <td colspan="2"><img src="/99bill/images/99bill_logo.gif" border="0"/>
                </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td width="80">������:</td>
            <td><%=requestId %>
            </td>
        </tr>
        <%--<tr bgcolor="#FFFFFF">--%>
            <%--<td>����ۿ���:</td>--%>
            <%--<td><%=debitAmount %>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr bgcolor="#FFFFFF">
            <td>֧����:</td>
            <td><%=payerName %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>��Ʒ����:</td>
            <td><%=productName %></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
    </table>


<%--<div align="center" style="font-size:12px;font-weight: bold;color:red;">--%>
    <form name="kqPay" action="http://www.99bill.com/bankdebit/serviceBinding.htm" method="post" target="_self">
        <div align="center" id="kq2" style="display:none;">
        <input type="text" name="inputCharset" value="<%=inputCharset %>"/>�ַ���
        <input type="text" name="bgUrl" value="<%=bgUrl %>"/>��̨���յ�ַ
        <input type="text" name="pageUrl" value="<%=pageUrl %>"/>ҳ����յ�ַ<br>
        <input type="text" name="version" value="<%=version %>"/>�ӿڰ汾
        <input type="text" name="language" value="<%=language %>"/>��������
        <input type="text" name="signType" value="<%=signType %>"/>ǩ������<br>
        <input type="text" name="signMsg" value="<%=signMsg %>"/>ǩ���ַ���
        <input type="text" name="merchantAcctId" value="<%=merchantAcctId %>"/>�˻���
        <input type="text" name="payerName" value="<%=payerName %>"/>֧��������<br>
        <input type="text" name="payerContactType" value="<%=payerContactType %>"/>֧������ϵ��ʽ����
        <input type="text" name="payerContact" value="<%=payerContact %>"/>֧������ϵ��ʽ
        <input type="text" name="requestId" value="<%=requestId %>"/>������<br>
        <input type="text" name="requestTime" value="<%=requestTime %>"/>�����ύʱ��
        <input type="text" name="productName" value="<%=productName %>"/>��Ʒ����
        <input type="text" name="productId" value="<%=productId %>"/>��Ʒ����<br>
        <input type="text" name="productDesc" value="<%=productDesc %>"/>��Ʒ����
        <input type="text" name="ext1" value="<%=ext1 %>"/>��չ�ֶ�һ
        <input type="text" name="ext2" value="<%=ext2 %>"/>��չ�ֶζ�<br>
        <input type="text" name="onceOrNot" value="<%=onceOrNot %>"/>һ���Կۿ��ʶ
        <input type="text" name="debitAmount" value="<%=debitAmount %>"/>����ۿ���
        <input type="text" name="totalAmount" value="<%=totalAmount %>"/>�ۿ��ܶ�<br>
        <input type="text" name="cycleAmount" value="<%=cycleAmount %>"/>�����ڿۿ��ܶ�
        <input type="text" name="debitCycle" value="<%=debitCycle %>"/>���ڿۿ�����
        <input type="text" name="debitTimes" value="<%=debitTimes %>"/>�����ڿۿ�������<br>
        <input type="text" name="debitStartTime" value="<%=debitStartTime %>"/>��Ȩ��Ч��ʱ��
        <input type="text" name="debitEndTime" value="<%=debitEndTime %>"/>�ۿ����ʱ��
        <input type="text" name="debitCancelAllowed" value="<%=debitCancelAllowed %>"/>��Ȩ�������<br>
        </div>
        <input type="submit" name="button" value="ȥ��ǮǩԼ" onclick="qy()">

    </form>
</div>
<script type="text/javascript">
        document.body.onload = function() {
            //document.forms[0].submit();
            //window.close();
        }

        <!--
        function qy() {
            document.getElementById("info").style.display="block";
            document.getElementById("kq1").style.display="none";
            self.moveTo(0, 0);
            self.resizeTo(screen.availWidth, screen.availHeight);
            self.outerWidth = screen.availWidth;
            self.outerHeight = screen.availHeight;
        }
        //-->
    </script>
</BODY>
</HTML>