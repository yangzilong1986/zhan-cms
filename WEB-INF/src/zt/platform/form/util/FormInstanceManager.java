//Source file: e:\\java\\zt\\platform\\form\\util\\FormInstanceManager.java

package zt.platform.form.util;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 管理FORM的实例化,一个会话（session）一个实例
 *
 * @author 王学吉
 * @version 1.0
 */
public class FormInstanceManager implements Serializable {

    /**
     * FormInstance缓冲池
     * <p/>
     * 采用前插模式，如果缓冲池满了，删除最后一个实例，第一个元素为当前元素
     * 请注意元素在添加时的互斥访问问题
     */
    private LinkedList manager = new LinkedList();

    /**
     * 实例缓冲池大小
     */
    private int maxSize;

    /**
     * 实例序号产生器，每个实例对应一个唯一的序号
     */
    private InstanceSequencer sequencer;

    /**
     * 实例化FormInstanceManager管理器
     * <p/>
     * maxSize:实例池的大小
     *
     * @param maxSize
     * @roseuid 3F7215D90371
     */
    public FormInstanceManager(int p_maxSize) {
        this.maxSize = p_maxSize;
        sequencer = new InstanceSequencer();
    }

    /**
     * 根据formid实例化FORM,并返回实例的序号
     *
     * @param formid
     * @return String
     * @roseuid 3F7215F002FC
     */
    public String instanceForm(String formid) {
        //判断有没有超出最大值，到了最大值就将第一个删除
        if (manager.size() >= maxSize) {
            manager.removeFirst();
        }
        //产生新的instanceForm，并返回FormInstanceId
        String sequence = sequencer.getSequence();
        FormInstance fi = new FormInstance(formid, sequence);
        //默认使用公共的FormBean
        fi.usePublicFormBean();
        manager.add(fi);
        return sequence;
    }

    /**
     * 获取实例instanceid
     *
     * @param instanceid
     * @return zt.platform.form.util.FormInstance
     * @roseuid 3F721605023E
     */
    public FormInstance getFormInstance(String instanceid) {
        //遍历manager找出instanceid相同的，删除，同时返回句柄。
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
     * 卸载实例instanceid
     *
     * @param instanceid
     * @return FormInsatnce
     * @roseuid 3F72CD4A0325
     */
    public FormInstance removeInstance(String instanceid) {
        //遍历manager找出instanceid相同的，删除，同时返回句柄。
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
