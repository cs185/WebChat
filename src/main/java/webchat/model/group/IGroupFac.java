package webchat.model.group;

import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;

public interface IGroupFac {
    public AChatGroup makeGroup(String name, AChatUser owner);
}
