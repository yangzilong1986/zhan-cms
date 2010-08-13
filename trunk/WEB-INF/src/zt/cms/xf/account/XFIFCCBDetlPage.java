package zt.cms.xf.account;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.constant.XFWithHoldStatus;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dao.XfifbankdetlDao;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.exceptions.XfifbankdetlDaoException;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfifbankdetlDaoFactory;
import zt.cms.xf.gateway.BatchQueryResult;
import zt.cms.xf.gateway.CtgManager;
import zt.cms.xf.gateway.CutpayFailRecord;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.Debug;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
�����Ŵ��Լ�����ϵͳ�뽨�еĴ���ͨѶ����
������������
��ϸҳ���д���PAGE��
 */

public class XFIFCCBDetlPage extends FormActions {

    //    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFIFCCBDetlPage");
    private static Log logger = LogFactory.getLog(XFIFCCBDetlPage.class);

    private String journalno;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs, EventManager manager, String parameter) {

        journalno = ctx.getParameter("JOURNALNO");

        if (journalno != null) {
            instance.setValue("JOURNALNO", journalno);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            instance.setReadonly(true);
        }


        instance.getFormBean().getElement("SENDREQUESTBTN").setComponetTp(6);
        instance.getFormBean().getElement("SENDQUERYBTN").setComponetTp(6);
        instance.getFormBean().getElement("RESETDETLSTATUSBTN").setComponetTp(6);

        Xfifbankdetl bankdetl;
        try {
            XfifbankdetlDao detldao = XfifbankdetlDaoFactory.create();
            bankdetl = detldao.findByPrimaryKey(journalno);
        } catch (XfifbankdetlDaoException e) {
            Debug.debug(e);
            return -1;
        }


        if ((bankdetl.getStatus().equals(XFWithHoldStatus.SEND_PENDING))
                || (bankdetl.getStatus().equals(XFWithHoldStatus.SEND_FAILD))
                || (bankdetl.getStatus().equals(XFWithHoldStatus.SEND_OVERTIME))) {
            instance.getFormBean().getElement("SENDREQUESTBTN").setComponetTp(15);
            instance.getFormBean().getElement("SENDQUERYBTN").setComponetTp(15);
        } else if ((bankdetl.getStatus().equals(XFWithHoldStatus.SEND_SUCCESS))
                || (bankdetl.getStatus().equals(XFWithHoldStatus.QUERY_FAILD))) {
//                || (bankdetl.getStatus().equals(XFWithHoldStatus.UPDATESTATUS_FAILD))
//                || (bankdetl.getStatus().equals(XFWithHoldStatus.UPDATESTATUS_SUCCESS))) {

            //TODO:
            instance.getFormBean().getElement("SENDQUERYBTN").setComponetTp(15);
        }

        if (bankdetl.getStatus().equals(XFWithHoldStatus.QUERY_FAILD)) {
            instance.getFormBean().getElement("RESETDETLSTATUSBTN").setComponetTp(15);
        }

        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        String systemtype = ctx.getParameter("SYSTEMTYPE");

        if (button != null && button.equals("SENDREQUESTBTN")) {
            int sendcount = doSendRequestBtn(ctx, conn, instance, msgs, true);
            if (sendcount >= 0) {
                msgs.add("�����ѷ��ͣ����ύ " + sendcount + "�ʿۿ��¼���Ժ����ѯ�����");

                int updatecount = 0;
                if ("1".equals(systemtype)) {     //�����Ŵ�ϵͳ
                    updatecount = changeXFCutpayDetlRecordsStatus(XFBillStatus.BILLSTATUS_SEND_SUCCESS);
                } else if ("2".equals(systemtype)) {
                    updatecount = changeFDCutpayDetlRecordsStatus(FDBillStatus.SEND_SUCCESS);
                }
                if (updatecount == -1) {
                    msgs.add("<br><br>�޸Ŀۿ��¼״̬�����ύ��ʱ�������ش������ѯ��!");
                } else {
                    if (sendcount != updatecount) {
                        msgs.add("<br><br>���ύ�Ŀۿ��¼����ϸ�ۿ��¼�����������ѯ����");
                    }
                }
            } else {
                msgs.add("�˱ʴ��۱��ķ���ʧ�� ,��鿴ͨѶ��־��Ϣ��");
            }

            logger.info(msgs.getAllMessages());
            ctx.setRequestAtrribute("msg", msgs.getAllMessages());
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");
            instance.setReadonly(true);
            return 0;
        }

        if (button != null && button.equals("SENDQUERYBTN")) {
            BatchQueryResult result = new BatchQueryResult();
            try {
                int rtn = 0;
                if (doSendQueryRequestBtn(conn, msgs, result) == 0) {

                    if (checkQueryResult(result, msgs, systemtype) == true) {
                        //���ݲ�ѯ���ؽ��������ÿ�ʿۿ��¼��״̬
                        int count = 0;
                        if ("1".equals(systemtype)) {     //�����Ŵ�ϵͳ
                            String txndate = ctx.getParameter("TXNDATE");
                            count = processXFCutpayRecord(conn, txndate, result, msgs);
                        } else if ("2".equals(systemtype)) {      //����ϵͳ
                            count = processFDCutpayRecord(result, msgs);
                        }

                        if (count <= 0) {
                            int index = msgs.size() - 1;
                            updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.UPDATESTATUS_FAILD, msgs.get(index).getMessage());
                            msgs.add("���ݽ��з��ؽ�������ؿۿ��¼״̬ʱ�������ѯ��");
                            rtn = -1;
                        }
                        if (count > 0) {
                            msgs.add("���ݽ��з��ؽ����������" + count + "����ϸ�ۿ��¼��");
                            int index = msgs.size() - 1;
                            updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.UPDATESTATUS_SUCCESS, msgs.get(index).getMessage());
                            //SBS���ʴ���
                            //count = processSBSAccount(result, instance, msgs);
                            // updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.SEND_SUCCESS, msgs.get(1).getMessage());
                            //����ϵͳ���´���
                        }
                    } else {
                        int index = msgs.size() - 1;
                        updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.QUERY_FAILD, msgs.get(index).getMessage());
                    }
                } else {
                    rtn = -1;
                }
                String rtnmsg = msgs.getAllMessages();
                logger.info(rtnmsg);
                ctx.setRequestAtrribute("msg", rtnmsg);
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return rtn;
            } catch (Exception e) {
                System.out.println("�������۲�ѯ��������");
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "�������۲�ѯ�������⣬���ѯ������־��");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
        }

        //20100813  zhanrui  �����ѯʧ����������� ����������������ϸ����״̬ �Ĺ��ܣ��������·������
        if (button != null && button.equals("RESETDETLSTATUSBTN")) {

            int count = changeXFCutpayDetlRecordsStatus(XFBillStatus.BILLSTATUS_CHECKED);
            if (count > 0) {
                msgs.add("״̬�����ã������� " + count + "�ʿۿ��¼�����ѯ�����");
            } else {
                msgs.add("״̬���ô���ʧ�� ,��֪ͨϵͳ������Ա��");
            }

            logger.info(msgs.getAllMessages());
            ctx.setRequestAtrribute("msg", msgs.getAllMessages());
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");
            instance.setReadonly(true);
            return 0;
        }


        return 0;
    }

    private int doSendRequestBtn(SessionContext ctx, DatabaseConnection conn,
                                 FormInstance instance, ErrorMessages msgs, boolean isPass) {


        List list = new ArrayList();

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        String txdate = sFmt.format(new Date());
        XfifbankdetlDao detldao = null;
        Xfifbankdetl bankdetl = null;
        XfifbankdetlPk detlpk = new XfifbankdetlPk(journalno);
        try {
            detldao = XfifbankdetlDaoFactory.create();
            bankdetl = detldao.findByPrimaryKey(journalno);
        } catch (Exception e) {
            msgs.add("ϵͳ�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new RuntimeException(e);
        }

        list.add(bankdetl.getTxndate());              //��������
        list.add(bankdetl.getBizseqno());      //������ˮ��  (MPC)
        list.add(bankdetl.getJournalno());      //���к�

        DecimalFormat df = new DecimalFormat("#############0.00");

        String totalamt = df.format(bankdetl.getTotalamt());
        list.add("+" + StringUtils.leftPad(totalamt, 16, '0'));     //�ܽ��  17

        String totalcount = String.valueOf(bankdetl.getTotalcount());
        list.add(StringUtils.leftPad(totalcount, 7, '0'));     // �ܱ���        7

        String currcount = String.valueOf(bankdetl.getCurrcount());
        list.add(StringUtils.leftPad(currcount, 7, '0'));     // �����ܱ���        7


        list.add(bankdetl.getMultiflag());                                       //  �Ƿ��к�����  0-��1-��
        list.add(StringUtils.rightPad(bankdetl.getTransferact(), 22, ' '));                  //  ת���ʻ�  22

        if (bankdetl.getUsage() != null) {
            list.add(StringUtils.rightPad(bankdetl.getUsage(), 12, ' '));                            //  ��; 12
        } else {
            list.add(StringUtils.rightPad("99999998", 12, ' '));                            //  ��; 12
        }

        if (bankdetl.getRemark() != null) {
            list.add(StringUtils.rightPad(bankdetl.getRemark(), 30, ' '));          //  ��ע,   30
        } else {
            list.add(StringUtils.rightPad("REMARK:", 30, ' '));          //  ��ע,   30
        }

        if (bankdetl.getRemark1() != null) {
            list.add(StringUtils.rightPad(bankdetl.getRemark1(), 32, ' '));        //  ��ע1,  32
        } else {
            list.add(StringUtils.rightPad("REMARK1", 32, ' '));        //  ��ע1,  32
        }

        if (bankdetl.getRemark2() != null) {
            list.add(StringUtils.rightPad(bankdetl.getRemark2(), 32, ' '));        //  ��ע2,  32
        } else {
            list.add(StringUtils.rightPad("REMARK2", 32, ' '));        //  ��ע2,  32
        }

        list.add(bankdetl.getBankid());                                     //  ���д���,  3
        list.add("+0000000000000.00");                       //  ʧ�ܽ��       17
        list.add("0000000");                                 //  ʧ�ܱ���        7
        list.add("BAW");                                     //  ������� BAP-��������,BAS-������������

        list.add(bankdetl.getData());  //  ���������ļ�����  29000

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        String log = "��" + sdf.format(new Date()) + " ���۷����ס� ---";

        try {
            byte[] buffer = CtgManager.processBatchRequest("n050", list);

            byte[] bFormcode = new byte[4];
            System.arraycopy(buffer, 21, bFormcode, 0, 4);
            String formcode = new String(bFormcode);

            String status = XFWithHoldStatus.SEND_FAILD;

            int rtn = -1;
            if (!formcode.equals("T531")) {     //�쳣�������
                if (formcode.equals("MZZZ")) {          //����MBPʧ��,�������ݰ��з�MBP�ķ��ش�����Ϣ
                    log += getLogFromReturnMessage(buffer);
                } else if (formcode.equals("MB01")) { //�������г�ʱ,���������ҪMPC��ѯȷ��
                    status = XFWithHoldStatus.SEND_FAILD;
                    log += "SBS����FORM�ţ�" + formcode + "  ";
                } else if (formcode.equals("WB02")) {  //�ñ�ҵ�񲻴��ڻ��ѱ����оܾ�    ���ѯԭ��״̬����
                    status = XFWithHoldStatus.SEND_FAILD;
                    log += "SBS����FORM�ţ�" + formcode + "  �ñ�ҵ�񲻴��ڻ��ѱ����оܾ� ,���ѯԭ��";
                } else {    //��������
                    status = XFWithHoldStatus.SEND_FAILD;
                    log += "SBS����FORM�ţ�" + formcode + ", �ñ�ҵ����ʧ�ܣ������FORM���Ų�ѯԭ��";
                }
            } else {      //���ķ��ͳɹ�
                status = XFWithHoldStatus.SEND_SUCCESS;
                log += "SBS����FORM�ţ�" + formcode + "  �ñ�ҵ����ɹ�";
                rtn = bankdetl.getTotalcount().intValue();  //���ر������ܱ���
            }

            updateTable_XFIFBANKDETL_StatusAndLog(journalno, status, log);
            return rtn;
        } catch (Exception e) {
            String status = XFWithHoldStatus.SEND_FAILD;
            log += " �ñʱ����ύʱ����ϵͳ�쳣����ȷ��ԭ��";
            updateTable_XFIFBANKDETL_StatusAndLog(journalno, status, log);
            msgs.add("ϵͳ�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

    private int doSendQueryRequestBtn(DatabaseConnection conn, ErrorMessages msgs, BatchQueryResult result) {


        List list = new ArrayList();

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        String txdate = sFmt.format(new Date());

        XfifbankdetlDao detldao = null;
        Xfifbankdetl bankdetl = null;
        XfifbankdetlPk detlpk = null;
        try {
            detldao = XfifbankdetlDaoFactory.create();
            bankdetl = detldao.findByPrimaryKey(journalno);
            detlpk = new XfifbankdetlPk(journalno);
        } catch (Exception e) {
            msgs.add("ϵͳ�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new RuntimeException(e);
        }

        list.add(bankdetl.getBizseqno());      //������ˮ��  (MPC)
        list.add(bankdetl.getJournalno());      //���к�
        list.add(bankdetl.getTxndate());              //��������
        list.add("000001");                                 //  ��ʼ���� 6

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        String log = "��" + sdf.format(new Date()) + " ���۲�ѯ���ס� ---";

        String status = null;

        try {
            byte[] buffer = CtgManager.processBatchRequest("n052", list);

            byte[] bFormcode = new byte[4];
            System.arraycopy(buffer, 21, bFormcode, 0, 4);
            String formcode = new String(bFormcode);

            status = XFWithHoldStatus.SEND_FAILD;

            int rtn = -1;
            if (!formcode.equals("T541")) {     //�쳣�������
                if (formcode.equals("MZZZ")) {          //����MBPʧ��,�������ݰ��з�MBP�ķ��ش�����Ϣ
                    status = XFWithHoldStatus.QUERY_FAILD;

                    log += getLogFromReturnMessage(buffer);

                    if (log == null) {
                        log = "ERROR!";
                    }
                    rtn = -1;
                } else if (formcode.equals("MB01")) { //�������г�ʱ,���������ҪMPC��ѯȷ��
                    status = XFWithHoldStatus.QUERY_FAILD;
                    log += "SBS����FORM�ţ�" + formcode + "  ";
                    rtn = -1;
                } else if (formcode.equals("WB02")) {  //�ñ�ҵ�񲻴��ڻ��ѱ����оܾ�    ���ѯԭ��״̬����
                    status = XFWithHoldStatus.QUERY_FAILD;
                    log += "SBS����FORM�ţ�" + formcode + getLogFromReturnMessage(buffer) + "  �ñ�ҵ�񲻴��ڻ��ѱ����оܾ� ,���ѯԭ��";
                    rtn = -1;
                } else {    //��������
                    status = XFWithHoldStatus.QUERY_FAILD;
                    log += "SBS����FORM�ţ�" + formcode + getLogFromReturnMessage(buffer) + "  �ñ�ҵ����ʧ�ܣ������FORM���Ų�ѯԭ��";
                    rtn = -1;
                }
            } else {      //���ķ��ͳɹ�
                status = XFWithHoldStatus.QUERY_SUCCESS;
                log += "SBS����FORM�ţ�" + formcode + "  �ñ�ҵ����ɹ�";
                rtn = 0;
                //�������

                int pos = 30;
                CtgManager.getBatchQueryMsg(buffer, result);
                msgs.add("<br>��ѯ���ķ��ͳɹ���������£�" +
                        "<br>--�ɹ�������" + result.getSuccnt() +
                        "<br>--ʧ�ܱ�����" + result.getFalcnt() +
                        "<br>--�ɹ���" + result.getSucamt() +
                        "<br>--ʧ�ܽ�" + result.getFalamt() + "<br><br>  ");
            }

            if (rtn == -1) {
                msgs.add(log);
            }
            return rtn;
        } catch (Exception e) {
            status = XFWithHoldStatus.QUERY_FAILD;
            log += " �ñʱ����ύʧ�ܣ���ȷ��ԭ��!";
            msgs.add("ϵͳ�����쳣��" + log);
            Debug.debug(e);
            return -1;
        } finally {
            updateTable_XFIFBANKDETL_StatusAndLog(journalno, status, log);
        }

    }

    /*
    ���ݴ��۲�ѯ���׵Ľ����FDCUTPAYDETL���е���ϸ�ۿ��¼���д���
     */
    int processFDCutpayRecord(BatchQueryResult result, ErrorMessages msgs) throws Exception {

        try {
            List<CutpayFailRecord> failrecords = result.getAll();
            int failedcount = failrecords.size();

            FdcutpaydetlDao detldao = FdcutpaydetlDaoFactory.create();
// 20100429 zhan
// String sqlwhere = "billstatus = " + FDBillStatus.SEND_SUCCESS + " and journalno = " + journalno;  //�����ѷ���
            String sqlwhere = " journalno = " + journalno;  //������ϸ���ݵ�״̬�Ƿ��ѷ��ͷ��͡�
            Fdcutpaydetl[] cutpaydetls = detldao.findByDynamicWhere(sqlwhere, null);
            FdcutpaydetlPk detlpk = null;

            if (failedcount == 0) { //�ۿ�ȫ���ɹ�
                for (int i = 0; i < cutpaydetls.length; i++) {
                    cutpaydetls[i].setBillstatus(FDBillStatus.CUTPAY_SUCCESS);
                    detlpk = new FdcutpaydetlPk(cutpaydetls[i].getSeqno());
                    detldao.update(detlpk, cutpaydetls[i]);
                }
                return cutpaydetls.length;
            } else {
                int count = 0;

                for (int i = 0; i < cutpaydetls.length; i++) {
                    int failrecordflag = 0;
                    detlpk = new FdcutpaydetlPk(cutpaydetls[i].getSeqno());
                    //������ѯ�������ʧ�ܼ�¼��
                    for (int k = 0; k < failedcount; k++) {
                        BigDecimal jhje = cutpaydetls[i].getGthtjhJhje().setScale(2);

                        if (cutpaydetls[i].getCutpayactno().trim().equals(failrecords.get(k).getActnum().trim()) &&
                                jhje.equals(new BigDecimal(failrecords.get(k).getTxnamt())) &&
                                !failrecords.get(k).isProcessed()) { //�ʺš������ͬ�����Ҵ�ʧ�ܼ�¼δ�������
                            cutpaydetls[i].setBillstatus(FDBillStatus.CUTPAY_FAILD);
                            cutpaydetls[i].setFailreason(failrecords.get(k).getReason());
                            detldao.update(detlpk, cutpaydetls[i]);
                            count++;
                            failrecordflag = 1;

                            //���Ѵ����־�����ڴ�������ʺš������ͬ�Ŀۿ�ʧ�ܼ�¼
                            failrecords.get(k).setProcessed(true);
                            break;
                        }
                    }
                    if (failrecordflag == 0) {  //���ǿۿ�ʧ�ܼ�¼
                        cutpaydetls[i].setBillstatus(FDBillStatus.CUTPAY_SUCCESS);
                        detldao.update(detlpk, cutpaydetls[i]);
                    }
                }
                if (count != Integer.parseInt(result.getFalcnt())) {
                    msgs.add("���ݲ�ѯ���������ϸ�ۿ��¼״̬ʱ���ֱ�����һ�����⣬���ѯ��");
                    return -1;
                }
            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new RuntimeException(e);
        }
        return Integer.parseInt(result.getSuccnt()) + Integer.parseInt(result.getCurcnt());
    }

    /*
     ���ݴ��۲�ѯ���׵Ľ����XFACTCUTPAYDETL���е���ϸ�ۿ��¼���д���
      */
    int processXFCutpayRecord(DatabaseConnection conn, String txndate, BatchQueryResult result, ErrorMessages msgs) throws Exception {

        try {

//            conn.setAuto(false);
            List<CutpayFailRecord> failrecords = result.getAll();
            int failedcount = failrecords.size();

//            XfactcutpaydetlDao detldao = XfactcutpaydetlDaoFactory.create(conn.getConnection());
            XfactcutpaydetlDao detldao = XfactcutpaydetlDaoFactory.create(conn.getConnection());
            String sqlwhere = "billstatus = " + XFBillStatus.BILLSTATUS_SEND_SUCCESS + " and txjournalno = " + journalno;  //�����ѷ���
            Xfactcutpaydetl[] cutpaydetls = detldao.findByDynamicWhere(sqlwhere, null);
            XfactcutpaydetlPk detlpk = null;

            BillsManager bm = new BillsManager(txndate);
            if (failedcount == 0) { //�ۿ�ȫ���ɹ�
                for (int i = 0; i < cutpaydetls.length; i++) {
//                     cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);
//                     detlpk = new XfactcutpaydetlPk(cutpaydetls[i].getSeqno());
//                     detldao.update(detlpk, cutpaydetls[i]);

                    //����billsmanager�趨�ۿ�ɹ����� ע��������
                    bm.setCutpayDetlPaidupSuccess(cutpaydetls[i].getJournalno());
                }
                return cutpaydetls.length;
            } else {
                int count = 0;

                for (int i = 0; i < cutpaydetls.length; i++) {
                    int failrecordflag = 0;
                    detlpk = new XfactcutpaydetlPk(cutpaydetls[i].getJournalno());
                    //������ѯ�������ʧ�ܼ�¼��
                    for (int k = 0; k < failedcount; k++) {
                        BigDecimal jhje = cutpaydetls[i].getPaybackamt().setScale(2);
                        if (cutpaydetls[i].getPaybackact().trim().equals(failrecords.get(k).getActnum().trim()) &&
                                jhje.equals(new BigDecimal(failrecords.get(k).getTxnamt())) &&
                                !failrecords.get(k).isProcessed()) { //�ʺš������ͬ�����Ҵ�ʧ�ܼ�¼δ�������
                            cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_FAILED);
                            cutpaydetls[i].setPaybackdate(new SimpleDateFormat("yyyyMMdd").parse(txndate));
                            cutpaydetls[i].setFailurereason(failrecords.get(k).getReason());
                            detldao.update(detlpk, cutpaydetls[i]);
                            count++;
                            failrecordflag = 1;

                            //���Ѵ����־�����ڴ�������ʺš������ͬ�Ŀۿ�ʧ�ܼ�¼
                            failrecords.get(k).setProcessed(true);
                            break;
                        }
                    }
                    if (failrecordflag == 0) {  //���ǿۿ�ʧ�ܼ�¼
//                        cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);
//                        detldao.update(detlpk, cutpaydetls[i]);

                        //����billsmanager�趨�ۿ�ɹ����� ע��������
                        bm.setCutpayDetlPaidupSuccess(cutpaydetls[i].getJournalno());
                    }
                }
                if (count != Integer.parseInt(result.getFalcnt())) {
                    msgs.add("���ݲ�ѯ���������ϸ�ۿ��¼״̬ʱ���ֱ�����һ�����⣬���ѯ��");
                    return -1;
                }
            }
        } catch (Exception e) {
            Debug.debug(e);
            throw new RuntimeException(e);
        }
        return Integer.parseInt(result.getSuccnt()) + Integer.parseInt(result.getCurcnt());
    }


    /*
   ��ȡ����Ӧ�����еĴ�����Ϣ
    */

    private String getLogFromReturnMessage(byte[] buffer) {

        byte[] bLen = new byte[2];
        System.arraycopy(buffer, 27, bLen, 0, 2);
        short iLen = (short) (((bLen[0] << 8) | bLen[1] & 0xff));
        byte[] bLog = new byte[iLen];
        System.arraycopy(buffer, 29, bLog, 0, iLen);
        String log = new String(bLog);
        log = StringUtils.trimToEmpty(log);

        return log;
    }


    /*
    ��Xfifbankdetl����STATUS&LOG�ֶν��и���
     */
    private void updateTable_XFIFBANKDETL_StatusAndLog(String journalno, String status, String log) {
        XfifbankdetlDao detldao = null;
        Xfifbankdetl bankdetl = null;
        XfifbankdetlPk detlpk = null;
        try {
            detldao = XfifbankdetlDaoFactory.create();
            bankdetl = detldao.findByPrimaryKey(journalno);
            detlpk = new XfifbankdetlPk(journalno);

//            if ((bankdetl.getLog().trim() + log).getBytes().length > 2048) {
            if ((bankdetl.getLog().trim() + log).getBytes().length > 1000) {
                bankdetl.setLog(log);
            } else {
                bankdetl.setLog(bankdetl.getLog() + "\n" + log);
            }
            bankdetl.setStatus(status);
            detldao.update(detlpk, bankdetl);
        } catch (Exception e) {
            Debug.debug(e);
            throw new RuntimeException(e);
        }
    }

    /*
   �Բ�ѯ���ؽ�����е����ݽ��м��
    */
    private boolean checkQueryResult(BatchQueryResult result, ErrorMessages msgs, String systemtype) throws Exception {

        List<CutpayFailRecord> failrecord = result.getAll();
        int failedcount = failrecord.size();

        //TODO: �����Ŵ� �ݲ����  zhanrui 20090813
        if ("1".equals(systemtype)) {     //�����Ŵ�ϵͳ
            return true;
        }
        //TODO: �����Ŵ� �ݲ����  zhanrui 20090813
        if ("2".equals(systemtype)) {     //�����Ŵ�ϵͳ
            return true;
        }

        try {
            //���ɹ��ܱ������ܽ�����ѯ�����еĽ���Ƿ�һ��
            FdcutpaydetlDao detldao = FdcutpaydetlDaoFactory.create();

//            String sqlwhere = "(billstatus = " + FDBillStatus.SEND_SUCCESS  +    //�����ѷ���
//                   "or billstatus = " + FDBillStatus.CUTPAY_FAILD  +  //�ۿ�ʧ��
//                   "or billstatus = " + FDBillStatus.CUTPAY_SUCCESS + ")";  //�ۿ�ɹ�
            String sqlwhere = "billstatus = " + FDBillStatus.SEND_SUCCESS;    //�����ѷ���
            Fdcutpaydetl[] detls = detldao.findByDynamicWhere(sqlwhere, null);

/*
            int resultcount = Integer.parseInt(result.getSuccnt()) + Integer.parseInt(result.getFalcnt());
            if (detls.length != resultcount) {
                msgs.add("��ѯ��������ܱ���Ϊ" + resultcount + "��������ϸ�ۿ��¼����" + detls.length + "��������");
                return false;
            }

            BigDecimal totalamt = new BigDecimal("0");
            for (int i = 0; i < detls.length; i++) {
                totalamt = totalamt.add(detls[i].getGthtjhJhje());
            }

            if (totalamt.compareTo(new BigDecimal(result.getSucamt()).add(new BigDecimal(result.getFalamt()))) != 0) {
                msgs.add("������ѯ��������ܽ��Ϊ" + result.getSucamt() + "��������ϸ�ۿ��¼���ܽ��" + totalamt.toString() + "��������");
                return false;
            }

*/

            //���ۿ�ʧ�ܼ�¼��һ����
            for (int i = 0; i < failedcount; i++) {
//                sqlwhere = "cutpayactno = " + failrecord.get(i).getActnum().trim() + " and billstatus = " + FDBillStatus.SEND_SUCCESS;
                String sql = "cutpayactno = " + failrecord.get(i).getActnum().trim() + " and " + sqlwhere;
                Fdcutpaydetl[] faildetls = detldao.findByDynamicWhere(sql, null);
                if (faildetls.length == 0) {
                    msgs.add("������ѯ��������ʺ�" + failrecord.get(i).getActnum() + "��Ӧ�Ŀۿ��¼�����ڣ�");
                    return false;
                }
/*
                if (faildetls.length > 1) {
                    boolean flag = false;
                    for (int k = 0; k < faildetls.length; k++) {
                        if (faildetls[k].getGthtjhJhje().equals(new BigDecimal(failrecord.get(i).getTxnamt()))) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        msgs.add("������ѯ��������ʺŽ�" + failrecord.get(i).getActnum()+" "+failrecord.get(i).getTxnamt() + "��Ӧ�Ŀۿ��¼�����ڣ�");
                        return false;
                    }

                    //TODO: δ����ۿ��¼�����������ʺŽ����ͬ��¼�����
                }
*/
            }

        } catch (Exception e) {
            msgs.add("��������ѯ��������к˶�ʱ�������⣬���ѯ");
            Debug.debug(e);
            throw new Exception(e);
        }
        return true;
    }

    /*
   ���ݵ�ǰͨѶ������ˮ�ţ����·�����ϸ��FDCUTPAYDETL��ϸ��¼״̬
    */
    private int changeFDCutpayDetlRecordsStatus(String status) {
        int count = 0;
        try {
            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            String sql = " journalno = " + journalno;
            Fdcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            FdcutpaydetlPk detlPk = new FdcutpaydetlPk();

            String seqno = null;

            for (int i = 0; i < cutpaydetls.length; i++) {
                seqno = cutpaydetls[i].getSeqno();
                detlPk.setSeqno(seqno);
                cutpaydetls[i].setBillstatus(status);//����״̬
                detlDao.update(detlPk, cutpaydetls[i]);
                count++;
            }
        } catch (Exception e) {
            Debug.debug(e);
            return -1;
        }
        return count;
    }

    /*
    ���ݵ�ǰͨѶ������ˮ�ţ����������Ŵ���ϸ��XFACTCUTPAYDETL��ϸ��¼״̬
     */
    private int changeXFCutpayDetlRecordsStatus(String status) {
        int count = 0;
        try {
            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
            String sql = " txjournalno = " + journalno;
            Xfactcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            XfactcutpaydetlPk detlPk = new XfactcutpaydetlPk();

            String seqno = null;

            for (int i = 0; i < cutpaydetls.length; i++) {
                seqno = cutpaydetls[i].getJournalno();
                detlPk.setJournalno(seqno);
                cutpaydetls[i].setBillstatus(status);//����״̬
                detlDao.update(detlPk, cutpaydetls[i]);
                count++;
            }
        } catch (Exception e) {
            Debug.debug(e);
            return -1;
        }
        return count;
    }

    /*
   ���ܣ����ƻ��ۿ�����£���鲢���·���ϵͳ����
   1��������ݲ�һ�����
   2�����·���ϵͳ3����
    */


}