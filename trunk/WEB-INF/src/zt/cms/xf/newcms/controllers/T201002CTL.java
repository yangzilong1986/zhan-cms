package zt.cms.xf.newcms.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import zt.cms.xf.newcms.domain.T100101.T100101Request;
import zt.cms.xf.newcms.domain.T100101.T100101Response;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
public class T201002CTL {
    private Log logger = LogFactory.getLog(this.getClass());
       
    public final static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("http://10.143.19.106:10002/LoanSysPortal/CMSServlet");

        // Execute HTTP request
        System.out.println("executing request " + httppost.getURI());

        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T100101Request.class);
        xstream.processAnnotations(T100101Response.class);


        T100101Request request = new T100101Request();
        //查询类交易
        request.setStdmsgtype("0100");
        //交易码
        request.setStd400trcd("201002");

        request.setStd400aqid("3");
        request.setStd400tlno("teller");

        request.setStdlocdate("20101010");
        request.setStdloctime("153000");

        request.setStdtermtrc("1");

        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        System.out.println(strXml);

        StringEntity xml = new StringEntity(strXml, "GBK");
        httppost.setEntity(xml);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);

        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");


        T100101Response response = (T100101Response) xstream.fromXML(responseBody);

        System.out.println(response);


        httpclient.getConnectionManager().shutdown();
    }

}
