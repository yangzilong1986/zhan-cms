<%
    /*
        建行网银签约
        域名	名称	类型	备注
        MERCHANTID	商户代码	CHAR(15)	由建行统一分配
        POSID	商户柜台代码	CHAR(9)	由建行统一分配，
        BRANCHID	分行代码	CHAR(9)	由建行统一指定
        AUTHID	授权号	CHAR(30)	由商户提供
        
        PAYMENT	授权金额	NUMBER(16,2)	由商户提供，按实际金额给出
        CURCODE	币种	CHAR(2)	缺省为01－人民币
        （只支持人民币支付）
        TXCODE	交易码	CHAR(6)	由建行统一分配为520105
        REM1	备注1	CHAR(30)	网银不处理，直接传到城综网
        REM2	备注2	CHAR(30)	网银不处理，直接传到城综网
        PUB32	公钥前30位	CHAR(30)	只在做MD5加密时使用，发送时不用。
        MAC	MAC校验域	CHAR(32)	采用标准MD5算法，由商户实现
      */

%>
<%@ page language="java" import="com.alipay.config.AlipayConfig" pageEncoding="GBK" %>
<%@page import="com.ccb.Payment" %>
<%@page import="com.zt.util.PropertyManager" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

    <title>代扣--签约</title>
    <meta http-equiv="pragma" content="no-cache">
    <%
        Log logger = LogFactory.getLog("ccb_qianyue.jsp");

        String paygeteway = "https://ibsbjstar.ccb.com.cn/app/ccbMain?";

        String merchantid = "105370257320044";
        String posid = "100000192";
        String branchid = "371000000";
        String authid =  request.getParameter("out_sign_no");    //客户签约号

        //TODO:
        String payment = "1000.00";
        String curcode = "01";
        String txcode = "520105";
        String rem1 = "test";
        String rem2 = "test";
        String pub32 = "30819c300d06092a864886f70d0101";  //只在做MD5加密时使用，发送时不用。
        String mac = "";

//        String service = "customer_sign"; //服务名称--签约(不用改)
        String input_charset = AlipayConfig.CharSet;   //页面编码（不用改）
        //String type_code = "XSCS100011000101"; //type_code合作伙伴ID(必填),这个由销售申请，后台开通，可以从销售拿到，例如：XSCS100011000101
        //String partner = AlipayConfig.partnerID;  //支付宝安全校验码(账户内提取)
        //String key = AlipayConfig.key; //key账户对应的支付宝安全校验码(必填)

//        String type_code = PropertyManager.getProperty("ZFB_type_code");//type_code合作伙伴ID(必填)
//        String partner = PropertyManager.getProperty("ZFB_partnerID");  //支付宝安全校验码(账户内提取)
//        String key = PropertyManager.getProperty("ZFB_key");  //key账户对应的支付宝安全校验码(必填)
        //以上是账户信息。请改写成网站自己的

        String sign_type = "MD5"; //签名方式(不用改)

        String path = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort();
//			String notify_url = path +"/alipay_daikou/alipay_notify.jsp"; //"http://10.2.5.51/liuzhuo/daikou/qianyue/alipay/Return_Alipay_Notify.asp"
//        String notify_url = path + PropertyManager.getProperty("ZFB_notify_url");
        String notify_url =  PropertyManager.getProperty("ZFB_notify_url");
        //服务器通知url（Alipay_Notify.asp文件所在路经）
//			String return_url = path +"/alipay_daikou/alipay_return.jsp";
//        String return_url = path + PropertyManager.getProperty("ZFB_return_url");
        String return_url =  PropertyManager.getProperty("ZFB_return_url");
        //服务器通知url（return_Alipay_Notify.asp文件所在路经）
        //相关参数名称具体含义，可以在支付宝接口服务文档中查询到，
        //以上两个文件，通知正常都可以在notify data目录找到通知过来的日志

        String customer_email = request.getParameter("email").trim(); //签约客户注册支付宝的Email
        String out_sign_no = request.getParameter("out_sign_no").trim(); //外部签约号,一个合作伙伴中唯一
        String is_account_read_only = "true"; //是否签约的帐号可以被修改，若为true且后台开通只读权限则为只读。

        Map params = new LinkedHashMap();
        params.put("MERCHANTID", merchantid);
        params.put("POSID", posid);
        params.put("BRANCHID", branchid);
        params.put("AUTHID", authid);
        
        params.put("PAYMENT", payment);
        params.put("CURCODE", curcode);
        params.put("TXCODE", txcode);
        params.put("REMARK1", rem1);
        params.put("REMARK2", rem2);

        params.put("PUB32", pub32);

        String itemUrl = Payment.CreateUrlQianyue(pub32, input_charset, params);
        itemUrl = "https://ibsbjstar.ccb.com.cn/app/ccbMain?" +   itemUrl;
        logger.info("CCB签约发送的URL="+itemUrl);
        response.sendRedirect(itemUrl);
    %>
</head>
</html>
