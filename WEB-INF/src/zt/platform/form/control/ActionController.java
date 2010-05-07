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
 * FORM请求处理的中央控制器
 * <p/>
 * 负责处理所有的FORM请求，根据请求的ID和事件ID调度不同工作对进行处理，它的具体工作
 * 由ControllerAssistor完成
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
     * FORM请求中控处理，执行逻辑如下：
     * 1、获得FORM实例管理器（FormInstanceManager）formInstanceManager
     * 2、获取请求的Formid、Instanceid、eventId
     * 3、创建事件管理器，并触发第一个请求事件
     * 4、申请数据库连接
     * 5、处理事件
     * 6、如果ctx.getHead==null,则ctx.setHead(FORM定义的Title)
     * 7、生成Form主体
     * 8、将FORM主体保存
     * 作者：王德良
     * 修改：王学吉
     */
    public int run() {
        try {
            //1.ctx中获得FormInstanceManager，不存在则重新产生
            FormInstanceManager fiManager = (FormInstanceManager) ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME);
            if (fiManager == null) {
                int size = PropertyManager.getIntProperty(SystemAttributeNames.FORM_INSTANCE_POOL_SIZE);
                if (size < POOL_MIN_SIZE) {
                    size = POOL_MIN_SIZE;
                }
                fiManager = new FormInstanceManager(size);
                ctx.setAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME, fiManager);
            }
            //2.构造事件管理器（管理每个请求的一系列事件）
            EventManager eManager = new EventManager();
            Event firstEvent = findEvent(fiManager);//从request中获取原始的触发事件
            if (firstEvent == null) {
                return ErrorCode.ERROR_NOT_EVENT;
            }
            eManager.trigger(firstEvent);//触发事件作为第一个事件压入事件管理器
            if (firstEvent.isInstance()) {//如果请求的是已经存在的实例，则根据request更新实例当中的数值
                FormInstance fi = fiManager.getFormInstance(firstEvent.getInstanceid());
                if (fi != null) {
                    fi.updateValue(ctx);
                }
            }
            //3.申请数据库资源
            ConnectionManager manager = ConnectionManager.getInstance();
            DatabaseConnection con = manager.getConnection();
            //4.处理事件
            ErrorMessages errMsgs = new ErrorMessages();
            Event event = null;
            int result = 0;
            try {
                while (eManager.hasMoreEvent() && result >= 0) {
                    event = eManager.nextEvent();
                    con.begin();
                    //单个事件处理，这是进入平台的核心所在
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
                System.out.println("=====================发生严重错误==========================");
                t.printStackTrace();
                con.rollback();
                System.out.println("=========================================================");
            }
            //5.释放数据库资源，适当返回
            manager.releaseConnection(con);
            if (ctx.needForward()) {
                return 0;
            }
            //6.设置FORM的Title
            if (event.getType() == EventType.LOAD_EVENT_TYPE && result < 0) {
                ctx.setHead("加载失败");
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
            //7.形成Form的主体
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
            ctx.setHead("系统发生例外");
            ctx.setBody("系统资源忙，稍后继续！");
            ctx.setTail("");
            return ErrorCode.EXCEPTION_THROWN;
        }
    }

    /**
     * 从requese中获取instanceid，formid，eventid，initParameters等请求参数
     * 检查instanceid，formid的存在性和eventid的合法性
     * 最终根据这些参数生成事件对象
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

        //instanceid存在
        if (instanceid != null && instanceid.length() > 0) {
            if (manager.getFormInstance(instanceid) == null) {
                ctx.setHead("操作超时");//从manager中找不到insttance则属于超时
                ctx.setBody("您请求的程序已经不存在,请重新签到！");
                ctx.setTail("");
                return null;
            }
            if (eventid == null || !EventType.validate(eventid)) {
                ctx.setHead("非法请求");
                ctx.setBody("事件ID[" + eventid + "]不存在！");
                ctx.setTail("");
                return null;
            }
            id = instanceid;
            isInstance = true;
        }
        //仅formid存在
        else if (formid != null && formid.length() > 0) {
            if (FormBeanManager.getForm(formid) == null) {
                ctx.setHead("非法请求");
                ctx.setBody("窗体ID[" + formid + "]不存在！");
                ctx.setTail("");
                return null;
            }
            if (eventid == null || !eventid.equals("" + EventType.LOAD_EVENT_TYPE)) {
                ctx.setHead("非法请求");
                ctx.setBody("窗体ID[" + eventid + "]不存在！");
                ctx.setTail("");
                return null;
            }
            id = formid;
            isInstance = false;
        }
        //instanceid和formid两者都不存在，错误退出
        else {
            ctx.setHead("非法请求");
            ctx.setBody("实例ID和窗体ID为空！");
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
