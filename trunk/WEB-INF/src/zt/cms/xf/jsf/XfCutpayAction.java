package zt.cms.xf.jsf;

import org.primefaces.component.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.dto.XfactcutpaydetlPk;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.newcms.controllers.T100102CTL;
import zt.cms.xf.newcms.controllers.T100104CTL;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;
import zt.cms.xf.newcms.domain.T100104.T100104RequestList;
import zt.cms.xf.newcms.domain.T100104.T100104RequestRecord;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-11-18
 * Time: 12:52:46
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
//@SessionScoped
@ViewScoped
public class XfCutpayAction {
    private static final Logger logger = LoggerFactory.getLogger(XfCutpayAction.class);

    private Xfactcutpaydetl[] detlList;
    private Xfactcutpaydetl detlRecord = new Xfactcutpaydetl();
    private Xfactcutpaydetl[] selectedRecords;
    private Xfactcutpaydetl selectedRecord;

    private int totalcount;
    private BigDecimal totalamt;
    private BigDecimal totalPrincipalAmt;   //����
    private BigDecimal totalInterestAmt;    //��Ϣ
    private BigDecimal totalFxjeAmt;    //��Ϣ

//    private String[] regionCodes = {"0532", "0531", "023", "0351"};
//    private String[] regionNames = {"�ൺ", "����", "����", "̫ԭ"};
//    private SelectItem[] regionOptions;

    private String field;


    @PostConstruct
    public void init() {
        try {
            detlList = getAllRecordsByStatus();
            countAmt(detlList);
            totalcount = detlList.length;
        } catch (XfactcutpaydetlDaoException e) {
            logger.error("��ʼ��ʱ���ִ���");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��ʼ��ʱ���ִ���", "�������ݿ�������⡣"));
        }
    }

    public String query() {
        try {
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:pdt");
            dataTable.setFirst(0);
            dataTable.setPage(1);
//            dataTable.setLiveScroll(true);
//            dataTable.setPaginator(false);
            detlList = getRecordsByWhere();
        } catch (XfactcutpaydetlDaoException e) {
            logger.error("��ѯʱ���ִ���");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��ѯʱ���ִ���", "�������ݿ�������⡣"));
        }
        return null;
    }

    public String reset() {
        this.detlRecord = new Xfactcutpaydetl();
        return null;
    }

    private Xfactcutpaydetl[] getAllRecordsByStatus() throws XfactcutpaydetlDaoException {
        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        String sql = "billstatus = " + XFBillStatus.BILLSTATUS_CORE_SUCCESS +
                " order by journalno";
        return detlDao.findByDynamicWhere(sql, null);
    }

    private Xfactcutpaydetl[] getRecordsByWhere() throws XfactcutpaydetlDaoException {
        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        String sql = "billstatus = " + XFBillStatus.BILLSTATUS_CORE_SUCCESS +
                " and clientname like '%" + detlRecord.getClientname() + "%' " +
                " and contractno like '%" + detlRecord.getContractno() + "%' " +
                " order by journalno";
        return detlDao.findByDynamicWhere(sql, null);
    }

    private void initAmt() {
        totalamt = new BigDecimal(0);
        totalPrincipalAmt = new BigDecimal(0);
        totalInterestAmt = new BigDecimal(0);
        totalFxjeAmt = new BigDecimal(0);
    }

    private void countAmt(Xfactcutpaydetl[] records) {
        initAmt();
        for (Xfactcutpaydetl record : records) {
            totalamt = totalamt.add(record.getPaybackamt());
            totalPrincipalAmt = totalPrincipalAmt.add(record.getPrincipalamt());
            totalInterestAmt = totalInterestAmt.add(record.getServicechargefee());
            totalFxjeAmt = totalFxjeAmt.add(record.getBreachfee()); //���ɽ� ��Ϣ��
        }
    }

    public String writebackAll(ActionEvent e) {

        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedRecords.length > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��дʱ���ִ���", "����ѡ����ϸ��¼��"));
            return null;
        }
        startWriteBack(this.detlList);
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

    private void startWriteBack(Xfactcutpaydetl[] detls) {
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

    public int processWriteBack(Xfactcutpaydetl[] detls) throws Exception {

        int count = 0;

        T100102CTL t100102ctl = new T100102CTL();
        T100104CTL t100104ctl = new T100104CTL();
        XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
        XfactcutpaydetlPk cutpaydetlPk = new XfactcutpaydetlPk();

        for (Xfactcutpaydetl detl : detls) {
            if (!detl.getBillstatus().equals(XFBillStatus.BILLSTATUS_CORE_SUCCESS)) {
                logger.error("״̬���ʧ��" + detl.getJournalno());
                continue;
            }
            boolean txResult = false;
            if (detl.getBilltype().equals("0")) { //��������
                T100102RequestRecord recordT102 = new T100102RequestRecord();
                recordT102.setStdjjh(detl.getRecvact());
                recordT102.setStdqch(detl.getPoano().toString());
                recordT102.setStdjhkkr(new SimpleDateFormat("yyyyMMdd").format(detl.getPaybackdate()));
                //1-�ɹ� 2-ʧ��
                recordT102.setStdkkjg("1");
                T100102RequestList t100102list = new T100102RequestList();
                t100102list.add(recordT102);
                //���ʷ��ʹ���
                txResult = t100102ctl.start(t100102list);
            } else if (detl.getBilltype().equals("2")) { //��ǰ����
                T100104RequestRecord recordT104 = new T100104RequestRecord();
                recordT104.setStdjjh(detl.getRecvact());
                recordT104.setStdqch(detl.getPoano().toString());
                recordT104.setStdjhkkr(new SimpleDateFormat("yyyyMMdd").format(detl.getPaybackdate()));
                //1-�ɹ� 2-ʧ��
                recordT104.setStdkkjg("1");
                T100104RequestList t100104list = new T100104RequestList();
                t100104list.add(recordT104);
                //���ʷ��ʹ���
                txResult = t100104ctl.start(t100104list);
            }

            if (txResult) {
                cutpaydetlPk.setJournalno(detl.getJournalno());
                detl.setBillstatus(XFBillStatus.FD_WRITEBACK_SUCCESS);
                detlDao.update(cutpaydetlPk, detl);
                count++;
            }
        }

        return count;
    }

    public String deleteRecord() {
        String contractno = selectedRecord.getContractno();

        query();
        return null;
    }
    //====================================================================================

    public Xfactcutpaydetl[] getDetlList() {
        return detlList;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public BigDecimal getTotalamt() {
        return totalamt;
    }

    public BigDecimal getTotalPrincipalAmt() {
        return totalPrincipalAmt;
    }

    public BigDecimal getTotalInterestAmt() {
        return totalInterestAmt;
    }

    public BigDecimal getTotalFxjeAmt() {
        return totalFxjeAmt;
    }

    public String getField() {
        return field;
    }

    public Xfactcutpaydetl getDetlRecord() {
        return detlRecord;
    }

    public void setDetlRecord(Xfactcutpaydetl detlRecord) {
        this.detlRecord = detlRecord;
    }

    public Xfactcutpaydetl getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(Xfactcutpaydetl selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public Xfactcutpaydetl[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(Xfactcutpaydetl[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }
}
