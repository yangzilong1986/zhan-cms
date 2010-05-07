package zt.cms.bm.app;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.logging.*;

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
import zt.cmsi.client.*;
import zt.cmsi.pub.define.BMBaseInst;
import java.lang.Integer;
import java.math.BigDecimal;
import zt.platform.utils.Debug;
import java.lang.*;


public class BMAppCommon extends FormActions {
  private static Logger logger = Logger.getLogger("zt.cms.bm.app.BMAppCommon");
  private String flag = null; //窗体是否可读
  private String BMNo = null; //业务号
  private String BMTransNo = null; //业务号
  private String ClientNo = null; //客户编号
  private String ClientName = null; //客户名称
  private String BrhID = null; //业务网点
  private String OPERATOR = null; //业务员
  private String TYPENO=null;//业务类型
  private Param param = null; //参数封装对象，用于传递
  private int creditClass = 0;

  private boolean isJustWeihu = false;
  /**
   * 扩展基类的load方法
   */
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //BMPARAM
    param = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (param == null) {
      param = (Param) ctx.getAttribute(ParamName.ParamName);
    }
    else {
      ctx.setAttribute(ParamName.ParamName, null);
    }
    if (param == null) {
      msgs.add("参数错误，参数对象不存在！");
      return -1;
    }
    //Flag
    flag = (String) param.getParam(ParamName.Flag);
    if (flag == null) {
      flag = "read";
    }
    flag = flag.toLowerCase();
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //BMNo
    BMNo = (String) param.getParam(ParamName.BMNo);
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("参数错误，业务号不存在！");
      return -1;
    }
    //BMTransNo
    Object tmp = param.getParam(ParamName.BMTransNo);
    BMTransNo = (tmp == null) ? null :
      param.getParam(ParamName.BMTransNo).toString();

    if(ctx.getRequestAttribute("isJustWeihu")==null){
        if (flag.equals("write") && (BMTransNo == null || BMTransNo.length() < 1)) {
            msgs.add("参数错误，业务明细号不存在！");
            return -1;
        }
    }else{
        this.isJustWeihu = true;
    }
    //ClientName
    BMTableData bmtbldata = BMTable.getBMTable(BMNo);
    ClientNo=bmtbldata.clientNo;
    ClientName = bmtbldata.clientName;
    if (ClientName == null || ClientName.length() < 1) {
      msgs.add("系统错误，客户名称不存在！");
      return -1;
    }
    //creditclass
    if (instance.getFormBean().getElement("CREDITCLASS") != null) {
      ElementBean creditClass = instance.getFormBean().getElement("CREDITCLASS");
      creditClass.setEnutpname("CreditClass");
      RecordSet clients = conn.executeQuery("select creditclass from cmindvclient where clientno='" + ClientNo + "'");
      if (clients.next()) {
        //instance.setValue("CREDITCLASS", clients.getInt("CREDITCLASS"));
        this.creditClass = clients.getInt("CREDITCLASS");
      }
      else {
        clients = conn.executeQuery("select creditclass from cmcorpclient where clientno='" + ClientNo + "'");
        if (clients.next()) {
          //instance.setValue("CREDITCLASS", clients.getInt("CREDITCLASS"));
          this.creditClass = clients.getInt("CREDITCLASS");
        }else{
          msgs.add("得不到客户信用等级信息！");
          return -1;
        }
      }
    }



    //BMSTATUS
    if (flag.equals("write") && bmtbldata.bmStatus == zt.cmsi.pub.cenum.EnumValue.BMStatus_QuXiao) {
      msgs.add("对不起，该笔业务已经被取消，请手工刷新工作台！");
      return -1;
    }
    //BrhID
    BrhID = (String) param.getParam(ParamName.BrhID);
    if (BrhID == null || BrhID.length() < 1) {
      //msgs.add("参数错误，业务网点不存在！");
      //return -1;
      BrhID = "";
    }
    //OPERATOR
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    OPERATOR = um.getUserName();
    if (OPERATOR == null || OPERATOR.length() < 1) {
      msgs.add("系统错误，获取业务操作员信息失败！");
      return -1;
    }
    //几种业务的特殊化控制
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    TYPENO=DBUtil.getCellValue(conn, "BMTABLE", "TYPENO", "BMNO='" + BMNo + "'");
    String BMType=DBUtil.getCellValue(conn, "PTENUMINFODETL", "ENUDT", "ENUID='BMType' and ENUTP=" + TYPENO);
    BMType=DBUtil.fromDB(BMType);
    //TITLE
    String title=BMType+"登记信息";
    fb.setTitle(title);
    //LOANCAT3
    ElementBean eb=fb.getElement("LOANCAT3");
    if(TYPENO.equals("1")){
      eb.setEnutpname("LoanCat3A");
    }
    else if(TYPENO.equals("7")){
      eb.setEnutpname("LoanCat3B");
    }
    else if(TYPENO.equals("23")){
      eb.setEnutpname("LoanCat3C");
    }
    else if(TYPENO.equals("25")){
      eb.setEnutpname("LoanCat3D");
    }

    //SYSBTN_CANCEL
    if (ctx.getRequestAttribute("fromList") != null) {
      instance.setFieldDisabled("SYSBTN_CANCEL", true);
      instance.setFieldReadonly("SYSBTN_CANCEL", true);
    }
    //SYSBTN_CANCEL
    if (instance.isReadonly()) {
      ElementBean ebx=fb.getElement("SYSBTN_CANCEL");
      ebx.setComponetTp(6);
    }

    //按照传入的业务号在详细信息登记表中中查找记录,如果找到,则显示此记录,没有则进入新增加状态。
    String tblName = fb.getTbl();
    if (DBUtil.getCellValue(conn, tblName, "BMNO", "BMNO='" + BMNo + "'") != null) {
      instance.setValue("BMNO", BMNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  /**
   * 扩展基类的beforeInsert方法，点击“添加”按钮后响应的事件
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    try {
      MyDB.getInstance().addDBConn(conn);
      String APPDATE = SystemDate.getSystemDate5("");
      //由参数传入的数据;
      instance.setValue("BMNO", BMNo);
      instance.setValue("APPDATE", APPDATE);
      instance.setValue("CLIENTNAME", ClientName);
      instance.setValue("CREDITCLASS", this.creditClass);
      instance.setValue("BRHID", BrhID);
      instance.setValue("OPERATOR", OPERATOR);
      //BMTableApp中获得登记借款金额，申请放款日，申请还款日。
      UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
      instance.setValue("APPAMT", bmtblappdata.appAmt.toString());
      if(!this.TYPENO.equals(zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin+"")){
        instance.setValue("APPSTARTDATE", util.calToString(bmtblappdata.appStartDate));
        instance.setValue("APPENDDATE", util.calToString(bmtblappdata.appEndDate));
      }
      //授信要自动取联保成员做担保人
      else{
        String sql="";
        sql="select count(*) from BMPLDGSECURITY where bmno='"+this.BMNo+"'";
        RecordSet rs1=conn.executeQuery(sql);
        int cnt=0;
        if(rs1.next()) cnt=rs1.getInt(0);
        if(cnt<1 && !instance.isReadonly()){
          sql = "select a.* from cmindvclient a where a.unionno = (select b.unionno from cmindvclient b where b.clientno='" + this.ClientNo + "' and b.unionno<>0) and a.clientno<>'" + this.ClientNo + "'";
          RecordSet rs2 = conn.executeQuery(sql);
          int count = 0;
          while (rs2.next() && count < 8) {
            count++;
            String db_bmno = this.BMNo;
            String db_pledgeno = zt.cms.pub.code.SerialNumber.getNextSN("BMPLDGSECURITY", "PLEDGENO") + "";
            String db_clientno = rs2.getString("clientno");
            String db_clientname = rs2.getString("name");
            String db_securityamt = instance.getStringValue("APPAMT");
            if(db_securityamt==null) db_securityamt="0";
            String db_operator = this.OPERATOR;
            String db_idtype = rs2.getString("idtype");
            String db_id = rs2.getString("id");
            String db_creditclass = rs2.getString("creditclass");
            String db_loancard = rs2.getString("loancardno");
            if (db_loancard == null) db_loancard = "";
            String db_loantype3 = "100";
            sql = "insert into BMPLDGSECURITY ";
            sql += "(bmno,";
            sql += "pledgeno,";
            sql += "clientno,";
            sql += "clientname,";
            sql += "securityamt,";
            sql += "operator,";
            sql += "idtype,";
            sql += "id,";
            sql += "creditclass,";
            sql += "loancard,";
            sql += "loantype3)";
            sql += " values";
            sql += "('" + db_bmno + "',";
            sql += "" + db_pledgeno + ",";
            sql += "'" + db_clientno + "',";
            sql += "'" + db_clientname + "',";
            sql += "" + db_securityamt + ",";
            sql += "'" + db_operator + "',";
            sql += "" + db_idtype + ",";
            sql += "'" + db_id + "',";
            sql += "" + db_creditclass + ",";
            sql += "'" + db_loancard + "',";
            sql += "" + db_loantype3 + ")";
            conn.executeUpdate(sql);
          }
          rs2.close();
        }
        rs1.close();
      }
      return 0;
    }
    catch (Exception e) {
      return -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * 扩展基类的preInsert方法，对实际业务做insert前的处理
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
//    String appmonths,chgtype,frate;
//    chgtype = ctx.getParameter("CHGTYPE");
//    appmonths = ctx.getParameter("APPMONTHS");
//    frate = ctx.getParameter("FRATE");
//    System.out.println("---------------------"+chgtype+appmonths+frate);
//
//    新加条件判断by sunzg 10-20
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
//        此处修改了，精确度 modify by sdj   10-20
//          fratef = fratef.divide(new BigDecimal(100), 4 ,BigDecimal.ROUND_CEILING);
//
//        }
//        catch (Exception e) {
//          Debug.debug(e);
//          msgs.add("申请期限或浮动利率数据格式错误！");
//          return -1;
//        }
//
//        BMBaseInst baseint = BMBaseInst.getBaseInst(Integer.parseInt(this.
//            TYPENO), appMonthInt);
//        if (baseint == null) {
//          instance.setValue("BRATE", "");
//          instance.setValue("RATE", "");
//          msgs.add("未取得基准利率设置，请检查系统的基准利率设置！");
//          return -1;
//        }
//
//        System.out.println("baseint ===================" + baseint.interest);
//
//        instance.setValue("BRATE", "" + baseint.interest);
//        BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
//            BigDecimal(1)));
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


    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    int LOANTYPE3=instance.getIntValue("LOANTYPE3");
    int appPlus=getAppPlus(BMNo,LOANTYPE3,conn);
    if(appPlus<1){
      if(LOANTYPE3==210){
        msgs.add("担保方式为保证，保证人信息不可以为空！");
        return -1;
      }
      if(LOANTYPE3==220){
        msgs.add("担保方式为抵押，抵押物信息不可以为空！");
        return -1;
      }
      if(LOANTYPE3==230){
        msgs.add("担保方式为质押，质押物信息不可以为空！");
        return -1;
      }
    }
    return 0;
  }

  /**
   * 扩展基类的postInsertOk方法，对实际业务做insert后的处理
   */
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("系统错误，是否完成标志为空！");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
          flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
          if (flg >= 0) {
              ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
              ctx.setRequestAtrribute("msg", "信息保存成功，并提交到第三级审批！");
              ctx.setRequestAtrribute("flag", "1");
              ctx.setTarget("/showinfo.jsp");
          }
      }
      else {
        ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
        ctx.setRequestAtrribute("msg", "信息保存成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      return flg;
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * 扩展基类的postFindOk方法，对实际业务做find成功后的处理
   */
  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("CREDITCLASS", this.creditClass);
    return 0;
  }

  /**
   * 扩展基类的beforeEdit方法，点击“添加”按钮后响应的事件
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    //客户名称
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("CREDITCLASS", this.creditClass);
    return 0;
  }

  /**
   * 扩展基类的preEdit方法，对实际业务做edit前的处理
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    int LOANTYPE3=instance.getIntValue("LOANTYPE3");
    int appPlus=getAppPlus(BMNo,LOANTYPE3,conn);
    if(appPlus<1){
      if(LOANTYPE3==210){
        msgs.add("担保方式为保证，保证人信息不可以为空！");
        return -1;
      }
      if(LOANTYPE3==220){
        msgs.add("担保方式为抵押，抵押物信息不可以为空！");
        return -1;
      }
      if(LOANTYPE3==230){
        msgs.add("担保方式为质押，质押物信息不可以为空！");
        return -1;
      }
    }
    return 0;
  }

  /**
   * 扩展基类的postEditOk方法，对实际业务做edit后的处理
   */
  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("系统错误，是否完成标志为空！");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
          flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
          if (flg >= 0) {
              ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
              ctx.setRequestAtrribute("msg", "信息保存成功，并提交到第三级审批！");
              ctx.setRequestAtrribute("flag", "1");
              ctx.setTarget("/showinfo.jsp");
          }
      }
      else {
        ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
        ctx.setRequestAtrribute("msg", "信息保存成功！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      return flg;
    }
    catch (Exception ex) {
      msgs.add("未知原因导致的系统错误！");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * 扩展基类的buttonEvent方法，响应自定义按钮的点击事件
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("业务尚未登记！");
      return -1;
    }

    ctx.setRequestAtrribute(ParamName.ParamName, param);
    if (button.equals("BTN_CKDKZ")) {
      BMTableData bmtbldata = BMTable.getBMTable(BMNo);
      param.addParam(ParamName.CLientNo, bmtbldata.clientNo);
      param.addParam(ParamName.Flag, ParamName.Flag_READ);
      ctx.setRequestAtrribute(ParamName.ParamName, param);
      trigger(manager, "BMCREDITLMAINTPAGE", null);
    }
    else if (button.equals("BTN_DYDJ")) {
      trigger(manager, "BMPLEDGELIST", null);
    }
    else if (button.equals("BTN_ZYDJ")) {
      trigger(manager, "BMPLEDGELIST1", null);
    }
    else if (button.equals("BTN_DBDJ")) {
      trigger(manager, "BMPLDGSECURITYLIST", null);
    }
    else if (button.equals("BTN_TXDJ")) {
      trigger(manager, "BMPLDGBILLDISLIST", null);
    }
    else if (button.equals("BTN_ZTXDJ")) {
      trigger(manager, "BMPLDGBILLREDISLIST", null);
    }
    else if (button.equals("BTN_ZRPJ")) {
      ctx.setRequestAtrribute("TYPE", "ZR");
      trigger(manager, "BMPLDGBILLDISLIST", null);
    }
    else if (button.equals("BTN_ZCPJ")) {
      ctx.setRequestAtrribute("TYPE", "ZC");
      trigger(manager, "BMPLDGBILLREDISLIST", null);
    }
    else if (button.equals("BTN_DZDJ")) {
      trigger(manager, "BMPLDGPDASSETLIST", null);
    }
    //显示客户信息
    else if(button.equals("BTN_CLIENT")){
      ctx.setRequestAtrribute("CLIENTNO",ClientNo);
      CMClient cc=CMClientMan.getCMClient(ClientNo);
      boolean isIndv=cc.ifIndv;
      if(isIndv){
        trigger(manager, "100001", null);
      }
      else{
        trigger(manager, "CMCC02", null);
      }
    }
    else if (button.equals("SYSBTN_CANCEL")) {
      if (instance.isReadonly()) {
        ctx.setRequestAtrribute("title", "贷款业务详细信息登记");
        ctx.setRequestAtrribute("msg", "查看历史审批记录中状态下不能取消登记，请到工作台办理！");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      else {
        ctx.setRequestAtrribute("BMNo", BMNo);
        ctx.setRequestAtrribute("BMTransNo", BMTransNo);
        ctx.setRequestAtrribute("OPERATOR", OPERATOR);
        ctx.setTarget("/jsp/bm/bmappcancle.jsp");
      }
    }
    return 0;
  }

  /**
   * 根据不同的担保方式获取是否存在实际担保的内容
   * -1：业务号（p_bmno）参数错误
   * -2：担保类型（p_loantype3）参数错误
   * -3：数据库异常
   */
  public int getAppPlus(String p_bmno,int p_loantype3,DatabaseConnection p_conn){
    if(p_bmno==null || p_bmno.trim().length()<1) return -1;
    String sql="select count(*) from ";
    String sql_from_where=null;
    //信用担保不需要担保
    if(p_loantype3==100){
      return 0;
    }
    //保证担保
    else if(p_loantype3==210){
      sql_from_where="BMPLDGSECURITY where BMNO='"+p_bmno+"'";
    }
    //抵押担保
    else if(p_loantype3==220){
      sql_from_where="BMPLDGMORT where BMNO='"+p_bmno+"' and PLDGMORTTYPE=1";
    }
    //质押担保
    else if(p_loantype3==230){
      sql_from_where="BMPLDGMORT where BMNO='"+p_bmno+"' and PLDGMORTTYPE=5";
    }
    //
    if(sql_from_where==null) return -2;
    sql+=sql_from_where;
    try {
      RecordSet rs=p_conn.executeQuery(sql);
      int tmp=0;
      if(rs.next()){
        tmp=rs.getInt(0);
      }
      rs.close();
      return tmp;
    }
    catch (Exception ex) {
      return -3;
    }
  }

  public void postField(SessionContext ctx, FormInstance instance, String fieldname,
          ErrorMessages msgs, EventManager manager)
 {

     String appmonths,chgtype,frate;
//     chgtype = (String)ctx.getRequestAttribute("CHGTYPE");
     appmonths = (String)ctx.getRequestAttribute("APPMONTHS");
     frate = (String)ctx.getRequestAttribute("FRATE");
//     System.out.println("---------------------"+chgtype+"|"+appmonths+"|"+frate);

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
         msgs.add("申请期限或浮动利率数据格式错误！");
         instance.setHTMLFieldValue("RATE","");
         instance.setHTMLFieldValue("BRATE","");
         return;
       }

       BMBaseInst baseint = BMBaseInst.getBaseInst(Integer.parseInt(this.
           TYPENO), appMonthInt);
       if (baseint == null) {
         instance.setHTMLFieldValue("RATE","");
         instance.setHTMLFieldValue("BRATE","");
         instance.setHTMLShowMessage("INFO","未取得基准利率设置，请检查系统的基准利率设置！");
         return;
       }

//       System.out.println("baseint ===================" + baseint.interest);

       instance.setHTMLFieldValue("BRATE","" + baseint.interest);

       BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
           BigDecimal(1)));
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
