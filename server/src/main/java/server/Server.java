package server;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryAuthDAO;
import service.ClearService;
import spark.*;

public class Server {

    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    private final UserDAO userAccess;
    private final ClearService clearService;

    public Server(){
        this.gameAccess = new MemoryGameDAO();
        this.authAccess = new MemoryAuthDAO();
        this.userAccess = new MemoryUserDAO();
        this.clearService = new ClearService(gameAccess, userAccess, authAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.delete("/db", this::clearApp);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearApp(Request req, Response res) throws DataAccessException{
        clearService.clear();
        res.status(200);
        return "{}";
    }
}

/*
-
 */