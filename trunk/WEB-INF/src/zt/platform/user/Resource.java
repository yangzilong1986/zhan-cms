//Source file: e:\\java\\zt\\platform\\user\\Resource.java

package zt.platform.user;

import java.io.Serializable;

/**
 * ��Դ��
 *
 * @author WangHaiLei
 * @version 1.1
 */
public class Resource
        implements Serializable, Comparable {

    private String resourceid = null;
    private String parentid = null;
    private String resource = null;
    private String description = null;
    private int type = 0;
    public static final int JSP_TYPE = 1;
    public static final int FORM_TYPE = 2;
    public static final int FORM_METHOD_TYPE = 3;
    public static final int MENU_TYPE = 4;

    public Resource() {
    }

    public Resource(String resourceid, String resource, int type) {

        super();
        this.resourceid = resourceid;
        this.resource = resource;
        this.type = type;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        }
        if (!(o instanceof Resource)) {
            return -1;
        }
        Resource r = (Resource) o;
        if (r != null && resource != null && resource.equals(r.getResource()) &&
                r.getType() == this.type) {
            return 0;
        }
        return 1;
    }

    public boolean equals(Object o) {
        if (compareTo(o) == 0) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "resourceid=" + resourceid + ",parentid=" + parentid + ",\"RESOURCE\"=" + resource +
                ",type=" + type;
    }
}