function ChkUtil() {
}
//У���Ƿ�Ϊ��(��ɾ�����߿ո�����֤)
ChkUtil.isNull = function (str) {
    if (null == str || "" == str.trim()) {
        return true;
    } else {
        return false;
    }
};
//У���Ƿ�ȫ������
ChkUtil.isDigit = function (str) {
    var patrn = /^\d+$/;
    return patrn.test(str);
};
//У���Ƿ�������
ChkUtil.isInteger = function (str) {
    var patrn = /^([+-]?)(\d+)$/;
    return patrn.test(str);
};
//У���Ƿ�Ϊ������
ChkUtil.isPlusInteger = function (str) {
    var patrn = /^([+]?)(\d+)$/;
    return patrn.test(str);
};
//У���Ƿ�Ϊ������
ChkUtil.isMinusInteger = function (str) {
    var patrn = /^-(\d+)$/;
    return patrn.test(str);
};
//У���Ƿ�Ϊ������
ChkUtil.isFloat = function(str) {
    var patrn = /^([+-]?)\d*\.\d+$/;
    return patrn.test(str);
};
//У���Ƿ�Ϊ��������
ChkUtil.isPlusFloat = function(str) {
    var patrn = /^([+]?)\d*\.\d+$/;
    return patrn.test(str);
};
//У���Ƿ�Ϊ��������
ChkUtil.isMinusFloat = function(str) {
    var patrn = /^-\d*\.\d+$/;
    return patrn.test(str);
};
//У���Ƿ������
ChkUtil.isChinese = function(str) {
    var patrn = /[\u4E00-\u9FA5\uF900-\uFA2D]+$/;
    return patrn.test(str);
};
//У���Ƿ��ACSII�ַ�
ChkUtil.isAcsii = function(str) {
    var patrn = /^[\x00-\xFF]+$/;
    return patrn.test(str);
};
//У���ֻ�����
ChkUtil.isMobile = function (str) {
    var patrn = /^0?1((3[0-9]{1})|(59)){1}[0-9]{8}$/;
    return patrn.test(str);
};
//У��绰����
ChkUtil.isPhone = function (str) {
    var patrn = /^(0[\d]{2,3}-)?\d{6,8}(-\d{3,4})?$/;
    return patrn.test(str);
};
//У��URL��ַ
ChkUtil.isUrl = function(str) {
    var patrn = /^http[s]?:\/\/[\w-]+(\.[\w-]+)+([\w-\.\/?%&=]*)?$/;
    return patrn.test(str);
};
//У����ʵ�ַ
ChkUtil.isEmail = function (str) {
    var patrn = /^[\w-]+@[\w-]+(\.[\w-]+)+$/;
    return patrn.test(str);
};
//У���ʱ�
ChkUtil.isZipCode = function (str) {
    var patrn = /^\d{6}$/;
    return patrn.test(str);
};
//У��Ϸ�ʱ��
ChkUtil.isDate = function (str) {
    if (!/\d{4}(\.|\/|\-)\d{1,2}(\.|\/|\-)\d{1,2}/.test(str)) {
        return false;
    }
    var r = str.match(/\d{1,4}/g);
    if (r == null) {
        return false;
    }
    ;
    var d = new Date(r[0], r[1] - 1, r[2]);
    return (d.getFullYear() == r[0] && (d.getMonth() + 1) == r[1] && d.getDate() == r[2]);
};
//У���ַ�����ֻ������6-20����ĸ�����֡��»���(������У���û���������)
ChkUtil.isString6_20 = function(str) {
    var patrn = /^(\w){6,20}$/;
    return patrn.test(str);
};