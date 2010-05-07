package zt.cms.bm.bill;

/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: Function.java,v 1.1 2005/06/28 07:00:22 jgo Exp $
 *
 * 
 */

import zt.cms.pub.*;
import zt.cms.report.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.utils.Debug;

public class Function {

  public static final int INTEGER_TYPE = 1;
  public static final int STRING_TYPE = 2;
  public static final int WRONG_DATE_FORMAT = 3;
  public static final int DATES_COMPARENT_WRONG = 4;
  public static final int DATES_COMPARENT_RIGHT = 5;
  public static String myKey = "5160";


  public static boolean isDataExist(DatabaseConnection conn, String tablename, String fieldname,
                                    String value, int type) {
    boolean isObjectExist = false;
    String strsql = null;


    if (type == INTEGER_TYPE) {
      strsql = "select * from " + tablename + " where " + fieldname + "=" + value;
    }
    else if (type == STRING_TYPE) {
      strsql = "select * from " + tablename + " where " + fieldname + "='" + value + "'";
    }
    if (strsql != null) {
      RecordSet rs = null;
      try {
        rs = conn.executeQuery(strsql);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return isObjectExist;
      }
      if (rs.next()) {
        isObjectExist = true;
      }
    }
    return isObjectExist;
  }

  public static boolean isDataExist(DatabaseConnection conn, String tablename, String where) {
    boolean isObjectExist = false;
    String strsql = null;
    strsql = "select * from " + tablename + " " + where;
    if (strsql != null) {
      RecordSet rs = null;
      try {
        rs = conn.executeQuery(strsql);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return isObjectExist;
      }
      if (rs.next()) {
        isObjectExist = true;
      }
    }
    return isObjectExist;
  }

  public static int isDatesCompWrong(String startdate, String enddate) {
    startdate = startdate.trim();
    enddate = enddate.trim();
    if ( (startdate != null && !startdate.equals("")) && (enddate != null) &&
        !enddate.equals("")) {
      int isdate = 0;
      int iedate = 0;
      try {
        isdate = Integer.parseInt(startdate);
        iedate = Integer.parseInt(enddate);
      }
      catch (NumberFormatException ex) {
        return WRONG_DATE_FORMAT;
      }
      if (isdate > iedate) {
        return DATES_COMPARENT_WRONG;
      }
    }
    return DATES_COMPARENT_RIGHT;
  }

  public static boolean checkData(SessionContext ctx, String name, String value) {
    if (name == null) {
      return false;
    }
    if (value == null) {

      if (ctx.getParameter(name.trim()) == null) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      if ( (value.trim()).equals(ctx.getParameter(name.trim()))) {
        return true;
      }
      else {
        return false;
      }
    }
  }

  public static String getUserName(SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String strUserName = null;
    try {
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return strUserName;
  }

  public static String getUserId(SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
    String strUserId = null;
    try {
      strUserId = um.getUserId();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return strUserId;
  }

  public static String getAllSubBrhIds(ErrorMessages msgs, SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String mybrhid = SCUser.getBrhId(um.getUserName());
    if (mybrhid == null || mybrhid.length() < 1) {
      msgs.add("当前用户无关联网点！  ");
      return null;
    }
    //APPBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getSubBranchAll(mybrhid);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！  ");
      return null;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }
    return SUBBRHIDs;
  }

  public static String getBrhid(SessionContext ctx) {
    return SCUser.getBrhId(getUserName(ctx));
  }

  public static boolean isNowBrhidRight(SessionContext ctx, String nowBrhid, String Brhid,
                                        String errormsgs
                                        , ErrorMessages msgs) {
    String strBrhid = getBrhid(ctx);
    if (strBrhid == null) {
      msgs.clear();
      msgs.add("无用户网点!");
      return false;
    }
    else {
      if (! (SCBranch.checkSub(nowBrhid, strBrhid) || strBrhid.equals(nowBrhid))) {
        msgs.clear();
        msgs.add(errormsgs);
        return false;
      }
    }
    return true;
  }

  public static String getFormTitleByInstance(FormInstance instance) {
    return FormBeanManager.getForm(instance.getFormid()).getTitle();
  }

  public static String getSelfAndAllSubBrhIds(ErrorMessages msgs, SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String mybrhid = SCUser.getBrhId(um.getUserName());
    if (mybrhid == null || mybrhid.length() < 1) {
      msgs.add("当前用户无关联网点！ ");
      return null;
    }
    //APPBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getSubBranchAll(mybrhid);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！ ");
      return null;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }
    return SUBBRHIDs;
  }

  public static void turnToPrint(String strbmtype, String strbmno, SessionContext ctx) {

    SystemDate sd = new SystemDate();
    String strSystemDate = sd.getSystemDate5("-");

    if (Integer.parseInt(strbmtype) == zt.cmsi.pub.cenum.EnumValue.BMType_YiZiDiZhai) {
      //以资抵债：抵债资产打印通知书

      if (isDiZhaiPrint(strbmno)) {
        RepParamObject rpobject = new RepParamObject();
        rpobject.setBindFile("DueBill/PayBackAssets");
        rpobject.setBmNo(strbmno);
        rpobject.setSqlString("select (select s.sname from scbranch s where s.brhid=b.BRHID) f1,a.BMNO f2,(select t.clientname from bmtable t where a.bmno=t.bmno) f3,b.ORIGBMNO f4,b.ORIGACCOUNTNO f5,b.ORIGDUEBILLNO f6,b.ORIGCONTRACTNO f7,(select d.curname from SCCURRENCY d where d.curno=b.CURNO) f8,a.FINALAMT f9 from bmtableapp a,bmappinactiveloan b where a.bmno = b.bmno and  a.bmno =?");
        rpobject.setPrintDate(strSystemDate);
        ctx.setRequestAtrribute("REPPARAMOBJECT", rpobject);
        ctx.setTarget("/jspreport/template.jsp");
      }
      else {
        ctx.setRequestAtrribute("title", "提示信息:");
        ctx.setRequestAtrribute("msg", "未满足条件：业务状态不是发放或正常。");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }

    }
    else if (Integer.parseInt(strbmtype) == zt.cmsi.pub.cenum.EnumValue.BMType_BuLiangDaiKuanHeXiao) {
      //不良贷款核销：不良贷款核销打印通知书
       if (isDiZhaiPrint(strbmno)) {
        RepParamObject rpobject = new RepParamObject();
        rpobject.setBindFile("DueBill/InactLoan");
        rpobject.setBmNo(strbmno);
        rpobject.setSqlString("select (select s.sname from scbranch s where s.brhid=b.BRHID) f1,a.bmno f2,(select t.clientname from bmtable t where t.bmno=a.bmno) f3,a.ORIGBMNO f4,a.ORIGACCOUNTNO f5,a.ORIGDUEBILLNO f6,b.ORIGCONTRACTNO f7,(select r.curname from sccurrency r where r.curno=b.CURNO) f8,a.FINALAMT f9,a.FINALAMT2 f10 from BMTableApp a,BMAppInactiveLoan b where a.bmno = b.bmno and a.bmno = ?");
        rpobject.setPrintDate(strSystemDate);
        ctx.setRequestAtrribute("REPPARAMOBJECT", rpobject);
        ctx.setTarget("/jspreport/template.jsp");
      }
      else {
        ctx.setRequestAtrribute("title", "提示信息:");
        ctx.setRequestAtrribute("msg", "未满足条件：业务状态不是发放或正常。");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }

    }
    else {
      //其他：贷款业务打印借据

      if (isDiZhaiPrint(strbmno)) {
        RepParamObject rpobject = new RepParamObject();
        rpobject.setBindFile("DueBill/LoanBiz");
        rpobject.setBmNo(strbmno);
        rpobject.setSqlString("select (select s.sname from scbranch s where s.brhid=b.BRHID) f1,a.bmno f2,b.clientname f3,(select c.address from cmclient c where a.clientno = c.clientno) f4,(select d.enudt from ptenuminfodetl d where d.enuid='BMType' and d.enutp=a.TYPENO) f5,a.LOANPURPOSE f6,a.FINALRATE f7,(select r.curname from sccurrency r where r.curno=a.CURNO) f8,a.FINALAMT f9 from BMTableApp a ,BMTable b where a.bmno = b.bmno and a.bmno = ?");
        rpobject.setPrintDate(strSystemDate);
        ctx.setRequestAtrribute("REPPARAMOBJECT", rpobject);
        ctx.setTarget("/jspreport/template.jsp");
      }
      else {
        ctx.setRequestAtrribute("title", "提示信息");
        ctx.setRequestAtrribute("msg", "未满足条件：业务状态不是发放或正常。");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setRequestAtrribute("isback", "0");
        ctx.setTarget("/showinfo.jsp");
      }

    }
  }

  public static String getSqlByBeanName(FormInstance instance) {
    FormBean formBean = FormBeanManager.getForm(instance.getFormid());
    String stret = formBean.getListsql();
    if (stret != null) {
      return stret;
    }
    return "";
  }

  public static String getBandFile(FormInstance instance) {
    FormBean formBean = FormBeanManager.getForm(instance.getFormid());
    String stret = formBean.getCountsql();
    if (stret != null) {
      return stret.trim();
    }
    return "";
  }

  public static ElementBean getElementBeanByName(FormInstance instance, String strName) {
    FormBean formBean = FormBeanManager.getForm(instance.getFormid());
    ElementBean eb = formBean.getElement(strName);
    return eb;
  }

  public static boolean isDiZhaiPrint(String strbmno) {

    DatabaseConnection conn = MyDB.getInstance().apGetConn();
    try {
      int a = DBRun.getBizStatusByBmNo(strbmno, conn);
      if ( (a == zt.cmsi.pub.cenum.EnumValue.BMStatus_FaFang) ||
          (a == zt.cmsi.pub.cenum.EnumValue.BMStatus_ZhengChang)) {
        return true;
      }
      else
        return false;
    }
    catch (Exception e) {
      Debug.debug(e);
      return false;
    }
    finally
    {
      MyDB.getInstance().apReleaseConn(0);
    }
  }
}
