package zt.platform.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 资源类集
 *
 * @author WangHaiLei
 * @version 1.1
 *          $ UpdateDate: Y-M-D-H-M: 2003-12-09-14-00 $
 */

public class Resources
        implements Serializable {

    private static Logger logger = Logger.getLogger("zt.platform.user.Resources");

    private String username = null;

    /**
     * 资源列表
     * <p/>
     * 资源（resource）- 资源实体(Privilege)
     */
    private Map resources;

    /**
     * 用户数据库代表，数据库操作都在其中进行。
     */
    private transient DatabaseAgent database;

//     /**
//      * 构造Privileges
//      * 在内部调用init()方法，获得资源列表
//      * 该Constructor未予以实现。
//      * @param roles
//      * @throws Exception
//      * @roseuid 3F80BD6301CE
//      */
//     public Resources(String[] roles)
//          throws Exception {
//          init(roles);
//     }

    /**
     * 构造Privileges
     * 在内部调用init()方法，获得
     *
     * @param username
     * @throws Exception
     * @roseuid 3F80BABE0076
     */
    public Resources(String username) throws Exception {
        init(username);
    }

    /**
     * 验证资源的权限
     *
     * @param resource
     * @return boolean
     * @roseuid 3F80BA9F0176
     */
    public boolean checkPermission(String resource, int type, String url) {
        boolean permitted = false;
        try {
            Resource rscToBeCheck = new Resource();
            rscToBeCheck.setResource(resource);
            rscToBeCheck.setType(type);
            if (resources.containsValue(rscToBeCheck)) {
                permitted = true;
            } else {
                permitted = false;
            }
            if (permitted && url != null && url.trim().length() > 0) {
                url = url.trim();
                database = new DatabaseAgent();
                permitted = database.checkUrl(username, url);
            }
        }
        catch (Exception ex2) {
            logger.severe("Wrong, when checking permission : [" + ex2 + " ]");
        }
        return permitted;
    }

    /**
     * 初始化，将属于该用户所有资源取出
     *
     * @param username
     * @roseuid 3F80BB050281
     */
    private void init(String username) {
        try {
            database = new DatabaseAgent();
            this.username = username;
            resources = database.getResourcesOfUser(username);
        }
        catch (Exception ex3) {
            logger.severe("Wrong, when initializing Resources: [" + ex3 +
                    "]. Place: zt.platform.user.Resources. " + ex3.getStackTrace());
        }
    }

//     /**
//      * 初始化，将属于该角色数组的资源取出
//      * 该方法未予以实现。
//      * @param roles
//      * @roseuid 3F80BE2B0103
//      */
//     private void init(String[] roles) {
//
//     }

    public void print() {
        Collection cl = resources.values();
        for (Iterator its = cl.iterator(); its.hasNext();) {
            Resource it = (Resource) its.next();
        }
    }
}
