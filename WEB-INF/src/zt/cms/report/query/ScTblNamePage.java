package zt.cms.report.query;

/**
 * <p>Title: 信贷管理系统</p>
 * <p>Description: 潍坊信贷</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 中天信息技术有限公司</p>
 * @author Yusg
 * @version 1.0
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.cmsi.pub.ParamName;
import zt.cmsi.pub.Param;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;

/********************************************
 *
 *    报表查询设置  详细页面
 *
 *******************************************/

public class ScTblNamePage extends FormActions
{
    String strFlag=null;             //读写标志
    private Param paramg = null;     //获得的变量集合
    private String strRptNo=null;    //项目编号

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);

        if (paramg == null)
        {
            msgs.add("数据不完整无法定制报表和查询！");
            return -1;
        }
        strFlag = (String) paramg.getParam(ParamName.Flag);
        strRptNo = (String) paramg.getParam("RPTNO");

        if ( strRptNo!=null)
        {
            instance.setValue("RPTNO",strRptNo);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }

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
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager)
    {
        return 0;
    }
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor)
    {
        int tmpRptNo=0;
        String strSql="select coalesce(max(cast(rptno as int)),1)+1 from sctblname";
        RecordSet rs=conn.executeQuery(strSql);
        if(rs.next())
        {
            tmpRptNo=rs.getInt(0);
        }
        strRptNo=String.valueOf(tmpRptNo);

        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "RPTNO", strRptNo);
        return 0;
    }
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor)
    {
        return 0;
    }




}