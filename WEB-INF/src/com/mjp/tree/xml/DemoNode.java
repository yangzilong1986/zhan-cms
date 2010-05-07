package com.mjp.tree.xml;

public class DemoNode implements Comparable<DemoNode>{
	private String id;
	private String text ;
	private boolean isLeaf ;
	private String parentId;
	private int index ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int compareTo(DemoNode o) {
		if(o == null){
			return 0;
		}
		int oIndex = o.getIndex();
		return this.getIndex() - oIndex;
	}
	
	
}
