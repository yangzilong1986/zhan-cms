package zt.cms.xf.account;

import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import java.util.Date;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.event.*;
import zt.platform.utils.Debug;
import zt.cms.xf.common.dao.XfactcutpaymainDao;
import zt.cms.xf.common.factory.XfactcutpaymainDaoFactory;
import zt.cms.xf.common.dto.Xfactcutpaymain;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class XFActCutPayLink extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayLink");

    private String chargeoffdate;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        chargeoffdate = sFmt.format(new Date());


        instance.setValue("CHARGEOFFDATE", chargeoffdate);

        int num = countWaitChargeoffBills(msgs);

        if (num >= 0) {
            instance.setValue("CHARGEOFFNUM", num);
        }

        //trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_BREAK_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        chargeoffdate = ctx.getParameter("CHARGEOFFDATE");
        int count = 0;
        if (button.equals("NEXT")) {
            BillsManager bm = new BillsManager(chargeoffdate);
            try {
                if (bm.accountOpenedBills(conn) > 0) {
                    ctx.setRequestAtrribute("msg", "存在未处理完成的扣款记录，请查询！");
                    ctx.setRequestAtrribute("flag", "1");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setTarget("/showinfo.jsp");
                    instance.setReadonly(true);
                    return (-1);
                }

                if ((count = bm.generateBills(ctx, conn, msgs)) == 0) {
                    ctx.setRequestAtrribute("msg", "今日无待出帐扣款记录，请查询！");
                } else {
                    ctx.setRequestAtrribute("msg", "帐单生成成功，共生成 " + count + " 条新扣款记录，请查询！");
                }

                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            } catch (Exception e) {
                ctx.setRequestAtrribute("msg", "系统处理出现错误，请查询！");
                ctx.setRequestAtrribute("flag", "0");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
                return (-1);
            }

        }
        return 0;
    }


    private int countWaitChargeoffBills(ErrorMessages msgs) {

        try {

            XfactcutpaymainDao maindao = XfactcutpaymainDaoFactory.create();

            SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
            String strDate = sFmt.format(new Date());

            String sql = "precutpaycd = '0' and ((chargeoffcd = '0' and to_char(paybackdate, 'yyyyMMdd') <= " + strDate + ")  or " +
                    "(chargeoffcd = '1' and  closedcd = '0' and to_char(paybackdate, 'yyyyMMdd') <= " + strDate + ") or " +
                    "(chargeoffcd = '1' and  closedcd = '1' and  overduecd = '1' and  odb_chargeoffcd = '0' and to_char(odb_paybackdate, 'yyyyMMdd') <= " + strDate + ") or " +
                    "(chargeoffcd = '1' and  closedcd = '1' and  overduecd = '1' and  odb_chargeoffcd = '1' and  odb_closedcd = '0' and to_char(odb_paybackdate, 'yyyyMMdd') <=" + strDate + "))";

            Xfactcutpaymain[] xfactcutpaymain = maindao.findByDynamicWhere(sql, null);

            return xfactcutpaymain.length;

        } catch (Exception e) {
            msgs.add("发生异常：" + e.getMessage());
            Debug.debug(e);
            return -1;
        }
    }


}