package webchat.model.user.msgStrat;

public class AnonymousStrategy extends AStrategy{
    protected static AnonymousStrategy ONLY;
    private static final String type = "anonymous";
    public AnonymousStrategy() {
    }

    public static AnonymousStrategy make() {
        if (ONLY == null ) {
            ONLY = new AnonymousStrategy();
        }
        return ONLY;
    }

    @Override
    public String getName() {
        return type;
    }
}
