package com.mjp.tree.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DemoNodeRegister {

	/**
	 * 存放所有的node
	 */
	private  List<DemoNode> lst = null;
	
	private static final String TREE_XML_FILENAME = "trees.xml";
	
	private DemoNodeRegister(){}
	
	private static final DemoNodeRegister instance = new DemoNodeRegister();
	
	public static final DemoNodeRegister getInstance(){
		return instance;
	}
	
	/**获取文档
	 * @return
	 */
	private Element getRootElement(){
		try{
		InputStream ins = this.getClass().getResourceAsStream(TREE_XML_FILENAME);
		SAXReader reader = new SAXReader();
		return reader.read(ins).getRootElement();
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("读取"+TREE_XML_FILENAME +"文件出错!");
		}
	}
	
	/**返回配置文件的所有节点
	 * @return
	 */
	public List<DemoNode> getNodes(){
		lst = new ArrayList<DemoNode>();
		initAllElements();
		return this.lst;
	}
	/**返回配置文件的所有节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DemoNode> getChildNodes(String parentId){
		List<DemoNode> result = new ArrayList<DemoNode>();
		Element root = getRootElement();
		String xpath = "node[@parentId='" + parentId + "']";
		List<Element> eLst = root.selectNodes(xpath);
		if(eLst != null){
			for(Element e : eLst){
				result.add(element2Node(e));
			}
			Collections.sort(result);
		}
		
		return result;
	}
	/**
	 * 读取所有的node
	 */
	@SuppressWarnings("unchecked")
	private void initAllElements(){
		Element root = getRootElement();
		List<Element> eLst = root.elements("node");
		if(eLst != null){
			for(Element e : eLst){
				this.lst.add(element2Node(e));
			}
			Collections.sort(this.lst);
		}
		
	}
	
	/**将element 转换成 node
	 * @param e
	 * @return
	 */
	private DemoNode element2Node(Element e){
		DemoNode node = new DemoNode();
		node.setId(nvl(e.attributeValue("id")));
		node.setText(nvl(e.attributeValue("text")));
		node.setParentId(nvl(e.attributeValue("parentId")));
		
		String isLeaf = nvl(e.attributeValue("isLeaf"));
		if(isLeaf.equals("true")){
			node.setLeaf(true);
		}else{
			node.setLeaf(false);
		}
		try{
			node.setIndex(Integer.parseInt(e.attributeValue("index")));
		}catch(Exception ex){
			ex.printStackTrace();
			node.setIndex(0);
		}
		return node;
	}
	
	private String nvl(Object obj){
		if(obj == null){
			return "";
		}else{
			return obj.toString().trim();
		}
	}
}
