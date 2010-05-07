package zt.cms.xf.gateway.test;
     import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-10-30
 * Time: 11:03:42
 * To change this template use File | Settings | File Templates.
 */
public class NetCatProtocolHandler extends IoHandlerAdapter {
    @Override
    public void sessionOpened(IoSession session) {
        // Set reader idle time to 10 seconds.
        // sessionIdle(...) method will be invoked when no data is read
        // for 10 seconds.
                System.err.println("send...");
//        session.getConfig().setIdleTime(IdleStatus.READER_IDLE, 10);
        session.write("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><TX><REQUEST_SN>2009112800003</REQUEST_SN><MERCHANTID>105370257320044</MERCHANTID><USER_ID>370200018079470</USER_ID><PASSWORD>000000</PASSWORD><TX_CODE>520200</TX_CODE><TX_INFO><AUTHID>1234567890</AUTHID><ORDERID>2009112800001</ORDERID><BRANCHID>371000000</BRANCHID><CURCODE>01</CURCODE><PAYMENT>449.94</PAYMENT><POSID>100000192</POSID><REM1>1</REM1><REM2>1</REM2><PUB32>30819c300d06092a864886f70d0101</PUB32><MAC></MAC></TX_INFO><SIGN_INFO></SIGN_INFO><SIGNCERT></SIGNCERT></TX>").awaitUninterruptibly();
        
    }

    @Override
    public void sessionClosed(IoSession session) {
        // Print out total number of bytes read from the remote peer.
        System.err.println("Total " + session.getReadBytes() + " byte(s)");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        // Close the connection if reader is idle.
        if (status == IdleStatus.READER_IDLE) {
            session.close(true);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        System.out.println("recv...");
        System.err.println("recv...");
        IoBuffer buf = (IoBuffer) message;
        // Print out read buffer content.
        while (buf.hasRemaining()) {
            System.out.print((char) buf.get());
        }
        System.out.flush();
    }


    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.close(true);
    }
}