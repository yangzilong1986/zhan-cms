package zt.cmsi.extrans;

import java.util.LinkedList;
import java.util.ListIterator;

public class RequestPool {
    LinkedList pool = null;

    public RequestPool() {
        pool = new LinkedList();
    }

    public int addRequest(IOEntity data) {
        if (pool.add(data)) {
            return 0;
        } else {
            return -1;
        }
    }

    public IOEntity getRequest() {
        if (!pool.isEmpty()) {
            IOEntity entity = (IOEntity) pool.removeFirst();
            return entity;
        } else {
            return null;
        }
    }

    public IOEntity getRequestByTag(long iotag) {
        ListIterator a = this.pool.listIterator();
        while (a.hasNext() == true) {
            IOEntity entity = (IOEntity) a.next();
            if (entity != null) {
                if (entity.ioTag == iotag) {
                    a.remove();
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * remove all IOEntity in pool that timeout!
     */
    public int removeTimeout(long timeout) {
        ListIterator a = this.pool.listIterator();
        while (a.hasNext() == true) {
            IOEntity entity = (IOEntity) a.next();
            if (entity != null) {
                if ((System.currentTimeMillis() - entity.timeCreated) > timeout) {
                    a.remove();
                }
            }
        }
        return 0;
    }
}
