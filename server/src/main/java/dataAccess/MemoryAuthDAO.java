package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void deleteAllAuths() {
        auths.clear();
    }

    @Override
    public String createAuth(String username) {
        String authToken =  UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken,username);
        auths.put(authToken,newAuth);

        return authToken;
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }
}
