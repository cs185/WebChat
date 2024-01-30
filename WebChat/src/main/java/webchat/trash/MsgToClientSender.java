//package webchat.trash;
//
//import com.google.gson.JsonObject;
//import j2html.TagCreator;
//
//
///**
// * Send messages to the client.
// */
//public class MsgToClientSender {
//
//    /**
//     * Broadcast message to all users.
//     * @param sender  The message sender.
//     * @param message The message.
//     */
//    public static void broadcastMessage(String sender, String message) {
//        UserDB.getSessions().forEach(session -> {
//            try {
//                JsonObject jo = new JsonObject();
//                // TODO: use .addProperty(key, value) add a JSON object property that has a key "userMessage"
//                //  and a j2html paragraph value
//                String htmlContent = TagCreator.p(sender + " says: " + message).render();
//                jo.addProperty("userMessage", htmlContent);
//                System.out.println(jo);
//                session.getRemote().sendString(String.valueOf(jo));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        });
//    }
//}
