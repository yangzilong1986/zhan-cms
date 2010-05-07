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
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    BMNO = param.getBmNo();
    if (BMNO == null || BMNO.trim().length() < 1) {
      msgs.add("参数错误，业务号不存在！");
      return -1;
    }
    SEQNO = ctx.getParameter("SEQNO");
    //修改
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
    //BRHID（用户网点）
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      msgs.add("用户网点不存在！");
      return -1;
    }
    //SUBBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！");
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
          msgs.add("贷款金额必须大于零!");
          return -1;
        }
      }
      else
      {
        msgs.add("输入的贷款不存在!");
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
