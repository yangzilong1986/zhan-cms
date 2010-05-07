//Source file: C:\\Program Files\\SQLLIB\\bin\\com\\ebis\\ebank\\general\\DXDataGeneric.java

package com.ebis.ebank.general;


public interface DXDataGeneric
{
   /**
   @param obj
   @param String formatstr
   @return java.lang.String
   @roseuid 3DF0D65B0205
    */

   abstract public String Obj2Str(Object obj, String formatstr);

   /**
   @param str
   @return Object
   @roseuid 3DF0D66C030E
    */
   abstract public Object Str2Obj(String str, String formatstr);

}
