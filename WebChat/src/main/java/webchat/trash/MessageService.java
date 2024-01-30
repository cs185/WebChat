//package webchat.trash;
//
//import com.google.gson.JsonObject;
//import org.eclipse.jetty.websocket.api.Session;
//import webchat.model.group.AChatGroup;
//import webchat.model.message.AMessage;
//import webchat.model.service.DBService;
//
//public class MessageService {
//    public static void broadcastMessage(String groupId, AMessage message) {
//        DBService.getGroupSessions(groupId).stream().filter(Session::isOpen).forEach(session -> {
//            try {
//                JsonObject jo = new JsonObject();
//                String htmlContent = message.render();
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
