package webchat.model.user.msgStrat;

public class ImportantStrategy extends AStrategy{
    protected static ImportantStrategy ONLY;
    private static final String type = "important";
    public ImportantStrategy() {
    }

    public static ImportantStrategy make() {
        if (ONLY == null ) {
            ONLY = new ImportantStrategy();
        }
        return ONLY;
    }

    @Override
    public String getName() {
        return type;
    }
}
