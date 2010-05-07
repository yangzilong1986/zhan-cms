package zt.cms.cm;


import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.*;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import zt.platform.form.util.event.EventType;
import zt.platform.form.util.event.Event;
import zt.cms.pub.code.SerialNumber;
import zt.platform.form.util.SqlAssistor;

/**
 *  Description of the Class
 *
 *@author     rock
 *@created    2004Äê3ÔÂ12ÈÕ
 */
public class QCorpManagePage extends FormActions {
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

        strClientNO = String.valueOf(ctx.getRequestAttribute("CLIENTNO"));
        strFlag = String.valueOf(ctx.getRequestAttribute("flag"));
        String strSeqNO = ctx.getParameter("SEQNO");

        if (strFlag.equals("write")) {
            instance.setReadonly(false);
        } else {
            instance.setReadonly(true);
        }

        if (strSeqNO != null) {
            instance.setValue("CLIENTNO", strClientNO);
            instance.setValue("SEQNO", Integer.parseInt(strSeqNO.trim()));

            trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }

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
     *@return           Description of the Return Value
     */
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager) {
        instance.setFieldReadonly("CLIENTNO", true);
        instance.setFieldReadonly("SEQNO", true);

        instance.setValue("CLIENTNO", strClientNO);

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
     *@param  assistor  Description of the Parameter
     *@return           Description of the Return Value
     */
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String idType = ctx.getParameter("IDTYPE");
        String id = ctx.getParameter("ID");
        if (hasDuplicateID(idType, id, conn)) {
            return -50001;
        }
        int iseqno = (int) SerialNumber.getNextSN("CMCORPMANAGEMENT", "SEQNO");
        String strSeqno = String.valueOf(iseqno);

        assistor.setSqlFieldValue(assistor.getDefaultTbl(), "SEQNO", strSeqno);

        if (strClientNO == null || iseqno < 0) {
            return -1;
        } else {
            return 0;
        }

    }


    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@param  assistor  Description of the Parameter
     *@return           Description of the Return Value
     */
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        String idType = ctx.getParameter("IDTYPE");
        String id = ctx.getParameter("ID");
        if (hasDuplicateID(idType, id, conn)) {
            msgs.clear();
            return -50001;
        }

        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  idType  Description of the Parameter
     *@param  id      Description of the Parameter
     *@param  conn    Description of the Parameter
     *@return         Description of the Return Value
     */
    public boolean hasDuplicateID(String idType, String id, DatabaseConnection conn) {
        RecordSet rs = conn.executeQuery("select * from CMCORPMANAGEMENT where id='" + id + "' and idtype=" + idType);
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

}
