package zt.cms.xf.query;

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
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import zt.platform.form.util.SessionAttributes;
import zt.platform.utils.Debug;

public class XFAppQueryList extends FormActions {


    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
//         ctx.setAttribute("CONTRACTNO",  "");

        String appno = null;
        String contractno = null;

        if (button == null) {
            return -1;
        }

        try {
            appno = ctx.getParameter("APPNO");
            String sql = "select contractno from xfvappquery where appno = '" + appno + "'";
            RecordSet rs = conn.executeQuery(sql);
            if (rs.next()) {
                contractno = rs.getString("contractno");
            }
        } catch (Exception e) {
            msgs.add("��ѯ����ʱ�����쳣��" + e.getMessage());
            Debug.debug(e);
            return -1;
        }

        if (contractno == null) {
            contractno = "";
        }
        if (button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            String buttonname = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
            if (buttonname.equals("SHOWCONTRACTBUTTON")) {
                ctx.setAttribute("CONTRACTNO", contractno);
                trigger(manager, "XFCONTRACTPAGE", null);
            } else if (buttonname.equals("SHOWBILLSBUTTON")) {
                ctx.setAttribute("CONTRACTNO", contractno);
                trigger(manager, "XFACTCUTPAYMAINQUERY", null);
            } else if (buttonname.equals("SHOWCUTPAYDETLBUTTON")) {
                ctx.setAttribute("CONTRACTNO", contractno);
                trigger(manager, "XFACTCUTPAYDETLQUERY", null);
            } else if (buttonname.equals("SHOWAPPBUTTON")) {
                ctx.setTarget("/consume/application.jsp");
            } else if (buttonname.equals("DOPRECUTPAYJB")) {

                //TODO������Ƿ����� �����ʵ���δ�����ڱ�־���������ڱ�־���ѽ���
//                if (checkMainBillOverdue()==false) {  //����
//                }

                ctx.setAttribute("CONTRACTNO", contractno);
                ctx.setAttribute("BUTTONNAME", "DOPRECUTPAYJB");  //��ǰ�����
                trigger(manager, "XFPRECUTPAYDATELINK", null);
            } else if (buttonname.equals("DOPRECUTPAYFH")) {
                ctx.setAttribute("CONTRACTNO", contractno);
                ctx.setAttribute("BUTTONNAME", "DOPRECUTPAYFH");  //��ǰ�����
                trigger(manager, "XFPRECUTPAYDATELINK", null);
            }
        }
        return 0;
    }

}