package zt.platform.user;

public class VirtualUser {
    private String username;
    private String brhname;
    private String loginid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBrhname() {
        return brhname;
    }

    public void setBrhname(String brhname) {
        this.brhname = brhname;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public VirtualUser(String loginid, String username, String brhname) {
        this.username = username;
        this.brhname = brhname;
        this.loginid = loginid;
    }
}