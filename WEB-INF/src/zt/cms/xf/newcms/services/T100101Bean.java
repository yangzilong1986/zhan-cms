package zt.cms.xf.newcms.services;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;
import zt.cms.xf.newcms.controllers.T100101CTL;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@ManagedBean(name = "T100101")
@SessionScoped
//@RequestScoped
public class T100101Bean implements Serializable {
//    private DataModel model;
//    private LazyDataModel<T100101ResponseRecord> lazyModel;
    private T100101ResponseRecord selectedRecord;

    private Log logger = LogFactory.getLog(this.getClass());
    private T100101ResponseRecord responseRecord = new T100101ResponseRecord();
    List<T100101ResponseRecord> responseFDList;
    List<T100101ResponseRecord> responseXFList;

    T100101CTL t100101ctl = new T100101CTL();

    private int totalcount;
    private BigDecimal totalamt;
    private BigDecimal totalPrincipalAmt;   //本金
    private BigDecimal totalInterestAmt;    //利息
    private BigDecimal totalFxjeAmt;    //罚息

    private String[] regionCodes = {"0532", "0531", "023", "0351"};
    private String[] regionNames = {"青岛", "济南", "重庆", "太原"};
    private SelectItem[] regionOptions;

    private String field;

    public T100101Bean() {
        FacesContext context = FacesContext.getCurrentInstance();
        field = context.getExternalContext().getRequestParameterMap().get("field");
        initFilters();
    }

    private void initList() {
        List<T100101ResponseRecord> fdList = t100101ctl.getAllFDRecords();
        totalcount = 0;
        initAmt();
        responseFDList = new ArrayList<T100101ResponseRecord>();

        for (T100101ResponseRecord record : fdList) {
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
        List<T100101ResponseRecord> newResponseFDList = new ArrayList<T100101ResponseRecord>();
        for (T100101ResponseRecord record : responseFDList) {
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
        totalFxjeAmt = new BigDecimal(0);
    }

    private void countAmt(T100101ResponseRecord record) {
        totalamt = totalamt.add(new BigDecimal(record.getStdhkje()));
        totalPrincipalAmt = totalPrincipalAmt.add(new BigDecimal(record.getStdhkbj()));
        totalInterestAmt = totalInterestAmt.add(new BigDecimal(record.getStdhklx()));
        totalFxjeAmt = totalFxjeAmt.add(new BigDecimal(record.getStdfxje()));
    }

    public String onRowSelectNavigate(SelectEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedRecord", event.getObject());

        return "FDCutpayDetail?faces-redirect=true";
    }

    //==============================================================

//    public LazyDataModel<T100101ResponseRecord> getLazyModel() {
//        return lazyModel;
//    }

/*    public void setLazyModel(LazyDataModel<T100101ResponseRecord> lazyModel) {
        this.lazyModel = lazyModel;
    }*/

    public T100101ResponseRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(T100101ResponseRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public List<T100101ResponseRecord> getResponseFDList() {
        return responseFDList;
    }

    public void setResponseFDList(List<T100101ResponseRecord> responseFDList) {
        this.responseFDList = responseFDList;
    }

    public List<T100101ResponseRecord> getResponseXFList() {
        return responseXFList;
    }

    public void setResponseXFList(List<T100101ResponseRecord> responseXFList) {
        this.responseXFList = responseXFList;
    }

    public T100101ResponseRecord getResponseRecord() {
        return responseRecord;
    }

    public void setResponseRecord(T100101ResponseRecord responseRecord) {
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

    public BigDecimal getTotalFxjeAmt() {
        return totalFxjeAmt;
    }

    public void setTotalFxjeAmt(BigDecimal totalFxjeAmt) {
        this.totalFxjeAmt = totalFxjeAmt;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
