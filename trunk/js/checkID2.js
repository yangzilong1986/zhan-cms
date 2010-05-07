/*****************************************************************
 * name     :checkTextNumber()
 * function :����Ƿ�Ϊ����
 * parameter:@theObj  Ҫ�����ַ�
 *           @isnull  true ����      false ����
 * return   :trueΪȫ�����֣�falseΪ��������
 ******************************************************************/
function checkTextNumber(theObj, isnull)
{
    var checkOK = "0123456789";
    var checkStr = theObj.value;
    var allValid = true;
    if (theObj.length == 0)
    {
        if (isnull == true)
            return true;
        else
            return false;
    }
    for (i = 0; i < checkStr.length; i++)
    {
        ch = checkStr.charAt(i);
        for (j = 0; j < checkOK.length; j++)
        {
            if (ch == checkOK.charAt(j))
                break;
            if (i == 17)
            {
                if (ch == "X" || ch == "x")
                    break;
            }
        }
        if (j == checkOK.length)
        {
            allValid = false;
            break;
        }
    }
    if (!allValid)
    {
        alert("���ִ�ֻ�ܰ�������,X,x�������Ƿ������˷Ƿ��ַ���");
        theObj.focus();
        return false;
    }
    return true;
}

//lj added in 20090307
function checkIDCard(tpObj, idObj) {
    if (tpObj.value == 0) {
        var id = idObj.value;
        if (id.Trim().length <= 0) return true;//lj added in 20090402 ������������룬��У��
        //        if (id.Trim().length <= 0) {
        //            alert("�����������֤����!");
        //            idObj.focus();
        //            return false;
        //        }

        id = id.Trim();
        if (id.length != 15 && id.length != 18) {
            alert("���֤���볤�ȱ���Ϊ15��18λ!");
            idObj.focus();
            //idObj.value = "";
            return false;
        }

        if (id.length == 15) {
            if (!checkDate_str("19" + id.substring(6, 12))) {
                alert("֤���������ڸ�ʽ�Ƿ�!");
                idObj.focus();
                return false;
            }
        }
        else {
            if (id.charAt(17) == 'x') {
                alert("֤�������У��λ������Сдx!");
                idObj.value = "";
                idObj.focus();
                return false;
            }

            if (checkDate_str(id.substring(6, 14)) == false) {
                alert("֤���������ڸ�ʽ�Ƿ�!");
                idObj.focus();
                return false;
            }

            var iS = 0,iY;
            var iW = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
            var szVerCode = "10X98765432";


            for (var i = 0; i < 17; i++) {
                iS += (id.charAt(i) - '0') * iW[i];
            }
            iY = iS % 11;
            if (szVerCode.charAt(iY) != id.charAt(i)) {
                alert("���֤����У�������!");
                idObj.focus();
                idObj.value = "";
                return false;
            }
        }
    }
    return true;
}

/*
 ����֤������õ�����
 lj added in 20090409
 */
function getbirthday(idval, birthdayname) {
    var obj = document.getElementById(birthdayname);
    if (idval.length == 15)obj.value = "19" + idval.substring(6, 12);
    else if (idval.length == 18)obj.value = idval.substring(6, 14);
}


function checkName(theObj) {
    var name = theObj.value;
    if (name.Trim().length <= 0) {
        alert("�������û�����!");
        theObj.focus();
        return false;
    }

    name = name.Trim();
    if (name.length < 2) {
        alert("��������ʵ�û�����!");
        theObj.focus();
        //theObj.value = "";
        return false;
    }
    return true;
}

function checkPass(theObj) {
    var pass = theObj.value;
    if (pass.Trim().length <= 0) {
        alert("�������½����!");
        theObj.focus();
        return false;
    }

    pass = pass.Trim();
    if (pass.length < 6) {
        alert("������������6λ!");
        theObj.focus();
        //theObj.value = "";
        return false;
    }
    return true;
}
/*****************************************************************
 * name     :checkLength()
 * function :����ַ��ĳ���
 * parameter:@len     �ַ�
 * return   :sum      �ַ��ĳ���
 ******************************************************************/
function checkLength(len)
{
    var i,sum;
    sum = 0;
    for (i = 0; i < len.length; i++)
    {
        if ((len.charCodeAt(i) >= 0) && (len.charCodeAt(i) <= 255) && (len.charAt(i) != " "))
            sum = sum + 1;
        else if (len.charAt(i) == " ")
            sum = sum;
        else
            sum = sum + 2;
    }
    return sum;
}
/*****************************************************************
 * name     :checkZJHM()
 * function :���֤�������Ƿ��׼
 * parameter:@idtype     Ҫ����֤������
 *           @id         Ҫ����֤������
 * return   :trueΪ���ϣ�falseΪ������
 ******************************************************************/
function checkZJHM(idtype, id)
{
    var nLength;
    if (idtype.options[idtype.selectedIndex].text == "���֤") {
        nLength = checkLength(id.value);
        if (nLength != 15 && nLength != 18) {
            alert("��ȷ�����֤�����Ƿ�Ϊ15λ��18λ");
            id.focus();
            return false;
        } else {
            return checkTextNumber(id, false);
        }
    }
    return true;
}

function checkSubmit()
{
    //alert("test");
    //alert(document.all.LAWPERSONIDTYPE.value);
    //if(document.all.CLIENTTYPE.value=='6')
    //{
    //	if(document.all.ICBREGCODE.value==''){
    //	  infoAlert(document.all.ICBREGCODE,"Ӫҵִ�պŲ�����Ϊ��");
    //	  return false;
    //	}
    //        if(document.all.CORPRESP.value==''){
    //	  infoAlert(document.all.CORPRESP,"�����˲�����Ϊ��");
    //	  return false;
    //	}
    //}
    if (document.all.ECOMDEPTTYPE == null)
    {
        //alert("is null");
    }
    else
    {
        if (document.all.CLIENTTYPE.value == 6) {
            //alert("TTTTTTTTTTTT");
            if (document.all.ICBREGCODE.value.length == 0) {
                infoAlert(document.all.ICBREGCODE, "Ӫҵִ�պŲ�����Ϊ��");
                return false;
            } else {
            }
        } else {

        }
    }
    if (document.all.IDTYPE.value == 1) {
        var idtype = document.all.IDTYPE;
        var id = document.all.ID;
        return checkZJHM(idtype, id);
    } else {
        return true;
    }


}
function doChange()
{
    if (document.all.CLIENTTYPE.value == '6')
    {
        //document.all.ICBREGCODE.readOnly=false;
        //document.all.CORPRESP.readOnly=false;
    }
    else
    {
        //document.all.ICBREGCODE.value='';
        //document.all.CORPRESP.value='';
        //document.all.ICBREGCODE.readOnly=true;
        //document.all.CORPRESP.readOnly=true;
    }

}