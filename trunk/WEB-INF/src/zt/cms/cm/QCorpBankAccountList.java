package zt.cms.cm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import zt.platform.form.util.SessionAttributes;
import zt.cms.cm.common.ClientSum;

public class QCorpBankAccountList extends FormActions
{
    private String strClientNO=null;
    private String strFlag=null;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
        EventManager manager, String parameter)
    {
        strFlag=String.valueOf(ctx.getRequestAttribute("flag"));
        //strClientNO=ctx.getParameter("CLIENTNO");
        strClientNO=String.valueOf(ctx.getRequestAttribute("CLIENTNO"));
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, PreparedStatement ps, PreparedStatement countps)
    {

       String value = ClientSum.getClientSum(strClientNO,"CMCORPBANKACCOUNT","BAL",conn);
       ctx.setAfterBody("BAL="+value+"");
       try
        {
            countps.setString(1,strClientNO);
            ps.setString(1, strClientNO);
            if(strFlag.equals("write"))
               instance.setReadonly(false);
            else
                instance.setReadonly(true);
            return 0;
        }
        catch(SQLException se)
        {
            return -1;
        }
    }
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager)
    {
        ctx.setRequestAtrribute("CLIENTNO",strClientNO);
        ctx.setRequestAtrribute("flag",strFlag);

        if ( button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE) )
        {
            trigger(manager,"CMBA02",null);
        }
        if ( button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME) )
        {
            trigger(manager,"CMBA02",null);
        }

        return 0;
    }

}