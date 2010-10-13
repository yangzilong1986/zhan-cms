package zt.cms.xf.newcms.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.gateway.NewCmsManager;
import zt.cms.xf.newcms.domain.T201002.T201002Request;
import zt.cms.xf.newcms.domain.T201002.T201002Response;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
public class T201002CTL {
    private Log logger = LogFactory.getLog(this.getClass());

    public final static void main(String[] args) throws Exception {

        T201002CTL ctl = new T201002CTL();
        ctl.start();

    }

    public T201002Response getAllRecords() {
        return start();
    }

    public T201002Response start() {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T201002Request.class);
        xstream.processAnnotations(T201002Response.class);

        T201002Request request = new T201002Request();

        request.initHeader("0100", "201002", "3");

        //��ѯ ����/�����Ŵ���1/2�� ����
//        request.setStdcxlx("2");
//        int pkgcnt = 100;
//        int startnum = 1;
//        request.setStdymjls(String.valueOf(pkgcnt));
        //request.setStdqsjls("1");

        request.setStdsqdh("3708291990022332560001");

        NewCmsManager ncm = new NewCmsManager();
        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        //System.out.println(strXml);

        //��������
        String responseBody = ncm.doPostXml(strXml);

        T201002Response response = (T201002Response) xstream.fromXML(responseBody);

        return response;
    }

}
