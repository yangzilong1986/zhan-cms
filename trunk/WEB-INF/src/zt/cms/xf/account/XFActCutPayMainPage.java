package zt.cms.xf.account;

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.user.UserManager;
import zt.platform.utils.Debug;
import zt.cmsi.mydb.MyDB;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.dao.XfactpaydetlDao;
import zt.cms.xf.common.dao.XfcontractDao;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.factory.XfactpaydetlDaoFactory;
import zt.cms.xf.common.factory.XfcontractDaoFactory;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.constant.XFContractStatus;
import zt.cms.xf.common.constant.XFCommon;
import zt.cms.xf.common.constant.XFPBStatus;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.Date;

public class XFActCutPayMainPage extends FormActions {

    private String superformid = null;
    private boolean isFHStatus = false;        //����״̬
    private String contractno;
    private String poano;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                    ErrorMessages msgs, EventManager manager, String parameter) {

        contractno = ctx.getParameter("CONTRACTNO");
        poano = ctx.getParameter("POANO");

        superformid = (String) ctx.getAttribute("SUPERFORMID");

        instance.setValue("CONTRACTNO", contractno);
        instance.setValue("POANO", poano);

        instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(6); //���ø��˰�ťΪ����
        instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(6); //���÷ſ�ƻ���ťΪ����

        if (superformid != null) {
            if (superformid.equals("XFACTCUTPAYMAINLISTODFH")) {
//                isFHStatus = true;
                instance.getFormBean().getElement("CONFIRMBUTTON").setComponetTp(15); //���ø��˰�ťΪϵͳ��ť
                instance.getFormBean().getElement("DISMISSALBUTTON").setComponetTp(15); //���ò��ذ�ťΪϵͳ��ť
                instance.setReadonly(true);
            }
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        String pbstatus = null;
        if (button == null) {
            return -1;
        }
        if (button.equals("CONFIRMBUTTON") || button.equals("DISMISSALBUTTON")) {
            if (button.equals("CONFIRMBUTTON")) {
                pbstatus = XFPBStatus.PBSTATUS_CHECKED;
            } else {
                pbstatus = XFPBStatus.PBSTATUS_DISMISSALED;
            }

            try {
                if (setPBStatus(ctx, conn, pbstatus) == 0) {
                    ctx.setRequestAtrribute("msg", "���ڴ��������ͨ����");
                } else {
                    ctx.setRequestAtrribute("msg", "���������ʵ������ڣ����ѯ��");
                }
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "���ڴ�����˳������⣬���ѯ��");
            }
            ctx.setRequestAtrribute("flag", "1");
            ctx.setRequestAtrribute("isback", "0");
            ctx.setTarget("/showinfo.jsp");
            instance.setReadonly(true);
        }
        return 0;
    }

    private int setPBStatus(SessionContext ctx, DatabaseConnection conn, String pbstatus) throws Exception {

        try {
            UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);

            MyDB.getInstance().addDBConn(conn);
            Connection sqlconn = conn.getConnection();

            XfactcutpaymainDao mainDao = XfactcutpaymainDaoFactory.create(sqlconn);
            XfactcutpaymainPk mainPk = new XfactcutpaymainPk(ctx.getParameter("CONTRACTNO"), BigDecimal.valueOf(Integer.parseInt(ctx.getParameter("POANO"))));

            Xfactcutpaymain xfactcutpaymain = mainDao.findByPrimaryKey(mainPk);

            if (xfactcutpaymain == null) {
                return -1;
            }

            xfactcutpaymain.setPbstatus(pbstatus);
            xfactcutpaymain.setCheckerid(um.getUserName());
            xfactcutpaymain.setCheckdate(new Date());

            //���ͨ������� �������ڱ�־
            if (pbstatus.equals(XFPBStatus.PBSTATUS_CHECKED)) {
                xfactcutpaymain.setOverduecd("1");
            }

            mainDao.update(mainPk, xfactcutpaymain);
            return 0;
        } catch (Exception e) {
            Debug.debug(e);
            throw new Exception(e);
        } finally {
            MyDB.getInstance().releaseDBConn();
        }
    }
}