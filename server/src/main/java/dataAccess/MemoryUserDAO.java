package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    final private HashMap<Integer, UserData> users = new HashMap<>();
    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
