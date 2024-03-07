package dataAccess;

import dataAccess.Exception.DataAccessException;
import model.UserData;

public interface UserDAO {
    void deleteAllUsers();

    UserData getUser(String username) throws DataAccessException;

    void createUser(String username,String password, String email) throws DataAccessException;
}
