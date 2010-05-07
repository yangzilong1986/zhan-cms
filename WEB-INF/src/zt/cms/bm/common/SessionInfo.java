package zt.cms.bm.common;

import javax.servlet.http.*;

import zt.cms.pub.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.user.*;

public class SessionInfo {

  public static String getLoginUserName(SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String strUserName = null;
    try {
      if(um==null) return strUserName;
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return strUserName;
  }

  public static String getLoginAllSubBrhIds(SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String mybrhid = SCUser.getBrhId(um.getUserName());
    if (mybrhid == null || mybrhid.length() < 1) {
      return null;
    }
    //APPBRHIDs（用户网点下的所有实网点，包括自己）
    String SUBBRHIDs = SCBranch.getSubBranchAll(mybrhid);
    if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
      return null;
    }
    else {
      SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
    }
    return SUBBRHIDs;
  }
  public static String getLoginAllSubBrhIds(HttpSession ctx) {
  UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
  //BRHID（用户网点）
  String mybrhid = SCUser.getBrhId(um.getUserName());
  if (mybrhid == null || mybrhid.length() < 1) {
    return null;
  }
  //APPBRHIDs（用户网点下的所有实网点，包括自己）
  String SUBBRHIDs = SCBranch.getSubBranchAll(mybrhid);
  if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
    return null;
  }
  else {
    SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
  }
  return SUBBRHIDs;
}


  public static String getLoginBrhId(SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String mybrhid = SCUser.getBrhId(um.getUserName());
    return mybrhid;
  }

  public static String getLoginBrhId(HttpSession ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //BRHID（用户网点）
    String mybrhid = SCUser.getBrhId(um.getUserName());
    return mybrhid;
  }

  public static String getLoginUserName(HttpSession ctx) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String strUserName = null;
    try {
      if(um==null) return strUserName;
      strUserName = um.getUserName();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return strUserName;
  }

  public static int getLoginUserNo(HttpSession ctx) {
    UserManager um = (UserManager) ctx.getAttribute( SystemAttributeNames.USER_INFO_NAME);
    int userNo = -1;
    try {
      if(um==null) return userNo;
      userNo = Integer.parseInt(um.getUserId());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return userNo;
  }

  public static int getLoginUserNo(SessionContext ctx) {
    UserManager um = (UserManager) ctx.getAttribute( SystemAttributeNames.USER_INFO_NAME);
    int userNo = -1;
    try {
      if(um==null) return userNo;
      userNo = Integer.parseInt(um.getUserId());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return userNo;
  }
}
