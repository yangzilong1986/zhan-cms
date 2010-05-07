//Source file: D:\\zt\\platform\\form\\control\\ControllerAssistor.java

package zt.platform.form.control;

import com.zt.util.PropertyManager;
import org.jdom.Document;
import org.jdom.Element;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.config.FormBean;
import zt.platform.form.util.*;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.Debug;
import zt.platform.utils.ErrorCode;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * FORM�¼�����
 *
 * @author ������
 * @version 1.0
 */
public class ControllerAssistor {
    private static Logger logger = Logger.getLogger("zt.platform.form.control.ControllerAssistor");
    private static int strrowcount = 0; //lj added in 20090324

    /**
     * 1�����FORM������
     * 2��ʵ����FORM
     * instanceid = instanceManager.instanceForm(event.getId())
     * ʧ���򽫴�����Ϣ����msgs�У�return�������
     * 3��ִ�ж����FormActionsʵ����load
     * 4���ɹ�����event��instanceid,����Ҫɾ����instance
     * <p/>
     * �����κ�һ��ʧ�ܶ�����С��0�Ĵ�����ţ��ɹ�����0
     */
    public static int load(SessionContext ctx, Event event, ErrorMessages msgs,
                           EventManager manager, DatabaseConnection conn) {
        //���FORM������
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        //2��ʵ����FormInstance
        String instanceid = fiManager.instanceForm(event.getId());
        if (instanceid == null) {
            msgs.add("" + ErrorCode.FORM_LOAD_FAIL);
            return ErrorCode.FORM_LOAD_FAIL;
        }
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
        //3��ʵ����FormActions
        FormBean fb = fi.getFormBean();
        FormActions fActions = fi.getFormAction();
        event.setInstanceid(instanceid);
        int result = 0;
        //4����������û��Զ���FormAction�����࣬�������������
        if (fActions != null) {
            result = fActions.load(ctx, conn, fi, msgs, manager, event.getParameters());
            if (result < 0) {
                //msgs.add("" + result);
                fiManager.removeInstance(instanceid);
            }
        }
        //5��ת�ƴ������̵�Ĭ�ϵ���һ������
        if (!manager.hasMoreEvent()) {
            //�б�Ͳ�ѯĬ�ϵ���ѯ�¼�
            if (fb.getType() == fb.LIST_TYPE || fb.getType() == fb.QUERY_TYPE) {
                manager.trigger(instanceid, EventType.FIND_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
            }
            //��������Ĭ�ϵ�����¼�
            else {
                manager.trigger(instanceid, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
            }
        }
        event.setResult(result);
        //reset list rowcount
        strrowcount = 0;//lj added in 20090324
        return result;
    }

    /**
     * 1�����FORMʵ��������
     * 2�����FORMʵ��
     * 3��ִ��formInstance��FormActions��unload()
     * 4��ж��ʵ��formInstance. removeInstance(instanceid)
     */
    public static int unload(SessionContext ctx, Event event, ErrorMessages msgs,
                             EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        FormInstance fi = fiManager.getFormInstance(event.getId());
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
        FormActions fActions = fi.getFormAction();
        if (fActions != null) {
            fActions.unload(ctx, conn, fi, msgs, manager);
        }
        fiManager.removeInstance(event.getId());
        return 0;
    }

    /**
     * 1�����FORM������
     * 2�����event.isInstance==false,���ȼ��ظ�FORM
     * 3����ȡ��FORMʵ��
     * 4����ʼ����ʵ����ֵ������initValues()
     * 5���õ���ʵ����FormActionsʵ����ִ����beforeInsert()����
     * <p/>
     * �����κ�һ��ʧ�ܶ�����С��0�Ĵ�����ţ��ɹ�����0
     */
    public static int insertview(SessionContext ctx, Event event,
                                 ErrorMessages msgs,
                                 EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);

        if (fi != null) {
            fi.initValues();
            FormActions actions = fi.getFormAction();
            int result = 0;
            if (actions != null) {
                result = actions.beforeInsert(ctx, conn, fi, msgs, manager);
                if (result < 0) {
                    msgs.add("" + result);
                }
            }
            event.setResult(result);
            return result;
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
    }

    /**
     * 1�����FORMʵ��������
     * 2�����FORMʵ��,���event.isInstance==false,���ȼ��ظ�FORM
     * 3������validate()����У������,ʧ�ܷ��ش������
     * 4������SqlAssistor,����ΪformInstance��1(����)
     * 5��ִ��formInstance��FormActions��preInsert()
     * 6��String sSql = assistor.toSql(inserttp)
     * 7��int rtn = conn.executeUpdate(sSql)
     * 8���ɹ�,��ִ��formInsatnce��FormActions��postInsertOk()
     * ʧ��,��ִ��formInsatnce��FormActions��postInsertFail()
     * 9�����ؽ��
     */
    public static int insert(SessionContext ctx, Event event, ErrorMessages msgs,
                             EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi != null) {
            if (validate(fi, msgs)) {
                SqlAssistor sqlAssistor = new SqlAssistor(fi,
                        SqlAssistor.PRIMARY_KEY_TYPE);
                FormActions actions = fi.getFormAction();
                int result = 0;
                if (actions != null) {
                    result = actions.preInsert(ctx, conn, fi, msgs, manager, sqlAssistor);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
                event.setBefore_result(result);
                if (result < 0) {
                    return result;
                }

                String sSql = sqlAssistor.toSql(SqlAssistor.INSERT_SQL_TYPE);
                result = conn.executeUpdate(sSql);

                if (result == 0) {
                    result = -1;
                    msgs.add("���Ӽ�¼ʧ��");
                }
                event.setResult(result);
                if (actions != null) {
                    if (result < 0) {
                        String errorMsg = conn.getErrorMsg();

                        if (errorMsg != null && errorMsg.trim().length() > 0) {
                            msgs.add(errorMsg);

                        }
                        result = actions.postInsertFail(ctx, conn, fi, msgs, manager);
                        if (result < 0) {
                            msgs.add("" + result);
                        }
                    } else {
                        result = actions.postInsertOk(ctx, conn, fi, msgs, manager);
                        if (result >= 0) {
                            msgs.add("�����Ϣ�ɹ�");
                        } else {
                            msgs.add("" + result);
                        }
                    }
                    event.setAfter_result(result);
                } else {
                    if (result >= 0) {
                        msgs.add("�����Ϣ�ɹ�");
                    } else {
                        String errorMsg = conn.getErrorMsg();
                        if (errorMsg != null && errorMsg.trim().length() > 0) {
                            msgs.add(errorMsg);
                        }
                    }
                }

                if (event.getResult() >= 0 && !manager.hasMoreEvent()) {
                    manager.trigger(instanceid, EventType.INSERT_VIEW_EVENT_TYPE,
                            Event.BRANCH_CONTINUE_TYPE, true);
                }

                return result;
            } else {
                event.setResult(ErrorCode.FORM_INSTANCE_VALUE_ERROR);
                return ErrorCode.FORM_INSTANCE_VALUE_ERROR;
            }
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
    }

    /**
     * 1�����FORMʵ��������
     * 2�����FORMʵ��
     * 3�����event.isInstance=false,����ظ�FORM
     * 4������SqlAssistor����ΪformInstance��1��������
     * 5��ִ��formInsatnce��FormActions��beforeEdit()
     * 6��String sSql = assistor.toSql(select)
     * 7�� ִ��conn.executeQuery(sSql)�����ҷ��������ļ�¼
     * 8��ȡ��һ�ʼ�¼������ʵ����ֵ
     */
    public static int editview(SessionContext ctx, Event event,
                               ErrorMessages msgs,
                               EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.
                        SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi != null) {
            SqlAssistor sqlAssistor = new SqlAssistor(fi,
                    SqlAssistor.PRIMARY_KEY_TYPE);
            FormActions actions = fi.getFormAction();
            int result = 0;
            if (actions != null) {
                result = actions.beforeEdit(ctx, conn, fi, msgs, manager, sqlAssistor);
                if (result < 0) {
                    msgs.add("" + result);
                }
            }
            event.setBefore_result(result);
            if (result < 0) {
                return result;
            }

            String sSql = sqlAssistor.toSql(SqlAssistor.SELECT_SQL_TYPE);
            RecordSet rs = conn.executeQuery(sSql, 1, 1);
            if (!rs.next()) {
                result = ErrorCode.RECORD_SET_IS_NULL;
                event.setResult(ErrorCode.RECORD_SET_IS_NULL);
            } else {
                event.setResult(ErrorCode.SUCCESS_NO);

                fi.updateValue(rs);
            }

            return result;
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
    }

    /**
     * 1�����FORMʵ��������
     * 2�����event.isInstance()==false,����ظ�FORM
     * 3�����FORMʵ��
     * 4��ִ��validate()У��ֵ
     * 5������SqlAssistor������formInstance��1(����)
     * 6��ִ��formInsatnce��FormActions��preEdit()
     * 7��String sSql = assistor.toSql(edit)
     * 8��int rtn = conn.executeUpdate(sSql)
     * 9���ɹ�ִ��formInsatnce��FormActions��postEditOk()
     * ʧ��ִ��formInsatnce��FormActions��postEditFail()
     */
    public static int edit(SessionContext ctx, Event event, ErrorMessages msgs,
                           EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi != null) {
            if (validate(fi, msgs)) {
                SqlAssistor sqlAssistor = new SqlAssistor(fi,
                        SqlAssistor.PRIMARY_KEY_TYPE);
                FormActions actions = fi.getFormAction();
                int result = 0;
                if (actions != null) {
                    result = actions.preEdit(ctx, conn, fi, msgs, manager, sqlAssistor);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
                event.setBefore_result(result);
                if (result < 0) {
                    return result;
                }

                String sSql = sqlAssistor.toSql(SqlAssistor.UPDATE_SQL_TYPE);
                result = conn.executeUpdate(sSql);
                event.setResult(result);
                if (result == 0) {
                    result = ErrorCode.NO_RECORD_EDITED;
                }
                event.setResult(result);

                if (actions != null) {
                    if (result < 0) {
                        String errorMsg = conn.getErrorMsg();
                        if (errorMsg != null && errorMsg.trim().length() > 0) {
                            msgs.add(errorMsg);

                        }
                        result = actions.postEditFail(ctx, conn, fi, msgs, manager);
                        if (result < 0) {
                            msgs.add("" + result);
                        }
                    } else {
                        result = actions.postEditOk(ctx, conn, fi, msgs, manager);
                        if (result >= 0) {
                            msgs.add("�޸���Ϣ�ɹ�");
                        } else {
                            msgs.add("" + result);
                        }
                    }
                    event.setAfter_result(result);
                } else {
                    if (result >= 0) {
                        msgs.add("�޸���Ϣ�ɹ�");
                    } else {
                        String errorMsg = conn.getErrorMsg();
                        if (errorMsg != null && errorMsg.trim().length() > 0) {
                            msgs.add(errorMsg);
                        }
                    }
                }
                if (event.getResult() >= 0 && !manager.hasMoreEvent()) {
                    manager.trigger(instanceid, EventType.INSERT_VIEW_EVENT_TYPE,
                            Event.BRANCH_CONTINUE_TYPE, true);

                }
                return result;
            } else {
                event.setResult(ErrorCode.FORM_INSTANCE_VALUE_ERROR);
                return ErrorCode.FORM_INSTANCE_VALUE_ERROR;
            }
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }

    }

    /**
     * 1�����FORMʵ��������
     * 2�����FORMʵ��
     * 3�����event.isInstance=false,����ظ�FORM
     * 4������SqlAssistor����ΪformInstance��1��������
     * 5��ִ��formInsatnce��FormActions��beforeDelete()
     * 6��String sSql = assistor.toSql(select)
     * 7�� ִ��conn.executeQuery(sSql)�����ҷ��������ļ�¼
     * 8��ȡ��һ�ʼ�¼������ʵ����ֵ
     */
    public static int deleteview(SessionContext ctx, Event event,
                                 ErrorMessages msgs,
                                 EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.
                        SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi != null) {
            SqlAssistor sqlAssistor = new SqlAssistor(fi,
                    SqlAssistor.PRIMARY_KEY_TYPE);
            FormActions actions = fi.getFormAction();
            int result = 0;
            if (actions != null) {
                result = actions.beforeDelete(ctx, conn, fi, msgs, manager, sqlAssistor);
                if (result < 0) {
                    msgs.add("" + result);
                }
            }
            event.setBefore_result(result);
            if (result < 0) {
                return result;
            }

            String sSql = sqlAssistor.toSql(SqlAssistor.SELECT_SQL_TYPE);
            RecordSet rs = conn.executeQuery(sSql, 1, 1);
            if (rs.next()) {
                event.setResult(ErrorCode.RECORD_SET_IS_NULL);
            } else {
                event.setResult(ErrorCode.SUCCESS_NO);
                fi.updateValue(rs);
            }

            return result;
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
    }

    /**
     * 1�����FORMʵ��������
     * 2�����event.isInstance()==false,����ظ�FORM
     * 3�����FORMʵ��
     * 4������SqlAssistor������formInstance��1(����)
     * 5��ִ��formInsatnce��FormActions��preDelete()
     * 6��String sSql = assistor.toSql(delete)
     * 7��int rtn = conn.executeUpdate(sSql)
     * 8���ɹ�ִ��formInsatnce��FormActions��postDeleteOk()
     * ʧ��ִ��formInsatnce��FormActions��postDeleteFail()
     */
    public static int delete(SessionContext ctx, Event event, ErrorMessages msgs,
                             EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi != null) {
            if (validate(fi, msgs)) {
                SqlAssistor sqlAssistor = new SqlAssistor(fi,
                        SqlAssistor.PRIMARY_KEY_TYPE);
                FormActions actions = fi.getFormAction();
                int result = 0;
                if (actions != null) {
                    result = actions.preDelete(ctx, conn, fi, msgs, manager, sqlAssistor);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
                event.setBefore_result(result);
                if (result < 0) {
                    return result;
                }

                String sSql = sqlAssistor.toSql(SqlAssistor.DELETE_SQL_TYPE);
                result = conn.executeUpdate(sSql);
                event.setResult(result);
                if (result == 0) {
                    result = ErrorCode.NO_RECORD_DELETED;
                }
                event.setResult(result);
                if (actions != null) {
                    if (result < 0) {
                        String errorMsg = conn.getErrorMsg();
                        if (errorMsg != null && errorMsg.trim().length() > 0) {
                            msgs.add(errorMsg);

                        }
                        result = actions.postDeleteFail(ctx, conn, fi, msgs, manager);
                        if (result < 0) {
                            msgs.add("" + result);
                        }
                    } else {
                        result = actions.postDeleteOk(ctx, conn, fi, msgs, manager);
                        if (result >= 0) {
                            msgs.add("ɾ����Ϣ�ɹ�");
                        } else {
                            msgs.add("" + result);
                        }
                    }
                    event.setAfter_result(result);
                } else {
                    if (result >= 0) {
                        msgs.add("ɾ����Ϣ�ɹ�");
                    } else {
                        String errorMsg = conn.getErrorMsg();
                        if (errorMsg != null && errorMsg.trim().length() > 0) {
                            msgs.add(errorMsg);
                        }
                    }
                }

                if (event.getResult() >= 0 && !manager.hasMoreEvent()) {
                    manager.trigger(instanceid, EventType.INSERT_VIEW_EVENT_TYPE,
                            Event.BRANCH_CONTINUE_TYPE, true);

                }
                return result;
            } else {
                event.setResult(ErrorCode.FORM_INSTANCE_VALUE_ERROR);
                return ErrorCode.FORM_INSTANCE_VALUE_ERROR;
            }
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }

    }

    /**
     * 1�����FORMʵ��������
     * 2�����event.isInstance==false,�����FORM
     * 3�����FORMʵ��
     * 4������ʵ��formInstance��initValue(������ʼ��FORMʵ����ֵ
     * 5��ִ��formInsatnce��FormActions��beforeFind()
     */
    public static int findview(SessionContext ctx, Event event,
                               ErrorMessages msgs, EventManager manager,
                               DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.
                        SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi != null) {
            fi.initValues();
            FormActions actions = fi.getFormAction();
            int result = 0;
            if (actions != null) {
                result = actions.beforeFind(ctx, conn, fi, msgs, manager);
                if (result < 0) {
                    msgs.add("" + result);
                }
            }
            event.setResult(result);

            return result;
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
    }

    /**
     * 1�����FORMʵ��������
     * 2�����event.isInstance==false,����ظ�FORM
     * 3�����FORMʵ��
     * 4��if ( List Form ) ��
     * 1��pageno   = ctx.getRequestParameter(REQUEST_LIST_PAGENO_NAME)�� ȱʡΪ1
     * 2��pagesize = ��FORM������ȡ��ʧ�����ϵͳ������ȡpagesize
     * 3��PreparedStatement ps = conn.getPS(form�����sql)
     * 4��ִ��formInsatnce��FormActions��preFind()
     * 5��ִ��ps�����������˿�ʼ��¼�ź�Ҫȡ�ļ�¼��
     * 6���ɹ�����ִ��postFindOk()
     * ʧ�ܣ���ִ��postFindFail()
     * 7�������������SessionContext��������REQUEST_FIND_RESULT_NAME
     * ,ע�����setRequestAttribute
     * 5��if ( Page Form ) ��
     * 1������SqlAssistor,����ΪformInstance��0����ѯ����
     * 2��String sSql = assistor.toSql(select)
     * 3��conn.executeQuery(sSql)
     * 4���ɹ�����ִ��formInstance��FormActions��postFindOk()
     * ʧ�ܣ���ִ��formInsatnce��FormActions��postFindFail()
     * 5����λ����һ����¼����ʵ����ֵ
     */
    public static int find(SessionContext ctx, Event event, ErrorMessages msgs,
                           EventManager manager, DatabaseConnection conn) {
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        int result = 0;
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
        FormBean fb = fi.getFormBean();
        //LIST_TYPE
        if (fb.getType() == fb.LIST_TYPE) {
            String sPageno = ctx.getParameter(SessionAttributes.REQUEST_LIST_PAGENO_NAME);
            String sPageCount = ctx.getParameter(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME);
            int rowcount = 0;
            int pageno = 0;
            int pagecount = 0;
            try {
                pageno = Integer.parseInt(sPageno);
            }
            catch (Exception e) {
                pageno = 0;
            }
            try {
                pagecount = Integer.parseInt(sPageCount);
            }
            catch (Exception e) {
                pagecount = 0;
            }
            int pagesize = fb.getRows();
            if (pagesize == 0) {
                pagesize = PropertyManager.getIntProperty(SessionAttributes.REQUEST_LIST_PAGESIZE_NAME);
            }
            String sSql = fb.getListsql();
            String countSql = fb.getCountsql();
            if (sSql == null || countSql == null) {
                msgs.add("" + ErrorCode.LIST_SQL_DEFINED_ERROR);
                event.setResult(ErrorCode.LIST_SQL_DEFINED_ERROR);
                return ErrorCode.LIST_SQL_DEFINED_ERROR;
            }
            PreparedStatement ps = conn.getPreparedStatement(sSql);
            PreparedStatement countps = conn.getPreparedStatement(countSql);
            FormActions fa = fi.getFormAction();
            if (fa != null) {
                result = fa.preFind(ctx, conn, fi, msgs, manager, ps, countps);
                if (result < 0) {
                    msgs.add("" + result);
                }
            }

            RecordSet rs1 = conn.executeQuery(countps);
//            if (rs1.next()) {
//                pagecount = rs1.getInt(0);
//                if (pagesize != 0) {
//                    pagecount = (pagecount - 1) / pagesize;
//                }
//            }

            if (rs1.next()) {//lj chenged in 20090323
                rowcount = rs1.getInt(0);
                if (rowcount != 0) {
                    pagecount = (rowcount - 1) / pagesize;
                    strrowcount = rowcount;
                }
            }
            event.setBefore_result(result);
            RecordSet rs = conn.executeQuery(ps, (pageno * pagesize) + 1, pagesize);
            if (rs.getRecordCount() <= 0) {
                event.setResult(ErrorCode.RECORD_SET_IS_NULL);
                if (fa != null) {
                    result = fa.postFindFail(ctx, conn, fi, msgs, manager);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
            } else {
                event.setResult(0);
                if (fa != null) {
                    result = fa.postFindOk(ctx, conn, fi, msgs, manager, rs);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
            }
            try {
                ps.close();
                countps.close();
            }
            catch (Exception e) {
                Debug.debug(e);
                //logger.severe(e.getMessage());
            }
            if (result >= 0) {
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_PAGENO_NAME, "" + pageno);
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME, "" + pagecount);
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_ROWCOUNT_NAME, "" + rowcount);//lj added in 20090323
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_FIND_RESULT_NAME, rs);
            }
            return result;
        }
        //QUERY_TYPE
        else if (fb.getType() == fb.QUERY_TYPE) {
            String sPageno = ctx.getParameter(SessionAttributes.REQUEST_LIST_PAGENO_NAME);
            String sPageCount = ctx.getParameter(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME);
            int rowcount = 0;
            int pageno = 0;
            int pagecount = 0;

            //200910 zhanrui
            double footertotal = 0.00;
            boolean isExistFootertotal = false;

            try {
                pageno = Integer.parseInt(sPageno);
            }
            catch (Exception e) {
                pageno = 0;
            }
            try {
                pagecount = Integer.parseInt(sPageCount);
            }
            catch (Exception e) {
                pagecount = 0;
            }
            int pagesize = fb.getRows();
            if (pagesize == 0) {
                pagesize = PropertyManager.getIntProperty(SessionAttributes.REQUEST_LIST_PAGESIZE_NAME);
            }
            String sSql = fb.getListsql();
            String countSql = fb.getCountsql();
            if (sSql == null || countSql == null) {
                msgs.add("" + ErrorCode.LIST_SQL_DEFINED_ERROR);
                event.setResult(ErrorCode.LIST_SQL_DEFINED_ERROR);
                return ErrorCode.LIST_SQL_DEFINED_ERROR;
            }
            FormActions fa = fi.getFormAction();
            boolean preFindHasError = false;
            if (fa != null) {
                try {
                    SqlWhereUtil sqlWhereUtil = new SqlWhereUtil();
                    result = fa.preFind(ctx, conn, fi, msgs, manager, sqlWhereUtil);
                    if (result < 0) {
                        preFindHasError = true;
                        msgs.add("" + result);
                    }
                    if (sSql != null && countSql != null) {
                        if (sSql.toLowerCase().indexOf(" where ") > 0) {
                            String tmp = sqlWhereUtil.toSqlWhere(false);
                            if (tmp != null && tmp.trim().length() > 0) {
                                if (sSql != null) {
                                    sSql = sSql.replaceAll("1=1", tmp);
                                }
                                if (countSql != null) {
                                    countSql = countSql.replaceAll("1=1", tmp);
                                }
                            }
                        } else {
                            String tmp = sqlWhereUtil.toSqlWhere(true);
                            if (tmp != null && tmp.trim().length() > 0) {
                                if (sSql != null) {
                                    sSql = sSql.replaceAll("1=1", tmp);
                                }
                                if (countSql != null) {
                                    countSql = countSql.replaceAll("1=1", tmp);
                                }
                            }
                        }
                    }
                    logger.info("listSql=" + sSql);
                    logger.info("countSql=" + countSql);

                    //zhanrui 20091110
                    fb.setCurrentListsql(sSql);
                    fb.setCurrentCountsql(countSql);

                }
                catch (Exception e) {
                    Debug.debug(e);
                    //e.printStackTrace();
                    fi.initValues();
                    msgs.add("δ��ò�ѯ�������������û����ٳ�ʱ�䣬�����µ�½��");
                    return -1;
                }
            }

            if (preFindHasError == true) {
                fi.initValues();
                return result;
            }

            PreparedStatement ps = conn.getPreparedStatement(sSql);
            /**
             * added by JGO on 20040804 to speed up performance
             */
            try {
                ps.setFetchSize(pagesize);
                ps.setMaxRows(pagesize);
            }
            catch (Exception e) {
                Debug.debug(e);
            }
            PreparedStatement countps = conn.getPreparedStatement(countSql);
            RecordSet rs1 = conn.executeQuery(countps);
            if (rs1.next()) {
                rowcount = rs1.getInt(0);
                if (rowcount != 0) {
                    pagecount = (rowcount - 1) / pagesize;
                    strrowcount = rowcount;//lj added in 20090324
                }

                /*
                200910 zhanrui  ��ʱ���ӽ��ͳ�ƹ���
                ֧����� countps �е� �ڶ�����ͳ�ƣ� ����select count(*), sum(GTHTJH_JHJE) from fdcutpaydetl
                */
                if (rs1.getfieldCount() > 1) {
                    footertotal = rs1.getDouble(1);
                    isExistFootertotal = true;
                }
            }
            event.setBefore_result(result);
            RecordSet rs = conn.executeQuery(ps, (pageno * pagesize) + 1, pagesize);
            if (rs.getRecordCount() <= 0) {
                if (fa != null) {
                    result = fa.postFindFail(ctx, conn, fi, msgs, manager);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
            } else {
                event.setResult(0);
                if (fa != null) {
                    result = fa.postFindOk(ctx, conn, fi, msgs, manager, rs);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
            }
            try {
                ps.close();
                countps.close();
            }
            catch (Exception e) {
                Debug.debug(e);
                //logger.severe(e.getMessage());
            }

            if (result >= 0) {
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_PAGENO_NAME, "" + pageno);
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME, "" + pagecount);
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_ROWCOUNT_NAME, "" + rowcount);//lj added in 20090323
                ctx.setRequestAtrribute(SessionAttributes.REQUEST_FIND_RESULT_NAME, rs);
            }
            fi.initValues();


            //200910 zhanrui
            if (isExistFootertotal) {
                ctx.setAfterSysButton("ҳ��:" + (pageno + 1) + "/" + (pagecount + 1) + " �ܱ���:" + rowcount + " �ܽ��:" + String.valueOf(footertotal));
            }else{
            ctx.setAfterSysButton("ҳ��:" + (pageno + 1) + "/" + (pagecount + 1) + " �ܱ���:" + rowcount);
            }
            return result;
        }
        //FORM_TYPE
        else {
            SqlAssistor sqlAssistor = new SqlAssistor(fi, SqlAssistor.SEARCH_KEY_TYPE);
            FormActions actions = fi.getFormAction();
            if (actions != null) {
                result = actions.preFind(ctx, conn, fi, msgs, manager, sqlAssistor);
                if (result < 0) {
                    msgs.add("" + result);
                }
            }
            event.setBefore_result(result);
            if (result < 0) {
                return result;
            }
            String sSql = sqlAssistor.toSql(SqlAssistor.SELECT_SQL_TYPE);
            RecordSet rs = conn.executeQuery(sSql, 1, 1);
            if (rs.next()) {
                if (actions != null) {
                    result = actions.postFindOk(ctx, conn, fi, msgs, manager, rs);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                    event.setAfter_result(result);
                }
                event.setResult(ErrorCode.SUCCESS_NO);
                if (result >= 0) {
                    fi.updateValue(rs);
                }
            } else {
                String errorMsg = conn.getErrorMsg();
                if (errorMsg != null && errorMsg.trim().length() > 0) {
                    msgs.add(errorMsg);
                }
                if (actions != null) {
                    result = actions.postFindFail(ctx, conn, fi, msgs, manager);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                    event.setAfter_result(result);
                }
                msgs.add("��Ϣ������,�����²���");
                fi.initValues();
                event.setResult(ErrorCode.RECORD_SET_IS_NULL);
                if (!manager.hasMoreEvent()) {
                    manager.trigger(instanceid, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
                }
            }
            return result;
        }
    }

    /**
     * ����ť�����¼�
     * <p/>
     * ���˼�룺
     * �����ĳ����ť���ύ������
     * Լ��������ҳ�����������ǩ
     * <input type="hidden" name="Plat_Form_Request_Button_Event" value=""/>
     * �����ĳ��ťʱ,��buttonԪ�ص�ֵ��Ϊ��Ӧ�ı�ǩ���ơ�
     * ȱʡ�¼���Plat_Form_Request_Button_Delete,��ɾ�����еļ�¼
     * checkbox������Ϊrecordnos,���ǵ�ֵ����searchKey��ɵĴ�
     * <p/>
     * ����
     * 1�����FORMʵ��������
     * 2�����event.isInstance==false����ظ�FORM
     * 3�����FORMʵ��
     * 4��button= ctx.getAttribute(REQUEST_BUTTON_EVENT_NAME)
     * 5��if ( button.equals(RQUEST_BUTTON_DELETE_NAME) ) {
     * ɾ����¼��ȡrecordnos��ֵ�飩
     * Ȼ�󴥷���ǰʵ����Find�¼�
     * return
     * } else {
     * ִ��formInsatnce��FormActions��buttonEvent()
     * }
     */
    public static int button(SessionContext ctx, Event event, ErrorMessages msgs,
                             EventManager manager, DatabaseConnection conn) {
        //���FORM������
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        //2��ʵ����FormInstance
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
        //3����ť�¼�����
        String button = ctx.getParameter(SessionAttributes.REQUEST_BUTTON_EVENT_NAME);
        if (button == null || button.trim().length() < 1) {
            msgs.add("" + ErrorCode.BUTTON_EVENT_ERROR);
            event.setResult(ErrorCode.BUTTON_EVENT_ERROR);
            return ErrorCode.BUTTON_EVENT_ERROR;
        }
        int result = 0;
        FormBean fb = fi.getFormBean();
        String tbl = fb.getTbl();
        //3.1����ɾ������ť�Ĵ�������
        if (button.equals(SessionAttributes.REQUEST_BUTTON_DELETE_NAME)) {
            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            List results = new ArrayList();
            if (recordnos != null) {
                for (int i = 0; i < recordnos.length; i++) {
                    try {
                        if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
                            String sSql = "delete from " + tbl + " where " + recordnos[i];
                            int thisresult = conn.executeUpdate(sSql);
                            if (thisresult <= 0) {
                                results.add("" + i);
                                msgs.add("" + thisresult);
                            }
                        }
                    }
                    catch (Exception e) {
                        msgs.add(e.getMessage());
                        results.add("" + i);
                        e.printStackTrace();
                    }
                }
                if (results.size() > 0) {
                    String err = "��";
                    for (int i = 0; i < results.size(); i++) {
                        err += results.get(i);
                        if (results.size() < results.size() - 1) {
                            err += ",";
                        } else {
                            err += "��¼ɾ��ʧ��";
                        }
                    }
                    msgs.add(err);
                }
            }
            manager.trigger(instanceid, EventType.FIND_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
            return 0;
        }
        //3.2��������ť�Ĵ�������
        else {
            if (fi != null) {
                FormActions fa = fi.getFormAction();
                if (fa != null) {
                    if (button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME))                                 //lj added in 20090323
                        ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_ROWCOUNT_NAME, "" + strrowcount);//lj added in 20090323
                    result = fa.buttonEvent(ctx, conn, fi, button, msgs, manager);
                    if (result < 0) {
                        msgs.add("" + result);
                    }
                }
                if (!manager.hasMoreEvent()) {
                    if (fb.getType() == fb.LIST_TYPE) {
                        manager.trigger(instanceid, EventType.FIND_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
                    } else {
                        manager.trigger(instanceid, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, true);
                    }
                }
            }
            return result;
        }
    }

    public static int postField(SessionContext ctx, Event event, ErrorMessages msgs,
                                EventManager manager) {
        //���FORM������
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        //2��ʵ����FormInstance
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
        //3����ť�¼�����
        String button = ctx.getParameter(SessionAttributes.REQUEST_BUTTON_EVENT_NAME);
        if (button == null || button.trim().length() < 1) {
            msgs.add("" + ErrorCode.BUTTON_EVENT_ERROR);
            event.setResult(ErrorCode.BUTTON_EVENT_ERROR);
            return ErrorCode.BUTTON_EVENT_ERROR;
        }
        int result = 0;
        //FormBean fb = fi.getFormBean();

        if (true) {
            if (fi != null) {
                FormActions fa = fi.getFormAction();
                if (fa != null) {
                    ControllerAssistor.updateParameters(fi, ctx);
                    fa.postField(ctx, fi, button, msgs, manager);
                }
            }
        }
        return result;
    }

    public static int beforeField(SessionContext ctx, Event event, ErrorMessages msgs,
                                  EventManager manager) {
        //���FORM������
        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
        //2��ʵ����FormInstance
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        if (fi == null) {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
        //3����ť�¼�����
        String button = ctx.getParameter(SessionAttributes.REQUEST_BUTTON_EVENT_NAME);
        if (button == null || button.trim().length() < 1) {
            msgs.add("" + ErrorCode.BUTTON_EVENT_ERROR);
            event.setResult(ErrorCode.BUTTON_EVENT_ERROR);
            return ErrorCode.BUTTON_EVENT_ERROR;
        }
        int result = 0;
        //FormBean fb = fi.getFormBean();

        if (true) {
            if (fi != null) {
                FormActions fa = fi.getFormAction();
                if (fa != null) {
                    ControllerAssistor.updateParameters(fi, ctx);
                    fa.beforeField(ctx, fi, button, msgs, manager);
                }
            }
        }
        return result;
    }

    /**
     * �ο����ı���ѯ
     * <p/>
     * 1�����FORMʵ��������
     * 2�����event.isInstance()==false,����ظ�FORM
     * 3�����FORMʵ��
     * 4����ȡ�ο��ֶε�����ctx.getParameter(SessionAttributes.REQUEST_REFERENCE_FIELD_
     * NAME)
     * 5�����ݶ������SQL��䣬����PreparedStatement ps
     * 5��ִ��formInsatnce��FormActions��preDelete()
     * 6��ִ��ps,�õ���ѯ���,�����ctx�У�setRequestAttribute(SessionAttributes.REQUES
     * T_REFERENCE_RESULT_NAME)
     * 7���ɹ�ִ��formInsatnce��FormActions��postDeleteOk()
     * ʧ��ִ��formInsatnce��FormActions��postDeleteFail()
     */
    public static int reference(SessionContext ctx, Event event,
                                ErrorMessages msgs, EventManager manager,
                                DatabaseConnection conn) {

        boolean preFindHasError = false;

        FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                SessionAttributes.
                        SESSION_FORM_INSTANCE_MANAGER_NAME);
        String instanceid = event.getId();
        FormInstance fi = fiManager.getFormInstance(instanceid);
        int result = 0;
        if (fi != null) {
            String reffldnm = ctx.getParameter(SessionAttributes.
                    REQUEST_REFERENCE_FIELD_NAME);
            //�����QueryForm�Ĺ��˲ο����ֶΣ���ص�largelarge(��QueryGenerator����ӵ�)
            if (reffldnm != null && reffldnm.endsWith("largelarge")) {
                reffldnm = reffldnm.substring(0,
                        reffldnm.length() - "largelarge".length());
            }
            FormBean fb = fi.getFormBean();
            if (fb == null) {
                msgs.add("" + ErrorCode.REF_GET_FORMBEAN_ERROR);
                event.setResult(ErrorCode.REF_GET_FORMBEAN_ERROR);
                return ErrorCode.REF_GET_FORMBEAN_ERROR;
            }

            String sSql = fi.getRefSQL(ctx, reffldnm);
            String sSql1 = fi.getRefCountSQL(ctx, reffldnm);

            if (sSql == null || sSql1 == null) {
                msgs.add("" + ErrorCode.REF_DEFINED_ERROR);
                event.setResult(ErrorCode.REF_DEFINED_ERROR);
                return ErrorCode.REF_DEFINED_ERROR;
            }

            String sPageno = ctx.getParameter(SessionAttributes.
                    REQUEST_LIST_PAGENO_NAME);
            String sPageCount = ctx.getParameter(SessionAttributes.
                    REQUEST_LIST_PAGECOUNT_NAME);
            int pageno = 0;
            int pagecount = 0;
            try {
                pageno = Integer.parseInt(sPageno);
            }
            catch (Exception e) {
                pageno = 0;
            }
            try {
                pagecount = Integer.parseInt(sPageCount);
            }
            catch (Exception e) {
                pagecount = 0;
            }
            //int pagesize = fb.getRows();
            int pagesize = 10;
            if (pagesize == 0) {
                pagesize = PropertyManager.getIntProperty(SessionAttributes.
                        REQUEST_LIST_PAGESIZE_NAME);

            }
            FormActions fa = fi.getFormAction();

            if (fa != null) {
                try {
                    SqlWhereUtil sqlWhereUtil = new SqlWhereUtil();
                    result = fa.preReference(ctx, conn, fi, msgs, manager, reffldnm,
                            sqlWhereUtil);

                    /**
                     * ����ӿڷ��ش����򲻲����κ����ݣ���ʾ������Ϣ
                     * ��Ϊ�˼ӿ��ٶ�
                     * JGO 20040906
                     */
                    if (result < 0) {
                        preFindHasError = true;
                        msgs.add("" + result);
                    }

                    if (sSql != null && sSql1 != null) {
                        if (sSql.toLowerCase().indexOf(" where ") > 0) {
                            String tmp = sqlWhereUtil.toSqlWhere(false);
                            if (tmp != null && tmp.trim().length() > 0) {
                                sSql += " and ( " + tmp + " ) ";
                                sSql1 += " and ( " + tmp + " ) ";
                            }
                        } else {
                            String tmp = sqlWhereUtil.toSqlWhere(true);
                            if (tmp != null && tmp.trim().length() > 0) {
                                sSql += tmp;
                                sSql1 += tmp;
                            }
                        }
                    }
                }
                catch (Exception e) {
                    //e.printStackTrace();
                    Debug.debug(e);
                    msgs.add("δ��ò�ѯ�������������û���½��ʱ�������µ�½��");
                    return -1;
                }
            }

            if (preFindHasError == true) {
                return result;
            }

            PreparedStatement ps = conn.getPreparedStatement(sSql);
            PreparedStatement countps = conn.getPreparedStatement(sSql1);

            RecordSet rs1 = conn.executeQuery(countps);
            if (rs1.next()) {
                pagecount = rs1.getInt(0);
                if (pagesize != 0) {
                    pagecount = (pagecount - 1) / pagesize;
                }
            }

            event.setBefore_result(result);
            if (result < 0) {
                return result;
            }
            RecordSet rs = conn.executeQuery(ps, (pageno * pagesize + 1), pagesize);

            if (rs.getRecordCount() > 0) {
                event.setResult(0);
                if (fa != null) {
                    result = fa.postReferenceOk(ctx, conn, fi, msgs, manager, reffldnm,
                            rs);
                }
            } else {
                if (fa != null) {
                    result = fa.postReferenceFail(ctx, conn, fi, msgs, manager, reffldnm,
                            rs);
                }
                msgs.add("" + ErrorCode.RECORD_SET_IS_NULL);
                event.setResult(ErrorCode.RECORD_SET_IS_NULL);
            }
            try {
                ps.close();
                countps.close();
            }
            catch (Exception e) {
                Debug.debug(e);
                //logger.severe(e.getMessage());
            }
            event.setAfter_result(result);
            ctx.setRequestAtrribute(SessionAttributes.REQUEST_REF_RESULT_NAME, rs);
            ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_PAGENO_NAME,
                    "" + pageno);
            ctx.setRequestAtrribute(SessionAttributes.REQUEST_LIST_PAGECOUNT_NAME,
                    "" + pagecount);

            return result;
        } else {
            msgs.add("" + ErrorCode.FORM_INSTANCE_NOT_EXIST);
            event.setResult(ErrorCode.FORM_INSTANCE_NOT_EXIST);
            return ErrorCode.FORM_INSTANCE_NOT_EXIST;
        }
    }

    public static int insertSmallQuery(SessionContext ctx, Event event,
                                       ErrorMessages msgs, EventManager manager,
                                       DatabaseConnection conn) {
        try {
            FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                    SessionAttributes.
                            SESSION_FORM_INSTANCE_MANAGER_NAME);
            String instanceid = event.getId();
            FormInstance fi = fiManager.getFormInstance(instanceid);
            if (fi != null) {
                FormActions fa = fi.getFormAction();
                if (fa != null) {
                    fa.insertSmallQueryEvent(ctx, conn, fi, msgs, manager);
                }
            }

        }
        catch (Exception e) {
            msgs.add(e.getMessage());
            e.printStackTrace();
        }
        if (!manager.hasMoreEvent()) {
            event.setType(EventType.INSERT_VIEW_EVENT_TYPE);
        }
        return 0;
    }

    public static int editSmallQuery(SessionContext ctx, Event event,
                                     ErrorMessages msgs, EventManager manager,
                                     DatabaseConnection conn) {
        try {
            FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(
                    SessionAttributes.
                            SESSION_FORM_INSTANCE_MANAGER_NAME);
            String instanceid = event.getId();
            FormInstance fi = fiManager.getFormInstance(instanceid);
            if (fi != null) {
                FormActions fa = fi.getFormAction();
                if (fa != null) {
                    fa.editSmallQueryEvent(ctx, conn, fi, msgs, manager);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            msgs.add(e.getMessage());
        }
        if (!manager.hasMoreEvent()) {
            event.setType(EventType.EDIT_VIEW_EVENT_TYPE);
        }

        return 0;
    }

    /**
     * ����ָ����ϵͳ�¼�������event������type��ѡ����Ӧ�ķ������д���
     */
    public static int process(SessionContext ctx, Event event, ErrorMessages msgs,
                              EventManager manager, DatabaseConnection conn) {
        switch (event.getType()) {
            case EventType.LOAD_EVENT_TYPE:
                return load(ctx, event, msgs, manager, conn);
            case EventType.BUTTON_EVENT_TYPE:
                return button(ctx, event, msgs, manager, conn);
            case EventType.DELETE_EVENT_TYPE:
                return delete(ctx, event, msgs, manager, conn);
            case EventType.DELETE_VIEW_EVENT_TYPE:
                return deleteview(ctx, event, msgs, manager, conn);
            case EventType.EDIT_EVENT_TYPE:
                return edit(ctx, event, msgs, manager, conn);
            case EventType.EDIT_VIEW_EVENT_TYPE:
                return editview(ctx, event, msgs, manager, conn);
            case EventType.FIND_EVENT_TYPE:
                return find(ctx, event, msgs, manager, conn);
            case EventType.FIND_VIEW_EVENT_TYPE:
                return findview(ctx, event, msgs, manager, conn);
            case EventType.INSERT_EVENT_TYPE:
                return insert(ctx, event, msgs, manager, conn);
            case EventType.INSERT_VIEW_EVENT_TYPE:
                return insertview(ctx, event, msgs, manager, conn);
            case EventType.REFERENCE_FIELD_EVENT_TYPE:
                return reference(ctx, event, msgs, manager, conn);
            case EventType.UNLOAD_EVENT_TYPE:
                return unload(ctx, event, msgs, manager, conn);
            case EventType.INSERT_SMALL_QUERY_EVENT_TYPE:
                return insertSmallQuery(ctx, event, msgs, manager, conn);
            case EventType.EDIT_SMALL_QUERY_EVENT_TYPE:
                return editSmallQuery(ctx, event, msgs, manager, conn);

            /**
             * following two events only to be triggered by XMLActionController
             * JGO on Aug 5, 2005
             */
            case EventType.POST_FIElD_EVENT:
                return postField(ctx, event, msgs, manager);
            case EventType.BEFORE_FIElD_EVENT:
                return beforeField(ctx, event, msgs, manager);
            default:
                return 0;
        }
    }

    /**
     * У��ÿ����λֵ�ĺϷ���
     */
    public static boolean validate(FormInstance instance, ErrorMessages msgs) {
        return true;
    }

    /**
     * added by JGO
     * here to parse xml data from request and save then into request's parameters
     *
     * @param fi FormInstance
     */
    private static void updateParameters(FormInstance fi, SessionContext ctx) {
        if (fi == null) return;
        HttpServletRequest req = null;
        ServletInputStream is = null;
        Document doc = null;
        String namestr, valuestr;

        try {
            req = ctx.getRequest();
            if (req == null) return;
            //req.setCharacterEncoding(PropertyManager.getProperty(SystemAttributeNames.WEB_SERVER_ENCODING));
            is = req.getInputStream();

            if (is == null) return;

//      String str = "";
//      byte[] bt = new byte[req.getContentLength()+1];
//      while(is.readLine(bt,0,req.getContentLength()) >= 0)
//      {
//        str += new String(bt);
//      }
//      if(str.trim().length() <= 0) return;
//      str = str.trim();
//
//      System.out.println("str="+str+"]");

//      Reader reader = new StringReader(str);

            org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
            doc = sb.build(is);
            Element el = doc.getRootElement();
            List list = el.getChildren();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                el = (Element) it.next();
                namestr = el.getChildText("name");
                valuestr = el.getChildText("value");
                if (namestr != null) namestr = namestr.trim();
                if (namestr != null && namestr.length() > 0 && valuestr != null) {
                    //System.out.println("XML LINE="+namestr+"="+valuestr);
                    ctx.setRequestAtrribute(namestr, valuestr);

                }
            }

        }
        catch (Exception e) {
            Debug.debug(e);
        }

    }

}
