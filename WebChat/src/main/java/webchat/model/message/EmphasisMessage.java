package webchat.model.message;

import static j2html.TagCreator.*;
import webchat.model.group.AChatGroup;
import webchat.model.user.AChatUser;

public class EmphasisMessage extends AMessage{
    public EmphasisMessage(String id, String content, AChatUser sender) {
        super(id, content, sender);
        this.type = "emphasis";
    }

    public String render() {
        return p(b(sender.getName() + " says: " + content)).render();
    }
}
