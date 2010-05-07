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
        String paygateway = "https://www.alipay.com/cooperate/gateway.do?"; //'֧���ӿڣ����øģ�
//        String paygateway = "http://www.alipay.com/cooperate/gateway.do?"; //'֧���ӿڣ����øģ�
        String t4 = "images/alipay_bwrx.gif"; //'֧������ťͼƬ
        String t5 = "ʹ��֧��������"; //'��ť��ͣ˵��
        String input_charset = AlipayConfig.CharSet; //ҳ����루���øģ�
        String service = "cae_charge_agent";//��������---���ۣ����øģ�

//        String partner = AlipayConfig.partnerID; //partner�������ID(����)
//        String key = AlipayConfig.key; //partner�˻���Ӧ��֧������ȫУ����(����)
//        String type_code = "TEST100011000101"; //type_code�������ID(����),������������룬��̨��ͨ�����Դ������õ������磺100410000192

//        String return_url = "http://218.58.70.181/test/alipay/alipay_return.jsp";
        //�������˻���Ϣ�����д����վ�Լ���
        //*****************************************************************
        String sign_type = "MD5"; //'ǩ����ʽ�����øģ�
        String subject = "commodityname-test"; //subject ��Ʒ���ơ���վ������

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        sFmt.setTimeZone(tz);
        String dateStr = sFmt.format(new Date());

        //String gmt_out_order_create = "2009-05-14 15:15:15"; //�̻���������ʱ��
        String gmt_out_order_create = dateStr; //�̻���������ʱ��

        String out_order_no = journalno; //�̻���վ������Ҳ�����ⲿ�����ţ���ͨ���ͻ���վ����֧�������������ظ���
        //String amount = amount; //�����ܼ�	0.01��50000.00

        //TODO:
//        String customer_code = "122850000035";

        //********************************************************************


			itemUrl = Payment.CreateUrl(paygateway, t4, t5, service, partner,
					sign_type, subject, gmt_out_order_create, out_order_no,
					amount, customer_code, key, type_code, input_charset);

        //����return_url
//        itemUrl = Payment.CreateUrl_zr(paygateway, t4, t5, service, partner,
//                sign_type, subject, gmt_out_order_create, out_order_no,
//                amount, customer_code, key, type_code, input_charset, return_url);
        //response.sendRedirect(itemUrl);
        return itemUrl;

    }

}
