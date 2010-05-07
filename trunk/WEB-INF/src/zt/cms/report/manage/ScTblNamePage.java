package zt.cms.report.manage;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: Ϋ���Ŵ�����ϵͳ</p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ�������޹�˾</p>
 * <p>Company: ������Ϣ�������޹�˾</p>
 * @author YUSG
 * @version 1.0
 * @CreateDate  20040210
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SessionAttributes;
import java.sql.PreparedStatement;
import zt.cmsi.pub.Param;
import zt.cmsi.pub.ParamName;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;


/*
 *
 */
public class ScTblNamePage extends FormActions
{
    private String strFlag=null;        //
    private Param paramg = null;        //��õı�������
    private String strRptNo=null;       //
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        paramg  = (Param) ctx.getRequestAttribute(ParamName.ParamName);
        strFlag = (String) paramg.getParam(ParamName.Flag);
        strRptNo= (String) paramg.getParam("rptno");

        if(strRptNo==null)
        {
            msgs.add("���ݲ������޷���Ȩ���޷��ҵ�������");
            return -1;
        }
        instance.setValue("RPTNO", strRptNo);
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                        Event.BRANCH_CONTINUE_TYPE);

        if(strFlag.equals(ParamName.Flag_WRITE))
        {

            instance.setReadonly(false);
        }
        else
        {
            instance.setReadonly(true);
        }

        return 0;
    }
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
           EventManager manager)
    {
        return 0;
    }


}