//Source file: e:\\java\\zt\\cmsi\\biz\\LoanAdm.java

package zt.cmsi.biz;

import zt.cmsi.pub.ErrorCode;

import java.math.BigDecimal;

/**
 * 贷款的责任人、形态管理程序
 *
 * @author zhouwei
 *         $Date: 2005/06/28 07:00:35 $
 * @version 1.0
 *          <p/>
 *          版权：青岛中天公司
 */

public class LoanAdm {

    /**
     * 更改贷款责任人
     * --操作BMTableApp表（调用BMTable中的更新方法）
     *
     * @param BMNo
     * @param newResp
     * @param newPct
     * @return int
     * @roseuid 3FEA545D03A3
     */
    public static int loanRespChange(String BMNo, String newResp, BigDecimal newPct) {
        if (BMNo == null || newResp == null || newPct == null) {
            return ErrorCode.PARAM_IS_NULL;
        }
        UpToDateApp utda = new UpToDateApp();
        utda.firstResp = newResp;
        utda.firstRespPct = newPct;
        return BMTable.updateUpToDateApp(BMNo, utda);
    }

    /**
     * 贷款形态调整
     * 更改贷款形态
     * --操作BMInactLoan表
     *
     * @param BMNo
     * @param newCat2
     * @return int
     * @roseuid 3FEA550F01E6
     */
    public static int loanCat2Change(String BMNo, int newCat2) {
        return 0;
        /*
     try {
       DatabaseConnection dc = MyDB.getInstance().apGetConn();
       String sqlStr =
           " update bminactloan "
           + " set ILStatus = " + newCat2
           + " where bmno = '" + BMNo + "' ";

       int result = dc.executeUpdate(sqlStr);
       if (result < 0)
         return ErrorCode.UPDATE_ILSTAT_OF_ILOAN_FAILED;
       else
         return result;
     }
     catch (Exception e) {
       Debug.debug(Debug.TYPE_WARNING, e.toString());
       Debug.debug(e);
       return ErrorCode.UPDATE_ILSTAT_OF_ILOAN_FAILED;
     }
     finally {
       MyDB.getInstance().apReleaseConn();
     }
        */
    }

    public static void main(String[] args) {
        LoanAdm.loanCat2Change("12", 3);
    }
}
