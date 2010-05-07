package zt.cms.bm.loancert;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @date   2004/01/05    created
 * @version 1.0
 */
import java.util.logging.*;
import zt.cms.bm.bill.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/******************************************************
 *
 *  贷款证发放列表
 *
 *****************************************************/
public class BmCreditLGrantList
    extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.loancert.BmCreditLGrantList");
  private String strFlag = null; //读写标志
  private Param params = null; //发送的变量集合
  private String strClientNO = null; //客户号

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag").trim();
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    logger.info("preFind********************************************");
    if (strFlag.equals(ParamName.Flag_WRITE)) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      String strinsql = Function.getSelfAndAllSubBrhIds(msgs, ctx);
      System.out.println(strinsql);
      sqlWhereUtil.addWhereField("CMINDVCLIENT", "APPBRHID", strinsql, SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button, ErrorMessages msgs, EventManager manager) {
    strClientNO = ctx.getParameter("CLIENTNO").trim();

    params = new Param();
    params.addParam(ParamName.Flag, strFlag);
    params.addParam(ParamName.CLientNo, strClientNO);

    ctx.setRequestAtrribute(ParamName.ParamName, params);

    if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
      trigger(manager, "BMCREDITLGRANTPAGE", null);
      return 0;
    }
    else {
      return -1;
    }
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
    EventManager manager, String reffldnm,SqlWhereUtil sqlWhereUtil) {

    String strinsql = Function.getAllSubBrhIds(msgs, ctx);
    sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);

return 0;
}

}
