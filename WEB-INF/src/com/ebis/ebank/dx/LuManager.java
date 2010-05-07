//Source file: c:\\src\\com\\ebis\\ebank\\dx\\LuManager.java

package com.ebis.ebank.dx;

import java.util.*;

/**
管理资源
匹配lu
承上启下的作用
 */
public class LuManager
{
   private HashMap lus;
   private HashMap trans;
   private static LuManager manager = new LuManager();

   /**
   @roseuid 3DEB568E0131
    */
   private LuManager()
   {
       lus = new HashMap();
       trans = new HashMap();
   }

   /**
   @param lu
   @param obj
   @roseuid 3DEB17C102C3
    */
   public void add(String lu,Object trans,Object obj)
   {
       lus.put(lu,obj);
       this.trans.put(lu,trans);
   }

   /**
   @param lu
   @param obj
   @roseuid 3DEB175100F5
    */
   public void modifyObject(String lu,Object obj)
   {
       if ( lus.containsKey(lu) )
           lus.put(lu,obj);
   }
   public void modifyTrans(String lu,Object trans) {
       if ( this.trans.containsKey(lu) )
           this.trans.put(lu,trans);
   }
   public Object getTrans(String lu) {
       return trans.get(lu);
   }
   public Object getObject(String lu) {
       return lus.get(lu);
   }

   public String getTrans(long lu){
       return (String)getTrans(SeriesID.format((int)lu));
   }

   public SeriesSource getSeriesSource(long lu){
    return (SeriesSource)getObject(SeriesID.format((int)lu));
}


   /**
   @param lu
   @return Object
   @roseuid 3DEB17E002C8
    */
   public Object remove(String lu)
   {
       trans.remove(lu);
       Object obj = lus.get(lu);
       if ( obj instanceof BTPResource ) {
           BTPResourceManager manager = BTPResourceManager.getInstance();
           manager.release((BTPResource)obj);
       }
       return lus.remove(lu);
   }

   /**
   @param lu
   @return boolean
   @roseuid 3DEB180A02E6
    */
   public boolean contain(String lu)
   {
       return lus.containsKey(lu) && trans.containsKey(lu);
   }

   /**
   @return com.ebis.ebank.dx.LuManager
   @roseuid 3DEB1838017A
    */
   public static LuManager getInstance()
   {
       return manager;
   }
}
