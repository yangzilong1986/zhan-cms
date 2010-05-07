package zt.cmsi.pub.define;

/**
 * ��UserRole�����������壬��ʼ��ʱһ�ζ����ڴ�
 * ���õ�̬ģʽ
 *
 * @author zhouwei
 * $Date: 2005/06/28 07:00:39 $
 * @version 1.0
 *
 * ��Ȩ���ൺ���칫˾
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
     * ����UserRoleMan���Ψһʵ��
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
     * ���췽������ʼ������
     */
    private UserRoleMan() {
        data = new HashMap();
        if (!loadData()) {
            Debug.debug(Debug.TYPE_ERROR, "��ȡҵ�����ͱ�����ʧ�ܣ�����");
        }
    }

    /**
     * �����ݿ���װ��ҵ������
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
     * �ж�ĳ���û��Ƿ���ĳ�ֽ�ɫ
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
