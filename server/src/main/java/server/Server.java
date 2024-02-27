package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import service.*;
import spark.*;

import java.util.Map;

public class Server {

    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    private final UserDAO userAccess;
    private final ClearService clearService;
    private final UserService userService;

    public Server(){
        this.gameAccess = new MemoryGameDAO();
        this.authAccess = new MemoryAuthDAO();
        this.userAccess = new MemoryUserDAO();
        this.clearService = new ClearService(gameAccess, userAccess, authAccess);
        this.userService = new UserService(userAccess,authAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clearApp);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
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
        clearService.clear();
        res.status(200);
        return "{}";
    }
}

/*
GameServices
- join game
- list games
- create game
UserServices
- logout
- login
- registration
ClearService
- clear
 */