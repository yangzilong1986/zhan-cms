package zt.cms.xf.fd.account;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-3-14
 * Time: 9:19:52
 * To change this template use File | Settings | File Templates.
 */

import com.zt.util.PropertyManager;
import org.apache.commons.lang.StringUtils;
import zt.cms.xf.common.constant.FDBillStatus;
import zt.cms.xf.common.dao.FdcutpaydetlDao;
import zt.cms.xf.common.dto.Fdcutpaydetl;
import zt.cms.xf.common.dto.FdcutpaydetlPk;
import zt.cms.xf.common.exceptions.FdcutpaydetlDaoException;
import zt.cms.xf.common.factory.FdcutpaydetlDaoFactory;
import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DatabaseConnection;
import zt.platform.form.config.FormBean;
import zt.platform.form.control.FormActions;
import zt.platform.form.control.SessionContext;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.SessionAttributes;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.utils.Debug;

import javax.sql.rowset.CachedRowSet;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class FDCutPayDetlList extends FormActions {

    public static Logger logger = Logger.getLogger("zt.cms.xf.account.XFActPayDetlList");

    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance, String button,
                           ErrorMessages msgs, EventManager manager) {

        ctx.setAttribute("SUPERFORMID", instance.getFormid());
        ctx.setAttribute("BUTTONNAME", button);

        if (button != null && button.equals(SessionAttributes.REQUEST_EDIT_BUTTON_VALUE)) {
            ctx.setAttribute("BUTTONNAME", ctx.getParameter(SessionAttributes.CLICK_COLUMN_NAME));
            trigger(manager, "FDCUTPAYDETLPAGE", null);
        }

        //����ؿͻ��������ۿ�״̬�趨
        if (button != null && (button.equals("SETWDCUTPAYSUCCESSBATCH")
            || button.equals("SETWDCUTPAYFAILBATCH"))) {

            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            
            try {
                Fdcutpaydetl[] fdcutpaydetls;
                if (recordnos != null) {
                    fdcutpaydetls = getCutPayDetlListByRecordnos(recordnos);
                } else {
                    FormBean fb = instance.getFormBean();
                    String sql = fb.getCurrentListsql();
                    fdcutpaydetls = getCutPayDetlListBySql(sql);
                }
                if (fdcutpaydetls.length == 0) {
                    msgs.add("�޷��������ļ�¼��");
                    return 0;
                }
                if (button.equals("SETWDCUTPAYSUCCESSBATCH")) {
                    setCutpayDetlStatusBatch(fdcutpaydetls,FDBillStatus.CUTPAY_SUCCESS,msgs);
                    msgs.add("<br><br>...�����趨�ۿ�ɹ���ɣ�");
                } else {
                    setCutpayDetlStatusBatch(fdcutpaydetls,FDBillStatus.CUTPAY_FAILD,msgs);
                    msgs.add("<br><br>...�����趨�ۿ�ʧ����ɣ����ѯ��");
                }
            } catch (Exception e) {
                Debug.debug(e);
                msgs.add("�趨�ۿ�״̬�������⣬����ѯϵͳ������Ա��");
                return 0;
            } finally {
                ctx.setRequestAtrribute("msg", msgs.getAllMessages());
                ctx.setRequestAtrribute("flag", "1");
                ctx.setRequestAtrribute("isback", "0");
                ctx.setTarget("/showinfo.jsp");
                instance.setReadonly(true);
            }
        }
        //����ؿͻ������ʿۿ�״̬�趨
        if (button != null && (button.equals("SETWDCUTPAYSUCCESS")
            || button.equals("SETWDCUTPAYFAIL"))) {
            trigger(manager, "FDCUTPAYDETLPAGE", null);
        }

        //����ͨѶ��¼��
        //��ȡ�����ۿ���ϸ��¼
        //��ȡ��ǰ����ۿ���ϸ��¼
        if (button != null && (button.equals("GENERATEPKGBUTTON")
                || button.equals("GETCUTPAYBUTTON")
                || button.equals("GETPRECUTPAYBUTTON"))) {
            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            ctx.setAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME, recordnos);
            ctx.setAttribute("BUTTONNAME", button);
            trigger(manager, "FDDATELINK", null);
        }

        if (button != null && button.equals("ACCOUNTBUTTON")) {
//            ctx.setAttribute("BUTTONNAME", button);
            ctx.setAttribute("BUTTONNAME", "FDACCOUNTBUTTON");
            trigger(manager, "FDDATELINK", null);
        }

        if (button != null && button.equals("FDWRITEBACKBUTTON")) {
            String[] recordnos = ctx.getParameters(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            if (recordnos == null) {
                ctx.removeAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME);
            }else{
                ctx.setAttribute(SessionAttributes.REQUEST_DELETE_RANGE_NAME, recordnos);
            }
            ctx.setAttribute("BUTTONNAME", button);
            trigger(manager, "FDDATELINK", null);
        }


        if (button != null && button.equals("TOEXCEL")) {
            String datestr = "";
            String sql = null;
            if (instance.getFormid().equals("FDCUTPAYDETLLIST")) {         //�����ۿ�
                sql = "select seqno," +
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
                        "       from fdcutpaydetl where billstatus <= 1 and preflag = 0  order by seqno";
            } else  if (instance.getFormid().equals("FDPRECUTPAYDETLLIST")) {      //��ǰ����
                sql = "select seqno," +
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
                        "       from fdcutpaydetl where billstatus <= 1  and preflag = 1 order by seqno";
            }
            try {
                ConnectionManager cmanager = ConnectionManager.getInstance();
                CachedRowSet crs = cmanager.getRs(sql);

                if (crs.size() == 0) {
                    instance.getFormBean().getElement("TOEXCEL").setDescription("newwin");
                    ctx.setRequestAtrribute("msg", "û�з������������ݣ�");
                    ctx.setRequestAtrribute("isback", "0");
                    ctx.setRequestAtrribute("funcdel", "history.go(-1)");
                    ctx.setTarget("/showinfo.jsp");
                    return -1;
                }
                String poiexceltemp = PropertyManager.getProperty("POI_EXCEl_PATH");
                HashMap<String, Object> excelMap = new HashMap<String, Object>();
                excelMap.put("filenm", poiexceltemp + "\\����ϵͳ�ۿ��¼��ϸ.xls");
                excelMap.put("cellname", "dataArea");
                excelMap.put("crs", crs);
                if (crs.next()) {
                    datestr += " " + crs.getString("gthtjh_date");
                    crs.beforeFirst();
                }
                excelMap.put("date", datestr);

                ctx.getRequest().setCharacterEncoding("iso8859-1");
                ctx.getRequest().getSession().setAttribute("excelMap", excelMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ctx.setTarget("/excelByTmp");
        }

        if (button != null && button.equals("FAILDRECORDTOEXCEL")) {
            ctx.setAttribute("BUTTONNAME", button);
            trigger(manager, "FDOUTPUT2EXCEL", null);

        }
        return 0;
    }

    /*
      �����û�ѡ�еļ�¼����������cutpaydetl��¼��
     */
    private Fdcutpaydetl[] getCutPayDetlListByRecordnos(String[] recordnos) throws FdcutpaydetlDaoException {

        List results = new ArrayList();
        try {
            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();

            String journalno = null;

            for (int i = 0; i < recordnos.length; i++) {
                if (recordnos[i] != null && recordnos[i].trim().length() > 0) {
                    String[] str = recordnos[i].split("=");
                    journalno = StringUtils.strip(str[1], "\'");
                    results.add(detlDao.findByPrimaryKey(journalno));
                }
            }
            Fdcutpaydetl[] fdcutpaydetls = new  Fdcutpaydetl[results.size()];
            for (int i = 0; i < results.size(); i++) {
                fdcutpaydetls[i] = ( Fdcutpaydetl) results.get(i);
            }
            return fdcutpaydetls;
        } catch (Exception e) {
            Debug.debug(e);
            throw new  FdcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }
    }

    /*
     �������м�¼״̬Ϊ���´������ļ�¼��
     */
    private Fdcutpaydetl[] getAllNewCutPayDetlList() throws FdcutpaydetlDaoException {

        List results = new ArrayList();
        try {
            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();

            String journalno = null;


            String sql = "billstatus <= " +
                    FDBillStatus.MSG_CREATED +
                    " order by journalno";

            return detlDao.findByDynamicWhere(sql, null);

        } catch (Exception e) {
            Debug.debug(e);
            throw new  FdcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }
    }

    /*
     ���ݵ�ǰLIST�Ĳ�ѯSQL���ҷ��������ļ�¼��
     */
    private Fdcutpaydetl[] getCutPayDetlListBySql(String sql) throws FdcutpaydetlDaoException {

        try {
            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
//            return detlDao.findByDynamicSelect(sql, null);
            int index = sql.indexOf("where");
            sql = sql.substring(index+5);
            return detlDao.findByDynamicWhere(sql, null);
        } catch (Exception e) {
            Debug.debug(e);
            throw new  FdcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }
    }


    private void setCutpayDetlStatusBatch(Fdcutpaydetl[] detls, String billstatus,ErrorMessages msgs) throws FdcutpaydetlDaoException{
        try {
            FdcutpaydetlDao detlDao = FdcutpaydetlDaoFactory.create();
            Fdcutpaydetl fdcutpaydetl = null;

            String seqno = null;

            BigDecimal totalamt = new BigDecimal("0");
            int count =0;

            for (int i = 0; i < detls.length; i++) {
                seqno = detls[i].getSeqno();
                fdcutpaydetl = detlDao.findByPrimaryKey(seqno);
                FdcutpaydetlPk detlPk = new FdcutpaydetlPk(seqno);
                fdcutpaydetl.setBillstatus(billstatus);//����״̬
                detlDao.update(detlPk, fdcutpaydetl);
                totalamt = totalamt.add(fdcutpaydetl.getGthtjhJhje());
                count++;
            }
            msgs.add("������" + count + " ���ۿ��¼���ܽ��Ϊ��" + totalamt.toString());
        } catch (Exception e) {
            Debug.debug(e);
            throw new FdcutpaydetlDaoException("Exception: " + e.getMessage(), e);
        }

        
    }
    
}