//package webchat.trash;
//
//import webchat.model.group.AChatGroup;
//import webchat.model.user.AChatUser;
//
//public class GroupService {
//    public static void groupCreation(AChatUser user, String name) {
//        AChatGroup group = user.createChatGroup(name);
//        // TODO: add a get method on the route "/chatroom/group/" + groupId in ChatAppController
//    }
//
//    public static void groupJoining(AChatUser user, String groupId) {
//        user.joinChatGroup(groupId);
//    }
//
//    public static void groupLeaving(AChatUser user, String groupId) {
//        user.leaveChatGroup(groupId);
//    }
//}
