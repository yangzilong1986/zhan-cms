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
  private String flag = null; //�����Ƿ�ɶ�
  private String BMNo = null; //ҵ���
  private String BMTransNo = null; //ҵ���
  private String ClientNo = null; //�ͻ����
  private String ClientName = null; //�ͻ�����
  private String BrhID = null; //ҵ������
  private String OPERATOR = null; //ҵ��Ա
  private String TYPENO=null;//ҵ������
  private Param param = null; //������װ�������ڴ���
  private int creditClass = 0;

  private boolean isJustWeihu = false;
  /**
   * ��չ�����load����
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
      msgs.add("�������󣬲������󲻴��ڣ�");
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
      msgs.add("��������ҵ��Ų����ڣ�");
      return -1;
    }
    //BMTransNo
    Object tmp = param.getParam(ParamName.BMTransNo);
    BMTransNo = (tmp == null) ? null :
      param.getParam(ParamName.BMTransNo).toString();

    if(ctx.getRequestAttribute("isJustWeihu")==null){
        if (flag.equals("write") && (BMTransNo == null || BMTransNo.length() < 1)) {
            msgs.add("��������ҵ����ϸ�Ų����ڣ�");
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
      msgs.add("ϵͳ���󣬿ͻ����Ʋ����ڣ�");
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
          msgs.add("�ò����ͻ����õȼ���Ϣ��");
          return -1;
        }
      }
    }



    //BMSTATUS
    if (flag.equals("write") && bmtbldata.bmStatus == zt.cmsi.pub.cenum.EnumValue.BMStatus_QuXiao) {
      msgs.add("�Բ��𣬸ñ�ҵ���Ѿ���ȡ�������ֹ�ˢ�¹���̨��");
      return -1;
    }
    //BrhID
    BrhID = (String) param.getParam(ParamName.BrhID);
    if (BrhID == null || BrhID.length() < 1) {
      //msgs.add("��������ҵ�����㲻���ڣ�");
      //return -1;
      BrhID = "";
    }
    //OPERATOR
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    OPERATOR = um.getUserName();
    if (OPERATOR == null || OPERATOR.length() < 1) {
      msgs.add("ϵͳ���󣬻�ȡҵ�����Ա��Ϣʧ�ܣ�");
      return -1;
    }
    //����ҵ������⻯����
    instance.useCloneFormBean();
    FormBean fb = instance.getFormBean();
    TYPENO=DBUtil.getCellValue(conn, "BMTABLE", "TYPENO", "BMNO='" + BMNo + "'");
    String BMType=DBUtil.getCellValue(conn, "PTENUMINFODETL", "ENUDT", "ENUID='BMType' and ENUTP=" + TYPENO);
    BMType=DBUtil.fromDB(BMType);
    //TITLE
    String title=BMType+"�Ǽ���Ϣ";
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

    //���մ����ҵ�������ϸ��Ϣ�ǼǱ����в��Ҽ�¼,����ҵ�,����ʾ�˼�¼,û�������������״̬��
    String tblName = fb.getTbl();
    if (DBUtil.getCellValue(conn, tblName, "BMNO", "BMNO='" + BMNo + "'") != null) {
      instance.setValue("BMNO", BMNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  /**
   * ��չ�����beforeInsert�������������ӡ���ť����Ӧ���¼�
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    try {
      MyDB.getInstance().addDBConn(conn);
      String APPDATE = SystemDate.getSystemDate5("");
      //�ɲ������������;
      instance.setValue("BMNO", BMNo);
      instance.setValue("APPDATE", APPDATE);
      instance.setValue("CLIENTNAME", ClientName);
      instance.setValue("CREDITCLASS", this.creditClass);
      instance.setValue("BRHID", BrhID);
      instance.setValue("OPERATOR", OPERATOR);
      //BMTableApp�л�õǼǽ�������ſ��գ����뻹���ա�
      UpToDateApp bmtblappdata = BMTable.getUpToDateApp(BMNo);
      instance.setValue("APPAMT", bmtblappdata.appAmt.toString());
      if(!this.TYPENO.equals(zt.cmsi.pub.cenum.EnumValue.BMType_ShouXin+"")){
        instance.setValue("APPSTARTDATE", util.calToString(bmtblappdata.appStartDate));
        instance.setValue("APPENDDATE", util.calToString(bmtblappdata.appEndDate));
      }
      //����Ҫ�Զ�ȡ������Ա��������
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
   * ��չ�����preInsert��������ʵ��ҵ����insertǰ�Ĵ���
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
//    �¼������ж�by sunzg 10-20
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
//        �˴��޸��ˣ���ȷ�� modify by sdj   10-20
//          fratef = fratef.divide(new BigDecimal(100), 4 ,BigDecimal.ROUND_CEILING);
//
//        }
//        catch (Exception e) {
//          Debug.debug(e);
//          msgs.add("�������޻򸡶��������ݸ�ʽ����");
//          return -1;
//        }
//
//        BMBaseInst baseint = BMBaseInst.getBaseInst(Integer.parseInt(this.
//            TYPENO), appMonthInt);
//        if (baseint == null) {
//          instance.setValue("BRATE", "");
//          instance.setValue("RATE", "");
//          msgs.add("δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
//          return -1;
//        }
//
//        System.out.println("baseint ===================" + baseint.interest);
//
//        instance.setValue("BRATE", "" + baseint.interest);
//        BigDecimal finalrate = baseint.interest.multiply(fratef.add(new
//            BigDecimal(1)));
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


    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    int LOANTYPE3=instance.getIntValue("LOANTYPE3");
    int appPlus=getAppPlus(BMNo,LOANTYPE3,conn);
    if(appPlus<1){
      if(LOANTYPE3==210){
        msgs.add("������ʽΪ��֤����֤����Ϣ������Ϊ�գ�");
        return -1;
      }
      if(LOANTYPE3==220){
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
      if(LOANTYPE3==230){
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
    }
    return 0;
  }

  /**
   * ��չ�����postInsertOk��������ʵ��ҵ����insert��Ĵ���
   */
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("ϵͳ�����Ƿ���ɱ�־Ϊ�գ�");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
          flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
          if (flg >= 0) {
              ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
              ctx.setRequestAtrribute("msg", "��Ϣ����ɹ������ύ��������������");
              ctx.setRequestAtrribute("flag", "1");
              ctx.setTarget("/showinfo.jsp");
          }
      }
      else {
        ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
        ctx.setRequestAtrribute("msg", "��Ϣ����ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      return flg;
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * ��չ�����postFindOk��������ʵ��ҵ����find�ɹ���Ĵ���
   */
  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("CREDITCLASS", this.creditClass);
    return 0;
  }

  /**
   * ��չ�����beforeEdit�������������ӡ���ť����Ӧ���¼�
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    //�ͻ�����
    instance.setValue("CLIENTNAME", ClientName);
    instance.setValue("CREDITCLASS", this.creditClass);
    return 0;
  }

  /**
   * ��չ�����preEdit��������ʵ��ҵ����editǰ�Ĵ���
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
        msgs.add("������ʽΪ��֤����֤����Ϣ������Ϊ�գ�");
        return -1;
      }
      if(LOANTYPE3==220){
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
      if(LOANTYPE3==230){
        msgs.add("������ʽΪ��Ѻ����Ѻ����Ϣ������Ϊ�գ�");
        return -1;
      }
    }
    return 0;
  }

  /**
   * ��չ�����postEditOk��������ʵ��ҵ����edit��Ĵ���
   */
  public int postEditOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    String finish = (String) ctx.getParameter("finish");
    if (finish == null || finish.length() < 1) {
      msgs.add("ϵͳ�����Ƿ���ɱ�־Ϊ�գ�");
      return -1;
    }
    try {
      MyDB.getInstance().addDBConn(conn);
      int flg = 0;
      if (finish.trim().equals("1") && !isJustWeihu) {
          flg = BMTrans.compltTrans(BMNo, Integer.parseInt(BMTransNo), OPERATOR);
          if (flg >= 0) {
              ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
              ctx.setRequestAtrribute("msg", "��Ϣ����ɹ������ύ��������������");
              ctx.setRequestAtrribute("flag", "1");
              ctx.setTarget("/showinfo.jsp");
          }
      }
      else {
        ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
        ctx.setRequestAtrribute("msg", "��Ϣ����ɹ���");
        ctx.setRequestAtrribute("flag", "1");
        ctx.setTarget("/showinfo.jsp");
      }
      return flg;
    }
    catch (Exception ex) {
      msgs.add("δ֪ԭ���µ�ϵͳ����");
      return -2;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
    }
  }

  /**
   * ��չ�����buttonEvent��������Ӧ�Զ��尴ť�ĵ���¼�
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    if (BMNo == null || BMNo.length() < 1) {
      msgs.add("ҵ����δ�Ǽǣ�");
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
    //��ʾ�ͻ���Ϣ
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
        ctx.setRequestAtrribute("title", "����ҵ����ϸ��Ϣ�Ǽ�");
        ctx.setRequestAtrribute("msg", "�鿴��ʷ������¼��״̬�²���ȡ���Ǽǣ��뵽����̨����");
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
   * ���ݲ�ͬ�ĵ�����ʽ��ȡ�Ƿ����ʵ�ʵ���������
   * -1��ҵ��ţ�p_bmno����������
   * -2���������ͣ�p_loantype3����������
   * -3�����ݿ��쳣
   */
  public int getAppPlus(String p_bmno,int p_loantype3,DatabaseConnection p_conn){
    if(p_bmno==null || p_bmno.trim().length()<1) return -1;
    String sql="select count(*) from ";
    String sql_from_where=null;
    //���õ�������Ҫ����
    if(p_loantype3==100){
      return 0;
    }
    //��֤����
    else if(p_loantype3==210){
      sql_from_where="BMPLDGSECURITY where BMNO='"+p_bmno+"'";
    }
    //��Ѻ����
    else if(p_loantype3==220){
      sql_from_where="BMPLDGMORT where BMNO='"+p_bmno+"' and PLDGMORTTYPE=1";
    }
    //��Ѻ����
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
         msgs.add("�������޻򸡶��������ݸ�ʽ����");
         instance.setHTMLFieldValue("RATE","");
         instance.setHTMLFieldValue("BRATE","");
         return;
       }

       BMBaseInst baseint = BMBaseInst.getBaseInst(Integer.parseInt(this.
           TYPENO), appMonthInt);
       if (baseint == null) {
         instance.setHTMLFieldValue("RATE","");
         instance.setHTMLFieldValue("BRATE","");
         instance.setHTMLShowMessage("INFO","δȡ�û�׼�������ã�����ϵͳ�Ļ�׼�������ã�");
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
