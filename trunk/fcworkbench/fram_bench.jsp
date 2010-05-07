<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>中国信合</title>
</head>
<link href="/css/main.css" rel="stylesheet" type="text/css">
<link href="/css/platform.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/js/main.js"></script>
<script language="javascript">
function tabCreate(objTab,id){
	objTab.design            = "1";
	objTab.orientation       = "0";
	objTab.tabarea           = true;
	objTab.designmode        = "IMAGE";
	objTab.tabMode           = 1;       // set to mutiple rows
	objTab.maxTabItemsPerRow = 10;       // we want to have max 3 items per row
	objTab.tabAlignment      = 0;       // center tabs
	objTab.loadOnStartup = true;		 	//If true, all pages are loaded during startup. If false, only the
																			//active page is loaded

	var item = objTab.createItem();
	item.text = "清分工作台";
	item.title = "清分工作台";
	item.active = true;
	item.cached = false;
	item.url = "/fcworkbench/fc_workbench.jsp";
	objTab.add(item);

	var item = objTab.createItem();
	item.text = "五级分类查询";
	item.title = "五级分类查询";
	item.active = false;
	item.cached = false; 
	item.url =  "/fcworkbench/fc_query.jsp";
	objTab.add(item);

	var item = objTab.createItem();
	item.text = "五级分类重分";
	item.title = "五级分类重分";
	item.active = false;
	item.cached = false; 
	item.url =  "/fcworkbench/fc_cf.jsp";
	objTab.add(item);

	objTab.create();
}

// this function is called when an item is clicked
function tabEventTabClick(objItem)
{
	//alert("Item clicked: " + objItem.text);
}
</script>
<body topmargin="5" leftmargin="0">
<iframe id="tab" style="Z-INDEX: 100; LEFT: 0px; WIDTH: 101%; POSITION: absolute; TOP: 5px; HEIGHT: 490"
src="pinTab.html?id=tab" frameBorder="0"></iframe>
</body>
</html>