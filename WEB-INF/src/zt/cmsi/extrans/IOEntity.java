//Source file: e:\\java\\zt\\cmsi\\extrans\\IOEntity.java

package zt.cmsi.extrans;

import java.io.Serializable;

public class IOEntity implements Serializable {
    public String BMNo = null;
    public boolean ifCreateGrant = true;
    public boolean sendOK = false;
    public long timeCreated;
    public long ioTag;
    public int failedTimes = 0;
}
