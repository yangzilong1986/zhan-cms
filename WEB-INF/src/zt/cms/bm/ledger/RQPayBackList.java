package zt.cms.bm.ledger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.sql.*;
import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cmsi.pub.*;

public class RQPayBackList extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.ledger.RQPayBackList");
  private Param param=null;
  private String BMNo=null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //Param
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if(param==null){
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    //BMNo
    BMNo = (String) param.getParam(ParamName.BMNo);
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("参数错误，业务号不存在！");
      return -1;
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlWhereUtil sqlWhereUtil) {
    sqlWhereUtil.addWhereField("RQPAYBACK", "BMNo", BMNo, SqlWhereUtil.DataType_Character, sqlWhereUtil.OperatorType_Equals);
    return 0;
  }
}
