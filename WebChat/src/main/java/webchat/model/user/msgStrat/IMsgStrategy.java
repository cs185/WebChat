package webchat.model.user.msgStrat;

import webchat.model.group.AChatGroup;
import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;

public interface IMsgStrategy {
    /**
     * Get the strategy name.
     * @return The strategy name
     */
    public String getName();

    /**
     * Update the line state using the behavior defined by the strategy.
     * @param context  The moving line to update
     */
    public void send(AChatUser context, String msg, String groupId);
}
