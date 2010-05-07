package zt.cms.xf;

/**
 * Created by IntelliJ IDEA.
 * User: hpadmin
 * Date: 2009-3-21
 * Time: 21:51:16
 * To change this template use File | Settings | File Templates.
 */

import zt.cms.cm.common.RightChecker;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class XFCrInfo extends FormActions {
    public static Logger logger = Logger.getLogger("zt.cms.xf.XFCrInfo");

    private String flag = null;  //�����Ƿ�ɶ�
    private String APPNO = null; //���뵥��
    private String CONFMONPAY = null; //�˶�������

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        
        RightChecker.checkReadonly(ctx, conn, instance);

        //�������ڴ�������͵����ݻ�һЩ��Ҫ���⴦������ݿ��ֶ�����
        APPNO = String.valueOf(ctx.getRequestAttribute("APPNO"));
        //������Ϊ�������༭״̬
        if (APPNO != null) {
            //����instance������ֵ
            instance.setValue("APPNO", APPNO);
            String str = "select CONFMONPAY from XFAPP where APPNO='" + APPNO + "'";
            RecordSet rs = conn.executeQuery(str);
            if (rs.next()) {
                CONFMONPAY = rs.getString("CONFMONPAY");
                instance.setValue("CONFMONPAY", CONFMONPAY);
            } else {
                ctx.setRequestAtrribute("msg", "��ȷ�Ͽͻ������룡");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                return -1;
            }

            str = "select * from XFCREDITINFO where APPNO='" + APPNO + "'";
            if (conn.isExist(str)) {
                //����ת�Ƶ��༭״̬
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
            } else
                //����ת�Ƶ����״̬
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        instance.setValue("APPNO", APPNO);
        String str = "select TRUNC(APPAMT/DIVID,2) APPAMTMON,APPAMT,DIVID,COMMISSIONRATE from XFAPPCOMM where APPNO='" + APPNO + "'";
        RecordSet rs = conn.executeQuery(str);
        if (rs.next()) {
            BigDecimal bd1 = new BigDecimal(rs.getString("APPAMT"));
            BigDecimal bd2 = new BigDecimal(rs.getString("DIVID"));
            BigDecimal bd3 = new BigDecimal(rs.getString("COMMISSIONRATE"));
            BigDecimal bd4 = bd1.divide(bd2, 3, BigDecimal.ROUND_HALF_UP).add(bd1.multiply(bd3)).setScale(2, BigDecimal.ROUND_HALF_UP);
            //instance.setValue("CCDIVIDAMT", rs.getString("APPAMTMON"));
            instance.setValue("CCDIVIDAMT", bd4.toString());
            instance.setValue("CONFMONPAY", CONFMONPAY);
        }
        return 0;
    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor) {
        instance.setValue("APPNO", APPNO);
        String str = "select TRUNC(APPAMT/DIVID,2) APPAMTMON,APPAMT,DIVID,COMMISSIONRATE from XFAPPCOMM where APPNO='" + APPNO + "'";
        RecordSet rs = conn.executeQuery(str);
        String CCDIVIDAMT = "0";
        if (rs.next()) {
            BigDecimal bd1 = new BigDecimal(rs.getString("APPAMT"));
            BigDecimal bd2 = new BigDecimal(rs.getString("DIVID"));
            BigDecimal bd3 = new BigDecimal(rs.getString("COMMISSIONRATE"));
            BigDecimal bd4 = bd1.divide(bd2, 3, BigDecimal.ROUND_HALF_UP).add(bd1.multiply(bd3)).setScale(2, BigDecimal.ROUND_HALF_UP);
            CCDIVIDAMT = bd4.toString();
        } else {
            ctx.setRequestAtrribute("msg", "���ڻ�������ʧ�ܣ�");
            ctx.setRequestAtrribute("flag", "0");
            return -1;
        }

        str = "update XFCREDITINFO set CCDIVIDAMT=" + CCDIVIDAMT + " where APPNO='" + APPNO + "'";
        if (conn.executeUpdate(str) < 0) {
            ctx.setRequestAtrribute("msg", "���ڻ�������ʧ�ܣ�");
            ctx.setRequestAtrribute("flag", "0");
            return -1;
        }
        //instance.setValue("CCDIVIDAMT", bd4.toString());
        instance.setValue("CONFMONPAY", CONFMONPAY);

        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        ctx.setRequestAtrribute("msg", "���������Ϣ�ɹ���");
        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);
        return 0;
    }

    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager) {
        ctx.setRequestAtrribute("msg", "ɾ��������Ϣ�ɹ���");
        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        ctx.setRequestAtrribute("msg", "�޸�������Ϣ�ɹ���");
        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {


        return 0;
    }

}