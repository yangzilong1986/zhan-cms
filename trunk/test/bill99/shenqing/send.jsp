<%@ page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%
    /**
     * @Description: 快钱快易付业务授权接口范例
     * @Copyright (c) 上海快钱信息服务有限公司
     * @version 2.0
     */

//人民币网关账户号
///请登录快钱系统获取用户编号，用户编号后加01即为人民币网关账户号。

    //String merchantAcctId = "1001147971701";
    //String merchantAcctId = "1000300079901";
    String merchantAcctId = "1001710038201";

//人民币网关密钥
///区分大小写.请与快钱联系索取
    //String key = "1234567891234567";
    //String key = "RD5XY5MT45Q6RY6D";
    String key = "AL5JEXGKZREDJHM3";

//字符集.固定选择值。可为空。
///只能选择1、2、3.
///1代表UTF-8; 2代表GBK; 3代表gb2312
///默认值为1
    String inputCharset = "3";

//接受支付结果的页面地址.与[bgUrl]不能同时为空。必须是绝对地址。
    String pageUrl = "http://192.168.91.66/99bill/receive.jsp";

//服务器接受支付结果的后台地址.与[pageUrl]不能同时为空。必须是绝对地址。
///快钱通过服务器连接的方式将交易结果发送到[bgUrl]对应的页面地址。
    String bgUrl = "http://192.168.91.66/99bill/receive.jsp";

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

//支付人姓名
///可为中文或英文字符
//    String payerName = "payerName";
    String payerName = "财务中心";

//支付人联系方式类型.固定选择值
///只能选择1, 2
///1代表Email,2代表mobil
    String payerContactType = "1";

//支付人联系方式
///当payerContactType为空时，本字段不填
///当payerContactType不为空时，本字段必填
///只能选择Email或手机号
    //String payerContact = "zhanrui@gmail.com";
    String payerContact = "kqkqklqcom06@126.com";

//申请编号
///由字母、数字、或[-][_]组成
    String requestId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

//申请提交时间
///14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///如；20080101010101
    String requestTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    //String requestTime = "20090501121212";

//商品名称
///可为中文或英文字符
    String productName = "冰箱";

//商品代码
///可为字符或者数字
    String productId = "com001";

//商品描述
    String productDesc = "com";

//一次性扣款标识
///不为空
///0：代表多次扣款    1：代表一次性扣款
    String onceOrNot = "0";

//定额扣款金额
///不为空
///整型，以分为单位，如1000代表10元
///当值为0时，表示不限制单次扣款金额，但单次扣款金额不得大于扣款总额；
///当值不为0时，表示扣款时金额必须与本字段金额一致，否则扣款会失败。
    String debitAmount = "2";

//扣款总额
///可为空，正整数。
    String totalAmount = "100000";

//周期内扣款总额
///正整数。
///周期内累计可扣款的最大金额。以分为单位，如1000代表10元。
    String cycleAmount = "6";

//定期扣款周期
///不可空
///0：不定周期；1：每天；2：每周；3：每月；4：每季；5：每年。
///扣款时间不限，扣款次数有最多扣款次数限制
    String debitCycle = "0";

//周期内扣款最大次数
///正整数
///当debitCycle=0时，本字段设置为空；
///当debitCycle>0时， 本字段表示在该周期内最多能成功扣款的次数
    String debitTimes = "";

//授权生效的时间
///不可空
///扣款授权生效的时间。
///不得早于当前时间。
///格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///例如：20080103000000
     String debitStartTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
//授权失效的时间
///不可空
///扣款授权失效的时间。
///不得早于扣款授权生效时间。
///格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///例如：20081103000000
    String debitEndTime = "20091212000000";


//授权撤销许可
///不可空
///是否允许用户中途取消授权
///Y表示允许
///N表示不允许
    String debitCancelAllowed = "Y";


//扩展字段1
///在支付结束后原样返回给商户
    String ext1 = "aaa";

//扩展字段2
///在支付结束后原样返回给商户
    String ext2 = "bbb";

    //生成加密签名串
    ///请务必按照如下顺序和规则组成加密串！
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
    <title>使用快钱支付</title>
    <meta http-equiv="content-type" content="text/html; charset=gb2312">
</head>

<BODY>

<div align="center">
    <table width="259" border="0" cellpadding="1" cellspacing="1" bgcolor="#CCCCCC">
        <tr bgcolor="#FFFFFF">
            <td width="80">支付方式:</td>
            <td>快钱[99bill]</td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>申请编号:</td>
            <td><%=requestId %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>定额扣款金额:</td>
            <td><%=debitAmount %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>支付人:</td>
            <td><%=payerName %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>商品名称:</td>
            <td><%=productName %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>开始时间:</td>
            <td><%=debitStartTime %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>结束时间:</td>
            <td><%=debitEndTime %>
            </td>
        </tr>
        <tr bgcolor="#FFFFFF">
            <td>申请时间:</td>
            <td><%=requestTime %>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
    </table>
    <%=signMsg %>
    <a>-----------</a>
    <%=signMsgVal %>

</div>

<div align="center" style="font-size:12px;font-weight: bold;color:red;">
    <form name="kqPay" action="http://www.99bill.com/bankdebit/serviceBinding.htm" method="post">
        <input type="text" name="inputCharset" value="<%=inputCharset %>"/>
        <input type="text" name="bgUrl" value="<%=bgUrl %>"/>
        <input type="text" name="pageUrl" value="<%=pageUrl %>"/>
        <input type="text" name="version" value="<%=version %>"/>
        <input type="text" name="language" value="<%=language %>"/>
        <input type="text" name="signType" value="<%=signType %>"/>
        <input type="text" name="signMsg" value="<%=signMsg %>"/>
        <input type="text" name="merchantAcctId" value="<%=merchantAcctId %>"/>
        <input type="text" name="payerName" value="<%=payerName %>"/>
        <input type="text" name="payerContactType" value="<%=payerContactType %>"/>
        <input type="text" name="payerContact" value="<%=payerContact %>"/>
        <input type="text" name="requestId" value="<%=requestId %>"/>
        <input type="text" name="requestTime" value="<%=requestTime %>"/>
        <input type="text" name="productName" value="<%=productName %>"/>
        <input type="text" name="productId" value="<%=productId %>"/>
        <input type="text" name="productDesc" value="<%=productDesc %>"/>
        <input type="text" name="ext1" value="<%=ext1 %>"/>
        <input type="text" name="ext2" value="<%=ext2 %>"/>
        <input type="text" name="onceOrNot" value="<%=onceOrNot %>"/>
        <input type="text" name="debitAmount" value="<%=debitAmount %>"/>
        <input type="text" name="totalAmount" value="<%=totalAmount %>"/>
        <input type="text" name="cycleAmount" value="<%=cycleAmount %>"/>
        <input type="text" name="debitCycle" value="<%=debitCycle %>"/>
        <input type="text" name="debitTimes" value="<%=debitTimes %>"/>
        <input type="text" name="debitStartTime" value="<%=debitStartTime %>"/>
        <input type="text" name="debitEndTime" value="<%=debitEndTime %>"/>
        <input type="text" name="debitCancelAllowed" value="<%=debitCancelAllowed %>"/>
        <input type="submit" name="submit" value="提交到快钱">

    </form>
</div>

</BODY>
</HTML>