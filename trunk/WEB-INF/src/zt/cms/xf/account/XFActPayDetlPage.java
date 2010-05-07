package zt.cms.xf.account;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;
import zt.cmsi.mydb.MyDB;
import zt.cms.xf.common.dto.Xfactpaydetl;
import zt.cms.xf.common.dto.XfactpaydetlPk;
import zt.cms.xf.common.dto.Xfcontract;
import zt.cms.xf.common.dto.XfcontractPk;
import zt.cms.xf.common.dao.XfactpaydetlDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.factory.XfactpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.constant.XFContractStatus;

public class XFActPayDetlPage extends FormActions {

    private boolean isFHStatus = false;
    private String journalno;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs, EventManager manager, String parameter) {


        String superformid = (String) ctx.getAttribute("SUPERFORMID");

        
        instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //���ø��˰�ťΪ����
        instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //���÷ſ�ƻ���ťΪ����

        if (superformid.indexOf("FH") > 0) {
            isFHStatus = true;
            //20090805  ��ס��ˮ�������
            /*
            instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(15);
            instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(15);
            */
        } else {
            isFHStatus = false;
            instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //���ø��˰�ťΪ����
            instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //���÷ſ�ƻ���ťΪ����
        }

        journalno = ctx.getParameter("JOURNALNO");

        if (journalno != null) {
            instance.setValue("JOURNALNO", journalno);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        if (button != null && button.equals("CONFIRMBUTTON")) {
            if (doConfirm(ctx, conn, instance, msgs, true) == 0) {
                ctx.setRequestAtrribute("msg", "�˱ʷſ��¼�����ͨ����");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);

            } else {
                return -1;
            }
        } else if (button != null && button.equals("DISMISSALBUTTON")) {
            if (doConfirm(ctx, conn, instance, msgs, false) == 0) {
                ctx.setRequestAtrribute("msg", "�˱ʷſ��¼�Ѳ��أ�");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);

            } else {
                return -1;
            }
        }

        return 0;
    }

    private int doConfirm(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs, boolean isPass) {


        try {

            MyDB.getInstance().addDBConn(conn);

            XfactpaydetlDao paydetldao = XfactpaydetlDaoFactory.create(conn.getConnection());
            Xfactpaydetl xfactpaydetl = paydetldao.findByPrimaryKey(journalno);

            if (xfactpaydetl == null) {
                return -1;
            }

            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
            xfactpaydetl.setCheckerid(um.getUserName());
            xfactpaydetl.setCheckdate(new java.util.Date()); //TODO DB TIME

            XfactpaydetlPk paydetlpk = new XfactpaydetlPk();
            paydetlpk.setJournalno(journalno);

            XfcontractDao  contractdao = XfcontractDaoFactory.create(conn.getConnection());
            XfcontractPk contractpk = new XfcontractPk(xfactpaydetl.getContractno());
            Xfcontract xfcontract = contractdao.findByPrimaryKey(xfactpaydetl.getContractno());
            if (xfcontract == null) {
                msgs.add("�˱ʷſ��¼��Ӧ�ĺ�ͬ��Ϣδ�ҵ������ѯ��");
                return -1;
            }

            //TODO:����XFCONTRACT���ֶΣ�����ž��츴����Աʱ����Ϣ
            //xfcontract.(um.getUserName());
            //xfcontract.setCheckdate(new java.util.Date());



            if (isPass) {
                xfactpaydetl.setPaystatus("3"); //����״̬Ϊ�����ͨ����  TODO:CONSTANT
            } else {
                xfactpaydetl.setPaystatus("2"); //����״̬Ϊ�����ء�  TODO:CONSTANT
            }
            paydetldao.update(paydetlpk, xfactpaydetl);


            /*
            ��ͬ״̬����
             */

            if (isPass) {
                xfcontract.setCstatus(XFContractStatus.FANGKUAN_TONGGUO); //����״̬Ϊ���ſ�ͨ����
            } else {
                xfcontract.setCstatus(XFContractStatus.FANGKUAN_BOHUI); //����״̬Ϊ���ſ�ء�
            }
            contractdao.update(contractpk, xfcontract);

            return 0;
        } catch (Exception e) {
            msgs.add("ϵͳ�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


}
