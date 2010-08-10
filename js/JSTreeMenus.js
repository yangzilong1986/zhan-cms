/** Global variables.  Not to be altered unless you know what you're doing.
 *  User-configurable options are at the end of this document.
 */

var MTMLoaded = false;
var MTMLevel;
var MTMBar = new Array();
var MTMIndices = new Array();
var MTMUA = new MTMBrowser();
var MTMClickedItem = false;
var MTMExpansion = false;
var MTMNumber = 1;
var MTMTrackedItem = false;
var MTMTrack = false;
var MTMFrameNames;
var MTMFirstRun = true;
var MTMCurrentTime = 0;
// for checking timeout.
var MTMUpdating = false;
var MTMWinSize, MTMyval, MTMxval;
var MTMOutputString = "";

var MTMCookieString = "";
var MTMCookieCharNum = 0;
// cookieString.charAt()-number


function MTMenuItem(text, url, target, tooltip, icon) {

    this.text = text;
    this.url = url ? url : "";
    this.target = target ? target : "";
    this.tooltip = tooltip;
    this.icon = icon ? icon : "";
    this.number = MTMNumber++;
    this.submenu = null;
    this.expanded = true;
    this.MTMakeSubmenu = MTMakeSubmenu;
}

function MTMakeSubmenu(menu, isExpanded, collapseIcon, expandIcon) {

    this.submenu = menu;
    this.expanded = isExpanded;
    this.collapseIcon = collapseIcon ? collapseIcon : "menu_line.gif";
    this.expandIcon = expandIcon ? expandIcon : "menu_t.gif";
}

/** Define the Menu object. */
function MTMenu() {

    this.items = new Array();
    this.MTMAddItem = MTMAddItem;
}

function MTMAddItem(item) {

    this.items[this.items.length] = item;
}

/** Define the icon list, addIcon function and MTMIcon item. */
function IconList() {

    this.items = new Array();
    this.addIcon = addIcon;
}

function addIcon(item) {

    this.items[this.items.length] = item;
}

function MTMIcon(iconfile, match, type) {

    this.file = iconfile;
    this.match = match;
    this.type = type;
}

/** Code that picks up frame names of frames in the parent frameset. */
function MTMgetFrames() {

    if (MTMUA.MTMable) {
        MTMFrameNames = new Array();
        for (i = 0; i < parent.frames.length; i++) {
            MTMFrameNames[i] = parent.frames[i].name;
        }
    }
}

/** Functions to draw the menu. */
function MTMSubAction(SubItem) {

    SubItem.expanded = (SubItem.expanded) ? false : true;

    if (SubItem.expanded) {
        MTMExpansion = true;
    }

    MTMClickedItem = SubItem.number;

    if (MTMTrackedItem && MTMTrackedItem != SubItem.number) {
        MTMTrackedItem = false;
    }

    if (MTMEmulateWE || SubItem.url == "" || !SubItem.expanded) {
        setTimeout("MTMDisplayMenu()", 10);
        return false;
    } else {
        return true;
    }
}

function MTMStartMenu() {

    MTMLoaded = true;
    if (MTMFirstRun) {
        MTMCurrentTime++;
        if (MTMCurrentTime == MTMTimeOut) { // call MTMDisplayMenu
            setTimeout("MTMDisplayMenu()", 10);
        } else {
            setTimeout("MTMStartMenu()", 100);
        }
    }
}

function MTMDisplayMenu() {

    if (MTMUA.MTMable && !MTMUpdating){
        MTMUpdating = true;

        if (MTMFirstRun) {
            MTMgetFrames();
            if (MTMUseCookies) {
                MTMFetchCookie();
            }
        }

        if (MTMTrack) {
            MTMTrackedItem = MTMTrackExpand(menu);
        }

        if (MTMExpansion && MTMSubsAutoClose) {
            MTMCloseSubs(menu);
        }
        MTMLevel = 0;
        MTMDoc = parent.frames[MTMenuFrame].document
        MTMDoc.open("text/html", "replace");
        MTMOutputString = '<html>\n<head>\n';
        if (MTMLinkedSS) {
            MTMOutputString += '<link rel="stylesheet" type="text/css" href="' + MTMUA.preHREF + MTMSSHREF + '">\n';
        }
        else if (MTMUA.cssEnabled) {
            MTMOutputString += '<style type="text/css">\n';
            MTMOutputString += 'body {\n\tcolor:' + MTMTextColor + ';\nmargin-left: 0px;margin-top: 0px;\nmargin-right: 0px;\nmargin-bottom: 0px;\nfont-family:宋体, Arial;\n}\n';
            //MTMOutputString += 'root {\n\tcolor:' + MTMRootColor + ';\n\tbackground:transparent;\n\tfont-family:' + MTMRootFont + ';\n\tfont-size:' + MTMRootCSSize + ';\n}\n';
            MTMOutputString += 'a {\n\tfont-family:' + MTMenuFont + ';\n\tfont-size:' + MTMenuCSSize + ';\n\ttext-decoration:none;\n\tcolor:' + MTMLinkColor + ';\n\tbackground:transparent;\n}\n';
            MTMOutputString += MTMakeA('pseudo', 'hover', MTMAhoverColor);
            MTMOutputString += MTMakeA('class', 'tracked', MTMTrackColor);
            MTMOutputString += MTMakeA('class', 'subexpanded', MTMSubExpandColor);
            MTMOutputString += MTMakeA('class', 'subclosed', MTMSubClosedColor) + MTMExtraCSS + '\n<\/style>\n';
        }

        MTMOutputString += '<\/head>\n';
        MTMOutputString += '<script language="javascript">\n';
        MTMOutputString += 'function dolink(tempString){\n';

        var linkStr = '<div style="position:absolute; top:35%; left:35%;layer-background-color: #DDDDDD; z-index:2;">'
                + '<table width="250" height="120" border="0" cellpadding="0" cellspacing="0">'
                + '<tr><td height="21" bgcolor="#A4AEB5">&nbsp;</td></tr>'
                + '<tr><td bgcolor="#F1F1F1" align="center" style="color:#000066;">'
                + '处理中......'
                + '</td></tr><tr><td height="20" bgcolor="#A4AEB5">&nbsp;</td></tr></table></div>';
        +'<div style="position:absolute; top:0; left:0;z-index:1;"><table width=100% height=600></table></div>';


        MTMOutputString += "top.frames['mainFrame'].document.write('" + linkStr + "');\n";
        MTMOutputString += "top.frames['mainFrame'].document.location=tempString;";

        MTMOutputString += '}\n';
        MTMOutputString += '	var menuAppear = true;\n';
        MTMOutputString += '	function menuMove(){\n';
        MTMOutputString += '		if(menuAppear){\n';
        MTMOutputString += '			parent.document.all("middle").cols="9,*";\n';
        MTMOutputString += '			document.all("menuDiv").style.display="none";\n';
        MTMOutputString += '			document.all("menuhandle").src="images/menu-images/splitter_r.gif";\n';
        MTMOutputString += '			menuAppear = false;\n';
        MTMOutputString += '		} else {\n';
        MTMOutputString += '			parent.document.all("middle").cols="180,*";\n';
        MTMOutputString += '			document.all("menuDiv").style.display="";\n';
        MTMOutputString += '			document.all("menuhandle").src="images/menu-images/splitter_l.gif";\n';
        MTMOutputString += '			menuAppear = true;\n';
        MTMOutputString += '		}\n';
        MTMOutputString += '	}\n';
        MTMOutputString += '<\/script>';
        MTMOutputString += '<body bottommargin="0" topmargin="0" leftmargin="0" rightmargin="0" align="right" ';

        if (MTMBackground != "") {
            MTMOutputString += 'background="' + MTMUA.preHREF + MTMenuImageDirectory + MTMBackground + '" ';
        }

        MTMOutputString += 'bgcolor="' + MTMBGColor + '" text="' + MTMTextColor + '" link="' + MTMLinkColor + '" vlink="' + MTMLinkColor + '" alink="' + MTMLinkColor + '">\n';


        MTMOutputString += MTMHeader + '<table height="100%" border="0" cellpadding="0" cellspacing="0" align="right"><tr><td id="menutbltd" valign="top">\n<div id="menuDiv" style="display:">\n<table border="0" cellpadding="0" cellspacing="0">\n';
        MTMOutputString += '<tr height="23" valign="top"><td nowrap width="162">';
        MTMOutputString += '<font size="' + MTMRootFontSize + '" face="' + MTMRootFont + '" color="' + MTMRootColor + '">' + MTMenuText + '<\/font>';

        MTMDoc.writeln(MTMOutputString + '<\/td>\n<\/tr>');
        // Output Menu Tree and items:
        MTMListItems(menu);

        //MTMDoc.writeln('<\/table>'+MTMFooter+'\n<\/body>\n<\/html>');
        MTMDoc.writeln('<\/table>\n<\/div>\n</td><td align="right" width="9" background="images/menu-images/splitter_bg.gif"><img id="menuhandle" src="images/menu-images/splitter_l.gif" onclick="menuMove()" style="cursor:hand;"/></td></tr></table>' + MTMFooter + '\n<\/body>\n<\/html>');
        //MTMDoc.writeln('<\/table>\n</td></tr></table>'+MTMFooter+'\n<\/body>\n<\/html>');
        MTMDoc.close();

        if (MTMUA.browserType == "NN5") {
            parent.frames[MTMenuFrame].scrollTo(0, 0);
        }

        if ((MTMClickedItem || MTMTrackedItem) && MTMUA.browserType != "NN3" && !MTMFirstRun) {

            MTMItemName = "sub" + (MTMClickedItem ? MTMClickedItem : MTMTrackedItem);

            if (document.layers && parent.frames[MTMenuFrame].scrollbars) {
                MTMyval = parent.frames[MTMenuFrame].document.anchors[MTMItemName].y;
                MTMWinSize = parent.frames[MTMenuFrame].innerHeight;
            } else if (MTMUA.browserType != "O5") {
                if (MTMUA.browserType == "NN5") {
                    parent.frames[MTMenuFrame].document.all = parent.frames[MTMenuFrame].document.getElementsByTagName("*");
                }
                MTMyval = MTMGetYPos(parent.frames[MTMenuFrame].document.all[MTMItemName]);
                MTMWinSize = MTMUA.browserType == "NN5" ? parent.frames[MTMenuFrame].innerHeight : parent.frames[MTMenuFrame].document.body.offsetHeight;
            }

            if (MTMyval > (MTMWinSize - 60)) {
                parent.frames[MTMenuFrame].scrollBy(0, parseInt(MTMyval - (MTMWinSize * 1 / 3)));
            }
        }

        if (!MTMFirstRun && MTMUA.cookieEnabled) {
            if (MTMCookieString != "") {
                setCookie(MTMCookieName, MTMCookieString.substring(0, 4000), MTMCookieDays);
            } else {
                setCookie(MTMCookieName, "", -1);
            }
        }

        MTMFirstRun = false;
        MTMClickedItem = false;
        MTMExpansion = false;
        MTMTrack = false;
        MTMCookieString = "";
    }

    MTMUpdating = false;
}

function MTMListItems(menu) {

    var i, isLast;
    for (i = 0; i < menu.items.length; i++) {
        MTMIndices[MTMLevel] = i;
        isLast = (i == menu.items.length - 1);
        MTMDisplayItem(menu.items[i], isLast);

        if (menu.items[i].submenu && menu.items[i].expanded) {
            MTMBar[MTMLevel] = (isLast) ? false : true;
            MTMLevel++;
            MTMListItems(menu.items[i].submenu);
            MTMLevel--;
        } else {
            MTMBar[MTMLevel] = false;
        }
    }
}

function MTMDisplayItem(item, last) {

    var i, img;
    var MTMfrm = "parent.frames['code']";
    var MTMref = '.menu.items[' + MTMIndices[0] + ']';

    if (MTMLevel > 0) {
        for (i = 1; i <= MTMLevel; i++) {
            MTMref += ".submenu.items[" + MTMIndices[i] + "]";
        }
    }

    if (MTMUA.cookieEnabled) {
        if (MTMFirstRun && MTMCookieString != "") {
            item.expanded = (MTMCookieString.charAt(MTMCookieCharNum++) == "1") ? true : false;
        } else {
            MTMCookieString += (item.expanded) ? "1" : "0";
        }
    }

    if (item.submenu) {
        var usePlusMinus = false;

        if (MTMSubsGetPlus.toLowerCase() == "always" || MTMEmulateWE) {
            usePlusMinus = true;
        } else if (MTMSubsGetPlus.toLowerCase() == "submenu") {
            for (i = 0; i < item.submenu.items.length; i++) {
                if (item.submenu.items[i].submenu) {
                    usePlusMinus = true;
                    break;
                }
            }
        }

        var MTMClickCmd = "return " + MTMfrm + ".MTMSubAction(" + MTMfrm + MTMref + ");";
        var MTMouseOverCmd = "parent.status='" + (item.expanded ? "Collapse " : "Expand ") + (item.text.indexOf("'") != -1 ? MTMEscapeQuotes(item.text) : item.text) + "';return true;";
        var MTMouseOutCmd = "parent.status=parent.defaultStatus;return true;";
    }

    // The following parts are used to generate Menu items and Subment items and their appearence:

    if (MTMLevel > 0 && MTMLevel < 2) {
        MTMOutputString = '<tr height="23" valign="top"><td nowrap  background="images/menu-images/menu_back_01.jpg">';
        //		for (i = 0; i < MTMLevel; i++) {
        //			MTMOutputString += (MTMBar[i]) ? MTMakeImage("menu_bar.gif") : MTMakeImage("menu_pixel.gif");
        MTMOutputString += MTMakeImage("menu_pixel.gif");
        //		}
    } else if (MTMLevel >= 2) {
        MTMOutputString = '<tr height="23" valign="top"><td nowrap  background="images/menu-images/menu_back_01.jpg">';
        MTMOutputString += MTMakeImage("menu_pixel.gif");
        MTMOutputString += MTMakeImage("menu_pixel.gif");
    } else {
        MTMOutputString = '<tr height="23" valign="top"><td nowrap  background="images/menu-images/menu_back_01.jpg">';
        // The 1st Level.
    }

    // Link & Image
    if (item.submenu && usePlusMinus) {
        if (item.url == "") {
            MTMOutputString += MTMakeLink(item, true, true, true, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
        } else {
            if (MTMEmulateWE) {
                MTMOutputString += MTMakeLink(item, true, true, false, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
            } else {
                if (!item.expanded) {
                    MTMOutputString += MTMakeLink(item, false, true, true, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
                } else {
                    MTMOutputString += MTMakeLink(item, true, true, false, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
                }
            }
        }
        //          if(item.expanded) {
        //               img = (last) ? "menu_corner_minus.gif" : "menu_tee_minus.gif";
        //          } else {
        //               img = (last) ? "menu_corner_plus.gif" : "menu_tee_plus.gif";
        //          }
        if (item.expanded) {
            img = (last) ? "folderOpen.gif" : "folderOpen.gif";
        } else {
            img = (last) ? "folderClosed.gif" : "folderClosed.gif";
        }
    } else {
        img = (last) ? "menu_pixel.gif" : "menu_pixel.gif";
    }
    //	MTMOutputString += "  ";
    MTMOutputString += MTMakeImage(img);

    if (item.submenu) {
        if (MTMEmulateWE && item.url != "") {
            MTMOutputString += '</a>' + MTMakeLink(item, true, true, true, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
        } else if (!usePlusMinus) {
            if (item.url == "") {
                MTMOutputString += MTMakeLink(item, true, true, true, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
            } else if (!item.expanded) {
                MTMOutputString += MTMakeLink(item, false, true, true, MTMClickCmd);
            } else {
                MTMOutputString += MTMakeLink(item, true, true, false, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
            }
        }
        //		img = (item.expanded) ? item.expandIcon : item.collapseIcon;
    } else {
        MTMOutputString += MTMakeLink(item, false, true, true);
    }
    //	MTMOutputString += "  ";

    if (item.submenu && item.url != "" && item.expanded && !MTMEmulateWE) {
        MTMOutputString += '</a>' + MTMakeLink(item, true, true, true, MTMClickCmd, MTMouseOverCmd, MTMouseOutCmd);
    }
    MTMOutputString += '&nbsp;' + item.text + ((MTMUA.browserType == "NN3" && !MTMLinkedSS) ? '</font>' : '') + '</a>';

    MTMDoc.writeln(MTMOutputString + '</td></tr>');
}

function MTMEscapeQuotes(myString) {

    var newString = "";
    var cur_pos = myString.indexOf("'");
    var prev_pos = 0;
    while (cur_pos != -1) {
        if (cur_pos == 0) {
            newString += "\\";
        } else if (myString.charAt(cur_pos - 1) != "\\") {
            newString += myString.substring(prev_pos, cur_pos) + "\\";
        } else if (myString.charAt(cur_pos - 1) == "\\") {
            newString += myString.substring(prev_pos, cur_pos);
        }
        prev_pos = cur_pos++;
        cur_pos = myString.indexOf("'", cur_pos);
    }
    return(newString + myString.substring(prev_pos, myString.length));
}

function MTMTrackExpand(thisMenu) {

    var i, targetPath, targetLocation;
    var foundNumber = false;
    for (i = 0; i < thisMenu.items.length; i++) {
        if (thisMenu.items[i].url != "" && MTMTrackTarget(thisMenu.items[i].target)) {
            targetLocation = parent.frames[thisMenu.items[i].target].location;
            targetPath = targetLocation.pathname + targetLocation.search;
            if (MTMUA.browserType == "IE4" && targetLocation.protocol == "file:") {
                var regExp = /\\/g;
                targetPath = targetPath.replace(regExp, "\/");
            }
            if (targetPath.lastIndexOf(thisMenu.items[i].url) != -1 && (targetPath.lastIndexOf(thisMenu.items[i].url) + thisMenu.items[i].url.length) == targetPath.length) {
                return(thisMenu.items[i].number);
            }
        }
        if (thisMenu.items[i].submenu) {
            foundNumber = MTMTrackExpand(thisMenu.items[i].submenu);
            if (foundNumber) {
                if (!thisMenu.items[i].expanded) {
                    thisMenu.items[i].expanded = true;
                    if (!MTMClickedItem) {
                        MTMClickedItem = thisMenu.items[i].number;
                    }
                    MTMExpansion = true;
                }
                return(foundNumber);
            }
        }
    }
    return(foundNumber);
}

function MTMCloseSubs(thisMenu) {

    var i, j;
    var foundMatch = false;
    for (i = 0; i < thisMenu.items.length; i++) {
        if (thisMenu.items[i].submenu && thisMenu.items[i].expanded) {
            if (thisMenu.items[i].number == MTMClickedItem) {
                foundMatch = true;
                for (j = 0; j < thisMenu.items[i].submenu.items.length; j++) {
                    if (thisMenu.items[i].submenu.items[j].expanded) {
                        thisMenu.items[i].submenu.items[j].expanded = false;
                    }
                }
            } else {
                if (foundMatch) {
                    thisMenu.items[i].expanded = false;
                } else {
                    foundMatch = MTMCloseSubs(thisMenu.items[i].submenu);
                    if (!foundMatch) {
                        thisMenu.items[i].expanded = false;
                    }
                }
            }
        }
    }
    return(foundMatch);
}

function MTMGetYPos(myObj) {

    return(myObj.offsetTop + ((myObj.offsetParent) ? MTMGetYPos(myObj.offsetParent) : 0));
}

function MTMCheckURL(myURL) {

    var tempString = "";
    if ((myURL.indexOf("http://") == 0) || (myURL.indexOf("https://") == 0) || (myURL.indexOf("mailto:") == 0) || (myURL.indexOf("ftp://") == 0) || (myURL.indexOf("telnet:") == 0) || (myURL.indexOf("news:") == 0) || (myURL.indexOf("gopher:") == 0) || (myURL.indexOf("nntp:") == 0) || (myURL.indexOf("javascript:") == 0)) {
        tempString += myURL;
    } else {
        tempString += MTMUA.preHREF + myURL;
    }
    //alert(tempString);
    if (tempString.indexOf('logout.jsp') != -1) {

    }else if(tempString.indexOf("query/common/qry_summary.jsp") != -1){//add by houcs
    
    }
	else if(tempString.indexOf("fcsort/fenlei/list.jsp") != -1){//add by houcs
	}
	else if(tempString.indexOf("fcsort/hangye/list.jsp") != -1){//add by houcs
	}
	else if(tempString.indexOf("fcsort/khjlcx/list.jsp") != -1){//add by houcs
	}
	else if(tempString.indexOf("fcsort/anterior/firstlist.jsp") != -1){//add by houcs
	}
	else if(tempString.indexOf("fcsort/stat/") != -1){
	}
	else {
        tempString = "javascript:dolink('" + tempString + "');"
    }
    return(tempString);
}

function MTMakeLink(thisItem, voidURL, addName, addTitle, clickEvent, mouseOverEvent, mouseOutEvent) {

    var tempString = '<a href="' + (voidURL ? 'javascript:;' : MTMCheckURL(thisItem.url)) + '" ';
    if (MTMUseToolTips && addTitle && thisItem.tooltip) {
        tempString += 'title="' + thisItem.tooltip + '" ';
    }
    if (addName) {
        tempString += 'name="sub' + thisItem.number + '" ';
    }
    if (clickEvent) {
        tempString += 'onclick="' + clickEvent + '" ';
    }
    if (mouseOverEvent && mouseOverEvent != "") {
        tempString += 'onmouseover="' + mouseOverEvent + '" ';
    }
    if (mouseOutEvent && mouseOutEvent != "") {
        tempString += 'onmouseout="' + mouseOutEvent + '" ';
    }
    if (thisItem.submenu && MTMClickedItem && thisItem.number == MTMClickedItem) {
        tempString += 'class="' + (thisItem.expanded ? "subexpanded" : "subclosed") + '" ';
    } else if (MTMTrackedItem && thisItem.number == MTMTrackedItem) {
        tempString += 'class="tracked"';
    }
    if (thisItem.target != "" && tempString.indexOf("logout.jsp") != -1) {
        tempString += 'target="' + thisItem.target + '" ';
    }
    if (thisItem.target != "" && tempString.indexOf("query/common/qry_summary.jsp") != -1) {//add by houcs
        tempString += 'target=_blank';
    }
	if (thisItem.target != "" && tempString.indexOf("fcsort/fenlei/list.jsp") != -1) {//add by houcs
        tempString += 'target=_blank';
    }
	if (thisItem.target != "" && tempString.indexOf("fcsort/hangye/list.jsp") != -1) {//add by houcs
        tempString += 'target=_blank';
    }
	if (thisItem.target != "" && tempString.indexOf("fcsort/khjlcx/list.jsp") != -1) {//add by houcs
        tempString += 'target=_blank';
    }
	if (thisItem.target != "" && tempString.indexOf("fcsort/anterior/firstlist.jsp") != -1) {//add by houcs
        tempString += 'target=_blank';
    }
    if(thisItem.target != "" && tempString.indexOf("fcsort/stat/") != -1){// zhengxin added in 20070531
		tempString += 'target=_blank';
	}
    return(tempString + '>');
}


// Noted by WangHaiLei: The function is used to transform the iamges to the same size and format.
function MTMakeImage(thisImage) {

    return('<img src="' + MTMUA.preHREF + MTMenuImageDirectory + thisImage + '" align="left" valign="middle" border="0" vspace="0" hspace="0" width="16" height="16">');
}

function MTMakeBackImage(thisImage) {

    var tempString = 'transparent url("' + ((MTMUA.preHREF == "") ? "" : MTMUA.preHREF);
    tempString += MTMenuImageDirectory + thisImage + '")'
    return(tempString);
}

function MTMakeA(thisType, thisText, thisColor) {

    var tempString = "";
    tempString += 'a' + ((thisType == "pseudo") ? ':' : '.');
    return(tempString + thisText + ' {\n\tcolor:' + thisColor + ';\n\tbackground:transparent;\n}\n');
}

function MTMTrackTarget(thisTarget) {

    if (thisTarget.charAt(0) == "_") {
        return false;
    } else {
        for (i = 0; i < MTMFrameNames.length; i++) {
            if (thisTarget == MTMFrameNames[i]) {
                return true;
            }
        }
    }
    return false;
}

// The MTMBrowser object.  A custom "user agent" that'll define the browser seen from the menu's point of view.

function MTMBrowser() {

    this.cookieEnabled = false;
    this.preHREF = "";
    this.MTMable = false;
    this.cssEnabled = true;
    this.browserType = "other";

    if (navigator.appName == "Netscape" && navigator.userAgent.indexOf("WebTV") == -1) {
        if (parseInt(navigator.appVersion) == 3 && (navigator.userAgent.indexOf("Opera") == -1)) {
            this.MTMable = true;
            this.browserType = "NN3";
            this.cssEnabled = false;
        } else if (parseInt(navigator.appVersion) >= 4) {
            this.MTMable = true;
            this.browserType = parseInt(navigator.appVersion) == 4 ? "NN4" : "NN5";
        }
    } else if (navigator.appName == "Microsoft Internet Explorer" && parseInt(navigator.appVersion) >= 4) {
        this.MTMable = true;
        this.browserType = "IE4";
    } else if (navigator.appName == "Opera" && parseInt(navigator.appVersion) >= 5) {
        this.MTMable = true;
        this.browserType = "O5";
    }

    if (this.browserType != "NN4") {
        this.preHREF = location.href.substring(0, location.href.lastIndexOf("/") + 1)
    }
}

// Noted by WangHaiLei:  The following three functions are about Cookie, and can not be deleted.

function MTMFetchCookie() {

    var cookieString = getCookie(MTMCookieName);
    if (cookieString == null) { // cookie wasn't found
        setCookie(MTMCookieName, "Say-No-If-You-Use-Confirm-Cookies");
        cookieString = getCookie(MTMCookieName);
        MTMUA.cookieEnabled = (cookieString == null) ? false : true;
        return;
    }

    MTMCookieString = cookieString;
    MTMUA.cookieEnabled = true;
}

// These are from Netscape's Client-Side JavaScript Guide.
// setCookie() is altered to make it easier to set expiry.
function getCookie(Name) {

    var search = Name + "="
    if (document.cookie.length > 0) { // if there are any cookies
        offset = document.cookie.indexOf(search)
        if (offset != -1) { // if cookie exists

            offset += search.length
            // set index of beginning of value
            end = document.cookie.indexOf(";", offset)
            // set index of end of cookie value
            if (end == -1)
                end = document.cookie.length
            return unescape(document.cookie.substring(offset, end))
        }
    }
}

function setCookie(name, value, daysExpire) {

    if (daysExpire) {
        var expires = new Date();
        expires.setTime(expires.getTime() + 1000 * 60 * 60 * 24 * daysExpire);
    }
    document.cookie = name + "=" + escape(value) + (daysExpire == null ? "" : (";expires=" + expires.toGMTString())) + ";path=/";
}

