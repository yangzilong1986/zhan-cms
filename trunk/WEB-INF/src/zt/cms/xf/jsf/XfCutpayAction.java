package zt.cms.xf.jsf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dto.Fdcutpaydetl;
import zt.cms.xf.common.dto.FdcutpaydetlPk;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.newcms.controllers.T100102CTL;
import zt.cms.xf.newcms.controllers.T100104CTL;
import zt.cms.xf.newcms.domain.T100102.T100102RequestList;
import zt.cms.xf.newcms.domain.T100102.T100102RequestRecord;
import zt.cms.xf.newcms.domain.T100104.T100104RequestList;
import zt.cms.xf.newcms.domain.T100104.T100104RequestRecord;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.utils.Debug;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-11-18
 * Time: 12:52:46
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
//@SessionScoped
@RequestScoped
public class XfCutpayAction {
    private static final Logger logger = LoggerFactory.getLogger(XfCutpayAction.class);

    private Xfactcutpaydetl[] detlList;
    private Xfactcutpaydetl detlRecord;
    private Xfactcutpaydetl[] selectedRecords;
    private Xfactcutpaydetl selectedRecord;

    private int totalcount;
    private BigDecimal totalamt;
    private BigDecimal totalPrincipalAmt;   //本金
    private BigDecimal totalInterestAmt;    //利息
    private BigDecimal totalFxjeAmt;    //罚息

//    private String[] regionCodes = {"0532", "0531", "023", "0351"};
//    private String[] regionNames = {"青岛", "济南", "重庆", "太原"};
//    private SelectItem[] regionOptions;

    private String field;


    @PostConstruct
    public void init() {
        try {
            detlList = getCutPayDetlListByCCB();
            countAmt(detlList);
            totalcount = detlList.length;
        } catch (XfactcutpaydetlDaoException e) {
            logger.error("初始化时出现错误。");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "初始化时出现错误。", "检索数据库出现问题。"));
        }
    }

    private Xfactcutpaydetl[] getCutPayDetlListByCCB() throws XfactcutpaydetlDaoException {
//        DatabaseConnection conn = null;
//        Connection sqlconn = null;
        try {

//            conn = MyDB.getInstance().apGetConn();
//            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();

            String sql = "billstatus = " + XFBillStatus.BILLSTATUS_CORE_SUCCESS +
                    " order by journalno";

            return detlDao.findByDynamicWhere(sql, null);

        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        } finally {
//            MyDB.getInstance().releaseDBConn();
        }
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
            totalFxjeAmt = totalFxjeAmt.add(record.getBreachfee()); //滞纳金 罚息？
        }
    }

    public String writeback(ActionEvent e) {

        if (selectedRecords.length == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "回写信贷系统出现错误。", "未选择明细记录。"));
            return null;
        }
        logger.info("aaa" + selectedRecords.length);
        String journalno;
        for (Xfactcutpaydetl record : selectedRecords) {
            journalno = record.getJournalno();
        }
        return null;
    }



    /*
    20101020 单笔处理
    查询房贷系统的扣款记录表，对SBS入帐成功的记录进行回写（to 新信贷）
    返回成功处理笔数
     */

    public int processWriteBack(Xfactcutpaydetl[] detls) throws Exception {

        int count = 0;

        try {
            if (detls != null && detls.length > 0) {
                T100102CTL t100102ctl = new T100102CTL();
                T100104CTL t100104ctl = new T100104CTL();
                FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
                FdcutpaydetlPk cutpaydetlPk = new FdcutpaydetlPk();

                for (Xfactcutpaydetl detl : detls) {
                    if (!detl.getBillstatus().equals(FDBillStatus.SBS_ACCOUNT_SUCCESS)) {
                        logger.error("状态检查失败");
                        continue;
                    }
                    boolean txResult = false;
                    if (detl.getBilltype().equals("0")) { //正常还款
                        T100102RequestRecord recordT102 = new T100102RequestRecord();
                        recordT102.setStdjjh(detl.());
                        recordT102.setStdqch(detl.getGthtjhJhxh());
                        recordT102.setStdjhkkr(detl.getGthtjhDate());
                        //1-成功 2-失败
                        recordT102.setStdkkjg("1");
                        T100102RequestList t100102list = new T100102RequestList();
                        t100102list.add(recordT102);
                        //单笔发送处理
                        txResult = t100102ctl.start(t100102list);
                    }else if (detl.getBilltype().equals("2")){ //提前还款
                        T100104RequestRecord recordT104 = new T100104RequestRecord();
                        recordT104.setStdjjh(detl.getGthtjhHtnm());
                        recordT104.setStdqch(detl.getGthtjhJhxh());
                        recordT104.setStdjhkkr(detl.getGthtjhDate());
                        //1-成功 2-失败
                        recordT104.setStdkkjg("1");
                        T100104RequestList t100104list = new T100104RequestList();
                        t100104list.add(recordT104);
                        //单笔发送处理
                        txResult = t100104ctl.start(t100104list);
                    }

                    if (txResult) {
                        cutpaydetlPk.setSeqno(detl.getSeqno());
                        detl.setBillstatus(FDBillStatus.FD_WRITEBACK_SUCCESS);
                        detlDao.update(cutpaydetlPk, detl);
                        count++;
                    }
                }
            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
            }
        } catch (Exception e) {
            logger.error(e);
            msgs.add("回写新信贷系统时出现错误。成功处理笔数:"+count);
            return -1;
        }
        return count;
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
