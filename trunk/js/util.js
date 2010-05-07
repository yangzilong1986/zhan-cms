/*================Method=======================
 *1.var_to_obj---------��һ������ת��Ϊ����
 *2.is_greater---------�ж��Ƿ����ĳ����
 *3.is_less----------�ж��Ƿ�С��ĳ����
 *4.Compare_Date------�Ƚ��������ڵĴ�С��Num1>Num2 return:true;Num1<=Num2 return:false
 *5.is_numeric------�ж��Ƿ�������
 *6.is_price-----�ж��Ƿ��Ǽ۸��ʽ��00.00����λС��
 *7.Is_Null-----�ж��Ƿ��ǿ�true:��false:�ǿ�
 *8.IsSpace------�ж��Ƿ�ո�
 *9.IsSpace----�ж��Ƿ�ո�
 *10.Is_Int(----�ж��Ƿ�������true:��������false:��������
 *11.is_date----�ж��Ƿ�������
 *12.is_date2---�ж��Ƿ�������
 *13.IsSelected----�ж��Ƿ�����Ч���ݱ�ѡ��
 *14.isCharacter----����Ƿ����ַ�
 *15.isOtherNameCharacter-----����Ƿ����������������Ƶ��ַ�
 *16.isNameCharacter-----����Ƿ��ǿ��������Ƶ��ַ�
 *17.isEmail-----����Ƿ���Email
 *18.isZIP----����Ƿ����ʱ�
 *19.isDigitalString-----����Ƿ��������ַ���
 *20.IsEmpty-----IsEmpty�����ж�һ���ַ����Ƿ�Ϊ��
 *21.Trim-----Trim����ȥ��һ�ַ������ߵĿո�
 *22.isDigital----IsDigital�����ж�һ���ַ����Ƿ�������(int or long)���
 *23.IsFloat----IsFloat�����ж�һ���ַ����Ƿ�������(int or long or float)���
 *24.IsTelephone---�ж�һ���ַ����Ƿ������ֻ�'-','*','()'���
 *25.dateTransfer-----���ڸ�ʽת��2/18/2000 ----2000-2-18
 *26.transferDate----ת������2000-10-20 ---->10/20/2000
 *27.ispassword-----����Ƿ�������
 *28.isLeapYear-----�ж��Ƿ�Ϊ����ĺ���
 *29.getDaysInMonth----ȡ��ÿ�������ĺ���
 *30.jtrimstr---ȥ���ַ������пո�
 *=============================================*/

function doInputConnotEmpty(txtInput, minLength, sAlert) {
    txtInput.value = Trim(txtInput.value);
    var m_IsValid = true;
    if (IsEmpty(txtInput.value)) m_IsValid = false;
    if (minLength > 0) {
        if (txtInput.value.length < minLength) m_IsValid = false;
    }
    if (!m_IsValid) {
        if (sAlert != "") window.alert(sAlert);
        txtInput.focus();
    }
    return m_IsValid;
}

// ��һ������ת��Ϊ����
function var_to_obj(val)
{
    this.value = val;
}
// �ж��Ƿ����ĳ����
function is_greater(field, crit, limit)
{
    var Ret = (is_numeric(field, -1) ) ? (field.value > limit ) : false;
    if (!Ret)
        doCritCode(field, crit, "Value must be greater than " + limit);
    return(Ret);
}
// �ж��Ƿ�С��ĳ����
function is_less(field, crit, limit)
{
    var Ret = (is_numeric(field, -1) ) ? (field.value < limit ) : false;
    if (!Ret)
        doCritCode(field, crit, "Value must be less than " + limit);
    return(Ret);
}

function is_numeric(field, crit, msg)
{
    var Ret = true;
    var NumStr = "0123456789";
    var decUsed = false;
    var chr;
    for (i = 0; i < field.value.length; ++i)
    {
        chr = field.value.charAt(i);
        if (NumStr.indexOf(chr, 0) == -1)
        {
            if ((!decUsed) && chr == ".")
            {
                decUsed = true;
            }
            else
            {
                Ret = false;
            }
        }
    }
    if (!Ret)
        doCritCode(field, crit, msg);
    return(Ret);
}
// �ж��Ƿ��Ǽ۸�
function is_price(field, crit, msg)
{
    var Ret = true;
    var NumStr = "0123456789";
    var decUsed = false;
    var chr;
    for (i = 0; i < field.value.length; ++i)
    {
        chr = field.value.charAt(i);
        if (NumStr.indexOf(chr, 0) == -1)
        {
            if ((!decUsed) && chr == ".")
            {
                decUsed = true;
            }
            else
            {
                Ret = false;
            }
        }
    }
    if (Ret)
    {
        if (decUsed && (field.value.length - field.value.indexOf('.') < 4))
            ;
        else if (decUsed)
            Ret = false;
    }
    if (!Ret)
        doCritCode(field, crit, msg);
    return(Ret);
}

// �ж��Ƿ��ǿ�
function is_null(field, crit, msg)
{
    var Text = "" + Trim(field.value);
    if (Text.length)
    {
        for (var i = 0; i < Text.length; i++)
            if (Text.charAt(i) != " " && Text.charAt(i) != "��")
                break;
        if (i >= Text.length) {
            Ret = true;
        }
        else {
            Ret = false;
        }
    }
    else
        Ret = true;
    if (Ret)
        doCritCode(field, crit, msg);
    return(Ret);
}
function IsSpace(field)
{
    var Text = "" + field.value;
    if (Text.length)
    {
        for (var i = 0; i < Text.length; i++)
            if (Text.charAt(i) != " " && Text.charAt(i) != "��")
                break;
        if (i >= Text.length)
            field.value = "";
    }
}

function doCritCode(field, crit, msg)
{
    if ((-1 != crit))
    {
        alert(msg)
        if (crit == 1)
        {
            field.focus(); // focus does not work on certain netscape versions
            field.select();
        }
    }
}
// �ж��Ƿ�������
function is_int(field, crit, msg) {
    var Ret = true;
    var NumStr = "0123456789";
    var chr;
    if (field.value.length == 0)
    {
        Ret = false;
    }
    for (i = 0; i < field.value.length; ++i)
    {
        chr = field.value.charAt(i);
        if (NumStr.indexOf(chr, 0) == -1)
        {
            Ret = false;
        }
    }
    if (!Ret)
        doCritCode(field, crit, msg);
    return(Ret);
}
// �ж��Ƿ�������
function is_date(field, crit, msg) {
    var Ret = false;
    var mark1;
    var mark2;
    var days;
    var y;
    var m;
    var d;
    if (field.value == "")
        return true;
    cd = new Date();

    if ((mark1 = field.value.indexOf('-')) == -1)
        mark1 = field.value.indexOf('-')
    if (mark1 > -1)
    {
        if ((mark2 = field.value.indexOf('-', mark1 + 1)) == -1)
            mark2 = field.value.indexOf('-', mark1 + 1);
        if ((mark2 > -1) && (mark2 + 1 < field.value.length))
        {
            y = parseInt(field.value.substring(0, mark1), 10);
            m = parseInt(field.value.substring(mark1 + 1, mark2), 10);
            d = parseInt(field.value.substring(mark2 + 1, field.value.length), 10);

            year = new var_to_obj(y);
            month = new var_to_obj(m);
            day = new var_to_obj(d);
            days = getDaysInMonth(month.value, year.value) + 1;

            if (
                    (is_greater(day, -1, 0)) && (is_less(day, -1, days)) &&
                    (is_greater(month, -1, 0)) && (is_less(month, -1, 13)) &&
                    (is_greater(year, -1, 1900)) && (is_less(year, -1, 2500))
                    )
                Ret = true;
        }
    }
    if (!Ret) doCritCode(field, crit, msg);

    return(Ret);
}

function doCrit(field, crit, msg)
{
    if ((-1 != crit))
    {
        alert(msg);
        if (crit == 1)
        {
            field.focus(); // focus does not work on certain netscape versions
        }
    }
}
// �ж��Ƿ�����Ч���ݱ�ѡ��
function isselected(field, crit, msg)
{
    value = "" + field.options[field.selectedIndex].value;
    if (value == "0")
        Ret = false;
    else
        Ret = true;
    if (!Ret)
        doCrit(field, crit, msg);
    return(Ret);
}

// ����Ƿ����ַ�
// cCharacter������ֵ
function isCharacter(cCharacter)
{
    var sFormat = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    if (sFormat.indexOf(cCharacter, 0) == -1)
    {
        return false;
    }

    return true;
}

//�ж��Ƿ������ֺ���ĸ�����
function isChrandNum(cCharacter)
{
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            if (str.charAt(ilen) < 'a' || str.charAt(ilen) > 'z')
            {
                if (str.charAt(ilen) < 'A' || str.charAt(ilen) > 'Z')
                    return false;
            }
        }
    }
    return true;
}

// ����Ƿ����������������Ƶ��ַ�
// cCharacter������ֵ
function isOtherNameCharacter(cCharacter)
{
    var sFormat = "_";

    if (sFormat.indexOf(cCharacter, 0) == -1)
    {
        return false;
    }
    return true;
}
function isOtherNameCharacter1(cCharacter)
{
    var sFormat = "-";

    if (sFormat.indexOf(cCharacter, 0) == -1)
    {
        return false;
    }

    return true;
}

function isOtherNameCharacter2(cCharacter)
{
    var sFormat = ".";

    if (sFormat.indexOf(cCharacter, 0) == -1)
    {
        return false;
    }

    return true;
}

// ����Ƿ��ǿ��������Ƶ��ַ�
// sValue������ֵ
function isNameCharacter(sValue)
{
    if (sValue == null)
    {
        return false;
    }

    for (i = 0; i < sValue.length; i ++)
    {
        var cCharacter = sValue.charAt(i);
        if (isDigital(cCharacter) == false && isCharacter(cCharacter) == false && isOtherNameCharacter(cCharacter) == false && isOtherNameCharacter1(cCharacter) == false && isOtherNameCharacter2(cCharacter) == false)
        {
            return false;
        }
    }

    return true;
}
//����Ƿ�������
function ispassword(sValue)
{
    if (sValue == null)
    {
        return false;
    }

    for (i = 0; i < sValue.length; i ++)
    {
        var cCharacter = sValue.charAt(i);
        if (isDigital(cCharacter) == false && isCharacter(cCharacter) == false && isOtherNameCharacter(cCharacter) == false)
        {
            return false;
        }
    }

    return true;
}

// ����Ƿ���Email
// sValue������ֵ���Ϸ���ʽΪa@b.c.d������ʽ
function isEmail(sValue)
{
    var iFirstIndex = 0;
    var iSecondIndex = sValue.indexOf('@');
    if (iSecondIndex == -1)
    {
        return false;
    }

    var sTemp = sValue.substring(iFirstIndex, iSecondIndex);
    if (isNameCharacter(sTemp) == false)
    {
        return false;
    }

    iSecondIndex = sValue.indexOf('.');
    if (iSecondIndex == -1 || sValue.substring(sValue.length - 1, sValue.length) == '.')
    {
        return false;
    }
    else if (sTemp.length == sValue.length - 2) // The last two characters are '@' and '.'
    {
        return false;
    }
    else
    {
        var sTempValue = sValue;
        iSecondIndex = sValue.indexOf('@');
        while (iSecondIndex != -1)
        {
            iFirstIndex = iSecondIndex + 1;
            sTempValue = sTempValue.substring(iFirstIndex, sTempValue.length); // The right section of value
            iSecondIndex = sTempValue.indexOf('.');
            //document.write( "sTempValue=" + sTempValue + "<br>" );
            sTemp = sTempValue.substring(0, iSecondIndex);
            //document.write( "sTemp=" + sTemp + "<br>" );
            if (isNameCharacter(sTemp) == false)
            {
                return false;
            }
        }

        if (isNameCharacter(sTempValue) == false)
        {
            return false;
        }
    }

    return true;
}

// ����Ƿ����ʱ�
// sValue������ֵ���Ϸ���ʽΪ��λ����
function isZIP(sValue)
{
    if (sValue == null)
    {
        return false;
    }

    if (sValue.length != 6)
    {
        return false;
    }
    else
    {
        for (i = 0; i < 6; i ++)
        {
            if (isDigital(sValue.charAt(i)) == false)
            {
                return false;
            }
        }
    }

    return true;
}

// ����Ƿ��������ַ���
// sValue������ֵ
function isDigitalString(sValue)
{
    if (sValue == null)
    {
        return false;
    }

    for (i = 0; i < sValue.length; i ++)
    {
        if (isDigital(sValue.charAt(i)) == false)
        {
            return false;
        }
    }
}

//IsEmpty�����ж�һ���ַ����Ƿ�Ϊ��
function IsEmpty(his)
{
    flag = true;
    for (var i = 0; i < his.length; i++)
    {
        if (his.charAt(i) != " ")
        {
            flag = false;
            break;
        }
    }
    return flag;
}
//Trim����ȥ��һ�ַ������ߵĿո�
function Trim(his)
{
    //�ҵ��ַ�����ʼλ��
    Pos_Start = -1;
    for (var i = 0; i < his.length; i++)
    {
        if (his.charAt(i) != " ")
        {
            Pos_Start = i;
            break;
        }
    }
    //�ҵ��ַ�������λ��
    Pos_End = -1;
    for (var i = his.length - 1; i >= 0; i--)
    {
        if (his.charAt(i) != " ")
        {
            Pos_End = i;
            break;
        }
    }
    //���ص��ַ���
    Str_Return = ""
    if (Pos_Start != -1 && Pos_End != -1)
    {
        for (var i = Pos_Start; i <= Pos_End; i++)
        {
            Str_Return = Str_Return + his.charAt(i);
        }
    }
    return Str_Return;
}
//IsDigital�����ж�һ���ַ����Ƿ�������(int or long)���
function isDigital(str)
{
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            return false;
        }
    }
    return true;
}
//IsFloat�����ж�һ���ַ����Ƿ�������(int or long or float)���
function IsFloat(str)
{
    flag_Dec = 0
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) == '.')
        {
            flag_Dec++;
            if (flag_Dec > 1)
                return false;
            else
                continue;
        }
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            return false;
        }
    }
    return true;
}
//IsTelephone�����ж�һ���ַ����Ƿ������ֻ�'-','*'���
function IsTelephone(str)
{
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            if ((str.charAt(ilen) != '-') && (str.charAt(ilen) != '*'))
                return false;
        }
    }
    return true;
}

//�Ƚ��������ڵĴ�С��Num1>Num2 return:true;Num1<=Num2 return:false
function Compare_Date(Num1, Num2)
{
    var pos1,pos2,end;
    var para1,para2,para3,para4,para5,para6;

    //para1:��
    //para2:��
    //para3:��
    end = Num1.length;
    pos1 = Num1.indexOf("-", 0);
    pos2 = Num1.indexOf("-", pos1 + 1);
    para1 = Num1.substring(0, pos1);
    para2 = Num1.substring(pos1 + 1, pos2);
    para3 = Num1.substring(pos2 + 1, end);
    para1 = parseInt(para1, 10);
    para2 = parseInt(para2, 10);
    para3 = parseInt(para3, 10);
    end = Num2.length;
    pos1 = Num2.indexOf("-", 0);
    pos2 = Num2.indexOf("-", pos1 + 1);
    para4 = Num2.substring(0, pos1);
    para5 = Num2.substring(pos1 + 1, pos2);
    para6 = Num2.substring(pos2 + 1, end);
    para4 = parseInt(para4, 10);
    para5 = parseInt(para5, 10);
    para6 = parseInt(para6, 10);
    if (para1 > para4)
    {
        return true;
    }
    else if (para1 == para4)
    {
        if (para2 > para5)
        {
            return true;
        }
        else if (para2 == para5)
        {
            if (para3 > para6)
            {
                return true;
            }
        }
    }
    l.co
    return false;
}

//ȥ���ַ������пո�
function jtrimstr(str)
{
    var i = 0;
    var j;
    var len = str.length;
    trimstr = "";
    while (i < len)
    {
        if (str.charAt(i) != " ")
        {
            trimstr = trimstr + str.charAt(i);
        }
        i++;
    }
    return(trimstr);
}
//ת������
function transferDate(str)
{
    var m = 4;
    var strlen = str.length
    var n = strlen - 1;
    while (n >= strlen - 2)
    {
        if (str.charAt(n) == "-")
        {
            break;
        }
        n = n - 1
    }
    trimstr = str.substring(m + 1, n) + "/" + str.substring(n + 1, strlen) + "/" + str.substring(0, m)
    return(trimstr)
}

//modify kings 2001-2-20
//1.ȡ��ÿ�������ĺ���
//����˵����month--��;year--��
// ����ֵ��days--����
function getDaysInMonth(month, year) {
    var days;
    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) days = 31;
    else if (month == 4 || month == 6 || month == 9 || month == 11) days = 30;
    else if (month == 2) {
            if (isLeapYear(year)) {
                days = 29;
            }
            else {
                days = 28;
            }
        }
    return (days);
}
//2.�ж��Ƿ�Ϊ����ĺ���
//����˵����Year--���
// ����ֵ:��������꣬����true�����򷵻�false.

function isLeapYear(Year) {
    if (((Year % 4) == 0) && ((Year % 100) != 0) || ((Year % 400) == 0)) {
        return (true);
    } else {
        return (false);
    }
}

function isCharnum(str)
{
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            if ((str.charAt(ilen) != 'x'))
                return false;
        }
    }
    return true;
}

function isCharsf(str)
{
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            if (str.charAt(ilen) < 'a' || str.charAt(ilen) > 'z')
                return false;
        }
    }
    return true;
}

//�ж�һ���ַ����Ƿ��������ֺ�"-"���

function ismonth(str)
{
    for (ilen = 0; ilen < str.length; ilen++)
    {
        if (str.charAt(ilen) < '0' || str.charAt(ilen) > '9')
        {
            if ((str.charAt(ilen) != '-'))
                return false;
        }
    }
    return true;
}

//�����Ի���
function popModalDialog(url, args, height, width) {
    return window.showModalDialog(url, args, "dialogHeight:" + height + "px;dialogWidth:" + width + "px;center:yes;help:no;resizable:no;status:no;");
}