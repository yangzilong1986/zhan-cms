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

public class QCmRegionList extends FormActions
{
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
            ErrorMessages msgs, EventManager manager)
    {
        if ( button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME) )
        {
            trigger(manager,"CMRG02",null);
        }
        if ( button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE) )
        {
            trigger(manager,"CMRG02",null);
        }

        return 0;
    }

}