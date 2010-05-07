package zt.cms.xf.common.ssl;
//
//import java.io.IOException;
//
//import org.apache.http.client.DefaultHttpMethodRetryHandler;
//import org.apache.commons.http.client.HttpClient;
//import org.apache.commons.http.client.HttpException;
//import org.apache.commons.http.client.HttpStatus;
//import org.apache.commons.http.client.methods.GetMethod;
//import org.apache.commons.http.client.params.HttpMethodParams;
//import org.apache.commons.http.client.protocol.Protocol;
//import org.apache.http.client.HttpClient;
//
//public class NoCertificationHttpsGetSample {
//	public static void main(String[] args) {
//		Protocol myhttps = new Protocol("https",
//				new MySecureProtocolSocketFactory(), 443);
//		Protocol.registerProtocol("https", myhttps);
//		// 构造HttpClient的实例
//		HttpClient httpClient = new HttpClient();
//		// 创建GET方法的实例
//		GetMethod getMethod = new GetMethod("https://www.99bill.com/bankdebit/serviceDeduction.htm");
//		// 使用系统提供的默认的恢复策略
//		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//				new DefaultHttpMethodRetryHandler());
//		try {
//			// 执行getMethod
//			int statusCode = httpClient.executeMethod(getMethod);
//			if (statusCode != HttpStatus.SC_OK) {
//				System.err.println("Method failed: "
//						+ getMethod.getStatusLine());
//			}
//			// 读取内容
//			byte[] responseBody = getMethod.getResponseBody();
//			// 处理内容
//			System.out.println(new String(responseBody));
//		} catch (HttpException e) {
//			System.out.println("Please check your provided http address!");
//			// 发生致命的异常，可能是协议不对或者返回的内容有问题
//			e.printStackTrace();
//		} catch (IOException e) {
//			// 发生网络异常
//			e.printStackTrace();
//		} finally {
//			// 释放连接
//			getMethod.releaseConnection();
//		}
//
//	}
//}

//
//import java.io.File;
//import java.io.FileInputStream;
//import java.security.KeyStore;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//
///**
// * This example demonstrates how to create secure connections with a custom SSL
// * context.
// */
////public class ClientCustomSSL {
//public class NoCertificationHttpsGetSample {
//
//    public final static void main(String[] args) throws Exception {
//        DefaultHttpClient httpclient = new DefaultHttpClient();
//
//        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
////        FileInputStream instream = new FileInputStream(new File("my.keystore"));
//        FileInputStream instream = new FileInputStream(new File("D:\\bea9\\jdk150_12\\jre\\lib\\security\\alibaba_keystore"));
//        try {
//            trustStore.load(instream, "changeit".toCharArray());
//        } finally {
//            instream.close();
//        }
//
//        SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
//        Scheme sch = new Scheme("https", socketFactory, 443);
//        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
//
//        HttpGet httpget = new HttpGet("https://www.alipay.com/cooperate/gateway.do");
////        HttpGet httpget = new HttpGet("https://www.99bill.com/bankdebit/serviceDeduction.htm/");
//
//        System.out.println("executing request" + httpget.getRequestLine());
//
//        HttpResponse response = httpclient.execute(httpget);
//        HttpEntity entity = response.getEntity();
//
//        System.out.println("----------------------------------------");
//        System.out.println(response.getStatusLine());
//        if (entity != null) {
//            System.out.println("Response content length: " + entity.getContentLength());
//        }
//        if (entity != null) {
//            entity.consumeContent();
//        }
//    }
//
//}
