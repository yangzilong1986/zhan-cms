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
  private Param paramg = null; ///��õı�������
  private String strClientNO = null; //�ͻ���
  private String strTypeNo = null; //ҵ�����ʹ���
  private String strUserName = null; //��ǰ��½�û���
  private CMClient cmclient = null; //�ͻ�����
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
      msgs.add("�������󣬿ͻ��Ų����ڣ�");
      return -1;
    }
    else {
      strTypeNo = "1";

    }
    cmclient = CMClientMan.getCMClient(strClientNO);

    instance.setValue("CLIENTNO", strClientNO);
    instance.setValue("TYPENO", strTypeNo);
    instance.setValue("CLIENTNAME", cmclient.name);

    //���մ���Ŀͻ��ź�ҵ�����ʹ�����Ҽ�¼,����ҵ�,������޸�״̬,û�����������״̬��
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
        ctx.setRequestAtrribute("title", "����֤�������Ȩ");
        ctx.setRequestAtrribute("msg", "�Բ��𣬸ÿͻ��Ĵ���֤�����ڣ�");
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

      String strKyjeInt = null; //���ý�����������
      String strKyjeDec = null; //���ý���С������
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
   * ��չ�����preInsert��������ʵ��ҵ����insertǰ�Ĵ���
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
//            msgs.add("�������������");
//            return -1;
//          }
//
//          fratef = new BigDecimal(frate);
//          fratef = fratef.divide(new BigDecimal(100), 4,
//                                 BigDecimal.ROUND_CEILING);
//        }
//        catch (Exception e) {
//          Debug.debug(e);
//          msgs.add("�������޻򸡶��������ݸ�ʽ����");
//          return -1;
//        }
//
//        BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
//            BMType_DaiKuanZhengDaiKuan, appMonthInt);
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
//        //System.out.println("finalaseint ===================" + finalrate);
//        instance.setValue("LOANRATE", "" + finalrate);
//
//        msgs.add("�Ѿ���û�׼���ʣ��ɹ�.���������.");
//        return -1;
//      }
//      else {
//        msgs.add("���������...");
//        return -1;
//      }
//    }
//    else if(chgtype.compareToIgnoreCase("PASSWDCHG") == 0)
//    {
//      try
//      {
//        if (passwd != null) passwd = passwd.trim();
//        if (passwd == null || passwd.length() <= 0) {
//          msgs.add("�����������֤����!");
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
//          msgs.add("�����������������!");
//          return -1;
//        }
//        else
//        {
//          msgs.add("������ȷ���������.");
//          return -1;
//        }
//      }
//      catch(Exception e)
//      {
//        Debug.debug(e);
//        instance.setValue("PASSWORD", "");
//        msgs.add("��������,����������!");
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
          msgs.add("�����ձ������ڽ�������!");
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
            msgs.add("�������������");

            return -1;
          }

          fratef = new BigDecimal(frate);
          fratef = fratef.divide(new BigDecimal(100), 4,
                                 BigDecimal.ROUND_CEILING);
          BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
              BMType_DaiKuanZhengDaiKuan, appMonthInt);
          if (baseint == null) {
            msgs.add("δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
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
          msgs.add("�����ջ򸡶�����δ��ȷ���룡");
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
          ctx.setRequestAtrribute("title", "����֤�������Ȩ�ɹ�");
          ctx.setRequestAtrribute("msg", "���ųɹ���");
          ctx.setRequestAtrribute("flag", "0");
          ctx.setTarget("/showinfo.jsp");
        }
      }
      else {
        ctx.setRequestAtrribute("title", "����֤�������Ȩʧ��!!!");
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
      msg = "�ͻ���Ϣ�����ڣ�";
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
        msg = "�ͻ�������Ϣ������";
        return -1;
      }

      if (loancert.getClientNo() == null || loancert.getStartDate() == null ||
          loancert.getEndDate() == null
          || loancert.getCreditLimit() == null || loancert.getLoanBal() == null) {
        msg = "�ͻ�������Ϣ������";
        return -1;
      }

      if (loancert.getDisabled() != EnumValue.LoanCertSts_KeYou) {
        msg = "�ͻ��������Ѿ�������ʹ��!";
        return -1;
      }

      if (loancert.getStartDate().after(thisdate)) {
        msg = "���ʼ��������������Ч����";
        return -1;
      }

      if (loancert.getEndDate().before(thisdate)) {
        msg = "���ʼ�����������Ž�������";
        return -1;
      }

      BigDecimal bal = loancert.getCreditLimit().subtract(loancert.getLoanBal());
      if (ret != null) {
        bal = bal.subtract(ret);
      }
      if (bal.compareTo(this.loanamt) < 0) {
        msg = "��������Ŷ��";
        return -1;
      }

      this.loancert = loancert;
      int rrr = this.getRequestData();
      if (rrr < 0) {
        Debug.debug(Debug.TYPE_ERROR, "����֤�Ŵ�ϵͳ�Զ�����������Ȩʧ�ܣ�������:" + rrr);
        this.msg = "������Ȩ����";
        return -1;
      }
      else {
        return 0;
      }
    }
    catch(Exception e)
    {
      Debug.debug(e);
      this.msg = "������Ȩ����";
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
        this.msg = "���ܽ���ҵ������";
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
          instance.setHTMLShowMessage("����","�����������֤����!");
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
          instance.setHTMLShowMessage("����","�����������������!");
          instance.setHTMLFieldValue("PASSWORD","");
          instance.setHTMLFocus("PASSWORD");
          return;
        }
        else {
//          instance.setHTMLShowMessage("��ʾ","������ȷ���������.");
          return;
        }
      }
      catch (Exception e) {
        Debug.debug(e);
        instance.setHTMLShowMessage("����","��������,����������!");
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
              instance.setHTMLShowMessage("INFO","�������������");
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
            instance.setHTMLShowMessage("INFO","�������޻򸡶��������ݸ�ʽ����");
            return;
          }

          BMBaseInst baseint = BMBaseInst.getBaseInst(EnumValue.
              BMType_DaiKuanZhengDaiKuan, appMonthInt);
          if (baseint == null) {
            instance.setHTMLFieldValue("LOANRATE","");
            instance.setHTMLFieldValue("BRATE","");
            instance.setHTMLShowMessage("INFO","δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
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
          //msgs.add("���������...");
          return;
        }
    } // end if for HTML onchange of field DUEDATE

  }

}
