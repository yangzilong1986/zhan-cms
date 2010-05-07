var B2T = 100;
var __aAllTabComponents = new Array();
var code = "";

function Browser() {
    var agent = navigator.userAgent.toLowerCase();
    this.ns = ((agent.indexOf('mozilla') != -1) && ((agent.indexOf('spoofer') == -1) && (agent.indexOf('compatible') == -1)));
    this.ie = (agent.indexOf("msie") != -1);
    this.opera = (agent.indexOf("opera") != -1)
};
var browser = new Browser();
var objTab = new Tab();

function Tab() {
    this.id = "";
    this.design = "1";
    this.aItems = new Array();
    this.orientation = "0";
    this.tabarea = true;
    this.designmode = "IMAGE";
    this.loadOnStartup = true;
    this.lastTabItem = null;
    this.refreshAfterLoad = false;
    this.loadFinished = false;
    this.isHooverEnabled = true;
    this.standardstyle = "";
    this.hooverstyle = "";
    this.activestyle = "";
    this.separatorstyle = "";
    this.backgroundstyle = "";
    this.bodystyle = "";
    this.textstyle = "";
    this.activetextstyle = "";
    this.eventTabClick = "";
    this.tabAlignment = 0;
    this.add = aMRH2;
    this.addDyn = alj6;
    this.create = rK1K;
    this.redraw = F5135;
    this.createItem = _xj;
    this.clear = S7k;
    this.getActiveTab = pk1TA;
    this.getActiveTabItem = RaR;
    this.getActiveTabDocument = $4f7;
    this.setActiveTab = W7QFd;
    this.getItems = D59Is;
    this.__intisev = true;
    this.width = 0;
    this.tabMode = 0;
    this.scrollPosition = -1;
    this.scrollButtonWidth = "34";
    this.scrollLeftAllowed = false;
    this.scrollRightAllowed = true;
    this.maxTabItemsPerRow = 100;
    this.textAlignment = 0;
    this.imageAlignment = 0;
    this.remove = kx481;
    this.getItemByIndex = s17;
    this.getTabItems = m138;
    this.getTabItemByIndex = e_Y92;
    if (this.designmode == "IMAGE") {
        var s7n8 = new Image();
        s7n8.src = "design/image/style" + this.design + "/left.png";
        var I34 = new Image();
        I34.src = "design/image/style" + this.design + "/right.png";
        var GG68 = new Image();
        GG68.src = "design/image/style" + this.design + "/leftright.png";
        var _756 = new Image();
        _756.src = "design/image/style" + this.design + "/middle.png";
        var tjx = new Image();
        tjx.src = "design/image/style" + this.design + "/hoover_middle.png";
        var fO8f1 = new Image();
        fO8f1.src = "design/image/style" + this.design + "/sel_left.png";
        var H0a2 = new Image();
        H0a2.src = "design/image/style" + this.design + "/sel_right.png";
        var P82 = new Image();
        P82.src = "design/image/style" + this.design + "/left-sel_right.png";
        var PK$ = new Image();
        PK$.src = "design/image/style" + this.design + "/sel_right-left.png"
    }
};

function kx481(index) {};

function D59Is() {
    return this.aItems
};

function s17(index) {
    var item = null;
    try {
        item = this.aItems[index]
    } catch (Error) {};
    return item
};

function m138() {
    var aTabItems = new Array();
    try {
        for (var i = 0; i < this.aItems.length; i++) {
            var item = this.aItems[i];
            if (item.type == "ITEM") {
                aTabItems[aTabItems.length] = item
            }
        }
    } catch (Error) {};
    return aTabItems
};

function e_Y92(index) {
    var nt1 = 0;
    try {
        for (var i = 0; i < this.aItems.length; i++) {
            var item = this.aItems[i];
            if (item.type == "ITEM") {
                if (nt1 == index) return item;
                nt1++
            }
        }
    } catch (Error) {}
};

function pk1TA() {
    var nt1 = 0;
    try {
        for (var i = 0; i < this.aItems.length; i++) {
            var item = this.aItems[i];
            if (item.type == "ITEM") {
                if (item.active) return nt1;
                nt1++
            }
        }
    } catch (Error) {};
    return 0
};

function RaR() {
    try {
        for (var i = 0; i < this.aItems.length; i++) {
            var item = this.aItems[i];
            if (item.active) return item
        }
    } catch (Error) {};
    return null
};

function $4f7() {
    var item = this.getActiveTabItem();
    try {
        var doc = window.frames[item.name].document
    } catch (Error) {
        doc = null
    }
    return doc
};

function W7QFd(tabNo) {
    var nt1 = 0;
    for (var i = 0; i < this.aItems.length; i++) {
        var item = this.aItems[i];
        if (item.type == "ITEM") {
            if (nt1 == tabNo)
                this.aItems[i].active = true;
            else
                this.aItems[i].active = false;
            nt1++
        }
    }
    this.create(true)
};

function S7k() {
    B2T = 100;
    __aAllTabComponents = new Array();
    this.aItems = new Array()
};

function _xj(type) {
    if (type == "TabSeparator")
        return new TabSeparator();
    else
        return new TabItem("", "", "", "")
};

function aMRH2(item) {
    var id = this.aItems.length;
    this.aItems[id] = item;
    item.id = "tabitem" + B2T;
    item.index = id;
    item.design = this.design;
    B2T++;
    item.tab = this;
    var id = __aAllTabComponents.length;
    __aAllTabComponents[id] = item
};

function alj6(item) {
    var id = this.aItems.length;
    this.aItems[id] = item;
    item.id = "tabitem" + B2T;
    item.index = id;
    item.design = this.design;
    B2T++;
    item.tab = this;
    var id = __aAllTabComponents.length;
    __aAllTabComponents[id] = item;
    item.name = "__tab_area" + id;
    item.pageLoaded = true;
    try {
        var iframe = document.createElement("iframe");
        iframe.name = "__tab_area" + id;
        iframe.id = "__tab_area" + id;
        if (item.url != "")
            iframe.src = item.url;
        else
            iframe.src = "dummy.html";
        iframe.frameBorder = 0;
        iframe.style.height = "100%";
        iframe.style.width = "100%";
        iframe.style.display = "none";
        document.getElementById("__container" + this.orientation).appendChild(iframe);
        this.redraw()
    } catch (Error) {}
};

function F5135() {
    this.create(true)
}

function rK1K(redraw) {
    redraw = (redraw == null) ? false : true;
    var _TPa$ = "";
    var gggV4 = "";
    var oriPrefix = "";
    var isSeparator = false;
    var events = "";
    this.id = controlID;
    if (this.orientation == "1") oriPrefix = "B";
    if (this.designmode == "IMAGE") {
        var backClass = "TabBack";
        var bodyClass = "TabBody"
    } else {
        var backClass = "TabColorBack";
        var bodyClass = "TabColorBody"
    }
    try {
        this.width = j63I6(parent.document.getElementById(controlID))
    } catch (Error) {}
    _TPa$ = __cl();
    if (this.tabMode == 2) {
        _TPa$ += "<div unselectable='on' style='z-Index:1001;position:absolute;width:" + this.scrollButtonWidth + "px;height:17px;left:" + (this.width - this.scrollButtonWidth) + "px;top:1px;'>";
        _TPa$ += "<img id='__tab_scroll_left'   type=image src='design/image/arrow_left.gif' value='<' onclick='__tab_scroll(0)'>";
        _TPa$ += "<img id='__tab_scroll_right'  type=image src='design/image/arrow_right.gif' value='>' onclick='__tab_scroll(1)'>";
        _TPa$ += "</div>"
    }
    if (this.backgroundstyle != "")
        _TPa$ += "<table unselectable='on' style='" + this.backgroundstyle + "' width='100%' height='100%' cellpadding='0' cellspacing='0' border='0'>";
    else
        _TPa$ += "<table unselectable='on' class='" + oriPrefix + backClass + this.design + "' width='100%' height='100%' cellpadding='0' cellspacing='0' border='0'>";
    if (!redraw) {
        if (this.orientation == "1") {
            if (this.tabarea) {
                _TPa$ += "<tr unselectable='on'>";
                _TPa$ += "<td id='__container1' unselectable='on' style='height:100%' class='" + oriPrefix + bodyClass + this.design + "' colspan='" + (this.aItems.length * 2 + 2) + "'>";
                for (var i = 0; i < this.aItems.length; i++) {
                    var item = this.aItems[i];
                    if (item.type == "ITEM") {
                        if (item.active)
                            _TPa$ += "<iframe onload=\"javascript:__tab_doc_loaded(" + i + ",'__tab_area" + i + "')\"  src='dummy.html' style='height:100%;width:100%;' id='__tab_area" + i + "'  name='__tab_area" + i + "' frameborder=0></iframe>";
                        else
                            _TPa$ += "<iframe onload=\"javascript:__tab_doc_loaded(" + i + ",'__tab_area" + i + "')\"  src='dummy.html' style='display:none;height:100%;width:100%;' id='__tab_area" + i + "'  name='__tab_area" + i + "' frameborder=0></iframe>";
                        item.name = "__tab_area" + i
                    }
                }
                _TPa$ += "</td>";
                _TPa$ += "</tr>"
            } else {
                _TPa$ += "<tr>";
                _TPa$ += "<td height='100%' colspan='" + (this.aItems.length * 2 + 2) + "'>";
                _TPa$ += "</td>";
                _TPa$ += "</tr>"
            }
        }
    }
    __calculate();
    var aVisibleItem = new Array();
    for (var i = 0; i < this.aItems.length; i++) {
        if (this.aItems[i].visible)
            aVisibleItem[aVisibleItem.length] = this.aItems[i];
        else
            this.aItems[i].active = false
    }
    this.visibleItems = aVisibleItem.length;
    var nt1 = 0;
    var isFirst = false;
    var isMiddle = false;
    var lastRowItemCount = aVisibleItem.length % this.maxTabItemsPerRow;
    while (nt1 < aVisibleItem.length) {
        _TPa$ += "<tr unselectable='on'>";
        _TPa$ += "<td unselectable='on' style='width:100%'>";
        _TPa$ += "<div  unselectable='on' id='row'>";
        gggV4 = "";
        if (this.tabMode == 2) {
            gggV4 += "<div style='height:22px'></div>";
            gggV4 += "<div id='scroll' style='position:absolute;left:0px;top:0px;'><table cellpadding='0' cellspacing='0' border='0'><tr>"
        } else {
            gggV4 += "<table cellpadding='0' cellspacing='0' border='0'><tr>"
        }
        for (var i = 0; i < this.maxTabItemsPerRow; i++) {
            if (nt1 == aVisibleItem.length) break;
            var item = aVisibleItem[nt1];
            var isLastItemSeparator = false;
            isFirst = (i == 0) ? true : false;
            isMiddle = (i > 0 && nt1 < aVisibleItem.length - 1 && i < this.maxTabItemsPerRow - 1) ? true : false;
            isLastRow = (nt1 >= (aVisibleItem.length - lastRowItemCount)) ? true : false;
            if (this.tabAlignment == 0) {
                alignwidth = ""
            } else {
                if (!isLastRow)
                    alignwidth = "width='" + 100 / this.maxTabItemsPerRow + "%'";
                else
                    alignwidth = "width='" + 100 / lastRowItemCount + "%'"
            }
            if (i > 0) {
                isLastItemSeparator = aVisibleItem[nt1 - 1].type == "SEPARATOR" ? true : false
            }
            var textAlignment = "";
            if (this.textAlignment == 0)
                textAlignment = "left";
            else if (this.textAlignment == 1)
                textAlignment = "center";
            else
                textAlignment = "right";
            events = " onmouseover='__tab_direct(\"" + item.id + "\",\"OVER\")'";
            events += " onmouseout='__tab_direct(\"" + item.id + "\",\"OUT\")'";
            events += " onmousedown='__tab_direct(\"" + item.id + "\",\"DOWN\")'";
            events += " onmouseup='__tab_direct(\"" + item.id + "\",\"UP\")'>";
            if (browser.ie)
                events += "<table unselectable='on' title='" + item.title + "' width='100%' height='100%' cellpadding='0' cellspacing='0' border='0'><tr unselectable='on'>";
            else
                events += "<a style='text-decoration: none' href='javascript: void(0);'><table unselectable='on' title='" + item.title + "' width='100%' height='100%' cellpadding='0' cellspacing='0' border='0'><tr unselectable='on'>";
            var imageCell = "<td style='padding-left: 3px;'><img unselectable='on'  src='" + item.image + "' border=0></td>";
            var textCell1 = "<td align='" + textAlignment + "' width='100%' nowrap><span id='" + item.id + "_text' " + (item.enabled ? "" : "style='color:gray'") + " unselectable='on' ";
            var textCell2 = item.text + "</span></td>";
            if (browser.ie)
                var closeTags = "</tr></table></td>";
            else
                var closeTags = "</tr></table></a></td>";
            var textClass = "";
            if (item.active)
                textClass = ((this.activetextstyle == "") ? (" class='" + oriPrefix + "TabItemTextSelected" + this.design + "' ") : (" style='" + this.activetextstyle + "' ")) + ">";
            else
                textClass = ((this.textstyle == "") ? (" class='" + oriPrefix + "TabItemText" + this.design + "' ") : (" style='" + this.textstyle + "' ")) + ">";
            var imagetext = "";
            if (this.imageAlignment == 0) {
                if (item.image) imagetext = imageCell;
                if (item.text) imagetext += textCell1 + textClass + textCell2
            } else {
                if (item.text) imagetext += textCell1 + textClass + textCell2;
                if (item.image) imagetext = imageCell
            }
            if (this.designmode == "IMAGE") {
                if (item.type == "ITEM") {
                    item.isFirst = false;
                    item.isMiddle = false;
                    item.isLast = false;
                    if (isFirst) {
                        item.isFirst = true;
                        if (item.active) {
                            var leftClass = oriPrefix + "TabItemSelectedLeft" + this.design;
                            var middleClass = oriPrefix + "TabItemSelectedMiddle" + this.design;
                            gggV4 += "<td unselectable='on'><div unselectable='on' id='" + item.id + "_1' class='" + leftClass + "'></div></td>";
                            gggV4 += "<td unselectable='on' " + alignwidth + " id='" + item.id + "' class='" + middleClass + "'";
                            gggV4 += events;
                            gggV4 += imagetext;
                            gggV4 += closeTags;
                            if (this.maxTabItemsPerRow == 1) gggV4 += "<td><div  unselectable='on' id='" + item.id + "_2' class='" + oriPrefix + "TabItemSelectedRight" + this.design + "'></div></td>"
                        } else {
                            var leftClass = oriPrefix + "TabItemLeft" + this.design;
                            var middleClass = oriPrefix + "TabItemMiddle" + this.design;
                            gggV4 += "<td><div  unselectable='on' class='" + leftClass + "' id='" + item.id + "_1'></div></td>";
                            gggV4 += "<td " + alignwidth + " id='" + item.id + "' class='" + middleClass + "'";
                            gggV4 += events;
                            gggV4 += imagetext;
                            gggV4 += closeTags
                        }
                    } else if (isMiddle) {
                        item.isMiddle = true;
                        if (aVisibleItem[nt1 - 1].type == "ITEM") {
                            if (aVisibleItem[nt1 - 1].active) {
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemRightSelectedLeft" + this.design + "'></div></td>";
                                gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemMiddle" + this.design + "'";
                                gggV4 += events;
                                gggV4 += imagetext;
                                gggV4 += closeTags
                            } else {
                                if (item.active) {
                                    gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemLeftSelectedRight" + this.design + "'></div></td>";
                                    gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemSelectedMiddle" + this.design + "'";
                                    gggV4 += events;
                                    gggV4 += imagetext;
                                    gggV4 += closeTags
                                } else {
                                    gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemLeftRight" + this.design + "'></div></td>";
                                    gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemMiddle" + this.design + "'";
                                    gggV4 += events;
                                    gggV4 += imagetext;
                                    gggV4 += closeTags
                                }
                            }
                        } else {
                            if (item.active) {
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemSelectedLeft" + this.design + "'></div></td>";
                                gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemSelectedMiddle" + this.design + "'";
                                gggV4 += events;
                                gggV4 += imagetext;
                                gggV4 += closeTags
                            } else {
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemLeft" + this.design + "'></div></td>";
                                gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemMiddle" + this.design + "'";
                                gggV4 += events;
                                gggV4 += imagetext;
                                gggV4 += closeTags
                            }
                        }
                    } else {
                        item.isLast = true;
                        if (aVisibleItem[nt1 - 1].type == "ITEM") {
                            if (aVisibleItem[nt1 - 1].active) {
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemRightSelectedLeft" + this.design + "'></div></td>";
                                gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemMiddle" + this.design + "'";
                                gggV4 += events;
                                gggV4 += imagetext;
                                gggV4 += closeTags;
                                gggV4 += "<td><div id='" + item.id + "_2' class='" + oriPrefix + "TabItemRight" + this.design + "'></div></td>"
                            } else {
                                if (item.active) {
                                    gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemLeftSelectedRight" + this.design + "'></div></td>";
                                    gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemSelectedMiddle" + this.design + "'";
                                    gggV4 += events;
                                    gggV4 += imagetext;
                                    gggV4 += closeTags;
                                    gggV4 += "<td><div unselectable='on' id='" + item.id + "_2' class='" + oriPrefix + "TabItemSelectedRight" + this.design + "'></div></td>"
                                } else {
                                    gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemLeftRight" + this.design + "'></div></td>";
                                    gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemMiddle" + this.design + "'";
                                    gggV4 += events;
                                    gggV4 += imagetext;
                                    gggV4 += closeTags;
                                    gggV4 += "<td><div unselectable='on' id='" + item.id + "_2' class='" + oriPrefix + "TabItemRight" + this.design + "'></div></td>"
                                }
                            }
                        } else {
                            if (item.active) {
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemSelectedLeft" + this.design + "'></div></td>";
                                gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemSelectedMiddle" + this.design + "'";
                                gggV4 += events;
                                gggV4 += imagetext;
                                gggV4 += closeTags;
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_2' class='" + oriPrefix + "TabItemSelectedRight" + this.design + "'></div></td>"
                            } else {
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemLeft" + this.design + "'></div></td>";
                                gggV4 += "<td " + alignwidth + "  id='" + item.id + "' class='" + oriPrefix + "TabItemMiddle" + this.design + "'";
                                gggV4 += events;
                                gggV4 += imagetext;
                                gggV4 += closeTags;
                                gggV4 += "<td><div unselectable='on' id='" + item.id + "_2' class='" + oriPrefix + "TabItemRight" + this.design + "'></div></td>"
                            }
                        }
                    }
                } else {
                    isSeparator = true;
                    if (i > 0) {
                        if (aVisibleItem[nt1 - 1].active) {
                            gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemSelectedRight" + this.design + "'></div></td>"
                        } else {
                            gggV4 += "<td><div unselectable='on' id='" + item.id + "_1' class='" + oriPrefix + "TabItemRight" + this.design + "'></div></td>"
                        }
                    }
                    gggV4 += "<td  style='width:" + item.width + "' nowrap class='" + oriPrefix + "TabLine" + this.design + "'><div style='width:" + item.width + "' id='" + item.id + "_text' unselectable='on' class='" + oriPrefix + "TabItemText" + this.design + "'>" + item.text + "</div></td>"
                }
            } else {
                if (item.type == "ITEM") {
                    var textClass = "";
                    var textStyle = "";
                    gggV4 += "<td  title='" + item.title + "' class='TabColorSeparator" + this.design + "'";
                    gggV4 += " onmouseover='__tab_direct(\"" + item.id + "\",\"OVER\")'";
                    gggV4 += " onmouseout='__tab_direct(\"" + item.id + "\",\"OUT\")'";
                    gggV4 += " onmousedown='__tab_direct(\"" + item.id + "\",\"DOWN\")'";
                    gggV4 += " onmouseup='__tab_direct(\"" + item.id + "\",\"UP\")'>";
                    if (item.active) {
                        textClass = "TabColorTextSelected" + this.design;
                        textStyle = this.activetextstyle;
                        if (this.activestyle != "")
                            gggV4 += "<a style='text-decoration: none' href='javascript: void(0);'><table unselectable='on'  id='" + item.id + "' style='" + this.activestyle + "' cellpadding='0' cellspacing='0'><tr>";
                        else
                            gggV4 += "<a style='text-decoration: none' href='javascript: void(0);'><table unselectable='on'  id='" + item.id + "' class='TabColorSelected" + this.design + "' cellpadding='0' cellspacing='0'><tr>"
                    } else {
                        textClass = "TabColorText" + this.design;
                        textStyle = this.textstyle;
                        if (this.standardstyle != "")
                            gggV4 += "<a style='text-decoration: none' href='javascript: void(0);'><table unselectable='on'  id='" + item.id + "' style='" + this.standardstyle + "' cellpadding='0' cellspacing='0'><tr>";
                        else
                            gggV4 += "<a style='text-decoration: none' href='javascript: void(0);'><table unselectable='on'  id='" + item.id + "' class='TabColorStandard" + this.design + "' cellpadding='0' cellspacing='0'><tr>"
                    }
                    if (item.image) gggV4 += "<td><span style='margin-left:2px;'><img unselectable='on'  src='" + item.image + "' border=0></span></td>";
                    if (item.text) {
                        if (this.textstyle != "")
                            gggV4 += "<td nowrap><span id='" + item.id + "_text' unselectable='on' style='" + textStyle + "'>" + item.text + "</span></td>";
                        else
                            gggV4 += "<td nowrap><span id='" + item.id + "_text' unselectable='on' class='" + textClass + "'>" + item.text + "</span></td>"
                    }
                    gggV4 += "</tr></table></a></td>"
                } else {
                    isSeparator = true;
                    if (this.separatorstyle != "")
                        gggV4 += "<td style='" + this.separatorstyle + "' width='100%'>&nbsp;</td>";
                    else
                        gggV4 += "<td class='" + oriPrefix + "TabColorSeparator" + this.design + "' width='100%'>&nbsp;</td>"
                }
            }
            nt1++
        }
        if (!isSeparator) {
            if (this.tabAlignment == 0) {
                if (this.designmode == "IMAGE")
                    gggV4 += "<td class='" + oriPrefix + "TabLine" + this.design + "' width='100%'>&nbsp;</td>";
                else
                    gggV4 += "<td class='" + oriPrefix + "TabColorSeparator" + this.design + "' width='100%'>&nbsp;</td>"
            }
        }
        if (this.tabMode == 2)
            gggV4 += "</tr></table></div>";
        else
            gggV4 += "</tr></table>";
        _TPa$ += gggV4;
        _TPa$ += "</div></td></tr>";
        this.html = gggV4
    }
    if (!redraw) {
        if (this.orientation == "0") {
            if (this.tabarea) {
                _TPa$ += "<tr>";
                _TPa$ += "<td id='__container0' style='height:100%;width:100%' class='" + oriPrefix + bodyClass + this.design + "'>";
                for (var i = 0; i < this.aItems.length; i++) {
                    var item = this.aItems[i];
                    if (item.type == "ITEM") {
                        if (item.active)
                            _TPa$ += "<iframe onload=\"javascript:__tab_doc_loaded(" + i + ",'__tab_area" + i + "')\" src='dummy.html' style='height:100%;width:100%;' id='__tab_area" + i + "'  name='__tab_area" + i + "' frameborder=0></iframe>";
                        else
                            _TPa$ += "<iframe onload=\"javascript:__tab_doc_loaded(" + i + ",'__tab_area" + i + "')\" src='dummy.html' style='display:none;height:100%;width:100%;' id='__tab_area" + i + "'  name='__tab_area" + i + "' frameborder=0></iframe>";
                        item.name = "__tab_area" + i
                    }
                }
                _TPa$ += "</td>";
                _TPa$ += "</tr>"
            } else {
                _TPa$ += "<tr>";
                _TPa$ += "<td height='100%' colspan='" + (this.maxTabItemsPerRow * 2 + 2) + "'>";
                _TPa$ += "</td>";
                _TPa$ += "</tr>"
            }
        }
    }
    _TPa$ += "</table>";
    __calculate();
    if (redraw) {
        document.getElementById("row").innerHTML = this.html;
        for (var W2K = 0; W2K < objTab.aItems.length; W2K++) {
            var item = objTab.aItems[W2K];
            if (item.type == "ITEM" && item.visible) {
                if (item.active) {
                    document.getElementById(item.name).style.display = "inline"
                } else {
                    if (document.getElementById(item.name).style.display != "none")
                        document.getElementById(item.name).style.display = "none"
                }
            }
        }
    } else {
        code = _TPa$;
        window.setTimeout("__tab_delay()", 50)
    }
}

function __tab_delay() {
    __buildString();
    document.body.innerHTML = code;
    if (!objTab.tabarea) {
        objTab.loadFinished = true;
        objTab.lastTabItem = objTab.getActiveTabItem();
        return
    }
    for (var i = 0; i < objTab.aItems.length; i++) {
        var item = objTab.aItems[i];
        if (objTab.loadOnStartup && item.type == "ITEM" && item.url != "" && !item.active) {
            document.getElementById(item.name).src = item.url
        }
        if (item.type == "ITEM" && item.active) {
            if (item.url != "") {
                document.getElementById(item.name).src = item.url
            } else {
                if (browser.ns) window.frames[item.name].document.body.style.backgroundColor = "white"
            }
            objTab.lastTabItem = item
        }
    }
    setTimeout("__tab_check_loaded()", 10)
};

function __tab_doc_loaded(index, name) {
    var item = objTab.getItemByIndex(index);
    if (item.url == "") objTab.loadFinished = true;
    if (document.getElementById(name).src.indexOf("dummy.html") >= 0) return;
    for (var i = 0; i < objTab.aItems.length; i++) {
        if (i == index) {
            var item = objTab.aItems[i];
            item.pageLoaded = true;
            try {
                parent.tabEventPageLoaded(item, controlID)
            } catch (Error) {}
            if (item.active && item.refreshAfterLoad) {
                item.refresh()
            }
        }
    }
    var allLoaded = true;
    if (objTab.loadOnStartup) {
        if (!browser.opera) {
            for (var i = 0; i < objTab.aItems.length; i++) {
                var item = objTab.aItems[i];
                if (item.type == "ITEM" && item.url != "" && !item.pageLoaded) {
                    allLoaded = false
                }
            }
        }
        if (allLoaded) {
            objTab.loadFinished = true;
            try {
                parent.tabEventLoaded(controlID)
            } catch (Error) {}
        }
    } else {
        objTab.loadFinished = true;
        allLoaded = true;
        for (var i = 0; i < objTab.aItems.length; i++) {
            var item = objTab.aItems[i];
            if (item.type == "ITEM" && item.url != "" && !item.pageLoaded) {
                allLoaded = false
            }
        }
        if (allLoaded) {
            try {
                parent.tabEventLoaded(controlID)
            } catch (Error) {}
        }
    }
}

function __tab_check_loaded() {
    if (objTab.tabMode == 2) {
        var allWidth = 0;
        var lastRight = 0;
        for (var i = 0; i < objTab.aItems.length; i++) {
            var item = objTab.aItems[i];
            if (item.visible) {
                var width = 0;
                try {
                    width = width + parseInt(document.getElementById(objTab.aItems[i].id + "_1").offsetWidth)
                } catch (Error) {}
                width = width + parseInt(document.getElementById(objTab.aItems[i].id).offsetWidth);
                try {
                    width = width + parseInt(document.getElementById(objTab.aItems[i].id + "_2").offsetWidth / 2)
                } catch (Error) {
                    width = width + 2
                }
                item.width = width;
                allWidth = allWidth + width;
                if (i > 0)
                    item.right = lastRight + width;
                else
                    item.right = width;
                lastRight = item.right
            }
        }
        objTab.allTabItemsWidth = allWidth
    }
    if (browser.opera) {
        objTab.loadFinished = true;
        try {
            parent.tabEventLoaded(controlID)
        } catch (Error) {}
    }
}

function __tab_direct(id, action) {
    var oriPrefix = "";
    var hasTabArea = true;
    if (action == "OVER" && !objTab.isHooverEnabled) {
        if (browser.ie)
            document.getElementById(id).style.cursor = "hand";
        return
    }
    try {
        if (__aAllTabComponents.length > 0) {
            var component = __aAllTabComponents[0];
            hasTabArea = component.tab.tabarea;
            if (component.tab.orientation == "1") oriPrefix = "B"
        }
        if (!objTab.loadFinished) return;
        var aVisibleItem = new Array();
        for (var i = 0; i < __aAllTabComponents.length; i++) {
            if (__aAllTabComponents[i].visible) aVisibleItem[aVisibleItem.length] = __aAllTabComponents[i]
        }
        for (var nt1 = 0; nt1 < aVisibleItem.length; nt1++) {
            if (aVisibleItem[nt1].id == id) {
                var component = aVisibleItem[nt1];
                if (action == "OVER") {
                    try {
                        parent.tabEventItemOver(component)
                    } catch (Error) {}

                }
                if (action == "DOWN" && component.type == "ITEM") {
                    try {
                        var $0m = false;
                        $0m = parent.tabEventBeforeTabClick(component.tab.lastTabItem);
                        if ($0m) return
                    } catch (Error) {}

                }
                if (component.enabled) {
                    if (component.tab.designmode == "IMAGE") {
                        if (action == "OVER" && component.type == "ITEM") {
                            if (component.active) {
                                if (document.getElementById(id).className != oriPrefix + "TabItemSelectedMiddle" + component.design) {
                                    document.getElementById(id).className = oriPrefix + "TabItemSelectedMiddle" + component.design
                                }

                            } else {
                                if (document.getElementById(id).className != oriPrefix + "TabItemHoverMiddle" + component.design) {
                                    document.getElementById(id).className = oriPrefix + "TabItemHoverMiddle" + component.design
                                }
                            }
                        }
                        if (action == "OUT" && component.type == "ITEM") {
                            if (component.active) {
                                if (document.getElementById(id).className != oriPrefix + "TabItemSelectedMiddle" + component.design)
                                    document.getElementById(id).className = oriPrefix + "TabItemSelectedMiddle" + component.design
                            } else {
                                if (document.getElementById(id).className != oriPrefix + "TabItemMiddle" + component.design)
                                    document.getElementById(id).className = oriPrefix + "TabItemMiddle" + component.design
                            }

                        }
                        if (action == "DOWN" && component.type == "ITEM") {
                            if (aVisibleItem.length > 1) {
                                for (var h6O9 = 0; h6O9 < aVisibleItem.length; h6O9++) {
                                    var item = aVisibleItem[h6O9];
                                    if (item.active) {
                                        if (h6O9 == 0) {
                                            document.getElementById(item.id + "_1").className = oriPrefix + "TabItemLeft" + component.design;
                                            document.getElementById(item.id).className = oriPrefix + "TabItemMiddle" + component.design;
                                            if (aVisibleItem[1].type == "ITEM")
                                                document.getElementById(aVisibleItem[h6O9 + 1].id + "_1").className = oriPrefix + "TabItemLeftRight" + component.design;
                                            else
                                                document.getElementById(aVisibleItem[h6O9 + 1].id + "_1").className = oriPrefix + "TabItemRight" + component.design
                                        } else {
                                            if (aVisibleItem[h6O9 - 1].type == "ITEM")
                                                document.getElementById(item.id + "_1").className = oriPrefix + "TabItemLeftRight" + component.design;
                                            else
                                                document.getElementById(item.id + "_1").className = oriPrefix + "TabItemLeft" + component.design;
                                            document.getElementById(item.id).className = oriPrefix + "TabItemMiddle" + component.design;
                                            if (h6O9 == aVisibleItem.length - 1) {
                                                document.getElementById(item.id + "_2").className = oriPrefix + "TabItemRight" + component.design
                                            } else {
                                                if (item.isFirst) {
                                                    document.getElementById(item.id + "_1").className = oriPrefix + "TabItemLeft" + component.design;
                                                    document.getElementById(item.id).className = oriPrefix + "TabItemMiddle" + component.design;
                                                    if (aVisibleItem[1].type == "ITEM")
                                                        document.getElementById(aVisibleItem[h6O9 + 1].id + "_1").className = oriPrefix + "TabItemLeftRight" + component.design;
                                                    else
                                                        document.getElementById(aVisibleItem[h6O9 + 1].id + "_1").className = oriPrefix + "TabItemRight" + component.design
                                                } else if (item.isLast) {
                                                    document.getElementById(item.id + "_2").className = oriPrefix + "TabItemRight" + component.design
                                                } else {
                                                    var id = aVisibleItem[h6O9 + 1].id;
                                                    if (aVisibleItem[h6O9 + 1].type == "ITEM")
                                                        document.getElementById(id + "_1").className = oriPrefix + "TabItemLeftRight" + component.design;
                                                    else
                                                        document.getElementById(id + "_1").className = oriPrefix + "TabItemRight" + component.design
                                                }
                                            }
                                        }
                                        if (item.text)
                                            document.getElementById(item.id + "_text").className = oriPrefix + "TabItemText" + component.design;
                                        item.active = false;
                                        break
                                    }
                                }
                            }
                            if (aVisibleItem.length > 1) {
                                if (component.type == "ITEM") {
                                    component.active = true;
                                    if (nt1 == 0) {
                                        document.getElementById(component.id + "_1").className = oriPrefix + "TabItemSelectedLeft" + component.design;
                                        document.getElementById(component.id).className = oriPrefix + "TabItemSelectedMiddle" + component.design;
                                        if (aVisibleItem[nt1 + 1].type == "ITEM")
                                            document.getElementById(aVisibleItem[nt1 + 1].id + "_1").className = oriPrefix + "TabItemRightSelectedLeft" + component.design;
                                        else
                                            document.getElementById(aVisibleItem[nt1 + 1].id + "_1").className = oriPrefix + "TabItemSelectedRight" + component.design
                                    } else if (nt1 > 0 && nt1 < aVisibleItem.length - 1) {
                                        if (component.isFirst) {
                                            document.getElementById(component.id + "_1").className = oriPrefix + "TabItemSelectedLeft" + component.design;
                                            document.getElementById(component.id).className = oriPrefix + "TabItemSelectedMiddle" + component.design;
                                            if (aVisibleItem[nt1 + 1].type == "ITEM")
                                                document.getElementById(aVisibleItem[nt1 + 1].id + "_1").className = oriPrefix + "TabItemRightSelectedLeft" + component.design;
                                            else
                                                document.getElementById(aVisibleItem[nt1 + 1].id + "_1").className = oriPrefix + "TabItemSelectedRight" + component.design
                                        } else if (component.isLast) {
                                            if (aVisibleItem[nt1 - 1].type == "ITEM")
                                                document.getElementById(component.id + "_1").className = oriPrefix + "TabItemLeftSelectedRight" + component.design;
                                            else
                                                document.getElementById(component.id + "_1").className = oriPrefix + "TabItemSelectedLeft" + component.design;
                                            document.getElementById(component.id).className = oriPrefix + "TabItemSelectedMiddle" + component.design;
                                            document.getElementById(component.id + "_2").className = oriPrefix + "TabItemSelectedRight" + component.design
                                        } else {
                                            if (aVisibleItem[nt1 - 1].type == "ITEM")
                                                document.getElementById(component.id + "_1").className = oriPrefix + "TabItemLeftSelectedRight" + component.design;
                                            else
                                                document.getElementById(component.id + "_1").className = oriPrefix + "TabItemSelectedLeft" + component.design;
                                            document.getElementById(component.id).className = oriPrefix + "TabItemSelectedMiddle" + component.design;
                                            if (aVisibleItem[nt1 + 1].type == "ITEM")
                                                document.getElementById(aVisibleItem[nt1 + 1].id + "_1").className = oriPrefix + "TabItemRightSelectedLeft" + component.design;
                                            else
                                                document.getElementById(aVisibleItem[nt1 + 1].id + "_1").className = oriPrefix + "TabItemSelectedRight" + component.design
                                        }

                                    } else {
                                        if (aVisibleItem[nt1 - 1].type == "ITEM")
                                            document.getElementById(component.id + "_1").className = oriPrefix + "TabItemLeftSelectedRight" + component.design;
                                        else
                                            document.getElementById(component.id + "_1").className = oriPrefix + "TabItemSelectedLeft" + component.design;
                                        document.getElementById(component.id).className = oriPrefix + "TabItemSelectedMiddle" + component.design;
                                        document.getElementById(component.id + "_2").className = oriPrefix + "TabItemSelectedRight" + component.design
                                    }
                                    if (item.text)
                                        document.getElementById(component.id + "_text").className = oriPrefix + "TabItemTextSelected" + component.design
                                }
                            }
                        }
                    } else {
                        if (action == "OVER" && component.type == "ITEM") {
                            if (!component.active) {
                                if (component.tab.hooverstyle != "") {
                                    document.getElementById(id).style.cssText = component.tab.hooverstyle
                                } else {
                                    document.getElementById(id).className = oriPrefix + "TabColorHover" + component.design
                                }
                            }
                        }
                        if (action == "OUT" && component.type == "ITEM") {
                            if (component.active) {
                                if (component.tab.activestyle != "")
                                    document.getElementById(id).style.cssText = component.tab.activestyle;
                                else
                                    document.getElementById(id).className = oriPrefix + "TabColorSelected" + component.design
                            } else {
                                if (component.tab.standardstyle != "")
                                    document.getElementById(id).style.cssText = component.tab.standardstyle;
                                else
                                    document.getElementById(id).className = oriPrefix + "TabColorStandard" + component.design
                            }

                        }
                        if (action == "DOWN" && component.type == "ITEM") {
                            for (var h6O9 = 0; h6O9 < aVisibleItem.length; h6O9++) {
                                var item = aVisibleItem[h6O9];
                                if (item.active) {
                                    if (component.tab.standardstyle != "")
                                        document.getElementById(item.id).style.cssText = component.tab.standardstyle;
                                    else
                                        document.getElementById(item.id).className = oriPrefix + "TabColorStandard" + component.design;
                                    if (component.tab.textstyle != "")
                                        document.getElementById(item.id + "_text").style.cssText = component.tab.textstyle;
                                    else
                                        document.getElementById(item.id + "_text").className = oriPrefix + "TabColorText" + component.design;
                                    item.active = false
                                }
                            }
                            component.active = true;
                            if (component.tab.activestyle != "")
                                document.getElementById(id).style.cssText = component.tab.activestyle;
                            else
                                document.getElementById(id).className = oriPrefix + "TabColorSelected" + component.design;
                            if (component.tab.activetextstyle != "")
                                document.getElementById(id + "_text").style.cssText = component.tab.activetextstyle;
                            else
                                document.getElementById(id + "_text").className = oriPrefix + "TabColorTextSelected" + component.design

                        }
                    }
                }
                if (action == "DOWN" && component.type == "ITEM" && component.enabled) {
                    try {
                        if (component.tab.eventTabClick != "") {
                            eval("parent." + component.tab.eventTabClick + "()");
                            return
                        }

                    } catch (Error) {}
                    component.tab.lastTabItem = component;
                    if (hasTabArea) {
                        if (component.url != "") {
                            if (!component.cached) {
                                document.getElementById(component.name).src = component.url
                            } else {
                                if (!component.pageLoaded) {
                                    document.getElementById(component.name).src = component.url;
                                    P6o2$ = component
                                }
                            }
                        } else {
                            if (browser.ns) window.frames[component.name].document.body.style.backgroundColor = "white"
                        }
                        for (var W2K = 0; W2K < objTab.aItems.length; W2K++) {
                            var curItem = objTab.aItems[W2K];
                            if (curItem.type == "ITEM" && curItem.visible) {
                                if (document.getElementById(curItem.name).style.display != "none")
                                    document.getElementById(curItem.name).style.display = "none"
                            }
                        }
                        document.getElementById(component.name).style.display = "inline";
                        try {
                            parent.tabEventTabClick(component, component.tab.id)
                        } catch (Error) {}
                    }
                }
                if (action == "OUT") {
                    try {
                        parent.tabEventItemOut(component)
                    } catch (Error) {}
                }
                return
            }
        }
    } catch (Error) {}
}

var P6o2$ = null;

function __tab_load_item() {
    try {
        var test = window.frames[P6o2$.name].document.body.innerHTML;
        if (test == "") {
            setTimeout("__tab_load_item()", 50);
            return
        }
        P6o2$.pageLoaded = true;
        try {
            parent.tabEventPageLoaded(P6o2$, controlID)
        } catch (Error) {}
        if (P6o2$.refreshAfterLoad) {
            P6o2$.refresh()
        }
    } catch (Error) {
        setTimeout("__tab_load_item()", 50);
        return
    }
}

function TabItem(text, image, active, url) {
    this.tab = null;
    this.text = text;
    this.image = image;
    this.active = active;
    this.index = 0;
    if (url != "")
        this.url = url;
    else
        this.url = "";
    this.iframe = null;
    this.design = "1";
    this.type = "ITEM";
    this.title = "";
    this.cached = true;
    this.tag = "";
    this.call = OeL6m;
    this.html = "";
    this.pageLoaded = false;
    this.visible = true;
    this.enabled = true;
    this.setVisible = _tL;
    this.setEnabled = aP01;
    this.netPostback = wHj40;
    this.netValidate = T841;
    this.refresh = Ih0P;
    this.getDocument = __tabitem_getdoc;
    this.getWindow = J62xF;
    this.setUrl = BH98
}

function Ih0P() {
    try {
        window.frames[this.name].document.body.innerHTML = window.frames[this.name].document.body.innerHTML
    } catch (Error) {}
}

function _tL(value) {
    this.visible = value;
    this.tab.create(true)
}

function aP01(value) {
    this.enabled = value;
    this.tab.create(true)
}

function J62xF() {
    var window = null;
    try {
        window = window.frames[this.name]
    } catch (Error) {}
    return window
}

function __tabitem_getdoc() {
    var doc = null;
    try {
        doc = window.frames[this.name].document
    } catch (Error) {}
    return doc
}

function OeL6m(name) {
    try {
        return eval("window.frames[this.name]." + name)
    } catch (Error) {
        return false
    }
}

function wHj40() {
    return this.call("__doPostBack('','')")
}

function T841() {
    if (browser.ie)
        return this.call("Page_ClientValidate()");
    else
        return true
}

function BH98(url) {
    try {
        document.getElementById(this.name).src = url;
        return true
    } catch (Error) {
        return false
    }
}

function TabSeparator() {
    this.type = "SEPARATOR";
    this.text = "";
    this.width = "100%";
    this.visible = true;
    this.enabled = true
}

function __tab_scroll(direction) {
    if (direction == 0) {
        if (objTab.scrollPosition >= 0) {
            if (objTab.scrollLeftAllowed) objTab.scrollPosition = objTab.scrollPosition - 1
        }
    } else {
        if (objTab.scrollRightAllowed) objTab.scrollPosition = objTab.scrollPosition + 1
    }
    var position = 0;
    if (objTab.scrollPosition == -1) {
        position = 0
    } else {
        var nt1 = 0;
        for (var i = 0; i < objTab.aItems.length; i++) {
            var item = objTab.aItems[i];
            if (item.visible) {
                if (nt1 == objTab.scrollPosition) {
                    position = objTab.aItems[i].right;
                    break
                }
                nt1++
            }
        }
    }
    document.getElementById("scroll").style.left = -position;
    if ((objTab.width - objTab.scrollButtonWidth) < (objTab.allTabItemsWidth - position))
        objTab.scrollRightAllowed = true;
    else
        objTab.scrollRightAllowed = false;
    if (objTab.scrollPosition > -1)
        objTab.scrollLeftAllowed = true;
    else
        objTab.scrollLeftAllowed = false
}

function j63I6(obj) {
    if (browser.ie) {
        return parseInt(obj.style.pixelWidth)
    } else {
        var width = obj.width ? obj.width : obj.style.width;
        if (width.indexOf("%") > 0) width = obj.offsetWidth;
        return parseInt(width)
    }
}

var JKB7u = "2.0.0323";
var EW70c = 20;