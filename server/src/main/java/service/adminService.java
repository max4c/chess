package service;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;


public class adminService {
    private final GameDAO gameAccess;
    private final UserDAO userAccess;
    private final AuthDAO authAccess;

    public adminService(GameDAO gameAccess, UserDAO userAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public void clear() {
        gameAccess.deletaAllGames();
    }
}
