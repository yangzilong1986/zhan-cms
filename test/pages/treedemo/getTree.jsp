<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.mjp.tree.TreeUtils;"%>
<%
	String nodeId = request.getParameter("node");
	nodeId = nodeId == null ? "":nodeId.trim();
	
   	System.out.println("nodeId:" + nodeId);
   	String jsonStr = TreeUtils.getSubTreeNodesJson(nodeId);
   	response.setContentType("text/json;charset=utf-8");
   	response.getWriter().write(jsonStr);
	
%>
