package zt.cms.xf;

import zt.cms.cm.common.RightChecker;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;

import java.util.logging.Logger;

public class XFModifyRate extends FormActions {
    public static Logger logger = Logger.getLogger("zt.cms.xf.XFModifyRate");

    private String flag = null;  //�����Ƿ�ɶ�
    private String APPNO = null; //���뵥��

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
            //����ת�Ƶ��༭״̬
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                    Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }


    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        ctx.setRequestAtrribute("msg", "ά���������ʳɹ���");
        ctx.setRequestAtrribute("reloadOperner", "1");
        ctx.setTarget("/showinfo.jsp");
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        return 0;
    }

}