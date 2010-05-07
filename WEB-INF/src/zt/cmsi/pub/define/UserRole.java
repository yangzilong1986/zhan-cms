//Source file: e:\\java\\zt\\cmsi\\pub\\define\\UserRole.java

package zt.cmsi.pub.define;

import java.util.ArrayList;

public class UserRole {
    public int userNo;
    private ArrayList roleNos = new ArrayList();

    public void addRole(int roleNo) {
        roleNos.add(new Integer(roleNo));
    }

    public boolean hasRole(int roleNo) {
        return roleNos.contains(new Integer(roleNo));
    }
}
