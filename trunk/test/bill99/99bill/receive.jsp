<%@page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%
    /**
     * @Description: ��Ǯ���׸�ҵ����Ȩ�ӿڷ���
     * @Copyright (c) �Ϻ���Ǯ��Ϣ�������޹�˾
     * @version 2.0
     */
    System.out.println("--��Ǯ--");
    //��Ǯ֪ͨ��ַ
    String KQ_mesUrl = PropertyManager.getProperty("KQ_mesUrl");
//��ȡ����������˻���
    String merchantAcctId = (String) request.getParameter("merchantAcctId").trim();
//String merchantAcctId= PropertyManager.getProperty("KQ_merchantAcctId");

//���������������Կ
///���ִ�Сд
    String key = PropertyManager.getProperty("KQ_key");

//��ȡ���ذ汾.�̶�ֵ
///��Ǯ����ݰ汾�������ö�Ӧ�Ľӿڴ������
///������汾�Ź̶�Ϊv2.0
    String version = (String) request.getParameter("version").trim();

//ǩ������.�̶�ֵ
///1����MD5ǩ��
///��ǰ�汾�̶�Ϊ1
    String signType = (String) request.getParameter("signType").trim();

//������
    String requestId = (String) request.getParameter("requestId").trim();

//��ȡ�����ύʱ��
///��ȡ�̻��ύ����ʱ��ʱ��.14λ���֡���[4λ]��[2λ]��[2λ]ʱ[2λ]��[2λ]��[2λ]
///�磺20080101010101
    String requestTime = (String) request.getParameter("requestTime").trim();

//��ȡ�ڿ�Ǯ����ʱ��
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
///10����ɹ� 11����ʧ��
    String dealResult = (String) request.getParameter("dealResult").trim();

//���׸�Э���
    String debitProtocolId = request.getParameter("debitProtocolId").trim();

//��ȡ�������
///��ϸ���ĵ���������б�
    String errCode = (String) request.getParameter("errCode").trim();

//��ȡ����ǩ����
    String signMsg = (String) request.getParameter("signMsg").trim();


//���ɼ��ܴ������뱣������˳��
    String merchantSignMsgVal = "";
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "version", version);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType", signType);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "merchantAcctId", merchantAcctId);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "requestId", requestId);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "requestTime", requestTime);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime", dealTime);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealResult", dealResult);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "debitProtocolId", debitProtocolId);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode", errCode);
    merchantSignMsgVal = appendParam(merchantSignMsgVal, "key", key);

    String merchantSignMsg = MD5Util.md5Hex(merchantSignMsgVal.getBytes("gb2312")).toUpperCase();


    //��ʼ���������ַ
    int rtnOk = 0;
    String rtnUrl = "";
    String msg = "";
    String funcdel = "";
    String isback = "1";
    String flag = "1";

//�̼ҽ������ݴ�������ת���̼���ʾ֧�������ҳ��
///���Ƚ���ǩ���ַ�����֤
    if (signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())) {

        ///���Ž���֧������ж�
        switch (Integer.parseInt(dealResult)) {
            case 10:

                //*
                // �̻���վ�߼������ȷ����¶���֧��״̬Ϊ�ɹ�
                // �ر�ע�⣺ֻ��signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())����payResult=10���ű�ʾ֧���ɹ���ͬʱ������������ύ����ǰ�Ķ��������жԱ�У�顣
                //*
                String sql1 = "update XFAPP " +
                        "set APPSTATUS='1',CUSTOMER_CODE='" + debitProtocolId + "' " +
                        "where seqno='" + requestId + "'";

                ConnectionManager manager = ConnectionManager.getInstance();
                boolean temp = manager.ExecCmd(sql1);

                if (temp) {
                    session.setAttribute("msg", "�����������ύ�ɹ������ڵ���ȷ�����������Ĵ�ӡ����ѡ���������뵥��ӡ��ǩ����������ͬ����֤���ļ���ӡ��һͬ���͸����ǣ����ͨ�������ǽ�����ȡ����ϵ��");
                    session.setAttribute("funcdel", "window.opener.document.getElementById('print').click();pageWinClose();");
                    session.setAttribute("isback", "0");

//                    msg = "�����������ύ�ɹ������ڵ���ȷ�����������Ĵ�ӡ����ѡ���������뵥��ӡ��ǩ����������ͬ����֤���ļ���ӡ��һͬ���͸����ǣ����ͨ�������ǽ�����ȡ����ϵ��";
//                    funcdel = "window.opener.document.getElementById('print').click();pageWinClose();";
//                    isback = "0";
                    rtnOk = 1;
                    rtnUrl = KQ_mesUrl + "?msg=success!";

                } else {
                    session.setAttribute("msg", "�����������ύ�ɹ����������뵥״̬��������ϵ���ǣ�");
                    session.setAttribute("flag", "0");

                    rtnOk = 1;
                    rtnUrl = KQ_mesUrl + "?msg=success part!";
//                    msg = "�����������ύ�ɹ����������뵥״̬��������ϵ���ǣ�";
//                    flag = "0";
                }

                //�������Ǯ�����������ṩ��Ҫ�ض���ĵ�ַ��
//                rtnOk = 1;
//                rtnUrl = KQ_mesUrl + "?msgkq=" + msg + "&funcdel=" + funcdel + "&isback=" + isback + "&flag=" + flag + "&msg=success!";
                break;

            default:
                session.setAttribute("msg", "�������뻹δ��ɣ����ڿ�Ǯ��ǩԼʧ�ܣ�����ϵ��Ǯ��");
                session.setAttribute("flag", "0");

//                msg = "�������뻹δ��ɣ����ڿ�Ǯ��ǩԼʧ�ܣ�����ϵ��Ǯ��";
//                flag = "0";
//
                rtnOk = 1;
                rtnUrl = KQ_mesUrl + "?msg=false!";
//                rtnUrl = KQ_mesUrl + "?msgkq=" + msg + "&funcdel=" + funcdel + "&isback=" + isback + "&flag=" + flag + "&msg=false!";
                break;
        }
    } else {
        session.setAttribute("msg", "�������뻹δ��ɣ����ڿ�Ǯ��ǩԼ��֤ʧ�ܣ�����ϵ���ǣ�");
        session.setAttribute("flag", "0");

//        msg = "�������뻹δ��ɣ����ڿ�Ǯ��ǩԼ��֤ʧ�ܣ�����ϵ���ǣ�";
//        flag = "0";
//
        rtnOk = 1;
        rtnUrl = KQ_mesUrl + "?msg=error!";
//        rtnUrl = KQ_mesUrl + "?msgkq=" + msg + "&funcdel=" + funcdel + "&isback=" + isback + "&flag=" + flag + "&msg=error!";
    }
//    System.out.println("rtnUrl = " + rtnUrl);
//    response.sendRedirect("/showinfo.jsp");
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


//���±������Ǯ�����������ṩ��Ҫ�ض���ĵ�ַ
%>
<%--<result>1</result><redirecturl>NA</redirecturl>--%>
<result><%=rtnOk%></result><redirecturl><%=rtnUrl%></redirecturl>

