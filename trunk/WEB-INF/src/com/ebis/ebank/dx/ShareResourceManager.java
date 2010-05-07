//Source file: c:\\src\\com\\ebis\\ebank\\dx\\ShareResourceManager.java

package com.ebis.ebank.dx;

import java.util.*;

public class ShareResourceManager
{
   private HashMap res;
   private static ShareResourceManager manager = null;

   /**
   @roseuid 3DEB568B0340
    */
   private ShareResourceManager()
   {
       res = new HashMap();
   }

   public static ShareResourceManager getInstance() {
       if ( manager == null )
           manager = new ShareResourceManager();
       return manager;
   }

   /**
   @param name
   @return Object
   @roseuid 3DEAE838009E
    */
   public Object get(String name)
   {
       return res.get(name);
   }

   /**
   @param name
   @param obj
   @roseuid 3DEAE85C0353
    */
   public void add(String name, Object obj)
   {
       res.put(name,obj);
   }

   /**
   @param name
   @roseuid 3DEAE86E0290
    */
   public void remove(String name)
   {
       res.remove(name);
   }
}
