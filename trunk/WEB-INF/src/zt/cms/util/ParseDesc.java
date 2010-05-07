package zt.cms.util;

/**
 * <p>Title: </p>
 * <p>Description:
       parse the   description Fields of the  ptfforminfomain Table
 * <p>Copyright: Copyright (c) 2004-10-11</p>
 * <p>Company: </p>
 * @author SZG
 * @version 1.0
 */

import java.util.logging.*;
import java.util.*;

public class ParseDesc {
  private static Logger logger = Logger.getLogger("zt.cms.util.ParseDesc");
  String desc;
  HashMap map = new HashMap();

  public ParseDesc() {
  }

  public void setDesc(String desc) {
    this.desc = desc;
    if (desc == "" && desc == null) {
      logger.severe(" szg ParseDesc:setDesc() description  is nothing can't  to be parsed");
    }
    else
    {
      ParseDesc();
    }
  }

  public String getDesc() {
    return this.desc;
  }

  public void ParseDesc() {
    String[] Str = desc.split(",");
    String[] oneStr;
    String firstStr = "";
    String secondStr = "";
    try {
      for (int i = 0; i < Str.length; i++) {
        oneStr = Str[i].split("=");
        firstStr = oneStr[0];
        secondStr = oneStr[1];
        if (secondStr == "" && secondStr == null) {
          logger.warning("szg parseDesc(): might be short of '=' after the "+firstStr);
        }
        else {
//            logger.info(" oneStr==" + oneStr + "  secondStr==" + secondStr);
          map.put(firstStr, secondStr);
        }
      }
    }
    catch (Exception e) {
      logger.warning("szg parseDesc:ParseDesc() the wong is " + e);
    }
  }

  public String getAttribute(String key) {
    String value = "";
    try {
      value = map.get(key).toString();
    }
    catch (Exception e) {
      logger.warning("szg parseDesc:getAttribute the wong is " + e);
      value = "";
    }
    return value;
  }

}
