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
    //����������˻���
    //���¼��Ǯϵͳ��ȡ�û���ţ��û���ź��01��Ϊ����������˻��š�
    private static String merchantAcctId = PropertyManager.getProperty("KQ_merchantAcctId");

    //�����������Կ
    private static String key = PropertyManager.getProperty("KQ_key");

    //���ܿۿ�����ҳ���ַ
    ///�������ҳ�淵�أ���Ǯ����GET��ʽ�ύ���̻���ַ��
    ///�����bgUrl���أ���ôҳ��ת����bgUrl��ת���ַΪ׼���緵��û��ת���ַ����ת���Դ˵�ַΪ׼��
    ///bgUrl��pageUrl���߲���ͬʱΪ��
     private static   String pageUrl = PropertyManager.getProperty("KQ_KK_pageUrl");

    //���������տۿ����ĺ�̨��ַ
    ///��Ǯ��ͨ�����������ӵķ�ʽ�����׽���������ݸ��̻��ṩ�����url���̻������������ܽ���ͷ���ҳ���ַ��
    ///�����Ǯ���ܲ����̻��ķ��أ���ֱ��Redirect��pageUrl��ȥ��ͬʱ����֧���������
     private static   String bgUrl = PropertyManager.getProperty("KQ_KK_bgUrl");



    /*
    ��Ǯ�����ݿۿ��¼����POST����
     */
    public List getPostValues(Xfactcutpaydetl xfcutpaydetl) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        sdf.setTimeZone(tz);

        //���׸�Э����
        //�������������׸�����󣬵õ���
        ///�ַ���.ֻ֧����ĸ�����֣�_��-���������ֻ���ĸ��ͷ��
//        String debitProtocolId = "1126637";                 //alippy
        String debitProtocolId = xfcutpaydetl.getCustomerCode();


        //�ַ���.�̶�ѡ��ֵ����Ϊ�ա�
        ///ֻ��ѡ��1��2��3.
        ///1����UTF-8; 2����GBK; 3����gb2312
        ///Ĭ��ֵΪ1
        String inputCharset = "3";

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
//         String orderId =  sdf.format(new java.util.Date());
        String orderId = xfcutpaydetl.getJournalno();

        //�������
        ///�Է�Ϊ��λ����������������
        ///�ȷ�2������0.02Ԫ
//        String orderAmount = "9999.9";
        BigDecimal amt = xfcutpaydetl.getPaybackamt().multiply(new BigDecimal(100));
        String orderAmount = String.valueOf(amt.intValue());

        //�����ύʱ��
        ///14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
        ///�磻20080101010101
        String orderTime = sdf.format(new java.util.Date());

        //��Ʒ����
        ///��Ϊ���Ļ�Ӣ���ַ�
        String productName = "productName_haier";

        //��Ʒ����
        ///��Ϊ�ַ���������
        String productId = "productType_haier";

        //��չ�ֶ�1
        ///��֧��������ԭ�����ظ��̻�
        String ext1 = "test1";

        //��չ�ֶ�2
        ///��֧��������ԭ�����ظ��̻�
        String ext2 = "text2";


        //���ɼ���ǩ����
        ///����ذ�������˳��͹�����ɼ��ܴ���
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
    * ���ܺ�����������ֵ��Ϊ�յĲ�������ַ���
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
    //���ܺ�����������ֵ��Ϊ�յĲ�������ַ���������
}
