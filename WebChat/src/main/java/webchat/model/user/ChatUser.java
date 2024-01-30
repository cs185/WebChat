package webchat.model.user;

import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import webchat.model.group.AChatGroup;
import webchat.model.group.GroupFac;
import webchat.model.message.AMessage;
import webchat.model.message.MsgFac;
import webchat.model.service.DBService;
import webchat.model.user.msgStrat.MsgStratFac;

import java.sql.SQLException;
import java.util.HashMap;

public class ChatUser extends AChatUser{
    public ChatUser(String id, String name) {
        this.id = id;
        this.name = name;
        msgStrategy = MsgStratFac.make().makeStrat();
        targetUserId = id;
    }

    @Override
    public void sendMessage(String message, String groupId) {
        msgStrategy.send(this, message, groupId);
    }

    @Override
    public AChatGroup createChatGroup(String name) {
        AChatGroup group = GroupFac.make().makeGroup(name, this.getId());  // 这是空值？
        DBService.addGroup(group.getId(), name, this.getId());
        group.addUser(this.getId());
        return group;
    }

    @Override
    public void joinChatGroup(String groupId) {
        GroupFac.make().getGroup(groupId).addUser(this.getId());
    }

    @Override
    public void leaveChatGroup(String groupId) {
        GroupFac.make().getGroup(groupId).deleteUser(this);
    }

    @Override
    public HashMap<String, String> getJoinedGroups() {
        return DBService.getUserGroups(this.getId());
    }

    @Override
    public void sendPeronalMessage(String msg) {
        AMessage message = MsgFac.make().makeMsg("general", msg, this);

        Session session = DBService.getIdUser(targetUserId).getChatSession();
        try {
            JsonObject jo = new JsonObject();
            String htmlContent = message.render();
            jo.addProperty("userMessage", htmlContent);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
