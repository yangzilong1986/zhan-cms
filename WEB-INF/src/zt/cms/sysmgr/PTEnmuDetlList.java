package zt.cms.sysmgr;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cmsi.pub.cenum.EnumValue;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class PTEnmuDetlList extends FormActions {

    private static Logger logger = Logger.getLogger("zt.cms.cm.PTEnmuDetlList");
    private String ENUID = null;
    private String flag = null;

    public int load(SessionContext ctx, DatabaseConnection conn,
                    FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {

        flag = ctx.getRequestAttribute("flag").toString().trim();
        //ENUID
        ENUID = ctx.getParameter("ENUID");
        if (ENUID == null) {
//            msgs.add("错误的参数：ENUID为空！");
//            return -1;
        } else ENUID = ENUID.toString().trim();
        //不采用传进来的flag，根据是否可以修改来决定flag
        String EDITALLOW = DBUtil.getCellValue(conn, "PTENUMINFOMAIN", "EDITALLOW", "ENUID='" + ENUID + "'");
        if (EDITALLOW != null && EDITALLOW.equals(EnumValue.YesNo_Yes + "")) {
            flag = "write";
        }
        if (flag.equals("write")) {
            instance.setReadonly(false);
        } else {
            instance.setReadonly(true);
        }
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps,
                       PreparedStatement countps) {
        try {
            ps.setString(1, ENUID);
            countps.setString(1, ENUID);
        }
        catch (Exception ex) {
            msgs.add(ex.getMessage());
            return -1;
        }
        return 2;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                           FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        ctx.setRequestAtrribute("ENUID", ENUID);
        ctx.setRequestAtrribute("flag", flag);
        trigger(manager, "ENMUDETLPAGE", null);

        return 0;
    }

}
