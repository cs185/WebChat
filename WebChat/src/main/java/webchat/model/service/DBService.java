package webchat.model.service;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.jetty.websocket.api.Session;
import webchat.model.group.AChatGroup;
import webchat.model.message.AMessage;
import webchat.model.user.AChatUser;
import webchat.model.group.GroupFac;
import webchat.model.user.ChatUser;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class DBService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/chatapp_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "SJCbb991120!";
//    private static final Map<Session,AChatUser> sessionUserMap = new ConcurrentHashMap<>();
    private static final Map<String,Set<Session>> groupSessionsMap = new ConcurrentHashMap<>();

    private static final Map<String,AChatUser> idUserMap = new ConcurrentHashMap<>();

//    public static void main(String[] args) {
//        String[] res = getGroupMessage("test");
//        for (String r : res)
//            System.out.println(r);
//    }

    public static void addUserToGroup(String userId, String groupId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO user_group (user_id, group_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, userId);
                ps.setObject(2, groupId);
                int n = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void DeleteUserFromGroup(String userId, String groupId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "DELETE FROM user_group WHERE user_id  = ? AND group_id  = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, userId);
                ps.setObject(2, groupId);
                int n = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearGroup(String groupId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "DELETE FROM user_group WHERE group_id  = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, groupId);
                int n = ps.executeUpdate();
            }

            sql = "DELETE FROM chat_group WHERE group_id  = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, groupId);
                int n = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getGroupMessage(String groupId) {
        try (MongoClient mongoClient = new MongoClient( "localhost" , 27017 )){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("chatapp");
            MongoCollection<Document> collection = mongoDatabase.getCollection("chat_log");
            List<String> messages = new ArrayList<>();

            FindIterable<Document> documentCursor = collection.find(new Document("groupId", groupId))
                    .projection(new Document("message", 1).append("_id", 0))
                    .sort(new Document("date", 1));

            for (Document doc : documentCursor)
                messages.add(doc.getString("message"));

            return messages.toArray(new String[0]);

        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }

    public static String[] getGroupMembers(String groupId) {
        ArrayList<String> memberNames = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT t3.name FROM user t3 INNER JOIN (SELECT user_id FROM chat_group t1 INNER JOIN user_group t2 ON t1.group_id = t2.group_id WHERE t1.group_id = '%s') t4 ON t3.user_id = t4.user_id;", groupId);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        memberNames.add(rs.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return memberNames.toArray(new String[0]);
    }

    public static void addMsgToGroup(String groupId, AMessage msg, String senderId) {
        try (MongoClient mongoClient = new MongoClient( "localhost" , 27017 )){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("chatapp");
            MongoCollection<Document> collection = mongoDatabase.getCollection("chat_log");

            Document doc = new Document("date", new Date())
                    .append("groupId", groupId)
                    .append("senderId", senderId)
                    .append("message", msg.render())
                    .append("type", msg.getType());

            collection.insertOne(doc);

        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public static void clearMsgFromGroup(String groupId) {
    }

    public static AChatGroup getGroup(String groupId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT * FROM chat_group WHERE group_id = '%s'", groupId);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String id = rs.getString(1); // index start from 1
                        String name = rs.getString(2);
                        String owner = rs.getString(3);

                        return GroupFac.make().makeGroup(id, name, owner);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void addGroup(String groupId, String groupName, String owner_id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO chat_group (group_id, group_name, owner_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, groupId);
                ps.setObject(2, groupName);
                ps.setObject(3, owner_id);
                int n = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addUser(String username, String name, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO user (user_id, name, password) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, username);
                ps.setObject(2, name);
                ps.setObject(3, password);
                int n = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static AChatUser getUser(String username) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT * FROM user WHERE user_id = '%s'", username);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String id = rs.getString(1);
                        String name = rs.getString(2);
                        return new ChatUser(id, name);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean matchUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT * FROM user WHERE user_id  = '%s' AND password = '%s'", username, password);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean existUser(String username) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT * FROM user WHERE user_id = '%s'", username);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static HashMap<String, String> getUserGroups(String userId) {
        HashMap<String, String> groups = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT t2.group_id, t2.group_name FROM user_group t1 INNER JOIN chat_group t2 ON t1.group_id = t2.group_id WHERE t1.user_id = '%s'", userId);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        groups.put(rs.getString(1), rs.getString(2));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }

    public static Set<Session> getGroupSessions(String groupId) {
        return groupSessionsMap.get(groupId);
    }

    public static void addGroupSession(String groupId, Session session) {
        if (!groupSessionsMap.containsKey(groupId))
            groupSessionsMap.put(groupId, new HashSet<Session>());

        groupSessionsMap.get(groupId).add(session);
    }

    public static void removeGroupSession(String groupId, Session session) {
        if (!groupSessionsMap.containsKey(groupId))
            return;

        groupSessionsMap.get(groupId).remove(session);
    }

    public static AChatUser getIdUser(String userId) {
        return idUserMap.get(userId);
    }

    public static void addIdUser(String userId, AChatUser user) {
        idUserMap.put(userId, user);
    }

    public static void removeIdUser(String userId) {
        idUserMap.remove(userId);
    }

    public static String getUserName(String userId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                String sql = String.format("SELECT name FROM user WHERE user_id = '%s'", userId);
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
