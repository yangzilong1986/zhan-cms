package zt.cms.xf.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 与新信贷系统接口.
 * User: zhanrui
 * Date: 2010-10-11
 * Time: 11:15:25
 * To change this template use File | Settings | File Templates.
 */
public class NewCmsManager {

    private String serverUrl = "http://10.143.19.106:10002/LoanSysPortal/CMSServlet";
    private Log logger = LogFactory.getLog(this.getClass());



    public String doPostXml(String xmlStr) {

        logger.info("发送报文： " + xmlStr);

        String responseBody = null;
        HttpClient httpclient = null;

        try {
            httpclient = new DefaultHttpClient();
            //请求超时
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            //读取超时
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000*5);
            HttpPost httppost = new HttpPost(serverUrl);

            httppost.getURI();


            StringEntity xmlSE = new StringEntity(xmlStr, "GBK");
            httppost.setEntity(xmlSE);


            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = null;

            responseBody = httpclient.execute(httppost, responseHandler);
        } catch (UnsupportedEncodingException e) {
            logger.error("报文格式转换错误", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("通讯错误!", e);
            throw new RuntimeException(e);
        }catch (Exception e) {
            logger.error("通讯错误!", e);
            throw new RuntimeException(e);
        } finally {
        }

        logger.info("接收报文： " + responseBody);
        return responseBody;
    }
}
