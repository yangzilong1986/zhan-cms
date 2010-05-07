package com.ebis.ebank.startup;

import java.util.*;
import com.ebis.ebank.ap.*;
import com.ebis.ebank.defines.*;
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
 *@created    2003Äê11ÔÂ7ÈÕ
 *@version    1.0
 */

public class TransTask {

    private ApDataPoint data;
    private Gateway gateway;
    private long series;


    /**
     *  Constructor for the TransTask object
     *
     *@param  data     Description of the Parameter
     *@param  gateway  Description of the Parameter
     *@param  series   Description of the Parameter
     */
    public TransTask(ApDataPoint data, Gateway gateway, long series) {
        this.data = data;
        this.gateway = gateway;
        this.series = series;
    }


    /**
     *  Gets the data attribute of the TransTask object
     *
     *@return    The data value
     */
    public ApDataPoint getData() {
        return data;
    }


    /**
     *  Gets the gateway attribute of the TransTask object
     *
     *@return    The gateway value
     */
    public Gateway getGateway() {
        return gateway;
    }


    /**
     *  Gets the series attribute of the TransTask object
     *
     *@return    The series value
     */
    public long getSeries() {
        return series;
    }


    /**
     *  Sets the data attribute of the TransTask object
     *
     *@param  data  The new data value
     */
    public void setData(ApDataPoint data) {
        this.data = data;
    }



}
