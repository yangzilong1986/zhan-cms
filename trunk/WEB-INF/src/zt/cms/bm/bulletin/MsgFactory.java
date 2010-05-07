package zt.cms.bm.bulletin;

import java.util.*;
import zt.cmsi.mydb.*;
import zt.platform.db.*;
import zt.platform.db.DBUtil;
import zt.platform.utils.Debug;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2004Äê2ÔÂ16ÈÕ
 */
public class MsgFactory {
    /**
     *  Constructor for the MsgFactory object
     */
    public MsgFactory() { }


    /**
     *  Description of the Method
     *
     *@param  toUser  Description of the Parameter
     *@param  page    Description of the Parameter
     *@param  number  Description of the Parameter
     *@return         Description of the Return Value
     */
    public static Collection findMyUnviewedMsgs(String toUser, int page, int number) {
        return findMyMsgs(toUser, page, number, false);
    }


    /**
     *  Description of the Method
     *
     *@param  toUser  Description of the Parameter
     *@param  page    Description of the Parameter
     *@param  number  Description of the Parameter
     *@return         Description of the Return Value
     */
    public static Collection findMyViewedMsgs(String toUser, int page, int number) {
        return findMyMsgs(toUser, page, number, true);
    }


    /**
     *  Description of the Method
     *
     *@param  toUser  Description of the Parameter
     *@return         Description of the Return Value
     */
    public static Collection findTop5UnviewedMsgs(String toUser) {
        return findMyUnviewedMsgs(toUser, 1, 5);
    }


    /**
     *  Description of the Method
     *
     *@param  toUser  Description of the Parameter
     *@return         Description of the Return Value
     */
    public static Collection findTop5Msgs(String toUser) {
        return findAllMyMsgs(toUser, 1, 5);
    }



    /**
     *  Gets the myMsgsNumber attribute of the MsgFactory class
     *
     *@param  toUser  Description of the Parameter
     *@return         The myMsgsNumber value
     */
    public static int getMyMsgsNumber(String toUser) {
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();
//        RecordSet rs = con.executeQuery(
//            "select count(*) from glmsg where touser='" + toUser + "'");
         RecordSet rs = con.executeQuery(
              "select count(*) from glmsg,scuser where glmsg.fromuser=scuser.loginname and touser='" + toUser + "'");
        int ret = 0;
        if (rs.next()) {
          ret = rs.getInt(0);
        }
        return ret;
      }

      catch (Exception e) {
        Debug.debug(e);
        return 0;
      }
      finally {
        MyDB.getInstance().apReleaseConn(1);
      }

    }


    /**
     *  Gets the myUnviewedMsgsNumber attribute of the MsgFactory class
     *
     *@param  toUser  Description of the Parameter
     *@return         The myUnviewedMsgsNumber value
     */
    public static int getMyUnviewedMsgsNumber(String toUser) {
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();
        RecordSet rs = con.executeQuery(
            "select count(*) from glmsg,scuser where glmsg.fromuser=scuser.loginname and readflag=0 and touser='" + toUser +
            "'");
        int ret = 0;
        if (rs.next()) {
          ret = rs.getInt(0);
        }
        return ret;
      }
      catch (Exception e) {
        Debug.debug(e);
        return 0;
      }
      finally {
        MyDB.getInstance().apReleaseConn(1);
      }

    }



    /**
     *  Description of the Method
     *
     *@param  toUser    Description of the Parameter
     *@param  page      Description of the Parameter
     *@param  number    Description of the Parameter
     *@param  readFlag  Description of the Parameter
     *@return           Description of the Return Value
     */
    public static Collection findMyMsgs(String toUser, int page, int number, boolean readFlag) {
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();
        int beginIndex = (page - 1) * number + 1;

        String readString = "";
        if (readFlag) {
          readString = " and readflag=1";
        }
        else {
          readString = " and readflag=0";
        }

        String toUserString = "";

        if (toUser != null) {
          toUserString = " and touser='" + toUser + "'";
        }

        String str = "select * from glmsg where 1=1 " + readString + toUserString +
            " order by createdate desc";
        //System.out.println("SQL:" + str);
        RecordSet rs = con.executeQuery(str, beginIndex, number);
        Collection msgs = new Vector();
        while (rs.next()) {
          Msg msg = new Msg();
          msg.setContent(DBUtil.fromDB(rs.getString("content")));
          msg.setCreateDate(rs.getCalendar("createdate"));
          msg.setFromUser(DBUtil.fromDB(rs.getString("fromUser")));
          msg.setMsgNo(rs.getInt("msgno"));

          if (rs.getInt("readflag") == 1) {
            msg.setReadFlag(true);
          }
          else {
            msg.setReadFlag(false);
          }
          msgs.add(msg);
        }
        return msgs;
      }
      catch (Exception e) {
        Debug.debug(e);
        return null;
      }
      finally {
        MyDB.getInstance().apReleaseConn(1);
      }

    }


    /**
     *  Description of the Method
     *
     *@param  toUser  Description of the Parameter
     *@param  page    Description of the Parameter
     *@param  number  Description of the Parameter
     *@return         Description of the Return Value
     */
    public static Collection findAllMyMsgs(String toUser, int page, int number) {
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();
        int beginIndex = (page - 1) * number + 1;

        String toUserString = "";

        if (toUser != null) {
          toUserString = " and touser='" + toUser + "'";
        }

        String str =
            "select * from glmsg,scuser where glmsg.fromuser=scuser.loginname and 1=1 " +
            toUserString + " order by createdate desc";
        //System.out.println("SQL:" + str);
        RecordSet rs = con.executeQuery(str, beginIndex, number);
        Collection msgs = new Vector();
        while (rs.next()) {
          Msg msg = new Msg();
          msg.setContent(DBUtil.fromDB(rs.getString("content")));
          msg.setCreateDate(rs.getCalendar("createdate"));
          msg.setFromUser(DBUtil.fromDB(rs.getString("fromUser")));
          msg.setMsgNo(rs.getInt("msgno"));
          msg.setFullName(DBUtil.fromDB(rs.getString("username")));

          if (rs.getInt("readflag") == 1) {
            msg.setReadFlag(true);
          }
          else {
            msg.setReadFlag(false);
          }
          msgs.add(msg);
        }

        return msgs;
      }
      catch (Exception e) {
        Debug.debug(e);
        return null;
      }
      finally {
        MyDB.getInstance().apReleaseConn(1);
      }

    }



    /**
     *  Description of the Method
     *
     *@param  no  Description of the Parameter
     *@return     Description of the Return Value
     */
    public static Msg findMsgByNo(int no) {
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();

        String str =
            "select * from glmsg,scuser where  glmsg.fromuser=scuser.loginname and msgno=" +
            no;
        //System.out.println("SQL:" + str);
        RecordSet rs = con.executeQuery(str);
        Msg msg = null;
        while (rs.next()) {
          msg = new Msg();
          msg.setContent(DBUtil.fromDB(rs.getString("content")));
          msg.setCreateDate(rs.getCalendar("createdate"));
          msg.setFromUser(DBUtil.fromDB(rs.getString("fromUser")));
          msg.setMsgNo(rs.getInt("msgno"));
          msg.setFullName(DBUtil.fromDB(rs.getString("username")));

          if (rs.getInt("readflag") == 1) {
            msg.setReadFlag(true);
          }
          else {
            msg.setReadFlag(false);
          }
        }
        return msg;
      }
      catch (Exception e) {
        Debug.debug(e);
        return null;
      }
      finally {
        MyDB.getInstance().apReleaseConn(1);
      }

    }


    /**
     *  Sets the viewed attribute of the MsgFactory class
     *
     *@param  no  The new viewed value
     *@return     Description of the Return Value
     */
    public static int setViewed(int no) {
      int errorcode = 0;
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();

        String str = "update glmsg set readflag=1 where msgno=" + no;
        //System.out.println("SQL:" + str);
        int ret = con.executeUpdate(str);
        return ret;
      }
      catch (Exception e) {
        errorcode = -1;
        Debug.debug(e);
        return errorcode;
      }
      finally {
        MyDB.getInstance().apReleaseConn(errorcode);
      }

    }


    /**
     *  Description of the Method
     *
     *@param  no  Description of the Parameter
     *@return     Description of the Return Value
     */
    public static int deleteMsgByNo(int no) {
      int errorcode = 0;
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();
        String str = "delete from glmsg  where msgno=" + no;
        //System.out.println("SQL:" + str);
        int ret = con.executeUpdate(str);
        return ret;
      }
      catch (Exception e) {
        errorcode = -1;
        Debug.debug(e);
        return errorcode;
      }
      finally {
        MyDB.getInstance().apReleaseConn(errorcode);
      }

    }

}
