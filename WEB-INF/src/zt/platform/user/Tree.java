package zt.platform.user;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author Houym
 * @version 1.0
 */

public class Tree {

    private Document doc;
    private int menuNum = 0;

    public Tree(String str) throws JDOMException {
        //System.out.println(str);
        Reader reader = new StringReader(str);
        SAXBuilder builder = new SAXBuilder();
        doc = builder.build(reader);
    }

    public void output(Writer out) {
        org.w3c.dom.Document document = null;
        try {
            DOMOutputter output = new DOMOutputter();
            document = output.output(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (document != null) {
            org.w3c.dom.Element tree = document.getDocumentElement();

            try {
                out.write("var menu=new MTMenu();\n");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            String parentMenu = "menu";
            parseTree(out, tree, parentMenu);
            //最后加上重新签到菜单
            //edit by wxj at 040211
            try {
                out.write(parentMenu +
                        ".MTMAddItem(new MTMenuItem(\"重新签到\",\"logout.jsp\",\"_parent\",\"\"));\n");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private void parseTree(Writer out, org.w3c.dom.Element tree,
                           String parentMenu) {
        org.w3c.dom.NodeList trees = tree.getChildNodes();
        for (int j = 0; j < trees.getLength(); j++) {
            boolean isTree = false;
            try {
                trees.item(j).getAttributes().getNamedItem("action").getNodeValue();
            }
            catch (Exception e) {
                isTree = true;
            }

            if (isTree) {
                try {
                    out.write(parentMenu + ".MTMAddItem(new MTMenuItem(\"" +
                            trees.item(j).getAttributes().getNamedItem("text").
                                    getNodeValue() + "\",\"" + "javascript:" +
                            "\",\"mainFrame\",\"\"));\n");
                    out.write("var menu" + (++this.menuNum) + "=new MTMenu();\n");
                    out.write(parentMenu + ".items[" + j + "].MTMakeSubmenu(menu" +
                            (menuNum) + ");\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                parseTree(out, (org.w3c.dom.Element) trees.item(j), "menu" + menuNum);
            } else {
                try {
                    out.write(parentMenu + ".MTMAddItem(new MTMenuItem(\"" +
                            trees.item(j).getAttributes().getNamedItem("text").
                                    getNodeValue() + "\",\"" +
                            trees.item(j).getAttributes().getNamedItem("action").
                                    getNodeValue() + "\",\"mainFrame\",\"\"));\n");
                }
                catch (org.w3c.dom.DOMException ex) {
                    ex.printStackTrace();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * lj add for test
     *
     * @param out
     * @return org.w3c.dom.Element tree
     */
    public org.w3c.dom.Element getDomTree(Writer out) {
        org.w3c.dom.Document document = null;
        org.w3c.dom.Element tree = null;
        try {
            DOMOutputter output = new DOMOutputter();
            document = output.output(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (document != null) {
            tree = document.getDocumentElement();
        }
        return tree;
    }
}
