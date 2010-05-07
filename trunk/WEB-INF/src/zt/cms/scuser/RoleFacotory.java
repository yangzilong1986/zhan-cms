package zt.cms.scuser;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;
import zt.platform.db.*;
public class RoleFacotory {
    public static Collection getAllRoles(){
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        RecordSet rs = con.executeQuery("select * from scrole");
        Collection roles = new Vector();
        while (rs.next()) {
            Role role = new Role();
            role.setRoleID(rs.getInt(0));
            role.setDescription(DBUtil.fromDB(rs.getString(1)));
            roles.add(role);
        }
        ConnectionManager.getInstance().releaseConnection(con);
        return roles;
    }

    public static Collection getUserRoles(int userno) {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        RecordSet rs = con.executeQuery("select roleno from scuserrole where userno="+userno);
        Collection roles = new Vector();
        while (rs.next()) {
            roles.add(new Integer(rs.getInt(0)));

        }

        ConnectionManager.getInstance().releaseConnection(con);
        return roles;
    }
}