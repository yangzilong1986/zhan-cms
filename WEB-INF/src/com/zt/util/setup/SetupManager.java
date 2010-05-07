//$Id: SetupManager.java,v 1.1 2007/04/28 14:08:11 liuj Exp $
package com.zt.util.setup;

import java.util.*;
import java.security.InvalidParameterException;

/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003年8月19日
 */
public class SetupManager {

    private HashMap modules = null;
    private String moduleName = null;
    private static SetupManager manager = null;
    private static Object managerLock = new Object();
    private Object propertiesLock = new Object();


    private static long lastAccessTime=-1;


    /**
     *  Constructor for the SetupManager object
     */
    private SetupManager() {
        this.modules = ModuleFactory.findAllModules();
        System.out.println("new module");
    }

    public static void refresh()
    {
        synchronized (managerLock) {
            if (manager == null) {
                manager = new SetupManager();
            }
        }

    }


    /**
     *  Some problem
     *
     *@return    The instance value
     */
    private static SetupManager getInstance() {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }
        return manager;
    }


    /**
     *  Gets the property attribute of the SetupManager class
     *
     *@param  name  这个名字必须符合“moduleName:propertyName”的形式,不为空。
     *@return       如果name为空,返回空,如果moduleName,propertyName有一项为空则返回为空<br>
     *      PreCondition:数据库能够连接正常，其他配置正确.<br>
     *      PostedCondition:不做任何改变
     */
    public static String getProperty(String name) {
        long currentTime = System.currentTimeMillis();
        if (manager == null||(currentTime-lastAccessTime)>1000*6) {
            synchronized (managerLock) {
                if (manager == null||(currentTime-lastAccessTime)>1000*60*60) {
                    manager = new SetupManager();
                    lastAccessTime =currentTime;
                }
            }
        }


        if (name == null) {
            System.err.println("Name or value is null");
            return null;
        }
        String[] temp = name.split(":");
        if (temp.length != 2) {
            return null;
        } else {
            return manager.getProp(temp[0], temp[1]);
        }
    }


    /**
     *  直接返回转化成int的值
     *
     *@param  name  Description of the Parameter
     *@return       The intProperty value
     */
    public static int getIntProperty(String name) {
        return Integer.parseInt(getProperty(name));
    }


    /**
     *  Gets the longProperty attribute of the SetupManager class
     *
     *@param  name  Description of the Parameter
     *@return       The longProperty value
     */
    public static long getLongProperty(String name) {
        return Long.parseLong(getProperty(name));
    }



    /**
     *  Gets the floatProperty attribute of the SetupManager class
     *
     *@param  name  Description of the Parameter
     *@return       The floatProperty value
     */
    public static float getFloatProperty(String name) {
        return Float.parseFloat(getProperty(name));
    }


    /**
     *  Gets the doubleProperty attribute of the SetupManager class
     *
     *@param  name  Description of the Parameter
     *@return       The doubleProperty value
     */
    public static double getDoubleProperty(String name) {
        return Double.parseDouble(getProperty(name));
    }


    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     *@return       Description of the Return Value
     */
    public static Date getDateProperty(String name) {
        return getCalendarProperty(name).getTime();
    }


    /**
     *  Gets the calendarProperty attribute of the SetupManager class
     *
     *@param  name  Description of the Parameter
     *@return       The calendarProperty value
     */
    public static Calendar getCalendarProperty(String name) {
        String dateStr = getProperty(name);
        String[] theDate = dateStr.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(theDate[0]), Integer.parseInt(theDate[1]) - 1,
                Integer.parseInt(theDate[2]));
        return c;
    }


    /**
     *  /** Gets the prop attribute of the SetupManager object
     *
     *@param  moduleName    Description of the Parameter
     *@param  propertyName  Description of the Parameter
     *@return               The prop value
     */

    private String getProp(String moduleName, String propertyName) {
        synchronized (propertiesLock) {
            IModule module = ((IModule) this.modules.get(moduleName));
            if (module == null) {
                System.err.println("***ModuleName: '" + moduleName + "' is invalid!");
                return null;
            } else {
                return module.getProperty(propertyName);
            }
        }
    }


    /**
     *  Sets the property attribute of the SetupManager class
     *
     *@param  name   这个名字必须符合“moduleName:propertyName”的形式,不为空。
     *@param  value  这个值不能为空，否则不执行。<br>
     *      PreCondition:数据库能够连接正常，其他配置正确，并且已经存在相应的Module<br>
     *      PostedCondition:不管原来这个propertyName存在不存在,执行以后通过
     *      SetupManager.getProperty(name)；可以得到这个propertyName的值。
     *      通过SetupManager.removeProperty(name)去掉这个值.
     */
    public static void setProperty(String name, String value) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }
        if (name == null || value == null) {
            System.err.println("Name or value is null");
            return;
        }
        String[] temp = name.split(":");
        if (temp.length != 2) {
        } else {
            manager.setProp(temp[0], temp[1], value);
        }
    }


    /**
     *  Sets the prop attribute of the SetupManager object
     *
     *@param  moduleName    The new prop value
     *@param  propertyName  The new prop value
     *@param  value         The new prop value
     */
    public void setProp(String moduleName, String propertyName, String value) {
        synchronized (propertiesLock) {
            IModule module = ((IModule) this.modules.get(moduleName));
            if (module == null) {
                System.err.println("+++ModuleName: '" + moduleName + "' is invalid!");
            } else {
                module.setProperty(propertyName, value);
            }
        }
    }


    /**
     *  Gets the properties attribute of the SetupManager class
     *
     *@param  name  Description of the Parameter
     *@return       The properties value
     */
    static Object[] getProperties(String name) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }
//    System.err.println("???"+name);
        return manager.getProps(name);
    }


    /**
     *  Gets the props attribute of the SetupManager object
     *
     *@param  name  Description of the Parameter
     *@return       The props value
     */
    private Object[] getProps(String name) {
        synchronized (propertiesLock) {
            IModule module = ((IModule) this.modules.get(name));
            if (module == null) {
                System.err.println("---ModuleName: '" + name + "'   is invalid ! ---");
                return null;
            } else {
                return module.getAllKeys();
            }
        }
    }


    /**
     *  Adds a feature to the Module attribute of the SetupManager class
     *
     *@param  moduleName  The feature to be added to the Module attribute
     */
    static void addModule(String moduleName) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }
        manager.addMod(moduleName);
    }


    /**
     *  Description of the Method
     *
     *@param  moduleName  Description of the Parameter
     */
    static void removeModule(String moduleName) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }
        manager.removeMod(moduleName);
    }


    /**
     *  Adds a feature to the Mod attribute of the SetupManager object
     *
     *@param  moduleName  The feature to be added to the Mod attribute
     */
    private void addMod(String moduleName) {
        synchronized (propertiesLock) {
            if (this.modules.get(moduleName) == null) {
                IModule module = ModuleFactory.newModule(moduleName);
                this.modules.put(moduleName, module);
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  moduleName  Description of the Parameter
     */
    private void removeMod(String moduleName) {
        synchronized (propertiesLock) {
            Object mod = modules.get(moduleName);
            if (mod != null) {
                ((IModule) mod).clean();
                this.modules.remove(moduleName);
            }
        }
    }


    /**
     *  Gets the modules attribute of the SetupManager class
     *
     *@return    The modules value
     */
    static Object[] getModules() {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }
        return manager.getMods();
    }


    /**
     *  Gets the mods attribute of the SetupManager object
     *
     *@return    The mods value
     */
    private Object[] getMods() {
        synchronized (propertiesLock) {
            Object mod = modules.get(moduleName);
            return modules.keySet().toArray();
        }
    }


    /**
     *@param  name  这个名字必须符合“moduleName:propertyName”的形式,不为空。如果为空则或不符合条件不操作<br>
     *      PreCondition:数据库能够连接正常，其他配置正确.<br>
     *      PostedCondition:结果是SetupManager.getProperty(name)返回null(name是与参数相同)。
     */
    public static void removeProperty(String name) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new SetupManager();
                }
            }
        }

        if (name == null) {
            System.err.println("Name or value is null");
            return;
        }

        String[] temp = name.split(":");
        if (temp.length != 2) {
            return;
        } else {
            manager.removeProp(temp[0], temp[1]);
        }

    }


    /**
     *  Description of the Method
     *
     *@param  moduleName    Description of the Parameter
     *@param  propertyName  Description of the Parameter
     */
    private void removeProp(String moduleName, String propertyName) {
        synchronized (propertiesLock) {
            Object mod = modules.get(moduleName);
            if (mod == null) {
                System.err.println("---ModuleName: '" + moduleName +
                        "'   is invalid ! ---");
            } else {
                ((IModule) mod).removeProperty(propertyName);
            }
        }
    }

}
