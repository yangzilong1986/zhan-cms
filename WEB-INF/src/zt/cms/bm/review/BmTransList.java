package zt.cms.bm.review;

/**
 * <p>Title: 查看历史审批记录</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @date   2004/01/05       created
 * @version 1.0
 */

import java.sql.*;

import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BmTransList extends FormActions {
  private String strFlag = null; //读写标志
  public Param params = null; //发送的变量集合
  private Param paramg = null; //获得的变量集合
  private String strBmNo = null; //业务号
  private String strBmTransNo = null; //业务明细号
  private String strBmActType = null; //审批级别
  private String strBmType = null; //贷款类型

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      strFlag = (String) ctx.getParameter("flag");
      strBmNo = (String) ctx.getParameter("BMNO");
      if(strFlag == null || strBmNo == null)
      {
        msgs.add("没有历史审批记录！");
        return -1;
      }
    }
    else
    {
      strFlag = (String) paramg.getParam(ParamName.Flag);
      strBmNo = (String) paramg.getParam(ParamName.BMNo);
    }

    if (strBmNo == null) {
      msgs.add("没有历史审批记录");
      return -1;
    }
    else {
      return 0;
    }
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, strBmNo);
      countps.setString(1, strBmNo);
      if (strFlag.equals(ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
    catch (SQLException se) {
      return -1;
    }
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    String strBrhId = ctx.getParameter("OPERBRHID");
    strBmTransNo = ctx.getParameter("BMTRANSNO");
    if (strBmTransNo == null || strBmTransNo.length() < 1) {
      msgs.add("业务明细代码没有获得！");
      return -1;
    }
    BMTransData bmTransData = BMTrans.getBMTransData(strBmNo, Integer.parseInt(strBmTransNo));
    int intBmActType = bmTransData.actType;
    BMTableData bmTableData = BMTable.getBMTable(strBmNo);
    int intTypeNo = bmTableData.TypeNo;
    strBmType = String.valueOf(bmTableData.TypeNo);
    strBmActType = String.valueOf(intBmActType);
    params = new Param();
    params.addParam(ParamName.Flag, ParamName.Flag_READ);
    params.addParam(ParamName.BMNo, strBmNo);
    params.addParam(ParamName.BMTransNo, strBmTransNo);
    params.addParam(ParamName.BrhID, strBrhId);
    params.addParam(ParamName.BMActType, strBmActType);
    params.addParam(ParamName.BMType, strBmType);
    BMProg prog = BMRoute.getInstance().getActProg(intTypeNo, intBmActType, null); //获得程序跳转的url
    ctx.setRequestAtrribute(ParamName.ParamName, params);
    if (prog.isForm()) {
      ctx.setRequestAtrribute("fromList", "OK");
      trigger(manager, prog.getProgName(), null);
    }
    else {
      ctx.setTarget(prog.getProgName());
    }
    return 0;
  }

}
