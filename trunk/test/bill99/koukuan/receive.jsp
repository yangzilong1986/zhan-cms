<%@page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="zt.platform.utils.Debug" %>
<%@ page import="java.util.Date" %>
<%
    /**
     * @Description: ��Ǯ���׸��ۿ����ؽӿڷ���
     * @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
     * @version 2.0
     */

    System.out.println("\n" + new Date());
    System.out.println("99Bill: ------------koukuan receive.jsp--------------" + new Date());

    int rtnOk = 0;
    String rtnUrl = "";

    //��Ǯ֪ͨ��ַ
    String KQ_KK_mesUrl = PropertyManager.getProperty("KQ_KK_mesUrl");

    System.out.println("KQ_KK_mesUrl=" + KQ_KK_mesUrl);

    try {


//��ȡ����������˻���
        String merchantAcctId = (String) request.getParameter("merchantAcctId").trim();

//���������������Կ
///���ִ�Сд
//String key="1234567891234567";
        String key = PropertyManager.getProperty("KQ_key");

//��ȡ���ذ汾.�̶�ֵ
///��Ǯ����ݰ汾�������ö�Ӧ�Ľӿڴ������
///������汾�Ź̶�Ϊv2.0
        String version = (String) request.getParameter("version").trim();

//ǩ������.�̶�ֵ
///1����MD5ǩ��
///��ǰ�汾�̶�Ϊ1
        String signType = (String) request.getParameter("signType").trim();

//��ȡ�̻�������
        String orderId = (String) request.getParameter("orderId").trim();

//��ȡ�����ύʱ��
///��ȡ�̻��ύ����ʱ��ʱ��.14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磺20080101010101
        String orderTime = (String) request.getParameter("orderTime").trim();

//��ȡԭʼ�������
///�����ύ����Ǯʱ�Ľ���λΪ�֡�
///�ȷ�2 ������0.02Ԫ
        String orderAmount = (String) request.getParameter("orderAmount").trim();

//��Ʒ����
///���̻��ύʱһ��
        String productName = (String) request.getParameter("productName").trim();

//��Ʒ����
///���̻��ύʱһ��
        String productId = (String) request.getParameter("productId").trim();

//��ȡ���н��׺�
///���ʹ�����п�֧��ʱ�������еĽ��׺š��粻��ͨ������֧������Ϊ��
        String bankDealId = (String) request.getParameter("bankDealId").trim();

//��ȡ�ڿ�Ǯ�������ʱ��
///14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磻20080101010101
        String dealTime = (String) request.getParameter("dealTime").trim();

//��ȡ����������
///��λΪ��
///�ȷ� 2 ������0.02Ԫ
        String fee = (String) request.getParameter("fee").trim();

//��ȡ��չ�ֶ�1
        String ext1 = (String) request.getParameter("ext1").trim();

//��ȡ��չ�ֶ�2
        String ext2 = (String) request.getParameter("ext2").trim();

//��ȡ������
///10���� �ɹ�11���� ʧ��
///00���� �¶����ɹ������Ե绰����֧���������أ�;01���� �¶���ʧ�ܣ����Ե绰����֧���������أ�
        String dealResult = (String) request.getParameter("dealResult").trim();

//��ȡ�������
///��ϸ���ĵ���������б�
        String errCode = (String) request.getParameter("errCode").trim();

//��ȡ����ǩ����
        String signMsg = (String) request.getParameter("signMsg").trim();


//���ɼ��ܴ������뱣������˳��
        String merchantSignMsgVal = "";
/*        merchantSignMsgVal = appendParam(merchantSignMsgVal, "merchantAcctId", merchantAcctId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version", version);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType", signType);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId", orderId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime", orderTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount", orderAmount);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "productName", productName);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "productId", productId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId", bankDealId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime", dealTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealResult", dealResult);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode", errCode);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "key", key);
 */

        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version", version);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType", signType);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "merchantAcctId", merchantAcctId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId", orderId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime", orderTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount", orderAmount);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "productName", productName);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "productId", productId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId", bankDealId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime", dealTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealResult", dealResult);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode", errCode);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "key", key);
        

        System.out.println("99Bill: receive.jsp" + merchantSignMsgVal);

        String merchantSignMsg = MD5Util.md5Hex(merchantSignMsgVal.getBytes("gb2312")).toUpperCase();

//�̼ҽ������ݴ�������ת���̼���ʾ֧�������ҳ��
///���Ƚ���ǩ���ַ�����֤




        if (signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())) {

            ///���Ž���֧������ж�
            switch (Integer.parseInt(dealResult)) {

                case 10:

                    //*
                    // �̻���վ�߼������ȷ����¶���֧��״̬Ϊ�ɹ�
                    // �ر�ע�⣺ֻ��signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())����dealResult=10���ű�ʾ֧���ɹ���ͬʱ������������ύ����ǰ�Ķ��������жԱ�У�顣
                    //*

                    rtnOk = 1;
                    rtnUrl = KQ_KK_mesUrl + "?msg=success!";

                    //�������Ǯ�����������ṩ��Ҫ�ض���ĵ�ַ��
//                response.sendRedirect("http://localhost/test/bill99/koukuan/show.jsp?msg=success!");


                    break;

                default:
                    rtnOk = 1;
                    rtnUrl = KQ_KK_mesUrl + "?msg=fail!";
//                response.sendRedirect("http://localhost/test/bill99/koukuan/show.jsp?msg=false!");
                    break;

            }

        } else {
            rtnOk = 1;
            rtnUrl = KQ_KK_mesUrl + "?msg=error!";
            System.out.println("Bill99 MD5�������⣡");
            System.out.println("Remote MD5=" +  signMsg.toUpperCase());
            System.out.println("Local MD5=" +  merchantSignMsg.toUpperCase());

//            response.sendRedirect("http://localhost/test/bill99/koukuan/show.jsp?msg=error!");
        }

    } catch (Exception e) {
        Debug.debug(e);
        rtnOk = 1;
        rtnUrl = KQ_KK_mesUrl + "?msg=error!";
        System.out.println("Bill99 �ۿ�������⣡ ");
%>

<result><%=rtnOk%></result><redirecturl><%=rtnUrl%></redirecturl>

<%
    }
%>

<%!
    //���ܺ�����������ֵ��Ϊ�յĲ�������ַ���
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

%>
<%
    System.out.println("-----------");
    System.out.println("rtnOk=" + rtnOk+"  rtnUrl=" + rtnUrl);
%>
<%--<result>1</result><redirecturl>NA</redirecturl>--%>
<result><%=rtnOk%></result><redirecturl><%=rtnUrl%></redirecturl>
