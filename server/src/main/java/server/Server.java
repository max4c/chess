package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.Exception.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import dataAccess.MySqlGameDAO;
import dataAccess.MySqlUserDAO;
import dataAccess.MySqlAuthDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.websocket.WebSocketHandler;
import service.*;
import spark.*;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    private final UserDAO userAccess;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.webSocketHandler = new WebSocketHandler();
        this.gameAccess = new MySqlGameDAO();
        this.authAccess = new MySqlAuthDAO();
        this.userAccess = new MySqlUserDAO();
        this.clearService = new ClearService(gameAccess, userAccess, authAccess);
        this.userService = new UserService(userAccess,authAccess);
        this.gameService = new GameService(gameAccess,authAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session",this::logout);
        Spark.post("/game",this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        Spark.delete("/db", this::clearApp);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object listGames(Request req, Response res){
        String authToken =  req.headers("authorization");
        try{
            Collection<GameData> games = gameService.listGames(authToken);
            res.status(200);
            return new Gson().toJson(Map.of("games",games));
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object joinGame(Request req, Response res){
        String authToken =  req.headers("authorization");
        String json = req.body();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String playerColor = jsonObject.has("playerColor") ?
                jsonObject.get("playerColor").getAsString() : null;
        Integer gameID = jsonObject.has("gameID") ?
                jsonObject.get("gameID").getAsInt() : null;
        try{
            gameService.joinGame(authToken, playerColor, gameID);
            res.status(200);
            return "{}";
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object createGame(Request req, Response res){
        String authToken =  req.headers("authorization");
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        try{
            int gameID = gameService.createGame(authToken,game.gameName());
            res.status(200);
            return new Gson().toJson(Map.of("gameID",gameID));
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object logout(Request req, Response res) {
        String authToken =  req.headers("authorization");
        try {
            userService.logout(authToken);
            res.status(200);
            return "{}";
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object login(Request req, Response res) {
        UserData user =  new Gson().fromJson(req.body(), UserData.class);
        try {
            AuthData result = userService.login(user.username(), user.password());
            res.status(200);
            return new Gson().toJson(result);
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object register(Request req, Response res){
        UserData user =  new Gson().fromJson(req.body(), UserData.class);
        try {
            AuthData result = userService.registration(user.username(), user.password(), user.email());
            res.status(200);
            return new Gson().toJson(result);
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }

    private Object clearApp(Request req, Response res) throws DataAccessException{
        try {
            clearService.clear();
            res.status(200);
            return "{}";
        }
        catch(HttpException e){
            res.status(e.getStatusCode());
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
