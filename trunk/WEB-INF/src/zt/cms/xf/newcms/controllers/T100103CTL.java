package zt.cms.xf.newcms.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.gateway.NewCmsManager;
import zt.cms.xf.newcms.domain.T100103.T100103Request;
import zt.cms.xf.newcms.domain.T100103.T100103Response;
import zt.cms.xf.newcms.domain.T100103.T100103ResponseRecord;

import java.util.ArrayList;
import java.util.List;


/**
 * ��ǰ���� ��ȡ �����¼  �������Ŵ� ���޴��ཻ�� 2010/11/20��
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */

public class T100103CTL  extends BaseCTL implements java.io.Serializable {

    private Log logger = LogFactory.getLog(this.getClass());
    private T100103ResponseRecord responseRecord = new T100103ResponseRecord();
    List<T100103ResponseRecord> responseFDList = new ArrayList();
    List<T100103ResponseRecord> responseXFList = new ArrayList();


    public T100103ResponseRecord getResponseRecord() {
        return responseRecord;
    }

    public void setResponseRecord(T100103ResponseRecord responseRecord) {
        this.responseRecord = responseRecord;
    }

    public List<T100103ResponseRecord> getResponseFDList() {
        return responseFDList;
    }

    public void setResponseFDList(List<T100103ResponseRecord> responseFDList) {
        this.responseFDList = responseFDList;
    }

    public List<T100103ResponseRecord> getResponseXFList() {
        return responseXFList;
    }

    public void setResponseXFList(List<T100103ResponseRecord> responseXFList) {
        this.responseXFList = responseXFList;
    }

    public final static void main(String[] args) throws Exception {

        T100103CTL ctl = new T100103CTL();
        ctl.start("1");

    }

    public String test() {
        return "about.xhtml";
    }

    public List<T100103ResponseRecord> getAllFDRecords() {
        //��ѯ ����/�����Ŵ���1/2�� ����
        setResponseFDList(start("1"));
        return this.getResponseFDList();
    }

    public List<T100103ResponseRecord> getAllXFRecords() {
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
    public List<T100103ResponseRecord> start(String systemcode) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T100103Request.class);
        xstream.processAnnotations(T100103Response.class);

        T100103Request request = new T100103Request();

        request.initHeader("0100", "100103", "3");

        //��ѯ ����/�����Ŵ���1/2�� ����
        request.setStdcxlx(systemcode);
        int pkgcnt = 100;
        int startnum = 1;
        request.setStdymjls(String.valueOf(pkgcnt));
        //request.setStdqsjls("1");

        NewCmsManager ncm = new NewCmsManager();

        List<T100103ResponseRecord> responseList = new ArrayList();
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
    public int processTxn(List<T100103ResponseRecord> responseList,
                          NewCmsManager ncm, XStream xstream, T100103Request request,
                          int pkgcnt, int startnum) {
        
        //��ѯ ����/�����Ŵ���1/2�� ����
        request.setStdqsjls(String.valueOf(startnum));


        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        //System.out.println(strXml);

        //��������
        String responseBody = ncm.doPostXml(strXml);

        T100103Response response = (T100103Response) xstream.fromXML(responseBody);

        //ͷ���ܼ�¼��
        String std400acur = response.getStd400acur();
        if (std400acur == null || std400acur == "") {
            std400acur = "0";
        }
        int totalcount = Integer.parseInt(std400acur);


        if (totalcount == 0) {

        } else {
            List<T100103ResponseRecord> tmpList = response.getBody().getContent();

            int currCnt = tmpList.size();
            logger.info(currCnt);
            logger.info("totalcount:" + totalcount + " currCnt:" + currCnt + " startnum:" + startnum);

            //���������list��
            for (T100103ResponseRecord record : tmpList) {
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

}
