package zt.cms.xf.account;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 9:19:52
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.math.BigDecimal;

import zt.platform.form.util.SessionAttributes;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;
import zt.cmsi.mydb.MyDB;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.dao.XfactcutpaydetlDao;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaydetlDaoFactory;
import zt.cms.xf.common.dto.Xfactcutpaymain;
import zt.cms.xf.common.dto.Xfactcutpaydetl;
import zt.cms.xf.common.dto.XfactcutpaydetlPk;
import zt.cms.xf.common.dto.XfactcutpaymainPk;
import zt.cms.xf.common.constant.XFBillStatus;
import zt.cms.xf.common.constant.XFBankCode;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.gateway.CtgManager;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class XFActCutPayDetlList extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayDetlList");

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        // ctx.setAttribute("SUPERFORMID", ctx..getAttribute());
//        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
//
//            trigger(manager, "XFCONTRACTLINK", null);
//            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
//        }
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "XFACTCUTPAYDETLPAGE", null);
        }

        if (button.equals("CCBCUTPAYBUTTON")
                || button.equals("DOT3BUTTON")) {
            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

            //δѡ�д������¼������£�Ĭ��Ϊ��ȫ�������м�¼�����д���

//            if (recordnos == null) {
//                ctx.setRequestAtrribute("msg", "δѡ�д������¼��");
//                ctx.setRequestAtrribute("flag", "0");
//                ctx.setRequestAtrribute("isback", "0");
//                ctx.setTarget("/showinfo.jsp");
//                return -1;
//            }

            if (recordnos == null) {
                ctx.removeAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            }
            ctx.setAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME, recordnos);
            ctx.setAttribute("BUTTONNAME", button);
            ctx.setAttribute("BANKCODE", XFBankCode.BANKCODE_CCB);
            trigger(manager, "XFDATELINK", null);
        }


        if (button != null && button.equals("BILLSBUTTON")) {
            //doGenerateBills(ctx, conn, instance, msgs);
            trigger(manager, "XFACTCUTPAYLINK", null);
        }

        //��ǰ�����ʵ�����
        if (button != null && button.equals("PRECUTPAYBILLSBUTTON")) {
            trigger(manager, "XFACTPRECUTPAYLINK", null);
        }

//        if (button != null && button.equals("SUCCESSBUTTON")) {
//            ctx.setAttribute("BUTTON_FLAG", "SUCCESSBUTTON");
//            trigger(manager, "XFACTCUTPAYDETLLISTWHLINK", null);
//        }
//
//        if (button != null && button.equals("FAILBUTTON")) {
//            ctx.setAttribute("BUTTON_FLAG", "FAILBUTTON");
//            trigger(manager, "XFACTCUTPAYDETLLISTWHLINK", null);
//        }

        if (button != null && button.equals("CHECKBUTTON")) {
            int rtn = doCheckButton(ctx, conn, instance, msgs);
            if (rtn == 0) {
                ctx.setRequestAtrribute("msg", "��˴���ɹ������ѯ��");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            } else if (rtn == -2) {
                ctx.setRequestAtrribute("msg", "δѡ�д����˼�¼�����ѯ��");
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            } else if (rtn == -1) {
                ctx.setRequestAtrribute("msg", "��˴���ʧ�ܣ����ѯ��");
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
            }

        }
        if (button != null && button.equals("ACCOUNTBUTTON")) {
            ctx.setAttribute("BUTTONNAME", "XFACCOUNTBUTTON");
            trigger(manager, "FDDATELINK", null);
        }


        //200910 ������ǩԼ�ӿڴ���

        //200910 ������ǩԼ���۷���
        if (button.equals("DOCCBBUTTON4SIGN")) {
            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

            //δѡ�д������¼������£�Ĭ��Ϊ��ȫ�������м�¼�����д���

//            if (recordnos == null) {
//                ctx.setRequestAtrribute("msg", "δѡ�д������¼��");
//                ctx.setRequestAtrribute("flag", "0");
//                ctx.setRequestAtrribute("isback", "0");
//                ctx.setTarget("/showinfo.jsp");
//                return -1;
//            }

            if (recordnos == null) {
                ctx.removeAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            }
            ctx.setAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME, recordnos);
            ctx.setAttribute("BUTTONNAME", button);
            //?
            ctx.setAttribute("BANKCODE", XFBankCode.BANKCODE_CCB);
            trigger(manager, "XFDATELINK", null);
        }



        return 0;
    }


    private int doCheckButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {

        String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);

        if (recordnos == null) {
            return -2;
        }

        List results = new ArrayList();

//        conn.setAuto(false);
//        conn.begin();

        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

        if (recordnos != null) {
            for (int i = 0; i < recordnos.length; i++) {
                try {
                    if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
                        String sql = "update xfactcutpaydetl set "
                                + " billstatus = \'" + XFBillStatus.BILLSTATUS_CHECKED    //��Ϊ�Ѹ���
                                + "\', checkerid= \'" + um.getUserName()
                                + "\', checkdate= sysdate"   //TODO: ʱ���ΪAS��ʱ��?
                                + " where " + recordnos[i];
                        logger.info(sql);

                        int thisresult = conn.executeUpdate(sql);
                        if (thisresult <= 0) {
                            results.add("" + i);
                            msgs.add("" + thisresult);
                        }
                    }
                }
                catch (Exception e) {
                    msgs.add(e.getMessage());
                    results.add("" + i);
                    e.printStackTrace();
                    return -1;
                }
            }

            if (results.size() > 0) {
                String err = "��";
                for (int i = 0; i < results.size(); i++) {
                    err += results.get(i);
                    if (results.size() < results.size() - 1) {
                        err += ",";
                    } else {
                        err += "��¼����ʧ��";
                    }
                }
                msgs.add(err);
                return -1;
            }
        }

        return 0;
    }

//    private int doAccountButton(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs) {
//
//        String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
//
//        if (recordnos == null) {
//            return -2;
//        }
//
//        List results = new ArrayList();
//
////        conn.setAuto(false);
////        conn.begin();
//
//        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//        Xfactcutpaydetl xfactcutpaydetl;
//
//        if (recordnos != null) {
//            // CtgManager ctg =new  CtgManager();
//
//            DecimalFormat df = new DecimalFormat("#############0.00");
//
//            for (int i = 0; i < recordnos.length; i++) {
//                try {
//                    if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
//                        String[] str = recordnos[i].split("=");
//                        xfactcutpaydetl = getCutPayDetlTblRecord(conn, StringUtils.strip(str[1], "\'"));
//
//                        //SBS ���˴���
//                        List list = new ArrayList();
//
//                        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
//                        String txdate = sFmt.format(new Date());
//
//                        list.add(txdate);              //��������
//                        list.add(xfactcutpaydetl.getJournalno());      //������ˮ��
//
//                        //TODO:!!
//                        xfactcutpaydetl.setPaybackact("801000029701105001");
//
//                        String account = StringUtils.rightPad(xfactcutpaydetl.getClientact(), 22, ' ');
//                        list.add(account);//�����ʻ�                 22λ�����㲹�ո�
//
//                        //TODO�������ֺܷ˶�
////                        String paybackamt = df.format(xfactcutpaydetl.getPaybackamt());
//
//                        String principal = df.format(xfactcutpaydetl.getPrincipalamt());
//                        String servicecharge = df.format(xfactcutpaydetl.getServicechargefee());
//                        String breachfee = df.format(xfactcutpaydetl.getBreachfee());
//                        String latefee = df.format(xfactcutpaydetl.getLatefee());
//
//                        if (xfactcutpaydetl.getBilltype().equals("0")) { //�����ʵ�
//                            list.add("+" + StringUtils.leftPad(principal, 16, '0'));     //������
//                            list.add("+0000000000000.00");     //ΥԼ����
//                            list.add("+0000000000000.00");     //���ɽ���
//                            list.add("+" + StringUtils.leftPad(servicecharge, 16, '0'));     //�����ѽ��
//                        } else {
//                            list.add("+0000000000000.00");     //������
//                            list.add("+" + StringUtils.leftPad(breachfee, 16, '0'));     //ΥԼ����
//                            list.add("+" + StringUtils.leftPad(latefee, 16, '0'));     //���ɽ���
//                            list.add("+0000000000000.00");     //�����ѽ��
//                        }
//                        list.add("                              ");//ժҪ
//
//                        CtgManager.processCtgTest(list);
//
//                        String sql = "update xfactcutpaydetl set "
//                                + " billstatus = \'" + XFBillStatus.BILLSTATUS_CORE_SUCCESS
//                                + "\', updatorid= \'" + um.getUserName()
//                                + "\', updatedate= sysdate"   //TODO: ʱ���ΪAS��ʱ��?
//                                + " where " + recordnos[i];
//
//                        logger.info(sql);
//
//                        int thisresult = conn.executeUpdate(sql);
//                        if (thisresult <= 0) {
//                            results.add("" + i);
//                            msgs.add("" + thisresult);
//                        }
//                    }
//                }
//                catch (Exception e) {
//                    msgs.add(e.getMessage());
//                    results.add("" + i);
//                    e.printStackTrace();
//                    return -1;
//                }
//            }
//
//            if (results.size() > 0) {
//                String err = "��";
//                for (int i = 0; i < results.size(); i++) {
//                    err += results.get(i);
//                    if (results.size() < results.size() - 1) {
//                        err += ",";
//                    } else {
//                        err += "��¼����ʧ��";
//                    }
//                }
//                msgs.add(err);
//                return -1;
//            }
//        }
//
//        return 0;
//    }

    private Xfactcutpaydetl getCutPayDetlTblRecord(DatabaseConnection conn, String journalno) throws XfactcutpaydetlDaoException {

        Connection sqlconn = null;

        try {

            MyDB.getInstance().addDBConn(conn);
            sqlconn = conn.getConnection();

            XfactcutpaydetlDao detlDao = XfactcutpaydetlDaoFactory.create(sqlconn);
            Xfactcutpaydetl xfactcutpaydetl = detlDao.findByPrimaryKey(journalno);

            return xfactcutpaydetl;

        } catch (Exception e) {
            Debug.debug(e);
            throw new XfactcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

}
