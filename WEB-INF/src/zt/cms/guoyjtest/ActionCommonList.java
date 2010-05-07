/*
 * Created on 2004-10-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package zt.cms.guoyjtest;

import java.util.logging.Logger;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.config.FormBean;

/**
 * @author guoyj 
 * @Description ActionList的公共类，通过数据库配置，实现从list到page的无代码调用
 */
public class ActionCommonList extends FormActions {

	private Logger log = Logger.getLogger("zt.cms.guoyjtest.ActionCommonList");

	/*
	 * 按钮事件 页面按钮被点击时，触发不同的Form
	 */
	public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, String button, ErrorMessages msgs,
			EventManager manager) {

		FormBean bean = instance.getFormBean();
		String sDesc = bean.getDescription();
		ParseDesc parse = new ParseDesc();
		String sFormId; 
		try {
			parse.Parse(sDesc);
			sFormId = parse.getParaValue("formid");
		} catch (Exception e) {
			msgs.add(e.getMessage());	
			return -1;
		}
		
		trigger(manager, sFormId, null);//触发FORM：TESTPAGE
		return 0;
	}
}