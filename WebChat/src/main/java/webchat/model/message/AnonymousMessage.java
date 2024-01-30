package webchat.model.message;

import j2html.TagCreator;
import webchat.model.user.AChatUser;

public class AnonymousMessage extends AMessage{
    public AnonymousMessage(String id, String content, AChatUser sender) {
        super(id, content, sender);
        this.type = "anonymous";
    }

    public String render() {
        return TagCreator.p(" says: " + content).render();
    }
}
