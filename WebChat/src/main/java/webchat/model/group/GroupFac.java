package webchat.model.group;

import webchat.model.user.AChatUser;
import webchat.model.service.DBService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupFac {
    private static GroupFac ONLY;
    private static int nextId = 1;
    private static HashMap<String, AChatGroup> cacheGroups;
    private static final String pre = "group";

    private GroupFac() {
        cacheGroups = new HashMap<>();
    }

    public static GroupFac make() {
        if (ONLY == null ) {
            ONLY = new GroupFac();
        }
        return ONLY;
    }

    public AChatGroup getGroup(String groupId) {
        AChatGroup group = cacheGroups.get(groupId);
        if (group == null) {
            group = DBService.getGroup(groupId);
            cacheGroups.put(groupId, group);
        }
        return group;
    }

    public AChatGroup makeGroup(String name, String owner_id){
        String id = pre + nextId++;
        AChatGroup group = new GeneralChatGroup(id, name, owner_id);
//        DBService.addGroup(id, name, owner_id);
        cacheGroups.put(id, group);
        return group;
    }

    public AChatGroup makeGroup(String id, String name, String owner_id){
        AChatGroup group = new GeneralChatGroup(id, name, owner_id);
        cacheGroups.put(id, group);
        return group;
    }
}
