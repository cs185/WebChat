package webchat.model.user;
import spark.Session;

/**
 * Defines the behavior of a user in a chat application.
 */
public interface IChatUser {

    /**
     * Get the user's unique identifier.
     *
     * @return The unique ID of the user.
     */
    String getId();
    void setId(String id);

    String getName();
    void setName(String name);
    Session getSession();
    void setSession(Session session);

    String getPwd();
    void setPwd(String pwd);
}

