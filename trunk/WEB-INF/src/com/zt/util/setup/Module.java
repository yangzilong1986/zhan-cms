package com.zt.util.setup;

import java.util.*;
import java.sql.*;
//import com.zt.db.*;

/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003年8月19日
 */
public class Module
         implements IModule {
    private HashMap properties = null;
    String moduleName = null;

    /**
     *  Description of the Field
     */
    public static String SETUP_MODULE = "setup_module";
    /**
     *  Description of the Field
     */
    public static String SETUP_PROPERTY = "setup_property";
    /**
     *  Description of the Field
     */
    public static String MODULE_NAME = "module_name";
    /**
     *  Description of the Field
     */
    public static String PROPERTY_NAME = "property_name";
    /**
     *  Description of the Field
     */
    public static String PROPERTY_VALUE = "property_value";


    /**
     *  Constructor for the Module object
     *
     *@param  moduleName  Description of the Parameter
     */
    public Module(String moduleName) {
        this.properties = new HashMap();
        this.moduleName = moduleName;
        this.init();
    }

    /**
     *  Gets the property attribute of the Module object
     *
     *@param  name  Description of the Parameter
     *@return       The property value
     */
    public String getProperty(String name) {
        return (String)this.properties.get(name);
    }

    /**
     *  Sets the property attribute of the Module object
     *
     *@param  name   The new property value
     *@param  value  The new property value
     *@return        Description of the Return Value
     */
    public int setProperty(String name, String value) {
        if (!this.hasProperty(name)) {
            if (this.addProperty(name, value) == 0) {
                return 0;
            }
            else {
                return -1;
            }
        }
        else {
            String str = "update " + SETUP_PROPERTY + " set " +
                this.PROPERTY_VALUE +
                "='" + value + "'" + " where " + this.MODULE_NAME + "='" +
                this.moduleName + "' and " + this.PROPERTY_NAME + "='" + name +
                "'";
            Connection con = ConnectionManager.getConnection();
            try {
                Statement st = con.createStatement();
                st.executeUpdate(str);
                ConnectionManager.releaseConnection();
                this.properties.put(name, value);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally {
                ConnectionManager.releaseConnection();
                return -1;
            }
        }
    }

    /**
     *  Adds a feature to the Property attribute of the Module object
     *
     *@param  name   The feature to be added to the Property attribute
     *@param  value  The feature to be added to the Property attribute
     *@return        Description of the Return Value
     */
    protected int addProperty(String name, String value) {

        if (this.hasProperty(name)) {
            return -1;
        }
        else {
            String str = "insert into " + this.SETUP_PROPERTY + " values('" +
                this.moduleName + "','" + name + "','" + value + "')";
            Connection con = ConnectionManager.getConnection();
            try {
                Statement st = con.createStatement();
                st.executeQuery(str);
                this.properties.put(name, value);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            finally {
                ConnectionManager.releaseConnection();
            }
            return 0;
        }
    }

    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     */
    public void removeProperty(String name) {
        String str = "delete from " + this.SETUP_PROPERTY + " where " +
            this.MODULE_NAME + "='" +
            this.moduleName + "' and " + this.PROPERTY_NAME + "='" + name + "'";
        Connection con = ConnectionManager.getConnection();
        try {
            Statement st = con.createStatement();
            st.executeQuery(str);
            this.properties.remove(name);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            ConnectionManager.releaseConnection();
        }
    }

    /**
     *  Gets the all Keys of the Module
     *
     *@return    The allKeys value
     */
    public Object[] getAllKeys() {
        Object[] o = this.properties.keySet().toArray();
        return o;
    }

    /**
     *  Description of the Method
     */
    public int clean() {
        Connection con = ConnectionManager.getConnection();
        String str1 = "delete from " + this.SETUP_PROPERTY + " where " +
            this.MODULE_NAME + "='" + this.moduleName + "'";
        String str2 = "delete from " + this.SETUP_MODULE + " where " +
            this.MODULE_NAME + "='" + this.moduleName + "'";
        try {
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            st.executeUpdate(str1);
            st.executeUpdate(str2);
            con.commit();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            ConnectionManager.releaseConnection();
        }
        return 0;
    }

    /**
     *  Description of the Method
     */
    private void init() {
        String moduleStr = "select * from " + this.SETUP_MODULE + " where " +
            this.MODULE_NAME + "='" + this.moduleName + "'";
        String str = "select * from " + this.SETUP_PROPERTY + " where " +
            this.MODULE_NAME + "='" + this.moduleName + "'";
        Connection con = ConnectionManager.getConnection();
        try {

            Statement st = con.createStatement();
            ResultSet rs = null;

            //检查是否存在模块，如果不存在加入
            rs = st.executeQuery(moduleStr);
            if (!rs.next()) {
                String insertString = "insert into " + this.SETUP_MODULE +
                    " values('" + this.moduleName + "')";
                st.execute(insertString);
            }
            else {
                //如果表里已经有了直接从属性表里查询
                rs = st.executeQuery(str);
                while (rs.next()) {
                    String name = rs.getString(this.PROPERTY_NAME);
                    String value = rs.getString(this.PROPERTY_VALUE);
                    this.properties.put(name, value);
                }
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            ConnectionManager.releaseConnection();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     *@return       Description of the Return Value
     */
    private boolean hasProperty(String name) {
        return this.properties.containsKey(name);
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        Object[] s = this.getAllKeys();
        String t = "";
        for (int i = 0; i < s.length; i++) {
            t = t + s[i] + ":" + this.properties.get(s[i]) + ",";
        }
        return t;
    }
}
