package zt.cms.report;

/**
 * <p>Title: TEST DataBase and Pager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author wz
 * @version 1.0
 */
import zt.platform.db.DatabaseConnection;
import zt.cmsi.mydb.MyDB;
import java.sql.*;
import zt.platform.db.DBUtil;

public class SCTblAcc {
 private String itmId = "";
 private String newItmId = "";
 private String itmName = "";
 private String accList = "";
 private String dspAccList = "";
 private String dspItmList = "";
 private String dspSelectList = "";
 private String errMsg ="";
 private int procFlag = 0;
 private ResultSet rs = null;
 private DatabaseConnection dbc = null;
 private Connection conn=null;
 private Statement st=null;
 private String cmdAdd;
 private String cmdChg;
 private String cmdDel;

 public SCTblAcc() {
   try {
         dbc = MyDB.getInstance().apGetConn();
         conn = dbc.getConnection();
         st = conn.createStatement();
   }
    catch (Exception e) {
      this.errMsg="数据库连接失败！";
    }
  }

 /**
  * 初始化页面初始数值
  * @throws java.lang.Exception
  */
 public void init(){
  try{
      if (this.cmdAdd != null) {
          addData();
      }
      if (this.cmdChg != null) {
          chgData();
      }

      if (this.cmdDel != null) {
          delData();
      }
  }
  catch(Exception se)
  {
      this.errMsg="操作数据库失败";
  }

   String sSql;

   String TmpAcc, TmpName, Tmp;

//构建项目列表和已选择项目的详细信息
   sSql = "select * from sctblacc  order by tblAccNo";
   try{
       rs = st.executeQuery(sSql);
       String flag = "";
       int begin = 0;
       String TmpItmId = itmId;
       while (rs.next()) {
           TmpAcc = rs.getString("tblAccNo").trim();

           if (TmpAcc.equals(TmpItmId) || begin == 0) {
               itmId = rs.getString("tblAccNo").trim(); //当第一次调用时，需要
               newItmId = itmId;
               accList = rs.getString("ListAccNo");
               itmName = DBUtil.fromDB(rs.getString("tblAccName"));
               procFlag = rs.getInt("ProcFlag");

               flag = " selected ";
               begin = 1;
           }
           else {
               flag = "";
           }

           TmpName = DBUtil.fromDB(rs.getString("tblAccName"));
           Tmp = "<option value='" + TmpAcc + "'" + flag + ">" + TmpAcc + "--" +
               TmpName +
               "</option>";
           dspItmList = dspItmList + Tmp;

       }
   }
   catch(SQLException se)
   {
       this.errMsg="操作数据库失败";
   }

   if (procFlag == 3) { //项目列表
     dspAccList = dspItmList;
   }
   else { //科目列表
     sSql = "select AccNo,AccName from SCHostAcc order by AccNo";
     try
     {
         rs = st.executeQuery(sSql);

         while (rs.next()) {
             TmpAcc = rs.getString("AccNo").trim();
             TmpName = DBUtil.fromDB(rs.getString("AccName"));
             System.out.println(TmpName);
             Tmp = "<option value='" + TmpAcc + "'>" + TmpAcc + "--" + TmpName +
                 "</option>";
             dspAccList = dspAccList + Tmp;
         }
     }
     catch(SQLException se)
     {
        this.errMsg="操作数据库失败";
     }

     MyDB.getInstance().apReleaseConn(1);
   }
 }

private void addData() throws Exception {
   String sSql;
   sSql = "select * from sctblacc where tblaccno='" + newItmId + "'";
   rs = st.executeQuery(sSql);
   if (rs.next()) {
     errMsg = "要增加的数据已存在:" + newItmId;
   }
   else {
     sSql =
	 "insert into sctblacc (tblAccNo,TblAccName,ProcFlag,listAccNo) values ('"
	 + newItmId + "','"
	 +  DBUtil.toDB(itmName) + "',"
	 + procFlag + ",'"
	 + accList + "')";

     dbc.executeUpdate(sSql);
     System.out.println(sSql);
   }

 }

 private void chgData() throws Exception {
   String sSql;
   sSql = "update sctblacc set  TblAccNo='" + newItmId
       + "',TblAccName='" + DBUtil.toDB(itmName) + "',"
       + "ProcFlag=" + procFlag + ","
       + "listAccNo='" + accList + "'"
       + " where tblaccno='" + itmId + "'";

   dbc.executeUpdate(sSql);

 }

 private void delData() throws Exception {
   dbc.executeUpdate("delete from sctblacc where tblAccNo='" + itmId + "'");
   itmId = "";

 }

 public String getItmId() {
   return itmId;
 }

 public String getItmName() {
   return itmName;
 }

 public int getProcFlag() {
   return procFlag;
 }

 public String getDspAccList() {
   return dspAccList;
 }

 public String getDspSelectList() {
   return dspSelectList;
 }

 public String getDspItmList() {
   return dspItmList;
 }

 public String getErrMsg() {
   return errMsg;
 }

 public String getNewItmId() {
   return newItmId;
 }

 public String getAccList() {
   return accList;
 }


  public void setAccList(String accList) {
   if (accList.endsWith(",")){
     accList=accList.substring(0,accList.length()-1);
   }
   this.accList = accList;

 }

  public void setCmdAdd(String cmdAdd) {
    this.cmdAdd = cmdAdd;
  }
  public void setCmdChg(String cmdChg) {
    this.cmdChg = cmdChg;
  }
  public void setCmdDel(String cmdDel) {
    this.cmdDel = cmdDel;
  }
  public void setItmId(String itmId) {
    this.itmId = itmId;
  }
  public void setItmName(String itmName) {
    this.itmName = itmName;
  }
  public void setNewItmId(String newItmId) {
    this.newItmId = newItmId;
  }
  public void setProcFlag(int procFlag) {
    this.procFlag = procFlag;
  }

}
