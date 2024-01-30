package webchat.model.group;

import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;

public interface IChatGroup {
    String getId();
    void setId(String id);

    void addUser(AChatUser user);

    void deleteUser(AChatUser user);
    void clearUser();
    String[] getMessages();
    void addMessage(AMessage msg);
    void clearMessage();
}
