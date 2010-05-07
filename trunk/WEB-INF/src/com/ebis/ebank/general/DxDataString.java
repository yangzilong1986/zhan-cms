//Source file: C:\\Program Files\\SQLLIB\\bin\\com\\ebis\\ebank\\general\\DxDataString.java

package com.ebis.ebank.general;


public class DxDataString implements DXDataGeneric
{
   public String Obj2Str(Object obj, String formatstr)
   {
      String str = "";
      if(str.getClass().isInstance(obj) ==  false)
         return null;
      else
      {
         // porcessing formatstr
         str = new String((String)obj);
         return str;
      }

   }


   public Object Str2Obj(String str, String formatstr)
   {
      // processing formatstr
      return new String(str);
   }


   static public void main(String args[])
   {
      ApDataSpec a = new ApDataSpec("asd","sdfs",true);
      try {
        ClassLoader cl = a.getClass().getClassLoader();
        if(cl == null)
            System.out.println("loader is null");
        Class c2 = cl.loadClass("com.ebis.ebank.general.DxDataString");
        DXDataGeneric fp = (DXDataGeneric)c2.newInstance();
        String value = fp.Obj2Str("yes yes", "sdf");
        System.out.println(value);
      } catch ( Exception e ) {
         e.printStackTrace();
      }



   }
}
