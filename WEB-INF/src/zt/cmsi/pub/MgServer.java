package zt.cmsi.pub;

public class MgServer {
    private String serverName = null;
    private String listenAddress = null;
    private String listenPort = null;

    public String getServerName() {
        return this.serverName;
    }

    public String getListenAddress() {
        return this.listenAddress;
    }

    public String getListenPort() {
        return this.listenPort;
    }

    public void setServerName(String servername) {
        this.serverName = servername;
    }

    public void setListenAddress(String listenaddress) {
        this.listenAddress = listenaddress;
    }

    public void setListenPort(String listenport) {
        this.listenPort = listenport;
    }

}