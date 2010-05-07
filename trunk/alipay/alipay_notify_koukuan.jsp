<%
    /*
     ���ƣ���������з�����֪ͨҳ��
     ���ܣ�������֪ͨ���أ�������ֵ���������Ƽ�ʹ��
     �汾��2.0
     ���ڣ�2008-12-31
     ���ߣ�֧������˾���۲�����֧���Ŷ�
     ��ϵ��0571-26888888
     ��Ȩ��֧������˾
      */

    /*
    �����Ŵ�ϵͳֻʹ�ñ��첽�ӿ�
    ��ע�⴦����ε���������ۿ�ɹ�ʱ�жϵ�ǰ�ʵ�״̬������Ϊ�ۿ�ɹ������ٽ��пۿ�ɹ�����
     */

%>
<%@ page language="java" contentType="text/html; charset=GBK"  pageEncoding="GBK" %>
<%@ page import="com.alipay.util.CheckURL" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="zt.cms.xf.account.BillsManager" %>
<%@ page import="zt.cms.xf.common.dto.XfactcutpaydetlPk" %>
<%@ page import="zt.cms.xf.common.dto.Xfactcutpaydetl" %>
<%@ page import="zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory" %>
<%@ page import="zt.cms.xf.common.dao.XfactcutpaydetlDao" %>
<%
    Log logger = LogFactory.getLog("receice_koukuan.jsp");


    //String partner = AlipayConfig.partnerID; //partner�������id��������д��
    //String privateKey = AlipayConfig.key; //partner �Ķ�Ӧ���װ�ȫУ���루������д��
    String partner = PropertyManager.getProperty("ZFB_partnerID"); //partner�������id��������д��
    String privateKey = PropertyManager.getProperty("ZFB_key"); //partner �Ķ�Ӧ���װ�ȫУ���루������д��

    //**********************************************************************************
    //�������������֧��https����������ʹ��http����֤��ѯ��ַ
    /*ע�������ע�ͣ�����ڲ��Ե�ʱ����response���ڿ�ֵ��������뽫����һ��ע�ͣ�������һ����֤���ӣ������鱾�ض˿ڣ�
       �뵲��80����443�˿�*/
    //String alipayNotifyURL = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify";
    String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
            + "partner="
            + partner
            + "&notify_id="
            + request.getParameter("notify_id");
    //**********************************************************************************

    //��ȡ֧����ATN���ؽ����true����ȷ�Ķ�����Ϣ��false ����Ч��
    String responseTxt = CheckURL.check(alipayNotifyURL);
    System.out.println("---responseTxt----" + responseTxt);
    Map params = new HashMap();
    //���POST �����������õ��µ�params��
    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
        String name = (String) iter.next();
        String[] values = (String[]) requestParams.get(name);
        String valueStr = "";
        for (int i = 0; i < values.length; i++) {
            valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
        }
//        System.out.println("-------" + name + "--" + valueStr);
        logger.info("-------" + name + "--" + valueStr);
        //����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת���������Ѿ�ʹ�ã�
        valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
        params.put(name, valueStr);
    }

    String mysign = com.alipay.util.SignatureHelper.sign(params,  privateKey);

    logger.info("===֧�������صĴ�����Ϊ��" +request.getParameter("orders"));

    //if (mysign.equals(request.getParameter("sign"))&& responseTxt.equals("true")) {
    if (mysign.equals(request.getParameter("sign")) && responseTxt.equals("true")) {

        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        Xfactcutpaydetl xfactcutpaydetl = null;

        String journalno = request.getParameter("out_order_no");
//        String dealtime = request.getParameter("");
        String dealTime = null;


        xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
        XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);
        if (dealTime == null) {
            dealTime = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        } else {
            dealTime = dealTime.substring(0, 8);
        }
        BillsManager bm = new BillsManager(dealTime);

        /*�����ڲ�ͬ״̬�»�ȡ������Ϣ�������̻����ݿ�ʹ����ͬ��*/
        if (request.getParameter("is_success") == "T") {
            //�ȴ���Ҹ���
            //���������д�����ݴ���
            boolean temp = false;
            ConnectionManager manager = ConnectionManager.getInstance();

//            String sta = manager.getCellValue("APPSTATUS", "XFAPP", "seqno='" + request.getParameter("out_sign_no") + "'");
//            sta = (sta == null) ? "0" : sta;
//
//            if (Float.parseFloat(sta) < 1 && Float.parseFloat(sta) != 0) {
//                String sql1 = "update XFAPP " +
//                        "set APPSTATUS='1',CUSTOMER_CODE='" + request.getParameter("customer_code") + "',SIGN_ACCOUNT_NO='" + request.getParameter("sign_account_no") + "' " +
//                        "where seqno='" + request.getParameter("out_sign_no") + "'";
//                temp = manager.ExecCmd(sql1);
//            }
//
//            if (temp) {
//                System.out.println("XFAPP seqno = " + request.getParameter("out_sign_no"));
//                out.println("success"); //ע��һ��Ҫ���ظ�֧����һ���ɹ�����Ϣ
//            }
        }
    } else {
        out.println("fail" + "<br>");
        //��ӡ���յ���Ϣ�ȶ�sign�ļ������ʹ�������sign�Ƿ�ƥ��
        out.println(mysign + "--------------------" + request.getParameter("sign") + "<br>");

    }
%>
