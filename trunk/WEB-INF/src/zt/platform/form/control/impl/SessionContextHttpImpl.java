package zt.platform.form.control.impl;

import zt.platform.form.config.SystemAttributeNames;
import zt.platform.form.control.SessionContext;
import zt.platform.user.UserManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * SessionContext的HTTP实现
 * XXXAtribute是指会话期间的属性操作，可以通过HttpSession的方法实现。
 * XXXParameter是指请求参数的操作，可以通过HttpServletRequest实现
 * <p/>
 * XXXRequestAttribute是指请求参数的操作，可以通过HttpServletRequest实现
 *
 * @author 请替换
 * @version 1.0
 */
public class SessionContextHttpImpl
        implements SessionContext, Serializable {
    private static Logger logger = Logger.getLogger("zt.platform.form.control.impl.SessionContextHttpImpl");
    protected static final String BEFORE_HEAD_NAME = "plat_form_before_head";
    protected static final String HEAD_NAME = "plat_form_head";
    protected static final String AFTER_HEAD_NAME = "plat_form_after_head";
    protected static final String BODY_NAME = "plat_form_body";
    protected static final String AFTER_BODY_NAME = "plat_form_after_body";
    protected static final String SYS_BUTTON_NAME = "plat_form_sys_button";
    protected static final String AFTER_SYS_BUTTON_NAME = "plat_form_after_sys_button";
    protected static final String TAIL_NAME = "plat_form_tail";
    private transient HttpServletRequest request;
    private transient HttpSession session;
    private transient HttpServletResponse response;
    private String target;
    private String ctxPath = "/";

    /**
     * @param p_request request
     * @roseuid 3F7EAAF2034A
     */
    public SessionContextHttpImpl(HttpServletRequest p_request, HttpServletResponse p_response) {
        this.request = p_request;
        this.response = p_response;
        this.session = this.request.getSession(true);
        ctxPath = request.getContextPath();
        try {
            this.request.setCharacterEncoding("GBK");
        }
        catch (UnsupportedEncodingException ex) {
            logger.severe("SessionContextHttpImpl exception:" + ex.getMessage());
        }
    }

    /**
     * @param name
     * @param value
     * @roseuid 3F73AAE60130
     */
    public void setAttribute(String name, Object value) {
        if (name != null && value != null) {
            session.setAttribute(name, value);
        }
    }

    /**
     * @param name
     * @return Object
     * @roseuid 3F73AAE6014E
     */
    public Object getAttribute(String name) {
        return session.getAttribute(name);
    }

    /**
     * @param name
     * @return Object
     * @roseuid 3F73AAE60162
     */
    public void removeAttribute(String name) {
        session.removeAttribute(name);
    }

    /**
     * @return String[]
     * @roseuid 3F73AAE60176
     */
    public Enumeration getAttributeNames() {
        return session.getAttributeNames();
    }

    /**
     * @return Object[]
     * @roseuid 3F73AAE60180
     */
    public Object[] getAttributes() {
        return null;
    }

    /**
     * @param name
     * @return String
     * @roseuid 3F73AAE60194
     */
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    /**
     * @param name
     * @return String[]
     * @roseuid 3F73AAE601A8
     */
    public String[] getParameters(String name) {
        return request.getParameterValues(name);
    }

    /**
     * @return String[]
     * @roseuid 3F73AAE601C6
     */
    public Enumeration getParameterNames() {
        return request.getParameterNames();
    }

    /**
     * @param name
     * @return String
     * @roseuid 3F73AAE601D0
     */
    public String removeParameter(String name) {
        return null;
    }

    /**
     * @roseuid 3F73AAE601EE
     */
    public void clearParameters() {

    }

    /**
     * @param head
     * @roseuid 3F73AAE601F8
     */
    public void setHead(String head) {
        request.setAttribute(HEAD_NAME, head);
    }

    /**
     * @param body
     * @roseuid 3F73AAE60216
     */
    public void setBody(String body) {
        request.setAttribute(BODY_NAME, body);
    }

    /**
     * @param tail
     * @roseuid 3F73AAE60235
     */
    public void setTail(String tail) {
        request.setAttribute(TAIL_NAME, tail);
    }

    /**
     * @return String
     * @roseuid 3F73AAE60253
     */
    public String getHead() {
        return (String) request.getAttribute(HEAD_NAME);
    }

    /**
     * @return String
     * @roseuid 3F73AAE60267
     */
    public String getBody() {
        return (String) request.getAttribute(BODY_NAME);
    }

    /**
     * @return String
     * @roseuid 3F73AAE60271
     */
    public String getTail() {
        return (String) request.getAttribute(TAIL_NAME);
    }

    /**
     * @param name
     * @param value
     * @roseuid 3F7DF93600B9
     */
    public void setParameter(String name, String value) {

    }

    /**
     * @param name
     * @param values
     * @roseuid 3F7DF936020D
     */
    public void setParameter(String name, String[] values) {

    }

    /**
     * @param name
     * @param value
     * @roseuid 3F7DF9360362
     */
    public void setRequestAtrribute(String name, Object value) {
        if (name != null && value != null) {
            request.setAttribute(name, value);
        }
    }

    /**
     * @param name
     * @return Object
     * @roseuid 3F7DF93700D8
     */
    public Object getRequestAttribute(String name) {
        return request.getAttribute(name);
    }

    /**
     * @param name
     * @return Object
     * @roseuid 3F7DF93701BF
     */
    public void removeRequestAttribute(String name) {
        request.removeAttribute(name);
    }

    /**
     * @return String[]
     * @roseuid 3F7DF93702AF
     */
    public Enumeration getRequestAttributeNames() {
        return request.getAttributeNames();
    }

    public void update(Object p_request) {
        if (p_request != null && p_request instanceof HttpServletRequest) {
            this.request = (HttpServletRequest) p_request;
            this.session = this.request.getSession(true);
            this.ctxPath = this.request.getContextPath();
            try {
                this.request.setCharacterEncoding("GBK");
            }
            catch (UnsupportedEncodingException ex) {
            }
        }

    }

    public String getUrl(String path) {
        if (path != null && path.startsWith("/")) {
            return ctxPath + path;
        } else {
            return ctxPath + "/" + path;
        }
    }

    public void setSession(Object p_session) {
        if (p_session == null) {
            return;
        }
        if (p_session instanceof HttpSession) {
            this.session = (HttpSession) p_session;
        }
    }

    public void setRequest(Object p_request) {
        if (p_request == null) {
            return;
        }
        if (p_request instanceof HttpServletRequest) {
            this.request = (HttpServletRequest) p_request;
        }
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public String getSysButton() {
        String sys = (String) request.getAttribute(SYS_BUTTON_NAME);
        if (sys == null) {
            return "";
        } else {
            return sys;
        }
    }

    public void setSysButton(String sysButton) {
        request.setAttribute(SYS_BUTTON_NAME, sysButton);
    }

    public String getBeforeHead() {
        return (String) request.getAttribute(BEFORE_HEAD_NAME);
    }

    public void setBeforeHead(String beforeHead) {
        request.setAttribute(BEFORE_HEAD_NAME, beforeHead);
    }

    public String getAfterHead() {
        return (String) request.getAttribute(AFTER_HEAD_NAME);
    }

    public void setAfterHead(String afterHead) {
        request.setAttribute(AFTER_HEAD_NAME, afterHead);
    }

    public String getAfterBody() {
        return (String) request.getAttribute(AFTER_BODY_NAME);
    }

    public void setAfterBody(String afterBody) {
        request.setAttribute(AFTER_BODY_NAME, afterBody);
    }

    public String getAfterSysButton() {
        return (String) request.getAttribute(AFTER_SYS_BUTTON_NAME);
    }

    public void setAfterSysButton(String afterSysButton) {
        request.setAttribute(AFTER_SYS_BUTTON_NAME, afterSysButton);
    }

    public UserManager getUserManager() {
        return (UserManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    }

    public boolean forward() {
        if (target != null && target.trim().length() > 0) {
            try {
                String resource = target;
                target = null;
                ServletContext sc = session.getServletContext();
                RequestDispatcher rd = sc.getRequestDispatcher(resource);
                if (rd != null) {
                    rd.forward(request, response);
                    return true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean needForward() {
        return target == null ? false : true;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
//        response.setHeader("Cache-Control","no-cache");
    }
}
