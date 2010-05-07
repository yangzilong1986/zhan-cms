package com.mjp.tree;

import java.util.List;

import com.mjp.tree.xml.DemoNode;
import com.mjp.tree.xml.DemoNodeRegister;

public class TreeUtils {
	
	protected static DemoNodeRegister register = DemoNodeRegister.getInstance();

	/**根据id返回对应的子节点字符串
	 * @param parentId
	 * @return
	 */
	public static String getSubTreeNodesJson(String parentId){
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		List<DemoNode> lst = register.getChildNodes(parentId);
		for(int i=0,count=lst.size();i<count;i++){
			if(i != 0){
				buf.append(",");
			}
			DemoNode node = lst.get(i);
			buf.append("{id:'" + node.getId() + "',text:'" + node.getText() + "'");
			buf.append(",leaf:"+ node.isLeaf() +",parentId:'" + node.getParentId() + "',checked:false}");
		}
		buf.append("]");
		System.out.println(buf.toString());
		return buf.toString();
	}
}
