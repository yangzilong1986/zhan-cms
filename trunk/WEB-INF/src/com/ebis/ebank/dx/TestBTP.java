package com.ebis.ebank.dx;

public class TestBTP {

    public TestBTP() {
    }
    public static void main(String[] args) {
        TestBTP testBTP1 = new TestBTP();
        for ( int i = 0 ; i < 9 ; i++ ) {
            TestThread t = new TestThread(i);
            t.start();
        }
    }
}
class TestThread extends Thread {
    int id ;
    public TestThread(int i) {
        id = i;
    }
    BTPResourceManager manager = BTPResourceManager.getInstance();
    public void run() {
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
}
