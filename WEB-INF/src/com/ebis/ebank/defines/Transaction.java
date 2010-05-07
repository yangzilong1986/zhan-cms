//Source file: c:\\src\\com\\ebis\\ebank\\defines\\Transaction.java

package com.ebis.ebank.defines;


public class Transaction
{
   private String id;
   private String hostTP;
   private String gatewayID;
   private String hostTransID;
   private String transOutID;
    private String serviceClass;

   /**
    * @roseuid 3DEB60630267
    */
   public Transaction() {

   }
   /**
    * @param id
    * @param hostTP
    * @param gatewayID
    * @param hostTransID
    * @param transOutID
    * @roseuid 3DEAEABB0071
    */
   public Transaction(String id, String hostTP, String gatewayID, String hostTransID, String transOutID)
   {
       this.id = id;
       this.hostTP = hostTP;
       this.gatewayID = gatewayID;
       this.hostTransID = hostTransID;
       this.transOutID = transOutID;
   }

   /**
    * @return String
    * @roseuid 3DEAEAF102C7
    */
   public String getId()
   {
    return id;
   }
   public void setId(String id) {
       this.id = id;
   }

   /**
    * @return String
    * @roseuid 3DEAEAFF0303
    */
   public String getHostTP()
   {
    return hostTP;
   }

   public void setHostTP(String hostTP) {
       this.hostTP = hostTP;
   }

   /**
    * @return String
    * @roseuid 3DEAEB0A02FF
    */
   public String getGateWayID()
   {
    return gatewayID;
   }
   public void setGatewayID(String gatewayID) {
       this.gatewayID = gatewayID;
   }

   /**
    * @return String
    * @roseuid 3DEAEB15008E
    */
   public String getHostTransID()
   {
    return hostTransID;
   }
   public void setHostTransID(String hostTransID) {
       this.hostTransID = hostTransID;
   }

   /**
    * @return String
    * @roseuid 3DEAEB210045
    */
   public String getTransOutID()
   {
    return transOutID;
   }
   public void setTransOutID(String transOutID) {
       this.transOutID = transOutID;
   }
    public String getServiceClass() {
        return serviceClass;
    }
    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }
}
