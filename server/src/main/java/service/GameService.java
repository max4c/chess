package service;
import chess.ChessGame;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    public GameService(GameDAO gameAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.authAccess = authAccess;
    }

    public int createGame(String authToken, String gameName) throws HttpException{
        if(authToken == null || gameName == null){
            throw new HttpException("Error: bad request",400);
        }

        AuthData auth = null;

        try{
            auth = authAccess.getAuthData(authToken);
        } catch (Exception e){
            throw new HttpException(e.getMessage(), 500);
        }

        if(auth == null){
            throw new HttpException("Error: unauthorized",401);
        }

        try {
            return gameAccess.createGame(gameName);
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }
    }

    public void joinGame(String authToken, String playerColor, Integer gameID) throws HttpException{
        if(authToken == null || gameID == null){
            throw new HttpException("Error: bad request",400);
        }

        AuthData auth = null;
        GameData game = null;

        try{
            auth = authAccess.getAuthData(authToken);
            game = gameAccess.getGame(gameID);
        } catch (Exception e){
            throw new HttpException(e.getMessage(), 500);
        }

        if(auth == null){
            throw new HttpException("Error: unauthorized",401);
        }
        if(game == null){
            throw new HttpException("Error: bad request",400);
        }

        if (playerColor == null){
            //add spectators later in phase 6
        }
        else {
            if(!"WHITE".equals(playerColor) && !"BLACK".equals(playerColor)) {
                throw new HttpException("Error: bad request", 400);
            }

            if(Objects.equals(playerColor, "WHITE") && game.whiteUsername() != null || Objects.equals(playerColor, "BLACK") && game.blackUsername() != null){
                throw new HttpException("Error: already taken", 403);
            }

            try {
                if ("WHITE".equals(playerColor)) {
                    gameAccess.updateGame(gameID, "whiteUsername", auth.username());
                } else {
                    gameAccess.updateGame(gameID, "blackUsername", auth.username());
                }
            }catch (Exception e) {
                throw new HttpException(e.getMessage(), 500);
            }
        }
    }

    public Collection<GameData> listGames(String authToken) throws HttpException{
        AuthData auth = null;

        try{
            auth = authAccess.getAuthData(authToken);
        } catch (Exception e){
            throw new HttpException(e.getMessage(), 500);
        }

        if(auth == null){
            throw new HttpException("Error: unauthorized",401);
        }
        try{
            return gameAccess.listGames();
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), 500);
        }
    }
}
