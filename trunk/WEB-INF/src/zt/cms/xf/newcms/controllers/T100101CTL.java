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
import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class T100101CTL  implements java.io.Serializable{

    private Log logger = LogFactory.getLog(this.getClass());

    public final static void main(String[] args) throws Exception {

        T100101CTL ctl = new T100101CTL();
        ctl.start();

    }

    public    List <T100101ResponseRecord> getAllRecords() {
        return start();
    }
    
    public List <T100101ResponseRecord> start() {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T100101Request.class);
        xstream.processAnnotations(T100101Response.class);

        T100101Request request = new T100101Request();

        request.initHeader("0100", "100101", "3");

        //查询 房贷/消费信贷（1/2） 数据
        request.setStdcxlx("1");
        int pkgcnt = 100;
        int startnum = 1;
        request.setStdymjls(String.valueOf(pkgcnt));
        //request.setStdqsjls("1");

        NewCmsManager ncm = new NewCmsManager();

        List <T100101ResponseRecord> responseList = new ArrayList();
        int totalcount = processTxn(responseList, ncm, xstream, request, pkgcnt, startnum);
        logger.info("received list zise:" + responseList.size());
        if (totalcount !=responseList.size() ) {
            logger.error("获取还款数据笔数有误！应收笔数：" +responseList.size() + "实收笔数："+totalcount);
            throw new RuntimeException("获取还款数据笔数有误.");
        }
        return responseList;
    }

    /**
     * 递归获取服务器数据
     * @param responseList
     * @param ncm
     * @param xstream
     * @param request
     * @param pkgcnt
     * @param startnum
     * @return
     */
    public int processTxn(List <T100101ResponseRecord> responseList ,
                           NewCmsManager ncm, XStream xstream, T100101Request request,
                           int pkgcnt, int startnum) {
//        T100101Request request = new T100101Request();

//        request.initHeader("0100","100101","3");

        //查询 房贷/消费信贷（1/2） 数据
//        request.setStdcxlx("1");
//        int pkgcnt = 50;
//        request.setStdymjls(String.valueOf(pkgcnt));
        request.setStdqsjls(String.valueOf(startnum));


        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        //System.out.println(strXml);

        //发送请求
        String responseBody = ncm.doPostXml(strXml);

        T100101Response response = (T100101Response) xstream.fromXML(responseBody);

        //头部总记录数
        String std400acur = response.getStd400acur();
        if (std400acur == null || std400acur == "") {
            std400acur = "0";
        }
        int totalcount = Integer.parseInt(std400acur);


        if (totalcount == 0) {

        } else {
            List<T100101ResponseRecord> tmpList =  response.getBody().getContent();
                    
            int currCnt = tmpList.size();
            logger.info(currCnt);
            logger.info("totalcount:" + totalcount + " currCnt:" + currCnt + " startnum:" + startnum);

            //打包到返回list中
            for (T100101ResponseRecord record : tmpList){
                   responseList.add(record);
            }

            //一个包不可以处理完
            if (totalcount > pkgcnt) {
                startnum += pkgcnt;
                if (startnum  <= totalcount) {
                    processTxn(responseList,ncm, xstream, request, pkgcnt, startnum);
                }
            }
        }


        //uploadCutpayResultBatch(response.getBody().getContent());
        return totalcount;
    }

    /**
     * 向信贷服务器批量上传银行扣款结果
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
            //1-成功 2-失败
            recordT102.setStdkkjg("2");

            t012.add(recordT102);
        }

        T100102CTL ctlT102 = new T100102CTL();
        ctlT102.start(t012);

        System.out.println("========" + count);

    }
}
