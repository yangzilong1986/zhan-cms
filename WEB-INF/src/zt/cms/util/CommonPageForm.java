package zt.cms.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-10-10</p>
 * <p>Company: </p>
 * @author sunzg
 * @version 1.0
 */
import zt.cms.util.ParseDesc;
import zt.cms.cm.common.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.config.FormBean;
import java.util.logging.*;
import zt.cms.pub.code.*;



public class CommonPageForm extends FormActions {

  ParseDesc  parsedesc=new ParseDesc();
  private static Logger logger = Logger.getLogger("zt.cms.util.ParseDesc");
  public CommonPageForm() {}

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
    RightChecker.checkReadonly(ctx, conn, instance);

    /** below code
     *  提取存在表description中的字段名
     *  2004-10-11
     */
    FormBean bean = instance.getFormBean();
    parsedesc.setDesc(bean.getDescription()) ;
    String fieldname = parsedesc.getAttribute("field") ;
    logger.info("szg commoonpageform load():fieldname=="+fieldname) ;
    if (ctx.getParameter(fieldname) != null) {
      int temp = (int) Integer.parseInt(ctx.getParameter(fieldname));
      instance.setValue(fieldname, temp);

      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    else {
      trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE,
              Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    /** below code
     *  提取存在表description中的表名，和字段名
     *  2004-10-11
     */
    FormBean bean = instance.getFormBean();
    parsedesc.setDesc(bean.getDescription());
    String fieldname = parsedesc.getAttribute("field");
    String tablename=parsedesc.getAttribute("table") ;
    logger.info("szg commoonpageform preInsert():=="+fieldname +"  tablename=="+tablename) ;
    int seqno = (int) SerialNumber.getNextSN(tablename, fieldname);
    instance.setValue(fieldname, seqno);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), fieldname, seqno + "");
    return 0;
  }


}




