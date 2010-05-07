//Source file: D:\\zt\\platform\\form\\control\\SessionContext.java

package zt.platform.form.control;

import zt.platform.user.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * �Ự�����Ļ����ӿ�
 *
 * @author ���滻
 * @version 1.0
 */
public interface SessionContext {

    /**
     * ���ò�����Ự���ڣ�������󣬴��û������Ự����ֹ�Ự��������name��ֵvalue
     *
     * @param name
     * @param value
     * @roseuid 3F721CA90242
     */
    public void setAttribute(String name, Object value);

    /**
     * ��ȡ�Ự���ڣ�������󣬴��û������Ự����ֹ�Ự��������name��ֵ�������ڷ���null
     *
     * @param name
     * @return Object
     * @roseuid 3F721CB10013
     */
    public Object getAttribute(String name);

    /**
     * ɾ���Ự���ڣ�������󣬴��û������Ự����ֹ�Ự��������name
     *
     * @param name
     * @return Object
     * @roseuid 3F721CCD012C
     */
    public void removeAttribute(String name);

    /**
     * ��ûỰ�������������Ե�����
     *
     * @return String[]
     * @roseuid 3F721CDF009B
     */
    public Enumeration getAttributeNames();

    /**
     * ��ûỰ�������������Ե�ֵ
     *
     * @return Object[]
     * @roseuid 3F721CEB0139
     */
    public Object[] getAttributes();

    /**
     * ��ȡ����name�����������ֵ
     *
     * @param name
     * @return String
     * @roseuid 3F7220D102F7
     */
    public String getParameter(String name);

    /**
     * �����������nameֵΪvalue
     *
     * @param name
     * @param value
     * @roseuid 3F7DF45C0077
     */
    public void setParameter(String name, String value);

    /**
     * ��ȡ�������nameֵ��valuesֵ����
     *
     * @param name
     * @return String[]
     * @roseuid 3F7220DC01BC
     */
    public String[] getParameters(String name);

    /**
     * �����������nameֵΪvalues
     *
     * @param name
     * @param values
     * @roseuid 3F7DF474034C
     */
    public void setParameter(String name, String[] values);

    /**
     * ��������������������
     *
     * @return String[]
     * @roseuid 3F7220E90215
     */
    public Enumeration getParameterNames();

    /**
     * ɾ���������name
     *
     * @param name
     * @return String
     * @roseuid 3F722100036C
     */
    public String removeParameter(String name);

    /**
     * ������е��������
     *
     * @roseuid 3F7221140393
     */
    public void clearParameters();

    /**
     * ����Ҫ����FORM��ͷ����������Ϊ��������
     *
     * @param head
     * @roseuid 3F722BF602E5
     */
    public void setHead(String head);

    /**
     * ��ȡFORM��ͷ����Ϣ
     *
     * @return String
     * @roseuid 3F722C26029E
     */
    public String getHead();

    /**
     * ����Ҫ����FORM�����壬��������Ϊ��������
     *
     * @param body
     * @roseuid 3F722C040209
     */
    public void setBody(String body);

    /**
     * ��ȡFORM������
     *
     * @return String
     * @roseuid 3F722C2E0105
     */
    public String getBody();

    /**
     * ����Ҫ����FORM��β������������Ϊ��������
     *
     * @param tail
     * @roseuid 3F722C15019F
     */
    public void setTail(String tail);

    /**
     * ���FORM��β��
     *
     * @return String
     * @roseuid 3F722C3502D2
     */
    public String getTail();

    /**
     * �����������ԣ���������Ϊ��������
     *
     * @param name
     * @param value
     * @roseuid 3F73B1DE0280
     */
    public void setRequestAtrribute(String name, Object value);

    /**
     * �����������Ϊ������������Ե�ֵ
     *
     * @param name
     * @return Object
     * @roseuid 3F73B1F402AA
     */
    public Object getRequestAttribute(String name);

    /**
     * ɾ����������Ϊ�������������
     *
     * @param name
     * @return Object
     * @roseuid 3F73B2050091
     */
    public void removeRequestAttribute(String name);

    /**
     * �����������Ϊ����������������Ե���������
     *
     * @return String[]
     * @roseuid 3F73B21802F1
     */
    public Enumeration getRequestAttributeNames();

    public void update(Object request);

    public String getUrl(String path);

    public void setSession(Object session);

    public void setRequest(Object request);

    public String getSysButton();

    public void setSysButton(String sysButton);

    public String getBeforeHead();

    public void setBeforeHead(String beforeHead);

    public String getAfterHead();

    public void setAfterHead(String afterHead);

    public String getAfterBody();

    public void setAfterBody(String afterBody);

    public String getAfterSysButton();

    public void setAfterSysButton(String afterSysButton);

    public UserManager getUserManager();

    public boolean forward();

    public boolean needForward();

    public void setTarget(String target);

    public void setResponse(HttpServletResponse response);

    public HttpServletRequest getRequest();

}
