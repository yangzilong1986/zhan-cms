//Source file: C:\\Program Files\\SQLLIB\\bin\\com\\ebis\\ebank\\general\\ApDataSpec.java

package com.ebis.ebank.general;


public class ApDataSpec
{
   private String ApID = null;
   private String CASubjectName = null;
   private boolean CAReady = false;

   /**
   @roseuid 3DECDE1903CF
    */
   public ApDataSpec()
   {

   }

   /**
   @param Apid
   @param CAsub
   @param caReady
   @roseuid 3DECDB7001E5
    */
   public ApDataSpec(String Apid, String CAsub, boolean caReady)
   {
      if(Apid != null)
         this.ApID = Apid;
      if(CAsub != null)
         this.CASubjectName = CAsub;
      this.CAReady = caReady;
   }

   /**
   @return java.lang.String
   @roseuid 3DEBF8FF00D1
    */
   public String getApID()
   {
    return this.ApID;
   }

   /**
   @return boolean
   @roseuid 3DEBF9880237
    */
   public boolean IfCAReady()
   {
    return this.CAReady ;
   }

   /**
   @return java.lang.String
   @roseuid 3DECDAA400DD
    */
   public String getCASubjectName()
   {
    return this.CASubjectName;
   }
}
