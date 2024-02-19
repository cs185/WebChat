package webchat.model.group;

import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;

public class GeneralChatGroup extends AChatGroup{
    public GeneralChatGroup(String id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }
}
