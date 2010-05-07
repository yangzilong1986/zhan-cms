package zt.cms.xf.gateway.test.httpclienttest;
import java.io.FileInputStream;
import java.io.File;
import java.security.KeyStore;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.ResponseHandler;

public class PostSample {
//    String getpath(){
////        ServletContext servletContext = Config.getServletContext();
////        ServletContext servletContext = servletConfig.getServletContext();
////         String ttt= servletContext.getRealPath("/");
//        String ttt = System.getProperty("user.dir");
//        return ttt;
//    }
    public final static void main(String[] args) throws Exception {
           DefaultHttpClient httpclient = new DefaultHttpClient();

           KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());

           String path=  System.getProperty("user.dir");

           System.out.println(path);
           FileInputStream instream = new FileInputStream(new File("d:/cms/keystore/cms.truststore.99bill"));
           try {
               trustStore.load(instream, "111111".toCharArray());
           } finally {
               instream.close();
           }

           SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
           Scheme sch = new Scheme("https", socketFactory, 443);
           httpclient.getConnectionManager().getSchemeRegistry().register(sch);

//           HttpGet httpget = new HttpGet("https://localhost/");
           HttpGet httpget = new HttpGet("https://www.99bill.com1/bankdebit/serviceDeduction.htm");

           System.out.println("executing request" + httpget.getRequestLine());

//           HttpResponse response = httpclient.execute(httpget);
//           HttpEntity entity = response.getEntity();
//
//           System.out.println("----------------------------------------");
//           System.out.println(response.getStatusLine());
//           if (entity != null) {
//               System.out.println("Response content length: " + entity.getContentLength());
//           }
//           if (entity != null) {
//               entity.consumeContent();
//
//           }

           // When HttpClient instance is no longer needed,
           // shut down the connection manager to ensure
           // immediate deallocation of all system resources

         ResponseHandler<String> responseHandler = new BasicResponseHandler();
         String responseBody = httpclient.execute(httpget, responseHandler);
         System.out.println(responseBody);

           httpclient.getConnectionManager().shutdown();
       }

}
