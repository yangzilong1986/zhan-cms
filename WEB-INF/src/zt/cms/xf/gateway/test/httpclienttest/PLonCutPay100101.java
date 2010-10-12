package zt.cms.xf.gateway.test.httpclienttest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import zt.cms.xf.gateway.test.httpclienttest.T100101.T100101Result;
import zt.cms.xf.gateway.test.httpclienttest.T100101.T100101ResultBody;
import zt.cms.xf.newcms.domain.common.MsgHeader;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
public class PLonCutPay100101 {

    public final static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();

//        HttpPost httppost = new HttpPost("http://10.143.19.120:10002/LoanSysPortal/CMSServlet");
        HttpPost httppost = new HttpPost("http://10.143.19.106:10002/LoanSysPortal/CMSServlet");

        // Execute HTTP request
        System.out.println("executing request " + httppost.getURI());

/*
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("out_sign_no", "value1"));
        formparams.add(new BasicNameValuePair("email", "value2"));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity1);
*/

        XStream xstream = new XStream(new DomDriver());

        MsgHeader header = new MsgHeader();
        header.setStdmsgtype("0100");
        header.setStd400trcd("100101");
        header.setStd400aqid("3");
        header.setStd400tlno("teller");

        header.setStdlocdate("20100925");
        header.setStdloctime("153000");

        header.setStdtermtrc("1");

//        xstream.alias("ROOT type=\"request\"",MsgHeader.class);
        xstream.alias("ROOT", MsgHeader.class);
        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(header);

        System.out.println(strXml);


        StringEntity xml = new StringEntity(strXml, "GBK");


        httppost.setEntity(xml);


        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);

        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");


        String rtnstr = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<ROOT><stdmsgtype>0100</stdmsgtype><std400trcd>100101</std400trcd><stdprocode/><std400aqid>3</std400aqid><stdmercno/><stdtermtyp/><stdtermid/><std400tlno>teller</std400tlno><stdpriacno/><stdpindata/><stdlocdate>20100827</stdlocdate><stdloctime>153000</stdloctime><stdtermtrc>1</stdtermtrc><std400autl/><stdauthid/><std400aups/><std400trdt/><stdrefnum>0</stdrefnum><stdsetdate/><std400trno/><std400acur>00</std400acur><std400mgid>AAAAAAA</std400mgid></ROOT>";


        MsgHeader tt = (MsgHeader) xstream.fromXML(rtnstr);

        
        xstream.alias("ROOT", T100101Result.class);  
//        xstream.alias("LIST", ArrayList.class);
//        xstream.alias("LIST", ArrayList.class);
        xstream.alias("ROWS", T100101ResultBody.class);
        xstream.aliasField("LIST", T100101ResultBody.class, "records");
        T100101Result result = (T100101Result) xstream.fromXML(responseBody);

        System.out.println(tt);
        System.out.println(result);

/*
        HttpResponse response = httpclient.execute(httppost);

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println("----------------------------------------");

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            long len = entity.getContentLength();
            if (len != -1 && len < 204800) {
                System.out.println(EntityUtils.toString(entity));
                System.out.println("==================================");
            } else {
                // Stream content out
            }
*/

/*
        // If the response does not enclose an entity, there is no need
        // to bother about connection release
        if (entity != null) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            try {

                // do something useful with the response
                System.out.println(reader.readLine());
                System.out.println("==================================");

            } catch (IOException ex) {

                // In case of an IOException the connection will be released
                // back to the connection manager automatically
                throw ex;

            } catch (RuntimeException ex) {

                // In case of an unexpected exception you may want to abort
                // the HTTP request in order to shut down the underlying
                // connection and release it back to the connection manager.
                httppost.abort();
                throw ex;

            } finally {

                // Closing the input stream will trigger connection release
                reader.close();

            }
*/
        // }

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();

        System.out.println("====***====");
    }

}
