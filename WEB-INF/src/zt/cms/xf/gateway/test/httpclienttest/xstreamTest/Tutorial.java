package zt.cms.xf.gateway.test.httpclienttest.xstreamTest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-10
 * Time: 15:28:25
 * To change this template use File | Settings | File Templates.
 */
public class Tutorial {
    public static void main(String[] args) {
//        XStream stream = new XStream();
        XStream stream = new XStream(new DomDriver());

		stream.processAnnotations(RendezvousMessage.class);
		RendezvousMessage msg = new RendezvousMessage(15, "firstPart","secondPart","aaa");

        String xml = stream.toXML(msg);
		System.out.println(xml);

        //
        String str = "<message>\n" +
                "  <type>25</type>\n" +
                "  <content class=\"java.util.Arrays$ArrayList\">\n" +
                "    <a class=\"string-array\">\n" +
                "      <string>firstPart</string>\n" +
                "      <string>secondPart</string>\n" +
                "      <string>aaa</string>\n" +
                "    </a>\n" +
                "  </content>\n" +
                "</message>";


         RendezvousMessage rrr = (RendezvousMessage)stream.fromXML(xml);
        System.out.println(rrr);
    }
}
