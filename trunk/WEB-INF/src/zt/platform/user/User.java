package zt.platform.user;
/**
 * <p>Title: User.java</p>
 * <p>Description: This class is used to represent the User object and its properties.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author WangHaiLei
 * @version 1.3
 * $ UpdateDate: Y-M-D-H-M: 2003-12-02-09-50 $
 */

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

public class User
        implements Serializable {
    private static Logger logger = Logger.getLogger("zt.platform.user.User");

    /**
     * The following are the properties.
     */
    private int status = 0;
    private String userid;
    private String username;
    private String sex;
    private String email;
    private String enabled;

    /**
     * 用户数据库代表，数据库操作都在其中进行。
     */
    private transient DatabaseAgent database;

    /**
     * Default Constructors.
     */
    public User() {
    }

    /**
     * This constructor is good to use for initialization.
     *
     * @param username
     */
    public User(String username) {

        super();
        this.username = username;
        init(username);
    }

    /**
     * The following are the getter and setter methods of the properties.
     */
    public int getStatus() {
        return status;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getEnabled() {
        return enabled;
    }

    /**
     * 初始化，将属于该用户的所有基本信息取出。
     *
     * @param username
     * @roseuid 3F80BB050281
     */
    private void init(String username) {
        try {
            database = new DatabaseAgent();
            Map basic = database.getBasicOfUser(username);

            userid = (String) basic.get("usid");
            sex = (String) basic.get("sexo");
            email = (String) basic.get("emai");
            enabled = (String) basic.get("enab");
            status = ((Integer) basic.get("stat")).intValue();

        }
        catch (Exception ex3) {
            logger.severe("Wrong, when initializing User: [" + ex3 +
                    "]. Place: zt.platform.user.User. " + ex3.getStackTrace());
        }
    }
}
