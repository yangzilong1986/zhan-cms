package zt.cms.xf.newcms.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.gateway.NewCmsManager;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;
import zt.cms.xf.newcms.domain.T201001.T201001Request;
import zt.cms.xf.newcms.domain.T201001.T201001RequestList;
import zt.cms.xf.newcms.domain.T201001.T201001RequestRecord;
import zt.cms.xf.newcms.domain.T201001.T201001Response;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;


/**
 * 消费信贷申请资料上传
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 13:22:35
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "T201001")
@SessionScoped
public class T201001CTL implements java.io.Serializable {
    private Log logger = LogFactory.getLog(this.getClass());
    private String ID = "aaa" ;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public final static void main(String[] args) throws Exception {

        T201001CTL ctl = new T201001CTL();
        ctl.start();
    }

    public void start() {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(T201001Request.class);
        xstream.processAnnotations(T201001Response.class);


        T201001Request request = new T201001Request();

        request.initHeader("0200", "201001", "3");
        //组包体
        T201001RequestRecord reqRecord = getAppFormInfo("4113241986060409140001");
        System.out.println(reqRecord);

        //组包
        T201001RequestList reqList = new T201001RequestList();
        reqList.add(reqRecord);
        request.setBody(reqList);

        String strXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xstream.toXML(request);
        System.out.println(strXml);

        //发送请求
        NewCmsManager ncm = new NewCmsManager();
        String responseBody = ncm.doPostXml(strXml);

        T201001Response response = (T201001Response) xstream.fromXML(responseBody);

        System.out.println(response);

        //uploadCutpayResultBatch(response.getBody().getContent());

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
            recordT102.setStdkkjg("1");

            t012.add(recordT102);
        }
        System.out.println(count);

        T100102CTL ctlT102 = new T100102CTL();
        ctlT102.start(t012);

    }


    private T201001RequestRecord getAppFormInfo(String AppFormNo) {
        T201001RequestRecord reqRecord = new T201001RequestRecord();
        DatabaseConnection conn = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();

            String sql = "select c.CLIENTNO,to_char(c.BIRTHDAY,'yyyyMMdd') BIRTHDAY,c.GENDER,c.NATIONALITY,c.MARRIAGESTATUS,c.HUKOUADDRESS,c.CURRENTADDRESS," +
                    "c.COMPANY,c.TITLE,c.QUALIFICATION,c.EDULEVEL,c.PHONE1,c.PHONE2," +
                    "c.PHONE3,c.NAME,c.CLIENTTYPE,c.DEGREETYPE,c.COMADDR,c.SERVFROM,c.RESIDENCEADR,c.HOUSINGSTS," +
                    "c.HEALTHSTATUS,c.MONTHLYPAY,c.BURDENSTATUS,c.EMPNO,c.SOCIALSECURITY,c.LIVEFROM,c.PC,c.COMPC,c.RESDPC,c.RESDADDR,c.EMAIL," +
                    "p.NAME  PNAME ,p.IDTYPE PIDTYPE ,p.ID PID ,p.COMPANY PCOMPANY ,p.TITLE PTITLE ,p.PHONE1 PPHONE1 ," +
                    "p.PHONE3 PPHONE3 ,p.CLIENTTYPE PCLIENTTYPE ,p.SERVFROM PSERVFROM ,p.MONTHLYPAY PMONTHLYPAY ," +
                    "p.LIVEFROM PLIVEFROM," +
                    "m.CHANNEL,m.COMMNAME,m.COMMTYPE,m.ADDR,m.NUM,m.AMT,m.RECEIVEAMT,m.APPAMT,m.DIVID," +
                    "d.ACTOPENINGBANK,d.BANKACTNO,d.XY,d.XYR,d.DY,d.DYW,d.ZY,d.ZYW,d.BZ,d.BZR,d.CREDITTYPE,d.MONPAYAMT," +
                    "d.LINKMAN,d.LINKMANGENDER,d.LINKMANPHONE1,d.LINKMANPHONE2,d.APPRELATION,d.LINKMANADD,d.LINKMANCOMPANY,d.ACTOPENINGBANK_UD," +
                    "a.IDTYPE,a.ID,to_char(a.APPDATE,'yyyyMMdd') as APPDATE,a.APPTYPE,a.APPSTATUS,a.SID,a.ORDERNO,a.REQUESTTIME " +
                    "from XFCLIENT c,XFAPPCOMM m,XFAPPADD d,XFAPP a " +
                    "left outer join XFCLIENT p on a.APPNO=p.APPNO and p.XFCLTP='2' " +
                    "where a.APPNO='" + AppFormNo + "' and a.APPNO=c.APPNO and c.XFCLTP='1' " +
                    "and a.APPNO=m.APPNO " +
                    "and a.APPNO=d.APPNO";


            RecordSet recordSet = conn.executeQuery(sql);
            while (recordSet.next()) {
                //reqRecord.setStdsqdh(recordSet.getString("appno"));    //申请单号
                reqRecord.setStdsqdh(AppFormNo);    //申请单号
                reqRecord.setStdurl("");    //文件URL
                reqRecord.setStdkhxm(recordSet.getString("name"));    //客户姓名
                reqRecord.setStdzjlx(recordSet.getString("idtype"));    //证件类型
                reqRecord.setStdzjhm(recordSet.getString("id"));    //证件号码
                reqRecord.setStdlxfs(recordSet.getString("phone1"));    //联系方式
                reqRecord.setStdxb(recordSet.getString("gender"));    //性别
                reqRecord.setStdcsrq(recordSet.getString("birthday"));    //出生日期
                reqRecord.setStdgj(recordSet.getString("CHN"));    //国籍
                reqRecord.setStdkhxz(recordSet.getString("clienttype"));    //客户性质 TODO
                reqRecord.setStdhyzk(recordSet.getString("marriagestatus"));    //婚姻状况
                reqRecord.setStdjycd(recordSet.getString("edulevel"));    //教育程度
                reqRecord.setStdkhly(recordSet.getString("99"));    //客户来源
                reqRecord.setStdhjszd(recordSet.getString("residenceadr"));    //户籍所在地
                reqRecord.setStdhkxz(recordSet.getString("residenceadr"));    //户口性质
                reqRecord.setStdrhzxqk(recordSet.getString("6"));    //人行征信情况
                reqRecord.setStdfdyqqs(recordSet.getString("0"));    //征信记录中零售房贷最高逾期期数
                reqRecord.setStdffdyqqs(recordSet.getString("0"));    //征信记录中零售非房贷最高逾期期数
                reqRecord.setStdjtdz(recordSet.getString("currentaddress"));    //家庭地址
                reqRecord.setStdjtdzdh(recordSet.getString("phone2"));    //家庭地址电话
                reqRecord.setStdzzxz(recordSet.getString("9"));    //住宅性质
                reqRecord.setStdzy(recordSet.getString("clienttype"));    //职业 TODO
                reqRecord.setStdzw(recordSet.getString("title"));    //职务
                reqRecord.setStdzc(recordSet.getString("qualification"));    //职称
                reqRecord.setStdgzdw(recordSet.getString("company"));    //工作单位
                reqRecord.setStdsshy(recordSet.getString("U"));    //所属行业
                reqRecord.setStdszqyrs(recordSet.getString("0"));    //所在企业人数

                String servyears = recordSet.getString("servfrom");
                if (servyears == null) {
                    servyears = "0000";
                } else {
                    int length = servyears.trim().length();
                    switch (length) {
                        case 1:
                            servyears = "0" + servyears + "00";
                            break;
                        case 2:
                            servyears = servyears + "00";
                            break;
                        case 0:
                            servyears = "0000";
                            break;
                        default:
                            servyears = "0000";
                    }
                }
                reqRecord.setStdgznx(servyears);    //目前工作持续年限
                reqRecord.setStdlxr(recordSet.getString("linkman"));    //联系人
                reqRecord.setStdlxrdh(recordSet.getString("linkmanphone1"));    //联系人电话
                reqRecord.setStdgrysr(recordSet.getString("monthlypay"));    //个人月收入
                reqRecord.setStdjtwdsr(recordSet.getString("0"));    //家庭稳定收入
                reqRecord.setStdzwzc(recordSet.getString("0"));    //每月其他债务支出
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            ConnectionManager.getInstance().releaseConnection(conn);
        }

        return reqRecord;
    }
}
