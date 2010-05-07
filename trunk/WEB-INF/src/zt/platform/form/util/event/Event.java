//Source file: D:\\zt\\platform\\form\\util\\event\\Event.java

package zt.platform.form.util.event;

/**
 * FORM������¼�
 * Notes:
 * 1.��������Ե�setter��getter����
 * 2.Ĭ���¼�������Continue���ͣ����Ǵ�������¼�����������һ���¼�
 * 3.Break�¼����ͣ��Ǵ�������¼��ͽ������е��¼�����
 *
 * @author ��ѧ��
 * @version 1.0
 */
public class Event {
    private String id;
    private String instanceid;
    private int type;//�¼�����
    private int branchType;
    private boolean isInstance;
    private int before_result;
    private int result;
    private int after_result;

    /**
     * ��ʼ������
     * <p/>
     * ���FORM��δ���أ���ͨ�������ݳ�ʼ������
     */
    private String parameters;
    public static final int BRANCH_CONTINUE_TYPE = 0;
    public static final int BRANCH_BREAK_TYPE = 1;

    /**
     * @param id
     * @param type
     * @param isInstance
     * @param parameters
     * @roseuid 3F725BB7036E
     */
    public Event(String p_id, int type, int p_branchtype, boolean p_isInstance,
                 String p_parameters) {
        this.id = p_id;
        this.type = type;
        this.branchType = p_branchtype;
        this.isInstance = p_isInstance;
        this.parameters = p_parameters;
        if (p_isInstance) this.instanceid = p_id;
    }

    /**
     * @roseuid 3F725BB2026C
     */
    public Event() {

    }

    /**
     * @param id
     * @roseuid 3F7EB6140081
     */
    public void setId(String p_id) {
        this.id = id;
        if (this.isInstance) this.instanceid = p_id;
    }

    /**
     * @return String
     * @roseuid 3F7EB62502A2
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param instanceid
     * @roseuid 3F7EB6140081
     */
    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    /**
     * @return String
     * @roseuid 3F7EB62502A2
     */
    public String getInstanceid() {
        return this.instanceid;
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
     * @return boolean
     * @roseuid 3F7EB62502A2
     */
    public boolean isInstance() {
        return this.isInstance;
    }

    /**
     * @param parameters
     * @roseuid 3F7EB6140081
     */
    public void setParameters(String p_parameters) {
        this.parameters = p_parameters;
    }

    /**
     * @return String
     * @roseuid 3F7EB62502A2
     */
    public String getParameters() {
        return this.parameters;
    }

    public int getBranchType() {
        return branchType;
    }

    public void setBranchType(int branchType) {
        this.branchType = branchType;
    }

    public int getAfter_result() {
        return after_result;
    }

    public int getBefore_result() {
        return before_result;
    }

    public void setAfter_result(int after_result) {
        this.after_result = after_result;
    }

    public void setBefore_result(int before_result) {
        this.before_result = before_result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
