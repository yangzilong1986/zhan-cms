package zt.cms.cm.common;
/*
 *  Copyright (c) 2002 Email: sundj@zhongtian.biz
 *  $Id: RightChecker.java,v 1.1 2005/06/28 07:00:31 jgo Exp $
 */

import java.util.logging.*;

import zt.platform.db.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;

public class RightChecker {
  public RightChecker() {}
  public static Logger logger = Logger.getLogger("zt.cms.cm.common.RightChecker");

  public static void checkReadonly(SessionContext ctx, DatabaseConnection conn, FormInstance instance) {
    String privilege1 = ctx.getParameter("flag");
    String privilege2 = null;
    if (ctx.getRequestAttribute("flag") != null) {
      privilege2 = (String) ctx.getRequestAttribute("flag");
    }

    if (privilege2 == null) {
      if (privilege1 != null) {
        if (privilege1.trim().equals("read")) {
          instance.setReadonly(true);
        }
        else if (privilege1.trim().equals("write")) {
          instance.setReadonly(false);
        }
      }
    }
    else if (privilege2.trim().equals("read")) {
      instance.setReadonly(true);
    }
    else if (privilege2.trim().equals("write")) {
      instance.setReadonly(false);
    }
    //logger.info(instance.getFormid() + " readonly is " + instance.isReadonly());
  }

  public static void transReadOnly(SessionContext ctx, DatabaseConnection conn, FormInstance instance) {
    if (instance.isReadonly() == true) {
      ctx.setRequestAtrribute("flag", "read");
    }
    else {
      ctx.setRequestAtrribute("flag", "write");
    }

  }

}
