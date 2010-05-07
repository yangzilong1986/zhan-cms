//Source file: c:\\src\\com\\ebis\\ebank\\dx\\BTPResourceManager.java

package com.ebis.ebank.dx;

import java.util.*;
import java.io.*;

public class BTPResourceManager
{
   private static LinkedList resources;
   private static byte[] mackey;
   private static byte[] bmkkey;
   private static byte[] pinkey;
   private static BTPResourceManager manager  = null;
   /**
   @roseuid 3DEB568B01D7
    */
   private BTPResourceManager()
   {
       resources = new LinkedList();
       BTPResourceCreate.create(resources);
       bmkkey = BMKStore.read();
       mackey = KEYStore.get(KEYStore.MAC_KEY_FILE);
       pinkey = KEYStore.get(KEYStore.PIN_KEY_FILE);
   }

   public static BTPResourceManager getInstance() {
       if ( manager == null )
           manager = new BTPResourceManager();
       return manager;
   }

   public static byte[] getBMK() {
       return bmkkey;
   }
   public static byte[] getMACK() {
       return mackey;
   }
   public static byte[] getPINK() {
       return pinkey;
   }
   /**
   @return Object
   @roseuid 3DEAE73302A1
    */
   public BTPResource apply(long timeout)
   {
       boolean isTimeout = false;
       synchronized ( resources ) {
           while ( resources.isEmpty() && !isTimeout) {
               try {
                   resources.wait(timeout);
                   if ( resources.isEmpty() )
                       isTimeout = true;
               } catch ( InterruptedException e ) {
                   System.out.println("BTP Source apply Exception:"+e.getMessage());
               }
           }
           if ( isTimeout )
               return null;
           return (BTPResource)resources.removeLast();
       }
   }

   /**
   @param obj
   @roseuid 3DEAE746005A
    */
   public void release(BTPResource obj)
   {
       synchronized ( resources ) {
//           try {
               resources.addFirst(obj);
               resources.notify();
//           } catch ( Exception e ) {}
       }
   }
}
class BTPResourceCreate {
    private static final String filename     = "btpresource.txt";
    private static final String BRHID_NAME   = "brhid";
    private static final String WSID_NAME    = "wsid";
    private static final String TELLID_NAME  = "tellid";
    private static final int    BRHID_LEN    = 12;
    private static final int    WSID_LEN     = 4;
    private static final int    TELLID_LEN   = 4;
    private static final String FILL_CONTENT = " ";

    static void create(LinkedList list) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(filename));
            int count = Integer.parseInt(props.getProperty("count"));
            for ( int i = 0 ; i < count ; i++ ) {
                String brhid  = (String)props.get(BRHID_NAME+i);
                String wsid   = (String)props.get(WSID_NAME+i);
                String tellid = (String)props.get(TELLID_NAME+i);
                if ( brhid == null || wsid == null || tellid == null )
                    continue;
                if ( brhid.length() < BRHID_LEN ) {
                    brhid = fill(brhid,BRHID_LEN,FILL_CONTENT);
                }
                if ( wsid.length() < WSID_LEN ) {
                    wsid  = fill(wsid,WSID_LEN,FILL_CONTENT);
                }
                if ( tellid.length() < TELLID_LEN ) {
                    tellid = fill(tellid,TELLID_LEN,FILL_CONTENT);
                }
                list.add(new BTPResource(brhid,wsid,tellid));
            }
        } catch ( Exception e ) {
        }
    }
    private static String fill(String resource,int len,String content) {
        if ( resource == null || content == null )
            return null;
        if ( resource.length() >= len )
            return resource;
        content = content.substring(0,1);
        StringBuffer buffer = new StringBuffer(resource);
        int length = len - resource.length() ;
        for ( int i = 0 ; i < length ; i++ )
            buffer.append(content);
        return buffer.toString();
    }
    public static void main(String[] argv) {
        BTPResourceManager btprm = BTPResourceManager.getInstance();
        for ( int i = 0 ; i < 10 ; i++ ) {
          BTPResource btpr = btprm.apply(0);
          System.out.println(btpr.getBrhid()+"="+btpr.getWsid()+"="+btpr.getTellid());
        }
    }
}
