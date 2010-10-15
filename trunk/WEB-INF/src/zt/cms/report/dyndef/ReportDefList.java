package zt.cms.report.dyndef;

/**
 * <p>Title: �Ŵ�����ϵͳ</p>
 * <p>Description: Ϋ���Ŵ�</p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author Yusg
 * @version 1.0
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.cmsi.pub.ParamName;
import zt.cmsi.pub.Param;
import zt.platform.form.util.SqlAssistor;

/******************************************
 *
 *    ��̬������  �б�ҳ��
 *
 *****************************************/

public class ReportDefList extends FormActions
{
    private String strFlag=null;    //��д��־
    public  Param  params=null;     //���͵ı�������

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        strFlag = ctx.getParameter("flag").trim();
        if (strFlag.equals(ParamName.Flag_WRITE))
        {
            instance.setReadonly(false);
        }
        else
        {
            instance.setReadonly(true);
        }
        return 0;
    }
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                ErrorMessages msgs, EventManager manager)
    {
        String strRptNo=ctx.getParameter("REPNO");
        params=new Param();

        params.addParam("REPNO",strRptNo);
        params.addParam(ParamName.Flag,strFlag);

        ctx.setRequestAtrribute(ParamName.ParamName,params);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))
        {
            trigger(manager, "REPORTDEFPAGE", null);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME))
        {
            trigger(manager, "REPORTDEFPAGE", null);
        }

        return 0;
    }
    public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        return 0;
    }
}