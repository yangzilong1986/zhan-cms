package zt.cms.xf.newcms.services;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;
import zt.cms.xf.newcms.controllers.T100103CTL;
import zt.cms.xf.newcms.domain.T100103.T100103ResponseRecord;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-21
 * Time: 11:28:16
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "T100103")
@ViewScoped
//@RequestScoped
public class T100103Bean implements Serializable {
//    private DataModel model;
//    private LazyDataModel<T100101ResponseRecord> lazyModel;
    private T100103ResponseRecord selectedRecord;

    private Log logger = LogFactory.getLog(this.getClass());
    private T100103ResponseRecord responseRecord = new T100103ResponseRecord();
    List<T100103ResponseRecord> responseFDList;
    List<T100103ResponseRecord> responseXFList;

    T100103CTL t100103ctl = new T100103CTL();

    private int totalcount;
    private BigDecimal totalamt;
    private BigDecimal totalPrincipalAmt;   //本金
    private BigDecimal totalInterestAmt;    //利息

    private String[] regionCodes = {"0532", "0531", "023", "0351"};
    private String[] regionNames = {"青岛", "济南", "重庆", "太原"};
    private SelectItem[] regionOptions;


    public T100103Bean() {
        initFilters();
    }

    private void initList() {
        List<T100103ResponseRecord> fdList = t100103ctl.getAllFDRecords();
        totalcount = 0;
        initAmt();
        responseFDList = new ArrayList<T100103ResponseRecord>();

        for (T100103ResponseRecord record : fdList) {
            String tmpStr = record.getStddqh();
            String regioncdTmp, bankcdTmp, nameTmp;
            if (tmpStr == null || tmpStr.equals("null")) {
                throw new RuntimeException("地区代号转换有误");
            } else {
                String[] code = tmpStr.split("-");
                record.setStddqh(code[0].trim());
                record.setStdyhh(code[1].trim());
            }

            responseFDList.add(record);
            totalcount++;
            countAmt(record);

        }

    }

    private void initFilters() {
        regionOptions = createRegionOptions(regionNames, regionCodes);
    }

    private SelectItem[] createRegionOptions(String[] names, String[] codes) {
        SelectItem[] options = new SelectItem[codes.length + 1];

        options[0] = new SelectItem("", "全部地区");
        for (int i = 0; i < codes.length; i++) {
//            options[i + 1] = new SelectItem(codes[i],codes[i]);
            options[i + 1] = new SelectItem(codes[i], names[i]);
        }

        return options;
    }

    public String query() {
        initList();

        String hth, dqh, khmc;
        dqh = responseRecord.getStddqh();
        hth = responseRecord.getStdhth();
        khmc = responseRecord.getStdkhmc();
        totalcount = 0;
        initAmt();
        List<T100103ResponseRecord> newResponseFDList = new ArrayList<T100103ResponseRecord>();
        for (T100103ResponseRecord record : responseFDList) {
            if (StringUtils.isNotEmpty(hth)) {
                if (!record.getStdhth().equals(hth)) {
                    continue;
                }
            }
            if (StringUtils.isNotEmpty(khmc)) {
                if (!record.getStdkhmc().equals(khmc)) {
                    continue;
                }
            }
            if (StringUtils.isNotEmpty(dqh)) {
                if (!record.getStddqh().equals(dqh)) {
                    continue;
                }
            }
            newResponseFDList.add(record);
            totalcount++;
            countAmt(record);
        }
        responseFDList = newResponseFDList;
        return null;
    }

    private void initAmt() {
        totalamt = new BigDecimal(0);
        totalPrincipalAmt = new BigDecimal(0);
        totalInterestAmt = new BigDecimal(0);
    }

    private void countAmt(T100103ResponseRecord record) {
        totalamt = totalamt.add(new BigDecimal(record.getStdhkje()));
        totalPrincipalAmt = totalPrincipalAmt.add(new BigDecimal(record.getStdhkbj()));
        totalInterestAmt = totalInterestAmt.add(new BigDecimal(record.getStdhklx()));
    }

    public String onRowSelectNavigate(SelectEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedRecord", event.getObject());

        return "FDCutpayDetail?faces-redirect=true";
    }

    //==============================================================

//    public LazyDataModel<T100103ResponseRecord> getLazyModel() {
//        return lazyModel;
//    }

/*    public void setLazyModel(LazyDataModel<T100103ResponseRecord> lazyModel) {
        this.lazyModel = lazyModel;
    }*/

    public T100103ResponseRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(T100103ResponseRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public List<T100103ResponseRecord> getResponseFDList() {
        return responseFDList;
    }

    public void setResponseFDList(List<T100103ResponseRecord> responseFDList) {
        this.responseFDList = responseFDList;
    }

    public List<T100103ResponseRecord> getResponseXFList() {
        return responseXFList;
    }

    public void setResponseXFList(List<T100103ResponseRecord> responseXFList) {
        this.responseXFList = responseXFList;
    }

    public T100103ResponseRecord getResponseRecord() {
        return responseRecord;
    }

    public void setResponseRecord(T100103ResponseRecord responseRecord) {
        this.responseRecord = responseRecord;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public BigDecimal getTotalamt() {
        return totalamt;
    }

    public SelectItem[] getRegionOptions() {
        return regionOptions;
    }

    public BigDecimal getTotalPrincipalAmt() {
        return totalPrincipalAmt;
    }

    public BigDecimal getTotalInterestAmt() {
        return totalInterestAmt;
    }

    public String[] getRegionCodes() {
        return regionCodes;
    }

    public void setRegionCodes(String[] regionCodes) {
        this.regionCodes = regionCodes;
    }

    public String[] getRegionNames() {
        return regionNames;
    }

    public void setRegionNames(String[] regionNames) {
        this.regionNames = regionNames;
    }

}
