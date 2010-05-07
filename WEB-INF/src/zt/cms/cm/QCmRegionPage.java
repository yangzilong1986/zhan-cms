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
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventType;

public class QCmRegionPage extends FormActions
{
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
        EventManager manager, String parameter)
    {
        String strRngCode=ctx.getParameter("RGNCODE");
        if ( strRngCode!=null)
        {
            instance.setValue("RGNCODE",strRngCode);
            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }

        return 0;
    }

}