package webchat.adapter;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webchat.model.service.DBService;
import webchat.model.user.AChatUser;

import java.util.List;
import java.util.Map;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketAdapter {

    /**
     * Open user's session.
     * @param session The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        Map<String, String> group_user_ids = getIdsFromSession(session);

        if (group_user_ids != null && group_user_ids.containsKey("groupId"))
            DBService.addGroupSession(group_user_ids.get("groupId"), session);
        else if (group_user_ids != null) {
            AChatUser user = DBService.getIdUser(group_user_ids.get("userId"));
            user.setChatSession(session);
        }


    }

    /**
     * Close the user's session.
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Map<String, String> group_user_ids = getIdsFromSession(session);
        if (group_user_ids != null && group_user_ids.containsKey("groupId"))
            DBService.removeGroupSession(group_user_ids.get("groupId"), session);

    }

    /**
     * Send a message.
     * @param session  The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        Map<String, String> group_user_ids = getIdsFromSession(session);

        AChatUser sender = DBService.getIdUser(group_user_ids.get("userId"));
        if (group_user_ids.containsKey("groupId")) {
            sender.sendMessage(message, group_user_ids.get("groupId"));
            sender.setMsgStrategy("general");
        }

        else {
            sender.sendPeronalMessage(message);
        }

    }

    public Map<String, String> getIdsFromSession(Session session) {
        Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();

        List<String> groupIds = params.get("groupId");
        List<String> userIds = params.get("userId");

        String groupId = groupIds != null && !groupIds.isEmpty() ? groupIds.get(0) : null;
        String userId = userIds != null && !userIds.isEmpty() ? userIds.get(0) : null;
        if (groupId != null && userId != null)
            return Map.of("groupId", groupId, "userId", userId);
        else if (groupId != null) {
            return Map.of("groupId", groupId);
        } else if (userId != null) {
            return Map.of("userId", userId);
        }
        return null;
    }
}
