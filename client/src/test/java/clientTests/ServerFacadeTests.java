package clientTests;

import model.AuthData;
import model.GameData;
import model.ListGamesResponse;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import ui.ResponseException;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
    @Test
    void pos_register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void neg_register() throws Exception {
        UserData user = new UserData(null, null, "p1@email.com"); // giving an empty username and password
        Exception exception = assertThrows(ResponseException.class, () -> {
            var authData = facade.register(user);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("400"), "Exception message should contain 400 status code"); // Assuming "400" is the failure status for invalid data
    }

    @Test
    void pos_createGame() throws Exception {
        UserData user = new UserData("player2", "password2", "p2");
        AuthData authData = facade.register(user);
        GameData game= new GameData(0,null,null,"test",null);
        int gameID = facade.createGame(game,authData.authToken());
        assertTrue(gameID<5);
    }

    @Test
    void neg_createGame() throws Exception {
        UserData user = new UserData("p_1", "p_1", "p_1");
        GameData game = new GameData(0,null,null,"test",null);

        Exception exception = assertThrows(ResponseException.class, () -> {
            facade.createGame(game, null);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("400"), "Exception message should contain 401 status code"); // Assuming 401 is the unauthorized status.
    }
    @Test
    void pos_login() throws Exception {
        UserData user = new UserData("p1", "p1", "p1");
        facade.register(user);
        var authData2 = facade.login(user);
        assertTrue(authData2.authToken().length() > 10);

    }


    @Test
    void neg_login() throws Exception {
        UserData user = new UserData("p3", "p3", "p3");
        facade.register(user);

        UserData wrongUser = new UserData("player1", "wrongpassword", "p1@email.com"); // User with wrong password

        Exception exception = assertThrows(ResponseException.class, () -> {
            facade.login(wrongUser);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("401"), "Exception message should contain 401 status code"); // Assuming "401" is the status code for unauthorized access.
    }


    @Test
    void pos_listGames() throws Exception {
        UserData user = new UserData("p4", "p4", "p4");
        AuthData authData = facade.register(user);
        GameData game= new GameData(0,null,null,"test",null);
        int gameID = facade.createGame(game,authData.authToken());
        ListGamesResponse games = facade.listGames(authData.authToken());
        assertFalse(games.games().isEmpty());
    }

    @Test
    void neg_listGames() throws Exception {
        UserData user = new UserData("p5", "p5", "p5");
        AuthData authData = facade.register(user);
        GameData game = new GameData(0, null, null, "test1", null);
        int gameID = facade.createGame(game, authData.authToken());

        Exception exception = assertThrows(ResponseException.class, () -> {
            facade.listGames(null);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("401"), "Exception message should contain 401 status code"); // Assuming 401 is the unauthorized status.
    }


    @Test
    void pos_joinGame() throws Exception {
        UserData user = new UserData("p6", "p6", "p6");
        AuthData authData = facade.register(user);
        GameData game= new GameData(0,null,null,"test",null);
        int gameID = facade.createGame(game,authData.authToken());
        facade.joinGame(authData.authToken(),gameID,"WHITE");
    }

    @Test
    void neg_joinGame() throws Exception {
        UserData user = new UserData("p7", "p7", "p7");
        AuthData authData = facade.register(user);
        GameData game= new GameData(0,null,null,"test",null);
        int gameID = facade.createGame(game,authData.authToken());

        Exception exception = assertThrows(ResponseException.class, () -> {
            facade.joinGame(authData.authToken(), -1, "WHITE");
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("400"), "Exception message should contain 404 status code"); // Assuming "404" is the status code for not found
    }

    @Test
    void pos_logout() throws Exception {
        UserData user = new UserData("p8", "p8", "p8");
        AuthData authData = facade.register(user);
        facade.logout(authData.authToken());
    }


    @Test
    void neg_logout() throws Exception {
        UserData user = new UserData("p9", "p9", "p9");
        AuthData authData = facade.register(user);

        Exception exception = assertThrows(ResponseException.class, () -> {
            facade.logout("InvalidToken");
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("401"), "Exception message should contain 401 status code"); // Assuming 401 is the unauthorized status.
    }



}
