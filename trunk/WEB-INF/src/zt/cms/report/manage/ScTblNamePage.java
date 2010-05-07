package zt.cms.report.manage;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: 潍坊信贷管理系统</p>
 * <p>Copyright: Copyright (c) 2003 中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
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
    private Param paramg = null;        //获得的变量集合
    private String strRptNo=null;       //
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        paramg  = (Param) ctx.getRequestAttribute(ParamName.ParamName);
        strFlag = (String) paramg.getParam(ParamName.Flag);
        strRptNo= (String) paramg.getParam("rptno");

        if(strRptNo==null)
        {
            msgs.add("数据不完整无法授权，无法找到报表编号");
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