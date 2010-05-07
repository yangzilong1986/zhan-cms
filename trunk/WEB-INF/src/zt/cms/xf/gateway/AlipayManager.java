package zt.cms.xf.gateway;

import com.alipay.config.AlipayConfig;
import com.alipay.util.Payment;
import com.zt.util.PropertyManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-5-10
 * Time: 22:33:34
 * To change this template use File | Settings | File Templates.
 */
public class AlipayManager {

    private static String partner = PropertyManager.getProperty("ZFB_partnerID");
    private static String key = PropertyManager.getProperty("ZFB_key");
    private static String type_code = PropertyManager.getProperty("ZFB_type_code");
    private static String return_url = PropertyManager.getProperty("ZFB_KK_return_url");
    private static String notify_url = PropertyManager.getProperty("ZFB_KK_notify_url");

    public String getAlipayUrl(String journalno, String amount, String customer_code) {
        String itemUrl = "";
        String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //'支付接口（不用改）
//        String paygateway = "http://www.alipay.com/cooperate/gateway.do?"; //'支付接口（不用改）
        String t4 = "images/alipay_bwrx.gif"; //'支付宝按钮图片
        String t5 = "使用支付宝付款"; //'按钮悬停说明
        String input_charset = AlipayConfig.CharSet; //页面编码（不用改）
        String service = "cae_charge_agent";//服务名称---代扣（不用改）

//        String partner = AlipayConfig.partnerID; //partner合作伙伴ID(必填)
//        String key = AlipayConfig.key; //partner账户对应的支付宝安全校验码(必填)
//        String type_code = "TEST100011000101"; //type_code合作伙伴ID(必填),这个由销售申请，后台开通，可以从销售拿到，例如：100410000192

//        String return_url = "http://218.58.70.181/test/alipay/alipay_return.jsp";
        //以上是账户信息。请改写成网站自己的
        //*****************************************************************
        String sign_type = "MD5"; //'签名方式（不用改）
        String subject = "commodityname-test"; //subject 商品名称“网站变量”

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        sFmt.setTimeZone(tz);
        String dateStr = sFmt.format(new Date());

        //String gmt_out_order_create = "2009-05-14 15:15:15"; //商户订单创建时间
        String gmt_out_order_create = dateStr; //商户订单创建时间

        String out_order_no = journalno; //商户网站订单（也就是外部订单号，是通过客户网站传给支付宝，不可以重复）
        //String amount = amount; //订单总价	0.01～50000.00

        //TODO:
//        String customer_code = "122850000035";

        //********************************************************************


			itemUrl = Payment.CreateUrl(paygateway, t4, t5, service, partner,
					sign_type, subject, gmt_out_order_create, out_order_no,
					amount, customer_code, key, type_code, input_charset);

        //增加return_url
//        itemUrl = Payment.CreateUrl_zr(paygateway, t4, t5, service, partner,
//                sign_type, subject, gmt_out_order_create, out_order_no,
//                amount, customer_code, key, type_code, input_charset, return_url);
        //response.sendRedirect(itemUrl);
        return itemUrl;

    }

}
