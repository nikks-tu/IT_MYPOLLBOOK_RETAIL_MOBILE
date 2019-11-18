package com.contus.chatlib;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.contus.chatlib.iqfilter.BindErrorIQFilter;
import com.contus.chatlib.iqresponse.ResponseBindErrorIQ;
import com.contus.chatlib.iqs.IQAddBroadcast;
import com.contus.chatlib.iqs.IQBroadcastMembers;
import com.contus.chatlib.iqs.IQClearChat;
import com.contus.chatlib.iqs.IQDeleteBroadcast;
import com.contus.chatlib.iqs.IQDeleteMessage;
import com.contus.chatlib.iqs.IQGetAvailability;
import com.contus.chatlib.iqs.IQGetContacts;
import com.contus.chatlib.iqs.IQGetGroup;
import com.contus.chatlib.iqs.IQGetGroups;
import com.contus.chatlib.iqs.IQGetProfile;
import com.contus.chatlib.iqs.IQGetSearchContacts;
import com.contus.chatlib.iqs.IQSetAddDevice;
import com.contus.chatlib.iqs.IQSetAddGroupMembers;
import com.contus.chatlib.iqs.IQSetCreateGroup;
import com.contus.chatlib.iqs.IQSetDeleteAccount;
import com.contus.chatlib.iqs.IQSetExitGroup;
import com.contus.chatlib.iqs.IQSetGroup;
import com.contus.chatlib.iqs.IQSetProfile;
import com.contus.chatlib.iqs.IQSetRemoveGroupMembers;
import com.contus.chatlib.iqs.IQUpdateBroadcast;
import com.contus.chatlib.listeners.BroadcastListener;
import com.contus.chatlib.listeners.GroupListener;
import com.contus.chatlib.listeners.MessageListener;
import com.contus.chatlib.listeners.RosterListener;
import com.contus.chatlib.listeners.XMPPLoginListener;
import com.contus.chatlib.models.ChatMessage;
import com.contus.chatlib.models.Contact;
import com.contus.chatlib.models.Group;
import com.contus.chatlib.providers.ProviderAcknowledge;
import com.contus.chatlib.providers.ProviderBindError;
import com.contus.chatlib.providers.ProviderGetContacts;
import com.contus.chatlib.providers.ProviderGetProfile;
import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;
import com.contus.chatlib.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtensionElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


/**
 * <p/>
 * XMPPConnectionController
 * <p/>
 * Controller class used to make the connection in XMPP server.
 * Authentication and all functionalities to the Server called through this class.
 * Default callback and custom iq callback also done here.
 *
 * @author ContusTeam <developers@contus.in>
 * @version 1.0
 */

public class XMPPConnectionController{

    public static XMPPTCPConnection connection;
    public static boolean connected = false;
    public static boolean isconnecting = false;
    public static boolean isBindError = false;

    public static String loginUser;
    public static String passwordUser;

    public boolean loggedin = false;
    private boolean chat_created = false;

    private XMPPLoginListener loginListener;
    private RosterListener rosterListener;
    private GroupListener groupListener;
    private BroadcastListener broadcastListener;
    private MessageListener messageListener;

    public static void setLoginUser(String loginUser) {
        XMPPConnectionController.loginUser = loginUser;
    }

    public static void setPasswordUser(String passwordUser) {
        XMPPConnectionController.passwordUser = passwordUser;
    }

    /**
     * * Decrypt. * * @param encryptedText * the encrypted text * @param msgID *
     * the msg id * @return the string
     */
    public static String decryptMsg(String encryptedText, String msgID) {
        try {
            CryptLib _crypt = new CryptLib();
            String output = "";
            String key = CryptLib.SHA256(msgID, 32); //32 bytes = 256 bit
            //String iv = CryptLib.generateRandomIV(16); //16 bytes = 128 bit
            String iv = "ddc0f15cc2c90fca";
            output = _crypt.decrypt(encryptedText, key, iv); //decrypt
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setLoginListener(XMPPLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setRosterListener(RosterListener rosterListener) {
        this.rosterListener = rosterListener;
    }

    public void setGroupListener(GroupListener groupListener) {
        this.groupListener = groupListener;
    }

    public void setBroadcastListener(BroadcastListener broadcastListener) {
        this.broadcastListener = broadcastListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * Configure settings xmpptcp connection.
     *
     * @param settings the settings
     * @return the xmpptcp connection
     */
    public void configureSettings(ConnectionSettings settings) {
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(settings.getXmppDomain());
        config.setHost(settings.getXmppHost());
        config.setPort(settings.getXmppPort());
        config.setResource(settings.getXmppResource());
        config.setDebuggerEnabled(true);
        config.setConnectTimeout(10000);

        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);

        setLoginUser(settings.getUserName());
        setPasswordUser(settings.getPassword());

        addProviders();

        connection = new XMPPTCPConnection(config.build());
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
        setMessageReceiptListener();
        addErrorBindFilter();
        addPresenceListener();
        new XMPPTCPConnection(config.build());
    }

    private void addProviders() {
        ProviderManager.addIQProvider(IQ.QUERY_ELEMENT, LibConstants.MODULE_PROFILE,
                new ProviderGetProfile());
        ProviderManager.addIQProvider(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT,
                new ProviderGetContacts());
        ProviderManager.addIQProvider(IQ.QUERY_ELEMENT, LibConstants.MODULE_LOGIN,
                new ProviderBindError());
        ProviderManager.addExtensionProvider(LibConstants.MESSAGE, LibConstants.MODULE_RECEIPT,
                new ProviderAcknowledge());
    }

    private void addErrorBindFilter() {
        StanzaFilter stanzaFilter = new BindErrorIQFilter();
        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
                try {
                    JSONObject result = ((ResponseBindErrorIQ) stanza).getJsonObject();
                    String type = result.getString("type");
                    Log.e("Profile result::", "Result::" + result);
                    isBindError = true;
                    if (rosterListener != null) {
                        if ("session-exists".equals(type))
                            rosterListener.onSessionExpire(result.getString("response"));
                        else
                            rosterListener.onSessionCleared();
                    }
                } catch (Exception e) {
                    LogMessage.e(e);
                }
            }
        }, stanzaFilter);
    }

    /**
     * addPresenceListener
     * <p>
     * Set the presence listener for all users
     * available will handle here
     */
    private void addPresenceListener() {
        StanzaTypeFilter presenceFilter = new StanzaTypeFilter(Presence.class);

        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                Presence presence = (Presence) packet;
                String username = presence.getFrom();
                username = TextUtils.split(username, "@")[0];
                String status = Presence.Type.available.equals(((Presence) packet).getType())
                        ? "online" : "offline";
                if (rosterListener != null) {
                    rosterListener.presenceCallback(username, status);
                }
            }
        }, presenceFilter);
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (connection.isConnected())
                    return false;
                isconnecting = true;
                Log.d("Connect() Function", caller + "=>connecting....");

                try {
                    connection.connect();
                    connected = true;

                } catch (IOException e) {
                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (SmackException e) {
                    Log.e("(" + caller + ")",
                            "SMACKException: " + e.getMessage());
                } catch (XMPPException e) {
                    Log.e("connect(" + caller + ")",
                            "XMPPException: " + e.getMessage());
                }
                return isconnecting = false;
            }
        };
        connectionThread.execute();
    }

    public void login() {
        try {
            if (connection.isConnected()) {
                if (!connection.isAuthenticated()) {
                    connection.setPacketReplyTimeout(10000);
                    connection.login(loginUser, passwordUser);
                    Log.i("LOGIN", "Yey! We're connected to the Xmpp server!");
                }
            } else if (!isconnecting) {
                connect("Controller");
            }

        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProfile(String jid) {
        try {
            connection.sendIqWithResponseCallback(new IQGetProfile(jid), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String result = Utils.parseResult(packet.toString());
                        Log.e("Profile result::", "Result::" + result);
                        Contact contact;
                        if (result != null && !result.isEmpty()) {
                            contact = new Gson().fromJson(result, Contact.class);
                            if (rosterListener != null) {
                                contact.setJid(from);
                                rosterListener.userProfileCallback(contact);
                            }
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void getAvailability(String jid) {
        try {
            connection.sendIqWithResponseCallback(new IQGetAvailability(jid), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String result = Utils.parseResult(packet.toString());
                        Log.e("availability result::", "Result::" + result);
                        if (result != null && !result.isEmpty()) {
                            if (rosterListener != null) {
                                JSONObject user = new JSONObject(result);

                                rosterListener.getCurrentAvailability(user.getString("user"),
                                        user.getString("availability"),
                                        user.getString("seen"));
                            }
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void getGroupInfo(String jid) {
        try {
            connection.sendIqWithResponseCallback(new IQGetGroup(jid), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

                    try {
                        String from = packet.getFrom();
                        String result = Utils.parseResult(packet.toString());
                        Log.e("Contacts result::", "Result::" + result);
                        List<Group> groups;
                        if (result != null && !result.isEmpty()) {
                            Type type = new TypeToken<List<Group>>() {
                            }.getType();
                            groups = new Gson().fromJson(result, type);
                            if (groupListener != null && !groups.isEmpty()) {
                                groupListener.groupCallback(groups.get(0));
                            }
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void getGroups() {
        try {
            connection.sendIqWithResponseCallback(new IQGetGroups(), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

                    try {
                        String from = packet.getFrom();
                        String result = Utils.parseResult(packet.toString());
                        Log.e("Contacts result::", "Result::" + result);
                        List<Group> groups;
                        if (result != null && !result.isEmpty()) {
                            Type type = new TypeToken<List<Group>>() {
                            }.getType();
                            groups = new Gson().fromJson(result, type);
                            if (groupListener != null) {
                                groupListener.groupsCallback(groups);
                            }
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }

                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void deleteAccount() {
        try {
            connection.sendIqWithResponseCallback(new IQSetDeleteAccount(), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("delete account:::", "delete account::" + response);
                        if (rosterListener != null)
                            rosterListener.deleteAccount(response, ((IQ) packet).getType().toString());
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void editProfile(String userName, String imageUrl, String status) {
        try {
            connection.sendIqWithResponseCallback(new IQSetProfile(userName, imageUrl, status), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("update profile:::", "update profile::" + response);
                        boolean isUpdate = IQ.Type.result.equals(((IQ) packet).getType());
                        if (rosterListener != null)
                            rosterListener.profileUpdated(isUpdate);
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void getUpDateGroupInfo(String groupid, String groupname, String image, String description) {
        try {
            connection.sendIqWithResponseCallback(new IQSetGroup(groupname, description, image, groupid), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("update group:::", "update group::" + response);
                        if (groupListener != null) {
                            groupListener.groupInfoUpdatedResponse(response);
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void sendDeviceId(String deviceid) {
        try {
            connection.sendIqWithResponseCallback(new IQSetAddDevice(deviceid), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void removeGroupMember(String groupid, String members) {
        try {
            connection.sendIqWithResponseCallback(new IQSetRemoveGroupMembers(groupid, members), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("remove group member:::", "remove member::" + response);
                        if (groupListener != null) {
                            groupListener.groupRemoveMemberResponse(response);
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void addGroupMember(String groupid, String members) {
        try {
            connection.sendIqWithResponseCallback(new IQSetAddGroupMembers(groupid, members), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("add group member:::", "add member::" + response);
                        if (groupListener != null) {
                            groupListener.groupMemberAdded(response, ((IQ) packet).getType().name());
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void createGroup(String groupname, String description, String image) {
        try {
            connection.sendIqWithResponseCallback(new IQSetCreateGroup(groupname, description, image), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("create group:::", "create::" + response);
                        if (groupListener != null)
                            groupListener.groupIdAdded(response);
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void exitGroup(String groupid) {
        try {
            connection.sendIqWithResponseCallback(new IQSetExitGroup(groupid), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String response = Utils.parseResponse(packet.toString());
                        Log.e("exit group member:::", "exit member::" + response);
                        if (groupListener != null) {
                            groupListener.groupExitResponse(response);
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void getSearchContacts(String searchterm) {
        try {
            connection.sendIqWithResponseCallback(new IQGetSearchContacts(searchterm), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void getContacts() {
        try {
            connection.sendIqWithResponseCallback(new IQGetContacts(), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String from = packet.getFrom();
                        String result = Utils.parseResult(packet.toString());
                        Log.e("Contacts result::", "Result::" + result);
                        List<Contact> contacts;
                        if (result != null && !result.isEmpty()) {
                            Type type = new TypeToken<List<Contact>>() {
                            }.getType();
                            contacts = new Gson().fromJson(result, type);
                            if (rosterListener != null) {
                                rosterListener.rosterCallback(contacts);
                            }
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void createBroadcast(String broadcastName, String description) {
        try {
            connection.sendIqWithResponseCallback(new IQAddBroadcast(broadcastName, description),
                    new StanzaListener() {
                        @Override
                        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

                            try {
                                String response = Utils.parseResponse(packet.toString());
                                if (!"error".equals(((IQ) packet).getType().toString()) && broadcastListener != null)
                                    broadcastListener.broadcastIdAdded(response);
                            } catch (Exception e) {
                                LogMessage.e(e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void addBroadcastMember(String broadcastId, String members) {
        try {
            connection.sendIqWithResponseCallback(new IQBroadcastMembers(broadcastId, members, LibConstants.STATUS_SET_ADD_BROADCAST_MEMBERS),
                    new StanzaListener() {
                        @Override
                        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                            try {
                                String response = Utils.parseResponse(packet.toString());
                                if (!"error".equals(((IQ) packet).getType().toString()) && broadcastListener != null) {
                                    broadcastListener.broadcastMemberAdded(response, ((IQ) packet).getType().name());
                                }
                            } catch (Exception e) {
                                LogMessage.e(e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void removeBroadcastMember(String broadcastId, String members) {
        try {
            connection.sendIqWithResponseCallback(new IQBroadcastMembers(broadcastId, members, LibConstants.STATUS_SET_REMOVE_BROADCAST_MEMBERS),
                    new StanzaListener() {
                        @Override
                        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                            try {
                                String response = Utils.parseResponse(packet.toString());
                                if (!"error".equals(((IQ) packet).getType().toString()) && broadcastListener != null) {
                                    broadcastListener.broadcastRemoveMemberResponse(response);
                                }
                            } catch (Exception e) {
                                LogMessage.e(e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void updateBroadcastInfo(String broadcastId, String broadcastName, String description) {
        try {
            connection.sendIqWithResponseCallback(new IQUpdateBroadcast(broadcastId, broadcastName, description),
                    new StanzaListener() {
                        @Override
                        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                            try {
                                String response = Utils.parseResponse(packet.toString());
                                if (!"error".equals(((IQ) packet).getType().toString()) && broadcastListener != null) {
                                    broadcastListener.broadcastInfoUpdatedResponse(response);
                                }
                            } catch (Exception e) {
                                LogMessage.e(e);
                            }
                        }
                    });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void deleteBroadcast(final String broadcastId) {
        try {
            connection.sendIqWithResponseCallback(new IQDeleteBroadcast(broadcastId), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String response = Utils.parseResponse(packet.toString());
                        if (!"error".equals(((IQ) packet).getType().toString()) && broadcastListener != null) {
                            broadcastListener.broadcastDeleteResponse(response);
                        }
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void sendMessage(ChatMessage msMessage) {
        try {
            Message message = new Message();
            String encBody = encryptMsg(msMessage.getMsg_body(), msMessage.getMid());
            message.setBody(encBody);
            message.setType(Message.Type.fromString(msMessage.getChat_type()));
            message.setMessageType(Message.MessageType.fromString(msMessage.getMsg_type()));
            message.setStanzaId(msMessage.getMid());
            message.setTime(msMessage.getTime());
            message.setBroadcastId(msMessage.getBroadcastId());
            if (Message.Type.groupchat.equals(message.getType())) {
                message.setTo(msMessage.getTo() + "@group." + connection.getHost());
                message.setFrom(msMessage.getFrom() + "@group." + connection.getHost());
            } else {
                message.setTo(msMessage.getTo() + "@" + connection.getHost());
                message.setFrom(msMessage.getFrom() + "@" + connection.getHost());
            }
            connection.sendStanza(message);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void deleteMessage(String messageId, String chatType) {
        try {
            connection.sendIqWithResponseCallback(new IQDeleteMessage(messageId, chatType), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String result = Utils.parseResult(packet.toString());
                        Log.e("Delete Message result::", "Result::" + result);
                        if (messageListener != null)
                            messageListener.deleteMessage(((IQ) packet).getType() == IQ.Type.result);
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void clearChat(String username, String chatType) {
        try {
            connection.sendIqWithResponseCallback(new IQClearChat(username, chatType), new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    try {
                        String result = Utils.parseResult(packet.toString());
                        Log.e("Clear chat result::", "Result::" + result);
                        if (messageListener != null)
                            messageListener.clearChat(((IQ) packet).getType() == IQ.Type.result);
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }
            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void sendTypingStatus(String username, String status, String chatType, String from) {
        try {
            Message message = new Message();
            message.setType(Message.Type.fromString(chatType));
            if (Message.Type.groupchat.equals(message.getType())) {
                message.setTo(username + "@group." + connection.getHost());
            } else {
                message.setTo(username + "@" + connection.getHost());
            }
            PacketExtensionElement extnElement;
            if (!TextUtils.isEmpty(status)) {
                extnElement = new PacketExtensionElement("cha:composing", "http://jabber.org/protocol/chatstates");
            } else {
                extnElement = new PacketExtensionElement("cha:gone", "http://jabber.org/protocol/chatstates");
            }
            extnElement.setAttributeValue("description", status);
            extnElement.setAttributeValue("user", from);
            extnElement.setAttributeValue("xmlns:cha", "http://jabber.org/protocol/chatstates");
            message.addExtension(extnElement);
            connection.sendStanza(message);
            message.removeExtension(extnElement);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void sendMessageReceipt(String to, String messageId, String chatType, String receiptType, String mTime) {
        try {
            Message message = new Message();
            message.setChatType(chatType);
            message.setType(Message.Type.fromString(receiptType));
            message.setStanzaId(messageId);
            message.setTime(mTime);
            if (Message.Type.groupchat.toString().equals(chatType)) {
                message.setTo(to + "@group." + connection.getHost());
//                message.setFrom(msMessage.getFrom() + "@group." + connection.getHost());
            } else {
                message.setTo(to + "@" + connection.getHost());
//                message.setFrom(msMessage.getFrom() + "@" + connection.getHost());
            }
            connection.sendStanza(message);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private void setMessageReceiptListener() {
        StanzaTypeFilter messageFilter = new StanzaTypeFilter(Message.class);
        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                String from = packet.getFrom();
                String to = packet.getTo();
                from = TextUtils.split(from, "@")[0];
                to = TextUtils.split(to, "@")[0];
                if (packet instanceof Message) {
                    Message receipt = (Message) packet;
                    String time = receipt.getTime();
                    String chatType = receipt.getChatType();
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setFrom(from);
                    chatMessage.setTo(to);
                    chatMessage.setTime(time);
                    chatMessage.setChat_type(chatType);
                    chatMessage.setMsg_type(receipt.getMessageType().toString());
                    chatMessage.setMid(packet.getStanzaId());
                    Message.Type type = receipt.getType();

                    if (Message.Type.seen.equals(type))
                        messageSeenCallback(chatMessage);
                    else if (Message.Type.receipts.equals(type))
                        messageReceiptCallback(chatMessage);
                    else if (Message.Type.receipt.equals(type))
                        handleReceipt(chatMessage, receipt);
                    else if (Message.Type.chat.equals(type)) {
                        chatMessage.setChat_type(type.toString());
                        String body = ((Message) packet).getBody();
                        String status = receipt.getStatus();
                        if ("composing".equals(status) || "gone".equals(status)) {
                            handleStatus(from, receipt.getDescription(), from);
                        } else if (!TextUtils.isEmpty(body)) {
                            String decBody = decryptMsg(body, packet.getStanzaId());
                            chatMessage.setMsg_body(decBody);
                            messageCallback(chatMessage);
                        }
                    } else if (Message.Type.groupchat.equals(type)) {
                        if (Message.MessageType.notification.equals(receipt.getMessageType())) {
                            chatMessage.setSender(TextUtils.split(packet.getFrom(), "/")[1]);
                            chatMessage.setRemovingUser(receipt.getRemovingUser());
                            handleGroupNotification(chatMessage, receipt);
                        } else if (!TextUtils.isEmpty(receipt.getMessageType().toString())) {
                            chatMessage.setChat_type(type.toString());
                            chatMessage.setSender(receipt.getSenduser());
                            String body = receipt.getBody();
                            String status = receipt.getStatus();
                            if ("composing".equals(status) || "gone".equals(status)) {
                                handleStatus(from, receipt.getDescription(), receipt.getSenduser());
                            } else if (!TextUtils.isEmpty(body)) {
                                String decBody = decryptMsg(body, packet.getStanzaId());
                                chatMessage.setMsg_body(decBody);
                                messageCallback(chatMessage);
                            }
                        }
                    }
                }
            }
        }, messageFilter);
    }

    private void handleStatus(String from, String status, String sender) {
        if (messageListener != null)
            messageListener.getTypingStatus(from, status, sender);
    }

    private void handleReceipt(ChatMessage chatMessage, Message receipt) {
        if ("update_profile".equals(receipt.getChatType())) {
            if (rosterListener != null)
                rosterListener.userProfileUpdateCallback(chatMessage);
        } else {
            messageAcknowledgementCallback(chatMessage);
        }
    }

    private void messageSeenCallback(ChatMessage chatMessage) {
        if (messageListener != null)
            messageListener.messageSeenCallback(chatMessage);
    }

    private void messageReceiptCallback(ChatMessage chatMessage) {
        if (messageListener != null)
            messageListener.messageReceiptCallback(chatMessage);
    }

    private void messageAcknowledgementCallback(ChatMessage chatMessage) {
        if (messageListener != null)
            messageListener.messageAcknowledgementCallback(chatMessage);
    }

    private void messageCallback(ChatMessage chatMessage) {
        if (messageListener != null)
            messageListener.messageCallback(chatMessage);
    }

    private void handleGroupNotification(ChatMessage chatMessage, Message message) {
        String status = message.getStatus();
        if ("new_user".equals(status))
            groupChatNewUser(chatMessage);
        else if ("user_removed".equals(status))
            groupChatRemoveUser(chatMessage);
        else if ("group_update".equals(status)) {
            chatMessage.setGroupName(message.getGroupName());
            chatMessage.setGroupImage(message.getGroupImage());
            groupChatInfoUpdate(chatMessage);
        }
    }

    private void groupChatNewUser(ChatMessage chatMessage) {
        if (groupListener != null)
            groupListener.groupChatNewUser(chatMessage);
    }

    private void groupChatRemoveUser(ChatMessage chatMessage) {
        if (groupListener != null)
            groupListener.groupChatRemoveUser(chatMessage);
    }

    private void groupChatInfoUpdate(ChatMessage chatMessage) {
        if (groupListener != null)
            groupListener.groupChatInfoUpdate(chatMessage);
    }

    /**
     * * Encrypt msg. * * @param msg * the msg * @param msgId * the msg id * @return
     * the string
     */
    private String encryptMsg(String msg, String msgId) {

        try {
            CryptLib _crypt = new CryptLib();
            String output = "";
            String key = CryptLib.SHA256(msgId, 32); //32 bytes = 256 bit
            //String iv = CryptLib.generateRandomIV(16); //16 bytes = 128 bit
            String iv = "ddc0f15cc2c90fca";
            output = _crypt.encrypt(msg, key, iv); //encrypt
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }

    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.d("xmpp", "Connected!");
            connected = true;
            login();
        }

        @Override
        public void connectionClosed() {
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            loggedin = false;
            if (loginListener != null)
                loginListener.onConnectionClosed();
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            Log.d("xmpp", "ConnectionClosedOn Error!");
            connected = false;

            chat_created = false;
            loggedin = false;
            if (loginListener != null)
                loginListener.onConnectionError();
        }

        @Override
        public void reconnectingIn(int arg0) {
            Log.d("xmpp", "Reconnectingin " + arg0);
            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;

//            ChatManager.getInstanceFor(connection).addChatListener(mChatManagerListener);

            chat_created = false;

            if (loginListener != null)
                loginListener.onLogin();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}