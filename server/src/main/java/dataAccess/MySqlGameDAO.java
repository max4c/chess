package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exception.DataAccessException;
import model.GameData;

import java.sql.*;
import java.util.Collection;

public class MySqlGameDAO implements GameDAO{

    public MySqlGameDAO() {
        try{
            String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `ChessGame` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(`gameName`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException{
        var statement = "INSERT INTO game (gameName,ChessGame) VALUES (?, ?)";
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        var json = new Gson().toJson(game);
        try {
            return DatabaseManager.executeUpdate(statement, gameName,json);
        }catch (DataAccessException e){
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(int gameID, String fieldToUpdate, String newValue) {

    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

}
