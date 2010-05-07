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
          1.������Դ(������Ϣ��������Ϣ��������ϸ��Ϣ��BTP���ڡ�BTP��Դ��������ˮ��)
        */
        ConfigManager config = ConfigManager.getInstance();
        Transaction   trans  = config.getTransaction(transID);           //���׶���
        Gateway       gateway= config.getGateway(trans.getGateWayID());  //���ض���
        List          l      = config.getTransField(transID);            //������ϸ����
        String        date   = DateManager.getInstance().getDate();      //BTP����
        BTPResourceManager btpManager = BTPResourceManager.getInstance();//BTP��Դ�����㡢�նˡ���Ա��

        //Modify by Rock,
        //BTPResource   btpr   = btpManager.apply(0);
        BTPResource   btpr   = new BTPResource("            ","    ","    ");
        if ( btpr == null ) {
          return DXErrorCode.BTP_RESOURCE_BUSY;
        }
        String        series = SeriesID.apply();                         //������ˮ��

        try {
            ByteArrayOutputStream mac  = new ByteArrayOutputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();

            int tialen = 0;

            /*
              2.TIAͷ��MAC��ϢԴ���������š���ˮ�š�����š��ն˺�
            */
            mac.write(trans.getHostTransID().getBytes());
            if ( date != null && date.length() != 0 )
                mac.write(date.getBytes());
            mac.write(series.getBytes());
            mac.write(btpr.getBrhid().getBytes());
            mac.write(btpr.getWsid().getBytes());

            /*
              3.��֯TIA��������������MAC�������λ����׷����MACDATA��
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
               4.����MACDATA�õ�MAC
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
              5.��TIA���ݰ�
            */
            byte[] head = BTPTxn.makeTiaHead(transID,btpr,date,series,tialen);
            byte[] tia = BTPTxn.combine(head,data.toByteArray());
            mac.close();
            data.close();
            /*
              6.����
            */
            DataPackUTag packUTag = new DataPackUTag();

            //lu is equals the number in packUTag!
            long lu = CommRouter.clientSend(gateway,tia,tialen+54+2,packUTag);

            if ( lu < 0 ) {
              //btpManager.release(btpr);
              return lu;
            } else {
                /*
                  7.����ý��׵�LU��/���״���/BTP��Դ
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
           1.��ȡ����TOA����Դ
        */
        LuManager lumanager = LuManager.getInstance();
        String transSeriesID = SeriesID.format((int)transSeries);
        //ȡ��LU��Դ
        if ( !lumanager.contain(""+transSeriesID) )
            return DXErrorCode.NO_THIS_LU;

        BTPResource  btpr    = (BTPResource)lumanager.getObject(""+transSeriesID);//ȡ��BTP��Դ

        SeriesSource source  = (SeriesSource)lumanager.getTrans(""+transSeriesID);//ȡ���ض����ص�LU�ͽ��״���
        String       transID = source.getTransID();

        DataPackUTag  dataPackUTag = source.getTag();
        ConfigManager config       = ConfigManager.getInstance();                 //ȡ��������Ϣ
        Transaction   trans        = config.getTransaction(transID);
        Gateway       gateway      = config.getGateway(trans.getGateWayID());

        int toalen    = 0;
        boolean bfail = false;

        try {
            ByteArrayOutputStream toabuf = new ByteArrayOutputStream();
            /*
              2.��ʼ����TOA()
              û�д���24Сʱ���⣬Ҳ����TOAHEAD���ֵĵڶ���controlΪ0x80ʱ���´����ڣ�PINKEY(32)+MACKEY(32)
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

                if ( (toa[this.BCONTROLFLAG] & 0x01) == 0x01 ) { //BTP����ʧ��
                    bfail = true;
                }
                if ( (toa[this.BCONTROLFLAG] & 0x80 ) == 0x80 ) { // �к���������ִ0002����
                    String date = DateManager.getInstance().getDate();
                    String txnseq = SeriesID.apply();
                    byte[] tia = BTPTxn.makeBTPCont(btpr,date,txnseq);
                    long lu = CommRouter.clientSend(gateway,tia,tia.length,new DataPackUTag());
                    dataPackUTag.dataUID = lu;
                    toabuf.flush();
                    continue;
                }
                if ( (toa[this.BCONTROLFLAG] & 0x40 ) == 0x40 ) {// �����������ִ

                }
                if ( (toa[this.BCONTROLFLAG] & 0x80 ) == 0x80 && (toa[this.BCONTROLFLAG] & 0x40 ) == 0x40 ) {// ���׽�������ִ����0001
                    String date   = DateManager.getInstance().getDate();
                    String txnseq = SeriesID.apply();
                    byte[] tia = BTPTxn.makeBTPOver(btpr,date,txnseq);
                    long lu = CommRouter.clientSend(gateway,tia,tia.length,new DataPackUTag());
                }
                break;
            }

            //2.�ͷ�BTP��Դ
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
     * ����TOA��
     *
     * @param apData
     * @param toa
     * @param len
     * @param bfail
     * @param config
     */
    private void result(ApDataPoint apData,byte[] toa,int len,boolean bfail,ConfigManager config) {
        try {
            int icurpos = 0; // toa�ĵ�ǰָ��
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
                        apData.addBlock(form); //�ж��Ƿ����
                    apData.setCurrentBlock(form);
                    if ( form.startsWith("A") || form.startsWith("S") || form.startsWith("W") ) {
                        if ( !apData.metaExist(form) )
                            apData.addMeta(form,0,"��Ϣ��ʾ"); //�ж��Ƿ����
                        apData.addRow();
                        String msg = new String(toa,icurpos+FORM_HEAD_LEN,formlen);
                        apData.setData(0,msg);
                    } else {
                        List outfields = config.getTransOutField(form);
                        if ( outfields != null ) {
                            /*
                               �������META��Ϣ
                            */
                            for ( int i = 0 ; i < outfields.size() ; i++ ) {
                                TransactionOutField outfield = (TransactionOutField)outfields.get(i);
                                if ( outfield == null )
                                    continue;
                                if ( !apData.metaExist(outfield.getName()) )
                                    apData.addMeta(outfield.getName(),0,outfield.getDesc());//�ж��Ƿ����
                            }
                            /*
                               ���һ������
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
                    //FORM����NULL
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