package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void deleteAllAuths();

    String createAuth(String username);

    AuthData getAuthData(String authToken);
}
