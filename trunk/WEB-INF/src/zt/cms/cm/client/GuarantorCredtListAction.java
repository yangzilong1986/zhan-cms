package zt.cms.cm.client;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.config.*;
import zt.platform.db.DBUtil;
import java.sql.PreparedStatement;
import java.util.logging.*;

import zt.cms.cm.common.*;
import zt.cms.pub.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import java.sql.*;


public class GuarantorCredtListAction  extends FormActions {
    String id = "";
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                ErrorMessages msgs,
                EventManager manager, String parameter)
{
    this.id = (String)ctx.getRequestAttribute("ID");
    return 0;
}



    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
        EventManager manager, PreparedStatement ps, PreparedStatement countps) {
    try {
        ps.setString(1, this.id);
        countps.setString(1,this.id);
    }
    catch (SQLException ex) {
        ex.printStackTrace();
    }
    return 0;
}

}
