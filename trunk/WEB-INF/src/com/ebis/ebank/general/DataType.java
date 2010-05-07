//Source file: C:\\Program Files\\SQLLIB\\bin\\com\\ebis\\ebank\\general\\DataType.java

package com.ebis.ebank.general;


public class DataType
{
   public final static int TypeString = 1;
   public final static int TypeNull = 0;
   public final static int TypeNumber = 2;
   public final static int TypeDate = 3;

   /**
   @roseuid 3DDBD9C2009D
    */
   public DataType()
   {

   }

   public static boolean ValidType(int typ)
   {
      if(typ >= TypeString || typ <= TypeDate)
      {
         return true;
      }
      else
      // not use this function currently.
         return true;
   }
}
