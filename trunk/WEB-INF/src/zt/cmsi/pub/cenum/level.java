package zt.cmsi.pub.cenum;

import zt.platform.cachedb.ConnectionManager;
import zt.platform.db.DBUtil;
import zt.platform.utils.Debug;

import javax.sql.rowset.CachedRowSet;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p/>
 * <p>Description: </p>
 * <p/>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p/>
 * <p>Company: DreamChaser</p>
 *
 * @author GZL(JGO)
 * @version 1.0
 */

public class level {

    private static HashMap<String, Vector<LevelItem>> data = new HashMap<String, Vector<LevelItem>>(40, 1);

    static {
        System.out.println("Enumeration Configuration initing ...");
        //�������ݿ���Դ
        ConnectionManager manager = ConnectionManager.getInstance();

        //sql
        String sql1 = "select * from ptenuminfodetl order by enuid,enutp";
        String sql2 = "select * from sccurrency order by curno";
        try {
            CachedRowSet rs1 = manager.getRs(sql1);
            CachedRowSet rs2 = manager.getRs(sql2);

            String lastenum = null;
            Vector<LevelItem> items = new Vector<LevelItem>();

            while (rs1.next()) {
                if (lastenum != null && rs1.getString("enuid").trim().compareToIgnoreCase(lastenum) != 0) {
                    data.put(lastenum, items);
                    items = new Vector<LevelItem>();
                }
                lastenum = rs1.getString("enuid").trim();
                items.add(new LevelItem(rs1.getString("enutp").trim(), DBUtil.fromDB(rs1.getString("enudt"))));

            }
            if (lastenum != null) {
                data.put(lastenum, items);
            }
            items = new Vector<LevelItem>();
            boolean flag = false;
            while (rs2.next()) {
                items.add(new LevelItem(rs2.getString("curno").trim(), DBUtil.fromDB(rs2.getString("curname"))));
                if (rs2.isLast()) flag = true;
            }
            if (flag) {
                lastenum = "CURRENCY";
                data.put(lastenum, items);
            }
            rs1.close();
            rs2.close();
        } catch (Exception ex) {
            Debug.debug(ex);
        }

        Debug.debug(Debug.TYPE_MESSAGE, "Enumeration Configuration inited ok !");
    }


    public static String levelHere(String selectname, String enumid) throws Exception {
        return levelHereExt(selectname, enumid, null);
    }

    public static String levelHere(String selectname, String enumid, String selecteditem) throws Exception {
        return levelHereExt(selectname, enumid, selecteditem, null, "page_form_select");
    }

    public static String levelHere(String selectname, String enumid, String selecteditem, String extraitemlabel) throws Exception {
        return levelHereExt(selectname, enumid, selecteditem, extraitemlabel, "page_form_select");
    }

    public static String levelHereExt(String selectname, String enumid, String selecteditem, String extraitemlabel) throws Exception {
        return level.levelHereExt(selectname, enumid, selecteditem, extraitemlabel, "page_form_select");
    }

    public static String levelHere1(String selectname, String enumid, String selecteditem) throws Exception {
        return levelHereExt1(selectname, enumid, selecteditem, null);
    }

    public static String radioHere(String selectname, String enumid, String selecteditem) throws Exception {
        return radioHereExt(selectname, enumid, selecteditem, "");
    }

    public static String checkHere(String selectname, String enumid, String selecteditem, String splitechar) throws Exception {
        return checkGroupHereExt(selectname, enumid, selecteditem, splitechar, "");
    }

    /**
     * @param selectname   selectname
     * @param enumid       enumid
     * @param selecteditem use default splitechar '-'
     * @return checkGroup
     * @throws Exception Exception
     */
    public static String checkHere(String selectname, String enumid, String selecteditem) throws Exception {
        return checkGroupHereExt(selectname, enumid, selecteditem, EnumValue.SPLIT_STR, "");
    }

    /**
     * levelHereExt
     *
     * @param selectname     String: Control Name of HTML
     * @param enumid         String: EnumID matching with enuid field of DB
     * @param extraitemlabel String: if u want to add a new Option before the existing first Option, here specify
     *                       the Option name to show. If pass a NULL to it, no new Option be added.
     * @return String
     * @throws Exception Exception
     */
    public static String levelHereExt(String selectname, String enumid, String extraitemlabel) throws Exception {

        if (enumid == null || selectname == null) return null;

        String temp = "<select name=\"" + selectname + "\" class=\"page_form_select\">";


        try {
            Vector<LevelItem> items = data.get(enumid);
            if (items == null) return null;

            if (extraitemlabel != null) temp += "<option value=''>" + extraitemlabel + "</option>";

            for (int i = 0; i < items.size(); i++) {
                temp += "<option value='" + (items.elementAt(i)).getItemNo() + "'>" + (items.elementAt(i)).getItemLabel() + "</option>";
            }
            temp += "</select>";

        } catch (Exception ex) {
            Debug.debug(ex);
        }
        return temp;
    }

    /**
     * @param selectname     String
     * @param enumid         String
     * @param selecteditem   String:the focused Option when the List Control is first shown.
     * @param extraitemlabel String:
     * @param selectclass    String:CSS class id.
     * @return String
     * @throws Exception Exception
     */
    private static String levelHereExt(String selectname, String enumid, String selecteditem, String extraitemlabel, String selectclass) throws Exception {

        if (enumid == null || selectname == null) return null;

        if (selecteditem != null) selecteditem = selecteditem.trim();
        if (selecteditem != null && selecteditem.trim().length() <= 0) selecteditem = null;

        String temp = "<select name=\"" + selectname + "\" ";
        if (selectclass != null) temp += " class=\"" + selectclass + "\"";
        temp += ">";


        try {
            Vector<LevelItem> items = data.get(enumid);
            if (items == null) return null;

            if (extraitemlabel != null) temp += "<option value=''>" + extraitemlabel + "</option>";

            for (int i = 0; i < items.size(); i++) {
                String itemno = (items.elementAt(i)).getItemNo();
                if (selecteditem != null && itemno.compareToIgnoreCase(selecteditem) == 0)
                    temp += "<option value='" + itemno + "' selected>" + (items.elementAt(i)).getItemLabel() + "</option>";
                else
                    temp += "<option value='" + itemno + "'>" + (items.elementAt(i)).getItemLabel() + "</option>";
            }
            temp += "</select>";

        } catch (Exception ex) {
            Debug.debug(ex);
        }
        return temp;
    }


    public static String levelHereExt1(String selectname, String enumid, String selecteditem, String extraitemlabel) throws Exception {

        if (enumid == null || selectname == null) return null;

        String[] extlabels = new String[2];
        if (selecteditem != null) selecteditem = selecteditem.trim();
        if (selecteditem != null && selecteditem.trim().length() <= 0) selecteditem = null;
        //lj added in 20090320
        if (extraitemlabel != null && !extraitemlabel.trim().equals("")) extlabels = extraitemlabel.split(":");

        String temp = "<select name=\"" + selectname + "\">";


        try {
            Vector<LevelItem> items = data.get(enumid);
            if (items == null) return null;

            if (extlabels != null) {//lj added in 20090320
                temp += "<option value='" + extlabels[0] + "'>" + extlabels[1] + "</option>";

            } else if (selecteditem == null) temp += "<option value=''>&nbsp;&nbsp;&nbsp;&nbsp;</option>";

            for (int i = 0; i < items.size(); i++) {
                String itemno = (items.elementAt(i)).getItemNo();
                if (selecteditem != null && itemno.compareToIgnoreCase(selecteditem) == 0)
                    temp += "<option value='" + itemno + "' selected>" + (items.elementAt(i)).getItemLabel() + "</option>";
            }
            temp += "</select>";

        } catch (Exception ex) {
            Debug.debug(ex);
        }
        return temp;
    }


    /**
     * radioHereExt
     *
     * @param selectname   String: Control Name of HTML
     * @param enumid       String: EnumID matching with enuid field of DB
     * @param selecteditem String:the focused radio when the List Control is first shown.
     * @param rangetype    String: if u want to range the radio group ,put <br> or other tags on this param,
     *                     if null,"" will be detail.
     * @return String     String
     * @throws Exception Exception
     */
    public static String radioHereExt(String selectname, String enumid, String selecteditem, String rangetype) throws Exception {

        if (enumid == null || selectname == null) return null;
        if (rangetype == null) rangetype = "";

        String temp = "";
        String ifcheck;

        try {
            Vector<LevelItem> items = data.get(enumid);
            if (items == null) return null;

            for (int i = 0; i < items.size(); i++) {
                temp += "<label>";
                String itemno = (items.elementAt(i)).getItemNo();
                if (selecteditem != null && itemno.compareToIgnoreCase(selecteditem) == 0) ifcheck = "checked";
                else ifcheck = "";
                temp += "<input type=\"radio\" class=\"page_form_radio\" " + ifcheck +
                        " name=\"" + selectname + "\" id='" + itemno + "' value=\"" + itemno + "\">" + (items.elementAt(i)).getItemLabel() + "" +
                        "</label>" + rangetype;
            }

        } catch (Exception ex) {
            Debug.debug(ex);
        }
        return temp;
    }


    /**
     * checkGroupHereExt
     *
     * @param selectname   String: Control Name of HTML
     * @param enumid       String: EnumID matching with enuid field of DB
     * @param selecteditem String:the focused checkboxs,splite by input character.
     * @param splitechar   String:the splite character.
     * @param rangetype    String: if u want to range the checkbox group ,put <br> or other tags on this param,
     *                     if null,"" will be detail.
     * @return String      String
     * @throws Exception Exception
     */
    public static String checkGroupHereExt(String selectname, String enumid, String selecteditem, String splitechar, String rangetype) throws Exception {

        if (enumid == null || selectname == null || splitechar == null) return null;
        if (rangetype == null) rangetype = "";

        String selectitem[] = new String[0];
        if (selecteditem != null) selectitem = selecteditem.split(splitechar);
        String temp = "";
        String ifcheck;

        try {
            Vector<LevelItem> items = data.get(enumid);
            if (items == null) return null;
            int j = 0;
            for (int i = 0; i < items.size(); i++) {
                temp += "<label>";
                String itemno = (items.elementAt(i)).getItemNo();
                if (j < selectitem.length && itemno.compareToIgnoreCase(selectitem[j]) == 0) {
                    ifcheck = "checked";
                    j++;
                } else ifcheck = "";
                temp += "<input type=\"checkbox\" class=\"page_form_checkbox\" " + ifcheck +
                        " name=\"" + selectname + "\" id='" + itemno + "' value=\"" + itemno + "\">" + (items.elementAt(i)).getItemLabel() + "" +
                        "</label>" + rangetype;
            }

        } catch (Exception ex) {
            Debug.debug(ex);
        }
        return temp;
    }

    public static String getEnumItemName(String enumid, String itemno) throws Exception {
        Vector<LevelItem> items = data.get(enumid);
        if (items == null) return null;
        if (itemno == null) return "";
        itemno = itemno.trim();

        for (int i = 0; i < items.size(); i++) {
            String itemno1 = (items.elementAt(i)).getItemNo().trim();
            if (itemno1.compareToIgnoreCase(itemno) == 0)
                return (items.elementAt(i)).getItemLabel();
        }

        return itemno;

    }
}
