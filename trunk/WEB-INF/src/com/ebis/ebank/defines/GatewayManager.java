//Source file: c:\\src\\com\\ebis\\ebank\\defines\\GatewayManager.java

package com.ebis.ebank.defines;


import java.util.*;


public class GatewayManager
{
   private static HashMap gateways;
   private static GatewayManager manager = null;

   /**
    * @roseuid 3DEB6066001D
    */
   private GatewayManager()
   {
       gateways = new HashMap();
   }
   public static GatewayManager getInstance() {
       if ( manager == null ) {
           manager = new GatewayManager();
       }
       return manager;
   }

   /**
    * @param gatewayID
    * @return com.ebis.ebank.defines.Gateway
    * @roseuid 3DEAEFD50083
    */
   public Gateway get(String gatewayID)
   {
       return (Gateway)gateways.get(gatewayID);
   }

   /**
    * @param gateway
    * @roseuid 3DEAEFE702E1
    */
   public void add(Gateway gateway)
   {
       gateways.put(gateway.getGatewayID(),gateway);
   }

   /**
    * @param gatewayID
    * @return boolean
    * @roseuid 3DEAF00C0258
    */
   public boolean contain(String gatewayID)
   {
       return gateways.containsKey(gatewayID);
   }

   public static void main(String[] argv) {
       ParseGateway.parse("");
       GatewayManager manager = GatewayManager.getInstance();
       manager.print();
   }

   public void print() {
       Debug.debug(""+gateways.size());
       Collection cl = gateways.values();
       Debug.debug("======================================");
       for ( Iterator it = cl.iterator() ; it.hasNext() ; ) {
           Gateway o = (Gateway)it.next();
           Debug.debug(o.getGatewayID()+"|"+o.getHostName()+"|"+o.getClassHandler());
       }
       Debug.debug("======================================");
   }
}
