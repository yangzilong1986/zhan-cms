package zt.cms.bm.app;

import java.math.*;
import java.util.*;

import zt.cms.bm.common.*;
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
import zt.platform.utils.Debug;
import zt.cmsi.pub.confitem;

public class NewCreditAction extends FormActions {
  private boolean firstRun = true;
  String flag = "";
  private int currBMType = -1;
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    String aFlag = ctx.getParameter("flag");
    if (aFlag == null || aFlag.equals("")) {
      this.flag = "read";
    }
    else {
      this.flag = aFlag;
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    FormElementValue element = instance.getValue("BMTYPE");
    element.clear();
    List l = new ArrayList();
    for (int i = 1; i <= 30; i++) {
      Integer s = BMType.getInstance().getStartAct(i);
      if (s == null) {
        continue;
      }
      else {
        int startActType = s.intValue();
        if (BMRoute.getInstance().hasRightToAct(SessionInfo.getLoginUserNo(ctx), i, startActType) >= 0) {
          l.add(i + "");
        }
      }
    }
    l.remove("17");
    element.setValueFilter(l);
    //去掉申请开始日期和申请结束日期
    //Added by wxj at 040324
    String APPBEGINDATE = "30000101";
    String APPENDDATE = "30010101";
    instance.setValue("APPBEGINDATE", APPBEGINDATE);
    instance.setValue("APPENDDATE", APPENDDATE);
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    Param param = null;
    int startActType = 0;

    try
    {
      MyDB.getInstance().addDBConn(conn);

      param = ParamFactory.getParamByCtx(ctx);
      param.addParam(ParamName.BrhID, ctx.getParameter("APPBRHID"));
      startActType = BMType.getInstance().getStartAct(param.getBmTypeNo()).
          intValue();
      param.addParam(ParamName.BMActType, new Integer(startActType));
      ctx.setRequestAtrribute("BMPARAM", param);
      //业务权限检查
      if (BMRoute.getInstance().hasRightToAct(SessionInfo.getLoginUserNo(ctx),
                                              param.getBmTypeNo(), startActType) <
          0) {
        msgs.add("对不起，您没有该业务的权限！");
        return -1;
      }
      //展期，必须输入原账号和借据号
      if (param.getBmTypeNo() == zt.cmsi.pub.cenum.EnumValue.BMType_ZhanQi) {
        String origAccNo = (String) param.getParam(ParamName.OrigAccNo);
        String origDueBillNo = (String) param.getParam(ParamName.OrigDueBillNo);
        LoanLedger ledger = LoanLedgerMan.getLoanLedger(origAccNo,
            origDueBillNo);
        if (ledger == null) {
          msgs.add("原业务找不到，请检查原账号和借据号");
          trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
          return 0;
        }
        else {
          BMTableData bd = BMTable.getBMTable(ledger.BMNo);
          if (bd == null) {
            msgs.add("原业务数据纪录找不到，请检查原账号和借据号");
            trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            return 0;
          }
          else {
//          System.out.println("["+bd.clientNo+"]");
//          System.out.println("["+(String)param.getParam(ParamName.CLientNo)+"]");
            if (bd.clientNo.trim().compareToIgnoreCase( ( (String) param.
                getParam(ParamName.CLientNo)).trim()) != 0) {
              msgs.add("账号和借据号对应的贷款不是输入的客户的贷款!");
              trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
              return 0;
            }
            else
            {
                if(util.daysBetweenCals(ledger.nowEndDate, SystemDate.getSystemDate1()) < 0)
                {
                  msgs.add("贷款已经过期,不能再展期!");
                  trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
                  return 0;
                }
            }
          }
          param.addParam(ParamName.OrigBMNo, ledger.BMNo);
        }
      }
    }
    catch(Exception e)
    {
      msgs.add("发生异常："+e.getMessage());
      Debug.debug(e);
      return -1;
    }
    finally
    {
        MyDB.getInstance().releaseDBConn();
    }

    //获取风险报告
    Vector results = AppCriteria.checkAppCriteria(param);

    //农户贷款要检查有没有家庭成员
    if ((param.getBmTypeNo() >= zt.cmsi.pub.cenum.EnumValue.BMType_DaiKuanZhengDaiKuan && param.getBmTypeNo() <= zt.cmsi.pub.cenum.EnumValue.BMType_GeRenShangFangDaiKuan) || param.getBmTypeNo() == zt.cmsi.pub.cenum.EnumValue.BMType_GeTiGongShangHuDaiKuan || param.getBmTypeNo() == zt.cmsi.pub.cenum.EnumValue.BMType_GeRenQiTaDaiKuan || param.getBmTypeNo() == zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin) {
      String cno=(String) param.getParam(ParamName.CLientNo);
      String sql="select count(*) from cmindvrela where clientno='"+cno+"'";
      RecordSet rs=conn.executeQuery(sql);
      int count=0;
      if(rs.next()){
        count=rs.getInt(0);
      }
      rs.close();
      if(count<1){
        if(results==null) results=new Vector();
        CriCheckResult ccr = new CriCheckResult();
        ccr.alertType = zt.cmsi.pub.cenum.EnumValue.AlertType_LanJie;
        ccr.message = "客户的家庭成员不存在！";
        results.add(ccr);
      }
    }
    //贷款证授信业务，必须检查联保小组和以前的贷款证贷款以及客户是否有照片
    if (param.getBmTypeNo() == zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin) {
      //联保小组存在检查
      String cno=(String) param.getParam(ParamName.CLientNo);
      String sql1="select b.UNIONNO from cmindvclient a,cmfamilyunion b where a.clientno='"+cno+"' and a.UNIONNO=b.UNIONNO";
      String uno=null;
      RecordSet rs1=conn.executeQuery(sql1);
      if(rs1.next()){
        uno=rs1.getString("unionno");
      }
      rs1.close();
      if(uno==null || uno.trim().length()<1){
        if(results==null) results=new Vector();
        CriCheckResult ccr = new CriCheckResult();
        ccr.alertType = zt.cmsi.pub.cenum.EnumValue.AlertType_JingGao;
        ccr.message = "客户的联保小组不存在！";
        results.add(ccr);
      }
      //尚未还清的贷款证贷款
      String sql2="select count(*) from bmtable where clientno='"+cno+"' and bmstatus<10 and typeno="+zt.cmsi.pub.cenum.EnumValue.BMType_DaiKuanZhengDaiKuan+"";
      int cnt=0;
      RecordSet rs2=conn.executeQuery(sql2);
      if(rs2.next()){
        cnt=rs2.getInt(0);
      }
      rs2.close();
      if(cnt>0){
        if(results==null) results=new Vector();
        CriCheckResult ccr = new CriCheckResult();
        ccr.alertType = zt.cmsi.pub.cenum.EnumValue.AlertType_JingGao;
        ccr.message = "客户有尚未还清或正在办理的贷款证贷款！";
        results.add(ccr);
      }
      //客户照片
      String sql3="select a.photono from cmindvclient a,glblob b where a.clientno='"+cno+"' and a.photono=b.objid";
      RecordSet rs3=conn.executeQuery(sql3);
      int photono=0;
      if(rs3.next()){
        photono=rs3.getInt(0);
      }
      rs3.close();
      if(photono<1){
        if(results==null) results=new Vector();
        CriCheckResult ccr = new CriCheckResult();
        ccr.alertType = zt.cmsi.pub.cenum.EnumValue.AlertType_LanJie;
        ccr.message = "客户没有登记照片！";
        results.add(ccr);
      }
    }
    //风险报告存在
    if (results != null) {
      ctx.setRequestAtrribute("results", results);
      ctx.setRequestAtrribute("BMPARAM", param);
      ctx.setTarget("/workbench/check.jsp");
      return -1;
    }
    //风险报告不存在
    try
    {
      MyDB.getInstance().addDBConn(conn);

      String bmNo = BMTable.createBMTable(param.getBmTypeNo(),
                                          (String) param.
                                          getParam(ParamName.CLientNo),
                                          (String)
                                          param.getParam(ParamName.BrhID),
                                          SessionInfo.getLoginBrhId(ctx),
                                          SessionInfo.getLoginUserName(ctx));
      if (bmNo == null) {
        msgs.add("建立业务主表失败！");
        conn.rollback();
        return -1;
      }

      param.addParam(ParamName.BMNo, bmNo);
      param.addParam("flag", "write");
      UpToDateApp data = new UpToDateApp();
      data.appAmt = new BigDecimal( (String) param.getParam(ParamName.AppAmt));
      data.bmTypeNo = new Integer(param.getBmTypeNo());
      data.origDueBillNo = (String) param.getParam(ParamName.OrigDueBillNo);
      data.origAccNo = (String) param.getParam(ParamName.OrigAccNo);
      data.clientNo = (String) param.getParam(ParamName.CLientNo);
      data.setAppEndDate( (String) param.getParam(ParamName.AppEndDate));
      data.setAppStartDate( (String) param.getParam(ParamName.AppBeginDate));
      data.finalAmt = data.appAmt;
      data.origBMNo = (String) param.getParam(ParamName.OrigBMNo);
      int ret = BMTable.updateUpToDateApp(bmNo, data);

      if (ret < 0) {
        conn.rollback();
        return ret;
      }


      int transNo = BMTrans.createBMTrans(bmNo,
                                          ( (Integer) param.getParam(ParamName.
          BMActType)).intValue(), SessionInfo.getLoginBrhId(ctx),
                                          SessionInfo.getLoginUserName(ctx));
      if (transNo < 0) {
        msgs.add("建立业务流程失败！");
        conn.rollback();
        return transNo;
      }

      param.addParam(ParamName.BMTransNo, new Integer(transNo));


      conn.commit();
    }
    catch(Exception e)
    {
      msgs.add("发生异常："+e.getMessage());
      conn.rollback();
      Debug.debug(e);
      return -1;
    }
    finally
    {
        MyDB.getInstance().releaseDBConn();
    }


    BMProg prog = BMRoute.getInstance().getActProg(param.getBmTypeNo(), startActType, null);
    if (prog.isForm()) {
      trigger(manager, prog.getProgName(), null);
    }
    else {
      msgs.add("系统错误，错误的系统配置！");
      return -1;
    }

    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (reffldnm.equals("APPBRHID") || reffldnm.equals("CLIENTMGR") ||
        reffldnm.equals("UNIONNO") || reffldnm.equals("CLIENTNO")) {
      //BRHID（用户网点）
      String BRHID = SCUser.getBrhId(um.getUserName());
      System.out.println("BRHID:" + BRHID);
      if (BRHID == null || BRHID.length() < 1) {
        return -1;
      }
      //APPBRHIDs（用户网点下的所有实网点，包括自己）
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("下属网点不存在！");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      //sqlWhereUtil
      if (reffldnm.equals("APPBRHID")) {
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      if (reffldnm.equals("CLIENTMGR")) {
        sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In,
                                   sqlWhereUtil.RelationOperator_And);
        sqlWhereUtil.addWhereField("SCUSER", "USERTYPE", "3",
//                                   SqlWhereUtil.DataType_Number,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Not_Equals);
      }
      if (reffldnm.equals("UNIONNO")) {
        sqlWhereUtil.addWhereField("CMFAMILYUNION", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }

      if (reffldnm.trim().equals("CLIENTNO")) {

        if (firstRun == true) {
          firstRun = false;
          return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
        }

        try
        {
          String bmtype = ctx.getParameter("BMTYPE");
          //System.out.println("bmtype is !!!!!!!!!!!!!!!!!!!!!!!!!!"+bmtype);
          int iBMType;

          if(bmtype == null)
            {
              if(this.currBMType != -1)
                iBMType = this.currBMType;
                else
                  iBMType = zt.cmsi.pub.cenum.EnumValue.BMType_DaiKuanZhengDaiKuan;
            }
            else
            {
              iBMType = Integer.parseInt(bmtype);
              this.currBMType = iBMType;
            }

          if (iBMType != zt.cmsi.pub.cenum.EnumValue.BMType_SheTuanDaiKuan &&
              iBMType != zt.cmsi.pub.cenum.EnumValue.BMType_ZhanQi) {
            sqlWhereUtil.addWhereField("CMCLIENT", "APPBRHID", SUBBRHIDs,
                                       SqlWhereUtil.DataType_Sql,
                                       sqlWhereUtil.OperatorType_In);
          }
        }
        catch(Exception e)
        {
          Debug.debug(e);
          msgs.add("未取到业务类型！");
          return -1;
        }
      }
    }
    return 0;
  }
}
