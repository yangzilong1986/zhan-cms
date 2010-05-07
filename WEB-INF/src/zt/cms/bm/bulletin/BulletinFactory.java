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
public class BulletinFactory {
    /**
     *  Constructor for the BulletinFactory object
     */
    private BulletinFactory() { }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public static Collection findTop5Bulletins() {
        return findBulletinsByPageAndNumber(1, 5, 1);
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public static Collection findTop5Rules() {
        return findBulletinsByPageAndNumber(1, 5, 2);
    }


    /**
     *  Description of the Method
     *
     *@param  page    Description of the Parameter
     *@param  number  Description of the Parameter
     *@param  type    Description of the Parameter
     *@return         Description of the Return Value
     */
    public static Collection findBulletinsByPageAndNumber(int page, int number, int type) {
      try
      {
        DatabaseConnection con = MyDB.getInstance().apGetConn();

        String typeString = "";
        if (type == 1 || type == 2) {
          typeString = "and bulletintype=" + type;
        }
        int beginIndex = (page - 1) * number + 1;
        String str = "select * from glbulletin,scuser where glbulletin.publishedby=scuser.loginname and 1=1 " +
            typeString + " order by createdate desc";
        //System.out.println("SQL:" + str);
        RecordSet rs = con.executeQuery(str, beginIndex, number);
        Collection bulletins = new Vector();
        while (rs.next()) {
          Bulletin bulletin = new Bulletin();
          bulletin.setBulletinType(rs.getInt("bulletintype"));
          bulletin.setCaption(DBUtil.fromDB(rs.getString("caption")));
          bulletin.setContent(DBUtil.fromDB(rs.getString("content")));
          bulletin.setCreateDate(rs.getCalendar("createdate"));
          bulletin.setPublishedBy(DBUtil.fromDB(rs.getString("publishedby")));
          bulletin.setSeqno(rs.getInt("seqno"));
          bulletin.setFullName(DBUtil.fromDB(rs.getString("username")));
          if (rs.getInt("iflink") == 1) {
            bulletin.setLink(true);
          }
          else {
            bulletin.setLink(false);
          }
          bulletins.add(bulletin);
        }
        return bulletins;
      }
      catch(Exception e)
      {
        Debug.debug(e);
        return null;
      }
      finally
      {
        MyDB.getInstance().apReleaseConn(1);
      }
    }


    /**
     *  Description of the Method
     *
     *@param  seqno  Description of the Parameter
     *@return        Description of the Return Value
     */
    public static Bulletin findBulletinBySeqno(int seqno) {
     try {
        DatabaseConnection con = MyDB.getInstance().apGetConn();
        String str = "select * from glbulletin,scuser where glbulletin.publishedby=scuser.loginname and seqno=" +
            seqno;
        //System.out.println("SQL:" + str);
        RecordSet rs = con.executeQuery(str);
        Bulletin bulletin = null;
        if (rs.next()) {
          bulletin = new Bulletin();
          bulletin.setBulletinType(rs.getInt("bulletintype"));
          bulletin.setCaption(DBUtil.fromDB(rs.getString("caption")));
          bulletin.setContent(DBUtil.fromDB(rs.getString("content")));
          bulletin.setCreateDate(rs.getCalendar("createdate"));
          bulletin.setPublishedBy(DBUtil.fromDB(rs.getString("publishedby")));
          bulletin.setSeqno(rs.getInt("seqno"));
          bulletin.setFullName(DBUtil.fromDB(rs.getString("username")));
          if (rs.getInt("iflink") == 1) {
            bulletin.setLink(true);
          }
          else {
            bulletin.setLink(false);
          }
        }
        return bulletin;
      }
      catch (Exception e) {
        Debug.debug(e);
        return null;
      }
      finally {
        MyDB.getInstance().apReleaseConn(1);
      }
    }


}
