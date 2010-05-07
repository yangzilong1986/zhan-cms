package zt.cmsi.startup;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.zt.util.PropertyManager;
import zt.cms.pub.SCBranch;
import zt.cms.pub.SCUser;
import zt.cmsi.pub.define.BMRouteBind;
import zt.cmsi.pub.define.UserRoleMan;
import zt.cmsi.pub.define.XFGradeMark;
import zt.platform.form.config.EnumerationType;
import zt.platform.form.config.FormBeanManager;
import zt.platform.form.config.TableBeanManager;
import zt.platform.utils.Debug;

import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;


public class startupServlet extends HttpServlet implements SingleThreadModel {
    static {
        int code = 0;
        System.out.println("System Loading ............................................!");
        //启动通迅模块
        try {
            String comm_start = PropertyManager.getProperty("comm_start");
            if (comm_start != null && comm_start.trim().toLowerCase().equals("true")) {
                String comm_path = PropertyManager.getProperty("comm_path");
                String comm_os = PropertyManager.getProperty("comm_os");
                /*在winxp和win2003中结束进程*/
                if (comm_os != null && comm_os.trim().toLowerCase().equals("win2003")) {
                    Runtime.getRuntime().exec("taskkill /F /T /IM CMWComm.exe");
                }
                /*在winnt和win2000中结束进程*/
                else {
                    Runtime.getRuntime().exec("tskill CMWComm /a");
                }
                /*sleep，防止下面刚起的进程被杀死*/
                Thread.sleep(1000);
                /*启动新的通讯模块*/
                Runtime.getRuntime().exec(comm_path);
            }
        }
        catch (Exception ex) {
            System.out.println("****************************************************************************");
            System.out.println("严重错误:启动通迅模块失败:" + ex.getMessage());
            System.out.println("****************************************************************************");
            //System.exit(0);
        }
        //装载系统数据
        try {
            //platform configuration data loading
            new EnumerationType();
            new TableBeanManager();
            new FormBeanManager();
            //CMS configuration data loading
            //new SCBranch();//lj del in 20090319
            new SCUser();
//************************lj del in 20090319             
//            BMAct.getInstance();
//            BMAppCriteria.getInstance();
//            BMCPConfMan.getInstance();
//            BMReviewLimit.getInstance();
//            BMRoute.getInstance();
//            BMType.getInstance();
//            zt.cmsi.pub.define.BMTypeOpen.getInstance();
//            SystemDate.getSystemDate2();
//************************
            BMRouteBind.getInstance();//lj add in 20090319
            XFGradeMark.getInstance();//lj add in 20090422
            UserRoleMan.getInstance();
            SCBranch.checkDirty();


            //new current SN number
//************************lj del in 20090319
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMAcptBill.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMBILLDIS.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMComments.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMGuarantor.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMILNotifi.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMILPaybackAgreement.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMILPaymentOrder.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMILProsecution.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMILRecallApp.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMLoanAdmChange.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMLoanRespChange.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMLoanTypeChange.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPDAssets.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPDTrans.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPldgBillDis.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPldgMort.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPldgPDAsset.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPldgSecurity.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMPostLoanCheck.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMReviewLimitCode.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMTransNo.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.BMTypeOpen.getCurNo());
//************************
            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.GLBlobObjID.getCurNo());
            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.GLBLOBSeqNo.getCurNo());
            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.GLBulletin.getCurNo());
            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.GLMsg.getCurNo());
//************************lj del in 20090319
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.SNBusinessID.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.FCMAIN.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.FCPrd.getCurNo());
//            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cmsi.pub.code.FCReason.getCurNo());
//************************            
            Debug.debug(Debug.TYPE_MESSAGE, "CURR SN:" + zt.cms.pub.code.ClientNo.getCurSN());

            //CMS Loan Grant Thread starting
            //code = StartService.getInstance().startupService();//lj del in 20090319
        }
        catch (Exception ex) {
            System.out.println("装载系统数据失败:" + ex.getMessage());
            System.exit(0);
        }
        System.out.println("System Loaded OK ..........................................!code=[" + code + "]");
    }
}
