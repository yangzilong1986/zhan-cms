<%@ page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%
    /**
     * @Description: 快钱商户扣款接口范例
     * @Copyright (c) 上海快钱信息服务有限公司
     * @version 2.0
     */

//    request.setCharacterEncoding("GBK");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    TimeZone tz = TimeZone.getTimeZone("GMT+8");
    sdf.setTimeZone(tz);

//人民币网关账户号
///请登录快钱系统获取用户编号，用户编号后加01即为人民币网关账户号。
//    String merchantAcctId = "1001147971701";
    String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

//人民币网关密钥
///区分大小写.请与快钱联系索取
//    String key = "1234567891234567";
    String key = PropertyManager.getProperty("KQ_key");

//快易付协议编号
//这个是在提出快易付申请后，得到的
///字符串.只支持字母，数字，_。-，并以数字或字母开头。
//    String debitProtocolId = "123456";
//    String debitProtocolId = "1126531";                 //bill99
    String debitProtocolId = "1126637";                 //alippy


//字符集.固定选择值。可为空。
///只能选择1、2、3.
///1代表UTF-8; 2代表GBK; 3代表gb2312
///默认值为1
    String inputCharset = "3";

//接受扣款结果的页面地址
///如果仅是页面返回，快钱将以GET方式提交给商户地址；
///如果有bgUrl返回，那么页面转向以bgUrl的转向地址为准，如返回没有转向地址，则转向以此地址为准。
///bgUrl和pageUrl两者不能同时为空
//    String pageUrl = "";
    String pageUrl = PropertyManager.getProperty("KQ_KK_pageUrl");

//服务器接收扣款结果的后台地址
///快钱将通过服务器连接的方式将交易结果参数传递给商户提供的这个url，商户处理后输出接受结果和返回页面地址。
///如果快钱接受不到商户的返回，则直接Redirect到pageUrl上去，同时带上支付结果参数
//    String bgUrl = "http://localhost/test/bill99/koukuan/receive.jsp";
    String bgUrl = PropertyManager.getProperty("KQ_KK_bgUrl");
//    String bgUrl = "http://192.168.91.66/99bill/receive.jsp";

//网关版本.固定值
///快钱会根据版本号来调用对应的接口处理程序。
///本代码版本号固定为v2.0
    String version = "v2.0";

//语言种类.固定选择值。
///只能选择1、2、3
///1代表中文；2代表英文
///默认值为1
    String language = "1";

//签名类型.固定值
///1代表MD5签名
///当前版本固定为1
    String signType = "1";

//商户订单号
///由字母、数字、或[-][_]组成
//    String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    String orderId = "20090606004";

//订单金额
///以分为单位，必须是整型数字
///比方2，代表0.02元
    String orderAmount = "999";

//订单提交时间
///14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///如；20080101010101

    String orderTime = sdf.format(new java.util.Date());
//String orderTime=new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

//产品名称
///可为中文或英文字符
    String productName = "productName0606";

//产品代码
///可为字符或者数字
    String productId = "";

//扩展字段1
///在支付结束后原样返回给商户
    String ext1 = "";

//扩展字段2
///在支付结束后原样返回给商户
    String ext2 = "";


    //生成加密签名串
    ///请务必按照如下顺序和规则组成加密串！
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
    //功能函数。将变量值不为空的参数组成字符串
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
    //功能函数。将变量值不为空的参数组成字符串。结束

%>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en" >
<html>
<head>
    <title>使用快钱快易付扣款</title>
    <meta http-equiv="content-type" content="text/html; charset=gb2312">
</head>

<BODY>

<div align="center">
    <table width="259" border="0" cellpadding="1" cellspacing="1" bgcolor="#CCCCCC">
        <tr bgcolor="#FFFFFF">
            <td width="80">扣款方式:</td>
            <td>快钱[99bill]</td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>订单编号:</td>
            <td><%=orderId %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>订单金额:</td>
            <td><%=orderAmount %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>产品名称:</td>
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
        <input type="submit" name="submit" value="提交到快钱">
    </form>
</div>

</BODY>
</HTML>