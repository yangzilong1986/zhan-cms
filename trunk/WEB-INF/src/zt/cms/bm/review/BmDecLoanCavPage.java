package zt.cms.bm.review;

/**
 * <p>Title: 信贷管理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @date   2004/01/02     created
 * @version 1.0
 */
import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

/****************************************************
 *
 * 不良贷款核销审批(第一级，第二级，第三级公用)
 *
 ****************************************************/

public class BmDecLoanCavPage extends FormActions {
  private String strFlag = null; //读写标志
  public Param params = null; //发送的变量集合
  private Param paramg = null; //获得的变量集合
  private String strBmNo = null; //业务号
  private String strBmTransNo = null; //业务明细号
  private String strUserName = null; //当前登陆用户名
  private String strOthers = "0"; //是否完成审批标志
  private String strBmActType = null; //审批级别
  private String strBmType = null; //贷款类型
  private String strScbrh = null; //所在网点
  private String strScbrh2 = null; //所在网点
  private String strIfrespLoan = null; //是否有责任人
  private String strFirstResp = null; //第一责任人
  private String strFirstRespPct = null; //第一责任人比例

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs, EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("数据不完整无法审批，请检查数据");
      return -1;
    }

    strFlag = paramg.getParam(ParamName.Flag).toString();
    strBmNo = paramg.getParam(ParamName.BMNo).toString();
    strBmTransNo = paramg.getParam(ParamName.BMTransNo).toString();
    strBmActType = paramg.getParam(ParamName.BMActType).toString();
    strBmType = paramg.getParam(ParamName.BMType).toString();

    if (strBmNo == null || strBmTransNo == null || strBmActType == null) {
      msgs.add("数据不完整无法审批，请检查数据");
      return -1;
    }
    else {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
      try {
        strUserName = um.getUserName();
        strScbrh = SCUser.getBrhId(strUserName);
        if (strScbrh == null || strScbrh.length() < 1) {
          msgs.add("下属网点不存在！");
          return -1;
        }
        else {
          strScbrh2 = "'" + strScbrh + "'";
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      int iCount = 0;
      String strSql = "select count(*) from bmdecision where bmno='" + strBmNo;
      strSql = strSql + "' and bmtransno=" + strBmTransNo;
      RecordSet rs = conn.executeQuery(strSql);
      if (rs.next()) {
        iCount = rs.getInt(0);
      }
      if (iCount > 0) { //如果bmdecision有数据则进入编辑模式
        instance.setValue("BMNO", strBmNo);
        instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }

      if (strFlag.equals( (String) ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    String strCurNo = null; //货币代码
    String strAMT = null; //决策决策金额
    String strAMT2 = null; //决策决策利息

    String strDate = SystemDate.getSystemDate5(null);
    UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);
    if (strBmActType.equals("2")) { //如果为第三级审批则获得登记的信息
      strCurNo = utda.curNo; //获得货币代码
      if (utda.appAmt == null) {
        strAMT = null;
      }
      else {
        strAMT = utda.appAmt.toString(); //获得决策金额
      }
    }
    else { //如果为第二级则获得第三级的信息，如果为第一级则获得第二级的信息
      strCurNo = utda.finalCurNo; //获得货币代码
      strAMT=(utda.finalAmt==null?"":utda.finalAmt.toString());
      strAMT2=(utda.finalAmt2==null?"":utda.finalAmt2.toString());
      strIfrespLoan = utda.ifRespLoan.toString(); //获得是否有第一责任人
      if (utda.firstResp == null) {
        strFirstResp = "";
      }
      else {
        strFirstResp = utda.firstResp; //获得第一责任人
      }
      strFirstRespPct = (utda.firstRespPct == null ? "" : utda.firstRespPct.toString()); //获得第一责任人比例
      instance.setValue("IFRESPLOAN", Integer.parseInt(strIfrespLoan)); //上一业务步骤的是否有第一责任人
      instance.setValue("FIRSTRESP", strFirstResp); //上一业务步骤的第一责任人
      instance.setValue("FISRTRESPPCT", strFirstRespPct); //上一业务步骤的第一责任人比例
    }

    instance.setValue("CURNO", strCurNo); //上一业务步骤的货币代码
    instance.setValue("AMT", strAMT); //上一业务步骤的决策金额
    instance.setValue("AMT2", strAMT2); //上一业务步骤的决策核销利息
    instance.setValue("CREATEDATE", strDate);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("BMNO", strBmNo);
    instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));

    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String strIsFirst = ctx.getParameter("IFRESPLOAN").trim();
    if (strIsFirst.equals("1")) {
      String strClientMgr = ctx.getParameter("FIRSTRESP").trim();
      String strWhere = " loginname='" + strClientMgr + "' and usertype<>'3'";
      String isnull = DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME", strWhere);
      if (isnull == null) {
        msgs.add("第一责任人不存在，请检查输入的第一责任人是否准确！");
        return -1;
      }
    }

    strOthers = ctx.getParameter("finish"); //获得完成审批标志
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String strIsFirst = ctx.getParameter("IFRESPLOAN").trim();
    if (strIsFirst.equals("1")) {
      String strClientMgr = ctx.getParameter("FIRSTRESP").trim();
      String strWhere = " loginname='" + strClientMgr + "' and usertype<>'3'";
      String isnull = DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME", strWhere);
      if (isnull == null) {
        msgs.add("第一责任人不存在，请检查输入的第一责任人是否准确！");
        return -1;
      }
    }

    strOthers = ctx.getParameter("finish"); //获得完成审批标志
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs, EventManager manager) {
    try {
      int iflag = 0;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      }
      if (iflag >= 0) {
        String msg = "信息保存成功！";
        BMTableData bd = BMTable.getBMTable(strBmNo);
        if(bd != null)
        {
          EnumerationBean eb = EnumerationType.getEnu("BMStatus");
          if(eb != null)
          {
            msg += "提交到"+(String)eb.getValue(new Integer(bd.bmStatus));
          }
        }

        ctx.setRequestAtrribute("title", "抵债资产审批");
        ctx.setRequestAtrribute("msg", msg);
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                        ErrorMessages msgs,
                        EventManager manager) {
    System.out.println(strOthers);
    try {
      int iflag = 0;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      }
      System.out.println(iflag);
      if (iflag >= 0) {
        String msg = "信息保存成功！";
        BMTableData bd = BMTable.getBMTable(strBmNo);
        if(bd != null)
        {
          EnumerationBean eb = EnumerationType.getEnu("BMStatus");
          if(eb != null)
          {
            msg += "提交到"+(String)eb.getValue(new Integer(bd.bmStatus));
          }
        }

        ctx.setRequestAtrribute("title", "抵债资产审批");
        ctx.setRequestAtrribute("msg", msg);
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    params = new Param();
    params.addParam(ParamName.Flag, strFlag);
    params.addParam(ParamName.BMNo, strBmNo);
    params.addParam(ParamName.BMTransNo, strBmTransNo);
    params.addParam(ParamName.BMActType, strBmActType);
    params.addParam(ParamName.BMType, strBmType);

    ctx.setRequestAtrribute(ParamName.ParamName, params);
    if (button.equals("BmComment")) {
      trigger(manager, "BMCOMMENTSLIST", null);
      return 0;
    }
    if (button.equals("BmTrans")) {
      trigger(manager, "BMTRANSLIST", null);
      return 0;
    }
    else {
      return -1;
    }
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {
    String strSubScbrh = SCBranch.getSubBranchAll(strScbrh);
    if (strSubScbrh == null || strSubScbrh.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      strSubScbrh = "'" + strSubScbrh.replaceAll(",", "','") + "'";
    }
    if (reffldnm.equals("DECIDEDBY")) {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", strScbrh2,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }
    if (reffldnm.equals("FIRSTRESP")) {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", strSubScbrh,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }

    return 0;
  }
}
