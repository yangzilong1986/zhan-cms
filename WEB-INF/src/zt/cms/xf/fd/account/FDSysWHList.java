package zt.cms.xf.fd.account;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 9:19:52
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.FormBean;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.utils.Debug;

import java.util.logging.Logger;

public class FDSysWHList extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.FDSysWHList");


       public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                       ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil) {
        if (super.preFind(ctx, conn, instance, msgs, manager, sqlWhereUtil) >= 0) {
            FormBean fb = instance.getFormBean();



//                    sqlWhereUtil.addWhereField(fb.getTbl(), "gthtb_dwbh", "GT", SqlWhereUtil.DataType_Sql, SqlWhereUtil.OperatorType_In);

//                sqlWhereUtil.addWhereField(fb.getTbl(), "gthtb_dwbh", "", SqlWhereUtil.DataType_Sql, SqlWhereUtil.OperatorType_In, SqlWhereUtil.RelationOperator_NONE);
                sqlWhereUtil.addWhereField(" ", " ", " gthtb_dwbh like 'aa' ", SqlWhereUtil.DataType_Sql, SqlWhereUtil.OperatorType_No, SqlWhereUtil.RelationOperator_NONE);
//                sqlWhereUtil.setWhereFld();
        }
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

/*
        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        ctx.setAttribute("BUTTONNAME", button);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            ctx.setAttribute("BUTTONNAME", ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME));
            trigger(manager, "FDCUTPAYDETLPAGE", null);
        }
*/

        //check
        if (button != null && (button.equals("CHECKBUTTON"))) {

            try {

                String sql = "select gthtb_htbh,gthtb_dwbh,xdkhzd_khmc,gthtb_qsrq,gthtb_dqrq,gthtb_htje,gthtb_tyckzh,gthtb_htnm " +
                        "from gthtb@haier_shengchan a,xdkhzd@haier_shengchan b w" +
                        "here a.gthtb_dwbh=b.xdkhzd_khbh " +
                        "and (a.gthtb_dwbh like 'GC%' or  a.gthtb_dwbh like 'GQ%' or  a.gthtb_dwbh like 'GSQ%' or  a.gthtb_dwbh like 'GT%' ) ";

                RecordSet rs = conn.executeQuery(sql);

                int count = 0;
                msgs.add("<br>检查结果如下：");
                while (rs.next()) {
                    String dwbh = rs.getString("gthtb_dwbh");
                    String actno = rs.getString("gthtb_tyckzh");
                    String htbh = rs.getString("gthtb_htbh");
                    String khmc = rs.getString("xdkhzd_khmc");

                    String promptStr = dwbh + " " + khmc + " " + htbh + " " + actno;
                    if (dwbh.startsWith("GC")) {
                        if (!"801000026701041001".equals(actno)) {
                            count++;
                            msgs.add("<br>" + count + ":" + promptStr);
                        }
                    } else if (dwbh.startsWith("GQ")) {
                        if (!"801000026701041001".equals(actno)) {
                            count++;
                            msgs.add("<br>" + count + ":" + promptStr);
                        }
                    } else if (dwbh.startsWith("GSQ")) {
                        if (!"801000026701041001".equals(actno)) {
                            count++;
                            msgs.add("<br>" + count + ":" + promptStr);
                        }
                    } else if (dwbh.startsWith("GT")) {
                        if ("GT001".equals(dwbh)) {
                            if (!"801000026701041001".equals(actno)) {
                                count++;
                                msgs.add("<br>" + count + ":" + promptStr);
                            }
                        } else {
                            if (!"801000026101041001".equals(actno)) {
                                count++;
                                msgs.add("<br>" + count + ":" + promptStr);
                            }
                        }
                    }
                }
                if (count == 0) {
                    msgs.add("<br>未发现帐号错误！");
                }
                msgs.add("<br>检查完成。");
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("帐号检查时出现问题，请咨询系统管理人员！");
                return 0;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }
        //设定帐号
        if (button != null && (button.equals("PROCESSBUTTON"))) {
            //rigger(manager, "FDSYSWHLIST", null);
            try {
                String sql = "update  gthtb@haier_shengchan a " +
                        " set a.gthtb_tyckzh = '' " +
                        "where a.gthtb_dwbh like 'GC%' and a.gthtb_tyckzh !='' " ;

                conn.setAuto(false);
                conn.begin();
                int rtn = conn.executeUpdate(sql);

                int count = 0;
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("帐号检查时出现问题，请咨询系统管理人员！");
                return 0;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }

        }

        return 0;
    }


}