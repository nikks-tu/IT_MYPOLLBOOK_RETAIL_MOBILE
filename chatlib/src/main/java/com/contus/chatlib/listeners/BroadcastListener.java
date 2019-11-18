package com.contus.chatlib.listeners;

/**
 * Created by user on 29/9/16.
 */
public interface BroadcastListener {
    void broadcastIdAdded(String groupId);

    void broadcastMemberAdded(String result, String resultType);

    void broadcastDeleteResponse(String response);

    void broadcastRemoveMemberResponse(String response);

    void broadcastInfoUpdatedResponse(String response);
}
