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

        
        instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //设置复核按钮为隐藏
        instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //设置放款计划按钮为隐藏

        if (superformid.indexOf("FH") > 0) {
            isFHStatus = true;
            //20090805  封住流水审核流程
            /*
            instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(15);
            instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(15);
            */
        } else {
            isFHStatus = false;
            instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //设置复核按钮为隐藏
            instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //设置放款计划按钮为隐藏
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
                ctx.setRequestAtrribute("msg", "此笔放款记录已审核通过！");
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);

            } else {
                return -1;
            }
        } else if (button != null && button.equals("DISMISSALBUTTON")) {
            if (doConfirm(ctx, conn, instance, msgs, false) == 0) {
                ctx.setRequestAtrribute("msg", "此笔放款记录已驳回！");
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
                msgs.add("此笔放款记录对应的合同信息未找到，请查询。");
                return -1;
            }

            //TODO:增加XFCONTRACT表字段：贷款发放经办复核人员时间信息
            //xfcontract.(um.getUserName());
            //xfcontract.setCheckdate(new java.util.Date());



            if (isPass) {
                xfactpaydetl.setPaystatus("3"); //设置状态为“审核通过”  TODO:CONSTANT
            } else {
                xfactpaydetl.setPaystatus("2"); //设置状态为“驳回”  TODO:CONSTANT
            }
            paydetldao.update(paydetlpk, xfactpaydetl);


            /*
            合同状态处理
             */

            if (isPass) {
                xfcontract.setCstatus(XFContractStatus.FANGKUAN_TONGGUO); //设置状态为“放款通过”
            } else {
                xfcontract.setCstatus(XFContractStatus.FANGKUAN_BOHUI); //设置状态为“放款驳回”
            }
            contractdao.update(contractpk, xfcontract);

            return 0;
        } catch (Exception e) {
            msgs.add("系统发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }


}
