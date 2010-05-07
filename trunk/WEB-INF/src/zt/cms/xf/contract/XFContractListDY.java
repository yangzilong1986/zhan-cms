package zt.cms.xf.contract;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 9:19:52
 * To change this template use File | Settings | File Templates.
 */

import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.util.FormInstance;

import zt.platform.form.util.event.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import zt.platform.form.util.SessionAttributes;
import zt.platform.cachedb.ConnectionManager;
import zt.cms.xf.common.dto.Xfvcontractprtinfo;
import zt.cms.xf.common.factory.XfvcontractprtinfoDaoFactory;
import zt.cms.xf.common.dao.XfvcontractprtinfoDao;
import zt.cms.util.poiutil.IWriteOtherInfos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zt.util.PropertyManager;

import javax.sql.rowset.CachedRowSet;

public class XFContractListDY extends FormActions {

    private static Log logger = LogFactory.getLog(XFContractListDY.class);

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        if (button != null && button.equals(SessionAttributes.REQUEST_BUTTON_ADD_NAME)) {

            trigger(manager, "XFCONTRACTLINK", null);
            //trigger(manager, instance, EventType.INSERT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
        }
        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            String buttonname = ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME);
            if (buttonname.equals("PAYSCHEDULE")) {
                trigger(manager, "XFCONTRACTPAYLIST", null);
            } else if (buttonname.equals("OPRATION")) {
                trigger(manager, "XFCONTRACTPAGE", null);
            } else if (buttonname.equals("PRINTBUTTON")) {
                ctx.setTarget("/report/contractprt.jsp");
            } else if (buttonname.equals("MONTHBILLPRINTBUTTON")) {
//                ctx.setTarget("/report/monthbillprt.jsp");
                String contractno = ctx.getParameter("CONTRACTNO");
                printMonthBill(ctx,instance,contractno);
            }
        }
        return 0;
    }

    void printMonthBill(SessionContext ctx,FormInstance instance,String contractno) {

        String sql="select poano,startdate,cpaamt,convert_money(cpaamt*100) as test from xfcontractpays where contractno = '" + contractno + "'";
        logger.info(sql);
        try {
//            XfvcontractprtinfoDao dao = XfvcontractprtinfoDaoFactory.create();
//            Xfvcontractprtinfo info = dao.findWhereContractnoEquals(contractno);
            ConnectionManager cmanager = ConnectionManager.getInstance();
            CachedRowSet crs = cmanager.getRs(sql);

            if (crs.size() == 0) {
                instance.getFormBean().getElement("savebtn").setDescription("newwin");
                ctx.setRequestAtrribute("msg", "没有符合条件的数据！");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setRequestAtrribute("funcdel", "history.go(-1)");
                ctx.setTarget("/showinfo.jsp");
//                return -1;
                return;
            }

            String poiexceltemp = PropertyManager.getProperty("POI_EXCEl_PATH");
            HashMap<String, Object> excelMap = new HashMap<String, Object>();
            excelMap.put("filenm", poiexceltemp + "\\月帐单.xls");
            excelMap.put("cellname", "dataArea");

            IWriteOtherInfos writeinfo = new WriteExcelInfoForMonthBill();
            //设置  IWriteOtherInfos 接口参数 为当前合同号
            excelMap.put("CONTRACTNO", contractno);
            excelMap.put("WRITEINFO", writeinfo);

            excelMap.put("crs", crs);
            excelMap.put("date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
            
            ctx.getRequest().setCharacterEncoding("iso8859-1");
            ctx.getRequest().getSession().setAttribute("excelMap", excelMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ctx.setTarget("/excelByTmp");

    }
}