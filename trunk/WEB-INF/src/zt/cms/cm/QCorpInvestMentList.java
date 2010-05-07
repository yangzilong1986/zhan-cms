package zt.cms.cm;



import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.SessionAttributes;
import zt.cms.cm.common.ClientSum;
/**
 *  Description of the Class
 *
 *@author     rock
 *@created    2004Äê3ÔÂ9ÈÕ
 */
public class QCorpInvestMentList extends FormActions {
    private String strClientNO = null;
    private String strFlag = null;


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
        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
        //strClientNO=ctx.getParameter("CLIENTNO");
        strClientNO = String.valueOf(ctx.getRequestAttribute("CLIENTNO"));

        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@param  ps        Description of the Parameter
     *@param  countps   Description of the Parameter
     *@return           Description of the Return Value
     */
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        String value = ClientSum.getClientSum(strClientNO, "CMCORPINVESTMENT", "CONTRACTAMT", conn);
        String value2 = ClientSum.getClientSum(strClientNO, "CMCORPINVESTMENT", "ACTUALAMT", conn);
        ctx.setAfterBody("CONTRACTAMT=" + value + ";ACTUALAMT=" + value2 + "");

        try {
            ps.setString(1, strClientNO);
            countps.setString(1, strClientNO);
            if (strFlag.equals("write")) {
                instance.setReadonly(false);
            } else {
                instance.setReadonly(true);
            }
            return 0;
        } catch (SQLException se) {
            return -1;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  button    Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@return           Description of the Return Value
     */
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager) {
        ctx.setRequestAtrribute("CLIENTNO", strClientNO);
        ctx.setRequestAtrribute("flag", strFlag);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            trigger(manager, "CMINV2", null);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {
            trigger(manager, "CMINV2", null);
        }
        return 0;
    }
}
