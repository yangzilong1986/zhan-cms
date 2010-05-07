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

public class PtEnumInfo {
  //配置变量
  private static Logger log = Logger.getLogger("zt.cms.tool.PtTblInfo");

  //成员变量
  private static DBAssistor dba;
  private static ArrayList tblArr=new ArrayList();

  public PtEnumInfo() {
    try{
      //dba
      dba=new DBAssistor();
    }
    catch(Exception ex){
      log.info(ex.getMessage());
      System.exit(0);
    }
  }
  private void printmain(){
    Connection conn=dba.getConn();
    if(conn==null) return;
    try{
      Statement stmt = conn.createStatement();
      ResultSet rs=stmt.executeQuery("Select * from ptenuminfomain order by enuid");
      System.out.println("--------------------main--------------------");
      while(rs.next()){
        String temp="final public static String ";
        temp+=rs.getString("enuid").trim();
        temp+=" = \"";
        temp+=rs.getString("enuid").trim();
        temp+="\";\n";
        System.out.print(DBAssistor.Encoding(temp,"ISO-8859-1","GBK"));
      }
    }
    catch(Exception ex){
      log.info(ex.getMessage());
    }
    finally{
      dba.freeConn(conn);
    }
  }
  private void printdetl(){
    Connection conn=dba.getConn();
    if(conn==null) return;
    try{
      Statement stmt = conn.createStatement();
      ResultSet rs=stmt.executeQuery("Select * from ptenuminfodetl order by enuid");
      System.out.println("--------------------detl--------------------");
      while(rs.next()){
        String temp="final public static int ";
        temp+=rs.getString("enuid").trim();
        temp+="_";
        temp+=rs.getString("enudt").trim();
        temp+=" = ";
        temp+=rs.getString("enutp");
        temp+=";\n";
        System.out.print(DBAssistor.Encoding(temp,"ISO-8859-1","GBK"));
      }
    }
    catch(Exception ex){
      log.info(ex.getMessage());
    }
    finally{
      dba.freeConn(conn);
    }
  }
  public static void main(String[] args) {
    PtEnumInfo ptEnumInfo = new PtEnumInfo();
    ptEnumInfo.printmain();
    ptEnumInfo.printdetl();
  }

}