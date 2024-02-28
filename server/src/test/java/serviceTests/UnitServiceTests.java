package serviceTests;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.HttpException;
import service.UserService;
import service.GameService;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class UnitServiceTests {
    static final MemoryGameDAO gameAccess = new MemoryGameDAO();
    static final MemoryUserDAO userAccess = new MemoryUserDAO();
    static final MemoryAuthDAO authAccess = new MemoryAuthDAO();
    static final ClearService clearService = new ClearService(gameAccess, userAccess, authAccess);
    static final UserService userService = new UserService(userAccess, authAccess);
    static final GameService gameService = new GameService(gameAccess, authAccess);
    @BeforeEach
    void clear() throws Exception{
        clearService.clear();
    }

    @Test
    @DisplayName("Positive Registration")
    public void posRegistration() throws Exception{
        AuthData authData = userService.registration("username","password","email");
        assertEquals(authAccess.getAuthData(authData.authToken()).username(),userAccess.getUser("username").username());
    }

    @Test
    @DisplayName("Negative Registration")
    public void negRegistration() throws Exception{
        try {
            userService.registration("username", null, "email");
        }
        catch(HttpException e){
            Assertions.assertEquals(400, e.getStatusCode());
        }
    }

    @Test
    @DisplayName("Positive Login")
    public void posLogin() throws Exception{
        AuthData authData1 = userService.registration("username","password","email");
        AuthData authData2 = userService.login("username","password");

        assertEquals(authData1.username(), authData2.username());
    }

    @Test
    @DisplayName("Negative Login")
    public void negLogin() throws Exception{
        userService.registration("username","password","email");
        try {
            userService.login("username", null);
        }
        catch(HttpException e){
            Assertions.assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @DisplayName("Positive Logout")
    public void posLogout() throws Exception{
        userService.registration("username","password","email");
        AuthData authData = userService.login("username","password");
        userService.logout(authData.authToken());
    }

    @Test
    @DisplayName("Negative Logout")
    public void negLogout() throws Exception{
        userService.registration("username","password","email");
        AuthData authData = userService.login("username","password");
        userService.logout(authData.authToken());
        try{
            userService.logout(authData.authToken());
        } catch (HttpException e){
            Assertions.assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @DisplayName("Positive Create Game")
    public void posCreateGame() throws Exception{
        AuthData authData = userService.registration("username","password","email");

        int gameID = gameService.createGame(authData.authToken(),"nameOfGame");

        Collection<GameData> games = gameService.listGames(authData.authToken());
        assertEquals(1,games.size());
        assertTrue(games.contains(gameAccess.getGame(gameID)));
    }

    @Test
    @DisplayName("Negative Create Game")
    public void negCreateGame() throws Exception{
        try{
            gameService.createGame("Random Auth Token","nameOfGame");
            Assertions.fail("Should have thrown HttpClientErrorException with status 401");
        } catch (HttpException e) {
            Assertions.assertEquals(401, e.getStatusCode());
        }
    }

    @Test
    @DisplayName("Positive Join Game")
    public void posJoinGame() throws Exception{
        AuthData authData = userService.registration("username","password","email");
        int gameID = gameService.createGame(authData.authToken(),"nameOfGame");
        gameService.joinGame(authData.authToken(),"WHITE",gameID);
    }

    @Test
    @DisplayName("Negative Join Game")
    public void negJoinGame() throws Exception{
        AuthData authData = userService.registration("username","password","email");
        int gameID = gameService.createGame(authData.authToken(),"nameOfGame");

        try {
            gameService.joinGame(authData.authToken(), "PURPLE", gameID);
        } catch (HttpException e){
            Assertions.assertEquals(400, e.getStatusCode());
        }
    }

    @Test
    @DisplayName("Positive List Game")
    public void posListGame() throws Exception{
        AuthData authData = userService.registration("username","password","email");

        int gameID1 = gameService.createGame(authData.authToken(),"nameOfGame1");
        int gameID2 = gameService.createGame(authData.authToken(),"nameOfGame2");

        Collection<GameData> games = gameService.listGames(authData.authToken());
        assertTrue(games.contains(gameAccess.getGame(gameID1)));
        assertTrue(games.contains(gameAccess.getGame(gameID2)));

    }

    @Test
    @DisplayName("Negative List Game")
    public void negListGame() throws Exception{
        AuthData authData = userService.registration("username","password","email");

        gameService.createGame(authData.authToken(),"nameOfGame");

        try {
            gameService.listGames("Random Token");
        } catch (HttpException e) {
            Assertions.assertEquals(401, e.getStatusCode());
        }

    }

    @Test
    @DisplayName("Positive Clear")
    public void posClear() throws Exception{
        AuthData authData = userService.registration("username","password","email");
        userService.login("username","password");
        gameService.createGame(authData.authToken(),"gameName");
        clearService.clear();
        try{
            userService.login("username","password");
        }catch (HttpException e){
            Assertions.assertEquals(401, e.getStatusCode());
        }
    }




}
