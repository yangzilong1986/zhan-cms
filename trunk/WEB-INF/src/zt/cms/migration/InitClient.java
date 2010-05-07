package zt.cms.migration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
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
import zt.platform.utils.Debug;
import zt.cmsi.pub.*;

public class InitClient extends FormActions{

  private String strFlag = null;   //读写标志
  private Param  params=null;      //发送的变量集合
  private String strClientNO=null; //客户号
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



      if(strFlag.equals(ParamName.Flag_WRITE))
      {
          instance.setReadonly(false);
      }
      else
      {
          instance.setReadonly(true);
      }
      if (firstRun == true) {
          firstRun = false;
          return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
      }


      if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0)
      {
          String strinsql = Function.getSelfAndAllSubBrhIds(msgs, ctx);
          sqlWhereUtil.addWhereField("INITCLIENT", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
      }
      return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
          String button,ErrorMessages msgs, EventManager manager)
  {
      strClientNO=ctx.getParameter("CLIENTNO").trim();

      Debug.debug(Debug.TYPE_MESSAGE,"in InitClient Button event!");
      //System.out.println("in InitClient Button event!");

      params=new Param();
      params.addParam(ParamName.Flag,strFlag);
      params.addParam(ParamName.CLientNo,strClientNO);

      ctx.setRequestAtrribute(ParamName.ParamName,params);

      if ( button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE) )
      {
          Debug.debug(Debug.TYPE_MESSAGE,"in InitClient Butjjjton event   !"+button);
          trigger(manager,"INITCLIENTPAGE",null);
          return 0;
      }
      else
          return -1;
  }

}
