package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData getUser(String username){return users.get(username);}

    @Override
    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        users.put(username,newUser);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
