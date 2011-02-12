package zt.cms.xf.repayment.grmf;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zt.cms.xf.newcms.controllers.T100103CTL;
import zt.cms.xf.newcms.controllers.T100104CTL;
import zt.cms.xf.newcms.domain.T100103.T100103ResponseRecord;
import zt.cms.xf.newcms.domain.T100104.T100104RequestList;
import zt.cms.xf.newcms.domain.T100104.T100104RequestRecord;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ��ѯ�Ŵ�ϵͳ�� �������Ŵ� �ĵ�ǰ��������  ����ǰ���
 * User: zhanrui
 * Date: 2011-1-27
 * Time: 10:59:40
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class GmPreRepayAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    List<GmRepayDetlBean> dbrecords;
    List<T100103ResponseRecord> responseMFList;

    T100103CTL t100103ctl = new T100103CTL();

    private int totalcount;
    private BigDecimal totalamt;
    private BigDecimal totalPrincipalAmt;   //����
    private BigDecimal totalInterestAmt;    //��Ϣ
    private BigDecimal totalFxjeAmt;    //��Ϣ

    private String contractno;
    private String clientname;

    private T100103ResponseRecord[] selectedRecords;
    private T100103ResponseRecord selectedRecord;


    public List<GmRepayDetlBean> getDbrecords() {
        return dbrecords;
    }

    public List<T100103ResponseRecord> getResponseMFList() {
        return responseMFList;
    }

    public void setResponseMFList(List<T100103ResponseRecord> responseMFList) {
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

    public T100103ResponseRecord[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(T100103ResponseRecord[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public T100103ResponseRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(T100103ResponseRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    //==========================================================================

    @PostConstruct
    public void init() {
        selectRecords();
        initAmt();
        sumRecords();
    }

    public String query() {

        try {
            responseMFList = t100103ctl.getAllGRMFRecords();
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(), null));
        }
        return null;
    }

    public String doGetCmsRecord() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!checkCurrentRecordStatus()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "����δ�������¼��", null));
                return null;
            }
            responseMFList = t100103ctl.getAllGRMFRecords();
            if (responseMFList.size() == 0) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "�Ŵ�ϵͳ���޴������¼��", null));
                return null;
            }
            //dbrecords = transform(responseMFList);
            insertRecords();
            selectRecords();
            sumRecords();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return null;
    }

    /**
     * ��ת����Ľӿ����ݴ��뱾�ر���
     *
     * @return
     */
    private void insertRecords() {

        String inputdate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        int rtn = 0;
        ConnectionManager cm = null;
        DatabaseConnection conn = null;
        RecordSet rs = null;
        PreparedStatement ps = null;
        try {
            cm = ConnectionManager.getInstance();
            conn = cm.getConnection();

            String sql = "select max(seqno) from fdcutpaydetl where substr(seqno,1,8) = '" + inputdate + "'";
            rs = conn.executeQuery(sql);

            int maxno = 0;
            if (rs.next()) {
                String max = rs.getString(0);
                if (max != null) {
                    maxno = Integer.parseInt(max.substring(8));
                }
            }

            conn.setAuto(false);

            sql = "insert into gmrepaydetl " +
                    "     (seqno, " +
                    "     iouno, " +
                    "     issueno, " +
                    "     contractno, " +
                    "     clientno, " +
                    "     clientname, " +
                    "     loanactno, " +
                    "     repaymentactno, " +
                    "     repaymentamt, " +
                    "     principalamt, " +
                    "     interestamt, " +
                    "     penaltyamt, " +
                    "     otheramt, " +
                    "     repaymentdate,  " +
                    "     regioncd, " +
                    "     bankcd, " +
                    "     billstatus, " +
                    "     createtime, " +
                    "     failreason, " +
                    "     remark, " +
                    "     preflag, " +
                    "     journalno) " +
                    " values " +
                    " (?,?,?,?,?," +
                    "?,?,?,?,?," +
                    "?,?,?,?,?," +
                    "?,?,?,?,?," +
                    "?,?)";

            logger.info("sql=" + sql);
            ps = conn.getPreparedStatement(sql);

            int count = 0;
            for (T100103ResponseRecord record : responseMFList) {
                //TODO �����ֶ�Ϊ��ʱ���� ������
                String tmpStr = record.getStddqh();
                String regioncdTmp, bankcdTmp, nameTmp;
                if (tmpStr == null || tmpStr.equals("null")) {
                    continue;
                } else {
                    String[] code = tmpStr.split("-");
                    regioncdTmp = code[0].trim();
                    bankcdTmp = code[1].trim();
                }

                count++;
                ps.setString(1, inputdate + StringUtils.leftPad(String.valueOf(maxno + count), 7, '0'));
                ps.setString(2, record.getStdjjh()); //��ݺ�
                ps.setString(3, record.getStdqch()); //�ڴκ�
                ps.setString(4, record.getStdhth());  //��ͬ��
                ps.setString(5, record.getStdkhh());  //�ͻ���
                ps.setString(6, record.getStdkhmc()); //�ͻ�����
                ps.setString(7, record.getStddkzh());//�����ʺţ��ڲ��ʺţ�
                ps.setString(8, record.getStdhkzh());//�����ʺţ������ʺţ�

                ps.setDouble(9, Double.valueOf(record.getStdhkje()));//�ܻ�����
                ps.setDouble(10, Double.valueOf(record.getStdhkbj()));//����
                ps.setDouble(11, Double.valueOf(record.getStdhklx()));//��Ϣ
                ps.setDouble(12, 0);//��Ϣ  (��ǰ�����޴˽ӿ��ֶ�)
                ps.setDouble(13, 0);//����

                ps.setString(14, record.getStdjhhkr());//�ƻ������� YYYYMMDD (��ǰ����ʱ ���������� ���ڷ����Ŵ�ϵͳ)
                ps.setString(15, regioncdTmp);//�ͻ�������� �ൺ��0532    enum=FDRegionCode
                ps.setString(16, bankcdTmp);//�ͻ��������д��� ���У�104  ���У�105   enum=Bank

                ps.setString(17, "0");//�ʵ�״̬               enum=FDBillStatus
//                ps.setDate(18, (java.sql.Date) (new Date()));//����ϵͳ�ʵ���ȡʱ��  (Ӧ�÷�������ϵͳʱ��)
                ps.setDate(18, new java.sql.Date(new Date().getTime()));    //����ϵͳ�ʵ���ȡʱ��  (Ӧ�÷�������ϵͳʱ��)

                ps.setString(19, "");//�ۿ�ʧ��ԭ��
                ps.setString(20, "");//��ע
                //��ǰ����
                ps.setString(21, "1");//��ǰ�����־            enum=FDCutpayType   ��0��=��������  ��1��=��ǰ����
                ps.setString(22, "");//ͨѶ����ˮ�ţ� 8λ����+8λ˳��ţ�
                //end

                ps.addBatch();
            }

            int[] results = ps.executeBatch();//ִ��������

            for (int i = 0; i < results.length; i++) {
                if (results[i] < 0) {
                    //throw new RuntimeException("���ӿڼ�¼���뱾�ر���ʱ���ִ���");
                }
            }
            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            logger.error("���ӿڼ�¼���뱾�ر���ʱ���ִ���", e);
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                    conn.getConnection().close();
                } catch (SQLException e) {
                    logger.error("Connection closed error!", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private boolean checkCurrentRecordStatus() {
        return true;
    }

    private void selectRecords() {
        dbrecords = new ArrayList<GmRepayDetlBean>();
        ConnectionManager cm = null;
        DatabaseConnection conn = null;
        RecordSet rs = null;
        String sql = "select t.* from gmrepaydetl t where preflag='1' and billstatus = '0' and bankcd='105'";

        try {
            cm = ConnectionManager.getInstance();
            conn = cm.getConnection();
            rs = conn.executeQuery(sql);
            while (rs.next()){
                GmRepayDetlBean record = new GmRepayDetlBean();
                record.setSeqno(rs.getString("seqno"));
                record.setIouno(rs.getString("iouno"));
                record.setIssueno(rs.getString("issueno"));
                record.setContractno(rs.getString("contractno"));
                record.setClientno(rs.getString("clientno"));
                record.setClientname(rs.getString("clientname"));
                record.setLoanactno(rs.getString("loanactno"));
                record.setRepaymentactno(rs.getString("repaymentactno"));
                record.setRepaymentamt(new BigDecimal(rs.getDouble("repaymentamt")));
                record.setPrincipalamt(new BigDecimal(rs.getDouble("principalamt")));
                record.setInterestamt(new BigDecimal(rs.getDouble("interestamt")));
                record.setPenaltyamt(new BigDecimal(rs.getDouble("penaltyamt")));
                record.setOtheramt(new BigDecimal(rs.getDouble("otheramt")));
                record.setRepaymentdate(rs.getString("repaymentdate"));
                record.setRegioncd(rs.getString("regioncd"));
                record.setBankcd(rs.getString("bankcd"));
                record.setBillstatus(rs.getString("billstatus"));
                record.setCreatetime(rs.getCalendar("createtime").getTime());
                record.setFailreason(rs.getString("failreason"));
                record.setRemark(rs.getString("remark"));
                record.setPreflag(rs.getString("preflag"));
                record.setJournalno(rs.getString("journalno"));
                dbrecords.add(record);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    conn.getConnection().close();
                } catch (SQLException e) {
                    logger.error("Connection closed error!", e);
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private void initAmt() {
        totalamt = new BigDecimal(0);
        totalPrincipalAmt = new BigDecimal(0);
        totalInterestAmt = new BigDecimal(0);
        totalFxjeAmt = new BigDecimal(0);
    }

    private void sumRecords(){
        this.totalcount = dbrecords.size();
        for (GmRepayDetlBean record : this.dbrecords){
             this.totalamt = this.totalamt.add(record.getRepaymentamt());
        }
    }

    public String writebackAll() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedRecords.length > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "��дʱ���ִ���", "����ѡ����ϸ��¼��"));
            return null;
        }
        T100103ResponseRecord[] records = new T100103ResponseRecord[this.responseMFList.size()];
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

    private void startWriteBack(T100103ResponseRecord[] detls) {
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
