package zt.cms.bm.pd;

import zt.platform.form.control.FormActions;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.pub.code.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.cenum.EnumValue;
import zt.cms.bm.common.*;
import zt.cmsi.mydb.*;
import zt.cmsi.biz.*;
import zt.cmsi.pub.define.*;

public class BMPDTransPageActionBX
    extends FormActions {
  private String pdno = null;
  private String pdtransno = null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    //RightChecker.checkReadonly(ctx, conn, instance);
    Param param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (param != null) {
      this.pdno =  (String)param.getParam(ParamName.PK1Str);
      this.pdtransno = (String)param.getParam(ParamName.PK2Str);
    }

    if (this.pdno != null && this.pdtransno != null) {
      instance.setValue("PDTRANSNO", this.pdtransno);
      instance.setValue("PDNO", this.pdno);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("PDNO", this.pdno);
    instance.setValue("MANAGEMODE", EnumValue.ManageMode_BianXian);
    String APPDATE = SystemDate.getSystemDate5("");
    instance.setValue("CREATEDATE",APPDATE);

    //instance.setFieldReadonly("PDNO", true);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    int transNo = (int) SerialNumber.getNextSN(assistor.getDefaultTbl(),
                                               "PDTRANSNO");
    instance.setValue("PDTRANSNO", transNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PDTRANSNO",
                              transNo + "");
    String operator = SessionInfo.getLoginUserName(ctx);
    instance.setValue("OPERATOR", operator);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR",
                              operator + "");

    instance.setValue("MANAGEMODE", EnumValue.ManageMode_BianXian);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "MANAGEMODE",
                              "" + EnumValue.ManageMode_BianXian);


    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "RECEIVABLESAMT",
                              "" + (Float.parseFloat(ctx.getParameter("BXJE"))-Float.parseFloat(ctx.getParameter("OFFSETAMT")) ));

    return 0;
  }

  public int preDelete(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    int errorcode = 0;
    try {
      MyDB.getInstance().addDBConn(conn);
      errorcode = InactLoanMan.processPDTrans(Integer.parseInt(this.pdno));
    }
    catch (Exception e) {}
    finally {
      MyDB.getInstance().releaseDBConn();
      return errorcode;
    }
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager)
  {
    int errorcode = 0;
    try {
      MyDB.getInstance().addDBConn(conn);
      errorcode = InactLoanMan.processPDTrans(Integer.parseInt(this.pdno));
    }
    catch (Exception e) {}
    finally {
      MyDB.getInstance().releaseDBConn();
      return errorcode;
    }
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager) {
    int errorcode = 0;
    try {
      MyDB.getInstance().addDBConn(conn);
      errorcode = InactLoanMan.processPDTrans(Integer.parseInt(this.pdno));
    }
    catch (Exception e) {}
    finally {
      MyDB.getInstance().releaseDBConn();
      return errorcode;
    }
  }
  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "RECEIVABLESAMT",
                            "" + (Float.parseFloat(ctx.getParameter("BXJE"))-Float.parseFloat(ctx.getParameter("OFFSETAMT")) ));

    return 0;
}



}
