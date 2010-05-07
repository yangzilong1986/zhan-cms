<%@ page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%
    /**
     * @Description: 快钱快易付业务授权接口范例
     * @Copyright (c) 上海快钱信息服务有限公司
     * @version 2.0
     */
    request.setCharacterEncoding("GBK");
//人民币网关账户号
///请登录快钱系统获取用户编号，用户编号后加01即为人民币网关账户号。
//String merchantAcctId="1001710038201";
    String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

//人民币网关密钥
///区分大小写.请与快钱联系索取
//String key="AL5JEXGKZREDJHM3";
    String key = PropertyManager.getProperty("KQ_key");

//字符集.固定选择值。可为空。
///只能选择1、2、3.
///1代表UTF-8; 2代表GBK; 3代表gb2312
///默认值为1
    String inputCharset = "2";

//接受支付结果的页面地址.与[bgUrl]不能同时为空。必须是绝对地址。
    String pageUrl = "";

//服务器接受支付结果的后台地址.与[pageUrl]不能同时为空。必须是绝对地址。
///快钱通过服务器连接的方式将交易结果发送到[bgUrl]对应的页面地址。
    String bgUrl = PropertyManager.getProperty("KQ_bgUrl");

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
    String payerName = request.getParameter("NAME");

//支付人联系方式类型.固定选择值
///只能选择1, 2
///1代表Email,2代表mobil
    String payerContactType = "1";

//支付人联系方式
///当payerContactType为空时，本字段不填
///当payerContactType不为空时，本字段必填
///只能选择Email或手机号
    String payerContact = request.getParameter("email");

//申请编号
///由字母、数字、或[-][_]组成
    String requestId = request.getParameter("out_sign_no");
//    String requestId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

//申请提交时间
///14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///如；20080101010101
    String requestTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

//商品名称
///可为中文或英文字符
    String productName = request.getParameter("COMMNAME").trim();

//商品代码
///可为字符或者数字
    String productId = "";

//商品描述
    String productDesc = "";

//一次性扣款标识
///不为空
///0：代表多次扣款    1：代表一次性扣款
    String onceOrNot = "0";

//定额扣款金额
///不为空
///整型，以分为单位，如1000代表10元
///当值为0时，表示不限制单次扣款金额，但单次扣款金额不得大于扣款总额；
///当值不为0时，表示扣款时金额必须与本字段金额一致，否则扣款会失败。
    String debitAmount = "0";

//扣款总额
///可为空，正整数。
    String totalAmount = "";

//周期内扣款总额
///正整数。
///周期内累计可扣款的最大金额。以分为单位，如1000代表10元。
    String cycleAmount = "1000000000";

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
    String debitEndTime = "29990508160154";


//授权撤销许可
///不可空
///是否允许用户中途取消授权
///Y表示允许
///N表示不允许
    String debitCancelAllowed = "Y";


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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>快钱-快易付</title>
<link href="wrap.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div id="info" style="color:#CC3300; font-size:14px; font-weight:bold; font-style:normal; display:none;left:15%;top:20%;position:absolute;">正在进入快钱网站，请稍候...</div>
<form name="kqPay" action="http://www.99bill.com/bankdebit/serviceBinding.htm" method="post" target="_self">
<div align="center" id="kq1" style="display:none;">
        <input type="text" name="inputCharset" value="<%=inputCharset %>"/>字符集
        <input type="text" name="bgUrl" value="<%=bgUrl %>"/>后台接收地址
        <input type="text" name="pageUrl" value="<%=pageUrl %>"/>页面接收地址<br>
        <input type="text" name="version" value="<%=version %>"/>接口版本
        <input type="text" name="language" value="<%=language %>"/>语言种类
        <input type="text" name="signType" value="<%=signType %>"/>签名类型<br>
        <input type="text" name="signMsg" value="<%=signMsg %>"/>签名字符串
        <input type="text" name="merchantAcctId" value="<%=merchantAcctId %>"/>账户号
        <input type="text" name="payerName" value="<%=payerName %>"/>支付人姓名<br>
        <input type="text" name="payerContactType" value="<%=payerContactType %>"/>支付人联系方式类型
        <input type="text" name="payerContact" value="<%=payerContact %>"/>支付人联系方式
        <input type="text" name="requestId" value="<%=requestId %>"/>申请编号<br>
        <input type="text" name="requestTime" value="<%=requestTime %>"/>申请提交时间
        <input type="text" name="productName" value="<%=productName %>"/>产品名称
        <input type="text" name="productId" value="<%=productId %>"/>产品代码<br>
        <input type="text" name="productDesc" value="<%=productDesc %>"/>商品描述
        <input type="text" name="ext1" value="<%=ext1 %>"/>扩展字段一
        <input type="text" name="ext2" value="<%=ext2 %>"/>扩展字段二<br>
        <input type="text" name="onceOrNot" value="<%=onceOrNot %>"/>一次性扣款标识
        <input type="text" name="debitAmount" value="<%=debitAmount %>"/>定额扣款金额
        <input type="text" name="totalAmount" value="<%=totalAmount %>"/>扣款总额<br>
        <input type="text" name="cycleAmount" value="<%=cycleAmount %>"/>周期内扣款总额
        <input type="text" name="debitCycle" value="<%=debitCycle %>"/>定期扣款周期
        <input type="text" name="debitTimes" value="<%=debitTimes %>"/>周期内扣款最大次数<br>
        <input type="text" name="debitStartTime" value="<%=debitStartTime %>"/>授权生效的时间
        <input type="text" name="debitEndTime" value="<%=debitEndTime %>"/>扣款结束时间
        <input type="text" name="debitCancelAllowed" value="<%=debitCancelAllowed %>"/>授权撤销许可<br>
</div>
</form>
<div align="center" id="kq2">
<!--网站头部开始-->
<div class="page_top div_cer"></div>
<!--网站头部结束-->
<!--网站内容开始-->
<div class="page_center  div_cer">
<div class="pcBody">
	<table width="94%" height="132" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td width="69%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="font_one corer_one"><h1>什么是快易付？</h1></td>
      </tr>
      <tr>
        <td class="font_spacing">快易付是快钱特别为交易频繁或周期性收费的商家和消费者或<br />
          客户提供的银行账户代扣产品，解决商家和消费者或客户之间<br />
          频繁或定期交易所产生的资金往来的一项服务。 通过快易付<br />
          消费者或客户可以在不需要任何人工进行操作的情况下，完成<br />
          相关协定的款项支付。</td>
      </tr>
    </table></td>
    <td width="31%" valign="top"><table class="font_sy" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><span class="corer_two">编号：</span><%=requestId%><br/>
            <span class="corer_two">支付人：</span><%=payerName%><br/>
            <span class="corer_two">商品名称：</span><%=productName%></td>
      </tr>
      <tr>
        <td height="62" align="left"><a href="#" onclick="qy()"><img src="images/kyf_index_10.gif" width="177" height="54" border="0" /></a></td>
      </tr>
    </table></td>
  </tr>
</table>
</div>
<div class="pcFooter"></div>
</div>
<div class="submenu div_cer">
  <ul>
  	<li class="font_one padding_one"><strong>快易付用户签约操作步奏：</strong>
  	  <br />
  	</li>
    <li><img src="images/2_12.gif" width="650" height="146" /></li>
  </ul>
</div>
<div class="help_cen div_cer">
<div class="right">
    	<ul class="font_sy">
    	  <li><a href="http://club.99bill.com/viewthread.php?tid=100162&amp;fid=95&amp;filter=type&amp;extra=page%3D1%26amp%3Bfilter%3Dtype%26amp%3Btypeid%3D127" target="_blank">为什么您在签约的过程中工行会提示您“您的电子商务功能未开通”？</a></li>
    	  <li><a href="http://club.99bill.com/viewthread.php?tid=76563&amp;fid=95&amp;filter=type&amp;extra=page%3D1%26amp%3Bfilter%3Dtype%26amp%3Btypeid%3D127" target="_blank">为什么要为每个签约银行卡单独设置支付密码？ </a></li>
            <li><a href="http://club.99bill.com/viewthread.php?tid=76569&amp;fid=95&amp;typeid=0&amp;filter=&amp;extra=page%253D1" target="_blank">如何取消和商家的快易付协议？ </a></li>   
    	</ul>
    	<div class="fudong"><a href="http://club.99bill.com/forumdisplay.php?fid=95&filter=type&typeid=127" target="_blank"></a></div>
  </div>
    <!--网站内容结束-->
    <!--网站底部开始-->
</div>
<div class="bottom_dib div_cer">
	<table class="font_sy" width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>快钱是国内领先的独立第三方支付企业，旨在为各类企业及个人提供安全、便捷和保密的综合电子支付服务。<br />
快钱欢迎您使用快钱支付，享受众多快钱精选商家在线消费优惠。</td>
  </tr>
</table>
</div>
</div>
<!--网站底部结束-->
<script type="text/javascript">
        document.body.onload = function() {
            self.moveTo(0, 0);
            self.resizeTo(screen.availWidth, screen.availHeight);
            self.outerWidth = screen.availWidth;
            self.outerHeight = screen.availHeight;
        }

        <!--
        function qy() {
            document.getElementById("info").style.display="block";
            document.getElementById("kq2").style.display="none";
            document.kqPay.submit();
        }
        //-->
</script>
</body>
</html>
