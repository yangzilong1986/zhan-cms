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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.security.auth.login.LoginException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//public class UserManager

//    implements Serializable,HttpSessionBindingListener {

public class UserManager
        implements Serializable {

//    private static Logger logger = Logger.getLogger("zt.platform.user.UserManager");
    private Log logger = LogFactory.getLog(this.getClass());
    private String username = null; //username��login(username, password)�еõ�
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
     * ���ز˵���xml
     */
    public String getXmlString() {
        return (this.xmlString);
    }

    /**
     * ����һ��User Object�����Ojbect�а������û��Ļ�����Ϣ
     * ������userid, email, enabled, sex, status
     */
    public User getUser() {
        return user;
    }

    /**
     * �õ���ǰ�û���username
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
     * �õ���ǰ�û���userId
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
     * �û�ǩ������֤username+passwd�Ƿ���ȷ
     * ǩ���ɹ���
     * 1.isLogin=true
     * 2.ȡ�ø��û���ص����н�ɫ
     * 3.��ʼ����Դ�б�
     * 4.ȡ���û��Ĳ˵�
     */
    public int login(String username, String password) throws LoginException {
        database = new DatabaseAgent();
        Map basicInfo = null;
        int loginStatus = -1;

        try {

            String userEnabled = null;
            int userStatus = 0;
            int errorLogin = 0;
            //�����û��Ƿ����
            basicInfo = database.getBasicOfUser(username);
            if (basicInfo == null) {
                logger.info("user [" + username + "]�����ڣ�");
                return 0;
            }
            //�����û���status��enable����
            userEnabled = basicInfo.get("enab").toString();
            userStatus = ((Integer) basicInfo.get("stat")).intValue();
            if ((userStatus == 1) || (userEnabled.equals("0"))) {
                logger.info("user [" + username + "]δ���ã�");
                return -1;
            }
            errorLogin = ((Integer) basicInfo.get("errorlogin")).intValue();
            if (errorLogin > 5) {
                logger.info("user [" + username + "]��¼����������ޣ�");
                return -3;
            }


        }
        catch (Exception ex) {
            logger.error("user login exception:" + ex.getMessage());
            return -100;
        }

        // �������
        EncryptData enda = new EncryptData();
        String passwordGave = new String(enda.enPasswd(password.getBytes()));
        String passwordWant = (String) basicInfo.get("pswd");
        if (passwordWant == null || !passwordWant.equals(passwordGave)) {
            database.updateErrorLogin(username);
            logger.info("user [" + username + "]�����������");
            return -2;
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
            //logger.info(username + "�û���¼ϵͳ...");
            loginStatus = 1;
        }

        // ȡ�ø��û������н�ɫ��
        try {
            roles = database.getRoleIdsOfUser(username);
        }
        catch (Exception ex) {
            logger.error("load user roles exception:" + ex.getMessage());
        }

        // ��ʼ����Դ�б�
        try {
            resources = new Resources(username);
        }
        catch (Exception ex) {
            logger.error("load user resources exception:" + ex.getMessage());
        }

        // ��ʼ���˵���
        try {
            mb = new MenuBean();
            this.xmlString = mb.generateStream(username);
        }
        catch (Exception ex) {
            logger.error("init user menus exception:" + ex.getMessage());
        }

        // ��ʼ���û����������ԡ�
        try {
            user = new User(username);
        }
        catch (Exception ex) {
            logger.error("init user object exception:" + ex.getMessage());
        }

        logger.info("user [ " + username + " ] Loged in!");
        return loginStatus;
    }

    /**
     * @return boolean
     * @roseuid 3F80B71A00BC
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * ����Ȩ��
     * 1��ȡ���Ϸ���Դ��ʶ
     * 2��ʹ��Resources��checkPermission����У��
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
     * ǩ��
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
    public void logout(String username) throws LoginException {
        isLogin = false;
        resources = null;
        user = null;
        userid = null;
        roles = null;
        mb = null;
        xmlString = null;
        logger.info("user [ " + username + " ] loged out!");
        username = null;
    }

    public static List getUsers() {
        return users;
    }

}
