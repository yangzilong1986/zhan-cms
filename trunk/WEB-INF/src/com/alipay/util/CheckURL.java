  
package com.alipay.util;
import java.net.*;
import java.io.*;

/**
 * ���ƣ�֧����֤��
 * ���ܣ�������֤�����Ϣ������֧����ATN���ؽ��
 * �����ԣ�֧����������
 * �汾��2.0
 * ���ڣ�2008-12-25
 * ���ߣ�֧������˾���۲�����֧���Ŷ�
 * ��ϵ��0571-26888888
 * ��Ȩ��֧������˾
 * */
public class CheckURL {
	   /**
     * ���ַ�������MD5����
	 * @param myUrl 
     *
     * @param url
     *
     * @return ��ȡurl����
     */
  public static String check(String urlvalue ) {
	 
	 
	  String inputLine="";
	  
		try{
				URL url = new URL(urlvalue);
				
				HttpURLConnection urlConnection  = (HttpURLConnection)url.openConnection();
				
				BufferedReader in  = new BufferedReader(
			            new InputStreamReader(
			            		urlConnection.getInputStream()));
			
				inputLine = in.readLine().toString();
			}catch(Exception e){
				e.printStackTrace();
			}
			//System.out.println(inputLine);  ϵͳ��ӡ��ץȡ����֤���
			/*�����Ӧ�Ĳ�����Ӧ����
			 * 1.invalid����������� ��������������ⷵ�ش�����partner��key�Ƿ�Ϊ��
			 * 2.true ������ȷ��Ϣ 
			 * 3.false �������ǽ�����Ƿ�������ֹ�˿������Լ���֤ʱ���Ƿ񳬹�һ����
			 * */
			
	    return inputLine;
  }


  }