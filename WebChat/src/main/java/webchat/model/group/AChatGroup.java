package webchat.model.group;

import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;
import webchat.model.service.DBService;

import java.sql.SQLException;

public abstract class AChatGroup{
    protected String id;
    protected String name;
    protected String owner;
    protected int count = 0;

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
    public String[] getMembers() {
        return DBService.getGroupMembers(getId());
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getMessages() {
        return DBService.getGroupMessage(getId());
    }

    public void addUser(String user_id){
        DBService.addUserToGroup(user_id, getId());
        count++;
    }

    public void deleteUser(AChatUser user) {
        DBService.DeleteUserFromGroup(user.getId(), getId());
        count--;
    }

    public void clearUser() {
        DBService.clearGroup(getId());
        count = 0;
    }
//
//    public void addMessage(AMessage msg) {
//        DBService.addMsgToGroup(this, msg);
//    }

    public void clearMessage() {
        DBService.clearMsgFromGroup(getId());
    }

    public void broadcastMessage(AMessage message) {
        DBService.getGroupSessions(getId()).stream().filter(Session::isOpen).forEach(session -> {
            try {
                JsonObject jo = new JsonObject();
                String htmlContent = message.render();
                jo.addProperty("userMessage", htmlContent);
                session.getRemote().sendString(String.valueOf(jo));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
