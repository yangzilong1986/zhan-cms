package com.ebis.ebank.dx;

import com.ebis.ebank.ap.*;
import com.ebis.ebank.defines.*;
import com.ebis.ebank.comm.*;
import java.util.*;
import java.io.*;
import com.ebis.encrypt.*;
import com.ebis.ebank.general.DXDataGeneric;

public class BTPControlCenter extends ControlCenter {
    private static final int TOAHEADLEN   = 29+2;
    private static final int BCONTROLFLAG = 24+2;
    public static final int TOA_BUFFER_LEN = 1024 * 4;

    public static final int FORM_CODE_LEN = 4;
    public static final int FORM_DATA_LEN = 2;
    public static final int FORM_HEAD_LEN = 8;

    public BTPControlCenter() {
    }



    public long send(ApDataPoint apData, String transID) {
        if ( apData == null || transID == null || transID.length() == 0 )
            return DXErrorCode.TIA_DATA_ERROR;
        /*
          1.申请资源(交易信息、网关信息、交易明细信息、BTP日期、BTP资源、交易流水号)
        */
        ConfigManager config = ConfigManager.getInstance();
        Transaction   trans  = config.getTransaction(transID);           //交易定义
        Gateway       gateway= config.getGateway(trans.getGateWayID());  //网关定义
        List          l      = config.getTransField(transID);            //交易明细定义
        String        date   = DateManager.getInstance().getDate();      //BTP日期
        BTPResourceManager btpManager = BTPResourceManager.getInstance();//BTP资源（网点、终端、柜员）

        //Modify by Rock,
        //BTPResource   btpr   = btpManager.apply(0);
        BTPResource   btpr   = new BTPResource("            ","    ","    ");
        if ( btpr == null ) {
          return DXErrorCode.BTP_RESOURCE_BUSY;
        }
        String        series = SeriesID.apply();                         //交易流水号

        try {
            ByteArrayOutputStream mac  = new ByteArrayOutputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();

            int tialen = 0;

            /*
              2.TIA头部MAC信息源包括主机号、流水号、网点号、终端号
            */
            mac.write(trans.getHostTransID().getBytes());
            if ( date != null && date.length() != 0 )
                mac.write(date.getBytes());
            mac.write(series.getBytes());
            mac.write(btpr.getBrhid().getBytes());
            mac.write(btpr.getWsid().getBytes());

            /*
              3.组织TIA数据区并将参与MAC计算的栏位数据追加至MACDATA中
            */
            apData.initRead();
            for ( int i = 0 ; l != null && i < l.size() ; i++ ) {
                TransactionField field = (TransactionField)l.get(i);
                Object obj   = apData.getData(field.getName());
                String type  = field.getType();
                String value = null;
                if ( type != null && type.length() != 0 ) {
                    try {
                        Class        cl     = this.getClass().getClassLoader().loadClass(type);
                        DXDataGeneric fp     = (DXDataGeneric)cl.newInstance();
                        String       format = field.getFormat();
                                     value  = (String)fp.Obj2Str(value,format);
                    } catch ( Exception e ) {
                        //btpManager.release(btpr);
                        return DXErrorCode.TRANS_TYPE_DEFINE_ERROR;
                    }
                } else {
                    try {
                        value = (String)obj;
                    } catch ( Exception e ) {
                        //btpManager.release(btpr);
                        return DXErrorCode.TRANS_TYPE_DEFINE_ERROR;
                    }
                }

                int fieldlen = field.getLen();
                if ( value == null )
                    value = "";
                if ( value.length() > fieldlen )
                    value = value.substring(0,fieldlen);

                byte[] bfield = value.getBytes();
                int len = bfield.length;
                data.write(len);
                data.write(bfield);
                tialen += 1 + len;

                if ( field.isMac() )
                    mac.write(bfield);
            }
            mac.flush();
            data.flush();

            /*
               4.根据MACDATA得到MAC
            */
            try {
                byte[] macdata;
                byte[] bmk  = btpManager.getBMK();
                byte[] mack = btpManager.getMACK();
                if ( !(bmk == null || bmk.length < 16 ||  mack == null || mack.length < 16 )) {
                  EncryptData encryptor = new EncryptData();
                  macdata = encryptor.getMacData(mac.toByteArray());
                  tialen += macdata.length;
                  data.write(macdata);
                }
            } catch ( Exception e ){
                //btpManager.release(btpr);
                return DXErrorCode.TRANS_MAC_ERROR;
            }
            /*
              5.组TIA数据包
            */
            byte[] head = BTPTxn.makeTiaHead(transID,btpr,date,series,tialen);
            byte[] tia = BTPTxn.combine(head,data.toByteArray());
            mac.close();
            data.close();
            /*
              6.发送
            */
            DataPackUTag packUTag = new DataPackUTag();

            //lu is equals the number in packUTag!
            long lu = CommRouter.clientSend(gateway,tia,tialen+54+2,packUTag);

            if ( lu < 0 ) {
              //btpManager.release(btpr);
              return lu;
            } else {
                /*
                  7.保存该交易的LU号/交易代号/BTP资源
                */
                LuManager lumanager = LuManager.getInstance();
                SeriesSource source = new SeriesSource(packUTag,transID);
                lumanager.add(""+series,source,btpr);
                return Long.parseLong(series);
            }
        } catch ( Exception e ) {
            //btpManager.release(btpr);
            return DXErrorCode.TIA_SEND_EXCEPTION;
        }
    }
    public int receive(ApDataPoint apData, long transSeries, long timeout) {

        /*
           1.获取接受TOA的资源
        */
        LuManager lumanager = LuManager.getInstance();
        String transSeriesID = SeriesID.format((int)transSeries);
        //取得LU资源
        if ( !lumanager.contain(""+transSeriesID) )
            return DXErrorCode.NO_THIS_LU;

        BTPResource  btpr    = (BTPResource)lumanager.getObject(""+transSeriesID);//取得BTP资源

        SeriesSource source  = (SeriesSource)lumanager.getTrans(""+transSeriesID);//取得特定网关的LU和交易代号
        String       transID = source.getTransID();

        DataPackUTag  dataPackUTag = source.getTag();
        ConfigManager config       = ConfigManager.getInstance();                 //取得网关信息
        Transaction   trans        = config.getTransaction(transID);
        Gateway       gateway      = config.getGateway(trans.getGateWayID());

        int toalen    = 0;
        boolean bfail = false;

        try {
            ByteArrayOutputStream toabuf = new ByteArrayOutputStream();
            /*
              2.开始接受TOA()
              没有处理24小时问题，也就是TOAHEAD部分的第二个control为0x80时，下传日期＋PINKEY(32)+MACKEY(32)
            */
           int ilen = 0;
            while ( true ) {
                byte[] toa   =  new byte[TOA_BUFFER_LEN];
                ilen  =  (int)CommRouter.clientRecv(gateway,toa,TOA_BUFFER_LEN,1,dataPackUTag,timeout);
                if ( ilen < 0 ) {
                    break;
                }
                toabuf.write(toa,TOAHEADLEN,ilen-TOAHEADLEN);
                toalen += ilen - TOAHEADLEN;

                if ( (toa[this.BCONTROLFLAG] & 0x01) == 0x01 ) { //BTP处理失败
                    bfail = true;
                }
                if ( (toa[this.BCONTROLFLAG] & 0x80 ) == 0x80 ) { // 有后续包，回执0002交易
                    String date = DateManager.getInstance().getDate();
                    String txnseq = SeriesID.apply();
                    byte[] tia = BTPTxn.makeBTPCont(btpr,date,txnseq);
                    long lu = CommRouter.clientSend(gateway,tia,tia.length,new DataPackUTag());
                    dataPackUTag.dataUID = lu;
                    toabuf.flush();
                    continue;
                }
                if ( (toa[this.BCONTROLFLAG] & 0x40 ) == 0x40 ) {// 结束，不需回执

                }
                if ( (toa[this.BCONTROLFLAG] & 0x80 ) == 0x80 && (toa[this.BCONTROLFLAG] & 0x40 ) == 0x40 ) {// 交易结束，回执交易0001
                    String date   = DateManager.getInstance().getDate();
                    String txnseq = SeriesID.apply();
                    byte[] tia = BTPTxn.makeBTPOver(btpr,date,txnseq);
                    long lu = CommRouter.clientSend(gateway,tia,tia.length,new DataPackUTag());
                }
                break;
            }

            //2.释放BTP资源
            lumanager.remove(""+transSeriesID);
            toabuf.flush();
            if ( ilen > 0 )
              result(apData,toabuf.toByteArray(),toalen,bfail,config);
            toabuf.close();
            return 0;
        } catch ( Exception e ) {
            lumanager.remove(""+transSeriesID);
            return DXErrorCode.RCV_TOA_FAIL;
        }
    }

    /**
     * 处理TOA体
     *
     * @param apData
     * @param toa
     * @param len
     * @param bfail
     * @param config
     */
    private void result(ApDataPoint apData,byte[] toa,int len,boolean bfail,ConfigManager config) {
        try {
            int icurpos = 0; // toa的当前指针
            if ( bfail ) {
                apData.setRequestSuccessed(false);
            } else {
                apData.setRequestSuccessed(true);
            }
            while ( len > FORM_HEAD_LEN ) {
                String form = new String(toa,icurpos,4);
                System.out.println("Form ="+form);
                int formlen = toa[icurpos+4]  >= 0 ? toa[icurpos+4] : (toa[icurpos+4]+256);
                formlen    += (toa[icurpos+5] >= 0 ? toa[icurpos+5] : (toa[icurpos+5]+256))*256;
                if ( form != null ) {
                    if ( !apData.blockExist(form) )
                        apData.addBlock(form); //判断是否存在
                    apData.setCurrentBlock(form);
                    if ( form.startsWith("A") || form.startsWith("S") || form.startsWith("W") ) {
                        if ( !apData.metaExist(form) )
                            apData.addMeta(form,0,"信息提示"); //判断是否存在
                        apData.addRow();
                        String msg = new String(toa,icurpos+FORM_HEAD_LEN,formlen);
                        apData.setData(0,msg);
                    } else {
                        List outfields = config.getTransOutField(form);
                        if ( outfields != null ) {
                            /*
                               设置输出META信息
                            */
                            for ( int i = 0 ; i < outfields.size() ; i++ ) {
                                TransactionOutField outfield = (TransactionOutField)outfields.get(i);
                                if ( outfield == null )
                                    continue;
                                if ( !apData.metaExist(outfield.getName()) )
                                    apData.addMeta(outfield.getName(),0,outfield.getDesc());//判断是否存在
                            }
                            /*
                               添加一行数据
                            */
                            apData.addRow();
                            int iformpos = FORM_HEAD_LEN;
                            for ( int i = 0 ; i < outfields.size() ; i++ ) {
                                TransactionOutField outfield = (TransactionOutField)outfields.get(i);
                                if ( outfield == null )
                                    continue;
                                if ( outfield.isConstant() ) {
                                    apData.setData(outfield.getName(),outfield.getValue());
                                } else {
                                    int fieldlen = (toa[icurpos+iformpos] >= 0) ? toa[icurpos+iformpos]:(toa[icurpos+iformpos]+256);
                                    iformpos += 1;
                                    if ( fieldlen == 0 )
                                        continue;
                                    Object value = new String(toa,icurpos+iformpos,fieldlen);
                                    if ( outfield.getType() != null ) {
                                        try {
                                            Class cl = this.getClass().getClassLoader().loadClass(outfield.getType());
                                            DXDataGeneric fp = (DXDataGeneric)cl.newInstance();
                                            value = fp.Str2Obj((String)value,outfield.getFormat());
                                        } catch ( Exception e ) {
                                        }
                                    }
                                    apData.setData(outfield.getName(),value);
                                    System.out.println(outfield.getName()+"="+value);
                                    iformpos += fieldlen;
                                }
                            }
                        }//OUTFIELD > 0
                    }//FORM!=NULL
                } else {
                    //FORM等于NULL
                }
                icurpos += FORM_HEAD_LEN + formlen;
                len -= FORM_HEAD_LEN + formlen;
            }
        } catch ( Exception e ) {   }
    }






    public long servRecv(ApDataPoint apData,Gateway gateway, long timeout){
      return -1;
    }


/**
 *  Description of the Method
 *
 *@param  apDataExch  Description of the Parameter
 *@param  transID     Description of the Parameter
 *@return             Description of the Return Value
 */
public long servSend(ApDataPoint apDataExch, long transSeriesID){
  return -1;
}

}