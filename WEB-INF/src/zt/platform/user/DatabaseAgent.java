package zt.platform.user;

/**
 * <p>Title: The persistent state (ie. database).</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ZhongTian</p>
 * @author WangHaiLei
 * @version 1.4
 * $ UpdateDate: Y-M-D-H-M: 2003-11-25-10-55 $
 */

import zt.platform.db.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseAgent {

    private static Logger logger = Logger.getLogger("zt.platform.user.DatabaseAgent");
    DBUtil dbut = new DBUtil();

    /**
     * Retrieve all the basic information of a User, Except password.
     *
     * @param userName
     * @return Map which includes all the basic info in a HashMap with Key & Value pare.
     * @throws java.lang.Exception
     */
    public Map getBasicOfUser(String userName) throws Exception {
        // 如果用户根本不存在，则返回null
        if (userName == null) {
            return null;
        }
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;
        String useridOfUser = null;
        String sexOfUser = null;
        String emailOfUser = null;
        String enabledOfUser = null;
        String passwd = null;
        int statusOfUser = 0;
        String SQL_GetBasicOfUser = "SELECT * " +
                "FROM ptuser " +
                "WHERE username = '" + userName + "'";
        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetBasicOfUser);
            if (!rs.next()) {
                return null;
            }
            useridOfUser = rs.getString("userid");
            useridOfUser = (useridOfUser == null ? "" : useridOfUser.trim());
            sexOfUser = rs.getString("sex");
            sexOfUser = (sexOfUser == null ? "" : sexOfUser.trim());
            emailOfUser = rs.getString("email");
            emailOfUser = (emailOfUser == null ? "" : emailOfUser.trim());
            enabledOfUser = rs.getString("enabled");
            enabledOfUser = (enabledOfUser == null ? "" : enabledOfUser.trim());
            passwd = rs.getString("passwd");
            passwd = (passwd == null ? null : passwd.trim());
            statusOfUser = rs.getInt("status");
        }
        catch (Exception ex) {
            logger.info(DBUtil.fromDB(ex.getMessage()));
            return null;
        }
        finally {
            cm.releaseConnection(dc);
        }
        //basicInfo
        Map basicInfo = new HashMap();
        basicInfo.put("usid", useridOfUser);
        basicInfo.put("stat", new Integer(statusOfUser));
        basicInfo.put("sexo", sexOfUser);
        basicInfo.put("emai", emailOfUser);
        basicInfo.put("enab", enabledOfUser);
        basicInfo.put("pswd", passwd);
        return basicInfo;
    }

    /**
     * 通过username得到该用户的userid。
     *
     * @param username
     * @return
     * @throws java.lang.Exception
     */
    public String getUserIdOfUser(String username) throws Exception {

        String SQL_GetUserIdOfUser = "" +
                "SELECT userid " +
                "FROM ptuser u " +
                "WHERE username='" + username + "'";

        String userIdOfUser = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetUserIdOfUser);
            while (rs.next()) {
                if (rs.getString("userid") != null) {
                    userIdOfUser = rs.getString("userid").trim();
                } else {
                    userIdOfUser = "";
                }
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println("Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getUserIdOfUser(String username)    [" +
                    ex + "] ");
        }

        return userIdOfUser;
    }

    /**
     * @return
     * @throws java.lang.Exception
     */
    public String[] getRoleIdsOfUser(String username) throws Exception {

        String SQL_GetRolesOfUser = "" +
                "SELECT roleid " +
                "FROM ptuserrole r " +
                "WHERE username='" + username + "'";

        String[] roleIdsOfUser = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetRolesOfUser);

            List listTemp = new ArrayList();
            while (rs.next()) {
                String roleId = null;
                if (rs.getString("roleid") != null) {
                    roleId = rs.getString("roleid").trim();
                } else {
                    roleId = "";
                }

                listTemp.add(roleId);
            }
            roleIdsOfUser = new String[listTemp.size()];
            for (int i = 0; i < listTemp.size(); i++) {
                roleIdsOfUser[i] = listTemp.get(i).toString();
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println("Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getRoleIdsOfUser(String username)    [" +
                    ex + "] ");
        }

        return roleIdsOfUser;
    }

//     /**
//      * The method is not enabled in this edition.
//      * @param roleId
//      */
//     public String getResourcesOfRole(String roleId)
//          throws Exception {
//
//          String SQL_GetResourcesOfRole = null;
//
//          return new String();
//     }

    /**
     * 输入username得到该用户可以使用的Resource Object的Map
     *
     * @param username
     * @return
     * @throws java.lang.Exception
     */
    public Map getResourcesOfUser(String username) throws Exception {

        String SQL_GetResourcesOfUser = "" +
                "SELECT distinct " +
                "rs.resourceid AS resid, " +
                "rs.\"RESOURCE\" AS res, " +
                "rs.type AS tp " +
                "FROM ptuserrole ur, " +
                "ptroleresource rr, " +
                "ptresource rs " +
                "WHERE ur.username='" + username + "' " +
                "and rr.roleid=ur.roleid " +
                "and rs.resourceid=rr.resourceid " +
                "and rs.enabled='1'";

        Map resources = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetResourcesOfUser);

            resources = new HashMap();
            while (rs.next()) {
                String resourceId = null;
                if (rs.getString("resid") != null) {
                    resourceId = rs.getString("resid").trim();
                } else {
                    resourceId = "";
                }

                String resource = null;
                if (rs.getString("res") != null) {
                    resource = rs.getString("res").trim();
                } else {
                    resource = "";
                }

                int type = rs.getInt("tp");

                Resource r = new Resource(resourceId, resource, type);
                resources.put(resourceId, r);
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println(
                    "Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getResourcesOfUser(String username)    [" +
                            ex + "] ");
        }

        return resources;
    }

    /**
     * @param username
     * @return
     * @throws java.lang.Exception
     */
    public List getResourceIdsOfUser(String username) throws Exception {

        String SQL_GetResourcesOfUser = "" +
                "SELECT distinct rs.resourceid " +
                "FROM ptuserrole rl, " +
                "ptroleresource rr, " +
                "ptresource rs, " +
                "WHERE rl.username='" + username + "' " +
                "and rr.roleid=rl.roleid " +
                "and rs.resourceid=rl.resourceid " +
                "and rs.enabled='1'";

        List resourceIdsOfUser = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetResourcesOfUser);

            resourceIdsOfUser = new ArrayList();
            while (rs.next()) {
                String resourceId = null;
                if (rs.getString("resourceid") != null) {
                    resourceId = rs.getString("resourceid").trim();
                } else {
                    resourceId = "";
                }

                resourceIdsOfUser.add(resourceId);
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println(
                    "Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getResourceIdsOfUser(String username).    [" +
                            ex + "] ");
        }

        return resourceIdsOfUser;
    }

    /**
     * @param username
     * @param roleId
     * @return
     * @throws java.lang.Exception
     */
    public boolean isUserInRole(String username, String roleId) throws Exception {

        String SQL_GetRolesOfUser = "" +
                "SELECT roleid AS roleid " +
                "FROM ptuserrole, " +
                "WHERE username='" + username + "' ";

        List roleIdsOfUser = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetRolesOfUser);

            roleIdsOfUser = new ArrayList();
            while (rs.next()) {
                String ri = null;
                if (rs.getString("roleid") != null) {
                    ri = rs.getString("roleid").trim();
                } else {
                    ri = "";
                }

                roleIdsOfUser.add(ri);
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println(
                    "Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.isUserInRole(String username, String roleId).    [" +
                            ex + "] ");
        }

        if (roleIdsOfUser.contains(roleId)) {
            return true;
        } else {
            return false;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////// Public Methods
    /**
     * 输入userid和nodesLevel之后，就得到这个用户在某一级上的所有的Node Object Array。
     * <p/>
     * 1，得到该用户的所有角色id。
     * 2，根据roleid，从ptroleresource中得到所有"where type=4"的resourceid，即得到了该用户的所有的menuid
     * 3，从ptresource中取出type=4并且resourceid的范围就在allResourcesForThisUser中的resourceid。
     * 4，根据上面的resourceid，从ptmenu通过where level='"+nodesLevel+"'得到在一个指定level上的所有menuid。
     * 5，依次根据每一个menuid从ptmenu表中取出相应的label和url和isleaf，将值分别赋给Node object中的String label, String url, String isleaf，构建每个Node Object。
     * 6，构建一个Node Object的Array。把该用户所拥有的所有的Node都放进去。返回。大功告成。
     *
     * @param username       用户ID，就是Loged In的那个用户。
     * @param menuItemsLevel 想要得到的Menu Item的层级。
     * @return Node] 返回一个该层级上该用户所看到的Node Oject的一个Array。
     */
    public MenuItemBean[] getMenuItems(String username, int menuItemsLevel) throws Exception {

        String SQL_GetMenuItemsForAUser = "" +
                "SELECT distinct " +
                "m.menuid AS menuItemId, " +
                "m.label AS menuItemLabel, " +
                "m.isleaf AS menuItemIsLeaf, " +
                "m.action AS menuItemUrl, m.disporder " +
                "FROM ptuserrole r, " +
                "ptroleresource rs, " +
                "ptresource s, " +
                "ptmenu m " +
                "WHERE r.username = '" + username + "' " +
                "and rs.roleid = r.roleid " +
                "and s.resourceid = rs.resourceid " +
                "and s.type = '4' " +
                "and m.menuid = s.\"RESOURCE\" " +
                "and m.funclvl = " + menuItemsLevel +
                " order by DispOrder";
        MenuItemBean[] menuItemsForThisUser = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetMenuItemsForAUser);

            List listTemp = new ArrayList();
            while (rs.next()) {
                String menuItemId = null;
                if (rs.getString("menuItemId") != null) {
                    menuItemId = rs.getString("menuItemId").trim();
                } else {
                    menuItemId = "";
                }

                String menuItemLabel = null;
                if (rs.getString("menuItemLabel") != null) {
                    menuItemLabel = dbut.fromDB(rs.getString("menuItemLabel").trim());
                } else {
                    menuItemLabel = "";
                }

                String menuItemIsLeaf = null;
                if (rs.getString("menuItemIsLeaf") != null) {
                    menuItemIsLeaf = rs.getString("menuItemIsLeaf").trim();
                } else {
                    menuItemIsLeaf = "";
                }

                String menuItemUrl = null;
                if (rs.getString("menuItemUrl") != null) {
                    menuItemUrl = rs.getString("menuItemUrl").trim();
                } else {
                    menuItemUrl = "";
                }

                listTemp.add(new MenuItemBean(menuItemId, menuItemLabel, menuItemIsLeaf,
                        menuItemUrl));
            }

            menuItemsForThisUser = new MenuItemBean[listTemp.size()];
            for (int i = 0; i < listTemp.size(); i++) {
                menuItemsForThisUser[i] = (MenuItemBean) listTemp.get(i);
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println(
                    "Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getMenuItems(String username, int menuItemsLevel).    [" +
                            ex + "] ");
        }

        return menuItemsForThisUser;
    }

    public boolean checkUrl(String username, String url) throws Exception {

        String SQL_GetMenuItemsForAUser = "" +
                "SELECT " +
                "m.action AS action " +
                "FROM ptuserrole r, " +
                "ptroleresource rs, " +
                "ptresource s, " +
                "ptmenu m " +
                "WHERE r.username = '" + username + "' " +
                "and rs.roleid = r.roleid " +
                "and s.resourceid = rs.resourceid " +
                "and s.type = '4' " +
                "and m.menuid = s.\"RESOURCE\" and m.action like '%" + url + "%' ";

        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        boolean checkpass = false;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetMenuItemsForAUser);

            if (rs.next()) {
                checkpass = true;
            }

            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println("Wrong, when data check. Place zt.platform.user.DatabaseAgent.checkUrl(String username, String url).    [" +
                    ex + "] ");
        }

        return checkpass;
    }

    /**
     * getMenuItems()的另一种形式。
     * 多了一个parentId参数，用于非第一级MenuItem。
     *
     * @param username
     * @param menuItemsLevel
     * @param parentId
     * @return
     */
    public MenuItemBean[] getMenuItems(String username, int menuItemsLevel, String parentId) {

        String SQL_GetMenuItemsForAUser = "" +
                "SELECT distinct " +
                "m.menuid AS menuItemId, " +
                "m.label AS menuItemLabel, " +
                "m.isleaf AS menuItemIsLeaf, " +
                "m.action AS menuItemUrl, m.disporder " +
                "FROM ptuserrole r, " +
                "ptroleresource rs, " +
                "ptresource s, " +
                "ptmenu m " +
                "WHERE r.username = '" + username + "' " +
                "and rs.roleid = r.roleid " +
                "and s.resourceid = rs.resourceid " +
                "and s.type = '4' " +
                "and m.menuid = s.\"RESOURCE\" " +
                "and m.funclvl = " + menuItemsLevel + " " +
                "and m.parentid = '" + parentId + "'" +
                " order by DispOrder";

        MenuItemBean[] menuItemsForThisUser = null;
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_GetMenuItemsForAUser);

            List listTemp = new ArrayList();
            while (rs.next()) {
                String menuItemId = null;
                if (rs.getString("menuItemId") != null) {
                    menuItemId = rs.getString("menuItemId").trim();
                } else {
                    menuItemId = "";
                }

                String menuItemLabel = null;
                if (rs.getString("menuItemLabel") != null) {
                    menuItemLabel = dbut.fromDB(rs.getString("menuItemLabel").trim());
                } else {
                    menuItemLabel = "";
                }

                String menuItemIsLeaf = null;
                if (rs.getString("menuItemIsLeaf") != null) {
                    menuItemIsLeaf = rs.getString("menuItemIsLeaf").trim();
                } else {
                    menuItemIsLeaf = "";
                }

                String menuItemUrl = null;
                if (rs.getString("menuItemUrl") != null) {
                    menuItemUrl = rs.getString("menuItemUrl").trim();
                } else {
                    menuItemUrl = "";
                }

                MenuItemBean aMenuItem = new MenuItemBean(menuItemId, menuItemLabel, menuItemIsLeaf,
                        menuItemUrl);
                listTemp.add(aMenuItem);
            }

            menuItemsForThisUser = new MenuItemBean[listTemp.size()];
            for (int i = 0; i < listTemp.size(); i++) {
                menuItemsForThisUser[i] = (MenuItemBean) listTemp.get(i);
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println(
                    "Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getMenuItems(String username, int menuItemsLevel, String parentId).    [" +
                            ex + "] ");
        }

        return menuItemsForThisUser;
    }

    public static Map getBrachAndUserName(String userid) {

        String sql = "select scuser.username username,scbranch.sname brhname " +
                "from scuser,scbranch where scuser.userno=" +
                userid + " and scuser.brhid=scbranch.brhid";
        Map map = new HashMap();
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;
        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(sql);
            if (rs.next()) {
                map.put("username", DBUtil.fromDB(rs.getString("username")));
                map.put("brhname", DBUtil.fromDB(rs.getString("brhname")));
            }
            cm.releaseConnection(dc);
        }
        catch (Exception ex) {
            System.err.println("Wrong, when data retrieving.   Place zt.platform.user.DatabaseAgent.getPasswordOfUser(String username).    [" +
                    ex + "] ");
        }
        return map;
    }

}
