//Source file: D:\\zt\\platform\\form\\util\\event\\EventManager.java

package zt.platform.form.util.event;

import java.util.LinkedList;

/**
 * ����һ��FORM���󴥷����¼�
 * ���ƣ�
 * ���д����ʹ��д����¼�
 *
 * @author ��ѧ��
 * @version 1.0
 */
public class EventManager {
    private LinkedList events = new LinkedList();

    /**
     * @roseuid 3F725CEA0207
     */
    public EventManager() {

    }

    /**
     * �����¼�
     * <p/>
     * ����¼����¼�������
     *
     * @param event
     * @roseuid 3F725CF203AD
     */
    public void trigger(Event event) {
        this.events.add(event);
    }

    /**
     * @param id
     * @param type
     * @param isInstance
     * @roseuid 3F7E3B7E00BA
     */
    public void trigger(String id, int type, int branchType, boolean isInstance) {
        this.events.add(new Event(id, type, branchType, isInstance, null));
    }

    /**
     * ����FORM�����¼�
     * <p/>
     * formid - FORM��ID
     * type - 0
     * isInstance - false
     * parameters - ��ʼ������
     *
     * @param formid
     * @param parameters
     * @roseuid 3F84B87D0168
     */
    public void trigger(String formid, String parameters) {
        this.events.add(new Event(formid, EventType.LOAD_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE, false, null));
    }

    /**
     * �Ƿ�����ѷ������¼�
     *
     * @return boolean
     * @roseuid 3F725D0000C8
     */
    public boolean hasMoreEvent() {
        if (this.events.size() > 0) return true;
        return false;
    }

    /**
     * ������һ�¼�������events��ɾ�������أ�
     *
     * @return zt.platform.form.util.event.Event
     * @roseuid 3F725E740145
     */
    public Event nextEvent() {
        if (hasMoreEvent()) {
            Event ev = (Event) this.events.getFirst();
            this.events.removeFirst();
            return ev;
        }
        return null;
    }
}
