//Source file: c:\\src\\com\\ebis\\ebank\\dx\\ControlCenter.java

package com.ebis.ebank.dx;

import com.ebis.ebank.ap.ApDataPoint;
import com.ebis.ebank.defines.*;
/**
 *  Description of the Class
 *
 *@author     sun
 *@created    2003年10月31日
 */
public abstract class ControlCenter {
    private ConfigManager config = ConfigManager.getInstance();


    /**
     *@roseuid    3DEB568D03BB
     */
    public ControlCenter() { }


    /**
     *  1.取得流水号 2.取得Brhid+wsid+tellid 3.组数据包 4.发送
     *
     *@param  apDataExch
     *@param  transID
     *@return             long
     *@roseuid            3DEAFD77030C
     */
    public abstract long send(ApDataPoint apDataExch, String transID);


    /**
     *  接收 是否有后续包 有，则继续接收（构造StringBuffer） 否则 转换成MappingObject
     *
     *@param  apData
     *@param  transSeriesID
     *@param  timeout
     *@return                int
     *@roseuid               3DEAFD890159
     */
    public abstract int receive(ApDataPoint apData, long transSeriesID, long timeout);


    /**
     *  Description of the Method
     *
     *@param  apData         Description of the Parameter
     *@param  transSeriesID  Description of the Parameter
     *@param  timeout        Description of the Parameter
     *@return                Description of the Return Value
     */
    public abstract long servRecv(ApDataPoint apData,Gateway gateway, long timeout);


    /**
     *  Description of the Method
     *
     *@param  apDataExch  Description of the Parameter
     *@param  transID     Description of the Parameter
     *@return             Description of the Return Value
     */
    public abstract long servSend(ApDataPoint apDataExch, long transSeriesID);
}
