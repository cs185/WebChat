package webchat.model.service;

import spark.Request;
import spark.Session;
import webchat.model.user.AChatUser;

public class AccountService {
    public static boolean authenticate(String username, String password) {
        return DBService.matchUser(username, password);
    }

    public static boolean authenticate(Session session) {
        return session.attribute("userId") != null;
    }

    public static boolean register(String username, String name, String password) {
        if (!DBService.existUser(username)) {
            DBService.addUser(username, name, password);
            return true;
        }
        return false;
    }

    public static AChatUser getUserFromReq(Request req) {
        String userId = req.session().attribute("userId");
        return DBService.getIdUser(userId);
    }
}
