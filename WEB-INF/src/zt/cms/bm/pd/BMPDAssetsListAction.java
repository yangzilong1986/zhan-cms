package zt.cms.bm.pd;

import java.sql.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMPDAssetsListAction extends FormActions {
  String PDMAINNO = null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    PDMAINNO = (String) ctx.getRequestAttribute("PDMAINNO");
    if (PDMAINNO == null || PDMAINNO.trim().length() < 1) {
      msgs.add("参数错误，抵债资产基本信息业务号为空！");
      return -1;
    }
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, PDMAINNO);
      countps.setString(1, PDMAINNO);
    }
    catch (Exception ex) {
      return -1;
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("PDMAINNO", PDMAINNO);
    String PDNO = (String) ctx.getParameter("PDNO");
    //增加
    if (PDNO == null || PDNO.trim().length() < 1) {
      trigger(manager, "BMPDASSETSPAGE1", null);
    }
    //详细
    else {
      String PDPLDGTYPE = DBUtil.getCellValue(conn, "BMPDASSETS", "PDPLDGTYPE", "PDNO=" + PDNO);
      trigger(manager, "BMPDASSETSPAGE" + PDPLDGTYPE, null);
    }
    return 0;
  }

}
