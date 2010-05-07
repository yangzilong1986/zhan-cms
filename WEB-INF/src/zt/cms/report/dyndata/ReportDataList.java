package zt.cms.report.dyndata;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003  中天信息技术有限公司</p>
 * <p>Company: 中天信息技术有限公司</p>
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


/******************************************
 *
 *    动态表数据录入  列表页面
 *
 *****************************************/

public class ReportDataList extends FormActions
{
    private String strFlag=null;    //读写标志
    public  Param  params=null;     //发送的变量集合

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
        String strMonth=ctx.getParameter("MONTH");
        String strBrhid=ctx.getParameter("BRHID");
        params=new Param();

        params.addParam("REPNO",strRptNo);
        params.addParam("MONTH",strMonth);
        params.addParam("BRHID",strBrhid);
        params.addParam(ParamName.Flag,strFlag);

        ctx.setRequestAtrribute(ParamName.ParamName,params);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))
        {
            trigger(manager, "REPORTDATAPAGE", null);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME))
        {
            trigger(manager, "REPORTDATAPAGE1", null);
        }
        return 0;
    }
}