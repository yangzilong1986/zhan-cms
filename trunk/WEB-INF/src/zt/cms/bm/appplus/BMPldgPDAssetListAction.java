package zt.cms.bm.appplus;

import java.sql.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月2日
 */
public class BMPldgPDAssetListAction extends FormActions {
  private String BMNo = "";
  private Param param = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    BMNo = param.getBmNo();
    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, BMNo);
      countps.setString(1, BMNo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute(ParamName.ParamName, param);
    String PLEDGENO=(String)ctx.getParameter("PLEDGENO");
    //增加
    if(PLEDGENO==null || PLEDGENO.trim().length()<1){
      trigger(manager, "BMPLDGPDASSETPAGE1", null);
    }
    //详细
    else{
      String BMPLDGPDASSET=DBUtil.getCellValue(conn,"BMPLDGPDASSET","PDPLDGTYPE","BMNO='"+BMNo+"' and PLEDGENO="+PLEDGENO);
      trigger(manager, "BMPLDGPDASSETPAGE"+BMPLDGPDASSET, null);
    }
    return 0;
  }
}
