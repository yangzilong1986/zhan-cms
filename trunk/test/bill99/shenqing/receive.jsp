<%@page contentType="text/html; charset=gb2312" language="java"%>
<%@ page import="com.bill99.encrypt.MD5Util"%>
<%
/**
 * @Description: ��Ǯ���׸�ҵ����Ȩ�ӿڷ���
 * @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
 * @version 2.0
 */

//��ȡ����������˻���
String merchantAcctId=(String)request.getParameter("merchantAcctId").trim();

//���������������Կ
///���ִ�Сд
String key="1234567891234567";

//��ȡ���ذ汾.�̶�ֵ
///��Ǯ����ݰ汾�������ö�Ӧ�Ľӿڴ������
///������汾�Ź̶�Ϊv2.0
String version=(String)request.getParameter("version").trim();

//ǩ������.�̶�ֵ
///1����MD5ǩ��
///��ǰ�汾�̶�Ϊ1
String signType=(String)request.getParameter("signType").trim();

//������
String requestId=(String)request.getParameter("requestId").trim();

//��ȡ�����ύʱ��
///��ȡ�̻��ύ����ʱ��ʱ��.14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磺20080101010101
String requestTime=(String)request.getParameter("requestTime").trim();

//��ȡ�ڿ�Ǯ����ʱ��
///14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磻20080101010101
String dealTime=(String)request.getParameter("dealTime").trim();

//��ȡ����������
///��λΪ��
///�ȷ� 2 ������0.02Ԫ
String fee=(String)request.getParameter("fee").trim();

//��ȡ��չ�ֶ�1
String ext1=(String)request.getParameter("ext1").trim();

//��ȡ��չ�ֶ�2
String ext2=(String)request.getParameter("ext2").trim();

//��ȡ������
///10����ɹ� 11����ʧ��
String dealResult=(String)request.getParameter("dealResult").trim();

//���׸�Э���
String debitProtocolId=request.getParameter("debitProtocolId").trim();

//��ȡ�������
///��ϸ���ĵ���������б�
String errCode=(String)request.getParameter("errCode").trim();

//��ȡ����ǩ����
String signMsg=(String)request.getParameter("signMsg").trim();



//���ɼ��ܴ������뱣������˳��
	String merchantSignMsgVal="";
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"version",version);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"signType",signType);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"merchantAcctId",merchantAcctId);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"requestId",requestId);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"requestTime",requestTime);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"dealTime",dealTime);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"fee",fee);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"ext1",ext1);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"ext2",ext2);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"dealResult",dealResult);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"debitProtocolId",debitProtocolId);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"errCode",errCode);
	merchantSignMsgVal=appendParam(merchantSignMsgVal,"key",key);

String merchantSignMsg= MD5Util.md5Hex(merchantSignMsgVal.getBytes("gb2312")).toUpperCase();

//�̼ҽ������ݴ�������ת���̼���ʾ֧�������ҳ��
///���Ƚ���ǩ���ַ�����֤
if(signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())){

	///���Ž���֧������ж�
	switch(Integer.parseInt(dealResult)){
	
		  case 10:
			
			//*  
			// �̻���վ�߼������ȷ����¶���֧��״̬Ϊ�ɹ�
			// �ر�ע�⣺ֻ��signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())����payResult=10���ű�ʾ֧���ɹ���ͬʱ������������ύ����ǰ�Ķ��������жԱ�У�顣
			//*
			
			//�������Ǯ�����������ṩ��Ҫ�ض���ĵ�ַ��
			response.sendRedirect("http://www.yoursite.com/show.jsp?msg=success!");
			break;
		  
		 default:
			response.sendRedirect("http://www.yoursite.com/show.jsp?msg=false!");
			break;

	}

}else{
	response.sendRedirect("http://www.yoursite.com/show.jsp?msg=error!");

}

%>
<%!
	//���ܺ�����������ֵ��Ϊ�յĲ�������ַ���
	public String appendParam(String returnStr,String paramId,String paramValue)
	{
			if(!returnStr.equals(""))
			{
				if(!paramValue.equals(""))
				{
					returnStr=returnStr+"&"+paramId+"="+paramValue;
				}
			}
			else
			{
				if(!paramValue.equals(""))
				{
				returnStr=paramId+"="+paramValue;
				}
			}	
			return returnStr;
	}
	//���ܺ�����������ֵ��Ϊ�յĲ�������ַ���������

%>
