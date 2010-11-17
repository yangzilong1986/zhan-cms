package zt.cms.xf;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.zt.util.PropertyManager;
import zt.cms.cm.common.RightChecker;
import zt.cms.util.Workflow;
import zt.cms.util.poiutil.IWriteOtherInfos;
import zt.cms.xf.contract.WriteExcelInfoForConfirm;
import zt.cmsi.pub.define.BMRouteBindNode;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.user.UserManager;

import javax.sql.rowset.CachedRowSet;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

public class XFConfirmPage10 extends FormActions {
	public static Logger logger = Logger.getLogger("zt.cms.xf.XFConfirmPage10");

	// private String flag = null; //�����Ƿ�ɶ�
	private String APPNO = null; // ���뵥��
	private String UMOP = null; // ҵ��Ա
	private int UMTP = 0; // ҵ��Ա����
	private String APPAMT = null; // ���ڽ��

	public int load(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, ErrorMessages msgs, EventManager manager,
			String parameter) {

		RightChecker.checkReadonly(ctx, conn, instance);

		UserManager um = (UserManager) ctx
				.getAttribute(SystemAttributeNames.USER_INFO_NAME);
		UMOP = um.getUserName();
		UMTP = um.getUser().getStatus();
		FormBean fb = instance.getFormBean();
		ElementBean ebx;

		ebx = fb.getElement("MODIPAY");
		ebx.setComponetTp(ComponentType.BUTTON_TYPE);
		ebx = fb.getElement("MODIRATE");
		ebx.setComponetTp(ComponentType.BUTTON_TYPE);
		ebx = fb.getElement("SPTOWORD");
		ebx.setComponetTp(ComponentType.BUTTON_TYPE);
		ebx = fb.getElement("GRADE");
		ebx.setComponetTp(ComponentType.BUTTON_TYPE);
		ebx = fb.getElement("SAVEBUTTON");
		ebx.setComponetTp(ComponentType.SYS_BUTTON);
		ebx = fb.getElement("BACKBUTTON");
		ebx.setComponetTp(ComponentType.SYS_BUTTON);

		// �������һ������(�����ύ)�������뵥���ݱ�Ϊֻ��
		if (!Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP),
				XFConf.APPSTATUS_TIJIAO)) {
			ebx = fb.getElement("MODIPAY");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
			ebx = fb.getElement("MODIRATE");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
			ebx = fb.getElement("SPTOWORD");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
			ebx = fb.getElement("SAVEBUTTON");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
			// instance.setReadonly(true);
		} else {
			ebx = fb.getElement("BACKBUTTON");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
		}

		if (instance.isReadonly()) {
			ebx = fb.getElement("SAVEBUTTON");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
			ebx = fb.getElement("BACKBUTTON");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
		}

		if (!Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP),
				XFConf.APPSTATUS_CHUSHEN3)
				&& UMTP <= 21) {
			ebx = fb.getElement("GRADE");
			ebx.setComponetTp(ComponentType.HIDDEN_TYPE);
		}

		// �������ڴ�������͵����ݻ�һЩ��Ҫ���⴦������ݿ��ֶ�����
		APPNO = ctx.getParameter("APPNO");
		APPAMT = ctx.getParameter("APPAMT");
		// ������Ϊ�������༭״̬
		if (APPNO != null) {
			// ����instance������ֵ
			instance.setValue("APPNO", APPNO);
			// ����ת�Ƶ��༭״̬
			trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE,
					Event.BRANCH_CONTINUE_TYPE);
		}
		return 0;
	}

	public int beforeEdit(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, ErrorMessages msgs, EventManager manager,
			SqlAssistor assistor) {
		return 0;
	}

	public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, ErrorMessages msgs, EventManager manager) {
		// zt.cmsi.pub.define.BMAppCriteria.refresh();
		return 0;
	}

	public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, ErrorMessages msgs, EventManager manager) {
		// zt.cmsi.pub.define.BMAppCriteria.refresh();
		return 0;
	}

	public int postEditOk(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, ErrorMessages msgs, EventManager manager) {
		// zt.cmsi.pub.define.BMAppCriteria.refresh();
		return 0;
	}

	public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
			FormInstance instance, String button, ErrorMessages msgs,
			EventManager manager) {

		if (APPNO == null || APPNO.length() < 1) {
			msgs.add("��ȷ�����뵥������Ϣ�Ѿ����ڣ�");
			return -1;
		}

		RightChecker.transReadOnly(ctx, conn, instance);
		ctx.setRequestAtrribute("APPNO", APPNO);

		if (button.equals("MODIRATE")) {// ��������
			trigger(manager, "XFCONFIRMPAGE11", null);
		}
		if (button.equals("MODIPAY")) {// �˶�������
			trigger(manager, "XFCONFIRMPAGE12", null);
		} else if (button.equals("MODCRINFO")) {// ������Ϣ
			if (!Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP),
					XFConf.APPSTATUS_TIJIAO)) {
				ctx.setRequestAtrribute("flag", "read");
			}
			trigger(manager, "XFCRINFO", null);
		} else if (button.equals("MODOPIN")) {// �������
			//20101116 ���Ŵ����� ����������Ϣ���

/*
            String str = "select * from XFCREDITINFO  where APPNO='" + APPNO
					+ "' ";
			if (!conn.isExist(str)) {
				ctx.setRequestAtrribute("msg", "��¼��ͻ�������Ϣ��");
				ctx.setRequestAtrribute("isback", "0");
				ctx.setTarget("/showinfo.jsp");
				return -1;
			}
*/

			ctx.setRequestAtrribute("APPAMT", APPAMT);
			trigger(manager, "XFCROPINIONLIST", null);
		} else if (button.equals("GRADE")) {// ��������
			if (!Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP),
					XFConf.APPSTATUS_CHUSHEN3)) {
				ctx.setRequestAtrribute("flag", "read");
			} else
				ctx.setRequestAtrribute("flag", "write");
			ctx.setTarget("/consume/appgrade.jsp?APPNO=" + APPNO);
		} else if (button.equals("SPTOWORD")) {// ������������word��
			 printMonthBill(ctx,instance,APPNO);
			/*String str = "select * from XFCREDITINFO  where APPNO='" + APPNO
					+ "' ";
			if (!conn.isExist(str)) {
				ctx.setRequestAtrribute("msg", "��¼��ͻ�������Ϣ��");
				ctx.setRequestAtrribute("isback", "0");
				ctx.setTarget("/showinfo.jsp");
				return -1;
			}
			ctx.setTarget("/consume/xfconfirm.jsp?APPNO=" + APPNO);*/
		} else if (button.equals("SAVEBUTTON")) {// �ύ
			String str;
			if (Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP),
					XFConf.APPSTATUS_TIJIAO)) {
				str = "select * from XFCREDITINFO where APPNO='" + APPNO + "' ";
				if (!conn.isExist(str)) {
					ctx.setRequestAtrribute("msg", "��¼��ͻ�������Ϣ��");
					ctx.setTarget("/showinfo.jsp");
					return -1;
				}
			} else if (Workflow.hasWriteRoleByUserTp(String.valueOf(UMTP),
					XFConf.APPSTATUS_CHUSHEN3)) {
				str = "select * from XFAPPGRADE  where APPNO='" + APPNO + "' ";
				if (!conn.isExist(str)) {
					ctx.setRequestAtrribute("msg", "��¼��ͻ����ۣ�");
					ctx.setTarget("/showinfo.jsp");
					return -1;
				}
			}

			str = "select a.APPSTATUS,b.OPINIONTP from XFAPP a,XFOPINION b "
					+ "where a.APPNO=b.APPNO and a.APPNO='" + APPNO
					+ "' and b.OPERATOR='" + UMOP + "'";
			RecordSet rs = conn.executeQuery(str);

			int OPINIONTP;
			if (rs.next()) {
				OPINIONTP = rs.getInt("OPINIONTP");
			} else {
				ctx.setRequestAtrribute("msg", "��¼�������");
				ctx.setTarget("/showinfo.jsp");
				return -1;
			}

			String appStaStr;
			if (OPINIONTP == 1) {
				BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String
						.valueOf(UMTP));
				appStaStr = Workflow
						.getNextStatusEndByUserTp(dt.status, APPAMT);// ���״̬Ϊ�գ��򷵻غ�ͬǩ��״̬��11����
			} else
				appStaStr = XFConf.APPSTATUS_BOHUI;// ���벵�� 99

			str = "update XFAPP set APPSTATUS=" + appStaStr + " where APPNO='"
					+ APPNO + "'";
			if (conn.executeUpdate(str) > 0) {
				ctx.setRequestAtrribute("msg", "�ѳɹ��ύ����һ��������");
				ctx.setRequestAtrribute("isback", "0");
				ctx.setTarget("/showinfo.jsp");
			} else {
				ctx.setRequestAtrribute("flag", "0");
				ctx.setRequestAtrribute("msg", "�ύʧ�ܣ�");
				ctx.setTarget("/showinfo.jsp");
				return -1;
			}
		} else if (button.equals("BACKBUTTON")) {// �˻�
			BMRouteBindNode dt = Workflow.getRouteBindByUserTp(String
					.valueOf(UMTP));
			String APPSTATUS = Workflow.getBeforeStatusBeginByUserTp(dt.status,
					APPAMT);// ���״̬Ϊ�գ��򷵻������ύ״̬��1����
			String[] sql = new String[2];
			sql[0] = "update XFAPP set APPSTATUS='" + APPSTATUS
					+ "' where appno='" + APPNO + "'";
			sql[1] = "delete from XFOPINION where appno='" + APPNO
					+ "' and operator='" + UMOP + "'";

			conn.begin();
			int[] temp = new int[2];
			temp[0] = conn.executeUpdate(sql[0]);
			temp[1] = conn.executeUpdate(sql[1]);

			if (temp[0] > 0 && temp[1] >= 0) {
				// if (conn.executeUpdate(sql[0])*conn.executeUpdate(sql[1]) >
				// 0) {
				conn.commit();
				ctx.setRequestAtrribute("msg", "�ѳɹ��˻ص���һ��������");
				ctx.setRequestAtrribute("isback", "0");
				ctx.setTarget("/showinfo.jsp");
			} else {
				conn.rollback();
				ctx.setRequestAtrribute("flag", "0");
				ctx.setRequestAtrribute("msg", "�˻�ʧ�ܣ�");
				ctx.setTarget("/showinfo.jsp");
				return -1;
			}

		}
		return 0;
	}

	// //////////////////////////////////////////////////
	private void printMonthBill(SessionContext ctx, FormInstance instance,
			String APPNO) {

		String sql = "select * from XFCREDITINFO  where APPNO='" + APPNO
		+ "' ";
		logger.info(sql);
		try {
			ConnectionManager cmanager = ConnectionManager.getInstance();
			CachedRowSet crs = cmanager.getRs(sql);

			if (crs.size() == 0) {
				instance.getFormBean().getElement("savebtn").setDescription(
						"newwin");
				ctx.setRequestAtrribute("msg", "û�з������������ݣ�");
				ctx.setRequestAtrribute("isback", "0");
				ctx.setRequestAtrribute("funcdel", "history.go(-1)");
				ctx.setTarget("/showinfo.jsp");
				// return -1;
				return;
			}

			String poiexceltemp = PropertyManager.getProperty("POI_EXCEl_PATH");
			HashMap<String, Object> excelMap = new HashMap<String, Object>();
			excelMap.put("filenm", poiexceltemp + "\\������-�ڲ�Ա��.xls");
			excelMap.put("cellname", "dataArea");

			IWriteOtherInfos writeinfo = new WriteExcelInfoForConfirm();
			// ���� IWriteOtherInfos �ӿڲ��� Ϊ��ǰ��ͬ��
			excelMap.put("APPNO", APPNO);
			excelMap.put("WRITEINFO", writeinfo);

			excelMap.put("crs", crs);
			excelMap.put("date", new SimpleDateFormat("yyyyMMdd")
					.format(new Date()));

			ctx.getRequest().setCharacterEncoding("iso8859-1");
			ctx.getRequest().getSession().setAttribute("excelMap", excelMap);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ctx.setTarget("/excelByTmp");

	}
	// //////////////////////////////////////////////////
}