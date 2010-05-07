package zt.cms.bm.appplus;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import zt.cms.pub.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.cmsi.biz.*;
import java.math.BigDecimal;

public class BMPldgPDILLoanPage extends FormActions {
  Param param = null;
  String BMNO = null;
  String SEQNO = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (param == null) {
      msgs.add("�������󣬲������󲻴��ڣ�");
      return -1;
    }
    BMNO = param.getBmNo();
    if (BMNO == null || BMNO.trim().length() < 1) {
      msgs.add("��������ҵ��Ų����ڣ�");
      return -1;
    }
    SEQNO = ctx.getParameter("SEQNO");
    //�޸�
    if (SEQNO != null && SEQNO.trim().length() > 0) {
      instance.setValue("BMNO", BMNO);
      instance.setValue("SEQNO", SEQNO);
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

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
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
    if (reffldnm.equals("ILBMNO")) {
      sqlWhereUtil.addWhereField("BMLOAN2", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
      sqlWhereUtil.addWhereField("BMLOAN2", "NOWBAL", "0",
                                 SqlWhereUtil.DataType_Number,
                                 sqlWhereUtil.OperatorType_Greater);
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    long tmp = zt.cms.pub.code.SerialNumber.getNextSN("BMPLDGPDILLOAN", "SEQNO");
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", "" + tmp);
    String ilbmno = ctx.getParameter("ILBMNO");
    if(ilbmno != null) ilbmno = ilbmno.trim();
    if(ilbmno.length() <= 0) ilbmno = null;
    if(ilbmno != null)
    {
      LoanLedger ll = LoanLedgerMan.getLoanLedger(ilbmno);
      if(ll !=  null)
      {
        if(ll.nowBal.compareTo(new BigDecimal(0)) <= 0)
        {
          msgs.add("��������������!");
          return -1;
        }
      }
      else
      {
        msgs.add("����Ĵ������!");
        return -1;
      }

    }

    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    return 0;
  }

}
