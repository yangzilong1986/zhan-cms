package zt.cms.xf.fd.actnoinfo;

import zt.cms.xf.common.dao.FdactnoinfoDao;
import zt.cms.xf.common.dto.Fdactnoinfo;
import zt.cms.xf.common.exceptions.FdactnoinfoDaoException;
import zt.cms.xf.common.factory.FdactnoinfoDaoFactory;

import javax.faces.bean.ManagedBean;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-10-12
 * Time: 22:33:50
 * To change this template use File | Settings | File Templates.
 */

@ManagedBean(name="searchService")
public class SearchService {

    private List<Fdactnoinfo> allRecords;
    private String ID = "aaaaaaa1zhan" ;
    private int port ;

    public int getPort(){
           return StaticClassTmp.getPort();
    }

    public String getID() {
        String str = StaticClassTmp.getHostip();
        return str;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<Fdactnoinfo> getAllRecords() throws FdactnoinfoDaoException {
        String actno, name, contractno;
        FdactnoinfoDao actnodao = FdactnoinfoDaoFactory.create();
        Fdactnoinfo actnoinfo;
        Fdactnoinfo[] actnoinfos;

        actnoinfos = actnodao.findAll();
        return Arrays.asList(actnoinfos);

    }
}
