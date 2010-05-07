Ext.namespace("Wsd.fn.common");

Wsd.fn.common.TreeDemo = function() {
	// 树根
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : '0',
		text : '总目录'
	});
	// 树加载器
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'getTree.jsp'
	});

	// 当选择节点时影响所有子节点
	var nodeSubCheckChange = function(node, bool) {
		if (node) {
			var childNodes = node.childNodes;
			// 选中或取消所有子节点
			if (childNodes && childNodes.length > 0) {
				var folders = new Array();
				for (var i = 0, len = childNodes.length; i < len; i++) {
					if(childNodes[i].getUI().checkbox){
						childNodes[i].getUI().checkbox.checked = bool;
					}
					// 递归调用将影响所有的子节点
					if (childNodes[i].leaf == false) {
						var tmpNode = childNodes[i]; 
						folders[folders.length] = tmpNode;
					}
				}
				for(var i=0,len=folders.length;i<len;i++){
					var tmpNode = folders[i];
					tmpNode.getOwnerTree().fireEvent('checkchange', tmpNode, bool);
				}
			}
		}
	};
	// 当选择节点时影响所有的父节点
	var nodeParCheckChange = function(node, bool) {
		if (node) {
			// 对所有的父节点进行选中与取消
			var parentNode = node.parentNode;
			while (parentNode) {
				if(parentNode.getUI().checkbox){
					parentNode.getUI().checkbox.checked = bool;
				}
				parentNode = parentNode.parentNode;
			}
		}
	};
	
	//获取所有的node
	var getNodes = function(treePanel){
		var startNode = treePanel.getRootNode();
        var r = [];
        var f = function(){
                r.push(this);
        };
        startNode.cascade(f);
        return r;
	};
	
	//真对单选进行处理
	var nodeAllRadioChange = function(node,bool){
		if(!bool){
			return;
		}
		var treePanel = Ext.getCmp('tree-demo-panel');
		var nodes = getNodes(treePanel);
		if(nodes && nodes.length>0){
			for(var i=0,len=nodes.length;i<len;i++){
				if(nodes[i].id!=node.id){
					if(nodes[i].getUI().checkbox){
						nodes[i].getUI().checkbox.checked = false;
					}
				}
			}
		}
	};
	// 树panel
	var treePanel = new Ext.tree.TreePanel({
		id : 'tree-demo-panel',
		autoScroll : true,
		animate : true,
		border : false,
		root : rootNode,
		loader : treeLoader,
		listeners : {
			'beforeload' : function(node) {
				node.attributes.depth = node.getDepth();
			},
			'click' : function(node, event) {
				node.select();
				alert(node.attributes.id + "\t" + node.getDepth() + "\t"
						+ node.attributes.parentId);
			},
			'checkchange' : function(node, bool) {
				//多选
				//nodeSubCheckChange(node, bool);
				//选择子节点则对应的所有父节点必须选择
				//nodeParCheckChange(node, bool);
				//单选
				nodeAllRadioChange(node,bool);
			}
		}
	});
	
	return {
		// 创建window
		createWindow : function() {
			var win = new Ext.Window({
				id : 'treeWin',
				title : 'tree列表',
				pageX : 100,
				pageY : 100,
				width : 250,
				autoHeight : true,
				closeAction : 'close',
				plain : true,
				buttonAlign : "center",
				resizable : false,
				modal : true,
				items : [treePanel],
				buttons : [{
					text : '保存',
					handler : function() {
						var nodeArr = Ext.getCmp('tree-demo-panel')
								.getChecked();
						if (nodeArr && nodeArr.length > 0) {
							for (var i = 0, count = nodeArr.length; i < count; i++) {
								alert(nodeArr[i].id + "\t" + nodeArr[i].text);
							}
						} else {
							Ext.MessageBox.alert('请先选择!');
						}

					}
				}, {
					text : '取消',
					handler : function() {
						win.close();
					}
				}]
			});
			win.show();
		}
	}
}();