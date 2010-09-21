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
 * ��������ƽ̨���Ӵ�����.
 * User: zhanrui
 * Date: 2009-10-11
 * Time: 14:42:06
 * To change this template use File | Settings | File Templates.
 */

/*
  �ۿ��500200���õ���ͨѶ����ʽ�������������
  �쳣������̣�
    1��ͨѶ��ʱ�����ʱ���״̬���䣬�跢���ѯ���ף�5W1002���� �����쳣���ж������ۿ��¼����
    2���鿴������Ϣ��
        1�������룺000000 ���׳ɹ�
        2������������ʧ�ܣ�д��־���������ɿۿ��
 */

public class CCBWLPTManager {
    private static Log log = LogFactory.getLog(CCBWLPTManager.class);

//    private static String merchantAcctId = PropertyManager.getProperty("CCB_merchantAcctId");

    private String merchantAcctId = "105370257320044";
    private String userId = "370200018079469-001";
    private String key = "000000";

    //todo WLPT���Ի�����ַ
    private String strUrl = "127.0.0.1";

    //SBS����������ַ TODO���ӵ������ļ���
//    private static String strUrl = "192.168.91.5";

    private int iPort = 9999;

    //TODO����ʱʱ������


    private IBlockingConnection connection;

    public void setConnection() throws IOException {
//        IBlockingConnection connection = null;
        try {
            InetAddress ia = InetAddress.getByName(strUrl);
            connection = new BlockingConnection(ia, iPort, 5 * 1000);
            connection.setIdleTimeoutMillis(30 * 1000L);
        } catch (java.net.UnknownHostException e) {
            log.error("��ַ����");
            log.error(e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("���ӳ�ʱ��");
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
            log.error("���ӹر�ʧ��" + e);
            throw new RuntimeException();
        }
    }

    public int process(Xfactcutpaydetl xfactcutpaydetl, StringBuffer returnCode, StringBuffer returnMsg) {

        //Ĭ�Ϸ���ֵΪ-1 ����ʱ���跢���ѯ����
        //rtn=0 ����ͨѶ����ɹ����Ѿ����ܵ�����ֵ
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
            log.debug("���ر��ģ�" + line);
            log.debug("���ر���=" + new ByteArrayInputStream(line.getBytes("GB2312")));
        } catch (Exception e) {
            log.error("�������ݳ��ִ���");
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

        //Ĭ�Ϸ���ֵΪ-1 ����ʱ���跢���ѯ����
        //rtn=0 ����ͨѶ����ɹ����Ѿ����ܵ�����ֵ
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
            // ����
            session.write(sendmsg).awaitUninterruptibly();
            // ����
            ReadFuture readFuture = session.read();
            if (readFuture.awaitUninterruptibly(20, TimeUnit.SECONDS)) {
                *//*               String msg = (String) readFuture.getMessage();
                               msg += (String) readFuture.getMessage();
                               // TODO ������Ϣ
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
                // ����ʱ
                rtn = -1;
            }
        } finally {
            // �Ͽ�
            session.close(true);
            session.getService().dispose();
        }

        return rtn;
    }

 */
    //�������ݿ��¼���ɷ��ͱ��ģ�XML��
    private String getSendMessage(Xfactcutpaydetl detl) {

        StringBuffer message = new StringBuffer("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>");

        message.append("<TX>");

        //����������
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
        //����ID
        message.append("<TX_CODE>520200</TX_CODE>");

        message.append("<TX_INFO>");
        //��Ȩ��
        message.append("<AUTHID>");
        message.append(detl.getCustomerCode());
//        message.append("ccb0000000218");
        message.append("</AUTHID>");
        //������
        message.append("<ORDERID>");
        //todo ?   ������= ��ͬ��+������
        message.append(detl.getJournalno());
        message.append("</ORDERID>");
        //һ���к�
        message.append("<BRANCHID>");
        message.append("371000000");
        message.append("</BRANCHID>");
        //���׻��Ҵ���
        message.append("<CURCODE>");
        message.append("01");
        message.append("</CURCODE>");
        //���׽��
        message.append("<PAYMENT>");
        message.append(detl.getPaybackamt());
//        message.append("1.01");
        message.append("</PAYMENT>");
        //������̨���
        message.append("<POSID>");
        message.append("100001200");
        message.append("</POSID>");
        //��ע
        message.append("<REM1>");
        message.append("1");
        message.append("</REM1>");
        message.append("<REM2>");
        message.append("1");
        message.append("</REM2>");
        //��Կǰ30λ
        message.append("<PUB32>");
        message.append("30819c300d06092a864886f70d0101");
        message.append("</PUB32>");
        message.append("<MAC>");
        message.append("</MAC>");

        message.append("</TX_INFO>");

        //ǩ����Ϣ
        message.append("<SIGN_INFO>");
        message.append("</SIGN_INFO>");
        //ǩ��CA��Ϣ
        message.append("<SIGNCERT>");
        message.append("</SIGNCERT>");

        message.append("</TX>");

        log.info("����������:" + message.toString());
        return message.toString();
    }
}

