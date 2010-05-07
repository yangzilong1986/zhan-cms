//Source file: c:\\src\\com\\ebis\\ebank\\defines\\TransactionOutFields.java

package com.ebis.ebank.defines;


public class TransactionOutField implements Comparable
{
   private String id;
   private int seqno;
   private boolean isConstant = false;
   private String value;
   private String name;
   private String type;
   private String format;
   private String desc;
   private int len;

   /**
    * @roseuid 3DEB606402C3
    */
   public TransactionOutField() {

   }
   /**
    * @param id
    * @param seqno
    * @param name
    * @param type
    * @param format
    * @roseuid 3DEAEE39024D
    */
   public TransactionOutField(String id, int seqno, String name, String type, String format)
   {
       this.id = id;
       this.seqno = seqno;
       this.name  = name;
       this.type  = type;
       this.format = format;
   }

   /**
    * @return String
    * @roseuid 3DEAEE890234
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
    * @roseuid 3DEAEE9000CB
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
    * @roseuid 3DEAEE9A012A
    */
   public String getName()
   {
    return name;
   }
   public void setName(String name) {
       this.name = name;
   }

   /**
    * @return String
    * @roseuid 3DEAEEA1009E
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
    * @roseuid 3DEAEEAA0213
    */
   public String getFormat()
   {
    return format;
   }
   public void setFormat(String format) {
       this.format = format;
   }

   public String getDesc() {
       return desc;
   }
   public void setDesc(String desc) {
       this.desc = desc;
   }


   public void isConstant(String constant) {
       if ( constant == null ) {
           isConstant = false;
           return;
       }
       if ( constant.toLowerCase().equals("true") || constant.equals("1") ) {
           isConstant = true;
       }
       isConstant = false;
   }
   public boolean isConstant() {
       return isConstant;
   }
   public void setValue(String value) {
       this.value = value;
   }
   public String getValue() {
       return value;
   }
   /**
    * @param obj
    * @return int
    * @roseuid 3DEAEEF603A3
    */
   public int compareTo(Object obj)
   {
       if ( obj instanceof TransactionOutField ) {
           TransactionOutField field = (TransactionOutField)obj;
           if ( this.seqno < field.getSeqno() )
               return -1;
           else if ( this.seqno > field.getSeqno() )
               return 1;
           else
               return 0;
       } else {
           return -1;
       }
   }
    public int getLen() {
        return len;
    }

    public void setLen(String len) {
        try {
            int l = Integer.parseInt(len);
            this.len = l;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.len=0;
        }
    }

    public boolean isMac(){
        if(getName().toLowerCase().trim().equals("mac")){
            return true;
        }else{
            return false;
        }
    }
}