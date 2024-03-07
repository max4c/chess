package dataAccess;

import dataAccess.Exception.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void deleteAllAuths() throws DataAccessException;

    String createAuth(String username);

    AuthData getAuthData(String authToken);
    void deleteAuth(String authToken);
}
