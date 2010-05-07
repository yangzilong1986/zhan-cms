//Source file: c:\\src\\com\\ebis\\ebank\\defines\\Gateway.java

package com.ebis.ebank.defines;


public class Gateway
{
   private String gatewayID;
   private String hostName;
   private String classHandler;

   /**
    * @roseuid 3DEB6065026A
    */
   public Gateway() {

   }
   /**
    * @param gatewayID
    * @param hostName
    * @param classHandler
    * @roseuid 3DEAF5060369
    */
   public Gateway(String gatewayID, String hostName, String classHandler)
   {
       this.gatewayID    = gatewayID;
       this.hostName     = hostName;
       this.classHandler = classHandler;
   }

   /**
    * @return String
    * @roseuid 3DEAF52C02F5
    */
   public String getGatewayID()
   {
       return gatewayID;
   }

   public void setGatewayID(String gatewayID) {
       this.gatewayID = gatewayID;
   }

   /**
    * @return String
    * @roseuid 3DEAF53A0223
    */
   public String getHostName()
   {
       return hostName;
   }
   public void setHostName(String hostName) {
       this.hostName = hostName;
   }

   /**
    * @return String
    * @roseuid 3DEAF6190224
    */
   public String getClassHandler()
   {
       return classHandler;
   }
   public void setClassHandler(String classHandler) {
       this.classHandler = classHandler;
   }
}
