package zt.cms.cm;

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

public class FamilyUnionMemList
    extends FormActions {

  public static Logger logger = Logger.getLogger("zt.cms.cm.FamilyUnionMemList");
  private String UNIONNO = null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //UNIONNO
    UNIONNO = (String) ctx.getRequestAttribute("UNIONNO");
    if (UNIONNO == null) {
      msgs.add("参数错误，联户联保小组代码不存在！");
      return -1;
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps,
                     PreparedStatement countps) {
    try {
      ps.setString(1, UNIONNO);
      countps.setString(1, UNIONNO);
    }
    catch (Exception ex) {
      msgs.add(ex.getMessage());
      return -1;
    }
    return 0;
  }

}
