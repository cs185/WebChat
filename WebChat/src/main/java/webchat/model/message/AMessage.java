package webchat.model.message;

import webchat.model.group.AChatGroup;
import webchat.model.user.AChatUser;

public abstract class AMessage implements IMessage{
    protected String id;
    protected String type;

    public String getType() {
        return type;
    }

    protected String content;
    protected AChatUser sender;
    protected AChatGroup group;
    protected String senderName;

    protected AMessage(String id, String content, AChatUser sender) {
        this.id = id;
        this.content = content;
        this.sender = sender;
    }

    protected AMessage(String id, String content, String senderName) {
        this.id = id;
        this.content = content;
        this.senderName = senderName;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public AChatUser getSender() {
        return sender;
    }

    public abstract String render();
}
