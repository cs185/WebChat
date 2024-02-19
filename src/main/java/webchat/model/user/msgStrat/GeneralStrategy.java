package webchat.model.user.msgStrat;

public class GeneralStrategy extends AStrategy{
    protected static GeneralStrategy ONLY;
    private static final String type = "general";
    public GeneralStrategy() {
    }

    public static GeneralStrategy make() {
        if (ONLY == null ) {
            ONLY = new GeneralStrategy();
        }
        return ONLY;
    }

    @Override
    public String getName() {
        return type;
    }
}
