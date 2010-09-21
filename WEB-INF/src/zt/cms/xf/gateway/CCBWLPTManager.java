package zt.cms.xf.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xsocket.connection.BlockingConnection;
import org.xsocket.connection.IBlockingConnection;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.platform.utils.Debug;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * 建行外联平台链接处理类.
 * User: zhanrui
 * Date: 2009-10-11
 * Time: 14:42:06
 * To change this template use File | Settings | File Templates.
 */

/*
  扣款交易500200采用单笔通讯处理方式（非批量打包）
  异常处理过程：
    1、通讯超时，本笔报文状态不变，需发起查询交易（5W1002？） ，抛异常，中断其它扣款记录处理
    2、查看返回信息：
        1、返回码：000000 交易成功
        2、其他：交易失败，写日志，重新生成扣款报文
 */

public class CCBWLPTManager {
    private static Log log = LogFactory.getLog(CCBWLPTManager.class);

//    private static String merchantAcctId = PropertyManager.getProperty("CCB_merchantAcctId");

    private String merchantAcctId = "105370257320044";
    private String userId = "370200018079469-001";
    private String key = "000000";

    //todo WLPT测试环境地址
    private String strUrl = "127.0.0.1";

    //SBS生产环境地址 TODO：加到配置文件中
//    private static String strUrl = "192.168.91.5";

    private int iPort = 9999;

    //TODO：超时时间设置


    private IBlockingConnection connection;

    public void setConnection() throws IOException {
//        IBlockingConnection connection = null;
        try {
            InetAddress ia = InetAddress.getByName(strUrl);
            connection = new BlockingConnection(ia, iPort, 5 * 1000);
            connection.setIdleTimeoutMillis(30 * 1000L);
        } catch (java.net.UnknownHostException e) {
            log.error("地址错误。");
            log.error(e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("连接超时。");
            log.error(e);
            throw new IOException();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.error("链接关闭失败" + e);
            throw new RuntimeException();
        }
    }

    public int process(Xfactcutpaydetl xfactcutpaydetl, StringBuffer returnCode, StringBuffer returnMsg) {

        //默认返回值为-1 处理超时，需发起查询交易
        //rtn=0 交易通讯处理成功，已经接受到返回值
        int rtn = -1;
        String line = null;
        try {
            String sendmsg = getSendMessage(xfactcutpaydetl);

            setConnection();

            connection.write(sendmsg);
            String linetemp = null;
            do {
                linetemp = connection.readStringByDelimiter("\r\n","GB2312").trim();
                if (linetemp.length() > 0) {
//                    System.out.println(line);
                    if (line == null) {
                        line = linetemp;
                    } else {
                        line += linetemp;
                    }
                    if ("</TX>".equals(linetemp)) {
                        break;
                    }
                }
            } while (linetemp.length() > 0);
            log.debug("返回报文：" + line);
            log.debug("返回报文=" + new ByteArrayInputStream(line.getBytes("GB2312")));
        } catch (Exception e) {
            log.error("接收数据出现错误！");
            Debug.debug(e);
            throw new RuntimeException(e);
        }  finally {
            closeConnection();
        }

        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new ByteArrayInputStream(line.getBytes("GB2312")));
//            Element root = doc.getRootElement();
            Node node = doc.selectSingleNode("//TX/RETURN_CODE");
            log.info("****" + node.getName() + node.getText());
            returnCode = returnCode.append(node.getText());
            node = doc.selectSingleNode("//TX/RETURN_MSG");
            returnMsg = returnMsg.append(node.getText());
            rtn = 0;
        } catch (Exception e) {
            //TODO:
            Debug.debug(e);
            throw new RuntimeException(e);
        }

        return rtn;
    }
/*
    public int processCCBWLPT(Xfactcutpaydetl xfactcutpaydetl, StringBuffer returnCode, StringBuffer returnMsg) {

        //默认返回值为-1 处理超时，需发起查询交易
        //rtn=0 交易通讯处理成功，已经接受到返回值
        int rtn = -1;

        // Create TCP/IP connector.
        NioSocketConnector connector = new NioSocketConnector();

        // Set connect timeout.
        connector.setConnectTimeoutMillis(30 * 1000L);

        // Start communication.
//        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("GB2312"))));
//        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        SocketSessionConfig cfg = connector.getSessionConfig();
        cfg.setUseReadOperation(true);
        IoSession session = connector.connect(new InetSocketAddress(strUrl, iPort)).awaitUninterruptibly().getSession();
        try {
            String sendmsg = getSendMessage(xfactcutpaydetl);
            // 发送
            session.write(sendmsg).awaitUninterruptibly();
            // 接收
            ReadFuture readFuture = session.read();
            if (readFuture.awaitUninterruptibly(20, TimeUnit.SECONDS)) {
                *//*               String msg = (String) readFuture.getMessage();
                               msg += (String) readFuture.getMessage();
                               // TODO 处理消息
                               log.debug(msg);
                *//*
//                IoBuffer buf = (IoBuffer) readFuture.getMessage();
                IoBuffer buf = (IoBuffer) readFuture.getMessage();
                // Print out read buffer content.
                String msg = "aaa";
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                System.out.flush();

                try {
                    SAXReader reader = new SAXReader();
                    Document doc = reader.read(new ByteArrayInputStream(msg.getBytes("GB2312")));
                    Element root = doc.getRootElement();
//                    for (Iterator i = root.elementIterator(); i.hasNext();) {
//                        Element element = (Element) i.next();
//                        log.debug("===" + element.getName() + element.getData() + "---" + element.elementText(""));
//                        //System.out.print("RETURNCODE:" + element.getName()+ element.elementText("RETURN_CODE"));
//                        //System.out.println(" RETURNMSG:" + element.elementText("RETURN_MSG"));
//                    }
                    Node node = doc.selectSingleNode("//TX/RETURN_CODE");
                    log.info("****" + node.getName() + node.getText());
                    returnCode = returnCode.append(node.getText());
                    node = doc.selectSingleNode("//TX/RETURN_MSG");
                    returnMsg = returnMsg.append(node.getText());
                    rtn = 0;
                } catch (Exception e) {
                    //TODO:
                    Debug.debug(e);
                    throw new RuntimeException(e);
                }
            } else {
                // 读超时
                rtn = -1;
            }
        } finally {
            // 断开
            session.close(true);
            session.getService().dispose();
        }

        return rtn;
    }

 */
    //根据数据库记录生成发送报文（XML）
    private String getSendMessage(Xfactcutpaydetl detl) {

        StringBuffer message = new StringBuffer("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>");

        message.append("<TX>");

        //请求序列码
        message.append("<REQUEST_SN>");
        message.append(detl.getJournalno());
        message.append("</REQUEST_SN>");
        message.append("<MERCHANTID>");
        message.append(merchantAcctId);
        message.append("</MERCHANTID>");
        message.append("<USER_ID>");
        message.append(userId);
        message.append("</USER_ID>");
        message.append("<PASSWORD>");
        message.append(key);
        message.append("</PASSWORD>");
        //交易ID
        message.append("<TX_CODE>520200</TX_CODE>");

        message.append("<TX_INFO>");
        //授权号
        message.append("<AUTHID>");
        message.append(detl.getCustomerCode());
//        message.append("ccb0000000218");
        message.append("</AUTHID>");
        //定单号
        message.append("<ORDERID>");
        //todo ?   订单号= 合同号+期数？
        message.append(detl.getJournalno());
        message.append("</ORDERID>");
        //一级行号
        message.append("<BRANCHID>");
        message.append("371000000");
        message.append("</BRANCHID>");
        //交易货币代码
        message.append("<CURCODE>");
        message.append("01");
        message.append("</CURCODE>");
        //交易金额
        message.append("<PAYMENT>");
        message.append(detl.getPaybackamt());
//        message.append("1.01");
        message.append("</PAYMENT>");
        //网银柜台编号
        message.append("<POSID>");
        message.append("100001200");
        message.append("</POSID>");
        //备注
        message.append("<REM1>");
        message.append("1");
        message.append("</REM1>");
        message.append("<REM2>");
        message.append("1");
        message.append("</REM2>");
        //公钥前30位
        message.append("<PUB32>");
        message.append("30819c300d06092a864886f70d0101");
        message.append("</PUB32>");
        message.append("<MAC>");
        message.append("</MAC>");

        message.append("</TX_INFO>");

        //签名信息
        message.append("<SIGN_INFO>");
        message.append("</SIGN_INFO>");
        //签名CA信息
        message.append("<SIGNCERT>");
        message.append("</SIGNCERT>");

        message.append("</TX>");

        log.info("请求报文内容:" + message.toString());
        return message.toString();
    }
}

