//Source file: c:\\src\\com\\ebis\\ebank\\dx\\SeriesID.java

package com.ebis.ebank.dx;


/**
��Ӧ��ˮ�ţ�6λ��,����6λ
 */
public class SeriesID
{
   private static long series = 0;
   private static final long MAX_VALUE = 999999;

   /**
   ��series > 999999ʱ�ָ�Ϊ0
   @return long
   @roseuid 3DEAE4FD0276
    */
   public static synchronized String apply()
   {
       if ( series >= MAX_VALUE )
           series = 1;
       else
           series++;
       String rtnseries = ""+series;
       String tmp = "000000";
       return tmp.substring(0,6-rtnseries.length())+rtnseries;
   }
   public static String format(int series) {
       String rtnseries = ""+series;
       String tmp = "000000";
       return tmp.substring(0,6-rtnseries.length())+rtnseries;
   }
   public static void main(String[] argv) {
       System.out.println(SeriesID.apply());
   }
}
