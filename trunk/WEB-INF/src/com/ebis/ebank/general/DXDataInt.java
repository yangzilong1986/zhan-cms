package com.ebis.ebank.general;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


// parameter passing in and out  is Long

public class DXDataInt implements DXDataGeneric {

   public String Obj2Str(Object obj, String formatstr) {
      if(obj == null)
         return null;

      Long x = new Long(1);

      if(x.getClass().isInstance(obj) ==  false)
         return null;
      else
      {

         String ret = ((Long)obj).toString();
         return ret;
      }

   }

   public Object Str2Obj(String str, String formatstr) {
      if(str == null)
         return null;
      // processing formatstr
      Long ret;
      try
      {
         ret = new Long(Long.parseLong(str));
      }
      catch(Exception e)
      {
         e.toString();
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
        Class c2 = cl.loadClass("com.ebis.ebank.general.DXDataInt");
        DXDataGeneric fp = (DXDataGeneric)c2.newInstance();
        Long dx = new Long(12341);
        String value = fp.Obj2Str(dx, "D");
        System.out.println(value);
        System.out.println(fp.Str2Obj("-34567",""));

      } catch ( Exception e ) {
         e.printStackTrace();
      }



   }

}