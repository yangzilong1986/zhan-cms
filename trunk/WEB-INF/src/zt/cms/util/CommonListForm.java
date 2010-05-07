package zt.cms.util ;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 -10-10</p>
 * <p>Company: </p>
 * @author  sunzg
 * @version 1.0
 */

import java.util.logging.*;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.cms.cm.common.RightChecker;
import  zt.platform.form.config.FormBean;


public class CommonListForm  extends FormActions {
     /**
      *  Constructor for the ListAction object
      */
     private static Logger logger = Logger.getLogger("zt.cms.util.CommonListForm");
     public CommonListForm() { }



     public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
             EventManager manager, String parameter) {
         RightChecker.checkReadonly(ctx, conn, instance);
         trigger(manager, instance, EventType.FIND_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
         return 0;
     }


     public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
             ErrorMessages msgs, EventManager manager) {

//         RightChecker.checkReadonly(ctx, conn, instance);
         FormBean bean=instance.getFormBean() ;
         ParseDesc parsedesc=new ParseDesc();
         String formname=""  ;

/**
*   从表中取出description里的值
*   取出","号前字符
*    然后取出"="后的页面值
*/
         String   desc=bean.getDescription();
         if(desc!="" &&desc!=null)
         {
         parsedesc.setDesc(desc) ;
         formname=parsedesc.getAttribute("formname") ;
         }
         logger.info("szg  CommonListForm:  BEAN.description =="+formname);
         trigger(manager, formname, null);
         return 0;
     }
 }

