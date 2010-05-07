package zt.cms.xf;

import zt.cms.cm.common.RightChecker;
import zt.cms.util.Workflow;
import zt.cmsi.pub.define.BMRouteBindNode;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.user.UserManager;

import java.util.logging.Logger;

public class XFCROptionPage extends FormActions {
    public static Logger logger = Logger.getLogger("zt.cms.xf.XFCROptionPage");

    private String APPNO = null; //���뵥��
    private String APPAMT = null; //������
    private String OPINIONNO = null; //���뵥���������
    private String UMOP = null; //��ǰҵ��Ա
    private int UMTP = 0;       //��ǰҵ��Ա����

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        RightChecker.checkReadonly(ctx, conn, instance);

        APPNO = String.valueOf(ctx.getRequestAttribute("APPNO"));
        APPAMT = String.valueOf(ctx.getRequestAttribute("APPAMT"));
        OPINIONNO = ctx.getParameter("OPINIONNO");
        String OPERATOR = ctx.getParameter("OPERATOR");//����޸���
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        UMOP = um.getUserName();
        UMTP = um.getUser().getStatus();

        FormBean fb = instance.getFormBean();
        ElementBean ebx;
        if (UMTP >= 21) {
            ebx = fb.getElement("AUTOGRADE");
            ebx.setComponetTp(ComponentType.TEXT_TYPE);
            ebx = fb.getElement("GRADE");
            ebx.setComponetTp(ComponentType.TEXT_TYPE);
        }
        ebx = fb.getElement("LASTMODIFIED");
        ebx.setComponetTp(ComponentType.DATE_TYPE);

        //������Ϊ�������༭״̬
        if (APPNO != null) {
            if (OPINIONNO != null) {
                //����instance������ֵ
                instance.setValue("APPNO", APPNO);
                instance.setValue("OPINIONNO", OPINIONNO);
                if (!UMOP.equals(OPERATOR)) instance.setReadonly(true);
                //����ת�Ƶ��༭״̬
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            } else {
                OPINIONNO = String.valueOf(ctx.getRequestAttribute("OPINIONNO"));
                trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            }
        }
        return 0;
    }

    public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        if (Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP), XFConf.APPSTATUS_CHUSHEN3)) {
            String str = "select * from XFAPPGRADE  where APPNO='" + APPNO + "' ";
            if (!conn.isExist(str)) {
                ctx.setRequestAtrribute("msg", "��¼��ͻ����ۣ�");
                ctx.setTarget("/showinfo.jsp");
                return -1;
            }
        }

        FormBean fb = instance.getFormBean();
        instance.setValue("APPNO", APPNO);
        instance.setValue("OPINIONNO", OPINIONNO);
        instance.setValue("OPERATOR", UMOP);

        ElementBean ebx = fb.getElement("LASTMODIFIED");
        ebx.setComponetTp(ComponentType.HIDDEN_TYPE);

        if (UMTP >= 21) {
            String str;
            if (UMTP == 21)
                str = "select * from XFAPPGRADE where APPNO='" + APPNO + "'";
            else str = "select * from XFOPINION where APPNO='" + APPNO + "' and GRADE is not null and rownum=1";

            RecordSet rs = conn.executeQuery(str);
            if (rs.next()) {
                if (UMTP == 21)
                    instance.setValue("AUTOGRADE", rs.getInt("GRADE"));
                else
                    instance.setValue("AUTOGRADE", rs.getInt("AUTOGRADE"));
                instance.setValue("GRADE", rs.getInt("GRADE"));
            }
            if (UMTP > 21) {
                ebx = fb.getElement("GRADE");
                ebx.setReadonly(true);
            }
        } else {
            ebx = fb.getElement("AUTOGRADE");
            ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
            ebx = fb.getElement("GRADE");
            ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
        }

        return 0;
    }

    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
        FormBean fb = instance.getFormBean();
        ElementBean ebx = fb.getElement("LASTMODIFIED");
        ebx.setComponetTp(ComponentType.DATE_TYPE);

        instance.setValue("LASTMODIFIED", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "LASTMODIFIED", "#SYSDATE#");
        return 0;
    }

    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);


        //�����һ������(�����ύ)����ֻ�������������ֱ�ӱ���������ύ
        if (!Workflow.hasWriteRoleByUserTp(String.valueOf(um.getUser().getStatus()), XFConf.APPSTATUS_TIJIAO)) {

            String appStaStr = "";
            if (ctx.getParameter("OPINIONTP").equals("1")) {
                BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String.valueOf(um.getUser().getStatus()));
                appStaStr = Workflow.getNextStatusEndByUserTp(dt.status, APPAMT);//���״̬Ϊ�գ��򷵻غ�ͬǩ��״̬��11����
            } else if (ctx.getParameter("OPINIONTP").equals("2"))
                appStaStr = XFConf.APPSTATUS_BOHUI;//���벵�� 99

            if (appStaStr.equals("")) {
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("msg", "�����������ɹ����ύ����һ������ʧ��,����ϵϵͳ����Ա��");
                ctx.setTarget("/showinfo.jsp");
                return -1;
            } else {
                String str = "update XFAPP set APPSTATUS=" + appStaStr + " where APPNO='" + APPNO + "'";
                if (conn.executeUpdate(str) <= 0) {
                    ctx.setRequestAtrribute("flag", "0");
                    ctx.setRequestAtrribute("msg", "�����������ɹ����ύ����һ������ʧ��,����ϵϵͳ����Ա��");
                    ctx.setTarget("/showinfo.jsp");
                    return -1;
                }
            }
            if (appStaStr.equals(XFConf.APPSTATUS_WANCHENG))
                ctx.setRequestAtrribute("msg", "�����������ɹ���������ɣ�");
            else
                ctx.setRequestAtrribute("msg", "�����������ɹ����ѳɹ��ύ����һ��������");
        } else
            ctx.setRequestAtrribute("msg", "�����������ɹ���");
        ctx.setTarget("/showinfo.jsp");
        instance.setReadonly(true);
        return 0;
    }

    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        ctx.setRequestAtrribute("msg", "ɾ����������ɹ���");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }

    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor) {
        return 0;
    }

    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
        instance.setValue("LASTMODIFIED", "#SYSDATE#");
        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "LASTMODIFIED", "#SYSDATE#");
        return 0;
    }

    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);


        //�����һ������(�����ύ)����ֻ�������������ֱ�ӱ���������ύ
        if (!Workflow.hasWriteRoleByUserTp(String.valueOf(um.getUser().getStatus()), XFConf.APPSTATUS_TIJIAO)) {

            String appStaStr = "";
            if (ctx.getParameter("OPINIONTP").equals("1")) {
                BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String.valueOf(um.getUser().getStatus()));
                appStaStr = Workflow.getNextStatusEndByUserTp(dt.status, APPAMT);//���״̬Ϊ�գ��򷵻غ�ͬǩ��״̬��11����
            } else if (ctx.getParameter("OPINIONTP").equals("2"))
                appStaStr = "99";

            if (appStaStr.equals("")) {
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("msg", "�޸���������ɹ����ύ����һ������ʧ��,����ϵϵͳ����Ա��");
                ctx.setTarget("/showinfo.jsp");
                return -1;
            } else {
                String str = "update XFAPP set APPSTATUS=" + appStaStr + " where APPNO='" + APPNO + "'";
                if (conn.executeUpdate(str) <= 0) {
                    ctx.setRequestAtrribute("flag", "0");
                    ctx.setRequestAtrribute("msg", "�޸���������ɹ����ύ����һ������ʧ��,����ϵϵͳ����Ա��");
                    ctx.setTarget("/showinfo.jsp");
                    return -1;
                }
            }
            if (appStaStr.equals(XFConf.APPSTATUS_WANCHENG))
                ctx.setRequestAtrribute("msg", "�޸���������ɹ���������ɣ�");
            else
                ctx.setRequestAtrribute("msg", "�޸���������ɹ����ѳɹ��ύ����һ��������");
        } else
            ctx.setRequestAtrribute("msg", "�޸���������ɹ���");
        ctx.setTarget("/showinfo.jsp");

        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {


        return 0;
    }

}