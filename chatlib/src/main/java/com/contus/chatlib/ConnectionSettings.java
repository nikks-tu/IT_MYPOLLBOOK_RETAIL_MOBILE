package com.contus.chatlib;

/**
 * Created by user on 16/6/16.
 */
public class ConnectionSettings {

    private String xmppHost;

    private int xmppPort;

    private String xmppDomain;

    private String xmppResource;
    private String userName;
    private String password;

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets xmpp resource.
     *
     * @return the xmpp resource
     */
    public String getXmppResource() {
        return xmppResource;
    }

    /**
     * Sets xmpp resource.
     *
     * @param xmppResource the xmpp resource
     */
    public void setXmppResource(String xmppResource) {
        this.xmppResource = xmppResource;
    }

    /**
     * Gets xmpp host.
     *
     * @return the xmpp host
     */
    public String getXmppHost() {
        return xmppHost;
    }

    /**
     * Sets xmpp host.
     *
     * @param xmppHost the xmpp host
     */
    public void setXmppHost(String xmppHost) {
        this.xmppHost = xmppHost;
    }

    /**
     * Gets xmpp domain.
     *
     * @return the xmpp domain
     */
    public String getXmppDomain() {
        return xmppDomain;
    }

    /**
     * Sets xmpp domain.
     *
     * @param xmppDomain the xmpp domain
     */
    public void setXmppDomain(String xmppDomain) {
        this.xmppDomain = xmppDomain;
    }

    /**
     * Gets xmpp port.
     *
     * @return the xmpp port
     */
    public int getXmppPort() {
        return xmppPort;
    }

    /**
     * Sets xmpp port.
     *
     * @param xmppPort the xmpp port
     */
    public void setXmppPort(int xmppPort) {
        this.xmppPort = xmppPort;
    }


}
