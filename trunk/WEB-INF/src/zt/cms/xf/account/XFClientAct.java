package zt.cms.xf.account;

/**
 * ���ڿۿ�ƻ�����.
 * User: zhanrui
 * Date: 2009-3-25
 * Time: 17:30:25
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.utils.Debug;
import zt.platform.user.UserManager;
import zt.cmsi.mydb.MyDB;

import zt.cms.xf.common.dao.XfactpaydetlDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.factory.XfactpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.dto.Xfactpaydetl;
import zt.cms.xf.common.dto.XfcontractPk;
import zt.cms.xf.common.dto.Xfcontract;
import zt.cms.xf.common.dto.XfactpaydetlPk;
import zt.cms.xf.common.constant.XFContractStatus;
import zt.cms.xf.common.constant.XFFlowStatus;

import java.math.BigDecimal;

public class XFClientAct extends FormActions {

    private Xfactpaydetl xfactpaydetl = new Xfactpaydetl();
    private String contractno = null;
    private int flowStatus;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        String fs = (String)ctx.getAttribute("FLOWSTATUS");
        if (fs != null) {
            if (fs.equals(XFFlowStatus.FANGKUAN_JINGBAN)) {
                flowStatus = 1;
            } else if (fs.equals(XFFlowStatus.FANGKUAN_WEIHU)) {
                flowStatus = 2;
            } else if (fs.equals(XFFlowStatus.FANGKUAN_FUHE)) {
                flowStatus = 3;
            }
        }

        contractno = ctx.getParameter("CONTRACTNO");
        instance.setValue("CONTRACTNO", contractno);

        xfactpaydetl.setContractno(ctx.getParameter("CONTRACTNO"));
        BigDecimal amt = new BigDecimal(ctx.getParameter("CONTRACTAMT"));
        xfactpaydetl.setPayamt(amt);
        //xfactpaydetl.setPayact(ctx.getParameter(""));
        xfactpaydetl.setRecvact(ctx.getParameter("PARTNERACT"));
        xfactpaydetl.setRecvbankid(ctx.getParameter("PARTNERBANKID"));
        xfactpaydetl.setRecvbankno(ctx.getParameter("PARTNERBANKNO"));
        //xfactpaydetl.setRecvbankname(ctx.getParameter("PARTNERBANKNAME"));


        instance.setHTMLFocus("CLIENTACT");
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);


        return 0;
    }

    public int preUpdate(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
//        instance.setValue("CHECKDATE", "#SYSDATE#");
//        instance.setValue("UPDATEDATE", "#SYSDATE#");
//        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CHECKDATE", "#SYSDATE#");
//        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "UPDATEDATE", "#SYSDATE#");
//
//        if (isJBStatus) {
//            instance.setValue("CSTATUS", "1");
//             assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CSTATUS", "1");
//        }
//        //�������ԱID
//        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//        instance.getFormBean().getElement("CHECKERID").setDefaultValue(um.getUserName());
//        instance.getFormBean().getElement("UPDATORID").setDefaultValue(um.getUserName());
//        instance.getFormBean().getElement("UPDATEFORM").setDefaultValue("XFCONTRACTPAGE");

        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {


        if (doCreditIssue(ctx, conn, instance, msgs) == 0) {
            if (flowStatus == 1) {
                ctx.setRequestAtrribute("msg", "�ͻ��ʺ��ύ��ɣ������ɷſ��¼��");
            } else if (flowStatus == 2) {
                ctx.setRequestAtrribute("msg", "�ſ��¼ά����ɣ�");
            }
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");
            instance.setReadonly(true);

            return 0;
        } else {
            return -1;
        }
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        if (button == null) {
            return 0;
        }

        if (button.equals("PAYSCHEDULEBUTTON")) {
            //generateNewPaySchedule();
            ctx.setAttribute("CONTRACTNO", ctx.getParameter("CONTRACTNO"));
            trigger(manager, "XFCONTRACTPAYPAGE", null);
            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
//        if (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
//            trigger(manager, "XFCONTRACTPAYPAGE", null);
//            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
//        }

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            String contractno = ctx.getParameter("CONTRACTNO");
            if (contractno != null) {
                RecordSet rs = conn.executeQuery("select cstatus from xfcontract where contractno = " + contractno);
                if (rs.next()) {
                    String cstatus = rs.getString(0);
                    if (cstatus != null) {
                        if (Integer.parseInt(cstatus) < 2) {  //TODO: 2
                            ctx.setRequestAtrribute("msg", "��ͬ��Ϣδ���ˣ�");
                            ctx.setTarget("/showinfo.jsp");
                            instance.setReadonly(true);
                            rs.close();
                            return -1;
                        }
                        if (Integer.parseInt(cstatus) > 5) {  //TODO: 2
                            ctx.setRequestAtrribute("msg", "�˺�ͬ�ѷſ");
                            ctx.setTarget("/showinfo.jsp");
                            instance.setReadonly(true);
                            rs.close();
                            return -1;
                        }

                    }
                }
                rs.close();
            }

            trigger(manager, "XFCONTRACTPAYPAGE", null);
        }

        return 0;
    }


    private int doCreditIssue(SessionContext ctx, DatabaseConnection conn,
                              FormInstance instance, ErrorMessages msgs) {


        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

        xfactpaydetl.setPayact(ctx.getParameter("CLIENTACT"));
        //���÷�����ˮ״̬��������
        xfactpaydetl.setPaystatus("1"); //TODO ����


        //java.sql.Date sqlDate = new java.sql.Date();
        java.util.Date currentdate = new java.util.Date();
        xfactpaydetl.setPaydate(currentdate);     //TODO: ���ݿ�ʱ�䣿
        xfactpaydetl.setOperatorid(um.getUserName());
        xfactpaydetl.setOperatedate(currentdate); //TODO: ���ݿ�ʱ�䣿
        //xfactpaydetl.setPaydate(new java.sql.Timestamp(date().getTime());

        try {
            MyDB.getInstance().addDBConn(conn);


            /*
            �˴�Ŀǰֻ���һ�ݺ�ͬ����һ���ſ���ˮ��¼��������д���
             �����������״̬��
             1����ͬ״̬Ϊ����ͬǩ��������ɡ� ��
                insert �·ſ���ˮ��¼
             2����ͬ״̬Ϊ����ͬ�ſ����ˡ��򡰺�ͬ�ſ��������ء���
                update �˺�ͬ�����Ӧ�ķſ���ˮ
             */

            XfactpaydetlDao dao = XfactpaydetlDaoFactory.create(conn.getConnection());
            if (flowStatus == 1) {    //����״̬���·ſ
                dao.insert(xfactpaydetl);
            } else if (flowStatus == 2) { //ά��״̬
                Xfactpaydetl[] paydetl = dao.findWhereContractnoEquals(contractno);
                if (paydetl.length < 1) {
                    msgs.add("δ�ҵ���ά���ķſ��¼�����ѯ��");
                    return -1;
                }
                XfactpaydetlPk payPk = new XfactpaydetlPk(paydetl[0].getJournalno());
                xfactpaydetl.setJournalno(paydetl[0].getJournalno());
                dao.update(payPk, xfactpaydetl);
            }


            /*
            ���º�ͬ�����״̬��Ϣ
             */
            XfcontractDao contractDao = XfcontractDaoFactory.create(conn.getConnection());
            XfcontractPk pk = new XfcontractPk(contractno);
            Xfcontract xfcontract = contractDao.findByPrimaryKey(pk);

            //��ͬ״̬��Ϊ����ˡ�
            xfcontract.setCstatus(XFContractStatus.FANGKUAN_DAISHENHE);
            contractDao.update(pk, xfcontract);

            return 0;
        } catch (Exception e) {
            msgs.add("�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }

}