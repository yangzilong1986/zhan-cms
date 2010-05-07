package zt.cms.bm.pd;

import zt.platform.form.control.*;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.config.*;
import zt.platform.db.DBUtil;
import java.sql.PreparedStatement;
import java.util.*;
import zt.platform.form.util.SqlWhereUtil;
import java.io.Serializable;
import java.sql.*;
import zt.cmsi.pub.*;

public class BMPDTransListAction extends FormActions {
    public BMPDTransListAction() {
    }

    String pdno="";

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance,ErrorMessages msgs,
        EventManager manager, String parameter) {
        pdno=(String)ctx.getRequestAttribute("PDNO");
    return 0;
}


    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, PreparedStatement ps, PreparedStatement countps) {
          try {
            ps.setString(1, pdno);
            countps.setString(1,pdno);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
        ErrorMessages msgs, EventManager manager) {
        Param param = new Param();
        param.addParam(ParamName.PK1Str, pdno);
        param.addParam(ParamName.PK2Str, ctx.getParameter("PDTRANSNO"));
        ctx.setRequestAtrribute(ParamName.ParamName,param);
        //ctx.setRequestAtrribute("PDNO",pdno);
        trigger(manager,"BMPDTRANSPAGE", null);
        return 0;
}



}
