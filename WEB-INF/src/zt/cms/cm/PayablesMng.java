/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: PayablesMng.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
 */
package zt.cms.cm;

import zt.platform.db.DatabaseConnection;
import zt.platform.db.RecordSet;
import zt.platform.form.util.FormInstance;
import zt.platform.form.util.event.ErrorMessages;
import zt.platform.form.util.event.EventManager;
import zt.platform.form.util.SqlAssistor;
import zt.platform.form.util.SqlField;
import java.sql.PreparedStatement;
import zt.platform.form.control.SessionContext;
import zt.platform.form.control.FormActions;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.*;
import zt.cms.pub.code.SerialNumber;
/**
 *  该业务名称 客户管理-企业负债情况(Page式)
 *
 *@author     Administrator
 *@created    2003年11月29日
 */
public class PayablesMng
         extends MngBase {
    /**
     *  Description of the Method
     *
     *@param  ctx       Description of the Parameter
     *@param  conn      Description of the Parameter
     *@param  instance  Description of the Parameter
     *@param  msgs      Description of the Parameter
     *@param  manager   Description of the Parameter
     *@param  assistor  Description of the Parameter
     *@return           Description of the Return Value
     */
    public int preInsert(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
            ErrorMessages msgs,
            EventManager manager, SqlAssistor assistor) {
        super.PageInsertBase("CMCORPPAYABLES", "SEQNO");
        return super.preInsert(ctx, conn, instance, msgs, manager, assistor);
    }

}
