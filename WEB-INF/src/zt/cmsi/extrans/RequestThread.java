package zt.cmsi.extrans;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import zt.cmsi.biz.LoanGranted;
import zt.platform.utils.Debug;

public class RequestThread extends Thread {
    public void run() {
        IOEntity data = null;
        int ret;

        Debug.debug(Debug.TYPE_MESSAGE, "IOCheckThread is running!");

        if (LoanGranted.addAllUnsendGrant() < 0) {
            Debug.debug(Debug.TYPE_MESSAGE, "Not all unauthorized Loan Grant are put into pool successfully!");
        }

        while (true) {
            data = RequestMan.getInstance().getRequest();
            if (data != null) {
                Debug.debug(Debug.TYPE_MESSAGE,
                        "Received Request in Request Pool, start processing.");
                ret = RequestMan.processRequest(data);
                if (ret >= 0) {
                    RequestMan.getInstance().addResponse(data);
                } else {
                    RequestMan.getInstance().reSendRequest(data);
                }
            }
        }
    }

}
