package zt.platform.form.config;

/**
 *管理所有的FORM定义信息 在static语句块中负责加载enabled=1的FORM定义信息到内存,数据来源
 *(ptforminfomain、ptforminfodetl)
 * 注意： 将表字段的信息从pttblinfodetl中复制到ElementBean中
 *@author Rock
 *@created 2003年10月10日
 *@version 1.0
 */

import zt.platform.db.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.datatype.ComponentType;
import zt.platform.form.util.datatype.DataType;

import java.util.*;
import java.util.logging.Logger;

public class FormBeanManager {

    /**
     * FORM管理器，管理所有的FORM实例 管理方式：FORMID : FORM实例值对的模式
     *
     * @author sun
     * @since 2003年10月10日
     */
    private static Map formBeans;
    private static Logger logger = Logger.getLogger("zt.platform.form.config.FormBeanManager");

    static {
        System.out.println("FormBeanManager initing ...");
        init();
        System.out.println("FormBeanManager inited ok !");
    }

    /**
     * Description of the Method
     *
     * @param eb Description of the Parameter
     * @param fb Description of the Parameter
     */
    private static void check(ElementBean eb, FieldBean fb) {
        if (fb == null) {
            return;
        }
        if (eb.getDefaultValue() == null || eb.getDefaultValue().equals("")) {
            String value = DBUtil.fromDB(fb.getDefaultValue());
            eb.setDefaultValue(value);
        }
        if (eb.getCaption() == null || eb.getCaption().equals("")) {
            eb.setCaption(fb.getCaption());
        }
        if (eb.getDescription() == null || eb.getDescription().equals("")) {
            eb.setDescription(fb.getDescription());
        }
        if (eb.getDataType() == 0) {
            eb.setDataType(fb.getDatatype());
        }
        if (eb.getComponetTp() == 0) {
            eb.setDataType(fb.getDatatype());
            if (eb.getDataType() == FieldBean.DATA_TYPE_BOOLEAN) {
                eb.setComponetTp(ComponentType.BOOLEAN_TYPE);
            }
            if (eb.getDataType() == FieldBean.DATA_TYPE_DECIMAL ||
                    eb.getDataType() == FieldBean.DATA_TYPE_STRING) {
                eb.setComponetTp(ComponentType.TEXT_TYPE);
            }
            if (eb.getDataType() == FieldBean.DATA_TYPE_INTEGER) {
                if (eb.getRefTbl() != null && eb.getRefName() != null &&
                        eb.getRefValue() != null) {
                    eb.setComponetTp(ComponentType.REFERENCE_TEXT_TYPE);
                } else {
                    eb.setComponetTp(ComponentType.TEXT_TYPE);
                }
            }
            if (eb.getDataType() == FieldBean.DATA_TYPE_ENUMERATION) {
                eb.setComponetTp(ComponentType.ENUMERATION_TYPE);
            }
            if (eb.getDataType() == FieldBean.DATA_TYPE_DATE) {
                eb.setComponetTp(ComponentType.TEXT_TYPE);
            }
        }
    }

    /**
     * 根据FORMID访问FORM实例
     *
     * @param id
     * @return zt.platform.form.config.FormBean
     * @roseuid 3F716FAA02A9
     */
    public static FormBean getForm(String id) {
        return (FormBean) formBeans.get(id);
    }

    /**
     * Gets the trim attribute of the FormBeanManager class
     *
     * @param value Description of the Parameter
     * @return The trim value
     */
    private static String getTrim(String value) {
        if (value == null) {
            return "";
        } else {
            return value.trim();
        }
    }

    /**
     * Description of the Method
     */
    public static void init() {
        //1、申请数据库连接资源
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        //2、根据PTFORMINFOMAIN设置每个FormBean的属性
        formBeans = new HashMap();
        RecordSet rs = con.executeQuery("select * from PTFORMINFOMAIN where enabled='1'");
        while (rs.next()) {
            String formId = rs.getString("formid").trim();
            FormBean tmpfrmb = new FormBean();
            formBeans.put(formId, tmpfrmb);
            tmpfrmb.setId(formId);
            tmpfrmb.setUrl(rs.getString("urlocate"));
            tmpfrmb.setProccls(getTrim(rs.getString("formproc")));
            tmpfrmb.setStyle(getTrim(rs.getString("formstyle")));
            tmpfrmb.setTbl(getTrim(rs.getString("formtbl")));
            tmpfrmb.setTitle(DBUtil.fromDB(getTrim(rs.getString("title"))));
            tmpfrmb.setType(rs.getInt("formtype"));
            tmpfrmb.setReadonly(rs.getBoolean("readonly"));
            tmpfrmb.setRows(rs.getInt("rows"));
            tmpfrmb.setCols(rs.getInt("cols"));
            tmpfrmb.setListsql(getTrim(rs.getString("listsql")));
            tmpfrmb.setDescription(DBUtil.fromDB(getTrim(getTrim(rs.getString("description")))));
            tmpfrmb.setUseAdd(rs.getBoolean("useadd"));
            tmpfrmb.setUseDelete(rs.getBoolean("usedelete"));
            tmpfrmb.setUseEdit(rs.getBoolean("useedit"));
            tmpfrmb.setUseReset(rs.getBoolean("usereset"));
            tmpfrmb.setUseSave(rs.getBoolean("usesave"));
            tmpfrmb.setUseSearch(rs.getBoolean("usesearch"));
            tmpfrmb.setCountsql(rs.getString("countsql"));
            tmpfrmb.setWidth(rs.getInt("width"));
            tmpfrmb.setScriptFile(DBUtil.fromDB(getTrim(rs.getString("SCRIPTFILE"))));
        }
        //3、为每个FormBean设置它的ElementBeans
        String elementStr = "select * from PTFORMINFODETL where formid='";
        String elementStr2 = "' order by xposition asc,yposition,seqno asc";
        for (Iterator iter = formBeans.values().iterator(); iter.hasNext();) {
            FormBean frmb = (FormBean) iter.next();
            Collection keys = new Vector();
            ElementBean elmb = null;
            RecordSet elmRs = con.executeQuery(elementStr + frmb.getId() + elementStr2);
            //对于每一个element保存到form中去
            while (elmRs.next()) {
                elmb = new ElementBean();
                elmb.setSeqno(elmRs.getInt("seqno"));
                elmb.setType(elmRs.getInt("type"));
                elmb.setName(getTrim(elmRs.getString("name")));
                elmb.setMaxLength(elmRs.getInt("maxlength"));
                keys.add(elmb.getName());
                elmb.setDefaultValue(DBUtil.fromDB(getTrim(elmRs.getString("defaultvalue"))));
                elmb.setCaption(DBUtil.fromDB(getTrim(elmRs.getString("caption"))));
                elmb.setDescription(DBUtil.fromDB(getTrim(getTrim(elmRs.getString("description")))));
                elmb.setReadonly(elmRs.getBoolean("readonly"));
                elmb.setMinLength(elmRs.getInt("minlength"));
                elmb.setSize(elmRs.getInt("size"));
                if (elmRs.getString("visible") != null && elmRs.getString("visible").equals("0")) {
                    elmb.setVisible(false);
                }
                elmb.setIsnull(elmRs.getBoolean("isnull"));
                elmb.setComponetTp(elmRs.getInt("componenttp"));
                elmb.setDataType(elmRs.getInt("datatype"));
                elmb.setXposition(elmRs.getInt("xposition"));
                elmb.setYposition(elmRs.getInt("yposition"));
                elmb.setRows(elmRs.getInt("rows"));
                elmb.setCols(elmRs.getInt("cols"));
                elmb.setMultiple(elmRs.getBoolean("multiple"));
                elmb.setDisabled(elmRs.getBoolean("disabled"));
                elmb.setValuesettype(elmRs.getInt("valuesettype"));
                elmb.setValueset(DBUtil.fromDB(getTrim(elmRs.getString("valueset"))));
                elmb.setHeadstr(getTrim(elmRs.getString("headstr")));
                elmb.setMiddleStr(getTrim(elmRs.getString("middlestr")));
                elmb.setAfterstr(getTrim(elmRs.getString("afterstr")));
                elmb.setFormatcls(getTrim(elmRs.getString("formatcls")), frmb.getType());
                elmb.setOnclick(getTrim(elmRs.getString("onclick")));
                elmb.setOnchange(getTrim(elmRs.getString("onchange")));
                elmb.setOnsubmit(getTrim(elmRs.getString("onsubmit")));
                elmb.setOthers(getTrim(elmRs.getString("others")));
                elmb.setWidth(elmRs.getInt("width"));
                elmb.setExpression(getTrim(elmRs.getString("expression")));
                elmb.setDisplayType(elmRs.getInt("displaytype"));
                elmb.setChangeEvent(elmRs.getBoolean("changeEvent"));
                //根据FieldBean来设置ElementBean
                TableBean tblb = TableBeanManager.getTable(frmb.getTbl());
                FieldBean fldb = null;
                if (tblb == null) {
                    logger.warning("The tablebean with name " + frmb.getTbl() + " is null");
                } else {
                    //数据库字段类型组件
                    if (elmb.getType() == 0) {
                        fldb = tblb.getField(elmb.getName());
                        if (fldb == null) {
                            if (frmb.getType() == 0) {
                                String tmpmsg = "字段型组件[" + frmb.getId() + ".";
                                tmpmsg += elmb.getName() + "]在pttblinfodetl中未找到定义！";
                                logger.warning(tmpmsg);
                            }
                        } else {
                            elmb.setNeedEncode(fldb.isNeedEncode());
                            elmb.setIsPrimaryKey(fldb.isIsPrimary());
                            elmb.setIsSearchKey(fldb.isIsSearch());
                            elmb.setEnutpname(fldb.getEnutpname());
                            elmb.setRefTbl(fldb.getReftbl());
                            elmb.setRefName(fldb.getRefnamefld());
                            elmb.setRefWhere(fldb.getRefWhere());
                            elmb.setRefValue(fldb.getRefvaluefld());
                            elmb.setDecimalDigits(fldb.getDecimals());
                            elmb.setPrecision(fldb.getPrecision());
                            if (elmb.getMaxLength() == 0 &&
                                    (fldb.getDatatype() == DataType.STRING_TYPE ||
                                            fldb.getDatatype() == DataType.INTEGER_TYPE)) {
                                elmb.setMaxLength(fldb.getPrecision());
                            }
                            //用PTTBLINFODETL的值替代PTFRMINFODETL中未设置的属性
                            check(elmb, fldb);
                        }
                    } else //Memory-type field :changed by JGO on Dec 27,2004
                    {
                        fldb = tblb.getField(elmb.getName());
                        if (fldb != null) {
                            elmb.setDecimalDigits(fldb.getDecimals());
                            elmb.setPrecision(fldb.getPrecision());
                        }
                    }
                }
                //对于每一个element保存到frmb中去
                frmb.addElement(elmb.getName(), elmb);
            }
            //如果没有element元素，通过field生成
            //20040221 deleted by wxj:无此要求。
            //if (!hasElementRecord) {
            //form = initFormWithFields(form, keys);
            //}
            Object[] tmpo = keys.toArray();
            String[] theKeys = new String[tmpo.length];
            for (int i = 0; i < tmpo.length; i++) {
                theKeys[i] = (String) tmpo[i];
            }
            frmb.setElementKeys(theKeys);
        }
        manager.releaseConnection(con);
    }
}
