//Source file: e:\\java\\zt\\cmsi\\pub\\define\\Post.java

package zt.cmsi.pub.define;

import java.util.Vector;


public class Post {
    private static Post ptr = null;
    private int appointment;

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE4018A0149
     */
    public boolean ifAppointment(int userid) {
        return true;
    }

    /**
     * @return zt.cmsi.pub.define.Post
     * @roseuid 3FE402CC0337
     */
    public static Post getInstance() {
        return null;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE4019E029D
     */
    public boolean ifApprov3rd(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE401FA0245
     */
    public boolean ifApprov2nd(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE4020202C9
     */
    public boolean ifApprov1st(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE4021200F5
     */
    public boolean ifContract(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402290351
     */
    public boolean ifLoanIssuer(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE40243000F
     */
    public boolean ifHandover(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE4025803E5
     */
    public boolean ifBill(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402640189
     */
    public boolean ifLoanAdm(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402740196
     */
    public boolean ifILAdm(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE4028301CA
     */
    public boolean ifPDAdm(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402990117
     */
    public boolean ifLawAffair(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402A20387
     */
    public boolean ifIL3rd(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402A80050
     */
    public boolean ifIL2nd(int userid) {
        return true;
    }

    /**
     * @param userid
     * @return boolean
     * @roseuid 3FE402AD0224
     */
    public boolean ifIL1st(int userid) {
        return true;
    }

    /**
     * 循环判断返回当前用户所在的所有岗位
     *
     * @param userid
     * @return Vector
     * @roseuid 3FE402D602E1
     */
    public Vector getAllPost(int userid) {
        return null;
    }
}
