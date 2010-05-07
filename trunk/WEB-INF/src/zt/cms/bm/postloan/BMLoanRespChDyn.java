/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMLoanRespChDyn.java,v 1.1 2005/06/28 07:00:26 jgo Exp $
 */
package zt.cms.bm.postloan;

import java.sql.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  测试：多表－－－数据添加,修改，删除(获得值)
 *
 *@author     Administrator
 *@created    2003年12月2日
 */
public class BMLoanRespChDyn extends FormActions {

  /**
   *  Description of the Method
   *
   *@param  ctx        Description of the Parameter
   *@param  conn       Description of the Parameter
   *@param  instance   Description of the Parameter
   *@param  msgs       Description of the Parameter
   *@param  manager    Description of the Parameter
   *@param  parameter  Description of the Parameter
   *@return            Description of the Return Value
   */

  private String strFlag = null;
  private String strBmNo = null;
  private Param pm = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    pm = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (pm != null) {
      strFlag = (String) pm.getParam(ParamName.Flag);
      strBmNo = (String) pm.getParam(ParamName.BMNo);
      //System.out.println("strFlag:"+strFlag+"\nstrBmNo:"+strBmNo);
    }
    if (strBmNo == null || strBmNo.equals("")) {
      msgs.clear();
      msgs.add("参数错误，业务号不存在！");
      return -1;
    }

    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@param  ps        Description of the Parameter
   *@param  countps   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    if (strFlag.equals("read") || strFlag == null) {
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
    }
    try{
      ps.setString(1, strBmNo);
      countps.setString(1, strBmNo);
    }
    catch(Exception ex){ }
    return 0;

  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  button    Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if (pm != null) {
      Param pm1 = new Param();
      pm1.addParam(ParamName.BMNo, pm.getBmNo());
      pm1.addParam(ParamName.Flag, "read");
      ctx.setRequestAtrribute(ParamName.ParamName, pm1);
    }
    else {
      msgs.clear();
      msgs.add("接口对象不存在。");
      return -1;
    }
    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      this.trigger(manager, "BMLRC0", null);
    }
    return 0;
  }
}
