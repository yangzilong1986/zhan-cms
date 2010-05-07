/*
 *  Copyright (c) 2004 Email: liwei@zhongtian.biz
 *  $Id: MyEditFieldFormat.java,v 1.1 2005/06/28 07:00:27 jgo Exp $
 */
package zt.cms.cm;

import zt.platform.form.control.SessionContext;
import zt.platform.db.RecordSet;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.event.*;
import zt.platform.form.util.datatype.DataType;
import zt.platform.db.*;
import zt.platform.form.util.*;

/**
 *  Description of the Class
 *
 *@author     李伟
 *@created    2003年12月3日
 */
public class MyEditFieldFormat
         extends FieldFormat {
    /**
     *  Description of the Method
     *
     *@param  ctx     Description of the Parameter
     *@param  eb1     Description of the Parameter
     *@param  fi      Description of the Parameter
     *@param  values  Description of the Parameter
     *@return         Description of the Return Value
     */
    public String format(SessionContext ctx, ElementBean eb1, FormInstance fi, RecordSet values) {
        String name = eb1.getName();
        String fldvalue = values.getString(name);
        if (fldvalue != null) {
            fldvalue = fldvalue.trim();
        }
        return DBUtil.fromDB(fldvalue);
    }

}
