package webchat.model.message;

import webchat.model.user.AChatUser;

public interface IMsgFac {
    public AMessage makeMsg(String type, String content, AChatUser sender);
}
