
//*************PageEffect************************
function mOvr(src) {
	if (!src.contains(event.fromElement)) {
		dataBgColor = src.bgColor;
		src.style.cursor = "hand";
		src.bgColor = "#E7E9CF";
	}
}
function mOut(src) {
	if (!src.contains(event.toElement)) {
		src.style.cursor = "default";
		src.bgColor = "#FFFFFF";
	}
}
//****************GoPage***********************************
function goPage(sort) {
			var currpage=parseInt(document.all.currpage.value);
			var maxpage=parseInt(document.all.maxpage.value);
			var pageer =parseInt(document.all.pageer.value);	
			//alert("currpage"+currpage +"maxpage="+maxpage+"="+ pageer);
			//alert("sum="+(currpage+maxpage+pageer));
			if (sort == "top") {
				document.all.currpage.value = 1;
			}
			if (sort == "bottom") {
				document.all.currpage.value = maxpage;
			}
			if (sort == "up") {
				document.all.currpage.value = parseInt(parseInt(currpage) - 1);
			}
			if (sort == "down") {
				document.all.currpage.value = parseInt(parseInt(currpage) + 1);
			}
			if(currpage>maxpage||currpage=="")
			{	//alert("currpage"+currpage +"maxpage="+maxpage+"="+ pageer);
				window.alert("请输入正确的页码！");
				document.all.currpage.value =pageer ;
				
				//alert(document.all.currpage.value+currpage);
				return false;
			}
			else
			{
				document.all.listfrom.submit();
			}
			
		}
//**************************************	
	function showLoanInfo(bmno)
		{
			var url = basePath+"fcsort/common/loanInfoA.jsp?bmno="+bmno;
			var st="status=yes,toolbar=yes,menubar=no,location=no,scrollbars=yes,resizable=yes";
			window.open(url,"123",st);
			
		}	
