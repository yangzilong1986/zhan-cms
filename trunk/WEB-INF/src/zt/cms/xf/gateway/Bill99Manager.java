package zt.cms.xf.gateway;

//import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.platform.utils.Debug;

import java.util.TimeZone;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

import com.zt.util.PropertyManager;
import com.bill99.encrypt.MD5Util;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-5-10
 * Time: 22:34:26
 * To change this template use File | Settings | File Templates.
 */
public class Bill99Manager {
    //人民币网关账户号
    //请登录快钱系统获取用户编号，用户编号后加01即为人民币网关账户号。
    private static String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

    //人民币网关密钥
    private static String key = PropertyManager.getProperty("KQ_key");

    //接受扣款结果的页面地址
    ///如果仅是页面返回，快钱将以GET方式提交给商户地址；
    ///如果有bgUrl返回，那么页面转向以bgUrl的转向地址为准，如返回没有转向地址，则转向以此地址为准。
    ///bgUrl和pageUrl两者不能同时为空
     private static   String pageUrl = PropertyManager.getProperty("KQ_KK_pageUrl");

    //服务器接收扣款结果的后台地址
    ///快钱将通过服务器连接的方式将交易结果参数传递给商户提供的这个url，商户处理后输出接受结果和返回页面地址。
    ///如果快钱接受不到商户的返回，则直接Redirect到pageUrl上去，同时带上支付结果参数
     private static   String bgUrl = PropertyManager.getProperty("KQ_KK_bgUrl");



    /*
    快钱：根据扣款记录生成POST内容
     */
    public List getPostValues(Xfactcutpaydetl xfcutpaydetl) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        sdf.setTimeZone(tz);

        //快易付协议编号
        //这个是在提出快易付申请后，得到的
        ///字符串.只支持字母，数字，_。-，并以数字或字母开头。
//        String debitProtocolId = "1126637";                 //alippy
        String debitProtocolId = xfcutpaydetl.getCustomerCode();


        //字符集.固定选择值。可为空。
        ///只能选择1、2、3.
        ///1代表UTF-8; 2代表GBK; 3代表gb2312
        ///默认值为1
        String inputCharset = "3";

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
//         String orderId =  sdf.format(new java.util.Date());
        String orderId = xfcutpaydetl.getJournalno();

        //订单金额
        ///以分为单位，必须是整型数字
        ///比方2，代表0.02元
//        String orderAmount = "9999.9";
        BigDecimal amt = xfcutpaydetl.getPaybackamt().multiply(new BigDecimal(100));
        String orderAmount = String.valueOf(amt.intValue());

        //订单提交时间
        ///14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
        ///如；20080101010101
        String orderTime = sdf.format(new java.util.Date());

        //产品名称
        ///可为中文或英文字符
        String productName = "productName_haier";

        //产品代码
        ///可为字符或者数字
        String productId = "productType_haier";

        //扩展字段1
        ///在支付结束后原样返回给商户
        String ext1 = "test1";

        //扩展字段2
        ///在支付结束后原样返回给商户
        String ext2 = "text2";


        //生成加密签名串
        ///请务必按照如下顺序和规则组成加密串！
        String signMsgVal = "";
        String signMsg ="";
        
        try {
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

            signMsg = MD5Util.md5Hex(signMsgVal.getBytes("gb2312")).toUpperCase();
        } catch (Exception e) {
            Debug.debug(e);
        }

//        NameValuePair[] data = {
//                new NameValuePair("inputCharset", inputCharset),
//                new NameValuePair("bgUrl", bgUrl),
//                new NameValuePair("pageUrl",pageUrl ),
//                new NameValuePair("version", version),
//                new NameValuePair("language",language ),
//                new NameValuePair("signType", signType),
//                new NameValuePair("signMsg",signMsg ),
//                new NameValuePair("merchantAcctId",merchantAcctId ),
//                new NameValuePair("debitProtocolId",debitProtocolId ),
//                new NameValuePair("orderId",orderId ),
//                new NameValuePair("orderAmount", orderAmount),
//                new NameValuePair("orderTime", orderTime),
//                new NameValuePair("productName",productName ),
//                new NameValuePair("productId", productId),
//                new NameValuePair("ext1",ext1 ),
//                new NameValuePair("ext2", ext2)
//        };

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("inputCharset", inputCharset));
        data.add(new BasicNameValuePair("bgUrl", bgUrl));
        data.add(new BasicNameValuePair("pageUrl",pageUrl ));
        data.add(new BasicNameValuePair("version", version));
        data.add(new BasicNameValuePair("language",language ));
        data.add(new BasicNameValuePair("signType", signType));
        data.add(new BasicNameValuePair("signMsg",signMsg ));
        data.add(new BasicNameValuePair("merchantAcctId",merchantAcctId ));
        data.add(new BasicNameValuePair("debitProtocolId",debitProtocolId ));
        data.add(new BasicNameValuePair("orderId",orderId ));
        data.add(new BasicNameValuePair("orderAmount", orderAmount));
        data.add(new BasicNameValuePair("orderTime", orderTime));
        data.add(new BasicNameValuePair("productName",productName ));
        data.add(new BasicNameValuePair("productId", productId));
        data.add(new BasicNameValuePair("ext1",ext1 ));
        data.add(new BasicNameValuePair("ext2", ext2));

        return data;
    }

    /*
    * 功能函数。将变量值不为空的参数组成字符串
     */
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
}
