package service;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;


public class ClearService {
    private final GameDAO gameAccess;
    private final UserDAO userAccess;
    private final AuthDAO authAccess;

    public ClearService(GameDAO gameAccess, UserDAO userAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public void clear() throws HttpException{
        try {
            gameAccess.deleteAllGames();
            userAccess.deleteAllUsers();
            authAccess.deleteAllAuths();
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }
    }
}
