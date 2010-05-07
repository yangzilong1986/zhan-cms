<%@page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.bill99.encrypt.MD5Util" %>
<%@ page import="com.zt.util.PropertyManager" %>
<%@ page import="zt.platform.cachedb.ConnectionManager" %>
<%
    /**
     * @Description: 快钱快易付业务授权接口范例
     * @Copyright (c) 上海快钱信息服务有限公司
     * @version 2.0
     */
    System.out.println("--快钱--");
    //快钱通知地址
    String KQ_mesUrl = PropertyManager.getProperty("KQ_mesUrl");
//获取人民币网关账户号
    String merchantAcctId = (String) request.getParameter("merchantAcctId").trim();
//String merchantAcctId= PropertyManager.getProperty("KQ_merchantAcctId");

//设置人民币网关密钥
///区分大小写
    String key = PropertyManager.getProperty("KQ_key");

//获取网关版本.固定值
///快钱会根据版本号来调用对应的接口处理程序。
///本代码版本号固定为v2.0
    String version = (String) request.getParameter("version").trim();

//签名类型.固定值
///1代表MD5签名
///当前版本固定为1
    String signType = (String) request.getParameter("signType").trim();

//申请编号
    String requestId = (String) request.getParameter("requestId").trim();

//获取申请提交时间
///获取商户提交订单时的时间.14位数字。年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
///如：20080101010101
    String requestTime = (String) request.getParameter("requestTime").trim();

//获取在快钱交易时间
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
///10代表成功 11代表失败
    String dealResult = (String) request.getParameter("dealResult").trim();

//快易付协议号
    String debitProtocolId = request.getParameter("debitProtocolId").trim();

//获取错误代码
///详细见文档错误代码列表
    String errCode = (String) request.getParameter("errCode").trim();

//获取加密签名串
    String signMsg = (String) request.getParameter("signMsg").trim();


//生成加密串。必须保持如下顺序。
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


    //初始化结果及地址
    int rtnOk = 0;
    String rtnUrl = "";
    String msg = "";
    String funcdel = "";
    String isback = "1";
    String flag = "1";

//商家进行数据处理，并跳转会商家显示支付结果的页面
///首先进行签名字符串验证
    if (signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())) {

        ///接着进行支付结果判断
        switch (Integer.parseInt(dealResult)) {
            case 10:

                //*
                // 商户网站逻辑处理，比方更新订单支付状态为成功
                // 特别注意：只有signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())，且payResult=10，才表示支付成功！同时将订单金额与提交订单前的订单金额进行对比校验。
                //*
                String sql1 = "update XFAPP " +
                        "set APPSTATUS='1',CUSTOMER_CODE='" + debitProtocolId + "' " +
                        "where seqno='" + requestId + "'";

                ConnectionManager manager = ConnectionManager.getInstance();
                boolean temp = manager.ExecCmd(sql1);

                if (temp) {
                    session.setAttribute("msg", "您的申请已提交成功，请在单击确定后，连接您的打印机，选择您的申请单打印并签署姓名，连同您的证明文件复印件一同寄送给我们，审核通过后我们将与您取得联系！");
                    session.setAttribute("funcdel", "window.opener.document.getElementById('print').click();pageWinClose();");
                    session.setAttribute("isback", "0");

//                    msg = "您的申请已提交成功，请在单击确定后，连接您的打印机，选择您的申请单打印并签署姓名，连同您的证明文件复印件一同寄送给我们，审核通过后我们将与您取得联系！";
//                    funcdel = "window.opener.document.getElementById('print').click();pageWinClose();";
//                    isback = "0";
                    rtnOk = 1;
                    rtnUrl = KQ_mesUrl + "?msg=success!";

                } else {
                    session.setAttribute("msg", "您的申请已提交成功，但是申请单状态有误，请联系我们！");
                    session.setAttribute("flag", "0");

                    rtnOk = 1;
                    rtnUrl = KQ_mesUrl + "?msg=success part!";
//                    msg = "您的申请已提交成功，但是申请单状态有误，请联系我们！";
//                    flag = "0";
                }

                //报告给快钱处理结果，并提供将要重定向的地址。
//                rtnOk = 1;
//                rtnUrl = KQ_mesUrl + "?msgkq=" + msg + "&funcdel=" + funcdel + "&isback=" + isback + "&flag=" + flag + "&msg=success!";
                break;

            default:
                session.setAttribute("msg", "您的申请还未完成，您在快钱的签约失败，请联系快钱！");
                session.setAttribute("flag", "0");

//                msg = "您的申请还未完成，您在快钱的签约失败，请联系快钱！";
//                flag = "0";
//
                rtnOk = 1;
                rtnUrl = KQ_mesUrl + "?msg=false!";
//                rtnUrl = KQ_mesUrl + "?msgkq=" + msg + "&funcdel=" + funcdel + "&isback=" + isback + "&flag=" + flag + "&msg=false!";
                break;
        }
    } else {
        session.setAttribute("msg", "您的申请还未完成，您在快钱的签约验证失败，请联系我们！");
        session.setAttribute("flag", "0");

//        msg = "您的申请还未完成，您在快钱的签约验证失败，请联系我们！";
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


//以下报告给快钱处理结果，并提供将要重定向的地址
%>
<%--<result>1</result><redirecturl>NA</redirecturl>--%>
<result><%=rtnOk%></result><redirecturl><%=rtnUrl%></redirecturl>

