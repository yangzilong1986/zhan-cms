package zt.platform.form.config;
/**
 * FORM类，存放FORM的定义信息
 *
 * Notes:
 * 1)请添加每个属性的setter和getter方法
 * 2)注意fields是按xposition和yposition升序排列存入elements属性中的
 * @author sun
 * @version 1.0
 */

import zt.platform.form.util.datatype.ComponentType;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

public class FormBean implements Cloneable, Serializable {
    private static Logger logger = Logger.getLogger("zt.platform.form.config.FormBeanManager");
    public static final int PAGE_TYPE = 0;
    public static final int LIST_TYPE = 1;
    public static final int QUERY_TYPE = 2;
    public static final int DISPLAY_LIST = 0;
    public static final int DISPLAY_QUERY = 1;
    public static final int DISPLAY_LIST_AND_QUERY = 2;
    private java.util.Map elements = new HashMap();
    private String[] elementKeys;
    private String Id;
    private String url;
    private String proccls;
    private String style;
    private String tbl;
    private String title;
    private int type;
    private boolean readonly;
    private String listsql;
    private String currentlistsql; //zhanrui 20091110 保存改变查询条件后的SQL
    private String description;
    private int cols;
    private int rows;
    private boolean useAdd;
    private boolean useDelete;
    private boolean useSearch;
    private boolean useSave;
    private boolean useReset;
    private boolean useEdit;
    private String countsql;
    private String currentcountsql; //zhanrui 20091110 保存改变查询条件后的SQL
    private int width;
    private String scriptFile;

    /**
     * 按元素名称取元素
     *
     * @param name
     * @return zt.platform.form.config.ElementBean
     * @roseuid 3F812E4603DE
     */
    public ElementBean getElement(String name) {
        return (ElementBean) this.elements.get(name);
    }

    /**
     * 按坐标（x,y）来取元素
     *
     * @param x
     * @param y
     * @return zt.platform.form.config.ElementBean
     * @roseuid 3F812E580254
     */
    public ElementBean getElement(int x, int y) {
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if (eb.getXposition() == x && eb.getYposition() == y && eb.getComponetTp() != ComponentType.SYS_BUTTON && eb.getComponetTp() != ComponentType.HIDDEN_TYPE) {
                return eb;
            }
        }
        return null;
    }

    public boolean hasElement(int x, int y) {
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if (eb.getXposition() == x && eb.getYposition() != y && eb.getComponetTp() != ComponentType.SYS_BUTTON && eb.getComponetTp() != ComponentType.HIDDEN_TYPE) {
                return true;
            }
        }
        return false;
    }

    public List getSearchKeys() {
        List ebs = new ArrayList();
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if (eb.isIsSearchKey()) {
                ebs.add(eb);
            }
        }
        Collections.sort(ebs);
        return ebs;
    }

    public List getQueryField() {
        List ebs = new ArrayList();
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if ((eb.getDisplayType() == this.DISPLAY_QUERY || eb.getDisplayType() == this.DISPLAY_LIST_AND_QUERY) && eb.getComponetTp() != ComponentType.HIDDEN_TYPE) {
                ebs.add(eb);
            }
        }
        Collections.sort(ebs);
        return ebs;
    }

    public List getQueryHiddenFlds() {
        List ebs = new ArrayList();
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if ((eb.getDisplayType() == this.DISPLAY_QUERY || eb.getDisplayType() == this.DISPLAY_LIST_AND_QUERY) && eb.getComponetTp() == ComponentType.HIDDEN_TYPE) {
                ebs.add(eb);
            }
        }
        return ebs;
    }

    public List getSysButton() {
        List ebs = new ArrayList();
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if (eb.getComponetTp() == ComponentType.SYS_BUTTON) {
                ebs.add(eb);
            }
        }
        Collections.sort(ebs);
        return ebs;
    }

    public List getHidden() {
        List ebs = new ArrayList();
        for (Iterator ies = this.elements.values().iterator(); ies.hasNext();) {
            ElementBean eb = (ElementBean) ies.next();
            if (eb.getComponetTp() == ComponentType.HIDDEN_TYPE) {
                ebs.add(eb);
            }
        }
        return ebs;
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public String getDescription() {
        return description;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setElementKeys(String[] elements) {
        this.elementKeys = elements;
    }

    public void setListsql(String listsql) {
        this.listsql = listsql;
    }
    public void setCurrentListsql(String currentlistsql) {
        this.currentlistsql = currentlistsql;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public String getCurrentListsql() {
        return currentlistsql;
    }
    public String getListsql() {
        return listsql;
    }

    public String getProccls() {
        return proccls;
    }

    public void setProccls(String proccls) {
        this.proccls = proccls;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getRows() {
        return rows;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTbl() {
        return tbl;
    }

    public void setTbl(String tbl) {
        this.tbl = tbl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return ":" + getId() + ":" + getUrl() + ":" + getProccls() + ":" + getStyle() + ":" + getTbl()
                + ":" + getTitle() + ":" + getType() + ":" + isReadonly() + ":" + getRows() + ":" + getCols() + ":" + getListsql() + ":" + getDescription();
    }

    public void setElements(java.util.Map elements) {
        this.elements = elements;
    }

    public void addElement(Object key, Object value) {
        this.elements.put(key, value);
    }

    public String[] getElementKeys() {
        return this.elementKeys;
    }

    public boolean isUseAdd() {
        return useAdd;
    }

    public void setUseAdd(boolean useAdd) {
        this.useAdd = useAdd;
    }

    public boolean isUseDelete() {
        return useDelete;
    }

    public void setUseDelete(boolean useDelete) {
        this.useDelete = useDelete;
    }

    public boolean isUseSearch() {
        return useSearch;
    }

    public void setUseSearch(boolean useSearch) {
        this.useSearch = useSearch;
    }

    public boolean isUseSave() {
        return useSave;
    }

    public void setUseSave(boolean useSave) {
        this.useSave = useSave;
    }

    public boolean isUseReset() {
        return useReset;
    }

    public void setUseReset(boolean useReset) {
        this.useReset = useReset;
    }

    public boolean isUseEdit() {
        return useEdit;
    }

    public void setUseEdit(boolean useEdit) {
        this.useEdit = useEdit;
    }

    public String getCountsql() {
        return countsql;
    }

    public void setCountsql(String countsql) {
        this.countsql = countsql;
    }
    public String getCurrentCountsql() {
        return currentcountsql;
    }

    public void setCurrentCountsql(String currentcountsql) {
        this.currentcountsql = currentcountsql;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    //Added by wxj at 040325
    public Object clone() {
        FormBean fb = new FormBean();
        fb.elements = (HashMap) ((HashMap) this.elements).clone();
        String[] elementKeys = this.getElementKeys();
        for (int i = 0; i < elementKeys.length; i++) {
            ElementBean tmp = (ElementBean) this.elements.get(elementKeys[i]);
            fb.elements.put(elementKeys[i], (ElementBean) tmp.clone());
        }
        fb.elementKeys = (String[]) elementKeys.clone();
        fb.Id = this.Id;
        fb.url = this.url;
        fb.proccls = this.proccls;
        fb.style = this.style;
        fb.tbl = this.tbl;
        fb.title = this.title;
        fb.type = this.type;
        fb.readonly = this.readonly;
        fb.listsql = this.listsql;
        fb.currentlistsql = this.currentlistsql;
        fb.description = this.description;
        fb.cols = this.cols;
        fb.rows = this.rows;
        fb.useAdd = this.useAdd;
        fb.useDelete = this.useDelete;
        fb.useSearch = this.useSearch;
        fb.useSave = this.useSave;
        fb.useReset = this.useReset;
        fb.useEdit = this.useEdit;
        fb.countsql = this.countsql;
        fb.currentcountsql = this.currentcountsql;
        fb.width = this.width;
        fb.scriptFile = this.scriptFile;
        return fb;
    }
}
