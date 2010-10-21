package zt.cms.xf.newcms.services;

import org.primefaces.model.LazyDataModel;
import zt.cms.xf.newcms.domain.T100101.T100101ResponseRecord;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-21
 * Time: 11:28:16
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "T100101Bean")
@SessionScoped
public class T100101Bean {
    private LazyDataModel<T100101ResponseRecord> lazyModel;
    private T100101ResponseRecord selectedRecord;

    public T100101Bean() {
        lazyModel = new LazyDataModel<T100101ResponseRecord>() {
            @Override
            public List<T100101ResponseRecord> load(int first, int pageSize, String sortField, boolean sortOrder, Map<String, String> filters) {
                //logger.log(Level.INFO, "Loading the lazy car data between {0} and {1}", new Object[]{first, (first+pageSize)});

                //Sorting and Filtering information are not used for demo purposes just random dummy data is returned

                List<T100101ResponseRecord> lazyCars = new ArrayList<T100101ResponseRecord>();
                //populateLazyRandomCars(lazyCars, pageSize);

                return lazyCars;
            }
        };
    }

    public LazyDataModel<T100101ResponseRecord> getLazyModel() {
        return lazyModel;
    }

/*    public void setLazyModel(LazyDataModel<T100101ResponseRecord> lazyModel) {
        this.lazyModel = lazyModel;
    }*/

    public T100101ResponseRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(T100101ResponseRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }
}
