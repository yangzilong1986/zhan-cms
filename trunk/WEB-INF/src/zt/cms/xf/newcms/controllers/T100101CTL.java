package zt.cms.xf.newcms.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.gateway.NewCmsManager;
import zt.cms.xf.newcms.domain.T100101.T100101Request;
import zt.cms.xf.newcms.domain.T100101.T100101Response;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;

import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "T100101")
//@SessionScoped
public class T100101CTL implements java.io.Serializable {

    private Log logger = LogFactory.getLog(this.getClass());
    private T100101ResponseRecord responseRecord = new T100101ResponseRecord() ;
    List<T100101ResponseRecord> responseFDList = new ArrayList();
    List<T100101ResponseRecord> responseXFList = new ArrayList();


    public T100101ResponseRecord getResponseRecord() {
        return responseRecord;
    }

    public void setResponseRecord(T100101ResponseRecord responseRecord) {
        this.responseRecord = responseRecord;
    }

    public List<T100101ResponseRecord> getResponseFDList() {
        return responseFDList;
    }

    public void setResponseFDList(List<T100101ResponseRecord> responseFDList) {
        this.responseFDList = responseFDList;
    }

    public List<T100101ResponseRecord> getResponseXFList() {
        return responseXFList;
    }

    public void setResponseXFList(List<T100101ResponseRecord> responseXFList) {
        this.responseXFList = responseXFList;
    }

    public final static void main(String[] args) throws Exception {

        T100101CTL ctl = new T100101CTL();
        ctl.start("1");

    }

    public String test() {
        return "about.xhtml";
    }

    public List<T100101ResponseRecord> getAllFDRecords() {
        //��ѯ ����/�����Ŵ���1/2�� ����
        setResponseFDList(start("1"));
        return this.getResponseFDList();
    }

    public List<T100101ResponseRecord> getAllXFRecords() {
        //��ѯ ����/�����Ŵ���1/2�� ����
        setResponseXFList(start("2"));
        return this.getResponseXFList();

    }

    public String query() {
        setResponseFDList(start("1"));
        setResponseXFList(start("2"));

         return null;
    }

    /**
     * @param systemcode Ҫ��ѯ��ϵͳ��    ����/�����Ŵ���1/2�� ����
     * @return
     */
    public List<T100101ResponseRecord> start(String systemcode) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T100101Request.class);
        xstream.processAnnotations(T100101Response.class);

        T100101Request request = new T100101Request();

        request.initHeader("0100", "100101", "3");

        //��ѯ ����/�����Ŵ���1/2�� ����
        request.setStdcxlx(systemcode);
        int pkgcnt = 100;
        int startnum = 1;
        request.setStdymjls(String.valueOf(pkgcnt));
        //request.setStdqsjls("1");

        NewCmsManager ncm = new NewCmsManager();

        List<T100101ResponseRecord> responseList = new ArrayList();
        int totalcount = processTxn(responseList, ncm, xstream, request, pkgcnt, startnum);
        logger.info("received list zise:" + responseList.size());
        if (totalcount != responseList.size()) {
            logger.error("��ȡ�������ݱ�������Ӧ�ձ�����" + responseList.size() + "ʵ�ձ�����" + totalcount);
            throw new RuntimeException("��ȡ�������ݱ�������.");
        }
        return responseList;
    }

    /**
     * �ݹ��ȡ����������
     *
     * @param responseList
     * @param ncm
     * @param xstream
     * @param request
     * @param pkgcnt
     * @param startnum
     * @return
     */
    public int processTxn(List<T100101ResponseRecord> responseList,
                          NewCmsManager ncm, XStream xstream, T100101Request request,
                          int pkgcnt, int startnum) {
//        T100101Request request = new T100101Request();

//        request.initHeader("0100","100101","3");

        //��ѯ ����/�����Ŵ���1/2�� ����
//        request.setStdcxlx("1");
//        int pkgcnt = 50;
//        request.setStdymjls(String.valueOf(pkgcnt));
        request.setStdqsjls(String.valueOf(startnum));


        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        //System.out.println(strXml);

        //��������
        String responseBody = ncm.doPostXml(strXml);

        T100101Response response = (T100101Response) xstream.fromXML(responseBody);

        //ͷ���ܼ�¼��
        String std400acur = response.getStd400acur();
        if (std400acur == null || std400acur == "") {
            std400acur = "0";
        }
        int totalcount = Integer.parseInt(std400acur);


        if (totalcount == 0) {

        } else {
            List<T100101ResponseRecord> tmpList = response.getBody().getContent();

            int currCnt = tmpList.size();
            logger.info(currCnt);
            logger.info("totalcount:" + totalcount + " currCnt:" + currCnt + " startnum:" + startnum);

            //���������list��
            for (T100101ResponseRecord record : tmpList) {
                responseList.add(record);
            }

            //һ���������Դ�����
            if (totalcount > pkgcnt) {
                startnum += pkgcnt;
                if (startnum <= totalcount) {
                    processTxn(responseList, ncm, xstream, request, pkgcnt, startnum);
                }
            }
        }


        //uploadCutpayResultBatch(response.getBody().getContent());
        return totalcount;
    }

    /**
     * ���Ŵ������������ϴ����пۿ���
     */
    private void uploadCutpayResultBatch(List<T100101ResponseRecord> records) {

        int count = 0;

        //List<T100102RequestRecord> recordsT012 = new ArrayList();
        T100102RequestList t012 = new T100102RequestList();

        for (T100101ResponseRecord record : records) {
            System.out.println(record.getStdjjh() + " " + record.getStdqch() + " " + record.getStdkhmc() + " " + record.getStdjhhkr());
            count++;

            T100102RequestRecord recordT102 = new T100102RequestRecord();
            recordT102.setStdjjh(record.getStdjjh());
            recordT102.setStdqch(record.getStdqch());
            recordT102.setStdjhkkr(record.getStdjhhkr());
            //1-�ɹ� 2-ʧ��
            recordT102.setStdkkjg("2");

            t012.add(recordT102);
        }

        T100102CTL ctlT102 = new T100102CTL();
        ctlT102.start(t012);

        System.out.println("========" + count);

    }

/*
       public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " );
        message.setDetail("Item index: " );

        FacesContext.getCurrentInstance().addMessage(null, message);  
    }
*/

}
