package zt.cms.bm.app;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.sql.*;

import com.zt.util.*;
import zt.cms.bm.bill.*;
import zt.cms.bm.common.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class TableCancelList extends FormActions {

  private boolean firstRun = true;

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps,
                     PreparedStatement countps) {
    try {
      ps.setString(1, SessionInfo.getLoginBrhId(ctx));
      countps.setString(1, SessionInfo.getLoginBrhId(ctx));
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {
    if (firstRun == true) {
      firstRun = false;
      return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
    }

    super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil);
    sqlWhereUtil.addWhereField("BMTABLE", "INITBRHID", SessionInfo.getLoginBrhId(ctx));
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    String bmno = ctx.getParameter("BMNO");
    System.out.println("bmno" + bmno);
    if (bmno != null) {
      int ret = BMTable.cancelBMTable(ctx.getParameter("BMNO"),
                                      SessionInfo.getLoginUserName(ctx));

      System.out.println("ret===" + ret);
      if (ret >= 0) {
        ctx.setRequestAtrribute("msg", "取消成功！");
      }
      else {
        String msg = PropertyManager.getProperty("" + ret);
        ctx.setRequestAtrribute("msg", msg);
      }
      ctx.setTarget("/showinfo.jsp");
    }
    else {
      msgs.add("参数不全 ！");
      return -1;
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {

    String strinsql = Function.getAllSubBrhIds(msgs, ctx);
    sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    return 0;
  }

}
