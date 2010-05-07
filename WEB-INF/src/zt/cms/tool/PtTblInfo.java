package zt.cms.tool;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class PtTblInfo {
  //配置变量
  private static Logger log = Logger.getLogger("zt.cms.tool.PtTblInfo");
  private static String tbCreator="CMS";
  //要生成哪个table的detl，“all”代表全部
  private static String tables="BMROUTEBIND";
  //成员变量
  private static DBAssistor dba;
  private static ArrayList tblArr=new ArrayList();
  private static Map pkMap=new HashMap();
  private static Map fkMap=new HashMap();

  /**
   * 构造函数。
   */
  public PtTblInfo() {
    try{
      //dba
      dba=new DBAssistor();
      //tblArr
      initTblArr();
      //pkMap
      initPkMap();
      //fkMap
      initFkMap();
    }
    catch(Exception ex){
      log.info(ex.getMessage());
      System.exit(0);
    }
  }

  /**
   * listMain
   */
  private static void initTblArr(){
    Connection conn=dba.getConn();
    if(conn==null) return;
    try{
      Statement stmt = conn.createStatement();
      //ResultSet rs=stmt.executeQuery("select * from sysibm.systables where creator='"+tbCreator+"' and name not like 'PT%' and name not like'PF%'");
      ResultSet rs=stmt.executeQuery("select * from sysibm.systables where creator='"+tbCreator+"'");
      java.sql.ResultSetMetaData rsmd=rs.getMetaData();
      while(rs.next()){
        String tmp=rs.getString(1).trim();
        if(tables!="all"){
          if(!tmp.toLowerCase().equals(tables.toLowerCase())){
            continue;
          }
        }
        tblArr.add(tmp);
      }
      dba.freeConn(conn);
    }
    catch(Exception ex){
      log.info(ex.getMessage());
    }
  }

  /**
   * initPkMap
   */
  private static void initPkMap(){
    Connection conn=dba.getConn();
    if(conn==null) return;
    try{
      Statement stmt = conn.createStatement();
      //一个表里面只能有一个主键，故UNIQUERULE='P'可以找到所有的
      //主键，且tbname不会重复，这样就可以放到map里面了。
      ResultSet rs=stmt.executeQuery("Select tbname,colnames from SYSIBM.SYSINDEXES WHERE TBCREATOR='"+tbCreator.toUpperCase()+"' AND UNIQUERULE='P'");
      while(rs.next()){
        pkMap.put(rs.getString(1).trim(),rs.getString(2).trim());
      }
      dba.freeConn(conn);
    }
    catch(Exception ex){
      log.info(ex.getMessage());
    }
  }
  /**
   * initFkMap
   */
  private static void initFkMap(){
    Connection conn=dba.getConn();
    if(conn==null) return;
    try{
      Statement stmt = conn.createStatement();
      //从SYSRELS中取出所有的关系
      ResultSet rs=stmt.executeQuery("Select tbname,fkcolnames,reftbname,pkcolnames from SYSIBM.SYSRELS WHERE CREATOR='"+tbCreator.toUpperCase()+"'");
      while(rs.next()){
        //System.out.println(rs.getString(1).trim()+"."+rs.getString(2).trim());
        fkMap.put(rs.getString(1).trim()+"."+rs.getString(2).trim(),rs.getString(3).trim()+"."+rs.getString(4).trim());
      }
      dba.freeConn(conn);
    }
    catch(Exception ex){
      log.info(ex.getMessage());
    }
  }
  /**
   * isPk
   */
  private static String isPk(String p_tblName,String p_fldName){
    Object tmp0=pkMap.get(p_tblName);
    if(tmp0==null) return "0";
    String tmp1=tmp0.toString();
    if(tmp1.length()<1) return "0";
    //tmp1=tmp1.replaceAll("+",",");
    tmp1=tmp1.substring(1);
    StringTokenizer tmp2=new StringTokenizer(tmp1,"+");
    while(tmp2.hasMoreTokens()){
      if(tmp2.nextToken().equals(p_fldName)) return "1";
    }
    return "0";
  }
  /**
   * getRefTbl
   * getRefValue
   */
  private static String getRefTbl(String p_tblName,String p_fkName){
    Object tmp0=fkMap.get(p_tblName+"."+p_fkName);
    if(tmp0==null) return null;
    String tmp1=tmp0.toString();
    if(tmp1.length()<1) return null;
    String tmp2[]=tmp1.split("\\.");
    return tmp2[0];
  }
  private static String getRefValue(String p_tblName,String p_fkName){
    Object tmp0=fkMap.get(p_tblName+"."+p_fkName);
    if(tmp0==null) return null;
    String tmp1=tmp0.toString();
    if(tmp1.length()<1) return null;
    String tmp2[]=tmp1.split("\\.");
    return tmp2[1];
  }

  /**
   * getType
   */
  private static String getType(String p_type){
    if(p_type.equals("VARCHAR")||p_type.equals("CHARacter")||p_type.equals("CHAR")){
       return "1";
    }
    if(p_type.equals("INTeger")){
       return "2";
    }
    if(p_type.equals("DECimal")){
       return "3";
    }
    if(p_type.equals("DATE")){
       return "5";
    }
    if(p_type.equals("SMALLINT")){
      return "6";
    }
    return "7";
  }

  /**
   * getComments获取数据库中表字段的注释
   */
  private static HashMap getComments(Connection p_conn,String p_tblName,String p_fldName){
    HashMap commMap=new HashMap();
    try{
      java.sql.DatabaseMetaData dbmd = p_conn.getMetaData();
      ResultSet rs = dbmd.getColumns(null, tbCreator, p_tblName.toUpperCase(), p_fldName.toUpperCase());
      if (rs.next()) {
        String temp1=rs.getString("REMARKS");
        if(temp1==null) return commMap;
        temp1=DBAssistor.cvtcoding(temp1);
        String temp2[]=temp1.split(" ");
        for(int i=0;i<temp2.length;i++){
          int idx=temp2[i].indexOf("=");
          if(idx<0) return commMap;
          String name=temp2[i].substring(0,idx).toLowerCase();
          String value=temp2[i].substring(idx+1);
          commMap.put(name,value);
        }
        return commMap;
      }
      return commMap;
    }
    catch(Exception ex){
      System.out.println(DBAssistor.cvtcoding(ex.getMessage()));
      return commMap;
    }
  }

  /**
   * listMain
   */
  public static void listMain(){
    System.out.println("--------------------pttblinfomain--------------------");
    for(int i=0;i<tblArr.size();i++){
      System.out.print("\"" + tblArr.get(i) + "\"," + "\"0\",\"1\",,\n");
    }
  }

  /**
   * listDetl
   */
  public static void listDetl(){
    Connection conn=dba.getConn();
    if(conn==null) return;
    try{
      Statement stmt = conn.createStatement();
      System.out.println("--------------------pttblinfodetl--------------------");
      for(int i=0;i<tblArr.size();i++){
        //TableName
        String tblName=tblArr.get(i).toString();
        ResultSet rs = stmt.executeQuery(
            "select * from "+tblName);
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        for (int j = 1; j < cols + 1; j++) {
          String str="\""+tblName+"\",";
          Object tmp;
          //SeqNo
          str+=""+j+",";
          //Name
          String colName=rsmd.getColumnName(j);
          str+="\""+colName+"\",";
          //IsPrimaryKey
          str+="\""+isPk(tblName,colName)+"\",";
          //DataType
          str += "" + getType(rsmd.getColumnTypeName(j)) + ",";
          HashMap commMap=getComments(conn,tblName.toUpperCase(),colName.toUpperCase());
          //SearchKey------
          tmp=commMap.get("sk");
          if(tmp==null){
            str += "\"0\",";
          }
          else{
            str += "\""+tmp.toString()+"\",";
          }
          //Length------
          str += "0,";
          //IsNull
          //if(rsmd.isNullable(j)==0){
            //System.out.println(colName);
          //}
          str += "\"" + rsmd.isNullable(j) + "\",";
          //IsRepeat------
          str += "\"1\",";
          //Caption
          tmp=commMap.get("cap");
          if(tmp==null){
            str += ",";
          }
          else{
            str += "\""+tmp.toString()+"\",";
          }
          //Description
          tmp=commMap.get("desc");
          if(tmp==null){
            str += ",";
          }
          else{
            str += "\""+tmp.toString()+"\",";
          }
          //DefaultValue------
          str += ",";
          //Precision
          str += "" + rsmd.getPrecision(j) + ",";
          //DecimalDigits
          str += "" + rsmd.getScale(j) + ",";
          //RefTbl
          tmp=commMap.get("reftable");
          if(tmp==null || tmp.toString().length()<1){
            tmp=getRefTbl(tblName,colName);
          }
          if(tmp==null){
            str += ",";
          }
          else{
            str += "\"" + tmp.toString() + "\",";
          }
          //RefName
          tmp=commMap.get("refname");
          if(tmp==null){
            str += ",";
          }
          else{
            str += "\""+tmp.toString()+"\",";
          }
          //RefValue
          tmp=commMap.get("refvalue");
          if(tmp==null || tmp.toString().length()<1){
            tmp=getRefValue(tblName,colName);
          }
          if(tmp==null){
            str += ",";
          }
          else{
            str += "\"" + tmp.toString() + "\",";
          }
          //RefWhere
          str += ",";
          //EnuID
          tmp=commMap.get("cenum");
          if(tmp==null){
            str += ",";
          }
          else{
            str += "\""+tmp.toString()+"\",";
          }
          //NeedEncode
          str += "\"1\"";
          System.out.print(str+"\n");
        }
        //while (rs.next()) {
          //System.out.println("\""+arr.get(i).toString()+"\",\""+rs.getString(1)+"\"");
        //}
        rs.close();
      }
      dba.freeConn(conn);
    }
    catch(Exception ex){
      log.info(ex.getMessage());
      ex.printStackTrace();
    }
  }

  /**
   * main
   */
  public static void main(String[] args) {
    PtTblInfo ti = new PtTblInfo();
    ti.listMain();
    ti.listDetl();
  }

}
