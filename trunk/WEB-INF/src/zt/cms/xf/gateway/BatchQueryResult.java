package zt.cms.xf.gateway;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-7-6
 * Time: 15:35:54
 * To change this template use File | Settings | File Templates.
 */
public class BatchQueryResult {
    String succnt;
    String falcnt;
    String sucamt;
    String falamt;
    String floflg; //后续包标志
    String curcnt; //当前包笔数
    String remark1;
    String remark2;

     List <CutpayFailRecord> dtldat = new ArrayList();

    public String getSuccnt() {
        return succnt;
    }

    public void setSuccnt(String succnt) {
        this.succnt = succnt;
    }

    public String getFalcnt() {
        return falcnt;
    }

    public void setFalcnt(String falcnt) {
        this.falcnt = falcnt;
    }

    public String getSucamt() {
        return sucamt;
    }

    public void setSucamt(String sucamt) {
        this.sucamt = sucamt;
    }

    public String getFalamt() {
        return falamt;
    }

    public void setFalamt(String falamt) {
        this.falamt = falamt;
    }

    public String getFloflg() {
        return floflg;
    }

    public void setFloflg(String floflg) {
        this.floflg = floflg;
    }

    public String getCurcnt() {
        return curcnt;
    }

    public void setCurcnt(String curcnt) {
        this.curcnt = curcnt;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    

    public void add(CutpayFailRecord record) {
        this.dtldat.add(record);
    }

    public List<CutpayFailRecord> getAll() {
        return this.dtldat;
    }
}
