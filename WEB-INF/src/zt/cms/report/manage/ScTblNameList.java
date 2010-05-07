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

/**
 *
 */
public class ScTblNameList extends FormActions
{
    private String strFlag=null;    //
    public  Param  params=null;     //发送的变量集合

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        strFlag=ctx.getParameter("flag");


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
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                EventManager manager, PreparedStatement ps, PreparedStatement countps)
    {
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                ErrorMessages msgs, EventManager manager)
        {
            String strRptNo=ctx.getParameter("");
            params=new Param();
            params.addParam(ParamName.Flag,strFlag);
            params.addParam("rptno",strRptNo);

            ctx.setRequestAtrribute(ParamName.ParamName,params);

            if ( button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE) )
            {
                trigger(manager,"SCTBLNAMEPAGE",null);
            }
            if ( button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME) )
            {
                trigger(manager,"SCTBLNAMEPAGE",null);
            }

            return 0;

        }




}