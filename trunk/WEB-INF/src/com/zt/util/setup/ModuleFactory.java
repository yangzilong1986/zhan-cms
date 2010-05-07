package com.zt.util.setup;


import java.util.*;
import java.sql.*;
//import com.zt.db.*;

/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003Äê9ÔÂ2ÈÕ
 */
public class ModuleFactory {
    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public static HashMap findAllModules() {
        Connection con = ConnectionManager.getConnection();
        try {
            HashMap modules = new HashMap();
            Collection keys=new Vector();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * from setup_module order by module_name");

            String moduleName = "";
            IModule module = null;
            while (rs.next()) {
                keys.add(rs.getString(1));
            }
            ConnectionManager.releaseConnection();
            for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
                Object item = (Object)iter.next();
                modules.put(item,new Module((String)item));
            }
            return modules;
        } catch (SQLException ex) {

            System.err.println("ModuleFactory init err");
            ex.printStackTrace();
        }
        return null;
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     *@return       Description of the Return Value
     */
    public static IModule newModule(String name) {
        return new Module(name);
    }
}
