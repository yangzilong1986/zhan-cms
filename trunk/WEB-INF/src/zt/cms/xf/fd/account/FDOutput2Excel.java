package zt.cms.xf.fd.account;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;

import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.event.*;
import zt.platform.utils.Debug;
import zt.platform.cachedb.ConnectionManager;
import zt.cms.xf.common.dao.*;
import zt.cms.xf.common.factory.*;
import zt.cms.xf.common.dto.*;
import zt.cms.xf.common.constant.*;
import zt.cms.xf.common.exceptions.XfactcutpaydetlDaoException;
import zt.cms.xf.common.exceptions.FdcutpaydetlDaoException;
import zt.cms.xf.account.SBSManager;

import zt.cmsi.mydb.MyDB;
import org.apache.commons.lang.StringUtils;

import javax.sql.rowset.CachedRowSet;

import com.zt.util.PropertyManager;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 20:24:05
 * To change this template use File | Settings | File Templates.
 */
public class FDOutput2Excel extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.fd.account.FDDateLink");

    private String  startdate;
    private String  enddate;

    public int load(SessionContext ctx, DatabaseConnection conn, FormInstance instance, ErrorMessages msgs, EventManager manager, String parameter) {

        SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
        startdate = sFmt.format(new Date());
        enddate = sFmt.format(new Date());


        instance.setValue("STARTDATE", startdate);
        instance.setValue("ENDDATE", enddate);

        instance.getFormBean().getElement("FAILDRECORDTOEXCEL").setComponetTp(6);
        instance.getFormBean().getElement("LOADEDRECORDTOEXCEL").setComponetTp(6);
        String lastbutton = (String) ctx.getAttribute("BUTTONNAME");
        if (lastbutton == null) {
            return -1;
        }

        if (lastbutton.equals("FAILDRECORDTOEXCEL")) {
            instance.getFormBean().getElement("FAILDRECORDTOEXCEL").setComponetTp(15);
        } else if (lastbutton.equals("TOEXCEL")) {
            instance.getFormBean().getElement("LOADEDRECORDTOEXCEL").setComponetTp(15);
        }

        trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_BREAK_TYPE);
        return 0;
    }

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button, ErrorMessages msgs, EventManager manager) {

        startdate = ctx.getParameter("STARTDATE");
        enddate = ctx.getParameter("ENDDATE");

        //建行直连代扣通讯记录生成
        if (button.equals("FAILDRECORDTOEXCEL")) {
            String datestr = "";
            String sql = "select seqno," +
                    "       xdkhzd_khbh," +
                    "       xdkhzd_khmc," +
                    "       gthtjh_htbh," +
                    "       to_char(createtime,'yyyyMMdd') as gthtjh_date," +
                    "       gthtjh_ll," +
                    "       gthtjh_jhje," +
                    "       gthtjh_bjje," +
                    "       gthtjh_lxje," +
                    "       gthtb_zhbh," +
                    "       cutpayactno," +
                    "       (select enudt from ptenuminfodetl where enuid='FDBillStatus' and enutp=billstatus) billstatus " +
                    "       from fdcutpaydetl where billstatus = 3 " +
                    "and to_char(createtime,'yyyyMMdd') >= " +startdate  +
                    "and to_char(createtime,'yyyyMMdd') <= " +enddate  +
                    "order by seqno";
            try {
                ConnectionManager cmanager = ConnectionManager.getInstance();
                CachedRowSet crs = cmanager.getRs(sql);

                if (crs.size() == 0) {
                    instance.getFormBean().getElement("FAILDRECORDTOEXCEL").setDescription("newwin");
                    ctx.setRequestAtrribute("msg", "没有符合条件的数据！");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setRequestAtrribute("funcdel", "history.go(-1)");
                    ctx.setTarget("/showinfo.jsp");
                    return -1;
                }
                String poiexceltemp = PropertyManager.getProperty("POI_EXCEl_PATH");
                HashMap<String, Object> excelMap = new HashMap<String, Object>();
                excelMap.put("filenm", poiexceltemp + "\\房贷系统扣款记录明细.xls");
                excelMap.put("cellname", "dataArea");
                excelMap.put("crs", crs);
//                if (crs.next()) {
//                    datestr += " " + crs.getString("gthtjh_date");
//                    crs.beforeFirst();
//                }
//                excelMap.put("date", datestr);
                excelMap.put("date", enddate);

                ctx.getRequest().setCharacterEncoding("iso8859-1");
                ctx.getRequest().getSession().setAttribute("excelMap", excelMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ctx.setTarget("/excelByTmp");
        }
       return 0;
}



}