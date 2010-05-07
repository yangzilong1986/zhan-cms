package zt.cms.bm.ledger;
/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
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

public class BMTXTMng extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.ledger.BMTXTMng");
  private String strFlag = null;
  private String strbilldisno = null;
  private UserManager um = null;
  private String BRHID = null;
  private long newbillno = 0;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //ά��ʱ���ύ����ť��Ϊ����
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    //strFlag
    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    if (strFlag != null && strFlag.equals("write")) {
      fb.setUseSave(true);
      instance.setReadonly(false);
    }
    else {
      fb.setUseSave(false);
      instance.setReadonly(true);
    }
    //
    um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    BRHID = SCUser.getBrhId(um.getUserName());
    //stracptbillno
    strbilldisno = ctx.getParameter("BILLDISNO");
    if (strbilldisno != null) {
      strbilldisno = strbilldisno.toString().trim();
      instance.setValue("BILLDISNO", strbilldisno);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    //SUBBRHIDs���û������µ�����ʵ���㣬�����Լ���
    String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("�������㲻���ڣ�");
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
      sqlWhereUtil.addWhereField("BMTABLE", "TYPENO", "(13,14)",
                                 SqlWhereUtil.DataType_Number,
                                 sqlWhereUtil.OperatorType_In);
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

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String OPERATOR = null;
    try {
      OPERATOR = um.getUserName();
    }
    catch (Exception ex) {
      return -1;
    }
    //assistor.setSqlFieldValue(assistor.getDefaultTbl(), "LASTMODIFIED", LASTMODIFIED);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {

    String brhid = ctx.getParameter("BRHID");
    if (SCBranch.getBrhtype(brhid).equals("9")) {
      msgs.add("ҵ�����㲻���������㣡");
      return -1;
    }

    newbillno = zt.cms.pub.code.SerialNumber.getNextSN("BMBILLDIS", "BILLDISNO");
    String BILLDISNO = newbillno + "";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BILLDISNO", BILLDISNO);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    if (button == null || button.equals("")) {
      msgs.clear();
      msgs.add("����ķ��񲻴��ڣ�");
      return -1;
    }
    String strbilldisstatus = ctx.getParameter("BILLDISSTATUS").trim();
    String strBmNo = ctx.getParameter("BMNO");
    Param pm = new Param();
    pm.addParam(ParamName.Flag, strFlag);
    pm.addParam("BILLDISNO", strbilldisno);
    if (button.equals("ACPTBILLBUTTON")) {
      pm.addParam(ParamName.Flag, ParamName.Flag_READ);
      if (strbilldisstatus != null) {
        if ( (Integer.parseInt(strbilldisstatus) ==
              zt.cmsi.pub.cenum.EnumValue.BillDisStatus_TieXian) ||
            (Integer.parseInt(strbilldisstatus) ==
             zt.cmsi.pub.cenum.EnumValue.BillDisStatus_TieXianShouHui) ||
            (Integer.parseInt(strbilldisstatus) ==
             zt.cmsi.pub.cenum.EnumValue.BillDisStatus_TieXianDianKuan) ||
            (Integer.parseInt(strbilldisstatus) ==
             zt.cmsi.pub.cenum.EnumValue.BillDisStatus_TieXianDianKuanShouHui)) {
          ctx.setRequestAtrribute(ParamName.ParamName, pm);
          this.trigger(manager, "BMBDH0", null);
        }
        else {
          pm.addParam("FORMID", "BMBDH0");
          ctx.setRequestAtrribute(ParamName.ParamName, pm);
          this.trigger(manager, "BMBDH0", null);
        }
      }
    }
    else if (button.equals("PRINTMOPAIEDNEY")) {
      BillDisData data = BMTrans.billDisNoToBMNo(Integer.parseInt(strbilldisno));
      if (data != null && data.advBMNo != null) {
        LoanGrantData grantData = zt.cmsi.biz.LoanGranted.getLoanGrant(data.advBMNo);
        if (grantData != null) {
          if (grantData.authorizedStatus != 4 && grantData.authorizedStatus != 5) {
            ctx.setRequestAtrribute("BMNO", data.advBMNo);
            ctx.setRequestAtrribute("IFADV", "YES");
            ctx.setTarget("/jspreport/loanbiz.jsp");
          }
          else {
            ctx.setRequestAtrribute("msg", "ҵ��ȡ�����߳���");
            ctx.setTarget("/showinfo.jsp");
          }
        }
        else {
          ctx.setRequestAtrribute("msg", "δ��Ȩ");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("msg", "δ��Ȩ,û�е��ҵ��ţ�δ�������");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }


  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager) {

    MyDB.getInstance().addDBConn(conn);
    int a = -1;
    if ( (a = Ledger.entryBillDisManually(this.newbillno)) < 0) {
         MyDB.getInstance().releaseDBConn();
         return a;
    }
    MyDB.getInstance().releaseDBConn();

    return 0;
  }

}
