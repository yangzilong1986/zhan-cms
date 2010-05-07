package zt.cms.xf.contract;

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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import zt.platform.form.util.SessionAttributes;
import zt.platform.utils.Debug;
import zt.cmsi.mydb.MyDB;

public class XFContractPayList extends FormActions {

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        boolean isNewRecord = false;
        String superformid = (String) ctx.getAttribute("SUPERFORMID");

        //�Ǿ��촦��״̬�£����������ɻ���ƻ�
        if (superformid != null) {
            if (!superformid.equals("XFCONTRACTLIST")) {
                //instance.setReadonly(false);
                instance.getFormBean().getElement("PAYSCHEDULEBUTTON").setComponetTp(6); //���÷ſ�ƻ���ťΪ����
            } else {
                instance.getFormBean().getElement("PAYSCHEDULEBUTTON").setComponetTp(15); 
            }
        }
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        try {
            ps.setString(1, ctx.getParameter("CONTRACTNO"));
            countps.setString(1, ctx.getParameter("CONTRACTNO"));
        } catch (Exception ex) {
            msgs.add(ex.getMessage());
            return -1;
        }
        return 0;
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


}
