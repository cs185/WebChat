package webchat.model.message;

import j2html.TagCreator;
import webchat.model.group.AChatGroup;
import webchat.model.user.AChatUser;
import webchat.model.user.msgStrat.GeneralStrategy;

public class GeneralMessage extends AMessage{
    public GeneralMessage(String id, String content, AChatUser sender) {
        super(id, content, sender);
        this.type = "general";
    }

    public String render() {
        return TagCreator.p(sender.getName() + " says: " + content).render();
    }

}
