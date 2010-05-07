package zt.cms.cm;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;
import java.sql.PreparedStatement;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.cms.pub.code.SerialNumber;
import zt.cms.pub.SCBranch;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SCBranchMngApp extends FormActions {
  private String brhid = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
          ErrorMessages msgs,
          EventManager manager, String parameter) {

    brhid = ctx.getParameter("BRHID");
    boolean hasRec = false;

    if (brhid != null ) {

      String sql = "select * from SCBranchApp where BrhID='" + brhid + "'";
      RecordSet rs = conn.executeQuery(sql);
      if (rs.next()) {
        hasRec = true;
      }
      else
        hasRec = false;

      if(hasRec)
      {
        instance.setValue("BRHID", brhid);
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }
      return 0;
    }
    else
    {
      msgs.add("没有当前网点代码!");
      return -1;
    }
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    if (brhid != null) {
      instance.setValue("BRHID", brhid);
    }
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    SCBranch.setDirty();
    ctx.setRequestAtrribute("title", "信贷网点信息维护");
    ctx.setRequestAtrribute("msg", "处理成功完成");
    ctx.setRequestAtrribute("flag", "1");
    ctx.setTarget("/showinfo.jsp");

    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager) {
    SCBranch.setDirty();
    ctx.setRequestAtrribute("title", "信贷网点信息维护");
    ctx.setRequestAtrribute("msg", "处理成功完成");
    ctx.setRequestAtrribute("flag", "1");
    ctx.setTarget("/showinfo.jsp");
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager) {
      SCBranch.setDirty();
      return 0;
  }

}
