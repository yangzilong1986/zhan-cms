package zt.cms.xf.newcms.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-11-16
 * Time: 10:24:42
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class AttachmentDownload implements Serializable {
    private Log logger = LogFactory.getLog(this.getClass());

    private String appno;
    private String name;
    private String clientid;
    private String appdate;

    private StreamedContent file;


    public AttachmentDownload() {
        Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "未传入有效申请单参数！", "未传入有效申请单参数！"));
        } else {
            appno = (String) params.get("appno");
        getAppInfo();
        downloadFile();
        }
    }


    private void getAppInfo() {
        DatabaseConnection conn = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();

            String sql = "select c.CLIENTNO,to_char(c.BIRTHDAY,'yyyyMMdd') BIRTHDAY," +
                    "(select enudt from PTENUMINFODETL where enuid = 'NewGender' and  enutp = c.GENDER ) as GENDER," +
                    "c.NATIONALITY," +
                    "(select enudt from PTENUMINFODETL where enuid = 'NewMarriageStatus' and  enutp = c.MARRIAGESTATUS ) as MARRIAGESTATUS," +
                    "c.HUKOUADDRESS,c.CURRENTADDRESS," +
                    "c.COMPANY," +
                    "(select enudt from PTENUMINFODETL where enuid = 'NewTitle' and  enutp = c.TITLE ) as TITLE," +
                    "(select enudt from PTENUMINFODETL where enuid = 'NewQualification' and  enutp = c.QUALIFICATION ) as QUALIFICATION," +
                    "(select enudt from PTENUMINFODETL where enuid = 'NewEduLevel' and  enutp = c.EDULEVEL ) as EDULEVEL," +
                    "c.PHONE1,c.PHONE2," +
                    "c.PHONE3,c.NAME,c.CLIENTTYPE,c.DEGREETYPE,c.COMADDR,c.SERVFROM,c.RESIDENCEADR,c.HOUSINGSTS," +
                    "c.HEALTHSTATUS,c.MONTHLYPAY,c.BURDENSTATUS,c.EMPNO,c.SOCIALSECURITY,c.LIVEFROM,c.PC,c.COMPC,c.RESDPC,c.RESDADDR,c.EMAIL," +
                    "p.NAME  PNAME ,p.IDTYPE PIDTYPE ,p.ID PID ,p.COMPANY PCOMPANY ,p.TITLE PTITLE ,p.PHONE1 PPHONE1 ," +
                    "p.PHONE3 PPHONE3 ,p.CLIENTTYPE PCLIENTTYPE ,p.SERVFROM PSERVFROM ,p.MONTHLYPAY PMONTHLYPAY ," +
                    "p.LIVEFROM PLIVEFROM," +
                    "m.CHANNEL,m.COMMNAME,m.COMMTYPE,m.ADDR,m.NUM,m.AMT,m.RECEIVEAMT,m.APPAMT,m.DIVID," +
                    "d.ACTOPENINGBANK,d.BANKACTNO,d.XY,d.XYR,d.DY,d.DYW,d.ZY,d.ZYW,d.BZ,d.BZR,d.CREDITTYPE,d.MONPAYAMT," +
                    "d.LINKMAN,d.LINKMANGENDER,d.LINKMANPHONE1,d.LINKMANPHONE2,d.APPRELATION,d.LINKMANADD,d.LINKMANCOMPANY,d.ACTOPENINGBANK_UD," +
                    "(select enudt from PTENUMINFODETL where enuid = 'NewIDType' and  enutp = a.IDTYPE ) as IDTYPE," +
                    "a.ID,to_char(a.APPDATE,'yyyyMMdd') as APPDATE,a.APPTYPE,a.APPSTATUS,a.SID,a.ORDERNO,a.REQUESTTIME " +

                    "from XFCLIENT c,XFAPPCOMM m,XFAPPADD d,XFAPP a " +
                    "left outer join XFCLIENT p on a.APPNO=p.APPNO and p.XFCLTP='2' " +

                    "where a.APPNO='" + this.appno + "' and a.APPNO=c.APPNO and c.XFCLTP='1' " +
                    "and a.APPNO=m.APPNO " +
                    "and a.APPNO=d.APPNO";


            logger.info(sql);
            RecordSet recordSet = conn.executeQuery(sql);

            if (recordSet.getRecordCount() == 0) {
                logger.error("未找到对应申请信息" + sql);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "未找到此申请单信息！", null));
                return;
            }

            while (recordSet.next()) {
                this.name = recordSet.getString("name");    //客户姓名
                this.clientid = recordSet.getString("id");    //证件号码
                this.appdate = recordSet.getString("appdate");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.getInstance().releaseConnection(conn);
        }

    }


    public void downloadFile() {
//        InputStream stream = this.getClass().getResourceAsStream("/download/primefaces.pdf");
        InputStream stream = this.getClass().getResourceAsStream( appno + ".rar");
//        InputStream stream = this.getClass().getResourceAsStream("download/" + appno + ".rar");
        if (stream == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "未找到附件文件！", null));
            return;
        }
        file = new DefaultStreamedContent(stream, "application/rar", appno + ".rar");
    }

    public String getAppno() {
        return appno;
    }

    public void setAppno(String appno) {
        this.appno = appno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getAppdate() {
        return appdate;
    }

    public void setAppdate(String appdate) {
        this.appdate = appdate;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
