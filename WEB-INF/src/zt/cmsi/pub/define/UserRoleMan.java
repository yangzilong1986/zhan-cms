package zt.cmsi.pub.define;

/**
 * 对UserRole的数据作缓冲，初始化时一次读入内存
 * 采用单态模式
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * 版权：青岛中天公司
 */

import zt.cms.pub.code.SCUserRoleBranchImpl;
import zt.cmsi.mydb.MyDB;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.utils.Debug;

import java.util.HashMap;

public class UserRoleMan {
    private static HashMap data = null;
    private static UserRoleMan ptr = null;
    private static boolean isDirty = false;


    public static void setDirty(boolean dirty) {
        SCUserRoleBranchImpl.setAllUserRoleDirty();
        isDirty = true;
    }

    public static void setDirtyLocal(boolean dirty) {
        UserRoleMan.isDirty = dirty;
    }

    /**
     * 返回UserRoleMan类的唯一实例
     */
    public static UserRoleMan getInstance() {
        if (ptr == null) {
            System.out.println("UserRoleMan initing ...");
            ptr = new UserRoleMan();
            System.out.println("UserRoleMan inited ok !");
            return ptr;
        } else {
            return ptr;
        }
    }

    /**
     * 构造方法，初始化数据
     */
    private UserRoleMan() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "读取业务类型表数据失败！！！");
        }
    }

    /**
     * 从数据库中装入业务数据
     *
     * @return
     */
    private boolean loadData() {

        try {
            DatabaseConnection dc = MyDB.getInstance().apGetConn();
            RecordSet rs = dc.executeQuery("select * from scuserrole order by userno");
            int userno = -1;
            //data.clear();
            data = new HashMap();
            while (rs.next()) {
                if (userno != rs.getInt("userno")) {
                    userno = rs.getInt("userno");
                    UserRole userrole = new UserRole();
                    userrole.userNo = userno;
                    userrole.addRole(rs.getInt("roleno"));
                    data.put(new Integer(userno), userrole);
                } else {
                    userno = rs.getInt("userno");
                    ((UserRole) data.get(new Integer(userno))).addRole(rs.getInt("roleno"));
                }
            }
        }
        catch (Exception e) {
            Debug.debug(Debug.TYPE_WARNING, e.toString());
            Debug.debug(e);
            return false;
        }
        finally {
            MyDB.getInstance().apReleaseConn(0);
        }
        return true;
    }

    /**
     * 判断某个用户是否有某种角色
     */
    public boolean ifHasRole(int userNo, int RoleNo) {
        if (this.isDirty == true) {
            this.loadData();
            this.isDirty = false;
        }

        if (!data.containsKey(new Integer(userNo))) {
            return false;
        } else {
            UserRole userrole = (UserRole) data.get(new Integer(userNo));
            return userrole.hasRole(RoleNo);
        }
    }

    public static void main(String[] args) {
        System.out.println(UserRoleMan.getInstance().ifHasRole(1, 1));
        System.out.println(UserRoleMan.getInstance().ifHasRole(1, 2));
        System.out.println(UserRoleMan.getInstance().ifHasRole(1, 3));
        System.out.println(UserRoleMan.getInstance().ifHasRole(1, 4));
    }
}
