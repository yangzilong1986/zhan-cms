var dba = {};
dba.start_text = "<br/><span class='info_message'>Select any entity in tree to view details</span>";
dba.i18n = {select_db:"You need to select DB first",select_connection:"Select any connection first",delete_connection:"Are you sure to delete connection:"};
dba.uid = function() {
    return(new Date()).valueOf();
};
dba.get_id_chain = function(tree, id) {
    var chain = [id];
    while (id = tree.getParentId(id))chain.push(id);
    return chain.reverse().join("|");
};
dba.pages = {};
dba.add_connection = function(server, user, pass) {
    top.setTimeout(function() {
        (new ag(function(a, b, c, d, xml) {
            eval(xml.ai.responseText);
        }, dba, true, true)).loadXML("./logic/php/connection.php?mode=add&server=" + encodeURIComponent(server) + "&user=" + encodeURIComponent(user) + "&pass=" + encodeURIComponent(pass));
        dba.popup_win.close();
    }, 1);
};
dba.delete_connection = function(server) {
    top.setTimeout(function() {
        (new ag(function(a, b, c, d, xml) {
            eval(xml.ai.responseText);
        }, dba, true, true)).loadXML("./logic/php/connection.php?mode=delete&server=" + encodeURIComponent(server));
    }, 1);
};
dba.create_tab = function(id, full_id, text, extra) {
    full_id = full_id || dba.get_id_chain(dba.tree, id);
    if (!dba.pages[full_id]) {
        var details = id.split("^");
        dba.tabbar.hG(full_id, full_id, 100);
        var win = dba.tabbar.cells(full_id);
        var toolbar = win.attachToolbar();
        toolbar.attachEvent("onClick", dba.tab_toolbar_click);
        toolbar.setIconsPath("./imgs/");
        toolbar.loadXML("xml/toolbar_" + details[0] + ".xml");
        dba.tabbar.fP(full_id);
        dba.tabbar.setLabel(full_id, text || dba.tree.getItemText(id));
        switch (details[0]) {case "table":dba.set_data_table(win, full_id);break;case "query":dba.set_query_layout(win);break;}
        dba.pages[full_id] = win;
        win.extra = extra;
    } else dba.tabbar.fP(full_id);
};
dba.tab_toolbar_click = function(id) {
    switch (id) {case "close":var id = dba.tabbar.wX();delete dba.pages[id];dba.tabbar.Af(id, true);break;case "run_query":var win = dba.tabbar.cells(dba.tabbar.wX());win.area.parentNode.removeChild(win.area);win.grid.post("./logic/php/sql.php", "id=" + encodeURIComponent(win.extra.join("|")) + "&sql=" + encodeURIComponent(win.area.value));break;case "refresh_table":var win = dba.tabbar.cells(dba.tabbar.wX());win.grid.loadXML(win.grid._refresh);break;case "show_structure":var win = dba.tabbar.cells(dba.tabbar.wX());win.grid.load(win.grid._refresh + "&struct=true");break;default:alert("Not implemented");break;}
};
dba.main_toolbar_click = function(id) {
    switch (id) {case "add_connection":var win = dba.layout.dhxWins.createWindow("creation", 1, 1, 300, 180);win.tI("Add connection");win.setModal(true);win.denyResize();win.center();win.attachURL("connection.html?etc=" + new Date().getTime());dba.popup_win = win;break;case "delete_connection":var data = dba.tree.ah();if (!data)return alert(dba.i18n.select_connection);data = dba.get_id_chain(dba.tree, data).split("|")[0];if (confirm(dba.i18n.delete_connection + dba.tree.getItemText(data))) {
        dba.delete_connection(data.split("^")[1])
    }break;case "sql_query":var data = dba.get_id_chain(dba.tree, dba.tree.ah()).split("|");if (data.length < 2)return alert(dba.i18n.select_db);dba.create_tab("query", dba.uid(), "SQL : " + dba.tree.getItemText(data[0]) + " : " + data[1].split("^")[1], data);break;}
};
dba.set_query_layout = function(win) {
    var grid = win.attachGrid();
    grid.enableSmartRendering(true);
    grid.setHeader("<textarea style='width:100%; height:80px; '>Type SQL query here</textarea>");
    grid.setInitWidths("*");
    grid.setSkin(skin);
    grid.init();
    grid.attachEvent("onXLE", function() {
        this.hdr.rows[1].cells[0].firstChild.appendChild(win.area);
        this.hdr.rows[1].cells[0].className = "grid_hdr_editable";
        this.setSizes();
        win.area.focus();
    });
    var area = grid.hdr.rows[1].cells[0];
    area.className = "grid_hdr_editable";
    area.onselectstart = function(e) {
        return((e || event).cancelBubble = true);
    };
    area = area.firstChild.firstChild;
    area.focus();
    area.select();
    dE(area, "keypress", function(e) {
        e = e || event;
        code = e.charCode || e.keyCode;
        if (e.ctrlKey && code == 13)dba.tab_toolbar_click("run_query");
    });
    win.area = area;
};
dba.set_data_table = function(win, full_id) {
    var grid = win.attachGrid();
    grid.enableSmartRendering(true);
    grid.setSkin(skin);
    grid._refresh = "./logic/php/datagrid.php?id=" + encodeURIComponent(full_id);
    grid.loadXML(grid._refresh);
};
function init() {
    dba.layout = new dhtmlXLayoutObject(document.body, "2U", skin);
    dba.layout.items[0].tI("Hierarchy");
    dba.layout.items[0].setWidth(250);
    dba.layout.items[1].tI("Details");
    dba.tree = dba.layout.items[0].attachTree(0);
    dba.tree.setSkin(skin);
    dba.tree.yZ(18, 18);
    dba.tree.setImagePath("./imgs/tree/");
    dba.tree.rx("function");
    dba.tree.eG(function(id) {
        dba.tree.loadXML("./logic/php/tree.php?id=" + id + "&full_id=" + encodeURIComponent(dba.get_id_chain(dba.tree, unescape(id))));
    });
    dba.tree.loadXML("./logic/php/tree.php");
    dba.tree.attachEvent("onClick", function(id) {
        if (id.split("^")[0] == "table")dba.create_tab(id);
        return true;
    });
    dba.toolbar = dba.layout.attachToolbar();
    dba.toolbar.setIconsPath("./imgs/");
    dba.toolbar.attachEvent("onClick", dba.main_toolbar_click);
    dba.toolbar.loadXML("./xml/buttons.xml");
    dba.tabbar = dba.layout.items[1].attachTabbar();
    dba.tabbar.setSkin(skin);
    dba.tabbar.setImagePath("../../dhtmlxTabbar/codebase/imgs/");
    dba.tabbar.enableForceHiding(true);
    dba.tabbar.hG("start", "Start", "100");
    dba.tabbar.gy("start", dba.start_text);
    dba.tabbar.fP("start");
    dba.tabbar.enableTabCloseButton(true);
    dba.tabbar.attachEvent("onTabClose", function(id) {
        delete dba.pages[id];
        return true;
    })
}