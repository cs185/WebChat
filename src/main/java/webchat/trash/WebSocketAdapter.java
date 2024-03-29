//package webchat.trash;
//
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
//import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webchat.trash.MsgToClientSender;
//import webchat.trash.UserDB;
//
///**
// * Create a web socket for the server.
// */
//@WebSocket
//public class WebSocketAdapter {
//
//    /**
//     * Open user's session.
//     * @param session The user whose session is opened.
//     */
//    @OnWebSocketConnect
//    public void onConnect(Session session) {
//        String username = "User" + UserDB.genNextUserId();
//        System.out.println(username);
//        UserDB.addSessionUser(session, username);
//    }
//
//    /**
//     * Close the user's session.
//     * @param session The use whose session is closed.
//     */
//    @OnWebSocketClose
//    public void onClose(Session session, int statusCode, String reason) {
//        UserDB.removeUser(session);
//    }
//
//    /**
//     * Send a message.
//     * @param session  The session user sending the message.
//     * @param message The message to be sent.
//     */
//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) {
//        // TODO: broadcast the message to all clients
//        String userId = UserDB.getUser(session);
//        System.out.println(message);
//        MsgToClientSender.broadcastMessage(userId, message);
//    }
//}
