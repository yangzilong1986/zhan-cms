package zt.cms.xf.contract;


/**
 * ���ڿۿ�ƻ� ������.
 * User: zhanrui
 * Date: 2009-3-25
 * Time: 17:30:25
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import zt.platform.form.util.SessionAttributes;

public class XFContractPayPage1 extends FormActions{


  public int load(SessionContext ctx, DatabaseConnection conn,  FormInstance instance,
                  ErrorMessages msgs,  EventManager manager, String parameter) {
      
            String contractno = ctx.getParameter("CONTRACTNO");
            String poano = ctx.getParameter("POANO");

            if (contractno != null) {
                //instance.setValue("CONTRACTNO", contractno);
      //����ת�Ƶ��༭״̬
                trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
            }
      return 0;
    }
}