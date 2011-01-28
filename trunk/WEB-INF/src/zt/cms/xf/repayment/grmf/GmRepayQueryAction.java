package zt.cms.xf.repayment.grmf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zt.cms.xf.newcms.controllers.T100101CTL;
import zt.cms.xf.newcms.controllers.T100102CTL;
import zt.cms.xf.newcms.controllers.T100104CTL;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;
import zt.cms.xf.newcms.domain.T100103.T100103ResponseRecord;
import zt.cms.xf.newcms.domain.T100104.T100104RequestList;
import zt.cms.xf.newcms.domain.T100104.T100104RequestRecord;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * ��ѯ�Ŵ�ϵͳ�� �������Ŵ� �ĵ�ǰ��������
 * User: zhanrui
 * Date: 2011-1-27
 * Time: 10:59:40
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class GmRepayQueryAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    List<T100101ResponseRecord> responseMFList;

    T100101CTL t100101ctl = new T100101CTL();

    private int totalcount;
    private BigDecimal totalamt;
    private BigDecimal totalPrincipalAmt;   //����
    private BigDecimal totalInterestAmt;    //��Ϣ
    private BigDecimal totalFxjeAmt;    //��Ϣ

    private String contractno;
    private String clientname;

    private T100101ResponseRecord[] selectedRecords;
    private T100101ResponseRecord selectedRecord;


    public List<T100101ResponseRecord> getResponseMFList() {
        return responseMFList;
    }

    public void setResponseMFList(List<T100101ResponseRecord> responseMFList) {
        this.responseMFList = responseMFList;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public BigDecimal getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(BigDecimal totalamt) {
        this.totalamt = totalamt;
    }

    public BigDecimal getTotalPrincipalAmt() {
        return totalPrincipalAmt;
    }

    public void setTotalPrincipalAmt(BigDecimal totalPrincipalAmt) {
        this.totalPrincipalAmt = totalPrincipalAmt;
    }

    public BigDecimal getTotalInterestAmt() {
        return totalInterestAmt;
    }

    public void setTotalInterestAmt(BigDecimal totalInterestAmt) {
        this.totalInterestAmt = totalInterestAmt;
    }

    public BigDecimal getTotalFxjeAmt() {
        return totalFxjeAmt;
    }

    public void setTotalFxjeAmt(BigDecimal totalFxjeAmt) {
        this.totalFxjeAmt = totalFxjeAmt;
    }

    public String getContractno() {
        return contractno;
    }

    public void setContractno(String contractno) {
        this.contractno = contractno;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public T100101ResponseRecord[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(T100101ResponseRecord[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public T100101ResponseRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(T100101ResponseRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    //==========================================================================

    @PostConstruct
    public void init() {
        initAmt();
        query();
    }

    public String query() {

        try {
            responseMFList = t100101ctl.getAllGRMFRecords();
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(), null));
        }
        return null;
    }

    private void initAmt() {
        totalamt = new BigDecimal(0);
        totalPrincipalAmt = new BigDecimal(0);
        totalInterestAmt = new BigDecimal(0);
        totalFxjeAmt = new BigDecimal(0);
    }

    public String writebackAll() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedRecords.length > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��дʱ���ִ���", "����ѡ����ϸ��¼��"));
            return null;
        }
        T100101ResponseRecord[] records = new T100101ResponseRecord[this.responseMFList.size()];
        startWriteBack(this.responseMFList.toArray(records));
        init();
        return null;
    }

    public String writebackMulti() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedRecords.length == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��дʱ���ִ���", "δѡ����ϸ��¼��"));
            return null;
        }

        startWriteBack(selectedRecords);
        init();
        return null;

    }

    private void startWriteBack(T100101ResponseRecord[] detls) {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            int result = processWriteBack(detls);
            if (result != detls.length) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "��д�����", "�ɹ�������" + result + "  ʧ�ܱ�����" + (detls.length - result)));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "��д���Ŵ�ϵͳ�ɹ���", "  ������" + result));
            }
        } catch (Exception e) {
            logger.error("��дʱ���ִ���", e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��дʱ���ִ���", null));
        }
        init();

    }

    /*
    20101020 ���ʴ���
    ��ѯ����ϵͳ�Ŀۿ��¼����SBS���ʳɹ��ļ�¼���л�д��to ���Ŵ���
    ���سɹ��������
     */

    public int processWriteBack(T100101ResponseRecord[] detls) throws Exception {

        int count = 0;

        T100102CTL t100102ctl = new T100102CTL();
//        T100104CTL t100104ctl = new T100104CTL();
//        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
//        XfactcutpaydetlPk cutpaydetlPk = new XfactcutpaydetlPk();

        for (T100101ResponseRecord detl : detls) {
/*
            if (!detl.getBillstatus().equals(XFBillStatus.BILLSTATUS_CORE_SUCCESS)) {
                logger.error("״̬���ʧ��" + detl.getJournalno());
                continue;
            }
*/
            boolean txResult = false;
            T100102RequestRecord recordT102 = new T100102RequestRecord();
            recordT102.setStdjjh(detl.getStdjjh());
            recordT102.setStdqch(detl.getStdqch());
            recordT102.setStdjhkkr(detl.getStdjhhkr());
            //1-�ɹ� 2-ʧ��
            recordT102.setStdkkjg("1");
            T100102RequestList t100102list = new T100102RequestList();
            t100102list.add(recordT102);
            //���ʷ��ʹ���
            txResult = t100102ctl.start(t100102list);

            if (txResult) {
//                cutpaydetlPk.setJournalno(detl.getJournalno());
//                detl.setBillstatus(XFBillStatus.FD_WRITEBACK_SUCCESS);
//                detlDao.update(cutpaydetlPk, detl);
                count++;
            }
        }

        return count;
    }
    public int processWriteBack(T100103ResponseRecord[] detls) throws Exception {

        int count = 0;

//        T100102CTL t100102ctl = new T100102CTL();
        T100104CTL t100104ctl = new T100104CTL();
//        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
//        XfactcutpaydetlPk cutpaydetlPk = new XfactcutpaydetlPk();

        for (T100103ResponseRecord detl : detls) {
/*
            if (!detl.getBillstatus().equals(XFBillStatus.BILLSTATUS_CORE_SUCCESS)) {
                logger.error("״̬���ʧ��" + detl.getJournalno());
                continue;
            }
*/
            boolean txResult = false;
            T100104RequestRecord record = new T100104RequestRecord();
            record.setStdjjh(detl.getStdjjh());
            record.setStdqch(detl.getStdqch());
            record.setStdjhkkr(detl.getStdjhhkr());
            //1-�ɹ� 2-ʧ��
            record.setStdkkjg("1");
            T100104RequestList list = new T100104RequestList();
            list.add(record);
            //���ʷ��ʹ���
            txResult = t100104ctl.start(list);

            if (txResult) {
//                cutpaydetlPk.setJournalno(detl.getJournalno());
//                detl.setBillstatus(XFBillStatus.FD_WRITEBACK_SUCCESS);
//                detlDao.update(cutpaydetlPk, detl);
                count++;
            }
        }

        return count;
    }


}
