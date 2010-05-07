package zt.cms.bm.inactloan;

/**
 *���û�������Ĳ��������б�
 *
 *@author     wxj
 *@created    2004��5��21��
 */
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.form.config.*;
import zt.cms.pub.*;
public class BMZHBLPageAction extends FormActions {
  String flag = null;
  String ilno = null;
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    flag = (String) ctx.getRequestAttribute("flag");
    if (flag == null) {
      flag = "read";
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //ilno
    ilno = ctx.getParameter("ILNO");
    if (ilno != null && ilno.trim().length() > 0) {
      //trigger
      instance.useCloneFormBean();
      FormBean fb=instance.getFormBean();
      ElementBean eb=fb.getElement("BTN_HKJL");
      eb.setReadonly(false);
      instance.setValue("ILNO", ilno);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (reffldnm.equals("BRHID") || reffldnm.equals("ADMINEDBY")) {
      //BRHID���û����㣩
      String BRHID = SCUser.getBrhId(um.getUserName());
      if (BRHID == null || BRHID.length() < 1) {
        msgs.add("�û����㲻���ڣ�");
        return -1;
      }
      //SUBBRHIDs���û������µ�����ʵ���㣬�����Լ���
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("�������㲻���ڣ�");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      //sqlWhereUtil
      if (reffldnm.equals("BRHID")) {
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      if (reffldnm.equals("ADMINEDBY")) {
        sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In,
                                   sqlWhereUtil.RelationOperator_And);
        sqlWhereUtil.addWhereField("SCUSER", "USERTYPE", "3",
//                                   SqlWhereUtil.DataType_Number,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Not_Equals);
      }
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    ilno = zt.cms.pub.code.SerialNumber.getNextSN("BMINACTLOANEXCHANGE","ILNO")+"";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "ILNO", ilno);
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("ILNO", ilno);
    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    instance.useCloneFormBean();
    FormBean fb=instance.getFormBean();
    ElementBean eb=fb.getElement("BTN_HKJL");
    eb.setReadonly(false);
    return 0;
  }

  public int preDelete(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String sql = "delete from BMILEXRECALLAPP where ILNO='" + ilno + "'";
    conn.executeUpdate(sql);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("ILNO",ilno);
    ctx.setRequestAtrribute("flag",flag);
    trigger(manager, "BMILEXRECALLLIST", null);
    return 0;
  }

}
