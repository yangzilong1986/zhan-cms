//Source file: c:\\src\\com\\ebis\\ebank\\defines\\TransOutFieldManager.java

package com.ebis.ebank.defines;

import java.util.*;

public class TransOutFieldManager
{
   private static HashMap fields;
   private static String  transID;
   private static TransOutFieldManager manager = null;

   /**
    * @roseuid 3DEB6065009D
    */
   private TransOutFieldManager()
   {
       fields = new HashMap();
   }

   public static TransOutFieldManager getInstance() {
       if ( manager == null )
           manager = new TransOutFieldManager();
       return manager;
   }
   /**
    * @param transID
    * @return java.util.List
    * @roseuid 3DEAEF2F012E
    */
   public List get(String transID)
   {
       List l = (List)fields.get(transID);
       Collections.sort(l);
       return l;
   }

   /**
    * @param transID
    * @param fields
    * @roseuid 3DEAEF4100EE
    */
   public void add(String transID, List fields)
   {
       this.fields.put(transID,fields);
   }
   public void setTransID(String transID) {
       this.transID = transID;
   }
   public void add(List fields)
   {
       this.fields.put(transID,fields);
   }

   /**
    * @param transID
    * @return boolean
    * @roseuid 3DEAEF6001BB
    */
   public boolean contain(String transID)
   {
    return fields.containsKey(transID);
   }

   public static void main(String[] argv) {
       TransOutFieldManager manager = ParseTransOutField.parse("");
       manager.print();
   }
   public void print() {

       Debug.debug(""+fields.size());
       Set keys = fields.keySet();
       for ( Iterator it = keys.iterator() ; it.hasNext() ; )
           System.out.println((String)it.next());
       Collection cl = fields.values();
       Debug.debug("======================");
       for ( Iterator it = cl.iterator(); it.hasNext(); ) {
           List l = (List)it.next();
           Collections.sort(l);
           for ( int i = 0 ; i < l.size() ; i++ ) {
               TransactionOutField t = (TransactionOutField)l.get(i);
               Debug.debug(t.getId()+"|"+t.getSeqno()+"|"+t.getName()+"|"+"|"+t.getType()+"|"+t.getFormat());
           }
       }
       Debug.debug("======================");
   }
}
