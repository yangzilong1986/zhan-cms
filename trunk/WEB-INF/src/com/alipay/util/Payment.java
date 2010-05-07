package com.alipay.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
/**
 * 名称：支付主类
 * 功能：支付宝外部服务接口控制
 * 接口名称：代扣接口
 * 版本：2.0
 * 日期：2008-12-25
 * 作者：支付宝公司销售部技术支持团队
 * 联系：0571-26888888
 * 版权：支付宝公司
 * */
public class Payment {
    /**
	 * 签约
	 * */
    public static String CreateUrl2(String paygeteway,String key,
    		String sign_type,String input_charset,Map params) {

        String prestr = "";
        prestr = prestr + key;
        //System.out.println("prestr=" + prestr);

        String sign = com.alipay.util.Md5Encrypt.md5(getContent(params, key));

        String parameter = "";
        parameter = parameter + paygeteway;

        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
            try {
                parameter = parameter + keys.get(i) + "="
                            + URLEncoder.encode((String) params.get(keys.get(i)), input_charset) + "&";
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        parameter = parameter + "sign=" + sign + "&sign_type=" + sign_type;

        return parameter;

    }

    
    /**
	 * 解约
	 * */
    public static String CreateUrl3(String paygetway,String service,
    		String key,String partner,String customer_code,
    		String sign_type,String input_charset) {
        Map params = new HashMap();
        params.put("service", service);
        params.put("partner", partner);
        params.put("customer_code", customer_code);
        params.put("input_charset", input_charset);

        String prestr = "";

        prestr = prestr + key;
        //System.out.println("prestr=" + prestr);

        String sign = com.alipay.util.Md5Encrypt.md5(getContent(params, key));

        String parameter = "";
        parameter = parameter + paygetway;

        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
            try {
                parameter = parameter + keys.get(i) + "="
                            + URLEncoder.encode((String) params.get(keys.get(i)), input_charset) + "&";
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        parameter = parameter + "sign=" + sign + "&sign_type=" + sign_type;

        return parameter;

    }
    
    /**
	 * 代扣
	 * */
    public static String CreateUrl(String paygateway,String t4,String t5,
    		String service,String partner,String sign_type,
    		String subject,String gmt_out_order_create,String out_order_no,
    		String amount,String customer_code,String key,String type_code,
    		String input_charset) {

        Map params = new HashMap();               

        // zhanrui   params.put("paygateway", paygateway);
        params.put("t4", t4);
        params.put("t5", t5);
        params.put("service", service);
        params.put("partner", partner);
        params.put("subject", subject);
        params.put("gmt_out_order_create", gmt_out_order_create);
        params.put("out_order_no", out_order_no);
        params.put("amount", amount);
        params.put("customer_code", customer_code);
        //zhanrui   params.put("key", key);
        params.put("type_code", type_code);
        params.put("input_charset", input_charset);

        String prestr = "";

        prestr = prestr + key;
        //System.out.println("prestr=" + prestr);

        String sign = com.alipay.util.Md5Encrypt.md5(getContent(params, key));

        String parameter = "";
        parameter = parameter + paygateway;

        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
            try {
                parameter = parameter + keys.get(i) + "="
                            + URLEncoder.encode((String) params.get(keys.get(i)), input_charset) + "&";
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        parameter = parameter + "sign=" + sign + "&sign_type=" + sign_type;

        return parameter;

    }
    
    /**
	 * 代扣 test-zhanrui
	 * */
    public static String CreateUrl_zr(String paygateway,String t4,String t5,
    		String service,String partner,String sign_type,
    		String subject,String gmt_out_order_create,String out_order_no,
    		String amount,String customer_code,String key,String type_code,
    		String input_charset,String return_url) {

        Map params = new HashMap();

        // zhanrui   params.put("paygateway", paygateway);
        params.put("t4", t4);
        params.put("t5", t5);
        params.put("service", service);
        params.put("partner", partner);
        params.put("subject", subject);
        params.put("gmt_out_order_create", gmt_out_order_create);
        params.put("out_order_no", out_order_no);
        params.put("amount", amount);
        params.put("customer_code", customer_code);
        //zhanrui   params.put("key", key);
        params.put("type_code", type_code);
        params.put("input_charset", input_charset);
        params.put("return_url", return_url);

        String prestr = "";

        prestr = prestr + key;
        //System.out.println("prestr=" + prestr);

        String sign = com.alipay.util.Md5Encrypt.md5(getContent(params, key));

        String parameter = "";
        parameter = parameter + paygateway;

        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
            try {
                parameter = parameter + keys.get(i) + "="
                            + URLEncoder.encode((String) params.get(keys.get(i)), input_charset) + "&";
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        parameter = parameter + "sign=" + sign + "&sign_type=" + sign_type;

        return parameter;

    }

    /**
	 * 代扣查询
	 * */
    public static String CreateUrl4(String paygateway,String service,
        	String partner,String sign_type, 
        	String gmt_create_start,String gmt_create_end,
        	String type_code,String key,String input_charset) {
    	
        Map params = new HashMap();
        params.put("service", service);
        params.put("partner", partner);
        //params.put("seller_email", seller_email);
        params.put("gmt_create_start", gmt_create_start);
        params.put("gmt_create_end", gmt_create_end);
        params.put("type_code", type_code);          
        params.put("_input_charset", input_charset);

        String prestr = "";

        prestr = prestr + key;
        //System.out.println("prestr=" + prestr);

        String sign = com.alipay.util.Md5Encrypt.md5(getContent(params, key));

        String parameter = "";
        parameter = parameter + paygateway;

        List keys = new ArrayList(params.keySet());
        for (int i = 0; i < keys.size(); i++) {
          	String value =(String) params.get(keys.get(i));
            if(value == null || value.trim().length() ==0){
            	continue;
            }
            try {
                parameter = parameter + keys.get(i) + "="
                    + URLEncoder.encode(value, input_charset) + "&";
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        parameter = parameter + "sign=" + sign + "&sign_type=" + sign_type;

        return parameter;

    }
    /**
     * 功能：将安全校验码和参数排序
     * 参数集合
     * @param params
     * 安全校验码
     * @param privateKey
     * */
    private static String getContent(Map params, String privateKey) {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);

            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr + privateKey;
    }
}
