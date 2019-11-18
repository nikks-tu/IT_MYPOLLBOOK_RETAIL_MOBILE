package com.contus.chatlib.listeners;

import com.contus.chatlib.models.ChatMessage;
import com.contus.chatlib.models.Group;

import java.util.List;

/**
 * Created by user on 29/9/16.
 */
public interface GroupListener {
    void groupIdAdded(String groupId);

    void groupMemberAdded(String result, String resultType);

    void groupExitResponse(String response);

    void groupRemoveMemberResponse(String response);

    void groupInfoUpdatedResponse(String response);

    void groupChatNewUser(ChatMessage chatMessage);

    void groupChatRemoveUser(ChatMessage chatMessage);

    void groupChatInfoUpdate(ChatMessage chatMessage);

    void groupsCallback(List<Group> groups);

    void groupCallback(Group group);
}
