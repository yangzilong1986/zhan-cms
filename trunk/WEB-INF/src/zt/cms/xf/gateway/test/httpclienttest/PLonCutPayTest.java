package zt.cms.xf.gateway.test.httpclienttest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import zt.cms.xf.gateway.test.httpclienttest.T100102.T100102Request;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
public class PLonCutPayTest {

    public final static void main(String[] args) throws Exception {

        XStream xstream = new XStream(new DomDriver());

        TxPkgHeader header = new TxPkgHeader();
        header.setStdmsgtype("0100");
        header.setStd400trcd("100101");
        header.setStd400aqid("3");
        header.setStd400tlno("teller");

        header.setStdlocdate("20100827");
        header.setStdloctime("153000");

        header.setStdtermtrc("1");


        T100102Request req = new T100102Request();
        req.setHead(header);

        req.setStdjjh("jiejuhao");
        req.setStdkkjg("1");
        req.setStdjhkkr("20100827");


//        xstream.alias("ROOT type=\"request\"",TxPkgHeader.class);
        xstream.alias("ROOT", TxPkgHeader.class);
        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(header);

        System.out.println(strXml);


        String rtnstr = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<ROOT><stdmsgtype>0100</stdmsgtype><std400trcd>100101</std400trcd><stdprocode/><std400aqid>3</std400aqid><stdmercno/><stdtermtyp/><stdtermid/><std400tlno>teller</std400tlno><stdpriacno/><stdpindata/><stdlocdate>20100827</stdlocdate><stdloctime>153000</stdloctime><stdtermtrc>1</stdtermtrc><std400autl/><stdauthid/><std400aups/><std400trdt/><stdrefnum>0</stdrefnum><stdsetdate/><std400trno/><std400acur>00</std400acur><std400mgid>AAAAAAA</std400mgid></ROOT>";


//         TxPkgHeader tt = (TxPkgHeader)xstream.fromXML(rtnstr);

    }

}
