/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: BMTableDyn.java,v 1.1 2005/06/28 07:00:23 jgo Exp $
 */
package zt.cms.bm.ledger;

import zt.cms.bm.bill.*;
import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  ���ԣ���������������,�޸ģ�ɾ��(���ֵ)
 *
 *@author     Administrator
 *@created    2003��12��2��
 */
public class BMTableDyn extends FormActions {

  /**
   *  Description of the Method
   *
   *@author            Administrator
   *@since             2004��1��5��
   *@param  ctx        Description of the Parameter
   *@param  conn       Description of the Parameter
   *@param  instance   Description of the Parameter
   *@param  msgs       Description of the Parameter
   *@param  manager    Description of the Parameter
   *@param  parameter  Description of the Parameter
   *@return            Description of the Return Value
   */

  String strFlag = null;
  private boolean firstRun = true;
  String strLoanType=null;       //ҵ������

  /**
   *  Description of the Method
   *
   *@param  ctx        Description of the Parameter
   *@param  conn       Description of the Parameter
   *@param  instance   Description of the Parameter
   *@param  msgs       Description of the Parameter
   *@param  manager    Description of the Parameter
   *@param  parameter  Description of the Parameter
   *@return            Description of the Return Value
   */
  public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    strFlag = ctx.getParameter("flag") == null ? null : ctx.getParameter("flag").trim();

    return 0;
  }

  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlWhereUtil sqlWhereUtil) {

    if (firstRun == true) {
      firstRun = false;
      return confitem.QUERY_FORM_INPUT_CRITERIA_CODE;
    }

    if (strFlag.equals("read") || strFlag == null) {
      instance.setReadonly(true);
    }
    else {
      instance.setReadonly(false);
    }

    if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
      String strinsql = null;
      try
      {
        strinsql = Function.getAllSubBrhIds(msgs, ctx);
      }
      catch(Exception e)
      {
        msgs.add("����ȡ�����������б�,�볢�����µ�½��");
        return -1;
      }
      sqlWhereUtil.addWhereField("BMTABLE", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);
    }
    return 0;
  }

  /**
   *  Description of the Method
   *
   *@param  ctx       Description of the Parameter
   *@param  conn      Description of the Parameter
   *@param  instance  Description of the Parameter
   *@param  button    Description of the Parameter
   *@param  msgs      Description of the Parameter
   *@param  manager   Description of the Parameter
   *@return           Description of the Return Value
   */
  public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                         String button,
                         ErrorMessages msgs, EventManager manager) {
    RecordSet rs=null;
    RecordSet rs2 = null;
    String strbmno = ctx.getParameter("BMNO").trim();
    String link = ctx.getParameter("link").trim();
    String strtypeno = ctx.getParameter("TYPENO").trim();
    String strStatus="7";//ctx.getParameter("STATUS").trim();
    Param pm = new Param();
    //strFlag
    if (strFlag != null && !strFlag.equals("")) {
      pm.addParam(ParamName.Flag, strFlag);
    }
    else {
      return -1;
    }
    //strbmno
    if (strbmno != null && !strbmno.equals("")) {
      pm.addParam(ParamName.BMNo, strbmno);
    }
    else {
      return -1;
    }

    String strSql="select typeno from bmtable where bmno='" + strbmno + "'";
    System.out.println("strSql:"+strSql);
    rs2=conn.executeQuery(strSql);
    try{
        if (rs2.next())
        {
            strLoanType = rs2.getString(0).trim();
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println("���ҵ������ʧ�ܣ�");
    }


    rs=conn.executeQuery("select bmstatus from bmtable where bmno='"+strbmno+"'");
    try
    {
        if(rs.next())
        {
            strStatus=rs.getString(0).trim();
        }
    }
    catch(Exception e)
    {
        System.out.println("���ҵ��״̬ʧ�ܣ�");
    }
    System.out.println("strStatus:"+strStatus);
    if(strStatus==null)
    {
        strStatus="0";
    }
    if ( (button != null) && (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)
                              || button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE))) {
      if (link.equals("1")) {
        ctx.setRequestAtrribute(ParamName.ParamName, pm);
        this.trigger(manager, "BMTRANSLIST", null);
      }
      if (link.equals("2")) {
        if (strtypeno != null && !strtypeno.trim().equals(""))
        {
            if(strStatus.equals("7") || strStatus.equals("8"))
            {
                ctx.setRequestAtrribute("BMNO", strbmno);
                if (strLoanType.equals("12")) {
                    ctx.setTarget("/jspreport/loanbizpo.jsp");
                }
                else if (strLoanType.trim().equals("17"))
                {
                   //�������������Ȩ����ӡ��
                   ctx.setRequestAtrribute("title", "��������������");
                   ctx.setRequestAtrribute("msg", "������������޽�ݣ�");
                   ctx.setRequestAtrribute("flag", "1");
                   ctx.setTarget("/showinfo.jsp");
                 }
                else {
                    ctx.setTarget("/jspreport/loanbiz.jsp");
                }

            }
            else
            {
                ctx.setRequestAtrribute("title","��ӡ���");
                ctx.setRequestAtrribute("msg","��ǰ״̬�޷���ӡ��ݣ�");
                ctx.setRequestAtrribute("flag","1");
                ctx.setRequestAtrribute("isback","0");
                ctx.setTarget("/showinfo.jsp");
            }
        }
        else {
          msgs.clear();
          msgs.add("���ʹ��벻����");
          return -1;
        }
      }
    }
    //
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {

    String strinsql = Function.getAllSubBrhIds(msgs, ctx);
    sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", strinsql, SqlWhereUtil.DataType_Sql, sqlWhereUtil.OperatorType_In);

    return 0;
  }

}
