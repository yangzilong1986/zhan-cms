package zt.platform.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * ��Դ�༯
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
     * ��Դ�б�
     * <p/>
     * ��Դ��resource��- ��Դʵ��(Privilege)
     */
    private Map resources;

    /**
     * �û����ݿ�������ݿ�����������н��С�
     */
    private transient DatabaseAgent database;

//     /**
//      * ����Privileges
//      * ���ڲ�����init()�����������Դ�б�
//      * ��Constructorδ����ʵ�֡�
//      * @param roles
//      * @throws Exception
//      * @roseuid 3F80BD6301CE
//      */
//     public Resources(String[] roles)
//          throws Exception {
//          init(roles);
//     }

    /**
     * ����Privileges
     * ���ڲ�����init()���������
     *
     * @param username
     * @throws Exception
     * @roseuid 3F80BABE0076
     */
    public Resources(String username) throws Exception {
        init(username);
    }

    /**
     * ��֤��Դ��Ȩ��
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
     * ��ʼ���������ڸ��û�������Դȡ��
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
//      * ��ʼ���������ڸý�ɫ�������Դȡ��
//      * �÷���δ����ʵ�֡�
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
