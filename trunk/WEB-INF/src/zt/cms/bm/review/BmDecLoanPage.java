package zt.cms.bm.review;
/**
 * <p>Title: ����ҵ������(��һ�����ڶ���������������)</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
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
  private String strFlag = null; //��д��־
  public Param params = null; //���͵ı�������
  private Param paramg = null; //��õı�������
  private String strBmNo = null; //ҵ���
  private String strBmTransNo = null; //ҵ����ϸ��
  private String strUserName = null; //��ǰ��½�û���
  private String strOthers = "0"; //�Ƿ����������־
  private String strBmActType = null; //��������
  private String strBmType = null; //��������
  private String strScbrh = null; //��������
  private String strIfrespLoan = null; //�Ƿ���������
  private String strFirstResp = null; //��һ������
  private String strFirstRespPct = null; //��һ�����˱���

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("���ݲ������޷���������������");
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
      msgs.add("���ݲ������޷���������������");
      return -1;
    }
    else {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
      try {
        strUserName = um.getUserName();
        strScbrh = SCUser.getBrhId(strUserName);
        if (strScbrh == null || strScbrh.length() < 1) {
          msgs.add("�������㲻���ڣ�");
          return -1;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      //���bmdecision�����������༭ģʽ
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
      //�жһ�Ʊȥ���������ʣ��������ޣ��£���Ϊ�������ޣ��գ�
      if (strBmType.trim().equals(zt.cmsi.pub.cenum.EnumValue.BMType_ChengDuiHuiPiao + "")) {
        FormBean fb = instance.getFormBean();
        ElementBean eb1 = fb.getElement("RATE");
        eb1.setComponetTp(6);
        ElementBean eb2 = fb.getElement("MONTHS");
        eb2.setCaption("�������ޣ��գ�");
        //add by sunzg  10-24
        //eb2.setComponetTp(6);
        ElementBean ebFrate=fb.getElement("FRATE") ;
        ebFrate.setComponetTp(6);
        ElementBean ebBrate=fb.getElement("BRATE") ;
        ebBrate.setComponetTp(6);
      }
      //����ת����ȥ���������ޣ��գ�
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
    String strCurNo = null; //���Ҵ���
    String strAMT = null; //���߽��
    String strRate = null; //��������
    String strMonths = null; //��������(��)
    String strStartDate = null; //���߷ſ���
    String strEndDate = null; //���߻�����
    String strBRate = null;
    String strFRate = null;
    String strDate = SystemDate.getSystemDate5(null);
    UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);
    if (strBmActType.equals("2")) { //���Ϊ�������������õǼǵ���Ϣ
      strCurNo = utda.curNo; //��û��Ҵ���
      if (utda.appAmt == null) {
        strAMT = "";
      }
      else {
        strAMT = utda.appAmt.toString(); //��þ��߽��
      }
      if (utda.rate == null) {
        strRate = "";
      }
      else {
        strRate = utda.rate.toString(); //��������
      }

      if (utda.bRate == null) {
        strBRate = "";
      }
      else {
        strBRate = utda.bRate.toString(); //��������
      }

      if (utda.fRate == null) {
        strFRate = "";
      }
      else {
        strFRate = utda.fRate.toString(); //��������
      }

      if (utda.appMonths == null) {
        strMonths = "";
      }
      else {
        strMonths = utda.appMonths.toString(); //��������(��)
      }
      strStartDate = util.calToString(utda.appStartDate); //���߷ſ���
//      System.out.println("bmtype:"+this.strBmType);
//      if(this.strBmType.equals("11"))
//        strStartDate="20000101";
      strEndDate = util.calToString(utda.appEndDate); //���߻�����
      strFirstResp = utda.clientMgr;
    }
    else { //���Ϊ�ڶ������õ���������Ϣ�����Ϊ��һ�����õڶ�������Ϣ
      strCurNo = utda.finalCurNo; //��û��Ҵ���
      if (utda.finalAmt == null) {
        strAMT = "";
      }
      else {
        strAMT = utda.finalAmt.toString(); //��þ��߽��
      }
      if (utda.finalRate == null) {
        strRate = "";
      }
      else {
        strRate = utda.finalRate.toString(); //��þ�������
      }
      /**
       * added by JGO on 2004/10/21
       */

      if (utda.bRate == null) {
        strBRate = "";
      }
      else {
        strBRate = utda.bRate.toString(); //��������
      }

      if (utda.fRate == null) {
        strFRate = "";
      }
      else {
        strFRate = utda.fRate.toString(); //��������
      }


      if (utda.finalMonths == null) {
        strMonths = "";
      }
      else {
        strMonths = utda.finalMonths.toString(); //��þ�������(��)
      }
      strStartDate = util.calToString(utda.finalStartDate); //��þ��߷ſ���
      strEndDate = util.calToString(utda.finalEndDate); //��þ��߻�����
      strIfrespLoan = utda.ifRespLoan.toString(); //����Ƿ��е�һ������
      if (utda.firstResp == null) {
        strFirstResp = "";
      }
      else {
        strFirstResp = utda.firstResp; //��õ�һ������
      }
      if (utda.firstRespPct == null) {
        strFirstRespPct = "0";
      }
      else {
        strFirstRespPct = utda.firstRespPct.toString(); //��õ�һ�����˱���
      }
      instance.setValue("IFRESPLOAN", Integer.parseInt(strIfrespLoan)); //��һҵ������Ƿ��е�һ������
      instance.setValue("FISRTRESPPCT", strFirstRespPct); //��һҵ����ĵ�һ�����˱���
    }

    //System.out.println("======================="+strBRate+strFRate);
    //System.out.println("strdate"+strStartDate);

    instance.setValue("FIRSTRESP", strFirstResp); //��һҵ����ĵ�һ������
    instance.setValue("CURNO", strCurNo); //��һҵ����Ļ��Ҵ���
    instance.setValue("AMT", strAMT); //��һҵ����ľ��߽��
    instance.setValue("RATE", strRate); //��һҵ����ľ�������
    instance.setValue("BRATE", strBRate);
    instance.setValue("FRATE", strFRate);
    instance.setValue("MONTHS", strMonths); //��һҵ����ľ�������(��)

    instance.setValue("STARTDATE", strStartDate); //��һҵ����ľ��߷ſ���
    instance.setValue("ENDDATE", strEndDate); //��һҵ����ľ��߻�����
    instance.setValue("CREATEDATE", strDate);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("BMNO", strBmNo);
    instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));
    //ֻ��״̬�½���һ��������ʾΪSCUSER.NAME
    if (instance.isReadonly()) {
      instance.setValue("FIRSTRESP", SCUser.getName(strFirstResp));
    }
    //��������ʱ����һ���������Ƹ�Ϊ����������
    String IFRESPLOAN=instance.getStringValue("IFRESPLOAN");
    if(IFRESPLOAN!=null){
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("FIRSTRESP");
      if (IFRESPLOAN.equals("1")) {
        eb.setCaption("��һ������");
      }
      else {
        eb.setCaption("����������");
      }
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
          EventManager manager, SqlAssistor assistor) {
    //��������ʱ����һ���������Ƹ�Ϊ����������
    String IFRESPLOAN=DBUtil.getCellValue(conn,"BMTABLEAPP","IFRESPLOAN","BMNO='" + strBmNo + "'");
    if(IFRESPLOAN!=null){
      FormBean fb = instance.getFormBean();
      ElementBean eb = fb.getElement("FIRSTRESP");
      if (IFRESPLOAN.equals("1")) {
        eb.setCaption("��һ������");
      }
      else {
        eb.setCaption("����������");
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
//          msgs.add("�������޻򸡶��������ݸ�ʽ����");
//          return -1;
//        }
//        int  i= BMTable.getBMTable(ctx.getParameter("BMNO")).TypeNo;
//        if(i == zt.cmsi.pub.cenum.EnumValue.BMType_ZhanQi)
//        {
//          String OrigBMNo = null;
//          UpToDateApp bmtblappdata = BMTable.getUpToDateApp(strBmNo);
//          OrigBMNo = bmtblappdata.origBMNo;
//          if (OrigBMNo == null || OrigBMNo.length() < 1) {
//            msgs.add("ϵͳ����չ�ڵ�ԭҵ��Ų����ڣ�");
//            return -1;
//          }
//          LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
//          if(RQLoanLedger == null)
//          {
//            msgs.add("û���ҵ�����̨����Ϣ��");
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
//          msgs.add("δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
//          return -1;
//        }
//
//        //System.out.println("baseint ===================" + baseint.interest);
//
//        instance.setValue("BRATE", "" + baseint.interest);
//        BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
//            BigDecimal(1)));
//      //        �¼���һ�䣬��ȷ�� modify by sunzg   10-25
//        //finalrate=finalrate.divide(new BigDecimal(100), 4 ,BigDecimal.ROUND_CEILING) ;
//        instance.setValue("RATE", "" + finalrate);
//        msgs.add("�Ѿ���û�׼���ʣ��ɹ�.���������.");
//        return -1;
//      }
//      else
//      {
//        msgs.add("���������...");
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
        msgs.add("��һ�����˲����ڣ���������ĵ�һ�������Ƿ�׼ȷ��");
        return -1;
      }
    }

    strOthers = ctx.getParameter("finish"); //������������־
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
        msgs.add("��һ�����˲����ڣ���������ĵ�һ�������Ƿ�׼ȷ��");
        return -1;
      }
    }

    strOthers = ctx.getParameter("finish"); //������������־
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
                                    strUserName); //�������
      }
      if (iflag >= 0) {

        String msg = "��Ϣ����ɹ���";
        BMTableData bd = BMTable.getBMTable(strBmNo);
        if(bd != null)
        {
          EnumerationBean eb = EnumerationType.getEnu("BMStatus");
          if(eb != null)
          {
            msg += "�ύ��"+(String)eb.getValue(new Integer(bd.bmStatus));
          }
        }

        ctx.setRequestAtrribute("title", "����ҵ������");
        ctx.setRequestAtrribute("msg", msg);
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");

      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
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
                                    strUserName); //�������
      }
      if (iflag >= 0) {

        String msg = "��Ϣ����ɹ���";
        BMTableData bd = BMTable.getBMTable(strBmNo);
        if(bd != null)
        {
          EnumerationBean eb = EnumerationType.getEnu("BMStatus");
          if(eb != null)
          {
            msg += "�ύ��"+(String)eb.getValue(new Integer(bd.bmStatus));
          }
        }

        ctx.setRequestAtrribute("title", "����ҵ������");
        ctx.setRequestAtrribute("msg", msg);
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");

      }
      else {
        return iflag;
      }
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
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
        msgs.add("�Ҳ�����Ӧ���û���Ϣ");
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
      msgs.add("�������㲻���ڣ�");
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
//    �¼������ж�by sunzg 10-20
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
          instance.setHTMLShowMessage("INFO","�������޻򸡶��������ݸ�ʽ����");
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
            instance.setHTMLShowMessage("INFO","ϵͳ����չ�ڵ�ԭҵ��Ų����ڣ�");
            return;
          }
          LoanLedger RQLoanLedger = LoanLedgerMan.getLoanLedger(OrigBMNo);
          if(RQLoanLedger == null)
          {
            instance.setHTMLFieldValue("BRATE","");
            instance.setHTMLFieldValue("RATE","");
            instance.setHTMLShowMessage("INFO","û���ҵ�����̨����Ϣ��");
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
          instance.setHTMLShowMessage("INFO","δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
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
