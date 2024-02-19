package webchat.controller;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Session;
import spark.template.velocity.VelocityTemplateEngine;
import webchat.adapter.WebSocketAdapter;
import static webchat.model.service.AccountService.*;

import webchat.model.group.AChatGroup;
import webchat.model.group.GroupFac;
import webchat.model.service.DBService;
import webchat.model.user.AChatUser;
import webchat.model.user.ChatUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {

    /**
     * Chat App entry point.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        webSocketIdleTimeoutMillis(3600000);
        webSocket("/chatapp", WebSocketAdapter.class);
        init();

        Gson gson = new Gson();

        get("/login", (req, res) -> {
            if (req.session().attribute("userId") != null)
                res.redirect("/chat");

            Map<String, Object> model = new HashMap<>();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "/public/login.html") // 'template.vm' is the file name in 'src/main/resources/templates'
            );
        });

        get("/register", (req, res) -> {
            if (req.session().attribute("userId") != null)
                res.redirect("/chat");

            Map<String, Object> model = new HashMap<>();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "/public/register.html") 
            );
        });

        get("/chat", (req, res) -> {
            Session session = req.session();
            if (!authenticate(session)) {
                res.status(401); // Unauthorized
                res.redirect("/login");
                return gson.toJson("Invalid credentials");
            }

            AChatUser user = getUserFromReq(req);
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getName());
            model.put("userId", user.getId());
            model.put("joinedGroups", user.getJoinedGroups());
//            model.put("allOtherGroups", DBService.getAllOtherGroups(user));
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "/public/chat.html")
            );
        });

        post("/chat/create", (req, res) -> {
            String groupName = req.queryParams("groupName");
            AChatUser creator = getUserFromReq(req);
            AChatGroup group = creator.createChatGroup(groupName);

            return gson.toJson(group.getId());
        });

        post("/chat/join", (req, res) -> {
            String groupId = req.queryParams("groupId");
            AChatUser joiner = getUserFromReq(req);
            HashMap<String, String> joinedGroups = DBService.getUserGroups(joiner.getId());
            if (joinedGroups.containsKey(groupId)) {
                res.status(400);
                return gson.toJson("");
            }
            joiner.joinChatGroup(groupId);

            return gson.toJson("Group Created");
        });

        post("/chat/leave", (req, res) -> {
            String groupId = req.queryParams("groupId");
            AChatUser leaver = getUserFromReq(req);
            leaver.leaveChatGroup(groupId);

            if (DBService.getGroup(groupId).getMembers().length == 0)
                DBService.clearGroup(groupId);

            return gson.toJson("Group left");
        });

        post("/chat/send", (req, res) -> {
            String targetUserId = req.queryParams("targetUserId");
            AChatUser sender = getUserFromReq(req);
            if (DBService.getIdUser(targetUserId) == null) {
                res.status(400);
                return gson.toJson("User not exist or not online");
            }

            sender.setTargetUserId(targetUserId);
            return gson.toJson("target user set, waiting to send");
        });

        get("/logout", (req, res) -> {
            Session session = req.session();
            if (authenticate(session)) {
                String userId = session.attribute("userId");
                session.removeAttribute("userId");
                DBService.removeIdUser(userId);
            }

            res.redirect("/login");
            return gson.toJson("Invalid credentials");
        });

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if (authenticate(username, password)) {
                AChatUser user = DBService.getUser(username);
                DBService.addIdUser(username, user);
                Session session = req.session();

                if (session.attribute("userId") == null)
                    session.attribute("userId", user.getId()); // Store user in session

                return gson.toJson("Login Success");
            } else {
                res.status(401); // Unauthorized
                return gson.toJson("Invalid credentials");
            }
        });

        post("/register", (req, res) -> {
            String username = req.queryParams("username");
            String name = req.queryParams("name");
            String password = req.queryParams("password");
            boolean status = register(username, name, password);

            if (status) {
                return gson.toJson("Register Success");
            } else {
                // Set a bad request status and return a message
                res.status(400); // HTTP 400 Bad Request
                return gson.toJson("Username already exists");
            }
        });

        get("/group/:groupId", (req, res) -> {
            String groupId = req.params(":groupId");
            // Now you have the groupId, you can perform actions based on this ID
            // For example, fetch the chat group details from the database
            AChatGroup group = GroupFac.make().getGroup(groupId);
            String[] members = group.getMembers();

            if (group != null) {
                AChatUser user = getUserFromReq(req);
                Map<String, Object> model = new HashMap<>();
                model.put("groupName", group.getName());
                model.put("groupId", group.getId());
                model.put("userId", user.getId());
                model.put("members", members);
                model.put("messageLog", group.getMessages());
                return new VelocityTemplateEngine().render(
                        new ModelAndView(model, "/public/group.html"));
            } else {
                res.status(404);
                return "Chat Group not found";
            }
        });

        post("/group/type", (req, res) -> {
            String messageType = req.queryParams("type");
            AChatUser sender = getUserFromReq(req);
            sender.setMsgStrategy(messageType);
            return gson.toJson("message send");
        });
    }

    /**
     * Get the heroku assigned port number.
     * @return The heroku assigned port number
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set.
    }
}
