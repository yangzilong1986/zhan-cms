package com.ebis.ebank.dx;


import com.ebis.ebank.ap.*;
import com.ebis.ebank.general.*;
import com.ebis.ebank.dx.*;

public class btp1161 {

    public static void main(String[] args) {
        btp1161 testBTP1 = new btp1161();
        for ( int i = 0 ; i < 1 ; i++ ) {
            Test1161Thread t = new Test1161Thread(i);
            t.start();
        }
    }
}

class Test1161Thread extends Thread {
    int id ;
    public Test1161Thread(int i) {
        id = i;
    }
    BTPResourceManager manager = BTPResourceManager.getInstance();
    public void run() {

    ApDataSpec sdf = new ApDataSpec("sdf","asdf",false);

    try
    {
    ApDataPoint a = new ApDataPoint(sdf);
    int r;
    r = a.addMeta("acctno",1,null);
    r = a.addMeta("inqtp",1,null);
    r = a.addMeta("date1",1,null);
    r = a.addMeta("date2",1,null);
    r = a.addMeta("disptype",1,null);
    r = a.addRow();
    r = a.setData("acctno","988010320010100000149");
    r = a.setData("inqtp","1");
    r = a.setData("date1","19900101");
    r = a.setData("date2","20080101");
    r = a.setData("disptype","0");
    ApDataPoint.toString(a);
    long ret = DXGateWay.clientSendTrans(a,"1161");
    System.out.println(ret);
    //ApDataPoint.toString(a);
    a.clearAll();
    ret = DXGateWay.clientRecvTrans(a,ret,5000);
    if(a.getRequestSuccessed() == true) //processing transaction successfully
    {
       System.out.println(ret);
       ApDataPoint.toString(a);
    }
    else
    {
      //error found, show error message in ApDataPoint object
      System.out.println("trans porcessing failed");
      ApDataPoint.toString(a);
    }
    }
   catch(Exception e)
   {
      e.printStackTrace();
   }
    /*
        BTPResource r = manager.apply(0);
        if ( r != null )
            System.out.println("Thread "+id+": "+r.getBrhid()+"|"+r.getWsid()+"|"+r.getTellid());
        else
            System.out.println("==null==");
System.out.println("Thread "+id+": sleep");
        try {
            Thread.sleep(23);

        } catch ( Exception e ) {

        }
System.out.println("Thread "+id+": start release");
         manager.release(r);
System.out.println("Thread "+id+": released");
    }
    */
    }
}
