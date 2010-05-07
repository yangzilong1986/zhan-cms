//Source file: c:\\src\\com\\ebis\\ebank\\defines\\TransactionManager.java

package com.ebis.ebank.defines;

import java.util.*;

public class TransactionManager
{
   private static HashMap transactions;
   private static TransactionManager manager = null;

   /**
    * @roseuid 3DEB606601E9
    */
   private TransactionManager()
   {
       transactions = new HashMap();
   }
   public static TransactionManager getInstance() {
       if ( manager == null )
           manager = new TransactionManager();
       return manager;
   }

   /**
    * @param transID
    * @return com.ebis.ebank.defines.Transaction
    * @roseuid 3DEAF0680020
    */
   public Transaction get(String transID)
   {
    return (Transaction)transactions.get(transID);
   }

   /**
    * @param transaction
    * @roseuid 3DEAF0770220
    */
   public void add(Transaction transaction)
   {
       transactions.put(transaction.getId(),transaction);
   }

   /**
    * @param transID
    * @return boolean
    * @roseuid 3DEAF099025B
    */
   public boolean contain(String transID)
   {
       return transactions.containsKey(transID);
   }
   public static void main(String[] argv) {
       TransactionManager manager = ParseTransaction.parse("");
       manager.print();
   }
   public void print() {
       Debug.debug(""+transactions.size());
       Collection cl = transactions.values();
       Debug.debug("=========================");
       for ( Iterator it = cl.iterator();it.hasNext(); ) {
           Transaction t = (Transaction)it.next();
           Debug.debug(t.getId()+"|"+t.getHostTP()+"|"+t.getGateWayID()+"|"+t.getHostTransID()+"|"+t.getTransOutID()+"|"+t.getServiceClass());
       }
       Debug.debug("=========================");
   }
}
