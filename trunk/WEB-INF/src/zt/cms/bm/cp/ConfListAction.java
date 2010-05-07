package zt.cms.bm.cp;

import zt.platform.form.control.FormActions;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.cms.bm.common.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ConfListAction extends FormActions
{
    public ConfListAction()
    {
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
        ErrorMessages msgs, EventManager manager) {
    RightChecker.transReadOnly(ctx,conn,instance);
    trigger(manager, "BMCPCONFPAGE", null);
    return 0;
}


}
