package zt.cms.xf.gateway.test.httpclienttest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
public class PLonCutPay100102 {

    public final static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("http://10.143.19.120:10002/LoanSysPortal/CMSServlet");

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

        TxPkgHeader header = new TxPkgHeader();
        header.setStdmsgtype("0100");
        header.setStd400trcd("100102");
        header.setStd400aqid("3");
        header.setStd400tlno("teller");

        header.setStdlocdate("20100827");
        header.setStdloctime("153000");

        header.setStdtermtrc("1");

//        xstream.alias("ROOT type=\"request\"",TxPkgHeader.class);
        xstream.alias("ROOT", TxPkgHeader.class);
        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(header);


        strXml="<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<ROOT>\n" +
                "  <stdmsgtype>0100</stdmsgtype>\n" +
                "  <std400trcd>100102</std400trcd>\n" +
                "  <stdprocode></stdprocode>\n" +
                "  <std400aqid>3</std400aqid>\n" +
                "  <stdmercno></stdmercno>\n" +
                "  <stdtermtyp></stdtermtyp>\n" +
                "  <stdtermid></stdtermid>\n" +
                "  <std400tlno>teller</std400tlno>\n" +
                "  <stdpriacno></stdpriacno>\n" +
                "  <stdpindata></stdpindata>\n" +
                "  <stdlocdate>20100827</stdlocdate>\n" +
                "  <stdloctime>153000</stdloctime>\n" +
                "  <stdtermtrc>1</stdtermtrc>\n" +
                "  <std400autl></std400autl>\n" +
                "  <stdauthid></stdauthid>\n" +
                "  <std400aups></std400aups>\n" +
                "  <std400trdt></std400trdt>\n" +
                "  <stdrefnum>0</stdrefnum>\n" +
                "  <stdsetdate></stdsetdate>\n" +
                "  <std400trno></std400trno>\n" +
                "  <std400mgid></std400mgid>\n" +
                "  <std400acur>00</std400acur>\n" +
                "<LIST>\n" +
                "<ROWS>\n" +
                "<stdjjh>111112</stdjjh>\n" +
                "<stdjhkkr>20100802</stdjhkkr>\n" +
                "<stdkkjg>1</stdkkjg>\n" +
                "</ROWS>\n" +
                "<ROWS>\n" +
                "<stdjjh>111113</stdjjh>\n" +
                "<stdjhkkr>20100803</stdjhkkr>\n" +
                "<stdkkjg>2</stdkkjg>\n" +
                "</ROWS>\n" +
                "</LIST>\n" +
                "</ROOT>";
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


//         TxPkgHeader tt = (TxPkgHeader)xstream.fromXML(rtnstr);

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
}

}
