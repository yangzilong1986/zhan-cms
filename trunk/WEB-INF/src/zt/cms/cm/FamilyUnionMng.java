package zt.cms.cm;

/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: FamilyUnionMng.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
 *  @author liwei
 */
import java.util.logging.*;

import zt.cms.pub.*;
import zt.cms.pub.code.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

public class FamilyUnionMng extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.cm.FamilyUnionMng");
  String strunionno = null;
  String strBrhid = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    String strAppBrhID = null;
    strunionno = ctx.getParameter("UNIONNO");
    if (getBrhID(ctx, msgs) != 0) {
      return -1;
    }

    String strFlag = (String)ctx.getRequestAttribute("flag");
    if (strFlag != null && strFlag.trim().equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }

    if (strunionno != null) {
      String strSqlApp = "select brhid from cmfamilyunion where unionno=";
      strSqlApp = strSqlApp + strunionno;
      RecordSet rs = conn.executeQuery(strSqlApp);
      if (rs.next()) {
        strAppBrhID = rs.getString("BRHID").trim();
      }
      instance.setValue("UNIONNO", Integer.parseInt(strunionno.trim()));
      if (! (SCBranch.checkSub(strAppBrhID, strBrhid) || strAppBrhID.equals(strBrhid))) {
        ctx.setRequestAtrribute("title", "���˿ͻ�����");
        ctx.setRequestAtrribute("msg", "�Բ���ҵ�����㲻��ά�����㷶Χ�ڣ�");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setTarget("/showinfo.jsp");
        return -1;
      }
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    if (getBrhID(ctx, msgs) != 0) {
      return -1;
    }
    if (reffldnm.equals("BRHID")) {
      //String MNTBRHID = SCUser.getBrhId(um.getUserName());
      if (strBrhid == null || strBrhid.length() < 1) {
        return -1;
      }
      //APPBRHIDs���û������µ�����ʵ���㣬�����Լ���
      String SUBBRHIDs = SCBranch.getSubBranchAll(strBrhid);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("�������㲻���ڣ�");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      //sqlWhereUtil
      sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs, SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String ssdate = ctx.getParameter("FROMDATE").trim();
    String sedate = ctx.getParameter("TODATE").trim();
    if ( (ssdate != null && !ssdate.equals("")) && (sedate != null) && !sedate.equals("")) {
      int isdate = 0;
      int iedate = 0;
      try {
        isdate = Integer.parseInt(ssdate);
        iedate = Integer.parseInt(sedate);
      }
      catch (NumberFormatException ex) {
        msgs.clear();
        msgs.add("�޸�ʧ�ܡ�ԭ��:");
        msgs.add("@  ���ڸ�ʽʧ�ܡ�");
        return -1;
      }
      if (isdate > iedate) {
        msgs.clear();
        msgs.add("�޸�ʧ�ܡ�ԭ��:");
        msgs.add("@  ��Ч����ʱ��������Ч��ʼʱ�䡣");
        return -1;
      }
    }

    String TEMPBRHID = ctx.getParameter("BRHID").trim();

    if (getBrhID(ctx, msgs) != 0) {
      return -1;
    }
    if (! (SCBranch.checkSub(TEMPBRHID, strBrhid) || TEMPBRHID.equals(strBrhid))) {
      msgs.add("�Ǽ����㲻��ά�����㷶Χ�ڣ�");
      return -1;
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String ssdate = ctx.getParameter("FROMDATE").trim();
    String sedate = ctx.getParameter("TODATE").trim();
    if ( (ssdate != null && !ssdate.equals("")) && (sedate != null) && !sedate.equals("")) {
      int isdate = 0;
      int iedate = 0;
      try {
        isdate = Integer.parseInt(ssdate);
        iedate = Integer.parseInt(sedate);
      }
      catch (NumberFormatException ex) {
        msgs.clear();
        msgs.add("���ʧ�ܡ�ԭ��:");
        msgs.add("@  ���ڸ�ʽʧ�ܡ�");
        return -1;
      }
      if (isdate > iedate) {
        msgs.clear();
        msgs.add("���ʧ�ܡ�ԭ��:");
        msgs.add("@  ��Ч����ʱ��������Ч��ʼʱ�䡣");
        return -1;
      }
    }

    String TEMPBRHID = ctx.getParameter("BRHID").trim();

    if (getBrhID(ctx, msgs) != 0) {
      return -1;
    }

    if (! (SCBranch.checkSub(TEMPBRHID, strBrhid) || TEMPBRHID.equals(strBrhid))) {
      msgs.add("�Ǽ����㲻��ά�����㷶Χ�ڣ�");
      return -1;
    }
    int iunionno = (int) SerialNumber.getNextSN("CMFAMILYUNION", "UNIONNO");
    assistor.setSqlFieldValue("CMFAMILYUNION","UNIONNO",iunionno+"");
    //instance.setValue("UNIONNO", iunionno);
    return 0;
  }

  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    if (getBrhID(ctx, msgs) != 0) {
      return -1;
    }

    String TEMPBRHID = result.getString("BRHID").trim();
    if (! (SCBranch.checkSub(TEMPBRHID, strBrhid) || TEMPBRHID.equals(strBrhid))) {
      msgs.add("�ÿͻ����ϣ��Ǽ����㣩����ά�����㷶Χ�ڣ�");
      return -1;
    }
    return 0;
  }

  public int postDeleteFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager) {
    if (msgs.size() != 0) {
      for (int i = 0; i < msgs.size(); i++) {
        ErrorMessage em = msgs.get(i);

        System.out.println("error" + i + ":" + em.getMessage());
        System.out.println("argument" + i + ":" + em.getArguments());
        if (em.getMessage().equals("-532")) {
          msgs.clear();
          msgs.add("������������¼�ѱ�ĳ�û�ʹ�ã�����ɾ�����������������ļ���");
          break;
        }

      }
    }
    return 0;
  }

  /**
   * ��չ�����buttonEvent��������Ӧ�Զ��尴ť�ĵ���¼�
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    if (strunionno == null || strunionno.length() < 1) {
      msgs.add("������С����δ��ӣ�");
      return -1;
    }
    ctx.setRequestAtrribute("UNIONNO", strunionno);
    if (button.equals("BTN_XZCY")) {
      trigger(manager, "CMFUMEMLIST", null);
    }
    return 0;
  }

  private int getBrhID(SessionContext ctx, ErrorMessages msgs) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (um == null) {
      msgs.add("����:û����Ȩ�û�!");
      return -1;
    }
    strBrhid = SCUser.getBrhId(um.getUserName());
    if (strBrhid == null) {
      msgs.add("����:û�������");
      return -1;
    }
    return 0;
  }

}
