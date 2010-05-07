//Source file: c:\\src\\com\\ebis\\ebank\\defines\\TransactionField.java

package com.ebis.ebank.defines;

public class TransactionField implements Comparable
{
   private String id;
   private int seqno;
   private String name;
   private int len;
   private String type;
   private String format;
   private boolean isMac;

   /**
    * @roseuid 3DEB606303BC
    */
   public TransactionField() {

   }
   /**
    * @param id
    * @param seqno
    * @param name
    * @param len
    * @param type
    * @param format
    * @roseuid 3DEAED0F02B2
    */
   public TransactionField(String id, int seqno, String name, int len, String type, String format,boolean isMac)
   {
       this.id = id;
       this.seqno = seqno;
       this.name  = name;
       this.len = len;
       this.type = type;
       this.format = format;
       this.isMac  = isMac;
   }

   /**
    * @return String
    * @roseuid 3DEAED6703BD
    */
   public String getId()
   {
    return id;
   }
   public void setId(String id) {
       this.id = id;
   }

   /**
    * @return int
    * @roseuid 3DEAED6E01C9
    */
   public int getSeqno()
   {
       return seqno;
   }
   public void setSeqno(String seqno) {
       try {
           int iseq = Integer.parseInt(seqno);
           this.seqno = iseq;
       } catch ( Exception e ) {
           this.seqno = 0;
       }
   }

   /**
    * @return String
    * @roseuid 3DEAED7703A2
    */
   public String getName()
   {
       return name;
   }
   public void setName(String name) {
       this.name = name;
   }

   /**
    * @return int
    * @roseuid 3DEAED800319
    */
   public int getLen()
   {
       return len;
   }
   public void setLen(String len) {
       try {
           int ilen = Integer.parseInt(len);
           this.len = ilen;
       } catch ( Exception e ) {
           this.len = 0;
       }
   }

   /**
    * @return String
    * @roseuid 3DEAED8803BB
    */
   public String getType()
   {
       return type;
   }
   public void setType(String type) {
       this.type = type;
   }

   /**
    * @return String
    * @roseuid 3DEAED930136
    */
   public String getFormat()
   {
    return format;
   }
   public void setFormat(String format) {
       this.format = format;
   }

   public void setIsMac(String isMac) {
       if ( isMac != null && (isMac.equals("true") || isMac.equals("1") ) )
           this.isMac = true;
       else
           this.isMac = false;

   }
   public boolean isMac() {
       return isMac;
   }


   /**
    * @param obj
    * @return int
    * @roseuid 3DEAEECC01FE
    */
   public int compareTo(Object obj)
   {
       if ( obj instanceof TransactionField ) {
           TransactionField field = (TransactionField)obj;
           if ( this.seqno > field.getSeqno() ) {
               return 1;
           } else if ( this.seqno < field.getSeqno() ) {
               return -1;
           } else
               return 0;

       } else {
           return -1;
       }
   }

}
