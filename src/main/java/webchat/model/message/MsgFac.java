package webchat.model.message;

import webchat.model.user.AChatUser;

public class MsgFac implements IMsgFac{
    private static MsgFac ONLY;
    private static int nextId = 1;
    private static final String pre = "message";

    /**
     * Constructor.
     */
    private MsgFac() {

    }

    /**
     * Only makes 1 vertical strategy.
     * @return The vertical strategy
     */
    public static MsgFac make() {
        if (ONLY == null ) {
            ONLY = new MsgFac();
        }
        return ONLY;
    }

    @Override
    public AMessage makeMsg(String type, String content, AChatUser sender) {
        String id = pre + nextId++;
        switch (type) {
            default:
                return new GeneralMessage(id, content, sender);
            case "emphasis":
                return new EmphasisMessage(id, content, sender);
            case "anonymous":
                return new AnonymousMessage(id, content, sender);
            case "important":
                return new ImportantMessage(id, content, sender);
        }
    }
}
