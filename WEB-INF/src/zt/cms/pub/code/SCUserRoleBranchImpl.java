package zt.cms.pub.code;
import com.caucho.hessian.server.HessianServlet;
import zt.cms.pub.*;
import weblogic.management.MBeanHome;
import weblogic.management.Helper;
import weblogic.management.configuration.ServerMBean;


import javax.naming.*;
import java.util.*;
import com.zt.util.PropertyManager;
import zt.cmsi.pub.MgServer;
import zt.cmsi.pub.MgServerMan;
import zt.platform.utils.Debug;
import zt.cmsi.pub.define.UserRoleMan;
import zt.cmsi.pub.define.BMTypeOpen;
import zt.cmsi.pub.define.BMReviewLimit;
import zt.cmsi.pub.define.BMAppCriteria;

public class SCUserRoleBranchImpl extends HessianServlet implements SCUserRoleBranch {
  public SCUserRoleBranchImpl() {
  }

  public boolean setUserDirty() {
    Debug.debug(Debug.TYPE_MESSAGE, "User should be refreshed!");
    SCUser.setDirtyLocal(true);
    return true;
  }

  public boolean setSCBranchDirty() {
    Debug.debug(Debug.TYPE_MESSAGE, "SCBranch should be refreshed!");
    return true;
  }

  public boolean setSCUserRoleDirty() {
    Debug.debug(Debug.TYPE_MESSAGE, "UserRole should be refreshed!");
    UserRoleMan.setDirtyLocal(true);
    return true;
  }

  public boolean setRevLimitDirty() {
    Debug.debug(Debug.TYPE_MESSAGE, "BMReviewLimit should be refreshed!");
    BMReviewLimit.setDirty(true);
    return true;
  }

  public boolean setTypeOpenDirty() {
    Debug.debug(Debug.TYPE_MESSAGE, "BMTypeOpen should be refreshed!");
    BMTypeOpen.setDirty(true);
    return true;
  }

  public boolean setAppCriteriaDirty() {
    Debug.debug(Debug.TYPE_MESSAGE, "AppCriteria should be refreshed!");
    BMAppCriteria.setDirty(true);
    return true;
  }

  public boolean setRoleDirty() {
    /**@todo Implement this zt.cms.pub.code.SCUserRoleBranch method*/
    throw new java.lang.UnsupportedOperationException("Method setRoleDirty() not yet implemented.");
  }

  public static boolean setAllUserDirty() {
    return setAllDirty("user");
  }

  public static boolean setAllUserRoleDirty() {
    return setAllDirty("userrole");
  }

  public static boolean setAllRevLimitDirty() {
    return setAllDirty("revlimit");
  }

  public static boolean setAllTypeOpenDirty() {
    return setAllDirty("typeopen");
  }

  public static boolean setAllAppCriteriaDirty() {
    return setAllDirty("appcrit");
  }

  public static boolean setAllDirty(String type) {
    try {
      Vector data = MgServerMan.getActiveMgServers();
      if(data != null)
      {
        for (Iterator iter = data.iterator(); iter.hasNext(); ) {
          MgServer item = (MgServer) iter.next();

          String url = "http://" + item.getListenAddress() + ":" +
              item.getListenPort() + "/globalsetupchanged";
          com.caucho.hessian.client.HessianProxyFactory factory = new com.
              caucho.hessian.client.HessianProxyFactory();
          SCUserRoleBranch t = (SCUserRoleBranch) factory.create(
              SCUserRoleBranch.class, url);
          if (type.equals("user")) {
            t.setUserDirty();
          }
          else if (type.equals("scbranch")) {
            t.setSCBranchDirty();
          }
          else if (type.equals("userrole")) {
            t.setSCUserRoleDirty();
          }
          else if (type.equals("revlimit")) {
            t.setRevLimitDirty();
          }
          else if (type.equals("typeopen")) {
            t.setTypeOpenDirty();
          }
          else if (type.equals("appcrit")) {
            t.setAppCriteriaDirty();
          }

        }
      }
      else
        return false;
      return true;
      }
      catch (Exception ex) {
        Debug.debug(ex);
        return false;
      }
  }



}
