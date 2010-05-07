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
import com.ebis.ebank.defines.ConfigManager;

public class InitEnvironment {

    private static final String INIT_TXN_CODE = "951C";

    public  static void init(String path) {

        if ( path == null )
            path = "";
        ApDataPoint apData = new ApDataPoint(new ApDataSpec("adminstrator","",false));
        ConfigManager.setPath(path);

        int rett = apData.addMeta("brhid",1,"brhid");
        apData.addRow();
        rett = apData.setData("brhid","988010300");
        try {
            long seriesID = DXGateWay.clientSendTrans(apData,INIT_TXN_CODE);
            apData.clearAll();
            long result = DXGateWay.clientRecvTrans(apData,seriesID,10000);
            String key = (String)apData.getData("key");
            byte[] buffer = key.getBytes();
            if ( buffer.length == 68 ) {
                byte[] pin = new byte[16];
                byte[] mac = new byte[16];
                System.arraycopy(buffer,4,pin,0,16);
                System.arraycopy(buffer,38,mac,0,16);
                KEYStore.store(KEYStore.PIN_KEY_FILE,pin);
                KEYStore.store(KEYStore.MAC_KEY_FILE,mac);
            } else {
                Runtime.getRuntime().exit(-1);
            }
        } catch ( Exception e ) {
            Runtime.getRuntime().exit(-1);
        }
    }
}
