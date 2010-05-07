//Source file: C:\\Program Files\\SQLLIB\\bin\\com\\ebis\\ebank\\general\\DXDataDate.java

package com.ebis.ebank.general;

//////////////////////////////////////////////////////////////////////
/*
  the parameter passing into this class accepts Calendar object.
  the parameter passing out this class is Calendar object.

  formatstr specification
  Y: year
  M: month
  D: day

  for example: current date is 2002/12/02
  format String         Result
  YYYY/MM/DD            2002/12/02
  YYY/M/D               002/2/2
  YY-MM-DD              02-12-02
  YYMMDD                021202

*/
import java.util.Calendar;

public class DXDataDate implements DXDataGeneric
{
   static int getStrCount(String source, char target)
   {
      int count = 0;
      int beg = 0;
      while(true)
      {
         beg = source.indexOf(target,beg);
         if(beg >= 0)
         {
            count++;
            beg++;
         }
         else
            break;
      }
      return count;
   }
   public String Obj2Str(Object obj, String formatstr)
   {
      Calendar date = Calendar.getInstance();
      if(date.getClass().isInstance(obj) ==  false)
         return null;
      else
      {
         String format;
         String year = new String(Integer.toString(
            (( Calendar)obj).get(Calendar.YEAR) ));
         String month = new String(Integer.toString(
            ((Calendar)obj).get(Calendar.MONTH)));
         String day = new String(Integer.toString(
            ((Calendar)obj).get(Calendar.DAY_OF_MONTH)));
         java.lang.StringBuffer ret = new StringBuffer();

         int yearpos = year.length();
         int monthpos = month.length() ;
         int daypos = day.length() ;


         if(formatstr != null)
            format = formatstr;
         else
            format = new String("YYYYMMDD");

         int yearlen = DXDataDate.getStrCount(format,'Y');
         int monthlen = DXDataDate.getStrCount(format,'M');
         int daylen = DXDataDate.getStrCount(format,'D');

         // porcessing formatstr
         for(int i = 0; i < format.length();)
         {
            switch(format.charAt(i))
            {
               case 'Y':

                  if(yearlen > yearpos)
                  {
                     ret.append('0');
                  }
                  else
                  {
                     ret.append(year.charAt(yearpos-yearlen));
                  }

                  yearlen--;

                  break;
               case 'M':
                  if(monthlen > monthpos)
                     ret.append('0');
                  else
                  {
                     ret.append(month.charAt(monthpos-monthlen));
                  }

                  monthlen--;

                  break;
               case 'D':
                  if(daylen > daypos)
                     ret.append('0');
                  else
                  {
                     ret.append(day.charAt(daypos - daylen));
                  }

                  daylen--;

                  break;
               default:
                  ret.append(format.charAt(i));
            }
            i++;
         }
         return ret.toString();
      }

   }


   public Object Str2Obj(String str, String formatstr)
   {
      String format;
      if(str == null)
         return null;
      if(formatstr == null)
         format = new String("YYYYMMDD");
      else
         format = formatstr;

      java.lang.StringBuffer year = new StringBuffer();
      java.lang.StringBuffer month = new StringBuffer();
      java.lang.StringBuffer day = new StringBuffer();

      for(int i = 0; i < str.length() && i < format.length();)
      {
         switch(format.charAt(i))
         {
            case 'Y':
               year.append(str.charAt(i));
               break;
            case 'M':
               month.append(str.charAt(i));
               break;
            case 'D':
               day.append(str.charAt(i));
               break;
            default:

         }

         i++;
      }

      Calendar cal = Calendar.getInstance();
      try
      {
         cal.set(
            Integer.parseInt(year.toString() ),
            Integer.parseInt(month.toString() ),
            Integer.parseInt(day.toString() )
            );
      }
      catch(Exception e)
      {
         cal = null;
      }

      return cal;
   }

   static public void main(String args[])
   {
      ApDataSpec a = new ApDataSpec("asd","sdfs",true);
      try {
         //System.out.println(DXDataDate.getStrCount("rrr454r5r",'r'));
        ClassLoader cl = a.getClass().getClassLoader();
        if(cl == null)
            System.out.println("loader is null");
        Class c2 = cl.loadClass("com.ebis.ebank.general.DXDataDate");
        DXDataGeneric fp = (DXDataGeneric)c2.newInstance();
        java.util.Calendar dx = java.util.Calendar.getInstance();
        String value = fp.Obj2Str(dx, "Y-YYYY-MMM-DD");
        System.out.println(value);

        System.out.println(
            fp.Str2Obj("2003-09-19","YYYY-MM-DD")
            );
      } catch ( Exception e ) {
         e.printStackTrace();
      }



   }

}
