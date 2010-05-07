package zt.cms.xf.account;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.net.URL;
import java.net.URLEncoder;
import java.io.*;
import java.math.BigDecimal;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.event.*;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.utils.Debug;
import zt.platform.user.UserManager;
import zt.platform.cachedb.ConnectionManager;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfifbankdetlDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfifbankdetlDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.constant.*;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.ssl.MyX509TrustManager;
//import zt.cms.xf.common.ssl.MySecureProtocolSocketFactory;
import zt.cms.xf.gateway.Bill99Manager;
import zt.cms.xf.gateway.AlipayManager;

import zt.cmsi.mydb.MyDB;
import org.apache.commons.lang.StringUtils;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.protocol.Protocol;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.beanutils.BeanUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.Payment;
import com.zt.util.PropertyManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.sql.rowset.CachedRowSet;


/**
 * ��ǰ����ҵ����
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFPreCutpayDateLink extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFPreCutpayDateLink");

    private class PreCutPay {
        public String paybackdate;      //��ǰ����Լ��������
        public String contractno;   //��ͬ��
        public int duration;       //δ��������
        public Double paybackamt;      //δ����Լ�������
        public Double principalamt;      //δ���屾��
        public Double servicechargefee;   //δ����������
        public Double preservicechargefee;   //��ǰ����������
        public Double totalamt;   //��ǰ�����ܽ��
        public String clientname;    //�ͻ�����
    }

    private PreCutPay precutpay;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {

        this.precutpay = new PreCutPay();
        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        precutpay.paybackdate = sFmt.format(new Date());

        precutpay.contractno = (String) ctx.getAttribute("CONTRACTNO");
        if (precutpay.contractno == null) {
            return -1;
        }

        instance.setValue("PAYBACKDATE", precutpay.paybackdate);
        instance.setValue("SYSTEMDATE", precutpay.paybackdate);

        instance.getFormBean().getElement("PRECUTPAYBUTTON").setComponetTp(6); //������ǰ�����ύ��ťΪ����
        instance.getFormBean().getElement("PRECUTPAYCONFIRMBUTTON").setComponetTp(6); //������ǰ����ȷ�ϰ�ťΪ����
        instance.getFormBean().getElement("PRECUTPAYDISMISSALBUTTON").setComponetTp(6); //������ǰ����ذ�ťΪ����


        String lastbutton = (String) ctx.getAttribute("BUTTONNAME");
        if (lastbutton == null) {
            return -1;
        }

        if (lastbutton.equals("DOPRECUTPAYJB")) {
            instance.getFormBean().getElement("PRECUTPAYBUTTON").setComponetTp(15);
        } else if (lastbutton.equals("DOPRECUTPAYFH")) {
            instance.getFormBean().getElement("PRECUTPAYCONFIRMBUTTON").setComponetTp(15);
//            instance.getFormBean().getElement("PRECUTPAYDISMISSALBUTTON").setComponetTp(15);
        }

        ctx.removeAttribute("BUTTONNAME");
        ctx.removeAttribute("CONTRACTNO");

        getPreCutPayInfo(ctx, conn, instance, msgs);

        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_BREAK_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        precutpay.paybackdate = ctx.getParameter("PAYBACKDATE");

        if (button.equals("PRECUTPAYBUTTON")) {  //��ǰ����ҵ�������
            int rtn = 0;
            try {
                UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                if (doPreCutPayButton(um.getUserName()) == 0) {
                    msgs.add("��ǰ���������ѳɹ��ύ��");
                    rtn = 0;
                } else {
                    msgs.add("��ǰ�����ύ�������⣬���ѯ��");
                    rtn = -1;
                }
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                return rtn;
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "ϵͳ���г������⣬���ѯ��");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                return -1;
            }
        }

        if (button.equals("PRECUTPAYCONFIRMBUTTON")) {  //��ǰ�����
            int rtn = 0;
            try {
                UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
                if (doPreCutPayConfirmButton(um.getUserName(), conn) == 0) {
                    msgs.add("��ǰ�������ͨ����");
                    rtn = 0;
                } else {
                    msgs.add("��ǰ������˳������⣬���ѯ��");
                    rtn = -1;
                }
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                return rtn;
            } catch (Exception e) {
                Debug.debug(e);
                ctx.setRequestAtrribute("msg", "ϵͳ���г������⣬���ѯ��");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                return -1;
            }
        }

        return 0;
    }

    /*
    ��ѯ��ͬ�������
     */
    private void getPreCutPayInfo(SessionContext ctx, DatabaseConnection conn,
                                  FormInstance instance, ErrorMessages msgs) {

        try {
            String sql = "select count(*),sum(t.paybackamt),sum(t.principalamt),sum(t.servicechargefee)" +
                    " from xfactcutpaymain t where t.contractno = '" + precutpay.contractno +
                    "' and t.closedcd = '0' ";

            RecordSet rs = conn.executeQuery(sql);

            if (rs.next()) {
                precutpay.duration = rs.getInt(0);
                precutpay.paybackamt = rs.getDouble(1);
                precutpay.principalamt = rs.getDouble(2);
                precutpay.servicechargefee = rs.getDouble(3);
                precutpay.preservicechargefee = Airth.round(Airth.mul(precutpay.principalamt, XFCommon.COMMON_PRECUTPAYRATE), 2);
            } else {
                msgs.add("δ�ҵ���Ӧ�����ʵ���Ϣ��" + precutpay.contractno);
                throw new Exception("δ�ҵ���Ӧ�����ʵ���Ϣ��" + precutpay.contractno);
            }

            precutpay.preservicechargefee = Airth.round(Airth.mul(precutpay.principalamt, XFCommon.COMMON_PRECUTPAYRATE), 2);
            precutpay.totalamt = Airth.add(precutpay.paybackamt, precutpay.preservicechargefee);

            instance.setValue("CONTRACTNO", precutpay.contractno);
            instance.setValue("UNCLEAREDDURATION", precutpay.duration);
            instance.setValue("PRINCIPALAMT", precutpay.principalamt.toString());
            instance.setValue("SERVICECHARGEFEE", precutpay.servicechargefee.toString());

            instance.setValue("PRESERVICECHARGEFEE", precutpay.preservicechargefee.toString());
            instance.setValue("TOTALAMT", precutpay.totalamt.toString());

            sql = "select duration,clientname from xfcontract where contractno = '" + precutpay.contractno + "'";
            rs = conn.executeQuery(sql);
            if (rs.next()) {
                precutpay.duration = rs.getInt(0);
                precutpay.clientname = rs.getString(1);
            } else {
                msgs.add("δ�ҵ���Ӧ�ĺ�ͬ��Ϣ��" + precutpay.contractno);
                throw new Exception("δ�ҵ���Ӧ�ĺ�ͬ��Ϣ��" + precutpay.contractno);
            }
            instance.setValue("DURATION", precutpay.duration);
            instance.setValue("CLIENTNAME", precutpay.clientname);
        } catch (Exception e) {
            Debug.debug(e);
            throw new RuntimeException(e);
        }

    }

    /*
   ��ǰ����ҵ�����󸴺ˣ��������ʵ����޸ĺ�ͬ״̬
    */
    private int doPreCutPayConfirmButton(String username, DatabaseConnection conn) throws Exception {

        try {
            conn.setAuto(false);

            XfcontractDao contractdao = XfcontractDaoFactory.create(conn.getConnection());
            Xfcontract contract = contractdao.findByPrimaryKey(precutpay.contractno);

            //���ʵ�����
            XfactcutpaymainDao cutpaydao = XfactcutpaymainDaoFactory.create(conn.getConnection());
            Xfactcutpaymain cutpay = new Xfactcutpaymain();
//            XfactcutpaymainPk cutpaypk = new XfactcutpaymainPk(contractno);
            BigDecimal poa = contract.getDuration();

            //�������һ�����ʵ�
            XfactcutpaymainPk cutpaypk = new XfactcutpaymainPk(precutpay.contractno, poa);
            Xfactcutpaymain oldcutpay = cutpaydao.findByPrimaryKey(cutpaypk);

            BeanUtils.copyProperties(cutpay, oldcutpay);


            poa = poa.add(new BigDecimal(1));

            //
            cutpay.setContractno(precutpay.contractno);
            cutpay.setPoano(poa);
            cutpay.setPbstatus("0");

            Date paybackdate = new SimpleDateFormat("yyyyMMdd").parse(precutpay.paybackdate);
            cutpay.setPaybackdate(paybackdate);

            cutpay.setStartdate(null);
            cutpay.setCloseddate(null);
            cutpay.setOverduecd("0");
            cutpay.setChargeoffcd("0");
            cutpay.setClosedcd("0");

/*
            //��ǰ����Լ����������δ����Ľ�� + �����
            cutpay.setPaybackamt(new BigDecimal(precutpay.totalamt));
            cutpay.setPrincipalamt(new BigDecimal(precutpay.principalamt));
            Double totalfee = Airth.add(precutpay.servicechargefee, precutpay.preservicechargefee);
            if (Airth.sub(precutpay.totalamt , Airth.add(precutpay.principalamt , totalfee)) != 0) {
                throw new Exception("���˶Գ������⣡");
            }
            //��ǰ��������  + δ�����������
            cutpay.setServicechargefee(new BigDecimal(totalfee));
*/
            //��ǰ����Լ�����������
            cutpay.setPaybackamt(new BigDecimal(precutpay.preservicechargefee));
            cutpay.setPrincipalamt(new BigDecimal(0));
            //��ǰ��������
            cutpay.setServicechargefee(new BigDecimal(precutpay.preservicechargefee));

            cutpay.setOdbPaybackamt(new BigDecimal(0));
            cutpay.setOdbPaybackdate(null);
            cutpay.setOdbBreachfee(new BigDecimal(0));
            cutpay.setOdbLatefee(new BigDecimal(0));
            cutpay.setOdbChargeoffcd("0");
            cutpay.setOdbClosedcd("0");
            cutpay.setOdbStartdate(null);
            cutpay.setOdbCloseddate(null);

            //��ǰ������Ϣ����
            cutpay.setPrecutpaycd("1");
//                   cutpay.setPrecutpaydate();
            //
            cutpaydao.insert(cutpay);

            //��������δ�����ʵ���
            //TODO:
            String sqlwhere = " contractno = '" + precutpay.contractno + "' and closedcd = '0' ";
            Xfactcutpaymain[] cutpays = cutpaydao.findByDynamicWhere(sqlwhere, null);

            for (int i = 0; i < cutpays.length; i++) {
                cutpays[i].setPrecutpaycd("1");
                cutpays[i].setPrecutpaydate(paybackdate);
                cutpaypk.setContractno(cutpays[i].getContractno());
                cutpaypk.setPoano(cutpays[i].getPoano());
                cutpaydao.update(cutpaypk, cutpays[i]);
            }


            contract.setCstatus(XFContractStatus.TIQIANHUANKUAN_TONGGUO);
            contract.setUpdatorid(username);
            contract.setUpdateform("��ǰ�������FORM");
            contract.setUpdatedate(new Date());

            contractdao.update(new XfcontractPk(precutpay.contractno), contract);

            conn.commit();
            return 0;
        } catch (Exception e) {
//            msgs.add("�����쳣��" + e.getMessage());
            conn.rollback();
            Debug.debug(e);
            throw new Exception(e);
        }
    }

    /*
   ��ǰ����ҵ�����ֻ�޸ĺ�ͬ״̬
    */
    private int doPreCutPayButton(String username) throws Exception {

        try {
            XfcontractDao dao = XfcontractDaoFactory.create();
            Xfcontract contract = dao.findByPrimaryKey(precutpay.contractno);

            contract.setCstatus(XFContractStatus.TIQIANHUANKUAN_DAISHENHE);
            contract.setUpdatorid(username);
            contract.setUpdateform("��ǰ�����ύFORM");
            contract.setUpdatedate(new Date());

            dao.update(new XfcontractPk(precutpay.contractno), contract);
            return 0;
        } catch (Exception e) {
//            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            throw new Exception(e);
        }
    }


}