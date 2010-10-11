package zt.cms.xf.newcms.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import zt.cms.xf.gateway.NewCmsManager;
import zt.cms.xf.newcms.domain.T100101.*;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
public class T100101CTL {

    public final static void main(String[] args) throws Exception {

        T100101CTL ctl = new T100101CTL();
        ctl.start();
/*        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("http://10.143.19.106:10002/LoanSysPortal/CMSServlet");

        // Execute HTTP request
        System.out.println("executing request " + httppost.getURI());

        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T100101Request.class);
        xstream.processAnnotations(T100101Response.class);


        T100101Request request = new T100101Request();
        //查询类交易
        request.setStdmsgtype("0100");
        //交易码
        request.setStd400trcd("100101");

        request.setStd400aqid("3");
        request.setStd400tlno("teller");

        request.setStdlocdate("20101010");
        request.setStdloctime("153000");

        request.setStdtermtrc("1");

        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        System.out.println(strXml);

        StringEntity xml = new StringEntity(strXml, "GBK");
        httppost.setEntity(xml);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);

        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");


        T100101Response response = (T100101Response) xstream.fromXML(responseBody);

        System.out.println(response);


        httpclient.getConnectionManager().shutdown();*/
    }

    public void start(){
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T100101Request.class);
        xstream.processAnnotations(T100101Response.class);


        T100101Request request = new T100101Request();

        request.initHeader("0100","100101","3");

        //查询 房贷/消费信贷（1/2） 数据
        request.setStdcxlx("1");

/*
        T100101RequestRecord reqRecord = new T100101RequestRecord();
        //查询 房贷/消费信贷（1/2） 数据
        reqRecord.setStdcxlx("2");

        T100101RequestList reqList = new T100101RequestList();
        reqList.add(reqRecord);
        request.setBody(reqList);
*/


        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        System.out.println(strXml);

        //发送请求
        NewCmsManager ncm =  new NewCmsManager();
        String responseBody =  ncm.doPostXml(strXml);

        T100101Response response = (T100101Response) xstream.fromXML(responseBody);

        uploadCutpayResultBatch(response.getBody().getContent());

    }

    /**
     * 向信贷服务器批量上传银行扣款结果
     */
    private void  uploadCutpayResultBatch(List<T100101ResponseRecord> records) {

        int count=0;

        //List<T100102RequestRecord> recordsT012 = new ArrayList();
        T100102RequestList t012 = new T100102RequestList();

        for (T100101ResponseRecord record :records){
            System.out.println(record.getStdjjh()+ " " + record.getStdqch() + " " + record.getStdkhmc()+ " " + record.getStdjhhkr());
            count++;

            T100102RequestRecord recordT102 = new T100102RequestRecord();
            recordT102.setStdjjh(record.getStdjjh());
            recordT102.setStdqch(record.getStdqch());
            recordT102.setStdjhkkr(record.getStdjhhkr());
            //1-成功 2-失败
            recordT102.setStdkkjg("2");

            t012.add(recordT102);
        }

        T100102CTL ctlT102 = new T100102CTL();
        ctlT102.start(t012);

        System.out.println("========"+count);

    }
}
