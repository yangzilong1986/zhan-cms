package zt.cms.bm.ledger;
/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMAcptBillTMng.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */

import java.util.logging.*;

import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.biz.Ledger;
import zt.cmsi.mydb.MyDB;

public class BMAcptBillTMng extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMAcptBillTMng");
  private String strFlag = null;
  private String stracptbillno = null;
  private UserManager um = null;
  private String BRHID = null;
  private long newbillno = 0;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //维护时“提交”按钮改为可用
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    //flag
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    if (strFlag != null && strFlag.equals("write")) {
      fb.setUseSave(true);
      instance.setReadonly(false);
      fb.setTitle("银行承兑汇票台帐维护-详细");
    }
    else {
      fb.setUseSave(false);
      instance.setReadonly(true);
    }
    //
    um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    BRHID = SCUser.getBrhId(um.getUserName());
    //stracptbillno
    stracptbillno = ctx.getParameter("ACPTBILLNO");
    if (stracptbillno == null) {
      stracptbillno = (String) ctx.getRequestAttribute("ACPTBILLNO");
    }
    if (stracptbillno != null) {
      stracptbillno = stracptbillno.toString().trim();
      instance.setValue("ACPTBILLNO", stracptbillno);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String CREATEDATE = SystemDate.getSystemDate5("");
    String OPERATOR = null;
    try {
      OPERATOR = um.getUserName();
    }
    catch (Exception ex) {
      return -1;
    }
    //instance.setValue("BRHID", BRHID);
    instance.setValue("OPERATOR", OPERATOR);
    instance.setValue("CREATEDATE", CREATEDATE);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {

    String brhid = ctx.getParameter("BRHID");
    //System.out.println("brhid=========================="+ctx.getParameter("BRHID"));
    if (SCBranch.getBrhtype(brhid).equals("9")) {
      msgs.add("业务网点不能是虚网点！");
      return -1;
    }

    //System.out.println(ctx.setParameter(("BRHID"));

    this.newbillno = zt.cms.pub.code.SerialNumber.getNextSN("BMACPTBILL", "ACPTBILLNO");
    String ACPTBILLNO = this.newbillno + "";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "ACPTBILLNO", ACPTBILLNO);
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    //SUBBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }
    //BMNO
    if (reffldnm.equals("BMNO")) {
      sqlWhereUtil.addWhereField("BMTABLE", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
      sqlWhereUtil.addWhereField("BMTABLE", "TYPENO", "12",
                                 SqlWhereUtil.DataType_Number,
                                 sqlWhereUtil.OperatorType_Equals);
    }
    //CLIENTNO
    if (reffldnm.equals("CLIENTNO")) {
      sqlWhereUtil.addWhereField("CMCLIENT", "APPBRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    //BRHID
    if (reffldnm.equals("BRHID")) {
      sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String LASTMODIFIED = SystemDate.getSystemDate2();
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String OPERATOR = null;
    try {
      OPERATOR = um.getUserName();
    }
    catch (Exception ex) {
      return -1;
    }
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    Param pm = new Param();
    pm.addParam(ParamName.ParamName, pm);
    String strBmNo = ctx.getParameter("BMNO");
    if (button == null || button.equals("")) {
      msgs.clear();
      msgs.add("请求的服务不存在！");
      return -1;
    }
    if (button.equals("ACPTBILLBUTTON")) {
      //查看汇票承兑
      ctx.setRequestAtrribute("flag", "read");
      ctx.setRequestAtrribute("mybillno", stracptbillno);
      this.trigger(manager, "BMHPC0", null);
    }
    else if (button.equals("PRINTMOPAIEDNEY")) {
      //打印垫款借据

      LoanGrantData data = zt.cmsi.biz.LoanGranted.getLoanGrant(strBmNo);
      if (data != null) {
        int authorizedStatus = data.authorizedStatus;
        if (authorizedStatus != 4 && authorizedStatus != 5) {
          ctx.setRequestAtrribute("BMNO", strBmNo);
          ctx.setRequestAtrribute("IFADV", "YES");
          ctx.setTarget("/jspreport/loanbiz.jsp");

          return -1;
        }
        else {
          ctx.setRequestAtrribute("msg", "业务取消或者超期");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("msg", "承兑汇票未发生垫款");
        ctx.setTarget("/showinfo.jsp");
      }
    }

    return 0;
  }


  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager) {

    MyDB.getInstance().addDBConn(conn);
    int a = -1;
    if ( (a = Ledger.entryAcptBillManually(this.newbillno)) < 0) {
         MyDB.getInstance().releaseDBConn();
         return a;
    }
    MyDB.getInstance().releaseDBConn();

    return 0;
  }

}
