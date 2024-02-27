package dataAccess;

import model.UserData;

public interface UserDAO {
    void deleteAllUsers();

    UserData getUser(String username);

    void createUser(String username,String password, String email);
}
