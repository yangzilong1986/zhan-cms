package zt.platform.db;

/**
 * Copyright 2003 ZhongTian, Inc. All rights reserved. Zhongtian
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * File:ConnectionManager.java Date Author Changes March 5 2003 wangdeliang
 * Created
 * Edit by wxj at 20040609
 */

import java.util.logging.Logger;

public class ConnectionManager {
    private static Logger logger = Logger.getLogger("zt.platform.db.ConnectionManager");
    private static ConnectionManager manager = null;

    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        if (manager == null) {
            manager = new ConnectionManager();
        }
        return manager;
    }

    public DatabaseConnection getConnection() {
        DatabaseConnection con = null;
        try {
            con = new DatabaseConnection();
        }
        catch (NoAvailableResourceException ex) {
            logger.info(DBUtil.fromDB(ex.getMessage()));
        }
        if (con.getConnection() == null) {
            return null;
        }
        return con;
    }

    public void releaseConnection(DatabaseConnection con) {
        con.close();
    }

}
