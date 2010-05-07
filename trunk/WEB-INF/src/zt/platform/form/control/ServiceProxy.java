//Source file: D:\\zt\\platform\\form\\control\\ServiceProxy.java

package zt.platform.form.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FORM Engine ����ӿ�
 *
 * @author ���滻
 * @version 1.0
 */
public interface ServiceProxy {

    /**
     * ���FORMͷ��
     *
     * @return String
     * @roseuid 3F722159020C
     */
    public String getHead();

    /**
     * ����FORMͷ��
     *
     * @param head
     * @roseuid 3F7222F6024C
     */
    public void setHead(String head);

    /**
     * ��ȡFORM����
     *
     * @return String
     * @roseuid 3F722161033A
     */
    public String getBody();

    /**
     * ����FORM����
     *
     * @param body
     * @roseuid 3F72230003CD
     */
    public void setBody(String body);

    /**
     * ��ȡFORMβ��
     *
     * @return String
     * @roseuid 3F72216D02DD
     */
    public String getTail();

    /**
     * ����FORMβ��
     *
     * @param tail
     * @roseuid 3F72230E01EC
     */
    public void setTail(String tail);

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


    /**
     * �ṩ���е�FORM�������
     *
     * @param request
     * @roseuid 3F72217F0116
     */
    public void service(HttpServletRequest request, HttpServletResponse response);

    public String getProgInfo(HttpServletRequest request);

}
