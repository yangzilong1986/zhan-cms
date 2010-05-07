package zt.cms.bm.review;

/**
 * <p>Title: �鿴��ʷ������¼</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 ������Ϣ</p>
 * <p>Company: ������Ϣ</p>
 * @author YUSG
 * @date   2004/01/05       created
 * @version 1.0
 */

import java.sql.*;

import zt.cmsi.biz.*;
import zt.cmsi.pub.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BmTransList extends FormActions {
  private String strFlag = null; //��д��־
  public Param params = null; //���͵ı�������
  private Param paramg = null; //��õı�������
  private String strBmNo = null; //ҵ���
  private String strBmTransNo = null; //ҵ����ϸ��
  private String strBmActType = null; //��������
  private String strBmType = null; //��������

  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    paramg = (Param) ctx.getRequestAttribute(ParamName.ParamName);
    if (paramg == null) {
      strFlag = (String) ctx.getParameter("flag");
      strBmNo = (String) ctx.getParameter("BMNO");
      if(strFlag == null || strBmNo == null)
      {
        msgs.add("û����ʷ������¼��");
        return -1;
      }
    }
    else
    {
      strFlag = (String) paramg.getParam(ParamName.Flag);
      strBmNo = (String) paramg.getParam(ParamName.BMNo);
    }

    if (strBmNo == null) {
      msgs.add("û����ʷ������¼");
      return -1;
    }
    else {
      return 0;
    }
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                     EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
      ps.setString(1, strBmNo);
      countps.setString(1, strBmNo);
      if (strFlag.equals(ParamName.Flag_WRITE)) {
        instance.setReadonly(false);
      }
      else {
        instance.setReadonly(true);
      }
      return 0;
    }
    catch (SQLException se) {
      return -1;
    }
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    String strBrhId = ctx.getParameter("OPERBRHID");
    strBmTransNo = ctx.getParameter("BMTRANSNO");
    if (strBmTransNo == null || strBmTransNo.length() < 1) {
      msgs.add("ҵ����ϸ����û�л�ã�");
      return -1;
    }
    BMTransData bmTransData = BMTrans.getBMTransData(strBmNo, Integer.parseInt(strBmTransNo));
    int intBmActType = bmTransData.actType;
    BMTableData bmTableData = BMTable.getBMTable(strBmNo);
    int intTypeNo = bmTableData.TypeNo;
    strBmType = String.valueOf(bmTableData.TypeNo);
    strBmActType = String.valueOf(intBmActType);
    params = new Param();
    params.addParam(ParamName.Flag, ParamName.Flag_READ);
    params.addParam(ParamName.BMNo, strBmNo);
    params.addParam(ParamName.BMTransNo, strBmTransNo);
    params.addParam(ParamName.BrhID, strBrhId);
    params.addParam(ParamName.BMActType, strBmActType);
    params.addParam(ParamName.BMType, strBmType);
    BMProg prog = BMRoute.getInstance().getActProg(intTypeNo, intBmActType, null); //��ó�����ת��url
    ctx.setRequestAtrribute(ParamName.ParamName, params);
    if (prog.isForm()) {
      ctx.setRequestAtrribute("fromList", "OK");
      trigger(manager, prog.getProgName(), null);
    }
    else {
      ctx.setTarget(prog.getProgName());
    }
    return 0;
  }

}
