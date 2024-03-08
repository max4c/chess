package dataAccessTests;
import dataAccess.MySqlGameDAO;
import dataAccess.MySqlUserDAO;
import dataAccess.MySqlAuthDAO;
import dataAccess.Exception.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    static final MySqlGameDAO gameAccess = new MySqlGameDAO();
    static final MySqlUserDAO userAccess = new MySqlUserDAO();
    static final MySqlAuthDAO authAccess = new MySqlAuthDAO();

    @BeforeEach
    void clear() throws Exception{
        gameAccess.deleteAllGames();
        userAccess.deleteAllUsers();
        authAccess.deleteAllAuths();
    }

    @Test
    @DisplayName("Positive Del All Users")
    public void posDelAllUsers() throws Exception{
        userAccess.createUser("username","password","email");
        userAccess.deleteAllUsers();
        assertNull(userAccess.getUser("username"));
    }

    @Test
    @DisplayName("Positive Del All Auths")
    public void posDelAllAuths() throws Exception{
        String authToken = authAccess.createAuth("username");
        authAccess.deleteAllAuths();
        assertNull(authAccess.getAuthData(authToken));
    }

    @Test
    @DisplayName("Positive Del All Games")
    public void posDelAllGames() throws Exception{
        int gameID = gameAccess.createGame("gameName");
        gameAccess.deleteAllGames();
        assertNull(gameAccess.getGame(gameID));
    }

    @Test
    @DisplayName("Positive Get User")
    public void posGetUser() throws Exception{
        userAccess.createUser("username","password","email");
        assertEquals("username",userAccess.getUser("username").username());
    }

    @Test
    @DisplayName("Positive Get Auth")
    public void posGetAuth() throws Exception{
        String authToken = authAccess.createAuth("username");
        assertEquals("username",authAccess.getAuthData(authToken).username());
    }

    @Test
    @DisplayName("Positive Get Game")
    public void posGetGame() throws Exception{
        int gameID = gameAccess.createGame("gameName");
        assertEquals("gameName",gameAccess.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Negative Get User")
    public void negGetUser() throws Exception{
        userAccess.createUser("username","password","email");
        assertNotEquals("newUsername",userAccess.getUser("username").username());
    }

    @Test
    @DisplayName("Negative Get Auth")
    public void negGetAuth() throws Exception{
        String authToken = authAccess.createAuth("username");
        assertNotEquals("newUsername",authAccess.getAuthData(authToken).username());
    }

    @Test
    @DisplayName("Negative Get Game")
    public void negGetGame() throws Exception{
        int gameID = gameAccess.createGame("gameName");
        assertNotEquals("newGameName",gameAccess.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Positive Create User")
    public void posCreateUser() throws Exception{
        userAccess.createUser("username","password","email");
        assertNotNull(userAccess.getUser("username"));
    }

    @Test
    @DisplayName("Positive Create Auth")
    public void posCreateAuth() throws Exception{
        String authToken = authAccess.createAuth("username");
        assertNotNull(authAccess.getAuthData(authToken));
    }

    @Test
    @DisplayName("Positive Create Game")
    public void posCreateGame() throws Exception{
        int gameID = gameAccess.createGame("gameName");
        assertNotNull(gameAccess.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Negative Create User")
    public void negCreateUser() throws Exception{
        userAccess.createUser("username","password","email");
        assertNotEquals("password",userAccess.getUser("username").password());
    }

    @Test
    @DisplayName("Negative Create Auth")
    public void negCreateAuth() throws Exception{
        String authToken = authAccess.createAuth("username");
        assertNotEquals("authToken",authAccess.getAuthData(authToken).authToken());
    }

    @Test
    @DisplayName("Negative Create Game")
    public void negCreateGame() throws Exception{
        int gameID = gameAccess.createGame("gameName");
        assertNotEquals(2,gameAccess.getGame(gameID).gameID());
    }

    @Test
    @DisplayName("Positive Del Auth")
    public void posDeAuth() throws Exception{
        String authToken = authAccess.createAuth("username");
        authAccess.deleteAuth(authToken);
        assertNull(authAccess.getAuthData(authToken));
    }

    @Test
    @DisplayName("Negative Del Auth")
    public void negDeAuth() throws Exception{
        String authToken = authAccess.createAuth("username");
        authAccess.deleteAuth("pizza");
        assertNotNull(authAccess.getAuthData(authToken));
    }

    @Test
    @DisplayName("Positive List Games")
    public void posListGames() throws Exception{
        int gameID = gameAccess.createGame("gameName");
        Collection<GameData> games = gameAccess.listGames();
        assertEquals(1,games.size());
        assertEquals(gameID,gameAccess.getGame(gameID).gameID());
    }

    @Test
    @DisplayName("Negative List Games")
    public void negListGames() throws Exception{
        gameAccess.createGame("gameName");
        Collection<GameData> games = gameAccess.listGames();
        Iterator<GameData> iterator = games.iterator();
        GameData firstGame = iterator.next();
        assertNotEquals(2,firstGame.gameID());
    }


}
