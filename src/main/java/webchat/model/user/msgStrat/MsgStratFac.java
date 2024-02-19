package webchat.model.user.msgStrat;

import webchat.model.message.MsgFac;

public class MsgStratFac implements IMsgStratFac{
    private static MsgStratFac ONLY;
    private static final MsgFac msgFac = MsgFac.make();

    /**
     * Constructor.
     */
    private MsgStratFac() {

    }

    /**
     * Only makes 1 vertical strategy.
     * @return The vertical strategy
     */
    public static MsgStratFac make() {
        if (ONLY == null ) {
            ONLY = new MsgStratFac();
        }
        return ONLY;
    }

    @Override
    public AStrategy makeStrat(String type) {
        switch (type) {
            default:
                return GeneralStrategy.make();
            case "emphasis":
                return EmphasisStrategy.make();
            case "anonymous":
                return AnonymousStrategy.make();
            case "important":
                return ImportantStrategy.make();
        }
    }

    public AStrategy makeStrat() {
        return GeneralStrategy.make();
    }
}
