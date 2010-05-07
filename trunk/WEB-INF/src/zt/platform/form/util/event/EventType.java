//Source file: e:\\java\\zt\\platform\\form\\util\\event\\EventType.java

package zt.platform.form.util.event;


/**
 * �¼�����
 *
 * @version 1.0
 */
public class EventType {

    /**
     * FORM�����¼�
     */
    public static final int LOAD_EVENT_TYPE = 0;

    /**
     * FORM����ҳ���¼�
     */
    public static final int INSERT_VIEW_EVENT_TYPE = 1;

    /**
     * FORM�����¼�
     */
    public static final int INSERT_EVENT_TYPE = 2;

    /**
     * FORM�޸�ҳ���¼�
     */
    public static final int EDIT_VIEW_EVENT_TYPE = 3;

    /**
     * FORM�޸��¼�
     */
    public static final int EDIT_EVENT_TYPE = 4;

    /**
     * FORMɾ��ҳ���¼�
     */
    public static final int DELETE_VIEW_EVENT_TYPE = 5;

    /**
     * FORMɾ���¼�
     */
    public static final int DELETE_EVENT_TYPE = 6;

    /**
     * FORM��ѯҳ���¼�
     */
    public static final int FIND_VIEW_EVENT_TYPE = 7;

    /**
     * FORM��ѯ�¼�
     */
    public static final int FIND_EVENT_TYPE = 8;

    /**
     * FORM��ť�¼�
     */
    public static final int BUTTON_EVENT_TYPE = 9;

    /**
     * ж���¼�
     */
    public static final int UNLOAD_EVENT_TYPE = 10;

    /**
     * �ο��¼�
     */
    public static final int REFERENCE_FIELD_EVENT_TYPE = 11;
    /**
     * ����С�Ͳ�ѯ
     */
    public static final int INSERT_SMALL_QUERY_EVENT_TYPE = 12;
    /**
     * �޸�С�Ͳ�ѯ
     */
    public static final int EDIT_SMALL_QUERY_EVENT_TYPE = 13;

    /**
     * POSTFIELD EVENT
     * the ptforminfodetl field of triggering this event is ONCHANGE
     */
    public static final int POST_FIElD_EVENT = 14;

    /**
     * BEFOREFIELD EVENT
     * the ptforminfodetl field of triggering this event is ONCLICK
     */
    public static final int BEFORE_FIElD_EVENT = 15;

    /**
     * �ж��¼������Ƿ�Ϸ�
     *
     * @param eventid
     * @return
     */
    public static boolean validate(String eventid) {
        return true;
    }
}
