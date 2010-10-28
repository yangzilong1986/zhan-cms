package zt.cms.xf.fd.actnoinfo;

import zt.cms.xf.common.dao.FdactnoinfoDao;
import zt.cms.xf.common.dto.Fdactnoinfo;
import zt.cms.xf.common.exceptions.FdactnoinfoDaoException;
import zt.cms.xf.common.factory.FdactnoinfoDaoFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-12
 * Time: 22:33:50
 * To change this template use File | Settings | File Templates.
 */

@ManagedBean(name = "searchService")
@SessionScoped
//@RequestScoped
public class SearchService implements Serializable {

    private List<Fdactnoinfo> records;
    private String regioncd;
    private String NAME;
    private int port;
    private Fdactnoinfo selectedRecord;
    private Fdactnoinfo[] selectedRecords;

    public String query() {
        try {
            records = queryDB();
        } catch (FdactnoinfoDaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
//        return "about.xhtml";
    }

    public Fdactnoinfo getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(Fdactnoinfo selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public Fdactnoinfo[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(Fdactnoinfo[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public String getRegioncd() {
        return regioncd;
    }

    public void setRegioncd(String regioncd) {
        this.regioncd = regioncd;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Fdactnoinfo> getRecords() {
        return records;
    }

    public void setRecords(List<Fdactnoinfo> records) {
        this.records = records;
    }

    public List<Fdactnoinfo> queryDB() throws FdactnoinfoDaoException {
        FdactnoinfoDao actnodao = FdactnoinfoDaoFactory.create();
        Fdactnoinfo actnoinfo;
        Fdactnoinfo[] actnoinfos;
        if (regioncd == null || regioncd.equals("")) {
            actnoinfos = actnodao.findAll();
        } else {
            String sql = " regioncd ='" + regioncd + "'";
            actnoinfos = actnodao.findByDynamicWhere(sql, null);
        }
        return Arrays.asList(actnoinfos);

    }
}
