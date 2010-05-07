package zt.cms.bm.ledger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.logging.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class RQDueIntrst
    extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.ledger.RQDueIntrst");
  private String flag = null; //�����Ƿ�ɶ�
  private String BMNo = null; //ҵ���
  private Param param = null; //������װ�������ڴ���
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
    //���մ����ҵ����ڴ���̨���в��Ҽ�¼,����ҵ�,������޸�״̬��������ʾ�û���Ϣ��¼�����ڡ�
    String formid = instance.getFormid();
    FormBean formBean = FormBeanManager.getForm(formid);
    String tblName = formBean.getTbl().trim();
    if (DBUtil.getCellValue(conn, tblName, "BMNO", "BMNO='" + BMNo + "'") != null) {
      instance.setValue("BMNO", BMNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      ctx.setRequestAtrribute("title", "������Ϣ��ѯ");
      ctx.setRequestAtrribute("msg", "��Ϣ��¼�����ڣ�");
      ctx.setRequestAtrribute("flag", "1");
      ctx.setTarget("/showinfo.jsp");
    }
    return 0;
  }

  /**
   * ��չ�����preEdit��������ʵ��ҵ����editǰ�Ĵ���
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "BMNO", BMNo);
    return 0;
  }

}
