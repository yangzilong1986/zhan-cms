function checkSelfSubmit(button) {
    with (winform) {
        if (typeof(CONTFROMDATE) != "undefined")
            if (CONTFROMDATE.value != "" && CONTTODATE.value != "")
                if (firstDateNotLaterThanSecond(CONTFROMDATE, CONTTODATE, '��ͬǩ����ʼ����', '��ͬǩ����������')) {
                    return true;
                } else {
                    return false;
                }
        if (typeof(PAYFROMDATE) != "undefined")
            if (PAYFROMDATE.value != "" && PAYTODATE.value != "")
                if (firstDateNotLaterThanSecond(PAYFROMDATE, PAYTODATE, 'Լ���ۿʼ����', 'Լ���ۿ��������')) {
                    return true;
                } else {
                    return false;
                }
        if (typeof(APPFROMDATE) != "undefined")
            if (APPFROMDATE.value != "" && APPTODATE.value != "")
                if (firstDateNotLaterThanSecond(APPFROMDATE, APPTODATE, '���뿪ʼ����', '�����������')) {
                    return true;
                } else {
                    return false;
                }
        return true;
    }
}