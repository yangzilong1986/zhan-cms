package zt.cms.bm.app;

import java.util.logging.*;

import zt.cmsi.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class BMRoutePage extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMRoutePage");

  private String flag = null; //�����Ƿ�ɶ�
  private String bmActType = null; //ҵ�����ʹ���

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //�������ڴ�������͵����ݻ�һЩ��Ҫ���⴦������ݿ��ֶ�����
    bmActType = (String) ctx.getParameter("BMACTTYPE");
    System.out.println(ctx.getRequestAttribute("BMACTTYPE") +"1*************************************");

    //����Ķ�д����
    flag = (String) ctx.getRequestAttribute("flag");
    System.out.println(ctx.getRequestAttribute("flag") +"1*************************************");
   if (flag == null) {
      flag = "read";
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //������Ϊ�������༭״̬
    if (bmActType != null) {
      //����instance������ֵ
      instance.setValue("BMACTTYPE", bmActType);
      //����ת�Ƶ��༭״̬
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,Event.BRANCH_CONTINUE_TYPE);
      System.out.println(bmActType+"2*************************************");
      return 0;
    }
    //����Ϊ�գ��������״̬
    else{
      //Ĭ��״̬trigger�����״̬
      return 0;
    }
  }

  /**
   * ��չ�����beforeEdit�������������ӡ���ť����Ӧ���¼�
   */
  public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  /**
   * ��չ�����preEdit��������ʵ��ҵ����editǰ�Ĵ���
   */
  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    return 0;
  }

}
