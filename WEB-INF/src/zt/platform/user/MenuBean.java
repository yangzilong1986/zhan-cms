//Source file: e:\\java\\zt\\platform\\user\\MenuBean.java

package zt.platform.user;

import java.io.IOException;
import java.io.Serializable;

/**
 * ²Ëµ¥
 *
 * @author WangHaiLei
 * @version 1.1
 *          $UpdateDate: Y-M-D-H-M: 2003-11-07-09-32 $
 */
public class MenuBean
        implements Serializable {

    private transient DatabaseAgent database;

//     /**
//      * generateXmlFile(String userid) {}.
//      *
//      * @exception Exception if a access error occurs
//      */
//     public void generateXmlFile(String username, String xmlFilePath)
//          throws Exception {
//
//
//          File fileNew = new File(xmlFilePath);
//          PrintWriter writer = null;
//          try {
//               // Configure our PrintWriter
//               FileOutputStream fos = new FileOutputStream(fileNew);
//               OutputStreamWriter osw = new OutputStreamWriter(fos);
//               writer = new PrintWriter(osw);
//
//               // Print entries for each defined node and associated subNodes
//               MenuItemBean[] menuItemsLevel1 = database.getMenuItems(username, 1);
//               for(int i = 0; i < menuItemsLevel1.length; i++) {
//                    writer.println(menuItemsLevel1[i].convertToString());
//
//                    String menuItemLevel1Id = menuItemsLevel1[i].getMenuItemId();
//                    MenuItemBean[] menuItemsLevel2 = database.getMenuItems(username, 2, menuItemLevel1Id);
//                    for(int j = 0; j < menuItemsLevel2.length; j++) {
//                         writer.println(menuItemsLevel2[j].convertToString());
//                         String menuItemLevel2Id = menuItemsLevel2[j].getMenuItemId();
//
//                         MenuItemBean[] leafNodes = database.getMenuItems(username, 3, menuItemLevel2Id);
//                         for(int k = 0; k < leafNodes.length; k++) {
//                              writer.println(leafNodes[k].convertToString());
//                              writer.println("</node>");
//                         }
//                         writer.println("</node>");
//                    }
//                    writer.println("</node>");
//               }
//
//               // Check for errors that occurred while printing
//               if(writer.checkError()) {
//                    writer.close();
//                    fileNew.delete();
//                    throw new IOException("Wrong, when saving to '" + xmlFilePath + "'");
//               }
//               writer.close();
//               writer = null;
//          } catch(IOException e) {
//               if(writer != null) {
//                    writer.close();
//               }
//               fileNew.delete();
//
//               System.err.println("IOException, when generating XML file : [" + e + "]");
//
//               throw e;
//          }
//     }

    /**
     * @param username
     * @throws java.lang.Exception
     */
    public String generateStream(String username) throws Exception {

        try {
            database = new DatabaseAgent();
            StringBuffer sb = new StringBuffer();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<tree>");
            // Print entries for each defined node and associated subNodes
            MenuItemBean[] menuItemsLevel1 = database.getMenuItems(username, 1);
            for (int i = 0; i < menuItemsLevel1.length; i++) {
                sb.append(menuItemsLevel1[i].convertToString());
                String menuItemLevel1Id = menuItemsLevel1[i].getMenuItemId();
                MenuItemBean[] menuItemsLevel2 = database.getMenuItems(username, 2,
                        menuItemLevel1Id);
                for (int j = 0; j < menuItemsLevel2.length; j++) {
                    sb.append(menuItemsLevel2[j].convertToString());
                    String menuItemLevel2Id = menuItemsLevel2[j].getMenuItemId();

                    MenuItemBean[] leafNodes = database.getMenuItems(username, 3, menuItemLevel2Id);
                    for (int k = 0; k < leafNodes.length; k++) {
                        sb.append(leafNodes[k].convertToString());
                        sb.insert((sb.length() - 1), "/");
                    }
                    sb.append("</tree>");
                }
                sb.append("</tree>");
            }

            // This is the last end of the XML file.
            sb.append("</tree>");

            String xmlString = sb.toString();
            //System.out.println("xmlString = " + xmlString);
            return xmlString;

        }
        catch (IOException e) {
            System.err.println("IOException, when generating XML file : [" + e + "]");
            throw e;
        }
    }
}
