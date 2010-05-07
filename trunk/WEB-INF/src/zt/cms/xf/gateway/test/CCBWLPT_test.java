package zt.cms.xf.gateway.test;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;

import java.net.InetSocketAddress;

import zt.cms.xf.gateway.test.NetCatProtocolHandler;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-10-30
 * Time: 11:00:11
 * To change this template use File | Settings | File Templates.
 */
public class CCBWLPT_test {

    //WLPT≤‚ ‘ª∑æ≥µÿ÷∑
    static private String strUrl = "192.168.91.46";
    static private int iPort = 9888;

    public static void main(String[] args) throws Exception {
        // Create TCP/IP connector.
        NioSocketConnector connector = new NioSocketConnector();

        // Set connect timeout.
        connector.setConnectTimeoutMillis(30 * 1000L);

//        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

         connector.getFilterChain().addLast("logger", new LoggingFilter());
        // Start communication.
        connector.setHandler(new NetCatProtocolHandler());


        ConnectFuture cf = connector.connect(new InetSocketAddress(strUrl, iPort));
        // Wait for the connection attempt to be finished.
        cf.awaitUninterruptibly();
        IoSession session = cf.getSession();
        
        session.getCloseFuture().awaitUninterruptibly();

        connector.dispose();
    }

}
