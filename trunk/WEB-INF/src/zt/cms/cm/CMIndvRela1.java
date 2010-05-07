package zt.cms.cm;

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

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class CMIndvRela1 extends FormActions {
  private String CLIENTNO = null;
  private String SEQNO = null;
  public static Logger logger = Logger.getLogger("zt.cms.cm.CMIndvRela1");
  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //flag
    String flag = "read";
    if (ctx.getRequestAttribute("flag") != null) {
      flag = ctx.getRequestAttribute("flag").toString().trim();
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //CLIENTNO,SEQNO
    if (ctx.getRequestAttribute("CLIENTNO") != null) {
      CLIENTNO = ctx.getRequestAttribute("CLIENTNO").toString().trim();
    }
    if (ctx.getParameter("SEQNO") != null) {
      SEQNO = ctx.getParameter("SEQNO").toString();
      instance.setValue("CLIENTNO", CLIENTNO);
      instance.setValue("SEQNO", SEQNO);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      instance.setValue("CLIENTNO", CLIENTNO);
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    instance.setFieldReadonly("CLIENTNO", true);
    instance.setFieldReadonly("SEQNO", true);
    instance.setValue("CLIENTNO", CLIENTNO);
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    //IDΨһ���
    String IDNO = instance.getStringValue("IDNO");
    if (DBUtil.getCellValue(conn, "CMINDVRELA", "IDNO", "CLIENTNO='" + CLIENTNO + "' and IDNO='" + IDNO + "'") != null) {
      msgs.add("��ͥ��Ա֤�������ظ�������ϸ���������ĺ��룡");
      return -1;
    }
    if (DBUtil.getCellValue(conn, "CMINDVCLIENT", "ID", "CLIENTNO='" + CLIENTNO + "' and ID='" + IDNO + "'") != null) {
      msgs.add("��ͥ��Ա��ͻ������֤�������ظ�������ϸ���������ĺ��룡");
      return -1;
    }
    String SEQNO = zt.cms.pub.code.SerialNumber.getNextSN("CMINDVRELA", "SEQNO") + "";
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", SEQNO);
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    //IDΨһ���
    String IDNO = instance.getStringValue("IDNO");
    if (DBUtil.getCellValue(conn, "CMINDVRELA", "IDNO", "CLIENTNO='" + CLIENTNO + "' and IDNO='" + IDNO + "' and SEQNO<>" + SEQNO) != null) {
      msgs.add("��ͥ��Ա֤�������ظ�������ϸ���������ĺ��룡");
      return -1;
    }
    if (DBUtil.getCellValue(conn, "CMINDVCLIENT", "ID", "CLIENTNO='" + CLIENTNO + "' and ID='" + IDNO + "'") != null) {
      msgs.add("��ͥ��Ա��ͻ������֤�������ظ�������ϸ���������ĺ��룡");
      return -1;
    }
    return 0;
  }

  public void postField(SessionContext ctx, FormInstance instance, String fieldname,
          ErrorMessages msgs, EventManager manager) {

    String id = (String)ctx.getRequestAttribute("IDNO");
    if( id == null || id.trim().length() <= 0)
    {
      instance.setHTMLShowMessage("����","�����������֤����!");
      return;
    }

    id = id.trim();
    if(id.length() != 15 && id.length() != 18)
    {
      instance.setHTMLShowMessage("����","���֤���볤�ȱ���Ϊ15��18λ!");
      instance.setHTMLFieldValue("IDNO","");
      return;
    }

    if(id.length() == 15)
    {
      if(util.isDate("19"+id.substring(6,12)) == false)
      {
        instance.setHTMLShowMessage("����", "֤���������ڸ�ʽ�Ƿ�!");
        return;
      }
    }
    else
    {
        if(id.charAt(17) == 'x')
        {
          instance.setHTMLShowMessage("����", "֤�������У��λ������Сдx!");
          instance.setHTMLFieldValue("IDNO","");
          return;
        }

        if(util.isDate(id.substring(6,14)) == false)
        {
          instance.setHTMLShowMessage("����", "֤���������ڸ�ʽ�Ƿ�!");
          return;
        }

        int iS = 0,iY;
        int iW[]={7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String szVerCode="10X98765432";

        int i;
        for(i=0;i<17;i++)
        {
          iS += (int)(id.charAt(i)-'0') * iW[i];
        }
        iY = iS%11;
        if ( szVerCode.charAt(iY) != id.charAt(i) )
        {
          instance.setHTMLShowMessage("����","���֤����У�������!");
          return;
        }
    }

//    String year = null;
//    if(id.length() == 15)
//      year = "19" + id.substring(6,8);
//    else
//      year = id.substring(6,10);
//    int iYear;
//
//    try
//    {
//      iYear = Integer.parseInt(year);
//    }
//    catch(Exception e)
//    {
//      instance.setHTMLShowMessage("����","���֤�������ո�ʽ����!");
//      return;
//    }
//
//    if(iYear <= 1900)
//    {
//      instance.setHTMLShowMessage("����","���֤������������1900��!");
//      return;
//    }

    return;
  }

}
