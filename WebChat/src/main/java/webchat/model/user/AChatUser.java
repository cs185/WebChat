package webchat.model.user;

import org.eclipse.jetty.websocket.api.Session;
import webchat.model.group.AChatGroup;
import webchat.model.group.GroupFac;
import webchat.model.message.AMessage;
import webchat.model.user.msgStrat.AStrategy;
import webchat.model.user.msgStrat.MsgStratFac;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AChatUser{
    protected String id;
    protected String name;
    protected int status;
    protected AStrategy msgStrategy;
    protected String targetUserId;
    protected Session chatSession;

    public Session getChatSession() {
        return chatSession;
    }

    public void setChatSession(Session chatSession) {
        this.chatSession = chatSession;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public AStrategy getMsgStrategy() {
        return msgStrategy;
    }

    public void setMsgStrategy(String type) {
        if (type.equals(getMsgStrategy().getName()))
            return;
        msgStrategy = MsgStratFac.make().makeStrat(type);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the user's display name.
     *
     * @return The display name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Send a message to the chat.
     *
     * @param message The message to be sent.
     */
    public abstract void sendMessage(String message, String groupId);

    public abstract AChatGroup createChatGroup(String name);

    /**
     * Join a chat room.
     *
     * @param GroupId The ID of the chat room to join.
     */
    public abstract void joinChatGroup(String GroupId);

    /**
     * Leave a chat room.
     *
     * @param GroupId The ID of the chat room to leave.
     */
    public abstract void leaveChatGroup(String GroupId);

    /**
     * Get the status of the user (e.g., online, offline, away).
     *
     * @return The current status of the user.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the status of the user (e.g., online, offline, away).
     *
     * @param status The new status of the user.
     */
    public void setStatus(int status) {
        this.status = status;
    }


    public abstract HashMap<String, String> getJoinedGroups();

    public abstract void sendPeronalMessage(String message);
}
