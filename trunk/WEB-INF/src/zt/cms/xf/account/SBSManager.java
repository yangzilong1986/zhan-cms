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
    ��ѯ����ϵͳ�Ŀۿ��¼���Դ��۳ɹ��ļ�¼��ʽ���SBS���ʴ���
    ���سɹ��������
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

                    list.add(txndate);          //��������
                    list.add(StringUtils.rightPad(cutpaydetls[i].getSeqno(), 18, ' '));      //������ˮ��

                    String account = StringUtils.rightPad(cutpaydetls[i].getGthtbZhbh(), 22, ' ');
                    list.add(account);//�����ʻ�                 22λ�����㲹�ո�

                    list.add("+" + StringUtils.leftPad(cutpaydetls[i].getGthtjhBjje().setScale(2).toString(), 16, '0'));     //������
                    list.add("+" + StringUtils.leftPad(cutpaydetls[i].getGthtjhLxje().setScale(2).toString(), 16, '0'));     //��Ϣ���
                    list.add("+0000000000000.00");     //ΥԼ����
                    list.add("+0000000000000.00");     //�����ѽ��

                    /*
                    20091020  zhanrui
                    Ϊ֧�ּ��ϡ��������ص����пۿ��޸ı�ע����ʽ
                     */
                    //list.add("                              ");//ժҪ    30
                    if (cutpaydetls[i].getBankcd() == null) {
                        list.add("                              ");//ժҪ    30
                    } else {
                        list.add(StringUtils.rightPad(cutpaydetls[i].getBankcd(), 30, ' '));//ժҪ    30
                    }


                    byte[] buffer = CtgManager.processAccount(list, "a542");

                    byte[] bFormcode = new byte[4];
                    System.arraycopy(buffer, 21, bFormcode, 0, 4);
                    String formcode = new String(bFormcode);

                    if (!formcode.equals("T531")) {     //�쳣�������
                        cutpaydetls[i].setRemark("SBS����ʧ�ܣ�FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(FDBillStatus.SBS_ACCOUNT_FAILD);
                    } else {
                        cutpaydetls[i].setRemark("SBS���ʳɹ���FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(FDBillStatus.SBS_ACCOUNT_SUCCESS);
                        rtn++;
                    }

                    cutpaydetlpk.setSeqno(cutpaydetls[i].getSeqno());
                    detlDao.update(cutpaydetlpk, cutpaydetls[i]);
                }

            } else {
                logger.info("�޷���SBS������������ϸ�ʵ���¼");
                rtn = 0;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new java.io.IOException("SBSϵͳ������ͨ�����ӳ�ʱ��");
        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception(e);
        }
        return rtn;
    }

    /*
    ��ѯ�����Ŵ�ϵͳ�Ŀۿ��¼���Դ��۳ɹ��ļ�¼��ʽ���SBS���ʴ���
    ���سɹ��������
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

                    list.add(txndate);          //��������
                    list.add(StringUtils.rightPad(cutpaydetls[i].getJournalno(), 18, ' '));      //������ˮ��

                    String account = StringUtils.rightPad(cutpaydetls[i].getClientact(), 22, ' ');
                    list.add(account);//�����ʻ�                 22λ�����㲹�ո�


                    String principal = df.format(cutpaydetls[i].getPrincipalamt());
                    String servicecharge = df.format(cutpaydetls[i].getServicechargefee());
                    String breachfee = df.format(cutpaydetls[i].getBreachfee());
                    String latefee = df.format(cutpaydetls[i].getLatefee());

                    if (cutpaydetls[i].getBilltype().equals("0")) { //�����ʵ�
                        list.add("+" + StringUtils.leftPad(principal, 16, '0'));     //������
                        list.add("+0000000000000.00");     //ΥԼ����
                        list.add("+0000000000000.00");     //���ɽ���
                        list.add("+" + StringUtils.leftPad(servicecharge, 16, '0'));     //�����ѽ��
                    } else {
                        list.add("+0000000000000.00");     //������
                        list.add("+" + StringUtils.leftPad(breachfee, 16, '0'));     //ΥԼ����
                        list.add("+" + StringUtils.leftPad(latefee, 16, '0'));     //���ɽ���
                        list.add("+0000000000000.00");     //�����ѽ��
                    }
                    //TODO: ������
                    list.add("105                           ");//ժҪ

                    byte[] buffer = CtgManager.processAccount(list, "a541");

                    byte[] bFormcode = new byte[4];
                    System.arraycopy(buffer, 21, bFormcode, 0, 4);
                    String formcode = new String(bFormcode);

                    if (!formcode.equals("T531")) {     //�쳣�������
                        cutpaydetls[i].setFailurereason("SBS����ʧ�ܣ�FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CORE_FAILED);
                    } else {
                        cutpaydetls[i].setFailurereason("SBS���ʳɹ���FORMCODE=" + formcode);
                        cutpaydetls[i].setBillstatus(XFBillStatus.BILLSTATUS_CORE_SUCCESS);
                        rtn++;
                    }

                    cutpaydetlpk.setJournalno(cutpaydetls[i].getJournalno());
                    detlDao.update(cutpaydetlpk, cutpaydetls[i]);
                }

            } else {
                logger.info("�޷���SBS������������ϸ�ʵ���¼");
                rtn = 0;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new java.io.IOException("SBSϵͳ������ͨ�����ӳ�ʱ��");
        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception(e);
        }
        return rtn;
    }


}
