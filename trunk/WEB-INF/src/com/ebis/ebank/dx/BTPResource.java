//Source file: c:\\src\\com\\ebis\\ebank\\dx\\BTPResource.java

package com.ebis.ebank.dx;


public class BTPResource
{
   private String brhid;
   private String wsid;
   private String tellid;

   /**
   @roseuid 3DEB568D00D6
    */

   /**
   @param brhid
   @param wsid
   @param tellid
   @roseuid 3DEAE78D0215
    */
   public BTPResource(String brhid, String wsid, String tellid)
   {
       this.brhid  = brhid;
       this.wsid   = wsid;
       this.tellid = tellid;
   }

   /**
   @return String
   @roseuid 3DEAE7BE0310
    */
   public String getBrhid()
   {
       return brhid;
   }

   /**
   @return String
   @roseuid 3DEAE7CD0090
    */
   public String getWsid()
   {
       return wsid;
   }

   /**
   @return String
   @roseuid 3DEAE7D30310
    */
   public String getTellid()
   {
    return tellid;
   }
}
