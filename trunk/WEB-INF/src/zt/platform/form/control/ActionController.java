package zt.platform.form.control;

import com.zt.util.PropertyManager;
import zt.platform.db.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.FormBean;
import zt.platform.form.config.FormBeanManager;
import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.FormInstanceManager;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.Event;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.event.EventType;
import zt.platform.utils.ErrorCode;

/**
 * FORM����������������
 * <p/>
 * ���������е�FORM���󣬸��������ID���¼�ID���Ȳ�ͬ�����Խ��д������ľ��幤��
 * ��ControllerAssistor���
 */
public class ActionController {
    private SessionContext ctx;
    public static final int POOL_MIN_SIZE = 20;

    /**
     * @param ctx
     * @roseuid 3F724F850093
     */
    public ActionController(SessionContext ctx) {
        this.ctx = ctx;
    }

    /**
     * FORM�����пش���ִ���߼����£�
     * 1�����FORMʵ����������FormInstanceManager��formInstanceManager
     * 2����ȡ�����Formid��Instanceid��eventId
     * 3�������¼�����������������һ�������¼�
     * 4���������ݿ�����
     * 5�������¼�
     * 6�����ctx.getHead==null,��ctx.setHead(FORM�����Title)
     * 7������Form����
     * 8����FORM���屣��
     * ���ߣ�������
     * �޸ģ���ѧ��
     */
    public int run() {
        try {
            //1.ctx�л��FormInstanceManager�������������²���
            FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
            if (fiManager == null) {
                int size = PropertyManager.getIntProperty(SystemAttributeNames.FORM_INSTANCE_POOL_SIZE);
                if (size < POOL_MIN_SIZE) {
                    size = POOL_MIN_SIZE;
                }
                fiManager = new FormInstanceManager(size);
                ctx.setAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME, fiManager);
            }
            //2.�����¼�������������ÿ�������һϵ���¼���
            EventManager eManager = new EventManager();
            Event firstEvent = findEvent(fiManager);//��request�л�ȡԭʼ�Ĵ����¼�
            if (firstEvent == null) {
                return ErrorCode.ERROR_NOT_EVENT;
            }
            eManager.trigger(firstEvent);//�����¼���Ϊ��һ���¼�ѹ���¼�������
            if (firstEvent.isInstance()) {//�����������Ѿ����ڵ�ʵ���������request����ʵ�����е���ֵ
                FormInstance fi = fiManager.getFormInstance(firstEvent.getInstanceid());
                if (fi != null) {
                    fi.updateValue(ctx);
                }
            }
            //3.�������ݿ���Դ
            ConnectionManager manager = ConnectionManager.getInstance();
            DatabaseConnection con = manager.getConnection();
            //4.�����¼�
            ErrorMessages errMsgs = new ErrorMessages();
            Event event = null;
            int result = 0;
            try {
                while (eManager.hasMoreEvent() && result >= 0) {
                    event = eManager.nextEvent();
                    con.begin();
                    //�����¼��������ǽ���ƽ̨�ĺ�������
                    result = ControllerAssistor.process(ctx, event, errMsgs, eManager, con);
                    if (result < 0) {
                        con.rollback();
                    } else {
                        con.commit();
                    }
                    if (event.getBranchType() == event.BRANCH_BREAK_TYPE) {
                        break;
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                con.rollback();
            }
            catch (Throwable t) {
                System.out.println("=====================�������ش���==========================");
                t.printStackTrace();
                con.rollback();
                System.out.println("=========================================================");
            }
            //5.�ͷ����ݿ���Դ���ʵ�����
            manager.releaseConnection(con);
            if (ctx.needForward()) {
                return 0;
            }
            //6.����FORM��Title
            if (event.getType() == EventType.LOAD_EVENT_TYPE && result < 0) {
                ctx.setHead("����ʧ��");
                if (errMsgs.size() > 0) {
                    ctx.setBody(PageGenerator.getErrorString(errMsgs, result));
                }
                return -1;
            }
            if (ctx.getHead() == null && event != null) {
                FormInstance fi = fiManager.getFormInstance(event.getInstanceid());
                if (fi != null) {
                    FormBean fb = fi.getFormBean();
                    ctx.setHead(fb.getTitle());
                }
            }
            //7.�γ�Form������
            String[] body = ViewController.process(ctx, event, errMsgs, result);
            if (body != null) {
                if (body.length >= 1) {
                    ctx.setBody(body[0]);
                }
                if (body.length >= 2) {
                    ctx.setSysButton(body[1]);
                }
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            ctx.setHead("ϵͳ��������");
            ctx.setBody("ϵͳ��Դæ���Ժ������");
            ctx.setTail("");
            return ErrorCode.EXCEPTION_THROWN;
        }
    }

    /**
     * ��requese�л�ȡinstanceid��formid��eventid��initParameters���������
     * ���instanceid��formid�Ĵ����Ժ�eventid�ĺϷ���
     * ���ո�����Щ���������¼�����
     */
    private Event findEvent(FormInstanceManager manager) {
        String instanceid = ctx.getParameter(SessionAttributes.REQUEST_INSATNCE_ID_NAME);
        String formid = ctx.getParameter(SessionAttributes.REQUEST_FORM_ID_NAME);
        String eventid = ctx.getParameter(SessionAttributes.REQUEST_EVENT_ID_NAME);
        String initParameters = ctx.getParameter(SessionAttributes.REQUEST_FORM_INIT_PARAMETERS_NAME);
        instanceid = (instanceid == null ? null : instanceid.trim());
        formid = (formid == null ? null : formid.trim());
        eventid = (eventid == null ? null : eventid.trim());
        String id = "";
        int eventType = EventType.LOAD_EVENT_TYPE;
        boolean isInstance = false;

        //instanceid����
        if (instanceid != null && instanceid.length() > 0) {
            if (manager.getFormInstance(instanceid) == null) {
                ctx.setHead("������ʱ");//��manager���Ҳ���insttance�����ڳ�ʱ
                ctx.setBody("������ĳ����Ѿ�������,������ǩ����");
                ctx.setTail("");
                return null;
            }
            if (eventid == null || !EventType.validate(eventid)) {
                ctx.setHead("�Ƿ�����");
                ctx.setBody("�¼�ID[" + eventid + "]�����ڣ�");
                ctx.setTail("");
                return null;
            }
            id = instanceid;
            isInstance = true;
        }
        //��formid����
        else if (formid != null && formid.length() > 0) {
            if (FormBeanManager.getForm(formid) == null) {
                ctx.setHead("�Ƿ�����");
                ctx.setBody("����ID[" + formid + "]�����ڣ�");
                ctx.setTail("");
                return null;
            }
            if (eventid == null || !eventid.equals("" + EventType.LOAD_EVENT_TYPE)) {
                ctx.setHead("�Ƿ�����");
                ctx.setBody("����ID[" + eventid + "]�����ڣ�");
                ctx.setTail("");
                return null;
            }
            id = formid;
            isInstance = false;
        }
        //instanceid��formid���߶������ڣ������˳�
        else {
            ctx.setHead("�Ƿ�����");
            ctx.setBody("ʵ��ID�ʹ���IDΪ�գ�");
            ctx.setTail("");
            return null;
        }
        //eventType
        try {
            eventType = Integer.parseInt(eventid);
        }
        catch (Exception e) {
            return null;
        }
        //Event
        return new Event(id, eventType, Event.BRANCH_CONTINUE_TYPE, isInstance, initParameters);
    }
}
