package zt.cms.xf.test;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-9-16
 * Time: 9:30:59
 * To change this template use File | Settings | File Templates.
 */

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

public class ClientCustomSSL{
    public final static void main(String[] args) throws Exception {
           DefaultHttpClient httpclient = new DefaultHttpClient();      

           KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
           FileInputStream instream = new FileInputStream(new File("my.keystore"));
           try {
               trustStore.load(instream, "nopassword".toCharArray());
           } finally {
               instream.close();
           }

           SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
           Scheme sch = new Scheme("https", socketFactory, 443);
           httpclient.getConnectionManager().getSchemeRegistry().register(sch);

//           HttpGet httpget = new HttpGet("https://localhost/");
           HttpGet httpget = new HttpGet("https://www.99bill.com/bankdebit/serviceDeduction.htm");

           System.out.println("executing request" + httpget.getRequestLine());

           HttpResponse response = httpclient.execute(httpget);
           HttpEntity entity = response.getEntity();

           System.out.println("----------------------------------------");
           System.out.println(response.getStatusLine());
           if (entity != null) {
               System.out.println("Response content length: " + entity.getContentLength());
           }
           if (entity != null) {
               entity.consumeContent();
           }

           // When HttpClient instance is no longer needed,
           // shut down the connection manager to ensure
           // immediate deallocation of all system resources
           httpclient.getConnectionManager().shutdown();
       }

}
