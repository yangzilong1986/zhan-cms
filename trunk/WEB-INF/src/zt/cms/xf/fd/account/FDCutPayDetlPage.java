package zt.cms.xf.fd.account;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;
import zt.platform.utils.Debug;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.exceptions.FdcutpaydetlDaoException;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FDCutPayDetlPage extends FormActions {

    private String seqno;
    private static Log logger = LogFactory.getLog(FDDateLink.class);


    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs, EventManager manager, String parameter) {


//        String superformid = (String) ctx.getAttribute("SUPERFORMID");
        String superbutton = (String) ctx.getAttribute("BUTTONNAME");

        instance.getFormBean().getElement("SETWDCUTPAYSUCCESS").setComponetTp(6);
        instance.getFormBean().getElement("SETWDCUTPAYFAIL").setComponetTp(6);

        if (superbutton != null) {
            if (superbutton.equals("SETWDCUTPAYSUCCESS")) {
                instance.getFormBean().getElement("SETWDCUTPAYSUCCESS").setComponetTp(15);
            }
            if (superbutton.equals("SETWDCUTPAYFAIL")) {
                instance.getFormBean().getElement("SETWDCUTPAYFAIL").setComponetTp(15);
            }
        }

        seqno = ctx.getParameter("SEQNO");

        if (seqno != null) {
            instance.setValue("SEQNO", seqno);
            instance.setReadonly(true);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        try {
            if (button != null && button.equals("SETWDCUTPAYSUCCESS")) {
                setCutpayDetlStatus(FDBillStatus.CUTPAY_SUCCESS);
                ctx.setRequestAtrribute("msg", "�˱ʴ��ۼ�¼������Ϊ�ۿ�ɹ���");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } else if (button != null && button.equals("SETWDCUTPAYFAIL")) {
                setCutpayDetlStatus(FDBillStatus.CUTPAY_FAILD);
                ctx.setRequestAtrribute("msg", "�˱ʴ��ۼ�¼������Ϊ�ۿ�ʧ�ܣ�");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }

        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    private void setCutpayDetlStatus(String billstatus) throws FdcutpaydetlDaoException {

        try {
            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl xfactcutpaydetl = detlDao.findByPrimaryKey(this.seqno);
            FdcutpaydetlPk detlPk = new FdcutpaydetlPk(this.seqno);
            xfactcutpaydetl.setBillstatus(billstatus);//����״̬
            detlDao.update(detlPk, xfactcutpaydetl);
        } catch (Exception e) {
            logger.error("�޸Ŀۿ�״̬ʱ����");
            Debug.debug(e);
            throw new FdcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

    }


}