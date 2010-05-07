package zt.cms.bm.loancert;

import java.math.*;

import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cmsi.client.*;

import java.util.*;

import zt.cmsi.biz.util;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.ErrorCode;
import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.utils.Debug;
import zt.cmsi.pub.define.SystemDate;
import zt.cmsi.pub.define.BMBaseInst;
import com.ebis.encrypt.*;

//import zt.platform.cachedb.*;

public class LoanIssue
    extends FormActions {
  private Param paramg = null; ///获得的变量集合
  private String strClientNO = null; //客户号
  private String strTypeNo = null; //业务类型代码
  private String strUserName = null; //当前登陆用户名
  private CMClient cmclient = null; //客户资料
  private String msg = null;
  private Loancert loancert = null;
  private BigDecimal loanamt = null;
  private BigDecimal loanrate = null;
  private BigDecimal frate = null;
  private BigDecimal brate = null;
  private Calendar duedate = null;
  private String bmno = null;
  private int myMonths = 0;
  private int loantype5 = 0;
  private int loancat3 = 0;
  private String cnlno = null;

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    strClientNO = (String) ctx.getParameter("CLIENTNO");
    if (strClientNO == null) {
      msgs.add("参数错误，客户号不存在！");
      return -1;
    }
    else {
      strTypeNo = "1";

    }
    cmclient = CMClientMan.getCMClient(strClientNO);

    instance.setValue("CLIENTNO", strClientNO);
    instance.setValue("TYPENO", strTypeNo);
    instance.setValue("CLIENTNAME", cmclient.name);

    //按照传入的客户号和业务类型代码查找记录,如果找到,则进入修改状态,没有则进入增加状态。
    if (strTypeNo != null && strTypeNo.length() > 0) {
      String formid = instance.getFormid();
      FormBean formBean = FormBeanManager.getForm(formid);
      String tblName = formBean.getTbl().trim();
      if (DBUtil.getCellValue(conn, tblName, "CLIENTNO",
                              "CLIENTNO='" + strClientNO + "' and TYPENO=" +
                              strTypeNo) != null) {
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }
      else {
        ctx.setRequestAtrribute("title", "贷款证贷款发放授权");
        ctx.setRequestAtrribute("msg", "对不起，该客户的贷款证不存在！");
        ctx.setRequestAtrribute("flag", "0");
        ctx.setTarget("/showinfo.jsp");
      }
    }
    return 0;
  }

  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    //Edit by wxj at 20040223
    String str = "";
    CreditLimitData cldata = CreditLimit.getCreditLimit(strClientNO,
        EnumValue.BMType_DaiKuanZhengDaiKuan);
    if (cldata != null) {
      BigDecimal ret = BMTable.getUnPostCertLoanBal(strClientNO);

      String strKyjeInt = null; //可用金额的整数部分
      String strKyjeDec = null; //可用金额的小数部分
      float fKyje = cldata.creditLimit.floatValue() - cldata.loanBal.floatValue();
      if (ret != null) {
        fKyje -= ret.floatValue();
      }
      String strKyje = String.valueOf(fKyje);

      int iLength = strKyje.length();
      int iDot = strKyje.indexOf(".");
      if (iLength > iDot + 2) {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = strKyje.substring(iDot + 1, iDot + 3);
      }
      else if (iLength > iDot + 1) {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = strKyje.substring(iDot + 1, iDot + 2);
        strKyjeDec = strKyjeDec + "0";
      }
      else {
        strKyjeInt = strKyje.substring(0, iDot);
        strKyjeDec = "00";
      }
      str = strKyjeInt + "." + strKyjeDec;
    }
    instance.setValue("KYJE", str);
    return 0;
  }

  /**
   * 扩展基类的preInsert方法，对实际业务做insert前的处理
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
//    String duedate, chgtype, frate;
//    chgtype = ctx.getParameter("CHGTYPE");
//    duedate = ctx.getParameter("DUEDATE");
//    frate = ctx.getParameter("FRATE");
//    String passwd = ctx.getParameter("PASSWORD");
//    String clientno = ctx.getParameter("CLIENTNO");
//
//    if (chgtype.compareToIgnoreCase("RATECHG") == 0) {
//      this.getOtherCatofLoan(this.strClientNO, conn);
//      if (duedate != null && frate != null && duedate.length() > 0 &&
//          frate.length() > 0) {
//        int appMonthInt;
//        BigDecimal fratef = null;
//        try {
//          appMonthInt = this.retMonth(duedate);
//          if (appMonthInt < 0) {
//            instance.setValue("LOANRATE", "");
//            instance.setValue("BRATE", "");
//            msgs.add("到期日输入错误！");
//            return -1;
//          }
//
//          fratef = new BigDecimal(frate);
//          fratef = fratef.divide(new BigDecimal(100), 4,
//                                 BigDecimal.ROUND_CEILING);
//        }
//        catch (Exception e) {
//          Debug.debug(e);
//          msgs.add("申请期限或浮动利率数据格式错误！");
//          return -1;
//        }
//
//        BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
//            BMType_DaiKuanZhengDaiKuan, appMonthInt);
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
//        //System.out.println("finalaseint ===================" + finalrate);
//        instance.setValue("LOANRATE", "" + finalrate);
//
//        msgs.add("已经获得基准利率：成功.请继续输入.");
//        return -1;
//      }
//      else {
//        msgs.add("请继续输入...");
//        return -1;
//      }
//    }
//    else if(chgtype.compareToIgnoreCase("PASSWDCHG") == 0)
//    {
//      try
//      {
//        if (passwd != null) passwd = passwd.trim();
//        if (passwd == null || passwd.length() <= 0) {
//          msgs.add("必须输入贷款证密码!");
//          return -1;
//        }
//        EncryptData ed = new EncryptData();
//        String encrypted = new String(ed.enPasswd(passwd.getBytes()));
//
//        zt.platform.cachedb.ConnectionManager db = zt.platform.cachedb.
//            ConnectionManager.getInstance();
//        String dbpasswd = db.getCellValue("passwd", "bmcreditlimit","typeno="+ EnumValue.BMType_DaiKuanZhengDaiKuan+" and clientno='" + clientno + "'");
//        if(dbpasswd.compareToIgnoreCase(encrypted) != 0)
//        {
//          instance.setValue("PASSWORD", "");
//          msgs.add("密码错误请重新输入!");
//          return -1;
//        }
//        else
//        {
//          msgs.add("密码正确请继续输入.");
//          return -1;
//        }
//      }
//      catch(Exception e)
//      {
//        Debug.debug(e);
//        instance.setValue("PASSWORD", "");
//        msgs.add("发生错误,请重新输入!");
//        return -1;
//      }
//    }
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance,
                         String button, ErrorMessages msgs,
                         EventManager manager) {
    String amt = ctx.getParameter("LOANAMT").trim();
    String rate = ctx.getParameter("LOANRATE").trim();
    String duedate = ctx.getParameter("DUEDATE").trim();
    String frate = ctx.getParameter("FRATE").trim();
    this.cnlno = ctx.getParameter("CNLNO").trim();
    if (button != null && amt != null && rate != null && duedate != null &&
        button.compareToIgnoreCase("PROCESS") == 0) {
      try {

        if (SystemDate.getSystemDate1().before(util.stringToCal(duedate)) == false) {
          msgs.add("到期日必须晚于今天日期!");
          return -1;
        }

        this.loanamt = new BigDecimal(amt);

        //calculate the fianlrate
        if (duedate != null && frate != null && duedate.length() > 0 &&
            frate.length() > 0) {
          int appMonthInt;
          BigDecimal fratef = null;

          appMonthInt = this.retMonth(duedate);
          if (appMonthInt < 0) {
            msgs.add("到期日输入错误！");

            return -1;
          }

          fratef = new BigDecimal(frate);
          fratef = fratef.divide(new BigDecimal(100), 4,
                                 BigDecimal.ROUND_CEILING);
          BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
              BMType_DaiKuanZhengDaiKuan, appMonthInt);
          if (baseint == null) {
            msgs.add("未取得基准利率设置，请检查系统的基准利率设置！");
            return -1;
          }

          BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
              BigDecimal(1)));
          //this.loanrate = new BigDecimal(rate);
          this.frate = fratef.multiply(new BigDecimal(100));
          this.brate = baseint.interest;
          this.loanrate = finalrate;
          this.duedate = util.stringToCal(duedate);
          this.myMonths = appMonthInt;
          if (this.duedate == null) {
            return -1;
          }

        }
        else {
          msgs.add("到期日或浮动利率未正确输入！");
          return -1;
        }
      }
      catch (Exception e) {
        Debug.debug(e);
        return -1;
      }

      //System.out.println("button:" + button + amt + rate+duedate);
      this.getOtherCatofLoan(this.strClientNO, conn);
      if (this.issueloan(conn) >= 0) {
        if (this.bmno != null) {
          ctx.setRequestAtrribute("BMNO", this.bmno);
          ctx.setTarget("/jspreport/loanbiz.jsp");
        }
        else {
          ctx.setRequestAtrribute("title", "贷款证贷款发放授权成功");
          ctx.setRequestAtrribute("msg", "发放成功！");
          ctx.setRequestAtrribute("flag", "0");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "贷款证贷款发放授权失败!!!");
        ctx.setRequestAtrribute("msg", this.msg);
        ctx.setRequestAtrribute("flag", "0");
        ctx.setTarget("/showinfo.jsp");
      }
      return 0;
    }
    else {
      return -1;
    }
  }

  private int issueloan(DatabaseConnection conn) {
    if (cmclient == null) {
      msg = "客户信息不存在！";
      return -1;
    }
    Loancert loancert = null;
    BigDecimal ret = null;

    try
    {
      MyDB.getInstance().addDBConn(conn);

      Calendar thisdate = SystemDate.getSystemDate1();

      loancert = LoancertFactory.findLoancertByClientNo(cmclient.
          clientNo);

      ret = BMTable.getUnPostCertLoanBal(strClientNO);
      //System.out.println("unposted bal is "+ret);

      if (loancert == null) {
        msg = "客户授信信息不存在";
        return -1;
      }

      if (loancert.getClientNo() == null || loancert.getStartDate() == null ||
          loancert.getEndDate() == null
          || loancert.getCreditLimit() == null || loancert.getLoanBal() == null) {
        msg = "客户授信信息不完整";
        return -1;
      }

      if (loancert.getDisabled() != EnumValue.LoanCertSts_KeYou) {
        msg = "客户的授信已经被不可使用!";
        return -1;
      }

      if (loancert.getStartDate().after(thisdate)) {
        msg = "贷款开始日期早于授信生效日期";
        return -1;
      }

      if (loancert.getEndDate().before(thisdate)) {
        msg = "贷款开始日期晚于授信结束日期";
        return -1;
      }

      BigDecimal bal = loancert.getCreditLimit().subtract(loancert.getLoanBal());
      if (ret != null) {
        bal = bal.subtract(ret);
      }
      if (bal.compareTo(this.loanamt) < 0) {
        msg = "贷款超过授信额度";
        return -1;
      }

      this.loancert = loancert;
      int rrr = this.getRequestData();
      if (rrr < 0) {
        Debug.debug(Debug.TYPE_ERROR, "贷款证信贷系统自动审批发放授权失败，代码是:" + rrr);
        this.msg = "发放授权错误";
        return -1;
      }
      else {
        return 0;
      }
    }
    catch(Exception e)
    {
      Debug.debug(e);
      this.msg = "发放授权错误";
      return -1;
    }
    finally
    {
        MyDB.getInstance().releaseDBConn();
    }
  }

  private int getRequestData() {

    int errorcode = 0;
    DatabaseConnection dc = MyDB.getInstance().apGetConn();
    if (dc == null) {
      return ErrorCode.NO_DB_CONN;
    }
    String bmno = null;
    try {
      bmno = BMTable.createBMTable(EnumValue.BMType_DaiKuanZhengDaiKuan,
                                   this.loancert.getClientNo(),
                                   this.loancert.getBrhid()
                                   , this.loancert.getBrhid(),
                                   this.loancert == null ? "000000" :
                                   this.loancert.getOperator());
      if (bmno != null) {
        this.bmno = bmno;
        errorcode = BMTable.updateTableStatus(bmno, EnumValue.BMStatus_FaFang,
                                              this.loancert == null ? "000000" :
                                              this.loancert.getOperator());
        if (errorcode >= 0) {

          //this.txId = bmno;
          UpToDateApp data = new UpToDateApp();

          data.appAmt = this.loanamt;
          data.appDate = SystemDate.getSystemDate1();
          data.appEndDate = this.duedate;
          data.appStartDate = SystemDate.getSystemDate1();
          data.finalStartDate = SystemDate.getSystemDate1();
          data.finalEndDate = this.duedate;
          data.finalAmt = this.loanamt;
          data.bRate = this.brate;
          data.fRate = this.frate;
          data.finalRate = this.loanrate;
          data.firstResp = loancert.getFirstResp();
          data.ifRespLoan = new Integer(loancert.getIfRespLoan());
          data.decidedBy = loancert.getDecidedby();
          data.contractNo = confitem.LOAN_CERT_ISSUE;
          data.appMonths = new Integer(this.myMonths);
          data.finalMonths = new Integer(this.myMonths);

          data.clientNo = this.strClientNO;
          if (cmclient.clientMgr != null) {
            data.clientMgr = cmclient.clientMgr;
          }
          if (cmclient.ecomDeptType != null) {
            data.eComDeptType = cmclient.ecomDeptType;
          }
          if (cmclient.ecomType != null) {
            data.eComType = cmclient.ecomType;
          }
          if (cmclient.etpScopType != null) {
            data.etpScopType = cmclient.etpScopType;
          }
          if (cmclient.sectorCat1 != null) {
            data.sectorCat1 = cmclient.sectorCat1;
          }
          if (cmclient.clientType != null) {
            data.clientType = cmclient.clientType;
          }
          data.loanType3 = new Integer(EnumValue.LoanType3_BaoZhneg);
          if (loancert != null && loancert.getIfRespLoan() == 1) {
            data.firstRespPct = this.loanamt;
          }
          data.bmTypeNo = new Integer(EnumValue.BMType_DaiKuanZhengDaiKuan);
          data.loanCat3 = new Integer(this.loancat3);
          data.loanType5 = new Integer(this.loantype5);
          data.cnlNo = this.cnlno;
          data.loanType3 = new Integer(EnumValue.LoanType3_BaoZhneg);

//          String getBmno = "select bmno from bmtableapp where clientno='"+loancert.getClientNo()+"' and typeno="+zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin + " order by bmno desc";
//          RecordSet bmnoRs = dc.executeQuery("getbmno");
//          if(bmnoRs.next()){
//            data.loanType3 = new Integer(bmnoRs.getInt("loantype3"));
//          }


          errorcode = BMTable.updateUpToDateApp(bmno, data);
          if (errorcode >= 0) {
            errorcode = LoanGranted.createGrant(bmno,
                                                this.loancert == null ? "000000" :
                                                this.loancert.getOperator(),
                                                this.loancert.getBrhid());
            if (errorcode >= 0) {
              errorcode = LoanGranted.sendLoanGrant(bmno, true);
            }
          }
        }
      }
      else {
        this.msg = "不能建立业务主表";
        errorcode = -1;
      }

    }
    catch (Exception e) {
      if (Debug.isDebugMode == true) {
        Debug.debug(e);
      }
      Debug.debug(Debug.TYPE_ERROR,
                  "Exception when creating credit authorization!");
      Debug.debug(Debug.TYPE_ERROR, e.getMessage());
      errorcode = ErrorCode.EXCPT_FOUND;
    }
    finally {
      MyDB.getInstance().apReleaseConn(errorcode);
      return errorcode;
    }
  }

  private int retMonth(String duedate) {
    if (duedate == null) {
      return -1;
    }
    try {
      Calendar temp;
      temp = util.stringToCal(duedate);
      //System.out.println(""+temp.get(Calendar.YEAR)+"E"+SystemDate.getSystemDate1().get(Calendar.YEAR)
      //                   +"E"+temp.get(Calendar.MONTH)+"E"+SystemDate.getSystemDate1().get(Calendar.MONTH));
      return ( (temp.get(Calendar.YEAR) -
                SystemDate.getSystemDate1().get(Calendar.YEAR)) * 12 +
              (temp.get(Calendar.MONTH) -
               SystemDate.getSystemDate1().get(Calendar.MONTH)));
    }
    catch (Exception e) {
      Debug.debug(e);
      return -1;
    }
  }

  private void getOtherCatofLoan(String clientno, DatabaseConnection conn) {
    try {
      String sSql = "select bmtableapp.loancat3,bmtableapp.loantype5 from BMTable,bmtableapp where bmtable.bmno=bmtableapp.bmno and bmtable.typeno=11 and bmtable.clientno='" +
          clientno + "' order by bmtable.bmno desc";
      Debug.debug(Debug.TYPE_SQL, sSql);
      RecordSet rs = null;
      Debug.debug(Debug.TYPE_SQL, sSql);
      rs = conn.executeQuery(sSql);
      if (rs.next()) {
        this.loancat3 = rs.getInt("loancat3");
        this.loantype5 = rs.getInt("loantype5");
      }
    }
    catch (Exception e) {
      Debug.debug(e);
    }
  }

  public void postField(SessionContext ctx, FormInstance instance,
                        String fieldname,
                        ErrorMessages msgs, EventManager manager) {

    String passwd = (String)ctx.getRequestAttribute("PASSWORD");
//    System.out.println("-------------------------------"+fieldname);
    if (fieldname.compareToIgnoreCase("PASSWORD") == 0) {
      try {
        if (passwd != null) {
          passwd = passwd.trim();
        }
        if (passwd == null || passwd.length() <= 0) {
          instance.setHTMLShowMessage("错误","必须输入贷款证密码!");
          instance.setHTMLFieldValue("PASSWORD","");
          return;
        }

        EncryptData ed = new EncryptData();
        String encrypted = new String(ed.enPasswd(passwd.getBytes()));

        zt.platform.cachedb.ConnectionManager db = zt.platform.cachedb.
            ConnectionManager.getInstance();
        String dbpasswd = db.getCellValue("passwd", "bmcreditlimit",
                                          "typeno=" +
                                          EnumValue.BMType_DaiKuanZhengDaiKuan +
                                          " and clientno='" + strClientNO + "'");
        if (dbpasswd.compareToIgnoreCase(encrypted) != 0) {
          instance.setHTMLShowMessage("错误","密码错误请重新输入!");
          instance.setHTMLFieldValue("PASSWORD","");
          instance.setHTMLFocus("PASSWORD");
          return;
        }
        else {
//          instance.setHTMLShowMessage("提示","密码正确请继续输入.");
          return;
        }
      }
      catch (Exception e) {
        Debug.debug(e);
        instance.setHTMLShowMessage("错误","发生错误,请重新输入!");
        instance.setHTMLFieldValue("PASSWORD","");
        instance.setHTMLFocus("PASSWORD");
        return;
      }
    }
    else
    {

      String duedate, chgtype, frate;
      chgtype = (String)ctx.getRequestAttribute("CHGTYPE");
      duedate = (String)ctx.getRequestAttribute("DUEDATE");
      frate = (String)ctx.getRequestAttribute("FRATE");

        //this.getOtherCatofLoan(this.strClientNO, conn);
        if (duedate != null && frate != null && duedate.length() > 0 &&
            frate.length() > 0) {
          int appMonthInt;
          BigDecimal fratef = null;
          try {
            appMonthInt = this.retMonth(duedate);
            if (appMonthInt < 0) {
              instance.setHTMLFieldValue("LOANRATE","");
              instance.setHTMLFieldValue("BRATE","");
              instance.setHTMLShowMessage("INFO","到期日输入错误！");
              return;
            }

            fratef = new BigDecimal(frate);
            fratef = fratef.divide(new BigDecimal(100), 4,
                                   BigDecimal.ROUND_CEILING);
          }
          catch (Exception e) {
            Debug.debug(e);
            instance.setHTMLFieldValue("LOANRATE","");
            instance.setHTMLFieldValue("BRATE","");
            instance.setHTMLShowMessage("INFO","申请期限或浮动利率数据格式错误！");
            return;
          }

          BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
              BMType_DaiKuanZhengDaiKuan, appMonthInt);
          if (baseint == null) {
            instance.setHTMLFieldValue("LOANRATE","");
            instance.setHTMLFieldValue("BRATE","");
            instance.setHTMLShowMessage("INFO","未取得基准利率设置，请检查系统的基准利率设置！");
            return;
          }

          //System.out.println("baseint ===================" + baseint.interest);

          instance.setHTMLFieldValue("BRATE","" + baseint.interest);
          BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
              BigDecimal(1)));
          //System.out.println("finalaseint ===================" + finalrate);
          finalrate=finalrate.divide(new BigDecimal(1), 4 ,BigDecimal.ROUND_HALF_EVEN) ;
          instance.setHTMLFieldValue("LOANRATE","" + finalrate);

          return;
        }
        else {
          //msgs.add("请继续输入...");
          return;
        }
    } // end if for HTML onchange of field DUEDATE

  }

}
