package com.ebis.ebank.dx;

/**
 * 初始化环境
 *
 * 执行交易XXXX获得主机当前交易日期和MACKEY、PINKEY
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import com.ebis.ebank.ap.*;
import com.ebis.ebank.general.*;
import com.ebis.ebank.dx.*;

public class QueryTest extends Thread {

    private static final String INIT_TXN_CODE = "95G1";
    private ApDataPoint apData = null;
    private int id = 0;

    public QueryTest(int i) {
        apData = new ApDataPoint(new ApDataSpec("1111","",false));
        id = i;
    }
    public void run() {
        int rett = apData.addMeta("curno",1,"curno");
        apData.addRow();
        rett = apData.setData("curno","01");
        try {
            long seriesID = DXGateWay.clientSendTrans(apData,INIT_TXN_CODE);
            apData.clearAll();
            long result = DXGateWay.clientRecvTrans(apData,seriesID,10000);
            if ( result >= 0 ) {
              apData.setCurrentBlock("B990");
              apData.initRead();
              while ( !apData.eof() ) {
                String key = (String) apData.getData("result");
                System.out.println("Thread[" + id + "]" + key);
                byte[] buffer = key.getBytes();
                apData.moveNext();
              }
            }
//            if ( buffer.length == 68 ) {
//                byte[] pin = new byte[16];
//                byte[] mac = new byte[16];
//                System.arraycopy(buffer,4,pin,0,16);
//                System.arraycopy(buffer,38,mac,0,16);
//                KEYStore.store(KEYStore.PIN_KEY_FILE,pin);
//                KEYStore.store(KEYStore.MAC_KEY_FILE,mac);
//            }
        } catch ( Exception e ) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {

        for ( int i = 0 ; i < 1 ; i++ ) {
          QueryTest init = new QueryTest(i);
          init.start();
        }
    }
}
