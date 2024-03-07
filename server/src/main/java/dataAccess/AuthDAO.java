package dataAccess;

import dataAccess.Exception.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void deleteAllAuths() throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    AuthData getAuthData(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);
}
