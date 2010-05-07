package zt.platform.form.control.impl;

/**
 * @author JGO(GZL) on Aug.5, 2005
 * @version 1.0
 */

import zt.platform.form.control.ServiceProxy;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.XMLActionController;
import zt.platform.form.util.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

public class ServiceProxyXMLImpl
        implements ServiceProxy {
    private static Logger logger = Logger.getLogger("zt.platform.form.control.impl.SeviceProxyXMLImpl");
    private String title;
    private String head;
    private String body;
    private String tail;
    private String sysButton;
    private String beforeHead;
    private String afterHead;
    private String afterBody;
    private String afterSysButton;

    /**
     * @return String
     * @roseuid 3F73AAE60086
     */
    public String getHead() {
        return head;
    }

    /**
     * @param head
     * @roseuid 3F73AAE60090
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * @return String
     * @roseuid 3F73AAE600A4
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     * @roseuid 3F73AAE600B8
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return String
     * @roseuid 3F73AAE600CC
     */
    public String getTail() {
        return tail;
    }

    /**
     * @param tail
     * @roseuid 3F73AAE600D6
     */
    public void setTail(String tail) {
        this.tail = tail;
    }

    public String getSysButton() {
        return sysButton;
    }

    public void setSysButton(String sysButton) {
        this.sysButton = sysButton;
    }

    public String getBeforeHead() {
        return beforeHead;
    }

    public void setBeforeHead(String beforeHead) {
        this.beforeHead = beforeHead;
    }

    public String getAfterHead() {
        return afterHead;
    }

    public void setAfterHead(String afterHead) {
        this.afterHead = afterHead;
    }

    public String getAfterBody() {
        return afterBody;
    }

    public void setAfterBody(String afterBody) {
        this.afterBody = afterBody;
    }

    public String getAfterSysButton() {
        return afterSysButton;
    }

    public void setAfterSysButton(String afterSysButton) {
        this.afterSysButton = afterSysButton;
    }

    /**
     * FORM服务请求入口，其流程如下：
     * 1．通过参数HttpRequest获取名称为SESSION_CONTEXT_NAME的上下文环境ctx
     * 2．ctx为空，则创建上下文环境ctx，并保存到Session中
     * 3．New Action Controller
     * 4．将request的Attribute和Parameter对象封装到ctx中
     * 5．调用ActionController的run方法
     * 6．通过ctx的方法getHead()得到FORM头部信息,并调用this.setHead
     * 7．通过ctx的方法getBody()得到FORM头部信息,并调用this.setBody
     * 8．通过ctx的方法getTail()得到FORM头部信息,并调用this.setTail
     *
     * @param request
     * @roseuid 3F73AAE600EA
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {

        if (request == null) {
            return;
        }
        HttpSession session = request.getSession(true);
        SessionContext ctx = (SessionContext) session.getAttribute(SessionAttributes.SESSION_CONTEXT_NAME);
        if (ctx == null) {
            ctx = new SessionContextHttpImpl(request, response);
            session.setAttribute(SessionAttributes.SESSION_CONTEXT_NAME, ctx);
        }
        ctx.setSession(session);
        ctx.setRequest(request);
        ctx.setResponse(response);
        //调试信息****************************************************************************************************************************************************************************************//
        String instanceid = ctx.getParameter(SessionAttributes.REQUEST_INSATNCE_ID_NAME);
        String formid = null;
        if (instanceid != null && instanceid.trim().length() > 0) {
            zt.platform.form.util.FormInstanceManager fim = (zt.platform.form.util.FormInstanceManager) (ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME));
            if (fim != null) {
                zt.platform.form.util.FormInstance fi = fim.getFormInstance(instanceid);
                if (fi != null) {
                    formid = fi.getFormid();
                }
            }
        }
        if (formid == null || formid.trim().length() < 1) {
            formid = ctx.getParameter(SessionAttributes.REQUEST_FORM_ID_NAME);
        }
        String eventid = ctx.getParameter(SessionAttributes.REQUEST_EVENT_ID_NAME);
        String initParameters = ctx.getParameter(SessionAttributes.REQUEST_FORM_INIT_PARAMETERS_NAME);
        if (instanceid == null) instanceid = " ";
        if (formid == null) formid = " ";
        if (eventid == null) eventid = " ";
        if (initParameters == null) initParameters = " ";
        logger.info("instanceid:[" + instanceid + "]" + "  formid:[" + formid + "]" + "  eventid:[" + eventid + "]" + "  initParameters:[" + initParameters + "]");
        //************************************************************************************************************************************************************************************************//
        ctx.update(request);
        XMLActionController control = new XMLActionController(ctx);
        int result = control.run();
        if (ctx.needForward()) {
            ctx.setBeforeHead("");
            ctx.setHead("");
            ctx.setAfterHead("");
            ctx.setBody("");
            ctx.setAfterBody("");
            ctx.setSysButton("");
            ctx.setAfterSysButton("");
            ctx.setTail("");
            ctx.forward();
            return;
        }
        setBeforeHead(ctx.getBeforeHead());
        setHead(ctx.getHead());
        setAfterHead(ctx.getAfterHead());
        setBody(ctx.getBody());
        setAfterBody(ctx.getAfterBody());
        setSysButton(ctx.getSysButton());
        setAfterSysButton(ctx.getAfterSysButton());
        setTail(ctx.getTail());
    }

    public String getProgInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        SessionContext ctx = (SessionContext) session.getAttribute(SessionAttributes.SESSION_CONTEXT_NAME);
        if (ctx == null) {
            return "NOTFOUNDSESSION";
        }

        String instanceid = ctx.getParameter(SessionAttributes.REQUEST_INSATNCE_ID_NAME);
        String formid = null;
        if (instanceid != null && instanceid.trim().length() > 0) {
            zt.platform.form.util.FormInstanceManager fim = (zt.platform.form.util.FormInstanceManager) (ctx.getAttribute(SessionAttributes.SESSION_FORM_INSTANCE_MANAGER_NAME));
            if (fim != null) {
                zt.platform.form.util.FormInstance fi = fim.getFormInstance(instanceid);
                if (fi != null) {
                    formid = fi.getFormid();
                }
            }
        }
        if (formid == null || formid.trim().length() < 1) {
            formid = ctx.getParameter(SessionAttributes.REQUEST_FORM_ID_NAME);
        }
        String eventid = ctx.getParameter(SessionAttributes.REQUEST_EVENT_ID_NAME);
        String initParameters = ctx.getParameter(SessionAttributes.REQUEST_FORM_INIT_PARAMETERS_NAME);
        if (instanceid == null) instanceid = " ";
        if (formid == null) formid = " ";
        if (eventid == null) eventid = " ";
        if (initParameters == null) initParameters = " ";
        return "formid:[" + formid + "]" + "  eventid:[" + eventid + "]" + "  initParameters:[" + initParameters + "]";
    }
}
