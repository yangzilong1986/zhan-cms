package zt.cms.bm.loancert;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @version 1.0
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.db.DatabaseConnection;
import zt.cmsi.pub.ParamName;
import zt.cmsi.pub.Param;
import zt.cms.bm.bill.Function;
import zt.cmsi.pub.confitem;

/******************************************************
 *
 *  贷款证维护列表
 *
 *****************************************************/
public class BmCreditLMainList extends FormActions
{
    private String strFlag = null;   //读写标志
    private Param  params=null;      //发送的变量集合
    private String strClientNO=null; //客户号
    private String strTypeNo=null;   //业务类型代码
    private boolean firstRun = true;


    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
                EventManager manager, String parameter)
    {
        strFlag=ctx.getParameter("flag").trim();
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlWhereUtil sqlWhereUtil)
    {
        if(firstRun == true)
        {
          firstRun = false;
          return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
        }

        if(strFlag.equals(ParamName.Flag_WRITE))
        {
            instance.setReadonly(false);
        }
        else
        {
            instance.setReadonly(true);
        }
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0)
        {
            String strinsql = Function.getSelfAndAllSubBrhIds(msgs, ctx);
            sqlWhereUtil.addWhereField("CMINDVCLIENT", "APPBRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            String button,ErrorMessages msgs, EventManager manager)
    {
        strTypeNo=ctx.getParameter("TYPENO").trim();
        strClientNO=ctx.getParameter("CLIENTNO").trim();

        params=new Param();
        params.addParam(ParamName.Flag,strFlag);
        params.addParam(ParamName.CLientNo,strClientNO);
        params.addParam(ParamName.BMType,strTypeNo);

        ctx.setRequestAtrribute(ParamName.ParamName,params);

        String col = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
        //System.out.println("COLD==========="+col);

        if ( col != null && col.compareToIgnoreCase("modify") == 0)
        {
            trigger(manager,"BMCREDITLMAINTPAGE",null);
            return 0;
        }
        else if ( col != null && col.compareToIgnoreCase("loan") ==0)
        {
          trigger(manager,"BMCREDITLIMITLOAN",null);
            return 0;
        }
        else if ( col != null && col.compareToIgnoreCase("chgpwd") ==0)
        {
          trigger(manager,"BMCREDITCHGPWD",null);
            return 0;
        }
        else if ( col != null && col.compareToIgnoreCase("initpwd") ==0)
        {
          trigger(manager,"BMCREDITINITPWD",null);
            return 0;
        }
        else
            return -1;

    }



}
