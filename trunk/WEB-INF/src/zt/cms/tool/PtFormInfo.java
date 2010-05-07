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

public class PtFormInfo {
  //配置变量
  private static Logger log = Logger.getLogger("zt.cms.tool.PtTblInfo");
  private static String tbCreator="GZLCRMS";
  //要生成哪个table的detl，“all”代表全部
  private static String tables="BMAppEduAiding";
  private static String formname="BMAP04";//空表示formname取tablename
  //成员变量
  private static DBAssistor dba;
  private static ArrayList tblArr=new ArrayList();
  private static Map pkMap=new HashMap();
  private static Map fkMap=new HashMap();

  /**
   * 构造函数。
   */
  public PtFormInfo() {
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
      ResultSet rs=stmt.executeQuery("select * from sysibm.systables where creator='"+tbCreator+"' and name not like 'PT%' and name not like'PF%'");
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
   * getRefPk
   */
  private static String getRefTbl(String p_tblName,String p_fkName){
    Object tmp0=fkMap.get(p_tblName+"."+p_fkName);
    if(tmp0==null) return null;
    String tmp1=tmp0.toString();
    if(tmp1.length()<1) return null;
    String tmp2[]=tmp1.split("\\.");
    return tmp2[0];
  }

  /**
   * getType
   */
  private static String getComponentTp(String p_type){
    if(p_type.equals("1")){
       return "4";
    }
    if(p_type.equals("2")){
       return "4";
    }
    if(p_type.equals("3")){
       return "4";
    }
    if(p_type.equals("5")){
       return "18";
    }
    if(p_type.equals("6")){
       return "1";
    }
    if(p_type.equals("7")){
       return "2";
    }
    return "";
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
    return "";
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
        temp1=new String(temp1.getBytes("iso-8859-1"),"GBK");
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
      System.out.println(ex.getMessage());
      return commMap;
    }
  }

  /**
   * listMain
   */
  public static void listMain(){
    System.out.println("--------------------ptforminfomain--------------------");
    for(int i=0;i<tblArr.size();i++){
      //formid
      String formid=formname;
      if(formname.length()<1) formid=tblArr.get(i).toString();
      String str="'" + formid + "',";
      //urlocate
      str+="null,";
      //formproc
      str+="'" + formid + "',";
      //formstyle
      str+="null,";
      //formtbl
      str+="'"+tblArr.get(i).toString()+"',";
      //title
      str+="'',";
      //formtype
      str+="'0',";
      //readonly
      str+="'0',";
      //rows
      str+="60,";
      //cols
      str+="4,";
      //enabled
      str+="'1',";
      //listsql
      str+="null,";
      //description
      str+="null,";
      //useadd
      str+="'1',";
      //usedelete
      str+="'1',";
      //usesave
      str+="'1',";
      //useedit
      str+="'1',";
      //usereset
      str+="'1',";
      //usesearch
      str+="'1',";
      //countsql
      str+="null,";
      //width
      str+="null,";
      //scriptfile
      str+="null";
      System.out.print("delete from ptforminfomain where formid='"+formid+"';\n");
      str="insert into ptforminfomain values("+str+");";
      System.out.print(str+"\n");
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
      System.out.println("--------------------ptforminfodetl--------------------");
      for(int i=0;i<tblArr.size();i++){
        //TableName
        String tblName=tblArr.get(i).toString();
        ResultSet rs = stmt.executeQuery(
            "select * from "+tblName);
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        String formid=formname;
        if(formname.length()<1) formid=tblName;
        System.out.print("delete from ptforminfodetl where formid='"+formid+"';\n");
        for (int j = 1; j < cols + 1; j++) {
          Object tmp;
          //formid
          String str="'"+formid+"',";
          //seqno
          str+=""+j*10+",";
          //type
          str+="'0',";
          //Name
          String colName=rsmd.getColumnName(j).trim();
          str+="'"+colName+"',";
          //defaultvalue
          str+="null,";
          //caption
          str+="null,";
          //description
          str+="null,";
          //readonly
          //if(isPk(tblName,colName).equals("1")){
          str += "'" + isPk(tblName, colName) + "',";
          //}
          //minlength
          str+="null,";
          //maxlength
          str+="null,";
          //size
          str+="null,";
          //visible
          if(isPk(tblName,colName).equals("1")){
            str += "'0',";
          }
          else{
            str += "'1',";
          }
          //isnull
          //str+="null,";
          str += "'" + rsmd.isNullable(j) + "',";
          //DataType
          String datatp=getType(rsmd.getColumnTypeName(j));
          if(datatp.trim().length()<1) datatp="null";
          //str += "" + datatp + ",";
          HashMap commMap=getComments(conn,tblName.toUpperCase(),colName.toUpperCase());
          //componenttp
          String componenttp="";
          tmp=commMap.get("cenum");
          if(tmp!=null){
            componenttp = "1";
          }
          tmp=commMap.get("reftable");
          if(tmp!=null){
            componenttp = "2";
          }
          if(componenttp.trim().length()<1) componenttp=getComponentTp(datatp);
          if(componenttp.trim().length()<1) componenttp="4";
          str += "" + componenttp + ",";
          str += "" + datatp + ",";
          //xposition
          str+=j+10+",";
          //yposition
          str+="1,";
          //rows
          str+="null,";
          //cows
          str+="null,";
          //multiple
          str+="null,";
          //disabled
          str+="null,";
          //valuesettype
          str+="null,";
          //valueset
          str+="null,";
          //headstr
          str+="null,";
          //middlestr
          str+="null,";
          //afterstr
          str+="null,";
          //formatcls
          str+="null,";
          //onclick
          str+="null,";
          //onchange
          str+="null,";
          //onsubmit
          str+="null,";
          //others
          str+="null,";
          //width
          str+="null,";
          //expression
          str+="null,";
          //displaytype
          str+="null,";
          //changeevent
          str+="null";
          //SearchKey------
          str="insert into ptforminfodetl values("+str+");";
          System.out.print(str+"\n");
        }
        //while (rs.next()) {
          //System.out.println("'"+arr.get(i).toString()+"','"+rs.getString(1)+"'");
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
    PtFormInfo ti = new PtFormInfo();
    ti.listMain();
    ti.listDetl();
  }

}