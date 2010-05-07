package zt.cms.bm.app;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

public class BMAppCriteriaPage extends FormActions {
  public static Logger logger = Logger.getLogger(
    "zt.cms.bm.app.BMRLPage");

  private String flag = null; //�����Ƿ�ɶ�
  private String BRHID = null; //ҵ������

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {
    //�������ڴ�������͵����ݻ�һЩ��Ҫ���⴦������ݿ��ֶ�����
    String typeno, checkpoint;
    BRHID = (String) ctx.getParameter("BRHID");
    typeno = (String) ctx.getParameter("TYPENO");
    checkpoint = (String) ctx.getParameter("CHECKPOINT");

    //������Ϊ�������༭״̬
    if (BRHID != null) {
      //����instance������ֵ
      instance.setValue("BRHID", BRHID);
      instance.setValue("TYPENO", typeno);
      instance.setValue("CHECKPOINT", checkpoint);
      //����ת�Ƶ��༭״̬
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }
  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

  public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                        EventManager manager) {
    zt.cmsi.pub.define.BMAppCriteria.refresh();
    return 0;
  }

}
