package com.contus.chatlib.listeners;

/**
 * Created by user on 28/9/16.
 */
public interface XMPPLoginListener {

    void onLogin();

    void onConnectionClosed();

    void onConnectionError();
}
