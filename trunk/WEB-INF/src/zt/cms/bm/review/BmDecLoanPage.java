package zt.cms.bm.review;
/**
 * <p>Title: 贷款业务审批(第一级，第二级，第三级公用)</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  中天信息</p>
 * <p>Company: 中天信息</p>
 * @author YUSG
 * @date   2003/12/31    created
 * @version 1.0
 * edit by wxj at 040513
 */

import java.util.*;

import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import java.math.BigDecimal;
import zt.platform.utils.Debug;
import java.lang.*;

public class BmDecLoanPage extends FormActions {
  private String strFlag = null; //读写标志
  public Param params = null; //发送的变量集合
  private Param paramg = null; //获得的变量集合
  private String strBmNo = null; //业务号
  private String strBmTransNo = null; //业务明细号
  private String strUserName = null; //当前登陆用户名
  private String strOthers = "0"; //是否完成审批标志
  private String strBmActType = null; //审批级别
  private String strBmType = null; //贷款类型
  private String strScbrh = null; //所在网点
  private String strIfrespLoan = null; //是否有责任人
  private String strFirstResp = null; //第一责任人
  private String strFirstRespPct = null; //第一责任人比例

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("数据不完整无法审批，请检查数据");
      return -1;
    }

    instance.useCloneFormBean();
    strFlag = paramg.getParam(ParamName.Flag).toString();
    strBmNo = paramg.getParam(ParamName.BMNo).toString();
    strBmTransNo = paramg.getParam(ParamName.BMTransNo).toString();
    strBmActType = paramg.getParam(ParamName.BMActType).toString();
    strBmType = paramg.getParam(ParamName.BMType).toString();

    if(strBmType.equals("11")){
        FormBean fb = instance.getFormBean();
        ElementBean endDate = fb.getElement("ENDDATE");
        ElementBean startDate = fb.getElement("STARTDATE");
        ElementBean mmm = fb.getElement("MONTHS");
        endDate.setComponetTp(18);
        startDate.setComponetTp(18);
        endDate.setXposition(4);
        startDate.setXposition(4);

        ElementBean rate = fb.getElement("RATE");
        rate.setComponetTp(6);
        mmm.setComponetTp(6);

        ElementBean fff = fb.getElement("FRATE");
        ElementBean bbb = fb.getElement("BRATE");
        fff.setIsnull(true);
        fff.setComponetTp(6);
        bbb.setIsnull(true);
        bbb.setComponetTp(6);

    }


    if (strBmNo == null || strBmTransNo == null || strBmActType == null) {
      msgs.add("数据不完整无法审批，请检查数据");
      return -1;
    }
    else {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
      try {
        strUserName = um.getUserName();
        strScbrh = SCUser.getBrhId(strUserName);
        if (strScbrh == null || strScbrh.length() < 1) {
          msgs.add("下属网点不存在！");
          return -1;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      //如果bmdecision有数据则进入编辑模式
      int iCount = 0;
      String strSql = "select count(*) from bmdecision where bmno='" + strBmNo;
      strSql += "' and bmtransno=" + strBmTransNo;
      RecordSet rs = conn.executeQuery(strSql);
      if (rs.next()) {
        iCount = rs.getInt(0);
      }
      if (iCount > 0) {
        instance.setValue("BMNO", strBmNo);
        instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
      }

      if (strFlag.equals( (String) ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      //承兑汇票去掉决策利率，申请期限（月）改为申请期限（日）
      if (strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao + "")) {
        FormBean fb = instance.getFormBean();
        ElementBean eb1 = fb.getElement("RATE");
        eb1.setComponetTp(6);
        ElementBean eb2 = fb.getElement("MONTHS");
        eb2.setCaption("申请期限（日）");
        //add by sunzg  10-24
        //eb2.setComponetTp(6);
        ElementBean ebFrate=fb.getElement("FRATE") ;
        ebFrate.setComponetTp(6);
        ElementBean ebBrate=fb.getElement("BRATE") ;
        ebBrate.setComponetTp(6);
      }
      //贴现转贴现去掉申请期限（日）
      if (strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_TieXian + "") || strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ZhuanTieXian + "")) {
        FormBean fb = instance.getFormBean();
        ElementBean eb1 = fb.getElement("MONTHS");
        eb1.setComponetTp(6);
        ElementBean eb2 = fb.getElement("RATE");
        eb2.setYposition(1);
        //hide fields of Base Interest and floating interest for draft discount
          ElementBean ebFrate=fb.getElement("FRATE") ;
          ebFrate.setComponetTp(6);
          ElementBean ebBrate=fb.getElement("BRATE") ;
          ebBrate.setComponetTp(6);
          ElementBean eb5 = fb.getElement("RATE");
          eb5.setReadonly(false);
      }

      return 0;
    }
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String strCurNo = null; //货币代码
    String strAMT = null; //决策金额
    String strRate = null; //决策利率
    String strMonths = null; //决策期限(月)
    String strStartDate = null; //决策放款日
    String strEndDate = null; //决策还款日
    String strBRate = null;
    String strFRate = null;
    String strDate = SystemDate.getSystemDate5(null);
    UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);
    if (strBmActType.equals("2")) { //如果为第三级审批则获得登记的信息
      strCurNo = utda.curNo; //获得货币代码
      if (utda.appAmt == null) {
        strAMT = "";
      }
      else {
        strAMT = utda.appAmt.toString(); //获得决策金额
      }
      if (utda.rate == null) {
        strRate = "";
      }
      else {
        strRate = utda.rate.toString(); //决策利率
      }

      if (utda.bRate == null) {
        strBRate = "";
      }
      else {
        strBRate = utda.bRate.toString(); //决策利率
      }

      if (utda.fRate == null) {
        strFRate = "";
      }
      else {
        strFRate = utda.fRate.toString(); //决策利率
      }

      if (utda.appMonths == null) {
        strMonths = "";
      }
      else {
        strMonths = utda.appMonths.toString(); //决策期限(月)
      }
      strStartDate = util.calToString(utda.appStartDate); //决策放款日
//      System.out.println("bmtype:"+this.strBmType);
//      if(this.strBmType.equals("11"))
//        strStartDate="20000101";
      strEndDate = util.calToString(utda.appEndDate); //决策还款日
      strFirstResp = utda.clientMgr;
    }
    else { //如果为第二级则获得第三级的信息，如果为第一级则获得第二级的信息
      strCurNo = utda.finalCurNo; //获得货币代码
      if (utda.finalAmt == null) {
        strAMT = "";
      }
      else {
        strAMT = utda.finalAmt.toString(); //获得决策金额
      }
      if (utda.finalRate == null) {
        strRate = "";
      }
      else {
        strRate = utda.finalRate.toString(); //获得决策利率
      }
      /**
       * added by JGO on 2004/10/21
       */

      if (utda.bRate == null) {
        strBRate = "";
      }
      else {
        strBRate = utda.bRate.toString(); //决策利率
      }

      if (utda.fRate == null) {
        strFRate = "";
      }
      else {
        strFRate = utda.fRate.toString(); //决策利率
      }


      if (utda.finalMonths == null) {
        strMonths = "";
      }
      else {
        strMonths = utda.finalMonths.toString(); //获得决策期限(月)
      }
      strStartDate = util.calToString(utda.finalStartDate); //获得决策放款日
      strEndDate = util.calToString(utda.finalEndDate); //获得决策还款日
      strIfrespLoan = utda.ifRespLoan.toString(); //获得是否有第一责任人
      if (utda.firstResp == null) {
        strFirstResp = "";
      }
      else {
        strFirstResp = utda.firstResp; //获得第一责任人
      }
      if (utda.firstRespPct == null) {
        strFirstRespPct = "0";
      }
      else {
        strFirstRespPct = utda.firstRespPct.toString(); //获得第一责任人比例
      }
      instance.setValue("IFRESPLOAN", Integer.parseInt(strIfrespLoan)); //上一业务步骤的是否有第一责任人
      instance.setValue("FISRTRESPPCT", strFirstRespPct); //上一业务步骤的第一责任人比例
    }

    //System.out.println("======================="+strBRate+strFRate);
    //System.out.println("strdate"+strStartDate);

    instance.setValue("FIRSTRESP", strFirstResp); //上一业务步骤的第一责任人
    instance.setValue("CURNO", strCurNo); //上一业务步骤的货币代码
    instance.setValue("AMT", strAMT); //上一业务步骤的决策金额
    instance.setValue("RATE", strRate); //上一业务步骤的决策利率
    instance.setValue("BRATE", strBRate);
    instance.setValue("FRATE", strFRate);
    instance.setValue("MONTHS", strMonths); //上一业务步骤的决策期限(月)

    instance.setValue("STARTDATE", strStartDate); //上一业务步骤的决策放款日
    instance.setValue("ENDDATE", strEndDate); //上一业务步骤的决策还款日
    instance.setValue("CREATEDATE", strDate);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("BMNO", strBmNo);
    instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));
    //只读状态下将第一责任人显示为SCUSER.NAME
    if (instance.isReadonly()) {
      instance.setValue("FIRSTRESP", SCUser.getName(strFirstResp));
    }
    //无责任人时，第一责任人名称改为管理责任人
    String IFRESPLOAN=instance.getStringValue("IFRESPLOAN");
    if(IFRESPLOAN!=null){
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("FIRSTRESP");
      if (IFRESPLOAN.equals("1")) {
        eb.setCaption("第一责任人");
      }
      else {
        eb.setCaption("管理责任人");
      }
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager, SqlAssistor assistor) {
    //无责任人时，第一责任人名称改为管理责任人
    String IFRESPLOAN=DBUtil.getCellValue(conn,"BMTABLEAPP","IFRESPLOAN","BMNO='" + strBmNo + "'");
    if(IFRESPLOAN!=null){
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("FIRSTRESP");
      if (IFRESPLOAN.equals("1")) {
        eb.setCaption("第一责任人");
      }
      else {
        eb.setCaption("管理责任人");
      }
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
//
//    String appmonths,chgtype,frate;
//    chgtype = ctx.getParameter("CHGTYPE");
//    appmonths = ctx.getParameter("MONTHS");
//    frate = ctx.getParameter("FRATE");
//    //System.out.println("---------------------"+chgtype+appmonths+frate);
//
//    System.out.println("bmtype:"+this.strBmType);
//    if(chgtype!=null)
//    {
//    if(chgtype.compareToIgnoreCase("RATECHG") == 0)
//    {
//      if(appmonths != null && frate != null && appmonths.length() > 0 && frate.length() > 0)
//      {
//        int appMonthInt;
//        BigDecimal fratef = null;
//        try {
//          appMonthInt = Integer.parseInt(appmonths);
//          fratef = new BigDecimal(frate);
//          fratef = fratef.divide(new BigDecimal(100), 4 ,BigDecimal.ROUND_CEILING);
//        }
//        catch (Exception e) {
//          Debug.debug(e);
//          msgs.add("申请期限或浮动利率数据格式错误！");
//          return -1;
//        }
//        int  i= BMTable.getBMTable(ctx.getParameter("BMNO")).TypeNo;
//        if(i == zt.cmsi.pub.cenum.EnumValue.BMType_ZhanQi)
//        {
//          String OrigBMNo = null;
//          UpToDateApp bmtblappdata = BMTable.getUpToDateApp(strBmNo);
//          OrigBMNo = bmtblappdata.origBMNo;
//          if (OrigBMNo == null || OrigBMNo.length() < 1) {
//            msgs.add("系统错误，展期的原业务号不存在！");
//            return -1;
//          }
//          LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
//          if(RQLoanLedger == null)
//          {
//            msgs.add("没有找到贷款台帐信息！");
//            return -1;
//          }
//          if(RQLoanLedger.perimon != null)
//          {
//            appMonthInt += RQLoanLedger.perimon.intValue();
//          }
//          //System.out.println("-----------------month="+appMonthInt);
//        }
//
//        BMBaseInst baseint = BMBaseInst.getBaseInst  (i, appMonthInt);
//        if (baseint == null) {
//          msgs.add("未取得基准利率设置，请检查系统的基准利率设置！");
//          return -1;
//        }
//
//        //System.out.println("baseint ===================" + baseint.interest);
//
//        instance.setValue("BRATE", "" + baseint.interest);
//        BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
//            BigDecimal(1)));
//      //        新加下一句，精确度 modify by sunzg   10-25
//        //finalrate=finalrate.divide(new BigDecimal(100), 4 ,BigDecimal.ROUND_CEILING) ;
//        instance.setValue("RATE", "" + finalrate);
//        msgs.add("已经获得基准利率：成功.请继续输入.");
//        return -1;
//      }
//      else
//      {
//        msgs.add("请继续输入...");
//        return -1;
//      }
//    }
//  }

//

    String strIsFirst = ctx.getParameter("IFRESPLOAN").trim();
    if (strIsFirst.equals("1")) {
      String strClientMgr = ctx.getParameter("FIRSTRESP").trim();
      String strWhere = " loginname='" + strClientMgr + "' and usertype<>'3'";
      String isnull = DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME", strWhere);
      if (isnull == null) {
        msgs.add("第一责任人不存在，请检查输入的第一责任人是否准确！");
        return -1;
      }
    }

    strOthers = ctx.getParameter("finish"); //获得完成审批标志
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String strIsFirst = ctx.getParameter("IFRESPLOAN").trim();
    if (strIsFirst.equals("1")) {
      String strClientMgr = ctx.getParameter("FIRSTRESP").trim();
      String strWhere = " loginname='" + strClientMgr + "' and usertype<>'3'";
      String isnull = DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME", strWhere);
      if (isnull == null) {
        msgs.add("第一责任人不存在，请检查输入的第一责任人是否准确！");
        return -1;
      }
    }

    strOthers = ctx.getParameter("finish"); //获得完成审批标志
    return 0;

  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    try {
      int iflag = 0;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo),
                                    strUserName); //完成审批
      }
      if (iflag >= 0) {

        String msg = "信息保存成功！";
        BMTableData bd = BMTable.getBMTable(strBmNo);
        if(bd != null)
        {
          EnumerationBean eb = EnumerationType.getEnu("BMStatus");
          if(eb != null)
          {
            msg += "提交到"+(String)eb.getValue(new Integer(bd.bmStatus));
          }
        }

        ctx.setRequestAtrribute("title", "贷款业务审批");
        ctx.setRequestAtrribute("msg", msg);
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");

      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    try {
      int iflag = 0;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo),
                                    strUserName); //完成审批
      }
      if (iflag >= 0) {

        String msg = "信息保存成功！";
        BMTableData bd = BMTable.getBMTable(strBmNo);
        if(bd != null)
        {
          EnumerationBean eb = EnumerationType.getEnu("BMStatus");
          if(eb != null)
          {
            msg += "提交到"+(String)eb.getValue(new Integer(bd.bmStatus));
          }
        }

        ctx.setRequestAtrribute("title", "贷款业务审批");
        ctx.setRequestAtrribute("msg", msg);
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");

      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    params = new Param();
    params.addParam(ParamName.Flag, strFlag);
    params.addParam(ParamName.BMNo, strBmNo);
    params.addParam(ParamName.BMTransNo, strBmTransNo);
    params.addParam(ParamName.BMActType, strBmActType);
    params.addParam(ParamName.BMType, strBmType);
    ctx.setRequestAtrribute(ParamName.ParamName, params);
    if (button.equals("BmComment")) {
      trigger(manager, "BMCOMMENTSLIST", null);
      return 0;
    }
    if (button.equals("BmTrans")) {
      trigger(manager, "BMTRANSLIST", null);
      return 0;
    }
    if (button.equals("CREDIT")) {
      String str = "select cmclient.id from bmtable,cmclient where bmtable.clientno=cmclient.clientno and bmtable.bmno='" + params.getBmNo() + "'";
      RecordSet rs = conn.executeQuery(str);
      if (rs.next()) {
        ctx.setRequestAtrribute("ID", rs.getString("ID"));
        trigger(manager, "GUARANTORCREDITLIST", null);
        return 0;
      }
      else {
        msgs.add("找不到相应的用户信息");
        return -1;
      }
    }
    if (button.equals("RiskCheck")) {
      params.addParam("if3rddec", "yes");
      //ctx.setRequestAtrribute("BMPARAM", params);
      try{
        //check the result
        Vector results = AppCriteria.getAppCriteriaData(strBmNo);
        if (results != null) {
          ctx.setRequestAtrribute("results", results);
          ctx.setRequestAtrribute("BMPARAM", params);
          ctx.setTarget("/workbench/check.jsp");
        }
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      trigger(manager, "BMTRANSLIST", null);
      return 0;
    }
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    //mod by wxj at 400209 getSubBranchAll-->getAllSubBrhAndSelf1
    String strSubScbrh = SCBranch.getAllSubBrhAndSelf1(strScbrh);
    if (strSubScbrh == null || strSubScbrh.length() < 1) {
      msgs.add("下属网点不存在！");
      return -1;
    }
    else {
      strSubScbrh = "'" + strSubScbrh.replaceAll(",", "','") + "'";
    }
    if (reffldnm.equals("DECIDEDBY")) {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", "'" + strScbrh + "'",
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }
    if (reffldnm.equals("FIRSTRESP")) {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", strSubScbrh,
                                 SqlWhereUtil.DataType_Sql,
                                 sqlWhereUtil.OperatorType_In,
                                 sqlWhereUtil.RelationOperator_And);
    }

    return 0;
  }

  public void postField(SessionContext ctx, FormInstance instance, String fieldname,
          ErrorMessages msgs, EventManager manager)
  {

      String appmonths,chgtype,frate;
      appmonths = (String)ctx.getRequestAttribute("MONTHS");
      frate = (String)ctx.getRequestAttribute("FRATE");
      //System.out.println("---------------------"+chgtype+appmonths+frate);

      System.out.println("bmtype:"+this.strBmType);
//    if(this.strBmType.equals("11"))
//    instance.setValue("ENDDATE","30000101");
//    instance.setValue("STARTDATE","25000101");
//    新加条件判断by sunzg 10-20
      if(appmonths != null && frate != null && appmonths.length() > 0 && frate.length() > 0)
      {
        int appMonthInt;
        BigDecimal fratef = null;
        try {
          appMonthInt = Integer.parseInt(appmonths);
          fratef = new BigDecimal(frate);
          fratef = fratef.divide(new BigDecimal(100), 4 ,BigDecimal.ROUND_CEILING);
        }
        catch (Exception e) {
          Debug.debug(e);
          instance.setHTMLFieldValue("BRATE","");
          instance.setHTMLFieldValue("RATE","");
          instance.setHTMLShowMessage("INFO","申请期限或浮动利率数据格式错误！");
          return;
        }

        int  i= BMTable.getBMTable((String)ctx.getRequestAttribute("BMNO")).TypeNo;
        if(i == zt.cmsi.pub.cenum.EnumValue.BMType_ZhanQi)
        {
          String OrigBMNo = null;
          UpToDateApp bmtblappdata = BMTable.getUpToDateApp(strBmNo);
          OrigBMNo = bmtblappdata.origBMNo;
          if (OrigBMNo == null || OrigBMNo.length() < 1) {
            instance.setHTMLFieldValue("BRATE","");
            instance.setHTMLFieldValue("RATE","");
            instance.setHTMLShowMessage("INFO","系统错误，展期的原业务号不存在！");
            return;
          }
          LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
          if(RQLoanLedger == null)
          {
            instance.setHTMLFieldValue("BRATE","");
            instance.setHTMLFieldValue("RATE","");
            instance.setHTMLShowMessage("INFO","没有找到贷款台帐信息！");
            return;
          }
          if(RQLoanLedger.perimon != null)
          {
            appMonthInt += RQLoanLedger.perimon.intValue();
          }
          //System.out.println("-----------------month="+appMonthInt);
        }

        BMBaseInst baseint = BMBaseInst.getBaseInst  (i, appMonthInt);
        if (baseint == null) {
          instance.setHTMLFieldValue("BRATE","");
          instance.setHTMLFieldValue("RATE","");
          instance.setHTMLShowMessage("INFO","未取得基准利率设置，请检查系统的基准利率设置！");
          return;
        }

        //System.out.println("baseint ===================" + baseint.interest);

        instance.setHTMLFieldValue("BRATE","" + baseint.interest);
        BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
            BigDecimal(1)));
        //System.out.println("old-------------------------"+"" + finalrate+"--"+finalrate.divide(new BigDecimal(1), 4 ,BigDecimal.ROUND_HALF_DOWN)+"---"+finalrate.divide(new BigDecimal(1), 4 ,BigDecimal.ROUND_HALF_UP));
        finalrate=finalrate.divide(new BigDecimal(1), 4 ,BigDecimal.ROUND_HALF_EVEN) ;
        instance.setHTMLFieldValue("RATE","" + finalrate);
        return;
      }
      else
      {
        return;
      }
  }


}
