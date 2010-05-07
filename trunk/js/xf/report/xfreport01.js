function checkSelfSubmit(button) {
    with (winform) {
        if (typeof(CONTFROMDATE) != "undefined")
            if (CONTFROMDATE.value != "" && CONTTODATE.value != "")
                if (firstDateNotLaterThanSecond(CONTFROMDATE, CONTTODATE, '合同签订开始日期', '合同签订结束日期')) {
                    return true;
                } else {
                    return false;
                }
        if (typeof(PAYFROMDATE) != "undefined")
            if (PAYFROMDATE.value != "" && PAYTODATE.value != "")
                if (firstDateNotLaterThanSecond(PAYFROMDATE, PAYTODATE, '约定扣款开始日期', '约定扣款结束日期')) {
                    return true;
                } else {
                    return false;
                }
        if (typeof(APPFROMDATE) != "undefined")
            if (APPFROMDATE.value != "" && APPTODATE.value != "")
                if (firstDateNotLaterThanSecond(APPFROMDATE, APPTODATE, '申请开始日期', '申请结束日期')) {
                    return true;
                } else {
                    return false;
                }
        return true;
    }
}