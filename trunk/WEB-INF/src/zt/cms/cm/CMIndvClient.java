package zt.cms.cm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.zt.util.*;
import zt.cms.pub.*;
import zt.cmsi.biz.*;
import zt.cmsi.client.*;
import zt.cmsi.migration.*;
import zt.cmsi.mydb.*;
import zt.cmsi.pub.define.*;
import zt.platform.db.*;
import zt.platform.form.config.*;
import zt.platform.form.control.*;
import zt.platform.form.util.*;
import zt.platform.form.util.event.*;
import zt.platform.user.*;
import zt.platform.utils.*;
import zt.cms.bm.common.*;
import zt.platform.db.*;

public class CMIndvClient extends FormActions {
  /**
   * 扩展基类的load方法
   */
  private String flag = null; //窗体是否可读
  private String strClientNo = null; //实例的客户号
  private String initClientNo = null; //移行程序调用本程序时，传入的移行生成的客户号
  private String currClientNo = null; //移行程序使用
  private String guarantorID = null; //保证人转为客户时传过来的证件号码
  private boolean isG = false; //是否为担保人转换过来的

  public int load(SessionContext ctx, DatabaseConnection conn,
                  FormInstance instance, ErrorMessages msgs,
                  EventManager manager, String parameter) {

    int userNo = SessionInfo.getLoginUserNo(ctx);
    String roleStr = "select * from scuserrole where userno="+userNo+" and roleno=1";
    RecordSet roleRs = conn.executeQuery(roleStr);
    if(roleRs.next()){
      instance.useCloneFormBean();
      instance.getFormBean().setUseDelete(true);
    }

    //flag
    flag = (String) ctx.getRequestAttribute("flag");
    if (flag == null) {
      flag = "read";
    }
    flag = flag.toLowerCase();
    if (flag.equals("write")) {
      instance.setReadonly(false);
    }
    else {
      instance.setReadonly(true);
    }
    //CLIENTNO
    String CLIENTNO = ctx.getParameter("CLIENTNO");
    if (CLIENTNO == null) {
      CLIENTNO = (String) ctx.getRequestAttribute("CLIENTNO");
    }
    initClientNo = (String) ctx.getRequestAttribute("INITCLIENTNO");
    guarantorID = (String) ctx.getRequestAttribute("GID");
    String sql = "select * from cmindvclient where ";
    if (CLIENTNO != null && CLIENTNO.length() > 0) {
      strClientNo = CLIENTNO;
      sql += "clientno='" + CLIENTNO + "'";
    }
    else if (guarantorID != null && guarantorID.trim().length() >0) {
      sql += "id='" + guarantorID + "'";
    }
    if (sql.endsWith("where ")) {
      return 0;
    }

    RecordSet rs = conn.executeQuery(sql);
    if (rs.next()) {
//lj del in 20090316          
//      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//      String BRHID = SCUser.getBrhId(um.getUserName()); //登录用户所在网点
//      String APPBRHID = rs.getString("APPBRHID").trim(); //客户资料的登记网点
//      if ( strClientNo!=null && ! (SCBranch.checkSub(APPBRHID, BRHID) || APPBRHID.equals(BRHID))) {
//        flag = "read";
//        msgs.add("该客户不在本网点维护范围内！");
//        instance.setReadonly(true);
//      }
//      else
        if(guarantorID!=null && guarantorID.trim().length()>0){
        flag = "read";
        msgs.add("客户信息已经存在，不需要再做转移，请删除原来的保证人信息即可！");
        instance.setReadonly(true);
        strClientNo=rs.getString("clientno");
      }
      //trigger
      instance.setValue("CLIENTNO", strClientNo);
      trigger(manager, instance, EventType.EDIT_VIEW_EVENT_TYPE, Event.BRANCH_CONTINUE_TYPE);
    }
    return 0;
  }

  /**
   * 扩展基类的beforeInsert方法，点击“添加”按钮后响应的事件
   */
  public int beforeInsert(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {

    String GID = (String) ctx.getRequestAttribute("GID");
    String GIDTYPE = (String) ctx.getRequestAttribute("GIDTYPE");
    String GNAME = (String) ctx.getRequestAttribute("GNAME");
    String GCREDITCLASS = (String) ctx.getRequestAttribute("GCREDITCLASS");
    if (GID != null) {
      instance.setReadonly(false);
      instance.setValue("NAME", GNAME);
      instance.setValue("IDTYPE", GIDTYPE);
      instance.setValue("ID", GID);
      instance.setValue("CREDITCLASS", GCREDITCLASS);
      instance.setFieldReadonly("NAME", true);
      instance.setFieldReadonly("IDTYPE", true);
      instance.setFieldReadonly("ID", true);
      instance.setFieldReadonly("CREDITCLASS", true);
      this.isG = true;
    }

    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String BrhId = SCUser.getBrhId(um.getUserName());
    String APPDATE = SystemDate.getSystemDate5("");
    String OPERATOR = null;
    try {
      OPERATOR = um.getUserName();
    }
    catch (Exception ex) {
      return -1;
    }
    if (BrhId != null) {
      instance.setValue("MNTBRHID", BrhId);
      if (!SCBranch.getBrhtype(BrhId).trim().equals("9")) {
        instance.setValue("APPBRHID", BrhId);
      }
      instance.setValue("OPERATOR", OPERATOR);
      instance.setValue("APPDATE", APPDATE);
      instance.setValue("INVESTDATE", APPDATE);
      instance.setValue("LASTMODIFIED", APPDATE);
    }

    if (this.initClientNo != null) {
      MyDB.getInstance().addDBConn(conn);
      InitClientData initdata = InitClientMan.getInitClient(initClientNo);
      MyDB.getInstance().releaseDBConn();

      if (initdata == null) {
        msgs.add("移行客户信息不存在，请检查数据!");
        return -1;
      }
      else {
        if (initdata.appBrhID != null) {
          instance.setValue("APPBRHID", initdata.appBrhID);
        }
        if (initdata.appDate != null) {
          instance.setValue("APPDATE", util.calToString(initdata.appDate, null));
        }
        if (initdata.clientType != null) {
          instance.setValue("CLIENTTYPE", initdata.clientType.intValue());
        }
        if (initdata.ecomDeptType != null) {
          instance.setValue("ECOMDEPTTYPE", initdata.ecomDeptType.intValue());
        }
        if (initdata.ecomType != null) {
          instance.setValue("ECOMTYPE", initdata.ecomType.intValue());
        }
        if (initdata.etpScopType != null) {
          instance.setValue("ETPSCOPTYPE", initdata.etpScopType.intValue());
        }
        if (initdata.gender != null) {
          instance.setValue("GENDER", initdata.gender.intValue());
        }
        if (initdata.ID != null) {
          instance.setValue("ID", initdata.ID);
        }
        if (initdata.IDType != null) {
          //instance.setValue("IDTYPE", initdata.IDType.intValue());
        }
        if (initdata.name != null) {
          instance.setValue("NAME", initdata.name);
        }
        if (initdata.sectorCat1 != null) {
          instance.setValue("SECTORCAT1", initdata.sectorCat1.intValue());
        }
      }
    }
    return 0;
  }

  public int postInsertOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager) {
    if (this.isG) {
      return conn.executeUpdate("delete from cmguarantor where id='" + ctx.getParameter("ID") + "'");
    }


    if (this.initClientNo == null) {
      return 0;
    }

    MyDB.getInstance().addDBConn(conn);
    int ret = 0;
    try {
      UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.
        USER_INFO_NAME);
      String OPERATOR = null;
      OPERATOR = um.getUserName();

      String clientno = null;

      if (this.initClientNo != null) {
        if (currClientNo == null) {
          msgs.add("客户号码未发现，请重做交易！");
          return -1;
        }
        else {
          ret = ClientMigration.clientMrig(initClientNo, currClientNo, OPERATOR);
          if (ret >= 0) {
            /**
             * I comment following line temporarily to speed up perfomance
             * JGO on 20040905
             */
            //ret = CMClientMan.updateClientCatAndType(currClientNo, OPERATOR);
            ret = 0;
          }
          if (ret < 0) {
            String msg = PropertyManager.getProperty("" + ret);
            if (msg == null) {
              msg = "" + ret;
            }
            ctx.setRequestAtrribute("title", "客户信息移行");
            ctx.setRequestAtrribute("msg", "处理失败:" + msg);
            ctx.setRequestAtrribute("flag", "0");
            ctx.setTarget("/showinfo.jsp");
          }
          else {
            ctx.setRequestAtrribute("title", "客户信息移行");
            ctx.setRequestAtrribute("msg", "处理成功完成！");
            ctx.setRequestAtrribute("flag", "1");
            ctx.setTarget("/showinfo.jsp");
          }
        }
      }
    }
    catch (Exception e) {
      if (Debug.isDebugMode) {
        e.printStackTrace();
      }
      ret = -1;
    }
    finally {
      MyDB.getInstance().releaseDBConn();
      return ret;
    }
  }

  /**
   * 扩展基类的preInsert方法，对实际业务做insert前的处理
   */
  public int preInsert(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {
    String CLIENTNO = zt.cms.pub.code.ClientNo.getNextSN() + "";
    currClientNo = CLIENTNO;
    String ID = ctx.getParameter("ID").trim();
    String CLIENTMGR = ctx.getParameter("CLIENTMGR").trim();
//    String UNIONNO = ctx.getParameter("UNIONNO").trim();
//    String RGNCODE = ctx.getParameter("RGNCODE").trim();
    String MNTBRHID = ctx.getParameter("MNTBRHID").trim();
    String APPBRHID = ctx.getParameter("APPBRHID").trim();

    //ID唯一检测
    String ID2 = "";
    if (ID.length() == 18) {
      ID2 = ID.substring(0, 6) + ID.substring(8, 17);
    }
    else if (ID.length() == 15) {
      ID2 = ID.substring(0, 6) + "19" + ID.substring(6, 15);
    }
    else {
      msgs.add("个人证件号码长度不正确，请仔细检查您输入的号码！");
      return -1;
    }
    String tmp = "";
    if (ID.length() == 18) {
      tmp = ID2;
      ID2 = ID;
      ID = tmp;
    }
    if (DBUtil.getCellValue(conn, "CMINDVCLIENT", "CLIENTNO", "ID='" + ID + "'") != null || DBUtil.getCellValue(conn, "CMINDVCLIENT", "CLIENTNO", "ID LIKE '" + ID2 + "%'") != null) {
      msgs.add("个人证件号码已经存在，请仔细检查您输入的号码！");
      return -1;
    }
    //CLIENTMGR存在检测
    if (DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME",
                            "LOGINNAME='" + CLIENTMGR + "' and USERTYPE<>'3'") == null) {
      msgs.add("客户经理不存在，请仔细检查后再提交！");
      return -1;
    }
    //UNIONNO存在检测
//    if (DBUtil.getCellValue(conn, "CMFAMILYUNION", "UNIONNO", "UNIONNO=" + UNIONNO) == null) {
//      msgs.add("联户联保小组代码不存在，请仔细检查后再提交！");
//      return -1;
//   }
    //UNIONNO在开户网点内
    //RGNCODE存在检测
//    if (RGNCODE.length() > 0 &&
//        DBUtil.getCellValue(conn, "CMREGION", "RGNCODE", "RGNCODE='" + RGNCODE + "'") == null) {
//      msgs.add("行政机构代码不存在，请仔细检查后再提交！");
//      return -1;
//    }
    //网点检测
//    if (! (SCBranch.checkSub(APPBRHID, MNTBRHID) || MNTBRHID.equals(APPBRHID))) {
//      msgs.add("业务网点不在维护网点范围内！");
//      return -1;
//    }
    if (SCBranch.getBrhtype(APPBRHID).equals("9")) {
      msgs.add("业务网点不能是虚网点！");
      return -1;
    }
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "CLIENTNO", CLIENTNO);
    return 0;
  }

  public int preReference(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance, ErrorMessages msgs,
                          EventManager manager, String reffldnm,
                          SqlWhereUtil sqlWhereUtil) {
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//    if (reffldnm.equals("APPBRHID") || reffldnm.equals("CLIENTMGR") || reffldnm.equals("UNIONNO")) {
    if (reffldnm.equals("APPBRHID") || reffldnm.equals("CLIENTMGR")) {
      //BRHID（用户网点）
      String BRHID = SCUser.getBrhId(um.getUserName());
      if (BRHID == null || BRHID.length() < 1) {
        msgs.add("用户网点不存在！");
        return -1;
      }
      //SUBBRHIDs（用户网点下的所有实网点，包括自己）
      String SUBBRHIDs = SCBranch.getSubBranchAll(BRHID);
      if (SUBBRHIDs == null || SUBBRHIDs.length() < 1) {
        msgs.add("下属网点不存在！");
        return -1;
      }
      else {
        SUBBRHIDs = "'" + SUBBRHIDs.replaceAll(",", "','") + "'";
      }
      //sqlWhereUtil
      if (reffldnm.equals("APPBRHID")) {
        sqlWhereUtil.addWhereField("SCBRANCH", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In);
      }
      if (reffldnm.equals("CLIENTMGR")) {
        sqlWhereUtil.addWhereField("SCUSER", "BRHID", SUBBRHIDs,
                                   SqlWhereUtil.DataType_Sql,
                                   sqlWhereUtil.OperatorType_In,
                                   sqlWhereUtil.RelationOperator_And);
        sqlWhereUtil.addWhereField("SCUSER", "USERTYPE", "3",
//                                   SqlWhereUtil.DataType_Number,
                                   SqlWhereUtil.DataType_Character,
                                   sqlWhereUtil.OperatorType_Not_Equals);
      }
//      if (reffldnm.equals("UNIONNO")) {
//        sqlWhereUtil.addWhereField("CMFAMILYUNION", "BRHID", SUBBRHIDs,
//                                   SqlWhereUtil.DataType_Sql,
//                                   sqlWhereUtil.OperatorType_In);
//      }
    }
    return 0;
  }

  public int preEdit(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    String CLIENTNO = ctx.getParameter("CLIENTNO").trim();
    String ID = ctx.getParameter("ID").trim();
    String CLIENTMGR = ctx.getParameter("CLIENTMGR").trim();
//    String UNIONNO = ctx.getParameter("UNIONNO").trim();
//    String RGNCODE = ctx.getParameter("RGNCODE").trim();
    String MNTBRHID = ctx.getParameter("MNTBRHID").trim();
    String APPBRHID = ctx.getParameter("APPBRHID").trim();
    String LASTMODIFIED = SystemDate.getSystemDate2();
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String BrhId = SCUser.getBrhId(um.getUserName());
    String OPERATOR = null;
    try {
      OPERATOR = um.getUserName();
    }
    catch (Exception ex) {
      return -1;
    }
    //ID唯一检测
    String ID2 = "";
    if (ID.length() == 18) {
      ID2 = ID.substring(0, 6) + ID.substring(8, 17);
    }
    else if (ID.length() == 15) {
      ID2 = ID.substring(0, 6) + "19" + ID.substring(6, 15);
    }
    else {
      msgs.add("个人证件号码长度不正确，请仔细检查您输入的号码！");
      return -1;
    }
    String tmp = "";
    if (ID.length() == 18) {
      tmp = ID2;
      ID2 = ID;
      ID = tmp;
    }
    String str1 = "select * from CMINDVCLIENT where ID='" + ID + "' and clientno<>'" + ctx.getParameter("CLIENTNO") + "'";
    String str2 = "select * from CMINDVCLIENT where ID like '" + ID2 + "%' and clientno<>'" + ctx.getParameter("CLIENTNO") + "'";
    RecordSet rs1 = conn.executeQuery(str1);
    RecordSet rs2 = conn.executeQuery(str2);
    if (rs1.next() || rs2.next()) {
      msgs.add("个人证件号码已经存在，请仔细检查您输入的号码！");
      return -1;
    }

    //CLIENTMGR存在检测
    if (DBUtil.getCellValue(conn, "SCUSER", "LOGINNAME",
                            "LOGINNAME='" + CLIENTMGR + "' and USERTYPE<>'3'") == null) {
      msgs.add("客户经理不存在，请仔细检查后再提交！");
      return -1;
    }
    //UNIONNO存在检测
//    if (DBUtil.getCellValue(conn, "CMFAMILYUNION", "UNIONNO", "UNIONNO=" + UNIONNO) == null) {
//      msgs.add("联户联保小组代码不存在，请仔细检查后再提交！");
//      return -1;
//    }
    //UNIONNO在开户网点内
    //RGNCODE存在检测
//    if (RGNCODE.length() > 0 &&
//        DBUtil.getCellValue(conn, "CMREGION", "RGNCODE", "RGNCODE='" + RGNCODE + "'") == null) {
//      msgs.add("行政机构代码不存在，请仔细检查后再提交！");
//      return -1;
//    }
    //网点检测
//    if (! (SCBranch.checkSub(APPBRHID, MNTBRHID) || MNTBRHID.equals(APPBRHID))) {
//      msgs.add("登记网点不在维护网点范围内！");
//      return -1;
//    }
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "MNTBRHID", BrhId);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "LASTMODIFIED", LASTMODIFIED);
    assistor.setSqlFieldValue(assistor.getDefaultTbl(), "OPERATOR", OPERATOR);
    return 0;
  }

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
  public int preFind(SessionContext ctx, DatabaseConnection conn,
                     FormInstance instance, ErrorMessages msgs,
                     EventManager manager, SqlAssistor assistor) {
    return 0;
  }

  public int postFindOk(SessionContext ctx, DatabaseConnection conn,
                        FormInstance instance, ErrorMessages msgs,
                        EventManager manager, RecordSet result) {
    //BRHID（用户网点）
    UserManager um = (UserManager) ctx.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String BRHID = SCUser.getBrhId(um.getUserName());
    if (BRHID == null || BRHID.length() < 1) {
      msgs.add("登录用户信息错误！");
      return -1;
    }
    //APPBRHID（登记网点）
    String APPBRHID = result.getString("APPBRHID").trim();
    if (! (SCBranch.checkSub(APPBRHID, BRHID) || BRHID.equals(APPBRHID))) {
      msgs.add("对不起，该客户的资料不在您的维护网点范围内！");
      return -1;
    }
    return 0;
  }

  /**
   * 扩展基类的preDelete方法，对实际业务做删除前的检验
   */
  public int preDelete(SessionContext ctx, DatabaseConnection conn,
                       FormInstance instance, ErrorMessages msgs,
                       EventManager manager, SqlAssistor assistor) {


    String cltNo = ctx.getParameter("CLIENTNO");

    String bmStr = "select * from bmtable where clientno='"+cltNo+"'";
    RecordSet bmRs = conn.executeQuery(bmStr);
    if(bmRs.next()){
      msgs.add("还有业务没有删除");
      return -1;
    }

    RecordSet rs = conn.executeQuery("select id from CMINDVCLIENT where clientno='" + cltNo + "'");

    if (!rs.next()) {
      msgs.add("取得id失败！");
      return -1;
    }
    else {
      String id = rs.getString("ID");
      String tempStr = "select * from bmguarantor where id='" + id + "'";
      //System.out.println(tempStr);
      RecordSet tempRs = conn.executeQuery(tempStr);
      if (tempRs.next()) {
        msgs.add("还有担保的贷款,不能删除！");
        return -1;
      }
    }



    String sql1[] = new String[4];
    sql1[0] = "delete from CMINDVRELA where clientno='" + cltNo + "'";
    sql1[1] = "delete from CMINDVASSET where clientno='" + cltNo + "'";//
    sql1[2] = "delete from CMINDVDEBT where clientno='" + cltNo + "'";//
    sql1[3] = "delete from CMINDVDEBTPAYMENT where clientno='" + cltNo + "'";//
    String sql2 = "";
    for (int i = 0; i < sql1.length; i++) {
      sql2 += sql1[i] + ";";
    }
    sql2 = sql2.substring(0, sql2.length() - 1);
    //System.out.println(sql2);
    int ret = conn.executeUpdate(sql2);
    return ret;
  }

  /**
   * 扩展基类的postDeleteOk方法，对实际业务做删除成功后的处理
   */
  public int postDeleteOk(SessionContext ctx, DatabaseConnection conn,
                          FormInstance instance,
                          ErrorMessages msgs,
                          EventManager manager) {
    return 0;
  }

  /**
   * 扩展基类的postDeleteFail方法，对实际业务做删除失败后的处理
   */
  public int postDeleteFail(SessionContext ctx, DatabaseConnection conn,
                            FormInstance instance,
                            ErrorMessages msgs,
                            EventManager manager) {
    return 0;
  }

  public int buttonEvent(SessionContext ctx, DatabaseConnection conn,
                         FormInstance instance, String button,
                         ErrorMessages msgs, EventManager manager) {
    //String CLIENTNO = instance.getStringValue("CLIENTNO");
    String CLIENTNO = strClientNo;
    //String CLIENTNO = ctx.getParameter("CLIENTNO");
    if (CLIENTNO == null || CLIENTNO.length() < 1 || CLIENTNO.equals("自动生成")) {
      System.out.println(CLIENTNO);
      msgs.add("请确定个人客户资料信息已经存在！");
      return -1;
    }
    //setRequestAtrribute
    ctx.setRequestAtrribute("flag", flag);
    ctx.setRequestAtrribute("CLIENTNO", CLIENTNO);

    if (button.equals("CMINDVRELA")) {
      trigger(manager, "100012", null);
    }
    else if (button.equals("BMRQL")) {
      trigger(manager, "BMRQL1", null);
    }
    else if (button.equals("BMCREDITLMAINTPAGE")) {
      trigger(manager, "BMCREDITLMAINTPAGE", null);
    }
    else if (button.equals("PHOTO")) {
      //ctx.setRequestAtrribute("PHOTONO",ctx.getParameter("PHOTONO"));
      ctx.setRequestAtrribute("TABLENAME", "CMINDVCLIENT");
      ctx.setRequestAtrribute("WHERECONDITION", "clientno='" + CLIENTNO + "'");
      ctx.setRequestAtrribute("title", "个人客户图片");
      ctx.setRequestAtrribute("flag", this.flag);
      ctx.setTarget("/photo/photo.jsp");
    }
    else if (button.equals("CREDIT")) {
      RecordSet rs = conn.executeQuery("select id from CMINDVCLIENT where clientno='" + CLIENTNO + "'");
      if (!rs.next()) {
        msgs.add("取得id失败！");
        return -1;
      }
      String id = rs.getString("ID");
      ctx.setRequestAtrribute("ID", id);
      trigger(manager, "GUARANTORCREDITLIST", null);
      return 0;
    }

    /**
         else if (button.equals("CMINDVASSET")) {
      trigger(manager, "100013", null);
         }
         else if (button.equals("CMINDVDEBT")) {
      trigger(manager, "100014", null);
         }
         else if (button.equals("CMINDVDEBTPAYMENT")) {
      trigger(manager, "100015", null);
         }
     */

    return 0;
  }

  public void postField(SessionContext ctx, FormInstance instance, String fieldname,
          ErrorMessages msgs, EventManager manager) {

    String id = (String)ctx.getRequestAttribute("ID");
    if( id == null || id.trim().length() <= 0)
    {
      instance.setHTMLShowMessage("错误","必须输入身份证号码!");
      return;
    }

    id = id.trim();
    if(id.length() != 15 && id.length() != 18)
    {
      instance.setHTMLShowMessage("错误","身份证号码长度必须为15或18位!");
      instance.setHTMLFieldValue("ID","");
      return;
    }

    if(id.length() == 15)
    {
      if(util.isDate("19"+id.substring(6,12)) == false)
      {
        instance.setHTMLShowMessage("警告", "证件号码日期格式非法!");
        return;
      }
    }
    else
    {
        if(id.charAt(17) == 'x')
        {
          instance.setHTMLShowMessage("错误", "证件号码的校验位不能是小写x!");
          instance.setHTMLFieldValue("ID","");
          return;
        }

        if(util.isDate(id.substring(6,14)) == false)
        {
          instance.setHTMLShowMessage("警告", "证件号码日期格式非法!");
          return;
        }

        int iS = 0,iY;
        int iW[]={7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String szVerCode="10X98765432";

        int i;
        for(i=0;i<17;i++)
        {
          iS += (int)(id.charAt(i)-'0') * iW[i];
        }
        iY = iS%11;
        if ( szVerCode.charAt(iY) != id.charAt(i) )
        {
          instance.setHTMLShowMessage("警告","身份证号码校验码错误!");
          return;
        }
    }

//    String year = null;
//    if(id.length() == 15)
//      year = "19" + id.substring(6,8);
//    else
//      year = id.substring(6,10);
//    int iYear;
//
//    try
//    {
//      iYear = Integer.parseInt(year);
//    }
//    catch(Exception e)
//    {
//      instance.setHTMLShowMessage("警告","身份证号码生日格式错误!");
//      return;
//    }
//
//    if(iYear <= 1900)
//    {
//      instance.setHTMLShowMessage("警告","身份证号码生日早于1900年!");
//      return;
//    }

    return;
  }


}
