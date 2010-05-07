

package com.zt.util.setup;

/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003Äê8ÔÂ25ÈÕ
 *@version    1.0
 */

public interface IModule {
    /**
     *  Gets the property attribute of the IModule object
     *
     *@param  name  Description of the Parameter
     *@return       The property value
     */
    public String getProperty(String name);


    /**
     *  Sets the property attribute of the IModule object
     *
     *@param  name   The new property value
     *@param  value  The new property value
     *@return        Description of the Return Value
     */
    public int setProperty(String name, String value);


    /**
     *  Gets the allKeys attribute of the IModule object
     *
     *@return    The allKeys value
     */
    public Object[] getAllKeys();


    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     */
    public void removeProperty(String name);


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    int clean();
    //public int addProperty(String name,String value);
}
