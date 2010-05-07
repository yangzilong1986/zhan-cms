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
        //����ͨѸģ��
        try {
            String comm_start = PropertyManager.getProperty("comm_start");
            if (comm_start != null && comm_start.trim().toLowerCase().equals("true")) {
                String comm_path = PropertyManager.getProperty("comm_path");
                String comm_os = PropertyManager.getProperty("comm_os");
                /*��winxp��win2003�н�������*/
                if (comm_os != null && comm_os.trim().toLowerCase().equals("win2003")) {
                    Runtime.getRuntime().exec("taskkill /F /T /IM CMWComm.exe");
                }
                /*��winnt��win2000�н�������*/
                else {
                    Runtime.getRuntime().exec("tskill CMWComm /a");
                }
                /*sleep����ֹ�������Ľ��̱�ɱ��*/
                Thread.sleep(1000);
                /*�����µ�ͨѶģ��*/
                Runtime.getRuntime().exec(comm_path);
            }
        }
        catch (Exception ex) {
            System.out.println("****************************************************************************");
            System.out.println("���ش���:����ͨѸģ��ʧ��:" + ex.getMessage());
            System.out.println("****************************************************************************");
            //System.exit(0);
        }
        //װ��ϵͳ����
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
            System.out.println("װ��ϵͳ����ʧ��:" + ex.getMessage());
            System.exit(0);
        }
        System.out.println("System Loaded OK ..........................................!code=[" + code + "]");
    }
}
