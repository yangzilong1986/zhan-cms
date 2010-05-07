//Source file: e:\\java\\zt\\platform\\form\\util\\FormInstanceManager.java

package zt.platform.form.util;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * ����FORM��ʵ����,һ���Ự��session��һ��ʵ��
 *
 * @author ��ѧ��
 * @version 1.0
 */
public class FormInstanceManager implements Serializable {

    /**
     * FormInstance�����
     * <p/>
     * ����ǰ��ģʽ�������������ˣ�ɾ�����һ��ʵ������һ��Ԫ��Ϊ��ǰԪ��
     * ��ע��Ԫ�������ʱ�Ļ����������
     */
    private LinkedList manager = new LinkedList();

    /**
     * ʵ������ش�С
     */
    private int maxSize;

    /**
     * ʵ����Ų�������ÿ��ʵ����Ӧһ��Ψһ�����
     */
    private InstanceSequencer sequencer;

    /**
     * ʵ����FormInstanceManager������
     * <p/>
     * maxSize:ʵ���صĴ�С
     *
     * @param maxSize
     * @roseuid 3F7215D90371
     */
    public FormInstanceManager(int p_maxSize) {
        this.maxSize = p_maxSize;
        sequencer = new InstanceSequencer();
    }

    /**
     * ����formidʵ����FORM,������ʵ�������
     *
     * @param formid
     * @return String
     * @roseuid 3F7215F002FC
     */
    public String instanceForm(String formid) {
        //�ж���û�г������ֵ���������ֵ�ͽ���һ��ɾ��
        if (manager.size() >= maxSize) {
            manager.removeFirst();
        }
        //�����µ�instanceForm��������FormInstanceId
        String sequence = sequencer.getSequence();
        FormInstance fi = new FormInstance(formid, sequence);
        //Ĭ��ʹ�ù�����FormBean
        fi.usePublicFormBean();
        manager.add(fi);
        return sequence;
    }

    /**
     * ��ȡʵ��instanceid
     *
     * @param instanceid
     * @return zt.platform.form.util.FormInstance
     * @roseuid 3F721605023E
     */
    public FormInstance getFormInstance(String instanceid) {
        //����manager�ҳ�instanceid��ͬ�ģ�ɾ����ͬʱ���ؾ����
        FormInstance fi;
        for (int i = manager.size() - 1; i >= 0; i--) {
            fi = (FormInstance) manager.get(i);
            if ((fi.getInstanceid()).equals(instanceid)) {
                return fi;
            }
        }
        return null;
    }

    /**
     * ж��ʵ��instanceid
     *
     * @param instanceid
     * @return FormInsatnce
     * @roseuid 3F72CD4A0325
     */
    public FormInstance removeInstance(String instanceid) {
        //����manager�ҳ�instanceid��ͬ�ģ�ɾ����ͬʱ���ؾ����
        FormInstance fi;
        for (int i = manager.size() - 1; i >= 0; i--) {
            fi = (FormInstance) manager.get(i);
            if ((fi.getInstanceid()).equals(instanceid)) {
                return (FormInstance) manager.remove(i);
            }
        }
        return null;
    }
}
