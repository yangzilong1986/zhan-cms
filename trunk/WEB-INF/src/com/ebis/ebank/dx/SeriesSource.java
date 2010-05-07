package com.ebis.ebank.dx;

import com.ebis.ebank.comm.DataPackUTag;

public class SeriesSource {

    private DataPackUTag tag;
    private String transID;
    public SeriesSource(DataPackUTag tag,String transID) {
        this.tag     = tag;
        this.transID = transID;
    }
    public DataPackUTag getTag() {
        return tag;
    }
    public void setTag(DataPackUTag tag) {
        this.tag = tag;
    }
    public void setTransID(String transID) {
        this.transID = transID;
    }
    public String getTransID() {
        return transID;
    }


}