function banli(a, b, c, d, e, f, g, h, i) {
    var FCNO = a;
    var BMNO = b;
    var BRHID = c;
    var OPERBRHID = d;
    var FCCRTTYPE = e;
    var FCTYPE = f;
    var FCSTATUS = g;
    var BMTYPE = h;
    var CLIENTTYPE = i;
    if (FCSTATUS == "1") {
        window.open("/fcworkbench/one.jsp?FCNO=" + FCNO + "&FCTYPE=" + FCTYPE + "&FCSTATUS=" + FCSTATUS, "", "width=800,height=600,left=120,top=50,toolbar=no,scrollbars=yes,resizable=yes");

    }
    if (FCSTATUS == "2") {
        window.open("/fcworkbench/two.jsp?FCNO=" + FCNO + "&FCTYPE=" + FCTYPE + "&FCSTATUS=" + FCSTATUS, "", "width=800,height=600,left=120,top=50,toolbar=no,scrollbars=yes,resizable=yes");

    }
    if (FCSTATUS == "3") {
        window.open("/fcworkbench/three.jsp?FCNO=" + FCNO + "&FCTYPE=" + FCTYPE + "&FCSTATUS=" + FCSTATUS, "", "width=800,height=600,left=120,top=50,toolbar=no,scrollbars=yes,resizable=yes");
    }
}
//金钱类型检查
function isDigit(theNum)
{
    var theMask = "0123456789,.-";
    var temp;
    for (i = 0; i < theNum.length; i++)
    {
        tmpn = theNum.substring(i, i + 1);
        if (theNum.indexOf(".") != -1) {
            temp = theNum.substring(0, theNum.length - 3);
            if (temp.length > 12)return false;
        }
        else {
            temp = theNum;
            if (temp.length > 12)return false;
        }
        if (theMask.indexOf(tmpn) == -1) return false;
        if (theNum.indexOf('.') != theNum.lastIndexOf('.'))
            return false;
        tempLen = theNum.indexOf("-");
        if (tempLen != -1) {
            strLen = theNum.substring(0, 1);
            if (strLen != "-") {
                return false;

            }
        }
    }
    return true;
}
//两个日期之间相减str1-str2
function checkDateGreat(str1, str2)
{
    var year1 = parseInt(str1.substring(0, 4), 10);
    var month1 = parseInt(str1.substring(4, 6), 10);
    var day1 = parseInt(str1.substring(6, 8), 10);

    date1 = new Date(year1, month1, day1);

    var year2 = parseInt(str2.substring(0, 4), 10);
    var month2 = parseInt(str2.substring(4, 6), 10);
    var day2 = parseInt(str2.substring(6, 8), 10);

    date2 = new Date(year2, month2, day2);

    if (date1 >date2)
        return true;
    else
        return false;

}
//ctrl+enter提交
/*function ctlkey(){
    //
    if((event.ctrlKey && window.event.keyCode==13)){
    	//alert("key down");
        //pressSaveButton(winform.Plat_Form_Request_Event_ID,2);
		
	//if ( document.all.addbtn != "undefined" ) {
	//		document.all.addbtn.click();
	//	}
   //if ( document.all.searchbtn != "undefined" ) {
	//		document.all.searchbtn.click();
	//	}
   if ( document.all.savebtn != "undefined" ) {
			document.all.savebtn.click();
		}
    }    
    //if(event.ctrlKey && window.event.keyCode==13){submitonce(document.form1);document.form1.submit();}
    //if(event.altKey && (window.event.keyCode==83 || window.event.keyCode==115)){submitonce(document.form1);document.form1.submit();}
}
var ie = (document.all)? true:false
if (ie)
{
    window.document.onkeydown=ctlkey;
}*/