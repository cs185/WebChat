package webchat.model.message;

import webchat.model.user.AChatUser;

public interface IMessage {

    String getId();

    String getContent();

    AChatUser getSender();
}
