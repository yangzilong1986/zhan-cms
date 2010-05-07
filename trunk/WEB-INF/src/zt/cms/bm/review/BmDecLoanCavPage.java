package zt.cms.bm.review;

/**
 * <p>Title: �Ŵ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003  ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSG
 * @date   2004/01/02     created
 * @version 1.0
 */
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

/****************************************************
 *
 * ���������������(��һ�����ڶ���������������)
 *
 ****************************************************/

public class BmDecLoanCavPage extends FormActions {
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
  private String strScbrh2 = null; //��������
  private String strIfrespLoan = null; //�Ƿ���������
  private String strFirstResp = null; //��һ������
  private String strFirstRespPct = null; //��һ�����˱���

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                  ErrorMessages msgs, EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      msgs.add("���ݲ������޷���������������");
      return -1;
    }

    strFlag = paramg.getParam(ParamName.Flag).toString();
    strBmNo = paramg.getParam(ParamName.BMNo).toString();
    strBmTransNo = paramg.getParam(ParamName.BMTransNo).toString();
    strBmActType = paramg.getParam(ParamName.BMActType).toString();
    strBmType = paramg.getParam(ParamName.BMType).toString();

    if (strBmNo == null || strBmTransNo == null || strBmActType == null) {
      msgs.add("���ݲ������޷���������������");
      return -1;
    }
    else {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
      try {
        strUserName = um.getUserName();
        strScbrh = SCUser.getBrhId(strUserName);
        if (strScbrh == null || strScbrh.length() < 1) {
          msgs.add("�������㲻���ڣ�");
          return -1;
        }
        else {
          strScbrh2 = "'" + strScbrh + "'";
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      int iCount = 0;
      String strSql = "select count(*) from bmdecision where bmno='" + strBmNo;
      strSql = strSql + "' and bmtransno=" + strBmTransNo;
      RecordSet rs = conn.executeQuery(strSql);
      if (rs.next()) {
        iCount = rs.getInt(0);
      }
      if (iCount > 0) { //���bmdecision�����������༭ģʽ
        instance.setValue("BMNO", strBmNo);
        instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
                Event.BRANCH_CONTINUE_TYPE);
      }

      if (strFlag.equals( (String) ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    String strCurNo = null; //���Ҵ���
    String strAMT = null; //���߾��߽��
    String strAMT2 = null; //���߾�����Ϣ

    String strDate = SystemDate.getSystemDate5(null);
    UpToDateApp utda = BMTable.getUpToDateApp(strBmNo);
    if (strBmActType.equals("2")) { //���Ϊ�������������õǼǵ���Ϣ
      strCurNo = utda.curNo; //��û��Ҵ���
      if (utda.appAmt == null) {
        strAMT = null;
      }
      else {
        strAMT = utda.appAmt.toString(); //��þ��߽��
      }
    }
    else { //���Ϊ�ڶ������õ���������Ϣ�����Ϊ��һ�����õڶ�������Ϣ
      strCurNo = utda.finalCurNo; //��û��Ҵ���
      strAMT=(utda.finalAmt==null?"":utda.finalAmt.toString());
      strAMT2=(utda.finalAmt2==null?"":utda.finalAmt2.toString());
      strIfrespLoan = utda.ifRespLoan.toString(); //����Ƿ��е�һ������
      if (utda.firstResp == null) {
        strFirstResp = "";
      }
      else {
        strFirstResp = utda.firstResp; //��õ�һ������
      }
      strFirstRespPct = (utda.firstRespPct == null ? "" : utda.firstRespPct.toString()); //��õ�һ�����˱���
      instance.setValue("IFRESPLOAN", Integer.parseInt(strIfrespLoan)); //��һҵ������Ƿ��е�һ������
      instance.setValue("FIRSTRESP", strFirstResp); //��һҵ����ĵ�һ������
      instance.setValue("FISRTRESPPCT", strFirstRespPct); //��һҵ����ĵ�һ�����˱���
    }

    instance.setValue("CURNO", strCurNo); //��һҵ����Ļ��Ҵ���
    instance.setValue("AMT", strAMT); //��һҵ����ľ��߽��
    instance.setValue("AMT2", strAMT2); //��һҵ����ľ��ߺ�����Ϣ
    instance.setValue("CREATEDATE", strDate);
    instance.setValue("OPERATOR", strUserName);
    instance.setValue("BMNO", strBmNo);
    instance.setValue("BMTRANSNO", Integer.parseInt(strBmTransNo));

    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
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

  public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                     ErrorMessages msgs,
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

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                          ErrorMessages msgs, EventManager manager) {
    try {
      int iflag = 0;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
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

        ctx.setRequestAtrribute("title", "��ծ�ʲ�����");
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

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                        ErrorMessages msgs,
                        EventManager manager) {
    System.out.println(strOthers);
    try {
      int iflag = 0;
      MyDB.getInstance().addDBConn(conn);
      if (strOthers.equals("1")) {
        iflag = BMTrans.compltTrans(strBmNo, Integer.parseInt(strBmTransNo), strUserName);
      }
      System.out.println(iflag);
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

        ctx.setRequestAtrribute("title", "��ծ�ʲ�����");
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

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
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
    else {
      return -1;
    }
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {
    String strSubScbrh = SCBranch.getSubBranchAll(strScbrh);
    if (strSubScbrh == null || strSubScbrh.length() < 1) {
      msgs.add("�������㲻���ڣ�");
      return -1;
    }
    else {
      strSubScbrh = "'" + strSubScbrh.replaceAll(",", "','") + "'";
    }
    if (reffldnm.equals("DECIDEDBY")) {
      sqlWhereUtil.addWhereField("SCUSER", "BRHID", strScbrh2,
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
}
