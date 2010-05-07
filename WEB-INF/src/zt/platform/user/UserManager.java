package zt.platform.user;

/**
 * <p>Title: UserManager.java</p>
 * <p>Description: This class includes the basic behaviors of user.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author WangHaiLei
 * @version 1.6
 * $ UpdateDate: Y-M-D-H-M: 2003-12-02-09-50 $
 */

import com.ebis.encrypt.EncryptData;

import javax.security.auth.login.LoginException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//public class UserManager

//    implements Serializable,HttpSessionBindingListener {
public class UserManager
        implements Serializable {

    private static Logger logger = Logger.getLogger("zt.platform.user.UserManager");
    private String username = null; //username从login(username, password)中得到
    private String userid = null;
    private String[] roles = new String[]{};
    private String xmlString = null;
    private boolean isLogin = false;
    private Resources resources;
    private User user;
    private MenuBean mb;
    private transient DatabaseAgent database;
    static List users = new ArrayList();

//  public void valueBound(HttpSessionBindingEvent event) {
//    /**@todo Implement this javax.servlet.http.HttpSessionBindingListener method*/
//    //UserManager um = (UserManager)session1.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    System.out.println("Value bound.....");
//        synchronized (UserManager.users) {
//          UserManager.users.remove(this);
//          UserManager.users.add(this);
//        }
//}
//  public void valueUnbound(HttpSessionBindingEvent event) {
//      System.out.println("Value unbound......");
//      synchronized (UserManager.users) {
//      UserManager.users.remove(this);
//   }
//
//    }


    /**
     * 返回菜单的xml
     */
    public String getXmlString() {
        return (this.xmlString);
    }

    /**
     * 返回一个User Object。这个Ojbect中包含该用户的基本信息
     * 包括：userid, email, enabled, sex, status
     */
    public User getUser() {
        return user;
    }

    /**
     * 得到当前用户的username
     */
    public String getUserName() {
        try {
            return user.getUsername();
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * 得到当前用户的userId
     */
    public String getUserId() throws Exception {
        try {
            return user.getUserid();
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * 用户签到，验证username+passwd是否正确
     * 签到成功后
     * 1.isLogin=true
     * 2.取得该用户相关的所有角色
     * 3.初始化资源列表
     * 4.取得用户的菜单
     */
    public boolean login(String username, String password) throws LoginException {
        database = new DatabaseAgent();
        Map basicInfo = null;
        isLogin = false;

        try {

            String userEnabled = null;
            int userStatus = 0;
            //检查该用户是否存在
            basicInfo = database.getBasicOfUser(username);
            if (basicInfo == null) {
                return false;
            }
            //检查该用户的status，enable属性
            userEnabled = basicInfo.get("enab").toString();
            userStatus = ((Integer) basicInfo.get("stat")).intValue();
            if ((userStatus == 1) || (userEnabled.equals("0"))) {
                return false;
            }
        }
        catch (Exception ex) {
            logger.severe("user login exception:" + ex.getMessage());
            return false;
        }

        // 检查密码
        EncryptData enda = new EncryptData();
        String passwordGave = new String(enda.enPasswd(password.getBytes()));
        String passwordWant = (String) basicInfo.get("pswd");
        if (passwordWant == null || !passwordWant.equals(passwordGave)) {
            return false;
        } else {

//      try {
//        Map map = DatabaseAgent.getBrachAndUserName(this.getUserId());
//        String brhname = (String) map.get("brhname");
//        String username2 = (String) map.get("username");
//        String userid = user.getUsername();
//        VirtualUser vu = new VirtualUser(userid, username2, brhname);
//        synchronized(user){
//            users.add(vu);
//        }
//      }
//      catch (Exception e) {
//        e.printStackTrace();
//      }

            isLogin = true;
        }

        // 取得该用户的所有角色。
        try {
            roles = database.getRoleIdsOfUser(username);
        }
        catch (Exception ex) {
            logger.severe("load user roles exception:" + ex.getMessage());
        }

        // 初始化资源列表。
        try {
            resources = new Resources(username);
        }
        catch (Exception ex) {
            logger.severe("load user resources exception:" + ex.getMessage());
        }

        // 初始化菜单。
        try {
            mb = new MenuBean();
            this.xmlString = mb.generateStream(username);
        }
        catch (Exception ex) {
            logger.severe("init user menus exception:" + ex.getMessage());
        }

        // 初始化用户，基本属性。
        try {
            user = new User(username);
        }
        catch (Exception ex) {
            logger.severe("init user object exception:" + ex.getMessage());
        }

        logger.info("user [ " + username + " ] Loged in!");
        return isLogin;
    }

    /**
     * @return boolean
     * @roseuid 3F80B71A00BC
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * 检验权限
     * 1。取到合法资源标识
     * 2。使用Resources的checkPermission方法校验
     *
     * @param resource resourceId
     * @return boolean
     * @roseuid 3F80B8590151
     */
    public boolean checkPermission(String resource, int type, String url) {
        boolean permit = resources.checkPermission(resource, type, url);
        return permit;
    }

    /**
     * 签退
     * isLogin=flase
     * privileges=null
     * roles=null
     * menu=null
     *
     * @return A boolean indicating whether or not the logout succeeded.
     * @throws javax.security.auth.login.LoginException
     *
     * @roseuid 3F80BD150276
     */
    public void logout() throws LoginException {
        isLogin = false;
        resources = null;
        user = null;
        userid = null;
        roles = null;
        mb = null;
        xmlString = null;
        System.out.println("user [ " + username + " ] loged out!");
        username = null;
    }

    public static List getUsers() {
        return users;
    }

}
