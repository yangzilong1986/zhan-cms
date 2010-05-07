package com.ebis.ebank.dx;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.defines.*;
import com.ebis.ebank.comm.*;
import java.util.*;
import com.ebis.ebank.general.*;
import java.util.logging.*;
import java.io.*;
import com.ebis.encrypt.*;
/**
 *  <p>
 *
 *  Title: </p> <p>
 *
 *  Description: </p> <p>
 *
 *  Copyright: Copyright (c) 2003</p> <p>
 *
 *  Company: </p>
 *
 *@author     not attributable
 *@created    2003年11月3日
 *@version    1.0
 */

public class MidControlCenter extends ControlCenter {
    /**
     *  1.取得流水号 2.取得Brhid+wsid+tellid 3.组数据包 4.发送
     *
     *@author             sun
     *@since              2003年11月3日
     *@param  apDataExch
     *@param  transID
     *@return             long
     *@roseuid            3DEAFD77030C
     */
    private final static int TRANSACTION_ID_LENGTH = 4;
    private final static int TOA_BUFFER_LEN = 1024 * 4;

    private static String[] macFields={"TxId","IdNo","CreditSum","DueDate","CreditInterestRate","AuthorizationValidDate"};
    private static int[] macLength={11,25,13,8,7,8};
   //11+13+8+13+8+1
    /**
     *  Description of the Field
     */
    public static Logger logger = Logger.getLogger("com.ebis.ebank.dx.MidControlCenter");



    /**
     *  Constructor for the MidControlCenter object
     */
    public MidControlCenter() { }


    /**
     *  Description of the Method
     *
     *@param  apData   Description of the Parameter
     *@param  transID  Description of the Parameter
     *@return          Description of the Return Value
     */
    public long send(ApDataPoint apData, String transID) {
        if (apData == null || transID == null || transID.length() == 0) {
            return DXErrorCode.TIA_DATA_ERROR;
        }

        ConfigManager config = ConfigManager.getInstance();
        Transaction trans = config.getTransaction(transID);
        //交易定义
        Gateway gateway = config.getGateway(trans.getGateWayID());
        //网关定义
        List l = config.getTransField(transID);
        //交易明细定义
        String date = DateManager.getInstance().getDate();
        //BTP日期
        BTPResourceManager btpManager = BTPResourceManager.getInstance();
        //BTP资源（网点、终端、柜员）
        BTPResource btpr = btpManager.apply(0);
        DataPackUTag packUTag = new DataPackUTag();
        int currentIndex = 0;

        byte[] buffer = new byte[TOA_BUFFER_LEN];
        currentIndex = makeBytesByApData(l, apData, currentIndex, buffer);

        long lu = 0;
        lu = CommRouter.clientSend(gateway, buffer, currentIndex, packUTag);

        if (lu < 0) {
            btpManager.release(btpr);
            return lu;
        } else {
            return storeLuInfo(packUTag, transID);
        }
    }


    /**
     *  接收 是否有后续包 有，则继续接收（构造StringBuffer） 否则 转换成MappingObject
     *
     *@param  apData
     *@param  transSeriesIDLong  Description of the Parameter
     *@param  timeout
     *@return                    int
     *@roseuid                   3DEAFD890159
     */
    public int receive(ApDataPoint apData, long transSeriesIDLong, long timeout) {
        LuManager lumanager = LuManager.getInstance();

        String transSeriesID = SeriesID.format((int) transSeriesIDLong);
        if (!lumanager.contain("" + transSeriesID)) {
            return DXErrorCode.NO_THIS_LU;
        }

        //BTPResource btpr = (BTPResource) lumanager.getObject("" + transSeriesID);
        //取得BTP资源
        SeriesSource source = (SeriesSource) lumanager.getObject("" +
                transSeriesID);
        //取得特定网关的LU和交易代号
        String transID = source.getTransID();
        DataPackUTag dataPackUTag = source.getTag();
        ConfigManager config = ConfigManager.getInstance();
        //取得网关信息
        Transaction trans = config.getTransaction(transID);
        Gateway gateway = config.getGateway(trans.getGateWayID());

        int currentIndex = 0;
        byte[] buffer = new byte[TOA_BUFFER_LEN];
        DataPackUTag packUTag = source.getTag();

        int ret = 0;
        ret = (int) CommRouter.clientRecv(gateway, buffer, TOA_BUFFER_LEN, 0, packUTag, timeout);
        if (ret < 0) {
            return ret;
        } else {
            makeOutApDataPointMeta(apData, transID);
            List l=config.getTransOutField(transID);
            addRow(apData,buffer,0,l);
            return 0;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  apData   Description of the Parameter
     *@param  gateway  Description of the Parameter
     *@param  timeout  Description of the Parameter
     *@return          Description of the Return Value
     */
    public long servRecv(ApDataPoint apData, Gateway gateway, long timeout) {

        //buffer is the data will contain the infomation send by client!
        byte[] buffer = new byte[TOA_BUFFER_LEN];

        //the uid will contain the lu number send by client
        DataPackUTag uid = new DataPackUTag();
        //The currentIndex record the current position of the byte

        //The lu is the transation series identification!
        //System.out.println("execute one data");
        //long lu=-1;
        long lu = CommRouter.serverRecv(gateway, buffer, TOA_BUFFER_LEN, 0, timeout, uid);
        //1,non block mode
        if (lu < 0) {
            buffer = null;
            return lu;
        } else {
          //System.out.println("execute one data================================recv=1=====");
            //Store the transID
            int currentIndex = 0;
            String transID = new String(buffer, currentIndex, TRANSACTION_ID_LENGTH);
            if(transID == null)
            {
              logger.info("Data Package Received, but TransID is null!");
              buffer = null;
              return -1;
            }

            //ConfigManager
            ConfigManager config = ConfigManager.getInstance();
            Transaction trans = config.getTransaction(transID);
            if(trans == null)
            {
              logger.info("Can not get Transaction Configuration for TransID:"+transID);
              buffer = null;
              return -2;
            }
            logger.info("TxnId "+trans+", extract the data");
            gateway = config.getGateway(trans.getGateWayID());
            List fields = config.getTransField(transID);

            //make the meta data and add a row
            makeApDataPointMeta(apData, transID);
            currentIndex = addRow(apData, buffer, currentIndex, fields);
            buffer = null;
            return storeLuInfo(uid, transID);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  apData    Description of the Parameter
     *@param  seriesId  Description of the Parameter
     *@return           Description of the Return Value
     */
    public long servSend(ApDataPoint apData, long seriesId) {
        //send back the data by apDate, and transSeriesId
        byte[] buffer = new byte[1024 * 32];
        int currentIndex = 0;

        LuManager lumanager = LuManager.getInstance();
        SeriesSource source = (SeriesSource) lumanager.getObject(SeriesID.format((int) seriesId));
        DataPackUTag uid = source.getTag();

        String transID = source.getTransID();

        ConfigManager config = ConfigManager.getInstance();
        Transaction trans = config.getTransaction(transID);
        Gateway gateway = config.getGateway(trans.getGateWayID());
        List fields = config.getTransOutField(transID);

        while(!apData.eof()){
          currentIndex = makeOutBytesByApData(fields, apData, currentIndex, buffer);
          apData.moveNext();
        }
        return CommRouter.serverSend(gateway, buffer, currentIndex, uid);
    }


    /**
     *  Gets the dataGeneric attribute of the MidControlCenter class
     *
     *@param  genericStr  Description of the Parameter
     *@return             The dataGeneric value
     */
    static DXDataGeneric getDataGeneric(String genericStr) {
        if (genericStr == null || genericStr.equals("")) {
            return new DxDataString();
        } else {
            try {
                DXDataGeneric dataGeneric = (DXDataGeneric) Class.forName(
                        genericStr).newInstance();
                return dataGeneric;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
            } catch (InstantiationException ex) {
            } finally {
                return new DxDataString();
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  aim         Description of the Parameter
     *@param  source      Description of the Parameter
     *@param  beginIndex  Description of the Parameter
     *@param  length      Description of the Parameter
     */
    static void fillByte(byte[] aim, byte[] source, int beginIndex, int length) {
        for (int i = 0; i < length; i++) {
            if (source.length <= i) {
                break;
            } else {
                aim[i + beginIndex] = source[i];
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  apData   Description of the Parameter
     *@param  transId  Description of the Parameter
     */
    public static void makeApDataPointMeta(ApDataPoint apData, String transId) {
        ConfigManager config = ConfigManager.getInstance();
        List l = config.getTransField(transId);
        makeApDataPointMeta(apData, l);
    }


    /**
     *  Description of the Method
     *
     *@param  apData   Description of the Parameter
     *@param  transId  Description of the Parameter
     */
    public static void makeOutApDataPointMeta(ApDataPoint apData, String transId) {
        ConfigManager config = ConfigManager.getInstance();
        List l = config.getTransOutField(transId);
        makeOutApDataPointMeta(apData, l);
    }


    /**
     *  Description of the Method
     *
     *@param  apData  Description of the Parameter
     *@param  l       Description of the Parameter
     */
    private static void makeApDataPointMeta(ApDataPoint apData, List l) {
        apData.addBlock("data");
        apData.setCurrentBlock("data");
        for (Iterator iter = l.iterator(); iter.hasNext(); ) {
            TransactionField field = (TransactionField) iter.next();
            apData.addMeta(field.getName(), 0, field.getName());
        }
    }


    /**
     *  Description of the Method
     *
     *@param  apData  Description of the Parameter
     *@param  l       Description of the Parameter
     */
    private static void makeOutApDataPointMeta(ApDataPoint apData, List l) {
        apData.addBlock("data");
        apData.setCurrentBlock("data");
        for (Iterator iter = l.iterator(); iter.hasNext(); ) {
            TransactionOutField field = (TransactionOutField) iter.next();
            apData.addMeta(field.getName(), 0, field.getName());
        }
    }



    /**
     *  Adds a feature to the Row attribute of the MidControlCenter object
     *
     *@param  apData        The feature to be added to the Row attribute
     *@param  buffer        The feature to be added to the Row attribute
     *@param  currentIndex  The feature to be added to the Row attribute
     *@param  l             The feature to be added to the Row attribute
     *@return               Description of the Return Value
     */
    int addRow(ApDataPoint apData, byte[] buffer, int currentIndex, List l) {
        apData.addRow();
        for (Iterator iter = l.iterator(); iter.hasNext(); ) {
            TransactionField field = (TransactionField) iter.next();
            DXDataGeneric generic = getDataGeneric(field.getType());
            Object o = generic.Str2Obj(new String(buffer, currentIndex, field.getLen()), field.getFormat());
            apData.setData(field.getName(), o);
            currentIndex += field.getLen();
        }
        return currentIndex;
    }

    int addOutRow(ApDataPoint apData, byte[] buffer, int currentIndex, List l) {
        apData.addRow();
        for (Iterator iter = l.iterator(); iter.hasNext(); ) {
            TransactionOutField field = (TransactionOutField) iter.next();
            DXDataGeneric generic = getDataGeneric(field.getType());
            Object o = generic.Str2Obj(new String(buffer, currentIndex, field.getLen()), field.getFormat());
            apData.setData(field.getName(), o);
            currentIndex += field.getLen();
        }
        return currentIndex;
    }
    /**
     *  Description of the Method
     *
     *@param  packUTag  Description of the Parameter
     *@param  transID   Description of the Parameter
     *@return           Description of the Return Value
     */
    long storeLuInfo(DataPackUTag packUTag, String transID) {
        String series = SeriesID.apply();
        LuManager lumanager = LuManager.getInstance();
        SeriesSource source = new SeriesSource(packUTag, transID);
        lumanager.add(series, transID, source);
        return Long.parseLong(series);
    }


    /**
     *  This Method convert the apdat
     *
     *@param  fields        Description of the Parameter
     *@param  apData        Description of the Parameter
     *@param  currentIndex  Description of the Parameter
     *@param  buffer        Description of the Parameter
     *@return               Description of the Return Value
     */
    static int makeOutBytesByApData(List fields, ApDataPoint apData, int currentIndex, byte[] buffer) {
        for (Iterator f = fields.iterator(); f.hasNext(); ) {
            TransactionOutField field = (TransactionOutField) f.next();
            if (field.isMac()) {
                byte[] mac = calculateMac(apData);
                fillByte(buffer, mac, currentIndex, field.getLen());
                currentIndex += mac.length;
            } else {
                String dataTypeStr = field.getType();
                DXDataGeneric generic = getDataGeneric(dataTypeStr);
                //System.out.println("datatypestr is "+dataTypeStr);
                String data = generic.Obj2Str(apData.getData(field.getName()), null);
                //if(data == null) System.out.println("fld isnull:"+field.getName());
                if(data != null)
                {
                  fillByte(buffer, data.getBytes(), currentIndex, field.getLen());
                }
                else
                {
                  byte[] a = new byte[field.getLen()];
                  for(int tt=0;tt<field.getLen();tt++) a[tt]= ' ';
                  fillByte(buffer, a, currentIndex, field.getLen());
                }
                currentIndex += field.getLen();
            }
        }
        return currentIndex;
    }


    /**
     *  Description of the Method
     *
     *@param  fields        Description of the Parameter
     *@param  apData        Description of the Parameter
     *@param  currentIndex  Description of the Parameter
     *@param  buffer        Description of the Parameter
     *@return               Description of the Return Value
     */
    public static int makeBytesByApData(List fields, ApDataPoint apData, int currentIndex, byte[] buffer) {
        int index = currentIndex;
        for (Iterator f = fields.iterator(); f.hasNext(); ) {
            TransactionField field = (TransactionField) f.next();
            logger.info("处理field of "+field.getName()+" and filed is mac : "+field.isMac());
            if (field.isMac()) {
                byte[] mac = calculateMac(apData);
                fillByte(buffer, mac, index, field.getLen());
                index += mac.length;
            } else {
                String dataTypeStr = field.getType();
                DXDataGeneric generic = getDataGeneric(dataTypeStr);
                String data = generic.Obj2Str(apData.getData(field.getName()), null);
                fillByte(buffer, data.getBytes(), index, field.getLen());
                index += field.getLen();
            }
        }
        return index;
    }


    //业务号+金额+到期日+利率+授权有效日期+状态
    public static byte[] calculateMac(ApDataPoint data) {
        EncryptData encrypt = new EncryptData();
        return encrypt.getMacData(getRawText(data));
    }

    public static byte[] getRawText(ApDataPoint data) {

        int length=0;
        for (int i = 0; i < macLength.length; i++) {
            length+=macLength[i];
        }
        length+=1;

        byte[] rawText=new byte[length];
        int index=0;
        for (int i = 0; i < macFields.length; i++) {
            //System.out.println("data is " + (String) data.getData(macFields[i]) + " len is:" + ((String) data.getData(macFields[i])).length());
            //byte[] temp = ( (String) data.getData(macFields[i])).getBytes();

            byte[] temp;
            if(data.getData(macFields[i]) != null)
            {
              temp = ( (String) data.getData(macFields[i])).getBytes();
              fillByte(rawText, temp, index, macLength[i]);
              index += macLength[i];
            }
            //else
              //System.out.println("null is :" + macFields[i]);

            //above logic is modified by JGO, cause the data item maybe be null, original program can not deal it
        }
        rawText[length - 1] = (byte)'0';
        //for(int i=0; i<rawText.length;i++)
        //   System.out.print(rawText[i]);
        return rawText;
    }



}
