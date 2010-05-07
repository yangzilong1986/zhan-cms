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

public class BMTypePage extends FormActions {
  public static Logger logger = Logger.getLogger("zt.cms.bm.app.BMTypePage");

  private String flag = null; //�����Ƿ�ɶ�
  private String typeNo = null; //ҵ�����ʹ���

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //�������ڴ�������͵����ݻ�һЩ��Ҫ���⴦������ݿ��ֶ�����
    typeNo = (String) ctx.getParameter("TYPENO");
    //����Ķ�д����
    System.out.println(typeNo);
    flag = (String) ctx.getRequestAttribute("flag");
    if (flag == null) {
      flag = "read";
    }
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    System.out.println(flag);
    //������Ϊ�������༭״̬
    if (typeNo != null) {
      //����instance������ֵ
      instance.setValue("TYPENO", typeNo);
      //����ת�Ƶ��༭״̬
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,Event.BRANCH_CONTINUE_TYPE);
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
