package zt.cms.bm.pd;

import zt.cms.bm.common.*;
import zt.cms.pub.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

public class BMPDAssetsPageAction extends FormActions {
  String PDMAINNO=null;
  String PDNO=null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    PDNO = ctx.getParameter("PDNO");
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    String formid = fb.getId().trim();
    //重设房屋质量状况枚举
    if (formid.equals("BMPDASSETSPAGE1")) {
      ElementBean eb = fb.getElement("PDQLTSTATUS");
      eb.setEnutpname("PDESTQltStatus");
    }
    //重设土地性质的枚举
    else if (formid.equals("BMPDASSETSPAGE5")) {
      ElementBean eb = fb.getElement("SPEC");
      eb.setEnutpname("PDLandType");
    }
    //修改
    if (PDNO != null && PDNO.trim().length() > 0) {
      instance.setValue("PDNO", PDNO.trim());
      ElementBean eb = fb.getElement("PDPLDGTYPE");
      eb.setReadonly(true);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    //填加
    else{
      ElementBean eb1 = fb.getElement("tenancy");
      ElementBean eb2 = fb.getElement("covert");
      ElementBean eb3 = fb.getElement("photo");
      eb1.setReadonly(true);
      eb2.setReadonly(true);
      eb3.setReadonly(true);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String OPERATOR = um.getUserName();
    PDMAINNO=(String)ctx.getRequestAttribute("PDMAINNO");
    String APPDATE = SystemDate.getSystemDate5("");
    instance.setValue("CREATEDATE", APPDATE);
    instance.setValue("OPERATOR", OPERATOR);
    instance.setValue("PDMAINNO", PDMAINNO);
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      msgs.add("用户网点不存在！");
      return -1;
    }
    //APPBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }
    //sqlWhereUtil
    if (reffldnm.equals("ADMINEDBY")) {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    if (reffldnm.equals("BRHID")) {
      sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    long tmp = zt.cms.pub.code.SerialNumber.getNextSN("BMPDASSETS", "PDNO");
    PDNO=tmp+"";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PDNO", "" + PDNO);
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    FormBean fb = instance.getFormBean();
    ElementBean eb0 = fb.getElement("PDPLDGTYPE");
    ElementBean eb1 = fb.getElement("tenancy");
    ElementBean eb2 = fb.getElement("covert");
    ElementBean eb3 = fb.getElement("photo");
    eb0.setReadonly(true);
    eb1.setReadonly(false);
    eb2.setReadonly(false);
    eb3.setReadonly(false);
    instance.setValue("PDNO",PDNO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String OPERATOR = um.getUserName();
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    instance.setValue("PDNO",PDNO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    Param param = ParamFactory.getParamByCtx(ctx);
    ctx.setRequestAtrribute("BMPARAM", param);
    ctx.setRequestAtrribute("PDMAINNO",PDMAINNO);
    ctx.setRequestAtrribute("PDNO",PDNO);
    if (button.equals("tenancy")) {
      trigger(manager, "BMPDTRANSLIST", null);
    }
    else if (button.equals("covert")) {
      trigger(manager, "BMPDTRANSLISTBX", null);
    }
    else if(button.equals("photo")){
        ctx.setRequestAtrribute("TABLENAME", "BMPDASSETS");
        ctx.setRequestAtrribute("WHERECONDITION", "PDNO=" + this.PDNO + "");
        ctx.setRequestAtrribute("title","抵债资产图片");

        ctx.setRequestAtrribute("flag","write");
        ctx.setTarget("/photo/photo.jsp");

    }else{
        trigger(manager, "BMPDASSETSPAGE"+button, null);
    }
    return 0;
  }

}
