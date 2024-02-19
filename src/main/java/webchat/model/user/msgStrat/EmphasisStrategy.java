package webchat.model.user.msgStrat;

public class EmphasisStrategy extends AStrategy{
    protected static EmphasisStrategy ONLY;
    private static final String type = "emphasis";
    public EmphasisStrategy() {
    }

    public static EmphasisStrategy make() {
        if (ONLY == null ) {
            ONLY = new EmphasisStrategy();
        }
        return ONLY;
    }

    @Override
    public String getName() {
        return type;
    }
}
