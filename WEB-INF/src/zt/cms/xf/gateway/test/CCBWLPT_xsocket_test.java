package zt.cms.xf.gateway.test;

import org.xsocket.connection.BlockingConnection;
import org.xsocket.connection.IBlockingConnection;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-10-30
 * Time: 16:08:39
 * To change this template use File | Settings | File Templates.
 */
public class CCBWLPT_xsocket_test {
    public static void main(String... args) throws Exception {
//        if (args.length != 3) {
//            System.out.println("usage org.xsocket.stream.BlockingClient <host> <port> <path>");
//            System.exit(-1);
//        }

        new CCBWLPT_xsocket_test().call("192.168.91.46", 9888);
    }


    public void call(String host, int port) throws IOException {

        IBlockingConnection connection = null;
        try {
            InetAddress ia= InetAddress.getByName(host);
            connection = new BlockingConnection(ia, port, 5*1000);
            connection.write("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><TX><REQUEST_SN>2009112800003</REQUEST_SN><MERCHANTID>105370257320044</MERCHANTID><USER_ID>370200018079470-001</USER_ID><PASSWORD>000000</PASSWORD><TX_CODE>520200</TX_CODE><TX_INFO><AUTHID>1234567890</AUTHID><ORDERID>2009112800001</ORDERID><BRANCHID>371000000</BRANCHID><CURCODE>01</CURCODE><PAYMENT>449.94</PAYMENT><POSID>100000192</POSID><REM1>1</REM1><REM2>1</REM2><PUB32>30819c300d06092a864886f70d0101</PUB32><MAC></MAC></TX_INFO><SIGN_INFO></SIGN_INFO><SIGNCERT></SIGNCERT></TX>");
            int bodyLength = 0;

            // print header
            String line = null;
            do {
//                line = connection.readStringByDelimiter("\r\n").getBytes("GB2312").toString().trim();
                line = new String(connection.readStringByDelimiter("\r\n").getBytes("GBK")).trim();
//                if (line.startsWith("Content-Length:")) {
//                    bodyLength = new Integer(line.substring("Content-Length:".length(), line.length()).trim());
//                }

                if (line.length() > 0) {
                    System.out.println(line);
                    if ("</TX>".equals(line)) {
                        break;
                    }
                }

            } while (line.length() > 0 );


            // print body
//            if (bodyLength > 0) {
//                System.out.println(connection.readStringByLength(bodyLength));
//            }

        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }

}
