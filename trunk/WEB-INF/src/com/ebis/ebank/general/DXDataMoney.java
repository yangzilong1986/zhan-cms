//Source file: C:\\Program Files\\SQLLIB\\bin\\com\\ebis\\ebank\\general\\DXDataMoney.java

package com.ebis.ebank.general;

/**
   passing in and out object type is Double
*/

public class DXDataMoney implements DXDataGeneric
{

   public String Obj2Str(Object obj, String formatstr)
   //not use fromatstr currently
   {
      if(obj == null)
         return null;

      Double x = new Double(1);

      if(x.getClass().isInstance(obj) ==  false)
         return null;
      else
      {

         String ret = Long.toString(
                new Double(((Double)obj).doubleValue()*100).longValue()
               );
         return ret;
      }

   }


   public Object Str2Obj(String str, String formatstr)
   //not use formatstr currently
   {
      if(str == null)
         return null;
      // processing formatstr
      Double ret;
      try
      {
         ret = new Double(Double.parseDouble(str));
      }
      catch(Exception e)
      {
         return null;
      }
      return ret;
   }

   static public void main(String args[])
   {
      ApDataSpec a = new ApDataSpec("asd","sdfs",true);
      try {
         //System.out.println(DXDataDate.getStrCount("rrr454r5r",'r'));
        ClassLoader cl = a.getClass().getClassLoader();
        if(cl == null)
            System.out.println("loader is null");
        Class c2 = cl.loadClass("com.ebis.ebank.general.DXDataMoney");
        DXDataGeneric fp = (DXDataGeneric)c2.newInstance();
        Double dx = new Double(-12341.989);
        String value = fp.Obj2Str(dx, "D");
        System.out.println(value);

        System.out.println(fp.Str2Obj("-234.234",""));

      } catch ( Exception e ) {
         e.printStackTrace();
      }



   }

}
