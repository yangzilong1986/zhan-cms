function checkSubmit() {
    return true;
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
            return false;
        }
        else {
            if (tmpchar == ".") {
                j++;
                //ֻ����һ��.
                if (j > 1) return false;
                //.��Ӧ��������
                //if (i == (p_obj.value.length - 1)) return false;
            }
            if (i > 0 && tmpchar == "-") {
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

function chkdecWithAlStr(str, alstr) {//obj name
    var obj = document.getElementsByName(str)[0];
    if (!checkDec(obj)) {
        if (alstr == null)alstr = "���������������!";
        alert(alstr);
        obj.focus();
        obj.select();
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

function countTotalAmt() {//�Զ������ܻ����
    var obj1 = document.getElementsByName("CCRPNOWAMT")[0];
    var obj2 = document.getElementsByName("CCDIVIDAMT")[0];
    var obj3 = document.getElementsByName("CCRPTOTALAMT")[0];

    if (obj1.value != null && chkdec("CCRPNOWAMT")) {
        var countamt = 0;
        if (obj2.value != null && chkdec("CCDIVIDAMT")) {
            countamt = obj1.value - (-obj2.value);
        } else countamt = obj1.value;
        obj3.value = countamt;
    }
}

function countNowAndTotalAmt() {//�Զ������ܻ����
    var obj1 = document.getElementsByName("CCDYRPMON")[0];
    var obj2 = document.getElementsByName("CCXYRPMON")[0];
    var obj3 = document.getElementsByName("CCCDRPMON")[0];
    var obj4 = document.getElementsByName("CCRPNOWAMT")[0];
    var obj5 = document.getElementsByName("CCDIVIDAMT")[0];
    var obj6 = document.getElementsByName("CCRPTOTALAMT")[0];
    var obj7 = document.getElementsByName("CCRPRATE")[0];
    var obj8 = document.getElementsByName("CONFMONPAY")[0];

    if (obj1.value == null)obj1.value = 0;
    if (obj2.value == null)obj2.value = 0;
    if (obj3.value == null)obj3.value = 0;

    if (obj1.value != null && chkdec("CCDYRPMON")) {
        if (obj2.value != null && chkdec("CCXYRPMON")) {
            if (obj3.value != null && chkdec("CCCDRPMON")) {
                obj4.value = (obj1.value * 100 - (-obj2.value * 100) - (-obj3.value * 100)) / 100;
                if (obj5.value != null && chkdec("CCDIVIDAMT")) {
                    obj6.value = (obj4.value * 100 - (-obj5.value * 100)) / 100;
                    obj7.value = (obj6.value * 10000 / (obj8.value * 100)).toFixed(2);
                }
            }
        }
    }
}