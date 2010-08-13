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
消费信贷以及房贷系统与建行的代扣通讯处理
用于批量发送
详细页面中处理（PAGE）
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
                msgs.add("报文已发送！共提交 " + sendcount + "笔扣款记录，稍后请查询结果。");

                int updatecount = 0;
                if ("1".equals(systemtype)) {     //消费信贷系统
                    updatecount = changeXFCutpayDetlRecordsStatus(XFBillStatus.BILLSTATUS_SEND_SUCCESS);
                } else if ("2".equals(systemtype)) {
                    updatecount = changeFDCutpayDetlRecordsStatus(FDBillStatus.SEND_SUCCESS);
                }
                if (updatecount == -1) {
                    msgs.add("<br><br>修改扣款记录状态（已提交）时出现严重错误，请查询！!");
                } else {
                    if (sendcount != updatecount) {
                        msgs.add("<br><br>已提交的扣款记录与明细扣款记录数不符，请查询！！");
                    }
                }
            } else {
                msgs.add("此笔代扣报文发送失败 ,请查看通讯日志信息。");
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
                        //根据查询返回结果，处理每笔扣款记录的状态
                        int count = 0;
                        if ("1".equals(systemtype)) {     //消费信贷系统
                            String txndate = ctx.getParameter("TXNDATE");
                            count = processXFCutpayRecord(conn, txndate, result, msgs);
                        } else if ("2".equals(systemtype)) {      //房贷系统
                            count = processFDCutpayRecord(result, msgs);
                        }

                        if (count <= 0) {
                            int index = msgs.size() - 1;
                            updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.UPDATESTATUS_FAILD, msgs.get(index).getMessage());
                            msgs.add("根据建行返回结果处理本地扣款记录状态时出错，请查询。");
                            rtn = -1;
                        }
                        if (count > 0) {
                            msgs.add("根据建行返回结果，共处理" + count + "条明细扣款记录。");
                            int index = msgs.size() - 1;
                            updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.UPDATESTATUS_SUCCESS, msgs.get(index).getMessage());
                            //SBS入帐处理
                            //count = processSBSAccount(result, instance, msgs);
                            // updateTable_XFIFBANKDETL_StatusAndLog(journalno, XFWithHoldStatus.SEND_SUCCESS, msgs.get(1).getMessage());
                            //房贷系统更新处理
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
                System.out.println("批量代扣查询出现问题");
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "批量代扣查询出现问题，请查询处理日志！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return -1;
            }
        }

        //20100813  zhanrui  加入查询失败情况下重置 本次批号下所有明细数据状态 的功能，便于重新发起代扣
        if (button != null && button.equals("RESETDETLSTATUSBTN")) {

            int count = changeXFCutpayDetlRecordsStatus(XFBillStatus.BILLSTATUS_CHECKED);
            if (count > 0) {
                msgs.add("状态已重置！共处理 " + count + "笔扣款记录，请查询结果。");
            } else {
                msgs.add("状态重置处理失败 ,请通知系统管理人员。");
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
            msgs.add("系统发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new RuntimeException(e);
        }

        list.add(bankdetl.getTxndate());              //交易日期
        list.add(bankdetl.getBizseqno());      //交易流水号  (MPC)
        list.add(bankdetl.getJournalno());      //序列号

        DecimalFormat df = new DecimalFormat("#############0.00");

        String totalamt = df.format(bankdetl.getTotalamt());
        list.add("+" + StringUtils.leftPad(totalamt, 16, '0'));     //总金额  17

        String totalcount = String.valueOf(bankdetl.getTotalcount());
        list.add(StringUtils.leftPad(totalcount, 7, '0'));     // 总笔数        7

        String currcount = String.valueOf(bankdetl.getCurrcount());
        list.add(StringUtils.leftPad(currcount, 7, '0'));     // 本包总笔数        7


        list.add(bankdetl.getMultiflag());                                       //  是否有后续包  0-否，1-是
        list.add(StringUtils.rightPad(bankdetl.getTransferact(), 22, ' '));                  //  转出帐户  22

        if (bankdetl.getUsage() != null) {
            list.add(StringUtils.rightPad(bankdetl.getUsage(), 12, ' '));                            //  用途 12
        } else {
            list.add(StringUtils.rightPad("99999998", 12, ' '));                            //  用途 12
        }

        if (bankdetl.getRemark() != null) {
            list.add(StringUtils.rightPad(bankdetl.getRemark(), 30, ' '));          //  备注,   30
        } else {
            list.add(StringUtils.rightPad("REMARK:", 30, ' '));          //  备注,   30
        }

        if (bankdetl.getRemark1() != null) {
            list.add(StringUtils.rightPad(bankdetl.getRemark1(), 32, ' '));        //  备注1,  32
        } else {
            list.add(StringUtils.rightPad("REMARK1", 32, ' '));        //  备注1,  32
        }

        if (bankdetl.getRemark2() != null) {
            list.add(StringUtils.rightPad(bankdetl.getRemark2(), 32, ' '));        //  备注2,  32
        } else {
            list.add(StringUtils.rightPad("REMARK2", 32, ' '));        //  备注2,  32
        }

        list.add(bankdetl.getBankid());                                     //  银行代码,  3
        list.add("+0000000000000.00");                       //  失败金额       17
        list.add("0000000");                                 //  失败笔数        7
        list.add("BAW");                                     //  交易类别 BAP-批量报销,BAS-批量代发工资

        list.add(bankdetl.getData());  //  代发代扣文件内容  29000

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        String log = "【" + sdf.format(new Date()) + " 代扣发起交易】 ---";

        try {
            byte[] buffer = CtgManager.processBatchRequest("n050", list);

            byte[] bFormcode = new byte[4];
            System.arraycopy(buffer, 21, bFormcode, 0, 4);
            String formcode = new String(bFormcode);

            String status = XFWithHoldStatus.SEND_FAILD;

            int rtn = -1;
            if (!formcode.equals("T531")) {     //异常情况处理
                if (formcode.equals("MZZZ")) {          //调用MBP失败,返回数据包中放MBP的返回错误信息
                    log += getLogFromReturnMessage(buffer);
                } else if (formcode.equals("MB01")) { //连接银行超时,结果不明需要MPC查询确认
                    status = XFWithHoldStatus.SEND_FAILD;
                    log += "SBS返回FORM号：" + formcode + "  ";
                } else if (formcode.equals("WB02")) {  //该笔业务不存在或已被银行拒绝    需查询原因，状态不变
                    status = XFWithHoldStatus.SEND_FAILD;
                    log += "SBS返回FORM号：" + formcode + "  该笔业务不存在或已被银行拒绝 ,请查询原因。";
                } else {    //其他，置
                    status = XFWithHoldStatus.SEND_FAILD;
                    log += "SBS返回FORM号：" + formcode + ", 该笔业务处理失败，请根据FORM代号查询原因。";
                }
            } else {      //报文发送成功
                status = XFWithHoldStatus.SEND_SUCCESS;
                log += "SBS返回FORM号：" + formcode + "  该笔业务处理成功";
                rtn = bankdetl.getTotalcount().intValue();  //返回报文中总笔数
            }

            updateTable_XFIFBANKDETL_StatusAndLog(journalno, status, log);
            return rtn;
        } catch (Exception e) {
            String status = XFWithHoldStatus.SEND_FAILD;
            log += " 该笔报文提交时出现系统异常，请确认原因。";
            updateTable_XFIFBANKDETL_StatusAndLog(journalno, status, log);
            msgs.add("系统发生异常：" + e.getMessage());
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
            msgs.add("系统发生异常：" + e.getMessage());
            Debug.debug(e);
            throw new RuntimeException(e);
        }

        list.add(bankdetl.getBizseqno());      //交易流水号  (MPC)
        list.add(bankdetl.getJournalno());      //序列号
        list.add(bankdetl.getTxndate());              //交易日期
        list.add("000001");                                 //  起始笔数 6

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        String log = "【" + sdf.format(new Date()) + " 代扣查询交易】 ---";

        String status = null;

        try {
            byte[] buffer = CtgManager.processBatchRequest("n052", list);

            byte[] bFormcode = new byte[4];
            System.arraycopy(buffer, 21, bFormcode, 0, 4);
            String formcode = new String(bFormcode);

            status = XFWithHoldStatus.SEND_FAILD;

            int rtn = -1;
            if (!formcode.equals("T541")) {     //异常情况处理
                if (formcode.equals("MZZZ")) {          //调用MBP失败,返回数据包中放MBP的返回错误信息
                    status = XFWithHoldStatus.QUERY_FAILD;

                    log += getLogFromReturnMessage(buffer);

                    if (log == null) {
                        log = "ERROR!";
                    }
                    rtn = -1;
                } else if (formcode.equals("MB01")) { //连接银行超时,结果不明需要MPC查询确认
                    status = XFWithHoldStatus.QUERY_FAILD;
                    log += "SBS返回FORM号：" + formcode + "  ";
                    rtn = -1;
                } else if (formcode.equals("WB02")) {  //该笔业务不存在或已被银行拒绝    需查询原因，状态不变
                    status = XFWithHoldStatus.QUERY_FAILD;
                    log += "SBS返回FORM号：" + formcode + getLogFromReturnMessage(buffer) + "  该笔业务不存在或已被银行拒绝 ,请查询原因。";
                    rtn = -1;
                } else {    //其他，置
                    status = XFWithHoldStatus.QUERY_FAILD;
                    log += "SBS返回FORM号：" + formcode + getLogFromReturnMessage(buffer) + "  该笔业务处理失败，请根据FORM代号查询原因。";
                    rtn = -1;
                }
            } else {      //报文发送成功
                status = XFWithHoldStatus.QUERY_SUCCESS;
                log += "SBS返回FORM号：" + formcode + "  该笔业务处理成功";
                rtn = 0;
                //解包处理

                int pos = 30;
                CtgManager.getBatchQueryMsg(buffer, result);
                msgs.add("<br>查询报文发送成功，结果如下：" +
                        "<br>--成功笔数：" + result.getSuccnt() +
                        "<br>--失败笔数：" + result.getFalcnt() +
                        "<br>--成功金额：" + result.getSucamt() +
                        "<br>--失败金额：" + result.getFalamt() + "<br><br>  ");
            }

            if (rtn == -1) {
                msgs.add(log);
            }
            return rtn;
        } catch (Exception e) {
            status = XFWithHoldStatus.QUERY_FAILD;
            log += " 该笔报文提交失败，请确认原因!";
            msgs.add("系统发生异常：" + log);
            Debug.debug(e);
            return -1;
        } finally {
            updateTable_XFIFBANKDETL_StatusAndLog(journalno, status, log);
        }

    }

    /*
    根据代扣查询交易的结果对FDCUTPAYDETL表中的明细扣款记录进行处理
     */
    int processFDCutpayRecord(BatchQueryResult result, ErrorMessages msgs) throws Exception {

        try {
            List<CutpayFailRecord> failrecords = result.getAll();
            int failedcount = failrecords.size();

            FdcutpaydetlDao detldao = FdcutpaydetlDaoFactory.create();
// 20100429 zhan
// String sqlwhere = "billstatus = " + FDBillStatus.SEND_SUCCESS + " and journalno = " + journalno;  //报文已发送
            String sqlwhere = " journalno = " + journalno;  //不管明细数据的状态是否已发送发送。
            Fdcutpaydetl[] cutpaydetls = detldao.findByDynamicWhere(sqlwhere, null);
            FdcutpaydetlPk detlpk = null;

            if (failedcount == 0) { //扣款全部成功
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
                    //遍历查询结果包中失败记录集
                    for (int k = 0; k < failedcount; k++) {
                        BigDecimal jhje = cutpaydetls[i].getGthtjhJhje().setScale(2);

                        if (cutpaydetls[i].getCutpayactno().trim().equals(failrecords.get(k).getActnum().trim()) &&
                                jhje.equals(new BigDecimal(failrecords.get(k).getTxnamt())) &&
                                !failrecords.get(k).isProcessed()) { //帐号、金额相同，并且此失败记录未被处理过
                            cutpaydetls[i].setBillstatus(FDBillStatus.CUTPAY_FAILD);
                            cutpaydetls[i].setFailreason(failrecords.get(k).getReason());
                            detldao.update(detlpk, cutpaydetls[i]);
                            count++;
                            failrecordflag = 1;

                            //置已处理标志，用于处理多条帐号、金额相同的扣款失败记录
                            failrecords.get(k).setProcessed(true);
                            break;
                        }
                    }
                    if (failrecordflag == 0) {  //不是扣款失败记录
                        cutpaydetls[i].setBillstatus(FDBillStatus.CUTPAY_SUCCESS);
                        detldao.update(detlpk, cutpaydetls[i]);
                    }
                }
                if (count != Integer.parseInt(result.getFalcnt())) {
                    msgs.add("根据查询结果更新明细扣款记录状态时出现笔数不一致问题，请查询。");
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
     根据代扣查询交易的结果对XFACTCUTPAYDETL表中的明细扣款记录进行处理
      */
    int processXFCutpayRecord(DatabaseConnection conn, String txndate, BatchQueryResult result, ErrorMessages msgs) throws Exception {

        try {

//            conn.setAuto(false);
            List<CutpayFailRecord> failrecords = result.getAll();
            int failedcount = failrecords.size();

//            XfactcutpaydetlDao detldao = XfactcutpaydetlDaoFactory.create(conn.getConnection());
            XfactcutpaydetlDao detldao = XfactcutpaydetlDaoFactory.create(conn.getConnection());
            String sqlwhere = "billstatus = " + XFBillStatus.BILLSTATUS_SEND_SUCCESS + " and txjournalno = " + journalno;  //报文已发送
            Xfactcutpaydetl[] cutpaydetls = detldao.findByDynamicWhere(sqlwhere, null);
            XfactcutpaydetlPk detlpk = null;

            BillsManager bm = new BillsManager(txndate);
            if (failedcount == 0) { //扣款全部成功
                for (int i = 0; i < cutpaydetls.length; i++) {
//                     cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);
//                     detlpk = new XfactcutpaydetlPk(cutpaydetls[i].getSeqno());
//                     detldao.update(detlpk, cutpaydetls[i]);

                    //调用billsmanager设定扣款成功方法 注意事务处理
                    bm.setCutpayDetlPaidupSuccess(cutpaydetls[i].getJournalno());
                }
                return cutpaydetls.length;
            } else {
                int count = 0;

                for (int i = 0; i < cutpaydetls.length; i++) {
                    int failrecordflag = 0;
                    detlpk = new XfactcutpaydetlPk(cutpaydetls[i].getJournalno());
                    //遍历查询结果包中失败记录集
                    for (int k = 0; k < failedcount; k++) {
                        BigDecimal jhje = cutpaydetls[i].getPaybackamt().setScale(2);
                        if (cutpaydetls[i].getPaybackact().trim().equals(failrecords.get(k).getActnum().trim()) &&
                                jhje.equals(new BigDecimal(failrecords.get(k).getTxnamt())) &&
                                !failrecords.get(k).isProcessed()) { //帐号、金额相同，并且此失败记录未被处理过
                            cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_FAILED);
                            cutpaydetls[i].setPaybackdate(new SimpleDateFormat("yyyyMMdd").parse(txndate));
                            cutpaydetls[i].setFailurereason(failrecords.get(k).getReason());
                            detldao.update(detlpk, cutpaydetls[i]);
                            count++;
                            failrecordflag = 1;

                            //置已处理标志，用于处理多条帐号、金额相同的扣款失败记录
                            failrecords.get(k).setProcessed(true);
                            break;
                        }
                    }
                    if (failrecordflag == 0) {  //不是扣款失败记录
//                        cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS);
//                        detldao.update(detlpk, cutpaydetls[i]);

                        //调用billsmanager设定扣款成功方法 注意事务处理
                        bm.setCutpayDetlPaidupSuccess(cutpaydetls[i].getJournalno());
                    }
                }
                if (count != Integer.parseInt(result.getFalcnt())) {
                    msgs.add("根据查询结果更新明细扣款记录状态时出现笔数不一致问题，请查询。");
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
   获取报错应答报文中的错误信息
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
    对Xfifbankdetl表中STATUS&LOG字段进行更新
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
   对查询返回结果包中的数据进行检查
    */
    private boolean checkQueryResult(BatchQueryResult result, ErrorMessages msgs, String systemtype) throws Exception {

        List<CutpayFailRecord> failrecord = result.getAll();
        int failedcount = failrecord.size();

        //TODO: 消费信贷 暂不检查  zhanrui 20090813
        if ("1".equals(systemtype)) {     //消费信贷系统
            return true;
        }
        //TODO: 消费信贷 暂不检查  zhanrui 20090813
        if ("2".equals(systemtype)) {     //消费信贷系统
            return true;
        }

        try {
            //检查成功总笔数和总金额与查询报文中的结果是否一致
            FdcutpaydetlDao detldao = FdcutpaydetlDaoFactory.create();

//            String sqlwhere = "(billstatus = " + FDBillStatus.SEND_SUCCESS  +    //报文已发送
//                   "or billstatus = " + FDBillStatus.CUTPAY_FAILD  +  //扣款失败
//                   "or billstatus = " + FDBillStatus.CUTPAY_SUCCESS + ")";  //扣款成功
            String sqlwhere = "billstatus = " + FDBillStatus.SEND_SUCCESS;    //报文已发送
            Fdcutpaydetl[] detls = detldao.findByDynamicWhere(sqlwhere, null);

/*
            int resultcount = Integer.parseInt(result.getSuccnt()) + Integer.parseInt(result.getFalcnt());
            if (detls.length != resultcount) {
                msgs.add("查询结果包中总笔数为" + resultcount + "条，与明细扣款记录笔数" + detls.length + "条不符！");
                return false;
            }

            BigDecimal totalamt = new BigDecimal("0");
            for (int i = 0; i < detls.length; i++) {
                totalamt = totalamt.add(detls[i].getGthtjhJhje());
            }

            if (totalamt.compareTo(new BigDecimal(result.getSucamt()).add(new BigDecimal(result.getFalamt()))) != 0) {
                msgs.add("批量查询结果包中总金额为" + result.getSucamt() + "条，与明细扣款记录的总金额" + totalamt.toString() + "条不符！");
                return false;
            }

*/

            //检查扣款失败记录的一致性
            for (int i = 0; i < failedcount; i++) {
//                sqlwhere = "cutpayactno = " + failrecord.get(i).getActnum().trim() + " and billstatus = " + FDBillStatus.SEND_SUCCESS;
                String sql = "cutpayactno = " + failrecord.get(i).getActnum().trim() + " and " + sqlwhere;
                Fdcutpaydetl[] faildetls = detldao.findByDynamicWhere(sql, null);
                if (faildetls.length == 0) {
                    msgs.add("批量查询结果包中帐号" + failrecord.get(i).getActnum() + "对应的扣款记录不存在！");
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
                        msgs.add("批量查询结果包中帐号金额：" + failrecord.get(i).getActnum()+" "+failrecord.get(i).getTxnamt() + "对应的扣款记录不存在！");
                        return false;
                    }

                    //TODO: 未处理扣款记录中两条以上帐号金额相同记录的情况
                }
*/
            }

        } catch (Exception e) {
            msgs.add("对批量查询结果包进行核对时出现问题，请查询");
            Debug.debug(e);
            throw new Exception(e);
        }
        return true;
    }

    /*
   根据当前通讯包的流水号，更新房贷明细表FDCUTPAYDETL明细记录状态
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
                cutpaydetls[i].setBillstatus(status);//设置状态
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
    根据当前通讯包的流水号，更新消费信贷明细表XFACTCUTPAYDETL明细记录状态
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
                cutpaydetls[i].setBillstatus(status);//设置状态
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
   功能：按计划扣款情况下，检查并更新房贷系统数据
   1、检查数据不一致情况
   2、更新房贷系统3个表
    */


}