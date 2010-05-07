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
 * @Description ActionList�Ĺ����࣬ͨ�����ݿ����ã�ʵ�ִ�list��page���޴������
 */
public class ActionCommonList extends FormActions {

	private Logger log = Logger.getLogger("zt.cms.guoyjtest.ActionCommonList");

	/*
	 * ��ť�¼� ҳ�水ť�����ʱ��������ͬ��Form
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
		
		trigger(manager, sFormId, null);//����FORM��TESTPAGE
		return 0;
	}
}