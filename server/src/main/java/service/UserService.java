package service;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    private final UserDAO userAccess;
    private final AuthDAO authAccess;

    public UserService(UserDAO userAccess, AuthDAO authAccess) {
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public AuthData registration(String username, String password, String email) throws HttpException {
        if(username == null || password == null || email == null){
            throw new HttpException("Error: bad request",400);
        }
        if(userAccess.getUser(username) != null){
            throw new HttpException("Error: already taken",403);
        }

        try {
            userAccess.createUser(username, password, email);
            String authToken = authAccess.createAuth(username);

            return authAccess.getAuthData(authToken);
        } catch (Exception e) {
            throw new HttpException("Error: description", 500);
        }

    }

    public AuthData login(String username, String password) throws HttpException{
        UserData user = null;

        try {
            user = userAccess.getUser(username);
        } catch (Exception e) {
            throw new HttpException("Error: server error", 500);
        }

        if(user == null || !Objects.equals(user.password(), password)){
            throw new HttpException("Error: unauthorized",401);
        }

        try {
            String authToken = authAccess.createAuth(username);

            return authAccess.getAuthData(authToken);
        } catch (Exception e) {
            throw new HttpException("Error: description", 500);
        }
    }
}
