package zt.cms.cm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.zt.util.*;
import zt.cms.pub.*;
import zt.cms.pub.code.*;
import zt.cmsi.biz.*;
import zt.cmsi.client.*;
import zt.cmsi.migration.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.utils.*;
import zt.cms.bm.common.*;


public class QCorpClient extends FormActions {
  String strClientNO = null;
  public String strRgnCode = null;
  public String strClientMgr = null;
  private String strFlag = null;
  private String initClientNo = null; //���г�����ñ�����ʱ��������������ɵĿͻ���
  private String currClientNo = null; //���г���ʹ��
  private String guarantorID = null; //��֤��תΪ�ͻ�ʱ��������֤������
  private boolean isG = false; //�Ƿ�Ϊ������ת��������

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    int userNo = SessionInfo.getLoginUserNo(ctx);
    String roleStr = "select * from scuserrole where userno=" + userNo + " and roleno=1";
    RecordSet roleRs = conn.executeQuery(roleStr);
    if (roleRs.next()) {
      instance.useCloneFormBean();
      instance.getFormBean().setUseDelete(true);
    }



    String strMntbrhID = null;
    String strAppBrhID = null;

    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String strUserName = um.getUserName();
    strMntbrhID = SCUser.getBrhId(strUserName);

    strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
    if (strFlag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }

    strClientNO = ctx.getParameter("CLIENTNO");
    if (strClientNO == null) {
      strClientNO = (String) ctx.getRequestAttribute("CLIENTNO");
    }
    initClientNo = (String) ctx.getRequestAttribute("INITCLIENTNO");
    guarantorID = (String) ctx.getRequestAttribute("GID");
    String strSqlApp = "select * from cmcorpclient where ";

    if (strClientNO != null && strClientNO.length() > 0) {
      strSqlApp += "clientno='" + strClientNO + "'";
    }
    else if (guarantorID != null && guarantorID.trim().length() > 0) {
      strSqlApp += "id='" + guarantorID + "'";
    }
    if (strSqlApp.endsWith("where ")) {
      return 0;
    }

    RecordSet rs = conn.executeQuery(strSqlApp);
    if (rs.next()) {
      strAppBrhID = rs.getString("appbrhid").trim();
      if (strClientNO!=null && ! (SCBranch.checkSub(strAppBrhID, strMntbrhID) || strAppBrhID.equals(strMntbrhID))) {
        strFlag = "read";
        msgs.add("�ÿͻ����ڱ�����ά����Χ�ڣ�");
        instance.setReadonly(true);
      }
      else if(guarantorID!=null && guarantorID.trim().length()>0){
        strFlag = "read";
        msgs.add("�ͻ���Ϣ�Ѿ����ڣ�����Ҫ����ת�ƣ���ɾ��ԭ���ı�֤����Ϣ���ɣ�");
        instance.setReadonly(true);
        strClientNO=rs.getString("clientno");
      }
      instance.setValue("CLIENTNO", strClientNO);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    String GID = (String) ctx.getRequestAttribute("GID");
    String GIDTYPE = (String) ctx.getRequestAttribute("GIDTYPE");
    String GNAME = (String) ctx.getRequestAttribute("GNAME");
    String GCREDITCLASS = (String) ctx.getRequestAttribute("GCREDITCLASS");
    if (GID != null) {
      instance.setReadonly(false);
      instance.setValue("NAME", GNAME);
      instance.setValue("IDTYPE", GIDTYPE);
      instance.setValue("ID", GID);
      instance.setValue("CREDITCLASS", GCREDITCLASS);
      instance.setFieldReadonly("NAME", true);
      instance.setFieldReadonly("IDTYPE", true);
      instance.setFieldReadonly("ID", true);
      instance.setFieldReadonly("CREDITCLASS", true);
      this.isG = true;
    }

    String strUserName = null;
    String strMntbrhID = null;
    String strDate = SystemDate.getSystemDate5("");

    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
      USER_INFO_NAME);
    try {
      strUserName = um.getUserName();
      strMntbrhID = SCUser.getBrhId(strUserName);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    if (strRgnCode != null) {
      instance.setValue("RGNCODE", strRgnCode);
    }
    if (strClientMgr != null) {
      instance.setValue("CLIENTMGR", strClientMgr);
    }
    instance.setValue("CLIENTNO", "ϵͳ�Զ�����");
    instance.setValue("CURNO", "01");
    instance.setValue("APPDATE", strDate);
    if (strMntbrhID != null && strMntbrhID.trim().length() > 0 && !SCBranch.getBrhtype(strMntbrhID).trim().equals("9")) {
      instance.setValue("APPBRHID", strMntbrhID);
    }
    instance.setValue("MNTBRHID", strMntbrhID);
    instance.setValue("LASTMODIFIED", strDate);
    instance.setValue("OPERATOR", strUserName);

    if (this.initClientNo != null) {
      MyDB.getInstance().addDBConn(conn);
      InitClientData initdata = InitClientMan.getInitClient(initClientNo);
      MyDB.getInstance().releaseDBConn();

      if (initdata == null) {
        msgs.add("���пͻ���Ϣ�����ڣ���������!");
        return -1;
      }
      else {
        if (initdata.appBrhID != null) {
          instance.setValue("APPBRHID", initdata.appBrhID);
        }
        if (initdata.appDate != null) {
          instance.setValue("APPDATE", util.calToString(initdata.appDate, null));
        }
        if (initdata.clientType != null) {
          instance.setValue("CLIENTTYPE", initdata.clientType.intValue());
        }
        if (initdata.ecomDeptType != null) {
          instance.setValue("ECOMDEPTTYPE", initdata.ecomDeptType.intValue());
        }
        if (initdata.ecomType != null) {
          instance.setValue("ECOMTYPE", initdata.ecomType.intValue());
        }
        if (initdata.etpScopType != null) {
          instance.setValue("ETPSCOPTYPE", initdata.etpScopType.intValue());
        }
        if (initdata.gender != null) {
          instance.setValue("GENDER", initdata.gender.intValue());
        }
        if (initdata.ID != null) {
          instance.setValue("ID", initdata.ID);
        }
        if (initdata.IDType != null) {
          //instance.setValue("IDTYPE", initdata.IDType.intValue());
        }
        if (initdata.name != null) {
          instance.setValue("NAME", initdata.name);
        }
        if (initdata.sectorCat1 != null) {
          instance.setValue("SECTORCAT1", initdata.sectorCat1.intValue());
        }
      }
    }

    return 0;

  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String strUserName = null;
    strClientNO = String.valueOf(ClientNo.getNextSN());
    currClientNo = strClientNO;
    //String strDate=ServerTime.getDbCurrentDate();
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
      USER_INFO_NAME);
    try {
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    String strClientMgr = ctx.getParameter("CLIENTMGR").trim();
    String strWhere = " loginname='" + strClientMgr + "' and usertype<>'3'";
    String isnull = DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME", strWhere);
    if (isnull == null) {
      msgs.add("�ͻ��������ڣ���������Ŀͻ��Ƿ�׼ȷ��");
      return -1;
    }

    String strCurNO = ctx.getParameter("CURNO").trim();
    strWhere = "curno ='" + strCurNO + "'";
    isnull = DBUtil.getCellValue(conn, "SCCURRENCY", "CURNO", strWhere);
    if (isnull == null) {
      msgs.add("���Ҵ��벻���ڣ���������Ļ��Ҵ����Ƿ�׼ȷ��");
      return -1;
    }

    String strID = ctx.getParameter("ID").trim();
    strWhere = " id='" + strID + "'";
    isnull = DBUtil.getCellValue(conn, "CMCORPCLIENT", "ID", strWhere);
    if (isnull != null) {
      msgs.add("��֯���������Ѿ����ڣ�������֯���������׼ȷ�ԣ�");
      return -1;
    }

    strClientMgr = ctx.getParameter("CLIENTMGR").trim();
    String strMntBrhID = ctx.getParameter("MNTBRHID").trim();
    String strAppBrhID = ctx.getParameter("APPBRHID").trim();
//    if (! (SCBranch.checkSub(strAppBrhID, strMntBrhID) || strMntBrhID.equals(strAppBrhID))) {
//      msgs.add("ҵ�����㲻��ά�����㷶Χ�ڣ�");
//      return -1;
//    }

    if (SCBranch.getBrhtype(strAppBrhID).equals("9")) {
      msgs.add("ҵ�����㲻���������㣡");
      return -1;
    }

    //�����Ψһ���
    String strLoanCardNo = ctx.getParameter("LOANCARDNO").trim();
    if (DBUtil.getCellValue(conn, "CMCORPCLIENT", "CLIENTNO", "LOANCARDNO='" + strLoanCardNo + "'") != null) {
      msgs.add("������Ѿ����ڣ�����ϸ���������ĺ��룡");
      return -1;
    }

    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CLIENTNO", strClientNO);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", strUserName);
    return 0;
  }

  /***
   *
   */
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    if (this.isG) {
      return conn.executeUpdate("delete from cmguarantor where id='" + ctx.getParameter("ID") + "'");
    }



    if (this.initClientNo == null) {
      return 0;
    }

    MyDB.getInstance().addDBConn(conn);
    int ret = 0;
    try {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
      String OPERATOR = null;
      OPERATOR = um.getUserName();

      String clientno = null;

      if (this.initClientNo != null) {
        if (currClientNo == null) {
          msgs.add("�ͻ�����δ���֣����������ף�");
          return -1;
        }
        else {
          ret = ClientMigration.clientMrig(initClientNo, currClientNo, OPERATOR);
          if (ret >= 0) {
            //ret = CMClientMan.updateClientCatAndType(currClientNo, OPERATOR);
            ret = 0;
          }
          if (ret < 0) {
            String msg = PropertyManager.getProperty("" + ret);
            if (msg == null) {
              msg = "" + ret;
            }
            ctx.setRequestAtrribute("title", "�ͻ���Ϣ����");
            ctx.setRequestAtrribute("msg", "����ʧ��:" + msg);
            ctx.setRequestAtrribute("flag", "0");
            ctx.setTarget("/showinfo.jsp");
          }
          else {
            ctx.setRequestAtrribute("title", "�ͻ���Ϣ����");
            ctx.setRequestAtrribute("msg", "����ɹ���ɣ�");
            ctx.setRequestAtrribute("flag", "1");
            ctx.setTarget("/showinfo.jsp");
          }
        }
      }
    }
    catch (Exception e) {
      if (Debug.isDebugMode) {
        e.printStackTrace();
      }
      ret = -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
      return ret;
    }
  }

  /**
   *
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  /**
   *
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String strUserName = null;
    String strDate = SystemDate.getSystemDate2();
    String strIDType = null;
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String BrhId = SCUser.getBrhId(um.getUserName());
    try {
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    String strID = ctx.getParameter("ID").trim();
    strClientNO = ctx.getParameter("CLIENTNO").trim();
    String strWhere = " id='" + strID + "' and clientno!='" + strClientNO + "'";
    String isnull = DBUtil.getCellValue("CMCORPCLIENT", "ID", strWhere);
    if (isnull != null) {
      msgs.add("��ҵ����֤���Ѿ����ڣ�������ҵ����֤�ŵ�׼ȷ�ԣ�");
      return -1;
    }
    String strClientMgr = ctx.getParameter("CLIENTMGR").trim();
    strWhere = " loginname='" + strClientMgr + "' and usertype<>'3'";
    isnull = DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME", strWhere);
    if (isnull == null) {
      msgs.add("�ͻ��������ڣ���������Ŀͻ������Ƿ�׼ȷ��");
      return -1;
    }
    String strRgnCode = ctx.getParameter("RGNCODE");
    if (strRgnCode != null && strRgnCode.length() > 0) {
      strWhere = "rgncode='" + strRgnCode + "'";
      isnull = DBUtil.getCellValue(conn, "CMREGION", "RGNCODE", strWhere);
      if (isnull == null) {
        msgs.add("�������������ڣ�������������������Ƿ�׼ȷ��");
        return -1;
      }
    }

    String strCurNO = ctx.getParameter("CURNO").trim();
    strWhere = "curno ='" + strCurNO + "'";
    isnull = DBUtil.getCellValue(conn, "SCCURRENCY", "CURNO", strWhere);
    if (isnull == null) {
      msgs.add("���Ҵ��벻���ڣ���������Ļ��Ҵ����Ƿ�׼ȷ��");
      return -1;
    }

    String strMntBrhID = ctx.getParameter("MNTBRHID").trim();
    String strAppBrhID = ctx.getParameter("APPBRHID").trim();
//    if (! (SCBranch.checkSub(strAppBrhID, strMntBrhID) || strMntBrhID.equals(strAppBrhID))) {
//      msgs.add("ҵ�����㲻��ά�����㷶Χ�ڣ�");
//      return -1;
//    }

    if (SCBranch.getBrhtype(strAppBrhID).equals("9")) {
      msgs.add("ҵ�����㲻���������㣡");
      return -1;
    }

    //�����Ψһ���
    String strLoanCardNo = ctx.getParameter("LOANCARDNO").trim();
    if (DBUtil.getCellValue(conn, "CMCORPCLIENT", "CLIENTNO", "CLIENTNO<>'" + strClientNO + "' and LOANCARDNO='" + strLoanCardNo + "'") != null) {
      msgs.add("������Ѿ����ڣ�����ϸ���������ĺ��룡");
      return -1;
    }

    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "MNTBRHID", BrhId);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "LASTMODIFIED", strDate);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", strUserName);

    return 0;
  }

  /**
   *
   */
  public int postFindOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    String strMntbrhID = null;

    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
      USER_INFO_NAME);
    try {
      String strUserName = um.getUserName();
      strMntbrhID = SCUser.getBrhId(strUserName);
    }
    catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
    String strAppBrhID = result.getString("APPBRHID").trim();
    if (! (SCBranch.checkSub(strAppBrhID, strMntbrhID) || strMntbrhID.equals(strAppBrhID))) {
      msgs.add("ҵ�����㲻��ά�����㷶Χ�ڣ�");
      return -1;
    }

    return 0;
  }

  /**
   *
   */
  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    if (reffldnm.equals("APPBRHID") || reffldnm.equals("CLIENTMGR") || reffldnm.equals("UNIONNO")) {
      //BRHID���û����㣩
      String BRHID = SCUser.getBrhId(um.getUserName());
      if (BRHID == null || BRHID.length() < 1) {
        return -1;
      }
      //APPBRHIDs���û������µ�����ʵ���㣬�����Լ���
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("�������㲻���ڣ�");
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
    }
    return 0;
  }

  /***
   *
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    String CLIENTNO = this.strClientNO;
    if (CLIENTNO == null || CLIENTNO.length() < 1 || CLIENTNO.equals("ϵͳ�Զ�����")) {
      msgs.add("��ȷ����ҵ�ͻ�������Ϣ�Ѿ����ڣ�");
      return -1;
    }

    ctx.setRequestAtrribute("flag", strFlag);
    ctx.setRequestAtrribute("CLIENTNO", CLIENTNO);

    if (button.equals("CorpCapital")) {
      trigger(manager, "CMCAP1", null);
      return 0;
    }
    else if (button.equals("CorpManagement")) {
      trigger(manager, "CMMM01", null);
      return 0;
    }
    else if (button.equals("CorpContact")) {
      trigger(manager, "CMCT01", null);
      return 0;
    }
    else if (button.equals("CorpInvestment")) {
      trigger(manager, "CMINV1", null);
      return 0;
    }
    else if (button.equals("CorpBankAccount")) {
      trigger(manager, "CMBA01", null);
      return 0;
    }
    else if (button.equals("CorpReceivables")) {
      trigger(manager, "CMREC1", null);
      return 0;
    }
    else if (button.equals("CorpStock")) {
      trigger(manager, "CMSK01", null);
      return 0;
    }
    else if (button.equals("CorpLandHolding")) {
      trigger(manager, "CMLH01", null);
      return 0;
    }
    else if (button.equals("CorpEstate")) {
      trigger(manager, "CMESSL", null);
      return 0;
    }
    else if (button.equals("CorpConstructingProj")) {
      trigger(manager, "CMCPSL", null);
      return 0;
    }
    else if (button.equals("CorpEquipment")) {
      trigger(manager, "CMEQ01", null);
      return 0;
    }
    else if (button.equals("CorpPayables")) {
      trigger(manager, "CMCP01", null);
      return 0;
    }
    else if (button.equals("CorpAppdInfo")) {
      trigger(manager, "CMAI01", null);
      return 0;
    }
    else if (button.equals("CorpFinAssetDebt")) {
      trigger(manager, "CMREDL", null);
      return 0;
    }
    else if (button.equals("CorpProsct")) {
      trigger(manager, "CMPS01", null);
      return 0;
    }
    else if (button.equals("CorpNegInfo")) {
      trigger(manager, "CMBI01", null);
      return 0;
    }
    else if (button.equals("CorpRelaCorp")) {
      trigger(manager, "CMRC01", null);
      return 0;
    }
    else if (button.equals("BMRQL")) {
      trigger(manager, "BMRQL1", null);
      return 0;
    }
    else if (button.equals("CREDIT")) {
      RecordSet rs = conn.executeQuery("select id from CMCLIENT where clientno='" + CLIENTNO + "'");
      if (!rs.next()) {
        msgs.add("ȡ��idʧ�ܣ�");
        return -1;
      }
      String id = rs.getString("ID");

      ctx.setRequestAtrribute("ID", id);
      trigger(manager, "GUARANTORCREDITLIST", null);
      return 0;
    }
    else if (button.equals("ACPTBILL")) {
      ctx.setRequestAtrribute("flag", "read");
      ctx.setRequestAtrribute("CLIENTNO",this.strClientNO);
      trigger(manager, "BMABT1", null);
      return 0;
    }
    else if (button.equals("DISCBILL")) {
      ctx.setRequestAtrribute("flag", "read");
      ctx.setRequestAtrribute("CLIENTNO",this.strClientNO);
      trigger(manager, "BMTXT1", null);
      return 0;
    }
    else {
      return -1;
    }
  }

  public int preDelete(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {


    String cltNo = ctx.getParameter("CLIENTNO");

    String bmStr = "select * from bmtable where clientno='"+cltNo+"'";
    RecordSet bmRs = conn.executeQuery(bmStr);
    if(bmRs.next()){
      msgs.add("����ҵ��û��ɾ��");
      return -1;
    }

    RecordSet rs = conn.executeQuery("select id from CMCORPCLIENT where clientno='" +cltNo + "'");
    if (!rs.next()) {
      msgs.add("ȡ��idʧ�ܣ�");
      return -1;
    }
    else {
      String id = rs.getString("ID");
      String tempStr = "select * from bmguarantor where id='" + id + "'";
      //System.out.println(tempStr);
      RecordSet tempRs = conn.executeQuery(tempStr);
      if (tempRs.next()) {
        msgs.add("���е����Ĵ���,����ɾ����");
        return -1;
      }
    }





   String began = "delete from ";
   String end = " where clientno='"+cltNo+"'";

    String sqlTable[] = {"CMCORPAPPDINFO","CMCORPBANKACCOUNT","CMCORPCAPITAL","CMCORPCONSTRUCTINGPROJ","CMCORPCONTACT",
      "CMCORPEQUIPMENT","CMCORPESTATE","CMCORPINVESTMENT","CMCORPLANDHOLDING","CMCORPMANAGEMENT",
      "CMCORPNEGINFO","CMCORPPAYABLES","CMCORPPROSCT","CMCORPRECEIVABLES","CMCORPRELACORP",
      "CMCORPSTOCK"};
    for (int i = 0; i < sqlTable.length; i++) {
      int ret = conn.executeUpdate(began + sqlTable[i] + end);
      if (ret < 0) {
        return -1;
      }
    }
    return 0;
  }



}
