//Source file: c:\\src\\com\\ebis\\ebank\\defines\\TransFieldManager.java

package com.ebis.ebank.defines;

import java.util.*;

public class TransFieldManager
{
   private static HashMap transactions;
   private static String  transID = null;
   private static TransFieldManager manager = null;

   /**
    * @roseuid 3DEB60640182
    */
   private TransFieldManager()
   {
       transactions = new HashMap();
   }
   public static TransFieldManager getInstance() {
       if ( manager == null )
           manager = new TransFieldManager();
       return manager;
   }

   /**
    * @param transID
    * @return java.util.List
    * @roseuid 3DEAEBFD033B
    */
   public List get(String transID)
   {
       List l = (List)transactions.get(transID);
       Collections.sort(l);
       return l;
   }

   /**
    * @param transID
    * @param fields
    * @roseuid 3DEAECBB033E
    */
   public void setTransID(String transID) {
       this.transID = transID;
   }
   public void add(List fields)
   {
       transactions.put(transID,fields);
//       if ( fields.size() > 0 ) {
//           TransactionField field = (TransactionField)fields.get(0);
//           String transID = field.getId();
//           transactions.put(transID,fields);
//       }
   }
   public void add(String transID,List fields)
   {
       transactions.put(transID,fields);
   }

   /**
    * @param transID
    * @return boolean
    * @roseuid 3DEAECFA008B
    */
   public boolean contain(String transID)
   {
    return transactions.containsKey(transID);
   }

   public static void main(String[] argv) {
       TransFieldManager manager = ParseTransactionField.parse("");
       manager.print();
   }
   public void print() {

       Debug.debug(""+transactions.size());
       Set keys = transactions.keySet();
       for ( Iterator it = keys.iterator() ; it.hasNext() ; )
           System.out.println((String)it.next());
       Collection cl = transactions.values();
       Debug.debug("======================");
       for ( Iterator it = cl.iterator(); it.hasNext(); ) {
           List l = (List)it.next();
           Collections.sort(l);
           for ( int i = 0 ; i < l.size() ; i++ ) {
               TransactionField t = (TransactionField)l.get(i);
               Debug.debug(t.getId()+"|"+t.getSeqno()+"|"+t.getName()+"|"+t.getLen()+"|"+t.getType()+"|"+t.getFormat());
           }
       }
       Debug.debug("======================");
   }
}
