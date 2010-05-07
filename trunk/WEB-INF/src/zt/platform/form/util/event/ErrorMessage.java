//Source file: e:\\java\\zt\\platform\\form\\util\\event\\ErrorMessage.java

package zt.platform.form.util.event;


/**
 * ������Ϣ
 * ������Ϣ��Ϊ�����ࣺ
 * 0���������ڳ�����д��������Ϣ
 * 1���������ڳ���������Ϣ���ŵķ�ʽ��д����Ϣ����ͨ�����ſ��Զ�̬��ò��ҿ��Զ������
 *
 * @author ��ѧ��
 * @version 1.0
 */
public class ErrorMessage {

    /**
     * ��Ϣ���ͣ�0-�޲���Ϣ 1-�в���Ϣ
     */
    private int type;

    /**
     * ��Ϣ����
     */
    private String message;

    /**
     * ��������
     */
    private String[] arguments;
    public static final int CONSTANT_TYPE = 0;
    public static final int VARIABLE_TYPE = 1;

    /**
     * ������Ϣ����������arguments��
     *
     * @param type
     * @param message
     * @param arguments
     * @roseuid 3F7266AD0228
     */
    public ErrorMessage(int p_type, String p_message, String[] p_arguments) {
        this.type = p_type;
        this.message = p_message;
        this.arguments = p_arguments;
    }

    /**
     * ������Ϣ������
     *
     * @param type
     * @param message
     * @roseuid 3F7266880153
     */
    public ErrorMessage(int p_type, String p_message) {
        this.type = p_type;
        this.message = p_message;
    }

    /**
     * @param type
     * @roseuid 3F7EB6140081
     */
    public void setType(int p_type) {
        this.type = p_type;
    }

    /**
     * @return int
     * @roseuid 3F7EB62502A2
     */
    public int getType() {
        return this.type;
    }

    /**
     * @param arguments
     * @roseuid 3F7EB6140081
     */
    public void setMessage(String p_message) {
        this.message = p_message;
    }

    /**
     * @return String
     * @roseuid 3F72683A007A
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param arguments
     * @roseuid 3F7EB6140081
     */
    public void setArguments(String[] p_arguments) {
        this.arguments = p_arguments;
    }

    /**
     * @return String[]
     * @roseuid 3F7E3CD50294
     */
    public String[] getArguments() {
        return this.arguments;
    }
}
