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
import zt.platform.form.config.FormBean;
import zt.platform.form.config.ElementBean;
import zt.cms.report.dyn.ReportSql;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.user.UserManager;
import zt.cms.pub.SCUser;
import zt.platform.form.config.SystemAttributeNames;
import zt.cms.pub.SCBranch;

/********************************************
 *
 *    动态表数据录入  详细页面
 *
 *******************************************/

public class ReportDataPage1 extends FormActions
{
    String strFlag=null;             //读写标志
    public  Param  params=null;     //发送的变量集合
    private Param paramg = null;     //获得的变量集合

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                    EventManager manager, String parameter)
    {
        paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);

        if (paramg == null)
        {
            msgs.add("数据不完整！");
            return -1;
        }
        strFlag = (String) paramg.getParam(ParamName.Flag);

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
    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil)
    {

        UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (reffldnm.equals("BRHID")) {
            //BRHID（用户网点）
            String BRHID = SCUser.getBrhId(um.getUserName());
            if (BRHID == null || BRHID.length() < 1) {
                return -1;
            }
            //APPBRHIDs（用户网点下的所有实网点，包括自己）
            String SUBBRHIDs = SCBranch.getSubBrh2(BRHID);
            if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
                msgs.add("下属实网点不存在！");
                return -1;
            }
            else {
                SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
            }
            //sqlWhereUtil
            if (reffldnm.equals("BRHID")) {
                sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                           SqlWhereUtil.DataType_Sql,
                                           sqlWhereUtil.OperatorType_In);
            }
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
        params.addParam("OPER","ADD");
        params.addParam(ParamName.Flag,strFlag);

        ctx.setRequestAtrribute(ParamName.ParamName,params);

        if (button != null && button.equals("goon"))
        {
            trigger(manager, "REPORTDATAPAGE", null);
        }
        return 0;
    }


}