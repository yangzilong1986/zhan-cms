package zt.cms.bm.appplus;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004年1月6日
 */
public class BMPldgPDAssetPageAction extends FormActions {
  Param param = null;
  String BMNO = null;
  String PLEDGENO = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (param == null) {
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    BMNO = param.getBmNo();
    if (BMNO == null || BMNO.trim().length() < 1) {
      msgs.add("参数错误，业务号不存在！");
      return -1;
    }
    PLEDGENO = ctx.getParameter("PLEDGENO");
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    String formid = fb.getId().trim();
    //重设房屋质量状况枚举
    if (formid.equals("BMPLDGPDASSETPAGE1")) {
      ElementBean eb = fb.getElement("PDQLTSTATUS");
      eb.setEnutpname("PDESTQltStatus");
    }
    //重设土地性质的枚举
    else if (formid.equals("BMPLDGPDASSETPAGE5")) {
      ElementBean eb = fb.getElement("SPEC");
      eb.setEnutpname("PDLandType");
    }
    //修改
    if (PLEDGENO != null && PLEDGENO.trim().length() > 0) {
      instance.setValue("BMNO", BMNO);
      instance.setValue("PLEDGENO", PLEDGENO);
      ElementBean eb = fb.getElement("PDPLDGTYPE");
      eb.setReadonly(true);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("BMNO", BMNO);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    long tmp = zt.cms.pub.code.SerialNumber.getNextSN("BMPLDGPDASSET", "PLEDGENO");
    PLEDGENO=tmp+"";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "PLEDGENO", "" + PLEDGENO);
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    FormBean fb = instance.getFormBean();
    ElementBean eb0 = fb.getElement("PDPLDGTYPE");
    eb0.setReadonly(true);
    instance.setValue("BMNO", BMNO);
    instance.setValue("PLEDGENO",PLEDGENO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    instance.setValue("BMNO", BMNO);
    instance.setValue("PLEDGENO",PLEDGENO);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager)  {
    ctx.setRequestAtrribute(ParamName.ParamName, this.param);
    trigger(manager, "BMPLDGPDASSETPAGE" + button, null);
    return 0;
  }
}
