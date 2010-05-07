package zt.cms.cm.client;

import zt.cms.pub.*;
import zt.cms.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

public class ApplyTransferPageAction extends FormActions {
  String USERNO = null;
  String BRHID = null;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    USERNO = um.getUserName();
    BRHID = SCUser.getBrhId(um.getUserName());
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setValue("APPLYUSER", USERNO);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String formBrhid = DBUtil.getCellValue(conn, "CMCLIENT", "APPBRHID", "clientno='" + ctx.getParameter("CLIENTNO") + "'");
    if (formBrhid == null || formBrhid.trim().length() < 1) {
      msgs.add("�����û���ҵ�����㲻���ڣ�����ϵϵͳ����Ա��");
    }
    formBrhid = formBrhid.trim();
    instance.setValue("FORMERBRHID", formBrhid);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "FORMERBRHID", formBrhid);

    String toBrhid = ctx.getParameter("APPLYBRHID");
    toBrhid = toBrhid.trim();
    instance.setValue("APPLYBRHID", toBrhid);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "APPLYBRHID", toBrhid);

    if (formBrhid.equals(toBrhid)) {
      msgs.add("�ÿͻ��Ѿ����ڸ��������㣬û�б�Ҫ����ת�ƣ�");
      return -1;
    }

    String nowBalStr = "select rqloanledger.* ";
    nowBalStr += "from bmtable,rqloanledger ";
    nowBalStr += "where bmtable.clientno='" + ctx.getParameter("CLIENTNO") + "' and bmtable.brhid='"+formBrhid+"' and bmtable.bmno=rqloanledger.bmno";
    RecordSet nowBalRs = conn.executeQuery(nowBalStr);
    boolean hasComplete = true;
    while (nowBalRs.next()) {
      if (nowBalRs.getInt("NOWBAL")>0) {
        hasComplete = false;
        msgs.add("���ͻ���δ������ҵ���=" + nowBalRs.getString("bmno") + ",��ݺ�=" + nowBalRs.getString("CNLNO") + "");
      }
    }
    if (!hasComplete) {
      return -1;
    }

    String upBrhid = SCBranch.getSupBrh(formBrhid);
    if (upBrhid == null || upBrhid.trim().length() < 1) {
      msgs.add("�ϼ����������޷�ȡ��");
      return -1;
    }
    upBrhid = upBrhid.trim();

    String supBrhid = this.getSupBrh(toBrhid, formBrhid);
    if (supBrhid == null || supBrhid.trim().length() < 1) {
      msgs.add("�ϼ����������޷�ȡ��");
      return -1;
    }
    supBrhid = supBrhid.trim();
    if (!supBrhid.equals(upBrhid)) {
      supBrhid = supBrhid + "," + upBrhid;
    }

    instance.setValue("AUDITINGBRHID", supBrhid + "," + formBrhid);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "AUDITINGBRHID", supBrhid + "," + formBrhid);

    instance.setValue("APPLYUSER", USERNO);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "APPLYUSER", USERNO);

    instance.setValue("APPROVED", "0");
    int seqNo = (int) SerialNumber.getNextSN(assistor.getDefaultTbl(), "SEQNO");
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", seqNo + "");
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

    if (reffldnm.equals("APPLYBRHID")) {
      sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }
    return 0;
  }

  public String getSupBrh(String apply, String former) {
    if (SCBranch.checkSub(apply, former)) {
      return former;
    }
    else {
      String supBrhid = SCBranch.getSupBrh(apply);
      if (SCBranch.checkSub(former, supBrhid)) {
        return supBrhid;
      }
      else {
        supBrhid = SCBranch.getSupBrh(supBrhid);
        if (SCBranch.checkSub(former, supBrhid)) {
          return supBrhid;
        }
        else {
          supBrhid = SCBranch.getSupBrh(supBrhid);
          if (SCBranch.checkSub(former, supBrhid)) {
            return supBrhid;
          }
          else {
            supBrhid = SCBranch.getSupBrh(supBrhid);
            if (SCBranch.checkSub(former, supBrhid)) {
              return supBrhid;
            }
            else
            {
              supBrhid = SCBranch.getSupBrh(supBrhid);
              if (SCBranch.checkSub(former, supBrhid)) {
                return supBrhid;
              }
              else
                return null;
            }
          }
        }
      }
    }
  }

}
