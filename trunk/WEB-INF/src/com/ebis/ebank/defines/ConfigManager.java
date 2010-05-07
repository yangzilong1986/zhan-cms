//Source file: c:\\src\\com\\ebis\\ebank\\defines\\ConfigManager.java

package com.ebis.ebank.defines;

import java.util.List;

public class ConfigManager
{
   private static ConfigManager config = null;
   private static TransactionManager transManager;
   private static TransFieldManager transFieldManager;
   private static TransOutFieldManager transOutFieldManager;
   private static GatewayManager gatewayManager;

   private static String path = "";

   /**
    * @roseuid 3DEB606202D4
    */
   private ConfigManager()
   {
     transManager = ParseTransaction.parse(path);
     transFieldManager = ParseTransactionField.parse(path);
     transOutFieldManager = ParseTransOutField.parse(path);
     gatewayManager = ParseGateway.parse(path);
   }
   /**
    * @param transID
    * @return com.ebis.ebank.defines.Transaction
    * @roseuid 3DEAF2160317
    */
   public Transaction getTransaction(String transID)
   {
       return transManager.get(transID);
   }

   /**
    * @param transID
    * @return java.util.List
    * @roseuid 3DEAF23302F1
    */
   public List getTransField(String transID)
   {
       return transFieldManager.get(transID);
   }

   /**
    * @param transID
    * @return java.util.List
    * @roseuid 3DEAF2490086
    */
   public List getTransOutField(String transID)
   {
       return transOutFieldManager.get(transID);
   }

   /**
    * singletonģʽ
    * @param gatewayID
    * @return com.ebis.ebank.defines.Gateway
    * @roseuid 3DEAF258014F
    */
   public Gateway getGateway(String gatewayID)
   {
       return gatewayManager.get(gatewayID);
   }

   /**
    * @return com.ebis.ebank.defines.ConfigManager
    * @roseuid 3DEAFAD3015B
    */
   public static ConfigManager getInstance()
   {
       if ( config == null )
           config = new ConfigManager();
       return config;
   }
   public static ConfigManager getInstance(String sPath) {
       path = sPath;
       return getInstance();
   }
   public static void setPath(String sPath) {
       path = sPath;
   }

   public static void main(String[] argv) {
       ConfigManager config = ConfigManager.getInstance();
       Gateway g = config.getGateway("166");
       System.out.println(g.getGatewayID()+"|"+g.getHostName()+"|"+g.getClassHandler());

   }
}
