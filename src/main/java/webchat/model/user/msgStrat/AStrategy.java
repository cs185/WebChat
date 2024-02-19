package webchat.model.user.msgStrat;

import webchat.model.group.AChatGroup;
import webchat.model.group.GroupFac;
import webchat.model.message.AMessage;
import webchat.model.message.MsgFac;
import webchat.model.user.AChatUser;
import webchat.model.service.DBService;

public class AStrategy implements IMsgStrategy{
    protected String type;

    /**
     * Get the strategy name.
     * @return strategy name
     */
    public String getName() {
        return type;
    }

    /**
     * Update the ball state in the ball world.
     * @param context The ball to update
     */
    public void send(AChatUser context, String msg, String groupId) {
        AMessage message = MsgFac.make().makeMsg(getName(), msg, context);
        AChatGroup group = GroupFac.make().getGroup(groupId);
        DBService.addMsgToGroup(group.getId(), message, context.getId());
        group.broadcastMessage(message);
    }
}
