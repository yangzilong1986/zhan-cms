package zt.cms.bm.inactloan;

import zt.platform.form.control.FormActions;
import java.sql.*;
import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004��1��6��
 */
public class PaymentOrderListAction extends CommonAction {
    /**
     *  Gets the pageFormId attribute of the PaymentOrderListAction object
     *
     *@return    The pageFormId value
     */
    public String getPageFormId() {
        return "BMILPAYMENTORDERPAGE";
    }
}
