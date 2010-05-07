//Source file: D:\\zt\\platform\\form\\control\\FormActions.java

package zt.platform.form.control;

import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.ElementBean;
import zt.platform.form.config.FormBean;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlWhereUtil;
import zt.platform.form.util.datatype.DataType;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Form���¼�������࣬Ӧ����Ա������չ�������������ݿ⶯��
 *
 * @author ���滻
 * @version 1.0
 * @created 2003��10��22��
 */
public class FormActions implements Serializable {

    public static final String QUERY_SQL_NAME = "ps";
    public static final String QUERY_COUNT_SQL_NAME = "countps";

    /**
     * @param ctx
     * @param conn
     * @param msgs
     * @param manager
     * @param parameter Description of the Parameter
     * @return int
     * @roseuid 3F72183B01DE
     */
    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                    EventManager manager, String parameter) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721D3503CA
     */
    public int unload(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                      EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721D4A017C
     */
    public int beforeInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param assistor
     * @return int
     * @roseuid 3F721DC40091
     */
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721DCE03D4
     */
    public int postInsertOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721DDD0386
     */
    public int postInsertFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                              EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param assistor
     * @return int
     * @roseuid 3F721DE4030E
     */
    public int beforeEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, SqlAssistor assistor) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param assistor
     * @return int
     * @roseuid 3F721E020267
     */
    public int preEdit(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E0D0371
     */
    public int postEditOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E1603CE
     */
    public int postEditFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param assistor
     * @return int
     * @roseuid 3F721E2C01F9
     */
    public int beforeDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager, SqlAssistor assistor) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param assistor
     * @return int
     * @roseuid 3F721E340146
     */
    public int preDelete(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                         EventManager manager, SqlAssistor assistor) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E3903C4
     */
    public int postDeleteOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E42020E
     */
    public int postDeleteFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                              EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E4800E1
     */
    public int beforeFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param ps
     * @param countps  Description of the Parameter
     * @return int
     * @roseuid 3F721E5200D1
     */
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, PreparedStatement ps, PreparedStatement countps) {
        return 0;
    }

    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlWhereUtil sqlWhereUtil) {
        try {
            FormBean fb = instance.getFormBean();
            List query = fb.getQueryField();
            if (query.size() <= 0) {
                query = fb.getSearchKeys();
            }
            String tblname = fb.getTbl();
            for (int i = 0; i < query.size(); i++) {
                ElementBean eb = (ElementBean) query.get(i);
                String fldname = eb.getName();
                String fldnamelarge = fldname + "largelarge";
                String value = ctx.getParameter(fldname);
                String valuelarge = ctx.getParameter(fldnamelarge);
                //if ( eb.isNeedEncode() ) {
                value = DBUtil.toDB(value);
                //}

                int datatype = eb.getDataType();
                int operatorType = SqlWhereUtil.OperatorType_Greater_Equals;
                if (datatype == DataType.DATE_TYPE) {
                    datatype = SqlWhereUtil.DataType_Date;
//                } else if (datatype == DataType.INTEGER_TYPE || datatype == DataType.DECIAML_TYPE ||
//                        datatype == DataType.ENUMERATION_TYPE) {
                } else if (datatype == DataType.INTEGER_TYPE || datatype == DataType.DECIAML_TYPE) {
                    datatype = SqlWhereUtil.DataType_Number;
                    if (valuelarge == null || valuelarge.trim().length() == 0)
                        operatorType = SqlWhereUtil.OperatorType_Equals;
                } else {
                    datatype = SqlWhereUtil.DataType_Character;
                    if (valuelarge == null || valuelarge.trim().length() == 0)
                        operatorType = SqlWhereUtil.OperatorType_Like;
                }
                if (value != null && value.trim().length() > 0)
                    sqlWhereUtil.addWhereField(tblname, fldname, value, datatype, operatorType, sqlWhereUtil.RelationOperator_And);

                if (valuelarge != null && valuelarge.trim().length() > 0) {
                    datatype = eb.getDataType();
                    operatorType = SqlWhereUtil.OperatorType_Lower_Equals;
                    if (datatype == DataType.DATE_TYPE) {
                        datatype = SqlWhereUtil.DataType_Date;
//                    } else if (datatype == DataType.INTEGER_TYPE || datatype == DataType.DECIAML_TYPE ||
//                            datatype == DataType.ENUMERATION_TYPE) {
                    } else if (datatype == DataType.INTEGER_TYPE || datatype == DataType.DECIAML_TYPE) {
                        datatype = SqlWhereUtil.DataType_Number;
                    } else {
                        datatype = SqlWhereUtil.DataType_Character;
                    }
                    sqlWhereUtil.addWhereField(tblname, fldname, valuelarge, datatype, operatorType, sqlWhereUtil.RelationOperator_And);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Description of the Method
     *
     * @param ctx      Description of the Parameter
     * @param conn     Description of the Parameter
     * @param instance Description of the Parameter
     * @param msgs     Description of the Parameter
     * @param manager  Description of the Parameter
     * @param assistor Description of the Parameter
     * @return Description of the Return Value
     */
    public int preFind(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param result
     * @return int
     * @roseuid 3F721E5603D0
     */
    public int postFindOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                          EventManager manager, RecordSet result) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E6500D8
     */
    public int postFindFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param reffldnm
     * @return int
     * @roseuid 3F7EC940015A
     */
    public int preReference(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                            EventManager manager, String reffldnm, SqlWhereUtil sqlWhereUtil) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param rs       Description of the Parameter
     * @return int
     * @roseuid 3F7EC950010D
     */
    public int postReferenceOk(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                               EventManager manager, String reffldnm, RecordSet rs) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param msgs
     * @param manager
     * @param rs       Description of the Parameter
     * @return int
     * @roseuid 3F7EC95C0255
     */
    public int postReferenceFail(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs,
                                 EventManager manager, String reffldnm, RecordSet rs) {
        return 0;
    }


    /**
     * @param ctx
     * @param conn
     * @param instance Description of the Parameter
     * @param button
     * @param msgs
     * @param manager
     * @return int
     * @roseuid 3F721E6C001A
     */
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {
        return 0;
    }

    public int insertSmallQueryEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                                     ErrorMessages msgs, EventManager manager) {
        return 0;
    }

    public int editSmallQueryEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                                   ErrorMessages msgs, EventManager manager) {
        return 0;
    }


    /**
     * ��������FORM�¼�����Ҫ�ǿ�FORM��
     * <p/>
     * Ҫ���ǣ�����ж��ʵ����Ȼ��ʵ���������FORM
     *
     * @param manager
     * @param formid
     * @param parameters
     */
    public final void trigger(EventManager manager, FormInstance fi, String formid, String parameters) {
        if (fi != null && fi.getInstanceid() != null) {
            manager.trigger(fi.getInstanceid(), EventType.UNLOAD_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
        }
        manager.trigger(formid, parameters);
    }

    /**
     * ��������FORM�¼�����Ҫ�ǿ�FORM��
     * <p/>
     * Ҫ���ǣ���ж��ʵ����ʵ���������FORM
     *
     * @param manager
     * @param formid
     * @param parameters
     */
    public final void trigger(EventManager manager, String formid, String parameters) {
        manager.trigger(formid, parameters);
    }


    /**
     * ����ʵ�����¼�
     *
     * @param manager
     * @param instance
     * @param type
     * @param branchType
     */
    public final void trigger(EventManager manager, FormInstance instance, int type, int branchType) {
        manager.trigger(instance.getInstanceid(), type, branchType, true);
    }

    /**
     * HTML�ؼ����ݱ��ı�
     *
     * @param ctx       SessionContext
     * @param instance  DatabaseConnection
     * @param fieldname FormInstance
     * @param msgs      ErrorMessages
     * @param manager   EventManager
     */
    public void postField(SessionContext ctx, FormInstance instance, String fieldname,
                          ErrorMessages msgs, EventManager manager) {
        return;
    }

    public void beforeField(SessionContext ctx, FormInstance instance, String fieldname,
                            ErrorMessages msgs, EventManager manager) {
        return;
    }


}
