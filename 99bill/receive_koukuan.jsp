<%@page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="zt.platform.utils.Debug" %>
<%@ page import="java.util.Date" %>
<%@ page import="zt.cms.xf.common.dao.XfactcutpaydetlDao" %>
<%@ page import="zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory" %>
<%@ page import="zt.cms.xf.common.dto.Xfactcutpaydetl" %>
<%@ page import="zt.cms.xf.common.dto.XfactcutpaydetlPk" %>
<%@ page import="zt.cms.xf.common.constant.XFBillStatus" %>
<%@ page import="zt.cms.xf.gateway.Bill99Manager" %>
<%@ page import="zt.cms.xf.account.BillsManager" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%
    /**
     * @Description: 快钱快易付扣款网关接口范例
     * @Copyright (c) 上海快钱信息服务有限公司
     * @version 2.0
     */
    Log log = LogFactory.getLog("receice_koukuan.jsp");

    System.out.println("\n" + new Date());
    System.out.println("99Bill:: ------------koukuan receive.jsp--------------" + new Date());

    log.debug("99Bill:: ------------koukuan receive.jsp--------------" + new Date());


    int rtnOk = 0;
    String rtnUrl = "";

    //快钱通知地址
    String KQ_KK_mesUrl = PropertyManager.getProperty("KQ_KK_mesUrl");

    System.out.println("===KQ_KK_mesUrl=" + KQ_KK_mesUrl);

    try {


//获取人民币网关账户号
        String merchantAcctId = (String) request.getParameter("merchantAcctId").trim();

//设置人民币网关密钥
///区分大小写
//String key="1234567891234567";
        String key = PropertyManager.getProperty("KQ_key");

//获取网关版本.固定值
///快钱会根据版本号来调用对应的接口处理程序。
///本代码版本号固定为v2.0
        String version = (String) request.getParameter("version").trim();

//签名类型.固定值
///1代表MD5签名
///当前版本固定为1
        String signType = (String) request.getParameter("signType").trim();

//获取商户订单号
        String orderId = (String) request.getParameter("orderId").trim();

//获取订单提交时间
///获取商户提交订单时的时间.14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///如：20080101010101
        String orderTime = (String) request.getParameter("orderTime").trim();

//获取原始订单金额
///订单提交到快钱时的金额，单位为分。
///比方2 ，代表0.02元
        String orderAmount = (String) request.getParameter("orderAmount").trim();

//产品名称
///与商户提交时一致
        String productName = (String) request.getParameter("productName").trim();

//产品代码
///与商户提交时一致
        String productId = (String) request.getParameter("productId").trim();

//获取银行交易号
///如果使用银行卡支付时，在银行的交易号。如不是通过银行支付，则为空
        String bankDealId = (String) request.getParameter("bankDealId").trim();

//获取在快钱处理完成时间
///14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///如；20080101010101
        String dealTime = (String) request.getParameter("dealTime").trim();

//获取交易手续费
///单位为分
///比方 2 ，代表0.02元
        String fee = (String) request.getParameter("fee").trim();

//获取扩展字段1
        String ext1 = (String) request.getParameter("ext1").trim();

//获取扩展字段2
        String ext2 = (String) request.getParameter("ext2").trim();

//获取处理结果
///10代表 成功11代表 失败
///00代表 下订单成功（仅对电话银行支付订单返回）;01代表 下订单失败（仅对电话银行支付订单返回）
        String dealResult = (String) request.getParameter("dealResult").trim();

//获取错误代码
///详细见文档错误代码列表
        String errCode = (String) request.getParameter("errCode").trim();
        log.debug("=== errCode = " + errCode);

//获取加密签名串
        String signMsg = (String) request.getParameter("signMsg").trim();


//生成加密串。必须保持如下顺序。
        String merchantSignMsgVal = "";
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
        log.debug("===99Bill: receive.jsp===" + merchantSignMsgVal);

        String merchantSignMsg = MD5Util.md5Hex(merchantSignMsgVal.getBytes("gb2312")).toUpperCase();

//商家进行数据处理，并跳转会商家显示支付结果的页面
///首先进行签名字符串验证


        if (signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())) {

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
            Xfactcutpaydetl xfactcutpaydetl = null;

            String journalno = orderId;
            xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk(journalno);
            if (dealTime == null) {
                dealTime = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            } else {
                dealTime = dealTime.substring(0, 8);
            }
            BillsManager bm = new BillsManager(dealTime);

            log.info("===快钱返回的处理结果为：" + dealResult + "   orderId=" + orderId);
            ///接着进行支付结果判断
            switch (Integer.parseInt(dealResult)) {
                case 10:
                    //*
                    // 商户网站逻辑处理，比方更新订单支付状态为成功
                    // 特别注意：只有signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())，且dealResult=10，才表示支付成功！同时将订单金额与提交订单前的订单金额进行对比校验。
                    //*
                    //报告给快钱处理结果，并提供将要重定向的地址。
                    rtnOk = 1;
                    rtnUrl = KQ_KK_mesUrl + "?msg=success!";

                    //置扣款成功标志及进行账务处理
                    //须避免重复调用
                    if (journalno == null) {
                        log.error("快钱返回序列号为NULL， journalno==null");
                        //置失败
                    } else {
                        //扣款成功
                        log.info("===快钱代扣成功:" + orderId);
                        //if (XFBillStatus.BILLSTATUS_CHARGEDOFF.equals(xfactcutpaydetl.getBillstatus())) {
                        if (XFBillStatus.BILLSTATUS_SEND_SUCCESS.equals(xfactcutpaydetl.getBillstatus())) {
                            bm.setCutpayDetlPaidupSuccess(journalno);
                            log.info("===快钱代扣回写状态成功:" + orderId);
                        } else {
                            log.error("===此笔代扣记录状态非BILLSTATUS_SEND_SUCCESS，状态改写失败！" + orderId);
                        }
                    }
                    break;

                default:
                    rtnOk = 1;
                    rtnUrl = KQ_KK_mesUrl + "?msg=fail!";
                    if (journalno == null) {
                        //TODO:log
                        //置失败
                    } else {
                        //if (XFBillStatus.BILLSTATUS_CHARGEDOFF.equals(xfactcutpaydetl.getBillstatus())) {
                        if (XFBillStatus.BILLSTATUS_SEND_SUCCESS.equals(xfactcutpaydetl.getBillstatus())) {
                            if (errCode.length() == 0) {
                                errCode = "快钱未返回确定的错误代码。" + "  URL=" + merchantSignMsgVal;
                            } else {
                                errCode = "快钱返回的错误代码=" + errCode + "  URL=" + merchantSignMsgVal;
                            }
                            bm.setCutpayDetlPaidupFail(journalno, errCode);
                        } else {
                            log.info("===快钱代扣回写状态有误，此序列号:" + orderId + " 对应的帐单状态有误");
                        }
                    }
                    break;

            }

        } else {
            rtnOk = 1;
            rtnUrl = KQ_KK_mesUrl + "?msg=error!";
            System.out.println("Bill99 MD5出现问题！");
            System.out.println("Remote MD5=" + signMsg.toUpperCase());
            System.out.println("Local MD5=" + merchantSignMsg.toUpperCase());

//            response.sendRedirect("http://localhost/test/bill99/koukuan/show.jsp?msg=error!");
        }

    } catch (Exception e) {
        Debug.debug(e);
        rtnOk = 1;
        rtnUrl = KQ_KK_mesUrl + "?msg=error!";
        log.error("Bill99 扣款出现问题！", e);
%>

<result><%=rtnOk%>
</result>
<redirecturl><%=rtnUrl%>
</redirecturl>

<%
    }
%>

<%!
    //功能函数。将变量值不为空的参数组成字符串
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
    //功能函数。将变量值不为空的参数组成字符串。结束

%>
<%
    System.out.println("-----------");
    System.out.println("rtnOk=" + rtnOk + "  rtnUrl=" + rtnUrl);
%>
<%--<result>1</result><redirecturl>NA</redirecturl>--%>
<result><%=rtnOk%>
</result>
<redirecturl><%=rtnUrl%>
</redirecturl>
