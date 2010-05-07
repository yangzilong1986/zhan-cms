/******************************�ַ�������������******************************/
// Trim() , Ltrim() , RTrim() ����
String.prototype.Trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.LTrim = function() {
    return this.replace(/(^\s*)/g, "");
}
String.prototype.RTrim = function() {
    return this.replace(/(\s*$)/g, "");
}
// Trim() , Ltrim() , RTrim() ����
function Trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
function LTrim(p_str) {
    var str = p_str;
    if (str.charAt(0) == " ") {
        str = str.slice(1);
        str = LTrim(str);
    }
    return str;
}
function RTrim(str) {
    return str.replace(/(\s*$)/g, "");
}
/******************************************************************************/

/*************************�������ύǰ��Ч�Լ�麯��*************************/

//�ж��Ƿ��ǿ�
function isEmpty(p_obj) {
    var obj = eval(p_obj);
    if (LTrim(obj.value) == "") {
        return true;
    }
    return false;
}
//�жϳ���
function checkMaxLength(p_obj, p_maxlength) {
    if (!p_obj) return false;
    //empty�򲻼��
    if (isEmpty(p_obj)) return false;
    var txtlength = p_obj.value.length;
    var rellength = 0;
    for (var i = 0; i < txtlength; i++) {
        if (escape(p_obj.value.charAt(i)).length == 6) {
            rellength += 2;
        }
        else {
            rellength += 1;
        }
    }
    if (rellength > parseInt(p_maxlength)) {
        return false;
    }
    return true;
}

//�ж��Ƿ���int����,����Ϊ����
function checkInteger(p_obj) {
    var strref = "1234567890-";
    if (isEmpty(p_obj)) return false;
    if (LTrim(p_obj.value) == '-') {
        return false;
    }
    for (i = 0; i < p_obj.value.length; i++) {
        var tmpchar = p_obj.value.substring(i, i + 1);
        if (strref.indexOf(tmpchar, 0) == -1) {
            return false;
        }
        if (i > 0 && tmpchar == "-") {
            return false;
        }
    }
    return true;
}
//�ж��Ƿ���decimal����,���ֿ���Ϊ����
function checkDecimal(p_obj) {
    var strref = "1234567890-.";
    if (isEmpty(p_obj)) return false;
    if (p_obj.value == '-') return false;
    if (p_obj.value == '.') return false;
    var j = 0;
    for (i = 0; i < p_obj.value.length; i++) {
        var tmpchar = p_obj.value.substring(i, i + 1);
        if (strref.indexOf(tmpchar, 0) == -1) {
            return false;
        }
        else {
            if (tmpchar == ".") {
                j++;
                //ֻ����һ��.
                if (j > 1) return false;
                //.��Ӧ��������
                if (i == (p_obj.value.length - 1)) return false;
            }
            if (i > 0 && tmpchar == "-") {
                return false;
            }
        }
    }
    return true;
}
//�ж��Ƿ�����
function isLeapYear(year) {
    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
        return true;
    }
    return false;
}
//�ж������Ƿ���ȷ
function checkDate(p_obj) {
    var datetime;
    if (isEmpty(p_obj))return false;
    datetime = Trim(p_obj.value);
    checkDate_str(datetime);
    return true;
}

//�ж������Ƿ���ȷ
function checkDate_str(datetime) {
    var year,month,day;

    datetime = Trim(datetime);
    if (datetime.length == 0)return false;

    //����8λ���
    if (datetime.length != 8) {
        return false;
    }
    year = datetime.substring(0, 4);
    if (isNaN(year) == true) {
        return false;
    }
    month = datetime.substring(4, 6);
    if (isNaN(month) == true) {
        return false;
    }
    day = datetime.substring(6, 8);
    if (isNaN(day) == true) {
        return false;
    }
    if (month < 1 || month > 12) {
        return false;
    }
    if (day < 1 || day > 31) {
        return false;
    }
    else {
        if (month == 2) {
            if (isLeapYear(year) && day > 29) {
                return false;
            }
            if (!isLeapYear(year) && day > 28) {
                return false;
            }
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && (day > 30)) {
            return false;
        }
    }
    return true;
}

//�ж�ֻ�����µ������Ƿ���ȷ           ��� sunzg  05-5-17
function checkDate2(p_obj) {
    var datetime;
    var year,month;
    if (isEmpty(p_obj))return false;
    datetime = Trim(p_obj.value);
    //����8λ���
    if (datetime.length != 6) {
        return false;
    }
    year = datetime.substring(0, 4);
    if (isNaN(year) == true) {
        return false;
    }
    month = datetime.substring(4, 6);
    if (isNaN(month) == true) {
        return false;
    }

    if (month < 1 || month > 12) {
        return false;
    }
    return true;
}

//�ж��������ڵ��Ⱥ��ϵ
function compTwinDate(val_bgndt, val_enddt) {
    if (val_bgndt.Trim() == '' || val_enddt.Trim() == '') {
        return true;
    }
    if (val_bgndt >= val_enddt) {
        return false;
    }
    return true;
}

//�������ֹ���麯������formDataAutoCheck������ÿ������д�˺���ʵ���Լ����߼�
//ע�⣺ÿ������д�˺���ʱλ��һ��������main.js����
function formDataHandCheck() {
    return true;
}
//�������Զ�������������ɰ�ť��Ӧ�¼�����
function formDataAutoCheck(p_frmnm) {
    var frmobj = document.all[p_frmnm];
    var allname;
    for (var i = 0; i < frmobj.length; i++) {
        var obj = frmobj.item(i);
        allname = obj.name;
        //ֻ�����Ͳ����
        if (obj.getAttribute("readonly")) continue;
        //nulls
        if (obj.getAttribute("nulls") == "n") {
            if (isEmpty(obj)) {
                alert("�������Ϊ��!");
                obj.focus();
                return false;
            }
        }
        //not empty
        if (!isEmpty(obj)) {
            //char
            if (obj.getAttribute("coltype") == "char") {
                var maxlen = obj.getAttribute("maxlength");
                if (maxlen == null || maxlen.length < 1) continue;
                if (!checkMaxLength(obj, maxlen)) {
                    alert("���ȳ��������Ƴ��ȣ�");
                    obj.focus();
                    return false;
                }
            }
            //int
            else if (obj.getAttribute("coltype") == "int") {
                if (!checkInteger(obj)) {
                    alert("���������������!");
                    obj.focus();
                    return false;
                }
            }
            //deci
            else if (obj.getAttribute("coltype") == "deci") {
                    if (!checkDecimal(obj)) {
                        alert("���������������!");
                        obj.focus();
                        return false;
                    }
                }
                //date
                else if (obj.getAttribute("coltype") == "date") {
                        if (!checkDate(obj)) {
                            alert("���ڸ�ʽ�������!");
                            obj.focus();
                            return false;
                        }
                    }
                    //date2
                    else if (obj.getAttribute("coltype") == "date2") {
                            if (!checkDate2(obj)) {
                                alert("�·ݸ�ʽ�������!");
                                obj.focus();
                                return false;
                            }
                        }
        }
    }
    //�����ֹ���鲿��
    return formDataHandCheck();
}
/******************************************************************************/

/*************************ҳ����ϵͳ���ܰ�ť����Ӧ����*************************/
//�ڵ���ύ�����¡�ɾ����ˢ�°�ťʱ��ʹ���еİ�ťʧЧ����ֹ����������ظ����
function DisableAllButtion() {
    if (document.form1) {
        for (var i = 0; i < document.form1.elements.length; i++) {
            var elem = document.form1.elements[i];
            if (elem.type.toLowerCase() == "button")
                elem.disabled = true;
        }
    }
    if (document.all("btn_add")) document.all("btn_add").disabled = true;
    if (document.all("btn_mod")) document.all("btn_mod").disabled = true;
    if (document.all("btn_del")) document.all("btn_del").disabled = true;
    if (document.all("btn_fsh")) document.all("btn_fsh").disabled = true;
    if (document.all("btn_ext")) document.all("btn_ext").disabled = true;
    if (document.all("btn_sav")) document.all("btn_sav").disabled = true;
    if (document.all("btn_cmt")) document.all("btn_cmt").disabled = true;
    if (document.all("btn_prt")) document.all("btn_prt").disabled = true;
    if (document.all("SystemButton1")) document.all("SystemButton1").disabled = true;
    if (document.all("SystemButton2")) document.all("SystemButton2").disabled = true;
    if (document.all("SystemButton3")) document.all("SystemButton3").disabled = true;
    if (document.all("SystemButton4")) document.all("SystemButton4").disabled = true;
    if (document.all("SystemButton5")) document.all("SystemButton5").disabled = true;
    if (document.all("SystemButton6")) document.all("SystemButton6").disabled = true;
}
//�ύ��ť����¼�
function btn_add_click() {
    if (!formDataAutoCheck('form1')) return false;
    document.all("act").value = "add";
    document.all("MessageDiv").innerText = "��������У���ȴ�...";
    DisableAllButtion();
    document.all("form1").submit();
    return true;
}

//���°�ť����¼�
function btn_mod_click() {
    if (!formDataAutoCheck('form1')) return false;
    document.all("act").value = "mod";
    document.all("MessageDiv").innerText = "���ݸ����У���ȴ�...";
    DisableAllButtion();
    document.all("form1").submit();
    return true;
}
//ɾ����ť����¼�
function btn_del_click() {
    if (!confirm("��ȷ��Ҫɾ����")) {
        return false;
    }
    document.all("act").value = "del";
    document.all("MessageDiv").innerText = "����ɾ���У���ȴ�...";
    DisableAllButtion();
    document.all("form1").submit();
    return true;
}
//���水ť����¼�
function btn_sav_click() {
    if (!formDataAutoCheck('form1')) return false;
    document.all("MessageDiv").innerText = "���ݱ����У���ȴ�...";
    DisableAllButtion();
    document.all("form1").submit();
    return true;
}
//�ύ��ť����¼�
function btn_cmt_click() {
    if (!confirm("��ȷ��Ҫ�ύ��")) {
        return false;
    }
    if (!formDataAutoCheck('form1')) return false;
    document.all("MessageDiv").innerText = "�����ύ�У���ȴ�...";
    if (document.all("iscmt")) {
        document.all("iscmt").value = "1";
    }
    DisableAllButtion();
    document.all("form1").submit();
    return true;
}
//ˢ�°�ť����¼�
function btn_fsh_click() {
    DisableAllButtion();
    window.location.reload();
}
//�˳���ť����¼�
function btn_ext_click() {
    window.close();
}
//��һ���´���
function openNewWindow(url) {
    var xx = (window.screen.width - 600) / 2;
    var yy = (window.screen.height - 450) / 2;
    window.open(url, '', 'left=' + xx + ',top=' + yy + ',height=450, width=600,toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no');
}

function open5sortWindow(url) {
    var xx = (window.screen.width - 600) / 2;
    var yy = (window.screen.height - 450) / 2;
    window.open(url, '', 'left=' + xx + ',top=' + yy + ',height=500, width=800,toolbar=no, menubar=no, scrollbars=yes,resizable=yes,location=no, status=no');
}
function openNewWindowByTitle(url, wid, hgt, title) {
    if (wid == undefined) wid = 600;
    if (hgt == undefined) hgt = 450
    var xx = (window.screen.width - wid) / 2;
    var yy = ((window.screen.height - 28) - hgt) / 2;
    if (xx < 0) xx = 0;
    if (yy < 0) yy = 0;
    //20050425��Ϊ����ʷ����ʱ��������
    //window.open (url, title, 'left='+xx+',top='+yy+',width='+width+',height='+height+', toolbar=no, menubar=no, scrollbars=yes,resizable=no,location=no, status=no');
    window.open(url, '', 'left=' + xx + ',top=' + yy + ',width=' + wid + ',height=' + hgt + ', toolbar=no, menubar=no, scrollbars=yes,resizable=no,location=no, status=no');
}
function openNewWindow1(url, width, height) {
    openNewWindowByTitle(url, width, height, "new1");
}
function openNewWindow2(url, width, height) {
    openNewWindowByTitle(url, width, height, "new2");
}
function openNewWindow3(url, width, height) {
    openNewWindowByTitle(url, width, height, "new3");
}
function openNewWindow4(url, width, height) {
    openNewWindowByTitle(url, width, height, "new4");
}
//�������մ���
function openRefWindow(url) {
    window.showModalDialog(url, window, "dialogWidth=500px;dialogHeight=540px;help=no;status=no;edge=raised");
}
/******************************************************************************/
/**
 * ����ִ������ ��java script ��ʽ
 * 2005-04-02  sunzg add
 * @param baserate ��������
 * @param floatrate ������ʸ�������
 * @return
 */
function getExecRate(baserate, floatrate) {
    var x = parseFloat(baserate);
    var y = parseFloat(floatrate);
    var z = x * (y / 100 + 1);
    return z.toFixed(4);
}

//�û��޸�������
function checkPWD(obj1, obj2) {
    if (obj1.value.length <= 0) {
        alert("�û����벻��Ϊ�գ�");
        obj1.focus();
        obj1.select();
        return false;
    }
    else if (obj2.value.length <= 0) {
        alert("ȷ�����벻��Ϊ�գ�");
        obj2.focus();
        obj2.select();
        return false;
    }
    else if (obj1.value != obj2.value) {
            alert("�û������ȷ�����벻��ͬ��");
            obj2.focus();
            obj2.select();
            return false;
        }
    return true;
}


//�ύ����ҳ��,����ʹ��referѡ����Ϣ������������
function selectDo() {
    document.all.form1.action = document.all.url.value;
    document.all.form1.submit();
}

/*
 * loadʱ�����㶨λ��form�е�ָ���ֶ�
 *
 * lj added in 20090409
 */
function setLoadFocus(obj) {
    document.forms[0].onload = setFocus(obj);
}

function setFocus(obj) {
    document.getElementsByName(obj)[0].focus();
}

//lj added in 20090409

//�ж��Ƿ���decimal����,���ֿ���Ϊ����,����Ϊ��
function checkDec(p_obj) {
    var strref = "1234567890-.";
    //    if (isEmpty(p_obj)) return false;
    if (p_obj.value == '-') return false;
    if (p_obj.value == '.') return false;
    var j = 0;
    for (var i = 0; i < p_obj.value.length; i++) {
        var tmpchar = p_obj.value.substring(i, i + 1);
        if (strref.indexOf(tmpchar, 0) == -1) {
            p_obj.value=parseFloat(p_obj.value);
            return false;
        }
        else {
            if (tmpchar == ".") {
                j++;
                //ֻ����һ��.
                if (j > 1) {
                    p_obj.value=parseFloat(p_obj.value);
                    return false;
                }
                //.��Ӧ��������
                //if (i == (p_obj.value.length - 1)) return false;
                if (isNaN(parseFloat(p_obj.value))){
                    p_obj.value=parseFloat(p_obj.value);
                    return false;
                }
            }
            if (i > 0 && tmpchar == "-") {
                p_obj.value=parseFloat(p_obj.value);
                return false;
            }
        }
    }
    return true;
}

//�ж��Ƿ���int����,����Ϊ����,����Ϊ��
function checkInt(p_obj) {
    var strref = "1234567890-";
    //    if (isEmpty(p_obj)) return false;
    if (LTrim(p_obj.value) == '-') {
        return false;
    }
    for (i = 0; i < p_obj.value.length; i++) {
        var tmpchar = p_obj.value.substring(i, i + 1);
        if (strref.indexOf(tmpchar, 0) == -1) {
            return false;
        }
        if (i > 0 && tmpchar == "-") {
            return false;
        }
    }
    return true;
}

//�ж��Ƿ���Phone����,����Ϊ��
function checkPhone(p_obj) {
    var strref = "1234567890-";
    if (p_obj.value == '-') return false;
    var j = 0;
    for (var i = 0; i < p_obj.value.length; i++) {
        var tmpchar = p_obj.value.substring(i, i + 1);
        if (strref.indexOf(tmpchar, 0) == -1) {
            return false;
        }
        else if (tmpchar == "-" && (i == 0 || i == p_obj.value.length-1)) {
            return false;
        }
    }
    return true;
}

function chkdecWithAlStr(str, alstr) {//obj name
    var obj = document.getElementsByName(str)[0];
    if (!checkDec(obj)) {
        if (alstr == null)alstr = "���������������!";
        alert(alstr);
        obj.focus();
        return false;
    }
    return true;
}

function chkintWithAlStr(str, alstr) {//obj name
    var obj = document.getElementsByName(str)[0];
    if (!checkInt(obj)) {
        if (alstr == null)alstr = "���������������!";
        alert(alstr);
        obj.focus();
        return false;
    }
    return true;
}

function chkdec(str) {//obj name
    return chkdecWithAlStr(str, null);
}
function chkint(str) {//obj name
    return chkintWithAlStr(str, null);
}

/**
 * ���ؼ���IE��FF��event
 */
function getEvent() {
    if (document.all) {
        return window.event;//�����ie
    }
    var func = getEvent.caller;
    while (func != null) {
        var arg0 = func.arguments[0];
        if (arg0) {
            if ((arg0.constructor == Event || arg0.constructor == MouseEvent)
                    || (typeof(arg0) == "object" && arg0.preventDefault && arg0.stopPropagation)) {
                return arg0;
            }
        }
        func = func.caller;
    }
    return null;
}
/**
 * ���ؼ���IE��FF��element
 */
function getElement() {
    var evt = getEvent();
    var element = evt.srcElement || evt.target;
    return element;
}