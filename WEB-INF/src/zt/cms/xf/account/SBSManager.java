package zt.cms.xf.account;

import zt.cms.xf.gateway.BatchQueryResult;
import zt.cms.xf.gateway.CtgManager;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.dto.Fdcutpaydetl;
import zt.cms.xf.common.dto.FdcutpaydetlPk;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.dto.XfactcutpaydetlPk;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.utils.Debug;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-7-16
 * Time: 15:37:05
 * To change this template use File | Settings | File Templates.
 */
public class SBSManager {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.SBSManager");

    /*
    查询房贷系统的扣款记录表，对代扣成功的记录逐笔进行SBS入帐处理
    返回成功处理笔数
     */
    public int processFDAccount(String txndate, ErrorMessages msgs) throws Exception {

        int rtn = 0;

        try {
            String sql = "billstatus = " + FDBillStatus.CUTPAY_SUCCESS;

            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            FdcutpaydetlPk cutpaydetlpk = new FdcutpaydetlPk();

            if (cutpaydetls != null && cutpaydetls.length > 0) {
                for (int i = 0; i < cutpaydetls.length; i++) {
                    List list = new ArrayList();

                    list.add(txndate);          //交易日期
                    list.add(StringUtils.rightPad(cutpaydetls[i].getSeqno(), 18, ' '));      //交易流水号

                    String account = StringUtils.rightPad(cutpaydetls[i].getGthtbZhbh(), 22, ' ');
                    list.add(account);//贷款帐户                 22位，不足补空格

                    list.add("+" + StringUtils.leftPad(cutpaydetls[i].getGthtjhBjje().setScale(2).toString(), 16, '0'));     //本金金额
                    list.add("+" + StringUtils.leftPad(cutpaydetls[i].getGthtjhLxje().setScale(2).toString(), 16, '0'));     //利息金额
                    list.add("+0000000000000.00");     //违约金金额
                    list.add("+0000000000000.00");     //手续费金额

                    /*
                    20091020  zhanrui
                    为支持济南、重庆两地的中行扣款修改备注处理方式
                     */
                    //list.add("                              ");//摘要    30
                    if (cutpaydetls[i].getBankcd() == null) {
                        list.add("                              ");//摘要    30
                    } else {
                        list.add(StringUtils.rightPad(cutpaydetls[i].getBankcd(), 30, ' '));//摘要    30
                    }


                    byte[] buffer = CtgManager.processAccount(list, "a542");

                    byte[] bFormcode = new byte[4];
                    System.arraycopy(buffer, 21, bFormcode, 0, 4);
                    String formcode = new String(bFormcode);

                    if (!formcode.equals("T531")) {     //异常情况处理
                        cutpaydetls[i].setRemark("SBS入帐失败：FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(FDBillStatus.SBS_ACCOUNT_FAILD);
                    } else {
                        cutpaydetls[i].setRemark("SBS入帐成功：FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(FDBillStatus.SBS_ACCOUNT_SUCCESS);
                        rtn++;
                    }

                    cutpaydetlpk.setSeqno(cutpaydetls[i].getSeqno());
                    detlDao.update(cutpaydetlpk, cutpaydetls[i]);
                }

            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
                rtn = 0;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new java.io.IOException("SBS系统不能连通或连接超时！");
        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception(e);
        }
        return rtn;
    }

    /*
    查询消费信贷系统的扣款记录表，对代扣成功的记录逐笔进行SBS入帐处理
    返回成功处理笔数
     */
    public int processXFCutPayAccount(String txndate, ErrorMessages msgs) throws Exception {

        int rtn = 0;

        try {
            String sql = "billstatus = " + XFBillStatus.BILLSTATUS_CUTPAY_SUCCESS;

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create();
            Xfactcutpaydetl[] cutpaydetls = detlDao.findByDynamicWhere(sql, null);
            XfactcutpaydetlPk cutpaydetlpk = new XfactcutpaydetlPk();

            DecimalFormat df = new DecimalFormat("#############0.00");

            if (cutpaydetls != null && cutpaydetls.length > 0) {
                for (int i = 0; i < cutpaydetls.length; i++) {
                    List list = new ArrayList();

                    list.add(txndate);          //交易日期
                    list.add(StringUtils.rightPad(cutpaydetls[i].getJournalno(), 18, ' '));      //交易流水号

                    String account = StringUtils.rightPad(cutpaydetls[i].getClientact(), 22, ' ');
                    list.add(account);//贷款帐户                 22位，不足补空格


                    String principal = df.format(cutpaydetls[i].getPrincipalamt());
                    String servicecharge = df.format(cutpaydetls[i].getServicechargefee());
                    String breachfee = df.format(cutpaydetls[i].getBreachfee());
                    String latefee = df.format(cutpaydetls[i].getLatefee());

                    if (cutpaydetls[i].getBilltype().equals("0")) { //正常帐单
                        list.add("+" + StringUtils.leftPad(principal, 16, '0'));     //本金金额
                        list.add("+0000000000000.00");     //违约金金额
                        list.add("+0000000000000.00");     //滞纳金金额
                        list.add("+" + StringUtils.leftPad(servicecharge, 16, '0'));     //手续费金额
                    } else {
                        list.add("+0000000000000.00");     //本金金额
                        list.add("+" + StringUtils.leftPad(breachfee, 16, '0'));     //违约金金额
                        list.add("+" + StringUtils.leftPad(latefee, 16, '0'));     //滞纳金金额
                        list.add("+0000000000000.00");     //手续费金额
                    }
                    //TODO: 参数化
                    list.add("105                           ");//摘要

                    byte[] buffer = CtgManager.processAccount(list, "a541");

                    byte[] bFormcode = new byte[4];
                    System.arraycopy(buffer, 21, bFormcode, 0, 4);
                    String formcode = new String(bFormcode);

                    if (!formcode.equals("T531")) {     //异常情况处理
                        cutpaydetls[i].setFailurereason("SBS入帐失败：FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CORE_FAILED);
                    } else {
                        cutpaydetls[i].setFailurereason("SBS入帐成功：FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CORE_SUCCESS);
                        rtn++;
                    }

                    cutpaydetlpk.setJournalno(cutpaydetls[i].getJournalno());
                    detlDao.update(cutpaydetlpk, cutpaydetls[i]);
                }

            } else {
                logger.info("无符合SBS入帐条件的明细帐单记录");
                rtn = 0;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new java.io.IOException("SBS系统不能连通或连接超时！");
        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception(e);
        }
        return rtn;
    }


}
