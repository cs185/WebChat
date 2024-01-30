package webchat.model.message;

import j2html.TagCreator;
import webchat.model.user.AChatUser;

public class ImportantMessage extends AMessage{
    public ImportantMessage(String id, String content, AChatUser sender) {
        super(id, content, sender);
        this.type = "important";
    }

    public ImportantMessage(String id, String content, String senderName) {
        super(id, content, senderName);
        this.type = "important";
    }

    public String render() {
        return TagCreator.p("~~~~~~~~~~!!!!!!!" + "[IMPORTANT]" + sender.getName()
                + " says: " + content + "!!!!!!!~~~~~~~~~~").render();
    }
}
