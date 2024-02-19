package webchat.model.user.msgStrat;

import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;

public interface IMsgStratFac {
    public AStrategy makeStrat(String type);
}
