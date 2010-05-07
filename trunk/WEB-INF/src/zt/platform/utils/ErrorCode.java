//Source file: e:\\java\\zt\\platform\\utils\\ErrorCode.java

package zt.platform.utils;


/**
 * ϵͳ������Ŷ�����
 * <p/>
 * 0  - �ɹ�
 * <0 - ʧ��
 * ��Ӵ�����ţ�������getMessage()�����������Ӧ��ע����Ϣ��
 * <p/>
 * ���磺
 * switch ( number ) {
 * case -1:
 * return ����Ч���ݿ���Դ��
 * case -2:
 * return ��������Ϊ�գ�
 * default:
 * return ����������number+"δ���壡"
 * }
 *
 * @author ���滻
 * @version 1.0
 */
public final class ErrorCode {
    public static final int SUCCESS_NO = 0;
    public static final int ERROR_NOT_EVENT = -100;
    public static final int EXCEPTION_THROWN = -101;
    public static final int FORM_LOAD_FAIL = -102;
    public static final int FORM_INSTANCE_NOT_EXIST = -103;
    public static final int FORM_INSTANCE_VALUE_ERROR = -104;
    public static final int RECORD_SET_IS_NULL = -105;
    public static final int LIST_SQL_DEFINED_ERROR = -106;
    public static final int REF_GET_FORMBEAN_ERROR = -107;
    public static final int REF_GET_ELEMENT_ERROR = -108;
    public static final int REF_DEFINED_ERROR = -109;
    public static final int BUTTON_EVENT_ERROR = -110;
    public static final int NO_RECORD_DELETED = -111;
    public static final int NO_RECORD_EDITED = -112;

    /**
     * @param number
     * @return String
     * @roseuid 3F8208E800A4
     */
    public String getMessage(int number) {
        switch (number) {
            case ERROR_NOT_EVENT:
                return "�����¼�����";
            case EXCEPTION_THROWN:
                return "ActionController�����̲߳�������";
            case FORM_LOAD_FAIL:
                return "FORM����ʧ�ܣ�";
            case FORM_INSTANCE_NOT_EXIST:
                return "��ʵ�������ڣ�";
            case FORM_INSTANCE_VALUE_ERROR:
                return "FORM����ֵ�Ƿ���";
            case RECORD_SET_IS_NULL:
                return "�޼�¼���أ�";
            case LIST_SQL_DEFINED_ERROR:
                return "LIST FORM�Ĳ�ѯSQL�������";
            case BUTTON_EVENT_ERROR:
                return "BUTTON��ֵΪ�գ�";
            case NO_RECORD_DELETED:
                return "";
            default:
                return "������Ų����ڣ�";
        }
    }
}
