package zt.cms.bm.postloan;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * edit by wxj at 040513
 */

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

public class DataMainPage extends FormActions {

  String bmno = "";
  int bmtype = -1;
  private String oldclientno = null;
  private String oldclientname = null;

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs,
                  EventManager manager, String parameter) {
    instance.useCloneFormBean();
    instance.setValue("BMNO", ctx.getParameter("BMNO"));
    this.bmno = ctx.getParameter("BMNO");
//    RecordSet rs = conn.executeQuery("select typeno from bmtable where bmno='" + this.bmno + "'");
//    if (rs.next()) {
//      this.bmtype = rs.getInt(0);
//    }
    String IFRESPLOAN = DBUtil.getCellValue(conn, "BMTABLEAPP", "IFRESPLOAN", "BMNO='" + this.bmno + "'");
    if (IFRESPLOAN != null) {
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("FIRSTRESP");
      if (IFRESPLOAN.equals("1")) {
        eb.setCaption("第一责任人");
      }
      else {
        eb.setCaption("管理责任人");
      }
    }
    String bmtype = DBUtil.getCellValue(conn, "BMTABLE", "TYPENO", "BMNO='" + this.bmno + "'");
    String appmonths = DBUtil.getCellValue(conn, "BMTABLEAPP", "APPMONTHS", "BMNO='" + this.bmno + "'");
    System.out.println("appmonths is "+appmonths);
    if((bmtype != null && bmtype.compareToIgnoreCase("" +zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin) == 0))
    {
      //System.out.println("is shouxin");
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("BRATE");
      eb.setComponetTp(6);
      eb = fb.getElement("FINALRATE");
      eb.setComponetTp(6);
      eb = fb.getElement("FRATE");
      eb.setComponetTp(6);
      eb = fb.getElement("APPMONTHS");
      eb.setComponetTp(6);
    }
    else if(appmonths == null || (appmonths != null && appmonths.compareTo("0") ==0))
    {
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("APPMONTHS");
      eb.setComponetTp(6);
    }
    else
    {
      //System.out.println("is not shouxin");
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("FINALSTARTDATE");
      eb.setComponetTp(6);
      eb = fb.getElement("FINALENDDATE");
      eb.setComponetTp(6);
    }

    trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
            Event.BRANCH_CONTINUE_TYPE);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String typeNo = ctx.getParameter("TYPENO");
    String clientNo = ctx.getParameter("CLIENTNO");
    String clientName = ctx.getParameter("CLIENTNAME");

    if (clientNo == null || clientNo.trim().equals("")) {
      msgs.add("客户号不可以为空");
      return -1;
    }
    //如果为移行用户，先不检查
    if (!clientNo.substring(0, 1).equals("9")) {
      RecordSet rs = conn.executeQuery("select * from cmclient where clientno='" + clientNo +
                                       "'");
      if (!rs.next()) {
        msgs.add("客户号不存在！");
        return -1;
      }
    }

    int ret = 0;

//    System.out.println(""+this.oldclientname + ":"+clientName+":"+this.oldclientno+":"+clientNo+":"+this.bmtype +":"+typeNo);
//    if(this.oldclientname.compareToIgnoreCase(clientName) != 0) System.out.println("not equal");

    if(this.oldclientname == null || this.oldclientno == null ||
       this.oldclientname.compareToIgnoreCase(clientName) != 0 ||
       this.oldclientno.compareToIgnoreCase(clientNo) != 0 ||
       this.bmtype != Integer.parseInt(typeNo))
    {
      if (clientName != null) {
        clientName = DBUtil.toDB(clientName);
      }

      System.out.println("begin !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      String str = "update bmtable set operator='" +
          SessionInfo.getLoginUserName(ctx) +
          "',typeno=" + typeNo + ",clientno='" + clientNo +
          "',clientname='" + clientName +
          "' where bmno='" + this.bmno + "'";
      ret = conn.executeUpdate(str);
    }

    return ret;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    ctx.setRequestAtrribute("msg", "修改成功");
    ctx.setTarget("/showinfo.jsp");
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                        ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    RecordSet rs = conn.executeQuery("select * from bmtable where bmno='" + this.bmno + "'");
    while (rs.next()) {
      this.bmtype = rs.getInt("TYPENO");
      this.oldclientname = DBUtil.fromDB(rs.getString("CLIENTNAME"));
      this.oldclientno = rs.getString("CLIENTNO");
      if(this.oldclientno != null) this.oldclientno = this.oldclientno.trim();

      instance.setValue("TYPENO", rs.getString("TYPENO"));
      instance.setValue("CLIENTNAME", DBUtil.fromDB(rs.getString("CLIENTNAME")));
      instance.setValue("CREATEDATE", rs.getString("CREATEDATE"));
      EnumerationBean enu = EnumerationType.getEnu("BMStatus");
      instance.setValue("BMSTATUS", (String) enu.getValue(new Integer(rs.getInt("BMSTATUS"))));
      instance.setValue("BRHID", rs.getString("BRHID"));
      instance.setValue("INITBRHID", rs.getString("INITBRHID"));
      instance.setValue("OPERATOR", rs.getString("OPERATOR"));
      instance.setValue("LASTCHECKDATE", rs.getString("LASTCHECKDATE"));
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
      USER_INFO_NAME);

    //BRHID（用户网点）
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      return -1;
    }
    //APPBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getAllSubBrhAndSelf1(BRHID);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }

    if(reffldnm.equals("CLIENTNO")){
      sqlWhereUtil.addWhereField("CMCLIENT", "APPBRHID", SUBBRHIDs,
                           SqlWhereUtil.DataType_Sql,
                           sqlWhereUtil.OperatorType_In);

    }else{
      sqlWhereUtil.addWhereField("SCUSER", "USERTYPE", "3",
//                                 SqlWhereUtil.DataType_Number,
                                 SqlWhereUtil.DataType_Character,
                                 sqlWhereUtil.OperatorType_Not_Equals);

      sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    ctx.setRequestAtrribute("BMNO", this.bmno);
    Param param = new Param();
    param.addParam(ParamName.BMNo, this.bmno);
    param.addParam(ParamName.Flag, "write");
    ctx.setRequestAtrribute("BMPARAM", param);
    if (button.equals("GUARANTOR")) {
      RightChecker.transReadOnly(ctx, conn, instance);
      trigger(manager, "BMGUARANTORLIST", null);
    }
    else if (button.equals("BTN_DYDJ")) {
      trigger(manager, "BMPLEDGELIST", null);
    }
    else if (button.equals("BTN_ZYDJ")) {
      trigger(manager, "BMPLEDGELIST1", null);
    }
    else {
      int startActType = BMType.getInstance().getStartAct(this.bmtype).intValue();
      BMProg prog = BMRoute.getInstance().getActProg(this.bmtype, startActType, null);
      RightChecker.transReadOnly(ctx, conn, instance);
      ctx.setRequestAtrribute("isJustWeihu", "true");
      if (prog != null) {
        trigger(manager, prog.getProgName(), null);
      }
      else {
        ctx.setRequestAtrribute("msg", "对应的数据维护不存在！");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }
}
